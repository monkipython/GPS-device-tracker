/**
 * 
 */
package com.veclink.poofpetgps.task;

import android.os.AsyncTask;

import com.amap.api.location.AMapLocation;
import com.veclink.poofpetgps.LocationUtil;
import com.veclink.poofpetgps.LocationUtil.ProofLocationListener;
import com.veclink.poofpetgps.tcp.TcpSocketManager;
import com.veclink.poofpetgps.tcp.pojo.LocationInfo;
import com.veclink.poofpetgps.util.AppContext;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;
import com.veclink.poofpetgps.util.GsonUtil;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class ResponseServerRequestTask extends AsyncTask<Object, Object, Object>{
	
	@Override
	protected Object doInBackground(Object... params) {
		LocationUtil locationUtil = LocationUtil.getInstance(AppContext.getContext());
		locationUtil.onDestroy();
		locationUtil.init();
		locationUtil.gpsonceLocationOption();
		locationUtil.setProofLocationListener(new ProofLocationListener() {
			
			@Override
			public void startLocation() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void sopLocation() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoacationChange(AMapLocation loc) {
				LocationInfo locationInfo = new LocationInfo(String.valueOf(loc.getLongitude()),String.valueOf(loc.getLatitude()));
            	String json = GsonUtil.toJson(locationInfo);
            	byte[] uploaddata = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.SINGAL_REQUEST_LOCATION, json);
            	TcpSocketManager.getInstance().sendMessage(uploaddata);
                LocationUtil.getInstance(AppContext.getContext()).onDestroy();//上传后关闭gps
			}
		});
		locationUtil.startLocation();
		return null;
	}

	

}
