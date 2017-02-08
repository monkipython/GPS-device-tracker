/**
 * 
 */
package com.veclink.poofpetgps.tcp.pojo;

import java.io.Serializable;

import com.veclink.poofpetgps.Config;
import com.veclink.poofpetgps.Global;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;
import com.veclink.poofpetgps.util.Utils;

/**
 * 
 * @author chenshen
 * @time 2016-6-29
 */
public class WifiConnectMessage implements Serializable{

	/**
	 * {
		“devid”:””,                      //设备ID
		“wifiname”:””,                   //wifi名称 
		“wifimac”:””,                    // wifi的MAC地址
		“datetime”:”2016-04-01 11:19:27”  //日期时间
		}
	 */
	public String devid;
	public String wifiname;
	public String wifimac;
	public String wifiinfo;
	public String datetime;
	
	public WifiConnectMessage(String wifiname,String wifimac,String wifiinfo){
		this.wifimac = wifimac;
		this.wifiname = wifiname;
		this.wifiinfo = wifiinfo;
		this.devid = Global.devid;
		this.datetime = CreateUploadDataUtil.createTimeStamp();
	}

	@Override
	public String toString() {
		return "WifiConnectMessage [devid=" + devid + ", wifiname=" + wifiname + ", wifimac=" + wifimac+ ", wifiinfo=" + wifiinfo + ", datetime=" + datetime + "]";
	}
	
	
	
	

}
