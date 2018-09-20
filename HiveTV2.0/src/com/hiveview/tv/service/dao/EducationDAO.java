package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.entity.RecommendEntity;

/**
 * DAO for table Focused
 * */
public class EducationDAO extends BaseDAO {

	private Context context;

	public static final String APP_ID = "_id";
	public static final String FOCUS_TYPE = "focusType";
	public static final String CONTENT_ID = "content_id";
	public static final String FOCUS_LARGE_IMG = "focus_large_img";
	public static final String FOCUS_THUMB_IMG = "focus_thumb_img";
	public static final String CONTENT_NAME = "content_name";
	public static final String INTERVAL_TIME = "interval_time";
	public static final String CONTENT_DESC = "content_desc";
	public static final String POSITIONID = "positionId";
	public static final String IS_EFFECTIVE = "is_effective";
	public static final String APK_PACKAGE="apkPackage";
	public static final String IS_APK="isApk";
	public static final String IMG_SIZE="img_size";
	public static final String IS_INTRANET="isIntranet";
	private final static byte[] _writeLock = new byte[0];

	public EducationDAO(Context context) {
		this.context = context;
	}

	/**
	 * String for executing SQL.
	 * 
	 * @return String , which is for creating TABLE after being executed.
	 * */
	public String createTableString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("create table if not exists ");
		buffer.append(TABLE_EDUCATION_FOCUS);
		buffer.append("(");
		buffer.append("_id integer primary key ,");
		buffer.append("focusType integer,");
		buffer.append("content_id integer,");
		buffer.append("focus_large_img text,");
		buffer.append("focus_thumb_img text,");
		buffer.append("content_name text,");
		buffer.append("interval_time integer,");
		buffer.append("content_desc text,");
		buffer.append("positionId text,");
		buffer.append("is_effective integer,");
		buffer.append("apkPackage text,");
		buffer.append("isApk text,");
		buffer.append("img_size text,");
		buffer.append("isIntranet integer");
		buffer.append(");");
		Log.d(TAG, buffer.toString() + "");
		return buffer.toString();

	}

	private String TAG = "EducationDAO";

	/**
	 * drop table if exists
	 * 
	 * @return String , for executing ,remove table from database
	 * */

	public String dropTable() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_EDUCATION_FOCUS);

		return buffer.toString();

	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		synchronized (_writeLock) {
			ContentResolver resolver = context.getContentResolver();
			resolver.delete(URI_EDUCATION_FOCUS, where, selectionArgs);
		}
	}

	@Override
	public void insert(ArrayList<? extends HiveBaseEntity> arrayList) {
		synchronized (_writeLock) {
			if (null == arrayList || arrayList.size() == 0) {
				return;
			}
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
		AppFocusEntity entity = (AppFocusEntity) obj;

		values.put(EducationDAO.APP_ID, entity.getId());
		values.put(EducationDAO.FOCUS_TYPE, entity.getFocusType());
		values.put(EducationDAO.CONTENT_ID, entity.getContentId());
		values.put(EducationDAO.FOCUS_LARGE_IMG, entity.getFocusLargeImg());
		values.put(EducationDAO.FOCUS_THUMB_IMG, entity.getFocusThumbImg());
		values.put(EducationDAO.CONTENT_NAME, entity.getContentName());
		values.put(EducationDAO.INTERVAL_TIME, entity.getIntervalTime());
		values.put(EducationDAO.CONTENT_DESC, entity.getContentDesc());
		values.put(EducationDAO.POSITIONID, entity.getPositionId());
		values.put(EducationDAO.IS_EFFECTIVE, entity.getIsEffective());
		values.put(EducationDAO.APK_PACKAGE, entity.getApkPackage());
		values.put(EducationDAO.IS_APK, entity.getIsApk());
		values.put(EducationDAO.IMG_SIZE, entity.getImgSize());
		values.put(EducationDAO.IS_INTRANET, entity.getIsIntranet());
		contentResolver.insert(URI_EDUCATION_FOCUS, values);
	}

	@Override
	public ArrayList<AppFocusEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_EDUCATION_FOCUS, selections, where, selectionArgs, sortOrder);
		ArrayList<AppFocusEntity> arrayList = new ArrayList<AppFocusEntity>();
		AppFocusEntity entity = null;

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

			entity = new AppFocusEntity();

			entity.setId(cursor.getInt(cursor.getColumnIndex(EducationDAO.APP_ID)));
			entity.setFocusType(cursor.getInt(cursor.getColumnIndex(EducationDAO.FOCUS_TYPE)));
			entity.setContentId(cursor.getInt(cursor.getColumnIndex(EducationDAO.CONTENT_ID)));
			entity.setFocusLargeImg(cursor.getString(cursor.getColumnIndex(EducationDAO.FOCUS_LARGE_IMG)));
			entity.setFocusThumbImg(cursor.getString(cursor.getColumnIndex(EducationDAO.FOCUS_THUMB_IMG)));
			entity.setContentName(cursor.getString(cursor.getColumnIndex(EducationDAO.CONTENT_NAME)));
			entity.setIntervalTime(cursor.getInt(cursor.getColumnIndex(EducationDAO.INTERVAL_TIME)));
			entity.setContentDesc(cursor.getString(cursor.getColumnIndex(EducationDAO.CONTENT_DESC)));
			entity.setPositionId(cursor.getString(cursor.getColumnIndex(EducationDAO.POSITIONID)));
			entity.setIsEffective(cursor.getInt(cursor.getColumnIndex(EducationDAO.IS_EFFECTIVE)));
			entity.setApkPackage(cursor.getString(cursor.getColumnIndex(EducationDAO.APK_PACKAGE)));
			entity.setIsApk(cursor.getString(cursor.getColumnIndex(EducationDAO.IS_APK)));
			entity.setImgSize(cursor.getString(cursor.getColumnIndex(IMG_SIZE)));
			entity.setIsIntranet(cursor.getInt(cursor.getColumnIndex(EducationDAO.IS_INTRANET)));
			arrayList.add(entity);

		} while (cursor.moveToNext());

		cursor.close();

		return arrayList;
	}

	public ArrayList<ArrayList<AppFocusEntity>> queryAppFocus(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		synchronized (_writeLock) {
			ArrayList<ArrayList<AppFocusEntity>> dataList = new ArrayList<ArrayList<AppFocusEntity>>();
			for (int i = 1; i <= MATRIX_EDUCATION_TOTAL; i++) {
				//暂时先这样，假数据 lihongji
				ArrayList<AppFocusEntity> list = query(null, new StringBuffer().append(POSITIONID).append(" = ? ").toString(), new String[] { i + "" }, null);

				dataList.add(list);

			}

			return dataList;
		}
	}

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}
	
	public boolean isCache() {
		boolean reslut = false;
		ArrayList<ArrayList<AppFocusEntity>> gameList= queryAppFocus(null, null, null, null);
		reslut = gameList.get(gameList.size() - 1).size() > 0 ? true : false;
		return reslut;
	}

}
