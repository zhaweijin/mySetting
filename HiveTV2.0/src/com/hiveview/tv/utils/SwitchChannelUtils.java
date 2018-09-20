package com.hiveview.tv.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.hiveview.tv.common.AppScene;

public class SwitchChannelUtils {
	private static boolean isFirst=true;
	private static final String TAG = "SwitchChannelUtils";
	private static final String PREFERENCE = "DomyBoxPreference";
	private static final String SWITCH_CHANNEL_ACTION = "com.iflytek.xiri2.app.CALL";
	public static final String KEY_CHANNEL_NAME = "channelname";
	public static final String KEY_VOICE_NAME = "voicechannelname";
	public static final String KEY_VOICE_DATE = "voicedate";
	public static final String KEY_CHANNEL_NUM = "channelnum";
	private static final String DEFAULT_CHANNEL_NAME = "cctv1";
	private static boolean channelInitialized = false;
	private static boolean channelInitializing = false;
	/*
	 * 当前场景
	 * 
	 */
	private static String actionScene=""; 

	public static void switchChannel(Context context, String channelName, boolean openPlayerFlag, String scene) {
		channelInitialized = true;
		Log.i(TAG, "switchChannel-->channelName:" + channelName);
		actionScene = scene;

		Log.i(TAG, "switchChannel-->actionScene:" + actionScene);
		Intent intent = new Intent(SWITCH_CHANNEL_ACTION);
		intent.putExtra("_action", "CHANGECHANNEL");
		intent.putExtra("name", channelName);
		context.startService(intent);
		/**
		 * 这段代码可以不用， 在一个广播里
		 */
//		if (openPlayerFlag) {
//			Intent playerIntent = new Intent((Activity) context, OnlivePlayerActivity.class);
//			playerIntent.putExtra("a1", 100);
//			Log.i(TAG, "switchChannel-->getActionScene:" + getTvTage() + "context : " + context.toString() + "intent : ");
//			((Activity) context).startActivity(playerIntent);
//		}
	}
       
	public static String getActionScene(){
		Log.i(TAG, "switchChannel-->getActionScene:" + actionScene);
		return actionScene;
	}
	public static String getCurrentChannelName(Context context) {
		Log.i(TAG, "getInitChannelName-->start");
		SharedPreferences preference = context.getSharedPreferences(PREFERENCE,
				Context.MODE_PRIVATE);
		Log.i(TAG, "getInitChannelName-->end");
		return preference.getString(KEY_CHANNEL_NAME, DEFAULT_CHANNEL_NAME);
	}

	public static String getVoiceCurrentChannelName(Context context) {
		Log.i(TAG, "getVoiceCurrentChannelName-->start");
		SharedPreferences preference = context.getSharedPreferences(PREFERENCE,
				Context.MODE_PRIVATE);
		Log.i(TAG, "getVoiceCurrentChannelName-->end");
		return preference.getString(KEY_VOICE_NAME, DEFAULT_CHANNEL_NAME);
	}
	public static String getVoiceDate(Context context) {
		Log.i(TAG, "getVoiceDate-->start");
		SharedPreferences preference = context.getSharedPreferences(PREFERENCE,
				Context.MODE_PRIVATE);
		Log.i(TAG, "getVoiceDate-->end");
		return preference.getString(KEY_VOICE_DATE, null);
	}
	public static boolean getisFirst() {
		return isFirst;
	}

	public static void setisFirst(boolean isFirst) {
		SwitchChannelUtils.isFirst = isFirst;
	}
    public static void initSTBChannel(Context context) {
        if (true == channelInitialized) {
        	return;
        }
        channelInitializing = true;
		Log.i(TAG, "initSTBChannel-->start");
		String channelName = SwitchChannelUtils
				.getCurrentChannelName(context);
		Log.i(TAG, "initSTBChannel-->channelName:" + channelName);
		switchChannel(context, channelName, false, AppScene.getScene());
		Log.i(TAG, "initSTBChannel-->end");
    	
    }
	
    public static boolean getChannelInitializingFlag() {
    	return channelInitializing;
    }
    public static void setChannelInitializingFlag(boolean flag) {
    	channelInitializing = flag;
    }
	
//	public static class SwitchReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Log.i(TAG, "switchReceiver:onReceive-->start");
//			if (SWITCHED_NOTIFY_ACTION.equals(intent.getAction())) {
//				String channelName = intent.getStringExtra(KEY_CHANNEL_NAME);
//				String channelNum = intent.getStringExtra(KEY_CHANNEL_NUM);
//				SharedPreferences preference = context.getSharedPreferences(
//						PREFERENCE, Context.MODE_PRIVATE);
//				SharedPreferences.Editor editor = preference.edit();
//				if (channelName != null) {
//					Log.i(TAG, "switchReceiver:onReceive-->channelName:"
//							+ channelName);
//					editor.putString(KEY_CHANNEL_NAME, channelName);
//					editor.commit();
//				}
//				if (channelNum != null) {
//					Log.i(TAG, "switchReceiver:onReceive-->channelNum:"
//							+ channelNum);
//					editor.putString(KEY_CHANNEL_NUM, channelNum);
//					editor.commit();
//				}
//			}
//			Log.i(TAG, "switchReceiver:onReceive-->end");
//		}
//	}

}
