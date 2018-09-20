package com.hiveview.tv.utils;

import java.util.ArrayList;

import com.hiveview.tv.service.entity.PlayerRecordEntity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class VideoLiveVipUtils {

	private static final String TAG = "VideoLiveVipUtils";
	public static final Uri URI_VIDEO_ONLIVE = Uri.parse("content://HiveViewCloudVideLiveAuthorities/channelrecord");
	
	private static final String ID = "_id";

	private static final String LIVEURL = "liveurl";

	private static final String TV_ID = "tv_id";

	private static final String TV_NAME = "tv_name";

	private static final String TV_PYNAME = "tv_pyname";

	private static final String TVLOGO = "tvlogo";

	public static final String TABLE_NAME = "channelrecord";
	
	public static final String CP = "cp";
	
	public static final String IS_VIP = "isVip";
	
	public static final String CP_ID = "cpId";
	
	public static final String TIMESTAMP = "timestamp";
	
	public static ArrayList<PlayerRecordEntity> getVideoLiveRecord(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_VIDEO_ONLIVE, null, null, null, null);
		ArrayList<PlayerRecordEntity> arrayList = new ArrayList<PlayerRecordEntity>();
		PlayerRecordEntity entity = null;

		if (null == cursor) {
			Log.e(TAG,TABLE_NAME + " query unknown error");
			return arrayList;
		}

		if (cursor.getCount() == 0) {
			Log.d(TAG, TABLE_NAME+ " query count is 0");
		}

		while (cursor.moveToNext()) {
			entity = new PlayerRecordEntity();
			entity.setSource(3);
			entity.setLiveurl(cursor.getString(cursor.getColumnIndex(LIVEURL)));
			entity.setAlbumId(cursor.getInt(cursor.getColumnIndex(TV_ID))+"");
			entity.setName(cursor.getString(cursor.getColumnIndex(TV_NAME)));
			entity.setAlbumName(cursor.getString(cursor.getColumnIndex(TV_PYNAME)));
			entity.setImage(cursor.getString(cursor.getColumnIndex(TVLOGO)));
			entity.setCp(cursor.getInt(cursor.getColumnIndex(CP)));
			int vip = cursor.getInt(cursor.getColumnIndex(IS_VIP));
			entity.setIsVip(vip);
			entity.setShowDate(cursor.getLong(cursor.getColumnIndex(TIMESTAMP)));
			entity.setCpId(cursor.getInt(cursor.getColumnIndex(CP_ID)));//新加 1017
			arrayList.add(entity);
		}

		cursor.close();

		return arrayList;
	}

	public static void DeleteVideoLiveRecord(Context context) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_VIDEO_ONLIVE, null, null);
	}
}
