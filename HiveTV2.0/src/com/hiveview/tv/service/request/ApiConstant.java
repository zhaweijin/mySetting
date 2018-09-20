package com.hiveview.tv.service.request;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;

public class ApiConstant {

	public static String UUID = "df1909e7c94af8e51ad62aa72e3be02a";

	public static String APP_VERSION = HiveviewApplication.getVersionName();
	
	public static String BLUELIGHT_TYPE = "1";

	/**
	 * 家视头地址125.39.118.36/data/open/epg/get.json
	 */
	// String HIVEVIEW_DOMAIN = "http://api.domybox.com";
	// 正式地址
	public static String HIVEVIEW_DOMAIN = "http://api.pthv.gitv.tv";
	// 新接口地址
	public static String HIVEVIEW_DOMAIN_TEST =AppConstant.ISDOMESTIC?"http://api.newapi.pthv.gitv.tv":"http://api.newapi.usa.domybox.com";
	//public static String HIVEVIEW_DOMAIN_TEST =AppConstant.ISDOMESTIC?"http://125.39.118.36:8095":"http://api.newapi.usa.domybox.com";
//	String HIVEVIEW_DOMAIN_TEST = "http://125.39.118.36:8095";
	
	/**
	 * 海外版测试直播流
	 */
	public static String HIVEVIEW_TV_LIST="http://125.39.118.61:8989";
	
	/**
	 * 获取直播流
	 */
	public static String HIVEVIEW_TV_API_GET_TV_LIST=HIVEVIEW_DOMAIN_TEST+"/api/open/carousel/getTvLists-%s-%s-%s-%s-%s.json";;

	/**
	 * 静默升级头地址 测试
	 */
	public static String HIVEVIEW_UPGRADER =AppConstant.ISDOMESTIC? "http://otaupdate.pthv.gitv.tv":"http://otaupdate.domybox.com";

	// String HIVEVIEW_DOMAIN = "http://124.207.119.79:8090";
	// String HIVEVIEW_DOMAIN = "http://124.192.132.203";
	// String HIVEVIEW_DOMAIN = "http://125.39.118.36";
	/**
	 * 锁网地址 测试
	 */
	public static String HIVEVIEW_LOCK_NET = "http://api.pthv.gitv.tv";
	/**
	 * 获取设备码
	 */
	public static  String HIVEVIEW_API_DOMYBOX = "http://deviceapi.pthv.gitv.tv";
	/**
	 * 首页推荐位URLString HIVEVIEW_TV_API_GET_HOME_RECOMMAND = HIVEVIEW_DOMAIN +
	 * "/api/open/home/getFocusList/%s" + ".json";
	 * "/api/open/home/getFocusListBynetWorkType-%s-%s" + ".json"
	 */

	public static String HIVEVIEW_TV_API_GET_HOME_RECOMMAND = HIVEVIEW_DOMAIN + "/api/open/home/getFocusListBynetWorkType-%s-%s" + ".json";

	/**
	 * 影院推荐位URL
	 */
	public static String HIVEVIEW_TV_API_GET_MOVIE_RECOMMAND = HIVEVIEW_DOMAIN + "/api/open/home/getVideoSetRecomList/%s" + ".json";
	/**
	 * 极清推荐位URL
	 */
	public static String HIVEVIEW_TV_API_GET_BLUELIGHT_RECOMMAND = HIVEVIEW_DOMAIN + "/api/open/home/getFocusList/%s/%s" + ".json";
	/**
	 * 鉴权URL
	 */
	public static String HIVEVIEW_TV_API_DEVICE_CHECK = HIVEVIEW_DOMAIN + "/api/sys/deviceCheck" + ".json";
	/**
	 * 热词，联想词URL
	 */
	public static String HIVEVIEW_TV_API_HOT_WORD = HIVEVIEW_DOMAIN_TEST + "/api/open/video/hotWordList/%s/" + APP_VERSION + ".json";

	/**
	 * 专题列表接口 api.domybox.com<br/>
	 */

