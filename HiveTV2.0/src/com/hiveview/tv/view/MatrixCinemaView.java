package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hiveview.box.framework.image.FailReason;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageLoadingListener;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.ChannelInvoker;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.view.pager3d.TabBasePageView;

/**
 * @ClassName: MatrixCinemaView
 * @Description: 新版影院的view
 * @author: zhangpengzhan
 * @date 2014年9月4日 上午10:59:39
 * 
 */
public class MatrixCinemaView extends TabBasePageView {
	protected static final String TAG = "MatrixCinemaView";

	private static final int LOAD_DATA_SUCCESS = 0x00123;
	private static final int LOAD_DATA_FAIL = 0x00124;

	/**
	 * 影院页面的推荐位Layout
	 */
	private View matrix_cinema_layout_1, matrix_cinema_layout_2, matrix_cinema_layout_3, matrix_cinema_layout_4, matrix_cinema_layout_5,
			matrix_cinema_layout_6, matrix_cinema_layout_7, matrix_cinema_layout_8, matrix_cinema_layout_9, matrix_cinema_layout_10;
	/**
	 * 推荐位轮播的ViewFlipper
	 */
	private ViewFlipper cinema_flipper_1, cinema_flipper_2, cinema_flipper_3, cinema_flipper_4, cinema_flipper_5, cinema_flipper_6, cinema_flipper_7,
			cinema_flipper_8, cinema_flipper_9, cinema_flipper_10;
	/**
	 * 显示推荐位文本信息的TextView数组
	 */
	private TextView[] desViews;
	/**
	 * @Fields cinemaList:应该显示数据的
	 */
	private ArrayList<FirstClassListEntity> cinemaList = null;

	/**
	 * @Fields recommendDAO:存储相关数据的数据库
	 */
	private ChannelDAO recommendDAO = null;

	private LinkedList<ViewFlipper> viewFilpers;

	/**
	 * @Fields viewIndex:viewFilper中view 的index
	 */
	private int viewIndex = 0;
	private int[][] viewfliperData;
	private int pages = 0;
	private int thePageItems = 9;
	private ViewFlipper[] viewFliper;
	/**
	 * 按钮的位置
	 */
	private int btnIndex = 5;

	/**
	 * @Fields gameTabView:底部菜单栏
	 */
	private View cinemaTabView;
	private View cinemaRecordLayout;
	private View cinemaFavouriteLayout;
	private View cinemaInstalledLayout;
	private View cinemaSearchLayout;
	private View cinemaSettingLayout;
	// private View cinemaUserLayout;
	private View cinemaExternalLayout;

	private ArrayList<View> cinemaViews;
	
	/**
	 * @Fields cinemaName 保存影院页面的频道名称
	 */
	private ArrayList<String> cinemaName;

	/**
	 * @Fields cinemaNo:埋点统计的编号
	 */
	private int[][] cinemaNo;

	private Context context;

	/**
	 * @Fields pageChange:需要换页的接口
	 */
	private PageChange pageChange;

	private int viewVisibleTime = 250;

	private Handler matrixCinemaViewHandler = new Handler();

	private HomeActivity homeActivity;

	public MatrixCinemaView(Context context) {
		super(context);
		init();
		this.context = context;
	}

