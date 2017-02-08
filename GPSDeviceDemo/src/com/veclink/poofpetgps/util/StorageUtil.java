package com.veclink.poofpetgps.util;

import android.content.Context;

import com.veclink.poofpetgps.Config;
import com.veclink.poofpetgps.bluetooth.pojo.WifiInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StorageUtil {

	public static final String fileName = "wifiInfos.txt";

	public static final String configFileName = "config.ini";

	public static void writeWifiInfoObjects(Context mContext,List<WifiInfo> list) {
		File fileDir = mContext.getFilesDir();
		File file = new File(fileDir.getAbsolutePath() + File.separator
				+ fileName);
		try {
			ObjectOutputStream outPut = new ObjectOutputStream(
					new FileOutputStream(file));
			outPut.writeObject(list);
			outPut.flush();
			outPut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<WifiInfo> readWifiInfoObjects(Context mContext) {
		List<WifiInfo> result = new ArrayList<WifiInfo>();
		File fileDir = mContext.getFilesDir();
		File file = new File(fileDir.getAbsolutePath() + File.separator
				+ fileName);
		try {
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream(file));
			result = (List<WifiInfo>) input.readObject();
			input.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}

		return result;

	}


	public static void writeConfig(Context mContext,Config config) {
		File fileDir = mContext.getFilesDir();
		File file = new File(fileDir.getAbsolutePath() + File.separator
				+ configFileName);
		try {
			ObjectOutputStream outPut = new ObjectOutputStream(
					new FileOutputStream(file));
			outPut.writeObject(config);
			outPut.flush();
			outPut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Config readConfig(Context mContext) {
		Config result = null;
		File fileDir = mContext.getFilesDir();
		File file = new File(fileDir.getAbsolutePath() + File.separator
				+ configFileName);
		try {
			ObjectInputStream input = new ObjectInputStream(
					new FileInputStream(file));
			result = (Config) input.readObject();
			input.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} catch (ClassNotFoundException e) {

		}
		if(result==null)result = new Config();
		return result;

	}
	
	public static Object readSerialObject(Context mContext,String filename){
        Object result=null;
        File fileDir = mContext.getFilesDir();
        File file = new File(fileDir.getAbsolutePath()+File.separator+filename);
        try {
            if(file.exists()){
                FileInputStream fin =mContext.openFileInput(filename);
                ObjectInputStream input = new ObjectInputStream(fin);
                result =input.readObject();
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static void writeSerialObject(Context mContext,Object list,String filename){
        File fileDir = mContext.getFilesDir();
        File file = new File(fileDir.getAbsolutePath()+File.separator+filename);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fout =mContext.openFileOutput(filename, Context.MODE_PRIVATE);//获得FileOutputStream
            ObjectOutputStream outPut = new ObjectOutputStream(fout);
            outPut.writeObject(list);
            outPut.flush();
            outPut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
