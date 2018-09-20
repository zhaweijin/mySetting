package com.hiveview.tv.utils;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.PlayerRecordEntity;

public class BlueLightUtils {
	private static final String TAG = "BlueLightUtils";
	public static final Uri URI_BLUELIGHT_COLLECT = Uri
			.parse("content://BlueLightAuthorities/TABLE_BLUELIGHT_FAVORITE");
	public static final Uri URI_BLUELIGHT_HISTORY = Uri
			.parse("content://BlueLightAuthorities/TABLE_BLUELIGHT_HISTORY");

	/**
	 * 
	 * @Title getFavoriteList
	 * @author xieyi
	 * @Description 获得极清2.0的所有收藏数据
	 * @param context
	 * @return
	 */
	public static List<FilmNewEntity> getFavoriteList(Context context) {
		if (null == context)
			return null;
		List<FilmNewEntity> blueLight = new ArrayList<FilmNewEntity>();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_BLUELIGHT_COLLECT, null, null, null,
				"showDate desc");
		// edit by haozening 增加null判断
		if (null != cursor) {
			while (cursor.moveToNext()) {
				FilmNewEntity entity = new FilmNewEntity();
					entity.setSource(1);
					entity.setCp(cursor.getInt(cursor.getColumnIndex("cpId"))
							+ "");
					entity.setName(cursor.getString(cursor
							.getColumnIndex("showName")));
					entity.setId(Integer.parseInt(cursor.getString(cursor
							.getColumnIndex("albumId"))));
					entity.setPosterUrl(cursor.getString(cursor
							.getColumnIndex("imguri")));
					blueLight.add(entity);
			}
			cursor.close();
		}
		return blueLight;
	}

	/**
	 * 
	 * @Title getHistoryList
	 * @author xieyi
	 * @Description 获得极清的播放历史数据
	 * @param context
	 * @param startTime
	 *            查询起始日期
	 * @param endTime
	 *            查询结束日期
	 * @return
	 */
	public static List<PlayerRecordEntity> getHistoryList(Context context) {
		List<PlayerRecordEntity> blueLight = new ArrayList<PlayerRecordEntity>();
		if (null == context)
			return blueLight;
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_BLUELIGHT_HISTORY, null, null, null,
				"recordTime desc");
		// edit by ZeningHao start
		if (null == cursor) {
			Log.d(TAG, "history list empty cause by cursor == null");
			return blueLight;
		}
		// edit by ZeningHao end
		while (cursor.moveToNext()) {

			PlayerRecordEntity entity = new PlayerRecordEntity();
			entity.setSource(1);
			entity.setVideoset_type(cursor.getInt(cursor
					.getColumnIndex("showType")));
			entity.setCp(cursor.getInt(cursor.getColumnIndex("cpId")));
			entity.setAlbumId(cursor.getString(cursor.getColumnIndex("albumId")));
			entity.setImage(cursor.getString(cursor.getColumnIndex("picrl")));
			entity.setName(cursor.getString(cursor.getColumnIndex("albumName")));
			entity.setPalyerDate(Long.parseLong(cursor.getString(cursor
					.getColumnIndex("recordTime"))) / 1000);
			entity.setSurplusTime(String.valueOf(cursor.getInt(cursor
					.getColumnIndex("lastPlayTime"))));
			entity.setAlbums(cursor.getString(cursor
					.getColumnIndex("lastPlayVideoIndex")));
			String during;
			try {
				during = String.valueOf(cursor.getInt(cursor
						.getColumnIndex("duration")));
				entity.setDuration(during);
			} catch (Exception e) {
				e.printStackTrace();
				Log.v(TAG, TAG + "Exception");
				during = null;
				// continue;
			}
			entity.setDuration(during);
			blueLight.add(entity);

		}
		cursor.close();
		return blueLight;
	}

	public static boolean deleteHistory(Context context, FilmNewEntity entity) {
		if (null == context)
			return false;
		ContentResolver resolver = context.getContentResolver();
		int columnResult = resolver.delete(URI_BLUELIGHT_HISTORY,
				"cpId = ? and albumId = ?", new String[] { entity.getCp() + "",
						entity.getId() + "" });
		if (columnResult > 0)
			return true;
		return false;
	}

	public static boolean deleteAllHistory(Context context) {
		if (null == context)
			return false;
		ContentResolver resolver = context.getContentResolver();
		int columnResult = resolver.delete(URI_BLUELIGHT_HISTORY, null, null);
		if (columnResult > 0)
			return true;
		return false;
	}

	public static boolean deleteCOLLECT(Context context, int cpId, int albumId) {
		if (null == context)
			return false;
		ContentResolver resolver = context.getContentResolver();
		int columnResult = resolver.delete(URI_BLUELIGHT_COLLECT,
				"cpId = ? and albumId = ?", new String[] { cpId + "",
						albumId + "" });
		if (columnResult > 0)
			return true;
		return false;
	}

	public static boolean deleteAllCollect(Context context) {
		if (null == context)
			return false;
		ContentResolver resolver = context.getContentResolver();
		int columnResult = resolver.delete(URI_BLUELIGHT_COLLECT, null, null);
		if (columnResult > 0)
			return true;
		return false;
	}
}
