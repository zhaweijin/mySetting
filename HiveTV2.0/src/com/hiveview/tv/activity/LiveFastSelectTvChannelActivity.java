package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.factory.TvChannelPageViewFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.KeyMappingHashMapUtil;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.utils.ThreeLineFocusStrategy;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.TvChannelPageViewItemView;
import com.hiveview.tv.view.onlive.OnliveSelect;
import com.hiveview.tv.view.television.voicecontrol.TVCodeBroadcastReciver;
import com.paster.util.JsonUtil;

@SuppressLint("HandlerLeak")
public class LiveFastSelectTvChannelActivity extends BaseActivity {
	private static final String TAG = "LiveFastSelectTvChannelActivity";
	/**
	 * 每页的个数
	 */
	private static final int PAGE_SIZE = 12;
	/**
	 * 央视 tab FrameLayout
	 */
	private View vCctvChannel;
	/**
	 * 卫视 tab FrameLayout
	 */
	private View vTvChannel;
	/**
	 * 本地 tab FrameLayout
	 */
	private View vLocalChannel;
	/**
	 * 高清 tab FrameLayout
	 */
	private View vHdChannel;
	/**
	 * 收费 tab FrameLayout
	 */
	private View vPayChannel;

	/**
	 * 电视Tab的数组
	 */
	private View tabViews[] = null;
	/**
	 * 央视 tab名称 TextView
	 */
	private TextView tvCCTV;
	/**
	 * 卫视 tab名称 TextView
	 */
	private TextView tvTV;
	/**
	 * 本地 tab名称 TextView
	 */
	private TextView tvLocal;
	/**
	 * 高清频道 tab名称 TextView
	 */
	private TextView tvHD;
	/**
	 * 收费频道 tab名称 TextView
	 */
	private TextView tvPay;

	/**
	 * 频道 tab名称 数组
	 */
	private TextView tabTextViews[] = null;
	/**
	 * 所有tab电台的总数 (%s个电视台)
	 */
	// private TextView tvChannelsCount;
	/**
	 * 等待条
	 */
	private ProgressDialog mProgressDialog;
	/**
	 * 主容器
	 */
	private RelativeLayout rlContainer;

	/**
	 * 央视 ViewPager
	 */
	private HivePreloadViewPager cctvViewPager;
	/**
	 * 卫视 ViewPager
	 */
	private HivePreloadViewPager tvViewPager;
	/**
	 * 本地 ViewPager
	 */
	private HivePreloadViewPager localViewPager;
	/**
	 * 高清频道 ViewPager
	 */
	private HivePreloadViewPager hdViewPager;
	/**
	 * 收费频道 ViewPager
	 */
	private HivePreloadViewPager payViewPager;
	/**
	 * 672720535 ViewPager数组
	 */
	private HivePreloadViewPager[] viewPagers;
	/**
	 * 央视ViewPager Adapter
	 */
	private HivePagerAdapter mCctvAdapter = null;
	/**
	 * 卫视ViewPager Adapter
	 */
	private HivePagerAdapter tvAdapter = null;
	/**
	 * 本地ViewPager Adapter
	 */
	private HivePagerAdapter localAdapter = null;
	/**
	 * 高清频道ViewPager Adapter
	 */
	private HivePagerAdapter hdAdapter = null;
	/**
	 * 收费频道ViewPager Adapter
	 */
	private HivePagerAdapter payAdapter = null;

	/**
	 * 访问央视数据接口成功
	 */
	private final int REQUEST_CCTV_CHANNEL_SUCCESS = 1000;
	/**
	 * 访问央视数据接口失败
	 */

	private final int REQUEST_CCTV_CHANNEL_FAIL = -1000;
	/**
	 * 访问卫视数据接口成功
	 */
	private final int REQUEST_TV_CHANNEL_SUCCESS = 1001;
	/**
	 * 访问卫视数据接口失败
	 */

	private final int REQUEST_TV_CHANNEL_FAIL = -1001;
	/**
	 * 访问本地数据接口成功
	 */
	private final int REQUEST_LOCAL_CHANNEL_SUCCESS = 1002;
	/**
	 * 访问本地数据接口失败
	 */
	private final int REQUEST_LOCAL_CHANNEL_FAIL = -1002;

	/**
	 * 访问高清频道数据接口成功
	 */
	private final int REQUEST_HD_CHANNEL_SUCCESS = 1003;
	/**
	 * 访问高清频道数据接口失败
	 */
	private final int REQUEST_HD_CHANNEL_FAIL = -1003;

