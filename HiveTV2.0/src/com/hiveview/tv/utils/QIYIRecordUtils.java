package com.hiveview.tv.utils;

import java.util.ArrayList;
import java.util.List;

import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.PlayerRecordEntity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils.StringSplitter;
import android.util.Log;

public class QIYIRecordUtils {

	/**
	 * 获取观看记录
	 */
	public static final Uri PLAYER_RECORD = Uri.parse("content://HiveViewCloudPlayerAuthorities/PLAYER_RECORD");
	/**
	 * 清空观看记录
	 */
	public static final Uri RECORD_CONTROLLER = Uri.parse("content://HiveViewCloudPlayerAuthorities/RecordController");

	private static final String TAG = "QIYIRecordUtils";

	/**
	 * 
	 * @param context
	 * @return 获取爱奇艺观看记录
	 */
	public static List<PlayerRecordEntity> getHistoryList(Context context, String id) {
		List<PlayerRecordEntity> QiyiPlayer = new ArrayList<PlayerRecordEntity>();
		if (null == context)
			return QiyiPlayer;
		ContentResolver resolver = context.getContentResolver();
		StringBuilder sb = new StringBuilder();
		String selection = null;
		if (!StringUtils.isEmpty(id)) {
			selection = sb.append("albumId").append("=").append(id).toString();
		}
		Cursor cursor = resolver.query(PLAYER_RECORD, null, selection, null, null);
		// edit by ZeningHao start
		if (null == cursor) {
			Log.d(TAG, "history list empty cause by cursor == null");
			return QiyiPlayer;
		}
		// edit by ZeningHao end
		while (cursor.moveToNext()) {

			PlayerRecordEntity entity = new PlayerRecordEntity();
			FilmNewEntity filmEntity = new FilmNewEntity();
			entity.setSource(0);
			entity.setAlbumId(cursor.getString(cursor.getColumnIndex("albumId")));
			entity.setName(cursor.getString(cursor.getColumnIndex("name")));
			entity.setAlbumName(cursor.getString(cursor.getColumnIndex("albumName")));
			entity.setAlbumPhotoName(cursor.getString(cursor.getColumnIndex("albumPhotoName")));
			entity.setImage(cursor.getString(cursor.getColumnIndex("image")));
			entity.setStartTime(cursor.getInt(cursor.getColumnIndex("startTime")));
			entity.setOrientation(cursor.getString(cursor.getColumnIndex("orientation")));
			entity.setPalyerDate(cursor.getLong(cursor.getColumnIndex("palyerDate")));
			entity.setSurplusTime(cursor.getString(cursor.getColumnIndex("surplusTime")));
			entity.setAlbums(cursor.getString(cursor.getColumnIndex("albums")));
			entity.setCurrentEpisode(cursor.getString(cursor.getColumnIndex("currentEpisode")));
			entity.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
			entity.setDescription(cursor.getString(cursor.getColumnIndex("description")));

			filmEntity.setCid(cursor.getInt(cursor.getColumnIndex("cid")));
			filmEntity.setCname(cursor.getString(cursor.getColumnIndex("cname")));
			filmEntity.setCurrCount(cursor.getInt(cursor.getColumnIndex("currCount")));
			filmEntity.setFocusName(cursor.getString(cursor.getColumnIndex("focusName")));
			filmEntity.setEqLen(cursor.getInt(cursor.getColumnIndex("eqLen")));
			filmEntity.setDesc(cursor.getString(cursor.getColumnIndex("desc")));
			filmEntity.setDirectors(cursor.getString(cursor.getColumnIndex("directors")));
			filmEntity.setId(cursor.getInt(cursor.getColumnIndex("id")));
			filmEntity.setInitIssueTime(cursor.getString(cursor.getColumnIndex("initIssueTime")));
			filmEntity.setMainActors(cursor.getString(cursor.getColumnIndex("mainActors")));
			filmEntity.setPlayCount(cursor.getInt(cursor.getColumnIndex("playCount")));
			filmEntity.setPosterUrl((cursor.getString(cursor.getColumnIndex("posterUrl"))));
			filmEntity.setScore(cursor.getInt(cursor.getColumnIndex("score")));
			filmEntity.setSeriesType(cursor.getInt(cursor.getColumnIndex("seriesType")));
			filmEntity.setStreams(cursor.getString(cursor.getColumnIndex("streams")));
			filmEntity.setTagNames(cursor.getString(cursor.getColumnIndex("tagNames")));
			filmEntity.setTime(cursor.getString(cursor.getColumnIndex("time")));
			filmEntity.setTotal(cursor.getInt(cursor.getColumnIndex("total")));
			filmEntity.setType3d(cursor.getInt(cursor.getColumnIndex("type3d")));
			filmEntity.setCp(cursor.getString(cursor.getColumnIndex("cp")));
			filmEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
			entity.setEntity(filmEntity);
			QiyiPlayer.add(entity);

		}
		cursor.close();
		return QiyiPlayer;
	}

	/**
	 * 清空观看记录
	 * 
	 * @param context
	 */
	public static void deleteAllHistory(Context context) {
		ContentResolver resolver = context.getContentResolver();
		resolver.call(RECORD_CONTROLLER, "deleteAll", null, null);
	}
}
