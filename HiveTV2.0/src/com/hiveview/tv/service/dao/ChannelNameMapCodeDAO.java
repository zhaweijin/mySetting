package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.ChannelNameMapCodeEntity;

public class ChannelNameMapCodeDAO extends BaseDAO {

	private Context context;

	/**
	 * 电视台名称字段
	 */
	public static final String CHANNEL_NAME = "channel_name";
	/**
	 * 电视台Code
	 */
	public static final String CHANNEL_CODE = "channel_code";
	/**
	 * 电视台logo
	 */
	public static final String CHANNEL_LOGO = "channel_logo";

	public ChannelNameMapCodeDAO(Context context) {
		this.context = context;
	}

	@Override
	public String createTableString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("create table if not exists  ");
		buffer.append(TABLE_CHANNEL_NAME_MAP_CODE);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("channel_name text,");
		buffer.append("channel_code text,");
		buffer.append("channel_logo text");
		buffer.append(");");
		return buffer.toString();
	}

	@Override
	public String dropTable() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_CHANNEL_NAME_MAP_CODE);
		return buffer.toString();
	}

	@Override
	public void insert(ArrayList<? extends HiveBaseEntity> arrayList) {

		if (null == arrayList) {
			return;
		}

		int count = arrayList.size();
		for (int i = 0; i < count; i++) {
			insert(arrayList.get(i));
		}
	}

	@Override
	public <T extends HiveBaseEntity> void insert(T obj) {
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		ChannelNameMapCodeEntity entity = (ChannelNameMapCodeEntity) obj;

		values.put(CHANNEL_NAME, entity.getChannelName());
		values.put(CHANNEL_CODE, entity.getChannelCode());
		values.put(CHANNEL_LOGO, entity.getChannelLogo());
		contentResolver.insert(URI_CHANNEL_NAME_MAP_CODE, values);
	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_CHANNEL_NAME_MAP_CODE, where, selectionArgs);
	}

	@Override
	public ArrayList<? extends HiveBaseEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_CHANNEL_NAME_MAP_CODE, selections, where, selectionArgs, sortOrder);
		ArrayList<ChannelNameMapCodeEntity> arrayList = new ArrayList<ChannelNameMapCodeEntity>();
		ChannelNameMapCodeEntity entity = null;

		if (null == cursor)
			return arrayList;

		if (cursor.getCount() == 0) {
			cursor.close();
			return arrayList;
		}

		int count = cursor.getCount();

		if (count > AppConstant.NO_0)
			cursor.moveToFirst();

		do {

			entity = new ChannelNameMapCodeEntity();
			entity.setChannelName(cursor.getString(cursor.getColumnIndex(CHANNEL_NAME)));
			entity.setChannelCode(cursor.getString(cursor.getColumnIndex(CHANNEL_CODE)));
			entity.setChannelLogo(cursor.getString(cursor.getColumnIndex(CHANNEL_LOGO)));

			arrayList.add(entity);

		} while (cursor.moveToNext());

		cursor.close();

		return arrayList;
	}

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}

}
