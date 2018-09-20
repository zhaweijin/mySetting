package com.hiveview.tv.common.voice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.AppScene.PlayerBroadCastContants;
import com.hiveview.tv.common.AppScene.PredefineSemantic;
import com.hiveview.tv.common.voice.AppSpeaker;
import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.utils.StringUtils;

/**
 * 控制播放器
 * @author haozening
 *
 */
public class PlayerVoiceController implements IVoiceController {

	private static final String TAG = "PlayerVoiceController";
	private static String[] playerCommands = {"voice_increase","voice_decrease","voice_close",
		"voice_open","PLAY", "PAUSE", "RESUME", "RESTART", "SEEK", "FORWARD", "BACKWARD", "EXIT"};
	private static List<String> playerCommandsList = new ArrayList<String>(Arrays.asList(playerCommands));

	@Override
	public void control(final Context context, Intent intent) {
		Log.d(TAG, "controlPlayer");
		final AppSpeaker speaker = new AppSpeaker(context);
		speaker.begin(intent);
		String command = intent.getStringExtra(COMMAND);
		Log.d(TAG, Uri.decode(intent.toURI()));
		String action = intent.getStringExtra(ACTION);
		int offset = intent.getIntExtra(PredefineSemantic.PARAM_PLAY_OFFSET, -1);
		int position = intent.getIntExtra(PredefineSemantic.PARAM_PLAY_SEEK_POSITION, -1);
		String offsetSpeakStr = StringUtils.getMinuteSecondString(StringUtils.second2Minute(60, offset));
		String positionSpeakStr = StringUtils.getMinuteSecondString(StringUtils.second2Minute(60, position));
		Log.d(TAG, Uri.decode(intent.toURI()));
		if (command != null) {
			Log.d(TAG, command);
		}
		if (action != null) {
			Log.d(TAG, action);
		}
		if (command != null && command.equals("voice_increase")) {
			speaker.showText(context.getString(R.string.voice_increase), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_VOLUMN_INCREASE);
			context.sendBroadcast(broad);
		}
		if (command != null && command.equals("voice_decrease")) {
			speaker.showText(context.getString(R.string.voice_decrease), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_VOLUMN_DECREASE);
			context.sendBroadcast(broad);
		}
		if (command != null && command.equals("voice_close")) {
			speaker.showText(context.getString(R.string.voice_close), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_VOLUMN_CLOSE);
			context.sendBroadcast(broad);
		}
		if (command != null && command.equals("voice_open")) {
			speaker.showText(context.getString(R.string.voice_open), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_VOLUMN_OPEN);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_PLAY)) {
			speaker.showText(context.getString(R.string.player_play), true);
			Log.d(TAG, "action.equals(PredefineSemantic.ACTION_PLAY)");
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_PLAY);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_PAUSE)) {
			speaker.showText(context.getString(R.string.player_pause), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_PAUSE);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_RESUME)) {
			speaker.showText(context.getString(R.string.player_play), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_PLAY);
			context.sendBroadcast(broad);
			
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_RESTART)) {
			speaker.showText(context.getString(R.string.player_restart), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_RESTART);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_EXIT)) {
			speaker.showText(context.getString(R.string.player_exit), true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_EXIT);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_SEEK)) {
			speaker.showText(context.getString(R.string.player_seekto) + positionSpeakStr, true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_SEEK_TO);
			broad.putExtra(PlayerBroadCastContants.PLAYER_TIME, position);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_FORWARD)) {
			speaker.showText(context.getString(R.string.player_fastforward) + offsetSpeakStr, true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_FASTFORWARD_TIME);
			broad.putExtra(PlayerBroadCastContants.PLAYER_TIME, offset);
			context.sendBroadcast(broad);
		}
		if (null != action && action.equals(PredefineSemantic.ACTION_BACKWARD)) {
			speaker.showText(context.getString(R.string.player_fastbackward) + offsetSpeakStr, true);
			Intent broad = new Intent(PlayerBroadCastContants.ACTION_VOICE_PLAYER);
			broad.putExtra(PlayerBroadCastContants.PLAYER_CONTROL, PlayerBroadCastContants.PLAYER_FASTBACKWARD_TIME);
			broad.putExtra(PlayerBroadCastContants.PLAYER_TIME, offset);
			context.sendBroadcast(broad);
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				speaker.close();
			}
		}, speaker.getSpeakTime());
	}
	
	/**
	 * 播放器是否展示在用户前边
	 * @param context
	 * @return
	 */
	public static boolean isFaceToPlayer(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfos = manager.getRunningTasks(1);
		String className = taskInfos.get(0).topActivity.getClassName();
		if (className.equalsIgnoreCase(AppScene.BLUE_LIGHT_PLAYER)) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是控制播放器的命令
	 * @param intent
	 * @return
	 */
	public static boolean isPlayerControlCommand(Intent intent) {
		String command = intent.getStringExtra(COMMAND);
		String preDefAction = intent.getStringExtra(ACTION);
		if (preDefAction != null && preDefAction.length() > 0) {
			if (playerCommandsList.contains(preDefAction)) {
				return true;
			}
		}
		if (command != null && command.length() > 0) {
			if (playerCommandsList.contains(command)) {
				return true;
			}
		}
		return false;
		
	}

}
