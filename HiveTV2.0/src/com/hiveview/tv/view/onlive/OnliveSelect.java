package com.hiveview.tv.view.onlive;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborOneLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.factory.OnTvChannelPageViewFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.TvChannelPageViewItemView;

public class OnliveSelect extends RelativeLayout implements OnClickListener {
	/**
	 * TAG
	 */
	private static final String TAG = "OnliveSelect";
	protected static final int REQUEST_CCTV_CHANNEL_SUCCESS = 100;
	protected static final int REQUEST_CCTV_CHANNEL_FAIL = -100;
	protected static final int REQUEST_TV_CHANNEL_SUCCESS = 200;
	protected static final int REQUEST_TV_CHANNEL_FAIL = -200;
	protected static final int REQUEST_LOCAL_CHANNEL_SUCCESS = 300;
	protected static final int REQUEST_LOCAL_CHANNEL_FAIL = -300;
	protected static final int REQUEST_HD_CHANNEL_SUCCESS = 400;
	protected static final int REQUEST_HD_CHANNEL_FAIL = -400;
	protected static final int REQUEST_PAY_CHANNEL_SUCCESS = 500;
	protected static final int REQUEST_PAY_CHANNEL_FAIL = -500;
	public static String channelName = "";
	/** 每页的数据 */
	protected int PAGE_COUNT = 4;

	private Context mContext;

	private ProgressDialog mProgressDialog;

	/**
	 * 快速选台viewPager
	 */

	private View container;

	/**
	 * 电影数据的集合
	 */
	List<ChannelEntity> cctvProList;
	/**
	 * 卫视数据的集合
	 */
	List<ChannelEntity> satelliteTVProList;
	/**
	 * 本地数据的集合
	 */
	List<ChannelEntity> localTVProList;
	/**
	 * 高清卫视数据的集合
	 */
	List<ChannelEntity> hdChannelList;
	/**
	 * 收费数据的集合
	 */
	List<ChannelEntity> payChannelList;
	/**
	 * 央视名称 TextView
	 */
	private TextView tvCCTV;
	/**
	 * 卫视名称 TextView
	 */
	private TextView tvStatelliteTV;
	/**
	 * 地方卫视名称 TextView
	 */
	private TextView tvLocal;
	/**
	 * 高清频道名称 TextView
	 */
	private TextView tvHDChannel;
	/**
	 * 收费名称 TextView
	 */
	private TextView tvSubChannel;

	/**
	 * 央视FrameLayout
	 */
	private FrameLayout flCCTV;
	/**
	 * 卫视FrameLayout
	 */
	private FrameLayout flStatelliteTV;
	/**
	 * 本地FrameLayout
	 */
	private FrameLayout flLocal;
	/**
	 * 高清FrameLayout
	 */
	private FrameLayout flHDChannel;
	/**
	 * 收费FrameLayout
	 */
	private FrameLayout flSubChannel;

	private Resources mResources;
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
	 * 央视viewPger
	 */
	private HivePreloadViewPager vpCctvViewPager;
	/**
	 * 卫视viewPger
	 */
	private HivePreloadViewPager vpTvViewPager;
	/**
	 * 本地viewPger
	 */
	private HivePreloadViewPager vpLocalViewPager;
	/**
	 * 付费viewPger
	 */
	private HivePreloadViewPager vpPayViewPager;
	/**
	 * 高清频道viewPger
	 */
	private HivePreloadViewPager vpHdViewPager;
	/**
	 * 当前频道的 ViewPager
	 */
	private HivePreloadViewPager currentVisbllePager;
	/**
	 * 从ViewPager中的ItemView上往上按键焦点落到Id为mUpFocusId的View上
	 */
	private int mUpFocusId = 0;
	/**
	 * 电视Tab的数组
	 */
	private View tabViews[] = null;
	/**
	 * 频道 tab名称 数组
	 */
	private TextView tabTextViews[] = null;
	/**
	 * 左右翻页图片
	 */
	private View ivArrowLeft;
	private View ivArrowRight;
	/**
	 * 当前频道的 ViewPager
	 */
	private List<ChannelEntity> currentVisblleList;
	/**
	 * 当前页ViewPager的位置
	 */
	private int currentPageIndex = 0;
	/**
	 * 当前VIewpager的页数
	 */
	private int currentPageCount = 0;
	/**
	 * 当前ViewPager右边的分类id
	 */
	private int mRightFocusId = R.id.fl_satellite;
	/**
	 * 焦点移动到下一页的标志
	 */
	private boolean isNextPage = false;
	private HivePreloadViewPager[] viewPagerAll;

