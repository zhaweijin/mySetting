package com.hiveview.tv.common.voice.impl;

import java.lang.ref.WeakReference;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hiveview.tv.R;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.voice.AppSpeaker;
import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.common.voice.OpenFuctionControllerSelector;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.iflytek.xiri.Feedback;

/**
 * 控制打开activity
 * 
 * @ClassName: OpenFunctionVoiceController
 * @Description:
 * @author: guosongsheng
 * @date 2014-10-31 下午1:27:15
 * 
 */
public class OpenFunctionVoiceController implements IVoiceController {
	/**
	 * TAG
	 */
	private static final String TAG = "PlayerVoiceController";
	/**
	 * 打开播放记录
	 */
	private static final int PLAY_RECORDS_MESSAGE = 10001;
	/**
	 * 打开影片搜索
	 */
	private static final int MOVIE_SEARCH_MESSAGE = 10002;
	/**
	 * 打开已安装的应用游戏
	 */
	private static final int INSTALLED_APPS_GAMES_MESSAGE = 10003;
	/**
	 * 打开影片收藏
	 */
	private static final int MOVIE_FAVORITES_MESSAGE = 10004;
	/**
	 * 打开外接存储
	 */
	private static final int EXTERNAL_STORAGE_MESSAGE = 10005;
	/**
	 * 打开应用商店
	 */
	private static final int APP_STORE_MESSAGE = 10006;
	/**
	 * 打开应用排行榜
	 */
	private static final int APPLICATION_LEADERBOARD_MESSAGE = 10007;
	/**
	 * 打开搜索应用
	 */
	private static final int SEARCH_APPLICATIONS_MESSAGE = 10008;
	/**
	 * 打开游戏中心
	 */
	private static final int GAME_CENTER_MESSAGE = 10009;
	/**
	 * 打开游戏排行榜
	 */
	private static final int GAME_LIST_MESSAGE = 10010;
	/**
	 * 打开搜索游戏
	 */
	private static final int SEARCH_GAMES_MESSAGE = 10011;
	/**
	 * 打开极清影院
	 */
	private static final int BLUE_LIGHT_CINEMA_MESSAGE = 10012;
	/**
	 * 打开快速选台
	 */
	private static final int LIVE_FAST_SELECT_TV_CHANNEL_MESSAGE = 10013;
	/**
	 * 打开分类选台
	 */
	private static final int LIVE_SELECT_TELEVISION_MESSAGE = 10014;
	/**
	 * 打开直播提醒
	 */
	private static final int ONLIVE_TIPS_MESSAGE = 10015;
	/**
	 * 打开天气设置
	 */
	private static final int WEATHER_MANAGE_MESSAGE = 10016;
	/**
	 * 打开网络设置
	 */
	private static final int NET_MANAGE_MESSAGE = 10017;
	/**
	 * 打开清理缓存
	 */
	private static final int CLEAN_MEMORY_MESSAGE = 10018;
	/**
	 * 打开版本检测
	 */
	private static final int CHECK_VERSION_MESSAGE = 10019;
	/**
	 * 打开播设置
	 */
	private static final int PLAY_MANAGER_MESSAGE = 10020;
	/**
	 * 打开音频解码
	 */
	private static final int AUDIO_DECODE_MESSAGE = 10021;
	/**
	 * 打开屏保
	 */
	private static final int SCREEN_SAVE_MESSAGE = 10022;
	/**
	 * 打开显示设置
	 */
	private static final int DISPLAY_MESSAGE = 10023;
	/**
	 * 打开机顶盒设置
	 */
	private static final int TV_SET_MESSAGE = 10024;
	/**
	 * 打开关于
	 */
	private static final int ABOUT_MESSAGE = 10025;
	/**
	 * 上
	 */
	private static final int UP_MESSAGE = 10026;
	/**
	 * 下
	 */
	private static final int DOWN_MESSAGE = 10027;
	/**
	 * 左
	 */
	private static final int LEFT_MESSAGE = 10028;
	/**
	 * 右
	 */
	private static final int RIGHT_MESSAGE = 10029;
	/**
	 * 确定
	 */
	private static final int CENTER_MESSAGE = 10030;
	/**
	 * 首页
	 */
	private static final int HOME_MESSAGE = 10031;
	/**
	 * 重启
	 */
	private static final int REBOOT_MESSAGE = 10032;
	/**
	 * 打开教育中心
	 */
	private static final int EDU_CENTER_MESSAGE = 10033;
	
