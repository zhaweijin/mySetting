package com.hiveview.tv.activity;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.fragment.LiveEpgInfoFragment;
import com.hiveview.tv.fragment.LiveEpgInfoFragment.ItemViewFocusCallBackListener;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.OnliveTipService;
import com.hiveview.tv.service.dao.OnliveTipsDAO;
import com.hiveview.tv.service.dao.TvSetTopDAO;
import com.hiveview.tv.service.entity.OnliveTipsEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.entity.StringEntity;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.ProgressDialog;
import com.paster.util.JsonUtil;

public class LiveEpgInfoActivity extends BaseActivity {

	/** 每页的数据 */
	protected int PAGE_COUNT = 7;
	/**
	 * 加载节目单数据失败
	 */
	protected static final int RQUEST_DATA_FAIL = -100;
	/**
	 * 加载节目单数据成功
	 */
	protected static final int RQUEST_DATA_SUCCESS = 200;
	/**
	 * 加载周一节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK1_SUCCESS = 300;
	/**
	 * 加载周二节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK2_SUCCESS = 400;
	/**
	 * 加载周三节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK3_SUCCESS = 500;
	/**
	 * 加载周四节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK4_SUCCESS = 600;
	/**
	 * 加载周五节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK5_SUCCESS = 700;
	/**
	 * 加载周六节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK6_SUCCESS = 800;
	/**
	 * 加载周日节目单数据成功
	 */
	protected static final int RQUEST_DATA_WEEK7_SUCCESS = 900;

	protected static final String TAG = "LiveEpgInfoActivity";
	/**
	 * FragmentManager
	 */
	private FragmentManager fm;

	/**
	 * 电台编号
	 */
	private String CHANNEL_CODE = "";
	/**
	 * 电台logo
	 */
	String CHANNEL_LOGO = "";
	/**
	 * 电台名称
	 */
	String CHANNEL_NAME = "";

	private ProgressDialog mProgressDialog;
	private RelativeLayout rlContainer;
	/**
	 * 星期一到星期日tab
	 */
	private LinearLayout llWeek01;
	private LinearLayout llWeek02;
	private LinearLayout llWeek03;
	private LinearLayout llWeek04;
	private LinearLayout llWeek05;
	private LinearLayout llWeek06;
	private LinearLayout llWeek07;
	/**
	 * 今天的数据集合
	 */
	private ArrayList<ProgramEntity> epgList;

	private RelativeLayout rlProgramInfo;

	private int index = 0;
	/**
	 * 星期一到星期日 文字
	 */
	private TextView tvWeek01;
	private TextView tvWeek02;
	private TextView tvWeek03;
	private TextView tvWeek04;
	private TextView tvWeek05;
	private TextView tvWeek06;
	private TextView tvWeek07;
	/**
	 * 星期对应的日期
	 */
	private TextView tvDate01;
	private TextView tvDate02;
	private TextView tvDate03;
	private TextView tvDate04;
	private TextView tvDate05;
	private TextView tvDate06;
	private TextView tvDate07;
	/**
	 * 星期与日期 下方的加量条
	 */
	private TextView tvLightLine01;
	private TextView tvLightLine02;
	private TextView tvLightLine03;
	private TextView tvLightLine04;
	private TextView tvLightLine05;
	private TextView tvLightLine06;
	private TextView tvLightLine07;
	/**
	 * 节目名称
	 */
	private TextView tvEpgName;
	/**
	 * 节目时间
	 */
	private TextView tvEpgTime;

	private ImageView ivPosters;
	/**
	 * 图片下载器
	 */
	protected ImageLoader imageLoader = null;
	private DisplayImageOptions optionsPoster;
	/**
	 * 节目viewPager
	 */
	private ViewPager vpSelectEpg;
	/**
	 * 当前选中的数据集合
	 */
	private List<View> selectEpgList;
	/**
	 * ViewPager适配器
	 */

	private ImageView ivChannelLogo;
	private TextView tvChannelName;

	/**
	 * 用户置顶成功标志位
	 */
	private final int DB_SET_TOP_SUCCESS = 110;
	/**
	 * 用户置顶失败
	 */
	private final int DB_SET_TOP_FAIL = -1000;
	/**
	 * 用户是否已经置顶的完成标志
	 */
	private final int DB_CHECK_TOP_RESULT_SUCCESS = 101;

	/**
	 * 用户没有置顶的完成标志
	 */
	private final int DB_CHECK_TOP_RESULT_FAIL = -101;

	private TextView epgLayoutEpgHint = null;

	private TvSetTopDAO topDao = null;
	/**
	 * 获取一个星期的时间
	 */
	private List<String> mDateList = null;
	/**
	 * 当前节目单View
	 */
	private View currentEpgView;
	/**
	 * 正在加载数据文字
	 */
	private TextView epgDownloadinghint;

	/**
	 * 当前节目是否已经顶置
	 * 
	 * @Fields isTops
	 */
	private boolean isTops = false;

