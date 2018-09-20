package com.hiveview.tv.common;

public interface AppConstant {

	String LIVE_ENTITY = "live_entity";
	String CHANNEL_CODE = "channel_code";
	String CHANNEL_LOGO = "channel_logo";
	String CHANNEL_NAME = "channel_name";
	String PRE_LOAD_IMAGE = "pre_load_image";
	String PRE_LOAD_IMAGE_KEY = "image_list";
	/**
	 * number from - 1 to 9
	 * */
	int NO_0 = 0;
	int NO_1 = 1;
	int NO_2 = 2;
	int NO_3 = 3;
	int NO_4 = 4;
	int NO_5 = 5;
	int NO_6 = 6;
	int NO_7 = 7;
	int NO_8 = 8;
	int NO_9 = 9;
	int NO_50 = 50;
	int NO_51 = 51;
	int NO_52 = 52;
	int NO_10 = 10;
	int NO_40 = 40;
	int NO_41 = 41;
	int NO_42 = 42;
	int NO_ERROR = -1;
	int NO_END = -1;

	int ERR_NetworkConnectException = 20;
	/**
	 * 1M
	 * */
	long SIZE_1M = 1048576L;

	/**
	 * 1M
	 * */
	long SIZE_100M = 104857600L;

	/**
	 * 1K
	 * */
	int BUFFER = 1024 * 100;

	/**
	 * common character for reusing
	 * */
	char CHARACTER_SLASH = '/';
	char CHARACTER_EQUAL = '=';
	char CHARACTER_AND = '&';
	char CHARACTER_QUESTION = '?';
	char CHARACTER_BARS = '-';

	/**
	 * seconds count
	 * */
	int SECOND_HALF = 500;
	int SECOND_1 = 1000;

	public static final String SP_COOKIES = "cookies";

	String DOMYBOXDIR = "DomyBoxLanucher/systemApp";

	String SELECT_TYPE_FILE = "电影";
	String SELECT_TYPE_TV = "电视剧";
	String SELECT_TYPE_CARTOON = "动漫";
	String SELECT_TYPE_MUSIC = "音乐";
	String SELECT_TYPE_SPORTS = "体育";
	String SELECT_TYPE_CHILDREN = "少儿";
	String SELECT_TYPE_TECHNOLOGY = "科技";
	String SELECT_TYPE_VARIETY = "综艺";
	String SELECT_TYPE_ENTERTAINMENT = "娱乐";
	String SELECT_TYPE_ECONOMICS = "科技";
	String SELECT_TYPE_COMPREHENSIVE = "综合";
	String SELECT_TYPE_CLIPS = "片花";
	String SELECT_TYPE_EDUCATION = "教育";
	String SELECT_TYPE_FASHION = "时尚.生活";
	String SELECT_TYPE_AMUSE = "搞笑";
	String SELECT_TYPE_TRAVEL = "旅游.纪录片";
	String SELECT_TYPE_BLUE = "蓝光极清";
	//start==>添加一个专题项 author：zhangpengzhan
	String SELECT_TYPE_SPECIAL = "专题";
	//end

	String SELECT_TYPE_CCTV = "央视";
	String SELECT_TYPE_SATELLITE = "卫视";
	String SELECT_TYPE_LOCAL = "本地";
	String SELECT_TYPE_HD = "高清频道";
	String SELECT_TYPE_SUB = "收费频道";
	/**
	 * 美国测试专用版使用时将其改成false
	 */
	public final static boolean ISDOMESTIC = true; 
	
	/**
	 * 电影类型
	 */
	public final static int VIDEO_TYPE_FILM = 1;
	/**
	 * 电视剧类型
	 */
	public final static int VIDEO_TYPE_TELEPLAY = 2;
	/**
	 * 动漫
	 */
	public final static int VIDEO_TYPE_CARTOON = 4;
	/**
	 * 音乐
	 */
	public final static int VIDEO_TYPE_MUSIC = 5;
	/**
	 * 综艺
	 */
	public final static int VIDEO_TYPE_VARIETY = 6;
	/**
	 * 娱乐
	 */
	public final static int VIDEO_TYPE_ENTERTAINMENT = 7;
	/**
	 * 极清片花
	 */
	public final static int VIDEO_TYPE_CLIPS = 11;
	/**
	 * 教育
	 */
	public final static int VIDEO_TYPE_EDUCATION = 12;
	/**
	 * 搞笑
	 */
	public final static int VIDEO_TYPE_AMUSE = 18;
	/**
	 * 时尚.生活
	 */
	public final static int VIDEO_TYPE_FASHION = 17;
	/**
	 * 体育
	 */
	public final static int VIDEO_TYPE_SPORT = 15;
	/**
	 * 
	 * 综艺
	 */
	//start 添加一个综艺 author:huzuwei
	public final static int VIDEO_TYPE_VARIETY_1 = 61;
	//end
	/**
	 * 旅游.纪录片
	 */
	public final static int VIDEO_TYPE_TRAVEL = 100;

