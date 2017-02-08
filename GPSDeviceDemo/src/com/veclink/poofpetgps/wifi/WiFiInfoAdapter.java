package com.veclink.poofpetgps.wifi;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.veclink.poofpetgps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenshen on 2016/6/24.
 */
public class WiFiInfoAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<ScanResult> list;
    Context context;
    public WiFiInfoAdapter(Context context, List<ScanResult> list) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = null;
        view = inflater.inflate(R.layout.wifi_list_item, null);
        ScanResult scanResult = list.get(position);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(scanResult.SSID);
        TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
        signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
       if(scanResult.capabilities.toUpperCase().contains("WPA")||scanResult.capabilities.toUpperCase().contains("WEA")){
           imageView.setBackgroundResource(R.drawable.wifi_lock);
       }else{
           imageView.setBackgroundResource(R.drawable.wifi_nopwd);
       }
        return view;
    }

    private Resources getResources(){
        return context.getResources();
    }


}
