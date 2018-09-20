package com.hiveview.tv.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
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
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.CallbackPageChangeListener;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.adapter.FilmPagerAdapter;
import com.hiveview.tv.common.factory.TeleplayGroupsPagerViewFactory;
import com.hiveview.tv.common.factory.TeleplayVideosPagerViewFactory;
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
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.QIYIRecordUtils;
import com.hiveview.tv.utils.QiYiPlayerUtil;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.TeleplayLiveTVLogoView;
import com.hiveview.tv.view.TeleplayVideoGroupView;
import com.hiveview.tv.view.TeleplayVideoGroupView.IButtonFocusListener;
import com.hiveview.tv.view.TeleplayVideoPageView;
import com.hiveview.tv.view.TeleplayVideosGroupsView;
import com.hiveview.tv.view.TeleplayVideosPageView;
import com.hiveview.tv.view.TeleplayVideosPageView.OnBtnGetFocus;
import com.hiveview.tv.view.TvPreloadingViewPager.IPreLoadingListener;
import com.hiveview.tv.view.television.MarqueeText;
import com.paster.util.JsonUtil;

/**
 * @ClassName: TeleplayDetailActivity
 * @Description: 电视剧详情页
 * @author:
 * @date 2014年12月5日 下午2:33:32
 * 
 */
public class TeleplayDetailActivity extends BaseActivity implements IPreLoadingListener {
	private String name = "";
	private FilmNewEntity mVideoEntity = null;
	private TextView mTitle;
	private TextView mContent;
	private ImageView ivPlay;
	private ImageView ivCollect;
	private ImageView ivLeft;
	private ImageView ivRight;
	private ImageView mCover;
	private TextView mDescribeLine1;
	private TextView mDescribeLine2;
	private HiveTVService dataService = null;
	private ArrayList<VideoNewEntity> videos = null;
	/**
	 * 剧集的总数的列表
	 * 
	 * @Fields newVideos
	 */
	private ArrayList<VideoNewEntity> newVideos = new ArrayList<VideoNewEntity>();
	/**
	 * 请求到的剧集数据
	 * 
	 * @Fields rquestVideos
	 */
	private ArrayList<VideoNewEntity> rquestVideos = null;
	private ArrayList<TVChannelEntity> channls = null;
	private ArrayList<ProgramByCodeEntity> nextps = null;
	private final int REQUEST_VIDEOS_SUCCESS = 1;
	private final int REQUEST_VIDEOS_FAIL = -1;
	private final int REQUEST_CHANNELS_SUCCESS = 2;
	private final int REQUEST_CHANNELS_FAIL = -2;
	private final int REQUEST_NEXT_SUCCESS = 3;
	private final int REQUEST_NEXT_FAIL = -3;
	private final int REQUEST_VIDEO_DETAIL_SUCCESS = 4;
	private final int REQUEST_VIDEO_DETAIL_FAIL = -4;
	/**
	 * 剧集添加数据。一次大于100集的时候需要多次请求数据
	 * 
	 * @Fields REQUEST_VIDEO_ADD_RESUASE
	 */
	private final int REQUEST_VIDEO_ADD_RESOURCE = 0x00180;
	private HivePreloadViewPager videoPager = null;// 电视剧剧集的viewpager
	private HivePreloadViewPager groupPager = null;// 分组用的viewpager
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
	private int mVideoId = 1;
	private int mVideoType = 1;
	/***
	 * 与adapterVideos相关联的数据源
	 */
	private List<View> videoViewsList = null;
	/***
	 * 与adapterGroup相关联的数据源
	 */
	private List<View> groupViews = null;
	/**
	 * 与电视台ViewPager的Adapter相关的数据源
	 */
	private List<View> channelViews = null;
	/**
	 * 电视剧按1-20文本的形式应该可以分多少组的集合
	 */
	private List<VideoNewEntity> listGroupText = null;
	/**
	 * 每页显示电视剧的集数
	 */
	private final double VIDEO_PAGE_SIZE = 20;
	/**
	 * 按1-20集进行分组，每页显示的组数
	 */
	private final double VIDEO_GROUP_PAGE_SIZE = 5;

	/**
	 * 焦点选中电视台，浮现的电视剧的相关的信息
	 */
	private PopupWindow pWindow = null;
	private ImageView popuVideoPoster = null;
	private ImageView popuChannelLogo = null;
	private TextView popuChannelName = null;
	private SeekBar popuBar = null;
	private View popuView;
	private TextView popuStartTime = null;
	private TextView popuEndTime = null;
	private MarqueeText popuVideoName = null;
	private MarqueeText popuVideoNextName = null;
	/**
	 * 显示数据列表的ViewPager
	 */
	private HivePreloadViewPager mViewPager = null;
	private HivePagerAdapter adapter = null;

	/**
	 * 剧集ViewPager的Adapter
	 */
	private FilmPagerAdapter adapterVideos = null;
	/**
	 * 剧集分组的ViewPager的Adapter
	 */
	private FilmPagerAdapter adapterGroup = null;

	/**
	 * 电视台的ViewPager上，每页显示的数量，默认数量为5
	 */
	private final double CHANNEL_GROUP_PAGE_SIZE = 5;
	/**
	 * 当前ViewPager显示剧集的页数，如：ViewPager的第三页，此时值为2
	 */
	private int VIDEOS_CURRENT_PAGE_NUM = 0;
	/**
	 * 每页请求剧集的个数，默认为800
	 */
	private int REQUEST_VIDEOS_PAGE_SIZE = 100;

	private int videoset_total = 0;

	/**
	 * 记录要请求网络数据的页码
	 */
	private int REQUEST_VIDEOS_PAGE_NUM = 1;
	/**
	 * 按年份查询的值如：2012
	 */
	private String REQUEST_VIDEOS_YEAR = "";
	/**
	 * 当前ViewPager显示电视台的页数，如：ViewPager的第三页，此时值为2
	 */
	private int CHANNELS_CURRENT_PAGE_NUM = 0;
	/**
	 * 查询 是否已经在收藏列表中
	 */
	private final int QUREY_SUCRESS = 0x00123;
	/**
	 * 剧集的viewpager翻页的时候发送的消息
	 * 
	 * @Fields ITEMVIEWPAGESINDEX
	 */
	private final int ITEMVIEWPAGESINDEX = 0x00652;

	/**
	 * 刷新播放按钮的图片
	 * 
	 * @Fields PLAYER_DETAIL
	 */
	private static final int PLAYER_DETAIL = 0x00589;

	/**
	 * 刷新pager页
	 * 
	 * @Fields REFUSE_VIEW
	 */
	private final int REFUSE_VIEW = 0x00412;

	/**
	 * shoujuku
	 */
	CollectVoideoNewDAO dao;
	/**
	 * 改变播放按钮 改为需播
	 */
	private final int PLAYER_GO_ON = 0x00140;
	/**
	 * 比较 当前 视频是否在观看记录里
	 */
	private String vrsAlbumId_;
	/**
	 * 没数据时显示的数据
	 */
	private TextView tvNoData;

	private RelativeLayout moreLayout;

	/**
	 * 当前获取焦点所在的集数的view
	 * 
	 * @Fields getFocusView
	 */
	private View getFocusView;
	private List<Integer> focusList;

	/**
	 * 当前实体类所在的位置
	 * 
	 * @Fields videosIndex
	 */
	private int videosIndex = -1;
	/**
	 * 来自播放记录中查询到的数据
	 * 
	 * @Fields vrsAlbumIdFromDatebase
	 */
	private String vrsAlbumIdFromDatebase = "";
	/**
	 * 从播放记录的数据库中查询到的数据
	 * 
	 * @Fields vrsTvIdFromDatebase
	 */
	private String vrsTvIdFromDatebase = "";
	/**
	 * 得到初始化状态是否成功
	 * 
	 * @Fields isInitPlayerRecordSuccess
	 */
	private boolean isInitPlayerRecordSuccess;

	/**
	 * 存在播放记录中的数据，需要现实反色的item
	 * 
	 * @Fields playerRecordEntityId
	 */
	public static String playerRecordEntityId = "1";
	public int order = 0;
	/**
	 * pager 动画执行结束
	 * 
	 * @Fields isViewPagerAnimactionOver
	 */
	private boolean isViewPagerAnimactionOver = true;
	/**
	 * 在查询时候存在播放记录的时候，显示的loading
	 * 
	 * @Fields playerProgress
	 */
	// private ProgressBar playerProgress;
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
	 * 是单行的情况
	 * 
	 * @Fields isOneLine
	 */
	private boolean isOneLine = false;
	/**
	 * 电视剧的所在页码
	 * 
	 * @Fields viewItemPages
	 */
	private int viewItemPages = 0;

