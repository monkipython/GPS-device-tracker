package com.veclink.poofpetgps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.veclink.poofpetgps.util.Gpsutil;
import com.veclink.poofpetgps.util.Utils;

public class LocationUtil implements AMapLocationListener {
    private Context mContext;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private static LocationUtil locationUtil;
    private ProofLocationListener listener;

//    Handler mHandler = new Handler() {
//        public void dispatchMessage(Message msg) {
//            switch (msg.what) {
//                //开始定位
//                case Utils.MSG_LOCATION_START:
//                    if(listener!=null)listener.startLocation();
//                    break;
//                // 定位完成
//                case Utils.MSG_LOCATION_FINISH:
//                    AMapLocation loc = (AMapLocation) msg.obj;
//                    String result = Utils.getLocationStr(loc);
//                    Log.v("Location",result);
//                    if(listener!=null)listener.onLoacationChange(loc);
//                    break;
//                //停止定位
//                case Utils.MSG_LOCATION_STOP:
//                    if(listener!=null)listener.sopLocation();
//                    break;
//                default:
//                    break;
//            }
//        };
//    };

    private LocationUtil(){}

    private LocationUtil(Context mContext){
        this.mContext = mContext;
    }

    public static LocationUtil getInstance(Context mCotnext){
        if(locationUtil==null){
            locationUtil = new LocationUtil(mCotnext);
        }
        locationUtil.init();
        return locationUtil;
    }

    public void init() {
        if(locationClient==null){
            locationClient = new AMapLocationClient(mContext);
            locationOption = new AMapLocationClientOption();
        }
    }

    public void onDestroy() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
        if(Gpsutil.getGpsStatu(mContext))Gpsutil.toogleGPS(mContext);
    }

//    /**
//     * wifi定位
//     * @param locationInterval 持续定位时间间隔
//     */
//    public void wifiLocationOption(int locationInterval){
//        locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
//        locationOption.setInterval(locationInterval*1000);
//        locationClient.setLocationListener(this);
//        // 设置是否需要显示地址信息
//        locationOption.setNeedAddress(true);
//        /**
//         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
//         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
//         */
////        locationOption.setGpsFirst(cbGpsFirst.isChecked());
//        // 设置是否开启缓存
//        locationOption.setLocationCacheEnable(true);
//        locationOption.setOnceLocation(false);
//    }

    /**
     * gps定位
     * @param locationInterval 持续定位时间间隔
     */
    public void gpsLocationOption(int locationInterval){
        locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(locationInterval*1000*60);
        locationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
//        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        locationOption.setOnceLocation(false);
    }
    
    /**
     * gps定位
     * @param locationInterval 持续定位时间间隔
     */
    public void gpsonceLocationOption(){
        locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);       
        locationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
//        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        locationOption.setOnceLocation(true);
    }

    public void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
//            Message msg = mHandler.obtainMessage();
//            msg.obj = loc;
//            msg.what = Utils.MSG_LOCATION_FINISH;
//            mHandler.sendMessage(msg);
        	Log.v("LocationUtil", "获取到定位信息:"+Utils.getLocationStr(loc));
            if(listener!=null&&loc.getErrorCode() == 0)listener.onLoacationChange(loc);
        }
    }

    public interface ProofLocationListener{
        public void startLocation();
        public void onLoacationChange(AMapLocation loc);
        public void sopLocation();
    }

    public void setProofLocationListener(ProofLocationListener listener){
        this.listener = listener;
    }
}
