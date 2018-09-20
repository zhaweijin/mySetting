package com.hiveview.tv.common.voice.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.voice.AppSpeaker;
import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.iflytek.xiri.Feedback;

/**
 * 用于打开某些系统页面的类
 * 
 * @ClassName: FunctionVoiceController
 * @Description: 打开类似设置页面的语音控制类
 * @author: yupengtong
 * @date 2014年9月12日 上午9:46:00
 * 
 */
public class FunctionVoiceController implements IVoiceController {

	/**
	 * 对应的提示语查看assets/global.xiri(待添加)
	 */

	private static final int MSG_SETTING = 1001;
	private static final int MSG_EPGINFO = 1002;

	private static final String TAG = "FunctionVoiceController";

	private static String[] functionList = { "setting","epg" };
	private static List<String> functionCommandsList = new ArrayList<String>(Arrays.asList(functionList));

	private SpeakerHandler handler;
	private static String name;
	private static String date;
	private static Context mContext;

	@Override
	public void control(Context context, Intent intent) {
		mContext=context;
		String command = intent.getStringExtra(COMMAND);
		AppSpeaker speaker = new AppSpeaker(context);
		handler = new SpeakerHandler(context, speaker);
		Log.d(TAG, command);
		speaker.begin(intent);
		if (command.equals(functionList[0])) {
			// 系统设置
			speaker.feedback(context.getString(R.string.open_setting), Feedback.DIALOG);
			handler.sendEmptyMessageDelayed(MSG_SETTING, speaker.getSpeakTime());
		}else 	if (command.equals(functionList[1])) {
			// 节目单
			if(BaseActivity.activityStack.lastElement().getLocalClassName().equals("activity.VoiceOnliveEPGActivity")){
				BaseActivity.activityStack.lastElement().finish();
			}
			name=intent.getStringExtra("channelname");
			date=intent.getStringExtra("starttime");
			speaker.feedback(name+"的节目单", Feedback.DIALOG);
			handler.sendEmptyMessageDelayed(MSG_EPGINFO, speaker.getSpeakTime());
		}

	}

	static class SpeakerHandler extends Handler {
		private WeakReference<Context> contextRef;
		private WeakReference<AppSpeaker> speakerRef;

		// private Context contextRef;
		// private AppSpeaker speakerRef;

		public SpeakerHandler(Context context, AppSpeaker speaker) {
			this.contextRef = new WeakReference<Context>(context);
			this.speakerRef = new WeakReference<AppSpeaker>(speaker);
			// this.contextRef =context;
			// this.speakerRef = speaker;
		}

		public void handleMessage(Message msg) {
			Context context = contextRef.get();
			AppSpeaker speaker = speakerRef.get();
			if (context == null || speaker == null) {
				return;
			}
			switch (msg.what) {

			case MSG_SETTING:
				speaker.close();
				Log.d(TAG, "startActivity!!!!!");
				Intent intent = new Intent();
				intent.setAction("com.hiveview.settings.ACTION_SETTING");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				int netStatus = HiveviewApplication.getNetStatus();
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				context.startActivity(intent);
				
				break;
			case MSG_EPGINFO:
				speaker.close();
				SharedPreferences preference = mContext.getSharedPreferences("DomyBoxPreference", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = preference.edit();
				if (name != null) {
					Log.i(TAG, "MSG_EPGINFO-->channelName:" + name);
					editor.putString(SwitchChannelUtils.KEY_VOICE_NAME, name);
				}
				if (date != null&&date.length()!=0) {
					Log.i(TAG, "MSG_EPGINFO-->date:" + date.length());
					editor.putString(SwitchChannelUtils.KEY_VOICE_DATE, date);
					
				}else{
					editor.putString(SwitchChannelUtils.KEY_VOICE_DATE, null);	
				}
				editor.commit();
				Intent intent1 = new Intent();
				intent1.setAction("com.hiveview.tv.voiceEpgInfo");
				intent1.addCategory(Intent.CATEGORY_DEFAULT);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				int netStatus1 = HiveviewApplication.getNetStatus();
				intent1.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus1);
				/* end by ZhaiJianfeng */
				context.startActivity(intent1);
				
				break;
			}
		};
	};
	
	
	/**
	 * 是否是功能命令
	 * 
	 * @param intent
	 * @return
	 */
	public static boolean isFunctionCommand(Intent intent) {
		String command = intent.getStringExtra(COMMAND);
		if (command != null && command.length() > 0) {
			if (functionCommandsList.contains(command)) {
				Log.d(TAG, "isFunctionCommand!!!!!");
				return true;
			}
		}
		return false;

	}

}
