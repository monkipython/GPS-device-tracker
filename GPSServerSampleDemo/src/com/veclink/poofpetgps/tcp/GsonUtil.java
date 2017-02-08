/**
 * 
 */
package com.veclink.poofpetgps.tcp;

import com.google.gson.Gson;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class GsonUtil {

	private static Gson gson;
	
	public static String toJson(Object object){
		if(gson==null)gson = new Gson();
		return gson.toJson(object);
	}
	
	

}
