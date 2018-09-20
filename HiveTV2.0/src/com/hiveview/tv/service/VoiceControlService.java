/**
 * SwitchTabService.java[V 1.0.0]
 * classes : com.hiveview.tv.service.SwitchTabService
 * 李红记 Creat at 2014年4月22日 下午4:20:00
 */
package com.hiveview.tv.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.XunfeiComplexVoiceSearchListActivity;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.voice.OpenFuctionControllerSelector;
import com.hiveview.tv.common.voice.VoiceControllerSelector;
import com.hiveview.tv.common.voice.impl.ChatVoiceController;
import com.hiveview.tv.common.voice.impl.FunctionVoiceController;
import com.hiveview.tv.common.voice.impl.OpenFunctionVoiceController;
import com.hiveview.tv.common.voice.impl.PlayerVoiceController;
import com.hiveview.tv.common.voice.impl.QiYiPlayerVoiceController;
import com.hiveview.tv.common.voice.impl.SearchVoiceController;
import com.hiveview.tv.common.voice.impl.SystemVolumnVoiceController;
import com.hiveview.tv.common.voice.search.impl.XunfeiComplexSearch;
import com.hiveview.tv.utils.CloseBlueLightUtil;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.iflytek.xiri.AppService;

/**
 * @author jia
 * 
 */
public class VoiceControlService extends AppService {
	public String TAG = "VoiceControlService";
	private IAppListener appListener = new IAppListener() {

		@Override
		public void onExecute(Intent intent) {
			if (intent == null) {
				return;
			}
			Context context = getApplication();
			VoiceControllerSelector.setController(context, intent, new ChatVoiceController());
			//是否是控制Tab的命令
			if (VoiceControllerSelector.isTabController(intent)) {
				Log.d(TAG, "是否是控制Tab的命令");
				//start author:zhangpengzhan
				CloseBlueLightUtil.closeBlueLight();
				//end
				intent.setClass(VoiceControlService.this, HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			//播放器是否展示在用户前边
			if (PlayerVoiceController.isFaceToPlayer(context)) {
				Log.d(TAG, Uri.decode(intent.toURI()));
				if (PlayerVoiceController.isPlayerControlCommand(intent)) {
					VoiceControllerSelector.setController(context, intent, new PlayerVoiceController());
				}
			} else if (QiYiPlayerVoiceController.isFaceToPlayer(context)) {
				Log.d(TAG, Uri.decode(intent.toURI()));
				if (PlayerVoiceController.isPlayerControlCommand(intent)) {
					VoiceControllerSelector.setController(context, intent, new QiYiPlayerVoiceController());
				}
			} else {
				//是否是控制声音的命令
				if (SystemVolumnVoiceController.isVolumnCommand(intent)) {
					Log.d(TAG, "是否是控制声音的命令");
					VoiceControllerSelector.setController(context, intent, new SystemVolumnVoiceController());
				}
				//是否是功能命令
				if (FunctionVoiceController.isFunctionCommand(intent)) {
					Log.d(TAG, "FunctionVoiceController");
					VoiceControllerSelector.setController(context, intent, new FunctionVoiceController());
				}
			}
			
			//打开按钮
			if (OpenFuctionControllerSelector.isFuctionController(intent)) {
				Log.d(TAG, "OpenFuctionControllerSelector...................................");
				//start author:zhangpengzhan
				CloseBlueLightUtil.closeBlueLight();
				//end
				OpenFuctionControllerSelector.setController(context,intent,new OpenFunctionVoiceController());
			}
		}
	};

	private IVideoIntentListener videoIntentListener = new IVideoIntentListener() {

		@Override
		public void onExecute(Intent intent) {
			// if (intent == null) {
			// return;
			// }
			// Context context = getApplication();
			// VoiceControllerSelector.setController(context, intent, new
			// SearchVoiceController(
			// new ComplexSearch(getApplication(),
			// ComplexVoiceSearchListActivity.class)));
		}
	};

	private IVideoSearchListener videoSearchListener = new IVideoSearchListener() {

		@Override
		public void onSearch(String arg0, String arg1) {
			//start author:zhangpengzhan
			CloseBlueLightUtil.closeBlueLight();
			//end
			Log.d(TAG, "  arg0 : " + arg0 + " arg1 : " + arg1);
			Intent intent = new Intent();
			intent.putExtra("url", arg0);
			Context context = getApplication();
			VoiceControllerSelector.setController(context, intent, new SearchVoiceController(new XunfeiComplexSearch(getApplication(),
					XunfeiComplexVoiceSearchListActivity.class)));
		}
	};

	public void onCreate() {
		super.onCreate();
		setAppListener(appListener);
		setAppStoreListener(mAppStoreListener);
		setVideoIntentListener(videoIntentListener);
		setVideoSearchListener(videoSearchListener);
	};

	@Override
	protected void onInit() {

	};
	
	/**
	 * 应用商店对接
	 */
	private IAppStoreListener mAppStoreListener = new IAppStoreListener() {
		@Override
		public void onExecute(String name) {
			// 发广播给点播应用
//			int netStatus = HiveviewApplication.getNetStatus();
//			Log.i("VoiceControlService", "mAppStoreListener......................"+name);
//			Intent intent = new Intent();
//			intent.setAction(OpenFunctionVoiceController.APP_STORE_ACTION);
//			intent.putExtra("info", name);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
//			VoiceControlService.this.startActivity(intent);
		}
	};

}
