package com.hiveview.tv.activity;

import java.text.DateFormat;
import java.text.DecimalFormat;
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

import android.content.ComponentName;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.Strategy.PlayerRecoderLineStrategy;
import com.hiveview.tv.activity.FilmDetailActivity.getQIYIPlayerRecords;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.factory.PlayerRecordItemFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.LiveStreamEntity;
import com.hiveview.tv.service.entity.MoviePermissionEntity;
import com.hiveview.tv.service.entity.PlayerRecordEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.BlueLightUtils;
import com.hiveview.tv.utils.BlueLightVipUtil;
import com.hiveview.tv.utils.DialogUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.utils.QIYIRecordUtils;
import com.hiveview.tv.utils.QiYiPlayerUtil;
import com.hiveview.tv.utils.VideoLiveVipUtils;
import com.hiveview.tv.view.MatrixTVForeidgnView;
import com.hiveview.tv.view.PlayerRecordListItemPageView.GetFocusEntity;
import com.paster.util.JsonUtil;

/**
 * @ClassName: PlayerRecordActivity
 * @Description: 播放记录
 * @author: zhangpengzhan
 * @date 2014年4月9日 上午10:00:20
 * 
 */
public class PlayerRecordActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener {

	public final  int LOAD_TVLIST_SUCCESS = 24;

	/**
	 * 实现类的集合
	 */
	private List<PlayerRecordEntity> mPlayerRecordEntities = null;

	/**
	 * 获取记录不为空成功
	 */
	private final int GETPLAYERRECORD_SUCCESS = 0x00230;

	/**
	 * 获取播放记录失败或为空
	 */
	private final int GETPLAYERRECORD_FAIL = 0x00240;

	/**
	 * 获取极清和爱奇艺观看记录成功
	 */
	private final int GETBULECORD_SUCCESS = 100000;

	/**
	 * 页面重新被激活时候 消息
	 * 
	 * @Fields PLAYER_RECORD_RESUME
	 */
	private final int PLAYER_RECORD_RESUME = 0x00153;

	/**
	 * 每页显示多少数据
	 */
	private final int pageViewSize = 12;

	/**
	 * 显示数据列表的ViewPager
	 */
	private HivePreloadViewPager mViewPager = null;
	private HivePagerAdapter adapter = null;

	/**
	 * 顶部包含按钮 和显示页码的 布局
	 */
	private RelativeLayout top_layout = null;

	/**
	 * 上一页按钮
	 */
	private View vUpPage = null;
	/**
	 * 下一页按钮
	 */
	private View vDownPage = null;
	/**
	 * 显示当前页
	 */
	private TextView tvCurrentPage = null;

	/**
	 * 是否是初次展示首页
	 */
	private boolean isFristCreate = false;

	/**
	 * 显示总页数
	 */
	private TextView tvTotalPage = null;
	private TextView tvTotal = null;

	/**
	 * 页码的分隔符
	 */
	private TextView tvPageDivision = null;

	/**
	 * 左右两侧指示图片
	 */
	private ImageView leftImage = null;
	private ImageView rightImage = null;

	/**
	 * 如果收藏为空的时候提示用户收藏为空的提示消息
	 */
	private TextView mCollectHine = null;

	/**
	 * 标题
	 */
	private TextView mTitle = null;
	private TextView hintMessage = null;

	/**
	 * 清除观看记录对话框的监听
	 * 
	 * @Fields dialogListener
	 */
	private CancelDialogListener dialogListener;
	/**
	 * 观看记录的实体类
	 * 
	 * @Fields mPlayerRecordEntity
	 */
	private PlayerRecordEntity mPlayerRecordEntity = null;

	/**
	 * 今天 昨天 更早的记录数量
	 */
	private int playerTodatNum = 0;
	private int playeryesterdayNum = 0;
	private int playerOlderNum = 0;

	/**
	 * 添加的假数据的个数 在计算收藏总数的时候要减去这个数
	 */
	private int playerAdd = 0;

	/**
	 * 显示 今天 昨天 明天 的时间轴啊~~
	 */
	private RelativeLayout relativeLayout;
	/**
	 * 今天时间轴标签
	 */
	private LinearLayout linearLayout;
	/**
	 * 今天时间轴后的延长线
	 */
	private LinearLayout linearLayout2;
	/**
	 * 昨天时间轴标签
	 */
	private LinearLayout linearLayout3;
	/**
	 * 昨天时间轴后的延长线
	 */
	private LinearLayout linearLayout4;
	/**
	 * 更早时间轴标签
	 */
	private LinearLayout linearLayout5;
	/**
	 * 更早时间轴标签后的延长线
	 */
	private LinearLayout linearLayout6;

	/**
	 * 单个view pager 的宽为180
	 */
	private int viewPageItmeWidth = 180;

	/**
	 * 左边间隙
	 */
	private int spaceWidth = 100;

	/**
	 * 尾巴距离顶部的距离
	 */
	private int topSpace = 12;
	private int TailLenWidth = 3;

	/**
	 * 标签的宽度
	 */
	private int tagWidth = 90;

	/**
	 * 时间轴所在的root view 的宽度
	 */
	private int viewPageWidth = 1070;

	/**
	 * 昨天尾巴的长度
	 */
	private int yesterdayTailLen;
	/**
	 * 昨天的标签第几页显示
	 */
	private int yesterdaPages;
	/**
	 * 昨天不足一页的 剩余的个数
	 */
	private int yesterdayItem;
	/**
	 * 需要显示到第几页
	 */
	private int yesterdayGroup;
	/**
	 * 更多第几页开始显示
	 */
	private int olderPages;
	/**
	 * 更多不足一页的 剩余的个数
	 */
	private int olderItem;
	/**
	 * 更多显示到第几页
	 */
	private int olderGroup;
	/**
	 * 今天 尾巴的长度
	 */
	private int todayTailLen;
	/**
	 * 今天需要显示到第几页
	 */
	private int todayGroup;
	/**
	 * 今天不足一页 剩下的个数
	 */
	private int todayItem;

	/**
	 * 今天昨天更早的时间标签的位置
	 */
	private int today_X = 0;
	private int yesterday_X = 0;
	private int older_X = 0;

	/**
	 * 当前页数
	 */
	private int indexPages = 0;

	/**
	 * 视频时长的标签
	 */
	private int allTime = 60 * 60;

	/**
	 * 最短时长的标签
	 */
	private int shortTime = 20 * 60;
	/**
	 * 最后一页的view数
	 * 
	 * @Fields lastViews
	 */
	int lastViews;
	/**
	 * 最后一页的view
	 * 
	 * @Fields LastViewIndexs
	 */
	private List<Integer> LastViewIndexs = null;

	// 存储的当前view pager 的itemview 的id 用于判断出现空缺的view 时候焦点跳转右上
	private int[] indexs = { R.id.playerrecord_item1_poster_iv,
			R.id.playerrecord_item2_poster_iv,
			R.id.playerrecord_item3_poster_iv,
			R.id.playerrecord_item4_poster_iv,
			R.id.playerrecord_item5_poster_iv,
			R.id.playerrecord_item6_poster_iv,
			R.id.playerrecord_item7_poster_iv,
			R.id.playerrecord_item8_poster_iv,
			R.id.playerrecord_item9_poster_iv,
			R.id.playerrecord_item10_poster_iv,
			R.id.playerrecord_item11_poster_iv,
			R.id.playerrecord_item12_poster_iv };

	/**
	 * 存储 当前view item的id
	 */
	List<Integer> list = new ArrayList<Integer>();

	/**
	 * loading view
	 */
	private com.hiveview.tv.view.ProgressDialog progressDialog;

	private String TAG = "PlayerRecordActivity";
	/**
	 * 当前列表的总页数
	 * 
	 * @Fields pageCount
	 */
	int pageCount;

	/**
	 * 当前view的位置
	 * 
	 * @Fields theViewIndex
	 */
	public int theViewIndex;
	/**
	 * 当前是否是处于最后一页
	 * 
	 * @Fields isLastPage
	 */
	public boolean isLastPage = false;

	List<PlayerRecordEntity> playerRecordEntity;

