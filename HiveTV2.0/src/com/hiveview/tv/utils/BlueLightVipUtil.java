package com.hiveview.tv.utils;

import java.util.ArrayList;

import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.entity.MoviePermissionEntity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class BlueLightVipUtil {


public static final	String AUTHORITIES       = "BlueLightAuthorities";
protected static final char CHARACTER_SLASH  = '/';
public static final String TABLE_BLUELIGHT_MOVIE_PAY_PERMESSION = "TABLE_BLUELIGHT_MOVIE_PAY_PERMESSION";
public static final Uri URI_BLUELIGHT_MOVIE_PAY_PERMESSION = Uri.parse(new StringBuffer()
													.append("content://")
													.append(AUTHORITIES)
													.append(CHARACTER_SLASH)
													.append(TABLE_BLUELIGHT_MOVIE_PAY_PERMESSION).toString()
													);

public static ArrayList<MoviePermissionEntity> query(String[] selections, String where, String[] selectionArgs, String sortOrder) {
		ContentResolver resolver = HiveviewApplication.mContext.getContentResolver();
		Cursor cursor = resolver.query(URI_BLUELIGHT_MOVIE_PAY_PERMESSION, selections, where, selectionArgs, sortOrder);
		ArrayList<MoviePermissionEntity> arrayList = new ArrayList<MoviePermissionEntity>();
		MoviePermissionEntity entity = null;

		if (null == cursor || cursor.getCount() <= 0) { 
			return arrayList;
		}

		cursor.moveToFirst();

		do {
			entity = new MoviePermissionEntity();
			entity.setCpId(cursor.getInt(cursor.getColumnIndex("cpId")));
			entity.setEffectiveTime(cursor.getString(cursor.getColumnIndex("efectTime")));
			entity.setFirstPayTime(cursor.getString(cursor.getColumnIndex("firstPayTime")));
			arrayList.add(entity);
		} while (cursor.moveToNext());

		cursor.close();
		return arrayList;
	}


}
