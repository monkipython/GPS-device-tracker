/**
 * 
 */
package com.veclink.poofpetgps.tcp.pojo;

import java.io.Serializable;

import com.veclink.poofpetgps.Global;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;

/**
 * 
 * @author chenshen
 * @time 2016-7-1
 */
public class LocationInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * {“devid”:”xxx”,“loctype”:”gps”,“lon”:”111.518”,“lat”:”36.096”,“datetime”:”2016-04-06”}
	 */
	
	public String devid;
	public String loctype;
	public String lon;
	public String lat;
	public String datetime;
	
	
	
	
	public LocationInfo(String loctype, String lon, String lat) {
		super();
		this.devid = Global.devid;
		this.loctype = loctype;
		this.lon = lon;
		this.lat = lat;
		this.datetime = CreateUploadDataUtil.createTimeStamp();
	}
	
	public LocationInfo( String lon, String lat) {
		super();
		this.devid = Global.devid;
		this.loctype = "gps";
		this.lon = lon;
		this.lat = lat;
		this.datetime = CreateUploadDataUtil.createTimeStamp();
	}



	@Override
	public String toString() {
		return "LocationInfo [devid=" + devid + ", loctype=" + loctype + ", lon=" + lon + ", lat=" + lat + ", datetime=" + datetime + "]";
	}	
	
	

}
