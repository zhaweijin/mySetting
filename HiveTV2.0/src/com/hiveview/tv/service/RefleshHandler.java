package com.hiveview.tv.service;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.request.GetAppFocusListRequest;
import com.hiveview.tv.utils.DateUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @ClassName: RefleshHandler
 * @Description: 在类里边调用loadservice刷新下载数据
 * @author:
 * @date 2014年9月18日 上午11:53:18
 * 
 */
public class RefleshHandler extends Handler {

	private boolean isReflesh = false;
	/**
	 * @Fields delayMillis:launcher 刷新时间
	 */
	public static  int delayMillis = 60000;
	/**
	 * 一天时间
	 */
	public static  int haiwai_delayMillis = 24 * 60 * 60 * 1000;
	
	//public static  int haiwai_delayMillis = 1000;
	
	private Context mContext;
	private String TAG = "RefleshHandler";

	public RefleshHandler(Context context) {
		mContext = context;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			if (isReflesh) {
				Log.v(TAG, "handleMessage==LoadService");
//				mContext.getSharedPreferences("RefleshHandler", Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0), " RefleshHandler LoadService").commit();
				Intent intent = new Intent(mContext, LoadService.class);
				intent.putExtra("isNeedDeviceCheck", false);// 在此启动的Service,不需要Service去鉴权设备，只需要去启动数据
				mContext.startService(intent);
				removeMessages(0);
				Log.d(TAG , "handleMessage：：delayMillis"+delayMillis);
				sendEmptyMessageDelayed(0, delayMillis);
			}
			break;
		case 1:
			if (isReflesh) {
				if(!AppConstant.ISDOMESTIC){
					Log.v(TAG, "handleMessage==海外");
//				mContext.getSharedPreferences("RefleshHandler", Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0), " RefleshHandler LoadService").commit();
				Intent intent = new Intent(mContext, LockVipService.class);
				mContext.startService(intent);
				removeMessages(1);
				Log.d(TAG , "handleMessage：：haiwai_delayMillis"+haiwai_delayMillis);
				sendEmptyMessageDelayed(1, haiwai_delayMillis);
				}
			}
			break;
		default:
			break;
		}

	}

	public void setDelayMillis(int minute) {
		int second = minute * 60;
		delayMillis = second * 1000;
		Log.d(TAG , "setDelayMillis：：delayMillis"+delayMillis);
		Log.d(TAG , "setDelayMillis：：haiwai_delayMillis"+haiwai_delayMillis);
	}

	public void startReflesh() {
		isReflesh = true;
		removeMessages(0);
		sendEmptyMessageDelayed(0, delayMillis);
		
		removeMessages(1);
		sendEmptyMessageDelayed(1, haiwai_delayMillis);
		Log.d(TAG , "startReflesh：：delayMillis"+delayMillis);
		Log.d(TAG , "handleMessage：：haiwai_delayMillis"+haiwai_delayMillis);
	}

	public void stopReflesh() {
		isReflesh = false;
	}

	public void startRefleshImmediately() {
		isReflesh = true;
		removeMessages(0);
		sendEmptyMessage(0);
		removeMessages(1);
		sendEmptyMessage(1);
	}

}
