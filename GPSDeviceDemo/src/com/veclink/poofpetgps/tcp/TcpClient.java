package com.veclink.poofpetgps.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

import com.google.gson.Gson;
import com.veclink.poofpetgps.tcp.pojo.ServerRequest;
import com.veclink.poofpetgps.tcp.pojo.ServerResponse;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;
import com.veclink.poofpetgps.util.Helper;

import de.greenrobot.event.EventBus;

/**
 *	C/S架构的客户端对象，持有该对象，可以随时向服务端发送消息。
 */
public class TcpClient {

	/**
	 * 处理服务端发回的对象，可实现该接口。
	 */
	public static interface ObjectAction{
		void doAction(byte[] obj);
	}
	public static final class DefaultObjectAction implements ObjectAction{
		private Gson gson = new Gson();
		public void doAction(byte[] recedata) {
//			System.out.println("处理：\t"+obj.toString());
			if(recedata.length>4){
				int messageType = recedata[4];
				byte[] dataArray = new byte[recedata.length-7];
				for(int i=0;i<dataArray.length;i++){
					dataArray[i] = recedata[i+7];
				}
				String json = new String(dataArray);
				ServerResponse response = null;
				switch (messageType) {
					case CreateUploadDataUtil.LOWPERY_LOCATION:
						response = gson.fromJson(json, ServerResponse.class);
						EventBus.getDefault().post(response);
						break;
					case CreateUploadDataUtil.WIFI_FAIL_LOCATION:
						response = gson.fromJson(json, ServerResponse.class);
						EventBus.getDefault().post(response);
						break;
						
					case CreateUploadDataUtil.SINGAL_REQUEST_LOCATION:
						response = gson.fromJson(json, ServerResponse.class);
						EventBus.getDefault().post(response);
						break;
						
					case CreateUploadDataUtil.WIFI_SUCCESS_LOCATION:
						response = gson.fromJson(json, ServerResponse.class);
						EventBus.getDefault().post(response);						
						break;
						
					case CreateUploadDataUtil.SERVER_SEND_RQUEST_LOCATION:
						ServerRequest request = gson.fromJson(json, ServerRequest.class);
						EventBus.getDefault().post(request);
						break;
	
					default:
						break;
				}
			}
		}
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		String serverIp = "127.0.0.1";
		int port = 65432;
		TcpClient client = new TcpClient(serverIp,port);
		client.start();
	}
	
	private String serverIp;
	private int port;
	private Socket socket;
	private boolean running=false;
	private long lastSendTime;
	private ConcurrentHashMap<Integer, ObjectAction> actionMapping = new ConcurrentHashMap<Integer,ObjectAction>();
	
	public TcpClient(String serverIp, int port) {
		this.serverIp=serverIp;this.port=port;
	}
	
	public void start() throws UnknownHostException, IOException {
		if(running)return;
		socket = new Socket(serverIp,port);
		System.out.println("本地端口："+socket.getLocalPort());
		lastSendTime=System.currentTimeMillis();
		running=true;
		new Thread(new KeepAliveWatchDog()).start();
		new Thread(new ReceiveWatchDog()).start();
	}
	
	public void stop(){
		if(running)running=false;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	/**
	 * 添加接收对象的处理对象。
	 * @param cls 待处理的对象，其所属的类。
	 * @param action 处理过程对象。
	 */
	public void addActionMap(int messageType,ObjectAction action){
		actionMapping.put(messageType, action);
	}

	public void sendMessage(byte[] buffer) throws IOException {	
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(buffer);
		outputStream.flush();
	}
	
	class KeepAliveWatchDog implements Runnable{
		long checkDelay = 10;
		long keepAliveDelay = 1000*60;
		public void run() {
			while(running){
				if(System.currentTimeMillis()-lastSendTime>keepAliveDelay){
					try {
						TcpClient.this.sendMessage(new byte[]{0x10,0x0f,0x0f,0x06});
					} catch (IOException e) {
						e.printStackTrace();
						TcpClient.this.stop();
					}
					lastSendTime = System.currentTimeMillis();
				}else{
					try {
						Thread.sleep(checkDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
						TcpClient.this.stop();
					}
				}
			}
		}
	}
	
	class ReceiveWatchDog implements Runnable{
		public void run() {
			while(running){
				try {
					InputStream in = socket.getInputStream();
					if(in.available()>0){
						byte[] recedata = new byte[in.available()];
						in.read(recedata);
						System.out.println("接收：\t"+Helper.bytesToHexString(recedata));
						int messageType = 0;
						if(recedata.length>4)messageType = recedata[4];
						ObjectAction oa = actionMapping.get(messageType);
						oa = oa==null?new DefaultObjectAction():oa;
						oa.doAction(recedata);
					}else{
						Thread.sleep(10);
					}
				} catch (Exception e) {
					e.printStackTrace();
					TcpClient.this.stop();
				} 
			}
		}
	}
	
}
