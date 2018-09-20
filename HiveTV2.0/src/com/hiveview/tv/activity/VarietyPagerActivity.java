package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.factory.VarietyItemFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.DialogUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.utils.PlayerParamsUtils;
import com.hiveview.tv.view.VarietyViewPagerItem.ViewItemFocusChangeListeners;
import com.paster.util.JsonUtil;


/**
 * @ClassName: VarietyPagerActivity
 * @Description: 
 * @author: zhangpengzhan
 * @date 2014年4月17日 下午1:22:50
 * 
 */
public class VarietyPagerActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {

	/**
	 * 实现类的集合
	 */
	private List<FilmNewEntity> mPlayerRecordEntities = null;

	/**
	 * 获取播放记录成功
	 */
	private final int GETVARIETYENTITY_SUCCESS = 0x00560;

	/**
	 * 成功获取数据 并添加到 相应的列表中
	 */
	private final int GETVARIETYENTITY_SUCCESS_ADD_DATA = 0x00561;
	
	/**
	 *  用于处理上下翻页的handler逻辑 
	 *  */
	public static final int PAGE_DOWN = 0x1001;
	public static final int PAGE_UP = 0x1002;

	/**
	 * 获取播放记录失败
	 */
	private final int GETVARIETYENTITY_FAIL = 0x00570;

	/**
	 * 每页显示多少数据
	 */
	private final int pageViewSize = 8;

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

	//右下角菜单提示
	private 	ImageView menuImageView ;
	/**
	 * 左右两侧指示图片
	 */
	private ImageView leftImage = null;
	private ImageView rightImage = null;

	/**
	 * 如果收藏为空的时候提示用户收藏为空的提示消息
	 */
	private TextView mCollectHine = null;

	private TextView mTitle = null;

	/**
	 * 上下页按钮 焦点状态
	 */
	private boolean isChangePageByUpButton = false;
	private boolean isChangePageByDownButton = false;
	private CancelDialogListener dialogListener;

	/**
	 * loading view
	 */
	private com.hiveview.tv.view.ProgressDialog progressDialog;

	/**
	 * 种类的id 此数值为暂设 正式应该是接收到intent 中的数据
	 */
	private int CATEGORY_ID = 18;

	/**
	 * 种类的名字
	 */
	private String CATEGORY_NAME;

	/**
	 * 包含数据的实现类
	 */
	private TotalListFilmNewEntity varietyPagerEntity;

	/**
	 * viewpager 显示要使用的实现类
	 */
	private FilmEntity FilmEntity;

	/**
	 * 网络请求次数
	 */
	private int connectNum = 1;
	private int pageSize = 160;
	// 设置总页数
	int pageCount;

	/**
	 * 搜索的标签
	 * 
	 * @Fields videoAllTag
	 */
	private String videoAllTag = null;

	/**
	 * 搜索失败的界面
	 * 
	 * @Fields search_fail
	 */
	private View search_fail;
	
