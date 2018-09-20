package com.hiveview.tv.common.voice;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.common.AppScene;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;

/**
 * 打开其他APK的场景，比如播放影片，使用的是蓝光的播放器，<br>
 * 这个时候场景是蓝光，但是场景实例却在Launcher，这时候需<br>
 * 要用此类来控制。
 * @author haozening
 *
 */
public class OtherApkSenceUtils implements ISceneListener {
	
	private static final String TAG = "OtherApkSenceUtils";
	private BaseActivity activity;
	private Scene scene;
	private Context context;
	private String json;
	
	public OtherApkSenceUtils(Context context, String json) {
		this.context = context;
		this.json = json;
		if (context instanceof BaseActivity) {
			Log.d(TAG, "context instanceof BaseActivity");
			activity = (BaseActivity) context;
			AppScene appScene = activity.getScene();
			appScene.release();
		}
		scene = new Scene(context);
	}
	
	public void initScene() {
		if (scene != null) {
			scene.init(this);
		}
	}
	
	public void release() {
		if (scene != null) {
			scene.release();
		}
	}
	

	@Override
	public void onExecute(Intent intent) {
//		VoiceController.controlPlayer(context, intent);
	}


	@Override
	public String onQuery() {
		return json;
	}
	
}
