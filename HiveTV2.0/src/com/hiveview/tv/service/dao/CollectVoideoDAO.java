package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.FilmEntity;

/**
 * DAO for table Focused
 * */
public class CollectVoideoDAO extends BaseDAO {

	private Context context;

	public static final String VIDEOSET_ID = "videoset_id";
	public static final String VIDEOSET_TYPE = "videoset_type";
	public static final String VIDEOSET_NAME = "videoset_name";
	public static final String VIDEOSET_IMG = "videoset_img";
	public static final String ACTORS = "actors";
	public static final String CP = "cp";
	public static final String DIRECTOR = "director";
	public static final String IS_SUETIME = "is_suetime";
	public static final String TAG = "tag";
	public static final String VIDEOSET_BRIEF = "videoset_brief";
	public static final String VIDEOSET_FOCUS = "videoset_focus";
	public static final String VIDEOSET_TOTAL = "videoset_total";
	public static final String YEARS = "years";
	public static final String TIME_LENGTH = "time_length";
	public static final String TWODIM_CODE = "twodim_code";
	public static final String VIDEOSET_UPDATE = "videoset_update";
	public static final String ISSERIES = "isSeries";
	public static final String VIDEOSET_TV_IMG = "videoset_tv_img";
	public static final String CP_VIDEOSET_ID = "cp_videoset_id";
	public static final String UID = "uid";
	public static final String ADD_TIME = "add_time";

	public CollectVoideoDAO(Context context) {
		this.context = context;
	}

	/**
	 * String for executing SQL.
	 * 
	 * @return String , which is for creating TABLE after being executed.
	 * */
	public String createTableString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("create table ");
		buffer.append(TABLE_COLLECT_VIDEO);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("videoset_id integer,");
		buffer.append("videoset_type integer,");
		buffer.append("videoset_name text,");
		buffer.append("videoset_img text,");
		buffer.append("actors text,");
		buffer.append("director text,");
		buffer.append("is_suetime text,");
		buffer.append("tag text,");
		buffer.append("videoset_brief text,");
		buffer.append("videoset_focus text,");
		buffer.append("videoset_total integer,");
		buffer.append("years text,");
		buffer.append("cp integer,");
		buffer.append("time_length integer,");
		buffer.append("twodim_code text,");
		buffer.append("videoset_update integer,");
		buffer.append("isSeries integer,");
		buffer.append("videoset_tv_img text,");
		buffer.append("cp_videoset_id text,");
		buffer.append("uid integer,");
		buffer.append("add_time integer");
		buffer.append(");");
		Log.d(TAG,buffer.toString()+"");
		return buffer.toString();

	}

	/**
	 * drop table if exists
	 * 
	 * @return String , for executing ,remove table from database
	 * */

	public String dropTable() {

		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_COLLECT_VIDEO);

		return buffer.toString();

	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_COLLECT_VIDEO, where, selectionArgs);
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
		FilmEntity entity = (FilmEntity) obj;
		/*vrsTvId, vrsAlbumId*/
		values.put(VIDEOSET_ID, entity.getVideoset_id());
		values.put(VIDEOSET_TYPE, entity.getVideoset_type());
		values.put(VIDEOSET_NAME, entity.getVideoset_name());
		values.put(VIDEOSET_IMG, entity.getVideoset_img());
		values.put(ACTORS, entity.getActors());
		values.put(DIRECTOR, entity.getDirector());
		values.put(IS_SUETIME, entity.getIs_suetime());
		values.put(TAG, entity.getTag());
		values.put(VIDEOSET_BRIEF, entity.getVideoset_brief());
		values.put(VIDEOSET_FOCUS, entity.getVideoset_focus());
		values.put(VIDEOSET_TOTAL, entity.getVideoset_total());
		values.put(YEARS, entity.getYears());
		values.put(TIME_LENGTH, entity.getTime_length());
		values.put(TWODIM_CODE, entity.getTwodim_code());
		values.put(VIDEOSET_UPDATE, entity.getVideoset_update());
		values.put(ISSERIES, entity.getIsSeries());
		values.put(VIDEOSET_TV_IMG, entity.getVideoset_tv_img());
		values.put(UID, entity.getUid());
		values.put(CP_VIDEOSET_ID, entity.getCp_videoset_id());
		values.put(ADD_TIME, System.currentTimeMillis());