	/**
	 * 播放器调用的
	 */
	private String sourceID = AppConstant.SOURCE_LIST_PAGE;

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
		Intent intent = getIntent();
		if (null != intent) {
			CATEGORY_ID = intent.getIntExtra("category_id", 18);
			CATEGORY_NAME = intent.getStringExtra("category_name");
			firstClassName = CATEGORY_NAME;
			firstClassId = String.valueOf(CATEGORY_ID);
		}
		setContentView(R.layout.variety_new_layout);
		isFristCreate = true;
		videoAllTag = getResources().getString(R.string.list_top_category);
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.tv.activity.BaseActivity#processData(android.os.Message)
	 */
	protected void processData(int msgWhat) {

		switch (msgWhat) {
		// 获取数据成功
		case GETVARIETYENTITY_SUCCESS:
			// 显示布局按钮和显示页
			setShowFailView("", "");
			top_layout.setVisibility(View.VISIBLE);
			mTitle.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.VISIBLE);
			tvPageDivision.setVisibility(View.VISIBLE);
			mCollectHine.setVisibility(View.GONE);// 保证在有数据的时候这个提示信息绝对不会出现的
			menuImageView.setVisibility(View.VISIBLE);
			if (null == adapter) {
				adapter = new HivePagerAdapter(this, new VarietyItemFactory(new ViewItemFocusListener(), new ViewItemKeyListener(),
						new ViewItemClickListener(), new ViewFocusListener()), mViewPager, pageViewSize, new NeighborTwoLineStrategy());

			} else {
				adapter.clear();
			}
			// 填充数据
			adapter.addDataSource(mPlayerRecordEntities);
			// 设置页码总数--------------全部-------all-------------------------->
			tvTotal.setText(videoAllTag + String.format(getResources().getString(R.string.variety_total_text1), (varietyPagerEntity.getRecCount())));
			adapter.setDataTotalSize(varietyPagerEntity.getRecCount());// 设置数据总记录数
			// 设置适配器
			mViewPager.setAdapter(adapter);
			// 添加监听
			mViewPager.setPreloadingListener(new ViewPagerProLoadingListener());

			// 设置第一页
			if (mPlayerRecordEntities.size() != 0)
				tvCurrentPage.setText(1 + "");
			else
				tvCurrentPage.setText(0 + "");
			tvCurrentPage.setVisibility(View.VISIBLE);
			// 设置总页数
			pageCount = (int) Math.ceil(varietyPagerEntity.getRecCount() / (double) pageViewSize);

			// 添加总数
			tvTotalPage.setText(String.format(getResources().getString(R.string.list_top_page_count), pageCount));
			tvTotalPage.setVisibility(View.VISIBLE);
			// 判断是否只有一页的情况
			if (mPlayerRecordEntities.size() > pageViewSize) {
				// 不是一页
				vDownPage.setVisibility(View.VISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.VISIBLE);
			} else {// 只是一页
				// start:只是一页的时候，页数显示右对齐 author:huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
			}
			mViewPager.requestFocus();
			// loading
			progressDialog.setVisibility(View.GONE);
			break;

		// 获取数据失败 或者初始化奇艺失败
		case GETVARIETYENTITY_FAIL:

			// 隐藏各种显示内容，显示失败了 就隐藏所有的 view 所以 内部的控件都需要隐藏了
			top_layout.setVisibility(View.INVISIBLE);

			if (isSearch) {
				setShowFailView("1", "");

			} else {
				mCollectHine.setVisibility(View.VISIBLE);
			}
			tvTotal.setVisibility(View.INVISIBLE);
			mTitle.setVisibility(View.INVISIBLE);
			vDownPage.setVisibility(View.INVISIBLE);
			vUpPage.setVisibility(View.INVISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.INVISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			tvCurrentPage.setVisibility(View.INVISIBLE);
			tvTotalPage.setVisibility(View.INVISIBLE);
			tvPageDivision.setVisibility(View.INVISIBLE);
			mViewPager.setVisibility(View.INVISIBLE);
			// loading
			progressDialog.setVisibility(View.GONE);
			break;
		case GETVARIETYENTITY_SUCCESS_ADD_DATA:// 预加载数据
			adapter.addDataSource(mPlayerRecordEntities);
			break;
		}
	}