	/**
	 * 打开播放记录的action
	 */
	public static String PLAY_RECORD_ACTION = "com.hiveview.tv.PLAY_RECORDS";
	/**
	 * 打开影片搜索action
	 */
	public static String MOVIE_SEARCH_ACTION = "com.hiveview.tv.MOVIE_SEARCH";
	/**
	 * 打开影片收藏action
	 */
	public static String MOVIE_FAVORITES_ACTION = "com.hiveview.tv.MOVIE_FAVORITES";
	/**
	 * 打开已安装的应用游戏
	 */
	public static String INSTALLED_APPS_GAMES_ACTION = "com.hiveview.appstore.buy";
	/**
	 * //打开外接存储action
	 */
	// public static String EXTERNAL_STORAGE_ACTION = "com.fone.player.domy";
	public static String EXTERNAL_STORAGE_ACTION = "com.hiveview.externalstorage.action.APP_HOME";
	/**
	 * 打开应用商店action
	 */
	public static String APP_STORE_ACTION = "com.hiveview.appstore.main";
	/**
	 * 打开应用排行榜
	 */
	public static String APPLICATION_LEADERBOARD_ACTION = "com.hiveview.appstore.top";
	/**
	 * 打开搜索应用
	 */
	public static String SEARCH_APPLICATIONS_ACTION = "com.hiveview.appstore.search";
	/**
	 * 打开游戏中心
	 */
	public static String GAME_CENTER_ACTION = "com.hiveview.appstore.main";
	/**
	 * 打开教育中心
	 */
	public static String EDU_CENTER_ACTION = "com.hiveview.appstore.main";
	/**
	 * 打开游戏排行榜
	 */
	public static String GAME_LIST_ACTION = "com.hiveview.appstore.top";
	/**
	 * 打开搜索游戏
	 */
	public static String SEARCH_GAMES_ACTION = "com.hiveview.appstore.search";
	/**
	 * 打开极清影院
	 */
	public static String BLUE_LIGHT_CINEMA_ACTION = "com.hiveview.bluelight";
	/**
	 * 打开快速选台
	 */
	public static String LIVE_FAST_SELECT_TV_CHANNEL_ACTION = "com.hiveview.tv.LIVE_FAST_SELECT_TV_CHANNEL";
	/**
	 * 打开分类选台
	 */
	public static String LIVE_SELECT_TELEVISION_ACTION = "com.hiveview.tv.LIVE_SELECT_TELEVISION";
	/**
	 * 打开直播提醒
	 */
	public static String ONLIVE_TIPS_ACTION = "com.hiveview.tv.ONLIVE_TIPS";
	
	/**
	 * 模拟遥控器广播
	 */
	public final static String BROADCAST_LAUNCHER_ACTION = "com.hiveview.tv.REMOTECONTROL";
	public final static String BROADCAST_LAUNCHER_VALUE = "com.hiveview.tv.value";
	/**
	 * SpeakerHandler
	 */
	private SpeakerHandler handler;

	private static Context mcontext;

