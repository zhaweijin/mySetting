package com.hiveview.tv.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiveview.box.framework.adapter.HivePagerAdapter;
import com.hiveview.box.framework.strategy.NeighborTwoLineStrategy;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.HivePreloadViewPager;
import com.hiveview.box.framework.view.HivePreloadViewPager.OnPreloadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.common.factory.SubjectTopicFactory;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.entity.SubjectListEntitys;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.view.ProgressDialog;

/**
 * 类名 SubjectTopicListActivity 专题 2014-4-21
 * 
 * @author gusongsheng
 */

public class SubjectTopicListActivity extends BaseActivity implements OnClickListener, OnFocusChangeListener {
	/**
	 * 载入本地数据为空
	 */
	protected static final int LOAD_COLLECT_DATA_EMPTY = -7000;
	/**
	 * 访问成功
	 */
	private final int REQUEST_SUBJECTS_SUCCESS = 7000;
	/**
	 * 访问成功
	 */
	private final int REQUEST_SUBJECTS_NOT_FIRST_SUCCESS = 6000;
	/**
	 * 共有多少部
	 */
	private TextView tvRecordCount;
	/**
	 * 当前页数:当前页/总也数
	 */
	private TextView tvPageCurrent;
	/**
	 * ViewPager影片列表
	 */
	private HivePreloadViewPager viewPager;
	/**
	 * 等待条
	 */
	private ProgressDialog loadingDialog;
	/**
	 * 上一页
	 */
	private View btnPageUp = null;
	/**
	 * 下一页
	 */
	private View btnPageDown = null;
	/**
	 * ViewPager的适配器
	 */
	private HivePagerAdapter adapter = null;
	/**
	 * 请求数据列表
	 */
	private ArrayList<SubjectListEntitys> subjectList = null;
	/**
	 * 一次加载的条数
	 */
	private int PAGE_SIZE = 120;
	/**
	 * 页数
	 */
	private int PAGE_NUM = 1;
	/**
	 * 一共多少条数据
	 */
	private int pageCount = 0;
	/**
	 * 每页显示多少数据
	 */
	private final int pageViewSize = 6;
	/**
	 * 读取第一个数据
	 */
	private final int FIRST_LINE = 0;
	/**
	 * 没数据时的提示
	 */
	private TextView tvSubjectTopicHint = null;
	/**
	 * 显示总页数
	 */
	private TextView tvTotalPage = null;
	/**
	 * 顶部布局
	 */
	private View vlistTop;
	/**
	 * 下一页图片
	 */
	private ImageView ivListLeft;
	/**
	 * 上一页图片
	 */
	private ImageView ivListRight;
	/**
	 * 当前页
	 */
	private int pageIndex = 1;
	/**
	 * 是否是初次展示首页
	 */
	private boolean isFristCreate = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.subject_topic_list_layout);
		// 第一次
		isFristCreate = true;
		init();
	}

	/**
	 * 初始化界面
	 * 
	 * @Title: SubjectTopicListActivity
	 * @author:周一川
	 * @Description: TODO
	 */
	private void init() {
		// 空数据时显示的数据
		tvSubjectTopicHint = (TextView) findViewById(R.id.movie_layout_subject_topichint);
		// 多少条记录
		tvRecordCount = (TextView) findViewById(R.id.list_top_layout_total_count);
		// 当前页 总页数
		tvPageCurrent = (TextView) findViewById(R.id.list_page_current);
		tvTotalPage = (TextView) findViewById(R.id.list_page_count);
		// Viewpager
		viewPager = (HivePreloadViewPager) findViewById(R.id.vp_list);
		// 等待框
		loadingDialog = (ProgressDialog) findViewById(R.id.iv_bulelight_loading);
		loadingDialog.setVisibility(View.VISIBLE);
		// 上一页下一页
		btnPageUp = findViewById(R.id.list_page_up_ll);
		btnPageDown = findViewById(R.id.list_page_down_ll);
		// 列表上方的 标题 页数等...
		vlistTop = findViewById(R.id.list_top_layout);
		// 左右翻页的图片
		ivListLeft = (ImageView) findViewById(R.id.list_left);
		ivListRight = (ImageView) findViewById(R.id.list_right);
		btnPageUp.setOnClickListener(this);
		btnPageDown.setOnClickListener(this);
		btnPageUp.setOnFocusChangeListener(this);
		btnPageDown.setOnFocusChangeListener(this);

		// 加载页面时隐藏列表页上边的标题 共多少部 多少页等
		vlistTop.setVisibility(View.INVISIBLE);
		// 隐藏左右方向的图标
		ivListLeft.setVisibility(View.INVISIBLE);
		ivListRight.setVisibility(View.INVISIBLE);
		// 空数据时显示
		tvSubjectTopicHint.setVisibility(View.INVISIBLE);

		viewPager.setNextPreviousPageView(btnPageUp, btnPageDown);

		loadData();
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		submitRequest(new Runnable() {
			@Override
			public void run() {
				// 通过网络接口访问数据
				HiveTVService hiveTVService = new HiveTVService();
				//假参数 这个类暂时有用到
				subjectList = hiveTVService.getSubjectList("M1001","","",PAGE_SIZE, PAGE_NUM,getResources().getString(R.string.language));
				// 第一次加载数据成功时 加载页面 以后每次加载数据成功后直接添加到adpter
				if (null != subjectList && subjectList.size() != 0 && PAGE_NUM == 1) {
					handler.sendEmptyMessage(REQUEST_SUBJECTS_SUCCESS);
				} else if (null != subjectList && subjectList.size() != 0 && PAGE_NUM != 1) {
					handler.sendEmptyMessage(REQUEST_SUBJECTS_NOT_FIRST_SUCCESS);
				} else {
					handler.sendEmptyMessage(LOAD_COLLECT_DATA_EMPTY);
				}
			}
		});
	}

	/**
	 * 显示数据
	 * 
	 * @param msgWhat
	 *            事件响应值
	 */
	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case REQUEST_SUBJECTS_SUCCESS:// 加载完成
			// 当数据加载完毕时 显示列表页上方的标题...
			vlistTop.setVisibility(View.VISIBLE);
			// 显示左右翻页的图片
			ivListRight.setVisibility(View.VISIBLE);

			loadingDialog.setVisibility(View.GONE);
			// 如果适配器不为空 添加数据
			if (null == adapter) {
				adapter = new HivePagerAdapter(this, new SubjectTopicFactory(new TopicCallBackItemViewKeyListener(), new TopicClickListener()),
						viewPager, pageViewSize, new NeighborTwoLineStrategy());
			}
			adapter.addDataSource(subjectList.get(FIRST_LINE).getPageContent());
			// 显示总共有多少页
			tvRecordCount.setText(String.format(getResources().getString(R.string.tv_filmlist_record_count), subjectList.get(FIRST_LINE)
					.getRecCount()));
			// 设置数据总记录数
			adapter.setDataTotalSize(subjectList.get(FIRST_LINE).getRecCount());
			viewPager.setAdapter(adapter);
			viewPager.setPreloadingListener(new MyOnPreloadingListener());

			// 设置第一页
			tvPageCurrent.setText(1 + "");
			// 设置总页数
			pageCount = (int) Math.ceil(subjectList.get(FIRST_LINE).getRecCount() / (double) pageViewSize);
			tvTotalPage.setText(String.format(getResources().getString(R.string.list_top_page_count), pageCount));

			// 判断是否只有一页的情况
		if (subjectList.get(FIRST_LINE).getRecCount() > pageViewSize) {
				pageIndex = Integer.parseInt(tvPageCurrent.getText().toString());
				if (pageIndex == 1) {
					// 第一个时显示右翻图片 与下一页
					ivListLeft.setVisibility(View.INVISIBLE);
					ivListRight.setVisibility(View.VISIBLE);
					btnPageDown.setVisibility(View.VISIBLE);
					btnPageUp.setVisibility(View.INVISIBLE);
				} else if (pageIndex == pageCount) {
					// 第一个时显示左翻图片 与上一页
					ivListLeft.setVisibility(View.VISIBLE);
					ivListRight.setVisibility(View.INVISIBLE);
					btnPageDown.setVisibility(View.INVISIBLE);
					btnPageUp.setVisibility(View.VISIBLE);
				} else {
					// 全部显示
					ivListLeft.setVisibility(View.VISIBLE);
					ivListRight.setVisibility(View.VISIBLE);
					btnPageDown.setVisibility(View.VISIBLE);
					btnPageUp.setVisibility(View.VISIBLE);
				}
			} else {
				btnPageDown.setVisibility(View.INVISIBLE);
				btnPageUp.setVisibility(View.INVISIBLE);
				// 隐藏向左翻页的图片 显示向右翻页的图片
				// start:修改只有一页的时候，页数右对齐 author:huzuwei
				ivListLeft.setVisibility(View.GONE);
				// end
				ivListRight.setVisibility(View.INVISIBLE);
			}
			break;
		case LOAD_COLLECT_DATA_EMPTY:// 空数据的时候
			loadingDialog.setVisibility(View.GONE);
			tvSubjectTopicHint.setVisibility(View.VISIBLE);// 提示用户没有对应数据
			break;
		case REQUEST_SUBJECTS_NOT_FIRST_SUCCESS:
			loadingDialog.setVisibility(View.GONE);
			adapter.addDataSource(subjectList.get(FIRST_LINE).getPageContent());
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/**
	 * 实现CallBackItemViewKeyListener接口的内部类
	 * 
	 * @author guosongehng
	 */
	class TopicCallBackItemViewKeyListener implements CallBackItemViewKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			return false;
		}
	}

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class TopicClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			SubjectListEntity entity = (SubjectListEntity) v.getTag();
			String sizeInfo = entity.getImgSize();
			// 进入详情页图面的url，不等于空， 长度大于0，包含有x字符
			if (null != sizeInfo && sizeInfo.length() > 0 && sizeInfo.contains("x")) {
				LogUtil.info(sizeInfo);
				String[] sizeArray = sizeInfo.split("x");
				int width = Integer.parseInt(sizeArray[0]);
				int height = Integer.parseInt(sizeArray[1]);
				Intent intent = new Intent();
				intent.putExtra("entity", entity);
				if (width > height) {
					intent.setClass(getApplication(), SubjectDetailLandspaceActivity.class);
				} else {
					intent.setClass(getApplication(), SubjectDetailPortraitActivity.class);
				}

				startActivity(intent);
			}
		}
	}

	/**
	 * 焦点发生变化
	 * 
	 * @param v
	 *            放生焦点变化的View
	 * @param hasFocus
	 *            是否有焦点
	 */
	public void onFocusChange(View v, boolean hasFocus) {
		// 焦点移走，下次在进入第一页的时候就不是第一次进入的考虑是否要把焦点设置在下一页的按钮上
		isFristCreate = hasFocus;
		// 是否是上一页
		if (v.getId() == R.id.list_page_up_ll) {
			v.setNextFocusRightId(R.id.list_page_down_ll);
		}
		// 是否是下一页
		if (v.getId() == R.id.list_page_down_ll) {
			v.setNextFocusLeftId(R.id.list_page_up_ll);
		}
	}

	/**
	 * HivePagerAdapter 提供简便操作 可以动态的做一些动作
	 * 
	 * @ClassName: MyOnPreloadingListener
	 * @Description: TODO
	 * @author: 周一川
	 * @date 2014-6-3 下午2:17:26
	 * 
	 */
	class MyOnPreloadingListener implements OnPreloadingListener {
		@Override
		public void setPageCurrent(int pageIndex) {// 翻页过程
			tvPageCurrent.setText(pageIndex + "");// 设置当前页数

			if (pageIndex != 1) {
				// 第一个时显示右翻图片 与下一页
				btnPageUp.setVisibility(View.VISIBLE);
				btnPageDown.setVisibility(View.VISIBLE);
				ivListLeft.setVisibility(View.VISIBLE);
				ivListRight.setVisibility(View.VISIBLE);
			}
			if (pageIndex != adapter.getViews().size()) {
				// 第一个时显示左翻图片 与上一页
				btnPageUp.setVisibility(View.VISIBLE);
				btnPageDown.setVisibility(View.VISIBLE);
				ivListLeft.setVisibility(View.VISIBLE);
				ivListRight.setVisibility(View.VISIBLE);
			}

			if (pageIndex >= pageCount) {
				// 全部显示
				ivListLeft.setVisibility(View.VISIBLE);
				ivListRight.setVisibility(View.INVISIBLE);
				btnPageUp.setVisibility(View.VISIBLE);
				// start 将Invisible修改为gone author:huzuwei
				btnPageDown.setVisibility(View.GONE);
				// end

				// start 删除 author：huzuwei
				/*
				 * if (pageIndex >= pageCount) {
				 * btnPageUp.setVisibility(View.VISIBLE);
				 * btnPageDown.setVisibility(View.INVISIBLE); }
				 */
				// end

			}
		}

		/**
		 * 预加载
		 * 
		 * @param int pageSize
		 */
		@Override
		public void preLoading(int pageSize) {
			PAGE_NUM++;// 每次访问网络页码加一
			loadData();
		}

		@Override
		public void preLoadNotFinish() {
		}

		@Override
		public void onLastPage() {// 最后一页

		}

		@Override
		public void onFirstPage() {// 第一页
			// 隐藏向左翻页的图片 显示向右翻页的图片
			ivListLeft.setVisibility(View.INVISIBLE);
			ivListRight.setVisibility(View.VISIBLE);
			// 显示下一页按钮 隐藏上一页按钮
			btnPageUp.setVisibility(View.INVISIBLE);
			btnPageDown.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.list_page_down_ll:// 下一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			btnPageDown.requestFocus();
			break;
		case R.id.list_page_up_ll:// 上一页按钮
			// 先改变按键焦点的状态，然后在改变viewpager的内容，保证不会出现在按钮上下页的时候焦点不会被转移
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
			break;
		default:
			break;
		}
	}

}
