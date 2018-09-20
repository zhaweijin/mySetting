package com.hiveview.tv.common;

import android.content.Context;
import android.util.Log;

import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;

public class AppScene {
	private static final String TAG="AppScene";
	public static final String EPGINFOACTIVITY_SCENE = "com.hiveview.tv.activity.LiveEpgInfoActivity";
	public static final String EPGINFO_SCENE = "com.hiveview.tv.fragment.LiveEpgInfoFragment";
	public static final String GUIDE_SCENE = "com.hiveview.tv.view.GuidePageView";
	public static final String TVVIEW_SCENE = "com.hiveview.tv.view.MaxtrTvView";
	public static final String ONLIVEPLAY_SCENE = "com.hiveview.tv.activity.OnlivePlayerActivity";
	public static final String WELCOME_SCENE = "com.hiveview.tv.activity.WelcomeActivity";
	public static final String HOME_SCENE = "com.hiveview.tv.activity.HomeActivity";
	public static final String NONE_SCENE = "";
	public static String CURRENCE_SCENE = "";
	public static String LAST_SCENE = "";
	
	private static Scene scene;
	/**
	 * 蓝光极清
	 */
	public static final String BLUE_LIGHT = "com.hiveview.bluelight";
	/**
	 * 蓝光极清电影详情
	 */
	public static final String BLUE_LIGHT_DETAIL_FILM = "com.hiveview.bluelight.activity.FilmDetailActivity";
	/**
	 * 蓝光极清播放器
	 */
	public static final String BLUE_LIGHT_PLAYER = "com.hiveview.bluelight.activity.PlayerActivity";
	/**
	 * 奇艺播放器
	 */
	public static final String QIYI_PLAYER = "com.qiyi.video.player.PlayerActivity";
	
	/**
	 * 场景实例
	 */
	private static AppScene appScene = new AppScene();
	
	public AppScene(){}
	/*
	 * 
	 * 得到当前场景
	 */
	
	public static String getScene() {
	Log.i(TAG, "getScene-->CURRENCE_SCENE:" + CURRENCE_SCENE);
	return CURRENCE_SCENE;
	}
	
	/**
	 * 
	 * 得到上个场景
	 * @return
	 */
	public static String getLastScene() {
		return AppScene.LAST_SCENE;
	}

	public static void setScene(String scene) {
		Log.i(TAG, "setScene-->CURRENCE_SCENE1:" + scene);
		AppScene.LAST_SCENE = AppScene.CURRENCE_SCENE;
		CURRENCE_SCENE = scene;
		Log.i(TAG, "setScene-->CURRENCE_SCENE2:" + CURRENCE_SCENE);
	}
	
	public static AppScene getInstance() {
		return appScene;
	}
	
	private Context mContext;
	ISceneListener listenner;
	
	/**
	 * 创建场景，只需要一个实例
	 * @param context
	 * @return
	 */
	public Scene createScene(Context context, ISceneListener listenner) {
		this.listenner = listenner;
		mContext = context;
		scene = new Scene(context);
		return scene;
	}
	
	/**
	 * 设置命令短语
	 * @param actioin
	 * @param phrase
	 * @return
	 */
	public AppScene setCommandPhrase(String action, String...phrase) {
		return this;
	}
	
	/**
	 * 设置预定义格式如下<br>
	 * "[描述...]PredefineSemantic.Property[描述...]"
	 * @param semantic
	 * @return
	 */
	public AppScene setPredefineSemantic(String semantic) {
		return this;
	}
	
	/**
	 * 设置模糊槽
	 * @param action 对应的Action
	 * @param fuzzyDes 模糊槽描述：[[描述...]$W(fuzzyName)[描述...]...]
	 * @return
	 */
	public AppScene setFuzzySlot(String action, String...fuzzyDes) {
		return this;
	}
	
	/**
	 * 设置模糊槽的具体内容
	 * @param fuzzyName 是模糊槽"$W(fuzzyName)"中的fuzzyName，两个单词是一致的
	 * @param fuzzyWord 描述内容
	 * @return
	 */
	public AppScene setFuzzyWord(String fuzzyName, String...fuzzyWord) {
		return this;
	}
	
	/**
	 * 初始化场景，需要提供场景名
	 * @param sceneName
	 */
	public void initScene(String sceneName) {
		setScene(sceneName);
		initScene();
	}
	/**
	 * 初始化场景，如果之前调用过setScene使用此方法初始化场景
	 * @param sceneName
	 */
	public void initScene() {
		if (scene != null) {
			scene.init(listenner);
		}
	}
	
	/**
	 * 将场景释放
	 */
	public void release() {
		if (scene != null) {
			scene.release();
		}
	}
	
	/**
	 * 创建预定义语义JSON
	 * @return
	 */
	private StringBuffer createPredefineSemanticJSON() {
		return new StringBuffer();
	}
	
	/**
	 * 创建模糊槽JSON
	 * @return
	 */
	private StringBuffer createFuzzySlotJSON() {
		return createCommonJSON();
	}
	
	/**
	 * 创建命令短语JSON
	 * @return
	 */
	private StringBuffer createCommandPhraseJSON() {
		return createCommonJSON();
	}
	
