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
import com.hiveview.tv.common.AppConstant;
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
import com.hiveview.tv.view.MatrixCinemaView.CinemaExternalClickListener;
import com.hiveview.tv.view.MatrixCinemaView.CinemaFavouriteClickListener;
import com.hiveview.tv.view.MatrixCinemaView.CinemaInstalledClickListener;
import com.hiveview.tv.view.MatrixCinemaView.CinemaRecordClickListener;
import com.hiveview.tv.view.MatrixCinemaView.CinemaSearchClickListener;
import com.hiveview.tv.view.MatrixCinemaView.CinemaSettingClickListener;
import com.hiveview.tv.view.pager3d.TabBasePageView;

/**
 * @ClassName: MatrixCinemaView
 * @Description: 新版影院的view
 * @author: zhangpengzhan
 * @date 2014年9月4日 上午10:59:39
 * 
 */
public class MatrixCinemaViewNew extends TabBasePageView {
	protected static final String TAG = "MatrixCinemaView";

	private static final int LOAD_DATA_SUCCESS = 0x00123;
	private static final int LOAD_DATA_FAIL = 0x00124;

	/**
	 * 影院页面的竖图推荐位Layout
	 */
	private View matrix_cinema_layout_1_new_v, matrix_cinema_layout_2_new_v,
			matrix_cinema_layout_3_new_v, matrix_cinema_layout_4_new_v,
			matrix_cinema_layout_5_new_v, matrix_cinema_layout_6_new_v,
			matrix_cinema_layout_7_new_v, matrix_cinema_layout_8_new_v,
			matrix_cinema_layout_9_new_v, matrix_cinema_layout_10_new_v,
			matrix_cinema_layout_11_new_v, matrix_cinema_layout_12_new_v;

	/**
	 * 影院页面的横图推荐位Layout
	 */
	private View matrix_cinema_layout_1_new_h1, matrix_cinema_layout_1_new_h2,
			matrix_cinema_layout_6_new_h11, matrix_cinema_layout_6_new_h12,
			matrix_cinema_layout_2_new_h3, matrix_cinema_layout_2_new_h4,
			matrix_cinema_layout_7_new_h13, matrix_cinema_layout_7_new_h14,
			matrix_cinema_layout_3_new_h5, matrix_cinema_layout_3_new_h6,
			matrix_cinema_layout_8_new_h15, matrix_cinema_layout_8_new_h16,
			matrix_cinema_layout_4_new_h7, matrix_cinema_layout_4_new_h8,
			matrix_cinema_layout_9_new_h17, matrix_cinema_layout_9_new_h18,
			matrix_cinema_layout_5_new_h9, matrix_cinema_layout_5_new_h10,
			matrix_cinema_layout_10_new_h19, matrix_cinema_layout_10_new_h20,
			matrix_cinema_layout_11_new_h21, matrix_cinema_layout_11_new_h22,
			matrix_cinema_layout_12_new_h23, matrix_cinema_layout_12_new_h24;

	/**
	 * 推荐位竖图
	 */
	private ImageView cinema_flipper_1, cinema_flipper_2, cinema_flipper_3,
			cinema_flipper_4, cinema_flipper_5, cinema_flipper_6,
			cinema_flipper_7, cinema_flipper_8, cinema_flipper_9,
			cinema_flipper_10, cinema_flipper_11, cinema_flipper_12;
	/**
	 * 推荐位竖图
	 */
	private ImageView cinema_flipper_1_h1, cinema_flipper_2_h3,
			cinema_flipper_3_h5, cinema_flipper_4_h7, cinema_flipper_5_h9,
			cinema_flipper_6_h11, cinema_flipper_7_h13, cinema_flipper_8_h15,
			cinema_flipper_9_h17, cinema_flipper_10_h19, cinema_flipper_1_h2,
			cinema_flipper_2_h4, cinema_flipper_3_h6, cinema_flipper_4_h8,
			cinema_flipper_5_h10, cinema_flipper_6_h12, cinema_flipper_7_h14,
			cinema_flipper_8_h16, cinema_flipper_9_h18, cinema_flipper_10_h20,
			cinema_flipper_11_h21, cinema_flipper_11_h22,
			cinema_flipper_12_h23, cinema_flipper_12_h24;
	/**
	 * @Fields cinemaList:应该显示数据的
	 */
	private ArrayList<FirstClassListEntity> cinemaList = null;
	
	/**
	 * @Fields recommendDAO:存储相关数据的数据库
	 */
	private ChannelDAO recommendDAO = null;

	/**
	 * @Fields viewIndex:viewFilper中view 的index
	 */
	private int viewIndex = 0;
	private int pages = 0;
	private int thePageItems = 9;
	/**
	 * 竖图和横图图片数组
	 * 
	 * @author lhj
	 */
	private ImageView[] imageview_v;
	private ImageView[] imageview_h;
	/**
	 * 竖图和横图layout数组
	 * 
	 * @author lhj
	 */
	private View[] layout_v;
	private View[] layout_h;
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
	/**
	 * @Fields cinemaName 保存影院页面的image
	 */
	private ArrayList<ImageView> cinemaViews;

	/**
	 * @Fields cinemaName 保存影院页面的频道名称
	 */
	private ArrayList<String> cinemaName;

	/**
	 * @Fields cinemaName 保存影院页面的Layout
	 */
	private ArrayList<View> cinemaLayouts;

	private Context context;

	/**
	 * @Fields pageChange:需要换页的接口
	 */
	private PageChange pageChange;
	/**
	 * @Fields pageChange:数据加载完，动态改变首页焦点走向的接口
	 * @author lhj
	 */
	private OnDataComplet onDataComplet;

	/**
	 * 顶部影院textview
	 */
	private TextView topTabText;
	/**
	 * 竖图和横图总数
	 * 
	 * @author lhj
	 */
	private final int ALL_SIZE_V = 12;
	private final int ALL_SIZE_H = 24;
	/**
	 * 获得焦点的view
	 */
	protected View focusView = null;
	/**
	 * 获得焦点的索引
	 */
	int focusViewIndex = 0;
	/**
	 * 是否获得焦点
	 */
	protected boolean isFocusC;

	public MatrixCinemaViewNew(Context context) {
		super(context);
		init();
		this.context = context;
	}

	public MatrixCinemaViewNew(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		this.context = context;
	}

	public MatrixCinemaViewNew(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		this.context = context;

	}

