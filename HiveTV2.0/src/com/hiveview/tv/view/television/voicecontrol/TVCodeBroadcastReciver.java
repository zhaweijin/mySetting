package com.hiveview.tv.view.television.voicecontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TVCodeBroadcastReciver extends BroadcastReceiver {
		public static String author;
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			  if(action.equals("com.hiveview.tv.activity.LiveFastSelectTvChannelActivity")) {
				 author = intent.getStringExtra("author");
			  }
		}  
	 }