package com.veclink.poofpetgps;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ServiceCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.veclink.poofpetgps.util.Utils;

public class LocationService extends Service implements AMapLocationListener{

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                //开始定位
                case Utils.MSG_LOCATION_START:

                    break;
                // 定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    String result = Utils.getLocationStr(loc);
                    Log.v("Location",result);
                    break;
                //停止定位
                case Utils.MSG_LOCATION_STOP:

                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        wifiLocationOption(10);
        startLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * wifi定位
     * @param locationInterval 持续定位时间间隔
     */
    private void wifiLocationOption(int locationInterval){
        locationOption.setLocationMode(AMapLocationMode.Battery_Saving);
        locationOption.setInterval(locationInterval*1000);
        locationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
//        locationOption.setGpsFirst(cbGpsFirst.isChecked());
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        locationOption.setOnceLocation(false);
    }

    /**
     * gps定位
     * @param locationInterval 持续定位时间间隔
     */
    private void gpsLocationOption(int locationInterval){
        locationOption.setLocationMode(AMapLocationMode.Device_Sensors);
        locationOption.setInterval(locationInterval*1000);
        locationClient.setLocationListener(this);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
//        locationOption.setGpsFirst(cbGpsFirst.isChecked());
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        locationOption.setOnceLocation(false);
    }

    private void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }
}
