package com.veclink.poofpetgps.bluetooth.pojo;

import java.io.Serializable;

import com.veclink.poofpetgps.Global;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;

public class LoginJson implements Serializable {
	private String devid;
	private String datetime;
	
	public LoginJson(){
		this.devid = Global.devid;
		this.datetime = CreateUploadDataUtil.createTimeStamp();
	}
	
	@Override
	public String toString() {
		return "LoginJson [devid=" + devid + ", datetime=" + datetime + "]";
	}
	
	

}
