/**
 * 
 */
package com.veclink.poofpetgps.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import android.util.Log;

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
	
	public static final int SERVER_LOGIN_RQUEST = 0x06;
	
	public static byte[] channelIdArray = new byte[4];
	
	public static byte[] createUploadData(int type,String uploadJsondata){	
		Log.v("uploadJsondata 加密前", uploadJsondata);
		
		String encryptKey = creatEncryptKey();
		Log.v("uploadJsondata encryptKey is", encryptKey);
		uploadJsondata = XXTEA.Encrypt(uploadJsondata, encryptKey);
		Log.v("uploadJsondata 加密后", uploadJsondata);
		byte[] contentArray = uploadJsondata.getBytes();
		int contentlenght = contentArray.length;
		byte[] backdata = new byte[7+contentlenght];
		backdata[0] = 0x10;
		backdata[1] = 0x0f;
		backdata[2] = 0x0f;
		backdata[3] = 0x06;
		backdata[4] = channelIdArray[0];
		backdata[5] = channelIdArray[1];
		backdata[6] = channelIdArray[2];
		backdata[7] = channelIdArray[3];
		backdata[4] = (byte)type;				
		backdata[5] = (byte) (((contentlenght) >> 8) & 0xff);
		backdata[6] = (byte) ((contentlenght) & 0xff);				
		for(int i=7;i<backdata.length;i++){
			backdata[i] = contentArray[i-7];
		}		
		Log.v("uploadJsondata is ", uploadJsondata);
		String dencryptKey = creatDEncryptKey(channelIdArray);
		Log.v("uploadJsondata dencryptKey is", dencryptKey);
		uploadJsondata = XXTEA.Decrypt(uploadJsondata, dencryptKey);
		Log.v("uploadJsondata 解密密后", uploadJsondata);
		return backdata;
	}
	
	
	public static String createTimeStamp(){
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = dateformat1.format(Calendar.getInstance().getTimeInMillis());
		return dateString;
	}
	
	private static int createRandNumber(){		
		int max=10000;
        Random random = new Random();
        int number = random.nextInt(max);
        return number;    
	}
	
	private static String creatEncryptKey(){
		int randNumber = createRandNumber();
		String key = "";
		String md5String = StringUtil.getMD5Str(randNumber+"proof");
		int start = randNumber%10;
		int end = start+6;
		key = md5String.substring(start, end);
		channelIdArray[0] = (byte)(randNumber/100);
		channelIdArray[1] = (byte)(randNumber%100);
		channelIdArray[2] = (byte)(start);
		channelIdArray[3] = (byte)(end);		
		return key; 		
	}
	
	private static String creatDEncryptKey(byte[] channelIdArray){
		int randNumber = channelIdArray[0]*100+channelIdArray[1];
		String key = "";
		String md5String = StringUtil.getMD5Str(randNumber+"proof");
		int start = channelIdArray[2];
		int end = channelIdArray[3];
		key = md5String.substring(start, end);
		return key; 		
	}
	

	

}
