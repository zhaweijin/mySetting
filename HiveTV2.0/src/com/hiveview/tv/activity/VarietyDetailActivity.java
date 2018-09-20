package com.hiveview.tv.activity;

import java.io.Serializable;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.strategy.NeighborOneLineStrategy;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.CallbackPageChangeListener;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.TeleplayDetailActivity.getQIYIPlayerRecords;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.adapter.FilmPagerAdapter;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.common.factory.TeleplayGroupsPagerViewFactory;
import com.hiveview.tv.common.factory.VarietyVideosPagerViewFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.dao.CollectVoideoNewDAO;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.PlayerRecordEntity;
import com.hiveview.tv.service.entity.ProgramByCodeEntity;
import com.hiveview.tv.service.entity.TVChannelEntity;
import com.hiveview.tv.service.entity.VideoEntity;
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.utils.HiveViewErrorCode;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.QIYIRecordUtils;
import com.hiveview.tv.utils.QiYiPlayerUtil;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.TeleplayLiveTVLogoView;
import com.hiveview.tv.view.TeleplayVideoGroupView;
import com.hiveview.tv.view.TeleplayVideosPageView;
import com.hiveview.tv.view.TeleplayVideoGroupView.IButtonFocusListener;
import com.hiveview.tv.view.VarietyVideosPageView;
import com.hiveview.tv.view.VarietyVideosPageView.OnBtnGetFocus;
import com.hiveview.tv.view.television.MarqueeText;
import com.paster.util.JsonUtil;

/**
 * 每一个节目的详情， 节目的剧集，介绍， 节目的名称
 * 
 * @ClassName: VarietyDetailActivity
 * @Description: 综艺详情页
 * @date 2014-6-4 下午3:24:21
 * 
 */
@SuppressLint("UseSparseArrays")
public class VarietyDetailActivity extends BaseActivity {
	private String name = "";
	private String namefoces = "";
	private FilmNewEntity mFilmEntity = null;
	private TextView mTitle;
	private TextView mContent;
	private ImageView ivPlay;
	private ImageView ivCollect;
	private ImageView ivLeft;
	private ImageView ivRight;
	private ImageView mCover;
	private TextView mDescribeLine1;
	private HiveTVService dataService = null;
	/**
	 * 临时视频
	 */
	private ArrayList<VideoNewEntity> tmpVideos = null;
	private ArrayList<VideoNewEntity> videos = null;
	/**
	 * 视频实体类
	 */
	private VideoNewEntity firstVideo;
	/**
	 * TV频道
	 */
	private ArrayList<TVChannelEntity> channls = null;
	/**
	 * 电视频道
	 */
	private ArrayList<ProgramByCodeEntity> nextps = null;
	/**
	 * 请求视屏集合成功
	 */
	private final int REQUEST_VIDEOS_SUCCESS = 1;
	/**
	 * 请求视频集合失败
	 */
	private final int REQUEST_VIDEOS_FAIL = -1;
	/**
	 * 请求频道集合成功
	 */
	private final int REQUEST_CHANNELS_SUCCESS = 2;
	/**
	 * 请求的频道集合失败
	 */
	private final int REQUEST_CHANNELS_FAIL = -2;
	/**
	 * 请求下一个成功
	 */
	private final int REQUEST_NEXT_SUCCESS = 3;
	/**
	 * 请求下一个失败
	 */
	private final int REQUEST_NEXT_FAIL = -3;
	/**
	 * 获取单个视频成功
	 */
	private final int REQUEST_VIDEO_SUCCESS = 4;
	/**
	 * 获取单个视频失败
	 */
	private final int REQUEST_VIDEO_FAIL = -4;

	/**
	 * 综艺的剧集和年份的显示的viewpager
	 * 
	 * @Fields videoPager groupPager
	 */
	private HivePreloadViewPager videoPager = null;
	private HivePreloadViewPager groupPager = null;
	private HivePagerAdapter videoAdapter = null;
	private HivePagerAdapter groupAdapter = null;

	private ViewPager channelsPager = null;
	private LinearLayout vChannel1;
	private LinearLayout vChannel2;
	private LinearLayout vChannel3;
	private ProgressDialog dialogLoadingView = null;

	private View layoutTv1 = null;
	private View layoutTv2 = null;
	private View layoutTv3 = null;
	private View layoutMore = null;

	private List<View> groupViews = null;
	private List<View> channelViews = null;
	/**
	 * 综艺节目按年度划分，一共可以分为几个年份
	 */
	private String[] yearsArray = null;

	/**
	 * 按年份设置Adapter缓存数据
	 */
	private HashMap<Integer, HivePagerAdapter> mapYearAndAdapter = null;
	private HashMap<Integer, ArrayList<Integer>> mapTheLastViewIndex = null;
	private HashMap<Integer, VarietyVideosPageView> mapVarietyVideosPageView = null;
	private HashMap<String, Integer> mapVarietyVideosPageViewIndex = null;
	/**
	 * 剧集列表每页请求多少条数据
	 */
	private final int VIDEO_LIST_PAGE_SIZE = 400;

	/**
	 * 每页显示综艺的集数
	 */
	private final double VIDEO_PAGE_SIZE = 20;
	/**
	 * 查询 是否已经在收藏列表中
	 */
	private final int QUREY_SUCRESS = 0x00123;
	/**
	 * 按1-20集进行分组，每页显示的组数
	 */
	private final double VIDEO_GROUP_PAGE_SIZE = 5;

	/**
	 * viewpage 右翻页
	 * 
	 * @Fields VIEWPAGER_RIGHT
	 */
	private final int VIEWPAGER_RIGHT = 0x00154;
	/**
	 * viewpage 左翻页
	 * 
	 * @Fields VIEWPAGER_LEFT
	 */
	private final int VIEWPAGER_LEFT = 0x00125;
	/**
	 * 当前有正在直播的电视台，出现的直播信息的相关view
	 * 
	 * @Fields popuChannelWindow
	 */
	private PopupWindow popuChannelWindow = null;
	private PopupWindow popuVideoWindow = null;
	private ImageView popuVideoPoster = null;
	private ImageView popuChannelLogo = null;
	private TextView popuChannelName = null;
	private SeekBar popuBar = null;
	private View popuChannelView;
	private View popuVideoView;
	private MarqueeText popuVideoHotName;
	private ImageView popuVideoHotImg;
	private TextView popuStartTime = null;
	private TextView popuEndTime = null;
	private MarqueeText popuVideoName = null;
	private MarqueeText popuVideoName2 = null;
	/**
	 * 第一次进去为true
	 */
	private boolean isFirst = true;
	private TextView tvNoData;
	/**
	 * 图片下载器和配置
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;

	private String viewID = "teleplay_btn6,teleplay_btn7,teleplay_btn8,teleplay_btn9,teleplay_btn10,teleplay_btn16"
			+ "teleplay_btn17,teleplay_btn18,teleplay_btn19,teleplay_btn20";
	private int[] viewId = { R.id.teleplay_btn6, R.id.teleplay_btn7, R.id.teleplay_btn8, R.id.teleplay_btn9, R.id.teleplay_btn10,
			R.id.teleplay_btn16, R.id.teleplay_btn17, R.id.teleplay_btn18, R.id.teleplay_btn19, R.id.teleplay_btn20 };
	private List<Integer> viewIds;
	/**
	 * 电视台的ViewPager上，每页显示的数量
	 */
	private final double CHANNEL_GROUP_PAGE_SIZE = 5;

	private int VIDEOS_CURRENT_YEAR_POSITION = 0;
	private int CHANNELS_CURRENT_PAGE_NUM = 0;
	private int mVideoId = 0;

	private RelativeLayout moreLayout;
	/**
	 * 组中包含的实体类列表
	 * 
	 * @Fields groupEntitys
	 */
	private ArrayList<VideoNewEntity> groupEntitys;

	/**
	 * 数据库类
	 */
	CollectVoideoNewDAO dao;
	/**
	 * 改变播放按钮 改为需播
	 */
	private final int PLAYER_GO_ON = 0x00140;
	private final int PLAYER_DETAIL = 0x00142;
	/**
	 * 延迟显示综艺的详情图
	 * 
	 * @Fields VIDEO_WINDOW_SHOW
	 */
	private final int VIDEO_WINDOW_SHOW = 0x00143;

