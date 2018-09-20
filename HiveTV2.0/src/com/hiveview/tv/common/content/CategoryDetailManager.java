package com.hiveview.tv.common.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.net.HttpTaskManager;

@SuppressLint("UseSparseArrays")
public class CategoryDetailManager {

	private HashMap<Integer, Integer> map = null;
	private ChannelDAO dao;
	protected String tag = "CategoryDetailManager";
	{

	}

	private static CategoryDetailManager manager = null;

	private CategoryDetailManager() {
		try {
			if (null == map || map.isEmpty())
				init(HiveviewApplication.mContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static CategoryDetailManager getInstance() {
		if (null == manager) {
			manager = new CategoryDetailManager();
		}
		return manager;
	}

	/**
	 * 初始化Map格式的category-showtype对应表
	 * 
	 * @Title: CategoryDetailManager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param context
	 */
	public void init(Context context) {
		map = new HashMap<Integer, Integer>();
		dao = new ChannelDAO(context);
		map = dao.queryShowTypeMap();
		Log.d(tag, "find the channel form dao");
		map.put(AppConstant.VIDEO_TYPE_CLIPS, AppConstant.VIDEO_TYPE_CLIPS);
		map.put(AppConstant.VIDEO_TYPE_BLUE, AppConstant.VIDEO_TYPE_BLUE);
	//	map.put(AppConstant.VIDEO_TYPE_BLUE_SUBJECT_DETAIL, AppConstant.VIDEO_TYPE_BLUE_SUBJECT_DETAIL);
		map.put(AppConstant.APP_TYPE_DETAIL, AppConstant.APP_TYPE_DETAIL);
		map.put(AppConstant.APP_TYPE_ALBUM, AppConstant.APP_TYPE_ALBUM);
		map.put(AppConstant.EDU_TYPE_DETAIL, AppConstant.EDU_TYPE_DETAIL);
		map.put(AppConstant.EDU_TYPE_ALBUM, AppConstant.EDU_TYPE_ALBUM);
		map.put(AppConstant.GAME_TYPE_DETAIL, AppConstant.GAME_TYPE_DETAIL);
		map.put(AppConstant.GAME_TYPE_ALBUM, AppConstant.GAME_TYPE_ALBUM);
		map.put(AppConstant.VIDEO_TYPE_SUBJECTLIST, AppConstant.VIDEO_TYPE_SUBJECTLIST);
		map.put(AppConstant.VIDEO_BLUE_TELEPALY_SUBJECTLIST, AppConstant.VIDEO_BLUE_TELEPALY_SUBJECTLIST);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_HALL, AppConstant.VIDEO_TYPE_BLUE_2_HALL);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_SUBJECT, AppConstant.VIDEO_TYPE_BLUE_2_SUBJECT);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_ALBUM, AppConstant.VIDEO_TYPE_BLUE_2_ALBUM);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_FORSALES, AppConstant.VIDEO_TYPE_BLUE_2_FORSALES);
		map.put(AppConstant.VIDEO_TYPE_BLUE_2_PREMIERE, AppConstant.VIDEO_TYPE_BLUE_2_PREMIERE);
		map.put(AppConstant.VIDEO_TYPE_ONLIVE, AppConstant.VIDEO_TYPE_ONLIVE);
	}

	/**
	 * 根据类型ID返回，此类型需要响应的show_type类型，可根据show_type确定响应详情页
	 * 
	 * @Title: CategoryDetailManager
	 * @author:陈丽晓
	 * @param categoryId
	 * @return
	 */
	public int getShowTypeByCategoryId(int categoryId) {
		int showType = 0;

		if (null == map || map.size() == 0) {
			// init(HiveviewApplication.mContext);
			return 0;
		}
		if (categoryId == 1001) {// 影院专题
			showType = 1001;
		} else if (categoryId == 1002) {// 影院专辑
			showType = 1002;
		} 
		else {
			showType = null != map.get(categoryId) ? map.get(categoryId) : 0;
		}
		return showType;
	}
}
