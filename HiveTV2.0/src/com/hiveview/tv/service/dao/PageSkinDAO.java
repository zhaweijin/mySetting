package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.SkinEntity;

/**
 * DAO for table Focused
 * */
public class PageSkinDAO extends BaseDAO {

	private Context context;

	public static final String RECOM_TYPE = "recom_type";
	public static final String IMG_URL_INSIDE = "img_url_inside";
	public static final String IMG_URL_OUTSIDE = "img_url_outside";
	private final static byte[] _writeLock = new byte[0];

	public PageSkinDAO(Context context) {
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
		buffer.append(TABLE_SKIN);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("recom_type integer,");
		buffer.append("img_url_inside text,");
		buffer.append("img_url_outside text");
		buffer.append(");");
		Log.d(TAG, buffer.toString());
		return buffer.toString();

	}

	/**
	 * log 标签
	 * 
	 * @Fields TAG:TODO
	 */
	private String TAG = "PageSkinDAO";

	/**
	 * drop table if exists
	 * 
	 * @return String , for executing ,remove table from database
	 * */

	public String dropTable() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_SKIN);

		return buffer.toString();

	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_SKIN, where, selectionArgs);
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
		SkinEntity entity = (SkinEntity) obj;

		values.put(PageSkinDAO.RECOM_TYPE, entity.getRecom_type());
		values.put(PageSkinDAO.IMG_URL_INSIDE, entity.getImg_url_inside());
		values.put(PageSkinDAO.IMG_URL_OUTSIDE, entity.getImg_url_outside());

		contentResolver.insert(URI_SKIN, values);
	}

	@Override
	public ArrayList<SkinEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		synchronized (_writeLock) {
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(URI_SKIN, selections, where, selectionArgs, sortOrder);
			ArrayList<SkinEntity> arrayList = new ArrayList<SkinEntity>();
			SkinEntity entity = null;

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

				entity = new SkinEntity();
				entity.setRecom_type(cursor.getInt(cursor.getColumnIndex(PageSkinDAO.RECOM_TYPE)));
				entity.setImg_url_inside(cursor.getString(cursor.getColumnIndex(PageSkinDAO.IMG_URL_INSIDE)));
				entity.setImg_url_outside(cursor.getString(cursor.getColumnIndex(PageSkinDAO.IMG_URL_OUTSIDE)));

				arrayList.add(entity);

			} while (cursor.moveToNext());

			cursor.close();

			return arrayList;
		}
	}

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}

}
