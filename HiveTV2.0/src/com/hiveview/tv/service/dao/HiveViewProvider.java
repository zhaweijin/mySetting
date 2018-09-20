package com.hiveview.tv.service.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.hiveview.tv.common.AppConstant;

/**
 * data base
 * */
public class HiveViewProvider extends ContentProvider {

	private final String DB_NAME = "hiveview";
	private final int DB_VERSION = 7;

	private SQLiteDatabase db;

	private UriMatcher mUriMatcher;

	/**
	 * 数据库版本号为2，TABLE_CHANNEL表需要添加的字段
	 */
	private final static String ALERT_SQL_TABLE_CHANNEL = "alter table " + BaseDAO.TABLE_CHANNEL + " add column show_type integer";

	/**
	 * set URI id in for created table
	 * */
	{
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_TV_TIPS, AppConstant.NO_1);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_SKIN, AppConstant.NO_2);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_RECOMMEND, AppConstant.NO_3);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_CHANNEL, AppConstant.NO_4);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_APP_FOCUS, AppConstant.NO_5);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_GAME_FOCUS, AppConstant.NO_6);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_EDUCATION_FOCUS, AppConstant.NO_7);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_COLLECT_VIDEO, AppConstant.NO_50);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_LIVE_CHANNEL_TOP, AppConstant.NO_51);
		mUriMatcher.addURI(BaseDAO.AUTHORITIES, BaseDAO.TABLE_CHANNEL_NAME_MAP_CODE, AppConstant.NO_52);

	}

	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {

			/* create database */
			super(context, DB_NAME, null, DB_VERSION);

		}

		/**
		 * create table
		 * */
		public void onCreate(SQLiteDatabase db) {
			OnliveTipsDAO recordDAD = new OnliveTipsDAO(getContext());
			db.execSQL(recordDAD.createTableString());

			PageSkinDAO skinDAO = new PageSkinDAO(getContext());
			db.execSQL(skinDAO.createTableString());

			RecommendDAO recommendDAO = new RecommendDAO(getContext());
			db.execSQL(recommendDAO.createTableString());

			ChannelDAO channelDAO = new ChannelDAO(getContext());
			db.execSQL(channelDAO.createTableString());

			CollectVoideoNewDAO collectVoideoDAO = new CollectVoideoNewDAO(getContext());
			db.execSQL(collectVoideoDAO.createTableString());

			AppFocusDAO appFocusDAO = new AppFocusDAO(getContext());
			db.execSQL(appFocusDAO.createTableString());

			GameFocusDAO gameFocusDAO = new GameFocusDAO(getContext());
			db.execSQL(gameFocusDAO.createTableString());

			TvSetTopDAO tvSetTopDAO = new TvSetTopDAO(getContext());
			db.execSQL(tvSetTopDAO.createTableString());

			ChannelNameMapCodeDAO nameAndCodeMapDAO = new ChannelNameMapCodeDAO(getContext());
			db.execSQL(nameAndCodeMapDAO.createTableString());

			EducationDAO educationDAO = new EducationDAO(getContext());
			db.execSQL(educationDAO.createTableString());
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if (newVersion > oldVersion) {
				switch (oldVersion) {
				case 1:
				//	db.execSQL(ALERT_SQL_TABLE_CHANNEL);
					getContext().deleteDatabase(DB_NAME);
					getContext().openOrCreateDatabase(DB_NAME, 0, null);
					//db.execSQL("create database "+DB_NAME+" ;");
					onCreate(db);
					break;
				case 2:
				//	db.execSQL(ALERT_SQL_TABLE_CHANNEL);
					getContext().deleteDatabase(DB_NAME);
					getContext().openOrCreateDatabase(DB_NAME, 0, null);
					onCreate(db);
					break;
				case 3:
					
				case 4:
					db.execSQL("DROP TABLE IF EXISTS TABLE_COLLECT_VIDEO;");// 收藏数据库字段变了，重构一下数据库
					CollectVoideoNewDAO collectVoideoDAO = new CollectVoideoNewDAO(getContext());
					db.execSQL(collectVoideoDAO.createTableString());
					db.execSQL("DROP TABLE IF EXISTS TABLE_APP_FOCUS;");// 应用数据库字段变了，重构一下数据库
					AppFocusDAO appFocusDAO = new AppFocusDAO(getContext());
					db.execSQL(appFocusDAO.createTableString());
					db.execSQL("DROP TABLE IF EXISTS TABLE_GAME_FOCUS;");// 游戏数据库字段变了，重构一下数据库
					GameFocusDAO gameFocusDAO = new GameFocusDAO(getContext());
					db.execSQL(gameFocusDAO.createTableString());
					db.execSQL("DROP TABLE IF EXISTS TABLE_EDUCATION_FOCUS;");// 教育数据库字段变了，重构一下数据库
					EducationDAO educationDAO = new EducationDAO(getContext());
					db.execSQL(educationDAO.createTableString());
					db.execSQL("DROP TABLE IF EXISTS TABLE_RECOMMEND;");// 首页、极清数据库字段变了，重构一下数据库
					RecommendDAO recommendDAO = new RecommendDAO(getContext());
					db.execSQL(recommendDAO.createTableString());
					break;
				case 5:
					db.execSQL("DROP TABLE IF EXISTS TABLE_APP_FOCUS;");// 应用数据库字段变了，重构一下数据库
					AppFocusDAO appFocusDAO5 = new AppFocusDAO(getContext());
					db.execSQL(appFocusDAO5.createTableString());
					db.execSQL("DROP TABLE IF EXISTS TABLE_RECOMMEND;");// 首页、极清数据库字段变了，重构一下数据库
					RecommendDAO recommendDAO5 = new RecommendDAO(getContext());
					db.execSQL(recommendDAO5.createTableString());
					break;
				case 6:
					db.execSQL("DROP TABLE IF EXISTS TABLE_RECOMMEND;");// 首页、极清数据库字段变了，重构一下数据库
					RecommendDAO recommendDAO6 = new RecommendDAO(getContext());
					db.execSQL(recommendDAO6.createTableString());
					break;
				default:
					break;
				}
			}
		}

	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {

		int matchCode = mUriMatcher.match(uri);

		switch (matchCode) {
		case AppConstant.NO_1:

			db.delete(BaseDAO.TABLE_TV_TIPS, selection, selectionArgs);

			break;
		case AppConstant.NO_2:

			db.delete(BaseDAO.TABLE_SKIN, selection, selectionArgs);

			break;
		case AppConstant.NO_3:

			db.delete(BaseDAO.TABLE_RECOMMEND, selection, selectionArgs);

			break;
		case AppConstant.NO_4:

			db.delete(BaseDAO.TABLE_CHANNEL, selection, selectionArgs);

			break;
		case AppConstant.NO_5:

			db.delete(BaseDAO.TABLE_APP_FOCUS, selection, selectionArgs);

			break;
		case AppConstant.NO_6:

			db.delete(BaseDAO.TABLE_GAME_FOCUS, selection, selectionArgs);

			break;
		case AppConstant.NO_7:

			db.delete(BaseDAO.TABLE_EDUCATION_FOCUS, selection, selectionArgs);

			break;
		case AppConstant.NO_50:
			db.delete(BaseDAO.TABLE_COLLECT_VIDEO, selection, selectionArgs);
			break;
		case AppConstant.NO_51:
			db.delete(BaseDAO.TABLE_LIVE_CHANNEL_TOP, selection, selectionArgs);
			break;
		case AppConstant.NO_52:
			db.delete(BaseDAO.TABLE_CHANNEL_NAME_MAP_CODE, selection, selectionArgs);
			break;
		}

		return 0;
	}

	public String getType(Uri uri) {

		return uri.toString();

	}

	public Uri insert(Uri uri, ContentValues values) {

		int matchCode = mUriMatcher.match(uri);

		switch (matchCode) {

		case AppConstant.NO_1:
			db.insert(BaseDAO.TABLE_TV_TIPS, null, values);
			break;
		case AppConstant.NO_2:
			db.insert(BaseDAO.TABLE_SKIN, null, values);
			break;
		case AppConstant.NO_3:
			db.insert(BaseDAO.TABLE_RECOMMEND, null, values);
			break;
		case AppConstant.NO_4:
			long count = db.insert(BaseDAO.TABLE_CHANNEL, null, values);
			System.out.println("个数：" + count);
			break;
		case AppConstant.NO_5:
			db.insert(BaseDAO.TABLE_APP_FOCUS, null, values);
			break;
		case AppConstant.NO_6:
			db.insert(BaseDAO.TABLE_GAME_FOCUS, null, values);
			break;
		case AppConstant.NO_7:
			db.insert(BaseDAO.TABLE_EDUCATION_FOCUS, null, values);
			break;
		case AppConstant.NO_50:
			db.insert(BaseDAO.TABLE_COLLECT_VIDEO, null, values);
			break;
		case AppConstant.NO_51:
			db.insert(BaseDAO.TABLE_LIVE_CHANNEL_TOP, null, values);
			break;
		case AppConstant.NO_52:
			db.insert(BaseDAO.TABLE_CHANNEL_NAME_MAP_CODE, null, values);
			break;
		}

		return null;
	}

	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		int matchCode = mUriMatcher.match(uri);
		Cursor cursor = null;

		switch (matchCode) {
		case AppConstant.NO_1:

			cursor = db.query(BaseDAO.TABLE_TV_TIPS, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_2:

			cursor = db.query(BaseDAO.TABLE_SKIN, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_3:

			cursor = db.query(BaseDAO.TABLE_RECOMMEND, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_4:

			cursor = db.query(BaseDAO.TABLE_CHANNEL, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_5:

			cursor = db.query(BaseDAO.TABLE_APP_FOCUS, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_6:

			cursor = db.query(BaseDAO.TABLE_GAME_FOCUS, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_7:

			cursor = db.query(BaseDAO.TABLE_EDUCATION_FOCUS, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_50:

			cursor = db.query(BaseDAO.TABLE_COLLECT_VIDEO, null, selection, selectionArgs, null, null, sortOrder);

			break;
		case AppConstant.NO_51:
			cursor = db.query(BaseDAO.TABLE_LIVE_CHANNEL_TOP, null, selection, selectionArgs, null, null, sortOrder);
			break;
		case AppConstant.NO_52:
			cursor = db.query(BaseDAO.TABLE_CHANNEL_NAME_MAP_CODE, null, selection, selectionArgs, null, null, sortOrder);
			break;
		}

		return cursor;
	}

	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		int matchCode = mUriMatcher.match(uri);

		switch (matchCode) {

		}

		return 0;
	}

	@Override
	public boolean onCreate() {

		db = new DBHelper(getContext()).getWritableDatabase();

		return false;
	}

}
