package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewAppLib;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.factory.SearchItemFactory;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.entity.SearchEntity;
import com.hiveview.tv.service.entity.TotalListSearchEntity;
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.DialogUtils;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.utils.PlayerParamsUtils;
import com.hiveview.tv.utils.PlayerParamsUtils.VideoPlayerListener;
import com.hiveview.tv.view.SearchCategoryPopupWindow;
import com.hiveview.tv.view.SearchCategoryPopupWindow.ReleasePopupWindow;
import com.hiveview.tv.view.SearchCategoryPopupWindow.onUserSelectedListener;
import com.paster.util.JsonUtil;

public class SearchPageActivtiy extends BaseActivity implements
		OnClickListener, OnFocusChangeListener {
	/**
	 * 实现类的集合
	 */
	private List<SearchEntity> mPlayerRecordEntities = null;

	/**
	 * 获取播放记录成功
	 */
	private final int GETVARIETYENTITY_SUCCESS = 0x00560;

	/**
	 * 成功获取数据 并添加到 相应的列表中
	 */
	private final int GETVARIETYENTITY_SUCCESS_ADD_DATA = 0x00561;

	/**
	 * 获取播放记录失败
	 */
	private final int GETVARIETYENTITY_FAIL = 0x00570;

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
	 * 搜索的类型
	 */
	private TextView tvType = null;

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
	private TotalListSearchEntity searchPagerEntity;

	/**
	 * viewpager 显示要使用的实现类
	 */
	private SearchEntity SearchEntity;

	/**
	 * 网络请求次数
	 */
	private int connectNum = 1;
	private int pageSize = 160;
	private static String KEYWORD = "墙";

	private static int CAST_ID = 0;

	private static int SELECT_MODE = 0;

	private static int RECORD_ID = 0;

	private static String mSelectedTag = "全部";

	private String old_mCategoryLeafs;
	/**
	 * 搜索没有数据时候的提示信息
	 * 
	 * @Fields search_warring
	 */
	private View search_warring;
	private TextView warring_text;

	/**
	 * 数据的总页数
	 */
	private int pageCount;

	private RelativeLayout mMainControlLayout;

	/**
	 * 分类搜索的窗口
	 * 
	 * @Fields popupWindow
	 */
	private SearchCategoryPopupWindow popupWindow;
	/**
	 * 分类搜索填充的内容
	 * 
	 * @Fields mCategoryList
	 */
	private ArrayList<String> mCategoryList;

	/**
	 * 显示
	 * 
	 * @Fields SHOW_CATEGORY_POPUPWINDOW
	 */
	private static final int SHOW_CATEGORY_POPUPWINDOW = 0x0541;

	private boolean isSearch = false;

	/**
	 * 添加所有的视频类型
	 * 
	 * @Fields videoTypes
	 */
	public Set<Integer> videoTypes = new HashSet<Integer>();

	private boolean isBack = false;
	{
		videoTypes.clear();
		videoTypes.add(1);
		videoTypes.add(2);
		videoTypes.add(4);
		videoTypes.add(5);
		videoTypes.add(6);
		videoTypes.add(7);
		videoTypes.add(11);
		videoTypes.add(12);
		videoTypes.add(18);
		videoTypes.add(17);
		videoTypes.add(15);
		// start 添加一个综艺 author:huzuwei
		videoTypes.add(61);
		// end
		videoTypes.add(100);
		videoTypes.add(16);
		videoTypes.add(1001);
		videoTypes.add(1007);
		videoTypes.add(1006);
		videoTypes.add(1004);
		videoTypes.add(1003);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		// 接受外部启动发送的intent
		Intent intent = getIntent();
		if (null != intent) {
			KEYWORD = intent.getStringExtra("keyword");
			CAST_ID = intent.getIntExtra("cast_id", 0);
			SELECT_MODE = intent.getIntExtra("select_mode", 0);
			RECORD_ID = intent.getIntExtra("record_id", 0);
			old_mCategoryLeafs = mSelectedTag = getResources().getString(R.string.list_top_category);
		}
		setContentView(R.layout.searchpager_layout);
		isFristCreate = true;
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if (null != intent) {
			KEYWORD = intent.getStringExtra("keyword");
			CAST_ID = intent.getIntExtra("cast_id", 0);
			SELECT_MODE = intent.getIntExtra("select_mode", 0);
			RECORD_ID = intent.getIntExtra("record_id", 0);
			old_mCategoryLeafs = mSelectedTag = getResources().getString(R.string.list_top_category);
		}
		isFristCreate = true;
		if (HiveviewAppLib.videoTypeSet.size() != 0)
			HiveviewAppLib.videoTypeSet.clear();
		adapter=null;
		CATEGORY_NAME=null;
		searchPagerEntity=null;
		SearchEntity=null;
		mCategoryList=null;
		// loading view
		progressDialog = (com.hiveview.tv.view.ProgressDialog) findViewById(R.id.pd_list_loading);
		progressDialog.setVisibility(View.VISIBLE);

		// warring date
		search_warring = (View) findViewById(R.id.search_warring);
		warring_text = (TextView) search_warring
				.findViewById(R.id.warring_textView1);

		// 因为是重用电影收藏的 所以要重新设置提示消息
		mCollectHine = (TextView) findViewById(R.id.collect_layout_collecthint);
		mCollectHine.setText(getResources().getString(
				R.string.search_erro));
		mCollectHine.setVisibility(View.GONE);
		// 因为是重用电影收藏的 所以要重新设置标题的内容
		mTitle = (TextView) findViewById(R.id.tv_collect_lbl);
		// 搜索的关键字
		tvType = (TextView) findViewById(R.id.tv_collect_count2);
		tvType.setText(KEYWORD + "");
		
		dialogListener = new CancelDialogListener();
		mPlayerRecordEntities = new ArrayList<SearchEntity>();
		submitRequest(new GetSearchListRunnable());
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
			top_layout.setVisibility(View.VISIBLE);
			mTitle.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			tvType.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.VISIBLE);
			tvTotalPage.setVisibility(View.VISIBLE);
			tvCurrentPage.setVisibility(View.VISIBLE);
			tvPageDivision.setVisibility(View.VISIBLE);
			search_warring.setVisibility(View.INVISIBLE);
			mCollectHine.setVisibility(View.GONE);// 保证在有数据的时候这个提示信息绝对不会出现的
			if (null == adapter) {// 如果适配器不为空的话。需要清除适配器的
				adapter = new HivePagerAdapter(this, new SearchItemFactory(
						new ViewItemFocusListener(),
						new ViewItemClickListener()), mViewPager, pageViewSize,
						new NeighborTwoLineStrategy());
			} else {
				adapter.clear();
			}
			// 填充数据---------------------all-------------------->
			adapter.addDataSource(mPlayerRecordEntities);
			old_mCategoryLeafs = null == old_mCategoryLeafs ? getResources().getString(
					R.string.list_top_category)
					: old_mCategoryLeafs;
			// 设置页码总数
			tvTotal.setText(String.format(
					getResources().getString(R.string.search_total_text),
					new Object[] { old_mCategoryLeafs + ""
							+ searchPagerEntity.getRecCount() }));
			adapter.setDataTotalSize(searchPagerEntity.getRecCount());// 设置数据总记录数
			// 设置适配器
			mViewPager.setAdapter(adapter);
			mViewPager.requestFocus();
			// 添加监听
			mViewPager.setPreloadingListener(new ViewPagerProLoadingListener());

			// 设置第一页
			if (mPlayerRecordEntities.size() != 0)
				tvCurrentPage.setText(1 + "");
			else
				tvCurrentPage.setText(0 + "");
			// 设置总页数
			pageCount = (int) Math.ceil(searchPagerEntity.getRecCount()
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
				// start:只是一页的时候，显示页数右对齐 author：huzuwei
				vDownPage.setVisibility(View.GONE);
				// end
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
			}

			// loading
			progressDialog.setVisibility(View.GONE);

			break;

		// 获取数据失败 或者初始化奇艺失败
		case GETVARIETYENTITY_FAIL:
			// loading
			progressDialog.setVisibility(View.GONE);
			String typeString = null != new ChannelDAO(getBaseContext())
					.queryMap().get(RECORD_ID) ? new ChannelDAO(
					getBaseContext()).queryMap().get(RECORD_ID) : "";
			// 设置页码总数
			tvTotal.setText(String.format(
					getResources().getString(R.string.search_total_text),
					new Object[] { typeString + "" + "0" }));
			// 隐藏各种显示内容，显示失败了 就隐藏所有的 view 所以 内部的控件都需要隐藏了
			// channeldao.queryMap().get(SearchEntity.getRecord_type())RECORD_ID
			if((search_warring.getVisibility() == View.VISIBLE)&&isBack){
				this.finish();
			}else{
				isBack = false;
			}
			search_warring.setVisibility(View.VISIBLE);
			warring_text.setText(String.format(
					getResources().getString(R.string.search_warring),
					new Object[] { typeString }));

			vDownPage.setVisibility(View.INVISIBLE);
			vUpPage.setVisibility(View.INVISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.INVISIBLE);
			mViewPager.setVisibility(View.INVISIBLE);
			tvCurrentPage.setText("1");// 设置当前页数
			// 添加总数
			tvTotalPage.setText(String
					.format(getResources().getString(
							R.string.list_top_page_count), "1"));
			break;
		case GETVARIETYENTITY_SUCCESS_ADD_DATA:// 预加载数据
			// loading
			progressDialog.setVisibility(View.GONE);

			adapter.addDataSource(mPlayerRecordEntities);

			break;
		case SHOW_CATEGORY_POPUPWINDOW:
			reallyShowPopupWindow();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化 奇异播放器的数据
	 */
	public void init() {
		if (HiveviewAppLib.videoTypeSet.size() != 0)
			HiveviewAppLib.videoTypeSet.clear();
		mMainControlLayout = (RelativeLayout) findViewById(R.id.list_content_layout);
		// loading view
		progressDialog = (com.hiveview.tv.view.ProgressDialog) findViewById(R.id.pd_list_loading);
		progressDialog.setVisibility(View.VISIBLE);

		// warring date
		search_warring = (View) findViewById(R.id.search_warring);
		warring_text = (TextView) search_warring
				.findViewById(R.id.warring_textView1);

		// 因为是重用电影收藏的 所以要重新设置提示消息
		mCollectHine = (TextView) findViewById(R.id.collect_layout_collecthint);
		mCollectHine.setText("暂时数据记录异常");
		mCollectHine.setVisibility(View.GONE);
		// 因为是重用电影收藏的 所以要重新设置标题的内容
		mTitle = (TextView) findViewById(R.id.tv_collect_lbl);
//		mTitle.setText("搜索视频" + "");
		mTitle.setVisibility(View.INVISIBLE);
		top_layout = (RelativeLayout) findViewById(R.id.list_top_layout);
		top_layout.setVisibility(View.INVISIBLE);
		// 页码信息
		tvTotal = (TextView) findViewById(R.id.tv_collect_count);
		tvTotal.setVisibility(View.INVISIBLE);
		// 搜索的关键字
		tvType = (TextView) findViewById(R.id.tv_collect_count2);
		tvType.setText(KEYWORD + "");
		tvType.setVisibility(View.INVISIBLE);
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
		mPlayerRecordEntities = new ArrayList<SearchEntity>();
		submitRequest(new GetSearchListRunnable());

	}

	/**
	 * @author zhangpengzhan
	 * 
	 *         获取搜索数据的Runnable子类，运行在异步线程
	 */
	class GetSearchListRunnable extends SafeRunnable {

		@Override
		public void requestData() {
			// TODO Auto-generated method stub
			HiveTVService hiveTVService = new HiveTVService();
			Log.d(TAG, "getPlayerRecord->keyword::::" + KEYWORD
					+ "||| RECORD_ID:::" + RECORD_ID);
			// 关键字 ； id ；
			searchPagerEntity = hiveTVService.getSearchList(KEYWORD, CAST_ID,
					SELECT_MODE, RECORD_ID, connectNum, pageSize).get(0);
			/*
			 * searchPagerEntity = hiveTVService.getSearchPagerData( "墙", 0, 0,
			 * 0, 1, 48);
			 */
			mPlayerRecordEntities = searchPagerEntity.getFilms();
			// 不同的情况发送不同的消息
			if (null != searchPagerEntity && mPlayerRecordEntities.size() != 0
					&& connectNum == 1) {
				Log.d(TAG, "GETVARIETYENTITY_SUCCESS");
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS);

			}

			if (null != searchPagerEntity && mPlayerRecordEntities.size() != 0
					&& connectNum != 1) {
				Log.d(TAG, "GETVARIETYENTITY_SUCCESS_ADD_DATA");
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS_ADD_DATA);

			}

			if (null == searchPagerEntity || mPlayerRecordEntities.size() == 0) {
				Log.d(TAG, "GETVARIETYENTITY_FAIL");
				if (connectNum == 1) {
					handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
				} else {

				}
			}

			if (isSearch) {
				old_mCategoryLeafs = null != new ChannelDAO(getBaseContext())
						.queryMap().get(RECORD_ID) ? new ChannelDAO(
						getBaseContext()).queryMap().get(RECORD_ID) : "";
				isSearch = false;
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
			// 展示分类搜素界面
			if (!dismissPopupWindow())
				return false;
			showPopupWindow();

			break;

		case KeyEvent.KEYCODE_BACK:
			// 如果在警告界面的时候，返回的是搜索结果列表，否则的才是退出
			if (search_warring.isShown()) {
				RECORD_ID = 0;
				// old_mCategoryLeafs = "全部";
				old_mCategoryLeafs = mSelectedTag = getResources().getString(R.string.list_top_category);
				// loading
				isBack  = true;
				progressDialog.setVisibility(View.VISIBLE);
				submitRequest(new GetSearchListRunnable());

			} else {
				// 退出当前activity
				this.finish();
			}
			break;
		}
		return false;
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

	private static final String TAG = "SearchPageActivity";

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
				SearchEntity = (SearchEntity) view.getTag();
			}
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
			SearchEntity entity = (SearchEntity) v.getTag();
			// 播放视频391301
			// 奇异的播放方法18
			String action = ContentInvoker.getInstance().getContentAction(
					entity.getRecord_type());
			Log.d(TAG, "video_type=====" + entity.getRecord_type());
			if (null != action
					&& !action.equals("")
					&& (ContentInvoker.CONTENT_ACTION_FILMDETAIL.equals(action)
							|| ContentInvoker.CONTENT_ACTION_TELEPLAYDETAIL
									.equals(action) || ContentInvoker.CONTENT_ACTION_VARIETYDETAIL
								.equals(action))) {
				Intent intent = new Intent(action);
				intent.putExtra("id", entity.getRecord_id());
				//start author:zhangpengzhan , 调用播放器的数据来源
				intent.putExtra("source", AppConstant.SOURCE_SEARCH);
				//end
				startActivity(intent);
			} else {
				PlayerParamsUtils.getVideoPlayParams(entity.getRecord_id(),
						entity.getRecord_type(),"", SearchPageActivtiy.this,new VideoPlayerListener(){

							@Override
							public void onStartPlayerComplete(VideoNewEntity entity) {
								// TODO Auto-generated method stub
								KeyEventHandler.post(new DataHolder.Builder(getBaseContext()).setTabNo(Tab.TAB)
										.setViewPosition("0306").setSource(AppConstant.SOURCE_SEARCH)
										.setEntity(entity).setDataType(DataType.CLICK_TAB_VIDEO)
										.setSrcType(ItemType.VIDEO)
										.build());
							}
					
				});
				
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
				// start 修改Invisible为gone author:huzuwei
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
			} else {
				// vUpPage.setFocusable(false);
				// vDownPage.setFocusable(false);
			}

			/*
			 * if (pageIndex >= pageCount) { if (mViewPager.getChildCount() <=
			 * (pageViewSize / 2)) { mViewPager.requestFocus(); }
			 * vUpPage.setVisibility(View.VISIBLE);
			 * vDownPage.setVisibility(View.INVISIBLE);
			 * leftImage.setVisibility(View.VISIBLE);
			 * rightImage.setVisibility(View.INVISIBLE); //
			 * vUpPage.requestFocus();
			 * 
			 * }
			 */

		}

		@Override
		public void preLoading(int pageSize) {
			Log.d(TAG, "====pageSize=" + pageSize);
			connectNum++;
			submitRequest(new GetSearchListRunnable());

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

	private static String mResultTag = new String();
	protected static StringBuilder mResultCategory;
	protected static StringBuilder mCategoryLeafs;

	protected void reallyShowPopupWindow() {
		if (null == popupWindow) {
			popupWindow = new SearchCategoryPopupWindow(this, mCategoryList,
					mSelectedTag);
			popupWindow.setReleasePopupWindow(new ReleasePopupWindow() {
				@Override
				public void dismiss() {
					dismissPopupWindow();
				}
			});
			popupWindow.setOnUserSelectedListener(new onUserSelectedListener() {

				@Override
				public void onUserSelected(String resultData) {
					LogUtil.info("onUserSelected----->");
					progressDialog.setVisibility(View.VISIBLE);
					isSearch = true;
					mResultTag = resultData;
					RECORD_ID = Integer.parseInt((mResultTag.split(":"))[0]);
					mSelectedTag = (mResultTag.split(":"))[1];
					if (mResultCategory != null)
						mResultCategory = null;
					mResultCategory = new StringBuilder(resultData);
					if (mCategoryLeafs != null)
						mCategoryLeafs = null;
					mCategoryLeafs = new StringBuilder(resultData);
					connectNum = 1;
					submitRequest(new GetSearchListRunnable());
					Log.d(TAG, "==========" + isSearch);

					dismissPopupWindow();
				}
			});
//			mCategoryList.clear();

		}
		popupWindow.show(mMainControlLayout, Gravity.CENTER, 0, 0);
	}

	public void showPopupWindow() {
		// 获取频道列表
		submitRequest(new SafeRunnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void requestData() {
				// TODO Auto-generated method stub
				service = new HiveTVService();
				// 去除重复的item
				Set set = new HashSet();
				boolean isPut = false;
				ArrayList<FirstClassListEntity> fcs = service
						.getFirstClassList(getResources().getString(R.string.language));
				if (fcs != null && fcs.size() > 1) {
					for (FirstClassListEntity fc : fcs) {
						for (int videoType : videoTypes) {
							if (fc.getFirstclass_id() == videoType) {
								isPut = true;
							}
						}
						if (isPut) {
							set.add(fc.getFirstclass_id() + ":"
									+ fc.getFirstclass_name());
							isPut = false;
						}
					}
					if(mCategoryList!=null){
						mCategoryList.clear();
					}
					mCategoryList = new ArrayList<String>(set);
					handler.sendEmptyMessage(SHOW_CATEGORY_POPUPWINDOW);
					LogUtil.info(mCategoryList.toString());
				} else {
					Log.d(TAG, "mCategoryList==>分类菜单数据请求失败");
					popupWindow.show(mMainControlLayout, Gravity.CENTER, 0, 0);
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				// TODO Auto-generated method stub
				showErrorDialog(e.getErrorCode(), false);
			}
		});

	}

	protected boolean dismissPopupWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
			return false;
		}
		return true;
	}

	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene")
				&& intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");

				if ("menu".equals(command)) {
					HomeSwitchTabUtil.closeSiRi(SearchPageActivtiy.this,
							"打开分类筛选", intent);
					// 展示分类搜素界面
					if (dismissPopupWindow())
						showPopupWindow();
				} else if ("page".equals(command)) {
					String action = intent.getStringExtra("_action");
					if ("PREV".equals(action)) {

						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == 1
								|| Integer.parseInt(tvCurrentPage.getText()
										.toString()) == 0) {
							HomeSwitchTabUtil
									.closeSiRi(SearchPageActivtiy.this,
											"您已经在第一页了", intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(
									SearchPageActivtiy.this, "上一页", intent);
							isChangePageByDownButton = true;
							mViewPager.setCurrentItem(mViewPager
									.getCurrentItem() - 1);
						}
					}

					else if ("NEXT".equals(action)) {
						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == pageCount) {
							HomeSwitchTabUtil.closeSiRi(
									SearchPageActivtiy.this, "您已经在最后一页了",
									intent);
						} else {
							HomeSwitchTabUtil.closeSiRi(
									SearchPageActivtiy.this, "下一页", intent);
							// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
							isChangePageByUpButton = true;
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
				} else {
					for (int i = 0; i < mCategoryList.size(); i++) {
						if (command.equals(mCategoryList.get(i))) {
							
							String resultData = mCategoryList.get(i);
							progressDialog.setVisibility(View.VISIBLE);
							isSearch = true;
							mResultTag = resultData;
							RECORD_ID = Integer
									.parseInt((mResultTag.split(":"))[0]);
							mSelectedTag = (mResultTag.split(":"))[1];
							HomeSwitchTabUtil.closeSiRi(SearchPageActivtiy.this, mSelectedTag,
									intent);
							if (mResultCategory != null)
								mResultCategory = null;
							mResultCategory = new StringBuilder(resultData);
							if (mCategoryLeafs != null)
								mCategoryLeafs = null;
							mCategoryLeafs = new StringBuilder(resultData);
							connectNum = 1;
							submitRequest(new GetSearchListRunnable());
							Log.d(TAG, "==========" + isSearch);

							dismissPopupWindow();
						}
					}
				}
			}

		}
	}

	public HashMap<String, String[]> setCommands(String key, String vaule) {
		// 固定词
		commands.put(key, new String[] { new StringBuffer().append(vaule)
				.toString() });
		return commands;
	}
	HashMap<String, String[]> commands = new HashMap<String, String[]>();
	// 场景id
	private String scenceId = "com.hiveview.tv.activity.SearchPageActivtiy";

	public String onQuery() {
		// TODO 自动生成的方法存根
		commands.put("menu", new String[] { "分类查找影片", "分类筛选", "分类查找", "筛选影片",
				"影片分类", "影片筛选" });
		commands.put("page", new String[] { "$P(_PAGE)" });
		if(mCategoryList!=null&&mCategoryList.size()!=0){
		for (int i = 0; i < mCategoryList.size(); i++) {
			setCommands(mCategoryList.get(i), mCategoryList.get(i));
		}
		}
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
