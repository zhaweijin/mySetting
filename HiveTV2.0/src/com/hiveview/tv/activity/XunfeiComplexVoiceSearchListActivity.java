package com.hiveview.tv.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.TotalListFilmEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.utils.PlayerParamsUtils;

public class XunfeiComplexVoiceSearchListActivity extends BaseVoiceSearchNewListActivity {

	protected static final String TAG = "XunfeiComplexVoiceSearchListActivity";


	@Override
	protected void search(Intent intent, final int pageSize, final int connectNum, int filter) {
		String url = intent.getStringExtra("data_url");
		Log.d(TAG, "url : " + url);
		final ArrayList<TotalListFilmNewEntity> list = new HiveTVService().getSearchList(url, pageSize + "", connectNum + "");
		TotalListFilmNewEntity totalListFilmEntity = list.get(0);
		ArrayList<FilmNewEntity> newResult = new ArrayList<FilmNewEntity>();
		if (null != totalListFilmEntity) {
			for (FilmNewEntity entity : totalListFilmEntity.getFilms()) {
				try {
					ArrayList<FilmNewEntity> temp = new HiveTVService().getFilmDetail(XunfeiComplexVoiceSearchListActivity.this, entity.getId());
					newResult.addAll(temp);
				}catch(ServiceException e) {
					e.printStackTrace();
					Log.d(TAG, e.getErrorCode() + "  " + e.getMessage());
					continue;
				}
			}
			Log.d(TAG, newResult.size() + "==" + newResult.toString());
		}
		totalListFilmEntity.setFilms(newResult);
		totalListFilmEntity.setRecCount(newResult.size());
		setList(list);
	}

	@Override
	protected void onItemClick(FilmNewEntity entity) {
		try {
			String action = ContentInvoker.getInstance().getContentAction(entity.getCid());
			if (entity.getCid() == AppConstant.VIDEO_TYPE_BLUE) {
				Intent intent = new Intent();
				intent.setAction(action);
				intent.putExtra("videoset_id", entity.getId());
				sendBroadcast(intent);
			} else if (entity.getCid() == AppConstant.VIDEO_TYPE_CLIPS) {
				Intent intent = new Intent();
				intent.setAction(action);
				Bundle bundle = new Bundle();
				bundle.putString("play_info", createClipsEntityJson(entity));
				intent.putExtras(bundle);
				sendBroadcast(intent);
			} else if (null == action) {
				PlayerParamsUtils.getVideoPlayParams(entity.getId(), entity.getCid(), "",getApplicationContext(), null);
			} else {
				Intent intent = new Intent();
				intent.setAction(action);
				intent.putExtra("id", entity.getId());
				startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private String createClipsEntityJson(FilmNewEntity entity) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("videoset_id", entity.getId());
			jo.put("videoset_name", entity.getName());
			jo.put("videoset_type", entity.getCid());
			jo.put("videoset_img", entity.getPosterUrl());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	


	
}
