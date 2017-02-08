package com.veclink.poofpetgps.task;

import android.content.Context;
import android.os.Handler;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.veclink.poofpetgps.Global;
import com.veclink.poofpetgps.LocationUtil;
import com.veclink.poofpetgps.bluetooth.pojo.WifiInfo;
import com.veclink.poofpetgps.tcp.TcpSocketManager;
import com.veclink.poofpetgps.tcp.pojo.LocationInfo;
import com.veclink.poofpetgps.tcp.pojo.WifiConnectMessage;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;
import com.veclink.poofpetgps.util.Gpsutil;
import com.veclink.poofpetgps.util.GsonUtil;
import com.veclink.poofpetgps.util.StorageUtil;
import com.veclink.poofpetgps.wifi.MobileNetUtil;
import com.veclink.poofpetgps.wifi.WifiAdmin;

import java.util.List;

/**
 * Created by chenshen on 2016/6/28.
 * wifi检测任务
 */
public class WifiConnectCheckTask implements  Runnable {

    private WifiAdmin wifiAdmin;
    private Context mContext;
    private static int count;

    public WifiConnectCheckTask(Context mContext) {
        wifiAdmin = WifiAdmin.getInstance(mContext);
        this.mContext = mContext;
 
    }

    @Override
    public void run() {
        Log.v("WifiConnectCheckTask","start");
        if(Global.isWifiConnected&&wifiAdmin.isWifiEnabled()){//如果是打开并且连接状态说明到时间了需要关闭wifi
            wifiAdmin.closeWifi();
            /**
             * 这里上传一次wifi围栏信息。
              */
            Log.v("WifiConnectCheckTask","关闭wifi ");
            return;
        }
        if(wifiAdmin.isWifiEnabled()==false)wifiAdmin.openWifi();
        Log.v("WifiConnectCheckTask","打开wifi ");
        wifiAdmin.startScan();
        List<WifiInfo> wifiInfoList = StorageUtil.readWifiInfoObjects(mContext);
        for(final WifiInfo wifiInfo:wifiInfoList){
            Log.v("WifiConnectCheckTask","addNetwork "+wifiInfo.toString());           
            wifiAdmin.addNetwork(wifiAdmin.createWifiInfo(wifiInfo.ssid,wifiInfo.pwd,wifiInfo.type));   
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(Global.isWifiConnected){
                count = 0;
                Global.config.wifiOpenCloseTimeInteval = 30;
                LocationUtil.getInstance(mContext).onDestroy();
                /**
                 * 这里上传一次wifi围栏信息。
                  */
                WifiConnectMessage wifiConnectMessage = new WifiConnectMessage(wifiInfo.ssid,wifiInfo.macAddress,wifiInfo.penName);
            	String json = GsonUtil.toJson(wifiConnectMessage);
            	byte[] uploaddata = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.WIFI_FAIL_LOCATION, json);
            	TcpSocketManager.getInstance().sendMessage(uploaddata);
                break;
            }
        }
        count++;
        if(Global.isWifiConnected==false){
        	Global.config.wifiOpenCloseTimeInteval = 15;        	
        }
        if(count>=Global.MAX_TRY_CHECK_COUNT){
        	MobileNetUtil.setMobileNetEnable();
        	TcpSocketManager.getInstance().startTcpConnect();
            LocationUtil.getInstance(mContext).setProofLocationListener(new LocationUtil.ProofLocationListener() {
                @Override
                public void startLocation() {

                }

                @Override
                public void onLoacationChange(AMapLocation loc) {
                    /**
                     * 这里上传gps信息
                     */
                	LocationInfo locationInfo = new LocationInfo(String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()));
                	String json = GsonUtil.toJson(locationInfo);
                	byte[] uploaddata = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.WIFI_FAIL_LOCATION, json);
                	TcpSocketManager.getInstance().sendMessage(uploaddata);
                    LocationUtil.getInstance(mContext).onDestroy();//上传后关闭gps

                }

                @Override
                public void sopLocation() {

                }
            });
            LocationUtil.getInstance(mContext).gpsLocationOption(Global.config.gpsLoacationTimeInteval);
            LocationUtil.getInstance(mContext).startLocation();
        }

    }
    
   
}
