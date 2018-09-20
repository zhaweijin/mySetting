package com.hiveview.tv.view.onlive;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborOneLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.VoiceOnliveEPGActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.factory.OnVoiceEpgPageViewFactory;
import com.hiveview.tv.common.factory.OnliveEpgPageViewFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.ChannelEntity;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.television.OnVoiceEpgPageItem;
import com.hiveview.tv.view.television.OnVoiceEpgPageItem.ONVoiceFoursInterface;
/**
 * 
 * @ClassName: VoiceOnliveEpg
 * @Description: 语音XX频道的节目单的view
 * @author: lihongji
 * @date 2014年12月4日 下午5:39:25
 *
 */
public class VoiceOnliveEpg extends FrameLayout implements  ONVoiceFoursInterface {

	private static final int RQUEST_DATA_SUCCESS = 100;

	private static final int RQUEST_DATA_FAIL = -100;
	/**
	 * 获取电台信息成功
	 */
	protected static final int RQUEST_CHANNEL_FAIL = 200;
	/**
	 * 获取电台信息失败
	 */
	protected static final int RQUEST_CHANNEL_SUCCESS = -200;

	/**
	 * @Fields VIEW_REQUEST_FOCUS:指定焦点获取焦点
	 */
	private static final int VIEW_REQUEST_FOCUS = 0x00142;
	private String TAG = "VoiceOnliveEpg";

	private static Context mContext;

	/** 每页的数据 */
	protected int PAGE_COUNT = 7;
	/**
	 * 当前页ViewPager的位置
	 */
	private int currentPageIndex = 0;
	/**
	 * 当前VIewpager的页数
	 */
	private int currentPageCount = 0;
	/**
	 * ViewPager
	 */
	private HivePreloadViewPager vpSelectEpg;
	/**
	 * 节目单集合
	 */
	private List<View> selectEpgList = null;
	private HivePagerAdapter mAdapter;

	private ArrayList<ProgramEntity> currentEpgList;
	private String mCurrentChannelName = "";
	private String currentDate="";
	private TextView epgLayoutEpghint;
	/**
	 * 节目viewPager当前的页数
	 */
	private int currentPage = 0;
	/**
	 * 向左翻页图片
	 */
	private View ivArrowLeft;
	/**
	 * 向右翻页
	 */
	private View ivArrowRight;
	/**
	 * 正在直播的位置
	 */
	private int playingIndex = -1;
	/**
	 * 电台结合
	 */
	private List<ChannelEntity> channelList;
	/**
	 * 
	 */
	private String channelCode = "cctv1";
	/**
	 * 是否注册广播
	 */
	private boolean isegisterReceiver = false;

	public VoiceOnliveEpg(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		init();
	}

	
	
	public String getmCurrentChannelName() {
		return mCurrentChannelName;
	}