	private static String MONDAY = "monday";
	private static String TUESDAY = "tuesday";
	private static String WEDNESDAY = "wednesday";
	private static String THURSDAY = "thursday";
	private static String FRIDAY = "friday";
	private static String SATURDAY = "saturday";
	private static String SUNDAY = "sunday";
	private HashMap<String, LiveEpgInfoFragment> mapFragment = new HashMap<String, LiveEpgInfoFragment>();
	/**
	 * 正在直播的位置在第几页
	 */
	private int locationId;
	/**
	 * 正在播放第几个条目
	 */
	private int isPlayLocations;
	/**
	 * 当前ViewPager的页数
	 */
	private int currentPagerNum = 0;
	/**
	 * 当前ViewPager获取焦点的位置
	 */
	private View pagerFocusPoint;

	private LiveEpgInfoActivity intance;

	/**
	 * 初始化数据
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_epg_info);
		intance = this;
		mDateList = DateUtils.date2Week();
		topDao = new TvSetTopDAO(getApplicationContext());

		CHANNEL_CODE = getIntent().getStringExtra(AppConstant.CHANNEL_CODE);
		// 台标
		CHANNEL_LOGO = getIntent().getStringExtra(AppConstant.CHANNEL_LOGO);

		CHANNEL_NAME = getIntent().getStringExtra(AppConstant.CHANNEL_NAME);
		// 获取定时任务管理类
		if (null == alarmManager) {
			alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		}
		checkChannelIsTop();// 置顶查看

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 台标地址
	 * 
	 * @return
	 */
	public String getCHANNEL_LOGO() {
		return CHANNEL_LOGO;
	}

	/**
	 * 电视台名称
	 * 
	 * @return
	 */
	public String getCHANNEL_NAME() {
		return CHANNEL_NAME;
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		imageLoader = ImageLoader.getInstance();

		optionsPoster = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.epg_image_default)
				.showImageOnFail(R.drawable.epg_image_default).resetViewBeforeLoading(false).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();

		// btnSetTop = findViewById(R.id.live_btn_set_top);
		// 节目单ViewPager
		// vpSelectEpg = (ViewPager) this.findViewById(R.id.vp_epg_select);
		// changeViewPageScroller(vpSelectEpg);
		vpSelectEpg = (ViewPager) this.findViewById(R.id.vp_epg_select);
		changeViewPageScroller(vpSelectEpg);

		/*
		 * 2014-05-22 end by maliang
		 */

		// 电台logo、电台名称
		ivChannelLogo = (ImageView) this.findViewById(R.id.iv_channel_logo);