	private String sourceFlag = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.tv.activity.BaseActivity#onCreate(android.os.Bundle)
	 * 
	 * 初始化 数据
	 */
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.palyerrecord_layout);
		// Log.v(TAG, BlueLightUtils.getHistoryList(this).toString());
		isFristCreate = true;
		sourceFlag = getIntent().getStringExtra("source");
		init();
		getContentResolver().registerContentObserver(
				QIYIRecordUtils.PLAYER_RECORD, true, cob);
	}

	private ContentObserver cob = new ContentObserver(new Handler()) {
		@Override
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications();
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.d(TAG, "---onChange11");
			// 监听播放记录数据库变化，改变播放状态，（解决当走到onresum,并没有走到保存数据库，拿不到数据库）
			if (!isFristCreate) {
				Log.d(TAG, "---onChange");
				progressDialog.setVisibility(View.VISIBLE);
				progressDialog.setFocusable(true);
				progressDialog.requestFocus();
				// 发消息重置view
				handler.sendEmptyMessage(PLAYER_RECORD_RESUME);
				if (null != adapter) {
					// 清除当前viewpager 的数据
					adapter.clear();

					mPlayerRecordEntities = new ArrayList<PlayerRecordEntity>();
					playerRecordEntity = new ArrayList<PlayerRecordEntity>();
					submitRequest(new GetPlayerRecord());

				}
				isFristCreate = true;
			}
			// testBtn.setText(DataUtils.getChangeName(getApplicationContext()));
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.tv.activity.BaseActivity#processData(android.os.Message)
	 */
	protected void processData(int msgWhat) {

		switch (msgWhat) {
		// 获取数据成功
		case GETPLAYERRECORD_SUCCESS:
			// 时间轴 各种标签和尾巴初始化的时候统统 隐藏掉
			// 后边的代码会重置相关内容
			linearLayout.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.GONE);
			linearLayout3.setVisibility(View.GONE);
			linearLayout4.setVisibility(View.GONE);
			linearLayout5.setVisibility(View.GONE);
			linearLayout6.setVisibility(View.GONE);

			isLastPage = false;
			// 显示布局按钮和显示页
			top_layout.setVisibility(View.VISIBLE);
			mTitle.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			hintMessage.setVisibility(View.VISIBLE);
			mCollectHine.setVisibility(View.GONE);// 保证在有数据的时候这个提示信息绝对不会出现的
			if (null == adapter) {
				adapter = new HivePagerAdapter(this,
						new PlayerRecordItemFactory(
								new ViewItemFocusListener(),
								new ViewItemKeyListener(),
								new ViewItemClickListener(),
								new GetFocusIndex()), mViewPager, pageViewSize,
						new PlayerRecoderLineStrategy());

			}
			adapter.addDataSource(mPlayerRecordEntities);
			// 全部多少部的字样
			tvTotal.setText(String.format(
					getResources().getString(R.string.collect_total_text),
					(mPlayerRecordEntities.size() - playerAdd)));
			adapter.setDataTotalSize(mPlayerRecordEntities.size());// 设置数据总记录数
			// 设置适配器
			mViewPager.setAdapter(adapter);
			// 设置可获取焦点
			mViewPager.setFocusableInTouchMode(true);

			// 设置viewpager的监听
			mViewPager.setPreloadingListener(new OnPreloadingListener() {

				@Override
				public void setPageCurrent(int pageIndex) {// 翻页过程
					indexPages = pageIndex;
					tvCurrentPage.setText(pageIndex + "");// 设置当前页数
					Log.d(TAG, "pageIndex::" + pageIndex);
					if (pageIndex != 1) {// 显示上一页按钮
						vUpPage.setVisibility(View.VISIBLE);
						leftImage.setVisibility(View.VISIBLE);
						// ===================================

					}

					if (pageIndex != adapter.getViews().size()) {// 显示下一页按钮
						vDownPage.setVisibility(View.VISIBLE);
						rightImage.setVisibility(View.VISIBLE);
					}
					isLastPage = false;
					// 最后一页
					if (pageIndex >= pageCount) {
						isLastPage = true;
						vUpPage.setVisibility(View.VISIBLE);
						// start:隐藏下一页，author:huzuwei
						vDownPage.setVisibility(View.GONE);
						// end
						leftImage.setVisibility(View.VISIBLE);
						rightImage.setVisibility(View.INVISIBLE);
						// 计算最后一页的数据的时候不能减去添加的假数据
						lastViews = (mPlayerRecordEntities.size())
								% pageViewSize;
						Log.d(TAG,
								"lastViews==>lastViews::"
										+ lastViews
										+ "====pageCount::"
										+ pageCount
										+ "====(mPlayerRecordEntities.size() - playerAdd)::"
										+ (mPlayerRecordEntities.size() - playerAdd));
						LastViewIndexs = setTheViewMove(lastViews, 12);
					}

					// 时间轴的翻页处理事件
					setOnTimeLine(pageIndex);
				}

				@Override
				public void preLoading(int pageSize) {

				}

				@Override
				public void preLoadNotFinish() {
					Log.d(TAG, "->preLoadNotFinish这个回调执行了");
				}

				@Override
				public void onLastPage() {// 最后一页

				}

				@Override
				public void onFirstPage() {// 第一页
					// 隐藏上一页按钮和指示图片

					vUpPage.setVisibility(View.INVISIBLE);
					vDownPage.setVisibility(View.VISIBLE);
					leftImage.setVisibility(View.INVISIBLE);
					rightImage.setVisibility(View.VISIBLE);

					// 第一页时间轴的设定
					setOnOnePageTimeLine(1);

				}
			});

			// 设置第一页
			if (mPlayerRecordEntities.size() != 0)
				tvCurrentPage.setText(1 + "");
			else
				tvCurrentPage.setText(0 + "");
			// 设置总页数
			pageCount = (int) Math.ceil(mPlayerRecordEntities.size()
					/ (double) pageViewSize);
			// 添加总数
			tvTotalPage.setText(String.format(
					getResources().getString(R.string.list_top_page_count),
					pageCount));

			// 判断是否只有一页的情况
			if (mPlayerRecordEntities.size() > pageViewSize) {
				// 不是一页
				vDownPage.setVisibility(View.VISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.VISIBLE);
			} else {// 只是一页
				// start:页数只是一页的时候显示右对齐，author:huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
			}

			// 第一页时间轴的设定
			setOnOnePageTimeLine(1);

			// loading
			progressDialog.setVisibility(View.GONE);
			vDownPage.setFocusableInTouchMode(true);
			vUpPage.setFocusableInTouchMode(true);
			vDownPage.setFocusable(true);
			vUpPage.setFocusable(true);
			mViewPager.setNextPreviousPageView(vUpPage, vDownPage);
			// 请求焦点
			mViewPager.requestFocus();
			break;

		// 获取数据失败 或者初始化奇艺失败
		case GETPLAYERRECORD_FAIL:
			mTitle.setVisibility(View.VISIBLE);
			// loading
			progressDialog.setVisibility(View.INVISIBLE);
			// 隐藏各种显示内容
			hintMessage.setVisibility(View.INVISIBLE);
			top_layout.setVisibility(View.INVISIBLE);
			mCollectHine.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			vDownPage.setVisibility(View.INVISIBLE);
			vUpPage.setVisibility(View.INVISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.INVISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			tvCurrentPage.setVisibility(View.INVISIBLE);
			tvTotalPage.setVisibility(View.INVISIBLE);
			tvPageDivision.setVisibility(View.INVISIBLE);
			mViewPager.setVisibility(View.INVISIBLE);
			// 标签和尾巴 统统 隐藏掉
			linearLayout.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.GONE);
			linearLayout3.setVisibility(View.GONE);
			linearLayout4.setVisibility(View.GONE);
			linearLayout5.setVisibility(View.GONE);
			linearLayout6.setVisibility(View.GONE);
			break;
		case GETBULECORD_SUCCESS:
			if (mPlayerRecordEntities.size() == 0) {
				getPlayerRecords();
			}
			break;
		case PLAYER_RECORD_RESUME:
			// 时间轴 各种标签和尾巴初始化的时候统统 隐藏掉
			// 后边的代码会重置相关内容
			linearLayout.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.GONE);
			linearLayout3.setVisibility(View.GONE);
			linearLayout4.setVisibility(View.GONE);
			linearLayout5.setVisibility(View.GONE);
			linearLayout6.setVisibility(View.GONE);
			// start:页数对齐,重新加载显示问题,author:huzuwei
			// 隐藏上一页按钮和指示图片
			// vUpPage.setVisibility(View.INVISIBLE);
			// vDownPage.setVisibility(View.VISIBLE);
			// leftImage.setVisibility(View.INVISIBLE);
			// rightImage.setVisibility(View.VISIBLE);

			// 判断是否只有一页的情况
			if (mPlayerRecordEntities.size() > pageViewSize) {
				// 不是一页
				vDownPage.setVisibility(View.VISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.GONE);
			} else {// 只是一页
				vDownPage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
			}
			// end

			tvCurrentPage.setText("1");// 设置当前页数
			break;
		}
	}

	/**
	 * 初始化 奇异播放器的数据
	 */
	public void init() {

		// 时间轴阿~~重用收藏的布局阿，先隐藏阿需要~~~初始化的时候就需要显示阿
		relativeLayout = (RelativeLayout) findViewById(R.id.usertag);
		// 调节尾巴的长度
		linearLayout = (LinearLayout) findViewById(R.id.usertag_l2);// 今天时间轴标签
		linearLayout2 = (LinearLayout) findViewById(R.id.usertag_l3);// 今天的尾巴
		linearLayout3 = (LinearLayout) findViewById(R.id.usertag_l4);// 昨天时间轴标签
		linearLayout4 = (LinearLayout) findViewById(R.id.usertag_l5);// 昨天的尾巴
		linearLayout5 = (LinearLayout) findViewById(R.id.usertag_l6);// 更早时间轴标签
		linearLayout6 = (LinearLayout) findViewById(R.id.usertag_l7);// 更早的尾巴
		// 时间轴 各种标签和尾巴初始化的时候统统 隐藏掉
		linearLayout.setVisibility(View.GONE);
		linearLayout2.setVisibility(View.GONE);
		linearLayout3.setVisibility(View.GONE);
		linearLayout4.setVisibility(View.GONE);
		linearLayout5.setVisibility(View.GONE);
		linearLayout6.setVisibility(View.GONE);
		// loading view
		progressDialog = (com.hiveview.tv.view.ProgressDialog) findViewById(R.id.pd_list_loading);
		progressDialog.setVisibility(View.VISIBLE);
		// 时间轴
		relativeLayout.setVisibility(View.VISIBLE);
		// 因为是重用电影收藏的 所以要重新设置提示消息
		mCollectHine = (TextView) findViewById(R.id.collect_layout_collecthint);
		mCollectHine
				.setText(getResources().getString(R.string.no_playerrecord));
		mCollectHine.setVisibility(View.GONE);
		hintMessage = (TextView) findViewById(R.id.user_hint);
		hintMessage.setVisibility(View.INVISIBLE);
		// 因为是重用电影收藏的 所以要重新设置标题的内容
		mTitle = (TextView) findViewById(R.id.tv_collect_lbl);
		mTitle.setText(getResources().getString(
				R.string.sub_navigation_common_record_text));
		mTitle.setVisibility(View.INVISIBLE);
		top_layout = (RelativeLayout) findViewById(R.id.list_top_layout);
		top_layout.setVisibility(View.INVISIBLE);
		// 页码信息
		tvTotal = (TextView) findViewById(R.id.tv_collect_count);
		tvTotal.setVisibility(View.INVISIBLE);
		// 页码的分隔符
		tvPageDivision = (TextView) findViewById(R.id.list_division);

		// 左右两侧的指示图片
		leftImage = (ImageView) findViewById(R.id.list_left);
		leftImage.setVisibility(View.INVISIBLE);
		rightImage = (ImageView) findViewById(R.id.list_right);
		rightImage.setVisibility(View.INVISIBLE);
		// 上下页按钮 图片
		vDownPage = findViewById(R.id.list_page_down_ll);
		vUpPage = findViewById(R.id.list_page_up_ll);
		vDownPage.setNextFocusLeftId(R.id.list_page_up_ll);
		vUpPage.setNextFocusRightId(R.id.list_page_down_ll);
		vUpPage.setNextFocusLeftId(R.id.list_page_up_ll);
		// 添加按键监听和焦点监听
		vUpPage.setOnClickListener(this);
		vDownPage.setOnClickListener(this);
		vUpPage.setOnFocusChangeListener(this);
		vDownPage.setOnFocusChangeListener(this);
		// 记录的总数和页码
		tvCurrentPage = (TextView) findViewById(R.id.list_page_current);
		tvTotalPage = (TextView) findViewById(R.id.list_page_count);
		// 展示图片的view pager
		mViewPager = (HivePreloadViewPager) findViewById(R.id.vp_list);
		// 取消
		dialogListener = new CancelDialogListener();
		mPlayerRecordEntities = new ArrayList<PlayerRecordEntity>();
		playerRecordEntity = new ArrayList<PlayerRecordEntity>();
		submitRequest(new GetPlayerRecord());

		// 把每夜上边的item的id转换成list
		for (int s : indexs) {
			list.add(s);
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
			PlayerRecordEntity mPlayerRecordEntity1 = (PlayerRecordEntity) lhs;
			PlayerRecordEntity mPlayerRecordEntity2 = (PlayerRecordEntity) rhs;
			LogUtil.info("mfileEntity1.getPhase()::"
					+ mPlayerRecordEntity1.getPalyerDate());
			LogUtil.info("nteger.parseInt(mfileEntity2.getPhase())::"
					+ mPlayerRecordEntity2.getPalyerDate());
			// 排序规则小数的排在前边，大树的排后边
			if (mPlayerRecordEntity1.getPalyerDate() > mPlayerRecordEntity2
					.getPalyerDate()) {// 大的返回-1排在后边
				return -1;
			} else if (mPlayerRecordEntity1.getPalyerDate() == mPlayerRecordEntity2
					.getPalyerDate()) {// 相等的就返回0
				return 0;
			} else if (mPlayerRecordEntity1.getPalyerDate() < mPlayerRecordEntity2
					.getPalyerDate()) {// 小的返回1排在前边
				return 1;
			}
			return 0;
		}
	}

	// 极清观看记录
	private void getBluePlayerRecords() {
		Log.v(TAG, "getBluePlayerRecords()---start");
		if (BlueLightUtils.getHistoryList(PlayerRecordActivity.this) != null
				&& BlueLightUtils.getHistoryList(PlayerRecordActivity.this)
						.size() != 0) {
			// 极清记录
			for (PlayerRecordEntity entity : BlueLightUtils
					.getHistoryList(PlayerRecordActivity.this)) {
				mPlayerRecordEntity = new PlayerRecordEntity();
				mPlayerRecordEntity.setSource(entity.getSource());
				mPlayerRecordEntity.setCp(entity.getCp());
				mPlayerRecordEntity.setAlbumId(entity.getAlbumId());
				mPlayerRecordEntity.setImage(entity.getImage());
				mPlayerRecordEntity.setAlbumName(entity.getAlbumName());
				mPlayerRecordEntity.setPalyerDate(entity.getPalyerDate());
				mPlayerRecordEntity.setName(entity.getName());// 名称
				mPlayerRecordEntity.setOrientation("true");
				// 极清已播放时间
				//int palyerTime = Integer.parseInt(entity.getSurplusTime()) / 1000;
				if (entity.getDuration() != null) {
					// 极清总时间
					//int duration = Integer.parseInt(entity.getDuration()) / 1000;
					// 极清剩余时间
					int surplusTime = (Integer.parseInt(entity.getDuration()) - Integer.parseInt(entity.getSurplusTime()))/1000;
					//Log.d(TAG, "== entitys.getRestVideoPlayTime()" + palyerTime);
					// 计算剩余的时间00:00
					if (surplusTime >= 0) {
						int mins = surplusTime / 60;
						int sen = surplusTime % 60;
						DecimalFormat df = new DecimalFormat("00");
						mPlayerRecordEntity.setSurplusTime(String.format(getResources().getString(R.string.playrecord_remaining), String.valueOf(mins) + ":" + df.format(sen))
								);// 还剩余的时间
					} else {
						mPlayerRecordEntity.setSurplusTime(String.format(getResources().getString(R.string.playrecord_over), ""));// 如果剩余时间小于10秒的时候显示已看完字样
					}
				}
				if (entity.getVideoset_type() == 2) {
					// 是电视剧的情况
					mPlayerRecordEntity.setAlbums(String.format(getResources().getString(R.string.playrecord_selection), Integer.parseInt(entity.getAlbums()) + 1)
							);// 当前的集数。默认为1
				}
				// 添加到列表中
				playerRecordEntity.add(mPlayerRecordEntity);
			}
		}

		if (VideoLiveVipUtils.getVideoLiveRecord(PlayerRecordActivity.this) != null
				&& VideoLiveVipUtils.getVideoLiveRecord(
						PlayerRecordActivity.this).size() != 0) {
			// 直播记录
			for (PlayerRecordEntity entity : VideoLiveVipUtils
					.getVideoLiveRecord(PlayerRecordActivity.this)) {
				mPlayerRecordEntity = new PlayerRecordEntity();
				mPlayerRecordEntity.setSource(entity.getSource());
				mPlayerRecordEntity.setCp(entity.getCp());
				mPlayerRecordEntity.setCpId(entity.getCpId());
				mPlayerRecordEntity.setAlbumId(entity.getAlbumId());
				mPlayerRecordEntity.setImage(entity.getImage());
				mPlayerRecordEntity.setAlbumName(entity.getAlbumName());
				mPlayerRecordEntity.setPalyerDate(entity.getShowDate() / 1000);
				mPlayerRecordEntity.setName(entity.getName());// 名称
				mPlayerRecordEntity.setOrientation("true");
				// 添加到列表中
				playerRecordEntity.add(mPlayerRecordEntity);
			}
		}

		Log.v(TAG, "getQIYIPlayerRecords()" + playerRecordEntity.toString());
		// if (HiveviewApplication.mQiyiClient.isAuthSuccess()) {
		getQIYIPlayerRecords();
		// } else {
		// handler.sendEmptyMessage(GETBULECORD_SUCCESS);
		// Log.v(TAG, "getQIYIPlayerRecords()--error");
		// getAuthSuccess();
		// }
	}

	// 爱奇艺观看记录
	private void getQIYIPlayerRecords() {
		Log.v(TAG, "getQIYIPlayerRecords()");
		// hm = HiveviewApplication.mQiyiClient.getHistoryManager();
		// result = hm.getHistoryList(30,false).data;
		if (QIYIRecordUtils.getHistoryList(PlayerRecordActivity.this, null) != null
				&& QIYIRecordUtils.getHistoryList(PlayerRecordActivity.this,
						null).size() != 0) {
			// 极清记录
			for (PlayerRecordEntity entity : QIYIRecordUtils.getHistoryList(
					PlayerRecordActivity.this, null)) {
				/**
				 * 点播记录获取成功， 获取极清观看记录
				 * 
				 */
				try {
					Log.d(TAG,
							"爱奇艺观看记录==== " + entity.getName()
									+ entity.getAlbums());
					// 获取播放记录
					mPlayerRecordEntity = new PlayerRecordEntity();
					mPlayerRecordEntity.setSource(0);
					mPlayerRecordEntity.setAlbumId(entity.getAlbumId());
					mPlayerRecordEntity.setAlbumName(entity.getName());// 专辑名称
					mPlayerRecordEntity.setAlbumPhotoName(entity.getName());
					mPlayerRecordEntity.setImage(entity.getImage());// 图片
					mPlayerRecordEntity.setName(entity.getName());// 名称
					mPlayerRecordEntity.setStartTime(entity.getStartTime());// 开始播放的时间
					mPlayerRecordEntity.setOrientation(entity.getOrientation());// 判断是不是竖图，true显示竖图，false显示横图
					mPlayerRecordEntity
							.setPalyerDate(entity.getPalyerDate() / 1000);// 添加播放记录的时间，以秒为单位
					mPlayerRecordEntity.setEntity(entity.getEntity());
					long palyerTime = Long.parseLong(entity.getSurplusTime()) / 1000;
					// 计算剩余的时间00:00
					if (palyerTime >= 10) {
						int mins = (int) (palyerTime / 60);
						int sen = (int) (palyerTime % 60);
						DecimalFormat df = new DecimalFormat("00");
						mPlayerRecordEntity.setSurplusTime(String.format(getResources().getString(R.string.playrecord_remaining), String.valueOf(mins) + ":" + df.format(sen)));// 还剩余的时间
					} else {
						mPlayerRecordEntity.setSurplusTime(String.format(getResources().getString(R.string.playrecord_over), ""));
						// 如果剩余时间小于10秒的时候显示已看完字样
					}

					if (Boolean.valueOf(entity.getOrientation()) == false) {// 判断是不是竖图，true显示竖图，false显示横图
						if (null != entity.getAlbums()
								&& !entity.getAlbums().equals("0")
								&& entity.getAlbums().length() >= 8) {
							mPlayerRecordEntity.setAlbums(entity.getAlbums());// 显示综艺的期数，如果有正确的期数的情况下
						}
					} else {
						if (entity.getCurrentEpisode().equals("-1")) {
							mPlayerRecordEntity.setSurplusTime(String.format(getResources().getString(R.string.playrecord_over), ""));
							mPlayerRecordEntity.setAlbums("");
						} else {
							if (entity.getEntity().getCid() == 2) {// 电视剧
								mPlayerRecordEntity.setAlbums(String.format(getResources().getString(R.string.playrecord_selection), entity.getAlbums())
										);// 当前的集数。默认为1
							} else if (entity.getEntity().getCid() == 6) {// 综艺
								if (null != entity.getAlbums()
										&& !entity.getAlbums().equals("0")
										&& entity.getAlbums().length() >= 8) {
									mPlayerRecordEntity.setAlbums(entity
											.getAlbums());// 显示综艺的期数，如果有正确的期数的情况下
								}
							}else if(entity.getEntity().getCid() == 4){
								mPlayerRecordEntity.setAlbums(String.format(getResources()
										.getString(R.string.playrecord_selection), entity.getAlbums()));// 当前的集数。默认为1
							}
						}
					}
					mPlayerRecordEntity.setDescription(entity.getDescription());//
					// 一句话看点
					// // 播放器需要的数据
					// mPlayerRecordEntity.setVrsAlbumId(entitys.getVrsAlbumId());//
					// 来自后台接口
					// mPlayerRecordEntity.setVrsTvId(entitys.getVrsTvId());//
					// 来自后台的接口

					// 添加到列表中
					playerRecordEntity.add(mPlayerRecordEntity);

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		handler.sendEmptyMessage(GETBULECORD_SUCCESS);
	}

	// private void getBluePlayerRecord() {
	// if (mPlayerRecordEntities.size() != 0) {
	// mPlayerRecordEntities.clear();
	// }
	// // 重置 今天 昨天 更早的总数的计数器
	// playerTodatNum = 0;
	// playeryesterdayNum = 0;
	// playerOlderNum = 0;
	// // 当前添加 假的数据的 计数器
	// playerAdd = 0;
	// // 当前list添加的数据的总数的计数器
	// int index = 0;
	// PlayerRecordEntity mPlayerRecordEntity = null;
	// // 获取当前的日期，用于跟观看记录比较
	// java.util.Date dt = new Date();
	// Log.d(TAG, "系统时间:" + System.currentTimeMillis());
	// // 日期转换
	// SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
	// SimpleDateFormat sdf2 = new SimpleDateFormat("dd", Locale.CHINA);
	// SimpleDateFormat sdf3_1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
	// Locale.CHINA);
	// Log.d(TAG, "系统日期:" + sdf3_1.format(dt));
	// String months_string = sdf.format(dt);
	// String days_string = sdf2.format(dt);
	// int months = Integer.parseInt(months_string);
	// int days = (Integer.parseInt(days_string)) + 0;
	// Log.d(TAG, "days===" + days);
	// Log.d(TAG, "months===" + months);
	// //
	// Log.d(TAG,"mm=="+months+"=="+days+"&&&&&&&"+System.currentTimeMillis());
	//
	// List<PlayerRecordEntity> playerRecordEntity = new
	// ArrayList<PlayerRecordEntity>();
	// if (BlueLightUtils.getHistoryList(PlayerRecordActivity.this) != null
	// && BlueLightUtils.getHistoryList(PlayerRecordActivity.this)
	// .size() != 0) {
	// // 极清记录
	// for (PlayerRecordEntity entity : BlueLightUtils
	// .getHistoryList(PlayerRecordActivity.this)) {
	// mPlayerRecordEntity = new PlayerRecordEntity();
	// mPlayerRecordEntity.setSource(entity.getSource());
	// mPlayerRecordEntity.setCp(entity.getCp());
	// mPlayerRecordEntity.setAlbumId(entity.getAlbumId());
	// mPlayerRecordEntity.setImage(entity.getImage());
	// mPlayerRecordEntity.setAlbumName(entity.getAlbumName());
	// mPlayerRecordEntity.setPalyerDate(entity.getPalyerDate());
	// mPlayerRecordEntity.setName(entity.getName());// 名称
	// mPlayerRecordEntity.setOrientation(true);
	// // 极清已播放时间
	// int palyerTime = Integer.parseInt(entity.getSurplusTime()) / 1000;
	// if (entity.getDuration() != null) {
	// // 极清总时间
	// int duration = Integer.parseInt(entity.getDuration()) / 1000;
	// // 极清剩余时间
	// int surplusTime = duration - palyerTime;
	// Log.d(TAG, "== entitys.getRestVideoPlayTime()" + palyerTime);
	// // 计算剩余的时间00:00
	// if (surplusTime >= 0) {
	// int mins = surplusTime / 60;
	// int sen = surplusTime % 60;
	// DecimalFormat df = new DecimalFormat("00");
	// mPlayerRecordEntity.setSurplusTime("剩余 "
	// + String.valueOf(mins) + ":" + df.format(sen));// 还剩余的时间
	// } else {
	// mPlayerRecordEntity.setSurplusTime("已看完");// 如果剩余时间小于10秒的时候显示已看完字样
	// }
	// }
	// if (entity.getVideoset_type() == 2) {
	// // 是电视剧的情况
	// mPlayerRecordEntity.setAlbums("第"
	// + (Integer.parseInt(entity.getAlbums()) + 1) + "集");// 当前的集数。默认为1
	// }
	// // 添加到列表中
	// playerRecordEntity.add(mPlayerRecordEntity);
	// }
	// }
	//
	// Collections.sort(playerRecordEntity, new SortByTime());
	//
	// for (PlayerRecordEntity playerRecord : playerRecordEntity) {
	// java.util.Date dt2 = new Date(
	// ((long) playerRecord.getPalyerDate()) * 1000);// 转换时间
	// // 区分昨天今天明天
	//
	// int monthsOld = Integer.parseInt(sdf.format(dt2));
	// int daysOld = Integer.parseInt(sdf2.format(dt2));
	// SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
	// Locale.CHINA);
	// // 计算记录的日期和当前的日期差值
	// if (months == monthsOld) {
	// int space = days - daysOld;
	// Log.d(TAG, "日期差:" + space);
	// if (space == 0) {// 今天
	//
	// playerTodatNum++;
	// playerRecord.setWitchDay("1");
	// } else if (space == 1) {// 昨天
	// playeryesterdayNum++;
	// playerRecord.setWitchDay("2");
	// } else if (space >= 2) {// 更早
	// playerOlderNum++;
	// playerRecord.setWitchDay("3");
	// }
	// } else {// 更早
	// playerOlderNum++;
	// playerRecord.setWitchDay("3");
	// }
	// Log.d(TAG,
	// "mm==" + monthsOld + "==" + daysOld + "==="
	// + sdf3.format(dt2));
	//
	// mPlayerRecordEntities.add(playerRecord);
	// index++;
	// // 保证数据量不超过30个
	// if (index >= 30)
	// break;
	//
	// }
	//
	// Log.d(TAG, "numbert===" + playerTodatNum + "^^^^^^^^"
	// + playeryesterdayNum);
	// Log.d(TAG, "===list.saze" + mPlayerRecordEntities.size());
	// if (playerTodatNum % 2 != 0) {
	// // 添加假数据 计数器 ＋1
	// playerAdd++;
	// // 当记录为奇数的时候应该添加一个生成偶数 ，这是添加一个虚假的数据
	// mPlayerRecordEntity = new PlayerRecordEntity();
	// mPlayerRecordEntity.setSource(0);
	// mPlayerRecordEntity.setAlbumId(" ");
	// mPlayerRecordEntity.setAlbumName(" ");
	// mPlayerRecordEntity.setAlbumPhotoName(" ");
	// mPlayerRecordEntity.setShow(false);
	// mPlayerRecordEntity.setName(" ");
	// mPlayerRecordEntity.setStartTime(0);
	// mPlayerRecordEntity.setWitchDay("1");
	// // 播放器需要的数据
	// mPlayerRecordEntity.setVrsAlbumId(" ");
	// mPlayerRecordEntity.setVrsTvId(" ");
	// // 添加到列表中
	// mPlayerRecordEntities.add(playerTodatNum, mPlayerRecordEntity);
	// // 添加假数据 计数器 ＋1
	// ++playerTodatNum;
	// }
	// if (playeryesterdayNum % 2 != 0 && playerOlderNum != 0) {
	// // 添加假数据 计数器 ＋1
	// playerAdd++;
	// // 同上 添加一个虚假的数据
	// mPlayerRecordEntity = new PlayerRecordEntity();
	// mPlayerRecordEntity.setSource(0);
	// mPlayerRecordEntity.setAlbumId(" ");
	// mPlayerRecordEntity.setAlbumName(" ");
	// mPlayerRecordEntity.setWitchDay("2");
	// mPlayerRecordEntity.setAlbumPhotoName(" ");
	// mPlayerRecordEntity.setShow(false);
	// mPlayerRecordEntity.setName(" ");
	// mPlayerRecordEntity.setStartTime(0);
	// // 播放器需要的数据
	// mPlayerRecordEntity.setVrsAlbumId(" ");
	// mPlayerRecordEntity.setVrsTvId(" ");
	// // 添加到列表中
	// mPlayerRecordEntities.add(playerTodatNum + playeryesterdayNum,
	// mPlayerRecordEntity);
	// // 添加假数据 计数器 ＋1
	// ++playeryesterdayNum;
	// }
	// changeTailLen(playerTodatNum, playeryesterdayNum, playerOlderNum);
	// if (null != mPlayerRecordEntities && mPlayerRecordEntities.size() != 0) {
	// // 发送成功的消息
	// Log.v(TAG,
	// "GETPLAYERRECORD_SUCCESS="
	// + mPlayerRecordEntities.toString());
	// handler.sendEmptyMessage(GETPLAYERRECORD_SUCCESS);
	// } else {
	// // 发送失败的消息
	// handler.sendEmptyMessage(GETPLAYERRECORD_FAIL);
	// Log.d(TAG, "访问记录失败");
	// }
	//
	// }

	private void getPlayerRecords() {
		// 获取奇艺的播放记录
		// TODO Auto-generated method stub
		// 重置 今天 昨天 更早的总数的计数器
		Log.v(TAG, "getPlayerRecords()");
		playerTodatNum = 0;
		playeryesterdayNum = 0;
		playerOlderNum = 0;
		// 当前添加 假的数据的 计数器
		playerAdd = 0;
		// 当前list添加的数据的总数的计数器
		int index = 0;
		PlayerRecordEntity mPlayerRecordEntity = null;
		// 获取当前的日期，用于跟观看记录比较
		java.util.Date dt = new Date();
		Log.d(TAG, "系统时间:" + System.currentTimeMillis());
		// 日期转换
		SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.CHINA);
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd", Locale.CHINA);
		SimpleDateFormat sdf3_1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
				Locale.CHINA);
		Log.d(TAG, "系统日期:" + sdf3_1.format(dt));
		String months_string = sdf.format(dt);
		String days_string = sdf2.format(dt);
		int months = Integer.parseInt(months_string);
		int days = (Integer.parseInt(days_string)) + 0;
		Log.d(TAG, "days===" + days);
		Log.d(TAG, "months===" + months);
		// Log.d(TAG,"mm=="+months+"=="+days+"&&&&&&&"+System.currentTimeMillis());

		Collections.sort(playerRecordEntity, new SortByTime());

		for (PlayerRecordEntity playerRecord : playerRecordEntity) {
			java.util.Date dt2 = new Date(
					((long) playerRecord.getPalyerDate()) * 1000);// 转换时间
			// 区分昨天今天明天

			int monthsOld = Integer.parseInt(sdf.format(dt2));
			int daysOld = Integer.parseInt(sdf2.format(dt2));
			SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
					Locale.CHINA);
			// 计算记录的日期和当前的日期差值
			if (months == monthsOld) {
				int space = days - daysOld;
				Log.d(TAG, "日期差:" + space);
				if (space == 0) {// 今天

					playerTodatNum++;
					playerRecord.setWitchDay("1");
				} else if (space == 1) {// 昨天
					playeryesterdayNum++;
					playerRecord.setWitchDay("2");
				} else if (space >= 2) {// 更早
					playerOlderNum++;
					playerRecord.setWitchDay("3");
				}
			} else {// 更早
				playerOlderNum++;
				playerRecord.setWitchDay("3");
			}
			Log.d(TAG,
					"mm==" + monthsOld + "==" + daysOld + "==="
							+ sdf3.format(dt2));

			mPlayerRecordEntities.add(playerRecord);
			index++;
			// 保证数据量不超过30个
			if (index >= 30)
				break;

		}

		Log.d(TAG, "numbert===" + playerTodatNum + "^^^^^^^^"
				+ playeryesterdayNum);
		Log.d(TAG, "===list.saze" + mPlayerRecordEntities.size());
		if (playerTodatNum % 2 != 0) {
			// 添加假数据 计数器 ＋1
			playerAdd++;
			// 当记录为奇数的时候应该添加一个生成偶数 ，这是添加一个虚假的数据
			mPlayerRecordEntity = new PlayerRecordEntity();
			mPlayerRecordEntity.setSource(0);
			mPlayerRecordEntity.setAlbumId(" ");
			mPlayerRecordEntity.setAlbumName(" ");
			mPlayerRecordEntity.setAlbumPhotoName(" ");
			mPlayerRecordEntity.setShow(false);
			mPlayerRecordEntity.setName(" ");
			mPlayerRecordEntity.setStartTime(0);
			mPlayerRecordEntity.setWitchDay("1");
			// 播放器需要的数据
			mPlayerRecordEntity.setVrsAlbumId(" ");
			mPlayerRecordEntity.setVrsTvId(" ");
			// 添加到列表中
			mPlayerRecordEntities.add(playerTodatNum, mPlayerRecordEntity);
			// 添加假数据 计数器 ＋1
			++playerTodatNum;
		}
		if (playeryesterdayNum % 2 != 0 && playerOlderNum != 0) {
			// 添加假数据 计数器 ＋1
			playerAdd++;
			// 同上 添加一个虚假的数据
			mPlayerRecordEntity = new PlayerRecordEntity();
			mPlayerRecordEntity.setSource(0);
			mPlayerRecordEntity.setAlbumId(" ");
			mPlayerRecordEntity.setAlbumName(" ");
			mPlayerRecordEntity.setWitchDay("2");
			mPlayerRecordEntity.setAlbumPhotoName(" ");
			mPlayerRecordEntity.setShow(false);
			mPlayerRecordEntity.setName(" ");
			mPlayerRecordEntity.setStartTime(0);
			// 播放器需要的数据
			mPlayerRecordEntity.setVrsAlbumId(" ");
			mPlayerRecordEntity.setVrsTvId(" ");
			// 添加到列表中
			mPlayerRecordEntities.add(playerTodatNum + playeryesterdayNum,
					mPlayerRecordEntity);
			// 添加假数据 计数器 ＋1
			++playeryesterdayNum;
		}
		changeTailLen(playerTodatNum, playeryesterdayNum, playerOlderNum);
		if (null != mPlayerRecordEntities && mPlayerRecordEntities.size() != 0) {
			// 发送成功的消息
			Log.v(TAG,
					"GETPLAYERRECORD_SUCCESS="
							+ mPlayerRecordEntities.toString());
			handler.sendEmptyMessage(GETPLAYERRECORD_SUCCESS);
		} else {
			// 发送失败的消息
			handler.sendEmptyMessage(GETPLAYERRECORD_FAIL);
		}
	}

	/**
	 * @author zhangpengzhan
	 * 
	 *         2014年4月9日 上午11:41:21 获取奇异的播放记录
	 * 
	 *         请求 奇异的播放记录
	 */
	class GetPlayerRecord extends SafeRunnable {
		public GetPlayerRecord() {

		}

		@Override
		public void requestData() {
			// if(mQiyiClient.isAuthSuccess()){
			getBluePlayerRecords();
			// getQIYIPlayerRecords();

		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub
			// start by huzuwei
			showErrorDialog(e.getErrorCode(), true);
			handler.sendEmptyMessage(GETPLAYERRECORD_FAIL);
			// end
		}
	}

	/*
	 * 按鍵監聽 (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "keyCode:" + keyCode + "");
		switch (keyCode) {
		// 如果 menu 按键被按下了
		case KeyEvent.KEYCODE_MENU:
			if (null != mPlayerRecordEntities
					&& mPlayerRecordEntities.size() > 0)
				DialogUtils.showDialogCancelCollect(PlayerRecordActivity.this,
						dialogListener,
						getResources()
								.getString(R.string.isdelete_playerrecord));
			break;

		case KeyEvent.KEYCODE_BACK:
			// 退出当前activity
			if (sourceFlag != null) {
				if (sourceFlag.equals("bluelight")) {
					sourceFlag = null;
					AppUtil.openAppForPackageName("com.hiveview.bluelight",
							PlayerRecordActivity.this);
				}
			}
			this.finish();
			break;
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// 焦点移走，下次在进入第一页的时候就不是第一次进入的考虑是否要把焦点设置在下一页的按钮上

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_page_down_ll:// 上一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			break;
		case R.id.list_page_up_ll:// 下一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			break;
		default:
			break;
		}
	}

	/**
	 * 监听ItemView上焦点事件，猜测用户操作，焦点落上时当前view 如果右侧为空时候 ，焦点应该落在右上侧
	 * 
	 * @author
	 * 
	 *         当走到空缺的view 的时候右键焦点跳转到他的右上的位置
	 * 
	 */
	class ViewItemFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {
			Log.d(TAG, "==" + view.getId() + "===" + has);
			if (has) {// 当前的view ，获取焦点的view
				mPlayerRecordEntity = (PlayerRecordEntity) view.getTag();
				int index = mPlayerRecordEntities.indexOf(mPlayerRecordEntity);
				if (index < mPlayerRecordEntities.size() - 2) {
					PlayerRecordEntity entity = new PlayerRecordEntity();
					// 右侧的view
					entity = mPlayerRecordEntities.get(index + 2);
					// 判断右侧是否显示
					if (!entity.isShow()) {
						Log.d(TAG, "ViewItemFocusListener===>需要跳啊");
						view.setNextFocusRightId(list.get(list.indexOf(view
								.getId()) - 5));
					}
				}
			}
		}

	}

	/**
	 * 监听ItemView上的按MENU键，用户选择是否清除观看记录
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ViewItemKeyListener implements CallBackItemViewKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
					&& event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键
				if ((null != LastViewIndexs ? LastViewIndexs
						.contains(theViewIndex) : false) && isLastPage) {
					Log.d(TAG, "LastViewIndex==>theViewIndex::" + theViewIndex);
					return true;
				}
			}
			return false;
		}
	}
	/*当前实体*/
	private PlayerRecordEntity mcurrentEntity;
	/**
	 * @author zhangpengzhan
	 * 
	 *         2014年4月14日 上午10:21:38
	 * 
	 *         点击view 直接跳转到播放器 播放文件
	 */
	class ViewItemClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			PlayerRecordEntity entity = (PlayerRecordEntity) v.getTag();
			mcurrentEntity=entity;
			// 直播
			if (entity.getSource() == 3) {
				isFristCreate = false;
				// 调到极清直播详情页
				try {
					Intent it = new Intent();
					ComponentName componentName = new ComponentName(
							"com.hiveview.bluelight",
							"com.hiveview.bluelight.activity.BlueLightActivity");
					it.setComponent(componentName);
					it.putExtra("page",
							"com.hiveview.bluelight.page.DispatchPage");
					it.putExtra("contentId", mcurrentEntity.getAlbumId() + "");
					it.putExtra("cpId", mcurrentEntity.getCpId() + "");
					it.putExtra("contentType", "2007");
					startActivity(it);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
			// 极清
			if (entity.getSource() == 1) {
				isFristCreate = false;
				Intent it = new Intent();
				ComponentName componentName = new ComponentName(
						"com.hiveview.bluelight",
						"com.hiveview.bluelight.activity.BlueLightActivity");
				it.setComponent(componentName);
				it.putExtra("page", "com.hiveview.bluelight.page.DispatchPage");
				it.putExtra("contentId", entity.getAlbumId() + "");
				it.putExtra("source", AppConstant.SOURCE_PLAYER_RECODER);
				it.putExtra("contentType", "2003");
				startActivity(it);
			} // 点播
			else if (entity.getSource() == 0) {
				isFristCreate = true;
				FilmNewEntity filmnewEntity = new FilmNewEntity();
				filmnewEntity.setName(entity.getName());
				String json = com.alibaba.fastjson.JSONObject
						.toJSONString(entity.getEntity());
				Log.d(TAG, "----json::" + json);
				QiYiPlayerUtil.startSDKPlayer(PlayerRecordActivity.this, json,
						null, false, false, true, null, false);

				KeyEventHandler.post(new DataHolder.Builder(getBaseContext())
						.setTabNo(Tab.TAB).setViewPosition("0306")
						.setSource(AppConstant.SOURCE_PLAYER_RECODER)
						.setEntity(filmnewEntity)
						.setDataType(DataType.CLICK_TAB_FILM)
						.setSrcType(ItemType.VIDEO).build());
			}
		}

	}

	/**
	 * 取消收藏对话框按钮事件监听
	 * 
	 * @author chenlixiao
	 * 
	 */
	class CancelDialogListener implements OnDialogClickListener {

		@Override
		public void onConfirm() {// 确定取消收藏
			// 清除极清播放记录
			if (BlueLightUtils.getHistoryList(PlayerRecordActivity.this) != null
					&& BlueLightUtils.getHistoryList(PlayerRecordActivity.this)
							.size() != 0) {
				BlueLightUtils.deleteAllHistory(PlayerRecordActivity.this);
			}
			if (VideoLiveVipUtils.getVideoLiveRecord(PlayerRecordActivity.this) != null
					&& VideoLiveVipUtils.getVideoLiveRecord(PlayerRecordActivity.this)
							.size() != 0) {
				VideoLiveVipUtils.DeleteVideoLiveRecord(PlayerRecordActivity.this);
				mPlayerRecordEntities.clear();
			}
			
			// 清除点播播放记录
			// submitRequest(new Runnable() {
			// public void run() {
			// hm.clearAnonymousHistory();
			// }
			// });

			QIYIRecordUtils.deleteAllHistory(PlayerRecordActivity.this);
			handler.sendEmptyMessage(GETPLAYERRECORD_FAIL);
			Log.d(TAG, "清除记录成功~~~~~~~~~~~~~");
			// 最后要关闭对话框
			DialogUtils.closeDialogCancelCollect();
		}

		@Override
		public void onCancel() {// 关闭对话框
			DialogUtils.closeDialogCancelCollect();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume() 在记录中观看电视 完了 回来之后
	 * 应该刷新数据 刷新数据
	 */

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "onResume111111");
		if (!isFristCreate) {
			Log.d(TAG, "onResume");
			// loading 框强制获取焦点，避免说上下页按钮获取焦点体验不好
			progressDialog.setVisibility(View.VISIBLE);
			progressDialog.setFocusable(true);
			progressDialog.requestFocus();
			// 发消息重置view
			handler.sendEmptyMessage(PLAYER_RECORD_RESUME);
			if (null != adapter) {
				// 清除当前viewpager 的数据
				adapter.clear();

				mPlayerRecordEntities = new ArrayList<PlayerRecordEntity>();
				playerRecordEntity = new ArrayList<PlayerRecordEntity>();
				submitRequest(new GetPlayerRecord());

			}
			// if (null != adapter) {
			// // 清除当前viewpager 的数据
			// adapter.clear();
			// // 避免第一次(oncreat)奇艺观看记录初始化失败，点击进入极清观看记录详情页再返回的时候，会一直loading,
			// // 所以再init一次以加载数据
			// // 奇异的接口
			// mQiyiClient = QiyiClient.instance();
			// mQiyiClient.initialize(getApplicationContext(),
			// "72gblq57wfdte629lfxcf&3uuwbpc4i16uj1il#d8s9i8vrv"); //
			// 第二个参数signature为测试数据
			// mQiyiClient.setListener(mConnectionListener);
			// mQiyiClient.connect();
			// }
		}
		isFristCreate = false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "onDestroy============");
		getContentResolver().unregisterContentObserver(cob);
	}

	/**
	 * @param arg1
	 *            今天的观看记录
	 * @param arg2
	 *            昨天的观看记录
	 * @param arg3
	 *            更早的观看记录
	 */
	public void changeTailLen(int arg1, int arg2, int arg3) {
		// 需要显示到几页
		todayGroup = (int) Math.ceil((arg1) / (double) 12);
		// 显示到什么位置
		todayItem = arg1 % 12;
		/*
		 * if (todayItem % 2 != 0) todayItem++;
		 */
		// 需要到显示几页
		yesterdayGroup = (int) Math.ceil((arg1 + arg2) / (double) 12);
		// 需要在什么位置显示
		yesterdayItem = todayItem / 2;
		/*
		 * if (yesterdayItem % 2 != 0) yesterdayItem++;
		 */
		// 今天的尾巴长度
		todayTailLen = (yesterdayItem / 2);
		// 昨天的尾巴长度
		yesterdayTailLen = (yesterdayItem / 2);

		// 需要第几页显示
		if (todayItem == 0)// 如果是零的 这个数字应该为1
			yesterdaPages = todayGroup + 1;
		else
			yesterdaPages = todayGroup;
		// 需要第几页显示
		olderPages = (arg1 + arg2) / 12 + 1;
		olderGroup = arg3 / 12 + 1;
		// 什么位置显示
		olderItem = ((arg1 + arg2) % 12) / 2;

		Log.d(TAG, "=arg1=" + arg1 + "=arg2=" + arg2 + "=arg3=" + arg3);
		Log.d(TAG, "=todayGroup=" + todayGroup + "=todayItem=" + todayItem);
		Log.d(TAG, "=yesterdayGroup=" + yesterdayGroup + "=yesterdayItem="
				+ yesterdayItem + "=yesterdaPages=" + yesterdaPages);
		Log.d(TAG, "=olderPages=" + olderPages + "=olderGroup=" + olderGroup
				+ "=olderItem=" + olderItem);

	}

	/**
	 * @param pageIndex
	 *            当前的页数 时间轴的翻页处理事件
	 */
	private void setOnTimeLine(int pageIndex) {
		// ========================================================
		// ==================时间轴处理==============================
		// ========================================================
		if (todayGroup >= pageIndex && pageIndex <= todayGroup
				&& playerTodatNum != 0) {
			// 显示今天和他的尾巴
			linearLayout.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.VISIBLE);
		} else {
			// 隐藏今天和他的尾巴
			linearLayout.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.GONE);
		}
		int linearLayout3_X_1 = 0;
		if (pageIndex >= yesterdaPages && pageIndex <= yesterdayGroup
				&& playeryesterdayNum != 0) {
			// 显示昨天和他的尾巴

			linearLayout3.setVisibility(View.VISIBLE);
			linearLayout4.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			if (pageIndex == yesterdaPages) {
				// 如果是第一次显示，为了控制标签的位置
				linearLayout3_X_1 = yesterdayItem * viewPageItmeWidth
						+ spaceWidth;
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						linearLayout3_X_1 - tagWidth - spaceWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = spaceWidth + tagWidth;
				linearLayout2.setLayoutParams(params2);

			} else {
				// 如果 显示
				linearLayout3_X_1 = spaceWidth;

			}
			// 为昨天标签 添加 计算好的属性值
			params.leftMargin = linearLayout3_X_1;
			linearLayout3.setLayoutParams(params);

		} else {
			// 如果没有昨天这个标签的时候，设置今天的尾巴的长度，这个可能是不准确的，所以 需要在更多里边进一步设置
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					viewPageWidth - tagWidth, TailLenWidth);
			params2.topMargin = topSpace;
			params2.leftMargin = spaceWidth + tagWidth;
			linearLayout2.setLayoutParams(params2);
			linearLayout3.setVisibility(View.GONE);
			linearLayout4.setVisibility(View.GONE);
			linearLayout3_X_1 = 0;
		}
		int linearLayout5_X = 0;
		if (pageIndex >= olderPages && playerOlderNum != 0) {
			// 显示更多
			linearLayout5.setVisibility(View.VISIBLE);
			linearLayout6.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			if (pageIndex == olderPages) {
				// 如果 显示 更多的位置
				linearLayout5_X = olderItem * viewPageItmeWidth + spaceWidth;

				RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
						linearLayout3_X_1 == 0 ? linearLayout5_X - 2 * tagWidth
								- topSpace : linearLayout3_X_1 - 2 * tagWidth
								- topSpace, TailLenWidth);
				params1.topMargin = topSpace;
				params1.leftMargin = spaceWidth + tagWidth;
				linearLayout2.setLayoutParams(params1);
				Log.d(TAG, "viewPageWidth::" + viewPageWidth
						+ "|| linearLayout3_X_1 ::" + linearLayout3_X_1
						+ "|| linearLayout3_X_1::" + viewPageItmeWidth
						+ "||linearLayout5_X::" + linearLayout5_X);
				Log.d(TAG, "params1:::"
						+ (viewPageWidth - linearLayout3_X_1
								- viewPageItmeWidth - linearLayout5_X));
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						linearLayout5_X - linearLayout3_X_1 - tagWidth,
						TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = tagWidth + linearLayout3_X_1;
				linearLayout4.setLayoutParams(params2);
				Log.d(TAG, "params2:::"
						+ (linearLayout5_X - linearLayout3_X_1 - tagWidth));
				RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
						viewPageWidth - linearLayout5_X + 9, TailLenWidth);
				params3.topMargin = topSpace;
				params3.leftMargin = tagWidth + linearLayout5_X;
				linearLayout6.setLayoutParams(params3);
				Log.d(TAG, "params3:::" + (viewPageWidth - linearLayout5_X + 9));
			} else {
				// 如果不是在第一次 显示 处理
				linearLayout5_X = spaceWidth;
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						viewPageWidth - tagWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = spaceWidth + tagWidth;
				linearLayout6.setLayoutParams(params2);
			}
			params.leftMargin = linearLayout5_X;
			linearLayout5.setLayoutParams(params);
		} else {
			linearLayout5.setVisibility(View.GONE);
			linearLayout6.setVisibility(View.GONE);
			// RelativeLayout.LayoutParams params2 = new
			// RelativeLayout.LayoutParams(olderItem*viewPageItmeWidth-yesterdayItem*viewPageItmeWidth,
			// 5);
			int linearLayout3_X = yesterdayItem * viewPageItmeWidth
					+ spaceWidth;
			if (pageIndex == yesterdaPages) {
				// 如果 显示

				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						viewPageWidth - linearLayout3_X, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = tagWidth + linearLayout3_X;
				linearLayout4.setLayoutParams(params2);
			} else {
				// 如果 显示
				linearLayout3_X = spaceWidth;
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						viewPageWidth - tagWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = spaceWidth + tagWidth;
				linearLayout4.setLayoutParams(params2);
			}

		}
	}

	/**
	 * @param indexpage
	 *            当前页数 时间轴第一页设置
	 */
	private void setOnOnePageTimeLine(int indexpage) {
		// 第一页需要显示的逻辑。好多好多啊 屮
		if ((todayGroup != 0 || todayItem != 0) && playerTodatNum != 0) {

			// 显示今天标签
			linearLayout.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.VISIBLE);

		} else if (todayGroup == 1 && todayItem == 0) {
			// 隐藏今天 标签
			today_X = spaceWidth;
			linearLayout.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.GONE);

		}

		if (yesterdaPages == 1 && playeryesterdayNum != 0) {
			// 显示昨天标签
			linearLayout3.setVisibility(View.VISIBLE);
			linearLayout4.setVisibility(View.VISIBLE);
			// 设置昨天标签的位置
			yesterday_X = yesterdayItem * viewPageItmeWidth + spaceWidth;

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = yesterday_X;
			linearLayout3.setLayoutParams(params);
			// 设置今天标签的尾巴，不管隐藏的状态
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					yesterday_X - today_X - 2 * tagWidth, TailLenWidth);
			params2.topMargin = topSpace;
			params2.leftMargin = spaceWidth + tagWidth;
			linearLayout2.setLayoutParams(params2);
		} else {
			// 隐藏昨天的标签 和尾巴
			yesterday_X = spaceWidth;
			linearLayout3.setVisibility(View.GONE);
			linearLayout4.setVisibility(View.GONE);
			// 同时设置今天的尾巴的长度
			RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
					viewPageWidth, TailLenWidth);
			params2.topMargin = topSpace;
			params2.leftMargin = spaceWidth + tagWidth;
			linearLayout2.setLayoutParams(params2);
		}

		if (olderPages == 1 && playerOlderNum != 0) {
			linearLayout5.setVisibility(View.VISIBLE);
			linearLayout6.setVisibility(View.VISIBLE);
			// 如果 设置 更早的标签的位置
			older_X = olderItem * viewPageItmeWidth + spaceWidth;
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = older_X;
			linearLayout5.setLayoutParams(params);
			// 设置更早尾巴的长度
			RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
					viewPageWidth - older_X, TailLenWidth);
			params3.topMargin = topSpace;
			params3.leftMargin = tagWidth + older_X;
			linearLayout6.setLayoutParams(params3);
			// 如果昨天的尾巴显示状态。设置他的长度呀
			if (linearLayout4.isShown()) {
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						older_X - yesterday_X - tagWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = yesterday_X + tagWidth;
				linearLayout4.setLayoutParams(params2);
			} else {// 如果不显示的话，那就设置今天的尾巴啊，不管他显示还是不显示，无所谓啊
				Log.d(TAG, "===older_X  - today_X - tagWidth=="
						+ (older_X - today_X - tagWidth));
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						older_X - today_X - tagWidth - tagWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = spaceWidth + tagWidth;
				linearLayout2.setLayoutParams(params2);
			}

		} else {
			// 隐藏更早的标签
			linearLayout5.setVisibility(View.GONE);
			linearLayout6.setVisibility(View.GONE);
			// 如果昨天的尾巴显示状态。设置他的长度呀
			if (linearLayout4.isShown()) {
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						viewPageWidth - yesterday_X + tagWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = yesterday_X + tagWidth;
				linearLayout4.setLayoutParams(params2);
			} else {// 如果不显示的话，那就设置今天的尾巴啊，不管他显示还是不显示，无所谓啊
				RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
						viewPageWidth - tagWidth, TailLenWidth);
				params2.topMargin = topSpace;
				params2.leftMargin = spaceWidth + tagWidth;
				linearLayout2.setLayoutParams(params2);
			}
		}
	}

	/**
	 * 当前view 的 位置
	 * 
	 * @ClassName: GetFocusIndex
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月4日 下午5:16:49
	 * 
	 */
	class GetFocusIndex implements GetFocusEntity {

		@Override
		public void getFocusEntity(PlayerRecordEntity playerRecordEntity,
				int entityIndex) {
			Log.d(TAG, "the view index ==>" + entityIndex);
			theViewIndex = entityIndex;
		}

	}

	/**
	 * 计算在焦点走到最后一个view 的时候向右焦点不再移动
	 * 
	 * @Title: PlayerRecordActivity
	 * @author:张鹏展
	 * @Description:
	 * @param views
	 */
	public List<Integer> setTheViewMove(int views, int pageSize) {
		Log.d(TAG, "setTheViewMove==>views::" + views + "---pageSize::"
				+ pageSize);
		// 保存最后view 所在的索引
		List<Integer> viewIndexs = new ArrayList<Integer>();
		// view的总数
		int all_view = views;
		// 是否是奇数
		boolean isOdd = false;
		// 判断综述是否是奇数
		if (all_view % 2 != 0) {
			// 补全成偶数
			all_view++;
			// 标签设置成true
			isOdd = true;
		}
		// 第一行的最后一个view 的索引
		int half_view1 = all_view / 2;
		// 第二行的最后一个索引
		int half_view2 = 0;
		if (isOdd) {// 如果是奇数最后一个应该在减去一，view 的索引是从0开始的
			// 奇数的话就是第一行的是最后一个，所以这个值不会被用到
			// half_view2 = half_view1 + (pageSize / 2 - 2);
		} else {// 如果不是奇数就不用减一
			half_view2 = half_view1 + (pageSize / 2 - 1);
		}
		// 列表中添加第一行的view 的最后一个索引
		viewIndexs.add(half_view1 - 1);
		// 如果第二行的小于一半数量，就不用添加了，就是没有第二行数据
		if (half_view2 > (pageSize / 2 - 1) && !isOdd)
			viewIndexs.add(half_view2);
		Log.d(TAG, "setTheViewMove==>half_view1::" + half_view1
				+ "---half_view2::" + half_view2);
		return viewIndexs;
	}

	HashMap<String, String[]> commands = new HashMap<String, String[]>();

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		if (intent.hasExtra("_scene")
				&& intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("recoder".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(PlayerRecordActivity.this,
							"当前已经在观看记录", intent);
				} else if ("play".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(PlayerRecordActivity.this, "播放"
							+ mPlayerRecordEntity.getAlbumName(), intent);
					QiYiPlayerUtil.startQiYiPlayer(PlayerRecordActivity.this,
							mPlayerRecordEntity.getVrsTvId(),
							mPlayerRecordEntity.getVrsAlbumId());
				} else if ("dialog".equals(command)) {
					// 对话框是否显示
					boolean isShow = DialogUtils.isDialogShow();
					Log.v("recoder--onExecute", "isShow=" + isShow);
					// 观看记录不为空且对话框没有显示
					if (null != mPlayerRecordEntities
							&& mPlayerRecordEntities.size() > 0 && !isShow) {
						DialogUtils.showDialogCancelCollect(
								PlayerRecordActivity.this,
								dialogListener,
								getResources().getString(
										R.string.isdelete_playerrecord));

					} else {
					}
					HomeSwitchTabUtil.closeSiRi(PlayerRecordActivity.this,
							"删除观看记录", intent);

				} else if ("yes".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(PlayerRecordActivity.this, "是",
							intent);
					// 清除极清播放记录
					BlueLightUtils.deleteAllHistory(PlayerRecordActivity.this);
					// 清除点播播放记录
					// submitRequest(new Runnable() {
					// public void run() {
					// hm.clearHistory();
					// }
					// });
					QIYIRecordUtils.deleteAllHistory(PlayerRecordActivity.this);
					// 最后要关闭对话框
					DialogUtils.closeDialogCancelCollect();
				} else if ("no".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(PlayerRecordActivity.this, "否",
							intent);
					DialogUtils.closeDialogCancelCollect();
				} else if ("page".equals(command)) {
					String action = intent.getStringExtra("_action");
					if ("PREV".equals(action)) {

						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == 1
								|| Integer.parseInt(tvCurrentPage.getText()
										.toString()) == 0) {
							HomeSwitchTabUtil.closeSiRi(
									PlayerRecordActivity.this, "您已经在第一页了",
									intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(
									PlayerRecordActivity.this, "上一页", intent);
							mViewPager.setCurrentItem(mViewPager
									.getCurrentItem() - 1);
						}
					}

					else if ("NEXT".equals(action)) {
						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == pageCount) {
							HomeSwitchTabUtil.closeSiRi(
									PlayerRecordActivity.this, "您已经在最后一页了",
									intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(
									PlayerRecordActivity.this, "下一页", intent);
							// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
							mViewPager.setCurrentItem(mViewPager
									.getCurrentItem() + 1);
						}
					}
					// else if("INDEX".equals(action)){
					// int index = intent.getIntExtra("index", -1);
					// HomeSwitchTabUtil
					// .closeSiRi(MovieDemandListActivity.this,
					// "第"+index+"页", intent);
					// // 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
					// mViewPager.setCurrentItem(index);
					//
					// }
				}
			}

		}
	}

	// 场景id
	private String scenceId = "com.hiveview.tv.activity.PlayerRecordActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("play", new String[] { "播放", "开始" });
		commands.put("recoder", new String[] { "观看记录" });
		commands.put("dialog", new String[] { "删除观看记录", "清除观看记录", "删除播放记录",
				"清除播放记录", "删除播放历史", "清除播放历史", "删除观看历史", "清除观看历史" });
		commands.put("yes", new String[] { "是" });
		commands.put("no", new String[] { "否" });
		commands.put("page", new String[] { "$P(_PAGE)" });
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
			jsonObject = JsonUtil.makeScenceJson(scenceId, commands,
					fuzzayWords1, fuzzayWords2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "onQunery====>2");
		return jsonObject.toString();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "disconnect()");
		// mQiyiClient.disconnect();
		// mQiyiClient.release();
	}
}