	public static String HIVEVIEW_API_GET_SUBJECT_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/subject/getSubjectList/%s-%s-%s-%s-%s%s-%s.json";

	/**
	 * 专题详细接口 api.domybox.com<br/>
	 */
	public static String HIVEVIEW_API_GET_SUBJECT_INFO = HIVEVIEW_DOMAIN_TEST + "/api/open/subject/getSubjectContentList/%s-%s-%s-%s-%s-%s.json";
	/**
	 * 剧集列表接口 api.domybox.com<br/>
	 * http://{Server}/api/open/video/VideoList/{videoset_id}/{video_type}/{year
	 * }/{page_size}/{page_number}/{version}.json
	 */
	public static String HIVEVIEW_API_GET_VIDEO_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/video/VideoList/%s/%s/%s/%s/%s.json";
	/**
	 * 剧集列表接口 api.domybox.com<br/>
	 * http://{Server}/api/open/video/VideoList/{videoset_id}/{video_type}/{year
	 * }/{page_size}/{page_number}/{version}.json
	 */
	public static String HIVEVIEW_API_GET_VIDEO_BY_YEAR_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/video/VideoList/%s/%s/%s/%s/%s/%s.json";

	/**
	 * 电影详情接口 api.domybox.com<br/>
	 * http://{Server}/api/open/video/VideoSetDetail/{videoset_id}/{version}.
	 * json
	 */
	public static String HIVEVIEW_API_GET_FILM_DETAIL = HIVEVIEW_DOMAIN_TEST + "/api/open/video/VideoSetDetail/%s/%s.json";

	/**
	 * 我们的视频详情 请求方式： GET
	 * 接口地址：http://{Server}/api/open/video/getVideoSet-{cp_videoset_id
	 * }-{version}.json
	 */
	public static 	String HIVEVIEW_API_GET_FILM_DETAL_REAL = HIVEVIEW_DOMAIN + "/api/open/video/getVideoSet/%s/%s.json";
	/**
	 * 电影相关推荐接口 api.domybox.com<br/>
	 * http://{Server}/api/open/relatedRecom/getRelatedRecom/{videoSet_id}/{
	 * version}.json
	 */
	public static String HIVEVIEW_API_GET_FILM_RELATED_FILM = HIVEVIEW_DOMAIN + "/api/open/relatedRecom/getRelatedRecom/%s/%s.json";
	/**
	 * 演员详情接口 api.domybox.com<br/>
	 * http://{Server}/api/open/cast/getCastInfo/{cast_id}/{version}.json
	 */
	public static String HIVEVIEW_API_GET_CAST_DETAIL = HIVEVIEW_DOMAIN + "/api/open/cast/getCastInfo/%s/%s.json";
	/**
	 * 演员相关推荐接口 api.domybox.com<br/>
	 * http://{Server}/api/open/relatedRecom/getCastRelated/{cast_id}/{version}.
	 * json
	 */
	public static String HIVEVIEW_API_GET_CAST_RELATED_FILM = HIVEVIEW_DOMAIN + "/api/open/relatedRecom/getCastRelated/%s/%s.json";

	/**
	 * 电影分类接口 api.domybox.com<br/>
	 * http://{Server}/api/open/video/ThirdClassList/{videoset_type}/{version}.
	 * json
	 */
	public static 	String HIVEVIEW_API_GET_FILM_CATEGORY = HIVEVIEW_DOMAIN + "/api/open/video/ThirdClassList/%s/%s.json";

	/**
	 * 收藏记录列表接口 api.domybox.com<br/>
	 */
	public static String HIVEVIEW_API_GET_COLLECT_LIST = HIVEVIEW_DOMAIN + "/api/device/getCollectRecordList/%s/%s/%s.json";
	/**
	 * 保存收藏记录接口 api.domybox.com<br/>
	 */
	public static 	String HIVEVIEW_API_GET_SAVE_COLLECT_LIST = HIVEVIEW_DOMAIN + "/api/device/saveCollectRecord/%s/%s/%s.json";

