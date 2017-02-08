/**
 * 
 */
package com.veclink.poofpetgps;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.veclink.poofpetgps.LocationUtil.ProofLocationListener;
import com.veclink.poofpetgps.task.WifiConnectCheckTask;
import com.veclink.poofpetgps.tcp.TcpSocketManager;
import com.veclink.poofpetgps.tcp.pojo.LocationInfo;
import com.veclink.poofpetgps.util.AppContext;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;
import com.veclink.poofpetgps.util.Gpsutil;
import com.veclink.poofpetgps.util.GsonUtil;
import com.veclink.poofpetgps.wifi.WifiAdmin;

/**
 * 设备工作模式管理类
 * @author chenshen
 * @time 2016-6-29
 */
public class ProofWorkModeManager {

	private static ProofWorkModeManager manager;
	
	private ScheduledExecutorService service;
	
	private Context mContext;
	
	
	private ProofWorkModeManager(Context mContext){
		this.mContext = mContext;
	}
	
	public static ProofWorkModeManager getIntance(Context mContext){
		if(manager==null){
			manager = new ProofWorkModeManager(mContext);
		}
		return manager;
	}
	
	public void changeWifiWordMode(){
		if(service!=null)service.shutdownNow();
		LocationUtil.getInstance(AppContext.getContext()).onDestroy();
		service = Executors  
                .newSingleThreadScheduledExecutor();  
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
        service.scheduleAtFixedRate(new WifiConnectCheckTask(mContext),1, Global.config.wifiOpenCloseTimeInteval*60, TimeUnit.SECONDS);
	}
	
	public void changeGpsWordMode(){
		if(service!=null)service.shutdownNow();
		LocationUtil util = LocationUtil.getInstance(AppContext.getContext());
		Gpsutil.turnGPSOn(mContext);
		util.gpsLocationOption(Global.config.gpsLoacationTimeInteval);
		util.setProofLocationListener(new ProofLocationListener() {
			
			@Override
			public void startLocation() {
				
			}
			
			@Override
			public void sopLocation() {
				
				
			}
			
			@Override
			public void onLoacationChange(AMapLocation loc) {
				LocationInfo locationInfo = new LocationInfo(String.valueOf(loc.getLongitude()),String.valueOf(loc.getAltitude()));
            	String json = GsonUtil.toJson(locationInfo);
            	byte[] uploaddata = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.WIFI_FAIL_LOCATION, json);
            	TcpSocketManager.getInstance().sendMessage(uploaddata);
                LocationUtil.getInstance(mContext).onDestroy();//上传后关闭gps
				
			}
		});
		util.startLocation();
		
	}
	
	public void changeDefalutWordMode(){
		if(service!=null)service.shutdownNow();
		LocationUtil.getInstance(mContext).onDestroy();
		WifiAdmin.getInstance(mContext).closeWifi();
	}
	

}
