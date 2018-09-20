package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.LiveFastSelectTvChannelActivity;
import com.hiveview.tv.activity.LiveSelectTelevisionActivity;
import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.activity.OnlivesTipsActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.dao.TvSetTopDAO;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.ProgramByCodeEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.entity.StringEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.AuxiliaryTimeView.TimeChangeListner;
import com.hiveview.tv.view.onlive.OnliveSelect;
import com.hiveview.tv.view.pager3d.HomeActions;
import com.hiveview.tv.view.pager3d.TabBasePageView;
import com.hiveview.tv.view.television.ChannelChangedListener;
import com.hiveview.tv.view.television.MarqueeText;
import com.hiveview.tv.view.television.voicecontrol.ChannelChangedReceiver;

public class MatrixTVView extends TabBasePageView implements ChannelChangedListener, TimeChangeListner {

	private static final String TAG = "MatrixTVView";

	/**
	 * TVView 初始化时获取当前频道的epg信息并设置相关view
	 */
	private static final int TV_VIEW_INIT_UPDATE_EPG = 1001;
	/**
	 * TVView matrixview进行切换时切换到TVView获取当前频道的epg信息并设置相关view
	 */
	private static final int TV_VIEW_SHOW_UPDATE_EPG = 1002;
	/**
	 * 当前节目播放完毕获取当前频道的epg信息并设置相关view
	 */
	private static final int PROGRAM_CHANGED_UPDATE_EPG = 1003;
	/**
	 * 频道切换时获取当前频道的epg信息并设置相关view
	 */
	private static final int CHANNEL_CHANGED_UPDATE_EPG = 1004;
	/**
	 * activity onRestart获取当前频道的epg信息并设置相关view
	 */
	private static final int ACTIVITY_RESTART_UPDATE_EPG = 1005;

	private final int LOAD_LEFT_DATA_SUCCESS = 100;

	private final int LOAD_LEFT_DATA_FAIL = -100;

	private final int LOAD_RIGHT_DATA_SUCCESS = 101;

	private final int LOAD_RIGHT_DATA_FAIL = -101;

	/**
	 * 存放推荐直播节目的数据集合
	 */
	private ArrayList<ProgramByCodeEntity> recommendProgramList = null;

	/**
	 * 
	 * EpgList为空
	 */
	private static final int GETEPGLIST_FAIL = -100;

	/**
	 * 推荐位Layout
	 */
	private View matrix_tv_layout_0, matrix_tv_layout_1, matrix_tv_layout_2, matrix_tv_layout_3, matrix_tv_layout_4, matrix_tv_layout_5,
			matrix_tv_layout_6, matrix_tv_layout_7, matrix_tv_layout_8;

	/**
	 * 显示推荐点播(直播窗口左侧)的海报图的ImageView
	 */
	private ImageView tv_image_load_view_0, tv_image_load_view_1;

	/**
	 * 推荐点播海报图ImageView数组
	 */
	private ImageView[] loadVews;

	/**
	 * 刷新直播节目（直播窗口右侧）的标志位
	 */
	private final int HANDLER_REFLESH_PROGRAMS = 1000;
	/**
	 * 下个节目开始时间的数组
	 */
	private String[] lnextProgramStartTime;
	/**
	 * 第一次开机时间
	 */
	public static String firstStartTime = "";

	/**
	 * 结合Timer使用，定时任务
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_REFLESH_PROGRAMS:
				getContext().sendBroadcast(new Intent(HomeActions.REFLESH_TV_RIGHT_ACTION));
				break;
			default:
				break;
			}
		}

	};

	private Handler mEPGUpdateHandler = new EPGUpdateHandler();

	/**
	 * 存储电视台节目海报ImageView的数组
	 */
	private ImageView[] wikiCovers;
	/**
	 * 显示电视台logo的ImageView数组
	 */
	private ImageView[] channelLogos;

	/**
	 * 显示电视台名称TextView数组
	 */
	private TextView[] channelNames;

	/**
	 * 显示当前节目名称的TextView数组
	 */
	private TextView[] currentProgram;

	/**
	 * 显示下一个节目名称的TextView数组
	 */
	private TextView[] nextProgram;
	/**
	 * 置顶电台可以获取焦点部分
	 */
	private View matrixTvLayout[];
	/**
	 * 计算下个节目开始时间距离当前时间的差
	 */
	private long durationToNextProgram5 = 0, durationToNextProgram6 = 0, durationToNextProgram7 = 0, durationToNextProgramMin = 0;

	private String mCurrentChannelName = "CCTV-新闻";
	private String mPreProgramName = "ERROR";
	private String mPreProgramStartTime = "ERROR";
	private String mNextProgramName = "ERROR";
	private String mNextProgramStartTime = "ERROR";
	private String mNextProgramStartDateTime = "ERROR";

	private Context mContext;
	/* start by guosongsheng 根据新需求修改 去掉上个节目、下个节目 修改成当前时间、进入全屏直播的提示 */
	private TextView tvLastOneContent, tvNextOneContent, tvLastOneContent_time, tvNextOneContent_time;
	/* end by guosongsheng */
	/**
	 * add tvCurrentTime by guosongsheng 当前时间
	 */
	private TextView tvCurrentTime;
	private ChannelChangedReceiver mChannelChangedReceiver = ChannelChangedReceiver.CreateChannelChangedReceiver(this);

	private ArrayList<ProgramEntity> currentEpgList;

//	private ArrayList<FilmNewEntity> films = null;

	private HiveTVService dataService = null;

	private String[] strings = new String[] { "2205", "2206", "2207" };

