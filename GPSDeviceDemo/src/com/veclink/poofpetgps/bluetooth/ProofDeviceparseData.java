package com.veclink.poofpetgps.bluetooth;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veclink.poofpetgps.bluetooth.pojo.BaseResponse;
import com.veclink.poofpetgps.bluetooth.pojo.QueryWifiResponse;
import com.veclink.poofpetgps.bluetooth.pojo.WifiInfo;
import com.veclink.poofpetgps.util.StorageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshen on 2016/6/27.
 */
public class ProofDeviceparseData {

    public static void parseData(Context mContext, String readData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(readData);
            String cmd = jsonObject.getString("cmd");
            if(cmd.equals("add_wifi")){
                WifiInfo wifiInfo = new WifiInfo();
                wifiInfo.ssid = jsonObject.getString("ssid");
                Log.v("receive ssid is", wifiInfo.ssid);
                wifiInfo.pwd = jsonObject.getString("pwd");
                wifiInfo.macAddress = jsonObject.getString("macAddress");
                wifiInfo.penName = jsonObject.getString("penName");
                wifiInfo.type = jsonObject.getInt("type");
                BaseResponse response = new BaseResponse();
                if(wifiInfo.ssid==null){
                    response.code = 1;
                    response.message = "Wifi name cannot be empty";
                }
                if(wifiInfo.pwd==null){
                    response.code = 2;
                    response.message = "Please input password to connect the wifi";
                }
                if(wifiInfo.type<1||wifiInfo.type>3){
                    response.code = 4;
                    response.message = "type is invaild";
                }
                if(response.code==0) {
                    List<WifiInfo> list = StorageUtil.readWifiInfoObjects(mContext);
                    list.add(wifiInfo);
                    StorageUtil.writeWifiInfoObjects(mContext, list);
                }
                Gson gson = new Gson();
                BluetoothUtil.getInstance(mContext).sendMessage(gson.toJson(response));

            }else if(cmd.equals("query_wifis")){
                List<WifiInfo> list = StorageUtil.readWifiInfoObjects(mContext);
                QueryWifiResponse response = new QueryWifiResponse();
                response.wifiInfos = list;
                Gson gson = new Gson();
                BluetoothUtil.getInstance(mContext).sendMessage(gson.toJson(response));
            }else if(cmd.equals("delete_wifi")){
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<String>>(){}.getType();
//                List<String> macAddressList = gson.fromJson(jsonObject.getString("macAddressArray"),type);
                String macAddressArray = jsonObject.getString("macAddressArray");
                List<WifiInfo> list = StorageUtil.readWifiInfoObjects(mContext);
                List<Integer> needRemoveIndexList = new ArrayList<Integer>();
                for (int i = 0; i < list.size(); i++) {
                    WifiInfo wifiInfo = list.get(i);
                    if(macAddressArray.contains(wifiInfo.macAddress)){
                        needRemoveIndexList.add(i);
                    }
                }
                for (int i = 0; i < needRemoveIndexList.size(); i++) {
                    list.remove(i);
                }
                StorageUtil.writeWifiInfoObjects(mContext,list);
                BaseResponse response = new BaseResponse();
                response.code = 0;
                response.message = "success";
                if(needRemoveIndexList.size()==0){
                    response.code = 1;
                    response.message = "macaddress not exist";
                }
                Gson gson = new Gson();
                BluetoothUtil.getInstance(mContext).sendMessage(gson.toJson(response));

            }else if(cmd.equals("update_wifi")){
                WifiInfo wifiInfo = new WifiInfo();
                wifiInfo.ssid = jsonObject.getString("ssid");
                wifiInfo.pwd = jsonObject.getString("pwd");
                wifiInfo.macAddress = jsonObject.getString("macAddress");
                wifiInfo.penName = jsonObject.getString("penName");
                wifiInfo.type = jsonObject.getInt("type");
                BaseResponse response = new BaseResponse();
                if(wifiInfo.ssid==null){
                    response.code = 1;
                    response.message = "Wifi name cannot be empty";
                }
                if(wifiInfo.pwd==null){
                    response.code = 2;
                    response.message = "Please input password to connect the wifi";
                }
                if(wifiInfo.type<1||wifiInfo.type>3){
                    response.code = 4;
                    response.message = "type is invaild";
                }
                if(response.code==0) {
                    List<WifiInfo> list = StorageUtil.readWifiInfoObjects(mContext);
                    boolean macAddressExit = false;
                    for (int i = 0; i < list.size(); i++) {
                        WifiInfo item = list.get(i);
                        if(item.macAddress.equals(wifiInfo.macAddress)){
                            item.ssid = wifiInfo.ssid;
                            item.pwd = wifiInfo.pwd;
                            item.penName = wifiInfo.penName;
                            item.type = wifiInfo.type;
                            macAddressExit = true;
                            break;
                        }
                    }
                    if(macAddressExit){
                        response.message = "success";
                        StorageUtil.writeWifiInfoObjects(mContext,list);
                    }else{
                        response.code = 3;
                        response.message = "macaddress not exist";
                    }
                }
                Gson gson = new Gson();
                BluetoothUtil.getInstance(mContext).sendMessage(gson.toJson(response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