	/**
	 * 访问付费频道数据接口成功
	 */
	private final int REQUEST_PAY_CHANNEL_SUCCESS = 1004;
	/**
	 * 访问付费频道数据接口失败
	 */
	private final int REQUEST_PAY_CHANNEL_FAIL = -1004;

	/**
	 * 央视数据集合
	 */
	private List<ChannelEntity> cctvChannelList;
	/**
	 * 卫视数据集合
	 */
	private List<ChannelEntity> tvChannelList;
	/**
	 * 本地数据集合
	 */
	private List<ChannelEntity> localChannelList;
	/**
	 * 高清频道数据集合
	 */
	private List<ChannelEntity> hdChannelList;
	/**
	 * 付费数据集合
	 */
	private List<ChannelEntity> payChannelList;

	/**
	 * 电台个数
	 */
	private int mChannelsCount = 0;

	/**
	 * 从ViewPager中的ItemView上往上按键焦点落到Id为mUpFocusId的View上
	 */
	private int mUpFocusId = 0;
	/**
	 * 右翻页图片
	 */
	private View ivPagerRight;
	/**
	 * 左翻页图片
	 */
	private View ivPagerLeft;
	/**
	 * 当前页ViewPager的位置
	 */
	private int currentPageIndex = 0;
	/**
	 * 当前VIewpager的页数
	 */
	private int currentPageCount = 0;
	/**
	 * 当前ViewPager的位置
	 */
	private int mShowViewPagerIndex = 0;

	/**
	 * 每一个ViewPager中数据的个数
	 */
	private int[] pagerCounts = new int[] { 0, 0, 0, 0, 0 };
	/**
	 * 焦点移动到下一页的标志
	 */
	private boolean isToNext = false;
	/**
	 * 科大讯飞ok键的keyCode
	 */
	private static int KEYCODE = 66;