	public MatrixTVView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatrixTVView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public MatrixTVView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MatrixTVView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		mContext = context;
		init();
	}

	protected void init() {
		/* 利用文字换台接口将hdmiin频道设置为上次关机时频道 */
		dataService = new HiveTVService();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view = inflate(getContext(), R.layout.matrix_tv_view, null);

		this.addView(view, params);

		matrix_tv_layout_0 = findViewById(R.id.matrix_tv_layout_0);
		matrix_tv_layout_1 = findViewById(R.id.matrix_tv_layout_1);
		matrix_tv_layout_2 = findViewById(R.id.matrix_tv_layout_2);
		matrix_tv_layout_3 = findViewById(R.id.matrix_tv_layout_3);
		matrix_tv_layout_4 = findViewById(R.id.matrix_tv_layout_4);
		matrix_tv_layout_5 = findViewById(R.id.matrix_tv_layout_5);
		matrix_tv_layout_6 = findViewById(R.id.matrix_tv_layout_6);
		matrix_tv_layout_7 = findViewById(R.id.matrix_tv_layout_7);
		matrix_tv_layout_8 = findViewById(R.id.matrix_tv_layout_8);

		matrix_tv_layout_0.setNextFocusUpId(R.id.navigation_tab_tv_text);
		matrix_tv_layout_0.setNextFocusRightId(R.id.matrix_tv_layout_4);
		matrix_tv_layout_0.setNextFocusLeftId(R.id.matrix_game_layout_4);
		matrix_tv_layout_0.setNextFocusDownId(R.id.matrix_tv_layout_1);

		matrix_tv_layout_1.setNextFocusUpId(R.id.matrix_tv_layout_0);
		matrix_tv_layout_1.setNextFocusRightId(R.id.matrix_tv_layout_4);
		matrix_tv_layout_1.setNextFocusLeftId(R.id.matrix_game_layout_5);
		matrix_tv_layout_1.setNextFocusDownId(R.id.matrix_tv_layout_2);

		matrix_tv_layout_2.setNextFocusUpId(R.id.matrix_tv_layout_1);
		matrix_tv_layout_2.setNextFocusRightId(R.id.matrix_tv_layout_4);
		matrix_tv_layout_2.setNextFocusLeftId(R.id.matrix_game_layout_6);
		matrix_tv_layout_2.setNextFocusDownId(matrix_tv_layout_2.getId());

		matrix_tv_layout_4.setNextFocusUpId(R.id.navigation_tab_tv_text);
		matrix_tv_layout_4.setNextFocusRightId(R.id.matrix_tv_layout_6);
		matrix_tv_layout_4.setNextFocusLeftId(R.id.matrix_tv_layout_0);
		matrix_tv_layout_4.setNextFocusDownId(matrix_tv_layout_4.getId());

		matrix_tv_layout_6.setNextFocusUpId(R.id.navigation_tab_tv_text);
	//	matrix_tv_layout_6.setNextFocusRightId(R.id.matrix_bluelight_layout_0);
		matrix_tv_layout_6.setNextFocusLeftId(R.id.matrix_tv_layout_4);
		matrix_tv_layout_6.setNextFocusDownId(R.id.matrix_tv_layout_7);

		matrix_tv_layout_7.setNextFocusUpId(R.id.matrix_tv_layout_6);
	 //matrix_tv_layout_7.setNextFocusRightId(R.id.matrix_bluelight_layout_0);
		matrix_tv_layout_7.setNextFocusLeftId(R.id.matrix_tv_layout_4);
		matrix_tv_layout_7.setNextFocusDownId(R.id.matrix_tv_layout_8);

		matrix_tv_layout_8.setNextFocusUpId(R.id.matrix_tv_layout_7);
	//	matrix_tv_layout_8.setNextFocusRightId(R.id.matrix_bluelight_layout_2);
		matrix_tv_layout_8.setNextFocusLeftId(R.id.matrix_tv_layout_4);
		matrix_tv_layout_8.setNextFocusDownId(matrix_tv_layout_8.getId());

		recommendEdgeViews = new View[] { matrix_tv_layout_0, matrix_tv_layout_1, matrix_tv_layout_5, matrix_tv_layout_6, matrix_tv_layout_7,
				matrix_tv_layout_8 };
		matrixTvLayout = new View[] { matrix_tv_layout_6, matrix_tv_layout_7, matrix_tv_layout_8 };

		tv_image_load_view_0 = (ImageView) findViewById(R.id.tv_image_load_view_0);
		tv_image_load_view_1 = (ImageView) findViewById(R.id.tv_image_load_view_1);

		loadVews = new ImageView[] { tv_image_load_view_0, tv_image_load_view_1 };

		ImageView captureLoadView6 = (ImageView) findViewById(R.id.tv_image_load_view_6_capture);
		ImageView captureLoadView7 = (ImageView) findViewById(R.id.tv_image_load_view_7_capture);
		ImageView captureLoadView8 = (ImageView) findViewById(R.id.tv_image_load_view_8_capture);
		wikiCovers = new ImageView[] { captureLoadView6, captureLoadView7, captureLoadView8 };

		ImageView logoLoadView6 = (ImageView) findViewById(R.id.tv_image_load_view_6_logo);
		ImageView logoLoadView7 = (ImageView) findViewById(R.id.tv_image_load_view_7_logo);
		ImageView logoLoadView8 = (ImageView) findViewById(R.id.tv_image_load_view_8_logo);
		channelLogos = new ImageView[] { logoLoadView6, logoLoadView7, logoLoadView8 };

		MarqueeText channNameTextView6 = (MarqueeText) findViewById(R.id.tv_channel_text_6_channel_name);
		MarqueeText channNameTextView7 = (MarqueeText) findViewById(R.id.tv_channel_text_7_channel_name);
		MarqueeText channNameTextView8 = (MarqueeText) findViewById(R.id.tv_channel_text_8_channel_name);
		channelNames = new TextView[] { channNameTextView6, channNameTextView7, channNameTextView8 };

		MarqueeText currentProgramTextView6 = (MarqueeText) findViewById(R.id.tv_channel_text_6_current_program);
		MarqueeText currentProgramTextView7 = (MarqueeText) findViewById(R.id.tv_channel_text_7_current_program);
		MarqueeText currentProgramTextView8 = (MarqueeText) findViewById(R.id.tv_channel_text_8_current_program);
		currentProgram = new TextView[] { currentProgramTextView6, currentProgramTextView7, currentProgramTextView8 };

		MarqueeText nextProgramTextView6 = (MarqueeText) findViewById(R.id.tv_channel_text_6_next_program);
		MarqueeText nextProgramTextView7 = (MarqueeText) findViewById(R.id.tv_channel_text_7_next_program);
		MarqueeText nextProgramTextView8 = (MarqueeText) findViewById(R.id.tv_channel_text_8_next_program);
		nextProgram = new TextView[] { nextProgramTextView6, nextProgramTextView7, nextProgramTextView8 };
		lnextProgramStartTime = new String[] { "", "", "" };

		matrix_tv_layout_0.setOnClickListener(new TvFastViewClickListener());
		matrix_tv_layout_1.setOnClickListener(new TvCategoryViewClickListener());
		matrix_tv_layout_2.setOnClickListener(new TvNotificationViewClickListener());

		/* start by guosongsheng 根据新需求修改 去掉上个节目、下个节目 修改成当前时间、进入全屏直播的提示 */
		tvCurrentTime = (TextView) this.findViewById(R.id.tv_current_time);
		try {
			Date date = new Date();
			tvCurrentTime.setText(String.format(getResources().getString(R.string.tv_current_matrix_time), DateUtils.dataToString(date.getTime()),
					DateUtils.getDateWithWeek(date)));
			// 设置首次设置的时间
			firstStartTime = DateUtils.dataToString(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// tvLastOneContent = (TextView)
		// this.findViewById(R.id.tv_last_one_content);
		// tvLastOneContent_time = (TextView)
		// this.findViewById(R.id.tv_last_one_content_time);
		// tvNextOneContent = (TextView)
		// this.findViewById(R.id.tv_next_one_content);
		// tvNextOneContent_time = (TextView)
		// this.findViewById(R.id.tv_next_one_content_time);
		/* end by guosongsheng */

		matrix_tv_layout_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					KeyEventHandler.post(new DataHolder.Builder(getContext()).setTabNo(Tab.TV).setViewPosition("2204")
							.setPositionId(String.valueOf(v.getId())).setDataType(null).build());
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 告诉讯飞进入直播
				// Log.i(TAG, "notifyTVLiveStatus------------------>start");
				// TvManUtil.tellTVState(mContext, true, null);
				Intent intent = new Intent((Activity) mContext, OnlivePlayerActivity.class);
				mContext.sendBroadcast(new Intent(HiveviewHdmiInView.ACTION_SMALL_SCREEN_SWITCH));
				((Activity) mContext).startActivity(intent);
			}
		});

		ChannelViewOnClickListener channelVideoOnClickListener = new ChannelViewOnClickListener();
		matrix_tv_layout_6.setOnClickListener(channelVideoOnClickListener);
		matrix_tv_layout_7.setOnClickListener(channelVideoOnClickListener);
		matrix_tv_layout_8.setOnClickListener(channelVideoOnClickListener);

		matrix_tv_layout_6.setOnFocusChangeListener(new ItemFocusChangeListener(nextProgramTextView6, currentProgramTextView6));
		matrix_tv_layout_7.setOnFocusChangeListener(new ItemFocusChangeListener(nextProgramTextView7, currentProgramTextView7));
		matrix_tv_layout_8.setOnFocusChangeListener(new ItemFocusChangeListener(nextProgramTextView8, currentProgramTextView8));

		mChannelChangedReceiver.registerChannelChangedReceiver(mContext);
		mEPGUpdateHandler.sendEmptyMessage(TV_VIEW_INIT_UPDATE_EPG);

		/* start by guosongsheng 初始化广播监听 监听置顶广播 */
		// 初始化监听数据变化的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(HomeActions.REFLESH_TV_RIGHT_ACTION);
		filter.addAction(AuxiliaryNetworkView.CONNECTION_STATUS_ACTION);
		mContext.registerReceiver(dataReceiver, filter);
		// 设置时间变换接口
		AuxiliaryTimeView auxiliaryTimeView = new AuxiliaryTimeView(mContext);
		auxiliaryTimeView.setTimeChangeListner(this);
		/* end by guosongsheng */
		super.init();

	}
	
	/* (non-Javadoc)
	 * @see com.hiveview.tv.view.pager3d.TabBasePageView#setUnregister()
	 */
	public void setUnregister() {
		try {
			mContext.unregisterReceiver(dataReceiver);
			((HiveviewHdmiInView) this.findViewById(R.id.view_hdmi_small)).setUnregister();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * 接受广播 add by guosongsheng 相应用户置顶电台 刷新电视置顶
	 */
	private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent mIntent) {
			if (mIntent.getAction().equals(HomeActions.REFLESH_TV_RIGHT_ACTION)) {// 响应用户置顶电视台
				// 刷新制订的电视位
				refreshTopChannel();
			} else if (mIntent.getAction().equals(AuxiliaryNetworkView.CONNECTION_STATUS_ACTION)) {
				// 刷新制订的电视位
				Log.i(TAG, "com.hiveview.tv.ACTION_INTERNET_CONNECTION_STATUS.......................................");
				refreshTopChannel();
			}
		}
	};

	int i = 0;

	/**
	 * 获取上个节目单、下个节目单 目前不适用
	 * 
	 * @Title: MatrixTVView
	 * @author:郭松胜
	 * @Description: TODO
	 * @param currentChannelName
	 */
	private void getEpgList(String currentChannelName) {
		Log.i(TAG, "getEpgList-->start");
		i++;
		Log.d(TAG, i + "");
		/* 设置当前频道名称来获取该频道对应的EPG信息 */
		if (mCurrentChannelName.equals("cctv1")) {
			mCurrentChannelName = "cctv2";
		} else {
			mCurrentChannelName = "cctv1";
		}

		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {

				currentEpgList = (ArrayList<ProgramEntity>) new HiveTVService().getProgramsByChannel(mCurrentChannelName, DateUtils.getDateYyMmDd(-1)
						+ " 00:00", DateUtils.getDateYyMmDd(1) + " 23:59");

				/* 未获取到当前频道的EPG信息 */
				if (0 == currentEpgList.size() && currentEpgList.isEmpty()) {

					mEPGUpdateHandler.sendEmptyMessageDelayed(GETEPGLIST_FAIL, 2000);

					return;
				}
				Log.i(TAG, "currentEpgList:" + currentEpgList.get(0).toString());
				int currentProgramEntiyIndex = 0;

				for (ProgramEntity entity : currentEpgList) {
					if (AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())
							&& !AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getEnd_time())) {
						if (0 < currentProgramEntiyIndex) {
							mPreProgramName = currentEpgList.get(currentProgramEntiyIndex - 1).getName();
							mPreProgramStartTime = currentEpgList.get(currentProgramEntiyIndex - 1).getStart_time();
							Log.d(TAG, mPreProgramName);
						}

						if (currentEpgList.size() > currentProgramEntiyIndex) {
							mNextProgramName = currentEpgList.get(currentProgramEntiyIndex + 1).getName();
							mNextProgramStartTime = currentEpgList.get(currentProgramEntiyIndex + 1).getStart_time();
							mNextProgramStartDateTime = currentEpgList.get(currentProgramEntiyIndex + 1).getDate() + " "
									+ currentEpgList.get(currentProgramEntiyIndex + 1).getStart_time() + ":00";
						}

						break;
					}
					currentProgramEntiyIndex++;
				}

				if (currentProgramEntiyIndex >= currentEpgList.size()) {
					return;
				}
				/* start by guosongsheng 根据新需求修改 去掉上个节目、下个节目 修改成当前时间、进入全屏直播的提示 */
				// 设置上一个节目和下一个节目信息到直播窗口上下显示
				((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Animation anima = AnimationUtils.loadAnimation(mContext, R.anim.bottomup);

						String preProgramStartTime = tvLastOneContent_time.getText().toString();
						String preProgramName = tvLastOneContent.getText().toString();
						String nextProgramStartTime = tvNextOneContent_time.getText().toString();
						String nextProgramName = tvNextOneContent.getText().toString();
						if (!mPreProgramStartTime.equals(preProgramStartTime) && !mPreProgramName.equals(preProgramName)) {
							Log.v(TAG, "mPreProgramStartTime1:" + mPreProgramStartTime);
							tvLastOneContent.setAnimation(anima);
							tvLastOneContent_time.setAnimation(anima);
							tvLastOneContent_time.setText(mPreProgramStartTime + "  ");
							tvLastOneContent.setText(mPreProgramName);

						} else {
							Log.v(TAG, "mPreProgramStartTime2:" + mPreProgramStartTime);

						}
						if (!mNextProgramStartTime.equals(nextProgramStartTime) && !mNextProgramName.equals(nextProgramName)) {
							Log.v(TAG, "mNextProgramStartTime1:" + mNextProgramStartTime);
							tvNextOneContent_time.setText(mNextProgramStartTime + "  ");
							tvNextOneContent.setText(mNextProgramName);
							tvNextOneContent.setAnimation(anima);
							tvNextOneContent_time.setAnimation(anima);
						} else {
							Log.v(TAG, "mNextProgramStartTime2:" + mNextProgramStartTime);

						}
					}
				});
				/* end by guosongsheng */

				Date nowDate = new Date();
				long durationToNextProgram = 0;
				if (!TextUtils.isEmpty(mNextProgramStartDateTime) && !mNextProgramStartDateTime.equals("ERROR")) {
					durationToNextProgram = DateUtils.twoTimeDiffer(DateUtils.formatTimeYyMmDdHhMmSs(nowDate), mNextProgramStartDateTime);
					Log.i(TAG, "getEpgList::HttpTaskManager-->durationToNextProgram:" + durationToNextProgram);
				}

				/* 设置定时器，下个节目开始时出发更新lastprogram和nextprograme显示信息 */
				mEPGUpdateHandler.removeMessages(PROGRAM_CHANGED_UPDATE_EPG);
				mEPGUpdateHandler.sendEmptyMessageDelayed(PROGRAM_CHANGED_UPDATE_EPG, durationToNextProgram);
			}
		});
	}

	/**
	 * 刷新制订的电视位
	 * 
	 * @Title: MatrixTVView
	 * @author:guosongsheng
	 * @Description: TODO
	 */
	protected void refreshTopChannel() {
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				// 刷新置顶信息
				loadData(true);
			}

			@Override
			public void processServiceException(ServiceException e) {
				Log.e(TAG, "tv refresh data error:" + e.toString());
			}

		});
	}

	/**
	 * 设置直播窗口右侧的直播节目信息
	 * 
	 * @param recommendProgramList
	 */
	public void setMatrixProgramData() {
		try {
			int count = recommendProgramList.size();

			DisplayImageOptions logoOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.live_channel_logo_default)
					.showImageOnFail(R.drawable.live_channel_logo_default).resetViewBeforeLoading(false).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

			DisplayImageOptions coverOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.live_recommand_program_default)
					.showImageOnFail(R.drawable.live_recommand_program_default).resetViewBeforeLoading(false).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

			for (int i = 0; i < count; i++) {
				ProgramByCodeEntity entity = recommendProgramList.get(i);
				matrixTvLayout[i].setTag(entity);
				wikiCovers[i].setImageResource(R.drawable.live_recommand_program_default);
				ImageLoader.getInstance().displayImage(entity.getWiki_cover(), wikiCovers[i], coverOptions);

				channelLogos[i].setImageResource(R.drawable.live_channel_logo_default);

				ImageLoader.getInstance().displayImage(entity.getChannel_logo(), channelLogos[i], logoOptions);
				channelNames[i].setText(entity.getChannel_name());// 电视台名称
				if (!TextUtils.isEmpty(entity.getName())) {
					currentProgram[i].setText(entity.getName());// 当前节目名称
					lnextProgramStartTime[i] = entity.getDate() + " " + entity.getNext_start_time() + ":00";
					nextProgram[i].setText(entity.getNext_start_time() + "  " + entity.getNext_name());// 下一个节目名称
				} else {
					currentProgram[i].setText("");// 当前节目名称
					lnextProgramStartTime[i] = "";
					nextProgram[i].setText("");// 下一个节目名称
				}
			}

			Log.i(TAG, "lnextProgramStartTime[0]" + lnextProgramStartTime[0] + "lnextProgramStartTime[1]" + lnextProgramStartTime[1]
					+ "lnextProgramStartTime[2]" + lnextProgramStartTime[2]);
			// 刷新电视直播窗口的右侧的直播节目单信息
			Date nowDate = new Date();
			// 计算下个节目开始时间距离当前时间的差
			durationToNextProgram5 = DateUtils.twoTimeDiffer(DateUtils.formatTimeYyMmDdHhMmSs(nowDate), lnextProgramStartTime[0]);
			durationToNextProgram6 = DateUtils.twoTimeDiffer(DateUtils.formatTimeYyMmDdHhMmSs(nowDate), lnextProgramStartTime[1]);
			durationToNextProgram7 = DateUtils.twoTimeDiffer(DateUtils.formatTimeYyMmDdHhMmSs(nowDate), lnextProgramStartTime[2]);
			// 延时2秒
			durationToNextProgram5 += 2000;
			durationToNextProgram6 += 2000;
			durationToNextProgram7 += 2000;
			Log.i(TAG, "refleshProgram-->nowdate" + DateUtils.formatTimeYyMmDdHhMmSs(nowDate));
			Log.i(TAG, "refleshProgram-->durationToNextProgram5" + durationToNextProgram5);
			Log.i(TAG, "refleshProgram-->durationToNextProgram6" + durationToNextProgram6);
			Log.i(TAG, "refleshProgram-->durationToNextProgram7" + durationToNextProgram7);
			// 获取最新时间
			durationToNextProgramMin = getMinStartTime(new long[] { durationToNextProgram5, durationToNextProgram6, durationToNextProgram7 });
			mHandler.removeCallbacksAndMessages(null);
			if (durationToNextProgramMin > 0) {
				Log.i(TAG, "refleshProgram-->durationToNextProgram  MIN........" + durationToNextProgramMin);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(HANDLER_REFLESH_PROGRAMS);
					}
				}, durationToNextProgramMin);
			} else {
				mHandler.removeMessages(HANDLER_REFLESH_PROGRAMS);
			}
			this.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取最新时间
	 * 
	 * @Title: MatrixTVView
	 * @author:郭松胜
	 * @Description: TODO
	 * @param durationToNextProgram5
	 * @param durationToNextProgram6
	 * @param durationToNextProgram7
	 * @return
	 */
	private long getMinStartTime(long[] startTimes) {
		long minTime = 0;
		// 排序
		for (int i = 0; i < startTimes.length; i++) {
			for (int j = i; j < startTimes.length; j++) {
				if (startTimes[i] > startTimes[j]) {
					minTime = startTimes[i];
					startTimes[i] = startTimes[j];
					startTimes[j] = minTime;
				}
			}
		}
		// 返回第一个大于0 的
		for (int k = 0; k < startTimes.length; k++) {
			if (startTimes[k] > 0) {
				return startTimes[k];
			}
		}
		return 0;
	}

	/**
	 * 响应直播换台的单击事件，讯飞的语音遥控器OK键
	 * 
	 * @author chenlixiao
	 * 
	 */
	private class ChannelViewOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int index = -1;
			switch (v.getId()) {
			case R.id.matrix_tv_layout_6:
				index = 0;
				break;
			case R.id.matrix_tv_layout_7:
				index = 1;
				break;
			case R.id.matrix_tv_layout_8:
				index = 2;
				break;
			}

			// 请求要换台的epg信息
			OnliveSelect.channelName = channelNames[index].getText().toString();
			Log.i(TAG, "OnliveSelect.channelName ----- >" + OnliveSelect.channelName);
			// 通知讯飞歌华盒子换台
			SwitchChannelUtils.switchChannel(getContext(), channelNames[index].getText().toString(), false, AppScene.getScene());

			KeyEventHandler.post(new DataHolder.Builder(getContext()).setTabNo(Tab.TV).setViewPosition(null == strings[index] ? "" : strings[index])
					.setPositionId(String.valueOf(v.getId())).setDataType(null).build());
		}
	}

	@Override
	public void onChannelChanged(String ChannelName) {
		mEPGUpdateHandler.sendEmptyMessage(CHANNEL_CHANGED_UPDATE_EPG);
	}

	@SuppressLint("HandlerLeak")
	private class EPGUpdateHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TV_VIEW_INIT_UPDATE_EPG:
				Log.i(TAG, "case TV_VIEW_INIT_UPDATE_EPG");
				break;
			case TV_VIEW_SHOW_UPDATE_EPG: // 需要验证view为
				Log.i(TAG, "case TV_VIEW_SHOW_UPDATE_EPG");
				break;
			case PROGRAM_CHANGED_UPDATE_EPG:
				Log.i(TAG, "case PROGRAM_CHANGED_UPDATE_EPG");
				break;
			case CHANNEL_CHANGED_UPDATE_EPG:
				Log.i(TAG, "case CHANNEL_CHANGED_UPDATE_EPG");
				break;
			case ACTIVITY_RESTART_UPDATE_EPG: // 设置监听器监听activity的onRestart事件
				Log.i(TAG, "case ACTIVITY_RESTART_UPDATE_EPG");
				break;
			case GETEPGLIST_FAIL:
				Log.i(TAG, "case GETEPGLIST_FAIL");
			default:
				break;
			}
			/* start by guosongsheng 新需求中没有上一个节目、下一个节目 */
			// getEpgList("");
			/* end by guosongsheng */
		}

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		mContext.unregisterReceiver(mChannelChangedReceiver);
		super.finalize();
	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_RIGHT_DATA_SUCCESS:
			setMatrixProgramData();
			break;
		default:
			break;
		}
	}

	@Override
	public void loadData(boolean isRefleshData) {
		try {

		//	List<TotalListFilmNewEntity> totalMovieList = dataService.getVideoSetList(getContext(), 1, 5, 1);
			// dataService.getChannelLiveRecommend("2" , "" , "cctv1");
			//films = totalMovieList.get(0).getFilms();
			//sendLoadDataResultMessage(LOAD_LEFT_DATA_SUCCESS);
			// 得到用户置顶的电视台
			TvSetTopDAO topDao = new TvSetTopDAO(getContext());
			@SuppressWarnings("unchecked")
			List<StringEntity> topList = (List<StringEntity>) topDao.query(null, null, null, null);

			// 随机的3个电视台，根据电视台的code去查询节目单
			ArrayList<String> codes = new ArrayList<String>();
			// "cctv1", "cctv2", "cctv10"

			// 填充用户置顶电视台的code
			for (int i = 0; i < topList.size(); i++) {
				codes.add(topList.get(i).getStrParams());
			}

			for (String code : getTestChannelCodes()) {
				if (codes.size() >= 3) {
					break;
				}

				if (!codes.contains(code)) {
					codes.add(code);
				}
			}

			String[] targetCode = new String[3];
			// 根据用户置顶的codes获取节目单
			recommendProgramList = dataService.getProgramsByCodes(codes.toArray(targetCode), null, null);
			for (int i = 0; i < recommendProgramList.size(); i++) {
				Log.v(TAG, "recommendProgramList-->channel_code:" + recommendProgramList.get(i).getChannel_code() + ",channel_name:"
						+ recommendProgramList.get(i).getChannel_name() + ",channel_logo:" + recommendProgramList.get(i).getChannel_logo());

			}

			// 根据用户置顶的先后顺序，把用户最后置顶的放到最前面
			for (ProgramByCodeEntity entity : recommendProgramList) {
				for (int i = 0; i < topList.size(); i++) {
					if (entity.getChannel_code().equals(topList.get(i).getStrParams())) {
						entity.setPositionInItemView(topList.get(i).getIntParams());
					}
				}
			}

			Collections.sort(recommendProgramList, new Comparator<ProgramByCodeEntity>() {

				@Override
				public int compare(ProgramByCodeEntity arg0, ProgramByCodeEntity arg1) {
					try {
						int previous = Integer.valueOf(arg0.getPositionInItemView());
						int next = Integer.valueOf(arg1.getPositionInItemView());
						if (previous > next) {
							return -1;
						} else if (previous < next) {
							return 1;
						} else {
							throw new Exception();
						}
					} catch (Exception e) {
						return 0;
					}
				}

			});

			sendLoadDataResultMessage(LOAD_RIGHT_DATA_SUCCESS);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 电视直播窗口右侧的三个电视台推荐测试code
	 */
	private String[] testCodes = { "cctv1", "cctv2", "cctv3", "cctv4_asia", "cctv5", "cctv6", "cctv7", "cctv8", "cctv9", "cctv10", "cctv11",
			"cctv12", "cctv_news", "cctv_kids", "cctv_music" };

	/**
	 * 得到测试电视台的code
	 * 
	 * @return
	 */
	public String[] getTestChannelCodes() {
		return testCodes;
	}

	@Override
	public void updateUIRefleshData() {

	}

	/**
	 * 响应点击事件 点击后进入 快速选台
	 */
	class TvFastViewClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			Intent intent = new Intent(getContext(), LiveFastSelectTvChannelActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_tv_direction_text))
					.setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON).setViewPosition("2201").setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应点击事件 点击后进入 分类选台
	 */
	class TvCategoryViewClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			Intent intent = new Intent(getContext(), LiveSelectTelevisionActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_tv_category_text))
					.setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON).setViewPosition("2202").setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应点击事件 点击后进入 直播提醒
	 */
	class TvNotificationViewClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), OnlivesTipsActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_tv_notification_text))
					.setSenceName(AppScene.getScene()).setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).setViewPosition("2203").build();
		}
	}

	/*
	 * @Override public View getBottomMenuView() { View tvTabView =
	 * inflate(getContext(), R.layout.sub_navigation_tv, null);
	 * 
	 * View tvCategoryLayout =
	 * tvTabView.findViewById(R.id.sub_navigation_tv_category_text_layout); View
	 * tvDirectionLayout =
	 * tvTabView.findViewById(R.id.sub_navigation_tv_direction_text_layout);
	 * View tvNotificationLayout =
	 * tvTabView.findViewById(R.id.sub_navigation_tv_notification_text_layout);
	 * 
	 * tvDirectionLayout.setNextFocusUpId(R.id.matrix_tv_layout_3);
	 * tvDirectionLayout
	 * .setNextFocusRightId(R.id.sub_navigation_tv_notification_text_layout);
	 * tvDirectionLayout
	 * .setNextFocusDownId(R.id.sub_navigation_tv_direction_text_layout);
	 * tvDirectionLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_tv_category_text_layout);
	 * 
	 * tvCategoryLayout.setNextFocusUpId(R.id.matrix_tv_layout_3);
	 * tvCategoryLayout
	 * .setNextFocusRightId(R.id.sub_navigation_tv_direction_text_layout);
	 * tvCategoryLayout
	 * .setNextFocusDownId(R.id.sub_navigation_tv_category_text_layout);
	 * tvCategoryLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_tv_category_text_layout);
	 * 
	 * tvNotificationLayout.setNextFocusUpId(R.id.matrix_tv_layout_3);
	 * tvNotificationLayout
	 * .setNextFocusRightId(R.id.sub_navigation_tv_notification_text_layout);
	 * tvNotificationLayout
	 * .setNextFocusDownId(R.id.sub_navigation_tv_notification_text_layout);
	 * tvNotificationLayout
	 * .setNextFocusLeftId(R.id.sub_navigation_tv_direction_text_layout);
	 * 
	 * tvDirectionLayout.setOnClickListener(new TvTabViewClickListener());
	 * tvCategoryLayout.setOnClickListener(new
	 * TvTabCategoryViewClickListener());
	 * tvNotificationLayout.setOnClickListener(new
	 * TvTabNotificationViewClickListener());
	 * 
	 * bottomMenuViews = new View[] { tvCategoryLayout, tvDirectionLayout,
	 * tvNotificationLayout };
	 * viewFocusDirectionListener.setButtomMenuViewFocusDirection
	 * (bottomMenuViews);
	 * 
	 * return tvTabView; }
	 *//**
	 * 响应点击事件 点击后进入 快速选台
	 */
	/*
	 * class TvTabViewClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) { Intent intent = new
	 * Intent(getContext(), LiveFastSelectTvChannelActivity.class);
	 * getContext().startActivity(intent); return new
	 * DataHolder.Builder(getContext()) .setDataType(DataType.CLICK_TAB_BUTTON)
	 * .setButton(String.valueOf(view.getId()), ((TextView)
	 * view.findViewById(R.id
	 * .sub_navigation_tv_direction_text_view)).getText().toString())
	 * .setSenceName
	 * (AppScene.getScene()).setSrcType(ItemType.BUTTON).setViewPosition
	 * ("2209").setTabNo(Tab.TV).build(); } }
	 *//**
	 * 响应点击事件 点击后进入 分类选台
	 */
	/*
	 * class TvTabCategoryViewClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) { Intent intent = new
	 * Intent(getContext(), LiveSelectTelevisionActivity.class);
	 * getContext().startActivity(intent); return new
	 * DataHolder.Builder(getContext()) .setDataType(DataType.CLICK_TAB_BUTTON)
	 * .setButton(String.valueOf(view.getId()), ((TextView)
	 * view.findViewById(R.id
	 * .sub_navigation_tv_category_text_view)).getText().toString())
	 * .setSenceName
	 * (AppScene.getScene()).setSrcType(ItemType.BUTTON).setViewPosition
	 * ("2210").setTabNo(Tab.TV).build(); } }
	 *//**
	 * 响应点击事件 点击后进入 直播提醒
	 */
	/*
	 * class TvTabNotificationViewClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) {
	 * 
	 * Intent intent = new Intent(getContext(), OnlivesTipsActivity.class);
	 * getContext().startActivity(intent); return new
	 * DataHolder.Builder(getContext()) .setDataType(DataType.CLICK_TAB_BUTTON)
	 * .setButton(String.valueOf(view.getId()), ((TextView)
	 * view.findViewById(R.id
	 * .sub_navigation_tv_notification_text_view)).getText().toString())
	 * .setSenceName
	 * (AppScene.getScene()).setSrcType(ItemType.BUTTON).setTabNo(Tab
	 * .TV).setViewPosition("2211").build(); } }
	 */

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup tvTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_tv_layout, null);
		TextView topTabTextView = (TextView) tvTabView.findViewById(R.id.navigation_tab_tv_text);
		ImageView topFadeTabImageView = (ImageView) tvTabView.findViewById(R.id.navigation_tab_tv_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));

		topTabTextView.setNextFocusDownId(R.id.matrix_tv_layout_4);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_tv_text);

		return tvTabView;
	}

	/**
	 * 焦点变化事件
	 */
	class ItemFocusChangeListener implements OnFocusChangeListener {

		public MarqueeText tv1;
		public MarqueeText tv2;

		public ItemFocusChangeListener(MarqueeText tv1, MarqueeText tv2) {
			this.tv1 = tv1;
			this.tv2 = tv2;
		}

		@Override
		public void onFocusChange(View view, boolean has) {
			ProgramByCodeEntity entity = (ProgramByCodeEntity) view.getTag();
			if (null != entity && null != view) {
				if (!TextUtils.isEmpty(entity.getNext_start_time()) && !TextUtils.isEmpty(entity.getNext_name())) {
					tv1.setStart(has);
					tv1.setText(entity.getNext_start_time() + "  " + entity.getNext_name());
				}
				if (!TextUtils.isEmpty(entity.getName())) {
					tv2.setStart(has);
					tv2.setText(entity.getName());
				}
			}
		}

	}

	/**
	 * 修改时间
	 */
	@Override
	public void updateTime(Date date) {
		if (null != date && !firstStartTime.equals(DateUtils.dataToString(date.getTime()))) {
			Log.i("AuxiliaryTimeView", "MatrixTVView ............updateTime..................");
			tvCurrentTime.setText(String.format(getResources().getString(R.string.tv_current_matrix_time), DateUtils.dataToString(date.getTime()),
					DateUtils.getDateWithWeek(date)));
			firstStartTime = DateUtils.dataToString(date.getTime());
		} else {
			// 测试的组合TAG
			Log.i("AuxiliaryTimeView", "time change");
		}
	}

	@Override
	public View getBottomMenuView() {
		View tvTabView = inflate(getContext(), R.layout.sub_navigation_common_tv, null);
		View tvRecordLayout = tvTabView.findViewById(R.id.sub_navigation_common_tv_record_text_layout);
		View tvFavouriteLayout = tvTabView.findViewById(R.id.sub_navigation_common_tv_favourite_text_layout);
		View tvInstalledLayout = tvTabView.findViewById(R.id.sub_navigation_common_tv_installed_text_layout);
		View tvSearchLayout = tvTabView.findViewById(R.id.sub_navigation_common_tv_search_text_layout);
		View tvSettingLayout = tvTabView.findViewById(R.id.sub_navigation_common_tv_setting_text_layout);
		// View tvUserLayout =
		// tvTabView.findViewById(R.id.sub_navigation_common_tv_user_text_layout);
		View tvExternalLayout = tvTabView.findViewById(R.id.sub_navigation_common_tv_external_text_layout);

		tvRecordLayout.setNextFocusUpId(matrix_tv_layout_2.getId());
		tvFavouriteLayout.setNextFocusUpId(matrix_tv_layout_4.getId());
		tvSearchLayout.setNextFocusUpId(matrix_tv_layout_4.getId());
		tvInstalledLayout.setNextFocusUpId(matrix_tv_layout_4.getId());
		tvSettingLayout.setNextFocusUpId(matrix_tv_layout_8.getId());
		tvExternalLayout.setNextFocusUpId(matrix_tv_layout_8.getId());

		tvRecordLayout.setOnClickListener(new tvRecordClickListener());
		tvFavouriteLayout.setOnClickListener(new tvFavouriteClickListener());
		tvInstalledLayout.setOnClickListener(new tvInstalledClickListener());
		tvSearchLayout.setOnClickListener(new tvSearchClickListener());
		tvSettingLayout.setOnClickListener(new tvSettingClickListener());
		// tvUserLayout.setOnClickListener(new tvUserClickListener());
		tvExternalLayout.setOnClickListener(new tvExternalClickListener());
		bottomMenuViews = new View[] { tvRecordLayout, tvFavouriteLayout, tvInstalledLayout, tvSearchLayout, tvSettingLayout, tvExternalLayout };// ,
																																					// tvUserLayout
		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);
		return tvTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class tvRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("2208").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class tvFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("2209").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class tvInstalledClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.appstore.buy");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra("category_id", 2);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_installed_text))
					.setSenceName(AppScene.getScene()).setViewPosition("2210").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class tvSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("2211").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class tvSettingClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.settings.ACTION_SETTING");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			/* start by ZhaiJianfeng */
			int netStatus = HiveviewApplication.getNetStatus();
			intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
			/* end by ZhaiJianfeng */
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_setting_text))
					.setSenceName(AppScene.getScene()).setViewPosition("2212").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 */
	// class tvUserClickListener extends SimpleOnClickListener {
	// @Override
	// public DataHolder doOnClick(View view) {
	// Intent intent = new Intent();
	// intent.setAction("com.hiveview.user.usercenter");
	// intent.addCategory(Intent.CATEGORY_DEFAULT);
	// getContext().startActivity(intent);
	//
	// return new DataHolder.Builder(getContext())
	// .setDataType(DataType.CLICK_TAB_BUTTON)
	// .setButton(String.valueOf(view.getId()),
	// ((TextView)
	// view.findViewById(R.id.sub_navigation_common_tv_user_text_view)).getText().toString())
	// .setSenceName(AppScene.getScene()).setViewPosition("2213").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
	// }
	// }

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class tvExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("2214").setSrcType(ItemType.BUTTON).setTabNo(Tab.TV).build();
		}
	}

}
