package com.hiveview.tv.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborOneLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.LiveEpgInfoActivity;
import com.hiveview.tv.common.factory.EpgPageViewFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.dao.TvSetTopDAO;
import com.hiveview.tv.service.entity.ProgramEntity;
import com.hiveview.tv.service.entity.StringEntity;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.television.EpgPageItem.EpgFoursInterface;
import com.hiveview.tv.view.television.ViewPageAdapter;

@SuppressLint("ValidFragment")
public class LiveEpgInfoFragment extends Fragment implements EpgFoursInterface {

	private static final String TAG = "FragmnetTv";
	/**
	 *  置顶按钮的ID
	 */
	public static final int BUTTON_ID = 5;
	/**
	 * 用户置顶成功标志位
	 */
	private final int DB_SET_TOP_SUCCESS = 110;

	private HivePreloadViewPager pager;

	private String dateType;
	private int tage;
	private static ArrayList<ProgramEntity> epgList = null;

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

	/**
	 * 向左翻页图片
	 */
	private ImageView leftImageView ;
	/**
	 * 向右翻页图片
	 */
	private ImageView rightImageView ;
	
	/**
	 * 是否已置顶
	 * 
	 * @Fields isTops
	 */
	private static boolean isTops = false;
	
	/**
	 * 当前VIewpager的页数
	 */
	private int currentPageCount = 0;
	/**
	 * 当前页ViewPager的位置
	 */
	private int currentPageIndex = 0;
	/**
	 * 每页的个数
	 */
	private int PAGE_SIZE = 7;

	public int getTage() {
		return tage;
	}

	public void setTage(int tage) {
		this.tage = tage;
	}

	/**
	 * 电台编号
	 */
	private static String CHANNEL_CODE;

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	private HivePagerAdapter hdAdapter;

	private final int RQUEST_DATA_WEEK3_SUCCESS = 100;
	private final int RQUEST_DATA_WEEK3_FAIL = -100;

	private ViewPageAdapter mAdapter;

