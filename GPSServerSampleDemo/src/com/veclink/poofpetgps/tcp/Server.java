package com.veclink.poofpetgps.tcp;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;

/**
 * C/S架构的服务端对象。
 */
public class Server {

	ServerSocket ss;
	/**
	 * 要处理客户端发来的对象，并返回一个对象，可实现该接口。
	 */
	public interface ObjectAction{
		byte[] doAction(byte[] rev);
	}
	
	public static final class DefaultObjectAction implements ObjectAction{
		public byte[] doAction(byte[] recedata) {			
			byte[] backdata = new byte[]{0x10,0x0f,0x0f,0x06};			
			int messageType = 0;
			if(recedata.length>4){
				messageType = recedata[4];
				byte[] dataArray = new byte[recedata.length-7];
				for(int i=0;i<dataArray.length;i++){
					dataArray[i] = recedata[i+7];
				}
				System.out.println("服务器接处理数据：\t"+new String(dataArray));
			}
			switch (messageType) {
			case 1:
				String content = "{\"status\":0,\"info\":\"ok\",\"code\":\"1001\"}";
				byte[] contentArray = content.getBytes();
				int contentlenght = contentArray.length;
				backdata = new byte[7+contentlenght];
				backdata[0] = 0x10;
				backdata[1] = 0x0f;
				backdata[2] = 0x0f;
				backdata[3] = 0x06;
				backdata[4] = (byte)messageType;				
				backdata[5] = (byte) (((contentlenght) >> 8) & 0xff);
				backdata[6] = (byte) ((contentlenght) & 0xff);				
				for(int i=7;i<backdata.length;i++){
					backdata[i] = contentArray[i-7];
				}				
				
				break;

			default:
				break;
			}
			
			System.out.println("处理并返回："+Helper.bytesToHexString(backdata));
			return backdata;
		}
	}
	
	public static void main(String[] args) {
//		System.out.print("start");
		int port = 10012;
		Server server = new Server(port);
		server.start();
	}
	
	private int port;
	private volatile boolean running=false;
	private long receiveTimeDelay=2*60*1000;
	private ConcurrentHashMap<Integer, ObjectAction> actionMapping = new ConcurrentHashMap<Integer,ObjectAction>();
	private Thread connWatchDog;
	
	public Server(int port) {
		this.port = port;
	}

	public void start(){
		if(running)return;
		running=true;
		connWatchDog = new Thread(new ConnWatchDog());
		connWatchDog.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop(){
		if(running)running=false;
		if(connWatchDog!=null)connWatchDog.stop();
	}
	
	public void addActionMap(int type,ObjectAction action){
		actionMapping.put(type, action);
	}
	
	class ConnWatchDog implements Runnable{
		public void run(){
			try {
				ss = new ServerSocket(port,5);
				while(running){
					Socket s = ss.accept();
					new Thread(new SocketAction(s)).start();
					
				}
			} catch (IOException e) {
				e.printStackTrace();
				Server.this.stop();
			}
			
		}
	}
	
	
	
	class SocketAction implements Runnable{
		Socket s;
		boolean run=true;
		long lastReceiveTime = System.currentTimeMillis();
		long lastSendTime = System.currentTimeMillis();
		public SocketAction(Socket s) {
			this.s = s;
		}
		public void run() {
			while(running && run){
				if(System.currentTimeMillis()-lastReceiveTime>receiveTimeDelay){
					overThis();
				}else{
					if(System.currentTimeMillis()-lastSendTime>30*1000){
						try {
							OutputStream oos = s.getOutputStream();
							byte[] request = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.SERVER_SEND_RQUEST_LOCATION,
									GsonUtil.toJson(new ServerRequest()));
							oos.write(request);
							oos.flush();
							lastSendTime = System.currentTimeMillis();
						}
						catch (Exception e) {
							// TODO: handle exception
						}
					}
					try {
						InputStream in = s.getInputStream();
						if(in.available()>0){
//							ObjectInputStream ois = new ObjectInputStream(in);							
//							Object obj = ois.readObject();
							byte[] recedata = new byte[in.available()];
							in.read(recedata);
							lastReceiveTime = System.currentTimeMillis();
							System.out.println("服务器接收数据：\t"+Helper.bytesToHexString(recedata));
							int messageType = 0;
							if(recedata.length>4){
								messageType = recedata[4];
							}
							ObjectAction oa = actionMapping.get(messageType);
							oa = oa==null?new DefaultObjectAction():oa;
							byte[] out = oa.doAction(recedata);
							if(out!=null){
								OutputStream oos = s.getOutputStream();
								oos.write(out);
								oos.flush();
							}
							
							
						}else{
							Thread.sleep(10);
						}
					} catch (Exception e) {
						e.printStackTrace();
						overThis();
					} 
				}
			}
		}
		
		private void overThis() {
			if(run)run=false;
			if(s!=null){
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("关闭："+s.getRemoteSocketAddress());
		}
		
	}
	
}