	public MatrixCinemaView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		this.context = context;
	}

	public MatrixCinemaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		this.context = context;
		
	}

	public MatrixCinemaView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
		this.context = context;
	}

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

	public void setPageChange(PageChange pageChange) {
		this.pageChange = pageChange;
	}

	/**
	 * 推荐位相关View初始化
	 */
	protected void init() {
		Log.d(TAG, "::==>" + Environment.getExternalStorageDirectory());
	//	homeActivity = (HomeActivity) context;
	//	homeActivity.setCinemaInterface(new ItemButtonOnClick());
		recommendDAO = new ChannelDAO(getContext());
		// 初始化viewfiler 列表
		viewFilpers = new LinkedList<ViewFlipper>();
		cinemaViews = new ArrayList<View>();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View view = inflate(getContext(), R.layout.matrix_cinema_view, null);
		this.addView(view, params);
		//
		matrix_cinema_layout_1 = (View) findViewById(R.id.matrix_cinema_layout_1);
		matrix_cinema_layout_2 = (View) findViewById(R.id.matrix_cinema_layout_2);
		matrix_cinema_layout_3 = (View) findViewById(R.id.matrix_cinema_layout_3);
		matrix_cinema_layout_4 = (View) findViewById(R.id.matrix_cinema_layout_4);
		matrix_cinema_layout_5 = (View) findViewById(R.id.matrix_cinema_layout_5);
		matrix_cinema_layout_6 = (View) findViewById(R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_7 = (View) findViewById(R.id.matrix_cinema_layout_7);
		matrix_cinema_layout_8 = (View) findViewById(R.id.matrix_cinema_layout_8);
		matrix_cinema_layout_9 = (View) findViewById(R.id.matrix_cinema_layout_9);
		matrix_cinema_layout_10 = (View) findViewById(R.id.matrix_cinema_layout_10);
		cinemaViews.add(matrix_cinema_layout_1);
		cinemaViews.add(matrix_cinema_layout_2);
		cinemaViews.add(matrix_cinema_layout_3);
		cinemaViews.add(matrix_cinema_layout_4);
		cinemaViews.add(matrix_cinema_layout_5);
		cinemaViews.add(matrix_cinema_layout_6);
		cinemaViews.add(matrix_cinema_layout_7);
		cinemaViews.add(matrix_cinema_layout_8);
		cinemaViews.add(matrix_cinema_layout_9);
		cinemaViews.add(matrix_cinema_layout_10);
		//
		cinema_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_1);
		cinema_flipper_2 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_2);
		cinema_flipper_3 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_3);
		cinema_flipper_4 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_4);
		cinema_flipper_5 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_5);
		cinema_flipper_6 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_6);
		cinema_flipper_7 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_7);
		cinema_flipper_8 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_8);
		cinema_flipper_9 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_9);
		cinema_flipper_10 = (ViewFlipper) findViewById(R.id.matrix_cinema_flipper_10);

		// 初始化推荐位Layout数组
		recommendEdgeViews = new View[] { matrix_cinema_layout_1, matrix_cinema_layout_2, matrix_cinema_layout_3, matrix_cinema_layout_4,
				matrix_cinema_layout_5, matrix_cinema_layout_6, matrix_cinema_layout_7, matrix_cinema_layout_8, matrix_cinema_layout_9,
				matrix_cinema_layout_10 };

		// 初始化 推荐位轮播的ViewFlipper数组
		viewFliper = new ViewFlipper[] { cinema_flipper_1, cinema_flipper_2, cinema_flipper_3, cinema_flipper_4, cinema_flipper_5, cinema_flipper_6,
				cinema_flipper_7, cinema_flipper_8, cinema_flipper_9, cinema_flipper_10 };
		// ViewFlipper 轮播动画
		for (int i = 0; i < viewFliper.length; i++) {
			Animation inAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.cinemascale_in);
			Animation outAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.cinemascale_out);
			viewFliper[i].stopFlipping();
			viewFliper[i].setAutoStart(false);
			viewFliper[i].setInAnimation(inAnimation);
			viewFliper[i].setOutAnimation(outAnimation);
		}
		// 推荐位绑定焦点改变事件和焦点改变事件
		for (int i = 0; i < recommendEdgeViews.length; i++) {
			recommendEdgeViews[i].setOnFocusChangeListener(recommendLayoutFocusListener);
			recommendEdgeViews[i].setOnClickListener(new RecommendLayoutClickListener());
		}
		focusRules();
		super.init();
	}

	ViewFlipper viewFlipper = null;

	/**
	 * right1--> matrix_app_layout_0 right2--> matrix_app_layout_5 left1-->
	 * matrix_recommend_flipper_2_out_layout left2--> matrix_recommend_layout_4
	 * 
	 * @Title: MatrixCinemaView
	 * @author:张鹏展
	 * @Description: 焦点走向规则
	 */
	private void focusRules() {

		matrix_cinema_layout_1.setNextFocusRightId((matrix_cinema_layout_2.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2.getId()
				: R.id.matrix_app_layout_0);
		matrix_cinema_layout_1.setNextFocusDownId(R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_1.setNextFocusLeftId(R.id.matrix_recommend_layout_3);
		matrix_cinema_layout_1.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_1.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_2.setNextFocusRightId((matrix_cinema_layout_3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3.getId()
				: R.id.matrix_app_layout_0);
		matrix_cinema_layout_2.setNextFocusDownId((matrix_cinema_layout_7.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7.getId()
				: R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_2.setNextFocusLeftId(R.id.matrix_cinema_layout_1);
		matrix_cinema_layout_2.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_2.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_3.setNextFocusRightId((matrix_cinema_layout_4.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4.getId()
				: R.id.matrix_app_layout_0);
		matrix_cinema_layout_3.setNextFocusDownId((matrix_cinema_layout_8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8.getId()
				: R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_3.setNextFocusLeftId(R.id.matrix_cinema_layout_2);
		matrix_cinema_layout_3.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_3.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_4.setNextFocusRightId((matrix_cinema_layout_5.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5.getId()
				: R.id.matrix_app_layout_0);
		matrix_cinema_layout_4.setNextFocusDownId((matrix_cinema_layout_9.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9.getId()
				: R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_4.setNextFocusLeftId(R.id.matrix_cinema_layout_3);
		matrix_cinema_layout_4.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_4.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_5.setNextFocusRightId(R.id.matrix_app_layout_0);
		matrix_cinema_layout_5.setNextFocusDownId((matrix_cinema_layout_10.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10.getId()
				: R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_5.setNextFocusLeftId(R.id.matrix_cinema_layout_4);
		matrix_cinema_layout_5.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_5.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_6.setNextFocusRightId((matrix_cinema_layout_7.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7.getId()
				: R.id.matrix_app_layout_4);
		matrix_cinema_layout_6.setNextFocusUpId(R.id.matrix_cinema_layout_1);
		matrix_cinema_layout_6.setNextFocusLeftId(R.id.matrix_recommend_layout_1);
		matrix_cinema_layout_6.setNextFocusDownId(matrix_cinema_layout_6.getId());
		matrix_cinema_layout_6.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_7.setNextFocusRightId((matrix_cinema_layout_8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8.getId()
				: R.id.matrix_app_layout_4);
		matrix_cinema_layout_7.setNextFocusUpId(R.id.matrix_cinema_layout_2);
		matrix_cinema_layout_7.setNextFocusLeftId(R.id.matrix_cinema_layout_6);
		matrix_cinema_layout_7.setNextFocusDownId(matrix_cinema_layout_7.getId());
		matrix_cinema_layout_7.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_8.setNextFocusRightId((matrix_cinema_layout_9.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9.getId()
				: R.id.matrix_app_layout_4);
		matrix_cinema_layout_8.setNextFocusUpId(R.id.matrix_cinema_layout_3);
		matrix_cinema_layout_8.setNextFocusLeftId(R.id.matrix_cinema_layout_7);
		matrix_cinema_layout_8.setNextFocusDownId(matrix_cinema_layout_8.getId());
		matrix_cinema_layout_8.setOnKeyListener(bottomViewKeyListener);

		matrix_cinema_layout_9.setNextFocusRightId((matrix_cinema_layout_10.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10.getId()
				: R.id.matrix_app_layout_4);
		matrix_cinema_layout_9.setNextFocusUpId(R.id.matrix_cinema_layout_4);
		matrix_cinema_layout_9.setNextFocusLeftId(R.id.matrix_cinema_layout_8);
		matrix_cinema_layout_9.setNextFocusDownId(matrix_cinema_layout_9.getId());
		matrix_cinema_layout_9.setOnKeyListener(bottomViewKeyListener);

		Log.d(TAG, "viewfilper:" + (viewFliper[1].getVisibility() == View.VISIBLE));
		matrix_cinema_layout_10.setNextFocusUpId(R.id.matrix_cinema_layout_5);
		matrix_cinema_layout_10.setNextFocusLeftId(R.id.matrix_cinema_layout_9);
		matrix_cinema_layout_10.setNextFocusRightId(R.id.matrix_app_layout_4);
		matrix_cinema_layout_10.setNextFocusDownId(matrix_cinema_layout_10.getId());
		// matrix_cinema_layout_10.setOnKeyListener(bottomViewKeyListener);

	}

	/**
	 * @Fields bottomViewKeyListener:view 上的key事件
	 */
	private OnKeyListener bottomViewKeyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && event.getAction() == KeyEvent.ACTION_DOWN) {
				if (null != cinemaViews && !cinemaViews.isEmpty()) {
					int viewIndex = cinemaViews.indexOf(v);
					if (viewIndex < cinemaViews.size() - 1 && cinemaViews.get(viewIndex + 1).getVisibility() == View.INVISIBLE) {
						if (null != pageChange) {
							if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

							} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
								pageChange.previousPage();
							}

							Log.d(TAG, "getShowPageIndex::" + getShowPageIndex() + "||getPages::" + getPages());

						}
					} else if ((v.getId() == matrix_cinema_layout_1.getId() || v.getId() == matrix_cinema_layout_6.getId())
							&& keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction() == KeyEvent.ACTION_DOWN) {

					}
				}
			}

			if (v.getId() == recommendEdgeViews[btnIndex].getId()) {
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					// 一般的轮播推荐位失去焦点的动画效果
					AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.5f);
					alphaAnim.setDuration(100);
					alphaAnim.setInterpolator(new LinearInterpolator());
					v.startAnimation(alphaAnim);
				}
				if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.getAction() == KeyEvent.ACTION_UP) {
					v.bringToFront();
					// 一般的轮播推荐位获得焦点的动画效果
					AlphaAnimation alphaAnim = new AlphaAnimation(0.5f, 1.0f);
					alphaAnim.setDuration(100);
					alphaAnim.setInterpolator(new LinearInterpolator());
					v.startAnimation(alphaAnim);
				}
			}
			return false;
		}

	};

	/**
	 * 推荐位的焦点变化监听
	 */
	private OnFocusChangeListener recommendLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			if (isFocused) {
				if (view.getId() == recommendEdgeViews[0].getId() || view.getId() == recommendEdgeViews[5].getId()) {
					focusRules();
				}
				view.bringToFront();
				// 一般的轮播推荐位获得焦点的动画效果
				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
			} else {
				// 一般的轮播推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);
			}

		}
	};

	/**
	 * @Title: MatrixCinemaView
	 * @author:张鹏展
	 * @Description: 补齐页面
	 */
	public void setPageChange() {
		try {
			if (getShowPageIndex() == getPages()) {

				// 仅仅是一个延迟线程，不然焦点不会跳转到下一页item上
				matrixCinemaViewHandler.postDelayed(new Runnable() {
					public void run() {
						viewfliperNext();
						// 设置焦点走向
						focusRules();
						setBottomViewNextFocus();
					}
				}, viewVisibleTime);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadData(boolean isRefleshData) {
		int count = 0;
		do {
			cinemaList = recommendDAO.query(null, null, null, null);
			if (cinemaList.size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (cinemaList.size() == 0);

		if (null != cinemaList && cinemaList.size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}

	}

	/**
	 * 设置影院上的推荐位数据
	 * 
	 * @param movieList
	 */
	public synchronized void setMatrixData() {
		try {
			cinemaName = new ArrayList<String>();
			int cinemaPages = 0;
			int count = cinemaList.size();
			Log.d(TAG, "count::: " +count);

			
			pages = cinemaList.size() / thePageItems;
			//清除原有的
			for(ViewFlipper view:viewFliper){
				view.removeAllViews();
			}
			
			/**
			 * @Fields cinemaNoIndex: 统计编号的开始值
			 */
			int cinemaNoIndex = 4420;
			cinemaNo[5][cinemaPages] = 4425;
			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			ImageView image1 = new ImageView(getContext());
			image1.setTag(false);
			image1.setScaleType(ScaleType.FIT_XY);
			viewFliper[btnIndex].addView(image1, params);
			
			int j = 0;
			for (int i = 0; i < count; i++) {
				FirstClassListEntity entity = cinemaList.get(i);// 获取每个推荐位上的数据列表
				cinemaName.add(String.valueOf(entity.getFirstclass_name()));
				//homeActivity.setCommands(String.valueOf(entity.getFirstclass_name()),
						//String.valueOf(entity.getFirstclass_name()));
				if (j >= viewFliper.length) {
					j = 0;
					cinemaPages++;
					// 排除6号位，页面切换按钮的
					cinemaNo[5][cinemaPages] = 4425;
				}
				if (j == btnIndex) {

					j++;
				}
				// 跳过4425 这个按钮的数据
				if (cinemaNoIndex == 4425) {
					cinemaNoIndex++;
				}
				Log.d(TAG, "movie pageview " + j + "position childs count" + viewFliper[j].getChildCount());
				if (!entity.getIcon().isEmpty()) {
					viewfliperData[j][0]++;
					cinemaNo[j][cinemaPages] = cinemaNoIndex;
					final ImageView image = new ImageView(getContext());
					image.setTag(entity);
					image.setScaleType(ScaleType.FIT_XY);
					ImageLoader.getInstance().displayImage(entity.getIcon(), image, options,new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
							Log.d(TAG, "onLoadingStarted");
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
							Log.d(TAG, "onLoadingFailed");
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							Log.d(TAG, "onLoadingComplete");
							image.requestLayout();
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							Log.d(TAG, "onLoadingCancelled");
						}
					});
					viewFliper[j].addView(image, params);
					j++;
					cinemaNoIndex++;
					Log.d(TAG, "cinemaNoIndex::" + cinemaNoIndex);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getCinemaName() {
		return cinemaName;
	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_DATA_SUCCESS:
			viewfliperData = new int[10][1];
			cinemaNo = new int[10][10];
			setMatrixData();
			break;
		case LOAD_DATA_FAIL:

			break;
		default:
			break;
		}
	}

	/*
	 * 底部菜单栏
	 */
	public View getBottomMenuView() {

		cinemaTabView = inflate(getContext(), R.layout.sub_navigation_common_cinema, null);
		// player record
		cinemaRecordLayout = cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_record_text_layout);

		// film favorite
		cinemaFavouriteLayout = cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_favourite_text_layout);

		// game installed
		cinemaInstalledLayout = cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_installed_text_layout);

		// film search
		cinemaSearchLayout = cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_search_text_layout);

		// system setting
		cinemaSettingLayout = cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_setting_text_layout);

		// user
		// cinemaUserLayout =
		// cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_user_text_layout);

		// external
		cinemaExternalLayout = cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_external_text_layout);

		setBottomViewNextFocus();

		cinemaRecordLayout.setOnClickListener(new CinemaRecordClickListener());
		cinemaFavouriteLayout.setOnClickListener(new CinemaFavouriteClickListener());
		cinemaInstalledLayout.setOnClickListener(new CinemaInstalledClickListener());
		cinemaSearchLayout.setOnClickListener(new CinemaSearchClickListener());
		cinemaSettingLayout.setOnClickListener(new CinemaSettingClickListener());
		// cinemaUserLayout.setOnClickListener(new CinemaUserClickListener());
		cinemaExternalLayout.setOnClickListener(new CinemaExternalClickListener());

		bottomMenuViews = new View[] { cinemaRecordLayout, cinemaFavouriteLayout, cinemaInstalledLayout, cinemaSearchLayout, cinemaSettingLayout,
				cinemaExternalLayout };// cinemaUserLayout,
		viewFocusDirectionListener.setButtomMenuViewFocusDirection(bottomMenuViews);
		return cinemaTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class CinemaRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4410").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class CinemaFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4409").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应已安装应用游戏记录点击事件 点击后进入
	 */
	class CinemaInstalledClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.appstore.buy");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra("category_id", 2);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_installed_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4411").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class CinemaSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), SearchHomeActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_search_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4407").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class CinemaSettingClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.settings.ACTION_SETTING");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			/* start by ZhaiJianfeng */
			int netStatus = HiveviewApplication.getNetStatus();
			intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
			/* end by ZhaiJianfeng */
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_setting_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4412").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 */
	// class CinemaUserClickListener extends SimpleOnClickListener {
	// @Override
	// public DataHolder doOnClick(View view) {
	// Intent intent = new Intent();
	// intent.setAction("com.hiveview.user.usercenter");
	// intent.addCategory(Intent.CATEGORY_DEFAULT);
	// getContext().startActivity(intent);
	//
	// return new
	// DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON).setButton(String.valueOf(view.getId()),
	// getResources().getString(R.string.sub_navigation_common_user_text))
	// .setSenceName(AppScene.getScene()).setViewPosition("4413").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
	// }
	// }

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class CinemaExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext()).setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(String.valueOf(view.getId()), getResources().getString(R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("4414").setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * @Title: MatrixCinemaView
	 * @author:张鹏展
	 * @Description: 底部菜单的焦点走向
	 */
	private void setBottomViewNextFocus() {
		cinemaExternalLayout.setNextFocusUpId((matrix_cinema_layout_10.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10.getId()
				: (matrix_cinema_layout_5.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5.getId() : R.id.navigation_tab_movie_text);
		// cinemaUserLayout.setNextFocusUpId((matrix_cinema_layout_9.getVisibility()
		// == View.VISIBLE) ? matrix_cinema_layout_9.getId()
		// : (matrix_cinema_layout_4.getVisibility() == View.VISIBLE) ?
		// matrix_cinema_layout_4.getId() : R.id.navigation_tab_movie_text);
		cinemaSettingLayout.setNextFocusUpId((matrix_cinema_layout_8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8.getId()
				: (matrix_cinema_layout_3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3.getId() : R.id.navigation_tab_movie_text);
		cinemaSearchLayout.setNextFocusUpId((matrix_cinema_layout_8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8.getId()
				: (matrix_cinema_layout_3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3.getId() : R.id.navigation_tab_movie_text);
		cinemaInstalledLayout.setNextFocusUpId((matrix_cinema_layout_7.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7.getId()
				: (matrix_cinema_layout_2.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2.getId() : R.id.navigation_tab_movie_text);
		cinemaFavouriteLayout.setNextFocusUpId(matrix_cinema_layout_6.getId());
		cinemaRecordLayout.setNextFocusUpId(matrix_cinema_layout_6.getId());
	}

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup movieTabView = (ViewGroup) inflate(getContext(), R.layout.top_menu_movie_layout, null);
		TextView topTabTextView = (TextView) movieTabView.findViewById(R.id.navigation_tab_movie_text);
		ImageView topFadeTabImageView = (ImageView) movieTabView.findViewById(R.id.navigation_tab_movie_fade);
		topTabTextView.setOnFocusChangeListener(new TopTabOnFocusChangeListener(topTabTextView, topFadeTabImageView));
		topTabTextView.setNextFocusDownId(R.id.matrix_cinema_layout_1);
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_movie_text);

		return movieTabView;
	}

	/**
	 * @ClassName: RecommendLayoutClickListener
	 * @Description: 获取viewfilper 页码
	 * @author: zhangpengzhan
	 * @date 2014年9月16日 上午9:59:56
	 * 
	 */
	public int getShowPageIndex() {
		Log.d(TAG, "viewIndex==>" + viewIndex % (pages + 1));
		return viewIndex % (pages + 1);
	}

	public int getPages() {
		return pages;
	}

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class RecommendLayoutClickListener implements OnClickListener {

		public void onClick(View view) {
			int index = -1;
			for (int i = 0; i < recommendEdgeViews.length; i++) {
				if (view == recommendEdgeViews[i]) {
					index = i;
				}
			}
			if (viewFliper[index].getCurrentView() == null || viewFliper[index].getChildCount() == 0) {
				return;
			}
			// 获取推荐位绑定的数据
			Object entity = (Object) viewFliper[index].getCurrentView().getTag();
			if (null == entity) {
				return;
			}
			if (entity instanceof FirstClassListEntity) {
				String action = ChannelInvoker.getInstance().getContent(((FirstClassListEntity) entity).getShow_type());
				if (null != action) {
					try {
						Intent intent = new Intent(action);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.putExtra("category_id", ((FirstClassListEntity) entity).getFirstclass_id());
						intent.putExtra("category_name", ((FirstClassListEntity) entity).getFirstclass_name());
						Log.d(TAG, "category_name::" + ((FirstClassListEntity) entity).getFirstclass_name());
						context.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
						return ;
					}
				}
				KeyEventHandler.post(new DataHolder.Builder(context)
						.setTabNo(Tab.FILM)
						.setSrcType(ItemType.CLASS)
						.setMovieDemandQueryInfo(String.valueOf(((FirstClassListEntity) entity).getFirstclass_name()),
								String.valueOf(((FirstClassListEntity) entity).getFirstclass_id()))
						.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).setViewPosition(String.valueOf(cinemaNo[index][getShowPageIndex()]))
						.setPositionId(String.valueOf(view.getId())).build());

			} else {
				if (entity.equals(false)) {

					viewfliperNext();
					// 设置焦点走向
					focusRules();
					setBottomViewNextFocus();
				}
				KeyEventHandler.post(new DataHolder.Builder(context).setTabNo(Tab.FILM).setMovieDemandQueryInfo("0", "0").setSrcType(ItemType.CLASS)
						.setViewPosition(String.valueOf(cinemaNo[index][getShowPageIndex()])).setPositionId(String.valueOf(view.getId()))
						.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).build());
			}
		}
	}

	public void viewfliperNext() {
		try {
			viewIndex++;
			for (int i = 0; i < viewFliper.length; i++) {
				if (i == btnIndex) {
					continue;
				}
				if (viewfliperData[i][0] > getShowPageIndex()) {
					recommendEdgeViews[i].setVisibility(View.VISIBLE);
					viewFliper[i].showNext();
					viewFliper[i].clearAnimation();
					viewFliper[i].stopFlipping();
				} else if (viewfliperData[i][0] == getShowPageIndex()) {
					recommendEdgeViews[i].setVisibility(View.INVISIBLE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @ClassName: PageChange
	 * @Description: 需要调用翻页的接口
	 * @author: zhangpengzhan
	 * @date 2014年9月17日 下午2:42:14
	 * 
	 */
	public interface PageChange {
		public void nextPage();

		public void previousPage();
	}
	
	
	
	
	/**
	 * @Title: MatrixCinemaView
	 * @author:张鹏展
	 * @Description: 语音控制打开对应的频道
	 * @param value
	 */
	public void openItemActivity(String value){
		try{
			int entityIndex = cinemaName.indexOf(value);
			FirstClassListEntity entity = cinemaList.get(entityIndex);

			String action = ChannelInvoker.getInstance().getContent(((FirstClassListEntity) entity).getShow_type());
			if (null != action) {
				try {
					Intent intent = new Intent(action);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id", ((FirstClassListEntity) entity).getFirstclass_id());
					intent.putExtra("category_name", ((FirstClassListEntity) entity).getFirstclass_name());
					Log.d(TAG, "category_name::" + ((FirstClassListEntity) entity).getFirstclass_name());
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		/*KeyEventHandler.post(new DataHolder.Builder(context)
				.setTabNo(Tab.FILM)
				.setSrcType(ItemType.CLASS)
				.setMovieDemandQueryInfo(String.valueOf(((FirstClassListEntity) entity).getFirstclass_name()),
						String.valueOf(((FirstClassListEntity) entity).getFirstclass_id()))
				.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST).setViewPosition(String.valueOf(cinemaNo[index][getShowPageIndex()]))
				.setPositionId(String.valueOf(view.getId())).build());*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
