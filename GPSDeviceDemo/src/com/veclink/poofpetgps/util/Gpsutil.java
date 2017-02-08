package com.veclink.poofpetgps.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by chenshen on 2016/6/28.
 */
public class Gpsutil {
	
	
	public static void turnGPSOn(Context context)
	{
	     
	    String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	    if(!provider.contains("gps")){ //if gps is disabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        context.sendBroadcast(poke);
	    }
	}

    public static final void toogleGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取GPS状态
     * @param context
     * @return
     */
    public static boolean getGpsStatu(Context context)
    {
        LocationManager alm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return  alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }
}
