/**
 * @Title DataType.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月25日 下午6:13:30
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;



/**
 * 获取需要发送的Entity的字段名，字段名数组不同，就需要组合作为一个新的DataType
 * @ClassName DataType
 * @Description
 * @author haozening
 * @date 2014年6月25日 下午6:13:30
 * 
 */
public enum DataType {

	CLICK_TAB_FILM(new FieldsMap(
			new String[]{"cid","videosetType"},
			new String[]{"id","videosetId"},
			new String[]{"id", "videoId"}, // FilmEntity中没有videoId
			new String[]{"name","videosetName"},
			new String[]{"name", "videoName"}, // 
			new String[]{"cp", "cp"}),
			"com.hiveview.tv.service.entity.FilmNewEntity"),
			CLICK_TAB_SUBJECT(new FieldsMap(
					new String[]{"cid","videosetType"},
					new String[]{"id","videosetId"},
					new String[]{"id", "videoId"}, // FilmEntity中没有videoId
					new String[]{"name","videosetName"},
					new String[]{"name", "videoName"}, // 
					new String[]{"position_id", "positionId"}, // 
					new String[]{"subject_id", "subject_id"}, //
					new String[]{"subject_name", "subject_name"}, //
					new String[]{"cp", "cp"}),
					"com.hiveview.tv.service.entity.FilmNewEntity"),
			CLICK_TAB_VIDEO(new FieldsMap(
					new String[]{"cid","videosetType"},
					new String[]{"videosetId","videosetId"},
					new String[]{"videoId", "videoId"}, // FilmEntity中没有videoId
					new String[]{"epname","videosetName"},
					new String[]{"epname", "videoName"} // FilmEntity中没有videoName
					),
					"com.hiveview.tv.service.entity.VideoNewEntity"),
	TIME_LENGTH_MOVIE_DEMAND_LIST(new FieldsMap(
			new String[]{"firstClassId", "videosetType"},
			new String[]{"firstClassName", "videosetTypeName"}),
			"com.hiveview.tv.common.statistics.MovieDemandQueryInfo"),
	TIME_LENGTH_SUBJECT(new FieldsMap(
			new String[]{"subjectId", "subject_id"},
			new String[]{"subjectName", "subject_name"}),
			"com.hiveview.tv.service.entity.SubjectListEntity"),
	CLICK_TAB_RECOMMEND(new FieldsMap(
			new String[]{"focusType","videosetType"},
			new String[]{"contentId","videosetId"},
			new String[]{"contentDesc","videosetName"},
			new String[]{"contentId","videoId"},
			new String[]{"contentDesc","videoName"}),
			"com.hiveview.tv.service.entity.RecommendEntity"),
	CLICK_TAB_RECOMMEND_APP(new FieldsMap(
			new String[]{"focusType","videosetType"},
			new String[]{"contentId","appId"},
			new String[]{"contentDesc","appName"}),
			"com.hiveview.tv.service.entity.RecommendEntity"),
	CLICK_TAB_RECOMMEND_APP_SUBJECT(new FieldsMap(
					new String[]{"focusType","videosetType"},
					new String[]{"contentId","subject_id"},
					new String[]{"contentDesc","subject_name"}),
					"com.hiveview.tv.service.entity.RecommendEntity"),
	CLICK_TAB_RECOMMEND_VIDEO_SUBJECT(new FieldsMap(
							new String[]{"focusType","videosetType"},
							new String[]{"contentId","subject_id"},
							new String[]{"contentDesc","subject_name"}),
							"com.hiveview.tv.service.entity.RecommendEntity"),
	CLICK_TAB_GAME_BUTTON(new FieldsMap(
			new String[]{"buttonId", "buttonId"},
			new String[]{"buttonName","buttonName"}), 
			"com.hiveview.tv.common.statistics.ButtonInfo"),
	CLICK_TAB_APP_BUTTON(new FieldsMap(
			new String[]{"buttonId", "buttonId"},
			new String[]{"buttonName","buttonName"}), 
			"com.hiveview.tv.common.statistics.ButtonInfo"),
	CLICK_TAB_EDUCATION_BUTTON(new FieldsMap(
			new String[]{"buttonId", "buttonId"},
			new String[]{"buttonName","buttonName"}), 
			"com.hiveview.tv.common.statistics.ButtonInfo"),
	CLICK_TAB_GAME_RECOMMAND(new FieldsMap(
			new String[]{"contentId", "appId"},
			new String[]{"contentName", "appName"}),
			"com.hiveview.tv.service.entity.AppFocusEntity"),
	CLICK_TAB_APP_RECOMMAND(new FieldsMap(
			new String[]{"contentId", "appId"},
			new String[]{"contentName", "appName"}),
			"com.hiveview.tv.service.entity.AppFocusEntity"),
	CLICK_TAB_APP_GAME_RECOMMAND(new FieldsMap(
					new String[]{"contentId", "subject_id"},
					new String[]{"contentName", "subject_name"}),
					"com.hiveview.tv.service.entity.AppFocusEntity"),	
	CLICK_TAB_APP_EDUCATION_RECOMMAND(new FieldsMap(
					new String[]{"contentId", "subject_id"},
					new String[]{"contentName", "subject_name"}),
					"com.hiveview.tv.service.entity.AppFocusEntity"),
	CLICK_TAB_EDUCATION_RECOMMAND(new FieldsMap(
					new String[]{"contentId", "appId"},
					new String[]{"contentName", "appName"}),
					"com.hiveview.tv.service.entity.AppFocusEntity"),
	CLICK_TAB_BUTTON(new FieldsMap(
			new String[]{"buttonId", "buttonId"},
			new String[]{"buttonName", "buttonName"}), 
			"com.hiveview.tv.common.statistics.ButtonInfo"),
	/**
	 * 应用、游戏点击量统计（点击行为类）
	 */
	CLICK_APP_LIST(new FieldsMap(
			new String[]{"appId", "appId"},
			new String[]{"appName", "appName"},
			new String[]{"category_id", "category_id"},
			new String[]{"bundle_id","bundle_id"},
			new String[]{"tag_id", "tag_id"},
			new String[]{"tag_name", "tag_name"},
			new String[]{"operateType", "operateType"},
			new String[]{"source", "source"},
			new String[]{"userSource", "userSource"},
			new String[]{"noAction", "noAction"}),
			"com.hiveview.appstore.entity.AppStatisticsEntity"),
	/**
	 * 应用、游戏点击量统计（时长）
	 */
	CLICK_APP_LIST_RECORD_TIME(new FieldsMap(
			new String[]{"appId", "appId"},
			new String[]{"appName", "appName"},
			new String[]{"category_id", "category_id"},
			new String[]{"bundle_id","bundle_id"},
			new String[]{"tag_id", "tag_id"},
			new String[]{"tag_name", "tag_name"},
			new String[]{"operateType", "operateType"},
			new String[]{"source", "source"},
			new String[]{"userSource", "userSource"},
			new String[]{"noAction", "noAction"},
			new String[]{"startTime","startTime"},
			new String[]{"endTime","endTime"},
			new String[]{"timeLength","timeLength"}),
			"com.hiveview.appstore.entity.AppStatisticsEntity"),
	/**
	 * 应用、游戏分类筛选点位点击量统计（点击行为类）
	 */
	CLICK_APP_CATEGORY(new FieldsMap(
			new String[]{"tag_id", "tag_id"},
			new String[]{"tag_name", "tag_name"},
			new String[]{"category_id", "category_id"}),
			"com.hiveview.appstore.entity.AppStatisticsEntity"),