	/**
	 * 控制接口
	 */
	@Override
	public void control(final Context context, Intent intent) {
		Log.d(TAG, "controlActivity");
		// 继承科大讯飞语音控制类APP，定义额外功能
		mcontext = context;
		final AppSpeaker speaker = new AppSpeaker(context);
		String command = intent.getStringExtra(COMMAND);
		handler = new SpeakerHandler(context, speaker);
		Log.d(TAG, Uri.decode(intent.toURI()));
		String action = intent.getStringExtra(ACTION);
		Log.d(TAG, Uri.decode(intent.toURI()));
		if (command != null) {
			Log.d(TAG, command);
		}
		if (action != null) {
			Log.d(TAG, action);
		}
		// 打开播放记录
		if (command != null && command.equals(OpenFuctionControllerSelector.PLAY_RECORDS)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.play_records), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(PLAY_RECORDS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(PLAY_RECORDS_MESSAGE);
		}
		// 打开影片搜索
		if (command != null && command.equals(OpenFuctionControllerSelector.MOVIE_SEARCH)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.movie_search), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(MOVIE_SEARCH_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(MOVIE_SEARCH_MESSAGE);
		}

		// 打开已安装的应用游戏
		if (command != null && command.equals(OpenFuctionControllerSelector.INSTALLED_APPS_GAMES)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.installed_apps_games), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(INSTALLED_APPS_GAMES_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(INSTALLED_APPS_GAMES_MESSAGE);
		}
		// 打开影片收藏
		if (command != null && command.equals(OpenFuctionControllerSelector.MOVIE_FAVORITES)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.movie_favorites), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(MOVIE_FAVORITES_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(MOVIE_FAVORITES_MESSAGE);
		}
		// 打开外接存储
		if (command != null && command.equals(OpenFuctionControllerSelector.EXTERNAL_STORAGE)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.external_storage), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(EXTERNAL_STORAGE_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(EXTERNAL_STORAGE_MESSAGE);
		}
		// 打开应用商店
		if (command != null && command.equals(OpenFuctionControllerSelector.APP_STORE)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.app_store), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(APP_STORE_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(APP_STORE_MESSAGE);
		}
		// 打开应用排行榜
		if (command != null && command.equals(OpenFuctionControllerSelector.APPLICATION_LEADERBOARD)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.application_leaderboard), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(APPLICATION_LEADERBOARD_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(APPLICATION_LEADERBOARD_MESSAGE);
		}
		// 打开搜索应用
		if (command != null && command.equals(OpenFuctionControllerSelector.SEARCH_APPLICATIONS)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.search_applications), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(SEARCH_APPLICATIONS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(SEARCH_APPLICATIONS_MESSAGE);
		}
		// 打开游戏中心
		if (command != null && command.equals(OpenFuctionControllerSelector.GAME_CENTER)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.game_center), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(GAME_CENTER_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(GAME_CENTER_MESSAGE);
		}
		// 打开教育中心
		if (command != null && command.equals(OpenFuctionControllerSelector.EDU_CENTER)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.edu_center), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(GAME_CENTER_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(EDU_CENTER_MESSAGE);
		}
		// 打开游戏排行榜
		if (command != null && command.equals(OpenFuctionControllerSelector.GAME_LIST)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.game_list), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(GAME_LIST_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(GAME_LIST_MESSAGE);
		}
		// 打开搜索游戏
		if (command != null && command.equals(OpenFuctionControllerSelector.SEARCH_GAMES)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.search_games), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(SEARCH_GAMES_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(SEARCH_GAMES_MESSAGE);
		}
		// 打开极清影院
		if (command != null && command.equals(OpenFuctionControllerSelector.BLUE_LIGHT_CINEMA)) {
			if(HiveviewApplication.outer == 8){
				speaker.feedback(context.getString(R.string.speaker_none), Feedback.DIALOG);
				return ;
			}
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.blue_light_cinema), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(BLUE_LIGHT_CINEMA_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(BLUE_LIGHT_CINEMA_MESSAGE);
		}
		// 打开快速选台
		if (command != null && command.equals(OpenFuctionControllerSelector.LIVE_FAST_SELECT_TV_CHANNEL)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.live_fast_select_tv_channel), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(LIVE_FAST_SELECT_TV_CHANNEL_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(LIVE_FAST_SELECT_TV_CHANNEL_MESSAGE);
		}
		// 打开分类选台
		if (command != null && command.equals(OpenFuctionControllerSelector.LIVE_SELECT_TELEVISION)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.live_select_television), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(LIVE_SELECT_TELEVISION_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(LIVE_SELECT_TELEVISION_MESSAGE);
		}
		// 打开直播提醒
		if (command != null && command.equals(OpenFuctionControllerSelector.ONLIVE_TIPS)) {
			speaker.begin(intent);
			speaker.feedback(context.getString(R.string.onlives_tips), Feedback.DIALOG);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(ONLIVE_TIPS_MESSAGE);
		}
		// 打开天气设置
		if (command != null && command.equals(OpenFuctionControllerSelector.WEATHER_MANAGE)) {
			speaker.begin(intent);

			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(WEATHER_MANAGE_MESSAGE);
		}
		// 打开网络设置
		if (command != null && command.equals(OpenFuctionControllerSelector.NET_MANAGE)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(NET_MANAGE_MESSAGE);
		}
		// 打开清除缓存
		if (command != null && command.equals(OpenFuctionControllerSelector.CLEAN_MEMORY)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(CLEAN_MEMORY_MESSAGE);
		}
		// 打开版本检测
		if (command != null && command.equals(OpenFuctionControllerSelector.CHECK_VERSION)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(CHECK_VERSION_MESSAGE);
		}
		// 打开播放设置
		if (command != null && command.equals(OpenFuctionControllerSelector.PLAY_MANAGER)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(PLAY_MANAGER_MESSAGE);
		}
		// 打开音频解码
		if (command != null && command.equals(OpenFuctionControllerSelector.AUDIO_DECODE)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(AUDIO_DECODE_MESSAGE);
		}
		// 打开屏保
		if (command != null && command.equals(OpenFuctionControllerSelector.SCREEN_SAVE)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(SCREEN_SAVE_MESSAGE);
		}
		// 打开显示设置
		if (command != null && command.equals(OpenFuctionControllerSelector.DISPLAY)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(DISPLAY_MESSAGE);
		}
		// 打开机顶盒设置
		if (command != null && command.equals(OpenFuctionControllerSelector.TV_SET)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(TV_SET_MESSAGE);
		}
		// 打开关于
		if (command != null && command.equals(OpenFuctionControllerSelector.ABOUT)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			handler.sendEmptyMessage(ABOUT_MESSAGE);
		}
		// 上
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_UP)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_up), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(UP_MESSAGE, 2000);
		}
		// 下
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_DOWN)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_down), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(DOWN_MESSAGE, 2000);
		}
		//左
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_LEFT)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_left), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(LEFT_MESSAGE, 2000);
		}
		// 右
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_RIGHT)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_right), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(RIGHT_MESSAGE, 2000);
		}
		// 确定
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_CENTER)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_center), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(CENTER_MESSAGE, 2000);
		}
		// home
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_HOME)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_home), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(HOME_MESSAGE, 2000);
		}
		// reboot
		if (command != null && command.equals(OpenFuctionControllerSelector.DROP_REBOOT)) {
			speaker.begin(intent);
			// handler.sendEmptyMessageDelayed(ONLIVE_TIPS_MESSAGE,
			// speaker.getSpeakTime());
			speaker.feedback(context.getString(R.string.drop_reboot), Feedback.SILENCE);
			handler.sendEmptyMessageDelayed(REBOOT_MESSAGE, 2000);
		}

	}

	/**
	 * 播放器是否展示在用户前边
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFaceToPlayer(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> taskInfos = manager.getRunningTasks(1);
		String className = taskInfos.get(0).topActivity.getClassName();
		if (className.equalsIgnoreCase(AppScene.BLUE_LIGHT_PLAYER)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @ClassName: SpeakerHandler
	 * @Description:
	 * @author: guosongsheng
	 * @date 2014-10-31 下午1:48:36
	 * 
	 */
	static class SpeakerHandler extends Handler {
		private WeakReference<Context> contextRef;
		private WeakReference<AppSpeaker> speakerRef;

		// private Context contextRef;
		// private AppSpeaker speakerRef;

		public SpeakerHandler(Context context, AppSpeaker speaker) {
			this.contextRef = new WeakReference<Context>(context);
			this.speakerRef = new WeakReference<AppSpeaker>(speaker);
			// this.contextRef =context;
			// this.speakerRef = speaker;
		}

		public void handleMessage(Message msg) {
			Context context = contextRef.get();
			AppSpeaker speaker = speakerRef.get();
			if (context == null || speaker == null) {
				return;
			}
			Intent intent = new Intent();
			int netStatus = HiveviewApplication.getNetStatus();

			switch (msg.what) {
			case PLAY_RECORDS_MESSAGE: // 打开播放记录
				intent.setAction(PLAY_RECORD_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case MOVIE_SEARCH_MESSAGE: // 打开影片搜索
				intent.setAction(MOVIE_SEARCH_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case INSTALLED_APPS_GAMES_MESSAGE: // 打开已安装的应用游戏
				intent.setAction(INSTALLED_APPS_GAMES_ACTION);// Intent.CATEGORY_DEFAULT:已安装应用游戏action
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case MOVIE_FAVORITES_MESSAGE:// 打开影片收藏
				intent.setAction(MOVIE_FAVORITES_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case EXTERNAL_STORAGE_MESSAGE:// 打开外接存储

				intent.setAction(EXTERNAL_STORAGE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				try {
					context.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					// context.sendBroadcast(intent);
				}

				break;
			case APP_STORE_MESSAGE:// 打开应用商店
				intent.setAction(APP_STORE_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("category_id", 2);
				intent.putExtra("app_type", 1);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case APPLICATION_LEADERBOARD_MESSAGE:// 打开应用排行榜
				intent.setAction(APPLICATION_LEADERBOARD_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("category_id", 2);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case SEARCH_APPLICATIONS_MESSAGE: // 打开搜索应用
				intent.setAction(SEARCH_APPLICATIONS_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("category_id", 2);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case GAME_CENTER_MESSAGE:// 打开游戏中心
				intent.setAction(GAME_CENTER_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("category_id", 1);
				intent.putExtra("app_type", 1);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case EDU_CENTER_MESSAGE:// 打开游戏中心
				intent.setAction(EDU_CENTER_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("category_id", 3);
				intent.putExtra("app_type", 1);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case GAME_LIST_MESSAGE:// 打开游戏排行榜
				intent.setAction(GAME_LIST_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("category_id", 1);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case SEARCH_GAMES_MESSAGE:// 打开搜索游戏
				intent.setAction(SEARCH_GAMES_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("category_id", 1);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case BLUE_LIGHT_CINEMA_MESSAGE:// 打开极清影院
				try {
					intent = context.getPackageManager().getLaunchIntentForPackage(BLUE_LIGHT_CINEMA_ACTION);
					if (intent != null) {
						context.startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case LIVE_FAST_SELECT_TV_CHANNEL_MESSAGE:// 打开快速选台
				intent.setAction(LIVE_FAST_SELECT_TV_CHANNEL_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case LIVE_SELECT_TELEVISION_MESSAGE:// 打开分类选台
				intent.setAction(LIVE_SELECT_TELEVISION_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case ONLIVE_TIPS_MESSAGE:// 打开直播提醒
				intent.setAction(ONLIVE_TIPS_ACTION);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				context.startActivity(intent);
				break;
			case WEATHER_MANAGE_MESSAGE:// 打开天气设置
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_WEATHER");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.weather_manage), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case NET_MANAGE_MESSAGE:// 打开网络设置
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_NET");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.net_manage), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case CLEAN_MEMORY_MESSAGE:// 打开清除缓存
				intent.setAction("com.hiveview.settings.ACTION_SETTING");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.putExtra("memory", "memory");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.clean_memory), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case CHECK_VERSION_MESSAGE:// 打开版本检测
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_VERSION");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by LiHongJi */
					speaker.feedback(context.getString(R.string.check_version), Feedback.DIALOG);
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::",  "startActivity");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", "sendBroadcast" );
				}
				break;
			case PLAY_MANAGER_MESSAGE:// 打开播放设置
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_PLAY");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.play_manager), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case AUDIO_DECODE_MESSAGE:// 打开音频解码
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_AUDIO_DECODE");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.audio_decode), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case SCREEN_SAVE_MESSAGE:// 打开屏保
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_SCREEN_PROTECTION");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.screen_save), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case DISPLAY_MESSAGE:// 打开显示设置
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_DISPLAY");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.display), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case TV_SET_MESSAGE:// 打开机顶盒设置
				STBSettingInfoUtil.STBShowTVSetUI(mcontext);
				break;
			case ABOUT_MESSAGE:// 打开关于
				intent.setAction("com.hiveview.domybox.ACTION_SETTING_INFORMATION");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				/* start by ZhaiJianfeng */
				intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
				/* end by ZhaiJianfeng */
				/* start by LiHongJi */
				if (checkApkExist(context, intent)) {
					speaker.feedback(context.getString(R.string.about), Feedback.DIALOG);
				} else {
					speaker.feedback(context.getString(R.string.noinstall), Feedback.DIALOG);
				}
				/* end by LiHongJi */
				try {
					context.startActivity(intent);
					Log.d("SettingActivity->click_try->intent::", intent + "");
				} catch (ActivityNotFoundException e) {
					context.sendBroadcast(intent);
					Log.d("SettingActivity->click_catch->intent::", intent + "");
				}
				break;
			case UP_MESSAGE:// shang
				   Intent intentUp = new Intent();
				   intentUp.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentUp.putExtra(BROADCAST_LAUNCHER_VALUE, "up");
				   context.sendBroadcast(intentUp);
				break;
			case DOWN_MESSAGE://下
				   Intent intentDown = new Intent();
				   intentDown.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentDown.putExtra(BROADCAST_LAUNCHER_VALUE, "down");
				   context.sendBroadcast(intentDown);
				break;
			case LEFT_MESSAGE:// 左
				   Intent intentLeft = new Intent();
				   intentLeft.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentLeft.putExtra(BROADCAST_LAUNCHER_VALUE, "left");
				   context.sendBroadcast(intentLeft);
				break;
			case RIGHT_MESSAGE:// 右
				   Intent intentRight = new Intent();
				   intentRight.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentRight.putExtra(BROADCAST_LAUNCHER_VALUE, "right");
				   context.sendBroadcast(intentRight);
				break;
			case CENTER_MESSAGE:// 确定
				   Intent intentCenter = new Intent();
				   intentCenter.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentCenter.putExtra(BROADCAST_LAUNCHER_VALUE, "center");
				   context.sendBroadcast(intentCenter);
				break;
			case HOME_MESSAGE:// 首页
				   Intent intentHome = new Intent();
				   intentHome.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentHome.putExtra(BROADCAST_LAUNCHER_VALUE, "home");
				   context.sendBroadcast(intentHome);
				break;
			case REBOOT_MESSAGE:// 重启
				   Intent intentReboot = new Intent();
				   intentReboot.setAction(BROADCAST_LAUNCHER_ACTION);
				   intentReboot.putExtra(BROADCAST_LAUNCHER_VALUE, "reboot");
				   context.sendBroadcast(intentReboot);
				break;
			}
		};
	};

	/**
	 * 
	 * @Title: OpenFunctionVoiceController
	 * @author:lihongji
	 * @Description: 根据intent判断apk是否存在
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean checkApkExist(Context context, Intent intent) {
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

}
