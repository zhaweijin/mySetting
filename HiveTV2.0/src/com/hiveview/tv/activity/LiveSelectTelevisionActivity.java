package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.factory.TvSelectTelevisionPageViewFactoryItem;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.LiveMediaEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.KeyMappingHashMapUtil;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.utils.SwitchChannelUtils;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.TvClassifyViewItemView;
import com.hiveview.tv.view.onlive.OnliveSelect;
import com.iflytek.xiri.Feedback;
import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.paster.util.JsonUtil;

@SuppressLint("HandlerLeak")
/***
 * 分类选台的active
 * @author maliang 2014-04-22 
 *
 */
public class LiveSelectTelevisionActivity extends BaseActivity {

	private View vTvChannel;
	private View vMovieChannel;
	private View vSportsChannel;
	private View vVarietyChannel;
	private View vChildrenChannel;
	private View vTechnologyChannel;
	private View vEconomicsChannel;
	private View vComprehensiveChannel;

	private TextView tvTV;
	private TextView tvMovie;
	private TextView tvSports;
	private TextView tvVariety;
	private TextView tvChildren;
	private TextView tvTechnology;
	private TextView tvEconomics;
	private TextView tvComprehensive;
	private TextView[] tvAll;

	private TextView tvChannelsCount;
	private ProgressDialog mProgressDialog;
	private RelativeLayout rlContainer;

	private HivePreloadViewPager tvViewPager;
	private HivePreloadViewPager movieViewPager;
	private HivePreloadViewPager sportsViewPager;
	private HivePreloadViewPager varietyViewPager;
	private HivePreloadViewPager childrenViewPager;
	private HivePreloadViewPager technologyViewPager;
	private HivePreloadViewPager economicsViewPager;
	private HivePreloadViewPager comprehensiveViewPager;
	private HivePreloadViewPager[] viewPagerAll;

	private HivePagerAdapter tvAdapter = null;
	private HivePagerAdapter movieAdapter = null;
	private HivePagerAdapter sportsAdapter = null;
	private HivePagerAdapter varietyAdapter = null;
	private HivePagerAdapter childrenAdapter = null;
	private HivePagerAdapter technologyAdapter = null;
	private HivePagerAdapter economicsAdapter = null;
	private HivePagerAdapter comprehensiveAdapter = null;

	private final int REQUEST_TV_CHANNEL_SUCCESS = 1000;
	private final int REQUEST_TV_CHANNEL_FAIL = -1000;

	private final int REQUEST_MOVIE_CHANNEL_SUCCESS = 2001;
	private final int REQUEST_MOVIE_CHANNEL_FAIL = -2001;

	private final int REQUEST_SPORTS_CHANNEL_SUCCESS = 3002;
	private final int REQUEST_SPORTS_CHANNEL_FAIL = -3002;

	private final int REQUEST_VARIETY_CHANNEL_SUCCESS = 4003;
	private final int REQUEST_VARIETY_CHANNEL_FAIL = -4003;

	private final int REQUEST_CHILDREN_CHANNEL_SUCCESS = 5004;
	private final int REQUEST_CHILDREN_CHANNEL_FAIL = -5004;

	private final int REQUEST_TECHNOLOGY_CHANNEL_SUCCESS = 6005;
	private final int REQUEST_TECHNOLOGY_CHANNEL_FAIL = -6005;

	private final int REQUEST_ECONOMICS_CHANNEL_SUCCESS = 7006;
	private final int REQUEST_ECONOMICS_CHANNEL_FAIL = -7006;

	private final int REQUEST_COMPREHENSIVE_CHANNEL_SUCCESS = 1007;
	private final int REQUEST_COMPREHENSIVE_CHANNEL_FAIL = -1007;

	private List<LiveMediaEntity> tvChannelList;
	private List<LiveMediaEntity> movieChannelList;
	private List<LiveMediaEntity> sportsChannelList;
	private List<LiveMediaEntity> varietyChannelList;
	private List<LiveMediaEntity> childrenChannelList;
	private List<LiveMediaEntity> technologyChannelList;
	private List<LiveMediaEntity> economicsChannelList;
	private List<LiveMediaEntity> comprehensiveChannelList;
	/**
	 * 右翻页图片
	 */
	private View ivPagerRight;
	/**
	 * 左翻页图片
	 */
	private View ivPagerLeft;
	/**
	 * 当前VIewpager的页数
	 */
	private int currentPageCount = 0;
	/**
	 * 当前页ViewPager的位置
	 */
	private int currentPageIndex = 0;

	private int mChannelsCount = 0;// 电视台总数
	/**
	 * 科大讯飞ok键的keyCode
	 */
	private static int KEYCODE = 66;