	/**
	 * 删除收藏记录接口 api.domybox.com<br/>
	 */
	public static String HIVEVIEW_API_GET_DELETE_COLLECT_LIST = HIVEVIEW_DOMAIN + "/api/device/deleteCollectRecord/%s/%s.json";
	/**
	 * 人物 导演详细信息接口 api.domybox.com<br/>
	 */
	public static String HIVEVIEW_API_GET_CAST_INFO = HIVEVIEW_DOMAIN + "/api/open/cast/getCastInfo/%s/%s.json";

	/**
	 * 电影频道接口 api.domybox.com<br/>
	 * http://{Server}//api/open/video/FirstClassList/%s/%s/%s.json
	 */
	public static String HIVEVIEW_API_GET_FILM_FIRSTCLASS = HIVEVIEW_DOMAIN_TEST + "/api/open/video/FirstClassList/%s%s/%s.json";

	/**
	 * 欢网头地址http://epg-hlive.pthv.gitv.tv/data/open/epg/get.json
	 */
	public static String HUANWANG_DOMAIN = "http://epg-hlive.pthv.gitv.tv/data/open/epg/get.json";// 正式地址
	// String HUANWANG_DOMAIN = "http://epg.pthv.gitv.tv/json";// 测试地址
	/**
	 * 获取直播标签URL
	 */
	public static String HUANWANG_GETLIVECATEGORY = "GetLiveCategory";

	/**
	 * 根据电视剧的名称获取正在直播的电视台
	 */
	public static String HUANWANG_CHANNELSBYPROGRAM = "SearchProgram";

	/**
	 * 获取推荐的资源列表URL
	 */
	public static 	String HUANWANG_RECOMMENDMEDIAS = "GetRecommendMedias";

	/**
	 * 根据频道获取节目单的URL
	 */
	public static String HUANWANG_GETPROGRAMCHANNEL = "GetProgramsByChannel";

	/**
	 * 根据频道Codes(多个Code，如["cctv1","cctv2"])获取节目单 修改方法为：GetProgramsByChannelsPbs
	 */
	public static 	String HUANWANG_GETPROGRAMBYCODES = "GetProgramsByChannelsPbs";

	/**
	 * 根据频道多个channelName(多个channelName，如["Cctv-1","Cctv-2"])获取channelCode
	 */
	public static String HUANWANG_GETCHANEEL_CODE_NAME = "GetChannelsByNames";

	/**
	 * 获取频道直播推荐
	 */
	public static String HUANWANG_GETCHANEEL_RECOMMEND = "GetChannelLiveRecommend";

	/**
	 * 根据标签（电影，电视剧）获取直播的媒体
	 */
	public static String HUANWANG_GETMEDIASBYTAG = "GetLiveProgrameByTag";

	/**
	 * 获取欢往返快速选台的标签(如：央视，卫视等)
	 */
	public static String HUANWANG_GETCHANNELCATEGORY = "GetChannelCategory";

	/**
	 * 根据快速选台的标签（央视，卫视等）获取频道
	 */
	public static String HUANWANG_GETCHANNELSBYSP = "GetChannelsBySP";

	/**
	 * 电影详情中的相关推荐接口 corp.itv.iqiyi.com -> corp.itv.ptqy.gitv.tv
	 */
	// String AIQIYI_RELATIVERECOMMANDS =
	// "http://corp.itv.ptqy.gitv.tv/itv/albumLike/7dd4b4005b321be8a0fd49fc6ddc95f1/0/%s//%s/%s";
	public static String AIQIYI_RELATIVERECOMMANDS = HIVEVIEW_DOMAIN_TEST + "/api/open/video/favor_list/%s/%s/%s.json";
	// String AIQIYI_RELATIVERECOMMANDS =
	// "http://corp.itv.iqiyi.com/itvqiyitest/albumLike/7dd4b4005b321be8a0fd49fc6ddc95f1/0/%s/%s/%s";

