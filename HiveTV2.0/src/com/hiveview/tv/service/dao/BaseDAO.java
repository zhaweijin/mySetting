package com.hiveview.tv.service.dao;

import java.util.ArrayList;

import android.net.Uri;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * base DAO for storing URI path & authorities
 * */
public abstract class BaseDAO {
	
	
	
	public static final String MATRIX_TYPE= "matrix_type";
	public static final int MATRIX_GAME = 0;
	public static final int MATRIX_TV = 1;
	public static final int MATRIX_RECOMMEND = 2;
	public static final int MATRIX_MOVIE = 3;
	public static final int MATRIX_APP = 4;
	public static final int MATRIX_BLUELIGHT = 4;
	
	/**
	 * @Fields MATRIX_GAME_TOTAL:推荐位游戏页面推荐位的总数
	 */
	public static final int MATRIX_GAME_TOTAL = 6;
	public static final int MATRIX_EDUCATION_TOTAL = 6;
	
	protected final String DROP_TABLE            = "DROP TABLE IF EXISTS ";	
	protected static final char CHARACTER_SLASH  = '/';
	
	/**
	 * String for VideoProvider authorities
	 * */
	public static final	String AUTHORITIES       = "HiveTVAuthorities";
	

	public static final String TABLE_MESSAGE = "TABLE_MESSAGE";
	public static final Uri URI_MESSAGE = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_MESSAGE).toString()
													);
	
	public static final String TABLE_TV_TIPS = "TABLE_TV_TIPS";
	public static final Uri URI_TV_TIPS = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_TV_TIPS).toString()
													);
	
	public static final String TABLE_SKIN = "TABLE_SKIN";
	public static final Uri URI_SKIN = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_SKIN).toString()
													);
	
	public static final String TABLE_LIVE_CHANNEL_TOP = "TABLE_CHANNEL_TOP";
	public static final Uri URI_LIVE_CHANNEL_TOP = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_LIVE_CHANNEL_TOP).toString()
													);
	
	public static final String TABLE_RECOMMEND = "TABLE_RECOMMEND";
	public static final Uri URI_RECOMMEND = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_RECOMMEND).toString()
													);
	
	public static final String TABLE_CHANNEL = "TABLE_CHANNEL";
	public static final Uri URI_CHANNEL = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_CHANNEL).toString()
													);
	
	/**
	 * 根据电视台名称和电视台的code对应关系
	 */
	public static final String TABLE_CHANNEL_NAME_MAP_CODE = "TABLE_NAME_MAP_CODE";
	public static final Uri URI_CHANNEL_NAME_MAP_CODE = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_CHANNEL_NAME_MAP_CODE).toString()
													);
	
	public static final String TABLE_COLLECT_VIDEO = "TABLE_COLLECT_VIDEO";
	public static final Uri URI_COLLECT_VIDEO = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_COLLECT_VIDEO).toString()
													);
	
	public static final String TABLE_APP_FOCUS = "TABLE_APP_FOCUS";
	public static final Uri URI_APP_FOCUS = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_APP_FOCUS).toString()
													);
	public static final String TABLE_GAME_FOCUS = "TABLE_GAME_FOCUS";
	public static final Uri URI_GAME_FOCUS = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_GAME_FOCUS).toString()
													);
	public static final String TABLE_EDUCATION_FOCUS = "TABLE_EDUCATION_FOCUS";
	public static final Uri URI_EDUCATION_FOCUS = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_EDUCATION_FOCUS).toString()
													);
	
	

 	/**
 	 * create a String for creating table in database
 	 * @return String , String for creating table.
 	 * */
	public abstract String createTableString();
	
	/**
	 * drop table if exists
	 * @return String , for executing ,remove table from database
	 * */
	public abstract String dropTable();
	
	/**
	 * insert data into database
	 * @param obj , completely down-loaded entity info
	 * */
	public abstract void insert(ArrayList<? extends HiveBaseEntity> arrayList);
	
	/**
	 * insert data into database
	 * @param obj , completely down-loaded entity info
	 * */
	public abstract <T extends HiveBaseEntity> void insert(T obj);
	
	/**
	 * delete record with filter
	 * @param where , A filter to apply to rows before deleting, formatted as an SQL WHERE clause (excluding the WHERE itself).
	 * @param selectionArgs , 
	 * */
	public abstract void delete(String where ,String[] selectionArgs);
	 
	/**
	 * query records with filter
	 * @param where , A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URI.
	 * @param selectionArgs , You may include ?s in selection, which will be replaced by the values from selectionArgs, in the order that they appear in the selection. The values will be bound as Strings.
	 * @return ArrayList<Object> , the list of records
	 * */
	public abstract ArrayList<? extends HiveBaseEntity> query(String[] selections,String where, String[] selectionArgs,String sortOrder);
	
	
//	public abstract <T extends BaseEntity> T query(); 
	
	/**
	 * update and reset the records
	 * @param <T>
	 * @param obj,
	 * @param where,  A filter to apply to rows before deleting, formatted as an SQL WHERE clause (excluding the WHERE itself).
	 * @param selectionArgs,
	 * */
	public abstract <T extends HiveBaseEntity> void update(T obj,String where, String[] selectionArgs); 
	
	

}