	public MatrixCinemaViewNew(Context context,
			RecommendViewFocusDirectionListener listener) {
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
		// homeActivity = (HomeActivity) context;
		// homeActivity.setCinemaInterface(new ItemButtonOnClick());
		recommendDAO = new ChannelDAO(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		View view = inflate(getContext(), R.layout.matrix_cinema_view_new, null);
		this.addView(view, params);
		// 初始化布局
		matrix_cinema_layout_1_new_v = (View) findViewById(R.id.matrix_cinema_layout_1_new_v);
		matrix_cinema_layout_2_new_v = (View) findViewById(R.id.matrix_cinema_layout_2_new_v);
		matrix_cinema_layout_3_new_v = (View) findViewById(R.id.matrix_cinema_layout_3_new_v);
		matrix_cinema_layout_4_new_v = (View) findViewById(R.id.matrix_cinema_layout_4_new_v);
		matrix_cinema_layout_5_new_v = (View) findViewById(R.id.matrix_cinema_layout_5_new_v);
		matrix_cinema_layout_6_new_v = (View) findViewById(R.id.matrix_cinema_layout_6_new_v);
		matrix_cinema_layout_7_new_v = (View) findViewById(R.id.matrix_cinema_layout_7_new_v);
		matrix_cinema_layout_8_new_v = (View) findViewById(R.id.matrix_cinema_layout_8_new_v);
		matrix_cinema_layout_9_new_v = (View) findViewById(R.id.matrix_cinema_layout_9_new_v);
		matrix_cinema_layout_10_new_v = (View) findViewById(R.id.matrix_cinema_layout_10_new_v);
		matrix_cinema_layout_11_new_v = (View) findViewById(R.id.matrix_cinema_layout_11_new_v);
		matrix_cinema_layout_12_new_v = (View) findViewById(R.id.matrix_cinema_layout_12_new_v);

		matrix_cinema_layout_1_new_h1 = (View) findViewById(R.id.matrix_cinema_layout_1_new_h1);
		matrix_cinema_layout_1_new_h2 = (View) findViewById(R.id.matrix_cinema_layout_1_new_h2);
		matrix_cinema_layout_2_new_h3 = (View) findViewById(R.id.matrix_cinema_layout_2_new_h3);
		matrix_cinema_layout_2_new_h4 = (View) findViewById(R.id.matrix_cinema_layout_2_new_h4);
		matrix_cinema_layout_3_new_h5 = (View) findViewById(R.id.matrix_cinema_layout_3_new_h5);
		matrix_cinema_layout_3_new_h6 = (View) findViewById(R.id.matrix_cinema_layout_3_new_h6);
		matrix_cinema_layout_4_new_h7 = (View) findViewById(R.id.matrix_cinema_layout_4_new_h7);
		matrix_cinema_layout_4_new_h8 = (View) findViewById(R.id.matrix_cinema_layout_4_new_h8);
		matrix_cinema_layout_5_new_h9 = (View) findViewById(R.id.matrix_cinema_layout_5_new_h9);
		matrix_cinema_layout_5_new_h10 = (View) findViewById(R.id.matrix_cinema_layout_5_new_h10);
		matrix_cinema_layout_6_new_h11 = (View) findViewById(R.id.matrix_cinema_layout_6_new_h11);
		matrix_cinema_layout_6_new_h12 = (View) findViewById(R.id.matrix_cinema_layout_6_new_h12);
		matrix_cinema_layout_7_new_h13 = (View) findViewById(R.id.matrix_cinema_layout_7_new_h13);
		matrix_cinema_layout_7_new_h14 = (View) findViewById(R.id.matrix_cinema_layout_7_new_h14);
		matrix_cinema_layout_8_new_h15 = (View) findViewById(R.id.matrix_cinema_layout_8_new_h15);
		matrix_cinema_layout_8_new_h16 = (View) findViewById(R.id.matrix_cinema_layout_8_new_h16);
		matrix_cinema_layout_9_new_h17 = (View) findViewById(R.id.matrix_cinema_layout_9_new_h17);
		matrix_cinema_layout_9_new_h18 = (View) findViewById(R.id.matrix_cinema_layout_9_new_h18);
		matrix_cinema_layout_10_new_h19 = (View) findViewById(R.id.matrix_cinema_layout_10_new_h19);
		matrix_cinema_layout_10_new_h20 = (View) findViewById(R.id.matrix_cinema_layout_10_new_h20);
		matrix_cinema_layout_11_new_h21 = (View) findViewById(R.id.matrix_cinema_layout_11_new_h21);
		matrix_cinema_layout_11_new_h22 = (View) findViewById(R.id.matrix_cinema_layout_11_new_h22);
		matrix_cinema_layout_12_new_h23 = (View) findViewById(R.id.matrix_cinema_layout_12_new_h23);
		matrix_cinema_layout_12_new_h24 = (View) findViewById(R.id.matrix_cinema_layout_12_new_h24);

		// 初始化image
		cinema_flipper_1 = (ImageView) findViewById(R.id.matrix_cinema_flipper_1_new);
		cinema_flipper_2 = (ImageView) findViewById(R.id.matrix_cinema_flipper_2_new);
		cinema_flipper_3 = (ImageView) findViewById(R.id.matrix_cinema_flipper_3_new);
		cinema_flipper_4 = (ImageView) findViewById(R.id.matrix_cinema_flipper_4_new);
		cinema_flipper_5 = (ImageView) findViewById(R.id.matrix_cinema_flipper_5_new);
		cinema_flipper_6 = (ImageView) findViewById(R.id.matrix_cinema_flipper_6_new);
		cinema_flipper_7 = (ImageView) findViewById(R.id.matrix_cinema_flipper_7_new);
		cinema_flipper_8 = (ImageView) findViewById(R.id.matrix_cinema_flipper_8_new);
		cinema_flipper_9 = (ImageView) findViewById(R.id.matrix_cinema_flipper_9_new);
		cinema_flipper_10 = (ImageView) findViewById(R.id.matrix_cinema_flipper_10_new);
		cinema_flipper_11 = (ImageView) findViewById(R.id.matrix_cinema_flipper_11_new);
		cinema_flipper_12 = (ImageView) findViewById(R.id.matrix_cinema_flipper_12_new);

		cinema_flipper_1_h1 = (ImageView) findViewById(R.id.matrix_cinema_flipper_1_new_h1);
		cinema_flipper_1_h2 = (ImageView) findViewById(R.id.matrix_cinema_flipper_1_new_h2);
		cinema_flipper_2_h3 = (ImageView) findViewById(R.id.matrix_cinema_flipper_2_new_h3);
		cinema_flipper_2_h4 = (ImageView) findViewById(R.id.matrix_cinema_flipper_2_new_h4);
		cinema_flipper_3_h5 = (ImageView) findViewById(R.id.matrix_cinema_flipper_3_new_h5);
		cinema_flipper_3_h6 = (ImageView) findViewById(R.id.matrix_cinema_flipper_3_new_h6);
		cinema_flipper_4_h7 = (ImageView) findViewById(R.id.matrix_cinema_flipper_4_new_h7);
		cinema_flipper_4_h8 = (ImageView) findViewById(R.id.matrix_cinema_flipper_4_new_h8);
		cinema_flipper_5_h9 = (ImageView) findViewById(R.id.matrix_cinema_flipper_5_new_h9);
		cinema_flipper_5_h10 = (ImageView) findViewById(R.id.matrix_cinema_flipper_5_new_h10);
		cinema_flipper_6_h11 = (ImageView) findViewById(R.id.matrix_cinema_flipper_6_new_h11);
		cinema_flipper_6_h12 = (ImageView) findViewById(R.id.matrix_cinema_flipper_6_new_h12);
		cinema_flipper_7_h13 = (ImageView) findViewById(R.id.matrix_cinema_flipper_7_new_h13);
		cinema_flipper_7_h14 = (ImageView) findViewById(R.id.matrix_cinema_flipper_7_new_h14);
		cinema_flipper_8_h15 = (ImageView) findViewById(R.id.matrix_cinema_flipper_8_new_h15);
		cinema_flipper_8_h16 = (ImageView) findViewById(R.id.matrix_cinema_flipper_8_new_h16);
		cinema_flipper_9_h17 = (ImageView) findViewById(R.id.matrix_cinema_flipper_9_new_h17);
		cinema_flipper_9_h18 = (ImageView) findViewById(R.id.matrix_cinema_flipper_9_new_h18);
		cinema_flipper_10_h19 = (ImageView) findViewById(R.id.matrix_cinema_flipper_10_new_h19);
		cinema_flipper_10_h20 = (ImageView) findViewById(R.id.matrix_cinema_flipper_10_new_h20);
		cinema_flipper_11_h21 = (ImageView) findViewById(R.id.matrix_cinema_flipper_11_new_h21);
		cinema_flipper_11_h22 = (ImageView) findViewById(R.id.matrix_cinema_flipper_11_new_h22);
		cinema_flipper_12_h23 = (ImageView) findViewById(R.id.matrix_cinema_flipper_12_new_h23);
		cinema_flipper_12_h24 = (ImageView) findViewById(R.id.matrix_cinema_flipper_12_new_h24);

		// 初始化 推荐位竖图的image数组
		imageview_v = new ImageView[] { cinema_flipper_1, cinema_flipper_6,
				cinema_flipper_2, cinema_flipper_7, cinema_flipper_3,
				cinema_flipper_8, cinema_flipper_4, cinema_flipper_9,
				cinema_flipper_5, cinema_flipper_10, cinema_flipper_11,
				cinema_flipper_12 };

		// 初始化 推荐位竖图的layout数组
		layout_v = new View[] { matrix_cinema_layout_1_new_v,
				matrix_cinema_layout_6_new_v, matrix_cinema_layout_2_new_v,
				matrix_cinema_layout_7_new_v, matrix_cinema_layout_3_new_v,
				matrix_cinema_layout_8_new_v, matrix_cinema_layout_4_new_v,
				matrix_cinema_layout_9_new_v, matrix_cinema_layout_5_new_v,
				matrix_cinema_layout_10_new_v, matrix_cinema_layout_11_new_v,
				matrix_cinema_layout_12_new_v };

		// 初始化 推荐位横图的layout数组
		layout_h = new View[] { matrix_cinema_layout_1_new_h1,
				matrix_cinema_layout_1_new_h2, matrix_cinema_layout_6_new_h11,
				matrix_cinema_layout_6_new_h12, matrix_cinema_layout_2_new_h3,
				matrix_cinema_layout_2_new_h4, matrix_cinema_layout_7_new_h13,
				matrix_cinema_layout_7_new_h14, matrix_cinema_layout_3_new_h5,
				matrix_cinema_layout_3_new_h6, matrix_cinema_layout_8_new_h15,
				matrix_cinema_layout_8_new_h16, matrix_cinema_layout_4_new_h7,
				matrix_cinema_layout_4_new_h8, matrix_cinema_layout_9_new_h17,
				matrix_cinema_layout_9_new_h18, matrix_cinema_layout_5_new_h9,
				matrix_cinema_layout_5_new_h10,
				matrix_cinema_layout_10_new_h19,
				matrix_cinema_layout_10_new_h20,
				matrix_cinema_layout_11_new_h21,
				matrix_cinema_layout_11_new_h22,
				matrix_cinema_layout_12_new_h23,
				matrix_cinema_layout_12_new_h24, };

		// 初始化 推荐位横图的image数组
		imageview_h = new ImageView[] { cinema_flipper_1_h1,
				cinema_flipper_1_h2, cinema_flipper_6_h11,
				cinema_flipper_6_h12, cinema_flipper_2_h3, cinema_flipper_2_h4,
				cinema_flipper_7_h13, cinema_flipper_7_h14,
				cinema_flipper_3_h5, cinema_flipper_3_h6, cinema_flipper_8_h15,
				cinema_flipper_8_h16, cinema_flipper_4_h7, cinema_flipper_4_h8,
				cinema_flipper_9_h17, cinema_flipper_9_h18,
				cinema_flipper_5_h9, cinema_flipper_5_h10,
				cinema_flipper_10_h19, cinema_flipper_10_h20,
				cinema_flipper_11_h21, cinema_flipper_11_h22,
				cinema_flipper_12_h23, cinema_flipper_12_h24 };

		// 没网时默认焦点走向
		matrix_cinema_layout_1_new_v
				.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_1_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_6_new_v);
		matrix_cinema_layout_1_new_v
				.setNextFocusLeftId(R.id.matrix_recommend_layout_3);
		matrix_cinema_layout_1_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_2_new_v);