	public void setmCurrentChannelName(String mCurrentChannelName) {
		this.mCurrentChannelName = mCurrentChannelName;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * 初始化数据
	 */
	private void init() {
		mCurrentChannelName=SwitchChannelUtils.getVoiceCurrentChannelName(mContext);
		String date=SwitchChannelUtils.getVoiceDate(mContext);
		if(date!=null){
		currentDate=date.substring(0, 10);
		}else{
			currentDate=getCurrentDate();	
		}
		vpSelectEpg = (HivePreloadViewPager) this.findViewById(R.id.voice_epg_select);
		// 左右翻页
		ivArrowLeft = this.findViewById(R.id.iv_arrow_left);
		ivArrowRight = this.findViewById(R.id.iv_arrow_right);

		epgLayoutEpghint = (TextView) this.findViewById(R.id.epg_epghint);

		// 初始化切换电台的广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppConstant.SWITCH_CHANNEL);
		filter.addAction(AppConstant.SWITCH_CHANNEL_XUNFEI);
		mContext.registerReceiver(dataReceiver, filter);
		isegisterReceiver = true;
//		Log.i(TAG, "/registerReceiver....................................." + isegisterReceiver);
		selectEpgList = new ArrayList<View>();
		Log.d(TAG, "name="+mCurrentChannelName+";STRATTIME="+currentDate);
		getChannelsByNames();
	}

	/**
	 * 接受广播 add by guosongsheng 切换电台
	 */
	private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent mIntent) {
			Log.i(TAG, "onliveepg....................dataReceiver");
			if (mIntent.getAction().equals(AppConstant.SWITCH_CHANNEL)) {// 响应用户切换电台
				Log.i(TAG, "com.hiveview.tv.activity.LiveFastSelectTvChannelActivity");
				getChannelsByNames();
			} else if (mIntent.getAction().equals(AppConstant.SWITCH_CHANNEL_XUNFEI)) {// 响应用户置切换电台
				Log.i(TAG, "com.iflytek.TVDCS.STBSTATUS.................");
				getChannelsByNames();
			}
		}
	};

	/**
	 * 获取当前电台的节目单
	 */
	private void getEpgList() {
		// mCurrentChannelName =
		// SwitchChannelUtils.getCurrentChannelName(mContext);
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				// 通过ChannelName访问节目单接口 测试数据cctv1
				Log.i(TAG, "上个界面传下来的界面名称.........................." + channelCode);
				currentEpgList = (ArrayList<ProgramEntity>) new HiveTVService().getProgramsByChannel(channelCode, currentDate + " 00:00", currentDate
						+ " 24:00");
				if (currentEpgList == null || currentEpgList.size() == 0) {
					handler.sendEmptyMessage(RQUEST_DATA_FAIL);
				} else {
					handler.sendEmptyMessage(RQUEST_DATA_SUCCESS);
				}
			};
		});
	}

	/**
	 * 根据channelNames 获取 channelCodes
	 * 
	 * @Title: OnliveEpg
	 * @author:郭松胜
	 * @Description: TODO
	 */
	private void getChannelsByNames() {
		HttpTaskManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				// 通过ChannelName获取channelCode 测试数据cctv-1
				channelList = new HiveTVService().getChannelsByNames(new String[] { mCurrentChannelName });
				if (channelList == null || channelList.size() == 0) {
					handler.sendEmptyMessage(RQUEST_CHANNEL_FAIL);
				} else {
					handler.sendEmptyMessage(RQUEST_CHANNEL_SUCCESS);
				}
			};
		});
	}

	// 反射机制 控制 viewpager滑动时间 为800
	private void changeViewPageScroller(ViewPager viewPager) {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			FixedSpeedScroller scroller;
			scroller = new FixedSpeedScroller(mContext, new AccelerateDecelerateInterpolator());
			mField.set(viewPager, scroller);
		} catch (Exception e) {
		}

	}

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

	private String getCurrentDate() {
		try {
			String currentDate = DateUtils.dateToString(new Date(), "yyyy-MM-dd");
			return currentDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
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

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			processData(msg.what);
		}
	};

	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case RQUEST_DATA_SUCCESS:
			setData();
			break;

		case RQUEST_DATA_FAIL:
			epgLayoutEpghint.setVisibility(View.VISIBLE);
			Log.i("OnliveEpg", "返回数据为空");
			break;
		case RQUEST_CHANNEL_SUCCESS:// 获取电台信息成功
			// 设置ChanneCode
			setChannelCode();
			// 获取节目单
			getEpgList();
			break;
		case RQUEST_CHANNEL_FAIL:// 获取电台信息失败
			Log.e(TAG, "根据ChannelName获取ChannelCode失败.......................");
			break;
		case VIEW_REQUEST_FOCUS:

			pageView.getTheInTimeView(itemIndex);
			break;
		default:
			break;
		}
	}

	OnVoiceEpgPageItem pageView = null;

	/**
	 * 设置ChannelCode
	 * 
	 * @Title: OnliveEpg
	 * @author:郭松胜
	 * @Description: TODO
	 */
	private void setChannelCode() {
		if (null != channelList && channelList.size() > 0) {
			// 获取集合中的第一个元素
			ChannelEntity channelEntity = channelList.get(0);
			((VoiceOnliveEPGActivity) mContext).setCHANNEL_LOGO(channelEntity.getLogo());
			((VoiceOnliveEPGActivity) mContext).setCHANNEL_NAME(channelEntity.getName());
			channelCode = channelEntity.getCode();

			Log.i(TAG, "上个界面传下来的界面名称...setChannelCode......................." + channelCode);
		}
	}

	/**
	 * 
	 * @Title: OnliveEpg
	 * @author:张鹏展
	 * @Description: 得到正在直播的view并使之获取焦点
	 * @return
	 */
	public void getTheOneView() {
		// 空判断
		if (null == mAdapter) {
			return;
		}
		// 得到当前view
		HashMap<Integer, HiveBaseView> hash = mAdapter.getViews();
		pageView = (OnVoiceEpgPageItem) hash.get(pageIndex);
		OnVoiceEpgPageItem pageViewNow = (OnVoiceEpgPageItem) mAdapter.getCurrentPageView();
		if (null != pageViewNow && pageViewNow.equals(pageView)) {
			if (null != pageView) {
				Log.d(TAG, "::pageView.getTheInTimeView==>indexView:::" + itemIndex);

				handler.sendEmptyMessage(VIEW_REQUEST_FOCUS);
			} else {
				Log.d(TAG, "::mAdapter.addDataSource");
				mAdapter.addDataSource(currentEpgList, pageIndex, itemIndex);
			}
		}
	}

	// 当前页码
	int pageIndex = 0;
	// 当前页的第几条
	int itemIndex = 0;

	private void setData() {
		// 空校验
		if (null == currentEpgList) {
			return;
		}
		// 当获取的节目列表的第一个节目的开始时间的小时是前一天的20或21或22或23时的时候，去掉这个节目
		removeFirstProgramEntity(currentEpgList);
		// 获取正在直播的位置
		getEpgListLateDate();
		// 设置设配器
		if (null == mAdapter) {
			mAdapter = new HivePagerAdapter(mContext,
					new OnVoiceEpgPageViewFactory(new ItemViewFocusListener(), new ItemCallBackItemViewKeyListener()), vpSelectEpg, 7,
					new NeighborOneLineStrategy());
		} else {
			mAdapter.clear();
		}

		if (playingIndex != -1) {
			pageIndex = playingIndex / 7;
			itemIndex = playingIndex % 7;
		}
		// 设置总条数
		mAdapter.setDataTotalSize(currentEpgList.size());
		// 设置data
		// mAdapter.addDataSource(currentEpgList);
		// 设置设配器
		vpSelectEpg.setAdapter(mAdapter);
		// 设置预加载
		mAdapter.addDataSource(currentEpgList, pageIndex);
		vpSelectEpg.setPreloadingListener(new FastPreloadingListener());
		vpSelectEpg.setVisibility(View.VISIBLE);
		// 跳转到到直播位置

		// if (null != currentEpgList && currentEpgList.size() > PAGE_COUNT) {
		// ivArrowRight.setVisibility(View.VISIBLE);
		// ivArrowLeft.setVisibility(View.INVISIBLE);
		// }

		// 获取一共多少页
		currentPageCount = getPageSize(currentEpgList);
		Log.i(TAG, "currentPageCount:" + currentPageCount + ",currentEpgList.size() :" + currentEpgList.size() + ",pageIndex:" + pageIndex);
		if (pageIndex == 0 && currentEpgList.size() > PAGE_COUNT) {
			ivArrowRight.setVisibility(View.VISIBLE);
			ivArrowLeft.setVisibility(View.INVISIBLE);
		} else if (pageIndex == (currentPageCount - 1) && currentEpgList.size() > PAGE_COUNT) {
			ivArrowLeft.setVisibility(View.VISIBLE);
			ivArrowRight.setVisibility(View.INVISIBLE);
		} else if (currentEpgList.size() > PAGE_COUNT) {
			ivArrowLeft.setVisibility(View.VISIBLE);
			ivArrowRight.setVisibility(View.VISIBLE);
		} else {
			ivArrowLeft.setVisibility(View.INVISIBLE);
			ivArrowRight.setVisibility(View.INVISIBLE);
		}
		getTheOneView();
	}

	/**
	 * 获取正在直播的位置
	 * 
	 * @Title: OnliveEpg
	 * @author:郭松胜
	 * @Description: TODO
	 */
	private void getEpgListLateDate() {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar nowDate = Calendar.getInstance();
		String nowTime = nowDate.get(Calendar.YEAR) + "-" + (nowDate.get(Calendar.MONTH) + 1) + "-" + nowDate.get(Calendar.DAY_OF_MONTH);// maliang
		for (ProgramEntity entity : currentEpgList) {
			boolean isNowDay = false;
			try {
				isNowDay = (sFormat.parse(nowTime).getTime() == sFormat.parse(entity.getDate()).getTime());

				if (AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())
						&& !AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getEnd_time()) && isNowDay) {
					playingIndex = currentEpgList.indexOf(entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	/**
	 * ItemView上焦点改变事件回调接口
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ItemViewFocusListener implements CallBackItemViewFocusListener {
		@Override
		public void onCallBackFocusChange(View view, boolean has) {
			Log.d(TAG, "get the fouch::" + view);
			ProgramEntity programEntity = (ProgramEntity) view.getTag();
			if (has && null != programEntity) {
				if (vpSelectEpg.getSelectedPageIndex() + 1 == getPageSize(currentEpgList)) {
					if (getLastPageViewCount(currentEpgList) <= PAGE_COUNT && programEntity.getPositionInItemView() == (PAGE_COUNT - 1)) {
						view.setNextFocusRightId(view.getId());
					} else if (getLastPageViewCount(currentEpgList) < PAGE_COUNT
							&& programEntity.getPositionInItemView() == (getLastPageViewCount(currentEpgList) - 1)) {
						view.setNextFocusRightId(view.getId());
					} else if (getLastPageViewCount(currentEpgList) == PAGE_COUNT && programEntity.getPositionInItemView() == (PAGE_COUNT - 1)) {
						view.setNextFocusRightId(view.getId());
					}
				}
			}

		}
	}

	public static void getEpgPage(ONVoiceEpgInterface onLiveEpgInterface) {
		/*
		 * start by guosongsheng CHANNEL_CODE参数传空在new LiveEpgInfoFragment
		 * 的时候会将中的LiveEpgInfoFragment CHANNEL_CODE制空
		 */
		onLiveEpgInterface.getEpgPage(new VoiceOnliveEpg(mContext, null));
		/* end by guosongsheng */
	}
	public interface ONVoiceEpgInterface {
		public void getEpgPage(ONVoiceFoursInterface foursInterface);
	}


	class ItemCallBackItemViewKeyListener implements CallBackItemViewKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			return false;
		}

	}

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
			ivArrowRight.setVisibility(View.VISIBLE);
		}

		/**
		 * 最后一页
		 */
		@Override
		public void onLastPage() {
			currentPageCount = getPageSize(currentEpgList);
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
			currentPageCount = getPageSize(currentEpgList);
			if (currentEpgList.size() < PAGE_COUNT) {
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
	 * 根据数据个数计算页数
	 * 
	 * @param list
	 * @return
	 */
	private int getPageSize(List<ProgramEntity> list) {
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
	private int getLastPageViewCount(List<ProgramEntity> list) {
		if (null == list) {
			return 0;
		}
		int count = list.size();
		if (0 == count) {
			return 0;
		}
		return count % PAGE_COUNT;
	}

	/**
	 * 如果adapter不为空时通知数据变换
	 * 
	 * @Title: OnliveEpg
	 * @author:guosongsheng
	 * @Description: TODO
	 */
	public void notifyDataSetChanged() {
		if (null != mAdapter) {
			getChannelsByNames();
		}
	}

	public void unRegisterReceiver() {
		if (isegisterReceiver) {
//			Log.i(TAG, "/unRegisterReceiver....................................." + isegisterReceiver);
			mContext.unregisterReceiver(dataReceiver);
			isegisterReceiver = false;
		}
	}

	/**
	 * 当前节目单View
	 */
	private static View currentEpgView;
	/**
	 * 当前节目单实体
	 */
	private static ProgramEntity currententity;
	/**
	 * 当前节目单实体
	 */
	private static LinearLayout currentlayout;

	public void getEpgFours(ProgramEntity entity, View view, LinearLayout layout) {
		// TODO Auto-generated method stub
		if (null != view) {
			currentEpgView = view;
			currententity = entity;
			currentlayout = layout;
		}
	}

	public ProgramEntity getEntity() {
		return currententity;
	}

	public LinearLayout getEpgLayout() {
		return currentlayout;
	}

}
