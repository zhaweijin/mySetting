package com.hiveview.tv.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.factory.SearchFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FilmNewEntity;
import com.hiveview.tv.service.entity.TotalListFilmNewEntity;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.HomeSwitchTabUtil;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.view.ProgressDialog;
import com.hiveview.tv.view.SearchListItemPageView.ItemViewFocusChange;
import com.paster.util.JsonUtil;
/**
 * 类名 BaseVoiceSearchListActivity
 * 
 * @author haozening 语音搜索列表
 * 
 */

public abstract class BaseVoiceSearchNewListActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {
	/**
	 * 分类ID 如:电影的id为1
	 */
	protected int CATEGORY_ID = 1;
	/**
	 * 分类名称 如:id为1的分类名称为电影
	 */
	protected String CATEGORY_NAME = "电影";
	/**
	 * 是否是初次展示首页
	 */
	private boolean isFristCreate = false;
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
	// 设置总页数
	int pageCount;
	/**
	 * viewpager 显示要使用的实现类
	 */
	private FilmEntity filmEntity;
	/**
	 * 上下页按钮 焦点状态
	 */
	private boolean isChangePageByUpButton = false;
	private boolean isChangePageByDownButton = false;
	private Intent voiceIntent;
	protected static final int FILTER_BLUELIGHT = 1;
	protected static final int FILTER_QIYI = -1;
	protected static final int FILTER_TOTAL = 0;

