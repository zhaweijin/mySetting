package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.content.CategoryDetailManager;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.content.ContentShowType;
import com.hiveview.tv.common.factory.MovieFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.view.MovieListItemPageView.ViewItemFocusChangeListeners;
import com.hiveview.tv.view.ProgressDialog;
import com.paster.util.JsonUtil;

/**
 * @author gusongsheng 点播列表竖向显示 2014-4-17
 * 
 */

@SuppressLint("ResourceAsColor")
public class MovieDemandListActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener {
	private static final String TAG = "MovieDemandListActivity";
	/**
	 *  用于处理上下翻页的handler逻辑 
	 *  */
	public static final int PAGE_DOWN = 0x1001;
	public static final int PAGE_UP = 0x1002;
	/**
	 * 分类ID 如:电影的id为1
	 */
	protected int CATEGORY_ID = 1;
	/**
	 * 分类名称 如:id为1的分类名称为电影
	 */
	protected String CATEGORY_NAME = "电影";

	/**
	 * 如果内容为空的时候提示用户暂时没有符合筛选条件的
	 */
	private TextView selectedHine = null;
	/**
	 * 显示分类名称
	 */
	private TextView tvCatgory = null;
	/**
	 * 等待框
	 */
	private ProgressDialog vLoading = null;
	/**
	 * 共有多少部视频
	 */
	private TextView tvTotal = null;
	/**
	 * 上一页按钮
	 */
	private View vUpPage = null;
	/**
	 * 下一页按钮
	 */
	private View vDownPage = null;
	/**
	 * 顶部包含按钮 和显示页码的 布局
	 */
	private RelativeLayout top_layout = null;
	/**
	 * 当前页数与总页数之间的分割
	 */
	private TextView tvListDivision = null;
	/**
	 * 左右两侧指示图片
	 */
	private ImageView leftImage = null;
	private ImageView rightImage = null;
	/**
	 * 显示当前页
	 */
	private TextView tvCurrentPage = null;
	/**
	 * 显示总页数
	 */
	private TextView tvTotalPage = null;
	/**
	 * 显示数据列表的ViewPager
	 */
	private HivePreloadViewPager mViewPager = null;
	private HivePagerAdapter adapter = null;
	/**
	 * 实现类的集合
	 */
	private List<FilmNewEntity> mPlayerRecordEntities = null;
	/**
	 * 每页显示多少数据
	 */
	private final int pageViewSize = 12;
	/**
	 * 网络请求次数
	 */
	private int connectNum = 1;
	private int pageSize = 240;
	/**
	 * 包含数据的实现类
	 */
	protected List<TotalListFilmNewEntity> totalMovieList;
	/**
	 * 成功获取数据 并添加到 相应的列表中
	 */
	private final int GETVARIETYENTITY_SUCCESS_ADD_DATA = 100;

	/**
	 * 获取播放记录失败
	 */
	private final int GETVARIETYENTITY_FAIL = -100;
	/**
	 * 获取播放记录成功
	 */
	private final int GETVARIETYENTITY_SUCCESS = 200;
	/**
	 * 设置总页数
	 */
	int pageCount;
	/**
	 * viewpager 显示要使用的实现类
	 */
	private FilmNewEntity filmEntity;
	/**
	 * 上下页按钮 焦点状态
	 */
	private boolean isChangePageByUpButton = false;
	private boolean isChangePageByDownButton = false;
	/**
	 * 服务类 访问网络
	 */
	private HiveTVService service;

	/**
	 * 是否是搜索操作
	 * 
	 * @Fields isSearch
	 */
	private boolean isSearch = false;
	/**
	 * 搜索的标签
	 * 
	 * @Fields videoAllTag
	 */
	private String videoAllTag = null;
	/**
	 * 分类筛选提示文字信息
	 * 
	 * @Fields hintText
	 */
	private TextView hintText;

	/**
	 * 搜索失败显示的界面
	 * 
	 * @Fields search_fail
	 */
	private RelativeLayout search_fail;
	
	/**
	 * 播放器调用的来源
	 */
	private String source_ID = AppConstant.SOURCE_LIST_PAGE;
	//右下角菜单提示
	private 	ImageView menuImageView ;

