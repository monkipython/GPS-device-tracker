package com.veclink.poofpetgps;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.veclink.poofpetgps.bluetooth.BluetoothUtil;
import com.veclink.poofpetgps.bluetooth.pojo.LoginJson;
import com.veclink.poofpetgps.task.ResponseServerRequestTask;
import com.veclink.poofpetgps.task.WifiConnectCheckTask;
import com.veclink.poofpetgps.tcp.TcpClient;
import com.veclink.poofpetgps.tcp.TcpSocketManager;
import com.veclink.poofpetgps.tcp.pojo.LocationInfo;
import com.veclink.poofpetgps.tcp.pojo.ServerRequest;
import com.veclink.poofpetgps.tcp.pojo.ServerResponse;
import com.veclink.poofpetgps.util.AppContext;
import com.veclink.poofpetgps.util.CreateUploadDataUtil;
import com.veclink.poofpetgps.util.GsonUtil;
import com.veclink.poofpetgps.util.StorageUtil;
import com.veclink.poofpetgps.util.Utils;
import com.veclink.poofpetgps.wifi.MobileNetUtil;
import com.veclink.poofpetgps.wifi.WifiAdmin;

import de.greenrobot.event.EventBus;

/**
 * Created by chenshen on 2016/6/23.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private TextView showmsg_tv;
    private RadioGroup work_mode_group;
    private StringBuffer buffer = new StringBuffer();
    BluetoothUtil bluetoothUtil;
    ConnectionChangeReceiver receiver = new ConnectionChangeReceiver();
    private Handler mHandler = new Handler();
    private Context mContext;
    private WifiAdmin wifiAdmin;
    private EditText et_ip1;
    private EditText et_ip2;
    private EditText et_ip3;
    private EditText et_ip4;
    private EditText et_port;
    private String ip="";
    private int port;
    private String ipname="ip_address";;
    private String portname="ip_port";
    ProofWorkModeManager proofWorkModeManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        AppContext.init(this);
        proofWorkModeManager = ProofWorkModeManager.getIntance(this);
        proofWorkModeManager.changeDefalutWordMode();
        showmsg_tv = (TextView) findViewById(R.id.showmsg_tv);
        Object ipObj=StorageUtil.readSerialObject(mContext, ipname);
        Object portObj=StorageUtil.readSerialObject(mContext, portname);
       
        et_ip1=(EditText)findViewById(R.id.et_ip1);
        et_ip2=(EditText)findViewById(R.id.et_ip2);
        et_ip3=(EditText)findViewById(R.id.et_ip3);
        et_ip4=(EditText)findViewById(R.id.et_ip4);
        et_port=(EditText)findViewById(R.id.et_port);
        
        if(ipObj != null){
        	ip=(String)ipObj;
        	Log.d("xwj", "ip="+ip);
        	 String[] ipStr=ip.split("\\.");
        	Log.d("xwj", "ipStr.length="+ipStr.length);
        	for(int i=0;i<ipStr.length;i++){
        		Log.d("xwj", "ipStr="+ipStr[i]);
        	}
        	if(ipStr.length == 4){
	            et_ip1.setText(ipStr[0]);
	            et_ip2.setText(ipStr[1]);
	            et_ip3.setText(ipStr[2]);
	            et_ip4.setText(ipStr[3]);
        	}
            
        }
        if(portObj != null){
        	port=(Integer)portObj;
        	Log.d("xwj", "port="+port);
        	et_port.setText(port+"");
        }
        
        work_mode_group = (RadioGroup)findViewById(R.id.work_mode_group);
        
        work_mode_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.default_mode:
					proofWorkModeManager.changeDefalutWordMode();
					break;
				case R.id.wifi_mode:
					proofWorkModeManager.changeWifiWordMode();
					break;
				case R.id.gps_mode:
					proofWorkModeManager.changeGpsWordMode();
					break;

				default:
					break;
				}
				
			}
		});
        bluetoothUtil = BluetoothUtil.getInstance(this);
        bluetoothUtil.setBluetoothListener(new BluetoothUtil.BluetoothListener() {
            @Override
            public void conntected(String deviceName) {
                buffer.append("已连接蓝牙设备：" + deviceName).append("\n");
                showmsg_tv.setText(buffer.toString());
            }

            @Override
            public void conntecting(String deviceName) {
                buffer.append("正在连接设备：" + deviceName).append("\n");
                showmsg_tv.setText(buffer.toString());
            }

            @Override
            public void disConntectd(String deviceName) {
                buffer.append(deviceName + "已断开连接").append("\n");
                showmsg_tv.setText(buffer.toString());
            }

            @Override
            public void readData(String recedata) {
                buffer.append("收到蓝牙发来的数据：").append(recedata).append("\n");
                showmsg_tv.setText(buffer.toString());
            }

            @Override
            public void writeDataSuccess(String writedata) {
                buffer.append("回复蓝牙发来的数据：").append(writedata).append("\n");
                showmsg_tv.setText(buffer.toString());
            }
        });
        bluetoothUtil.start();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        Global.config = StorageUtil.readConfig(this);   
        Global.devid = Utils.getPhoneId();
        if(MobileNetUtil.isNetConnected(this)){
        	TcpSocketManager manager = TcpSocketManager.getInstance();
        	manager.startTcpConnect();
        	Gson gson = new Gson();
        	LoginJson loginJson=new LoginJson();
			String content = gson.toJson(loginJson);			
			byte[] backdata = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.SERVER_LOGIN_RQUEST, content);			
			TcpSocketManager.getInstance().sendMessage(backdata);
        }
		EventBus.getDefault().register(this,ServerResponse.class);
		EventBus.getDefault().register(this,ServerRequest.class);
    }


    @Override
    public void onClick(View v) {
    	
    	if(MobileNetUtil.isNetConnected(this)==false){
    		Toast.makeText(mContext, "请打开网络或通过app是设置wifi后切换到wifi模式,才能上传定位信息", Toast.LENGTH_SHORT).show();
    	}
    	
    	switch (v.getId()) {
		

		case R.id.lowper_location_upload:
			String ip1=et_ip1.getText().toString();
			String ip2=et_ip2.getText().toString();
			String ip3=et_ip3.getText().toString();
			String ip4=et_ip4.getText().toString();
			String str_port=et_port.getText().toString();
			if(ip1.isEmpty() || ip2.isEmpty() || ip3.isEmpty() || ip4.isEmpty()){
				Toast.makeText(mContext, "ip地址输入不正确", Toast.LENGTH_SHORT);
				return;
			}
			if(str_port.isEmpty()){
				Toast.makeText(mContext, "IP地址端口未输入", Toast.LENGTH_SHORT);
				return;
			}
			ip=ip1+"."+ip2+"."+ip3+"."+ip4;
			port=Integer.parseInt(str_port);
		    StorageUtil.writeSerialObject(mContext, ip, ipname);
		    StorageUtil.writeSerialObject(mContext, port, portname);
			TcpSocketManager.getInstance().setIpAddressAndPort(ip, port);
			LocationInfo laInfo = new LocationInfo("113.56", "22.48");
			Gson gson = new Gson();
			String content = gson.toJson(laInfo);			
			byte[] backdata = CreateUploadDataUtil.createUploadData(CreateUploadDataUtil.LOWPERY_LOCATION, content);			
			TcpSocketManager.getInstance().sendMessage(backdata);
			break;
		}
       
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this,ServerResponse.class);
        EventBus.getDefault().unregister(this,ServerRequest.class);
        TcpSocketManager.getInstance().sopTcpConnect();
        bluetoothUtil.stop();
    }
    
    public void onEventMainThread(ServerResponse response){    
    	buffer.append("服务器响应：").append(GsonUtil.toJson(response)).append("\n");
    	showmsg_tv.setText(buffer.toString());
    }
    
    public void onEventMainThread(ServerRequest request){
    	buffer.append("服务器请求：").append(GsonUtil.toJson(request)).append("\n");
    	showmsg_tv.setText(buffer.toString());
    	ResponseServerRequestTask task = new ResponseServerRequestTask();
    	task.execute(0);
    }
}
