package com.hiveview.tv.utils;

import com.hiveview.tv.common.AppScene;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

public class RemotecontrolUtils {
	private static final String TAG = "RemotecontrolUtils";
	public static void onKeyExecute(Context context,AppScene appScene, KeyEvent event) {
		AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//当前声音值
		int currentVolumn = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
		//最大声音值
		int maxVolumn = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//增大音量 
		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			if (currentVolumn >= maxVolumn) {
				currentVolumn = maxVolumn;
			} else {
				currentVolumn++;
			}
			manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
		}
		//减小音量
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (currentVolumn <= 0) {
				currentVolumn = 0;
			} else {
				currentVolumn--;
			}
			manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
		}
		//静音键
		else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_MUTE) {
			int statusFlag = (manager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) ? 1: 0; 
			//打开声音
			if(statusFlag==1){
				manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
			}
			//静音
			else{
				manager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, AudioManager.FLAG_SHOW_UI|AudioManager.FLAG_PLAY_SOUND);
				manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
		}
	}
}