	/**
	 * 应用、游戏专题点击量（点击行为类）
	 */
	CLICK_APP_SUBJECT(new FieldsMap(
			new String[]{"positionId", "positionId"},
			new String[]{"subject_id", "subject_id"},
			new String[]{"subject_name", "subject_name"},
			new String[]{"appId", "appId"},
			new String[]{"appName", "appName"},
			new String[]{"category_id", "category_id"},
			new String[]{"bundle_id","bundle_id"}),
			"com.hiveview.appstore.entity.AppStatisticsEntity"),

	/**
	 * 应用、游戏排行榜点击量（点击行为类）
	 */
	CLICK_APP_RANK_BUTTON(new FieldsMap(
			new String[]{"category_id", "category_id"},
			new String[]{"topType", "topType"}),
			"com.hiveview.appstore.entity.AppStatisticsEntity"),
	/**
	 * 应用、游戏排行榜点位点击量（点击行为类）
	 */
	CLICK_APP_RANK_POSITION(new FieldsMap(
			new String[]{"positionId", "positionId"},
			new String[]{"appId", "appId"},
			new String[]{"appName", "appName"},
			new String[]{"category_id", "category_id"},
			new String[]{"topType", "topType"},
			new String[]{"bundle_id","bundle_id"}),
			"com.hiveview.appstore.entity.AppStatisticsEntity"),
	/**
	 * 频道分类
	 */
	CLICK_FIRST_CLASS(new FieldsMap(
			new String[]{"firstclass_id", "firstclass_id"},
			new String[]{"firstclass_name", "firstclass_name"}), 
			"com.hiveview.tv.service.entity.FirstClassListEntity");
	
	
	private FieldsMap fields;
	private String clazz;

	private DataType(FieldsMap fields, String clazz) {
		this.fields = fields;
		this.clazz = clazz;
	}
	
	public String getClazz() {
		return clazz;
	}

	public FieldsMap getFields() {
		return fields;
	}
	
}
