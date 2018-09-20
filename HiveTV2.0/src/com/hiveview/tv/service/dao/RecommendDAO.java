package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.RecommendEntity;

/**
 * DAO for table Focused
 * */
public class RecommendDAO extends BaseDAO {

	private Context context;
	public static final String APP_ID = "_id";
	public static final String CONTENT_ID = "content_id";// id
	public static final String FOCUS_TYPE = "focusType";// 内容类型
	public static final String INTERVAL_TIME = "interval_time";// 轮播时间（毫秒数）
	public static final String POSITIONID = "positionId";// 当前对象所属的页面位置的推荐位编号
	public static final String CONTENT_DESC = "content_desc";// 描述
	public static final String CONTENT_NAME = "content_name";// 名称（电影\专题名称）
	public static final String FOCUS_LARGE_IMG = "focus_large_img";// 大图地址
	public static final String FOCUS_THUMB_IMG = "focus_thumb_img";// 缩略图地址
	public static final String APK_PACKAGE="apkPackage";
	public static final String IS_APK="isApk";
	public static final String IS_INTRANET="isIntranet";
	public static final String IMG_SIZE="img_size";
	public static final String SHOWTYPE="showType";
	public static final String VIDEOID = "videoId";
	public static final String SUBJECTBGIMG = "subjectBgImg";
	public static final String CPID = "cpId";
	public static final String POSITION = "position";//具体位置12345
	private final static byte[] _writeLock = new byte[0];

	public RecommendDAO(Context context) {
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
		buffer.append(TABLE_RECOMMEND);
		buffer.append("(");
		buffer.append("_id integer primary key, ");
		buffer.append("content_id integer,");
		buffer.append("focusType integer,");
		buffer.append("matrix_type integer,");
		buffer.append("position integer,");
		buffer.append("interval_time integer,");
		buffer.append("positionId text,");
		buffer.append("content_name text,");
		buffer.append("content_desc text,");
		buffer.append("focus_large_img text,");
		buffer.append("focus_thumb_img text,");
		buffer.append("apkPackage text,");
		buffer.append("subjectBgImg text,");
		buffer.append("isApk text,");
		buffer.append("img_size text,");
		buffer.append("showType text,");
		buffer.append("videoId integer,");
		buffer.append("isIntranet integer,");
		buffer.append("cpId integer");
		buffer.append(");");
		Log.d(TAG, buffer.toString());
		return buffer.toString();

	}

	private String TAG = "RecommendDAO";

	/**
	 * drop table if exists
	 * 
	 * @return String , for executing ,remove table from database
	 * */

