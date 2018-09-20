package com.hiveview.tv.service.dao;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.FirstClassListEntity;

/**
 * DAO for table Focused
 * */
@SuppressLint("UseSparseArrays")
public class ChannelDAO extends BaseDAO {

	private Context context;

	public static final String FIRSTCLASS_ID = "firstclass_id";
	public static final String FIRSTCLASS_NAME = "firstclass_name";
	public static final String PIC = "pic";
	public static final String ICON = "icon";
	public static final String SHOW_TYPE = "show_type";

	private final static byte[] _writeLock = new byte[0];

	public ChannelDAO(Context context) {
		this.context = context;
	}

	/**
	 * String for executing SQL.
	 * 
	 * @return String , which is for creating TABLE after being executed.
	 * */
	public String createTableString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("create table if not exists  ");
		buffer.append(TABLE_CHANNEL);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("firstclass_id integer,");
		buffer.append("firstclass_name text,");
		buffer.append("icon text,");
		buffer.append("pic text,");
		buffer.append("show_type integer");
		buffer.append(");");
		Log.d(TAG, buffer.toString());
		return buffer.toString();

	}

	private String TAG = "ChannelDAO";

	/**
	 * drop table if exists
	 * 
	 * @return String , for executing ,remove table from database
	 * */

	public String dropTable() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_CHANNEL);

		return buffer.toString();

	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_CHANNEL, where, selectionArgs);
	}

	@Override
	public void insert(ArrayList<? extends HiveBaseEntity> arrayList) {
		synchronized (_writeLock) {
			int count = arrayList.size();
			for (int i = 0; i < count; i++) {
				insert(arrayList.get(i));
			}
		}

	}

	@Override
	public <T extends HiveBaseEntity> void insert(T obj) {

		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		FirstClassListEntity entity = (FirstClassListEntity) obj;

		values.put(ChannelDAO.FIRSTCLASS_ID, entity.getFirstclass_id());
		values.put(ChannelDAO.FIRSTCLASS_NAME, entity.getFirstclass_name());
		values.put(ChannelDAO.PIC, entity.getPic());
		values.put(ChannelDAO.ICON, entity.getIcon());
		values.put(ChannelDAO.SHOW_TYPE, entity.getShow_type());

		contentResolver.insert(URI_CHANNEL, values);
	}

	@Override
	public ArrayList<FirstClassListEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		synchronized (_writeLock) {

			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(URI_CHANNEL, selections, where, selectionArgs, sortOrder);
			ArrayList<FirstClassListEntity> arrayList = new ArrayList<FirstClassListEntity>();
			FirstClassListEntity entity = null;

			if (null == cursor)
				return arrayList;

			if (cursor.getCount() < AppConstant.NO_1) {

				cursor.close();
				return arrayList;

			}

			int count = cursor.getCount();

			if (count > AppConstant.NO_0)
				cursor.moveToFirst();

			do {
				entity = new FirstClassListEntity();
				entity.setFirstclass_id(cursor.getInt(cursor.getColumnIndex(ChannelDAO.FIRSTCLASS_ID)));
				entity.setFirstclass_name(cursor.getString(cursor.getColumnIndex(ChannelDAO.FIRSTCLASS_NAME)));
				entity.setPic(cursor.getString(cursor.getColumnIndex(ChannelDAO.PIC)));
				entity.setIcon(cursor.getString(cursor.getColumnIndex(ChannelDAO.ICON)));
				entity.setShow_type(cursor.getInt(cursor.getColumnIndex(ChannelDAO.SHOW_TYPE)));

				arrayList.add(entity);

			} while (cursor.moveToNext());

			cursor.close();

			return arrayList;
		}
	}

	/**
	 * 返回一个HashMap，键是视频类型，值是此ID对的视频类型的名称
	 * 
	 * @Title: ChannelDAO
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public HashMap<Integer, String> queryMap() {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_CHANNEL, null, null, null, null);
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		if (null == cursor)
			return map;

		if (cursor.getCount() < AppConstant.NO_1) {
			cursor.close();
			return map;
		}

		int count = cursor.getCount();

		if (count > AppConstant.NO_0)
			cursor.moveToFirst();

		do {
			map.put(cursor.getInt(cursor.getColumnIndex(ChannelDAO.FIRSTCLASS_ID)), cursor.getString(cursor.getColumnIndex(ChannelDAO.FIRSTCLASS_NAME)));
		} while (cursor.moveToNext());

		cursor.close();

		return map;
	}

	/**
	 * 返回一个HashMap，键是视频类型，值是此视频类型对应的响应类型
	 * 
	 * @Title: ChannelDAO
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public HashMap<Integer, Integer> queryShowTypeMap() {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_CHANNEL, null, null, null, null);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		if (null == cursor)
			return map;

		if (cursor.getCount() < AppConstant.NO_1) {
			cursor.close();
			return map;
		}

		int count = cursor.getCount();

		if (count > AppConstant.NO_0)
			cursor.moveToFirst();

		do {
			map.put(cursor.getInt(cursor.getColumnIndex(ChannelDAO.FIRSTCLASS_ID)), cursor.getInt(cursor.getColumnIndex(ChannelDAO.SHOW_TYPE)));
		} while (cursor.moveToNext());

		cursor.close();

		return map;
	}

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}

}