	/*
	 * 初始化 数据
	 */
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Intent intent = getIntent();
		// 获取数据
		if (null != intent) {
			CATEGORY_ID = intent.getIntExtra("category_id", 0);
			CATEGORY_NAME = intent.getStringExtra("category_name");
			firstClassName = CATEGORY_NAME;
			firstClassId = String.valueOf(CATEGORY_ID);
		}
		setContentView(R.layout.movie_demand_list_layout);
		videoAllTag = getResources().getString(R.string.list_top_category);
		initView();
		menuImageView = (ImageView) findViewById(R.id.menu_class);
//		if(AppConstant.ISDOMESTIC){
		// 如果是综艺就隐藏分类筛选的提示消息，===》今天又给了新的综艺的搜索ue
		if (CategoryDetailManager.getInstance().getShowTypeByCategoryId(
				CATEGORY_ID) != ContentShowType.TYPE_VARIETY_VIDEO_DETAIL) {
//			hintText = (TextView) findViewById(R.id.user_hint);
//			hintText.setText("xxx按菜单键搜索查找影片");
//			hintText.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(353, 30);
			rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		//	menuImageView.setVisibility(View.VISIBLE);
			menuImageView.setLayoutParams(rllp);
		}
//		}//海外
//		else{
//			RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(353, 30);
//			rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//			rllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			menuImageView= (ImageView) findViewById(R.id.menu_class);
//			menuImageView.setBackgroundResource(R.drawable.menu_class_haiwai);
//			menuImageView.setLayoutParams(rllp);
//			
//			
//		}
	}

	/**
	 * 初始化数据
	 */
	private void initView() {

		// 等待框
		vLoading = (ProgressDialog) this.findViewById(R.id.pd_list_loading);
		// 空数据时显示的数据
		selectedHine = (TextView) this
				.findViewById(R.id.movie_layout_moviehint);
		// 分类名称
		tvCatgory = (TextView) this.findViewById(R.id.list_top_layout_category);
		top_layout = (RelativeLayout) this.findViewById(R.id.list_top_layout);
		// 总条数
		tvTotal = (TextView) this
				.findViewById(R.id.list_top_layout_total_count);
		// 页码的分隔符
		tvListDivision = (TextView) this.findViewById(R.id.list_division);

		// 左右两侧的指示图片
		leftImage = (ImageView) this.findViewById(R.id.list_left);
		rightImage = (ImageView) this.findViewById(R.id.list_right);

		// 上一页 下一页
		vUpPage = this.findViewById(R.id.list_page_up_ll);
		vDownPage = this.findViewById(R.id.list_page_down_ll);

		// 记录的总数和页码
		tvCurrentPage = (TextView) this.findViewById(R.id.list_page_current);
		tvTotalPage = (TextView) this.findViewById(R.id.list_page_count);

		// 展示图片的view pager
		mViewPager = (HivePreloadViewPager) this.findViewById(R.id.vp_list);
		mPlayerRecordEntities = new ArrayList<FilmNewEntity>();

		// 添加按键监听和焦点监听
//		vUpPage.setOnClickListener(this);
//		vDownPage.setOnClickListener(this);
		vUpPage.setOnFocusChangeListener(this);
		vDownPage.setOnFocusChangeListener(this);
		vUpPage.setOnKeyListener(mPageUpOnKeyListener);
		vDownPage.setOnKeyListener(mPageDownOnKeyListener);

		mViewPager.setNextPreviousPageView(vUpPage, vDownPage);
		selectedHine.setVisibility(View.INVISIBLE);
		tvCatgory.setVisibility(View.INVISIBLE);
		top_layout.setVisibility(View.INVISIBLE);
		tvTotal.setVisibility(View.INVISIBLE);
		leftImage.setVisibility(View.INVISIBLE);
		rightImage.setVisibility(View.INVISIBLE);

		vLoading.setVisibility(View.VISIBLE);
		// 搜索失败的界面
		search_fail = (RelativeLayout) this.findViewById(R.id.search_fail);
		search_fail.setVisibility(View.GONE);

		// 加载数据
		submitRequest(new GetMovieDemandList(CATEGORY_ID, pageSize));
	}
	/**
	 * 下一页翻页
	 * */
	private OnKeyListener mPageDownOnKeyListener = new OnKeyListener() {
		private static final int CENTER_KEYCODE = 66;// 遥控器OK键键值
		boolean isDownKeyDown = true;

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN && (keyCode == CENTER_KEYCODE || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
					&& v.getId() == vDownPage.getId()) {
				if (isDownKeyDown) {
					Log.d(TAG, "onLongClickListner=>ACTION_DOWN**************下一页****下");
					clickHandler.removeMessages(PAGE_UP);
					clickHandler.sendEmptyMessage(PAGE_DOWN);
					isDownKeyDown = false;
				}

			} else if (event.getAction() == MotionEvent.ACTION_UP && (keyCode == CENTER_KEYCODE || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
					&& v.getId() == vDownPage.getId()) {
				Log.d(TAG, "onLongClickListner=>ACTION_UP************下一页*****上");
				clickHandler.removeMessages(PAGE_DOWN);
				clickHandler.removeMessages(PAGE_UP);
				isDownKeyDown = true;
			}
			return false;
		}
	};
	
	/**
	 * 上一页翻页
	 * */
	private OnKeyListener mPageUpOnKeyListener = new OnKeyListener() {
		private static final int CENTER_KEYCODE = 66;// 遥控器OK键键值
		boolean isUpKeyDown = true;

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN && (keyCode == CENTER_KEYCODE || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
					&& v.getId() == vUpPage.getId()) {
				if (isUpKeyDown) {
					Log.d(TAG, "onLongClickListner=>ACTION_DOWN-----上一页---按下》");
					clickHandler.removeMessages(PAGE_DOWN);
					clickHandler.sendEmptyMessage(PAGE_UP);
					isUpKeyDown = false;
				}

			} else if (event.getAction() == MotionEvent.ACTION_UP && (keyCode == CENTER_KEYCODE || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
					&& v.getId() == vUpPage.getId()) {
				Log.d(TAG, "onLongClickListner=>ACTION_UP--------上一页---弹起《");
				clickHandler.removeMessages(PAGE_UP);
				clickHandler.removeMessages(PAGE_DOWN);
				isUpKeyDown = true;
			}
			return false;
		}
	};
	Handler clickHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == PAGE_DOWN) {
				clickHandler.removeMessages(PAGE_DOWN);
				clickHandler.sendEmptyMessageDelayed(PAGE_DOWN, 1500);
				Log.d(TAG, "PAGE_DOWN!!!!下一页!!!!!");
				int currentPosition = mViewPager.getCurrentItem();
				if (currentPosition == adapter.getCount() - 1) {// 如果当前页是最后一页，则无需变化(正常不能出现这种情况，防止有bug时出现这种情况会奔溃)
					return;
				} else {
					isChangePageByDownButton = true;
					mViewPager.setCurrentItem(currentPosition + 1);
				}
			} else if (msg.what == PAGE_UP) {
				clickHandler.removeMessages(PAGE_UP);
				clickHandler.sendEmptyMessageDelayed(PAGE_UP, 1500);
				Log.d(TAG, "PAGE_up!!!上一页!!!!");
				int currentPosition = mViewPager.getCurrentItem();
				if (currentPosition == 0) {// 如果当前页是第一页，则无需变化(正常不能出现这种情况，防止有bug时出现这种情况会奔溃)
					return;
				} else {
					isChangePageByUpButton = true;
					mViewPager.setCurrentItem(currentPosition - 1);
				}
			}
		}

	};

	/**
	 * 显示失败的界面
	 * 
	 * @Title: MovieDemandListActivity
	 * @author:张鹏展
	 * @Description:
	 * @param isSwitch
	 */
	public void setShowFailView(String isSwitch, String tag) {
		TextView tv1 = (TextView) search_fail
				.findViewById(R.id.search_fail_hint1);
		TextView tv2 = (TextView) search_fail
				.findViewById(R.id.search_fail_hint2);
		TextView tv2_1 = (TextView) search_fail
				.findViewById(R.id.search_fail_hint2_1);
		TextView tv2_2 = (TextView) search_fail
				.findViewById(R.id.search_fail_hint2_2);
		TextView tv2_3 = (TextView) search_fail
				.findViewById(R.id.search_fail_hint2_3);
		TextView tv2_4 = (TextView) search_fail
				.findViewById(R.id.search_fail_hint2_4);
		search_fail.setVisibility(View.VISIBLE);
		// 区分显示
		if ("1".equals(isSwitch)) {
			tv1.setVisibility(View.VISIBLE);
			tv2.setVisibility(View.INVISIBLE);
			tv2_1.setVisibility(View.INVISIBLE);
			tv2_2.setVisibility(View.INVISIBLE);
			tv2_3.setVisibility(View.INVISIBLE);
			tv2_4.setVisibility(View.INVISIBLE);
		} else if ("2".equals(isSwitch)) {
			tv1.setVisibility(View.INVISIBLE);
			tv2.setVisibility(View.VISIBLE);
			tv2_1.setVisibility(View.VISIBLE);
			tv2_2.setVisibility(View.VISIBLE);
			tv2_3.setVisibility(View.VISIBLE);
			tv2_4.setVisibility(View.VISIBLE);
		} else {
			tv1.setVisibility(View.INVISIBLE);
			tv2.setVisibility(View.INVISIBLE);
			tv2_1.setVisibility(View.INVISIBLE);
			tv2_2.setVisibility(View.INVISIBLE);
			tv2_3.setVisibility(View.INVISIBLE);
			tv2_4.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取 网络的数据
	 */
	class GetMovieDemandList extends SafeRunnable {
		// 分类id
		private int videoId;
		// 页数
		private int pageSize;

		public GetMovieDemandList(int videoId, int pageSize) {
			this.videoId = videoId;
			this.pageSize = pageSize;

		}

		@Override
		public void requestData() {
			// TODO Auto-generated method stub
			// try {
			service = new HiveTVService();

			totalMovieList = service
					.getVideoSetList(MovieDemandListActivity.this, videoId,
							pageSize, connectNum);

			mPlayerRecordEntities = totalMovieList.get(0).getFilms();
			// 服务器返回的数据为空，节目集合的长度不为空，历史播放的长度等于一， 获取播放记录
			if (null != totalMovieList && mPlayerRecordEntities.size() != 0
					&& connectNum == 1) {
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS);
			}
			// 服务器返回的数据为空，节目集合的长度不为空，历史播放的长度不等于一， 添加相应列表的数据
			if (null != totalMovieList && mPlayerRecordEntities.size() != 0
					&& connectNum != 1) {
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS_ADD_DATA);
			}
			// 服务器返回的数据为空，节目集合的长度不为空，历史播放的长度等于一， 获取播放记录失败
			if (null == totalMovieList || mPlayerRecordEntities.size() == 0) {

				handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
			}
			// start by huzuwei
			// } catch (Exception e) {
			// // start
			// // guosongsheng 添加异常处理
			// handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
			// LogUtil.info(e.getMessage());
			// // end
			// }
			// end by huzuwei
		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub
			showErrorDialog(e.getErrorCode(), true);
			// start:发送异常，author:huzuwei
			handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
			// end
		}
	}

	/**
	 * 获取 网络的数据
	 */
	class GetSearchMovieDemandList extends SafeRunnable {
		// 分类id
		private int videoId;
		// 页数
		private int pageSize;

		public GetSearchMovieDemandList(int videoId, int pageSize) {
			this.videoId = videoId;
			this.pageSize = pageSize;

		}

		@Override
		public void requestData() {
			// TODO Auto-generated method stub
			service = new HiveTVService();

			totalMovieList = service.getVideosSetListByTag(getBaseContext(),
					videoId, videoTag, pageSize, 120);

			mPlayerRecordEntities = totalMovieList.get(0).getFilms();
			// 服务器返回的数据为空，节目集合的长度不为空，历史播放的长度等于一， 获取播放记录
			if (null != totalMovieList && mPlayerRecordEntities.size() != 0
					&& connectNum == 1) {
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS);
			}
			// 服务器返回的数据为空，节目集合的长度不为空，历史播放的长度不等于一， 添加相应列表的数据
			if (null != totalMovieList && mPlayerRecordEntities.size() != 0
					&& connectNum != 1) {
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS_ADD_DATA);
			}
			// 服务器返回的数据为空，节目集合的长度不为空，历史播放的长度等于一， 获取播放记录失败
			if (null == totalMovieList || mPlayerRecordEntities.size() == 0) {

				handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
			}
		}

		@Override
		public void processServiceException(ServiceException e) {
			// 显示错误提示框
			showErrorDialog(e.getErrorCode(), true);
			handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
		}
	}

	protected void processData(int msgWhat) {

		switch (msgWhat) {
		// 获取数据成功
		case GETVARIETYENTITY_SUCCESS:
			vLoading.setVisibility(View.GONE);
			// 显示布局按钮和显示页
			top_layout.setVisibility(View.VISIBLE);
			tvCatgory.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.VISIBLE);
			selectedHine.setVisibility(View.GONE);// 保证在有数据的时候这个提示信息绝对不会出现的
			tvCatgory.setText(CATEGORY_NAME);
			if (CategoryDetailManager.getInstance().getShowTypeByCategoryId(
					CATEGORY_ID) != ContentShowType.TYPE_VARIETY_VIDEO_DETAIL) {
			menuImageView.setVisibility(View.VISIBLE);	
			}
			// 隐藏搜索失败界面
			setShowFailView("", "");
			if (null == adapter) {
				adapter = new HivePagerAdapter(this, new MovieFactory(
						new ViewItemKeyListener(), new ViewItemClickListener(),
						new ViewFocusListener()), mViewPager, pageViewSize,
						new NeighborTwoLineStrategy());
			} else {
				adapter.clear();
			}
			// 填充数据
			adapter.addDataSource(mPlayerRecordEntities);
			// 设置页码总数------------全部----------all----------------------------------->
			tvTotal.setText(videoAllTag
					+ String.format(
							getResources().getString(
									R.string.variety_total_text1),
							(totalMovieList.get(0).getRecCount())));
			adapter.setDataTotalSize(totalMovieList.get(0).getRecCount());// 设置数据总记录数
			// 设置适配器list_top_category
			mViewPager.setAdapter(adapter);

			// 添加监听
			mViewPager.setPreloadingListener(new ViewPagerProLoadingListener());

			// 设置第一页
			if (mPlayerRecordEntities.size() != 0) {
				tvCurrentPage.setText(1 + "");
			} else {
				tvCurrentPage.setText(0 + "");
			}
			tvCurrentPage.setVisibility(View.VISIBLE);
			// 设置总页数
			pageCount = (int) Math.ceil(totalMovieList.get(0).getRecCount()
					/ (double) pageViewSize);
			// 添加总数
			tvTotalPage.setText(String.format(
					getResources().getString(R.string.list_top_page_count),
					pageCount));
			tvTotalPage.setVisibility(View.VISIBLE);
			tvTotalPage.setTextColor(0xffFD8D00);
			tvListDivision.setTextColor(0xffFD8D00);
			// 判断是否只有一页的情况
			if (mPlayerRecordEntities.size() > pageViewSize) {
				// 不是一页
				vDownPage.setVisibility(View.VISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.VISIBLE);
			} else {// 只是一页
				// start:只是一页的时候，显示页数右对齐 author:huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);

			}
			mViewPager.requestFocus();
			tvListDivision.setVisibility(View.VISIBLE);
			break;

		// 获取数据失败 或者初始化奇艺失败
		case GETVARIETYENTITY_FAIL:
			vLoading.setVisibility(View.GONE);
			menuImageView.setVisibility(View.GONE);	
			// 隐藏各种显示内容，显示失败了 就隐藏所有的 view 所以 内部的控件都需要隐藏了

			if (isSearch) {
				setShowFailView("1", "");
				top_layout.setVisibility(View.VISIBLE);
				// 设置页码总数
				tvTotal.setText(videoAllTag
						+ String.format(
								getResources().getString(
										R.string.variety_total_text1), (0)));
				tvCurrentPage.setVisibility(View.VISIBLE);
				tvListDivision.setVisibility(View.VISIBLE);
				tvTotalPage.setText("1页");
				tvTotalPage.setVisibility(View.VISIBLE);
				tvListDivision.setTextColor(0xffffffff);
				tvTotalPage.setTextColor(0xffffffff);
				tvTotal.setVisibility(View.VISIBLE);
				tvCatgory.setVisibility(View.VISIBLE);
			} else {
				selectedHine.setVisibility(View.VISIBLE);
				top_layout.setVisibility(View.INVISIBLE);
				tvCurrentPage.setVisibility(View.INVISIBLE);
				tvListDivision.setVisibility(View.INVISIBLE);
				tvTotalPage.setVisibility(View.INVISIBLE);
				tvTotal.setVisibility(View.INVISIBLE);
				tvCatgory.setVisibility(View.INVISIBLE);
			}

			vDownPage.setVisibility(View.INVISIBLE);
			vUpPage.setVisibility(View.INVISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.INVISIBLE);
			mViewPager.setVisibility(View.INVISIBLE);

			break;
		case GETVARIETYENTITY_SUCCESS_ADD_DATA:// 预加载数据
			vLoading.setVisibility(View.GONE);
			adapter.addDataSource(mPlayerRecordEntities);
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
			LogUtil.info("==" + view.getId() + "===" + has);

		}

	}

	/**
	 * 监听ItemView上的按MENU键，用户选择是否取消收藏
	 * 
	 * @author chenlixiao
	 * 
	 */
	class ViewItemKeyListener implements CallBackItemViewKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_MENU
					&& event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键
				/*
				 * DialogUtils.showDialogCancelCollect(CollectActivity.this,
				 * dialogListener);
				 */
			}
			return false;
		}
	}

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class ViewItemClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				FilmNewEntity entity = (FilmNewEntity) v.getTag();
				String action = ContentInvoker.getInstance().getContentAction(
						entity.getCid());
				Intent intent = new Intent();
				intent.setAction(action);
				intent.putExtra("source", source_ID);
				intent.putExtra("entity", entity);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 预加载
	 */
	class ViewPagerProLoadingListener implements OnPreloadingListener {

		@Override
		public void setPageCurrent(int pageIndex) {// 翻页过程

			tvCurrentPage.setText(pageIndex + "");// 设置当前页数

			if (pageIndex != 1) {// 显示上一页按钮
				vUpPage.setVisibility(View.VISIBLE);
				leftImage.setVisibility(View.VISIBLE);

			}

			if (pageIndex != adapter.getViews().size()) {// 显示下一页按钮
				vDownPage.setVisibility(View.VISIBLE);
				rightImage.setVisibility(View.VISIBLE);
			}
			if (pageIndex >= pageCount) {
				vUpPage.setVisibility(View.VISIBLE);
				// start 修改为gone author:huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				leftImage.setVisibility(View.VISIBLE);
				rightImage.setVisibility(View.INVISIBLE);

				// start 修改在最后一页的时候viewpager 强制获取焦点,不再让焦点转移到上一页
				// zhangpengzhan 修改

				// vUpPage.requestFocus();

			}
			if (isChangePageByUpButton || isChangePageByDownButton) {
				if (isChangePageByUpButton) {
					vUpPage.requestFocus();
					// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
					isChangePageByUpButton = false;
				}

				if (isChangePageByDownButton) {
					vDownPage.requestFocus();
					// 初始化焦点状态，保证用户在翻页的时候按键总是在同一按钮上
					isChangePageByDownButton = false;
				}
			}
		}

		@Override
		public void preLoading(int pageSize) {
			connectNum++;

			if (isSearch) {
				submitRequest(new GetSearchMovieDemandList(CATEGORY_ID,
						connectNum));
			} else {
				submitRequest(new GetMovieDemandList(CATEGORY_ID, pageSize));
			}
		}

		@Override
		public void preLoadNotFinish() {
		}

		@Override
		public void onLastPage() {// 最后一页
			LogUtil.info("onLastPage()");
			// 隐藏下一页按钮和指示图片

		}

		@Override
		public void onFirstPage() {// 第一页
			// 隐藏上一页按钮和指示图片
			vUpPage.setVisibility(View.INVISIBLE);
			vDownPage.setVisibility(View.VISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.VISIBLE);
			// 如果是第一次加载
			/*
			 * if (isFristCreate) { vDownPage.requestFocus(); }
			 */

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_page_down_ll:// 上一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			isChangePageByDownButton = true;
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			break;
		case R.id.list_page_up_ll:// 下一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			isChangePageByUpButton = true;
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// 焦点移走，下次在进入第一页的时候就不是第一次进入的考虑是否要把焦点设置在下一页的按钮上
		if (v.getId() == R.id.list_page_up_ll) {
			v.setNextFocusRightId(R.id.list_page_down_ll);
		}
		if (v.getId() == R.id.list_page_down_ll) {
			v.setNextFocusLeftId(R.id.list_page_up_ll);
		}
	}

	// 按键监听
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			// 打开搜索界面
			Intent intent = new Intent();
			intent.putExtra("videoType", CATEGORY_ID);
			intent.putExtra("orientation", true); // orientation
			intent.setClass(this, SearchConditionsActivity.class);
			// 综艺不打开搜索,依旧打开，只显示输入搜索按钮
			/* if (CATEGORY_ID != AppConstant.VIDEO_TYPE_VARIETY) */
			startActivityForResult(intent, AppConstant.SEARCH_DATE_RESULT);
		}
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (isSearch) {
				source_ID = AppConstant.SOURCE_LIST_PAGE;
				videoAllTag = getResources().getString(R.string.list_top_category);
				vLoading.setVisibility(View.VISIBLE);
				// 按下返回键
				isSearch = false;
				// 加载数据
				connectNum = 1;
				// 加载数据
				submitRequest(new GetMovieDemandList(CATEGORY_ID, pageSize));
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	// 分类搜索的类型
	private String videoTag;

	// 接受返回回来的消息
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == AppConstant.SEARCH_DATE_RESULT) {
			if (null != arg2) {
				Bundle bundle = arg2.getExtras();
				videoTag = bundle.getString("Result");
				Log.d("MovieDemandListActivity==>onActivityResult::",
						"videoTag::" + videoTag);
				source_ID = AppConstant.SOURCE_FILTER;
				if (null != videoTag && !"".equals(videoTag)) {
					vLoading.setVisibility(View.VISIBLE);
					videoAllTag = bundle.getString("ResultName");
					isSearch = true;
					// 加载数据
					connectNum = 1;
					submitRequest(new GetSearchMovieDemandList(CATEGORY_ID,
							connectNum));
				} else {
					videoAllTag = getResources().getString(R.string.list_top_category);
					vLoading.setVisibility(View.VISIBLE);
					// 按下返回键
					isSearch = false;
					// 加载数据
					connectNum = 1;
					// 加载数据
					submitRequest(new GetMovieDemandList(CATEGORY_ID, pageSize));
				}
			}
		}
	}

	class ViewFocusListener implements ViewItemFocusChangeListeners {

		@Override
		public void viewFocusListener(View v, boolean arg1) {
			if (arg1) {// 当前的view ，获取焦点的view
				filmEntity = (FilmNewEntity) v.getTag();
				AnimationUtil.getBigAnimation(getBaseContext(), v);
			} else {
				AnimationUtil.getLitterAnimation(getBaseContext(), v);
			}
		}

	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene")
				&& intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("menu".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(MovieDemandListActivity.this,
							"打开分类筛选", intent);
					// 打开搜索界面
					Intent intent1 = new Intent();
					intent1.putExtra("videoType", CATEGORY_ID);
					intent1.putExtra("orientation", true); // orientation
					intent1.setClass(this, SearchConditionsActivity.class);
					// 综艺不打开搜索,依旧打开，只显示输入搜索按钮
					/* if (CATEGORY_ID != AppConstant.VIDEO_TYPE_VARIETY) */
					startActivityForResult(intent1,
							AppConstant.SEARCH_DATE_RESULT);
				} else if ("page".equals(command)) {
					String action = intent.getStringExtra("_action");
					if ("PREV".equals(action)) {

						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == 1
								|| Integer.parseInt(tvCurrentPage.getText()
										.toString()) == 0) {
							HomeSwitchTabUtil.closeSiRi(
									MovieDemandListActivity.this, "您已经在第一页了",
									intent);
						} else {
							HomeSwitchTabUtil
									.closeSiRi(MovieDemandListActivity.this,
											"上一页", intent);
							isChangePageByDownButton = true;
							mViewPager.setCurrentItem(mViewPager
									.getCurrentItem() - 1);
						}
					}

					else if ("NEXT".equals(action)) {
						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == pageCount) {
							HomeSwitchTabUtil.closeSiRi(
									MovieDemandListActivity.this, "您已经在最后一页了",
									intent);
						} else {
							HomeSwitchTabUtil
									.closeSiRi(MovieDemandListActivity.this,
											"下一页", intent);
							// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
							isChangePageByUpButton = true;
							mViewPager.setCurrentItem(mViewPager
									.getCurrentItem() + 1);
						}
					}
//					else if("INDEX".equals(action)){
//						int index = intent.getIntExtra("index", -1);
//						HomeSwitchTabUtil
//						.closeSiRi(MovieDemandListActivity.this,
//								"第"+index+"页", intent);
//				// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
//				mViewPager.setCurrentItem(index);
//						
//					}
				}
			}

		}
	}
	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.MovieDemandListActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("menu", new String[] { "分类查找影片", "分类筛选", "分类查找", "筛选影片",
				"影片分类", "影片筛选" });
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

}
