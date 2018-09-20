package com.hiveview.tv.common.voice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 语音控制器
 * 
 * @author haozening
 * 
 */
public class VoiceControllerSelector {

	private static final String TAG = "VoiceController";
	private static String[] switchTabCommands = { "game", "tv","blue", "recom", "movie", "app","edu" };

	private static List<String> switchTabCommandsList = new ArrayList<String>(Arrays.asList(switchTabCommands));

	/**
	 * 控制目标
	 * 
	 * @param context
	 * @param intent
	 * @param controller
	 */
	public static void setController(Context context, Intent intent, IVoiceController controller) {
		controller.control(context, intent);
	}

	/**
	 * 是否是控制Tab的命令
	 * 
	 * @param intent
	 * @return
	 */
	public static boolean isTabController(Intent intent) {
		String action = intent.getStringExtra(IVoiceController.COMMAND);
		Log.d(TAG, "action-========" + action);
		if (action != null && action.length() > 0) {
			if (switchTabCommandsList.contains(action)) {
				return true;
			}
		}
		return false;
	}
}