		matrix_cinema_layout_2_new_v
				.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_2_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_7_new_v);
		matrix_cinema_layout_2_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_1_new_v);
		matrix_cinema_layout_2_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_3_new_v);

		matrix_cinema_layout_3_new_v
				.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_3_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_8_new_v);
		matrix_cinema_layout_3_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_2_new_v);
		matrix_cinema_layout_3_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_4_new_v);

		matrix_cinema_layout_4_new_v
				.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_4_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_9_new_v);
		matrix_cinema_layout_4_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_3_new_v);
		matrix_cinema_layout_4_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_5_new_v);

		matrix_cinema_layout_5_new_v
				.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_5_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_10_new_v);
		matrix_cinema_layout_5_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_4_new_v);
		matrix_cinema_layout_5_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_11_new_v);

		matrix_cinema_layout_6_new_v
				.setNextFocusUpId(R.id.matrix_cinema_layout_1_new_v);
		matrix_cinema_layout_6_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_6_new_v);
		matrix_cinema_layout_6_new_v
				.setNextFocusLeftId(R.id.matrix_recommend_layout_1);
		matrix_cinema_layout_6_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_7_new_v);

		matrix_cinema_layout_7_new_v
				.setNextFocusUpId(R.id.matrix_cinema_layout_2_new_v);
		matrix_cinema_layout_7_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_7_new_v);
		matrix_cinema_layout_7_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_6_new_v);
		matrix_cinema_layout_7_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_8_new_v);

		matrix_cinema_layout_8_new_v
				.setNextFocusUpId(R.id.matrix_cinema_layout_3_new_v);
		matrix_cinema_layout_8_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_8_new_v);
		matrix_cinema_layout_8_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_7_new_v);
		matrix_cinema_layout_8_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_9_new_v);

		matrix_cinema_layout_9_new_v
				.setNextFocusUpId(R.id.matrix_cinema_layout_4_new_v);
		matrix_cinema_layout_9_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_9_new_v);
		matrix_cinema_layout_9_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_8_new_v);
		matrix_cinema_layout_9_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_10_new_v);

		matrix_cinema_layout_10_new_v
				.setNextFocusUpId(R.id.matrix_cinema_layout_5_new_v);
		matrix_cinema_layout_10_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_10_new_v);
		matrix_cinema_layout_10_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_9_new_v);
		matrix_cinema_layout_10_new_v
				.setNextFocusRightId(R.id.matrix_cinema_layout_12_new_v);

		matrix_cinema_layout_11_new_v
				.setNextFocusUpId(R.id.navigation_tab_movie_text);
		matrix_cinema_layout_11_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_12_new_v);
		matrix_cinema_layout_11_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_5_new_v);
		matrix_cinema_layout_11_new_v
				.setNextFocusRightId(R.id.matrix_education_layout_1);

		matrix_cinema_layout_12_new_v
				.setNextFocusUpId(R.id.matrix_cinema_layout_11_new_v);
		matrix_cinema_layout_12_new_v
				.setNextFocusDownId(R.id.matrix_cinema_layout_12_new_v);
		matrix_cinema_layout_12_new_v
				.setNextFocusLeftId(R.id.matrix_cinema_layout_10_new_v);
		matrix_cinema_layout_12_new_v
				.setNextFocusRightId(R.id.matrix_education_layout_1);

		super.init();
	}

	/**
	 * right1--> matrix_app_layout_0 right2--> matrix_app_layout_5 left1-->
	 * matrix_recommend_flipper_2_out_layout left2--> matrix_recommend_layout_4
	 * 
	 * @Title: MatrixCinemaView
	 * @author:李红记
	 * @Description: 正常焦点走向规则
	 */
	private void focusRules() {
		// 第一个推荐位焦点走向
		if (matrix_cinema_layout_1_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_1_new_v
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_1_new_v
					.setNextFocusDownId((matrix_cinema_layout_6_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_6_new_v
							.getId()
							: ((matrix_cinema_layout_6_new_h11.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_6_new_h11
									.getId() : matrix_cinema_layout_1_new_v
									.getId()));
			if(HomeActivity.isNeedChlitina){
				matrix_cinema_layout_1_new_v
				.setNextFocusLeftId(R.id.matrix_game_layout_4);
			}else{
			matrix_cinema_layout_1_new_v
					.setNextFocusLeftId(R.id.matrix_recommend_layout_3);
			}
			matrix_cinema_layout_1_new_v
					.setNextFocusRightId((matrix_cinema_layout_2_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_v
							.getId()
							: ((matrix_cinema_layout_2_new_h3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_h3
									.getId() : R.id.matrix_app_layout_0));
		} else if (matrix_cinema_layout_1_new_h1.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_1_new_h1
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_1_new_h1
					.setNextFocusDownId(matrix_cinema_layout_1_new_h2.getId());
			if(HomeActivity.isNeedChlitina){
				matrix_cinema_layout_1_new_h1
				.setNextFocusLeftId(R.id.matrix_game_layout_4);
			}else{
			matrix_cinema_layout_1_new_h1
					.setNextFocusLeftId(R.id.matrix_recommend_layout_3);
			}
			matrix_cinema_layout_1_new_h1
					.setNextFocusRightId(matrix_cinema_layout_2_new_h3.getId());

			matrix_cinema_layout_1_new_h2
					.setNextFocusUpId(matrix_cinema_layout_1_new_h1.getId());
			matrix_cinema_layout_1_new_h2
					.setNextFocusDownId(matrix_cinema_layout_6_new_h11.getId());
			if(HomeActivity.isNeedChlitina){
				matrix_cinema_layout_1_new_h2
				.setNextFocusLeftId(R.id.matrix_game_layout_4);
			}else{
			matrix_cinema_layout_1_new_h2
					.setNextFocusLeftId(R.id.matrix_recommend_layout_3);
			}
			matrix_cinema_layout_1_new_h2
					.setNextFocusRightId(matrix_cinema_layout_2_new_h4.getId());
		}
		// 第二个推荐位焦点走向
		if (matrix_cinema_layout_2_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_2_new_v
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_2_new_v
					.setNextFocusDownId((matrix_cinema_layout_7_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7_new_v
							.getId()
							: ((matrix_cinema_layout_7_new_h13.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7_new_h13
									.getId() : matrix_cinema_layout_2_new_v
									.getId()));
			matrix_cinema_layout_2_new_v
					.setNextFocusLeftId((matrix_cinema_layout_1_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_v
							.getId()
							: ((matrix_cinema_layout_1_new_h1.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_h1
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_2_new_v
					.setNextFocusRightId((matrix_cinema_layout_3_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_v
							.getId()
							: ((matrix_cinema_layout_3_new_h5.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_h5
									.getId() : R.id.matrix_app_layout_0));
		} else if (matrix_cinema_layout_2_new_h3.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_2_new_h3
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_2_new_h3
					.setNextFocusDownId(matrix_cinema_layout_2_new_h4.getId());
			matrix_cinema_layout_2_new_h3
					.setNextFocusLeftId((matrix_cinema_layout_1_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_v
							.getId()
							: ((matrix_cinema_layout_1_new_h1.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_h1
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_2_new_h3
					.setNextFocusRightId(matrix_cinema_layout_3_new_h5.getId());

			matrix_cinema_layout_2_new_h4
					.setNextFocusUpId(matrix_cinema_layout_2_new_h3.getId());
			matrix_cinema_layout_2_new_h4
					.setNextFocusDownId(matrix_cinema_layout_7_new_h13.getId());
			matrix_cinema_layout_2_new_h4
					.setNextFocusLeftId((matrix_cinema_layout_1_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_v
							.getId()
							: ((matrix_cinema_layout_1_new_h2.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_h2
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_2_new_h4
					.setNextFocusRightId(matrix_cinema_layout_3_new_h6.getId());
		}
		// 第三个推荐位焦点走向
		if (matrix_cinema_layout_3_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_3_new_v
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_3_new_v
					.setNextFocusDownId((matrix_cinema_layout_8_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8_new_v
							.getId()
							: ((matrix_cinema_layout_8_new_h15.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8_new_h15
									.getId() : matrix_cinema_layout_3_new_v
									.getId()));
			matrix_cinema_layout_3_new_v
					.setNextFocusLeftId(matrix_cinema_layout_2_new_v.getId());
			matrix_cinema_layout_3_new_v
					.setNextFocusRightId((matrix_cinema_layout_4_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_v
							.getId()
							: ((matrix_cinema_layout_4_new_h7.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_h7
									.getId() : R.id.matrix_app_layout_0));
		} else if (matrix_cinema_layout_3_new_h5.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_3_new_h5
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_3_new_h5
					.setNextFocusDownId(matrix_cinema_layout_3_new_h6.getId());
			matrix_cinema_layout_3_new_h5
					.setNextFocusLeftId((matrix_cinema_layout_2_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_v
							.getId()
							: ((matrix_cinema_layout_2_new_h3.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_h3
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_3_new_h5
					.setNextFocusRightId(matrix_cinema_layout_4_new_h7.getId());

			matrix_cinema_layout_3_new_h6
					.setNextFocusUpId(matrix_cinema_layout_3_new_h5.getId());
			matrix_cinema_layout_3_new_h6
					.setNextFocusDownId(matrix_cinema_layout_8_new_h15.getId());
			matrix_cinema_layout_3_new_h6
					.setNextFocusLeftId((matrix_cinema_layout_2_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_v
							.getId()
							: ((matrix_cinema_layout_2_new_h4.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_h4
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_3_new_h6
					.setNextFocusRightId(matrix_cinema_layout_4_new_h8.getId());
		}

		// 第四个推荐位焦点走向
		if (matrix_cinema_layout_4_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_4_new_v
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_4_new_v
					.setNextFocusDownId((matrix_cinema_layout_9_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9_new_v
							.getId()
							: ((matrix_cinema_layout_9_new_h17.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9_new_h17
									.getId() : matrix_cinema_layout_4_new_v
									.getId()));
			matrix_cinema_layout_4_new_v
					.setNextFocusLeftId(matrix_cinema_layout_3_new_v.getId());
			matrix_cinema_layout_4_new_v
					.setNextFocusRightId((matrix_cinema_layout_5_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5_new_v
							.getId()
							: ((matrix_cinema_layout_5_new_h9.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5_new_h9
									.getId() : R.id.matrix_app_layout_0));
		} else if (matrix_cinema_layout_4_new_h7.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_4_new_h7
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_4_new_h7
					.setNextFocusDownId(matrix_cinema_layout_4_new_h8.getId());
			matrix_cinema_layout_4_new_h7
					.setNextFocusLeftId((matrix_cinema_layout_3_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_v
							.getId()
							: ((matrix_cinema_layout_3_new_h5.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_h5
									.getId() : R.id.matrix_recommend_layout_4));
			matrix_cinema_layout_4_new_h7
					.setNextFocusRightId(matrix_cinema_layout_5_new_h9.getId());

			matrix_cinema_layout_4_new_h8
					.setNextFocusUpId(matrix_cinema_layout_4_new_h7.getId());
			matrix_cinema_layout_4_new_h8
					.setNextFocusDownId(matrix_cinema_layout_9_new_h17.getId());
			matrix_cinema_layout_4_new_h8
					.setNextFocusLeftId((matrix_cinema_layout_3_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_v
							.getId()
							: ((matrix_cinema_layout_3_new_h6.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_h6
									.getId() : R.id.matrix_recommend_layout_4));
			matrix_cinema_layout_4_new_h8
					.setNextFocusRightId(matrix_cinema_layout_5_new_h10.getId());
		}

		// 第五个推荐位焦点走向
		if (matrix_cinema_layout_5_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_5_new_v
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_5_new_v
					.setNextFocusDownId((matrix_cinema_layout_10_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10_new_v
							.getId()
							: ((matrix_cinema_layout_10_new_h19.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10_new_h19
									.getId() : matrix_cinema_layout_5_new_v
									.getId()));
			matrix_cinema_layout_5_new_v
					.setNextFocusLeftId(matrix_cinema_layout_4_new_v.getId());
			matrix_cinema_layout_5_new_v
					.setNextFocusRightId((matrix_cinema_layout_11_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_11_new_v
							.getId()
							: ((matrix_cinema_layout_11_new_h21.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_11_new_h21
									.getId() : R.id.matrix_app_layout_0));
		} else if (matrix_cinema_layout_5_new_h9.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_5_new_h9
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_5_new_h9
					.setNextFocusDownId(matrix_cinema_layout_5_new_h10.getId());
			matrix_cinema_layout_5_new_h9
					.setNextFocusLeftId((matrix_cinema_layout_4_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_v
							.getId()
							: ((matrix_cinema_layout_4_new_h7.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_h7
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_5_new_h9
					.setNextFocusRightId(matrix_cinema_layout_11_new_h21
							.getId());

			matrix_cinema_layout_5_new_h10
					.setNextFocusUpId(matrix_cinema_layout_5_new_h9.getId());
			matrix_cinema_layout_5_new_h10
					.setNextFocusDownId(matrix_cinema_layout_10_new_h19.getId());
			matrix_cinema_layout_5_new_h10
					.setNextFocusLeftId((matrix_cinema_layout_4_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_v
							.getId()
							: ((matrix_cinema_layout_4_new_h8.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_h8
									.getId() : R.id.matrix_recommend_layout_3));
			matrix_cinema_layout_5_new_h10
					.setNextFocusRightId(matrix_cinema_layout_11_new_h22
							.getId());
		}
		// 第六个推荐位焦点走向
		if (matrix_cinema_layout_6_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_6_new_v
					.setNextFocusUpId(R.id.matrix_cinema_layout_1_new_v);
			matrix_cinema_layout_6_new_v
					.setNextFocusDownId(matrix_cinema_layout_6_new_v.getId());
			if(HomeActivity.isNeedChlitina){
				matrix_cinema_layout_6_new_v
				.setNextFocusLeftId(R.id.matrix_game_layout_5);
			}else{
			matrix_cinema_layout_6_new_v
					.setNextFocusLeftId(R.id.matrix_recommend_layout_4);
			}
			matrix_cinema_layout_6_new_v
					.setNextFocusRightId((matrix_cinema_layout_7_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7_new_v
							.getId()
							: ((matrix_cinema_layout_7_new_h13.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7_new_h13
									.getId() : R.id.matrix_app_layout_0));
		} else if (matrix_cinema_layout_6_new_h11.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_6_new_h11
					.setNextFocusUpId((matrix_cinema_layout_1_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_1_new_v
							.getId() : matrix_cinema_layout_1_new_h2.getId());
			matrix_cinema_layout_6_new_h11
					.setNextFocusDownId(R.id.matrix_cinema_layout_6_new_h12);
			if(HomeActivity.isNeedChlitina){
				matrix_cinema_layout_6_new_h11
				.setNextFocusLeftId(R.id.matrix_game_layout_5);
			}else{
			matrix_cinema_layout_6_new_h11
					.setNextFocusLeftId(R.id.matrix_recommend_layout_4);
			}
			matrix_cinema_layout_6_new_h11
					.setNextFocusRightId(matrix_cinema_layout_7_new_h13.getId());

			matrix_cinema_layout_6_new_h12
					.setNextFocusUpId(matrix_cinema_layout_6_new_h11.getId());
			matrix_cinema_layout_6_new_h12
					.setNextFocusDownId(matrix_cinema_layout_6_new_h12.getId());
			if(HomeActivity.isNeedChlitina){
				matrix_cinema_layout_6_new_h12
				.setNextFocusLeftId(R.id.matrix_game_layout_6);
			}else{
			matrix_cinema_layout_6_new_h12
					.setNextFocusLeftId(R.id.matrix_recommend_layout_1);
			}
			matrix_cinema_layout_6_new_h12
					.setNextFocusRightId(matrix_cinema_layout_7_new_h14.getId());
		}
		// 第七个推荐位焦点走向
		if (matrix_cinema_layout_7_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_7_new_v
					.setNextFocusUpId(matrix_cinema_layout_2_new_v.getId());
			matrix_cinema_layout_7_new_v
					.setNextFocusDownId(matrix_cinema_layout_7_new_v.getId());
			matrix_cinema_layout_7_new_v
					.setNextFocusLeftId(matrix_cinema_layout_6_new_v.getId());
			matrix_cinema_layout_7_new_v
					.setNextFocusRightId((matrix_cinema_layout_8_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8_new_v
							.getId() : matrix_cinema_layout_8_new_h15.getId());
		} else if (matrix_cinema_layout_7_new_h13.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_7_new_h13
					.setNextFocusUpId((matrix_cinema_layout_2_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_2_new_v
							.getId() : matrix_cinema_layout_2_new_h4.getId());
			matrix_cinema_layout_7_new_h13
					.setNextFocusDownId(matrix_cinema_layout_7_new_h14.getId());
			matrix_cinema_layout_7_new_h13
					.setNextFocusLeftId((matrix_cinema_layout_6_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_6_new_v
							.getId() : matrix_cinema_layout_6_new_h11.getId());
			matrix_cinema_layout_7_new_h13
					.setNextFocusRightId(matrix_cinema_layout_8_new_h15.getId());

			matrix_cinema_layout_7_new_h14
					.setNextFocusUpId(matrix_cinema_layout_7_new_h13.getId());
			matrix_cinema_layout_7_new_h14
					.setNextFocusDownId(matrix_cinema_layout_7_new_h14.getId());
			matrix_cinema_layout_7_new_h14
					.setNextFocusLeftId((matrix_cinema_layout_6_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_6_new_v
							.getId() : matrix_cinema_layout_6_new_h12.getId());
			matrix_cinema_layout_7_new_h14
					.setNextFocusRightId(matrix_cinema_layout_8_new_h16.getId());
		}
		// 第八个推荐位焦点走向
		if (matrix_cinema_layout_8_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_8_new_v
					.setNextFocusUpId(matrix_cinema_layout_3_new_v.getId());
			matrix_cinema_layout_8_new_v
					.setNextFocusDownId(matrix_cinema_layout_8_new_v.getId());
			matrix_cinema_layout_8_new_v
					.setNextFocusLeftId(matrix_cinema_layout_7_new_v.getId());
			matrix_cinema_layout_8_new_v
					.setNextFocusRightId((matrix_cinema_layout_9_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9_new_v
							.getId() : matrix_cinema_layout_9_new_h17.getId());
		} else if (matrix_cinema_layout_8_new_h15.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_8_new_h15
					.setNextFocusUpId((matrix_cinema_layout_3_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_3_new_v
							.getId() : matrix_cinema_layout_3_new_h6.getId());
			matrix_cinema_layout_8_new_h15
					.setNextFocusDownId(matrix_cinema_layout_8_new_h16.getId());
			matrix_cinema_layout_8_new_h15
					.setNextFocusLeftId((matrix_cinema_layout_7_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7_new_v
							.getId() : matrix_cinema_layout_7_new_h13.getId());
			matrix_cinema_layout_8_new_h15
					.setNextFocusRightId(matrix_cinema_layout_9_new_h17.getId());

			matrix_cinema_layout_8_new_h16
					.setNextFocusUpId(matrix_cinema_layout_8_new_h15.getId());
			matrix_cinema_layout_8_new_h16
					.setNextFocusDownId(matrix_cinema_layout_7_new_h14.getId());
			matrix_cinema_layout_8_new_h16
					.setNextFocusLeftId((matrix_cinema_layout_7_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_7_new_v
							.getId() : matrix_cinema_layout_7_new_h14.getId());
			matrix_cinema_layout_8_new_h16
					.setNextFocusRightId(matrix_cinema_layout_9_new_h18.getId());
		}
		// 第九个推荐位焦点走向
		if (matrix_cinema_layout_9_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_9_new_v
					.setNextFocusUpId(matrix_cinema_layout_4_new_v.getId());
			matrix_cinema_layout_9_new_v
					.setNextFocusDownId(matrix_cinema_layout_9_new_v.getId());
			matrix_cinema_layout_9_new_v
					.setNextFocusLeftId(matrix_cinema_layout_8_new_v.getId());
			matrix_cinema_layout_9_new_v
					.setNextFocusRightId((matrix_cinema_layout_10_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10_new_v
							.getId() : matrix_cinema_layout_10_new_h19.getId());
		} else if (matrix_cinema_layout_9_new_h17.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_9_new_h17
					.setNextFocusUpId((matrix_cinema_layout_4_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_4_new_v
							.getId() : matrix_cinema_layout_4_new_h8.getId());
			matrix_cinema_layout_9_new_h17
					.setNextFocusDownId(matrix_cinema_layout_9_new_h18.getId());
			matrix_cinema_layout_9_new_h17
					.setNextFocusLeftId((matrix_cinema_layout_8_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8_new_v
							.getId() : matrix_cinema_layout_8_new_h15.getId());
			matrix_cinema_layout_9_new_h17
					.setNextFocusRightId(matrix_cinema_layout_10_new_h19
							.getId());

			matrix_cinema_layout_9_new_h18
					.setNextFocusUpId(matrix_cinema_layout_9_new_h17.getId());
			matrix_cinema_layout_9_new_h18
					.setNextFocusDownId(matrix_cinema_layout_9_new_h18.getId());
			matrix_cinema_layout_9_new_h18
					.setNextFocusLeftId((matrix_cinema_layout_8_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_8_new_v
							.getId() : matrix_cinema_layout_8_new_h16.getId());
			matrix_cinema_layout_9_new_h18
					.setNextFocusRightId(matrix_cinema_layout_10_new_h20
							.getId());
		}
		// 第十个推荐位焦点走向
		if (matrix_cinema_layout_10_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_10_new_v
					.setNextFocusUpId(matrix_cinema_layout_5_new_v.getId());
			matrix_cinema_layout_10_new_v
					.setNextFocusDownId(matrix_cinema_layout_10_new_v.getId());
			matrix_cinema_layout_10_new_v
					.setNextFocusLeftId(matrix_cinema_layout_9_new_v.getId());
			matrix_cinema_layout_10_new_v
					.setNextFocusRightId((matrix_cinema_layout_12_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_12_new_v
							.getId() : matrix_cinema_layout_12_new_h23.getId());
		} else if (matrix_cinema_layout_10_new_h19.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_10_new_h19
					.setNextFocusUpId((matrix_cinema_layout_5_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5_new_v
							.getId() : matrix_cinema_layout_5_new_h10.getId());
			matrix_cinema_layout_10_new_h19
					.setNextFocusDownId(matrix_cinema_layout_10_new_h20.getId());
			matrix_cinema_layout_10_new_h19
					.setNextFocusLeftId((matrix_cinema_layout_9_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9_new_v
							.getId() : matrix_cinema_layout_9_new_h17.getId());
			matrix_cinema_layout_10_new_h19
					.setNextFocusRightId(matrix_cinema_layout_12_new_h23
							.getId());

			matrix_cinema_layout_10_new_h20
					.setNextFocusUpId(matrix_cinema_layout_10_new_h19.getId());
			matrix_cinema_layout_10_new_h20
					.setNextFocusDownId(matrix_cinema_layout_10_new_h20.getId());
			matrix_cinema_layout_10_new_h20
					.setNextFocusLeftId((matrix_cinema_layout_9_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_9_new_v
							.getId() : matrix_cinema_layout_9_new_h18.getId());
			matrix_cinema_layout_10_new_h20
					.setNextFocusRightId(matrix_cinema_layout_12_new_h24
							.getId());
		}
		// 第十一个推荐位焦点走向
		if (matrix_cinema_layout_11_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_11_new_v
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_11_new_v
					.setNextFocusDownId((matrix_cinema_layout_12_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_12_new_v
							.getId() : matrix_cinema_layout_12_new_h23.getId());
			matrix_cinema_layout_11_new_v
					.setNextFocusLeftId(matrix_cinema_layout_5_new_v.getId());
			if (AppConstant.ISDOMESTIC) {
				if(HomeActivity.isNeedChlitina){
					matrix_cinema_layout_11_new_v
					.setNextFocusRightId(R.id.matrix_recommend_layout_0);
				}else{
				matrix_cinema_layout_11_new_v
						.setNextFocusRightId(R.id.matrix_education_layout_1);}
			} else {
				matrix_cinema_layout_11_new_v
						.setNextFocusRightId(R.id.matrix_app_layout_0);
			}
		} else if (matrix_cinema_layout_11_new_h21.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_11_new_h21
					.setNextFocusUpId(R.id.navigation_tab_movie_text);
			matrix_cinema_layout_11_new_h21
					.setNextFocusDownId(matrix_cinema_layout_11_new_h22.getId());
			matrix_cinema_layout_11_new_h21
					.setNextFocusLeftId((matrix_cinema_layout_5_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5_new_v
							.getId() : matrix_cinema_layout_5_new_h9.getId());
			if (AppConstant.ISDOMESTIC) {
				if(HomeActivity.isNeedChlitina){
					matrix_cinema_layout_11_new_h21
					.setNextFocusRightId(R.id.matrix_recommend_layout_0);
				}else{
				matrix_cinema_layout_11_new_h21
						.setNextFocusRightId(R.id.matrix_education_layout_1);}
			} else {
				matrix_cinema_layout_11_new_h21
						.setNextFocusRightId(R.id.matrix_app_layout_0);
			}
			
			matrix_cinema_layout_11_new_h22
					.setNextFocusUpId(matrix_cinema_layout_11_new_h21.getId());
			matrix_cinema_layout_11_new_h22
					.setNextFocusDownId(matrix_cinema_layout_12_new_h23.getId());
			matrix_cinema_layout_11_new_h22
					.setNextFocusLeftId((matrix_cinema_layout_5_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_5_new_v
							.getId() : matrix_cinema_layout_5_new_h10.getId());
			if (AppConstant.ISDOMESTIC) {
				if(HomeActivity.isNeedChlitina){
					matrix_cinema_layout_11_new_h22
					.setNextFocusRightId(R.id.matrix_recommend_layout_0);
				}else{
				matrix_cinema_layout_11_new_h22
						.setNextFocusRightId(R.id.matrix_education_layout_1);}
			} else {
				matrix_cinema_layout_11_new_h22
						.setNextFocusRightId(R.id.matrix_app_layout_0);
			}
		}

		// 第十二个推荐位焦点走向
		if (matrix_cinema_layout_12_new_v.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_12_new_v
					.setNextFocusUpId(matrix_cinema_layout_11_new_v.getId());
			matrix_cinema_layout_12_new_v
					.setNextFocusDownId(matrix_cinema_layout_12_new_v.getId());
			matrix_cinema_layout_12_new_v
					.setNextFocusLeftId(matrix_cinema_layout_10_new_v.getId());
			if (AppConstant.ISDOMESTIC) {
				if(HomeActivity.isNeedChlitina){
					matrix_cinema_layout_12_new_v
					.setNextFocusRightId(R.id.matrix_recommend_layout_0);
				}else{
				matrix_cinema_layout_12_new_v
						.setNextFocusRightId(R.id.matrix_education_layout_1);}
			} else {
				matrix_cinema_layout_12_new_v
						.setNextFocusRightId(R.id.matrix_app_layout_0);
			}
		} else if (matrix_cinema_layout_12_new_h23.getVisibility() == View.VISIBLE) {
			matrix_cinema_layout_12_new_h23
					.setNextFocusUpId((matrix_cinema_layout_11_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_11_new_v
							.getId() : matrix_cinema_layout_11_new_h22.getId());
			matrix_cinema_layout_12_new_h23
					.setNextFocusDownId(matrix_cinema_layout_12_new_h24.getId());
			matrix_cinema_layout_12_new_h23
					.setNextFocusLeftId((matrix_cinema_layout_10_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10_new_v
							.getId() : matrix_cinema_layout_10_new_h19.getId());
			if (AppConstant.ISDOMESTIC) {
				if(HomeActivity.isNeedChlitina){
					matrix_cinema_layout_12_new_h23
					.setNextFocusRightId(R.id.matrix_recommend_layout_0);
				}else{
				matrix_cinema_layout_12_new_h23
						.setNextFocusRightId(R.id.matrix_education_layout_1);}
			} else {
				matrix_cinema_layout_12_new_h23
						.setNextFocusRightId(R.id.matrix_app_layout_0);
			}

			matrix_cinema_layout_12_new_h24
					.setNextFocusUpId(matrix_cinema_layout_12_new_h23.getId());
			matrix_cinema_layout_12_new_h24
					.setNextFocusDownId(matrix_cinema_layout_12_new_h24.getId());
			matrix_cinema_layout_12_new_h24
					.setNextFocusLeftId((matrix_cinema_layout_10_new_v
							.getVisibility() == View.VISIBLE) ? matrix_cinema_layout_10_new_v
							.getId() : matrix_cinema_layout_10_new_h20.getId());
			if (AppConstant.ISDOMESTIC) {
				if(HomeActivity.isNeedChlitina){
					matrix_cinema_layout_12_new_h24
					.setNextFocusRightId(R.id.matrix_recommend_layout_0);
				}else{
				matrix_cinema_layout_12_new_h24
						.setNextFocusRightId(R.id.matrix_education_layout_1);}
			} else {
				matrix_cinema_layout_12_new_h24
						.setNextFocusRightId(R.id.matrix_app_layout_0);
			}
		}

		/**
		 * 数据加载完，回调 改变首页向右焦点走向
		 * 
		 * @author lhj
		 */
		onDataComplet.getCinemaLayout_1(matrix_cinema_layout_1_new_v
				.getVisibility());
		onDataComplet.getCinemaLayout_6(matrix_cinema_layout_6_new_v
				.getVisibility());
		/**
		 * 数据加载完，回调 改变影院向左焦点走向
		 * 
		 * @author lhj
		 */
		onDataComplet.getCinemaLayout_11(matrix_cinema_layout_11_new_v
				.getVisibility());
		onDataComplet.getCinemaLayout_12(matrix_cinema_layout_12_new_v
				.getVisibility());
		/**
		 * 数据加载完，改变顶部影院向下按键焦点走向
		 * 
		 * @author lhj
		 */
		topTabText
				.setNextFocusDownId((matrix_cinema_layout_1_new_v
						.getVisibility() == View.VISIBLE) ? R.id.matrix_cinema_layout_1_new_v
						: R.id.matrix_cinema_layout_1_new_h1);
		// matrix_cinema_layout_10.setOnKeyListener(bottomViewKeyListener);

	}

	/**
	 * 初始化监听
	 * 
	 * @author lhj
	 */
	public void getCinemaLayout(OnDataComplet onDataComplet2) {
		// TODO Auto-generated method stub
		this.onDataComplet = onDataComplet2;
	}

	/**
	 * 推荐位的焦点变化监听
	 */
	private OnFocusChangeListener recommendLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {
			if (isFocused) {
				if (view.getId() == cinemaLayouts.get(0).getId()
						|| view.getId() == cinemaLayouts.get(1).getId()) {
					focusRules();
				}
				isFocusC = true;
				//得到当前获取焦点的view和索引
				focusView  = view;
				focusViewIndex = cinemaLayouts.indexOf(view);
				Log.v(TAG, "onFocusChange==focusViewIndex="+focusViewIndex+";focusView="+focusView.getTag().toString());
				view.bringToFront();
				// 一般的轮播推荐位获得焦点的动画效果
				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
				HiveviewApplication.mcurrentfocus=view;
			} else {
				// 一般的轮播推荐位失去焦点的动画效果
				AnimationUtil.loadSmallAnimation(view);
				isFocusC = false;
			}

		}
	};

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
			if (null == cinemaName)
				cinemaName = new ArrayList<String>();
			else
				cinemaName.clear();
			if (null == cinemaViews)
				cinemaViews = new ArrayList<ImageView>();
			else
				cinemaViews.clear();
			if (null == cinemaLayouts)
				cinemaLayouts = new ArrayList<View>();
			int count = cinemaList.size();
			int cinemaLayoutsSize = cinemaLayouts.size();
			boolean isLayout_v = false;
			Log.d(TAG, "count::: " + count);
			// 推荐位绑定焦点改变事件和焦点改变事件
			for (int i = 0; i < layout_v.length; i++) {
				// 先隐藏所有控件，数据加载完成后再显示应该显示的layout
				//layout_v[i].setVisibility(View.VISIBLE);
				if(layout_v[i]==focusView){
					isLayout_v = true;
				}
				layout_v[i]
						.setOnFocusChangeListener(recommendLayoutFocusListener);
				layout_v[i]
						.setOnClickListener(new RecommendLayoutClickListener());
			}
			for (int i = 0; i < layout_h.length; i++) {
				// 先隐藏所有控件，数据加载完成后再显示应该显示的layout
			//	layout_h[i].setVisibility(View.VISIBLE);
				if(layout_h[i]==focusView){
					isLayout_v = false;
				}
				layout_h[i]
						.setOnFocusChangeListener(recommendLayoutFocusListener);
				layout_h[i]
						.setOnClickListener(new RecommendLayoutClickListener());
			}
			
			
			cinemaLayouts.clear();
			// 判断总数是否大于12，小于12则显示竖图的view，
			if (count <= ALL_SIZE_V) {
				for (int i = 0; i <ALL_SIZE_H ; i++) {
					layout_h[i].setVisibility(View.INVISIBLE);
				}
				
				for (int i = 0; i < ALL_SIZE_V; i++) {
					cinemaLayouts.add(layout_v[i]);
					cinemaViews.add(imageview_v[i]);
					layout_v[i].setVisibility(i < count ? View.VISIBLE
							: View.INVISIBLE);
					
				}
				if(isFocusC)
					cinemaLayouts.get(focusViewIndex < count?focusViewIndex:count-1).requestFocus();

			}// 大于12,算出要显示几个横图，几个竖图
			else if (count > ALL_SIZE_V) {
				boolean isSmall = false;
				int reremain = count - ALL_SIZE_V;
				int reremain1 =- count + 2*ALL_SIZE_V;
				Log.d(TAG, "reremain::: " + reremain1);
				for (int i = 0; i < ALL_SIZE_V; i++) {
					if(i < reremain1){
					cinemaLayouts.add(layout_v[i]);
					cinemaViews.add(imageview_v[i]);
					layout_v[i].setVisibility(View.VISIBLE);
					}else{
						if(layout_v[i].isFocused()){
							if(isFocusC)
							layout_v[reremain1-1].requestFocus();
						}
						layout_v[i].setVisibility(View.INVISIBLE);
					}
				}
			
				
				// 横图添加view从前面开始
				int j = 0;
				for (int i = ALL_SIZE_H - 1; i >= ALL_SIZE_H - reremain * 2; i--) {
					cinemaLayouts.add(layout_h[ALL_SIZE_H - reremain * 2 + j]);
					cinemaViews.add(imageview_h[ALL_SIZE_H - reremain * 2 + j]);
					layout_h[ALL_SIZE_H - reremain * 2 + j]
							.setVisibility(View.VISIBLE);
					j++;
				}
			
				for (int i = 0; i < ALL_SIZE_H - reremain * 2; i++) {
					if(layout_h[i].isFocused()){
						if(i%2==0){
							if(isFocusC)
							layout_v[i/2].requestFocus();
						}else{
							if(isFocusC)layout_v[(i+1)/2].requestFocus();
						}
					}
					layout_h[i]
							.setVisibility(View.INVISIBLE);
				}
				
			}
			// 添加正常焦点走向
			focusRules();
			pages = cinemaList.size() / thePageItems;

			for (int i = 0; i < count; i++) {
				final int k = i;
				FirstClassListEntity entity = cinemaList.get(i);// 获取每个推荐位上的数据列表
				cinemaName.add(String.valueOf(entity.getFirstclass_name()));
				cinemaLayouts.get(i).setTag(entity);
				// homeActivity.setCommands(String.valueOf(entity.getFirstclass_name()),
				// String.valueOf(entity.getFirstclass_name()));
				if (!entity.getIcon().isEmpty()) {

					ImageLoader.getInstance().displayImage(entity.getIcon(),
							cinemaViews.get(k), options,
							new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String arg0,
										View arg1) {
									// TODO Auto-generated method stub
									Log.d(TAG, "onLoadingStarted");
								}

								@Override
								public void onLoadingFailed(String arg0,
										View arg1, FailReason arg2) {
									// TODO Auto-generated method stub
									Log.d(TAG, "onLoadingFailed");
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									// TODO Auto-generated method stub
									Log.d(TAG, "onLoadingComplete");
									cinemaLayouts.get(k).requestLayout();
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									// TODO Auto-generated method stub
									Log.d(TAG, "onLoadingCancelled");
								}
							});
				}
			}
			this.setVisibility(View.VISIBLE);
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
			setMatrixData();
			break;
		case LOAD_DATA_FAIL:

			break;
		default:
			break;
		}
	}

	@Override
	public ViewGroup getTopMenuView() {
		ViewGroup movieTabView = (ViewGroup) inflate(getContext(),
				R.layout.top_menu_movie_layout, null);
		TextView topTabTextView = (TextView) movieTabView
				.findViewById(R.id.navigation_tab_movie_text);
		topTabText = topTabTextView;
		ImageView topFadeTabImageView = (ImageView) movieTabView
				.findViewById(R.id.navigation_tab_movie_fade);
		topTabTextView
				.setOnFocusChangeListener(new TopTabOnFocusChangeListener(
						topTabTextView, topFadeTabImageView));
		topTabTextView.setNextFocusDownId(R.id.matrix_cinema_layout_1_new_v);
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
			for (int i = 0; i < cinemaLayouts.size(); i++) {
				if (view == cinemaLayouts.get(i)) {
					index = i;
				}
			}
			if (cinemaLayouts == null || cinemaLayouts.size() == 0) {
				return;
			}
			// 获取推荐位绑定的数据
			Object entity = (Object) cinemaLayouts.get(index).getTag();
			if (null == entity) {
				return;
			}
			if (entity instanceof FirstClassListEntity) {
				String action = ChannelInvoker.getInstance().getContent(
						((FirstClassListEntity) entity).getShow_type());
				if (null != action) {
					try {
						Intent intent = new Intent(action);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.putExtra("category_id",
								((FirstClassListEntity) entity)
										.getFirstclass_id());
						intent.putExtra("category_name",
								((FirstClassListEntity) entity)
										.getFirstclass_name());
						Log.d(TAG,
								"category_name::"
										+ ((FirstClassListEntity) entity)
												.getFirstclass_name()
										+ ((FirstClassListEntity) entity)
												.getFirstclass_id());
						context.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}

				KeyEventHandler.post(new DataHolder.Builder(context)
						.setTabNo(Tab.FILM)
						.setSrcType(ItemType.VIDEOSETTYPE)
						.setViewPosition(String.valueOf(((FirstClassListEntity) entity)
						  .getFirstclass_id()))
						.setMovieDemandQueryInfo(
								String.valueOf(((FirstClassListEntity) entity)
										.getFirstclass_name()),
								String.valueOf(((FirstClassListEntity) entity)
										.getFirstclass_id()))
						.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST)
						.build());

			} else {
				if (entity.equals(false)) {

					// 设置焦点走向
					focusRules();
				}

				KeyEventHandler.post(new DataHolder.Builder(context)
						.setTabNo(Tab.FILM).setMovieDemandQueryInfo("0", "0")
						.setSrcType(ItemType.VIDEOSETTYPE)
						.setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST)
						.build());

			}
		}
	}

	/*
	 * 底部菜单栏
	 */
	public View getBottomMenuView() {

		cinemaTabView = inflate(getContext(),
				R.layout.sub_navigation_common_cinema, null);
		// player record
		cinemaRecordLayout = cinemaTabView
				.findViewById(R.id.sub_navigation_common_cinema_record_text_layout);

		// film favorite
		cinemaFavouriteLayout = cinemaTabView
				.findViewById(R.id.sub_navigation_common_cinema_favourite_text_layout);

		// game installed
		cinemaInstalledLayout = cinemaTabView
				.findViewById(R.id.sub_navigation_common_cinema_installed_text_layout);

		// film search
		cinemaSearchLayout = cinemaTabView
				.findViewById(R.id.sub_navigation_common_cinema_search_text_layout);

		// system setting
		cinemaSettingLayout = cinemaTabView
				.findViewById(R.id.sub_navigation_common_cinema_setting_text_layout);

		// user
		// cinemaUserLayout =
		// cinemaTabView.findViewById(R.id.sub_navigation_common_cinema_user_text_layout);

		// external
		cinemaExternalLayout = cinemaTabView
				.findViewById(R.id.sub_navigation_common_cinema_external_text_layout);

		bottomMenuViews = new View[] { cinemaRecordLayout,
				cinemaFavouriteLayout, cinemaInstalledLayout,
				cinemaSearchLayout, cinemaSettingLayout, cinemaExternalLayout };// cinemaUserLayout,
		viewFocusDirectionListener
				.setButtomMenuViewFocusDirection(bottomMenuViews);
		return cinemaTabView;
	}

	/**
	 * @Title: MatrixCinemaView
	 * @author:张鹏展
	 * @Description: 语音控制打开对应的频道
	 * @param value
	 */
	public void openItemActivity(String value) {
		try {
			int entityIndex = cinemaName.indexOf(value);
			FirstClassListEntity entity = cinemaList.get(entityIndex);

			String action = ChannelInvoker.getInstance().getContent(
					((FirstClassListEntity) entity).getShow_type());
			if (null != action) {
				try {
					Intent intent = new Intent(action);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.putExtra("category_id",
							((FirstClassListEntity) entity).getFirstclass_id());
					intent.putExtra("category_name",
							((FirstClassListEntity) entity)
									.getFirstclass_name());
					Log.d(TAG,
							"category_name::"
									+ ((FirstClassListEntity) entity)
											.getFirstclass_name());
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			/*
			 * KeyEventHandler .post(new DataHolder.Builder(context)
			 * .setTabNo(Tab.FILM) .setSrcType(ItemType.CLASS)
			 * .setMovieDemandQueryInfo( String.valueOf(((FirstClassListEntity)
			 * entity) .getFirstclass_name()),
			 * String.valueOf(((FirstClassListEntity) entity)
			 * .getFirstclass_id()))
			 * .setDataType(DataType.TIME_LENGTH_MOVIE_DEMAND_LIST)
			 * .setViewPosition(
			 * String.valueOf(cinemaNo[index][getShowPageIndex()]))
			 * .setPositionId(String.valueOf(view.getId())) .build());
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPageChange() {
		// TODO Auto-generated method stub

	}

	public void viewfliperNext() {
		// TODO Auto-generated method stub

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
	 * @ClassName: PageChange
	 * @Description: 数据加载完，动态改变焦点走向的接口
	 * @author: lihongji
	 * @date 2014年9月17日 下午2:42:14
	 * 
	 */
	public interface OnDataComplet {

		// 改变首页右侧边向右按键时的焦点
		public void getCinemaLayout_1(int isVisible_1);

		public void getCinemaLayout_6(int isVisible_6);

		// 改变教育左侧边向左按键时的焦点
		public void getCinemaLayout_11(int isVisible_11);

		public void getCinemaLayout_12(int isVisible_12);
	}
}
