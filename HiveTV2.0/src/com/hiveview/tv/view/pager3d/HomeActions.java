package com.hiveview.tv.view.pager3d;

public class HomeActions {

	/**
	 * 应用游戏接受下载进度的Action
	 */
	public final static String ACTION_DOWNLOAD = "com.hiveview.appstore.home_download_progress";
	/**
	 * 应用市场安装应用游戏成功的Action
	 */
	public final static String ACTION_INSTALL_APP_SUCCESS = "com.hiveview.appstore.home_install_success";
	/**
	 * 应用市场安装应用游戏失败的Action
	 */
	public final static String ACTION_INSTALL_APP_FAIL = "com.hiveview.appstore.home_install_fail";
	/**
	 * 应用市场安装应用下载中断网下载中断Action
	 */
	public final static String ACTION_DOWNLOAD_PAUSE="com.hiveview.appstore.download_pause";
	/**
	 * 推荐页面推荐大图加载完成的，可以给用户显示界面的Action
	 */
	public final static String ACTION_RECOMMEND_LARGE_LOAD_COMPLETE = "com.hiveview.tv.large_image_complete";
	/**
	 * 刷新推荐页面的推荐位数据的Action
	 */
	public static final String REFLESH_HOME_RECOMMEND_ACTION = "com.hiveview.tv.REFLESH_HOME_RECOMMEND_ACTION";
	/**
	 * 刷新App页面推荐位数据的Action
	 */
	public static final String REFLESH_HOME_APP_ACTION = "com.hiveview.tv.REFLESH_HOME_APP_ACTION";
	/**
	 * 刷新游戏页面推荐位数据的Action
	 */
	public static final String REFLESH_HOME_GAME_ACTION = "com.hiveview.tv.REFLESH_HOME_GAME_ACTION";
	/**
	 * 刷新影院页面推荐位数据的Action
	 */
	public static final String REFLESH_HOME_MOVIE_ACTION = "com.hiveview.tv.REFLESH_HOME_MOVIE_ACTION";
	/**
	 * 刷新安装游戏应用的个数的Action
	 */
	public final static String REFLESH_APP_GAME_INSTALL_COUNT_ACTION = "com.hiveview.appstore.refresh";
	/**
	 * 通知“电视”页面刷新右侧推荐或置顶电视台Action
	 */
	public static final String REFLESH_TV_RIGHT_ACTION = "com.hiveview.tv.TV_CHANNEL_SET_TOP_ACTION";
	/**
	 * 通知“电视”页面置顶右侧推荐或置	顶电视台Action 后刷新
	 */
	public static final String SET_TOP_TV_RIGHT_ACTION = "com.hiveview.tv.TV_CHANNEL_SET_TOP";

	/**
	 * 用户其他途径卸载应用游戏的Action
	 */
	public static final String REFLESH_APP_GAME_WHEN_USER_UNINSTALL_BY_OTHER_ACTION = "com.hiveview.tv.TV_CHANNEL_SET_TOP_ACTION";

	/**
	 * 用户其他途径卸载应用游戏的Action
	 */
	public static final String REFLESH_APP_GAME_UPDATE_ACTION = "com.hiveview.appstore.update_number";
	
	/**
	 * 开机启动鉴权失败发送的广播
	 */
	public static final String BOX_START_DEVICE_CHECK_FAIL_ACTION = "com.hiveview.tv.REFLESH_DEVICE_CHECK_FAIL_ACTION";
	
	public static final String NETWORK_CHANGE_ACTION="android.net.conn.CONNECTIVITY_CHANGE";
	
	/**
	 * 静默升级检测广播
	 */
	public static final String NEWUPGRADER_ACTION = "com.hiveview.tv.NEWUPGRADER_ACTION";
	/**
	 * 静默升级取消检测广播
	 */
	public static final String NEWUPGRADER_CANCEL_ACTION = "com.hiveview.tv.NEWUPGRADER_CANCEL_ACTION";
	
}
