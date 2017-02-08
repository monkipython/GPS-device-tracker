/**
 * 
 */
package com.veclink.poofpetgps.tcp.pojo;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class ServerResponse {

	/**
	 * {“status”:0,”info”:”ok”,”code”:”1002”}
	 */
	public int status;
	public String info;
	public int code;
	@Override
	public String toString() {
		return "ServerResponse [status=" + status + ", info=" + info + ", code=" + code + "]";
	}
	
	

}
