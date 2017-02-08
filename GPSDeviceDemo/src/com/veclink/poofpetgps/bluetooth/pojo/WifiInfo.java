/**
 * 
 */
package com.veclink.poofpetgps.bluetooth.pojo;

import java.io.Serializable;

/**
 * 
 * @author chenshen
 * @time 2016-6-27
 */
public class WifiInfo implements Serializable{

	public String ssid;
	public String pwd;
	public String macAddress;
	public String penName;
	public int type;
	public WifiInfo(){

	}
	public WifiInfo(String ssid, String pwd, String macAddress, String penName, int type) {
		this.ssid = ssid;
		this.pwd = pwd;
		this.macAddress = macAddress;
		this.penName = penName;
		this.type = type;
	}
	@Override
	public String toString() {
		return "WifiInfo [ssid=" + ssid + ", pwd=" + pwd + ", macAddress="
				+ macAddress + ", penName=" + penName + ", type=" + type + "]";
	}

	
}
