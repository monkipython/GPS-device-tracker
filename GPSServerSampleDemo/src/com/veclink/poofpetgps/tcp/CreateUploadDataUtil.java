/**
 * 
 */
package com.veclink.poofpetgps.tcp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class CreateUploadDataUtil {
	
	/**
	 * 低电量定位信息上传
	 */
	public static final int LOWPERY_LOCATION = 0x01;
	
	
	/**
	 * wifi扫描失败，开启gps定位，上传gps定位信息
	 */
	public static final int WIFI_FAIL_LOCATION = 0x02;	
	
	/**
	 * 设备响应服务器下发的单次请求指令，回复定位信息上传
	 */
	public static final int SINGAL_REQUEST_LOCATION = 0x03;
	
	
	/**
	 * wifi连接成功，wifi信息作为定位信息上传
	 */
	public static final int WIFI_SUCCESS_LOCATION = 0x04;
	
	/**
	 * 服务器下发单次请求定位指令
	 */
	public static final int SERVER_SEND_RQUEST_LOCATION = 0x05;
	
	
	
	public static byte[] createUploadData(int type,String uploadJsondata){		
		byte[] contentArray = uploadJsondata.getBytes();
		int contentlenght = contentArray.length;
		byte[] backdata = new byte[7+contentlenght];
		backdata[0] = 0x10;
		backdata[1] = 0x0f;
		backdata[2] = 0x0f;
		backdata[3] = 0x06;
		backdata[4] = (byte)type;				
		backdata[5] = (byte) (((contentlenght) >> 8) & 0xff);
		backdata[6] = (byte) ((contentlenght) & 0xff);				
		for(int i=7;i<backdata.length;i++){
			backdata[i] = contentArray[i-7];
		}		
	
		return backdata;
	}
	
	
	public static String createTimeStamp(){
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		String dateString = dateformat1.format(new Date());
		return dateString;
	}
	
	

	

}