	/**
	 * 初始化 奇异播放器的数据
	 */
	public void init() {

		// loading view
		progressDialog = (com.hiveview.tv.view.ProgressDialog) findViewById(R.id.pd_list_loading);
		progressDialog.setVisibility(View.VISIBLE);

		// 因为是重用电影收藏的 所以要重新设置提示消息
		mCollectHine = (TextView) findViewById(R.id.collect_layout_collecthint);
		mCollectHine.setText(getResources().getString(R.string.varietypager_mCollectHine));
		mCollectHine.setVisibility(View.GONE);
		// 因为是重用电影收藏的 所以要重新设置标题的内容
		mTitle = (TextView) findViewById(R.id.tv_collect_lbl);
		mTitle.setText(CATEGORY_NAME + "");
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

		// 添加按键监听和焦点监听
//		vUpPage.setOnClickListener(this);
//		vDownPage.setOnClickListener(this);
		vUpPage.setOnFocusChangeListener(this);
		vDownPage.setOnFocusChangeListener(this);
		vUpPage.setOnKeyListener(mPageUpOnKeyListener);
		vDownPage.setOnKeyListener(mPageDownOnKeyListener);
		// 记录的总数和页码
		tvCurrentPage = (TextView) findViewById(R.id.list_page_current);
		tvTotalPage = (TextView) findViewById(R.id.list_page_count);
		// 展示图片的view pager
		mViewPager = (HivePreloadViewPager) findViewById(R.id.vp_list);

		// 取消
		dialogListener = new CancelDialogListener();
		mPlayerRecordEntities = new ArrayList<FilmNewEntity>();

		mViewPager.setNextPreviousPageView(vUpPage, vDownPage);

		// 搜索失败的界面
		search_fail = (RelativeLayout) this.findViewById(R.id.search_fail);
		search_fail.setVisibility(View.GONE);
		menuImageView = (ImageView) findViewById(R.id.menu_class);
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(353, 30);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rllp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		menuImageView.setLayoutParams(rllp);
//		if(!AppConstant.ISDOMESTIC){
//			menuImageView.setBackgroundResource(R.drawable.menu_class_haiwai);
//		}
		// 添加监听
		submitRequest(new GetMovieListRunnable(CATEGORY_ID, pageSize));

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
		TextView tv1 = (TextView) search_fail.findViewById(R.id.search_fail_hint1);
		TextView tv2 = (TextView) search_fail.findViewById(R.id.search_fail_hint2);
		TextView tv2_1 = (TextView) search_fail.findViewById(R.id.search_fail_hint2_1);
		TextView tv2_2 = (TextView) search_fail.findViewById(R.id.search_fail_hint2_2);
		TextView tv2_3 = (TextView) search_fail.findViewById(R.id.search_fail_hint2_3);
		TextView tv2_4 = (TextView) search_fail.findViewById(R.id.search_fail_hint2_4);
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
	 * @author zhangpengzhan
	 * 
	 *         获取旅游纪录片，体育，片花等列表页数据的Runnable,运行在异步线程
	 */
	class GetMovieListRunnable extends SafeRunnable {
		// 每页获取的条数
		private int videoId;
		// 页数
		private int pageSize;

		public GetMovieListRunnable(int videoId, int pageSize) {
			this.videoId = videoId;
			this.pageSize = pageSize;

		}

		@Override
		public void requestData() {
			// TODO Auto-generated method stub
			HiveTVService hiveTVService = new HiveTVService();
			varietyPagerEntity = hiveTVService.getVideoSetList(getBaseContext(), videoId, pageSize, connectNum).get(0);

			mPlayerRecordEntities = varietyPagerEntity.getFilms();

			if (null != varietyPagerEntity && mPlayerRecordEntities.size() != 0 && connectNum == 1) {

				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS);

			}
			if (null != varietyPagerEntity && mPlayerRecordEntities.size() != 0 && connectNum != 1) {

				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS_ADD_DATA);

			}
			if (null == varietyPagerEntity || mPlayerRecordEntities.size() == 0) {

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

	/**
	 * @author zhangpengzhan
	 * 
	 *         分类搜索接口
	 */
	class GetSearchMovieListRunnable extends SafeRunnable {
		// 每页获取的条数
		private int videoId;
		// 页数
		private int pageSize;

		public GetSearchMovieListRunnable(int videoId, int pageSize) {
			this.videoId = videoId;
			this.pageSize = pageSize;

		}

		@Override
		public void requestData() {
			// TODO Auto-generated method stub
			HiveTVService hiveTVService = new HiveTVService();
			varietyPagerEntity = hiveTVService.getVideosSetListByTag(getBaseContext(), videoId, videoTag, pageSize, 120).get(0);

			mPlayerRecordEntities = varietyPagerEntity.getFilms();

			if (null != varietyPagerEntity && mPlayerRecordEntities.size() != 0 && connectNum == 1) {

				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS);

			}
			if (null != varietyPagerEntity && mPlayerRecordEntities.size() != 0 && connectNum != 1) {

				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS_ADD_DATA);

			}
			if (null == varietyPagerEntity || mPlayerRecordEntities.size() == 0) {

				handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
			}
		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub
			showErrorDialog(e.getErrorCode(), true);
		}
	}

