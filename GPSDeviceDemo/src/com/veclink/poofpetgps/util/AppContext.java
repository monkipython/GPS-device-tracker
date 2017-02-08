/**
 * 
 */
package com.veclink.poofpetgps.util;

import android.content.Context;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class AppContext {

	private static Context mContext;
	public static void init(Context context){
		mContext = context;
	}
	public static Context getContext(){
		return mContext;
	}

}