	/**
	 * 
	 * 得到专辑列表接口
	 */
	public static String HIVEVIEW_API_GET_FILM_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/video/VideoSetList/%s/%s/%s/%s.json";

	/**
	 * 
	 * 得到专辑筛选接口
	 */
	public static String HIVEVIEW_API_GET_FILM_LIST_BY_TAG = HIVEVIEW_DOMAIN_TEST + "/api/open/video/VideoSetListByTag/%s/%s/%s/%s/%s.json";

	/**
	 * 获取各个页面的背景
	 */
	public static String HIVEVIEW_API_GET_BG_LIST = HIVEVIEW_DOMAIN + "/api/open/home/getModuleSkinList/%s.json";

	/**
	 * 搜索接口
	 * */
	public static String HIVEVIEW_API_POST_SEARCH_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/video/so.json";

	/**
	 * 按人物搜索
	 * */
	public static 	String HIVEVIEW_API_SEARCH_COUNT_BY_HUMAN_LIST = HIVEVIEW_DOMAIN + "/api/open/video/getCastWithCount/%s/%s/" + APP_VERSION + ".json";

	/**
	 * 首页、应用、游戏、教育、极清页面焦点图请求
	 * */
	public static 	String HIVEVIEW_API_APP_FOCUS_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/focus/getfocusList/%s%s-%s.json";

	/**
	 * 搜索接口 api.domybox.com<br/>
	 * http://{Server}/api/open/video/search/{version}.json
	 */
	public static 	String HIVEVIEW_API_GET_SEARCH = HIVEVIEW_DOMAIN + "/api/open/video/search" + APP_VERSION + ".json";

	/**
	 * 升级接口
	 */
	public static 	String HIVEVIEW_API_GET_SYS_UPDATA = HIVEVIEW_DOMAIN + "/api/open/sys/version/getInfo/%s.json";
	/**
	 * 系统应用列表接口 api.domybox.com
	 * http://<Server>/api/open/app/getAppList/<pageNo>/
	 * <pageSize>/<version>.json
	 */
	public static 	String HIVEVIEW_API_GET_SYSTEM_APP_LIST = HIVEVIEW_DOMAIN + "/api/open/app/getAppList/1/100/%s.json?apptype=0";
	/**
	 * 获取分类筛选标签 的接口
	 * 
	 * @Fields HIVEVIEW_API_GET_THIRDLIST_RESULT_LIST
	 */
	public static 	String HIVEVIEW_API_GET_THIRDLIST_RESULT_LIST = HIVEVIEW_DOMAIN_TEST + "/api/open/video/ThirdClassList/%s/%s.json";

	/**
	 * 分类筛选数据的接口 video_type , tag , page_size , page_number , version
	 * 
	 * @Fields HIVEVIEW_API_GET_VIDEO_SET_LIST_BY_TAG
	 */
	public static String HIVEVIEW_API_GET_VIDEO_SET_LIST_BY_TAG = HIVEVIEW_DOMAIN_TEST + "/api/open/video/VideoSetListByTag/%s/%s/%s/%s/%s.json";
	/**
	 * 静默升级地址
	 */
	public static String HIVEVIEW_API_UPGRADER = HIVEVIEW_UPGRADER + "/otaupdate/silentupdate.json";
	/**
	 * 获取锁网标示
	 */
	public static String HIVEVIEW_API_LOCK_NET = HIVEVIEW_LOCK_NET + "/api/d/isLockNetWork/%s/%s/%s/%s.json";
	/**
	 * 海外是否冻结盒子接口
	 */
	public static String HIVEVIEW_API_LOCK_VIP = "http://api.newapi.usa.domybox.com/abroad-vip/api/s/query.json";
	
	/**
	 * 获取设备码
	 */
	public static String API_GET_DEVICES_CODE = HIVEVIEW_API_DOMYBOX
			+ "/device/code.json?mac=%S&sn=%S";
}