	/*
	 * 按鍵監聽 (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		// 如果 menu 按键被按下了
		case KeyEvent.KEYCODE_MENU:

			// 打开搜索界面
			Intent intent = new Intent();
			intent.putExtra("videoType", CATEGORY_ID);
			Log.d(TAG, "videoTag::" + CATEGORY_ID);
			intent.putExtra("orientation", false);
			intent.putExtra("tag", videoAllTag);
			intent.setClass(this, SearchConditionsActivity.class);

			startActivityForResult(intent, AppConstant.SEARCH_DATE_RESULT);
			break;

		case KeyEvent.KEYCODE_BACK:
			// 退出当前activity
			this.finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// 焦点移走，下次在进入第一页的时候就不是第一次进入的考虑是否要把焦点设置在下一页的按钮上
		isFristCreate = hasFocus;
		if (v.getId() == R.id.list_page_up_ll) {
			v.setNextFocusRightId(R.id.list_page_down_ll);
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

	private String TAG = "VarietyPagerActivity";

	/**
	 * 监听ItemView上焦点事件，猜测用户操作，焦点落上时当前view 如果右侧为空时候 ，焦点应该落在右上侧
	 * 
	 * @author
	 * 
	 *         当走到空缺的view 的时候右键焦点跳转到他的右上的位置 这个不会执行了
	 */
	class ViewItemFocusListener implements CallBackItemViewFocusListener {

		@Override
		public void onCallBackFocusChange(View view, boolean has) {

			Log.d(TAG, "==" + view.getId() + "===" + has);
			/*
			 * if (has) {// 当前的view ，获取焦点的view FilmEntity = (FilmEntity)
			 * view.getTag();
			 * 
			 * }
			 */
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
			if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {// 响应菜单按键
				/*
				 * DialogUtils.showDialogCancelCollect(CollectActivity.this,
				 * dialogListener);
				 */

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

		@Override
		public void onClick(View v) {
			FilmNewEntity entity = (FilmNewEntity) v.getTag();
			// 播放视频391301
			// 奇异的播放方法18
			isOnClick = true;
			player_Entity = entity;
			startPlayerTime = System.currentTimeMillis();
			Log.v(TAG, "=="+entity.getName());
			PlayerParamsUtils.getVideoPlayParams(entity.getId(), entity.getCid(),"", VarietyPagerActivity.this,null);
			KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB)
					.setViewPosition("0306").setSource(sourceID)
					.setEntity((FilmNewEntity) player_Entity).setDataType(DataType.CLICK_TAB_FILM)
					.setSrcType(sourceID.equals(AppConstant.SOURCE_SUBJECT)?ItemType.SUBJECT:ItemType.VIDEO)
					.build());
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

			// 最后要关闭对话框
			DialogUtils.closeDialogCancelCollect();
		}

		@Override
		public void onCancel() {// 关闭对话框
			DialogUtils.closeDialogCancelCollect();
		}
	}

	/**
	 * @author zhangpengzhan viewpager 实现的监听 2014年4月18日 上午9:27:14
	 */
	class ViewPagerProLoadingListener implements OnPreloadingListener {