	/**
	 * 当前类的标签
	 * 
	 * @Fields TAG
	 */
	private String TAG = "VarietyDetailActivity";
	private List<View> focusList;
	/**
	 * viewItemViewIndex view 的索引值
	 * 
	 * @Fields viewItemViewIndex
	 */
	private int viewItemViewIndex = -1;
	/**
	 * 分组当中获取焦点的view
	 * 
	 * @Fields btnGetFocus
	 */
	private Button btnGetFocus;
	/**
	 * 是最后一个
	 * 
	 * @Fields isLastPager
	 */
	private boolean isLastPager = false;
	/**
	 * 第一页
	 * 
	 * @Fields isFirstPager
	 */
	private boolean isFirstPager = false;
	/**
	 * 是单行的情况
	 * 
	 * @Fields isOneLine
	 */
	private boolean isOneLine = false;
	/**
	 * 当前页的view 的index
	 * 
	 * @Fields viewIndex
	 */
	private int viewIndex;
	/**
	 * view item 中获取焦点的view
	 * 
	 * @Fields viewItemFocus
	 */
	private Button viewItemFocus;
	/**
	 * pager 动画执行结束
	 * 
	 * @Fields isViewPagerAnimactionOver
	 */
	private boolean isViewPagerAnimactionOver = true;
	/**
	 * 存在播放记录中的数据，需要现实反色的item
	 * 
	 * @Fields playerRecordEntityId
	 */
	public static String playerRecordEntityId = "1";