//start:删除，author:huzuwei
//		// 删除重复的
//		//如果是综艺，则根据Videoset_id删除，其他根据CP_VIDEOSET_ID删除
//		Log.d(TAG, "videoset_name = " + entity.getVideoset_name() + " videoset_id = " + entity.getVideoset_id() + " cp_videoset_id = " + "'" + entity.getCp_videoset_id() + "'");
//		if(entity.getVideoset_type()==6){
//			contentResolver.delete(URI_COLLECT_VIDEO, "videoset_id = ? ", new String[] { entity.getVideoset_id()+ "" });	
//		}else{
//			Log.d(TAG, "videoset_name = " + entity.getVideoset_name() + " videoset_id = " + entity.getVideoset_id() + " cp_videoset_id = " + "'" + entity.getCp_videoset_id() + "'");
//		contentResolver.delete(URI_COLLECT_VIDEO, "cp_videoset_id = ? ", new String[] { entity.getCp_videoset_id()+ "" });
//		}
	//end
		// 插入新的收藏记录
		contentResolver.insert(URI_COLLECT_VIDEO, values);

	}

	

	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where, String[] selectionArgs) {

	}

	@Override
	public ArrayList<FilmEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_COLLECT_VIDEO, selections, where, selectionArgs, sortOrder);
		ArrayList<FilmEntity> arrayList = new ArrayList<FilmEntity>();
		FilmEntity entity = null;

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
			entity = new FilmEntity();
			entity.setSource(0);
			entity.setActors(cursor.getString(cursor.getColumnIndex(ACTORS)));
			entity.setCp(cursor.getInt(cursor.getColumnIndex(CP)));
			entity.setCp_videoset_id(cursor.getString(cursor.getColumnIndex(CP_VIDEOSET_ID)));
			entity.setDirector(cursor.getString(cursor.getColumnIndex(DIRECTOR)));
			entity.setIs_suetime(cursor.getString(cursor.getColumnIndex(IS_SUETIME)));
			entity.setIsSeries(cursor.getInt(cursor.getColumnIndex(ISSERIES)));
			entity.setTag(cursor.getString(cursor.getColumnIndex(TAG)));
			entity.setTime_length(cursor.getInt(cursor.getColumnIndex(TIME_LENGTH)));
			entity.setTwodim_code(cursor.getString(cursor.getColumnIndex(TWODIM_CODE)));
			entity.setVideoset_brief(cursor.getString(cursor.getColumnIndex(VIDEOSET_BRIEF)));
			entity.setVideoset_focus(cursor.getString(cursor.getColumnIndex(VIDEOSET_FOCUS)));
			entity.setVideoset_id(cursor.getInt(cursor.getColumnIndex(VIDEOSET_ID)));
			entity.setVideoset_img(cursor.getString(cursor.getColumnIndex(VIDEOSET_IMG)));
			entity.setVideoset_name(cursor.getString(cursor.getColumnIndex(VIDEOSET_NAME)));
			entity.setVideoset_total(cursor.getInt(cursor.getColumnIndex(VIDEOSET_TOTAL)));
			entity.setVideoset_tv_img(cursor.getString(cursor.getColumnIndex(VIDEOSET_TV_IMG)));
			entity.setVideoset_type(cursor.getInt(cursor.getColumnIndex(VIDEOSET_TYPE)));
			entity.setVideoset_update(cursor.getInt(cursor.getColumnIndex(VIDEOSET_UPDATE)));
			entity.setYears(cursor.getString(cursor.getColumnIndex(YEARS)));
			arrayList.add(entity);

		} while (cursor.moveToNext());

		cursor.close();

		return arrayList;
	}

}
