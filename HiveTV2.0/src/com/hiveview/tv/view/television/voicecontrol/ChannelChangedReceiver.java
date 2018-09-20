/**
 * @author: zhaijianfeng
 * @description: 用于接收科大讯飞超级指控换台完成时发出的广播 (讯飞匹配回来tv名字)
 */
package com.hiveview.tv.view.television.voicecontrol;

import com.hiveview.tv.view.television.ChannelChangedListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

public class ChannelChangedReceiver extends BroadcastReceiver {
	private static final String TAG = "ChannelChangedReceiver";
	
	/*科大讯飞超级智控换台完成后发出的广播action*/
	private static final String CHANNEL_SWITCHED_NOTIFY_ACTION = "com.iflyek.TVDCS.STBSTATUS"; 
	private static final String KEY_CHANNEL_NAME = "channelname";
	private static final String KEY_CHANNEL_NUM = "channelnum";
	private static final String PREFERENCE = "DomyBoxPreference";
	
	private ChannelChangedListener mListener = null; //如果需要监听换台完成时间则常见监听器
	
	public ChannelChangedReceiver() {
		super();
		this.mListener = null;
	}

	public ChannelChangedReceiver(ChannelChangedListener listener) {
		super();
		this.mListener = listener;
	}
	/**
	 * 创建监听器对象
	 * @param 
	 * @return
	 */
	public static ChannelChangedReceiver CreateChannelChangedReceiver(ChannelChangedListener listener) {
		return new ChannelChangedReceiver(listener);
	}
	
	/**
	 * 注册频道切换广播接收器
	 * @param context
	 */
	public void registerChannelChangedReceiver(Context context) {
		Log.i(TAG, "registerReceiver-->start");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CHANNEL_SWITCHED_NOTIFY_ACTION);
//		context.registerReceiver(this, intentFilter);
		Log.i(TAG, "registerReceiver-->end");
	}
		

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "onReceive-->start");
		if (CHANNEL_SWITCHED_NOTIFY_ACTION.equals(intent.getAction())) {
			/*广播intent中携带channelName和channelNum两个键值*/
			String channelName = intent.getStringExtra(KEY_CHANNEL_NAME);
			String channelNum = intent.getStringExtra(KEY_CHANNEL_NUM);
			SharedPreferences preference = context.getSharedPreferences(
					PREFERENCE, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preference.edit();
			if (channelName != null) {
				Log.i(TAG, "onReceive-->channelName:"
						+ channelName+"...................channelNum:"+channelNum);
				
				/*如果listener为null，这位全局广播，监听目的为记录换台后channel，用于频道记忆/恢复feature*/
				if (mListener == null) {
					editor.putString(KEY_CHANNEL_NAME, channelName);
					editor.commit();
				}
				/*listener不为null，则该receiver对象为activity创建，需要接收到广播后进行相应UI设置*/
				if (mListener != null) {
					mListener.onChannelChanged(channelName);
				}
			}
			/*channelNum暂时不需要进行处理*/
//			if (channelNum != null) {
//				Log.i(TAG, "onReceive-->channelNum:"
//						+ channelNum);
//				if (mListener == null) {
//					editor.putString(KEY_CHANNEL_NUM, channelNum);
//					editor.commit();
//				}
//				if (mListener != null) {
//					mListener.onChannelChanged(channelNum);
//				}
//			}
		}
		Log.i(TAG, "onReceive-->end");
		
	}
}
