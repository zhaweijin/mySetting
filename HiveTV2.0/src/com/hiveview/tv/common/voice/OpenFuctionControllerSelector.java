package com.hiveview.tv.common.voice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * activity 控制器
 * 
 * @ClassName: OpenFuctionControllerSelector
 * @Description:
 * @author: guosongsheng
 * @date 2014-10-31 下午1:42:35
 * 
 */
public class OpenFuctionControllerSelector {
	/**
	 * TAG
	 */
	private static final String TAG = "VoiceController";
	/**
	 * 打开播放记录
	 */
	public static final String PLAY_RECORDS = "play_records";
	/**
	 * 打开影片搜索
	 */
	public static final String MOVIE_SEARCH = "movie_search";
	/**
	 * 打开已安装的应用游戏
	 */
	public static final String INSTALLED_APPS_GAMES = "installed_apps_games";
	/**
	 * 打开影片收藏
	 */
	public static final String MOVIE_FAVORITES = "movie_favorites";
	/**
	 * 打开外接存储
	 */
	public static final String EXTERNAL_STORAGE = "external_storage";
	/**
	 * 打开应用商店
	 */
	public static final String APP_STORE = "app_store";
	/**
	 * 打开应用排行榜
	 */
	public static final String APPLICATION_LEADERBOARD = "application_leaderboard";
	/**
	 * 打开搜索应用
	 */
	public static final String SEARCH_APPLICATIONS = "search_applications";
	/**
	 * 打开游戏中心
	 */
	public static final String GAME_CENTER = "game_center";
	/**
	 * 打开游戏排行榜
	 */
	public static final String GAME_LIST = "game_list";
	/**
	 * 打开搜索游戏
	 */
	public static final String SEARCH_GAMES = "search_games";
	/**
	 * 打开极清影院
	 */
	public static final String BLUE_LIGHT_CINEMA = "blue_light_cinema";
	/**
	 * 打开快速选台
	 */
	public static final String LIVE_FAST_SELECT_TV_CHANNEL = "live_fast_select_tv_channel";
	/**
	 * 打开分类选台
	 */
	public static final String LIVE_SELECT_TELEVISION = "live_select_television";
	/**
	 * 打开直播提醒
	 */
	public static final String ONLIVE_TIPS = "onlives_tips";
	/**
	 * 打开天气设置
	 */
	public static final String WEATHER_MANAGE = "weather_manage";
	/**
	 * 打开网络设置
	 */
	public static final String NET_MANAGE = "net_manage";
	/**
	 * 打开清除缓存
	 */
	public static final String CLEAN_MEMORY = "clean_memory";
	/**
	 * 打开升级
	 */
	public static final String CHECK_VERSION = "check_version";
	/**
	 * 打开播放设置
	 */
	public static final String PLAY_MANAGER = "play_manager";
	/**
	 * 打开音频解码
	 */
	public static final String AUDIO_DECODE = "audio_decode";
	/**
	 * 打开屏保
	 */
	public static final String SCREEN_SAVE = "screen_save";
	/**
	 * 打开显示设置
	 */
	public static final String DISPLAY = "display";
	/**
	 * 打开机顶盒设置
	 */
	public static final String TV_SET = "tv_set";
	/**
	 * 打开关于
	 */
	public static final String ABOUT = "about";
	/**
	 * 向上
	 */
	public static final String DROP_UP = "drop_up";
	/**
	 * 向下
	 */
	public static final String DROP_DOWN = "drop_down";
	/**
	 * 向左
	 */
	public static final String DROP_LEFT = "drop_left";
	/**
	 * 向右
	 */
	public static final String DROP_RIGHT = "drop_right";
	/**
	 * 首页
	 */
	public static final String DROP_HOME = "drop_home";
	/**
	 * 确定
	 */
	public static final String DROP_CENTER = "drop_center";
	/**
	 * 重启
	 */
	public static final String DROP_REBOOT= "drop_reboot";
	/**
	 * 打开教育中心
	 */
	public static final String EDU_CENTER = "edu_store";
	
	/**
	 * 功能列表
	 */
	private static String[] switchFuctionCommands = { PLAY_RECORDS, MOVIE_SEARCH, INSTALLED_APPS_GAMES, MOVIE_FAVORITES, EXTERNAL_STORAGE, APP_STORE,
			APPLICATION_LEADERBOARD, SEARCH_APPLICATIONS, GAME_CENTER, GAME_LIST, SEARCH_GAMES, BLUE_LIGHT_CINEMA, LIVE_FAST_SELECT_TV_CHANNEL,
			LIVE_SELECT_TELEVISION, ONLIVE_TIPS,WEATHER_MANAGE,NET_MANAGE,CLEAN_MEMORY, CHECK_VERSION,PLAY_MANAGER,AUDIO_DECODE,SCREEN_SAVE,
			DISPLAY,TV_SET,ABOUT,DROP_UP,DROP_DOWN,DROP_LEFT,DROP_RIGHT,DROP_HOME,DROP_CENTER,DROP_REBOOT,EDU_CENTER};

	private static List<String> switchFuctionCommandsList = new ArrayList<String>(Arrays.asList(switchFuctionCommands));

	/**
	 * 控制目标
	 * 
	 * @param context
	 * @param intent
	 * @param controller
	 */
	public static void setController(Context context, Intent intent, IVoiceController controller) {
		controller.control(context, intent);
	}

	/**
	 * 是否是控制activity的命令
	 * 
	 * @param intent
	 * @return
	 */
	public static boolean isFuctionController(Intent intent) {
		String action = intent.getStringExtra(IVoiceController.COMMAND);
		Log.d(TAG, "action-========" + action);
		if (action != null && action.length() > 0) {
			if (switchFuctionCommandsList.contains(action)) {
				return true;
			}
		}
		return false;
	}
}
