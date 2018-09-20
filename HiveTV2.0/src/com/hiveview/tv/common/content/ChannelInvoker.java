package com.hiveview.tv.common.content;

import java.util.HashMap;

import android.annotation.SuppressLint;

import com.hiveview.tv.common.AppConstant;

public class ChannelInvoker {

	private static ChannelInvoker container = new ChannelInvoker();

	/**
	 * 显示竖图（电影，电视剧，动漫）列表页Activity的Action
	 */
	private static final String VIEDEO_VERTICAL_LIST_ACTION = "com.hiveview.video.list.VERTICAL";

	/**
	 * 显示横图（娱乐，体育，音乐，纪录片）列表页Activity的Action
	 */
	public static final String VIEDEO_HORIZONTAL_LIST_ACTION = "com.hiveview.video.list.HORIZONTAL";

	/**
	 * 显示蓝光极清电影列表页Activity的Action
	 */
	private static final String VIEDEO_BLUELIGHT_LIST_ACTION = "com.hiveview.bluelight.VIDEOLIST";

	/**
	 * 显示专题（娱乐，体育，音乐，纪录片）列表页Activity的Action
	 */
	private static final String VIEDEO_SUBJECT_LIST_ACTION = "com.hiveview.video.SUBJECTLIST";
	@SuppressLint("UseSparseArrays")
	private static HashMap<Integer, String> map = new HashMap<Integer, String>();

	public static ChannelInvoker getInstance() {

		if (map.size() == 0)
			initTips();

		return container;
	}

	public String getContent(Integer code) {

		return map.get(code);
	}

	private static void initTips() {
		map.put(ContentShowType.TYPE_SINGLE_VIDEO_DETAIL, VIEDEO_VERTICAL_LIST_ACTION);
		map.put(ContentShowType.TYPE_SINGLE_VIDEO_NO_DETAIL, VIEDEO_HORIZONTAL_LIST_ACTION);
		map.put(ContentShowType.TYPE_MULTIPLE_VIDEO_DETAIL, VIEDEO_VERTICAL_LIST_ACTION);
		map.put(ContentShowType.TYPE_VARIETY_VIDEO_DETAIL, VIEDEO_VERTICAL_LIST_ACTION);
		map.put(ContentShowType.TYPE_SUBJECT_VIDEO_DETAIL, VIEDEO_SUBJECT_LIST_ACTION);
	}

}