	public String dropTable() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_RECOMMEND);

		return buffer.toString();

	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		synchronized (_writeLock) {
			ContentResolver resolver = context.getContentResolver();
			resolver.delete(URI_RECOMMEND, where, selectionArgs);
		}
	}

	@Override
	public void insert(ArrayList<? extends HiveBaseEntity> arrayList) {

		int count = arrayList.size();
		for (int i = 0; i < count; i++) {
			insert(arrayList.get(i));
		}

	}

	@Override
	public <T extends HiveBaseEntity> void insert(T obj) {

		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		RecommendEntity entity = (RecommendEntity) obj;
		values.put(APP_ID, entity.getId());
		values.put(CONTENT_ID, entity.getContentId());
		values.put(FOCUS_TYPE, entity.getContentType());
		values.put(INTERVAL_TIME, entity.getIntervalTime());
		values.put(POSITIONID, entity.getPositionId());
		values.put(CONTENT_DESC, entity.getContentDesc());
		values.put(CONTENT_NAME, entity.getContentName());
		values.put(FOCUS_LARGE_IMG, entity.getFocusLargeImg());
		values.put(FOCUS_THUMB_IMG, entity.getFocusThumbImg());
		values.put(APK_PACKAGE, entity.getApkPackage());
		values.put(SUBJECTBGIMG, entity.getSubjectBgImg());
		values.put(IS_APK, entity.getIsApk());
		values.put(IS_INTRANET, entity.getIsIntranet());
		values.put(SHOWTYPE, entity.getShowType());
		values.put(VIDEOID, entity.getVideoId());
		values.put(IMG_SIZE, entity.getImgSize());
		values.put(CPID, entity.getCpId());
		contentResolver.insert(URI_RECOMMEND, values);
	}

	public void insert(ArrayList<? extends HiveBaseEntity> arrayList, int matrix_type) {
		synchronized (_writeLock) {
			int count = arrayList.size();
			for (int i = 0; i < count; i++) {
				Log.v(TAG, "RecommendDAO="+arrayList.get(i).toString());
				insert(arrayList.get(i), matrix_type,i);
			}
			Log.v("vip", "极清加入数据库======");
		}
	}

	public <T extends HiveBaseEntity> void insert(T obj, int matrix_type,int position) {

		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		RecommendEntity entity = (RecommendEntity) obj;
		values.put(APP_ID, entity.getId());
		values.put(CONTENT_ID, entity.getContentId());
		values.put(FOCUS_TYPE, entity.getFocusType());
		values.put(INTERVAL_TIME, entity.getIntervalTime());
		values.put(POSITIONID, entity.getPositionId());
		values.put(MATRIX_TYPE, matrix_type);
		values.put(POSITION, position);
		values.put(CONTENT_DESC, entity.getContentDesc());
		values.put(CONTENT_NAME, entity.getContentName());
		values.put(FOCUS_LARGE_IMG, entity.getFocusLargeImg());
		values.put(FOCUS_THUMB_IMG, entity.getFocusThumbImg());
		values.put(APK_PACKAGE, entity.getApkPackage());
		values.put(SUBJECTBGIMG, entity.getSubjectBgImg());
		values.put(IS_APK, entity.getIsApk());
		values.put(IMG_SIZE, entity.getImgSize());
		values.put(IS_INTRANET, entity.getIsIntranet());
		values.put(SHOWTYPE, entity.getShowType());
		values.put(VIDEOID, entity.getVideoId());
		values.put(CPID, entity.getCpId());
		contentResolver.insert(URI_RECOMMEND, values);
	}

	public ArrayList<ArrayList<RecommendEntity>> queryMatrix(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		synchronized (_writeLock) {
			ArrayList<ArrayList<RecommendEntity>> dataList = new ArrayList<ArrayList<RecommendEntity>>();
			for (int i = 1; i < 7; i++) {

				//暂时先这样，假数据 lihongji
				ArrayList<RecommendEntity> list = query(null, new StringBuffer().append(where).append(" and ").append(POSITIONID).append(" = ? ").toString(), new String[] { i + "" }, sortOrder);


				dataList.add(list);

			}

			return dataList;
		}
	}

	@Override
	public ArrayList<RecommendEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_RECOMMEND, selections, where, selectionArgs, sortOrder);
		ArrayList<RecommendEntity> arrayList = new ArrayList<RecommendEntity>();
		RecommendEntity entity = null;

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

			entity = new RecommendEntity();
			entity.setId(cursor.getInt(cursor.getColumnIndex(AppFocusDAO.APP_ID)));
			entity.setContentId(cursor.getInt(cursor.getColumnIndex(CONTENT_ID)));
			entity.setFocusType(cursor.getInt(cursor.getColumnIndex(FOCUS_TYPE)));
			entity.setIntervalTime(cursor.getInt(cursor.getColumnIndex(INTERVAL_TIME)));
			entity.setPositionId(cursor.getString(cursor.getColumnIndex(POSITIONID)));
			entity.setContentName(cursor.getString(cursor.getColumnIndex(CONTENT_NAME)));
			entity.setContentDesc(cursor.getString(cursor.getColumnIndex(CONTENT_DESC)));
			entity.setFocusLargeImg(cursor.getString(cursor.getColumnIndex(FOCUS_LARGE_IMG)));
			entity.setFocusThumbImg(cursor.getString(cursor.getColumnIndex(FOCUS_THUMB_IMG)));
			entity.setApkPackage(cursor.getString(cursor.getColumnIndex(APK_PACKAGE)));
			entity.setSubjectBgImg(cursor.getString(cursor.getColumnIndex(SUBJECTBGIMG)));
			entity.setIsApk(cursor.getString(cursor.getColumnIndex(IS_APK)));
			entity.setImgSize(cursor.getString(cursor.getColumnIndex(IMG_SIZE)));
			entity.setIsIntranet(cursor.getInt(cursor.getColumnIndex(IS_INTRANET)));
			entity.setShowType(cursor.getString(cursor.getColumnIndex(SHOWTYPE)));
			entity.setVideoId(cursor.getInt(cursor.getColumnIndex(VIDEOID)));
			entity.setCpId(cursor.getInt(cursor.getColumnIndex(CPID)));
			arrayList.add(entity);

		} while (cursor.moveToNext());

		cursor.close();

		return arrayList;
	}

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}

	public boolean isCache() {
		boolean reslut = false;
		ArrayList<ArrayList<RecommendEntity>> movieList = queryMatrix(null, null, null, null);
		reslut = movieList.get(movieList.size() - 1).size() > 0 ? true : false;
		return reslut;
	}
	
	
}
