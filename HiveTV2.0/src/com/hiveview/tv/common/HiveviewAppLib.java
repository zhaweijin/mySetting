package com.hiveview.tv.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HiveviewAppLib {

	public static Map<Integer,String> maps = new HashMap<Integer,String>();
	
	public static Set<Integer> videoTypeSet = new HashSet<Integer>();
	
	static{
		maps.put(AppConstant.VIDEO_TYPE_FILM, AppConstant.SELECT_TYPE_FILE);
		maps.put(AppConstant.VIDEO_TYPE_TELEPLAY, AppConstant.SELECT_TYPE_TV);
		maps.put(AppConstant.VIDEO_TYPE_CARTOON, AppConstant.SELECT_TYPE_CARTOON);
		maps.put(AppConstant.VIDEO_TYPE_MUSIC, AppConstant.SELECT_TYPE_MUSIC);
		maps.put(AppConstant.VIDEO_TYPE_VARIETY, AppConstant.SELECT_TYPE_VARIETY);
		maps.put(AppConstant.VIDEO_TYPE_ENTERTAINMENT, AppConstant.SELECT_TYPE_ENTERTAINMENT);
		maps.put(AppConstant.VIDEO_TYPE_CLIPS, AppConstant.SELECT_TYPE_CLIPS);
		maps.put(AppConstant.VIDEO_TYPE_EDUCATION, AppConstant.SELECT_TYPE_EDUCATION);
		maps.put(AppConstant.VIDEO_TYPE_SPORT, AppConstant.SELECT_TYPE_SPORTS);
		maps.put(AppConstant.VIDEO_TYPE_FASHION, AppConstant.SELECT_TYPE_FASHION);
		maps.put(AppConstant.VIDEO_TYPE_AMUSE, AppConstant.SELECT_TYPE_AMUSE);
		maps.put(AppConstant.VIDEO_TYPE_TRAVEL, AppConstant.SELECT_TYPE_TRAVEL);
		maps.put(AppConstant.VIDEO_TYPE_BLUE, AppConstant.SELECT_TYPE_BLUE);
		maps.put(AppConstant.VIDEO_TYPE_FLOWER, AppConstant.SELECT_TYPE_CLIPS);
		//start 添加一个综艺项 author:huzuwei
		maps.put(AppConstant.VIDEO_TYPE_VARIETY_1, AppConstant.SELECT_TYPE_VARIETY);
		//end
		
		
		//start 添加一个专题的项  author：zhangpengzhan
		maps.put(AppConstant.VIDEO_TYPE_SUBJECTLIST, AppConstant.SELECT_TYPE_SPECIAL);
		//end
	}
	
	public static void putSearchVideoType(int type){
		videoTypeSet.add(type);
	}
}
