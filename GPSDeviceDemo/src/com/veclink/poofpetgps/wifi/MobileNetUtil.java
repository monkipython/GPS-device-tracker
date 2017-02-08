/**
 * 
 */
package com.veclink.poofpetgps.wifi;

import java.lang.reflect.Method;

import com.veclink.poofpetgps.Global;
import com.veclink.poofpetgps.tcp.TcpSocketManager;
import com.veclink.poofpetgps.util.AppContext;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author chenshen
 * @time 2016-7-1
 */
public class MobileNetUtil {

	/**
	 * 开启系统设置里的移动网络
	 */
	public final static void setMobileNetEnable() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled", arg);
			if (!isMobileDataEnable) {
				invokeBooleanArgMethod("setMobileDataEnabled", true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeMobileNet(){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		Object[] arg = null;
		try {
			boolean isMobileDataEnable = invokeMethod("getMobileDataEnabled", arg);
			if (isMobileDataEnable) {
				invokeBooleanArgMethod("setMobileDataEnabled", false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isMobilNetEnble(){
		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		Object[] arg = null;
		
		boolean isMobileDataEnable = false;
		try {
			isMobileDataEnable = invokeMethod("getMobileDataEnabled", arg);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isMobileDataEnable;
	}

	public static boolean invokeMethod(String methodName,
			Object[] arg) throws Exception {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = null;
		if (arg != null) {
			argsClass = new Class[1];
			argsClass[0] = arg.getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

		return isOpen;
	}

	public static Object invokeBooleanArgMethod(String methodName,
			boolean value) throws Exception {

		ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		Class ownerClass = mConnectivityManager.getClass();

		Class[] argsClass = new Class[1];
		argsClass[0] = boolean.class;

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(mConnectivityManager, value);
	}
	
	public static boolean isNetConnected(Context context){
		boolean result = false;
		ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
        	result = false;
        }else{
        	result = true;
        }
        return result;
	}

}