	/**
	 * 创建模糊槽具体内容的JSON
	 * @return
	 */
	private StringBuffer createFuzzyWordJSON() {
		return new StringBuffer();
	}
	
	/**
	 * 创建JSON，适用于数据结构为Map<String, List<String>>的数据源
	 * @param map
	 * @return
	 */
	private StringBuffer createCommonJSON() {
		return new StringBuffer();
	}
	
	/**
	 * 预定义语义表
	 * @author haozening
	 *
	 */
	public static interface PredefineSemantic {
		String PREDEFINE_PLAY   = "$P(_PLAY)";
		String PREDEFINE_PAGE   = "$P(_PAGE)";
		String PREDEFINE_EPSOID = "$P(_EPSOID)";
		String PREDEFINE_MUSIC  = "$P(_MUSIC)";
		String PREDEFINE_KTV    = "$P(_KTV)";
		String PREDEFINE_FLIGHT = "$P(_FLIGHT)";
		String PREDEFINE_TRAIN  = "$P(_TRAIN)";
		String PREDEFINE_SELECT = "$P(_SELECT)";
		String PREDEFINE_SEARCH = "$P(_SEARCH)";
		String PREDEFINE_CHAT   = "$P(_CHAT_XXX)";

		// 播放相关Action
		String ACTION_PLAY		= "PLAY";
		String ACTION_PAUSE		= "PAUSE";
		String ACTION_RESUME	= "RESUME";
		String ACTION_RESTART	= "RESTART";
		String ACTION_SEEK		= "SEEK";
		String ACTION_FORWARD	= "FORWARD";
		String ACTION_BACKWARD	= "BACKWARD";
		String ACTION_PREVIOUS	= "PREV";
		String ACTION_NEXT		= "NEXT";
		String ACTION_INDEX		= "INDEX";
		String ACTION_EXIT		= "EXIT";
		
		// 播放相关参数
		/**
		 * 跳到指定位置，单位秒
		 */
		String PARAM_PLAY_SEEK_POSITION = "position";
		/**
		 * 快进快退的时间，单位秒
		 */
		String PARAM_PLAY_OFFSET = "offset";
		/**
		 * 播放第几集
		 */
		String PARAM_EPISODE = "index";
	}
	
	public static interface PlayerBroadCastContants{
		/**
		 * Action
		 */
		public static final String ACTION_VOICE_PLAYER = "com.hiveview.bluelight.action.VOICEPLAYER";
		/**
		 * QIYI Action
		 */
		public static final String ACTION_VOICE_QIYI_PLAYER = "com.qiyi.video.action.VOICEPLAYER";
		/**
		 * Extra快进快退时间
		 */
		public static final String PLAYER_TIME = "PLAYER_TIME";
		/**
		 * Extra播放控制
		 */
		public static final String PLAYER_CONTROL = "PLAYER_CONTROL";
		/**
		 * Extra播放控制第几集
		 */
		public static final String PLAYER_EPISODE = "PLAYER_EPISODE";
		/**
		 * 播放
		 */
		public static final int PLAYER_PLAY = 1001;
		/**
		 * 暂停
		 */
		public static final int PLAYER_PAUSE = 1002;
		/**
		 * 快进
		 */
		public static final int PLAYER_FASTFORWARD = 1003;
		/**
		 * 快退
		 */
		public static final int PLAYER_FASTBACKWARD = 1004;
		/**
		 * 快进到指定时间
		 */
		public static final int PLAYER_FASTFORWARD_TIME = 1005;
		/**
		 * 快退到指定时间
		 */
		public static final int PLAYER_FASTBACKWARD_TIME = 1006;
		/**
		 * 重新播放
		 */
		public static final int PLAYER_RESTART = 1007;
		/**
		 * 跳到指定时间
		 */
		public static final int PLAYER_SEEK_TO = 1008;
		/**
		 * 声音放大
		 */
		public static final int PLAYER_VOLUMN_INCREASE = 1009;
		/**
		 * 声音缩小
		 */
		public static final int PLAYER_VOLUMN_DECREASE = 1010;
		/**
		 * 退出
		 */
		public static final int PLAYER_EXIT = 1011;
		/**
		 * 关闭声音
		 */
		public static final int PLAYER_VOLUMN_CLOSE = 1012;
		/**
		 * 打开声音
		 */
		public static final int PLAYER_VOLUMN_OPEN = 1013;
		/**
		 * 上一集
		 */
		public static final int PLAYER_PREVIOUS = 1014;
		/**
		 * 下一集
		 */
		public static final int PLAYER_NEXT = 1015;
		/**
		 * 指定集
		 */
		public static final int PLAYER_INDEX = 1016;
	}
	
	public static interface PredefineJSON {
		String JSON_BLUE_LIGHT_PLAYER = "{\"scene\":\"" + BLUE_LIGHT_PLAYER + "\",\"commands\":{\"key1\":[\"" + PredefineSemantic.PREDEFINE_PLAY + "\"]}}";
		String JSON_HOME_TEST = "{\"scene\":\"" + HOME_SCENE + "\",\"commands\":{\"bluelight\":[\"打开蓝光极清\"]}}";
	}
}
