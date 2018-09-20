package com.hiveview.tv.common.voice.impl;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hiveview.tv.R;
import com.hiveview.tv.common.voice.AppSpeaker;
import com.hiveview.tv.common.voice.IVoiceController;

public class ChatVoiceController implements IVoiceController {

	/**
	 * 对应的提示语查看assets/xiri.txt
	 */
	private static final String COMMAND_1 = "command1";
	/**
	 * 对应的提示语查看assets/xiri.txt
	 */
	private static final String COMMAND_2 = "command2";
	/**
	 * 对应的提示语查看assets/xiri.txt
	 */
	private static final String COMMAND_3 = "command3";
	/**
	 * 关闭语音提示界面
	 */
	private static final int MSG_CLOSE = 1001;
	/**
	 * 关闭语音提示界面后进行其他操作
	 */
	private static final int MSG_CLOSE_OTHER_OPERA = 1002;
	private SpeakerHandler handler;
	private static final String TAG = "TargetChat";
	
	@Override
	public void control(Context context, Intent intent) {
		String command = intent.getStringExtra(COMMAND);
		AppSpeaker speaker = new AppSpeaker(context);
		handler = new SpeakerHandler(context, speaker);
		Log.d(TAG, command);
		speaker.begin(intent);
		if (command.equals(COMMAND_1)) {
			speaker.showText(context.getString(R.string.speak_command_1), true);
			handler.sendEmptyMessageDelayed(MSG_CLOSE, speaker.getSpeakTime());
		} else if (command.equals(COMMAND_2)) {
			speaker.showText(context.getString(R.string.speak_command_2), true);
			handler.sendEmptyMessageDelayed(MSG_CLOSE_OTHER_OPERA, speaker.getSpeakTime());
		} else if (command.equals(COMMAND_3)) {
			speaker.showText(context.getString(R.string.speak_command_3), true);
			handler.sendEmptyMessageDelayed(MSG_CLOSE, speaker.getSpeakTime());
		}
	}
	
	static class SpeakerHandler extends Handler{
		private WeakReference<Context> contextRef;
		private WeakReference<AppSpeaker> speakerRef;
		public SpeakerHandler(Context context, AppSpeaker speaker) {
			this.contextRef = new WeakReference<Context>(context);
			this.speakerRef = new WeakReference<AppSpeaker>(speaker);
		}

		public void handleMessage(Message msg) {
			Context context = contextRef.get();
			AppSpeaker speaker = speakerRef.get();
			if (context == null || speaker == null) {
				return;
			}
			switch (msg.what) {
			case MSG_CLOSE:
				speaker.close();
				break;
			case MSG_CLOSE_OTHER_OPERA:
				speaker.close();
				Intent intent = new Intent("com.hiveview.bluelight.ACTION_VIDEO_DETAILS");
				intent.putExtra("videoset_id", 1);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.sendBroadcast(intent);
				break;
			}
		};
	};

}
