///**
// *
// */
//package com.veclink.poofpetgps.wifi;
//
///**
// *
// * @author chenshen
// * @time 2016-6-24
// */
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.net.Uri;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.kyleduo.switchbutton.SwitchButton;
//import com.veclink.poofpetgps.R;
//
//public class WifiListActivity extends Activity {
//
//	private ArrayList<WifiInfo> wifiArray;
//	private WiFiInfoAdapter wifiInfoAdapter;
//	private ListView listWifi;
//
//	private ProgressBar updateProgress;
//	private Button updateButton;
//	private String wifiPassword = null;
//
//	private WifiManager wifiManager;
//	private WifiAdmin wiFiAdmin;
//	private List<ScanResult> list;
//	private ScanResult mScanResult;
//	private StringBuffer sb = new StringBuffer();
//	/**
//	 * ATTENTION: This was auto-generated to implement the App Indexing API.
//	 * See https://g.co/AppIndexing/AndroidStudio for more information.
//	 */
//	private GoogleApiClient client;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.activity_wifi_list);
//		wiFiAdmin = new WifiAdmin(WifiListActivity.this);
//		initLayout();
//		getAllNetWorkList();
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//	}
//
//	public void initLayout() {
//		listWifi = (ListView) findViewById(R.id.listWiFi);
//		RelativeLayout btnToSettingFromWiFi = (RelativeLayout) findViewById(R.id.btnToSettingFromWiFi);
//		btnToSettingFromWiFi.setOnClickListener(new MyOnClickListener());
//		updateProgress = (ProgressBar) findViewById(R.id.updateProgress);
//		updateProgress.setVisibility(View.INVISIBLE);
//		updateButton = (Button) findViewById(R.id.updateButton);
//		updateButton.setVisibility(View.VISIBLE);
//		updateButton.setOnClickListener(new MyOnClickListener());
//		SwitchButton switchWifi = (SwitchButton) findViewById(R.id.switchWifi);
//		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		switchWifi.setChecked(wifiManager.isWifiEnabled());
//		switchWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//										 boolean isChecked) {
//				wifiManager.setWifiEnabled(isChecked);
//				if (isChecked) {
//					listWifi.setVisibility(View.VISIBLE);
//					updateProgress.setVisibility(View.VISIBLE);
//					updateButton.setVisibility(View.INVISIBLE);
//					new Thread(new refreshWifiThread()).start();
//				} else {
//					listWifi.setVisibility(View.GONE);
//				}
//			}
//		});
//
//	}
//
//	final Handler refreshWifiHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//				case 1:
//					getAllNetWorkList();
//					updateProgress.setVisibility(View.INVISIBLE);
//					updateButton.setVisibility(View.VISIBLE);
//					break;
//
//				default:
//					break;
//			}
//		}
//	};
//
//	@Override
//	public void onStart() {
//		super.onStart();
//
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		client.connect();
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"WifiList Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app URL is correct.
//				Uri.parse("android-app://com.veclink.poofpetgps/http/host/path")
//		);
//		AppIndex.AppIndexApi.start(client, viewAction);
//	}
//
//	@Override
//	public void onStop() {
//		super.onStop();
//
//		// ATTENTION: This was auto-generated to implement the App Indexing API.
//		// See https://g.co/AppIndexing/AndroidStudio for more information.
//		Action viewAction = Action.newAction(
//				Action.TYPE_VIEW, // TODO: choose an action type.
//				"WifiList Page", // TODO: Define a title for the content shown.
//				// TODO: If you have web page content that matches this app activity's content,
//				// make sure this auto-generated web page URL is correct.
//				// Otherwise, set the URL to null.
//				Uri.parse("http://host/path"),
//				// TODO: Make sure this auto-generated app URL is correct.
//				Uri.parse("android-app://com.veclink.poofpetgps.wifi/http/host/path")
//		);
//		AppIndex.AppIndexApi.end(client, viewAction);
//		client.disconnect();
//	}
//
//	public class refreshWifiThread implements Runnable {
//
//		@Override
//		public void run() {
//			try {
//				Thread.sleep(3000);
//				Message message = new Message();
//				message.what = 1;
//				refreshWifiHandler.sendMessage(message);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	private class MyOnClickListener implements OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//				case R.id.btnToSettingFromWiFi:
//					finish();
//					break;
//				case R.id.updateButton:
//					updateButton.setVisibility(View.INVISIBLE);
//					updateProgress.setVisibility(View.VISIBLE);
//					new Thread(new refreshWifiThread()).start();
//					break;
//				default:
//					break;
//			}
//		}
//
//	}
//
//	public void getAllNetWorkList() {
//
//		wifiArray = new ArrayList<WifiInfo>();
//		if (sb != null) {
//			sb = new StringBuffer();
//		}
//		wiFiAdmin.startScan();
//		list = wiFiAdmin.getWifiList();
//		if (list != null) {
//			for (int i = 0; i < list.size(); i++) {
//				mScanResult = list.get(i);
//				WifiInfo wifiInfo = new WifiInfo(mScanResult.BSSID,
//						mScanResult.SSID, mScanResult.capabilities,
//						mScanResult.level);
//
//				wifiArray.add(wifiInfo);
//			}
//			wifiInfoAdapter = new WiFiInfoAdapter(getApplicationContext(),
//					wifiArray);
//			listWifi.setAdapter(wifiInfoAdapter);
//
//			//
//			wiFiAdmin.getConfiguration();
//
//			listWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//				String wifiItemSSID = null;
//
//				public void onItemClick(AdapterView<?> parent,
//										View view, int position, long id) {
//
//					Log.d("PoofWifi", "BSSID:" + list.get(position).BSSID);
//					wifiItemSSID = list.get(position).SSID;
//					int wifiItemId = wiFiAdmin.IsConfiguration("\""
//							+ list.get(position).SSID + "\"");
//					if (wifiItemId != -1) {
//						if (wiFiAdmin.ConnectWifi(wifiItemId)) {
//							// 连接已保存密码的WiFi
//							Toast.makeText(getApplicationContext(), "正在连接",
//									Toast.LENGTH_SHORT).show();
//							updateButton.setVisibility(View.INVISIBLE);
//							updateProgress.setVisibility(View.VISIBLE);
//							new Thread(new refreshWifiThread()).start();
//						}
//					} else {
//
//						WifiPswDialog pswDialog = new WifiPswDialog(
//								WifiListActivity.this,
//								new WifiPswDialog.OnCustomDialogListener() {
//									@Override
//									public void back(String str) {
//										wifiPassword = str;
//										if (wifiPassword != null) {
//											int netId = wiFiAdmin
//													.AddWifiConfig(list,
//															wifiItemSSID,
//															wifiPassword);
//											if (netId != -1) {
//												wiFiAdmin.getConfiguration();
//												if (wiFiAdmin
//														.ConnectWifi(netId)) {
//													updateProgress
//															.setVisibility(View.VISIBLE);
//													updateButton
//															.setVisibility(View.INVISIBLE);
//													new Thread(
//															new refreshWifiThread())
//															.start();
//												}
//											} else {
//
//											}
//										} else {
//										}
//									}
//								});
//						pswDialog.show();
//					}
//				}
//			});
//		}
//	}
//}
