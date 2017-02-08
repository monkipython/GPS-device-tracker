package com.veclink.poofpetgps;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.veclink.poofpetgps.wifi.WiFiInfoAdapter;
import com.veclink.poofpetgps.wifi.WifiAdmin;
import com.veclink.poofpetgps.wifi.WifiPswDialog;

import java.util.List;


public class TestConnectWifi extends Activity {

    private WifiAdmin wifiAdmin;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connect_wifi);
        init();


    }


    private void init() {
        wifiAdmin = WifiAdmin.getInstance(this);
        wifiAdmin.openWifi();
        wifiAdmin.startScan();
        wifiAdmin.startScan();
        final List<ScanResult> list = wifiAdmin.getWifiList();
        ListView listView = (ListView) findViewById(R.id.list);
        if (list == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        }else {
            listView.setAdapter(new WiFiInfoAdapter(this,list));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                createWifiInfo(String SSID, String Password,
//                int Type)
//                wifiAdmin.createWifiInfo();
                final ScanResult scanResult = list.get(position);
                Log.v("select ScanResult is",scanResult.toString());
                int type=1;
                if(scanResult.capabilities.toUpperCase().contains("WPA")){
                    type = WifiAdmin.TYPE_WPA;
                }else if(scanResult.capabilities.toUpperCase().contains("WEA")){
                    type = WifiAdmin.TYPE_WEP;
                }else{
                    type = WifiAdmin.TYPE_NO_PASSWD;
                }
                if(type==1){
                    wifiAdmin.addNetwork(wifiAdmin.createWifiInfo(scanResult.BSSID,null,type));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("connect macaddress is",wifiAdmin.getMacAddress());
                        }
                    },3000);
                }else{
                    WifiPswDialog wifiPswDialog = new WifiPswDialog(TestConnectWifi.this, new WifiPswDialog.OnCustomDialogListener() {
                        @Override
                        public void back(String password) {
                            if(password!=null){
                                int type=1;
                                if(scanResult.capabilities.toUpperCase().contains("WPA")){
                                    type = WifiAdmin.TYPE_WPA;
                                }else if(scanResult.capabilities.toUpperCase().contains("WEA")){
                                    type = WifiAdmin.TYPE_WEP;
                                }else{
                                    type = WifiAdmin.TYPE_NO_PASSWD;
                                }
                                wifiAdmin.addNetwork(wifiAdmin.createWifiInfo(scanResult.SSID,password,type));
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.v("connect macaddress is",wifiAdmin.getMacAddress());
                                    }
                                },3000);
                            }
                        }
                    });
                    wifiPswDialog.show();
                }


            }


        });

    }



}
