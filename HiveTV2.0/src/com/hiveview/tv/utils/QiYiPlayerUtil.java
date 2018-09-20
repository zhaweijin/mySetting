package com.hiveview.tv.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hiveview.tv.R.string;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;

public class QiYiPlayerUtil {
	private static final String TAG = "QiYiPlayerUtil";
	public static String vAlbumId;
	public static String vtvId;
	private static boolean onAuthSuccess = false;

	public static void startQiYiPlayer(Context context, String vrsTvId, String vrsAlbumId) {
		JSONObject json = new JSONObject();
		try {
			json.put("version", "1.0");
			json.put("playType", "history");
			json.put("vrsAlbumId", vrsAlbumId);
			json.put("vrsTvId", vrsTvId);
			json.put("history", "0");
			json.put("customer", "hisense");
			json.put("device", "H01-23");

			Intent intent = new Intent("com.qiyi.tvplayer.drpeng.action.ACTION_PLAYVIDEO");
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			Bundle bundle = new Bundle();
			bundle.putString("playInfo", json.toString());
			intent.putExtras(bundle);
			context.sendBroadcast(intent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param context
	 * @param film FilmNewEntity实体类
	 * @param video 剧集实体类
	 * @param isFromEpisode 播放来源(是否剧集)
	 * @param isFromPlayButton 播放来源(是否播放按钮)
	 * @param IsRecord 播放来源(是否观看记录)
	 * @param search 搜索
	 * @param isFromSearch 是否搜索按钮
	 */
	public static void startSDKPlayer(Context context,String film,String video,Boolean isFromEpisode,Boolean isFromPlayButton,Boolean IsRecord,String search,Boolean isFromSearch) {
		Intent mintent=new Intent("com.hiveview.cloudscreen.action.QIYI_PLAYER");
		mintent.putExtra("com.hiveview.tv.FilmNewEntity", film);
		mintent.putExtra("com.hiveview.tv.VideoNewEntity", video);
		mintent.putExtra("com.hiveview.tv.VideosetId", search);
		mintent.putExtra("com.hiveview.tv.isFromEpisode",isFromEpisode);
		mintent.putExtra("com.hiveview.tv.isFromPlayButton",isFromPlayButton);
		mintent.putExtra("com.hiveview.tv.IsRecord",IsRecord);
		mintent.putExtra("com.hiveview.tv.IsOnlyId",isFromSearch);
		try {
			context.startActivity(mintent);
			Log.d("SettingActivity->click_try->intent::", mintent + "");
		} catch (ActivityNotFoundException e) {
			context.sendBroadcast(mintent);
			Log.d("SettingActivity->click_catch->intent::", mintent + "");
		}	
	}

}
