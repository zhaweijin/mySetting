package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.service.entity.StringEntity;

public class TvSetTopDAO extends BaseDAO {

	public static final String CHANNEL_CODE = "channel_code";
	public static final String CHANNEL_ID = "_id";

	private Context context;

	public TvSetTopDAO(Context context) {
		this.context = context;
	}

	@Override
	public String createTableString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("create table if not exists  ");
		buffer.append(TABLE_LIVE_CHANNEL_TOP);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("channel_code text");
		buffer.append(");");
		Log.d(TAG,buffer.toString());
		return buffer.toString();
	}
	private String TAG = "TvSetTopDAO";
	@Override
	public String dropTable() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_LIVE_CHANNEL_TOP);

		return buffer.toString();
	}

	@Override
	public void insert(ArrayList<? extends HiveBaseEntity> arrayList) {

	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public <T extends HiveBaseEntity> void insert(T obj) {
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		StringEntity entity = (StringEntity) obj;

		// 检查这个电视台用户是否置顶，如果置顶了，就不在插入了，预防用户多次点击的情况
		ArrayList<StringEntity> existList = (ArrayList<StringEntity>) query(null, "channel_code = ?", new String[] { entity.getStrParams() }, null);
		if (existList.size() > 0) {
			return;
		}

		values.put(CHANNEL_CODE, entity.getStrParams());

		// 插入新的收藏记录
		contentResolver.insert(URI_LIVE_CHANNEL_TOP, values);

		// 保持用户置顶的最新的3条
		ArrayList<StringEntity> list = ((ArrayList<StringEntity>) query(null, null, null, "_id asc"));
		if (list.size() > 3) {
			delete("_id = ?", new String[] { list.get(0).getIntParams() + "" });
		}
	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_LIVE_CHANNEL_TOP, where, selectionArgs);
	}

	@Override
	public ArrayList<? extends HiveBaseEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_LIVE_CHANNEL_TOP, selections, where, selectionArgs, sortOrder);
		ArrayList<StringEntity> list = new ArrayList<StringEntity>();

		while (cursor.moveToNext()) {
			StringEntity entity = new StringEntity();
			entity.setStrParams(cursor.getString(cursor.getColumnIndex(CHANNEL_CODE)));
			entity.setIntParams(cursor.getInt(cursor.getColumnIndex(CHANNEL_ID)));
			list.add(entity);
		}

		cursor.close();

		return list;
	}

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}

}
