package com.hiveview.cloudtv.settings;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.hiveview.cloudtv.settings.imageprofile.TimeZoneEntity;
import com.hiveview.cloudtv.settings.util.LoadConfigTask;

public class SettingsApplication extends Application {
	private Bitmap mBGBitmap = null;
	private static SettingsApplication mApplication = null;
	private int mPingStatus = -999;
	public  int status;
	public ArrayList<TimeZoneEntity> entities =new ArrayList<TimeZoneEntity>();
	public static int TYPE_PPPOE  = 0;
	public static float density;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("SettingsApplication", "------------SettingsApplication START-----");
		mApplication = this;
		LoadConfigTask task=new LoadConfigTask();
		task.execute();
		Log.i("SettingsApplication", "-------------"+status);
		intData();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        density = outMetrics.density;
	}
	
	public static SettingsApplication getInstance(){
		if(mApplication == null){
			mApplication = new SettingsApplication();
		}
		return mApplication;
	}
	
	public Bitmap getBGBitmap(){
		return mBGBitmap;
	}
	
	public void setBGBitmap(Bitmap bitmap){
		mBGBitmap = bitmap;
	}
	
	public void setPingStatus(int status){
		mPingStatus = status;
	}
	
	public int getPingStatus(){
		return mPingStatus;
	}
	
	private void intData(){
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	        TYPE_PPPOE = connectivity.getPppoeTypeValue();
		} catch (NoSuchMethodError e) {
			// TODO: handle exception
			e.printStackTrace();
			TYPE_PPPOE = 18;//默认1.0s
		}finally{
			
		}
		
	}
}
