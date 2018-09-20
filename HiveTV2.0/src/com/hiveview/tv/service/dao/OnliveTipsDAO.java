package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
/**
 * DAO for table Focused
 * */
public class OnliveTipsDAO extends BaseDAO{

	private Context context;
	
	public static final String NAME = "name";
	public static final String DATE = "date";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";
	public static final String WIKI_ID = "wiki_id";
	public static final String WIKI_COVER = "wiki_cover";
	public static final String HASVIDEO = "hasvideo";
	public static final String SOURCE = "source";
	public static final String TIP_TIME = "tip_time";
	public static final String TELEVISIONlOGOURL = "television_logo_url";
	public static final String TELEVISIONlOGONAME = "television_logo_name";
	public OnliveTipsDAO(Context context) {
		this.context = context;
	}
	
	/**
	 * 	String for executing SQL.
	 * @return String , which is for creating TABLE after being executed.
	 * */
	public String createTableString(){
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("create table if not exists  ");
		buffer.append(TABLE_TV_TIPS);
		buffer.append("(");
		buffer.append("_id integer primary key AUTOINCREMENT,");
		buffer.append("name text,");
		buffer.append("date text,");
		buffer.append("start_time text,");
		buffer.append("end_time text,");
		buffer.append("wiki_id text,");
		buffer.append("wiki_cover text,");
		buffer.append("tip_time text,");
		buffer.append("television_logo_name text,");
		buffer.append("hasvideo text,");
		buffer.append("source text,");
		buffer.append("television_logo_url text");
		buffer.append(");");
		Log.d(TAG,buffer.toString());
		return buffer.toString();
		
	}
	private String TAG = "OnliveTipsDAO";
	
	/**
	 * drop table if exists
	 * @return String , for executing ,remove table from database
	 * */

	public String dropTable(){
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.DROP_TABLE);
		buffer.append(TABLE_TV_TIPS);
		
		return buffer.toString();
		
	}

	@Override
	public void delete(String where, String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(URI_TV_TIPS, where, selectionArgs);
	}
	

	@Override
	public void insert(ArrayList<? extends HiveBaseEntity> arrayList) {
	}

	@Override
	public ArrayList<? extends HiveBaseEntity> query(String[] selections,String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver           = context.getContentResolver();
		Cursor cursor                      =  resolver.query(URI_TV_TIPS, selections, where, selectionArgs, sortOrder);
		ArrayList<OnliveTipsEntity> arrayList = new ArrayList<OnliveTipsEntity>();		
		OnliveTipsEntity entity               = null;
		
		if(null==cursor) return arrayList;
		
		if(cursor.getCount()<AppConstant.NO_1){
			
			cursor.close();
			return arrayList;
			
		}
		
		int count = cursor.getCount();
		
		if(count>AppConstant.NO_0) cursor.moveToFirst();
		
		do {
		
			entity = new OnliveTipsEntity();
			entity.setStart_time(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.START_TIME)));
			entity.setDate(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.DATE)));
			entity.setName(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.NAME)));
			entity.setWiki_id(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.WIKI_ID)));
			entity.setWiki_cover(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.WIKI_COVER)));
			entity.setSource(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.SOURCE)));
			entity.setTelevisionLogoUrl(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.TELEVISIONlOGOURL)));
			entity.setTelevisionName(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.TELEVISIONlOGONAME)));
			entity.setEnd_time(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.END_TIME)));
			entity.setHasvideo(cursor.getString(cursor.getColumnIndex(OnliveTipsDAO.HASVIDEO)));

			arrayList.add(entity);
			
		} while (cursor.moveToNext());
		
		cursor.close();
		
		return arrayList;
	}

	
	/**
	 * 根据wiki_id 值其查询 是否存在
	 * @param selections
	 * @param where
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	public boolean isExist(String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
//		Cursor cursor = resolver.query(URI_TV_TIPS, null, "television_logo_name = ? and date = ?",
			Cursor cursor = resolver.query(URI_TV_TIPS, null, "television_logo_name =? and date =? and start_time =?",selectionArgs, null);
		if (null == cursor)
			return false;

		if (cursor.getCount() < AppConstant.NO_1) {

			cursor.close();
			return false;

		}

		int count = cursor.getCount();
		cursor.close();

		if (count > AppConstant.NO_0)
			return true;
		else 
			return false;

		
	}
	
	
	/**
	 * 根据wiki_id 值其查询 是否存在
	 * @param selections
	 * @param where
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	public boolean isExists(String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
//		Cursor cursor = resolver.query(URI_TV_TIPS, null, "television_logo_name = ? and date = ?",
			Cursor cursor = resolver.query(URI_TV_TIPS, null, "television_logo_name =? and date =?",selectionArgs, null);
		if (null == cursor)
			return false;

		if (cursor.getCount() < AppConstant.NO_1) {

			cursor.close();
			return false;

		}

		int count = cursor.getCount();
		cursor.close();

		if (count > AppConstant.NO_0)
			return true;
		else 
			return false;

		
	}

	
	
	@Override
	public <T extends HiveBaseEntity> void update(T obj, String where,String[] selectionArgs) {
	}

	@Override
	public <T extends HiveBaseEntity> void insert(T obj) {
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		OnliveTipsEntity entity = (OnliveTipsEntity) obj;
		
		values.put(OnliveTipsDAO.NAME, entity.getName());
		
		values.put(OnliveTipsDAO.DATE,null == entity.getDate() ? "":entity.getDate());
		values.put(OnliveTipsDAO.START_TIME, entity.getStart_time());
		values.put(OnliveTipsDAO.WIKI_COVER, null == entity.getWiki_cover()?"":entity.getWiki_cover().toString());
		values.put(OnliveTipsDAO.WIKI_ID, entity.getWiki_id());
		values.put(OnliveTipsDAO.SOURCE, entity.getSource());
		values.put(OnliveTipsDAO.HASVIDEO, entity.getHasvideo());
		values.put(OnliveTipsDAO.TELEVISIONlOGOURL, entity.getTelevisionLogoUrl());
		values.put(OnliveTipsDAO.TELEVISIONlOGONAME, entity.getTelevisionName());
		values.put(OnliveTipsDAO.END_TIME, entity.getEnd_time());
		values.put(OnliveTipsDAO.TIP_TIME, entity.getTip_time());
		contentResolver.insert(URI_TV_TIPS, values);
		Log.i(TAG, "date=>"+null == entity.getDate() ? "":entity.getDate()
				+"start_time=>" + entity.getStart_time()
				+"wiki_cover=>" + entity.getWiki_cover()
				+"wiki_id=>" + entity.getWiki_id()
				+"source=>" + entity.getSource()
				+"hasvideo=>" + entity.getHasvideo()
				+"televisionilogourl=>" +entity.getTelevisionLogoUrl()
				+"televisionlogoname=>" + entity.getTelevisionName()
				+"end_time=>" + entity.getEnd_time()
				+"tip_time=>" + entity.getTip_time());
	}


}
