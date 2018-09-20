package com.hiveview.tv.common.content;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.util.Log;

import com.hiveview.tv.common.AppConstant;

public class ContentInvoker {

	
	public static final String CONTENT_ACTION_MOVIESUBJECT = "CONTENT_ACTION_MOVIESUBJECT";
	public static final String CONTENT_ACTION_MOVIEDETAIL = "CONTENT_ACTION_MOVIEDETAIL";
	
	public static final String CONTENT_ACTION_FILMDETAIL = "CONTENT_ACTION_FILMDETAIL";
	public static final String CONTENT_ACTION_TELEPLAYDETAIL = "CONTENT_ACTION_TELEPLAYDETAIL";
	public static final String CONTENT_ACTION_VARIETYDETAIL = "CONTENT_ACTION_VARIETYDETAIL";
	public static final String CONTENT_ACTION_BLUE = "com.hiveview.bluelight.ACTION_VIDEO_DETAILS";
	public static final String CONTENT_ACTION_BLUE_SUBJECT_DETAIL = "com.hiveview.bluelight.ACTION_SUBJECT_DETAILS";
	public static final String CONTENT_ACTION_APP_ALBUM = "com.hiveview.appstore.album";
	public static final String CONTENT_ACTION_APP_DETAIL = "com.hiveview.appstore.detail";
	public static final String CONTENT_ACTION_SUBJECTDETAIL = "com.hiveview.subject.detail";
	public static final String CONTENT_ACTION_BLUE_PLAYER = "com.hiveview.bluelight.ACTION_PLAYER";
	public static final String CONTENT_ACTION_BLUE_TELEPALY_DETAIL = "com.hiveview.bluelight.action.SUBJECT_TELEPALY_DETAIL";
	public static final String CONTENT_ACTION_BLUE__PREMIERE="com.hiveview.premiere";
	
	/* 海外vip跳转直播 */
	public static final String CONTENT_ACTION_ONLIVE="CONTENT_ACTION_ONLIVE";
	/* start by Zhaijianfeng */
	/**
	 * 为极清2.0添加action type
	 */
	public static final String CONTENT_ACTION_BLUE_2_HALL = "com.hiveview.bluelight.ACTION_HALL";//大厅
	public static final String CONTENT_ACTION_BLUE_2_SUBJECT = "com.hiveview.bluelight.ACTION_SUBJECT";//专题
	public static final String CONTENT_ACTION_BLUE_2_ALBUM = "com.hiveview.bluelight.ACTION_ALBUM";//专辑
	public static final String CONTENT_ACTION_BLUE_2_FORSALES = "com.hiveview.bluelight.ACTION_FORSALES";//促销
	/* end by Zhaijianfeng */
	private static ContentInvoker container = new ContentInvoker();
	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, String> map = new HashMap<Integer, String>();

	public static ContentInvoker getInstance() {

		if (map.size() == 0)
			initTips();

		return container;
	}

	public String getContentAction(Integer code) {
		int showType = CategoryDetailManager.getInstance().getShowTypeByCategoryId(code);
		Log.v("action", "CONTENT_ACTION_MOVIESUBJECT0"+showType);
		return map.get(showType);
	}

	private static void initTips() {
		map.put(ContentShowType.TYPE_MOVIE_SUBJECT, CONTENT_ACTION_MOVIESUBJECT);// 电影专题
		map.put(ContentShowType.TYPE_MOVIE_DETIAL, CONTENT_ACTION_MOVIEDETAIL);// 电影专辑
		map.put(ContentShowType.TYPE_SINGLE_VIDEO_DETAIL, CONTENT_ACTION_FILMDETAIL);// 电影
		map.put(ContentShowType.TYPE_MULTIPLE_VIDEO_DETAIL, CONTENT_ACTION_TELEPLAYDETAIL);// 电视剧,动漫
		map.put(ContentShowType.TYPE_VARIETY_VIDEO_DETAIL, CONTENT_ACTION_VARIETYDETAIL);// 综艺
		map.put(ContentShowType.TYPE_SINGLE_VIDEO_NO_DETAIL, null);// 体育，音乐，纪录片
		map.put(ContentShowType.TYPE_SUBJECT_VIDEO_DETAIL, CONTENT_ACTION_SUBJECTDETAIL);// 专题详情

		map.put(AppConstant.VIDEO_TYPE_CLIPS, CONTENT_ACTION_BLUE_PLAYER);
		map.put(AppConstant.VIDEO_TYPE_BLUE, CONTENT_ACTION_BLUE);
		//map.put(AppConstant.VIDEO_TYPE_BLUE_SUBJECT_DETAIL, CONTENT_ACTION_BLUE_SUBJECT_DETAIL);
		map.put(AppConstant.APP_TYPE_DETAIL, CONTENT_ACTION_APP_DETAIL);
		map.put(AppConstant.APP_TYPE_ALBUM, CONTENT_ACTION_APP_ALBUM);
		map.put(AppConstant.EDU_TYPE_DETAIL, CONTENT_ACTION_APP_DETAIL);
		map.put(AppConstant.EDU_TYPE_ALBUM, CONTENT_ACTION_APP_ALBUM);
		map.put(AppConstant.GAME_TYPE_DETAIL, CONTENT_ACTION_APP_DETAIL);
		map.put(AppConstant.GAME_TYPE_ALBUM, CONTENT_ACTION_APP_ALBUM);
		map.put(AppConstant.VIDEO_TYPE_SUBJECTLIST, CONTENT_ACTION_SUBJECTDETAIL);
		map.put(AppConstant.VIDEO_BLUE_TELEPALY_SUBJECTLIST, CONTENT_ACTION_BLUE_TELEPALY_DETAIL);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_HALL, CONTENT_ACTION_BLUE_2_HALL);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_SUBJECT, CONTENT_ACTION_BLUE_2_SUBJECT);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_ALBUM, CONTENT_ACTION_BLUE_2_ALBUM);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_FORSALES, CONTENT_ACTION_BLUE_2_FORSALES);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_PREMIERE, CONTENT_ACTION_BLUE__PREMIERE);
		map.put(AppConstant.VIDEO_TYPE_ONLIVE, CONTENT_ACTION_ONLIVE);
	}

}