		DisplayImageOptions logoOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.live_channel_logo_default)
				.showImageOnFail(R.drawable.live_channel_logo_default).resetViewBeforeLoading(false).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(HiveviewApplication.Bitmapconfig).considerExifParams(true).build();
		ImageLoader.getInstance().displayImage(CHANNEL_LOGO, ivChannelLogo, logoOptions);

		tvChannelName = (TextView) this.findViewById(R.id.tv_channel_name);
		tvChannelName.setText(CHANNEL_NAME);

		// 没有数据时的相关提醒
		epgLayoutEpgHint = (TextView) findViewById(R.id.epg_layout_epghint);
		// 正在下载数据文字提示
		epgDownloadinghint = (TextView) findViewById(R.id.epg_layout_download_loading);

		// 节目单中间线
		// ivEpgCenterLine = (ImageView) findViewById(R.id.iv_epg_center_line);

		// 初始化当前选中的节目单数据集合
		selectEpgList = new ArrayList<View>();
		// mAdapter = new ViewPageAdapter(selectEpgList);
		// vpSelectEpg.setAdapter(mAdapter);
		// mAdapter = new ViewPageAdapter(selectEpgList);
		// vpSelectEpg.setAdapter(mAdapter);
		/*
		 * 2014-05-22 start by maliang
		 */
		// vpSelectEpg.setOnPageChangeListener(new OnPageChangeListener() {
		// @Override
		// public void onPageSelected(int arg0) {
		// // TODO Auto-generated method stub
		// if (currentPage > arg0) {
		// EpgPageItem item = (EpgPageItem) selectEpgList.get(arg0);
		// item.findViewById(R.id.ll_item_07).findViewById(R.id.iv_focus_point).requestFocus();
		//
		// // if (AppUtil.nowDateTimeIsAferBoolean(entity.getDate() +
		// // " " + entity.getStart_time())
		// /*
		// * 当节目单页数往回翻的时候，让焦点落在前一页的最后一个节目上
		// */
		// }
		// currentPage = arg0;
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// 星期的LinearLayout
		llWeek01 = (LinearLayout) this.findViewById(R.id.ll_week_01);
		llWeek02 = (LinearLayout) this.findViewById(R.id.ll_week_02);
		llWeek03 = (LinearLayout) this.findViewById(R.id.ll_week_03);
		llWeek04 = (LinearLayout) this.findViewById(R.id.ll_week_04);
		llWeek05 = (LinearLayout) this.findViewById(R.id.ll_week_05);
		llWeek06 = (LinearLayout) this.findViewById(R.id.ll_week_06);
		llWeek07 = (LinearLayout) this.findViewById(R.id.ll_week_07);

		// start author:zhangpengzhan 控制第一个和最后一个焦点走向
		llWeek01.setNextFocusLeftId(R.id.ll_week_01);
		llWeek07.setNextFocusRightId(R.id.ll_week_07);
		// end

		// 星期的名称
		tvWeek01 = (TextView) this.findViewById(R.id.tv_week_01);
		tvWeek02 = (TextView) this.findViewById(R.id.tv_week_02);
		tvWeek03 = (TextView) this.findViewById(R.id.tv_week_03);
		tvWeek04 = (TextView) this.findViewById(R.id.tv_week_04);
		tvWeek05 = (TextView) this.findViewById(R.id.tv_week_05);
		tvWeek06 = (TextView) this.findViewById(R.id.tv_week_06);
		tvWeek07 = (TextView) this.findViewById(R.id.tv_week_07);
		// 星期对应的日期
		tvDate01 = (TextView) this.findViewById(R.id.tv_date_01);
		tvDate02 = (TextView) this.findViewById(R.id.tv_date_02);
		tvDate03 = (TextView) this.findViewById(R.id.tv_date_03);
		tvDate04 = (TextView) this.findViewById(R.id.tv_date_04);
		tvDate05 = (TextView) this.findViewById(R.id.tv_date_05);
		tvDate06 = (TextView) this.findViewById(R.id.tv_date_06);
		tvDate07 = (TextView) this.findViewById(R.id.tv_date_07);
		// 星期与日期下的选中图片
		tvLightLine01 = (TextView) this.findViewById(R.id.tv_light_line_01);
		tvLightLine02 = (TextView) this.findViewById(R.id.tv_light_line_02);
		tvLightLine03 = (TextView) this.findViewById(R.id.tv_light_line_03);
		tvLightLine04 = (TextView) this.findViewById(R.id.tv_light_line_04);
		tvLightLine05 = (TextView) this.findViewById(R.id.tv_light_line_05);
		tvLightLine06 = (TextView) this.findViewById(R.id.tv_light_line_06);
		tvLightLine07 = (TextView) this.findViewById(R.id.tv_light_line_07);

		// 节目名称 时间
		tvEpgName = (TextView) this.findViewById(R.id.tv_epg_name);
		tvEpgTime = (TextView) this.findViewById(R.id.tv_epg_time);

		// 节目的选中时节目预览 信息与图片
		rlProgramInfo = (RelativeLayout) this.findViewById(R.id.rl_epg_info);
		ivPosters = (ImageView) this.findViewById(R.id.iv_posters);
		/* start by guosngsheng 初始化隐藏海报图 */
		ivPosters.setVisibility(View.INVISIBLE);
		/* end by guosongsheng */

		mProgressDialog = (ProgressDialog) this.findViewById(R.id.iv_hiveview_loading);
		rlContainer = (RelativeLayout) this.findViewById(R.id.rl_container);

		// 添加焦点事件
		llWeek01.setOnFocusChangeListener(mOnFocusChangeListener);
		llWeek02.setOnFocusChangeListener(mOnFocusChangeListener);
		llWeek03.setOnFocusChangeListener(mOnFocusChangeListener);
		llWeek04.setOnFocusChangeListener(mOnFocusChangeListener);
		llWeek05.setOnFocusChangeListener(mOnFocusChangeListener);
		llWeek06.setOnFocusChangeListener(mOnFocusChangeListener);
		llWeek07.setOnFocusChangeListener(mOnFocusChangeListener);

		// 置顶按钮设置点击事件
		// btnSetTop.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// submitRequest(new Runnable() {
		// @Override
		// public void run() {
		// isTops = true;
		// topDao.insert(new StringEntity(CHANNEL_CODE));
		// handler.sendEmptyMessage(DB_SET_TOP_SUCCESS);
		// // sendBroadcast(new
		// Intent("com.hiveview.tv.TV_CHANNEL_SET_TOP_ACTION"));
		// }
		// });
		// }
		// });

		/* start by guosongsheng 添加构造方法参数 是否已经置顶 在fragment中根据此参数判断是否显示置顶按钮 */
		mapFragment.put(MONDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		mapFragment.put(TUESDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		mapFragment.put(WEDNESDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		mapFragment.put(THURSDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		mapFragment.put(FRIDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		mapFragment.put(SATURDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		mapFragment.put(SUNDAY, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		/* end by guosongsheng */
		initData();

	}

	/**
	 * 检测当前的电视台是否被置顶
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private void checkChannelIsTop() {
		submitRequest(new Runnable() {
			@Override
			public void run() {
				List<StringEntity> entities = (List<StringEntity>) topDao.query(null, TvSetTopDAO.CHANNEL_CODE + " = ?",
						new String[] { CHANNEL_CODE }, null);
				/*
				 * start author:zhangpengzhan
				 * 添加一个标签在刷新电视节目单信息的时候不至于吧已经隐藏的顶置给显示出来
				 */
				if (entities.size() > 0) {// 已经被置顶的
					// 检测数据库是否包含当前的电视节目
					Log.d("LiveEpgInfoActivity->checkChannelsIsTop::", "i am alreadly here");
					isTops = true;
					handler.sendEmptyMessage(DB_CHECK_TOP_RESULT_SUCCESS);
				} else {// 没有被置顶的
					Log.d("LiveEpgInfoActivity->checkChannelsIsTop::", "i am not top");
					isTops = false;
					handler.sendEmptyMessage(DB_CHECK_TOP_RESULT_FAIL);
				}
			}
		});

	}

	/**
	 * 填充数据
	 * 
	 * @param msgWhat
	 */
	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		// 置顶成功
		case DB_SET_TOP_SUCCESS:
			// 置顶成功后焦点移到对应的节目单上
			currentEpgView.requestFocus();
			// btnSetTop.setVisibility(View.GONE);
			alert(getResources().getString(R.string.alert_place_top));
			break;
		// 置顶失败
		case DB_SET_TOP_FAIL:
			break;
		case DB_CHECK_TOP_RESULT_SUCCESS:
			// 已经置顶 隐藏置顶按钮
			// btnSetTop.setVisibility(View.INVISIBLE);
			init();
			mProgressDialog.setVisibility(View.GONE);
			break;
		case DB_CHECK_TOP_RESULT_FAIL:
			// 未置顶 显示置顶按钮
			// btnSetTop.setVisibility(View.VISIBLE);
			init();
			mProgressDialog.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	/**
	 * 第一次初始化显示数据 根据当前的时间显示对应的数据
	 */
	private void initData() {
		// 将对应的日期转化当前一星期
		List<String> list = DateUtils.dateToWeek();
		// 获取当前时间
		String currentWeek = DateUtils.getCurrentDate();
		int count = list.size();
		// 根据当前日期初始化显示数据
		for (int i = 0; i < count; i++) {
			if (currentWeek.equals(list.get(i))) {
				index = i;
			}
			switch (i) {
			case 0:
				tvDate01.setText(list.get(i));
				break;
			case 1:
				tvDate02.setText(list.get(i));
				break;
			case 2:
				tvDate03.setText(list.get(i));
				break;
			case 3:
				tvDate04.setText(list.get(i));
				break;
			case 4:
				tvDate05.setText(list.get(i));
				break;
			case 5:
				tvDate06.setText(list.get(i));
				break;
			case 6:
				tvDate07.setText(list.get(i));
				break;
			default:
				break;
			}
		}
		/* start by guosongsheng 没有数据时当天的TAB也要获取焦点new蜂巢2.0 NEWFC-672 */

		// 初始化当前日志TAB的状态
		changeCurrentTabStatus();
		/* end by guosongsheng 没有数据时当天的TAB也要获取焦点new蜂巢2.0 NEWFC-672 */
		getEpgList();// 获取节目数据
	}

	/***
	 * maliang 2014-04-26:当获取的节目列表的第一个节目的开始时间的小时是前一天的20或21或22或23时的时候，去掉这个节目
	 * 
	 * @param list
	 */
	private void removeFirstProgramEntity(ArrayList<ProgramEntity> list) {
		if (list != null && list.size() > 0) {
			ProgramEntity programEntity = list.get(0);
			String startTime = programEntity.getStart_time();
			if (startTime != null && startTime.length() > 2) {
				int startHour = Integer.parseInt(startTime.split(":")[0]);
				if (startHour == 20 || startHour == 21 || startHour == 22 || startHour == 23) {
					list.remove(0);
				}
			}
		}
	}

	/**
	 * 根据频道的得到节目单 只加载当天的数据
	 */
	private void getEpgList() {
		submitRequest(new Runnable() {
			@Override
			public void run() {
				mDateList = DateUtils.date2Week();
				String currentDate = getCurrentDate();
				epgList = (ArrayList<ProgramEntity>) new HiveTVService().getProgramsByChannel(CHANNEL_CODE, currentDate + " 00:00", currentDate
						+ " 24:00");
				if (epgList == null || epgList.size() == 0) {
					handler.sendEmptyMessage(RQUEST_DATA_FAIL);
					return;
				} else {
					// 当获取的节目列表的第一个节目的开始时间的小时是前一天的20或21或22或23时的时候，去掉这个节目
					removeFirstProgramEntity(epgList);
					// 发送消息 加载成功
					handler.sendEmptyMessage(RQUEST_DATA_SUCCESS);
				}
				if (null != epgList && epgList.size() != 0) {
					/* start by guosongsheng 优化代码，将修改TAB状态的代码提出来 */
					// 更改页面展示今天的节目单
					changeCurrentTabStatus();
					/* end by guosongsheng 优化代码，将修改TAB状态的代码提出来 */
				}
				// // for (int i = 0; i < epgList.size(); i++) {
				// // ProgramEntity e = epgList.get(i);
				// // if (AppUtil.nowDateTimeIsAferBoolean(e.getDate() + " " +
				// e.getStart_time())
				// // && !AppUtil.nowDateTimeIsAferBoolean(e.getDate() + " " +
				// e.getEnd_time())) {
				// //
				// // float cont = i / 7;
				// // locationId = (int) Math.rint(cont);
				// // isPlayLocations = i % 7;
				// // }
				// // }
				//
				// for (int i = 0; i < epgList.size(); i++) {
				// ProgramEntity e = epgList.get(i);
				// if (AppUtil.nowDateTimeIsAferBoolean(e.getDate() + " " +
				// e.getStart_time())
				// && !AppUtil.nowDateTimeIsAferBoolean(e.getDate() + " " +
				// e.getEnd_time())) {
				//
				// float cont = i / 7;
				// locationId = (int) Math.rint(cont);
				// isPlayLocations = i % 7;
				// }
				// }
				//
			}
		});
	}

	/**
	 * 修改TAB的状态
	 */
	private void changeCurrentTabStatus() {
		// 更改页面展示今天的节目单
		LiveEpgInfoActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "changeCurrentTabStatus   ------------------------------> " + index);
				// 修改当前选中TAB的状态
				switch (index) {
				// start author:zhangpengzhan 添加view requset focus
				// 把焦点真实的指向该对应的view
				case 0:
					hasFocusPoint(tvWeek01, tvDate01, tvLightLine01);
					attachFragments(MONDAY, mDateList.get(0), 1);
					llWeek01.requestFocus();
					Log.i(TAG, "当前的时间=====1");
					break;
				case 1:
					hasFocusPoint(tvWeek02, tvDate02, tvLightLine02);
					attachFragments(TUESDAY, mDateList.get(1), 2);
					llWeek02.requestFocus();
					Log.i(TAG, "当前的时间=====2");
					break;
				case 2:
					hasFocusPoint(tvWeek03, tvDate03, tvLightLine03);
					attachFragments(WEDNESDAY, mDateList.get(2), 3);
					llWeek03.requestFocus();
					Log.i(TAG, "当前的时间=====3");
					break;
				case 3:
					hasFocusPoint(tvWeek04, tvDate04, tvLightLine04);
					attachFragments(THURSDAY, mDateList.get(3), 4);
					llWeek04.requestFocus();
					Log.i(TAG, "当前的时间=====4" + mDateList.get(3));
					break;
				case 4:
					hasFocusPoint(tvWeek05, tvDate05, tvLightLine05);
					attachFragments(FRIDAY, mDateList.get(4), 5);
					llWeek05.requestFocus();
					Log.i(TAG, "当前的时间=====5");
					break;
				case 5:
					hasFocusPoint(tvWeek06, tvDate06, tvLightLine06);
					attachFragments(SATURDAY, mDateList.get(5), 6);
					llWeek06.requestFocus();
					Log.i(TAG, "当前的时间=====6");
					break;
				case 6:
					hasFocusPoint(tvWeek07, tvDate07, tvLightLine07);
					attachFragments(SUNDAY, mDateList.get(6), 7);
					llWeek07.requestFocus();
					Log.i(TAG, "当前的时间=====7");
					break;
				}
				// end
			}
		});
	}

	/**
	 * 设置当前的ViewPager
	 * 
	 * @param index
	 * @param findViewById
	 */
	public void setCurrentPager(int index, View findViewById) {
		currentPagerNum = index;
		pagerFocusPoint = findViewById;
	}

	/**
	 * 焦点变换事件 切换日期tab时显示对应日期的节目单
	 */
	// private boolean booleanFours = true;
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {

				if (v == llWeek01) {
					// if (booleanFours) {
					// booleanFours = false;
					// } else if (booleanFours == false) {
					hasFocusPoint(tvWeek01, tvDate01, tvLightLine01);
					attachFragments(MONDAY, mDateList.get(0), 1);
					// }
				} else {
					lostaFocusPoint(tvWeek01, tvDate01, tvLightLine01);
				}
				if (v == llWeek02) {
					hasFocusPoint(tvWeek02, tvDate02, tvLightLine02);
					attachFragments(TUESDAY, mDateList.get(1), 2);
				} else {
					lostaFocusPoint(tvWeek02, tvDate02, tvLightLine02);
				}
				if (v == llWeek03) {
					hasFocusPoint(tvWeek03, tvDate03, tvLightLine03);
					attachFragments(WEDNESDAY, mDateList.get(2), 3);
				} else {
					lostaFocusPoint(tvWeek03, tvDate03, tvLightLine03);
				}
				if (v == llWeek04) {
					hasFocusPoint(tvWeek04, tvDate04, tvLightLine04);
					attachFragments(THURSDAY, mDateList.get(3), 4);
				} else {
					lostaFocusPoint(tvWeek04, tvDate04, tvLightLine04);
				}
				if (v == llWeek05) {
					hasFocusPoint(tvWeek05, tvDate05, tvLightLine05);
					attachFragments(FRIDAY, mDateList.get(4), 5);
				} else {
					lostaFocusPoint(tvWeek05, tvDate05, tvLightLine05);
				}
				if (v == llWeek06) {
					hasFocusPoint(tvWeek06, tvDate06, tvLightLine06);
					attachFragments(SATURDAY, mDateList.get(5), 6);
				} else {
					lostaFocusPoint(tvWeek06, tvDate06, tvLightLine06);
				}
				if (v == llWeek07) {
					hasFocusPoint(tvWeek07, tvDate07, tvLightLine07);
					attachFragments(SUNDAY, mDateList.get(6), 7);
				} else {
					lostaFocusPoint(tvWeek07, tvDate07, tvLightLine07);
				}
			}
		}

	};

	/**
	 * 获取焦点时设置文字颜色
	 * 
	 * @param tvWeek
	 *            当前星期TextView
	 * @param tvDate
	 *            当前日期TextView
	 * @param tvLightLine
	 *            当前选中加亮图片
	 */
	private void hasFocusPoint(TextView tvWeek, TextView tvDate, TextView tvLightLine) {
		tvWeek.setTextColor(Color.parseColor("#FF8B00"));
		tvDate.setTextColor(Color.parseColor("#FF8B00"));
		tvLightLine.setVisibility(View.VISIBLE);
	}

	/**
	 * 切换到其他Tab时设置文字颜色
	 * 
	 * @param tvWeek
	 *            当前星期TextView
	 * @param tvDate
	 *            当前日期TextView
	 * @param tvLightLine
	 *            当前选中加亮图片
	 */
	private void lostaFocusPoint(TextView tvWeek, TextView tvDate, TextView tvLightLine) {
		tvWeek.setTextColor(Color.parseColor("#FFFFFF"));
		tvDate.setTextColor(Color.parseColor("#FFFFFF"));
		tvLightLine.setVisibility(View.INVISIBLE);
	}

	// 反射机制 控制 viewpager滑动时间 为800
	private void changeViewPageScroller(ViewPager viewPager) {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			FixedSpeedScroller scroller;
			scroller = new FixedSpeedScroller(this, new AccelerateDecelerateInterpolator());
			mField.set(viewPager, scroller);
		} catch (Exception e) {
		}

	}

	/**
	 * 滑动样式
	 */
	class FixedSpeedScroller extends Scroller {
		private int mDuration = 400;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, mDuration);
		}

		public void setmDuration(int time) {
			mDuration = time;
		}

		public int getmDuration() {
			return mDuration;
		}

	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	private String getCurrentDate() {
		try {
			String currentDate = DateUtils.dateToString(new Date(), "yyyy-MM-dd");
			return currentDate;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @Title: LiveEpgInfoActivity
	 * @author:周一川
	 * @Description: TODO
	 * @param tag
	 * @param dataType
	 */
	FragmentTransaction ft;
	LiveEpgInfoFragment fragment;
	private boolean isChange = false;

	@SuppressLint("NewApi")
	public void attachFragments(String tag, String dataType, int tags) {

		/** 碎片管理 */
		fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Iterator<Entry<String, LiveEpgInfoFragment>> entryKeyIterator = mapFragment.entrySet().iterator();
		while (entryKeyIterator.hasNext()) {
			Entry<String, LiveEpgInfoFragment> e = entryKeyIterator.next();
			LiveEpgInfoFragment value = e.getValue();
			if (null != value) {
				ft.detach(value);
			}
		}

		Log.d(TAG, "attachFragments:::" + ivPosters);
		// start author:zhangpengzhan
		// 修改海报图更换图片时机
		if (isChange) {
			ivPosters.setImageResource(R.drawable.epg_image_default);
		} else {
			isChange = true;
		}
		// end
		fragment = mapFragment.get(tag);
		fragment.setDateType(dataType);
		fragment.setTage(tags);

		fragment.setItemViewKeyListener(new ItemViewKeyListener(tags));
		fragment.setItemViewFocusCallBackListener(new ItemFocusLisener());
		if (null != fragment) {
			ft.add(R.id.fragment, fragment, "");
			ft.attach(fragment);
		}
		if (!this.isDestroyed()) {
			ft.commitAllowingStateLoss();
		}
	}

	/**
	 * @ClassName: ItemFocusLisener
	 * @Description: 焦点监听
	 * @author: zhangpengzhan
	 * @date 2014年8月7日 上午11:15:14
	 * 
	 */
	class ItemFocusLisener implements ItemViewFocusCallBackListener {

		public void onCallBackFocusListener(View v, boolean has) {
			if (null == imageLoader) {
				imageLoader = ImageLoader.getInstance();
			}
			if (has) {
				ProgramEntity entity = (ProgramEntity) v.getTag();
				if (entity != null) {
					tvEpgName.setVisibility(View.VISIBLE);
					/* start by guosngsheng 获取焦点时显示海报图 与名称 */
					ivPosters.setVisibility(View.VISIBLE);
					tvEpgName.setText(entity.getName());
					// tvEpgTime.setText(entity.getStart_time());
					imageLoader.displayImage(String.valueOf(entity.getWiki_cover()), ivPosters, optionsPoster);
				}
			} else {
				ivPosters.setVisibility(View.GONE);
				tvEpgName.setVisibility(View.GONE);
				/* end by guosngsheng 失去取焦点时隐藏海报图 与名称 */
			}
		}

	}

	/**
	 * 关闭activity时移除fragment
	 * 
	 * @author 郝泽泞
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		for (Map.Entry<String, LiveEpgInfoFragment> entry : mapFragment.entrySet()) {
			if (null != entry && !this.isDestroyed()) {
				fm.beginTransaction().remove(entry.getValue()).commitAllowingStateLoss();
			}
		}
		super.onDestroy();
	}

	/**
	 * ItemView上按键事件回调接口 按菜单键
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ItemViewKeyListener implements CallBackItemViewKeyListener {
		private int tage;

		public ItemViewKeyListener(int tage) {
			this.tage = tage;
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
				isChange = false;
				switch (tage) {
				case 1:
					v.setNextFocusUpId(R.id.ll_week_01);
					break;
				case 2:
					v.setNextFocusUpId(R.id.ll_week_02);
					break;
				case 3:
					v.setNextFocusUpId(R.id.ll_week_03);
					break;
				case 4:
					v.setNextFocusUpId(R.id.ll_week_04);
					break;
				case 5:
					v.setNextFocusUpId(R.id.ll_week_05);
					break;
				case 6:
					v.setNextFocusUpId(R.id.ll_week_06);
					break;
				case 7:
					v.setNextFocusUpId(R.id.ll_week_07);
					break;
				default:
					break;
				}
			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				v.setNextFocusDownId(LiveEpgInfoFragment.BUTTON_ID);
			}
			// 控制最后一个item向右的按键走向
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (null != v.getTag()) {

					if (LiveEpgInfoFragment.getEpgList().indexOf(v.getTag()) == LiveEpgInfoFragment.getEpgList().size() - 1) {
						return true;
					}
				}
			}
			return false;
		}

	}

	public static LinearLayout currentView;
	public static ProgramEntity currententity;

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("top".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "置顶本电视台", intent);
					fragment.mvTop();
				} else if ("Monday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期一", intent);
						hasFocusPoint(tvWeek01, tvDate01, tvLightLine01);
						attachFragments(MONDAY, mDateList.get(0), 1);
						llWeek01.requestFocus();
						Log.i(TAG, "当前的时间=====1");
					}
				} else if ("Tuesday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期二", intent);
						hasFocusPoint(tvWeek02, tvDate02, tvLightLine02);
						attachFragments(TUESDAY, mDateList.get(1), 2);
						llWeek02.requestFocus();
						Log.i(TAG, "当前的时间=====2");
					}
				} else if ("Wednesday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期三", intent);
						hasFocusPoint(tvWeek03, tvDate03, tvLightLine03);
						attachFragments(WEDNESDAY, mDateList.get(2), 3);
						llWeek03.requestFocus();
						Log.i(TAG, "当前的时间=====3");

					}
				} else if ("Thursday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期四", intent);
						hasFocusPoint(tvWeek04, tvDate04, tvLightLine04);
						attachFragments(THURSDAY, mDateList.get(3), 4);
						llWeek04.requestFocus();
						Log.i(TAG, "当前的时间=====4" + mDateList.get(3));
					}
				} else if ("Friday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期五", intent);
						hasFocusPoint(tvWeek05, tvDate05, tvLightLine05);
						attachFragments(FRIDAY, mDateList.get(4), 5);
						llWeek05.requestFocus();
						Log.i(TAG, "当前的时间=====5");

					}
				} else if ("Saturday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期六", intent);
						hasFocusPoint(tvWeek06, tvDate06, tvLightLine06);
						attachFragments(SATURDAY, mDateList.get(5), 6);
						llWeek06.requestFocus();
						Log.i(TAG, "当前的时间=====6");

					}
				} else if ("Sunday".equals(command)) {
					if (null != epgList && epgList.size() != 0) {
						HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, "星期日", intent);
						hasFocusPoint(tvWeek07, tvDate07, tvLightLine07);
						attachFragments(SUNDAY, mDateList.get(6), 7);
						llWeek07.requestFocus();
						Log.i(TAG, "当前的时间=====7");

					}
				} else if ("join".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveEpgInfoActivity.this, getResources().getString(R.string.alert_join_liveremind), intent);
					currentView = fragment.getEpgLayout();
					currententity = fragment.getEntity();
					if (currententity != null && currentView != null) {
						LogUtil.info("currententity==>" + currententity.getName());
						OnliveTipsEntity onliveTipsEntity = new OnliveTipsEntity();
						onliveTipsEntity.setDate(currententity.getDate());
						onliveTipsEntity.setEnd_time(currententity.getEnd_time());
						onliveTipsEntity.setHasvideo(currententity.getHasvideo());
						onliveTipsEntity.setName(currententity.getName());
						onliveTipsEntity.setSource(currententity.getSource());
						onliveTipsEntity.setStart_time(currententity.getStart_time());
						onliveTipsEntity.setTags(currententity.getTags());
						onliveTipsEntity.setWiki_cover(currententity.getWiki_cover());
						onliveTipsEntity.setWiki_id(currententity.getWiki_id());
						onliveTipsEntity.setTelevisionLogoUrl(getCHANNEL_LOGO().toString());
						onliveTipsEntity.setTelevisionName(getCHANNEL_NAME().toString());
						StringBuffer time = new StringBuffer();
						time.append(currententity.getDate());
						time.append(" ");
						time.append(currententity.getStart_time());
						try {
							onliveTipsEntity.setTip_time(String.valueOf(DateUtils.stringToLong(time.toString(), "yyyy-MM-dd HH:mm")));

							Log.d(TAG, "==直播提醒的台标==" + this.getCHANNEL_LOGO().toString());
							Log.d(TAG, "==直播提醒的台ming==" + this.getCHANNEL_NAME().toString());
							if (null == dao)
								dao = new OnliveTipsDAO(this);
							// 需要填充到直播提醒列表的 数据
							if (!AppUtil.nowDateTimeIsAferBoolean(currententity.getDate() + " " + currententity.getStart_time())) {
								if (dao.isExist(new String[] { getCHANNEL_NAME().toString(), currententity.getDate(), currententity.getStart_time() })) {
									dao.delete("television_logo_name = ? and date = ? and start_time = ?", new String[] {
											getCHANNEL_NAME().toString(), currententity.getDate(), currententity.getStart_time() });
									StringBuffer buffer = new StringBuffer();
									buffer.append(currententity.getDate());
									buffer.append(" ");
									buffer.append(currententity.getStart_time());

									long startTime;
									startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
									cancelAlar(startTime);
									ToastUtils.alert(this, getResources().getString(R.string.alert_cancle_liveremind));
									setCurrentPager(index, currentView.findViewById(R.id.iv_focus_point));
									((ImageView) currentView.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.GONE);
									((View) currentView.findViewById(R.id.iv_logo)).setVisibility(View.GONE);
									return;
								}

								setCurrentPager(index, currentView.findViewById(R.id.iv_focus_point));
								((ImageView) currentView.findViewById(R.id.iv_onlive_logo1)).setVisibility(View.VISIBLE);
								((View) currentView.findViewById(R.id.iv_logo)).setVisibility(View.VISIBLE);

								dao.insert(onliveTipsEntity);
								ToastUtils.alert(this, getResources().getString(R.string.alert_join_liveremind));

								Log.d(TAG, "定时发消息");
								// 制作定时任务
								Intent intent1 = new Intent();
								Bundle bundle = new Bundle();
								bundle.putString("name", currententity.getName());
								bundle.putString("wiki_cover", String.valueOf(currententity.getWiki_cover()));
								bundle.putString("date", currententity.getDate());
								bundle.putString("start_time", currententity.getStart_time());
								bundle.putString("end_time", currententity.getEnd_time());
								bundle.putString("wiki_id", currententity.getWiki_id());
								bundle.putString("logo", getCHANNEL_LOGO().toString());
								bundle.putString("logoName", getCHANNEL_NAME().toString());
								intent1.putExtras(bundle);
								intent1.setClass(this, OnliveTipService.class);
								StringBuffer buffer = new StringBuffer();
								buffer.append(currententity.getDate());
								buffer.append(" ");
								buffer.append(currententity.getStart_time());

								long startTime;
								startTime = DateUtils.stringToLong(buffer.toString(), "yyyy-MM-dd HH:mm");
								pendingIntent = PendingIntent.getService(this, (int) startTime, intent1, 0);
								alarmManager.set(AlarmManager.RTC, startTime - seconds, pendingIntent);
								Log.d(TAG, "long time ==" + System.currentTimeMillis());
							} else {
								ToastUtils.alert(this, getResources().getString(R.string.alert_no_backsee));
							}
						} catch (Exception e) {
							LogUtil.info(this + "==" + e.getMessage());

							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 取消当前的定时任务 参数是 日期 时间
	 * 
	 * @param date
	 * @param start_Time
	 */
	/**
	 * 定时任务的管理类
	 */
	private AlarmManager alarmManager;
	/**
	 * 定时任务要触发的广播
	 */
	private PendingIntent pendingIntent;

	/**
	 * 数据库类
	 */
	private OnliveTipsDAO dao;
	/**
	 * 直播提醒的提前量
	 */
	private int seconds = 60 * 1000;

	public void cancelAlar(long startTime) {
		// 获取定时任务管理类
		if (null == alarmManager)
			alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		// 制作定时任务
		Intent intent = new Intent();
		intent.setClass(this, OnliveTipService.class);

		pendingIntent = PendingIntent.getService(this, (int) startTime, intent, 0);
		alarmManager.cancel(pendingIntent);

	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.LiveEpgInfoActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根

		commands.put("top", new String[] { "置顶本台到电视页", "置顶本电视台", "置顶", "置顶这个电视台" });
		commands.put("Monday", new String[] { "星期一" });
		commands.put("Tuesday", new String[] { "星期二" });
		commands.put("Wednesday", new String[] { "星期三" });
		commands.put("Thursday", new String[] { "星期四" });
		commands.put("Friday", new String[] { "星期五" });
		commands.put("Saturday", new String[] { "星期六" });
		commands.put("Sunday", new String[] { "星期日" });
		commands.put("join", new String[] { "加入提醒", "增加直播提醒", "直播提醒", "添加到直播提醒" });
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
