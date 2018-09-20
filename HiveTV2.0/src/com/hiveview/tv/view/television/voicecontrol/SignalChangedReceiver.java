package com.hiveview.tv.view.television.voicecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.utils.CloseBlueLightUtil;
import com.hiveview.tv.utils.SwitchChannelUtils;

/**
 * @author: zhaijianfeng
 * @description: 用于接收科大讯飞超级指控切换信号源时发出的广播(打开播放的方案)
 */
public class SignalChangedReceiver extends BroadcastReceiver {
	/**
	 * @Fields TAG:TODO
	 */
	private static final String TAG = "SignalChangedReceiver";

	/* 科大讯飞超级智控切换信号源时发出的广播action */
	private static final String SIGNAL_CHANGED_NOTIFY_ACTION = "com.iflytek.tvdcs.change_input_source";

//	private SignalChangedReceiver mInstance = null;
	
	/**
	 * @Fields intentFilter:TODO 广播过滤器
	 */
	private IntentFilter intentFilter =null;

	/**
	 * 创建监听器对象
	 * 
	 * @param
	 * @return
	 */
//	public SignalChangedReceiver createSignalChangedReceiver() {
//		if (null == mInstance) {
//			mInstance = new SignalChangedReceiver();
//		}
//		return mInstance;
//	}

	/**
	 * 注册信号切换广播接收器
	 * 
	 * @param context
	 */
	public void registerSignalChangedReceiver(Context context) {
		Log.i(TAG, "registerReceiver-->start");
		intentFilter = new IntentFilter();
		intentFilter.addAction(SIGNAL_CHANGED_NOTIFY_ACTION);
		context.registerReceiver(this, intentFilter);
		Log.i(TAG, "registerReceiver-->end");
	}
	
	/** 
	 * 反注册
	 * @Title: SignalChangedReceiver
	 * @author:郭松胜
	 * @Description: TODO
	 * @param context
	 */
	public void unRegisterSignalChangedReceiver(Context context){
		context.unregisterReceiver(this);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceive-->start");
		Log.i(TAG, "onReceive-->SwitchChannelUtils.getActionScene():" + SwitchChannelUtils.getActionScene());

		if (SwitchChannelUtils.getChannelInitializingFlag()) {
			SwitchChannelUtils.setChannelInitializingFlag(false);
			return;
		}
		if (SIGNAL_CHANGED_NOTIFY_ACTION.equals(intent.getAction()) && !AppScene.CURRENCE_SCENE.equals(AppScene.ONLIVEPLAY_SCENE)
		/* && !AppScene.CURRENCE_SCENE.equals(AppScene.TVVIEW_SCENE) */
		&& !AppScene.CURRENCE_SCENE.equals(AppScene.WELCOME_SCENE)) {
			// start author:zhangpengzhan
			CloseBlueLightUtil.closeBlueLight();
			// end
			Intent it = new Intent(context, OnlivePlayerActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(it);
		}
		Log.i(TAG, "onReceive-->end");
	}
}
