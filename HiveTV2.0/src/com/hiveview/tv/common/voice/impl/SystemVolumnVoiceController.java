package com.hiveview.tv.common.voice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;

import com.hiveview.tv.R;
import com.hiveview.tv.common.voice.AppSpeaker;
import com.hiveview.tv.common.voice.IVoiceController;
/**
 * 控制系统音量
 * @author haozening
 *
 */
public class SystemVolumnVoiceController implements IVoiceController {


	private static String[] volumnCommands = {"voice_increase","voice_decrease","voice_close","voice_open"};
	private static List<String> volumnCommandsList = new ArrayList<String>(Arrays.asList(volumnCommands));
	
	@Override
	public void control(final Context context, Intent intent) {
		final AppSpeaker speaker = new AppSpeaker(context);
		speaker.begin(intent);
		
		AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int currentVolumn = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
		int maxVolumn = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		String command = intent.getStringExtra(COMMAND);
		if (null != command && command.equals("voice_increase")) {
			speaker.showText(context.getString(R.string.voice_increase), true);
			if (currentVolumn >= maxVolumn) {
				currentVolumn = maxVolumn;
			} else {
				currentVolumn++;
			}
			manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
		}
		if (null != command && command.equals("voice_decrease")) {
			speaker.showText(context.getString(R.string.voice_decrease), true);
			if (currentVolumn <= 0) {
				currentVolumn = 0;
			} else {
				currentVolumn--;
			}
			manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
		}
		if (null != command && command.equals("voice_close")) {
			speaker.showText(context.getString(R.string.voice_close), true);
			// 重新设置下声音
			manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
			manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
		if (null != command && command.equals("voice_open")) {
			speaker.showText(context.getString(R.string.voice_open), true);
			manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
		}
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				speaker.close();
			}
		}, speaker.getSpeakTime());
	}
	
	/**
	 * 是否是控制声音的命令
	 * @param intent
	 * @return
	 */
	public static boolean isVolumnCommand(Intent intent) {
		String command = intent.getStringExtra(COMMAND);
		if (command != null && command.length() > 0) {
			if (volumnCommandsList.contains(command)) {
				return true;
			}
		}
		return false;
		
	}

}
