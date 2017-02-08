package com.veclink.poofpetgps;

import com.veclink.poofpetgps.tcp.TcpSocketManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chenshen on 2016/6/28.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Global.isWifiConnected = wifiNetInfo.isConnected();
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
        	TcpSocketManager.getInstance().sopTcpConnect();
        }else{
        	TcpSocketManager.getInstance().startTcpConnect();
        }

    }
}