	/**
	 * 从ViewPager中的ItemView上往上按键焦点落到Id为mUpFocusId的View上
	 */
	private int mUpFocusId = 0;
	private int mShowViewPagerIndex = 0;
	private boolean isFromLastItemView = false;// 分类切换方式标志(判断是从tab切换还是从下方viewpager切换)

	protected int PAGE_COUNT = 8;// 每一页电视台最多10个
	// protected int recCountTv=0;//电视台的总的电视台数
	// protected int recCountMovie=0;//电影的总的电视台数
	// protected int recCountSports=0;//体育的总的电视台数
	// protected int recCountVariety=0;//综艺的总的电视台数
	// protected int recCountChildren=0;//少儿的总的电视台数
	// protected int recCountTechnology=0;//科技的总的电视台数
	// protected int recCountEconomics=0;//财经的总的电视台数
	// protected int recCountComprehensive=0;//综合的总的电视台数
	private Integer[] recCount = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0 };
	private String TAG = "LiveSelectTelevisionActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_select_television);
		init();
		mProgressDialog.setVisibility(View.VISIBLE);
		rlContainer.setVisibility(View.INVISIBLE);

		tvTV.setText(String.format(getString(R.string.tv_count), ".."));
		tvMovie.setText(String.format(getString(R.string.film_count), ".."));
		tvSports.setText(String.format(getString(R.string.sports_count), ".."));
		tvVariety.setText(String.format(getString(R.string.variety_count), ".."));
		tvChildren.setText(String.format(getString(R.string.children_count), ".."));
		tvTechnology.setText(String.format(getString(R.string.technology_count), ".."));
		tvEconomics.setText(String.format(getString(R.string.economics_count), ".."));
		tvComprehensive.setText(String.format(getString(R.string.comprehensive_count), ".."));

		tvChannelsCount.setText(String.format(getString(R.string.television_count), mChannelsCount));

		requestTvChannels();
		requestMovieChannels();
		requestSportsChannels();
		requestVarietyChannels();
		requestChildrenChannels();
		requestTechnologyChannels();
		requestEconomicsChannels();
		requestComprehensiveChannels();
	}

	/***
	 * 初始化各个组件
	 */
	private void init() {
		tvChannelsCount = (TextView) findViewById(R.id.tv_television_count);
		tvTV = (TextView) this.findViewById(R.id.tv_television);
		tvMovie = (TextView) this.findViewById(R.id.tv_film);
		tvSports = (TextView) this.findViewById(R.id.tv_sports);
		tvVariety = (TextView) this.findViewById(R.id.tv_variety);
		tvChildren = (TextView) this.findViewById(R.id.tv_children);
		tvTechnology = (TextView) this.findViewById(R.id.tv_technology);
		tvEconomics = (TextView) this.findViewById(R.id.tv_economics);
		tvComprehensive = (TextView) this.findViewById(R.id.tv_comprehensive);
		tvAll = new TextView[] { tvTV, tvMovie, tvSports, tvVariety, tvChildren, tvTechnology, tvEconomics, tvComprehensive };

		vTvChannel = findViewById(R.id.fl_television);
		vMovieChannel = findViewById(R.id.fl_film);
		vSportsChannel = findViewById(R.id.fl_sports);
		vVarietyChannel = findViewById(R.id.fl_variety);
		vChildrenChannel = findViewById(R.id.fl_children);
		vTechnologyChannel = findViewById(R.id.fl_technology);
		vEconomicsChannel = findViewById(R.id.fl_economics);
		vComprehensiveChannel = findViewById(R.id.fl_comprehensive);

		// 左右翻页图片
		ivPagerRight = findViewById(R.id.iv_pager_right);
		ivPagerLeft = findViewById(R.id.iv_pager_left);
		// 初始化隐藏
		ivPagerLeft.setVisibility(View.INVISIBLE);
		ivPagerRight.setVisibility(View.INVISIBLE);

		tvViewPager = (HivePreloadViewPager) findViewById(R.id.vp_select_tv);
		movieViewPager = (HivePreloadViewPager) findViewById(R.id.vp_select_movie);
		sportsViewPager = (HivePreloadViewPager) findViewById(R.id.vp_select_sports);
		varietyViewPager = (HivePreloadViewPager) findViewById(R.id.vp_seleect_variety);
		childrenViewPager = (HivePreloadViewPager) findViewById(R.id.vp_seleect_children);
		technologyViewPager = (HivePreloadViewPager) findViewById(R.id.vp_select_technology);
		economicsViewPager = (HivePreloadViewPager) findViewById(R.id.vp_select_economics);
		comprehensiveViewPager = (HivePreloadViewPager) findViewById(R.id.vp_select_comprehensive);

		viewPagerAll = new HivePreloadViewPager[] { tvViewPager, movieViewPager, sportsViewPager, varietyViewPager, childrenViewPager,
				technologyViewPager, economicsViewPager, comprehensiveViewPager };

		setAllViewPagerVisibility(0);

		vTvChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vMovieChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vSportsChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vVarietyChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vChildrenChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vTechnologyChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vEconomicsChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());
		vComprehensiveChannel.setOnFocusChangeListener(new ChannelTabFocusChangeListener());

		// 设置没有数据时焦点不可以向下移动
		vTvChannel.setNextFocusDownId(R.id.fl_television);
		vMovieChannel.setNextFocusDownId(R.id.fl_film);
		vSportsChannel.setNextFocusDownId(R.id.fl_sports);
		vVarietyChannel.setNextFocusDownId(R.id.fl_variety);
		vChildrenChannel.setNextFocusDownId(R.id.fl_children);
		vTechnologyChannel.setNextFocusDownId(R.id.fl_technology);
		vEconomicsChannel.setNextFocusDownId(R.id.fl_economics);
		vComprehensiveChannel.setNextFocusDownId(R.id.fl_comprehensive);

		mProgressDialog = (ProgressDialog) this.findViewById(R.id.iv_hiveview_loading);
		rlContainer = (RelativeLayout) this.findViewById(R.id.rl_container);
		setAllPreloadingListener();

	}

	/***
	 * 给所有viewpager设置FastPreloadingListener
	 */
	public void setAllPreloadingListener() {
		for (int i = 0; i < viewPagerAll.length; i++) {
			viewPagerAll[i].setPreloadingListener(new FastPreloadingListener());
		}
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
					viewPagerAll[i].setVisibility(View.VISIBLE);
					if (viewPagerAll[i].getCurrentItem() > 0) {
						HivePagerAdapter adapter = (HivePagerAdapter) viewPagerAll[i].getAdapter();
						viewPagerAll[i].setAdapter(adapter);
					}
				}
			} else {
				hivePreloadViewPager.setVisibility(View.INVISIBLE);

			}
		}
	}

	/***
	 * 设置各个tab的textview的颜色
	 * 
	 * @param currentViewPager
	 */
	public void setAllTextViewColor(int currentViewPager) {
		for (int i = 0; i < tvAll.length; i++) {
			tvAll[i].setTextColor(Color.parseColor("#a8a8a8"));
			if (i == currentViewPager) {
				tvAll[i].setTextColor(Color.parseColor("#FFFFFF"));
			}
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
			if (recCount[mShowViewPagerIndex] < PAGE_COUNT) {
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

	@Override
	protected void processData(int msgWhat) {
		mProgressDialog.setVisibility(View.INVISIBLE);
		rlContainer.setVisibility(View.VISIBLE);
		switch (msgWhat) {
		case REQUEST_TV_CHANNEL_SUCCESS:// 电视剧频道
			mChannelsCount += tvChannelList.size();
			recCount[0] = tvChannelList.size();
			if (recCount[0] == 0) {
				vTvChannel.setNextFocusDownId(R.id.fl_television);
				vTvChannel.setVisibility(View.GONE);
			} else {
				vTvChannel.setVisibility(View.VISIBLE);
				vTvChannel.setNextFocusDownId(R.id.vp_select_tv);
			}
			tvTV.setText(String.format(getString(R.string.tv_count), tvChannelList.size()));
			tvAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(), new ItemViewKeyListener()),
					tvViewPager, 8, new NeighborTwoLineStrategy());
			tvAdapter.addDataSource(tvChannelList);
			tvAdapter.setDataTotalSize(tvChannelList.size());
			tvViewPager.setAdapter(tvAdapter);
			// 初始化央视是否显示向右翻页的图片
			if (PAGE_COUNT < tvChannelList.size()) {
				ivPagerRight.setVisibility(View.VISIBLE);
			}
			Log.i(TAG, "...........................tvChannelList.size():" + tvChannelList.size());
			break;
		case REQUEST_TV_CHANNEL_FAIL:
			tvTV.setText(String.format(getString(R.string.tv_count), 0));
			recCount[0] = 0;
			vTvChannel.setNextFocusDownId(R.id.fl_television);
			vTvChannel.setVisibility(View.GONE);
			Log.d("processData", "电视剧请求失败");
			break;
		case REQUEST_MOVIE_CHANNEL_SUCCESS:// 电影频道
			mChannelsCount += movieChannelList.size();
			recCount[1] = movieChannelList.size();
			if (recCount[1] == 0) {
				vMovieChannel.setNextFocusDownId(R.id.fl_film);
				vMovieChannel.setVisibility(View.GONE);
			} else {
				vMovieChannel.setVisibility(View.VISIBLE);
				vMovieChannel.setNextFocusDownId(R.id.vp_select_movie);
			}
			tvMovie.setText(String.format(getString(R.string.film_count), movieChannelList.size()));
			movieAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), movieViewPager, 8, new NeighborTwoLineStrategy());
			movieAdapter.addDataSource(movieChannelList);
			movieAdapter.setDataTotalSize(movieChannelList.size());
			movieViewPager.setAdapter(movieAdapter);
			Log.i(TAG, "...........................movieChannelList.size():" + movieChannelList.size());
			break;
		case REQUEST_MOVIE_CHANNEL_FAIL:
			tvMovie.setText(String.format(getString(R.string.film_count), 0));
			recCount[1] = 0;
			vMovieChannel.setNextFocusDownId(R.id.fl_film);
			vMovieChannel.setVisibility(View.GONE);
			Log.d("processData", "电影请求失败");
			break;
		case REQUEST_SPORTS_CHANNEL_SUCCESS:// 体育频道
			mChannelsCount += sportsChannelList.size();
			recCount[2] = sportsChannelList.size();
			if (recCount[2] == 0) {
				vSportsChannel.setNextFocusDownId(R.id.fl_sports);
				vSportsChannel.setVisibility(View.GONE);
			} else {
				vSportsChannel.setVisibility(View.VISIBLE);
				vSportsChannel.setNextFocusDownId(R.id.vp_select_sports);
			}
			tvSports.setText(String.format(getString(R.string.sports_count), sportsChannelList.size()));
			sportsAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), sportsViewPager, 8, new NeighborTwoLineStrategy());
			sportsAdapter.addDataSource(sportsChannelList);
			sportsAdapter.setDataTotalSize(sportsChannelList.size());
			sportsViewPager.setAdapter(sportsAdapter);
			Log.i(TAG, "...........................sportsChannelList.size():" + sportsChannelList.size());
			break;
		case REQUEST_SPORTS_CHANNEL_FAIL:
			Log.d("processData", "体育请求失败");
			tvSports.setText(String.format(getString(R.string.sports_count), 0));
			recCount[2] = 0;
			vSportsChannel.setNextFocusDownId(R.id.fl_sports);
			vSportsChannel.setVisibility(View.GONE);
			break;
		case REQUEST_VARIETY_CHANNEL_SUCCESS:// 综艺频道
			mChannelsCount += varietyChannelList.size();
			recCount[3] = varietyChannelList.size();
			if (recCount[3] == 0) {
				vVarietyChannel.setNextFocusDownId(R.id.fl_variety);
				vVarietyChannel.setVisibility(View.GONE);
			} else {
				vVarietyChannel.setVisibility(View.VISIBLE);
				vVarietyChannel.setNextFocusDownId(R.id.vp_seleect_variety);
			}
			tvVariety.setText(String.format(getString(R.string.variety_count), varietyChannelList.size()));
			varietyAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), varietyViewPager, 8, new NeighborTwoLineStrategy());
			varietyAdapter.addDataSource(varietyChannelList);
			varietyAdapter.setDataTotalSize(varietyChannelList.size());
			varietyViewPager.setAdapter(varietyAdapter);
			Log.i(TAG, "...........................varietyChannelList.size():" + varietyChannelList.size());
			break;
		case REQUEST_VARIETY_CHANNEL_FAIL:
			Log.d("processData", "综艺请求失败");
			tvVariety.setText(String.format(getString(R.string.variety_count), 0));
			recCount[3] = 0;
			vVarietyChannel.setNextFocusDownId(R.id.fl_variety);
			vVarietyChannel.setVisibility(View.GONE);
			break;
		case REQUEST_CHILDREN_CHANNEL_SUCCESS:// 少儿频道
			mChannelsCount += childrenChannelList.size();
			recCount[4] = childrenChannelList.size();
			if (recCount[4] == 0) {
				vChildrenChannel.setNextFocusDownId(R.id.fl_children);
				vChildrenChannel.setVisibility(View.GONE);
			} else {
				vChildrenChannel.setVisibility(View.VISIBLE);
				vChildrenChannel.setNextFocusDownId(R.id.vp_seleect_children);
			}
			tvChildren.setText(String.format(getString(R.string.children_count), childrenChannelList.size()));
			childrenAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), childrenViewPager, 8, new NeighborTwoLineStrategy());
			childrenAdapter.addDataSource(childrenChannelList);
			childrenAdapter.setDataTotalSize(childrenChannelList.size());
			childrenViewPager.setAdapter(childrenAdapter);
			Log.i(TAG, "...........................childrenChannelList.size():" + childrenChannelList.size());
			break;
		case REQUEST_CHILDREN_CHANNEL_FAIL:
			Log.d("processData", "少儿请求失败");
			tvChildren.setText(String.format(getString(R.string.children_count), 0));
			recCount[4] = 0;
			vChildrenChannel.setNextFocusDownId(R.id.fl_children);
			vChildrenChannel.setVisibility(View.GONE);
			break;

		case REQUEST_TECHNOLOGY_CHANNEL_SUCCESS:// 科技频道
			mChannelsCount += technologyChannelList.size();
			recCount[5] = technologyChannelList.size();
			if (recCount[5] == 0) {
				vTechnologyChannel.setNextFocusDownId(R.id.fl_technology);
				vTechnologyChannel.setVisibility(View.GONE);
			} else {
				vTechnologyChannel.setVisibility(View.VISIBLE);
				vTechnologyChannel.setNextFocusDownId(R.id.vp_select_technology);
			}
			tvTechnology.setText(String.format(getString(R.string.technology_count), technologyChannelList.size()));
			technologyAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), technologyViewPager, 8, new NeighborTwoLineStrategy());
			technologyAdapter.addDataSource(technologyChannelList);
			technologyAdapter.setDataTotalSize(technologyChannelList.size());
			technologyViewPager.setAdapter(technologyAdapter);
			Log.i(TAG, "...........................technologyChannelList.size():" + technologyChannelList.size());
			break;
		case REQUEST_TECHNOLOGY_CHANNEL_FAIL:
			Log.d("processData", "科技请求失败");
			tvTechnology.setText(String.format(getString(R.string.technology_count), 0));
			recCount[5] = 0;
			vTechnologyChannel.setNextFocusDownId(R.id.fl_technology);
			vTechnologyChannel.setVisibility(View.GONE);
			break;

		case REQUEST_ECONOMICS_CHANNEL_SUCCESS:// 财经频道
			mChannelsCount += economicsChannelList.size();
			recCount[6] = economicsChannelList.size();
			if (recCount[6] == 0) {
				// start author：zhangpengzhan
				vEconomicsChannel.setNextFocusDownId(R.id.fl_economics);
				vEconomicsChannel.setVisibility(View.GONE);
			} else {
				vEconomicsChannel.setVisibility(View.VISIBLE);
				vEconomicsChannel.setNextFocusDownId(R.id.vp_select_economics);
			}
			tvEconomics.setText(String.format(getString(R.string.economics_count), economicsChannelList.size()));
			economicsAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), economicsViewPager, 8, new NeighborTwoLineStrategy());
			economicsAdapter.addDataSource(economicsChannelList);
			economicsAdapter.setDataTotalSize(economicsChannelList.size());
			economicsViewPager.setAdapter(economicsAdapter);
			Log.i(TAG, "...........................economicsChannelList.size():" + economicsChannelList.size());
			break;
		case REQUEST_ECONOMICS_CHANNEL_FAIL:
			Log.d("processData", "财经请求失败");
			tvEconomics.setText(String.format(getString(R.string.economics_count), 0));
			recCount[6] = 0;
			vEconomicsChannel.setNextFocusDownId(R.id.fl_economics);
			vEconomicsChannel.setVisibility(View.GONE);
			break;

		case REQUEST_COMPREHENSIVE_CHANNEL_SUCCESS:// 综合频道
			mChannelsCount += comprehensiveChannelList.size();
			recCount[7] = comprehensiveChannelList.size();
			if (recCount[7] == 0) {
				// start author:zhangpengzhan 没有数据的时候，向下按键不移动焦点
				vComprehensiveChannel.setNextFocusDownId(vComprehensiveChannel.getId());
				vComprehensiveChannel.setVisibility(View.GONE);
				// end
			} else {
				vComprehensiveChannel.setVisibility(View.VISIBLE);
				vComprehensiveChannel.setNextFocusDownId(R.id.vp_select_comprehensive);
			}
			tvComprehensive.setText(String.format(getString(R.string.comprehensive_count), comprehensiveChannelList.size()));
			comprehensiveAdapter = new HivePagerAdapter(this, new TvSelectTelevisionPageViewFactoryItem(new ItemViewFocusListener(),
					new ItemViewKeyListener()), comprehensiveViewPager, 10, new NeighborTwoLineStrategy());
			comprehensiveAdapter.addDataSource(comprehensiveChannelList);
			comprehensiveAdapter.setDataTotalSize(comprehensiveChannelList.size());
			comprehensiveViewPager.setAdapter(comprehensiveAdapter);
			Log.i(TAG, "...........................comprehensiveChannelList.size():" + comprehensiveChannelList.size());
			break;
		case REQUEST_COMPREHENSIVE_CHANNEL_FAIL:
			Log.d("processData", "综合请求失败");
			tvComprehensive.setText(String.format(getString(R.string.comprehensive_count), 0));
			recCount[7] = 0;
			// start author : zhangpengzhan 请求失败的情况下焦点向下不移动
			vComprehensiveChannel.setNextFocusDownId(vComprehensiveChannel.getId());
			vComprehensiveChannel.setVisibility(View.GONE);
			// end
			break;
		default:
			break;
		}

		tvChannelsCount.setText(String.format(getString(R.string.television_count), mChannelsCount));
	}

	/***
	 * 请求电视剧的信息
	 */
	private void requestTvChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				tvChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_TV, 1);
				if (tvChannelList != null && tvChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_TV_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author:zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_TV_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求电视剧出现异常
				handler.sendEmptyMessage(REQUEST_TV_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求电影的信息
	 */
	private void requestMovieChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				movieChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_FILE, 1);
				if (movieChannelList != null && movieChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_MOVIE_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author:zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_MOVIE_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求电影出现异常
				handler.sendEmptyMessage(REQUEST_MOVIE_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求体育的信息
	 */
	private void requestSportsChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				sportsChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_SPORTS, 1);
				if (sportsChannelList != null && sportsChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_SPORTS_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author:zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_SPORTS_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求体育出现异常
				handler.sendEmptyMessage(REQUEST_SPORTS_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求综艺的信息
	 */
	private void requestVarietyChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				varietyChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_VARIETY, 1);
				if (varietyChannelList != null && varietyChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_VARIETY_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author:zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_VARIETY_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求综艺出现异常
				handler.sendEmptyMessage(REQUEST_VARIETY_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求少儿的信息
	 */
	private void requestChildrenChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				childrenChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_CHILDREN, 1);
				Log.d(TAG, "childrenChannelList.size()=" + childrenChannelList.size());
				if (childrenChannelList != null && childrenChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_CHILDREN_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author：zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_CHILDREN_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求少儿出现异常
				handler.sendEmptyMessage(REQUEST_CHILDREN_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求科技的信息
	 */
	private void requestTechnologyChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				technologyChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_TECHNOLOGY, 1);
				if (technologyChannelList != null && technologyChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_TECHNOLOGY_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author : zhangpengzhan 获取数据为空时候发送失败消息
				else {
					handler.sendEmptyMessage(REQUEST_TECHNOLOGY_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求科技出现异常
				handler.sendEmptyMessage(REQUEST_TECHNOLOGY_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求财经的信息
	 */
	private void requestEconomicsChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				economicsChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_ECONOMICS, 1);
				if (economicsChannelList != null && economicsChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_ECONOMICS_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author:zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_ECONOMICS_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求财经出现异常
				handler.sendEmptyMessage(REQUEST_ECONOMICS_CHANNEL_FAIL);
			}
		});
	}

	/***
	 * 请求综合的信息
	 */
	private void requestComprehensiveChannels() {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				comprehensiveChannelList = new HiveTVService().getMediasByTag(AppConstant.SELECT_TYPE_ECONOMICS, 1);
				if (comprehensiveChannelList != null && comprehensiveChannelList.size() > 0) {
					handler.sendEmptyMessage(REQUEST_COMPREHENSIVE_CHANNEL_SUCCESS);// 信息请求成功
				}
				// start author:zhangpengzhan
				else {
					handler.sendEmptyMessage(REQUEST_COMPREHENSIVE_CHANNEL_FAIL);
				}
				// end
			}

			@Override
			public void processServiceException(ServiceException e) {// 请求综合出现异常
				handler.sendEmptyMessage(REQUEST_COMPREHENSIVE_CHANNEL_FAIL);
			}
		});
	}

	/**
	 * 分类选台（电视剧、电影、体育、综艺、少儿、科技、财经、综合）的View，焦点监听
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ChannelTabFocusChangeListener implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v == vTvChannel) {// 显示电视剧列表
					mShowViewPagerIndex = 0;
					mUpFocusId = R.id.fl_television;
					// mLeftFocusId=R.id.fl_comprehensive;
					setAllTextViewColor(0);
					setAllViewPagerVisibility(0);
				} else if (v == vMovieChannel) {// 显示电影列表
					mShowViewPagerIndex = 1;
					mUpFocusId = R.id.fl_film;
					// mLeftFocusId=R.id.fl_television;
					setAllTextViewColor(1);
					setAllViewPagerVisibility(1);
				} else if (v == vSportsChannel) {// 显示体育列表
					mShowViewPagerIndex = 2;
					mUpFocusId = R.id.fl_sports;
					// mLeftFocusId=R.id.fl_film;
					setAllTextViewColor(2);
					setAllViewPagerVisibility(2);
				} else if (v == vVarietyChannel) {// 显示综艺列表
					mShowViewPagerIndex = 3;
					mUpFocusId = R.id.fl_variety;
					// mLeftFocusId=R.id.fl_sports;
					setAllTextViewColor(3);
					setAllViewPagerVisibility(3);
				} else if (v == vChildrenChannel) {// 显示少儿列表
					mShowViewPagerIndex = 4;
					mUpFocusId = R.id.fl_children;
					// mLeftFocusId=R.id.fl_variety;
					setAllTextViewColor(4);
					setAllViewPagerVisibility(4);
				} else if (v == vTechnologyChannel) {// 显示科技列表
					mShowViewPagerIndex = 5;
					mUpFocusId = R.id.fl_technology;
					// mLeftFocusId=R.id.fl_children;
					setAllTextViewColor(5);
					setAllViewPagerVisibility(5);
				} else if (v == vEconomicsChannel) {// 显示财经列表
					mShowViewPagerIndex = 6;
					mUpFocusId = R.id.fl_economics;
					// mLeftFocusId=R.id.fl_technology;
					setAllTextViewColor(6);
					setAllViewPagerVisibility(6);
				} else if (v == vComprehensiveChannel) {// 显示综合列表
					mShowViewPagerIndex = 7;
					mUpFocusId = R.id.fl_comprehensive;
					// mLeftFocusId=R.id.fl_economics;
					setAllTextViewColor(7);
					setAllViewPagerVisibility(7);
				}
				if (isFromLastItemView && recCount[mShowViewPagerIndex] > 0) {
					// 判断是否是从下方电视台切换，而不是从上方tab切换，若是从下方最后一页最右边的电视台切换，则焦点在切换后的分类的第一个节目(若给分类节目数据为0，则焦点在tab上)
					viewPagerAll[mShowViewPagerIndex].requestFocus();
				}
				isFromLastItemView = false;
				// 切换tab
				changeTab();
			}
		}

	}

	/**
	 * 切换tab时改变左右翻页图片的显示 每次切换tab时根据viewPager的状态初始化
	 */
	private void changeTab() {
		int currentSize = recCount[mShowViewPagerIndex];
		// 获取当前ViewPager的currentItem 从0开始
		int currentItem = viewPagerAll[mShowViewPagerIndex].getCurrentItem();

		// 获取当前ViewPager的总页数
		currentPageCount = getPageSize(mShowViewPagerIndex);
		if (currentSize < PAGE_COUNT) {
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

	/**
	 * ItemView上焦点改变事件回调接口
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ItemViewFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {

			TvClassifyViewItemView itemView = (TvClassifyViewItemView) view;
			LiveMediaEntity entity = (LiveMediaEntity) view.getTag();
			if (has) {
				if (entity.getPositionInItemView() < PAGE_COUNT / 2) {
					itemView.setNextFocusUpId(mUpFocusId);
				}

				itemView.setTextMarquee(true);
			} else {
				itemView.setTextMarquee(false);
			}

		}

	}

	/**
	 * 计算总共有多少页
	 * */
	private int getPageSize(int showIndex) {
		return (int) Math.ceil(recCount[showIndex] / (double) PAGE_COUNT);
	}

	/**
	 * 计算最后一页的电视台数量
	 * 
	 * @return
	 */
	private int getLastPageViewCount(int showIndex) {
		return recCount[showIndex] % PAGE_COUNT;
	}

	private LiveMediaEntity entity;

	/**
	 * ItemView上按键事件回调接口
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ItemViewKeyListener implements CallBackItemViewKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			entity = (LiveMediaEntity) v.getTag();
			if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
				Intent intent = new Intent(getApplicationContext(), LiveEpgInfoActivity.class);
				// intent.putExtra(AppConstant.LIVE_ENTITY, currentEntity);
				intent.putExtra(AppConstant.CHANNEL_CODE, entity.getChannel_code());
				intent.putExtra(AppConstant.CHANNEL_LOGO, entity.getChannel_logourl());
				intent.putExtra(AppConstant.CHANNEL_NAME, entity.getChannel_name());
				startActivity(intent);
			}

			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_DOWN) {
				HivePagerAdapter adapter = (HivePagerAdapter) viewPagerAll[mShowViewPagerIndex].getAdapter();
				if ((entity.getPositionInItemView() + 1) == adapter.getLocalDataSize()) {
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

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene") && intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("tv".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的电视剧", intent);
					// mShowViewPagerIndex = 0;
					// mUpFocusId = R.id.fl_television;
					// // mLeftFocusId=R.id.fl_comprehensive;
					// setAllTextViewColor(0);
					// setAllViewPagerVisibility(0);
					vTvChannel.requestFocus();
				} else if ("movie".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的电影", intent);
					// mShowViewPagerIndex = 1;
					// mUpFocusId = R.id.fl_film;
					// // mLeftFocusId=R.id.fl_television;
					// setAllTextViewColor(1);
					// setAllViewPagerVisibility(1);
					vMovieChannel.requestFocus();
				} else if ("sports".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的体育", intent);
					// mShowViewPagerIndex = 2;
					// mUpFocusId = R.id.fl_sports;
					// // mLeftFocusId=R.id.fl_film;
					// setAllTextViewColor(2);
					// setAllViewPagerVisibility(2);
					vSportsChannel.requestFocus();
				} else if ("variety".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的综艺", intent);
					// mShowViewPagerIndex = 3;
					// mUpFocusId = R.id.fl_variety;
					// // mLeftFocusId=R.id.fl_sports;
					// setAllTextViewColor(3);
					// setAllViewPagerVisibility(3);
					vVarietyChannel.requestFocus();
				} else if ("children".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的少儿", intent);
					// mShowViewPagerIndex = 4;
					// mUpFocusId = R.id.fl_children;
					// // mLeftFocusId=R.id.fl_variety;
					// setAllTextViewColor(4);
					// setAllViewPagerVisibility(4);
					vChildrenChannel.requestFocus();
				} else if ("technology".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的科技", intent);
					// mShowViewPagerIndex = 5;
					// mUpFocusId = R.id.fl_technology;
					// // mLeftFocusId=R.id.fl_children;
					// setAllTextViewColor(5);
					// setAllViewPagerVisibility(5);
					vTechnologyChannel.requestFocus();
				} else if ("economics".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的财经", intent);
					// mShowViewPagerIndex = 6;
					// mUpFocusId = R.id.fl_economics;
					// // mLeftFocusId=R.id.fl_technology;
					// setAllTextViewColor(6);
					// setAllViewPagerVisibility(6);
					vEconomicsChannel.requestFocus();
				} else if ("comprehensive".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "正在直播的综合", intent);
					// mShowViewPagerIndex = 7;
					// mUpFocusId = R.id.fl_comprehensive;
					// // mLeftFocusId=R.id.fl_economics;
					// setAllTextViewColor(7);
					// setAllViewPagerVisibility(7);
					vComprehensiveChannel.requestFocus();
				} else if ("menu".equals(command)) {

					if (entity != null) {
						HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "打开节目单", intent);
						Intent intent1 = new Intent(getApplicationContext(), LiveEpgInfoActivity.class);
						// intent.putExtra(AppConstant.LIVE_ENTITY,
						// currentEntity);
						intent1.putExtra(AppConstant.CHANNEL_CODE, entity.getChannel_code());
						intent1.putExtra(AppConstant.CHANNEL_LOGO, entity.getChannel_logourl());
						intent1.putExtra(AppConstant.CHANNEL_NAME, entity.getChannel_name());
						startActivity(intent1);
					} else {
						HomeSwitchTabUtil.closeSiRi(LiveSelectTelevisionActivity.this, "没有节目单", intent);
					}
				}
			}

		}
	}

	public HashMap<String, String[]> setCommands(String key, String vaule) {
		// 固定词
		commands.put(key, new String[] { new StringBuffer().append("正在直播的").append(vaule).toString(),
				new StringBuffer().append("正在播放的").append(vaule).toString(), new StringBuffer().append("现在在播的").append(vaule).toString() });
		return commands;
	}

	// 场景id
	private String scenceId = "com.hiveview.tv.activity.LiveSelectTelevisionActivity";
	private boolean isSuccessed = false;
	HashMap<String, String[]> commands = new HashMap<String, String[]>();

	public String onQuery() {
		// TODO 自动生成的方法存根

		if (!isSuccessed) {
			ArrayList<String> list = new ArrayList<String>();
			list.add("电视剧");
			list.add("电影");
			list.add("体育");
			list.add("综艺");
			list.add("少儿");
			list.add("科技");
			list.add("财经");
			list.add("综合");
			ArrayList<String> key = new ArrayList<String>();
			key.add("tv");
			key.add("movie");
			key.add("sports");
			key.add("variety");
			key.add("children");
			key.add("technology");
			key.add("economics");
			key.add("comprehensive");
			for (int k = 0; k < list.size(); k++) {
				setCommands(key.get(k), list.get(k));
			}
			isSuccessed = true;

		}
		commands.put("menu", new String[] { "查看节目单", "打开节目单", "节目单" });
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
		SwitchChannelUtils.switchChannel(getApplicationContext(), entity.getChannel_name(), false, AppScene.getScene());
		Log.i(TAG, "获取的台的名称====>" + entity.getChannel_name());
		Intent intent = new Intent();
		intent.setAction(AppConstant.SWITCH_CHANNEL);
		// 要发送的内容
		intent.putExtra("author", entity.getChannel_name());
		// 发送 一个无序广播
		sendBroadcast(intent);
	}

}