	/**
	 * 电视剧排序位置
	 * 
	 * @Fields videoOrder
	 */
	private int videoOrder = 0;

	/**
	 * 对于onResume 而言
	 * 
	 * @Fields isFirstCreat
	 */
	private boolean isFirstCreat = true;

	/**
	 * 剧集的最后一个
	 * 
	 * @Fields isVideoItemLastPage
	 */
	private boolean isVideoItemLastPage = false;
	/**
	 * 分组的最后一个
	 * 
	 * @Fields isVideoGroupLastPage
	 */
	private boolean isVideoGroupLastPage = false;
	/**
	 * 调用播放器的来源
	 */
	private String sourceID;
	/**
	 * 当焦点在播放，收藏，直播电视台的按钮上的时候，用户按遥控器向下按钮，焦点要设置到剧集的第一个上
	 */
	OnKeyListener goVideoPageViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (videoViewsList != null && videoViewsList.size() > 0) {
					((TeleplayVideoPageView) videoViewsList.get(VIDEOS_CURRENT_PAGE_NUM)).setDefault01Btn();
					return true;
				}
			}
			return false;
		}
	};

	/**
	 * 当焦点在播放，收藏，直播电视台的按钮上的时候，用户按遥控器向下按钮，焦点要设置到剧集的第一个上
	 */
	OnKeyListener goGroupPageViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				int pageNum = (int) (VIDEOS_CURRENT_PAGE_NUM / VIDEO_GROUP_PAGE_SIZE);
				TeleplayVideoGroupView group = (TeleplayVideoGroupView) groupViews.get(pageNum);
				group.setVideoPageButtonNextFocus(VIDEOS_CURRENT_PAGE_NUM);
				return true;
			}
			return false;
		}
	};
	/**
	 * 电视剧的每集
	 */
	OnFocusChangeListener videoViewFocusChangeListener = new OnFocusChangeListener() {

		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {

				getFocusView = v;
				focusList.add((Integer) v.getTag());
				int index = focusList.size();
				if (index > 3)
					focusList.remove(0);
				Log.d(TAG, "onFocusChang->view.id::" + v.getId() + "===tag==" + (((Integer) v.getTag()) % 20));

			}
			/*
			 * Button btn = null; if(v instanceof Button){ btn = (Button) v;
			 * }else{ return; } if (hasFocus) { btn.setTextColor(0xff8c00); }
			 * else { btn.setTextColor(0xbcbcbc); }
			 */
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
	 * 当分组按钮获取焦点的时候，处理电视剧的ViewPager翻到对应的页面
	 */
	IButtonFocusListener groupButtonFoucsListener = new IButtonFocusListener() {

		public void processFocus(int position) {
			videoPager.setCurrentItem(position);
		}
	};

	/**
	 * 当电视台的ViewPager上的View（显示电视台的Logo）获取焦点的时候显示，此电视台播放当前电视剧的海报
	 */
	OnFocusChangeListener channelViewFocusChangeListener = new OnFocusChangeListener() {

		public void onFocusChange(View v, boolean hasFocus) {

			if (hasFocus) {
				int position = (Integer) v.getTag();
				setPopuWindowData(position);
				pWindow.showAsDropDown(v, -120, 0);
			} else {
				pWindow.dismiss();
			}
		}
	};

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		playerRecordEntityId = "1";
		setContentView(R.layout.teleplay_detail_layout);
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

	private void init() {
		// 初始化奇异的sdk
		// qiyiTVApi = QiyiTVApi.getInstance();
		// // 初始化 奇异的数据
		// qiyiTVApi.initApi(this, new IInitCallback() {
		// // 奇艺返回初始化状态的消息
		// public void onInitResult(boolean success) {
		// // 把初始化成功的变量传递出来，便于做判断
		// isInitPlayerRecordSuccess = success;
		// if (success) {
		// Log.d(TAG, "初始化成功");
		// } else {
		// Log.d(TAG, "初始化失败");
		// }
		// }
		// });
		// 创建数据库查询对象
		dao = new CollectVoideoNewDAO(getApplicationContext());

		// 初始化布局控件
		initView();
		// 网络请求的方法
		dataService = new HiveTVService();
		// 焦点所在的列表的位置
		focusList = new ArrayList<Integer>();
		// 传递过来的实体类
		Serializable serializable = getIntent().getSerializableExtra("entity");
		sourceID = getIntent().getStringExtra("source");
		// 开始现实loading
		dialogLoadingView.setVisibility(View.VISIBLE);
		// loading还没开始结束的时候，viewpager 暂时不需要显示，正在处理数据中
		mViewPager.setVisibility(View.INVISIBLE);
		groupPager.setVisibility(View.INVISIBLE);
		// 如果传递的数据不为空
		if (null != serializable) {
			Log.d(TAG, "serializable::");
			// 接收相关数据
			mVideoEntity = (FilmNewEntity) serializable;
			video_Entity = mVideoEntity;
			mVideoId = mVideoEntity.getId();
			// 分组数的依据
			videoset_total = mVideoEntity.getCurrCount() != 0 ? mVideoEntity.getCurrCount() : mVideoEntity.getTotal();
			mVideoType = mVideoEntity.getCid();
			REQUEST_VIDEOS_YEAR = mVideoEntity.getTime();
			// start:查询数据库,author:huzuwei
			fillFilmInfo();
			// end by huzuwei
			// 请求剧集的数据
			requestVideoList();
			// 根据电视剧的名称去搜索播放此电视剧的电视台
			// requestChannlList();

		} else {// 为空的情况下，必须要有这个id
			Log.d(TAG, "mVideoId::");
			mVideoId = getIntent().getIntExtra("id", 0);
			// mVideoId = 3963;
			if (mVideoId != 0) {// 如果这个id不为空可以去借口查询数据
				requestFilmDetail();
			} else {// 如果为空就表示这个数据是错误的，不应该存在的数据
				LogUtil.info("data error!!!!");
				finish();
			}
		}
	}

	/**
	 * 初始化View
	 */
	/**
	 * @Title: TeleplayDetailActivity
	 * @author:张鹏展
	 * @Description:
	 */
	private void initView() {
		moreLayout = (RelativeLayout) this.findViewById(R.id.teleplay_ll_3_more);
		mTitle = (TextView) this.findViewById(R.id.tv_title);
		mContent = (TextView) this.findViewById(R.id.tv_content);
		ivPlay = (ImageView) this.findViewById(R.id.iv_film_detail_play);
		mCover = (ImageView) this.findViewById(R.id.iv_film_detail_cover);
		mDescribeLine1 = (TextView) this.findViewById(R.id.tv_des_line1);
		mDescribeLine2 = (TextView) this.findViewById(R.id.tv_des_line2);
		ivCollect = (ImageView) findViewById(R.id.iv_film_detail_fav);
		ivCollect.setTag(false);
		// 制定播放按钮下按键的焦点处理
		ivPlay.setNextFocusDownId(R.id.teleplay_video_pagaer);
		// 显示电视剧剧集的viewpager
		mViewPager = (HivePreloadViewPager) findViewById(R.id.teleplay_video_pagaer);

		groupPager = (HivePreloadViewPager) findViewById(R.id.teleplay_group_pagaer);
		// groupPager.setOnPageChangeListener(new ViewpagerListener());
		// playerProgress = (ProgressBar) findViewById(R.id.progress_small);
		// playerProgress.setVisibility(View.VISIBLE);
		dialogLoadingView = (ProgressDialog) findViewById(R.id.iv_films_loading);

		ivLeft = (ImageView) findViewById(R.id.teleplay_tvs_iv_left);
		ivRight = (ImageView) findViewById(R.id.teleplay_tvs_iv_right);
		layoutTv1 = findViewById(R.id.lr_tv_1);
		layoutTv2 = findViewById(R.id.lr_tv_2);
		layoutTv3 = findViewById(R.id.lr_tv_3);
		layoutMore = findViewById(R.id.lr_tv_more);
		channelsPager = (ViewPager) findViewById(R.id.teleplay_pager_tvs);

		tvNoData = (TextView) findViewById(R.id.tv_no_data);

		vChannel1 = (LinearLayout) findViewById(R.id.lr_tv_1);
		vChannel2 = (LinearLayout) findViewById(R.id.lr_tv_2);
		vChannel3 = (LinearLayout) findViewById(R.id.lr_tv_3);

		vChannel1.setOnFocusChangeListener(channelViewFocusChangeListener);
		vChannel1.setOnClickListener(new ChannlOnClickListener());
		vChannel1.setTag(0);
		vChannel2.setOnFocusChangeListener(channelViewFocusChangeListener);
		vChannel2.setOnClickListener(new ChannlOnClickListener());
		vChannel2.setTag(1);
		vChannel3.setOnFocusChangeListener(channelViewFocusChangeListener);
		vChannel3.setOnClickListener(new ChannlOnClickListener());
		vChannel3.setTag(2);

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

		// 播放按钮的点击监听事件
		ivPlay.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				video_Entity = mVideoEntity;
				String json = com.alibaba.fastjson.JSONObject.toJSONString(video_Entity);
				Log.v(TAG, "JSONAnalyze====" + json);
				if (null != newVideos && newVideos.size() > 0) {// 如果获取的播放列表不为空
					// 如果为-1的话说明用户直接点击的播放按钮，焦点没有经过剧集列表
					Log.d(TAG, "----order::" + order);
					// if (order > 1) {// 不为空说明播放记录中查出了记录
					// Log.d(TAG, "爱奇艺播放");
					//
					// if (!StringUtils.isEmpty(name)) {
					// for (Media entitys : result) {
					// if (entitys.getName().equals(name)) {
					// Log.d(TAG, "entitys.getId()::" + entitys.getId());
					// Log.d(TAG, "entitys.getName()::" + entitys.getName());
					// Log.d(TAG, "entitys.getType()::" + entitys.getType());
					// HiveviewApplication.mQiyiClient.playMedia(entitys);
					// }
					//
					// }
					// } else {
					// Log.d(TAG, "播放");
					// VideoNewEntity video = null;
					// /* 判断当剧集了列表数据小与播放集数， */
					// if (order <= newVideos.size()) {
					// video = newVideos.get(order - 1);
					// } else {
					// video = newVideos.get(0);
					// }
					// // 播放的最新的名字数据
					// if (video.getVideoId() > 0 && video.getVideosetId() > 0)
					// {// 如果需要播放的关键数据不为空
					// String vrsAlbumId =
					// String.valueOf(video.getVideosetId());
					// String vrsTvId = String.valueOf(video.getVideoId());
					// //
					// QiYiPlayerUtil.startNewQiYiPlayer(TeleplayDetailActivity.this,
					// vrsTvId, vrsAlbumId);
					// QiYiPlayerUtil.startSDKPlayer(TeleplayDetailActivity.this,
					// JSONAnalyze.ObjectToJson(video_Entity), null, false,
					// true, false);
					// isOnClick = true;
					// startPlayerTime = System.currentTimeMillis();
					// handler.sendEmptyMessage(PLAYER_GO_ON);
					// } else {// 如果播放数据不正确的时候，提示错误
					// alertError();
					// }
					// }
					// } else {// 如果为空说明播放记录中没有这条，这样的话就是从获取的播放列表中提取第一个播放
					// Log.d(TAG, "爱奇艺播放记录为空");
					// if (videosIndex == -1)
					// videosIndex = 0;
					// // 需要播放的实体类
					// VideoNewEntity video = newVideos.get(videosIndex);
					// // 播放的最新的名字数据
					// if (video.getVideoId() > 0 && video.getVideosetId() > 0)
					// {// 如果需要播放的关键数据不为空
					// String vrsAlbumId =
					// String.valueOf(video.getVideosetId());
					// String vrsTvId = String.valueOf(video.getVideoId());
					// player_Entity = video;
					// player_video_Entity = video;
					// isOnClick = true;
					// startPlayerTime = System.currentTimeMillis();
					// QiYiPlayerUtil.startNewQiYiPlayer(TeleplayDetailActivity.this,
					// vrsTvId, vrsAlbumId);
					// handler.sendEmptyMessage(PLAYER_GO_ON);
					//
					// } else {// 如果播放数据不正确的时候，提示错误
					// alertError();
					// }

					//
					// }
				//	handler.sendEmptyMessage(PLAYER_GO_ON);
					isOnClick = true;
					QiYiPlayerUtil.startSDKPlayer(TeleplayDetailActivity.this, json, null, false, true, false, null, false);
					startPlayerTime = System.currentTimeMillis();
					long time = startPlayerTime - creatTime;
					try {
						sendStatistics(String.valueOf(time), player_Entity, DataType.CLICK_TAB_VIDEO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB).setViewPosition("0306").setSource(sourceID)
								.setEntity(player_Entity).setDataType(DataType.CLICK_TAB_VIDEO)
								.setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT) ? ItemType.SUBJECT : ItemType.VIDEO).build());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		ivCollect.setOnClickListener(new RecommendRecordLayoutListener());
		/*
		 * ivCollect.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { if (null != mVideoEntity) {
		 * 
		 * boolean isSaveed =
		 * Boolean.parseBoolean(ivCollect.getTag().toString()); //
		 * 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据 if (isSaveed) {
		 * dao.delete("videoset_id = ?", new String[] {
		 * String.valueOf(mVideoEntity.getVideoset_id()) });
		 * ivCollect.setImageResource(R.drawable.film_detail_fav);
		 * ivCollect.setTag(false); } else { dao.insert(mVideoEntity);
		 * ivCollect.setImageResource(R.drawable.film_detail_fav_has);
		 * ivCollect.setTag(true); } }
		 * 
		 * } });
		 */

		setNextFocus();

		initPopuWindow();
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
			if (null != mVideoEntity) {
				boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
				// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
				if (isSaveed) {
					dao.delete("id = ?", new String[] { String.valueOf(mVideoEntity.getId()) });
					ivCollect.setImageResource(R.drawable.film_detail_fav);
					ivCollect.setTag(false);
					return null;
				} else {
					dao.insert(mVideoEntity);
					ivCollect.setImageResource(R.drawable.film_detail_fav_has);
					ivCollect.setTag(true);
				}

			}
			// start:修改XX为Tab.TAB，author：huzuwei
			return new DataHolder.Builder(getBaseContext()).setDataType(DataType.CLICK_TAB_FILM).setTabNo(Tab.TAB).setViewPosition("0101")
					.setEntity(mVideoEntity).setSrcType(ItemType.VIDEO).build();
			// end
		}
	}

	/**
	 * 初始化电视剧海报浮层
	 */
	private void initPopuWindow() {
		popuView = getLayoutInflater().inflate(R.layout.teleplay_popuwindow_layout, null);
		popuBar = (SeekBar) popuView.findViewById(R.id.sb_television_item);
		popuChannelLogo = (ImageView) popuView.findViewById(R.id.iv_television_logo);
		popuVideoPoster = (ImageView) popuView.findViewById(R.id.iv_thumb);
		popuChannelName = (TextView) popuView.findViewById(R.id.tv_television_name);
		popuEndTime = (TextView) popuView.findViewById(R.id.tv_end_time);
		popuStartTime = (TextView) popuView.findViewById(R.id.tv_start_time);
		popuVideoName = (MarqueeText) popuView.findViewById(R.id.tv_program);
		popuVideoNextName = (MarqueeText) popuView.findViewById(R.id.tv_program_thumb);

		pWindow = new PopupWindow(popuView, 202, 222);
		pWindow.setBackgroundDrawable(new BitmapDrawable());

	}

	private String tvChannelLogoName;

	/**
	 * 设置某个电视台播放当前电视剧的海报，进度等信息
	 * 
	 * @param p
	 */
	private void setPopuWindowData(int p) {
		TVChannelEntity entity = channls.get(p);

		// 电视台logo
		popuChannelLogo.setImageResource(R.drawable.default_channel_logo);
		DisplayImageOptions logoOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_channel_logo)
				.showImageOnFail(R.drawable.default_channel_logo).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(entity.getChannel_logo(), popuChannelLogo, logoOptions);

		popuChannelName.setText(entity.getChannel_name());
		popuStartTime.setText(DateUtils.getTimeFromFullDate(entity.getStart_time()));
		popuEndTime.setText(DateUtils.getTimeFromFullDate(entity.getEnd_time()));

		// 节目海报
		popuVideoPoster.setImageResource(R.drawable.default_teleplay_live_poster);
		DisplayImageOptions posterOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_teleplay_live_poster)
				.showImageOnFail(R.drawable.default_teleplay_live_poster).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(entity.getWiki_cover(), popuVideoPoster, posterOptions);

		popuVideoName.setText(entity.getName());

		// 设置节目的播放进度
		DateUtils.setProgramPlayProgress(popuBar, entity.getStart_time(), entity.getEnd_time());

		if (entity.getNext_starttime() != null && entity.getNext_name() != null) {
			popuVideoNextName.setStart(true);
			popuVideoNextName.setText(entity.getNext_starttime() + "    " + entity.getNext_name());
		}
	}

	/**
	 * 设置焦点方向
	 */
	private void setNextFocus() {
		ivPlay.setNextFocusRightId(R.id.iv_film_detail_fav);
		// 没有直播信息的情况下，按钮焦点向右不移动
		ivCollect.setNextFocusRightId(layoutTv1.isShown() ? R.id.lr_tv_1 : ivCollect.getId());
		layoutTv1.setNextFocusRightId(R.id.lr_tv_2);
		layoutTv2.setNextFocusRightId(R.id.lr_tv_3);
		layoutTv3.setNextFocusRightId(R.id.lr_tv_more);

		ivPlay.setNextFocusLeftId(ivPlay.getId());
		ivCollect.setNextFocusLeftId(ivPlay.getId());
		ivCollect.setNextFocusDownId(R.id.teleplay_video_pagaer);
		layoutTv1.setNextFocusLeftId(ivCollect.getId());
		layoutTv2.setNextFocusLeftId(layoutTv1.getId());
		layoutTv3.setNextFocusLeftId(layoutTv2.getId());

	}

	/**
	 * log 标签
	 * 
	 * @Fields TAG:TODO
	 */

	private String TAG = "TeleplayDetailActivity";

	/**
	 * 显示电视剧详情信息
	 */
	private void fillFilmInfo() {

		try {
			ArrayList<FilmNewEntity> filmEntities = dao.query(null, "id = ?", new String[] { null == String.valueOf(mVideoEntity.getId()) ? ""
					: String.valueOf(mVideoEntity.getId()) }, null);

			Log.d(TAG, "======filmEntities=" + filmEntities.size());
			if (filmEntities.size() != 0)
				handler.sendEmptyMessage(QUREY_SUCRESS);
		} catch (Exception e) {

		}
		// 当前详情页的entity
		video_Entity = mVideoEntity;
		StringBuilder describeLine1 = new StringBuilder();
		StringBuilder describeLine2 = new StringBuilder();

		// 显示电视剧的时长、上映时间、分类信息
		describeLine1.append(0 == mVideoEntity.getEqLen() ? "" : getResources().getString(R.string.text_time_long))
				.append(0 == mVideoEntity.getEqLen() ? "" : mVideoEntity.getEqLen() / 60)
				.append(0 == mVideoEntity.getEqLen() ? "" : getResources().getString(R.string.text_minute))
				.append(!StringUtils.isEmpty(mVideoEntity.getTime()) ? getResources().getString(R.string.text_film_detail_separater) : "")
				.append(!StringUtils.isEmpty(mVideoEntity.getTime()) ? getResources().getString(R.string.text_issue_time) : "")

				.append(StringUtils.isEmpty(mVideoEntity.getTime()) ? "" : mVideoEntity.getTime())

				.append(/*
						 * getResources() .getString(R.string.text_year)
						 */"")
				.append(!StringUtils.isEmpty(mVideoEntity.getTagNames()) ? getResources().getString(R.string.text_film_detail_separater) : "")
				.append(!StringUtils.isEmpty(mVideoEntity.getTagNames()) ? getResources().getString(R.string.text_category) : "")

				.append(!StringUtils.isEmpty(mVideoEntity.getTagNames()) ? mVideoEntity.getTagNames() : "");
		Log.d(TAG, "=mVideoEntity.getActors():" + mVideoEntity.getMainActors());
		// 显示电视剧的导演，主演信息
		describeLine2

				.append(null != mVideoEntity.getDirectors() && !mVideoEntity.getDirectors().equals("") ? getResources().getString(
						R.string.text_director) : "")

				.append(mVideoEntity.getDirectors())

				.append(null != mVideoEntity.getMainActors() && !mVideoEntity.getMainActors().equals("") ? getResources().getString(
						R.string.text_film_detail_separater) : "")
				.append(null != mVideoEntity.getMainActors() && !mVideoEntity.getMainActors().equals("") ? getResources().getString(
						R.string.text_actor) : "")

				.append(mVideoEntity.getMainActors());

		describeLine1.toString().trim().replace("null", "");
		describeLine2.toString().trim().replace("null", "");

		mDescribeLine1.setText(StringUtils.isEmpty(describeLine1.toString().trim()) ? "" : describeLine1);// 时长，上映时间，分类信息
		mDescribeLine2.setText(StringUtils.isEmpty(describeLine2.toString().trim()) ? "" : describeLine2);// 显示导演，主演信息

		mTitle.setText(StringUtils.isEmpty(mVideoEntity.getName()) || "null".equals(mVideoEntity.getName()) ? "" : mVideoEntity.getName());// 电视剧名称
		mContent.setText(StringUtils.isEmpty(mVideoEntity.getDesc()) || "null".equals(mVideoEntity.getDesc()) ? "" : mVideoEntity.getDesc());// 电视剧详情
		name = mVideoEntity.getName();
		Log.d(TAG, "======describeLine1.toString().trim()=" + describeLine1.toString().trim());
		Log.d(TAG, "======describeLine2.toString().trim()=" + describeLine2.toString().trim());
		Log.d(TAG, "======mVideoEntity.getVideoset_name()=" + mVideoEntity.getName());
		Log.d(TAG, "======mVideoEntity.getVideoset_brief()=" + mVideoEntity.getDesc());
		Log.d(TAG, "======mVideoEntity.getVideoset_tv_img()=" + mVideoEntity.getPosterUrl());
		if (null != mVideoEntity.getPosterUrl() && mVideoEntity.getPosterUrl().length() > 0 && !"null".equals(mVideoEntity.getPosterUrl())) {
			String sizeNewUrl = StringUtils.getImage260_360Url(mVideoEntity.getPosterUrl());

			DisplayImageOptions coverOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_film_detail_big)
					.showImageOnFail(R.drawable.default_film_detail_big).resetViewBeforeLoading(false).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
			ImageLoader.getInstance().displayImage(sizeNewUrl, mCover, coverOptions);
		}

	}

	/**
	 * 处理网络返回的电视剧数据，包括剧集信息，电视台信息等
	 */

	protected void processData(int msgWhat) {
		switch (msgWhat) {
		// 获取电视剧的剧集成功
		case REQUEST_VIDEOS_SUCCESS:
			try {

				fillFilmInfo();
				// 如果适配器为空的时候初始化适配器
				if (null == adapter) {
					adapter = new HivePagerAdapter(this, new TeleplayVideosPagerViewFactory(new ViewItemFocusListener(), new ViewItemKeyListener(),
							new ViewItemClickListener(), new ViewItemGetFocus()), mViewPager, 20, new NeighborTwoLineStrategy());
				}
				// 隐藏loading和显示viewpager相关
				// playerProgress.setVisibility(View.GONE);
				dialogLoadingView.setVisibility(View.INVISIBLE);
				tvNoData.setVisibility(View.INVISIBLE);
				mViewPager.setVisibility(View.VISIBLE);
				groupPager.setVisibility(View.VISIBLE);
				// 添加数据信息
				adapter.addDataSource(newVideos);
				adapter.setDataTotalSize(videoset_total);
				Log.d(TAG, "newVideos.size()==>size::" + newVideos.size());
				// playerRecordEntityId = String.valueOf(order);
				Log.i(TAG, "playerRecordEntityId----::" + playerRecordEntityId);
				mViewPager.setAdapter(adapter);
				mViewPager.setCallBackListener(new CallbackPageChangeListener() {

					public void onPageSelected(int arg0) {
						Log.d(TAG, "onPageSelected=>arg0::" + arg0);

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
							break;
						case 1:

							break;
						case 2:
							// 动画执行开始
							isViewPagerAnimactionOver = false;
							break;
						}

					}
				});
				// 添加viewpager 的监听方法
				mViewPager.setPreloadingListener(new OnPreloadingListener() {

					public void setPageCurrent(int pageIndex) {
						// pageIndex 是从1开始，viewItemPages 是需要从0开始的
						viewItemPages = pageIndex - 1;
						handler.sendEmptyMessage(ITEMVIEWPAGESINDEX);
						// 改变背景
						changeVideoPagerBackgroud(pageIndex - 1 >= 0 ? pageIndex - 1 : 0);
						int pageCount = (int) Math.ceil(videoset_total / (double) 20);
						if (pageIndex >= pageCount)// 最后一页
							isLastPager = true;
						else
							isLastPager = false;

						Log.d(TAG, "allPages::" + pageCount);
					}

					/*
					 * 欲加载 (non-Javadoc)
					 * 
					 * @see
					 * com.hiveview.box.framework.view.HivePreloadViewPager.
					 * OnPreloadingListener#preLoading(int)
					 */
					public void preLoading(int pageSize) {
						// TODO Auto-generated method stub
						REQUEST_VIDEOS_PAGE_NUM++;
						requestVideoList();
					}

					public void preLoadNotFinish() {
						// TODO Auto-generated method stub

					}

					public void onLastPage() {
						// TODO Auto-generated method stub

					}

					public void onFirstPage() {
						// TODO Auto-generated method stub
						viewItemPages = 0;
					}
				});

				// notifyPageView();
				notifyPageView(videoset_total);
				if (null == groupAdapter) {
					groupAdapter = new HivePagerAdapter(this, new TeleplayGroupsPagerViewFactory(new ViewGroupItemFocusListener(),
							new ViewGroupItemKeyListener(), new ViewGroupItemClickListener()), groupPager, 5, new NeighborOneLineStrategy());
				}
				groupAdapter.addDataSource(listGroupText);
				groupPager.setAdapter(groupAdapter);
				groupPager.setPreloadingListener(new OnPreloadingListener() {

					@Override
					public void setPageCurrent(int pageIndex) {
						// TODO Auto-generated method stub
						int pageCount = (int) Math.ceil(listGroupText.size() / (double) 5);
						if (pageIndex >= pageCount)// 最后一页
							isVideoGroupLastPage = true;
						else
							isVideoGroupLastPage = false;
					}

					@Override
					public void preLoading(int pageSize) {
						/*
						 * REQUEST_VIDEOS_PAGE_NUM++; requestVideoList();
						 */

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
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case REQUEST_VIDEO_ADD_RESOURCE:// 电视剧剧集大于100的时候，需要多次请求数据
			// REQUEST_VIDEOS_PAGE_NUM++;
			tvNoData.setVisibility(View.INVISIBLE);
			adapter.addDataSource(rquestVideos);
			// notifyPageView(videoset_total);
			// groupAdapter.addDataSource(listGroupText);
			break;
		case REQUEST_VIDEOS_FAIL:// 没有数据时显示暂无相关数据
			// playerProgress.setVisibility(View.GONE);
			// videoPager.setLoading(false);
			dialogLoadingView.setVisibility(View.INVISIBLE);
			break;
		case REQUEST_CHANNELS_SUCCESS:// 展示直播的信息

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
			}
			// 没有直播信息的情况下，按钮焦点向右不移动
			ivCollect.setNextFocusRightId(layoutTv1.isShown() ? R.id.lr_tv_1 : ivCollect.getId());
			break;
		case REQUEST_CHANNELS_FAIL:
			break;
		case REQUEST_NEXT_SUCCESS:// 有直播信息显示相关浮层
			setNextProgram();
			break;
		case REQUEST_NEXT_FAIL:
			break;
		case REQUEST_VIDEO_DETAIL_SUCCESS:// 获取详情数据成功
			// playerProgress.setVisibility(View.GONE);
			dialogLoadingView.setVisibility(View.INVISIBLE);
			fillFilmInfo();

			requestVideoList();
			// requestChannlList();
			break;
		case REQUEST_VIDEO_DETAIL_FAIL:// 获取详情数据失败
			// showDialogAboutNetFault();
			dialogLoadingView.setVisibility(View.INVISIBLE);
			// playerProgress.setVisibility(View.GONE);
			break;
		case QUREY_SUCRESS:
			// 如果存在收藏的列表中 就显示已收藏
			ivCollect.setImageResource(R.drawable.film_detail_fav_has);
			ivCollect.setTag(true);
			break;
		case PLAYER_GO_ON:// 改变播放按钮的背景
			if (null != adapter) {
				try {
					// 循环清除所有页面高亮状态
					for (int i = 0; i < adapter.getViews().size(); i++) {
						((TeleplayVideosPageView) adapter.getViews().get(i)).setClearColor();
					}
					// 当前view不为空时设置高亮状态

					if (((TeleplayVideosPageView) adapter.getViews().get((Integer.parseInt(playerRecordEntityId) - 1) / 20)) != null) {
						((TeleplayVideosPageView) adapter.getViews().get((Integer.parseInt(playerRecordEntityId) - 1) / 20))
								.setBtnRequsetFocus((Integer.parseInt(playerRecordEntityId) - 1) % 20);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			ivPlay.setImageResource(R.drawable.player_go);
			// playerProgress.setVisibility(View.GONE);
			break;
		// 剧集翻页的时候发出的消息
		case ITEMVIEWPAGESINDEX:
			// 改变btn文字颜色
			ViewGroupItemChange((viewItemPages / 5), viewItemPages);
			break;
		// 观看完电视之后返回刷新view
		case REFUSE_VIEW:
			if (null != adapter) {
				int pages = (int) (Math.ceil(videoOrder / (double) 20)) - 1;
				int items = (videoOrder % 20) == 0 ? 19 : (videoOrder % 20) - 1;
				TeleplayVideosPageView tvpv = (TeleplayVideosPageView) adapter.getViews().get(pages);
				for (int i = 0; i < adapter.getViews().size(); i++) {

					TeleplayVideosPageView tvpv1 = (TeleplayVideosPageView) adapter.getViews().get(i);
					// 本页就跳过
					/*
					 * if(i == pages) continue;
					 */
					// 不然就清除颜色
					if (null != tvpv1)
						tvpv1.setClearColor();
				}
				// 确定要跳转的page 页码
				if (null != mViewPager && null != tvpv) {
					mViewPager.setCurrentItem(pages);
					tvpv.setBtnRequsetFocus(items);
				}

				Log.d(TAG, "REFUSE_VIEW==>videoOrder::" + videoOrder + "==pages::" + pages + "==items::" + items);
			}
			break;
		case PLAYER_DETAIL:
			if (null != adapter) {
				try {
					// 循环清除所有页面高亮状态
					for (int i = 0; i < adapter.getViews().size(); i++) {
						((TeleplayVideosPageView) adapter.getViews().get(i)).setClearColor();
					}
					// 当前view不为空时设置高亮状态

					if (((TeleplayVideosPageView) adapter.getViews().get((Integer.parseInt(playerRecordEntityId) - 1) / 20)) != null) {
						((TeleplayVideosPageView) adapter.getViews().get((Integer.parseInt(playerRecordEntityId) - 1) / 20))
								.setBtnRequsetFocus((Integer.parseInt(playerRecordEntityId) - 1) % 20);
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			ivPlay.setImageResource(R.drawable.film_detail_play);
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
				 * TeleplayDetailActivity.this, mVideoId); if (null != list2 &&
				 * list2.size() > 0) { FilmEntity mVideoEntity2 = list2.get(0);
				 */
				// 得到详情的数据
				List<FilmNewEntity> list = dataService.getFilmDetail(TeleplayDetailActivity.this, mVideoId);
				if (null != list && list.size() > 0) {// 如果数据不为空就表示获取数据成功
					mVideoEntity = list.get(0);

					video_Entity = mVideoEntity;
					mVideoType = mVideoEntity.getCid();
					name = mVideoEntity.getName();
					Log.d(TAG, "-----name:" + name);
					videoset_total = mVideoEntity.getCurrCount() == 0 ? mVideoEntity.getTotal() : mVideoEntity.getCurrCount();// 总集数

					Log.d(TAG, "======mVideoEntity.getVideoset_name()::" + mVideoEntity.getName());
					Log.d(TAG, "======mVideoEntity.getVideoset_brief()::" + mVideoEntity.getDesc());
					Log.d(TAG, "======mVideoEntity.getVideoset_tv_img()::" + mVideoEntity.getPosterUrl());
					handler.sendEmptyMessage(REQUEST_VIDEO_DETAIL_SUCCESS);
				} else {// 如果获取数据量为空就是表示获取失败
					handler.sendEmptyMessage(REQUEST_VIDEO_DETAIL_FAIL);
				}
				/*
				 * } else { handler.sendEmptyMessage(REQUEST_VIDEO_DETAIL_FAIL);
				 * }
				 */
			}

			public void processServiceException(ServiceException e) {// 获取数据的网络异常
				handler.sendEmptyMessage(REQUEST_VIDEO_DETAIL_FAIL);
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), true);
			}
		});

	}

	/**
	 * 得到电视剧剧集
	 */
	private void requestVideoList() {
		submitRequest(new SafeRunnable() {

			public void requestData() {
				Log.d(TAG, "------剧集");
				rquestVideos = dataService.getVideoList(TeleplayDetailActivity.this, mVideoId, mVideoType, "", REQUEST_VIDEOS_PAGE_SIZE,
						REQUEST_VIDEOS_PAGE_NUM, REQUEST_VIDEOS_YEAR);
				if (rquestVideos != null && rquestVideos.size() != 0) {
					Log.i(TAG, "不为空====rquestVideos:" + rquestVideos);
					/* 删除重复数据start by huzuwei */
					// for (int i = 0; i < rquestVideos.size(); i++) {
					// for (int j = rquestVideos.size() - 1; j > i; j--) {
					// if (rquestVideos.get(i).getSequence() == rquestVideos
					// .get(j).getSequence()) {
					// rquestVideos.remove(j);
					// }
					// }
					// }
					/* end by huzuwei */
					if (REQUEST_VIDEOS_PAGE_NUM <= 1) {
						// if (!HiveviewApplication.mQiyiClient.isAuthSuccess())
						// {
						// getAuthSuccess();
						// } else {
						// getQiYi();
						// }
						// 如果是第一次查询数据的时候发送的数据请求成功的初始化相关信息
						newVideos.addAll(rquestVideos);
						handler.sendEmptyMessage(REQUEST_VIDEOS_SUCCESS);
					} else {// 如果不是第一个请求数据的时候，发送添加数据的消息
						newVideos.addAll(rquestVideos);
						handler.sendEmptyMessage(REQUEST_VIDEO_ADD_RESOURCE);
					}
				} else {// 没有数据时显示暂无相关数据
					newVideos.addAll(rquestVideos);
					// handler.sendEmptyMessage(REQUEST_VIDEOS_FAIL);
				}
			}

			public void processServiceException(ServiceException e) {
				handler.sendEmptyMessage(REQUEST_VIDEOS_FAIL);
				// 显示错误提示框
				showErrorDialog(e.getErrorCode(), false);
			}
		});
	}

	/**
	 * 得到播放当前电视剧的电视台
	 */
	private void requestChannlList() {
		submitRequest(new SafeRunnable() {

			public void requestData() {

				SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.CHINA);
				Date date2 = new Date(System.currentTimeMillis() + (60 * 1000));
				String dateString = time.format(date2);
				channls = dataService.getTvChannelByProgram(TeleplayDetailActivity.this, StringUtils.isEmpty(mVideoEntity.getName()) ? ""
						: mVideoEntity.getName(), dateString);
				Log.d(TAG, "end_time:" + dateString);
				handler.sendEmptyMessage(REQUEST_CHANNELS_SUCCESS);
			}

			public void processServiceException(ServiceException e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * 得到播放当前电视剧的电视台的下一个节目
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

	OnClickListener videoViewOnClickListener = new OnClickListener() {

		public void onClick(View v) {
			if (null != videos && videos.size() != 0) {
				int position = (Integer) v.getTag();
				VideoNewEntity video = videos.get(position);
				// if (video.getCp_videoset_id().length() > 0
				// && video.getCp_video_id().length() > 0) {
				// String vrsAlbumId = video.getCp_videoset_id().split(",")[1];
				// String vrsTvId = video.getCp_video_id().split(",")[1];
				// QiYiPlayerUtil.startQiYiPlayer(TeleplayDetailActivity.this,
				// vrsTvId, vrsAlbumId);
				// try{
				// KeyEventHandler.post(new
				// DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB)
				// .setViewPosition("0306").setSource(sourceID)
				// .setEntity(video).setDataType(DataType.CLICK_TAB_VIDEO)
				// .setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT)?ItemType.SUBJECT:ItemType.VIDEO)
				// .build());
				// }catch(Exception e){

				// }
				// }
			}
		}
	};

	/**
	 * 根据得到电视剧剧集数据，通知数据变化、进行分页、更新UI
	 * 
	 * @Title: TeleplayDetailActivity
	 * @author:张鹏展
	 * @Description:
	 * @param org0
	 */
	public void notifyPageView(int org0) {
		int newCount = org0;
		listGroupText = new ArrayList<VideoNewEntity>();
		int pageCount = (int) Math.ceil((newCount) / VIDEO_PAGE_SIZE) + 1;// 计算新增的页数
		Log.d(TAG, "notifyPageView=>pageCount::" + pageCount);
		for (int i = 1; i < pageCount; i++) {
			VideoNewEntity entity = new VideoNewEntity();
			String groupText = "";// 设置电视剧分组显示文本，如1-20
			if (i * 20 < newCount) {
				groupText = (i * 20 - 20 + 1) + "-" + (i * 20);
			} else {
				groupText = (i * 20 - 20 + 1) + "-" + newCount;
			}
			entity.setEpname(groupText);
			// entity.setVideo_type(2);
			listGroupText.add(entity);
		}
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
	 * 设置电视台浮层上下一个节目信息
	 */
	private void setNextProgram() {
		if (channls.size() == nextps.size()) {
			for (int i = 0; i < channls.size(); i++) {
				ProgramByCodeEntity nextEntity = nextps.get(i);
				TVChannelEntity channelEntity = channls.get(i);
				if (nextEntity.getChannel_code().equals(channelEntity.getChannel_code())) {
					channelEntity.setNext_name(nextEntity.getNext_name());
					channelEntity.setNext_starttime(nextEntity.getNext_start_time());
				}
			}
		}
	}

	/**
	 * 填充电视台的ViewPager
	 */
	private void fillChannlsViewPager() {
		channelViews = new ArrayList<View>();
		double channlCount = channls.size();
		int pageCount = (int) Math.ceil(channlCount / 5);
		int position = 3;// 前3个是静态的，在vChanenl123中已经指定，所以这里从3位置开始显示在下方的ViewPager中
		for (int i = 0; i < pageCount; i++) {
			TeleplayLiveTVLogoView tv = new TeleplayLiveTVLogoView(this);
			tv.setNextUpFocus(R.id.lr_tv_more);
			for (int j = 0; j < CHANNEL_GROUP_PAGE_SIZE; j++) {
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
					ivLeft.setVisibility(View.INVISIBLE);
				} else if (arg0 == channelViews.size() - 1) {
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
	 * 显示指定位置电视台显示
	 * 
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

	/*
	 * private void initVideosViewPager() { videos = new
	 * ArrayList<VideoEntity>(); listGroupText = new ArrayList<VideoEntity>();
	 * videoViewsList = new ArrayList<View>(); adapterVideos = new
	 * FilmPagerAdapter(videoViewsList); // 填充剧集分组的ViewPager groupViews = new
	 * ArrayList<View>(); adapterGroup = new FilmPagerAdapter(groupViews);
	 * 
	 * // videoPager.setAdapter(adapterVideos);// ViewPager填充 //
	 * groupPager.setAdapter(adapterGroup); // videoPager.setPreListener(this);
	 * 
	 * ivPlay.setOnKeyListener(goVideoPageViewKeyListener);
	 * ivCollect.setOnKeyListener(goVideoPageViewKeyListener);
	 * layoutTv1.setOnKeyListener(goVideoPageViewKeyListener);
	 * layoutTv2.setOnKeyListener(goVideoPageViewKeyListener);
	 * layoutTv3.setOnKeyListener(goVideoPageViewKeyListener); //
	 * layoutMore.setOnKeyListener(goVideoPageViewKeyListener);
	 * layoutMore.setNextFocusDownId(R.id.teleplay_pager_tvs);
	 * 
	 * }
	 */

	/**
	 * 当电视剧页面变化时，下面的剧集分组按钮要有选中效果
	 */
	private void setGroupButtonTextColor() {
		int pageNum = (int) (VIDEOS_CURRENT_PAGE_NUM / VIDEO_GROUP_PAGE_SIZE);
		if (groupViews.size() > 0) {
			TeleplayVideoGroupView group = (TeleplayVideoGroupView) groupViews.get(pageNum);
			groupPager.setCurrentItem(pageNum);
			group.setSpecialButtonTextColor(VIDEOS_CURRENT_PAGE_NUM);
		}
	}

	/**
	 * 根据切换的页面，改变videoPager的背景
	 * 
	 * @param position
	 */
	private void changeVideoPagerBackgroud(int position) {
		position = position % 5;
		if (position == 0) {
			mViewPager.setBackgroundResource(R.drawable.teleplay_video_bg0);
		} else if (position == 1) {
			mViewPager.setBackgroundResource(R.drawable.teleplay_video_bg1);
		} else if (position == 2) {
			mViewPager.setBackgroundResource(R.drawable.teleplay_video_bg2);
		} else if (position == 3) {
			mViewPager.setBackgroundResource(R.drawable.teleplay_video_bg3);
		} else if (position == 4) {
			mViewPager.setBackgroundResource(R.drawable.teleplay_video_bg4);
		}

	}

	/**
	 * 预加载数据方法
	 */

	public void preLoading() {
		requestVideoList();
	}

	/**
	 * 
	 */

	public void onPrePageSelected(int position) {
		changeVideoPagerBackgroud(position);
		VIDEOS_CURRENT_PAGE_NUM = position;
		setGroupButtonTextColor();
	}

	public class getQIYIPlayerRecords extends SafeRunnable {

		@Override
		public void requestData() {

			String videoId = String.valueOf(mVideoId);
			List<PlayerRecordEntity> QiyiPlayer = new ArrayList<PlayerRecordEntity>();
			QiyiPlayer = QIYIRecordUtils.getHistoryList(TeleplayDetailActivity.this, videoId);
			if (QiyiPlayer != null && QiyiPlayer.size() != 0) {
				for (PlayerRecordEntity entity : QiyiPlayer) {
					PlayerRecordEntity entitys = entity;
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getName()::" + entitys.getName());
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getStartTime()::" + entitys.getStartTime());
					Log.d(TAG, "getQIYIPlayerRecords----entitys.getAlbums()::" + entitys.getAlbums());
					Log.d(TAG, "getQIYIPlayerRecords----entitys::" + entitys.toString());
					Log.d(TAG, "getQIYIPlayerRecords----name::" + name);

					if (entitys.getCurrentEpisode().equals("-1")) {
						playerRecordEntityId = "1";
						handler.sendEmptyMessage(PLAYER_DETAIL);
					} else {
						playerRecordEntityId = entitys.getAlbums();
						handler.sendEmptyMessage(PLAYER_GO_ON);
					}
				}

			} else {
				playerRecordEntityId = "1";
				handler.sendEmptyMessage(PLAYER_DETAIL);
			}

		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub

		}

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.tv.activity.BaseActivity#onResume()
	 */
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "----onResume");
		super.onResume();
		ivPlay.requestFocus();
		// 清除第一个view 的颜色
		submitRequest(new getQIYIPlayerRecords());
		// if (!isFirstCreat) {
		// Log.d(TAG, "Resume---::" + (!isFirstCreat));
		// playerRecordEntityId = "";
		// // 清除第一个view 的颜色
		// if (null != adapter)
		// ((TeleplayVideosPageView)
		// adapter.getViews().get(0)).clearFirstBtnColor();
		// // submitRequest(new GetPlayerRecord());
		// submitRequest(new getQIYIPlayerRecords());
		// // requestVideoList();
		// }
		isFirstCreat = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getContentResolver().unregisterContentObserver(cob);
	}

	class ChannlOnClickListener implements OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.lr_tv_1:
				SwitchChannelUtils.switchChannel(getBaseContext(), popuChannelName.getText().toString(), false, AppScene.getScene());
				break;
			case R.id.lr_tv_2:
				SwitchChannelUtils.switchChannel(getBaseContext(), popuChannelName.getText().toString(), false, AppScene.getScene());
				break;
			case R.id.lr_tv_3:
				SwitchChannelUtils.switchChannel(getBaseContext(), popuChannelName.getText().toString(), false, AppScene.getScene());
				break;
			}
		}

	}

	/**
	 * 
	 * 
	 * 这个方法无效了已经
	 * 
	 * 
	 */
	class ViewItemFocusListener implements CallBackItemViewFocusListener {

		public void onCallBackFocusChange(View view, boolean has) {

		}

	}

	/**
	 * view 身上的按键事件
	 * 
	 * @ClassName: ViewItemKeyListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月17日 下午3:20:18
	 * 
	 */
	class ViewItemKeyListener implements CallBackItemViewKeyListener {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (newVideos.get(newVideos.size() - 1).equals(videoBtn.getTag())) {
					return true;
				}
			}
			Log.d(TAG, "ViewItemKeyListener::::");
			// 当前的view身上的按键事件
			if (viewItemViewIndex != -1 && keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (viewItemViewIndex) {
				case 0:
					if (isLastPager && isOneLine) {
						// 计算在下按键的时候焦点的走向
						HivePagerAdapter adapter = (HivePagerAdapter) mViewPager.getAdapter();
						int allItem = adapter.getDataSource().size();
						int theLastOne = (int) (allItem % VIDEO_PAGE_SIZE != 0 ? (allItem % VIDEO_PAGE_SIZE) - 1 : VIDEO_PAGE_SIZE - 1);
						int lastPage = (int) Math.ceil(allItem / (double) VIDEO_PAGE_SIZE) - 1;

						if (theLastOne < VIDEO_PAGE_SIZE / 2) {
							Log.i(TAG, "========11111");
							// 不为空就回到上一次获取焦点的组view 上去
							if (null != btnGetFocus) {
								Log.i(TAG, "=====this is not null!!");
								btnGetFocus.requestFocus();
							} else {
								Log.i(TAG, "=====this is  null!!");
								// 如果为空就代表这第一次走到view 就应该走到第一个分组的view上去
								groupPager.requestFocus();
							}
						} else {
							Log.i(TAG, "========2222");
							Log.d(TAG, "allItem::" + allItem);
							Log.d(TAG, "theLastOne::" + theLastOne);
							Log.d(TAG, "lastPage::" + lastPage);
							/* 处理最后页中，焦点问题 start by huzuwei */
							TeleplayVideosPageView tvpv = (TeleplayVideosPageView) adapter.getViews().get(lastPage);
							int theLastTwo = (int) (allItem % VIDEO_PAGE_SIZE);
							int theLastThree = theLastTwo / 10;
							int theLastFour = theLastTwo % 10;
							VideoNewEntity entity = (VideoNewEntity) videoBtn.getTag();
							if (theLastTwo <= 10 && theLastTwo >= 1) {// 最后一页时，单行，下
																		// ，焦点落到分组焦点上
								btnGetFocus.requestFocus();
							} else if (theLastTwo >= 11 && theLastTwo < 20) {// 两行时，且不为20数据时
								Log.i(TAG, "=======tvpv.getTag(index):" + tvpv.getTag() + "::::" + tvpv.getTag(theLastThree)
										+ "==videoBtn.getTag(index):" + videoBtn.getTag() + "::::" + videoBtn.getTag(theLastThree));
								if ((entity.getEporder() - 20 * lastPage) >= theLastFour) {// 第二行中,第一行数据焦点在第二行没有对应()，直接跳到第二行最后一集
									if (tvpv.getBtnView(entity.getEporder() - 20 * lastPage - 1).requestFocus()) {
										Log.i(TAG, "====tvpv.getBtnView().requestFocus()" + true);
										tvpv.getBtn(theLastOne);
									}
								} else {// 第一行与第 二行数据有对应时，焦点对应往下
									if (tvpv.getBtnView(entity.getEporder() - 20 * lastPage - 1).requestFocus()) {
										Log.i(TAG, "====tvpv.getBtnView().requestFocus()+++" + true);
										tvpv.getBtn(entity.getEporder() - 20 * lastPage - 1 + 10);
									}
								}
							} else {// 最后页，20个数据，焦点对应的向下
								if (tvpv.getBtnView(entity.getEporder() - 20 * lastPage - 1).requestFocus()) {
									Log.i(TAG, "====tvpv.getBtnView().requestFocus()+++II" + true);
									tvpv.getBtn(entity.getEporder() - 20 * lastPage - 1 + 10);
								}
							}

							/* end by huzuwei */

							// TeleplayVideosPageView tvpv =
							// (TeleplayVideosPageView)
							// adapter.getViews().get(lastPage);
							// if (null != tvpv)
							// tvpv.getBtn(theLastOne);
							return true;
						}
					}
					/*
					 * if (isLastPager && isOneLine) {
					 * 
					 * // 不为空就回到上一次获取焦点的组view 上去 if (null != btnGetFocus)
					 * btnGetFocus.requestFocus(); else // 如果为空就代表这第一次走到view
					 * 就应该走到第一个分组的view上去 groupPager.requestFocus(); }
					 */
					break;
				case 1:
					// 不为空就回到上一次获取焦点的组view 上去
					if (null != btnGetFocus) {
						Log.i(TAG, "========3333");
						btnGetFocus.requestFocus();
					} else {
						Log.i(TAG, "========4444");
						// 如果为空就代表这第一次走到view 就应该走到第一个分组的view上去
						groupPager.requestFocus();
					}
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
			video_Entity = mVideoEntity;
			String json = com.alibaba.fastjson.JSONObject.toJSONString(video_Entity);
			Log.v(TAG, "JSONAnalyze1====" + json);
			try {
				VideoNewEntity video = (VideoNewEntity) v.getTag();
				videosIndex = newVideos.indexOf(video);
				// 不出现数组out of index
				if (videosIndex >= newVideos.size())
					videosIndex = 0;

				Log.d(TAG, "ViewItemClickListener==>videosIndex::" + videosIndex);
				if (video.getVideoId() > 0 && video.getVideosetId() > 0) {
					isOnClick = true;
					startPlayerTime = System.currentTimeMillis();
					// String vrsAlbumId =
					// String.valueOf(video.getVideosetId());
					// String vrsTvId = String.valueOf(video.getVideoId());
					// QiYiPlayerUtil.startNewQiYiPlayer(TeleplayDetailActivity.this,
					// vrsTvId, vrsAlbumId);
					// startPlayerTime = System.currentTimeMillis();
					String jsonVideoString = com.alibaba.fastjson.JSONObject.toJSONString(video);
					QiYiPlayerUtil.startSDKPlayer(TeleplayDetailActivity.this, json, jsonVideoString, true, false, false, null, false);
					//handler.sendEmptyMessage(PLAYER_GO_ON);
					long time = startPlayerTime - creatTime;
					sendStatistics(String.valueOf(time), video, DataType.CLICK_TAB_VIDEO);

					KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB).setViewPosition("0306").setSource(sourceID)
							.setEntity(video).setDataType(DataType.CLICK_TAB_VIDEO)
							.setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT) ? ItemType.SUBJECT : ItemType.VIDEO).build());

				}
			} catch (Exception e) {

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
	Button videoBtn = null;

	class ViewItemGetFocus implements OnBtnGetFocus {

		@Override
		public void btnGetFocus(View v, int viewIndex, boolean has) {
			// TODO Auto-generated method stub
			if (has) {
				videoBtn = (Button) v;
				// 计算当前view 是第几行的view
				viewItemViewIndex = viewIndex / 10;
				if (isLastPager) {// 如果是最后一页就算出剩余单行的个数
					int items = newVideos.size();
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
			}
		}

	}

	/**
	 * @ClassName: ViewGroupItemFocusListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月11日 上午9:23:21 分组按钮上边的item的焦点监听
	 * 
	 */
	class ViewGroupItemFocusListener implements CallBackItemViewFocusListener {

		public void onCallBackFocusChange(View view, boolean has) {
			if (has) {
				if (null != btnGetFocus)// 只要不为空就代表这是上一个要还在焦点状态的btn
					btnGetFocus.setTextColor(getResources().getColor(R.color.white));
				// 这个新的btn是要获取焦点的btn
				btnGetFocus = (Button) view;
				// 为这个要获取焦点的btn 改变他的文字背景颜色
				btnGetFocus.setTextColor(getResources().getColor(R.color.yellow));
				// 获取btn上边要显示的文字
				String btn_text = btnGetFocus.getText().toString();
				// 对获取的String 操作分割
				btn_text = btn_text.split("-")[0];
				// 获取到的是相关的分组信息
				int btn_value = Integer.parseInt(btn_text) / 20;
				// 确定要跳转的page 页码
				mViewPager.setCurrentItem(btn_value);
			}
		}
	}

	/**
	 * 
	 * 年份分组的组监听事件 焦点落在组的item上边key监听
	 */
	class ViewGroupItemKeyListener implements CallBackItemViewKeyListener {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (listGroupText.get(listGroupText.size() - 1).equals(btnGetFocus.getTag())) {
					return true;
				}
			}
			// 分集的动画没有执行完成的时候屏蔽组的按键移动
			// ，不然的话，分集的viewpager不会连续移动的
			return !isViewPagerAnimactionOver;
		}
	}

	/**
	 * @ClassName: ViewGroupItemClickListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月11日 上午9:22:43 组item的item的监听事件
	 * 
	 */
	class ViewGroupItemClickListener implements View.OnClickListener {

		public void onClick(View v) {

		}

	}

	/**
	 * 计算需要显示反色item
	 * 
	 * @Title: TeleplayDetailActivity
	 * @author:张鹏展
	 * @Description:
	 * @param pageIndex
	 *            第几组
	 * @param itemIndex
	 *            第几个
	 */
	public void ViewGroupItemChange(int pageIndex, int itemIndex) {
		// 得到adapter的全部view
		HashMap<Integer, HiveBaseView> mapView = groupAdapter.getViews();
		// 得到当前展示到前台的view
		TeleplayVideosGroupsView pageView = (TeleplayVideosGroupsView) mapView.get(pageIndex);
		if (null != btnGetFocus) {// 只要不为空就代表这是上一个要还在焦点状态的btn
			btnGetFocus.setTextColor(getResources().getColor(R.color.white));
		} else {
			// 如果btn为空说就是在第一页
			btnGetFocus = pageView.setBtnChangeColor(0);
			btnGetFocus.setTextColor(getResources().getColor(R.color.white));
		}
		// 设置要改变的颜色的btn
		btnGetFocus = pageView.setBtnChangeColor(itemIndex % 5);
		groupPager.setCurrentItem(pageIndex);
	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("play".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(TeleplayDetailActivity.this, "播放", intent);
					if (null != newVideos && newVideos.size() > 0) {// 如果获取的播放列表不为空
						// 如果为-1的话说明用户直接点击的播放按钮，焦点没有经过剧集列表
						if (!vrsAlbumIdFromDatebase.equals("") && null != vrsAlbumIdFromDatebase) {// 不为空说明播放记录中查出了记录
							// QiYiPlayerUtil.startNewQiYiPlayer(TeleplayDetailActivity.this,
							// vrsTvIdFromDatebase, vrsAlbumIdFromDatebase);
							QiYiPlayerUtil.startSDKPlayer(TeleplayDetailActivity.this, com.alibaba.fastjson.JSONObject.toJSONString(video_Entity),
									null, false, true, false, null, false);
							isOnClick = true;
							startPlayerTime = System.currentTimeMillis();
							handler.sendEmptyMessage(PLAYER_GO_ON);
						} else {// 如果为空说明播放记录中没有这条，这样的话就是从获取的播放列表中提取第一个播放
							if (videosIndex == -1)
								videosIndex = 0;
							// 需要播放的实体类
							VideoNewEntity video = newVideos.get(videosIndex);
							// 播放的最新的名字数据
							if (video.getVideoId() > 0 && video.getVideosetId() > 0) {// 如果需要播放的关键数据不为空
								String vrsAlbumId = String.valueOf(video.getVideosetId());
								String vrsTvId = String.valueOf(video.getVideoId());
								player_Entity = video;
								player_video_Entity = video;
								isOnClick = true;
								startPlayerTime = System.currentTimeMillis();
								// QiYiPlayerUtil.startNewQiYiPlayer(TeleplayDetailActivity.this,
								// vrsTvId, vrsAlbumId);
								QiYiPlayerUtil.startSDKPlayer(TeleplayDetailActivity.this,
										com.alibaba.fastjson.JSONObject.toJSONString(video_Entity), null, false, true, false, null, false);
								handler.sendEmptyMessage(PLAYER_GO_ON);

							} else {// 如果播放数据不正确的时候，提示错误
								alertError();
							}
							startPlayerTime = System.currentTimeMillis();
							long time = startPlayerTime - creatTime;
							try {
								sendStatistics(String.valueOf(time), player_Entity, DataType.CLICK_TAB_VIDEO);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}

				} else if ("collect".equals(command)) {
					if (null != mVideoEntity) {
						boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
						// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
						if (isSaveed) {
							HomeSwitchTabUtil.closeSiRi(TeleplayDetailActivity.this, "您已经收藏", intent);

						} else {
							dao.insert(mVideoEntity);
							ivCollect.setImageResource(R.drawable.film_detail_fav_has);
							ivCollect.setTag(true);
							HomeSwitchTabUtil.closeSiRi(TeleplayDetailActivity.this, "收藏", intent);
						}

					}
				} else if ("canclecollect".equals(command)) {
					if (null != mVideoEntity) {
						boolean isSaveed = Boolean.parseBoolean(ivCollect.getTag().toString());
						// 修改如果当前是收藏状态就修改成非收藏状态,同时删除数据库的数据
						if (isSaveed) {
							dao.delete("id = ?", new String[] { String.valueOf(mVideoEntity.getId()) });
							ivCollect.setImageResource(R.drawable.film_detail_fav);
							ivCollect.setTag(false);
							HomeSwitchTabUtil.closeSiRi(TeleplayDetailActivity.this, "取消收藏", intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(TeleplayDetailActivity.this, "您还未收藏", intent);
						}

					}

				}
			}

		}
	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.TeleplayDetailActivity";

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