		@Override
		public void setPageCurrent(int pageIndex) {// 翻页过程

			tvCurrentPage.setText(pageIndex + "");// 设置当前页数

			if (pageIndex != 1) {// 显示上一页按钮
				vUpPage.setVisibility(View.VISIBLE);
				leftImage.setVisibility(View.VISIBLE);
				// ===================================

			}

			if (pageIndex != adapter.getViews().size()) {// 显示下一页按钮
				vDownPage.setVisibility(View.VISIBLE);
				rightImage.setVisibility(View.VISIBLE);
			}
			// 最后一页
			if (pageIndex >= pageCount) {
				vUpPage.setVisibility(View.VISIBLE);
				// start 修改为gone，author：huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				leftImage.setVisibility(View.VISIBLE);
				rightImage.setVisibility(View.INVISIBLE);

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

			/*
			 * if (pageIndex >= pageCount) {
			 * if(mViewPager.getChildCount()<=(pageViewSize/2)){
			 * mViewPager.requestFocus(); } vUpPage.setVisibility(View.VISIBLE);
			 * vDownPage.setVisibility(View.INVISIBLE);
			 * leftImage.setVisibility(View.VISIBLE);
			 * rightImage.setVisibility(View.INVISIBLE);
			 * //vUpPage.requestFocus();
			 * 
			 * }
			 */

		}

		@Override
		public void preLoading(int pageSize) {
			Log.d(TAG, "====pageSize=" + pageSize);
			connectNum++;

			if (isSearch) {
				submitRequest(new GetSearchMovieListRunnable(CATEGORY_ID, connectNum));
			} else {
				submitRequest(new GetMovieListRunnable(CATEGORY_ID, pageSize));
			}
		}

		@Override
		public void preLoadNotFinish() {
			Log.d(TAG, "=======pageSize========");
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

	// 分类搜索的类型
	private String videoTag;
	// 搜索
	private boolean isSearch = false;

	// 接受返回回来的消息
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == AppConstant.SEARCH_DATE_RESULT) {
			if (null != arg2) {
				sourceID = AppConstant.SOURCE_FILTER;
				Bundle bundle = arg2.getExtras();
				videoTag = bundle.getString("Result");
				if (null != videoTag && !"".equals(videoTag)) {
					progressDialog.setVisibility(View.VISIBLE);
					videoAllTag = bundle.getString("ResultName");
					isSearch = true;
					// 加载数据
					connectNum = 1;
					submitRequest(new GetSearchMovieListRunnable(CATEGORY_ID, connectNum));
				} else {
					progressDialog.setVisibility(View.VISIBLE);
					videoAllTag = getResources().getString(R.string.list_top_category);
					// 按下返回键
					isSearch = false;
					// 加载数据
					connectNum = 1;
					// 加载数据
					// 添加监听
					submitRequest(new GetMovieListRunnable(CATEGORY_ID, pageSize));
				}
			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub

		// 如果是搜索返回就搜索全部
		if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (isSearch) {
				progressDialog.setVisibility(View.VISIBLE);
				sourceID = AppConstant.SOURCE_LIST_PAGE;
				videoAllTag = getResources().getString(R.string.list_top_category);
				// 按下返回键
				isSearch = false;
				// 加载数据
				connectNum = 1;
				// 加载数据
				// 添加监听
				submitRequest(new GetMovieListRunnable(CATEGORY_ID, pageSize));

				return true;
			}
		}

		return super.dispatchKeyEvent(event);
	}

	class ViewFocusListener implements ViewItemFocusChangeListeners {

		@Override
		public void viewFocusListener(View v, boolean arg1) {
			if (arg1) {// 当前的view ，获取焦点的view
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
					HomeSwitchTabUtil.closeSiRi(VarietyPagerActivity.this,
							"打开分类筛选", intent);
					// 打开搜索界面
					
					// 打开搜索界面
					Intent intent1 = new Intent();
					intent1.putExtra("videoType", CATEGORY_ID);
					Log.d(TAG, "videoTag::" + CATEGORY_ID);
					intent1.putExtra("orientation", false);
					intent1.putExtra("tag", videoAllTag);
					intent1.setClass(this, SearchConditionsActivity.class);

					startActivityForResult(intent1, AppConstant.SEARCH_DATE_RESULT);				}
			
				
				if ("page".equals(command)) {
				String action = intent.getStringExtra("_action");
				if ("PREV".equals(action)) {

					if (Integer
							.parseInt(tvCurrentPage.getText().toString()) == 1
							|| Integer.parseInt(tvCurrentPage.getText()
									.toString()) == 0) {
						HomeSwitchTabUtil.closeSiRi(
								VarietyPagerActivity.this, "您已经在第一页了",
								intent);
					} else {
						HomeSwitchTabUtil
								.closeSiRi(VarietyPagerActivity.this,
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
								VarietyPagerActivity.this, "您已经在最后一页了",
								intent);
					} else {
						HomeSwitchTabUtil
								.closeSiRi(VarietyPagerActivity.this,
										"下一页", intent);
						// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
						isChangePageByUpButton = true;
						mViewPager.setCurrentItem(mViewPager
								.getCurrentItem() + 1);
					}
				}
//				else if("INDEX".equals(action)){
//					int index = intent.getIntExtra("index", -1);
//					HomeSwitchTabUtil
//					.closeSiRi(MovieDemandListActivity.this,
//							"第"+index+"页", intent);
//			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
//			mViewPager.setCurrentItem(index);
//					
//				}
			}

		}}
	}
	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.VarietyPagerActivity";

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
