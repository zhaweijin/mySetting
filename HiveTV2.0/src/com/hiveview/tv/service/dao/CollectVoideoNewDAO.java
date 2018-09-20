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
import com.hiveview.tv.service.entity.FilmNewEntity;

/**
 * DAO for table Focused
 * */
public class CollectVoideoNewDAO extends BaseDAO {

	private Context context;
	private static final String TAG = "CollectVoideoNewDAO";
	public static final String CID = "cid";
	public static final String CNAME = "cname";
	public static final String CP = "cp";
	public static final String CURRCOUNT = "currCount";
	public static final String FOCUSNAME = "focusName";
	public static final String EQLEN = "eqLen";
	public static final String DESC = "desc";
	public static final String DIRECTORS = "directors";
	public static final String ID = "id";
	public static final String INITISSUETIME = "initIssueTime";
	public static final String MAINACTORS = "mainActors";
	public static final String NAME = "name";
	public static final String PLAYCOUNR = "playCount";
	public static final String POSTERURL = "posterUrl";
	public static final String SCORE = "score";
	public static final String SERIESTYPE = "seriesType";
	public static final String STREAMS = "streams";
	public static final String TAGNAMES = "tagNames";
	public static final String TIME = "time";
	public static final String TOTAL = "total";
	public static final String TYPE3D = "type3d";
	public static final String UID = "uid";
	public static final String ADD_TIME = "add_time";
	public CollectVoideoNewDAO(Context context) {
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
		buffer.append(TABLE_COLLECT_VIDEO);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("cid integer,");
		buffer.append("cname text,");
		buffer.append("cp text,");
		buffer.append("currCount integer,");
		buffer.append("focusName text,");
		buffer.append("eqLen integer,");
		buffer.append("desc text,");
		buffer.append("directors text,");
		buffer.append("id integer,");
		buffer.append("initIssueTime text,");
		buffer.append("mainActors text,");
		buffer.append("name text,");
		buffer.append("playCount integer,");
		buffer.append("posterUrl text,");
		buffer.append("score double,");
		buffer.append("seriesType integer,");
		buffer.append("streams text,");
		buffer.append("tagNames text,");
		buffer.append("time text,");
		buffer.append("total integer,");
		buffer.append("type3d integer,");
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
		FilmNewEntity entity = (FilmNewEntity) obj;
		/*vrsTvId, vrsAlbumId*/
		values.put(CID, entity.getCid());
		values.put(CNAME, entity.getCname());
		values.put(CP, entity.getCp());
		values.put(CURRCOUNT, entity.getCurrCount());
		values.put(FOCUSNAME, entity.getFocusName());
		values.put(EQLEN, entity.getEqLen());
		values.put(DESC, entity.getDesc());
		values.put(DIRECTORS, entity.getDirectors());
		values.put(ID, entity.getId());
		values.put(INITISSUETIME, entity.getInitIssueTime());
		values.put(MAINACTORS, entity.getMainActors());
		values.put(NAME, entity.getName());
		values.put(PLAYCOUNR, entity.getPlayCount());
		values.put(POSTERURL, entity.getPosterUrl());
		values.put(SCORE, entity.getScore());
		values.put(SERIESTYPE, entity.getSeriesType());
		values.put(STREAMS, entity.getStreams());
		values.put(TAGNAMES, entity.getTagNames());
		values.put(TIME, entity.getTime());
		values.put(TOTAL, entity.getTotal());
		values.put(TYPE3D, entity.getType3d());
		values.put(UID, entity.getUid());
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
	public ArrayList<FilmNewEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(URI_COLLECT_VIDEO, selections, where, selectionArgs, sortOrder);
		ArrayList<FilmNewEntity> arrayList = new ArrayList<FilmNewEntity>();
		FilmNewEntity entity = null;

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
			entity = new FilmNewEntity();
			entity.setSource(0);
			entity.setCid(cursor.getInt(cursor.getColumnIndex(CID)));
			entity.setCname(cursor.getString(cursor.getColumnIndex(CNAME)));
			entity.setCp(cursor.getString(cursor.getColumnIndex(CP)));
			entity.setCurrCount(cursor.getInt(cursor.getColumnIndex(CURRCOUNT)));
			entity.setFocusName(cursor.getString(cursor.getColumnIndex(FOCUSNAME)));
			entity.setEqLen(cursor.getInt(cursor.getColumnIndex(EQLEN)));
			entity.setDesc(cursor.getString(cursor.getColumnIndex(DESC)));
			entity.setDirectors(cursor.getString(cursor.getColumnIndex(DIRECTORS)));
			entity.setId(cursor.getInt(cursor.getColumnIndex(ID)));
			entity.setInitIssueTime(cursor.getString(cursor.getColumnIndex(INITISSUETIME)));
			entity.setMainActors(cursor.getString(cursor.getColumnIndex(MAINACTORS)));
			entity.setName(cursor.getString(cursor.getColumnIndex(NAME)));
			entity.setPlayCount(cursor.getInt(cursor.getColumnIndex(PLAYCOUNR)));
			entity.setPosterUrl(cursor.getString(cursor.getColumnIndex(POSTERURL)));
			entity.setScore(cursor.getDouble(cursor.getColumnIndex(SCORE)));
			entity.setSeriesType(cursor.getInt(cursor.getColumnIndex(SERIESTYPE)));
			entity.setStreams(cursor.getString(cursor.getColumnIndex(STREAMS)));
			entity.setTagNames(cursor.getString(cursor.getColumnIndex(TAGNAMES)));
			entity.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
			entity.setTotal(cursor.getInt(cursor.getColumnIndex(TOTAL)));
			entity.setType3d(cursor.getInt(cursor.getColumnIndex(TYPE3D)));
			arrayList.add(entity);

		} while (cursor.moveToNext());

		cursor.close();

		return arrayList;
	}

}