	/*
	 * 初始化 数据
	 */
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		voiceIntent = getIntent();
		// 获取数据
		if (null != voiceIntent) {
			CATEGORY_NAME = voiceIntent.getStringExtra("category_name");
		}
		setContentView(R.layout.movie_demand_list_layout);
		isFristCreate = true;
		initView();
		// 加载数据
		submitRequest(new GetPlayerRecord(pageSize));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		voiceIntent = intent;
		startLoading();
		isFristCreate = true;
		connectNum = 1;
		// 加载数据
		submitRequest(new GetPlayerRecord(pageSize));
	}

	/**
	 * 初始化数据
	 */
	private void initView() {
		// 等待框
		vLoading = (ProgressDialog) this.findViewById(R.id.pd_list_loading);
		// 空数据时显示的数据
		selectedHine = (TextView) this.findViewById(R.id.movie_layout_moviehint);
		// 分类名称
		tvCatgory = (TextView) this.findViewById(R.id.list_top_layout_category);
		top_layout = (RelativeLayout) this.findViewById(R.id.list_top_layout);

		// 总条数
		tvTotal = (TextView) this.findViewById(R.id.list_top_layout_total_count);
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
		vUpPage.setOnClickListener(this);
		vDownPage.setOnClickListener(this);
		vUpPage.setOnFocusChangeListener(this);
		vDownPage.setOnFocusChangeListener(this);

		mViewPager.setNextPreviousPageView(vUpPage, vDownPage);

		vLoading.setVisibility(View.VISIBLE);
		selectedHine.setVisibility(View.INVISIBLE);
		tvCatgory.setVisibility(View.INVISIBLE);
		top_layout.setVisibility(View.INVISIBLE);
		tvTotal.setVisibility(View.INVISIBLE);
		leftImage.setVisibility(View.INVISIBLE);
		rightImage.setVisibility(View.INVISIBLE);
		tvCatgory.setText(CATEGORY_NAME);
	}

	private void startLoading() {
		if(null != adapter){
			adapter.clear();
			adapter.notifyDataSetChanged();
		}
		vLoading.setVisibility(View.VISIBLE);
		selectedHine.setVisibility(View.INVISIBLE);
		tvCatgory.setVisibility(View.INVISIBLE);
		top_layout.setVisibility(View.INVISIBLE);
		tvTotal.setVisibility(View.INVISIBLE);
		leftImage.setVisibility(View.INVISIBLE);
		rightImage.setVisibility(View.INVISIBLE);
	}

	/**
	 * 子类实现此方法获取数据，此方法在子线程，直接调用下载方法即可
	 * 
	 * @param intent
	 * @param pageSize
	 * @param connectNum
	 * @param filter
	 *            蓝光极清为 FILTER_BLUELIGHT，爱奇艺为 FILTER_QIYI，全部为 FILTER_TOTAL
	 */
	protected abstract void search(Intent intent, int pageSize, int connectNum, int filter);

	/**
	 * 子类实现此方法，处理Item点击事件
	 * 
	 * @param entity
	 */
	protected abstract void onItemClick(FilmNewEntity entity);

	/**
	 * 子类调用此方法设置数据
	 * 
	 * @param filmEntities
	 */
	protected void setList(ArrayList<TotalListFilmNewEntity> filmEntities) {
		this.totalMovieList = filmEntities;
	}

	/**
	 * 子类调用此方法取得数据下载接口
	 * 
	 * @return
	 */
	protected HiveTVService getService() {
		return service;
	}

	/**
	 * 获取 网络的数据
	 */
	class GetPlayerRecord implements Runnable {
		// 页数
		private int pageSize;

		public GetPlayerRecord(int pageSize) {
			this.pageSize = pageSize;

		}

		public void run() {
			search(voiceIntent, pageSize, connectNum, FILTER_TOTAL);

			mPlayerRecordEntities = totalMovieList.get(0).getFilms();
			if (null != totalMovieList && mPlayerRecordEntities.size() != 0 && connectNum == 1) {
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS);
			}
			if (null != totalMovieList && mPlayerRecordEntities.size() != 0 && connectNum != 1) {
				handler.sendEmptyMessage(GETVARIETYENTITY_SUCCESS_ADD_DATA);
			}
			if (null == totalMovieList || mPlayerRecordEntities.size() == 0) {
				handler.sendEmptyMessage(GETVARIETYENTITY_FAIL);
			}
		}
	}

	/*
	 * com.hiveview.tv.activity.BaseActivity#processData(android.os.Message)
	 */
	protected void processData(int msgWhat) {

		switch (msgWhat) {
		// 获取数据成功
		case GETVARIETYENTITY_SUCCESS:
			// 显示布局按钮和显示页
			top_layout.setVisibility(View.VISIBLE);
			tvCatgory.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			selectedHine.setVisibility(View.GONE);// 保证在有数据的时候这个提示信息绝对不会出现的
			if (null == adapter) {
				adapter = new HivePagerAdapter(this, new SearchFactory(new ViewItemKeyListener(), new ViewItemClickListener(),
						new ViewItemFocusListener()), mViewPager, pageViewSize, new NeighborTwoLineStrategy());

			}
			// 填充数据
			adapter.addDataSource(mPlayerRecordEntities);
			// 设置页码总数
			tvTotal.setText(String.format(getResources().getString(R.string.variety_total_text), (totalMovieList.get(0).getRecCount())));
			adapter.setDataTotalSize(totalMovieList.get(0).getRecCount());// 设置数据总记录数
			// 设置适配器
			mViewPager.setAdapter(adapter);
			// 添加监听
			mViewPager.setPreloadingListener(new ViewPagerProLoadingListener());

			// 设置第一页
			if (mPlayerRecordEntities.size() != 0)
				tvCurrentPage.setText(1 + "");
			else
				tvCurrentPage.setText(0 + "");
			// 设置总页数
			pageCount = (int) Math.ceil(totalMovieList.get(0).getRecCount() / (double) pageViewSize);
			// 添加总数
			tvTotalPage.setText(String.format(getResources().getString(R.string.list_top_page_count), pageCount));

			// 判断是否只有一页的情况
			if (mPlayerRecordEntities.size() > pageViewSize) {
				// 不是一页
				vDownPage.setVisibility(View.VISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.VISIBLE);
			} else {// 只是一页
				vDownPage.setVisibility(View.INVISIBLE);
				vUpPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
			}

			// loading
			vLoading.setVisibility(View.GONE);
			mViewPager.requestFocus();
			mViewPager.requestFocusFromTouch();

			selectedHine.setVisibility(View.GONE);
			top_layout.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			tvCatgory.setVisibility(View.VISIBLE);
			vDownPage.setVisibility(View.VISIBLE);
			rightImage.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.VISIBLE);
			tvCurrentPage.setVisibility(View.VISIBLE);
			tvTotalPage.setVisibility(View.VISIBLE);
			tvListDivision.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.VISIBLE);
			break;

		// 获取数据失败 或者初始化奇艺失败
		case GETVARIETYENTITY_FAIL:
			// loading
			vLoading.setVisibility(View.GONE);
			// 隐藏各种显示内容，显示失败了 就隐藏所有的 view 所以 内部的控件都需要隐藏了
			top_layout.setVisibility(View.INVISIBLE);
			selectedHine.setVisibility(View.VISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			tvCatgory.setVisibility(View.INVISIBLE);
			vDownPage.setVisibility(View.INVISIBLE);
			vUpPage.setVisibility(View.INVISIBLE);
			leftImage.setVisibility(View.INVISIBLE);
			rightImage.setVisibility(View.INVISIBLE);
			tvTotal.setVisibility(View.INVISIBLE);
			tvCurrentPage.setVisibility(View.INVISIBLE);
			tvTotalPage.setVisibility(View.INVISIBLE);
			tvListDivision.setVisibility(View.INVISIBLE);
			mViewPager.setVisibility(View.INVISIBLE);

			break;
		case GETVARIETYENTITY_SUCCESS_ADD_DATA:// 预加载数据
			adapter.addDataSource(mPlayerRecordEntities);
			break;
		}
	}

	private String TAG = "BaseVoiceSearchListActivity";

	/**
	 * 
	 * viewpager item焦点监听
	 */
	class ViewItemFocusListener implements ItemViewFocusChange {

		@Override
		public void itemFocusChange(View v, boolean arg1) {
			if (arg1) {// 当前的view ，获取焦点的view
				AnimationUtil.getBigAnimation(getBaseContext(), v);
			} else {
				AnimationUtil.getLitterAnimation(getBaseContext(), v);

			}
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
	 * 响应点击事件 点击后进入 详情页
	 */
	class ViewItemClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			FilmNewEntity entity = (FilmNewEntity) v.getTag();
			onItemClick(entity);
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

			if (pageIndex >= pageCount) {
				vUpPage.setVisibility(View.VISIBLE);
				vDownPage.setVisibility(View.INVISIBLE);
				leftImage.setVisibility(View.VISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
				vUpPage.requestFocus();

			}

		}

		@Override
		public void preLoading(int pageSize) {
			connectNum++;
			submitRequest(new GetPlayerRecord(pageSize));

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
			// if (isFristCreate) {
			// vDownPage.requestFocus();
			// }

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
		isFristCreate = hasFocus;
		if (v.getId() == R.id.list_page_up_ll) {
			v.setNextFocusRightId(R.id.list_page_down_ll);
		}
		if (v.getId() == R.id.list_page_down_ll) {
			v.setNextFocusLeftId(R.id.list_page_up_ll);
		}
	}
	
	public void onExecute(Intent intent) {
		// TODO Auto-generated method stub
		super.onExecute(intent);
		if (intent.hasExtra("_scene")
				&& intent.getStringExtra("_scene").equals(scenceId)) {
			if (intent.hasExtra("_command")) {
				String command = intent.getStringExtra("_command");
				if ("page".equals(command)) {
					String action = intent.getStringExtra("_action");
					if ("PREV".equals(action)) {

						if (Integer
								.parseInt(tvCurrentPage.getText().toString()) == 1
								|| Integer.parseInt(tvCurrentPage.getText()
										.toString()) == 0) {
							HomeSwitchTabUtil.closeSiRi(
									BaseVoiceSearchNewListActivity.this, "您已经在第一页了",
									intent);
						} else {
							HomeSwitchTabUtil
									.closeSiRi(BaseVoiceSearchNewListActivity.this,
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
									BaseVoiceSearchNewListActivity.this, "您已经在最后一页了",
									intent);
						} else {
							HomeSwitchTabUtil
									.closeSiRi(BaseVoiceSearchNewListActivity.this,
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
	private String scenceId = "com.hiveview.tv.activity.BaseVoiceSearchListActivity";

	public String onQuery() {
		// TODO 自动生成的方法存根
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
