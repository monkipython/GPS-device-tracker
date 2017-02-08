/**
 * 
 */
package com.veclink.poofpetgps.tcp;

import java.io.Serializable;

/**
 * 服务器下发定位请求
 * @author chenshen
 * @time 2016-7-1
 */
public class ServerRequest implements Serializable{

	public String cmd;
	public String dataTime;
	public ServerRequest() {
		super();
		this.cmd = "location";
		this.dataTime = CreateUploadDataUtil.createTimeStamp();
	}
	
	
}