	/**
	 * 极清详情
	 */
	public final static int VIDEO_TYPE_BLUE = 16;

	/**
	 * 极清专题详情
	 */
	public final static int VIDEO_TYPE_BLUE_SUBJECT_DETAIL = 1001;

	/**
	 * 专题
	 */
	public final static int VIDEO_TYPE_SUBJECTLIST = 1007;
	
	
	/**
	 * 专题
	 */
	public final static int VIDEO_BLUE_TELEPALY_SUBJECTLIST = 1009;

	/**
	 * 应用专题
	 */
	public final static int APP_TYPE_ALBUM = 3002;

	/**
	 * 片花
	 */
	public final static int VIDEO_TYPE_FLOWER = 1004;

	/**
	 * 应用详情
	 */
	public final static int APP_TYPE_DETAIL = 3001;
	
	/**
	 * 教育专题
	 */
	public final static int EDU_TYPE_ALBUM = 5002;


	/**
	 * 教育详情
	 */
	public final static int EDU_TYPE_DETAIL = 5001;
	
	/**
	 * 游戏专题
	 */
	public final static int GAME_TYPE_ALBUM = 4002;


	/**
	 * 游戏详情
	 */
	public final static int GAME_TYPE_DETAIL = 4001;
	
	/**
	 * 极清2.0大厅
	 */
	public final static int VIDEO_TYPE_BLUE_2_HALL = 2001;
	/**
	 * 极清2.0专题
	 */
	public final static int VIDEO_TYPE_BLUE_2_SUBJECT = 2002;
	/**
	 * 极清2.0专辑
	 */
	public final static int VIDEO_TYPE_BLUE_2_ALBUM = 2003;
	/**
	 * 极清2.0促销
	 */
	public final static int VIDEO_TYPE_BLUE_2_FORSALES = 2004;
	public final static int VIDEO_TYPE_BLUE_2_PREMIERE = 2006;
	/**
	 海外vip跳转直播
	 */
	public final static int VIDEO_TYPE_ONLIVE = 2007;
	
	public final static int BACKGROUND_GAME_INDEX = 0;// 游戏背景索引
	public final static int BACKGROUND_TELEPLAY_INDEX = 1;// 电视背景索引
	public final static int BACKGROUND_RECOMMAND_INDEX = 2;// 推荐背景索引
	public final static int BACKGROUND_MOVIE_INDEX = 3;// 影院背景索引
	public final static int BACKGROUND_APP_INDEX = 4;// 应用背景索引
	
	
	/**
	 * 分类筛选数据窗口标签
	 * @Fields SEARCH_DATE_RESULT
	 */
	public final static int SEARCH_DATE_RESULT = 0x00159;
	/**
	 * 换台广播
	 */
	public static String SWITCH_CHANNEL = "com.hiveview.tv.activity.LiveFastSelectTvChannelActivity";
	/**
	 * 换台讯飞发送广播   这个讯飞那边拼错了iflyek少了一个t
	 */
	public static String SWITCH_CHANNEL_XUNFEI = "com.iflyek.TVDCS.STBSTATUS";
	/**
	 * 换台讯飞发送广播   这个是拼正确  讯飞那边说两个都支持
	 */
	public static String SWITCH_CHANNEL_XUNFEI_NORMAL = "com.iflytek.TVDCS.STBSTATUS";

	/**
	 * 保存图片尺寸
	 * @ClassName ImageViewSize
	 * @Description 
	 * @author haozening
	 * @date 2014年8月20日 上午11:49:34
	 *
	 */
	public static class ImageViewSize{
		public int width;
		public int height;
	}
	
	/**
	 * @Fields IFly_Center_KeyCode 讯飞遥控器ok键键值
	 */
	public final static int IFly_Center_KeyCode = 66 ;
	
	/**
	 * 锁网私钥
	 */
	public final static String LOCK_NET_PRIVATE_KEY = "166ce8895c5c8cb9f0d46194e01e99338c61" ;
	/**
	 * ==============================================================================================
	 * =========================================统计参数=============================================
	 * ===============================================================================================
	 */
	
	/**
	 * 播放器来源 分类筛选
	 */
	public final static String SOURCE_FILTER = "1";
	/**
	 * 搜索
	 */
	public final static String SOURCE_SEARCH = "2";
	/**
	 * 推荐位
	 */
	public final static String SOURCE_ROMMAND = "3";
	/**
	 * 专题
	 */
	public final static String SOURCE_SUBJECT = "4";
	/**
	 * 相关推荐
	 */
	public final static String SOURCE_RELATED_RECOMMAND = "5";
	/**
	 * 收藏
	 */
	public final static String SOURCE_COLLECT = "6";
	/**
	 * 列表页
	 */
	public final static String SOURCE_LIST_PAGE = "7";
	/**
	 * 播放机路
	 */
	public final static String SOURCE_PLAYER_RECODER = "8";
}
