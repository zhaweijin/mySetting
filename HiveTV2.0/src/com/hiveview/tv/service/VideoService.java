package com.hiveview.tv.service;

import android.app.IntentService;
import android.content.Intent;

import com.hiveview.tv.utils.PlayerParamsUtils;


/**
 * 此服务主要是 通过launcher来启动
 * 		1.数据（图片）的预加载 完成释放掉
 * 
 * @author lidm
 *
 */
public class VideoService extends IntentService {
	
	private static final String TAG = VideoService.class.getSimpleName();

	public VideoService() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {	
		int id = intent.getIntExtra("id", -1);
		int type = intent.getIntExtra("type", -1);
		PlayerParamsUtils.getVideoPlayParams(id, type,"", getApplicationContext(),null);
	}
}
