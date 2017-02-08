/**
 * 
 */
package com.veclink.poofpetgps.tcp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.veclink.poofpetgps.util.Helper;

import android.util.Log;

/**
 * 
 * @author chenshen
 * @time 2016-6-29
 */
public class TcpSocketManager {

	private TcpClient client;
	
	private static TcpSocketManager manager;
	
	private String ip = "192.168.0.50";
	
	private int port = 65432;
	
	private ExecutorService sendExecutorService = Executors.newSingleThreadExecutor();
	
	private TcpSocketManager(){
		
	}
	
	public static TcpSocketManager getInstance(){
		if(manager == null){
			manager = new TcpSocketManager();
		}
		return manager;
	}
	
	public void setIpAddressAndPort(String ip,int port){
		this.ip=ip;
		this.port=port;
	} 
	
	/**
	 * 开始tcp连接
	 * @param ip
	 * @param port
	 */
	
	public void startTcpConnect(){
		if(client!=null&&client.isRunning())return;
		new Thread(){
			public void run(){
				
				client = new TcpClient(ip,port);

				try {
					client.start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}.start();		
		
	}
	
	/**
	 * 停止tcp连接
	 */
	public void sopTcpConnect(){
		if(client!=null){
			client.stop();
			client = null;
		}
	}
	
	public void sendMessage(final byte[] message){
		startTcpConnect();
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					Log.v("TcpClient", "向服务器发送数据："+Helper.bytesToHexString(message));
					client.sendMessage(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			
				}
			}
		};
		sendExecutorService.execute(runnable);
		
	}
	

}