	public OnliveSelect(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
		requestCctvChannels();
	}

	private void init() {
		mResources = mContext.getResources();
		/**
		 * viewPager
		 */
		vpCctvViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_cctv);
		vpTvViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_tv);
		vpLocalViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_local);
		vpPayViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_pay);
		vpHdViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_hd);

		// selectTVList = new ArrayList<View>();

		// 电台分类名称
		tvCCTV = (TextView) this.findViewById(R.id.tv_cctv);
		tvStatelliteTV = (TextView) this.findViewById(R.id.tv_satellite);
		tvLocal = (TextView) this.findViewById(R.id.tv_local);
		tvHDChannel = (TextView) this.findViewById(R.id.tv_hd_channel);
		tvSubChannel = (TextView) this.findViewById(R.id.tv_subscription_channel);

		mProgressDialog = (ProgressDialog) this.findViewById(R.id.iv_hiveview_loading);
		// tab
		container = this.findViewById(R.id.rl_tv_menu);
		// 电台分类的FrameLayout
		flCCTV = (FrameLayout) this.findViewById(R.id.fl_cctv);
		flStatelliteTV = (FrameLayout) this.findViewById(R.id.fl_satellite);
		flLocal = (FrameLayout) this.findViewById(R.id.fl_local);
		flHDChannel = (FrameLayout) this.findViewById(R.id.fl_hd_channel);
		flSubChannel = (FrameLayout) this.findViewById(R.id.fl_subscription_channel);

		// 设置二级菜单焦点向下移动时
		// flCCTV.setNextFocusDownId(R.id.vp_cctv);
		// flStatelliteTV.setNextFocusDownId(R.id.vp_tv);
		// flLocal.setNextFocusDownId(R.id.vp_local);
		// flHDChannel.setNextFocusDownId(R.id.vp_hd);
		// flSubChannel.setNextFocusDownId(R.id.vp_pay);

		// 频道tab 数组
		tabViews = new View[] { flCCTV, flStatelliteTV, flLocal, flHDChannel, flSubChannel };

		// 频道 tab名称 数组
		tabTextViews = new TextView[] { tvCCTV, tvStatelliteTV, tvLocal, tvHDChannel, tvSubChannel };

		// Tab 添加(央视，卫视，本地，高清，收费)的按键事件
		flCCTV.setOnKeyListener(channelTabKeyListener);
		flStatelliteTV.setOnKeyListener(channelTabKeyListener);
		flLocal.setOnKeyListener(channelTabKeyListener);
		flHDChannel.setOnKeyListener(channelTabKeyListener);
		flSubChannel.setOnKeyListener(channelTabKeyListener);

		// Tab焦点改变事件
		flCCTV.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		flStatelliteTV.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		flLocal.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		flHDChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		flSubChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());

		// 左右翻页图片
		ivArrowLeft = this.findViewById(R.id.list_left);
		ivArrowRight = this.findViewById(R.id.list_right);
		ivArrowLeft.setVisibility(View.INVISIBLE);
		ivArrowRight.setVisibility(View.INVISIBLE);

		// 等待条
		mProgressDialog = (ProgressDialog) this.findViewById(R.id.iv_hiveview_loading);
		// ViewPager 添加预加载事件
		vpCctvViewPager.setPreloadingListener(new FastPreloadingListener());
		vpTvViewPager.setPreloadingListener(new FastPreloadingListener());
		vpLocalViewPager.setPreloadingListener(new FastPreloadingListener());
		vpHdViewPager.setPreloadingListener(new FastPreloadingListener());
		vpPayViewPager.setPreloadingListener(new FastPreloadingListener());

		// 初始化Viewpager显示
		vpCctvViewPager.setVisibility(View.VISIBLE);
		vpTvViewPager.setVisibility(View.INVISIBLE);
		vpLocalViewPager.setVisibility(View.INVISIBLE);
		vpHdViewPager.setVisibility(View.INVISIBLE);
		vpPayViewPager.setVisibility(View.INVISIBLE);

		tvCCTV.setText(String.format(mResources.getString(R.string.cctv_count), ".."));
		tvStatelliteTV.setText(String.format(mResources.getString(R.string.satellite_tv_count), ".."));
		tvLocal.setText(String.format(mResources.getString(R.string.local_count), ".."));
		tvHDChannel.setText(String.format(mResources.getString(R.string.hd_channel_count), ".."));
		tvSubChannel.setText(String.format(mResources.getString(R.string.subscription_channel_count), ".."));

		viewPagerAll = new HivePreloadViewPager[] { vpCctvViewPager, vpTvViewPager, vpLocalViewPager, vpHdViewPager, vpPayViewPager };

		setAllViewPagerVisibility(0);

		flCCTV.setNextFocusDownId(R.id.fl_cctv);
		tvStatelliteTV.setNextFocusDownId(R.id.fl_satellite);
		tvLocal.setNextFocusDownId(R.id.fl_local);
		flHDChannel.setNextFocusDownId(R.id.fl_hd_channel);
		tvSubChannel.setNextFocusDownId(R.id.fl_subscription_channel);
	}

	private void requestCctvChannels() {
		HttpTaskManager.getInstance().submit(new SafeRunnable() {

			@Override
			public void requestData() {
				cctvProList = new HiveTVService().getChannelsBySp("cctv");
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

	/***
	 * 设置所有viewpager的显示情况
	 * 
	 * @param currentViewPager
	 */
	public void setAllViewPagerVisibility(int currentViewPager) {
		for (int i = 0; i < viewPagerAll.length; i++) {
			HivePreloadViewPager hivePreloadViewPager = viewPagerAll[i];
			if (i == currentViewPager) {
				if (!hivePreloadViewPager.isShown()) {
					Log.i(TAG, "ChannelTabFocusChangeListener..........................cctv");
					viewPagerAll[i].setVisibility(View.VISIBLE);
					// viewPagerAll[i].setCurrentItem(0);
					viewPagerAll[i].setAdapter(viewPagerAll[i].getAdapter());
				}
			} else {
				hivePreloadViewPager.setVisibility(View.INVISIBLE);

			}
		}
	}

	/**
	 * 请求卫视频道信息
	 */
	private void requestTvChannels() {
		HttpTaskManager.getInstance().submit(new SafeRunnable() {

			@Override
			public void requestData() {
				satelliteTVProList = new HiveTVService().getChannelsBySp("tv");
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
		HttpTaskManager.getInstance().submit(new SafeRunnable() {

			@Override
			public void requestData() {
				localTVProList = new HiveTVService().getChannelsBySp("local");
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
		HttpTaskManager.getInstance().submit(new SafeRunnable() {

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
		HttpTaskManager.getInstance().submit(new SafeRunnable() {

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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			processData(msg.what);
		}
	};

	/**
	 * 填充数据
	 */
	protected void processData(int msgWhat) {
		mProgressDialog.setVisibility(View.GONE);
		container.setVisibility(View.VISIBLE);
		switch (msgWhat) {
		case REQUEST_CCTV_CHANNEL_SUCCESS:// 央视频道
			tvCCTV.setText(String.format(mResources.getString(R.string.cctv_count), cctvProList.size()));
			if (cctvProList.size() == 0) {
				flCCTV.setNextFocusDownId(R.id.fl_cctv);
			} else {
				flCCTV.setNextFocusDownId(R.id.vp_cctv);
			}
			if (null == mCctvAdapter) {
				mCctvAdapter = new HivePagerAdapter(mContext, new OnTvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
						vpCctvViewPager, 4, new NeighborOneLineStrategy());
			} else {
				mCctvAdapter.clear();
			}

			mCctvAdapter.addDataSource(cctvProList);
			mCctvAdapter.setDataTotalSize(cctvProList.size());
			vpCctvViewPager.setAdapter(mCctvAdapter);
			//当Viewpager显示的是当前Viewpager时设置当前数据			
			if(vpCctvViewPager.getVisibility()  == 0){
				currentVisblleList = cctvProList;
				ivArrowRight.setVisibility(View.VISIBLE);
			}
			requestTvChannels();// 请求卫视电视频道信息
			break;
		case REQUEST_CCTV_CHANNEL_FAIL:
			tvCCTV.setText(String.format(mResources.getString(R.string.cctv_count), 0));
			flCCTV.setNextFocusDownId(R.id.fl_cctv);
			Log.d("processData", "央视请求失败");
			requestTvChannels();// 请求卫视电视频道信息
			break;
		case REQUEST_TV_CHANNEL_SUCCESS:// 卫视频道
			tvStatelliteTV.setText(String.format(mResources.getString(R.string.satellite_tv_count), satelliteTVProList.size()));
			if (satelliteTVProList.size() == 0) {
				flStatelliteTV.setNextFocusDownId(R.id.fl_satellite);
			} else {
				flStatelliteTV.setNextFocusDownId(R.id.vp_tv);
			}
			if (null == tvAdapter) {
				tvAdapter = new HivePagerAdapter(mContext, new OnTvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
						vpTvViewPager, 4, new NeighborOneLineStrategy());
			} else {
				tvAdapter.clear();
			}
			tvAdapter.addDataSource(satelliteTVProList);
			tvAdapter.setDataTotalSize(satelliteTVProList.size());
			vpTvViewPager.setAdapter(tvAdapter);
			//当Viewpager显示的是当前Viewpager时设置当前数据
			if(vpTvViewPager.getVisibility()  == 0){
				currentVisblleList = satelliteTVProList;
				ivArrowRight.setVisibility(View.VISIBLE);
			}
			requestLocalChannels();// 请求本地电视频道信息
			break;
		case REQUEST_TV_CHANNEL_FAIL:
			tvStatelliteTV.setText(String.format(mResources.getString(R.string.satellite_tv_count), 0));
			flStatelliteTV.setNextFocusDownId(R.id.fl_satellite);
			Log.d("OnliveSelect", "卫视请求失败");
			requestLocalChannels();// 请求本地电视频道信息
			break;
		case REQUEST_LOCAL_CHANNEL_SUCCESS:// 本地频道
			tvLocal.setText(String.format(mResources.getString(R.string.local_count), localTVProList.size()));
			if (localTVProList.size() == 0) {
				flLocal.setNextFocusDownId(R.id.fl_local);
			} else {
				flLocal.setNextFocusDownId(R.id.vp_local);
			}

			if (null == localAdapter) {
				localAdapter = new HivePagerAdapter(mContext, new OnTvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
						vpLocalViewPager, 4, new NeighborOneLineStrategy());
			}else{
				localAdapter.clear();
			}
			localAdapter.addDataSource(localTVProList);
			localAdapter.setDataTotalSize(localTVProList.size());
			vpLocalViewPager.setAdapter(localAdapter);
			//当Viewpager显示的是当前Viewpager时设置当前数据			
			if(vpLocalViewPager.getVisibility()  == 0){
				currentVisblleList = localTVProList;
				ivArrowRight.setVisibility(View.VISIBLE);
			}
			requestHdChannels();// 请求高清电视频道信息
			break;
		case REQUEST_LOCAL_CHANNEL_FAIL:
			Log.d("OnliveSelect", "本地电视台请求失败");
			tvLocal.setText(String.format(mResources.getString(R.string.local_count), 0));
			flLocal.setNextFocusDownId(R.id.fl_local);
			requestHdChannels();// 请求高清电视频道信息
			break;
		case REQUEST_HD_CHANNEL_SUCCESS:// 高清频道
			tvHDChannel.setText(String.format(mResources.getString(R.string.hd_channel_count), hdChannelList.size()));
			if (hdChannelList.size() == 0) {
				flHDChannel.setNextFocusDownId(R.id.fl_hd_channel);
			} else {
				flHDChannel.setNextFocusDownId(R.id.vp_hd);
			}
			if(null == hdAdapter){
				hdAdapter = new HivePagerAdapter(mContext, new OnTvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
						vpHdViewPager, 4, new NeighborOneLineStrategy());
			}else{
				hdAdapter.clear();
			}
			hdAdapter.addDataSource(hdChannelList);
			hdAdapter.setDataTotalSize(hdChannelList.size());
			vpHdViewPager.setAdapter(hdAdapter);
			//当Viewpager显示的是当前Viewpager时设置当前数据			
			if(vpHdViewPager.getVisibility()  == 0){
				currentVisblleList = hdChannelList;
				ivArrowRight.setVisibility(View.VISIBLE);
			}
			requestPayChannels();// 请求付费电视频道信息
			break;
		case REQUEST_HD_CHANNEL_FAIL:
			Log.d("OnliveSelect", "高清电视台请求失败");
			tvHDChannel.setText(String.format(mResources.getString(R.string.hd_channel_count), 0));
			flHDChannel.setNextFocusDownId(R.id.fl_hd_channel);
			requestPayChannels();// 请求付费电视频道信息
			break;
		case REQUEST_PAY_CHANNEL_SUCCESS:// 收费频道
			tvSubChannel.setText(String.format(mResources.getString(R.string.subscription_channel_count), payChannelList.size()));
			if (payChannelList.size() == 0) {
				flSubChannel.setNextFocusDownId(R.id.fl_subscription_channel);
			} else {
				flSubChannel.setNextFocusDownId(R.id.vp_pay);
			}
			if(null == payAdapter){
				
				payAdapter = new HivePagerAdapter(mContext, new OnTvChannelPageViewFactory(new ItemViewFocusListener(), new ItemViewKeyListener()),
						vpPayViewPager, 4, new NeighborOneLineStrategy());
			}else{
				payAdapter.clear();
			}
			payAdapter.addDataSource(payChannelList);
			payAdapter.setDataTotalSize(payChannelList.size());
			vpPayViewPager.setAdapter(payAdapter);
			//当Viewpager显示的是当前Viewpager时设置当前数据
			if(vpPayViewPager.getVisibility()  == 0){
				currentVisblleList = payChannelList;
				ivArrowRight.setVisibility(View.VISIBLE);
			}
			break;
		case REQUEST_PAY_CHANNEL_FAIL:
			Log.d("processData", "付费电视台请求失败");
			tvSubChannel.setText(String.format(mResources.getString(R.string.subscription_channel_count), 0));
			flSubChannel.setNextFocusDownId(R.id.fl_subscription_channel);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * ItemView上焦点改变事件回调接口
	 * 
	 * 
	 */
	class ItemViewFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {

			TvChannelPageViewItemView itemView = (TvChannelPageViewItemView) view;
			if (has) {
				ChannelEntity entity = (ChannelEntity) itemView.getTag();
				if (entity.getPositionInItemView() < 5) {
					itemView.setNextFocusUpId(mUpFocusId);
				}

				if (currentVisbllePager.getSelectedPageIndex() + 1 == getPageSize(currentVisblleList)) {
					// 当该电视台是所在分类的最后一页的最右边的电视台时，向右的时候就跳到下一个分类
					// 最后一页条目个数小于等于每页页数 并且 当前条目位置等于最后一个条目时
					if (getLastPageViewCount(currentVisblleList) <= PAGE_COUNT && entity.getPositionInItemView() == (PAGE_COUNT - 1)) {
						itemView.setNextFocusRightId(itemView.getId());
						// isNextPage = true;
					} else if (getLastPageViewCount(currentVisblleList) < PAGE_COUNT
					// 最后一页条目个数小于每页页数 并且为最后一个元素时
							&& entity.getPositionInItemView() == (getLastPageViewCount(currentVisblleList) - 1)) {
						itemView.setNextFocusRightId(itemView.getId());
						// isNextPage = true;
					} else if (getLastPageViewCount(currentVisblleList) == PAGE_COUNT
					// 最后一页条目个数等于每页页数 或者当前位置为最后一个元素时
							&& (entity.getPositionInItemView() == (PAGE_COUNT - 1))) {
						itemView.setNextFocusRightId(itemView.getId());
						// isNextPage = true;
					}

				}

				itemView.setTextMarquee(true);
			} else {
				itemView.setTextMarquee(false);
			}

		}
	}

	/**
	 * ItemView上按键事件回调接口 按菜单键
	 * 
	 */
	class ItemViewKeyListener implements CallBackItemViewKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			ChannelEntity entity = (ChannelEntity) v.getTag();

			// 节目单
			// if (keyCode == KeyEvent.KEYCODE_MENU
			// && event.getAction() == KeyEvent.ACTION_DOWN) {
			// Intent intent = new Intent(mContext, OnlivePlayerActivity.class);
			// //
			// intent.putExtra(AppConstant.CHANNEL_CODE, entity.getCode());
			// intent.putExtra(AppConstant.CHANNEL_LOGO, entity.getLogo());
			// intent.putExtra(AppConstant.CHANNEL_NAME, entity.getName());
			// mContext.startActivity(intent);
			// }
			if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_DOWN) {
				((OnlivePlayerActivity) mContext).closeMune();
				SwitchChannelUtils.switchChannel(mContext, entity.getName(), false, AppScene.getScene());
				Intent intent = new Intent();
				intent.setAction(AppConstant.SWITCH_CHANNEL);
				setChannelName(entity.getName());
				channelName = entity.getName();
				// 要发送的内容
				intent.putExtra("author", entity.getName());
				// 发送 一个无序广播
				Log.i(TAG, "SwitchChannelUtils.....:" + entity.getName() + "................entity.getCode():" + entity.getCode());
				mContext.sendBroadcast(intent);
			}
			return false;
		}

		private void setChannelName(String channelName) {
			SharedPreferences preference = mContext.getSharedPreferences("DomyBoxPreference", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = preference.edit();
			if (channelName != null) {
				Log.i(TAG, "onReceive-->channelName:" + channelName);
				editor.putString(SwitchChannelUtils.KEY_CHANNEL_NAME, channelName);
				editor.commit();
			}
		}
	}

	/**
	 * 电视台分类（央视，卫视，本地，高清，收费）的View，焦点监听
	 * 
	 */
	class ChannelTabFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v == flCCTV) {// 显示央视列表
					// 赋值mUpFocusId
					mUpFocusId = R.id.fl_cctv;
					mRightFocusId = R.id.fl_satellite;
					currentVisbllePager = vpCctvViewPager;
					currentVisblleList = cctvProList;
					// 修改显示状态
					setAllViewPagerVisibility(0);
					vpCctvViewPager.setVisibility(View.VISIBLE);
					vpTvViewPager.setVisibility(View.INVISIBLE);
					vpLocalViewPager.setVisibility(View.INVISIBLE);
					vpHdViewPager.setVisibility(View.INVISIBLE);
					vpPayViewPager.setVisibility(View.INVISIBLE);
				} else if (v == flStatelliteTV) {// 显示卫视列表
					mUpFocusId = R.id.fl_satellite;
					mRightFocusId = R.id.fl_local;
					currentVisbllePager = vpTvViewPager;
					currentVisblleList = satelliteTVProList;
					setAllViewPagerVisibility(1);
					vpTvViewPager.setVisibility(View.VISIBLE);
					vpCctvViewPager.setVisibility(View.INVISIBLE);
					vpLocalViewPager.setVisibility(View.INVISIBLE);
					vpHdViewPager.setVisibility(View.INVISIBLE);
					vpPayViewPager.setVisibility(View.INVISIBLE);
				} else if (v == flLocal) {// 显示本地电视台列表
					mUpFocusId = R.id.fl_local;
					mRightFocusId = R.id.fl_hd_channel;
					currentVisbllePager = vpLocalViewPager;
					currentVisblleList = localTVProList;
					setAllViewPagerVisibility(2);
					vpLocalViewPager.setVisibility(View.VISIBLE);
					vpCctvViewPager.setVisibility(View.INVISIBLE);
					vpTvViewPager.setVisibility(View.INVISIBLE);
					vpHdViewPager.setVisibility(View.INVISIBLE);
					vpPayViewPager.setVisibility(View.INVISIBLE);
				} else if (v == flHDChannel) {// 显示高清电视台列表
					mUpFocusId = R.id.fl_hd_channel;
					mRightFocusId = R.id.fl_subscription_channel;
					currentVisbllePager = vpHdViewPager;
					currentVisblleList = hdChannelList;
					setAllViewPagerVisibility(3);
					vpHdViewPager.setVisibility(View.VISIBLE);
					vpCctvViewPager.setVisibility(View.INVISIBLE);
					vpTvViewPager.setVisibility(View.INVISIBLE);
					vpLocalViewPager.setVisibility(View.INVISIBLE);
					vpPayViewPager.setVisibility(View.INVISIBLE);
				} else if (v == flSubChannel) {// 显示收费电视台列表
					setAllViewPagerVisibility(4);
					mUpFocusId = R.id.fl_subscription_channel;
					mRightFocusId = R.id.fl_cctv;
					currentVisbllePager = vpPayViewPager;
					currentVisblleList = payChannelList;
					vpPayViewPager.setVisibility(View.VISIBLE);
					vpCctvViewPager.setVisibility(View.INVISIBLE);
					vpTvViewPager.setVisibility(View.INVISIBLE);
					vpLocalViewPager.setVisibility(View.INVISIBLE);
					vpHdViewPager.setVisibility(View.INVISIBLE);
				}
				// 如果从当前页移动到下一个分类 当前分类的元素大于0 第一个元素获取焦点
				if (null != currentVisblleList && isNextPage && currentVisblleList.size() > 0) {
					currentVisbllePager.requestFocus();
				}
				// 变换标志
				isNextPage = false;
				// 切换tab时修改左右翻页图片
				changeTab();
				// 当前焦点的View设置成橘黄色，其余的Tab设置成白色
				for (int i = 0; i < tabViews.length; i++) {
					if ((tabViews[i] == v)) {
						tabTextViews[i].setTextColor(Color.parseColor("#FF8B00"));
					} else {
						tabTextViews[i].setTextColor(Color.parseColor("#FFFFFF"));
					}
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
					if ((tabViews[i] == v)) {
						tabTextViews[i].setTextColor(Color.parseColor("#FF8B00"));
					} else if (R.id.tv_select == v.getId()) {
						tabTextViews[i].setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			}
			return false;
		}
	};

	/**
	 * 预加载
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
			ivArrowLeft.setVisibility(View.INVISIBLE);
		}

		/**
		 * 最后一页
		 */
		@Override
		public void onLastPage() {
			currentPageCount = getPageSize(currentVisblleList);
			// 最后一页
			if (currentPageIndex == currentPageCount) {
				ivArrowRight.setVisibility(View.INVISIBLE);
				ivArrowLeft.setVisibility(View.VISIBLE);
			}
		}

		/**
		 * 设置当前页
		 */
		@Override
		public void setPageCurrent(int pageIndex) {
			currentPageIndex = pageIndex;
			currentPageCount = getPageSize(currentVisblleList);
			if (currentVisblleList.size() < PAGE_COUNT) {
				// 只有一页时
				ivArrowRight.setVisibility(View.INVISIBLE);
				ivArrowLeft.setVisibility(View.INVISIBLE);
			} else {
				// 有多页时
				if (pageIndex == 1) {
					// 第一页
					ivArrowLeft.setVisibility(View.INVISIBLE);
					ivArrowRight.setVisibility(View.VISIBLE);
				} else if (pageIndex == currentPageCount) {
					// 最后一页
					ivArrowRight.setVisibility(View.INVISIBLE);
					ivArrowLeft.setVisibility(View.VISIBLE);
				} else {
					// 除第一页最后一页的 其他页
					ivArrowRight.setVisibility(View.VISIBLE);
					ivArrowLeft.setVisibility(View.VISIBLE);
				}
			}
		}

	}
	
	/**
	 * 切换tab时改变左右翻页图片的显示 每次切换tab时根据viewPager的状态初始化
	 */
	private void changeTab() {
		if (null == currentVisblleList || currentVisblleList.size() == 0) {
			ivArrowRight.setVisibility(View.INVISIBLE);
			ivArrowLeft.setVisibility(View.INVISIBLE);
			return;
		}
		int currentSize = currentVisblleList.size();
		// 获取当前ViewPager的currentItem 从0开始
		int currentItem = currentVisbllePager.getCurrentItem();
		// 获取当前ViewPager的总页数
		currentPageCount = getPageSize(currentVisblleList);
		if (currentSize < PAGE_COUNT) {
			// 只有一页
			ivArrowRight.setVisibility(View.INVISIBLE);
			ivArrowLeft.setVisibility(View.INVISIBLE);
		} else {
			// 有多页时
			if (currentItem == 0) {
				// 第一页
				ivArrowRight.setVisibility(View.VISIBLE);
				ivArrowLeft.setVisibility(View.INVISIBLE);
			} else if (currentItem == currentPageCount - 1) {
				// 最后一页
				ivArrowRight.setVisibility(View.INVISIBLE);
				ivArrowLeft.setVisibility(View.VISIBLE);
			} else {
				// 除第一页最后一页的其他页
				ivArrowRight.setVisibility(View.VISIBLE);
				ivArrowLeft.setVisibility(View.VISIBLE);
			}
		}
	}
	

	/**
	 * 根据数据个数计算页数
	 * 
	 * @param list
	 * @return
	 */
	private int getPageSize(List<ChannelEntity> list) {
		if (null == list) {
			return 0;
		}
		int count = list.size();
		if (0 == count) {
			return 0;
		}
		int pagesize = (int) Math.ceil(count / (double) PAGE_COUNT);

		return pagesize;

	}

	/**
	 * 计算最后一页的电视台数量
	 * 
	 * @return
	 */
	private int getLastPageViewCount(List<ChannelEntity> list) {
		if (null == list) {
			return 0;
		}
		int count = list.size();
		if (0 == count) {
			return 0;
		}
		return count % PAGE_COUNT;
	}

}