	private TVCodeBroadcastReciver receiver;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_fast_select_television);
		// 代码注册一个广播
		receiver = new TVCodeBroadcastReciver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstant.SWITCH_CHANNEL);
		registerReceiver(receiver, filter);

		init();
		// 隐藏控件
		mProgressDialog.setVisibility(View.VISIBLE);
		rlContainer.setVisibility(View.INVISIBLE);

		/**
		 * 初始化每个tab的电台个数 “..”
		 */
		tvCCTV.setText(String.format(getString(R.string.cctv_count), ".."));
		tvTV.setText(String.format(getString(R.string.satellite_tv_count), ".."));
		tvLocal.setText(String.format(getString(R.string.local_count), ".."));
		tvHD.setText(String.format(getString(R.string.hd_channel_count), ".."));
		tvPay.setText(String.format(getString(R.string.subscription_channel_count), ".."));
		// tvChannelsCount.setText(String.format(getString(R.string.television_count),
		// mChannelsCount));
		// 请求央视频道信息
		requestCctvChannels();
	}

	private void init() {
		// (%s个电视台)
		// tvChannelsCount = (TextView) findViewById(R.id.tv_television_count);
		// 央视 tab名称 TextView
		tvCCTV = (TextView) this.findViewById(R.id.tv_cctv);
		// 卫视 tab名称 TextView
		tvTV = (TextView) this.findViewById(R.id.tv_satellite);
		// 本地 tab名称 TextView
		tvLocal = (TextView) this.findViewById(R.id.tv_local);
		// 高清频道 tab名称 TextView
		tvHD = (TextView) this.findViewById(R.id.tv_hd_channel);
		// 收费频道 tab名称 TextView
		tvPay = (TextView) this.findViewById(R.id.tv_subscription_channel);

		// 央视 tab FrameLayout
		vCctvChannel = findViewById(R.id.fl_cctv);
		// 卫视 tab FrameLayout
		vTvChannel = findViewById(R.id.fl_satellite);
		// 央视 tab FrameLayout
		vLocalChannel = findViewById(R.id.fl_local);
		// 高清频道 tab FrameLayout
		vHdChannel = findViewById(R.id.fl_hd_channel);
		// 收费频道 tab FrameLayout
		vPayChannel = findViewById(R.id.fl_subscription_channel);
		// 频道tab 数组
		tabViews = new View[] { vCctvChannel, vTvChannel, vLocalChannel, vHdChannel, vPayChannel };
		// 频道 tab名称 数组
		tabTextViews = new TextView[] { tvCCTV, tvTV, tvLocal, tvHD, tvPay };

		// 央视 ViewPager
		cctvViewPager = (HivePreloadViewPager) findViewById(R.id.vp_cctv);
		// 卫视 ViewPager
		tvViewPager = (HivePreloadViewPager) findViewById(R.id.vp_tv);
		// 本地 ViewPager
		localViewPager = (HivePreloadViewPager) findViewById(R.id.vp_local);
		// 高清频道 ViewPager
		hdViewPager = (HivePreloadViewPager) findViewById(R.id.vp_hd);
		// 收费频道 ViewPager
		payViewPager = (HivePreloadViewPager) findViewById(R.id.vp_pay);

		viewPagers = new HivePreloadViewPager[] { cctvViewPager, tvViewPager, localViewPager, hdViewPager, payViewPager };

		// 左右翻页图片
		ivPagerRight = findViewById(R.id.iv_pager_right);
		ivPagerLeft = findViewById(R.id.iv_pager_left);
		// 初始化隐藏
		ivPagerLeft.setVisibility(View.INVISIBLE);
		ivPagerRight.setVisibility(View.INVISIBLE);

		// 初始化Viewpager显示
		setCurrentViewPagerVisibility(0);

		// Tab焦点改变事件
		vCctvChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vTvChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vLocalChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vHdChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vPayChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());

		// 初始化 焦点不可以下移
		vCctvChannel.setNextFocusDownId(R.id.fl_cctv);
		vPayChannel.setNextFocusDownId(R.id.fl_subscription_channel);
		vHdChannel.setNextFocusDownId(R.id.fl_hd_channel);
		vLocalChannel.setNextFocusDownId(R.id.fl_local);
		vTvChannel.setNextFocusDownId(R.id.fl_satellite);

		// 设置ViewPager显示情况
		setAllViewPagerVisibility(0);

		// Tab 添加(央视，卫视，本地，高清，收费)的按键事件
		vCctvChannel.setOnKeyListener(channelTabKeyListener);
		vTvChannel.setOnKeyListener(channelTabKeyListener);
		vLocalChannel.setOnKeyListener(channelTabKeyListener);
		vHdChannel.setOnKeyListener(channelTabKeyListener);
		vPayChannel.setOnKeyListener(channelTabKeyListener);
		// 等待条
		mProgressDialog = (ProgressDialog) this.findViewById(R.id.iv_hiveview_loading);
		// 主容器
		rlContainer = (RelativeLayout) this.findViewById(R.id.rl_container);
		// ViewPager 添加预加载事件
		setAllPreloadingListener();
	}

	/***
	 * 设置所有viewpager的显示情况
	 * 
	 * @param currentViewPager
	 */
	public void setCurrentViewPagerVisibility(int currentViewPager) {
		for (int i = 0; i < viewPagers.length; i++) {
			viewPagers[i].setVisibility(View.INVISIBLE);
			if (i == currentViewPager) {
				viewPagers[i].setVisibility(View.VISIBLE);
			}
		}
	}

	/***
	 * 给所有viewpager设置FastPreloadingListener
	 */
	public void setAllPreloadingListener() {
		for (int i = 0; i < viewPagers.length; i++) {
			viewPagers[i].setPreloadingListener(new FastPreloadingListener());
		}
	}

	/**
	 * 预加载 ViewPager翻页事件
	 */
	class FastPreloadingListener implements OnPreloadingListener {

		@Override
		public void preLoading(int pageSize) {
		}

		@Override
		public void preLoadNotFinish() {
		}

		/**
		 * 第一页
		 */
		@Override
		public void onFirstPage() {
			ivPagerLeft.setVisibility(View.INVISIBLE);
		}

		/**
		 * 最后一页
		 */
		@Override
		public void onLastPage() {
			currentPageCount = getPageSize(mShowViewPagerIndex);
			// 最后一页
			if (currentPageIndex == currentPageCount) {
				ivPagerRight.setVisibility(View.INVISIBLE);
				ivPagerLeft.setVisibility(View.VISIBLE);
			}
		}

		/**
		 * 设置当前页
		 */
		@Override
		public void setPageCurrent(int pageIndex) {
			currentPageIndex = pageIndex;
			currentPageCount = getPageSize(mShowViewPagerIndex);
			if (pagerCounts[mShowViewPagerIndex] < PAGE_SIZE) {
				// 只有一页时
				ivPagerRight.setVisibility(View.INVISIBLE);
				ivPagerLeft.setVisibility(View.INVISIBLE);
			} else {
				// 有多页时
				if (pageIndex == 1) {
					// 第一页
					ivPagerLeft.setVisibility(View.INVISIBLE);
					ivPagerRight.setVisibility(View.VISIBLE);
				} else if (pageIndex == currentPageCount) {
					// 最后一页
					ivPagerRight.setVisibility(View.INVISIBLE);
					ivPagerLeft.setVisibility(View.VISIBLE);
				} else {
					// 除第一页最后一页的 其他页
					ivPagerRight.setVisibility(View.VISIBLE);
					ivPagerLeft.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	/**
	 * 填充数据
	 */
	@Override
	protected void processData(int msgWhat) {
		mProgressDialog.setVisibility(View.INVISIBLE);
		rlContainer.setVisibility(View.VISIBLE);
		switch (msgWhat) {
		case REQUEST_CCTV_CHANNEL_SUCCESS:// 央视频道
			mChannelsCount += cctvChannelList.size();
			pagerCounts[0] = cctvChannelList.size();
			// 数据为空时向下焦点不变
			if (pagerCounts[0] == 0) {
				vCctvChannel.setNextFocusDownId(R.id.fl_cctv);
			} else {
				vCctvChannel.setNextFocusDownId(R.id.vp_cctv);
			}
			tvCCTV.setText(String.format(getString(R.string.cctv_count), cctvChannelList.size()));
			mCctvAdapter = new HivePagerAdapter(this, new TvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
					cctvViewPager, PAGE_SIZE, new ThreeLineFocusStrategy());
			mCctvAdapter.addDataSource(cctvChannelList);
			mCctvAdapter.setDataTotalSize(cctvChannelList.size());
			cctvViewPager.setAdapter(mCctvAdapter);
			// 初始化央视是否显示向右翻页的图片
			if (PAGE_SIZE < cctvChannelList.size() && (cctvViewPager.getVisibility() == 0)) {
				ivPagerRight.setVisibility(View.VISIBLE);
			}
			requestTvChannels();// 请求卫视电视频道信息
			break;
		case REQUEST_CCTV_CHANNEL_FAIL:
			tvCCTV.setText(String.format(getString(R.string.cctv_count), 0));
			pagerCounts[0] = 0;
			requestTvChannels();// 请求卫视电视频道信息
			vCctvChannel.setNextFocusDownId(R.id.fl_cctv);
			Log.d("processData", "央视电视台请求失败");
			break;
		case REQUEST_TV_CHANNEL_SUCCESS:// 卫视频道
			mChannelsCount += tvChannelList.size();
			pagerCounts[1] = tvChannelList.size();
			// 数据为空时向下焦点不变
			if (pagerCounts[1] == 0) {
				vTvChannel.setNextFocusDownId(R.id.fl_satellite);
			} else {
				vTvChannel.setNextFocusDownId(R.id.vp_tv);
			}
			tvTV.setText(String.format(getString(R.string.satellite_tv_count), tvChannelList.size()));
			tvAdapter = new HivePagerAdapter(this, new TvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()), tvViewPager,
					PAGE_SIZE, new ThreeLineFocusStrategy());
			tvAdapter.addDataSource(tvChannelList);
			tvAdapter.setDataTotalSize(tvChannelList.size());
			tvViewPager.setAdapter(tvAdapter);
			// 初始化央视是否显示向右翻页的图片
			if (PAGE_SIZE < tvChannelList.size() && (tvViewPager.getVisibility() == 0)) {
				ivPagerRight.setVisibility(View.VISIBLE);
			}
			requestLocalChannels();// 请求本地
			break;
		case REQUEST_TV_CHANNEL_FAIL:
			tvTV.setText(String.format(getString(R.string.satellite_tv_count), 0));
			pagerCounts[1] = 0;
			Log.d("processData", "卫视请求失败");
			vTvChannel.setNextFocusDownId(R.id.fl_satellite);
			requestLocalChannels();// 请求本地
			break;
		case REQUEST_LOCAL_CHANNEL_SUCCESS:// 本地频道
			mChannelsCount += localChannelList.size();
			pagerCounts[2] = localChannelList.size();
			// 数据为空时向下焦点不变
			if (pagerCounts[2] == 0) {
				vLocalChannel.setNextFocusDownId(R.id.fl_local);
			} else {
				vLocalChannel.setNextFocusDownId(R.id.vp_local);
			}
			tvLocal.setText(String.format(getString(R.string.local_count), localChannelList.size()));
			localAdapter = new HivePagerAdapter(this, new TvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
					localViewPager, PAGE_SIZE, new ThreeLineFocusStrategy());
			localAdapter.addDataSource(localChannelList);
			localAdapter.setDataTotalSize(localChannelList.size());
			localViewPager.setAdapter(localAdapter);
			// 初始化央视是否显示向右翻页的图片
			if (PAGE_SIZE < localChannelList.size() && (localViewPager.getVisibility() == 0)) {
				ivPagerRight.setVisibility(View.VISIBLE);
			}
			requestHdChannels();// 请求高清
			break;
		case REQUEST_LOCAL_CHANNEL_FAIL:
			Log.d("processData", "本地电视台请求失败");
			tvLocal.setText(String.format(getString(R.string.local_count), 0));
			pagerCounts[2] = 0;
			vLocalChannel.setNextFocusDownId(R.id.fl_local);
			requestHdChannels();// 请求高清
			break;
		case REQUEST_HD_CHANNEL_SUCCESS:// 高清频道
			mChannelsCount += hdChannelList.size();
			pagerCounts[3] = hdChannelList.size();
			// 数据为空时向下焦点不变
			if (pagerCounts[3] == 0) {
				vHdChannel.setNextFocusDownId(R.id.fl_hd_channel);
			} else {
				vHdChannel.setNextFocusDownId(R.id.vp_hd);
			}
			tvHD.setText(String.format(getString(R.string.hd_channel_count), hdChannelList.size()));
			hdAdapter = new HivePagerAdapter(this, new TvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()), hdViewPager,
					PAGE_SIZE, new ThreeLineFocusStrategy());
			hdAdapter.addDataSource(hdChannelList);
			hdAdapter.setDataTotalSize(hdChannelList.size());
			hdViewPager.setAdapter(hdAdapter);
			// 初始化央视是否显示向右翻页的图片
			if (PAGE_SIZE < hdChannelList.size() && (hdViewPager.getVisibility() == 0)) {
				ivPagerRight.setVisibility(View.VISIBLE);
			}
			requestPayChannels();// 请求付费
			break;
		case REQUEST_HD_CHANNEL_FAIL:
			Log.d("processData", "高清电视台请求失败");
			tvHD.setText(String.format(getString(R.string.hd_channel_count), 0));
			pagerCounts[3] = 0;
			vHdChannel.setNextFocusDownId(R.id.fl_hd_channel);
			requestPayChannels();// 请求付费
			break;
		case REQUEST_PAY_CHANNEL_SUCCESS:// 收费频道
			mChannelsCount += payChannelList.size();
			pagerCounts[4] = payChannelList.size();
			// 数据为空时向下焦点不变
			if (pagerCounts[4] == 0) {
				vPayChannel.setNextFocusDownId(R.id.fl_subscription_channel);
			} else {
				vPayChannel.setNextFocusDownId(R.id.vp_pay);
			}
			tvPay.setText(String.format(getString(R.string.subscription_channel_count), payChannelList.size()));
			payAdapter = new HivePagerAdapter(this, new TvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
					payViewPager, PAGE_SIZE, new ThreeLineFocusStrategy());
			payAdapter.addDataSource(payChannelList);
			payAdapter.setDataTotalSize(payChannelList.size());
			payViewPager.setAdapter(payAdapter);
			// 初始化央视是否显示向右翻页的图片
			if (PAGE_SIZE < payChannelList.size() && (payViewPager.getVisibility() == 0)) {
				ivPagerRight.setVisibility(View.VISIBLE);
			}
			break;
		case REQUEST_PAY_CHANNEL_FAIL:
			Log.d("processData", "付费电视台请求失败");
			tvPay.setText(String.format(getString(R.string.subscription_channel_count), 0));
			pagerCounts[4] = 0;
			vPayChannel.setNextFocusDownId(R.id.fl_subscription_channel);
			break;
		default:
			break;
		}

		// tvChannelsCount.setText(String.format(getString(R.string.television_count),
		// mChannelsCount));
	}

	/**
	 * 请求央视频道信息
	 */
	private void requestCctvChannels() {
		submitRequest(new SafeRunnable() {
			@Override
			public void requestData() {
				cctvChannelList = new HiveTVService().getChannelsBySp("cctv");
				// 信息请求成功
				handler.sendEmptyMessage(REQUEST_CCTV_CHANNEL_SUCCESS);
			}

			@Override
			public void processServiceException(ServiceException e) {
				// 请求央视出现异常
				handler.sendEmptyMessage(REQUEST_CCTV_CHANNEL_FAIL);
			}
		});
	}

	/**
	 * 请求卫视频道信息
	 */
	private void requestTvChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				tvChannelList = new HiveTVService().getChannelsBySp("tv");
				handler.sendEmptyMessage(REQUEST_TV_CHANNEL_SUCCESS);
			}

			@Override
			public void processServiceException(ServiceException e) {
				handler.sendEmptyMessage(REQUEST_TV_CHANNEL_FAIL);
			}
		});
	}

	/**
	 * 请求本地频道信息
	 */
	private void requestLocalChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				localChannelList = new HiveTVService().getChannelsBySp("local");
				handler.sendEmptyMessage(REQUEST_LOCAL_CHANNEL_SUCCESS);
			}

			@Override
			public void processServiceException(ServiceException e) {
				handler.sendEmptyMessage(REQUEST_LOCAL_CHANNEL_FAIL);
			}
		});
	}

	/**
	 * 请求高清频道信息
	 */
	private void requestHdChannels() {
		// 请求高清频道
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				hdChannelList = new HiveTVService().getChannelsBySp("hd");
				handler.sendEmptyMessage(REQUEST_HD_CHANNEL_SUCCESS);
			}

			@Override
			public void processServiceException(ServiceException e) {
				handler.sendEmptyMessage(REQUEST_HD_CHANNEL_FAIL);
			}
		});

	}

	/**
	 * 请求高清频道信息
	 */
	private void requestPayChannels() {
		// 请求收费频道
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				payChannelList = new HiveTVService().getChannelsBySp("pay");
				handler.sendEmptyMessage(REQUEST_PAY_CHANNEL_SUCCESS);
			}

			@Override
			public void processServiceException(ServiceException e) {
				handler.sendEmptyMessage(REQUEST_PAY_CHANNEL_FAIL);
			}
		});

	}

	/**
	 * 电视台分类（央视，卫视，本地，高清，收费）的View，焦点监听
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ChannelTabFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v == vCctvChannel) {// 显示央视列表
					// 赋值mUpFocusId
					mUpFocusId = R.id.fl_cctv;
					mShowViewPagerIndex = 0;
					// 修改显示状态
					setAllViewPagerVisibility(mShowViewPagerIndex);
					setCurrentViewPagerVisibility(mShowViewPagerIndex);
				} else if (v == vTvChannel) {// 显示卫视列表
					mShowViewPagerIndex = 1;
					mUpFocusId = R.id.fl_satellite;
					// 修改显示状态
					setAllViewPagerVisibility(mShowViewPagerIndex);
					setCurrentViewPagerVisibility(mShowViewPagerIndex);
				} else if (v == vLocalChannel) {// 显示本地电视台列表
					mShowViewPagerIndex = 2;
					mUpFocusId = R.id.fl_local;
					// 修改显示状态
					setAllViewPagerVisibility(mShowViewPagerIndex);
					setCurrentViewPagerVisibility(mShowViewPagerIndex);
				} else if (v == vHdChannel) {// 显示高清电视台列表
					mShowViewPagerIndex = 3;
					mUpFocusId = R.id.fl_hd_channel;
					// 修改显示状态
					setAllViewPagerVisibility(mShowViewPagerIndex);
					setCurrentViewPagerVisibility(mShowViewPagerIndex);
				} else if (v == vPayChannel) {// 显示收费电视台列表
					mShowViewPagerIndex = 4;
					mUpFocusId = R.id.fl_subscription_channel;
					// 修改显示状态
					setAllViewPagerVisibility(mShowViewPagerIndex);
					setCurrentViewPagerVisibility(mShowViewPagerIndex);
				}
				// 如果从当前页移动到下一个分类 当前分类的元素大于0 第一个元素获取焦点 1
				if (isToNext && pagerCounts[mShowViewPagerIndex] > 0) {
					viewPagers[mShowViewPagerIndex].requestFocus();
				}

				// 变换标志
				isToNext = false;
				// 切换tab时修改左右翻页图片
				changeTab();
				// 当前焦点的View设置成橘黄色，其余的Tab设置成白色
				for (int i = 0; i < tabViews.length; i++) {
					if (tabViews[i] == v) {
						tabTextViews[i].setTextColor(Color.parseColor("#ffffff"));
					} else {
						tabTextViews[i].setTextColor(Color.parseColor("#a8a8a8"));
					}
				}

			}
		}

		/**
		 * 切换tab时改变左右翻页图片的显示 每次切换tab时根据viewPager的状态初始化
		 */
		private void changeTab() {
			int currentSize = pagerCounts[mShowViewPagerIndex];
			// 获取当前ViewPager的currentItem 从0开始
			int currentItem = viewPagers[mShowViewPagerIndex].getCurrentItem();

			// 获取当前ViewPager的总页数
			currentPageCount = getPageSize(mShowViewPagerIndex);
			if (currentSize < PAGE_SIZE) {
				// 只有一页
				ivPagerRight.setVisibility(View.INVISIBLE);
				ivPagerLeft.setVisibility(View.INVISIBLE);
			} else {
				// 有多页时
				if (currentItem == 0) {
					// 第一页
					ivPagerRight.setVisibility(View.VISIBLE);
					ivPagerLeft.setVisibility(View.INVISIBLE);
				} else if (currentItem == currentPageCount - 1) {
					// 最后一页
					ivPagerRight.setVisibility(View.INVISIBLE);
					ivPagerLeft.setVisibility(View.VISIBLE);
				} else {
					// 除第一页最后一页的其他页
					ivPagerRight.setVisibility(View.VISIBLE);
					ivPagerLeft.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	/**
	 * 监听Tab(央视，卫视，本地，高清，收费)的按键事件
	 * 
	 * 如果是向下按键Tab中的TextView显示橘黄色
	 */
	OnKeyListener channelTabKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// 当前的Tab,如果是向下按键Tab中的TextView显示橘黄色
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
				for (int i = 0; i < tabViews.length; i++) {
					if (tabViews[i] == v) {
						tabTextViews[i].setTextColor(Color.parseColor("#ffffff"));
						break;
					} else {
						tabTextViews[i].setTextColor(Color.parseColor("#a8a8a8"));
					}
				}

			}

			return false;
		}
	};

	/**
	 * ItemView上焦点改变事件回调接口
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ItemViewFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {

			TvChannelPageViewItemView itemView = (TvChannelPageViewItemView) view;
			if (has) {
				HiveBaseEntity entity = (HiveBaseEntity) itemView.getTag();
				if (entity.getPositionInItemView() < PAGE_SIZE / 3) {
					view.setNextFocusUpId(mUpFocusId);
				}
				itemView.setTextMarquee(true);
			} else {
				itemView.setTextMarquee(false);
			}

		}
	}

	private ChannelEntity entity;

	/**
	 * ItemView上按键事件回调接口 按菜单键
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ItemViewKeyListener implements CallBackItemViewKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			entity = (ChannelEntity) v.getTag();
			Log.i(TAG, keyCode + "");
			if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
				Intent intent = new Intent(getApplicationContext(), LiveEpgInfoActivity.class);
				// intent.putExtra(AppConstant.LIVE_ENTITY, currentEntity);
				// OnliveSelect.channelName 进入直播时设置台名
				OnliveSelect.channelName = entity.getName();
				intent.putExtra(AppConstant.CHANNEL_CODE, entity.getCode());
				intent.putExtra(AppConstant.CHANNEL_LOGO, entity.getLogo());
				intent.putExtra(AppConstant.CHANNEL_NAME, entity.getName());
				startActivity(intent);
			}

			/**
			 * 判断当前的位置焦点不让移动
			 */
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
				HivePagerAdapter adapter = (HivePagerAdapter) viewPagers[mShowViewPagerIndex].getAdapter();
				// 判断最后一个
				if (viewPagers[mShowViewPagerIndex].getCurrentItem() == adapter.getLocalPageTotal() - 1
						&& (entity.getPositionInItemView() + 1) == adapter.getLocalDataSize() % 12) {
					return true;
				}
				// 第二行的最后一个
				if (viewPagers[mShowViewPagerIndex].getCurrentItem() == adapter.getLocalPageTotal() - 1 && (entity.getPositionInItemView() + 1) == 8) {
					return true;
				}
				// 第一行最后一个
				if (viewPagers[mShowViewPagerIndex].getCurrentItem() == adapter.getLocalPageTotal() - 1 && (entity.getPositionInItemView() + 1) == 4) {
					return true;
				}
			}

			if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {
				HivePagerAdapter adapter = (HivePagerAdapter) viewPagers[mShowViewPagerIndex].getAdapter();
				if (viewPagers[mShowViewPagerIndex].getCurrentItem() == 0 && entity.getPositionInItemView() == 0) {
					return true;
				}
				if (viewPagers[mShowViewPagerIndex].getCurrentItem() == 0 && entity.getPositionInItemView() == 4) {
					return true;
				}
				if (viewPagers[mShowViewPagerIndex].getCurrentItem() == 0 && entity.getPositionInItemView() == 8) {
					return true;
				}
			}

			// 换台
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (keyCode == KEYCODE) {
					changceChannel();
				} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && KeyMappingHashMapUtil.getInstance().containsKey(keyCode)) {
					STBSettingInfoUtil.notifySTBIrKeyPress(KeyMappingHashMapUtil.getInstance().get(keyCode));//
					changceChannel();
				}
			}

			return false;
		}
	}

	/**
	 * 根据数据个数计算页数
	 * 
	 * @param list
	 * @return
	 */
	private int getPageSize(int index) {
		return (int) Math.ceil(pagerCounts[index] / (double) PAGE_SIZE);
	}

	/**
	 * 计算最后一页的电视台数量
	 * 
	 * @return
	 */
	private int getLastPageViewCount(int index) {
		return pagerCounts[index] % PAGE_SIZE;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	/***
	 * 设置所有viewpager的显示情况
	 * 
	 * @param currentViewPager
	 */
	public void setAllViewPagerVisibility(int currentViewPager) {
		for (int i = 0; i < viewPagers.length; i++) {
			HivePreloadViewPager hivePreloadViewPager = viewPagers[i];
			if (i == currentViewPager) {
				if (!hivePreloadViewPager.isShown()) {
					viewPagers[i].setVisibility(View.VISIBLE);
					viewPagers[i].setAdapter(viewPagers[i].getAdapter());
				}
			} else {
				hivePreloadViewPager.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("cctv".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "打开央视频道", intent);
					vCctvChannel.requestFocus();
				} else if ("tv".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "打开卫视频道", intent);
					vTvChannel.requestFocus();
				} else if ("local".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "打开本地频道", intent);
					vLocalChannel.requestFocus();
				} else if ("hd".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "打开高清频道", intent);
					vHdChannel.requestFocus();
				} else if ("charge".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "打开收费频道", intent);
					vPayChannel.requestFocus();
				} else if ("menu".equals(command)) {
					if (entity != null) {
						HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "打开节目单", intent);
						Intent intent1 = new Intent(getApplicationContext(), LiveEpgInfoActivity.class);
						// intent.putExtra(AppConstant.LIVE_ENTITY,
						// currentEntity);
						// OnliveSelect.channelName 进入直播时设置台名
						OnliveSelect.channelName = entity.getName();
						intent1.putExtra(AppConstant.CHANNEL_CODE, entity.getCode());
						intent1.putExtra(AppConstant.CHANNEL_LOGO, entity.getLogo());
						intent1.putExtra(AppConstant.CHANNEL_NAME, entity.getName());
						startActivity(intent1);
					} else {
						HomeSwitchTabUtil.closeSiRi(LiveFastSelectTvChannelActivity.this, "没有节目单", intent);

					}
				}
			}

		}
	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.LiveFastSelectTvChannelActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("menu", new String[] { "查看节目单", "打开节目单", "节目单" });
		commands.put("cctv", new String[] { "央视", "中央台", "央视频道", "中央频道" });
		commands.put("tv", new String[] { "卫视", "卫视频道" });
		commands.put("local", new String[] { "本地", "地方台" });
		commands.put("hd", new String[] { "高清", "高清频道", "高清台" });
		commands.put("charge", new String[] { "收费", "收费频道", "收费台" });
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

	/**
	 * 换台
	 */
	private void changceChannel() {
		// 换台
		SwitchChannelUtils.switchChannel(LiveFastSelectTvChannelActivity.this, entity.getName(), false, AppScene.getScene());
		Log.i(TAG, "获取的台的名称====>" + entity.getName());
		OnliveSelect.channelName = entity.getName();
		Intent intent = new Intent();
		intent.setAction(AppConstant.SWITCH_CHANNEL);
		// 要发送的内容
		intent.putExtra("author", entity.getName());
		// 发送 一个无序广播
		sendBroadcast(intent);
	}

}
