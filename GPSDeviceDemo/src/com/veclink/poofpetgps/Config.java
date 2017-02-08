package com.veclink.poofpetgps;

import java.io.Serializable;

/**
 * Created by chenshen on 2016/6/28.
 * 全局设备参数设置
 */
public class Config implements Serializable{

    public static final int DEFAULT_MODE = 0;
    public static final int WIFI_MODE = 1;
    public static final int GPS_MODE = 2;

    /**
     * 设备当前的工作模式
     */

    public int workMode;


    /**
     * 超低电量设置
     */
    public int lowPower = 20;

    /**
     * wifi打开关闭时间间隔,单位分钟
     */
    public int wifiOpenCloseTimeInteval = 30;

    /**
     * 启动一次gps，gps工作时间，单位分钟
     */
    public int gpsOpenTime = 30;

    /**
     * gps模式下设备向服务器上传Gps地址信息的时间间隔
     */
    public int uploadLocationTimeInteval = 2;

    /**
     * gps定位时间
     */
    public int gpsLoacationTimeInteval = 2;
}