	/**
	 * 调用
	 */
	private String sourceID;
	private boolean isFirstCreat = true;
	/**
	 * 当焦点在播放，收藏，直播电视台的按钮上的时候，用户按遥控器向下按钮，焦点要设置到剧集的第一个上
	 */
	OnKeyListener goVideoPageViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				/*
				 * TeleplayVideoPageView item = (TeleplayVideoPageView)
				 * (((HivePagerAdapter)
				 * videoPager.getAdapter()).getPrimaryItem());
				 * item.setDefault01Btn();
				 */
				return true;
			}
			return false;
		}
	};

	/**
	 * 当焦点在VideoPager上的某个按钮（2行按钮每行10个），当用户点击向下的按键的时候，焦点要跑到对应的年份分组的按钮上
	 */
	OnKeyListener goGroupPageViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				int pageNum = (int) (VIDEOS_CURRENT_YEAR_POSITION / VIDEO_GROUP_PAGE_SIZE);
				TeleplayVideoGroupView group = (TeleplayVideoGroupView) groupViews.get(pageNum);
				group.setVideoPageButtonNextFocus(VIDEOS_CURRENT_YEAR_POSITION);
				return true;
			}
			return false;
		}
	};

	/**
	 * 当焦点在电视台更多按钮上，当用户点击向下的时候，电视台的ViewPager上第一个获取焦点
	 */
	OnKeyListener goChannelPageViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				TeleplayLiveTVLogoView group = (TeleplayLiveTVLogoView) channelViews.get(CHANNELS_CURRENT_PAGE_NUM);
				group.setDefault01Focus();
				return true;
			}

			return false;
		}
	};

	/**
	 * 当分组按钮获取焦点的时候,根据当前按钮来判断
	 */
	IButtonFocusListener groupButtonFoucsListener = new IButtonFocusListener() {

		public void processFocus(int position) {
			changeVideoPagerBackgroud(position);
			VIDEOS_CURRENT_YEAR_POSITION = position;
			HivePagerAdapter adapter = mapYearAndAdapter.get(position);
			setGroupButtonTextColor(position);
			if (null != adapter && adapter != videoPager.getAdapter() && adapter.getCount() != 0) {
				videoPager.setAdapter(adapter);
			}
		}
	};

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.variety_detail_layout);
		init();
		getContentResolver().registerContentObserver(QIYIRecordUtils.PLAYER_RECORD, true, cob);
	}

	private ContentObserver cob = new ContentObserver(new Handler()) {
		@Override
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications();
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.d(TAG, "---onChange");
			//监听播放记录数据库变化，改变播放状态，（解决当走到onresum,并没有走到保存数据库，拿不到数据库）
			if (!isFirstCreat) {
				submitRequest(new getQIYIPlayerRecords());
				isFirstCreat = true;
			}
			// testBtn.setText(DataUtils.getChangeName(getApplicationContext()));
		}
	};

	/**
	 * 初始化界面
	 * 
	 * @Title: VarietyDetailActivity
	 * @Description: TODO
	 */
	private void init() {
		// 初始化图片下载器和相关的配置方法
		imageLoader = ImageLoader.getInstance();
		/**
		 * 用builder的模式给DisplayImageOptions() 构造传多个参数
		 */
		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.variety_default_img)
				.showImageOnFail(R.drawable.variety_default_img).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		dao = new CollectVoideoNewDAO(getApplicationContext());
		focusList = new ArrayList<View>();
		// 初始化奇异的sdk
		viewIds = new ArrayList<Integer>();
		// 添加剧集的每一期
		for (int i : viewId) {
			viewIds.add(i);
		}
		mapYearAndAdapter = new HashMap<Integer, HivePagerAdapter>();
		initView();
		dataService = new HiveTVService();
		// dialogLoadingView.setVisibility(View.VISIBLE);
		videoPager.setVisibility(View.INVISIBLE);
		groupPager.setVisibility(View.INVISIBLE);
		Serializable serializable = getIntent().getSerializableExtra("entity");
		sourceID = getIntent().getStringExtra("source");
		// 上个界面数据不为空， 并且添加到具体的控件
		if (null != serializable) {
			mFilmEntity = (FilmNewEntity) serializable;
			video_Entity = mFilmEntity;
			mVideoId = mFilmEntity.getId();
			dialogLoadingView.setVisibility(View.VISIBLE);
			if (null != mFilmEntity.getTime() && mFilmEntity.getTime().length() > 0) {
				yearsArray = mFilmEntity.getTime().split(",");
				requestVideoList(0);
				fillFilmInfo();
				// requestChannlList();// 根据综艺的名称去搜索播放此综艺的电视台
			} else {// 异常处理
				requestVideoList(0);
				fillFilmInfo();
				// if (null != dialogLoadingView) {
				// dialogLoadingView.setVisibility(View.INVISIBLE);
				// showErrorDialog(HiveViewErrorCode.E0000601, true);
				// }
			}
		} else {
			mVideoId = getIntent().getIntExtra("id", 0);
			// mVideoId = 333021;
			if (mVideoId != 0) {
				requestFilmDetail();
			} else {// 异常处理
				if (null != dialogLoadingView) {
					dialogLoadingView.setVisibility(View.INVISIBLE);
					showErrorDialog(HiveViewErrorCode.E0000601, true);
				}
				LogUtil.info("no data!!!!!");
			}
		}
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		moreLayout = (RelativeLayout) this.findViewById(R.id.teleplay_ll_3_more);
		mTitle = (TextView) this.findViewById(R.id.tv_title);
		mContent = (TextView) this.findViewById(R.id.tv_content);
		ivPlay = (ImageView) this.findViewById(R.id.iv_film_detail_play);
		mCover = (ImageView) this.findViewById(R.id.iv_film_detail_cover);
		mDescribeLine1 = (TextView) this.findViewById(R.id.tv_des_line1);
		ivCollect = (ImageView) findViewById(R.id.iv_film_detail_fav);
		ivCollect.setTag(false);
		videoPager = (HivePreloadViewPager) findViewById(R.id.teleplay_video_pagaer);

		ivPlay.setNextFocusDownId(R.id.teleplay_video_pagaer);

		groupPager = (HivePreloadViewPager) findViewById(R.id.teleplay_group_pagaer);
		dialogLoadingView = (ProgressDialog) findViewById(R.id.iv_films_loading);
		tvNoData = (TextView) findViewById(R.id.tv_no_data);
		ivLeft = (ImageView) findViewById(R.id.teleplay_tvs_iv_left);
		ivRight = (ImageView) findViewById(R.id.teleplay_tvs_iv_right);
		layoutTv1 = findViewById(R.id.lr_tv_1);
		layoutTv2 = findViewById(R.id.lr_tv_2);
		layoutTv3 = findViewById(R.id.lr_tv_3);
		layoutMore = findViewById(R.id.lr_tv_more);
		channelsPager = (ViewPager) findViewById(R.id.teleplay_pager_tvs);

		vChannel1 = (LinearLayout) findViewById(R.id.lr_tv_1);
		vChannel2 = (LinearLayout) findViewById(R.id.lr_tv_2);
		vChannel3 = (LinearLayout) findViewById(R.id.lr_tv_3);

		vChannel1.setOnFocusChangeListener(channelViewFocusChangeListener);
		vChannel1.setTag(0);
		vChannel2.setOnFocusChangeListener(channelViewFocusChangeListener);
		vChannel2.setTag(1);
		vChannel3.setOnFocusChangeListener(channelViewFocusChangeListener);
		vChannel3.setTag(2);
		vChannel1.setOnClickListener(new ChannlOnClickListener());
		vChannel2.setOnClickListener(new ChannlOnClickListener());
		vChannel3.setOnClickListener(new ChannlOnClickListener());

		layoutMore.setOnKeyListener(goChannelPageViewKeyListener);
		// layoutMore.setOnKeyListener(goVideoPageViewKeyListener);
		layoutMore.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (channelsPager.getVisibility() == View.INVISIBLE) {
					channelsPager.setVisibility(View.VISIBLE);
					ivLeft.setVisibility(View.VISIBLE);
					ivRight.setVisibility(View.VISIBLE);
				} else {
					channelsPager.setVisibility(View.INVISIBLE);
					ivLeft.setVisibility(View.INVISIBLE);
					ivRight.setVisibility(View.INVISIBLE);
				}

			}
		});
		/**
		 * 播放点击事件
		 */
		ivPlay.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				video_Entity = mFilmEntity;
				Log.v(TAG, "JSONAnalyze1====" + com.alibaba.fastjson.JSONObject.toJSONString(video_Entity));

				// if (!StringUtils.isEmpty(namefoces)) {
				// for (int j = 0; j < tmpVideos.size(); j++) {
				// if (namefoces.contains(tmpVideos.get(j).getEpname())) {
				// firstVideo = tmpVideos.get(j);
				// break;
				// }
				// }
				// }
				if (null != firstVideo) {
					// 请求的视频集合的id和单个的视频的url都满足，可以请求爱奇艺的sdk播放
					if (firstVideo.getVideoId() > 0 && firstVideo.getVideosetId() > 0) {
						isOnClick = true;
						startPlayerTime = System.currentTimeMillis();
						player_Entity = firstVideo;
						// String vrsAlbumId =
						// String.valueOf(firstVideo.getVideosetId());
						// String vrsTvId =
						// String.valueOf(firstVideo.getVideoId());
						// Log.d(TAG, "==vrsAlbumId:" + vrsAlbumId +
						// "==vrsTvId:" + vrsTvId);
						// QiYiPlayerUtil.startNewQiYiPlayer(VarietyDetailActivity.this,
						// vrsTvId, vrsAlbumId);
						QiYiPlayerUtil.startSDKPlayer(VarietyDetailActivity.this, com.alibaba.fastjson.JSONObject.toJSONString(video_Entity), null,
								false, true, false, null, false);
					//	handler.sendEmptyMessage(PLAYER_GO_ON);
						startPlayerTime = System.currentTimeMillis();
						long time = startPlayerTime - creatTime;
						sendStatistics(String.valueOf(time), player_Entity, DataType.CLICK_TAB_VIDEO);

					}

					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB).setViewPosition("0306").setSource(sourceID)
							.setEntity((VideoNewEntity) player_Entity).setDataType(DataType.CLICK_TAB_VIDEO)
							.setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT) ? ItemType.SUBJECT : ItemType.VIDEO).build());
				} else {
					// 提示没有剧集,无法播放
					if (!isFirst) {
						alertError();
					}
				}
			}
		});

		/**
		 * 收藏点击事件
		 */
		ivCollect.setOnClickListener(new RecommendRecordLayoutListener());

		/**
		 * 收藏点击事件
		 */

		/*
		 * ivCollect.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { if (null != mFilmEntity) { boolean
		 * isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString()); //
		 * 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据 if (isSaveed) {
		 * dao.delete("videoset_id = ?", new String[] {
		 * String.valueOf(mFilmEntity.getVideoset_id()) });
		 * ivCollect.setImageResource(R.drawable.film_detail_fav);
		 * ivCollect.setTag(false); } else { dao.insert(mFilmEntity);
		 * ivCollect.setImageResource(R.drawable.film_detail_fav_has);
		 * ivCollect.setTag(true); }
		 * 
		 * }
		 * 
		 * } });
		 */

		setNextFocus();

		initChannelPopuWindow();
		initVideoPopuWindow();

		// ivPlay.setOnKeyListener(goVideoPageViewKeyListener);
		// ivCollect.setOnKeyListener(goVideoPageViewKeyListener);
		layoutTv1.setOnKeyListener(goVideoPageViewKeyListener);
		layoutTv2.setOnKeyListener(goVideoPageViewKeyListener);
		layoutTv3.setOnKeyListener(goVideoPageViewKeyListener);
		layoutMore.setNextFocusDownId(R.id.teleplay_pager_tvs);

	}

	/**
	 * 收藏的监听
	 * 
	 * @ClassName: RecommendRecordLayoutListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月30日 上午10:10:55
	 * 
	 */
	class RecommendRecordLayoutListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			if (null != mFilmEntity) {
				boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
				// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
				if (isSaveed) {
					dao.delete("id = ?", new String[] { String.valueOf(mFilmEntity.getId()) });
					ivCollect.setImageResource(R.drawable.film_detail_fav);
					ivCollect.setTag(false);
					return null;
				} else {
					dao.insert(mFilmEntity);
					ivCollect.setImageResource(R.drawable.film_detail_fav_has);
					ivCollect.setTag(true);
				}

			}
			// start:修改默认XX为Tab.TAB，author:huzuwei
			return new DataHolder.Builder(getBaseContext()).setDataType(DataType.CLICK_TAB_FILM).setTabNo(Tab.TAB).setViewPosition("0101")
					.setEntity(mFilmEntity).setSrcType(ItemType.VIDEO).build();
			// end
		}
	}

	OnFocusChangeListener channelViewFocusChangeListener = new OnFocusChangeListener() {

		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				int position = (Integer) v.getTag();
				setChannelPopuWindowData(position);

				popuChannelWindow.showAsDropDown(v, -120, 0);

			} else {
				popuChannelWindow.dismiss();
			}
		}
	};

	/**
	 * 初始化视屏的浮窗
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 * @param p
	 */
	private void initChannelPopuWindow() {
		popuChannelView = getLayoutInflater().inflate(R.layout.teleplay_popuwindow_layout, null);
		popuBar = (SeekBar) popuChannelView.findViewById(R.id.sb_television_item);
		popuChannelLogo = (ImageView) popuChannelView.findViewById(R.id.iv_television_logo);
		popuVideoPoster = (ImageView) popuChannelView.findViewById(R.id.iv_thumb);
		popuChannelName = (TextView) popuChannelView.findViewById(R.id.tv_television_name);
		popuEndTime = (TextView) popuChannelView.findViewById(R.id.tv_end_time);
		popuStartTime = (TextView) popuChannelView.findViewById(R.id.tv_start_time);
		popuVideoName = (MarqueeText) popuChannelView.findViewById(R.id.tv_program);
		popuVideoName2 = (MarqueeText) popuChannelView.findViewById(R.id.tv_program_thumb);

		popuChannelWindow = new PopupWindow(popuChannelView, 202, 222);
		popuChannelWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	/**
	 * 初始化频道获取带点的浮窗
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 * @param p
	 */
	private void initVideoPopuWindow() {
		popuVideoView = getLayoutInflater().inflate(R.layout.variety_popuwindow_layout, null);
		popuVideoHotName = (MarqueeText) popuVideoView.findViewById(R.id.tv_var_name);
		popuVideoHotImg = (ImageView) popuVideoView.findViewById(R.id.iv_thumb);

		popuVideoWindow = new PopupWindow(popuVideoView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// popuVideoWindow.setAnimationStyle(R.style.PopupVideoAnimation);
		popuVideoWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 填充视频的PopupWindow 数据
	 * 
	 * @Title: VarietyDetailActivity
	 * @Description: TODO
	 * @param p
	 */
	private void setVideoPopuWindowData(VideoNewEntity p) {
		VideoNewEntity entity = p;
		String originalUrl = entity.getPicurl();
		int dotIndex = originalUrl.lastIndexOf(".");
		String sizeUrlExe = originalUrl.substring(dotIndex, originalUrl.length());
		String sizeUrlHead = originalUrl.substring(0, dotIndex);
		String sizeNewUrl = "";
		sizeNewUrl = sizeUrlHead + "_320_180" + sizeUrlExe;
		popuVideoHotImg.setImageResource(R.drawable.variety_default_img);
		if (null == imageLoader) {
			imageLoader = ImageLoader.getInstance();
		}
		imageLoader.displayImage(sizeNewUrl, popuVideoHotImg, optionsPoster);
		// popuVideoHotImg.setImageUrl(sizeNewUrl, handler);
		popuVideoHotName.setVisibility(View.VISIBLE);
		// 添加view的setFocusableInTouchMode(true)属性，文字可以生成走马灯效果
		popuVideoHotName.setFocusable(true);
		popuVideoHotName.setFocusableInTouchMode(true);
		popuVideoHotName.setStart(true);
		popuVideoHotName.setText(entity.getEpname());
	}

	/**
	 * 填充频道的PopupWindow 数据
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 * @param p
	 */
	private void setChannelPopuWindowData(int p) {
		TVChannelEntity entity = channls.get(p);

		popuChannelLogo.setImageResource(R.drawable.default_channel_logo);
		DisplayImageOptions logoOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_channel_logo)
				.showImageOnFail(R.drawable.default_channel_logo).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(entity.getChannel_logo(), popuChannelLogo, logoOptions);

		popuChannelName.setText(entity.getChannel_name());
		popuStartTime.setText(DateUtils.getTimeFromFullDate(entity.getStart_time()));
		popuEndTime.setText(DateUtils.getTimeFromFullDate(entity.getEnd_time()));

		DisplayImageOptions posterOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_teleplay_live_poster)
				.showImageOnFail(R.drawable.default_teleplay_live_poster).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(entity.getChannel_logo(), popuVideoPoster, posterOptions);
		popuVideoName.setText(entity.getName());

		long endLength = DateUtils.twoTimeDiffer(entity.getStart_time(), entity.getEnd_time());
		popuBar.setMax((int) endLength);

		Date nowDate = new Date();
		long currentProgress = DateUtils.twoTimeDiffer(entity.getStart_time(), DateUtils.formatTimeYyMmDdHhMmSs(nowDate));
		popuBar.setProgress((int) currentProgress);

		if (entity.getNext_starttime() != null && entity.getNext_name() != null) {
			popuVideoName2.setStart(true);
			popuVideoName2.setText(entity.getNext_starttime() + "    " + entity.getNext_name());
		}
	}

	/**
	 * 设置播放，搜藏的焦点
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 */
	private void setNextFocus() {
		ivPlay.setNextFocusRightId(R.id.iv_film_detail_fav);
		ivPlay.setNextFocusDownId(R.id.teleplay_video_pagaer);
		ivPlay.setNextFocusLeftId(ivPlay.getId());
		ivCollect.setNextFocusRightId(R.id.lr_tv_1);
		layoutTv1.setNextFocusRightId(R.id.lr_tv_2);
		layoutTv2.setNextFocusRightId(R.id.lr_tv_3);
		layoutTv3.setNextFocusRightId(R.id.lr_tv_more);

		ivCollect.setNextFocusLeftId(ivPlay.getId());
		ivCollect.setNextFocusDownId(R.id.teleplay_video_pagaer);
		ivCollect.setNextFocusRightId(ivCollect.getId());
		layoutTv1.setNextFocusLeftId(ivCollect.getId());
		layoutTv2.setNextFocusLeftId(layoutTv1.getId());
		layoutTv3.setNextFocusLeftId(layoutTv2.getId());

	}

	/**
	 * 显示综艺详情信息
	 */
	private void fillFilmInfo() {
		// 初始化 奇异的数据
		// submitRequest(new getQIYI());
		// submitRequest(new getQIYIPlayerRecords());
		// 当前详情页的entity
		video_Entity = mFilmEntity;
		try {
			// 去数据库里查找数据
			ArrayList<FilmNewEntity> filmEntities = dao.query(
					new String[] { String.valueOf(mFilmEntity.getId()), String.valueOf(mFilmEntity.getUid()) }, "id = ? and uid = ?", new String[] {
							String.valueOf(mFilmEntity.getId()), String.valueOf(mFilmEntity.getUid()) }, null);
			Log.d(TAG, "======filmEntities=" + filmEntities.size());
			if (filmEntities.size() != 0)
				handler.sendEmptyMessage(QUREY_SUCRESS);
		} catch (Exception e) {
			LogUtil.info(e.getMessage().toString());
		}
		StringBuilder describeLine1 = new StringBuilder();

		// 显综艺分类信息
		describeLine1
				.append((null != mFilmEntity.getTagNames() && !mFilmEntity.getTagNames().equals("") && !mFilmEntity.getTagNames().equals("null") && !mFilmEntity
						.getTagNames().equals("NULL"))
						&& (null != mFilmEntity.getMainActors() && !mFilmEntity.getMainActors().equals("")
								&& !mFilmEntity.getMainActors().equals("null") && !mFilmEntity.getMainActors().equals("NULL")) ? getResources()
						.getString(R.string.text_category) : "").append(mFilmEntity.getTagNames());
		
		if(!"".equals(mFilmEntity.getMainActors()) && !"null".equals(mFilmEntity.getMainActors())
				&& !"NULL".equals(mFilmEntity.getMainActors())){
		// 显示综艺节目的主持人
		describeLine1
				.append(null != mFilmEntity.getTagNames() && !mFilmEntity.getTagNames().equals("") && !mFilmEntity.getTagNames().equals("null")
						&& !mFilmEntity.getTagNames().equals("NULL") ? getResources().getString(R.string.text_film_detail_separater) : "")
				.append(!"".equals(mFilmEntity.getMainActors()) && !"null".equals(mFilmEntity.getMainActors())
						&& !"NULL".equals(mFilmEntity.getMainActors()) ? getResources().getString(R.string.text_host_actor) : "")
				.append(mFilmEntity.getMainActors());
		}
		name = mFilmEntity.getName();
		mDescribeLine1.setText(StringUtils.isEmpty(describeLine1.toString()) ? "" : describeLine1);

		mTitle.setText(StringUtils.isEmpty(mFilmEntity.getName()) ? "" : mFilmEntity.getName());// 综艺名称名称
		mContent.setText(StringUtils.isEmpty(mFilmEntity.getDesc()) ? "" : mFilmEntity.getDesc());// 综艺详情
		// 获取指定的图片url
		if (null != mFilmEntity.getPosterUrl() && mFilmEntity.getPosterUrl().length() > 0) {
			String sizeNewUrl = StringUtils.getImage260_360Url(mFilmEntity.getPosterUrl());

			// default_film_detail_big

			DisplayImageOptions posterOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_film_detail_big)
					.showImageOnFail(R.drawable.default_film_detail_big).resetViewBeforeLoading(false).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
			ImageLoader.getInstance().displayImage(sizeNewUrl, mCover, posterOptions);
		}

	}

	/**
	 * 处理网络返回的综艺数据，包括剧集信息，电视台信息等
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.tv.activity.BaseActivity#processData(android.os.Message)
	 */
	protected void processData(Message msg) {
		switch (msg.what) {
		case REQUEST_VIDEOS_SUCCESS:
			dialogLoadingView.setVisibility(View.INVISIBLE);

			if (null == tmpVideos || tmpVideos.size() == 0) {
				i++;
				if (i < yearsArray.length) {
					requestVideoList(i);
					return;
				}

				groupPager.setVisibility(View.INVISIBLE);
				videoPager.setVisibility(View.INVISIBLE);
				tvNoData.setVisibility(View.VISIBLE);

			} else {
				if (isFirst) {
					// 临时的视频结合的条目大于0的
					if (tmpVideos.size() > 0) {
						isFirst = false;
						firstVideo = tmpVideos.get(0);
						video_Entity = firstVideo;
						fillVideosViewPager();
						dialogLoadingView.setVisibility(View.INVISIBLE);
						videoPager.setVisibility(View.VISIBLE);
						groupPager.setVisibility(View.VISIBLE);

					}

				} else {
					fillNewDataSetToViewPager(msg.arg1);
				}
			}
			((VarietyVideosPageView) ((HivePagerAdapter) videoPager.getAdapter()).getViews().get(0)).setBtnRequsetFocus(0);
			break;
		case REQUEST_VIDEOS_FAIL:
			break;
		case REQUEST_CHANNELS_SUCCESS:
			/*
			 * requestNextProgram();// 根据电视台的Codes请求下一个节目 fillChannlsInfo();
			 */
			if (null != channls & channls.size() != 0) {
				requestNextProgram();// 根据电视台的Codes请求下一个节目
				fillChannlsInfo();
				// 区分不同平台的盒子，只有带HDMIin的盒子展示频道信息
				String deviceVersionCode = String.valueOf(DeviceBoxUtils.getDeviceVersionCode());
				if ("1".equals(deviceVersionCode)) {
					moreLayout.setVisibility(View.INVISIBLE);
					moreLayout.setFocusable(false);
					moreLayout.setFocusableInTouchMode(false);
				} else if ("2".equals(deviceVersionCode)) {
					moreLayout.setVisibility(View.VISIBLE);
					moreLayout.setFocusableInTouchMode(true);
					moreLayout.setFocusable(true);
				} else {
					moreLayout.setVisibility(View.INVISIBLE);
					moreLayout.setFocusable(false);
					moreLayout.setFocusableInTouchMode(false);
				}
			} else {
				moreLayout.setVisibility(View.INVISIBLE);
				moreLayout.setFocusable(false);
				moreLayout.setFocusableInTouchMode(false);
			}
			break;
		case REQUEST_CHANNELS_FAIL:
			break;
		case REQUEST_NEXT_SUCCESS:
			setNextProgram();
			break;
		case REQUEST_NEXT_FAIL:
			break;
		case REQUEST_VIDEO_SUCCESS:// 请求到数据详情后根据详情信息去加载列表
			if (null != mFilmEntity.getTime()) {
				yearsArray = mFilmEntity.getTime().split(",");
				requestVideoList(0);
				fillFilmInfo();
				// requestChannlList();// 根据综艺的名称去搜索播放此综艺的电视台
			} else {
				showErrorDialog(HiveViewErrorCode.E0000601, true);
			}
			break;
		case REQUEST_VIDEO_FAIL:
			// showDialogAboutNetFault();
			dialogLoadingView.setVisibility(View.INVISIBLE);
			break;
		case QUREY_SUCRESS:
			// 如果存在收藏的列表中 就显示已收藏
			ivCollect.setImageResource(R.drawable.film_detail_fav_has);
			ivCollect.setTag(true);
			break;
		case PLAYER_GO_ON:
			if (null != videoPager.getAdapter()) {
				try {
					mapVarietyVideosPageView=new HashMap<Integer, VarietyVideosPageView>();
					mapVarietyVideosPageViewIndex=new HashMap<String, Integer>();
					for(int i=0;i<tmpVideos.size();i++){
						Log.v(TAG, "tmpVideos "+tmpVideos.get(i).toString()+"count:"+i);
						mapVarietyVideosPageViewIndex.put(tmpVideos.get(i).getYear(), i);	
						mapVarietyVideosPageView.put(i, ((VarietyVideosPageView) ((HivePagerAdapter) videoPager.getAdapter()).getViews().get(i/20)));
					}
					
					// 循环清除所有页面高亮状态
					for (int i = 0; i < ((HivePagerAdapter) videoPager.getAdapter()).getViews().size(); i++) {
						((VarietyVideosPageView) ((HivePagerAdapter) videoPager.getAdapter()).getViews().get(i)).setClearColor();
					}
					// 当前view不为空时设置高亮状态
					Log.v(TAG, "PLAYER_GO_ON "+mapVarietyVideosPageViewIndex.get(playerRecordEntityId));
					Log.v(TAG, "PLAYER_GO_ON "+mapVarietyVideosPageViewIndex.get(playerRecordEntityId)%20);
					if (mapVarietyVideosPageView.get(mapVarietyVideosPageViewIndex.get(playerRecordEntityId))  != null) {
						mapVarietyVideosPageView.get(mapVarietyVideosPageViewIndex.get(playerRecordEntityId)) .setBtnRequsetFocus(mapVarietyVideosPageViewIndex.get(playerRecordEntityId)%20); 
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			ivPlay.setImageResource(R.drawable.player_go);
			break;
		case PLAYER_DETAIL:
			if (null !=videoPager.getAdapter()) {
				try {
					mapVarietyVideosPageView=new HashMap<Integer, VarietyVideosPageView>();
					mapVarietyVideosPageViewIndex=new HashMap<String, Integer>();
					for(int i=0;i<tmpVideos.size();i++){
						Log.v(TAG, "tmpVideos "+tmpVideos.get(i).toString());
						mapVarietyVideosPageView.put(i, ((VarietyVideosPageView) ((HivePagerAdapter) videoPager.getAdapter()).getViews().get(i/20)));
						mapVarietyVideosPageViewIndex.put(tmpVideos.get(i).getYear(), i);	
					}
					
					// 循环清除所有页面高亮状态
					for (int i = 0; i < ((HivePagerAdapter) videoPager.getAdapter()).getViews().size(); i++) {
						((VarietyVideosPageView) ((HivePagerAdapter) videoPager.getAdapter()).getViews().get(i)).setClearColor();
					}
					// 当前view不为空时设置高亮状态
					Log.v(TAG, "PLAYER_DETAIL "+mapVarietyVideosPageViewIndex.get(playerRecordEntityId));
					Log.v(TAG, "PLAYER_DETAIL "+mapVarietyVideosPageViewIndex.get(playerRecordEntityId)%20);
					if (mapVarietyVideosPageView.get(mapVarietyVideosPageViewIndex.get(playerRecordEntityId))   != null) {
						mapVarietyVideosPageView.get(mapVarietyVideosPageViewIndex.get(playerRecordEntityId)) .setBtnRequsetFocus(mapVarietyVideosPageViewIndex.get(playerRecordEntityId)%20); 
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			ivPlay.setImageResource(R.drawable.film_detail_play);
			break;
		case VIEWPAGER_RIGHT:// page 右翻页
			Log.d(TAG, "右翻页：：");

			break;
		case VIEWPAGER_LEFT:// page 左翻页
			Log.d(TAG, "左翻页：：");
			// 获取view tag 的实体类

			break;
		case VIDEO_WINDOW_SHOW:
			// 显示综艺期的海报图
			popuVideoWindow.showAsDropDown(viewItemFocus, 120, -200);

			break;
		default:
			break;
		}
	}

	/**
	 * 网络请求电影详情数据
	 */
	private void requestFilmDetail() {
		// // 请求电影详情数据
		submitRequest(new SafeRunnable() {

			public void requestData() {
				/*
				 * List<FilmEntity> list2 = dataService.getFilmDetail(
				 * VarietyDetailActivity.this, mVideoId); if (null != list2 &&
				 * list2.size() > 0) { FilmEntity mVideoEntity2 = list2.get(0);
				 */
				List<FilmNewEntity> list = dataService.getFilmDetail(VarietyDetailActivity.this, mVideoId/*
																										 * mVideoEntity2
																										 * .
																										 * getCp_videoset_id
																										 * (
																										 * )
																										 */);
				if (null != list && list.size() > 0) {
					mFilmEntity = list.get(0);
					video_Entity = mFilmEntity;
					handler.sendEmptyMessage(REQUEST_VIDEO_SUCCESS);
				} else {
					handler.sendEmptyMessage(REQUEST_VIDEO_FAIL);
				}
				/*
				 * }else { handler.sendEmptyMessage(REQUEST_VIDEO_FAIL); }
				 */
			}

			public void processServiceException(ServiceException e) {
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), true);
				handler.sendEmptyMessage(REQUEST_VIDEO_FAIL);
			}
		});

	}

	/**
	 * 得到综艺剧集,id=373436
	 */
	private void requestVideoList(final int position) {
		submitRequest(new SafeRunnable() {
			@SuppressWarnings("unchecked")
			public void requestData() {
				// 清空视频的数据
				if (null != tmpVideos) {
					tmpVideos.clear();
					LogUtil.info(tmpVideos.size() + "");
				}
				// 初始化视频的集合
				if (null == videos) {
					videos = new ArrayList<VideoNewEntity>();
				}
				// 获取服务器的数据
				if (null != yearsArray) {
					tmpVideos = dataService.getVideoList(VarietyDetailActivity.this, mFilmEntity.getId(), mFilmEntity.getCid(), "",
							VIDEO_LIST_PAGE_SIZE, 1, yearsArray[position]);
				} else {
					tmpVideos = dataService.getVideoList(VarietyDetailActivity.this, mFilmEntity.getId(), mFilmEntity.getCid(), "",
							VIDEO_LIST_PAGE_SIZE, 1, null);
				}

				try {
					Collections.sort(tmpVideos, new SortByTime());
				} catch (Exception e) {
					e.printStackTrace();
				}
				videos.addAll(tmpVideos);
				Message msg = handler.obtainMessage();
				msg.what = REQUEST_VIDEOS_SUCCESS;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}

			public void processServiceException(ServiceException e) {

				Message msg = handler.obtainMessage();
				msg.what = REQUEST_VIDEOS_FAIL;
				msg.arg1 = position;
				handler.sendMessage(msg);
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), false);
			}
		});
	}

	/**
	 * 得到播放当前综艺的电视台
	 */
	private void requestChannlList() {
		submitRequest(new SafeRunnable() {

			public void requestData() {
				// 格式化时间
				SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.CHINA);
				Date date2 = new Date(System.currentTimeMillis() + (60 * 1000));
				String dateString = time.format(date2);
				channls = dataService.getTvChannelByProgram(VarietyDetailActivity.this, mFilmEntity.getName(), dateString);
				handler.sendEmptyMessage(REQUEST_CHANNELS_SUCCESS);
			}

			public void processServiceException(ServiceException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 得到播放当前综艺的电视台的下一个节目
	 */
	private void requestNextProgram() {
		submitRequest(new SafeRunnable() {

			public void requestData() {
				String[] codes = new String[channls.size()];
				for (int i = 0; i < channls.size(); i++) {
					codes[i] = channls.get(i).getChannel_code();
				}
				nextps = dataService.getProgramsByCodes(codes, "", "");
				handler.sendEmptyMessage(REQUEST_NEXT_SUCCESS);
			}

			public void processServiceException(ServiceException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 根据得到综艺剧集数据进行分页
	 * 
	 * @param tmpVideos
	 * @return
	 */

	private List<VideoNewEntity> getPageViewList() {
		// 修改需要显示的viewpager 的类型的不需要手动分页，创建所有view
		if (null == tmpVideos || tmpVideos.size() == 0) {
			LogUtil.info("videos data is empty!!");
			return null;
		}
		return tmpVideos;
	}

	/**
	 * 返回综艺剧集分组列表的PageVeiw结合,每个人Page上有5组，每组都是1-20，21-40的形式
	 * 
	 * @return
	 */
	public List<VideoNewEntity> getPageGroupViewList() {
		groupEntitys = new ArrayList<VideoNewEntity>();
		// 如果年份信息不为空
		if (null != mFilmEntity.getTime() && mFilmEntity.getTime().length() > 0) {
			int groupCount = yearsArray.length;// 一共可以分多少组

			for (int i = 0; i < groupCount; i++) {// 根据年份的数组去创建要显示的实体类
				Log.d(TAG, "getPageGroupViewList==>groupCount::" + groupCount);
				VideoNewEntity entity = new VideoNewEntity();
				// entity.setVideo_type(6);
				entity.setEpname((yearsArray[i]));
				Log.d(TAG, "getPageGroupViewList==>groupCount::" + yearsArray[i]);
				// 添加要传递给组pager显示的list
				groupEntitys.add(entity);
			}
		}
		// 把创建好的实体类传递出来
		return groupEntitys;
	}

	private void fillChannlsInfo() {

		int channlCount = channls.size();

		if (channlCount > 0) {
			setStaticChannlsPosition(0, vChannel1);
			setStaticChannlsPosition(1, vChannel2);
			setStaticChannlsPosition(2, vChannel3);

			if (channlCount > 3) {
				layoutMore.setVisibility(View.VISIBLE);
				fillChannlsViewPager();
			}
		}
	}

	/**
	 * 设置下一页 的节目name和开始时间
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 */
	private void setNextProgram() {
		if (channls.size() == nextps.size()) {
			for (int i = 0; i < channls.size(); i++) {
				ProgramByCodeEntity nextEntity = nextps.get(i);
				TVChannelEntity channelEntity = channls.get(i);
				// 频道和tv是一样的，去填充他的name和开始的时间
				if (nextEntity.getChannel_code().equals(channelEntity.getChannel_code())) {
					channelEntity.setNext_name(nextEntity.getNext_name());
					channelEntity.setNext_starttime(nextEntity.getNext_start_time());
				}
			}
		}
	}

	/**
	 * 填充频道的viewpager
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 */
	private void fillChannlsViewPager() {
		channelViews = new ArrayList<View>();
		double channlCount = channls.size();
		int pageCount = (int) Math.ceil(channlCount / 5);
		int position = 3;// 前3个是静态的，在vChanenl123中已经指定，所以这里从3位置开始显示在下方的ViewPager中
		for (int i = 0; i < pageCount; i++) {
			TeleplayLiveTVLogoView tv = new TeleplayLiveTVLogoView(this);
			tv.setNextUpFocus(R.id.lr_tv_more);
			for (int j = 0; j < 5; j++) {
				if (position >= channlCount)
					break;
				tv.createGroupButton(j, position, goVideoPageViewKeyListener, channelViewFocusChangeListener);

				DisplayImageOptions posterOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_teleplay_live_poster)
						.showImageOnFail(R.drawable.default_teleplay_live_poster).resetViewBeforeLoading(false).cacheOnDisc(true)
						.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
				ImageLoader.getInstance().displayImage(channls.get(position).getChannel_logo(), tv.getLogoImageView(j), posterOptions);

				position++;
			}

			channelViews.add(tv);
		}

		FilmPagerAdapter adapter = new FilmPagerAdapter(channelViews);
		channelsPager.setAdapter(adapter);

		channelsPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				CHANNELS_CURRENT_PAGE_NUM = arg0;
				if (arg0 == 0) {
					// 影藏右边的箭头
					ivLeft.setVisibility(View.INVISIBLE);
				} else if (arg0 == channelViews.size() - 1) {
					// 影藏左边的箭头
					ivRight.setVisibility(View.INVISIBLE);
				} else {
					ivLeft.setVisibility(View.VISIBLE);
					ivRight.setVisibility(View.VISIBLE);
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 设置开始频道的位置
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:周一川
	 * @Description: TODO
	 * @param p
	 * @param v
	 */
	private void setStaticChannlsPosition(int p, LinearLayout v) {

		if (p > (channls.size() - 1))
			return;

		TVChannelEntity entity1 = channls.get(p);
		v.setVisibility(View.VISIBLE);
		ImageView imgLogo1 = (ImageView) v.getChildAt(0);

		DisplayImageOptions logoOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_channel_logo)
				.showImageOnFail(R.drawable.default_channel_logo).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(entity1.getChannel_logo(), imgLogo1, logoOptions);
	}

	/**
	 * 填充视频viewPager 第一次初始化的第一个要显示的内容
	 * 
	 * @Title: VarietyDetailActivity
	 * @Description: TODO
	 */
	// 记录当前页面的索引，便于比较用户操作是前翻页还是后翻页
	int oldPage = -1;

	private void fillVideosViewPager() {
		fillNewDataSetToViewPager(0);

		videoPager.setAdapter(mapYearAndAdapter.get(0));// ViewPager填充
		videoPager.setCallBackListener(new CallbackPageChangeListener() {

			public void onPageSelected(int arg0) {
				Log.d(TAG, "onPageSelected=>arg0::" + arg0);
				// 用户前后翻页的计算
				if (arg0 - oldPage > 0) {
					popuVideoWindow.dismiss();
				}
				oldPage = arg0;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.d(TAG, "onPageScrolled=>arg0::" + arg0 + "=arg1::" + arg1 + "=arg2::" + arg2);

			}

			public void onPageScrollStateChanged(int arg0) {
				Log.d(TAG, "onPageScrollStateChanged=>arg0::" + arg0);
				switch (arg0) {
				case 0:
					// 动画执行结束
					isViewPagerAnimactionOver = true;
					popuVideoWindow.showAsDropDown(viewItemFocus, 120, -200);
					break;
				default:
					popuVideoWindow.dismiss();
					// 动画执行开始
					isViewPagerAnimactionOver = false;
					break;
				}

			}
		});
		videoPager.setPreloadingListener(new OnPreloadingListener() {

			@Override
			public void setPageCurrent(int pageIndex) {
				if (pageIndex == 1)
					isFirstPager = true;
				else
					isFirstPager = false;

				int pageCount = (int) Math.ceil(((HivePagerAdapter) videoPager.getAdapter()).getLocalDataSize() / (double) 20);
				if (pageIndex >= pageCount)// 最后一页
					isLastPager = true;
				else
					isLastPager = false;

				Log.d(TAG, "allPages::" + pageCount);
			}

			@Override
			public void preLoading(int pageSize) {
				// TODO Auto-generated method stub

			}

			@Override
			public void preLoadNotFinish() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLastPage() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFirstPage() {
				// TODO Auto-generated method stub

			}
		});
		HivePagerAdapter groupAdapter = new HivePagerAdapter(this, new TeleplayGroupsPagerViewFactory(new ViewGroupItemFocusListener(),
				new ViewGroupItemKeyListener(), new ViewGroupItemClickListener()), groupPager, 5, new NeighborOneLineStrategy());
		groupAdapter.addDataSource(getPageGroupViewList());
		groupPager.setAdapter(groupAdapter);
	}

	private int i = 0;

	/**
	 * 填充剧情分页
	 * 
	 * @Title: VarietyDetailActivity
	 * @Description: TODO
	 * @param yearPosition
	 */
	private void fillNewDataSetToViewPager(int yearPosition) {
		// 填充剧集分页的ViewPager
		List<VideoNewEntity> videoViewsList = getPageViewList();
		HivePagerAdapter videoAdapter = null;
		videoAdapter = new HivePagerAdapter(this, new VarietyVideosPagerViewFactory(new ViewItemFocusListener(), new ViewItemKeyListener(),
				new ViewItemClickListener(), new ViewItemGetFocus()), videoPager, 20, new NeighborTwoLineStrategy());
		videoAdapter.addDataSource(videoViewsList);
		mapYearAndAdapter.put(yearPosition, videoAdapter);
		i++;
		if (i < yearsArray.length) {
			requestVideoList(i);
		}
	}

	/**
	 * 当综艺页面变化时，
	 */
	private void setGroupButtonTextColor(int position) {
		int pageNum = (int) (position / VIDEO_GROUP_PAGE_SIZE);
		TeleplayVideoGroupView group = (TeleplayVideoGroupView) groupViews.get(pageNum);
		group.setSpecialButtonTextColor(position);
	}

	/**
	 * 根据切换的页面，改变videoPager的背景
	 * 
	 * @param position
	 */
	private void changeVideoPagerBackgroud(int position) {
		position = position % 5;
		if (position == 0) {
			videoPager.setBackgroundResource(R.drawable.teleplay_video_bg0);
		} else if (position == 1) {
			videoPager.setBackgroundResource(R.drawable.teleplay_video_bg1);
		} else if (position == 2) {
			videoPager.setBackgroundResource(R.drawable.teleplay_video_bg2);
		} else if (position == 3) {
			videoPager.setBackgroundResource(R.drawable.teleplay_video_bg3);
		} else if (position == 4) {
			videoPager.setBackgroundResource(R.drawable.teleplay_video_bg4);
		}

	}

	class getQIYIPlayerRecords extends SafeRunnable {

		@Override
		public void requestData() {

			String videoId = String.valueOf(mVideoId);
			if (QIYIRecordUtils.getHistoryList(VarietyDetailActivity.this, videoId) != null
					&& QIYIRecordUtils.getHistoryList(VarietyDetailActivity.this, videoId).size() != 0) {
				for (PlayerRecordEntity entity : QIYIRecordUtils.getHistoryList(VarietyDetailActivity.this, videoId)) {
					PlayerRecordEntity entitys = entity;
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getName()::" + entitys.getName());
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getStartTime()::" + entitys.getStartTime());
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getAlbums()::" + entitys.getAlbums());
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getCurrentEpisode()::" + entitys.getCurrentEpisode());
					playerRecordEntityId=entitys.getCurrentEpisode();
					if (entity.getCurrentEpisode().equals("-1")) {
						handler.sendEmptyMessage(PLAYER_DETAIL);
					} else {
					//	handler.sendEmptyMessageDelayed(PLAYER_GO_ON, 2000);
						handler.sendEmptyMessage(PLAYER_GO_ON);
					}
				}

			} else {
				handler.sendEmptyMessage(PLAYER_DETAIL);
			}

		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub

		}
	}


	/*
	 * 窗口被覆盖重新显示调用的方法 (non-Javadoc)
	 * 
	 * @see com.hiveview.tv.activity.BaseActivity#onResume()
	 */

	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "-----onResume");
		super.onResume();
		ivPlay.requestFocus();
		submitRequest(new getQIYIPlayerRecords());

		isFirstCreat = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "---onDestroy");
		getContentResolver().unregisterContentObserver(cob);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onpause");
	}

	/**
	 * 详情页中有正在播放的电视剧的时候点击电视 换台的方法
	 * 
	 * @ClassName: ChannlOnClickListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月5日 上午10:42:03
	 * 
	 */
	class ChannlOnClickListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.lr_tv_1:// 电视1号位
				SwitchChannelUtils.switchChannel(getBaseContext(), popuChannelName.getText().toString(), false, AppScene.getScene());
				break;
			case R.id.lr_tv_2:// 电视2号位
				SwitchChannelUtils.switchChannel(getBaseContext(), popuChannelName.getText().toString(), false, AppScene.getScene());
				break;
			case R.id.lr_tv_3:// 电视3号位
				SwitchChannelUtils.switchChannel(getBaseContext(), popuChannelName.getText().toString(), false, AppScene.getScene());
				break;
			}
		}

	}

	/**
	 * list 的排序方法
	 * 
	 * @ClassName: SortByTime
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月5日 上午10:59:36
	 * 
	 */
	class SortByTime implements Comparator {

		public int compare(Object lhs, Object rhs) {
			VideoNewEntity mfileEntity1 = (VideoNewEntity) lhs;
			VideoNewEntity mfileEntity2 = (VideoNewEntity) rhs;
			LogUtil.info("mfileEntity1.getYear()::" + mfileEntity1.getYear());
			LogUtil.info("nteger.parseInt(mfileEntity2.getYear())::" + mfileEntity2.getYear());
			// 排序规则小数的排在前边，大树的排后边
			if (Integer.parseInt(mfileEntity1.getYear()) > Integer.parseInt(mfileEntity2.getYear())) {// 大的返回-1排在后边
				return -1;
			} else if (Integer.parseInt(mfileEntity1.getYear()) == Integer.parseInt(mfileEntity2.getYear())) {// 相等的就返回0
				return 0;
			} else if (Integer.parseInt(mfileEntity1.getYear()) < Integer.parseInt(mfileEntity2.getYear())) {// 小的返回1排在前边
				return 1;
			}
			return 0;
		}

	}

	/**
	 * 
	 */
	class ViewItemFocusListener implements CallBackItemViewFocusListener {

		public void onCallBackFocusChange(View v, boolean has) {

		}

	}

	/**
	 * viewItem 身上的按键事件
	 * 
	 * @ClassName: ViewItemKeyListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月17日 下午3:42:59
	 * 
	 */
	int viewPagerIndex;

	class ViewItemKeyListener implements CallBackItemViewKeyListener {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (viewItemViewIndex != -1 && keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (viewItemViewIndex) {// 0代表的是第一行的按钮，1代表的是第二行的按钮
				case 0:
					// 计算在下按键的时候焦点的走向
					if (isLastPager && isOneLine) {

						HivePagerAdapter adapter = (HivePagerAdapter) videoPager.getAdapter();
						int allItem = adapter.getDataSource().size();
						int theLastOne = (int) (allItem % VIDEO_PAGE_SIZE != 0 ? (allItem % VIDEO_PAGE_SIZE) - 1 : VIDEO_PAGE_SIZE - 1);
						int lastPage = (int) Math.ceil(allItem / (double) VIDEO_PAGE_SIZE) - 1;

						if (theLastOne < VIDEO_PAGE_SIZE / 2) {
							// 不为空就回到上一次获取焦点的组view 上去
							if (null != btnGetFocus)
								btnGetFocus.requestFocus();
							else
								// 如果为空就代表这第一次走到view 就应该走到第一个分组的view上去
								groupPager.requestFocus();
						} else {

							Log.d(TAG, "allItem::" + allItem);
							Log.d(TAG, "theLastOne::" + theLastOne);
							Log.d(TAG, "lastPage::" + lastPage);
							VarietyVideosPageView tvpv = (VarietyVideosPageView) adapter.getViews().get(lastPage);
							if (null != tvpv)
								tvpv.getBtn(theLastOne);
							return true;
						}
					}
					break;
				case 1:
					if (null != btnGetFocus)
						// 不为空的时候上次是那个分组的view 获取的焦点就回到那个view
						btnGetFocus.requestFocus();
					else
						// 如果分组的btn为空的话，代表着从来没有分组的焦点获取过焦点，那个下来的焦点必定是第一个分组
						groupPager.requestFocus();
					break;
				default:
					break;
				}
			}

			return false;
		}
	}

	/**
	 * @author zhangpengzhan
	 * 
	 *         2014年4月14日 上午10:21:38
	 * 
	 *         点击view 直接跳转到播放器 播放文件
	 */
	class ViewItemClickListener implements View.OnClickListener {

		public void onClick(View v) {
			video_Entity = mFilmEntity;
			Log.v(TAG, "JSONAnalyze1====" + com.alibaba.fastjson.JSONObject.toJSONString(video_Entity));
			Log.d(TAG, "ViewItemClickListener==>::viewItem的点击事件");
			// 从view 中取出对应的tag信息
			VideoNewEntity video = (VideoNewEntity) v.getTag();
			// 解析出相应的参数，传递给播放器
			if (video.getVideoId() > 0 && video.getVideosetId() > 0) {
				isOnClick = true;
				startPlayerTime = System.currentTimeMillis();
				// 播放器需要的信息
				// String vrsAlbumId = String.valueOf(video.getVideosetId());
				// String vrsTvId = String.valueOf(video.getVideoId());
				// QiYiPlayerUtil.startNewQiYiPlayer(VarietyDetailActivity.this,
				// vrsTvId, vrsAlbumId);
				QiYiPlayerUtil.startSDKPlayer(VarietyDetailActivity.this, com.alibaba.fastjson.JSONObject.toJSONString(video_Entity),
						com.alibaba.fastjson.JSONObject.toJSONString(video), true, false, false, null, false);
			//	handler.sendEmptyMessage(PLAYER_GO_ON);
				startPlayerTime = System.currentTimeMillis();
				long time = startPlayerTime - creatTime;
				sendStatistics(String.valueOf(time), video, DataType.CLICK_TAB_VIDEO);
				KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB).setViewPosition("0306").setSource(sourceID)
						.setEntity(video).setDataType(DataType.CLICK_TAB_VIDEO)
						.setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT) ? ItemType.SUBJECT : ItemType.VIDEO).build());
			}
		}

	}

	/**
	 * 当前获取焦点的view 和 viewIndex
	 * 
	 * @ClassName: ViewItemGetFocus
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月17日 下午3:09:53
	 * 
	 */
	class ViewItemGetFocus implements OnBtnGetFocus {

		@Override
		public void btnGetFocus(View v, int viewIndexs, boolean has) {
			// TODO Auto-generated method stub
			if (has) {
				if (null != v)
					viewItemFocus = (Button) v;
				Log.d(TAG, "ViewY::" + v.getY());
				Log.d(TAG, "ViewX::" + v.getX());
				viewIndex = viewIndexs;
				// 计算当前的view是第一行还是第二行
				viewItemViewIndex = viewIndexs / 10;
				if (isLastPager) {// 如果是最后一页就算出剩余单行的个数
					HivePagerAdapter adapter = (HivePagerAdapter) videoPager.getAdapter();
					int items = adapter.getLocalDataSize();
					Log.d(TAG, "adapter.size::" + items);
					int lastItems = items % 20;
					if (lastItems <= 10) {// 如果最后一页小于10个就只有一行
						isOneLine = true;
					} else {// 大于10就算第一行是单行的从第几个开始
						int twoLineItems = lastItems % 10;// 第二行有几个，第一行就从第几个开始是单行
						if (viewIndex > twoLineItems - 1 && viewIndex < 10)// 这个这能限制在第一行
							isOneLine = true;// 赋值
						else
							isOneLine = false;
					}
				}
				VideoNewEntity entity = (VideoNewEntity) v.getTag();
				Log.d(TAG, "====position=====" + entity);
				setVideoPopuWindowData(entity);
				// 焦点移到第6个浮窗应该显示到左边
				if (viewIds.contains(v.getId())) {
					popuVideoWindow.showAsDropDown(v, -220, -200);
				} else {
					popuVideoWindow.showAsDropDown(v, 120, -200);
				}

			} else {
				// 失去焦点把popuWind关闭
				popuVideoWindow.dismiss();
			}
		}

	}

	/**
	 * 
	 * 组列表在每个tiem上边跳转的时候综艺的期集也跟着切换 焦点监听
	 * 
	 * @ClassName: ViewGroupItemFocusListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月10日 下午5:18:04
	 * 
	 */
	class ViewGroupItemFocusListener implements CallBackItemViewFocusListener {

		public void onCallBackFocusChange(View view, boolean has) {
			if (has) {
				// button 在获取焦点的时候反色在焦点移动到上边的条目上的时候
				// 反色不消失，只有在移动到其他的分组的按钮的时候才需要改变该按钮的反色效果
				if (null != btnGetFocus)// 只要不为空就代表这是上一个要还在焦点状态的btn
					btnGetFocus.setTextColor(getResources().getColor(R.color.white));
				// 这个新的btn是要获取焦点的btn
				btnGetFocus = (Button) view;
				// 为这个要获取焦点的btn 改变他的文字背景颜色
				btnGetFocus.setTextColor(getResources().getColor(R.color.yellow));
				// 获取view tag 的实体类
				VideoNewEntity entity = (VideoNewEntity) view.getTag();
				// 当前的entity在列表中的位置
				int viewPagerIndex = groupEntitys.indexOf(entity);
				// 根据实体类在列表中的位置得到要切换的adapter
				HivePagerAdapter adapter = mapYearAndAdapter.get(viewPagerIndex);
				// 代码健壮性空判断
				if (null != adapter && adapter != videoPager.getAdapter() && adapter.getCount() != 0) {
					videoPager.setAdapter(mapYearAndAdapter.get(viewPagerIndex));// ViewPager填充
					// 同时切换期数的背景
					changeVideoPagerBackgroud(viewPagerIndex);
				}

				Log.d(TAG, "ViewGroupItemFocusListener==>has::" + has + "=viewpagerIndex=::" + viewPagerIndex);
			}
		}

	}

	/**
	 * @ClassName: ViewGroupItemKeyListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月11日 上午10:31:54
	 * 
	 */
	class ViewGroupItemKeyListener implements CallBackItemViewKeyListener {

		public boolean onKey(View v, int keyCode, KeyEvent event) {

			return false;
		}
	}

	/**
	 * @author zhangpengzhan
	 * 
	 *         2014年4月14日 上午10:21:38
	 * 
	 *         点击view 直接跳转到播放器 播放文件
	 */
	class ViewGroupItemClickListener implements View.OnClickListener {

		public void onClick(View v) {

		}

	}

	/**
	 * 计算出每个group的每行第一个和最后一个
	 * 
	 * @Title: VarietyDetailActivity
	 * @author:张鹏展
	 * @Description:
	 */
	public void goNextGroup(int everyPageItem, int allItems, int lines) {
		// 一页的数量
		int onePageItems = everyPageItem;
		// 全部的总数
		int allPageItems = allItems;
		// 一页的行数
		int onePageLines = lines;

	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("play".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(VarietyDetailActivity.this, "播放", intent);
					if (null != firstVideo) {
						// 请求的视频集合的id和单个的视频的url都满足，可以请求爱奇艺的sdk播放

						if (firstVideo.getVideoId() > 0 && firstVideo.getVideosetId() > 0) {
							isOnClick = true;
							startPlayerTime = System.currentTimeMillis();
							player_Entity = firstVideo;
							String vrsAlbumId = String.valueOf(firstVideo.getVideosetId());
							String vrsTvId = String.valueOf(firstVideo.getVideoId());
							Log.d(TAG, "==vrsAlbumId:" + vrsAlbumId + "==vrsTvId:" + vrsTvId);

							// QiYiPlayerUtil.startNewQiYiPlayer(VarietyDetailActivity.this,
							// vrsTvId, vrsAlbumId);
							QiYiPlayerUtil.startSDKPlayer(VarietyDetailActivity.this, com.alibaba.fastjson.JSONObject.toJSONString(video_Entity),
									null, false, true, false, null, false);
							handler.sendEmptyMessage(PLAYER_GO_ON);
							startPlayerTime = System.currentTimeMillis();
							long time = startPlayerTime - creatTime;
							sendStatistics(String.valueOf(time), player_Entity, DataType.CLICK_TAB_VIDEO);

						}
						// if (firstVideo.getCp_videoset_id().length() > 0
						// && firstVideo.getCp_video_id().length() > 0) {
						// isOnClick = true;
						// startPlayerTime = System.currentTimeMillis();
						// player_Entity = firstVideo;
						// String vrsAlbumId = firstVideo.getCp_videoset_id()
						// .split(",")[1];
						// String vrsTvId =
						// firstVideo.getCp_video_id().split(",")[1];
						// Log.d(TAG, "==vrsAlbumId:" + vrsAlbumId +
						// "==vrsTvId:"
						// + vrsTvId);
						//
						// QiYiPlayerUtil
						// .startQiYiPlayer(VarietyDetailActivity.this,
						// vrsTvId, vrsAlbumId);
						// handler.sendEmptyMessage(PLAYER_GO_ON);
						// startPlayerTime = System.currentTimeMillis();
						// long time = startPlayerTime - creatTime;
						// // sendStatistics(String.valueOf(time),
						// player_Entity,
						// // DataType.CLICK_TAB_VIDEO);
						// }
					} else {
						// 提示没有剧集,无法播放
						if (!isFirst) {
							alertError();
						}
					}

				} else if ("collect".equals(command)) {
					if (null != mFilmEntity) {
						boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
						// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
						if (isSaveed) {
							HomeSwitchTabUtil.closeSiRi(VarietyDetailActivity.this, "您已经收藏", intent);
						} else {
							dao.insert(mFilmEntity);
							ivCollect.setImageResource(R.drawable.film_detail_fav_has);
							ivCollect.setTag(true);
							HomeSwitchTabUtil.closeSiRi(VarietyDetailActivity.this, "收藏", intent);
						}

					}
				} else if ("canclecollect".equals(command)) {
					if (null != mFilmEntity) {
						boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
						// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
						if (isSaveed) {
							dao.delete("id = ?", new String[] { String.valueOf(mFilmEntity.getId()) });
							ivCollect.setImageResource(R.drawable.film_detail_fav);
							ivCollect.setTag(false);
							HomeSwitchTabUtil.closeSiRi(VarietyDetailActivity.this, "取消收藏", intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(VarietyDetailActivity.this, "您还未收藏", intent);
						}

					}

				}
			}

		}
	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.VarietyDetailActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("play", new String[] { "播放", "续播" });
		commands.put("collect", new String[] { "收藏" });
		commands.put("canclecollect", new String[] { "取消收藏" });
		Log.d(TAG, "onQunery====>1");

		HashMap<String, String[]> fuzzayWords1 = new HashMap<String, String[]>();
		// fuzzayWords1.put("video", VIDEOS);
		JSONObject jsonObject = null;
		// 预定义槽二
		// String fuzzayVideoKey = "video";
		// String[] fuzzayVideoValue1 = new String[] { "收藏" };
		// String[] fuzzayVideoValue2 = new String[] { "取消收藏"};
		ArrayList<String[]> fuzzayValue = new ArrayList<String[]>();
		// fuzzayValue.add(fuzzayVideoValue1);
		// fuzzayValue.add(fuzzayVideoValue2);
		// 第三类
		HashMap<String, ArrayList<String[]>> fuzzayWords2 = new HashMap<String, ArrayList<String[]>>();
		fuzzayWords2.put("hello", fuzzayValue);
		try {
			jsonObject = JsonUtil.makeScenceJson(scenceId, commands, fuzzayWords1, fuzzayWords2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "onQunery====>2");
		return jsonObject.toString();

	}

}
