/**
 * 
 */
package com.veclink.poofpetgps;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.veclink.poofpetgps.util.AppContext;
import com.veclink.poofpetgps.util.StringUtil;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class ProofApplication extends Application{

	@Override
	public void onCreate() {	
		super.onCreate();
		AppContext.init(this);
		Global.devid = getDevid();
	}
	
	private  String getDevid(){
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		String szImei = TelephonyMgr.getDeviceId(); 
		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE); 
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter      
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();      
		String m_szBTMAC = m_BluetoothAdapter.getAddress();
		StringBuffer buffer = new StringBuffer();
		if(szImei!=null)buffer.append(szImei);
		if(m_szWLANMAC!=null)buffer.append(m_szWLANMAC);
		if(m_szBTMAC!=null)buffer.append(m_szBTMAC);
		return StringUtil.getMD5Str(buffer.toString());
	}
	
	
}
