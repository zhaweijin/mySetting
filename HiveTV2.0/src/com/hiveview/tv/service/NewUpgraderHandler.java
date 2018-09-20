package com.hiveview.tv.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hiveview.tv.common.silenceupgrade.NewUpgrader;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
/**
 * 静默升级轮询检测
 * @ClassName: NewUpgraderHandler
 * @Description: TODO
 * @author: huzuwei
 * @date 2015年8月17日 下午6:52:31
 *
 */
public class NewUpgraderHandler extends Handler {
	public static boolean isNewUpgrader = false;
	/**
	 * @Fields delayMillis:launcher 静默升级检测12小时
	 */
	public static int newUpgraderdeTime = 12 * 60 * 60 * 1000;
	private Context mContext;
	private String TAG = "NewUpgraderHandler";

	public NewUpgraderHandler(Context context) {
		mContext = context;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if (isNewUpgrader) {
			Log.d(TAG, "---isNewUpgrader::" + isNewUpgrader + "--handleMessage");
			new NewUpgrader().upgradeApp(mContext);
		//	KeyEventHandler.post(new DataHolder.Builder(mContext).setTabNo(Tab.TAB).setViewPosition("0000").setIntervalDay("0").build());
			isNewUpgrader = false;
		}
	}

	public void setDelayMillis(int minute) {
		int second = minute * 60;
		newUpgraderdeTime = second * 1000;
		Log.d(TAG, "setDelayMillis：：delayMillis" + newUpgraderdeTime);
	}

	/**
	 * 静默升级检测开始
	 * 
	 * @Title: NewUpgraderHandler
	 * @author:huzuwei
	 * @Description: TODO
	 */
	public void startNewUpgrader() {
		removeMessages(0);
		sendEmptyMessageDelayed(0, newUpgraderdeTime);
	}

	/**
	 * 关闭静默升级检测
	 * 
	 * @Title: NewUpgraderHandler
	 * @author:huzuwei
	 * @Description: TODO
	 */
	public void stopNewUpgrader() {
		removeMessages(0);
	}

	public void startNewUpgraderImmediately() {
		removeMessages(0);
		sendEmptyMessage(0);
	}
}