	public LiveEpgInfoFragment(String channel_code, boolean isToped) {
		CHANNEL_CODE = channel_code;
		isTops = isToped;

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RQUEST_DATA_WEEK3_SUCCESS:
				mRelativeLayout.setVisibility(View.VISIBLE);
				int pageIndex = 0;
				int itemIndex = 0;
				if (playingIndex != -1) {
					pageIndex = playingIndex / 7;
					itemIndex = playingIndex % 7;
					hdAdapter.addDataSource(epgList, pageIndex, itemIndex);
				} else {
					hdAdapter.addDataSource(epgList);
				}
				hdAdapter.setDataTotalSize(epgList.size());
				changeTab();
				break;
			case RQUEST_DATA_WEEK3_FAIL:
				mRelativeLayout.setVisibility(View.GONE);
				break;
			case DB_SET_TOP_SUCCESS:// 置顶成功
				// 置顶成功后焦点移到对应的节目单上
				currentEpgView.requestFocus();
				button.setVisibility(View.GONE);
				alert(getResources().getString(R.string.alert_place_top));
				currentEpgView = null;
				break;
			default:
				break;
			}

		}
	};

	
	
	private RelativeLayout mRelativeLayout = null;
	private boolean isSuccess = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		infoActivity = (LiveEpgInfoActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	private TvSetTopDAO topDao;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRelativeLayout = initView();
		topDao = new TvSetTopDAO(getActivity());
		if (hdAdapter == null) {
			pager = new HivePreloadViewPager(getActivity());
			hdAdapter = new HivePagerAdapter(getActivity(), new EpgPageViewFactory(new ItemViewFocusListener()/* focusListener */, keyListener), pager,
					7, new NeighborOneLineStrategy());
			pager.setAdapter(hdAdapter);
			pager.setPreloadingListener(new FastPreloadingListener());
			getEpgListLateDate();
		}else{
			changeTab();
		}

		RelativeLayout.LayoutParams mLayoutParams_Adapter = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLayoutParams_Adapter.topMargin = 363;
		// pager.setEpgFoursInterface
		// add ViewPager
		mRelativeLayout.addView(pager, mLayoutParams_Adapter);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mvTop();
			}
		});
		// start author:zhangpengzhan 没有数据的时候不显示该布局
		mRelativeLayout.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
		// end
		return mRelativeLayout;
	}

	
	public void mvTop(){
		/* start by guosongsheng 添加置顶 */
		isTops = true;
		topDao.insert(new StringEntity(CHANNEL_CODE));
		handler.sendEmptyMessage(DB_SET_TOP_SUCCESS);
		getActivity().sendBroadcast(new Intent("com.hiveview.tv.TV_CHANNEL_SET_TOP_ACTION"));
		/* end by guosongsheng */
	}
	/**
	 * 初始化布局
	 * 
	 * @Title: LiveEpgInfoFragment
	 * @author:周一川`
	 * @Description: TODO
	 * @return
	 */
	private RelativeLayout initView() {
		// 父窗体
		RelativeLayout mRelativeLayout = new RelativeLayout(getActivity());
		mRelativeLayout.setId(1);
		// 设置父窗体的大小
		mRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// 设置子控件相对父窗体的位置
		RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
		mLayoutParams.topMargin = 424;
		mLayoutParams.leftMargin = 51;
		mLayoutParams.rightMargin = 68;
		View view = new View(getActivity());
		view.setId(2);
		// start author : zhangpengzhan
		view.setBackgroundColor(Color.WHITE);
		// end
		// add view
		mRelativeLayout.addView(view, mLayoutParams);

		RelativeLayout.LayoutParams mLayoutParams_image = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams_image.topMargin = 408;
		mLayoutParams_image.leftMargin = 1220;
		rightImageView = new ImageView(getActivity());
		rightImageView.setId(3);
		rightImageView.setBackgroundResource(R.drawable.iv_page_right);
		// add ImageView
		mRelativeLayout.addView(rightImageView, mLayoutParams_image);

		RelativeLayout.LayoutParams mLayoutParams_image_right = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams_image_right.topMargin = 408;
		mLayoutParams_image_right.leftMargin = 30;
		leftImageView = new ImageView(getActivity());
		leftImageView.setId(4);
		leftImageView.setBackgroundResource(R.drawable.iv_page_left);
		// add ImageView
		mRelativeLayout.addView(leftImageView, mLayoutParams_image_right);

		RelativeLayout.LayoutParams mLayoutParams_button = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams_button.topMargin = 630;
		mLayoutParams_button.leftMargin = 530;
		button = new Button(getActivity());

		button.setTextColor(Color.parseColor("#ffffff"));
		button.setText("置顶本台电视页");
		button.setPadding(20, 5, 20, 10);
		// 设置Button的Id为5
		button.setId(BUTTON_ID);
		// start author:zhangpengzhan
		button.setNextFocusLeftId(button.getId());
		button.setNextFocusRightId(button.getId());
		// end
		button.setTextSize(22);
		button.setBackgroundResource(R.drawable.select_focus_bg);
		/* start by guosongsheng 根据是否置顶参数 判断是否显示置顶按钮 */
		// 已经置顶
		if (isTops) {
			button.setVisibility(View.INVISIBLE);
		} else {
			button.setVisibility(View.VISIBLE);
		}
		/* end by guosongsheng */
		// add Button
		mRelativeLayout.addView(button, mLayoutParams_button);

		return mRelativeLayout;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private int playingIndex = -1;

	/**
	 * 根据频道的得到节目单 加载周三的节目
	 */
	private void getEpgListLateDate() {
		submitRequest(new Runnable() {

			@Override
			public void run() {
				epgList = (ArrayList<ProgramEntity>) new HiveTVService().getProgramsByChannel(CHANNEL_CODE, dateType + " 00:00", dateType + " 24:00");
				System.out.println(epgList.size() + "================" + dateType);
				if (epgList == null || epgList.size() == 0) {
					handler.sendEmptyMessage(RQUEST_DATA_WEEK3_FAIL);
					handlerChildThread();
					return;
				} else {
					isSuccess = true;
					// 当获取的节目列表的第一个节目的开始时间的小时是前一天的20或21或22或23时的时候，去掉这个节目
					removeFirstProgramEntity(epgList);
					SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
					Calendar nowDate = Calendar.getInstance();
					String nowTime = nowDate.get(Calendar.YEAR) + "-" + (nowDate.get(Calendar.MONTH) + 1) + "-" + nowDate.get(Calendar.DAY_OF_MONTH);// maliang
					for (ProgramEntity entity : epgList) {
						boolean isNowDay = false;
						try {
							isNowDay = (sFormat.parse(nowTime).getTime() == sFormat.parse(entity.getDate()).getTime());

							if (AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getStart_time())
									&& !AppUtil.nowDateTimeIsAferBoolean(entity.getDate() + " " + entity.getEnd_time()) && isNowDay) {
								Log.d(TAG, "==存在正在直播==");
								playingIndex = epgList.indexOf(entity);
							}
						} catch (Exception e) {
							e.printStackTrace();
							continue;
						}
					}

					handler.sendEmptyMessage(RQUEST_DATA_WEEK3_SUCCESS);
				}
			}
		});
	}

	/**
	 * 得到当前数据list
	 * @Title: LiveEpgInfoFragment
	 * @author:张鹏展
	 * @Description: TODO
	 * @return
	 */
	public static ArrayList<ProgramEntity> getEpgList() {
		return epgList;
	}

	private void handlerChildThread() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				// changeViewPagerVisibility(false);
			}
		});
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
	 * 发送网络请求请求数据
	 * 
	 * @param runnable
	 *            请求数据的耗时操作
	 */
	protected final void submitRequest(Runnable runnable) {
		if (null != runnable) {
			HttpTaskManager.getInstance().submit(runnable);
		}
	}

	/**
	 * 设置数据
	 * 
	 * @param epgList
	 */
	// private void setDataSources(ArrayList<ProgramEntity> epgList) {
	// int count = 0;
	// // 计算页数
	// if (epgList != null && epgList.size() != 0) {
	// if (epgList.size() % 7 == 0) {
	// count = epgList.size() / 7;
	// } else {
	// count = epgList.size() / 7 + 1;
	// }
	// }
	// selectEpgList.clear();
	// for (int i = 0; i < count; i++) {
	// EpgPageItem item = new EpgPageItem(getActivity());
	// item.setDataSources(epgList, i);
	// selectEpgList.add(item);
	//
	// }
	// mAdapter.notifyDataSetChanged();
	// Log.d(TAG, "after notifyDataSetChanged()");
	// // if (isFirst) {
	// // isFirst = false;
	// // // vpSelectEpg.setCurrentItem(locationId);
	// // EpgPageItem itemId = (EpgPageItem) selectEpgList.get(locationId);
	// // if (itemId != null) {
	// // itemId.isPlayLocation((isPlayLocations + 1));
	// // }
	// // }
	// }

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mRelativeLayout.removeView(pager);
	}

	private CallBackItemViewKeyListener keyListener;

	private static LiveEpgInfoActivity infoActivity;

	private Button button;

	/**
	 * @Title: LiveEpgInfoFragment
	 * @author:周一川
	 * @Description: TODO
	 * @param itemViewKeyListener
	 *            activity 回调 fragment的监听
	 */
	public void setItemViewKeyListener(CallBackItemViewKeyListener itemViewKeyListener) {
		this.keyListener = itemViewKeyListener;
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
			mFocusListene.onCallBackFocusListener(view, has);
		}
	}

	/**
	 * @ClassName: ItemViewFocusCallBackListener
	 * @Description: TODO焦点监听回调借口
	 * @author: zhangpengzhan
	 * @date 2014年8月7日 上午11:10:48
	 * 
	 */
	public interface ItemViewFocusCallBackListener {
		public void onCallBackFocusListener(View v, boolean has);
	}

	/**
	 * @Fields mFocusListene:TODO焦点监听回调接口
	 */
	public ItemViewFocusCallBackListener mFocusListene;

	public void setItemViewFocusCallBackListener(ItemViewFocusCallBackListener mFocusListener) {
		this.mFocusListene = mFocusListener;
	}

	public static void getEpgPage(EpgPageInterface id) {
		/*
		 * start by guosongsheng CHANNEL_CODE参数传空在new LiveEpgInfoFragment
		 * 的时候会将中的LiveEpgInfoFragment CHANNEL_CODE制空
		 */
		id.getEpgPage(epgList, CHANNEL_CODE, infoActivity, new LiveEpgInfoFragment(CHANNEL_CODE, isTops));
		/* end by guosongsheng */
	}

	public interface EpgPageInterface {
		public void getEpgPage(ArrayList<ProgramEntity> entity, String CHANNEL_CODE, Context contex, EpgFoursInterface foursInterface);
	}

	@Override
	public void getEpgFours(ProgramEntity entity, View view,LinearLayout layout) {
		if (null != view) {
			currentEpgView = view;
			currententity=entity;
			currentlayout=layout;
		}
	}
	
	public ProgramEntity getEntity(){
		return currententity;
	}
	
	public LinearLayout getEpgLayout(){
		return currentlayout;
	}

	/**
	 * 用户提示
	 * 
	 * @param text
	 */
	protected void alert(String text) {
		ToastUtils.alert(getActivity(), text);
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
			leftImageView.setVisibility(View.INVISIBLE);
		}

		/**
		 * 最后一页
		 */
		@Override
		public void onLastPage() {
			currentPageCount = getPageSize();
			// 最后一页
			if (currentPageIndex == currentPageCount) {
				rightImageView.setVisibility(View.INVISIBLE);
				leftImageView.setVisibility(View.VISIBLE);
			}
		}

		/**
		 * 设置当前页
		 */
		@Override
		public void setPageCurrent(int pageIndex) {
			currentPageIndex = pageIndex;
			currentPageCount = getPageSize();
			if (epgList.size() < PAGE_SIZE) {
				// 只有一页时
				rightImageView.setVisibility(View.INVISIBLE);
				leftImageView.setVisibility(View.INVISIBLE);
			} else {
				// 有多页时
				if (pageIndex == 1) {
					// 第一页
					leftImageView.setVisibility(View.INVISIBLE);
					rightImageView.setVisibility(View.VISIBLE);
				} else if (pageIndex == currentPageCount) {
					// 最后一页
					rightImageView.setVisibility(View.INVISIBLE);
					leftImageView.setVisibility(View.VISIBLE);
				} else {
					// 除第一页最后一页的 其他页
					rightImageView.setVisibility(View.VISIBLE);
					leftImageView.setVisibility(View.VISIBLE);
				}
			}
		}

	}
	
	/**
	 * 切换tab时改变左右翻页图片的显示 每次切换tab时根据viewPager的状态初始化
	 */
	public void changeTab() {
		if(null == epgList || epgList.size() == 0){
			return ;
		}
		int currentSize = epgList.size();
		// 获取当前ViewPager的currentItem 从0开始
		int currentItem = pager.getCurrentItem();

		// 获取当前ViewPager的总页数
		currentPageCount = getPageSize();
		if (currentSize < PAGE_SIZE) {
			// 只有一页
			rightImageView.setVisibility(View.INVISIBLE);
			leftImageView.setVisibility(View.INVISIBLE);
		} else {
			// 有多页时
			if (currentItem == 0) {
				// 第一页
				rightImageView.setVisibility(View.VISIBLE);
				leftImageView.setVisibility(View.INVISIBLE);
			} else if (currentItem == currentPageCount - 1) {
				// 最后一页
				rightImageView.setVisibility(View.INVISIBLE);
				leftImageView.setVisibility(View.VISIBLE);
			} else {
				// 除第一页最后一页的其他页
				rightImageView.setVisibility(View.VISIBLE);
				leftImageView.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * 根据数据个数计算页数
	 * 
	 * @param list
	 * @return
	 */
	private int getPageSize() {
		return (int) Math.ceil(epgList.size() / (double) PAGE_SIZE);
	}

}
