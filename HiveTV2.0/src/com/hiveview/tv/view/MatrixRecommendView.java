package com.hiveview.tv.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.hiveview.box.framework.image.FailReason;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageLoadingListener;
import com.hiveview.box.framework.image.ImageLoadingProgressListener;
import com.hiveview.box.framework.image.ImageViewAware;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.BaseActivity;
import com.hiveview.tv.activity.CollectActivity;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.PlayerRecordActivity;
import com.hiveview.tv.activity.SearchHomeActivity;
import com.hiveview.tv.activity.SubjectDetailLandspaceActivity;
import com.hiveview.tv.activity.SubjectDetailPortraitActivity;
import com.hiveview.tv.activity.VarietyPagerActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.AppConstant.ImageViewSize;
import com.hiveview.tv.common.AppScene;
import com.hiveview.tv.common.GetStorageListener;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.StorageAdapter;
import com.hiveview.tv.common.content.ContentInvoker;
import com.hiveview.tv.common.content.ContentShowType;
import com.hiveview.tv.common.statistics.DataType;
import com.hiveview.tv.common.statistics.ItemType;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.VideoService;
import com.hiveview.tv.service.dao.AppFocusDAO;
import com.hiveview.tv.service.dao.BaseDAO;
import com.hiveview.tv.service.dao.RecommendDAO;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.LiveStreamEntity;
import com.hiveview.tv.service.entity.MoviePermissionEntity;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.service.entity.SubjectEntity;
import com.hiveview.tv.service.entity.SubjectListEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.net.HttpTaskManager;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.BlueLightVipUtil;
import com.hiveview.tv.utils.DeviceSN;
import com.hiveview.tv.utils.InitAppCountUtils;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.utils.PlayerParamsUtils;
import com.hiveview.tv.utils.ToastUtils;
import com.hiveview.tv.view.pager3d.HomeActions;
import com.hiveview.tv.view.pager3d.TabAppBasePageView;
import com.hiveview.tv.view.pager3d.ViewFlipperInAnimationListener;
import com.hiveview.tv.view.television.MarqueeText;

@SuppressLint("UseSparseArrays")
public class MatrixRecommendView extends TabAppBasePageView {

	private static final int RECOMMEND_FOCUS_COUNT = 2;

	/**
	 * 对应UE上6个推荐位的位置
	 */
	private View matrix_recommend_layout_0, matrix_recommend_layout_1,
			matrix_recommend_layout_2, matrix_recommend_layout_3,
			matrix_recommend_layout_4;

	/**
	 * 推荐页面 5个焦点图的缩略图显示的Layout
	 */
	private View thumb_layout_0, thumb_layout_1, thumb_layout_2,
			thumb_layout_3, thumb_layout_4;

	/**
	 * 对应推荐位中推荐位图片来回切换的ViewFlipper
	 */
	private ViewFlipper matrix_recommend_flipper_0, matrix_recommend_flipper_1;

	/**
	 * 推荐位显示的文本信息的TextView
	 */
	private MarqueeText matrix_recommend_flipper_0_text_view,
			matrix_recommend_flipper_1_text_view;

	/**
	 * 推荐位文本信息显示TextView数组
	 */
	private MarqueeText[] desViews;

	/**
	 * 焦点图缩略图view数组
	 */
	private ImageView[] thumbViews;

	/**
	 * 焦点图缩略图ImageView数组
	 */
	private int[] thumbImages;

	/**
	 * 焦点图大图ImageView数组
	 */
	private int[] LargeImages;

	/**
	 * 焦点图大图文子数组
	 */
	private String[] largeTextStrings;

	/**
	 * 焦点图缩略图Layout数组
	 */
	private View[] thumbLayouts;

	/**
	 * 焦点图的蒙板View的数组
	 */
	private View[] thumbFades;

	/**
	 * 推荐位Layout数组
	 */
	private View[] recommendLayouts;

	/**
	 * 是否直接播放
	 * 
	 * @Fields isPlayer
	 */
	public static boolean isPlayer = false;

	private ArrayList<ArrayList<RecommendEntity>> recommends = null;

	private RecommendDAO recommendDAO = null;

	private final int LOAD_DATA_SUCCESS = 200;

	private final int LOAD_TVLIST_SUCCESS = 201;

	private final int LOAD_DATA_FAIL = -200;

	private boolean[] isLoadFinishArray = new boolean[4];

	/**
	 * H 显示安装应用个数的TextView
	 */
	private TextView tvInstallCount;

	/**
	 * 显示使用存储空间的TextView
	 */
	private TextView installMemory;

	private String storageInfo = null;

	/**
	 * 显示使用存储空间的进度的ProgressBar
	 */
	private ProgressBar installMemoryBar;

	private int usageRate = 0;
	// memory增长数字
	private int num = 0;
	/**
	 * 已安装的应用个数
	 */
	private int installAppCount = 0;

	private boolean isInitAppCount = false;

	private AppFocusDAO appFocusDAO = null;

	ArrayList<ArrayList<AppFocusEntity>> appList = null;

	public int isViewFilperFocus = 0;

	public RecommendEntity theFocusEntity;

	/**
	 * @Fields sharedPreferences 数据存储
	 */
	private SharedPreferences sharedPreferences;
	/**
	 * @Fields openNewsApp 打开新闻客户端的标示
	 */
	private String openNewsApp = "0";

	private HiveTVService service;
	// 推荐位直播流实体
	private LiveStreamEntity liveEntity;


	/**
	 * 用户中心或者搜索图
	 */
	private ImageView recommend_usercenter_search;

	private ItemType[] itemType = new ItemType[] { ItemType.VIDEO,
			ItemType.APP, ItemType.SUBJECT, ItemType.SUBJECT };
	private DataType[] dataType = new DataType[] {
			DataType.CLICK_TAB_RECOMMEND, DataType.CLICK_TAB_RECOMMEND_APP,
			DataType.CLICK_TAB_RECOMMEND_APP_SUBJECT,
			DataType.CLICK_TAB_RECOMMEND_VIDEO_SUBJECT };

	private HashMap<Integer, AppRecommendView> mapDownloadView = new HashMap<Integer, AppRecommendView>();

	private Handler mAnimactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				Log.d(TAG, "case 0 installAppCount=========" + installAppCount);
				tvInstallCount.setText(String.format(
						getResources().getString(R.string.install_app_count),
						installAppCount));
				tvInstallCount.invalidate();
				break;
			case -1:
				// Log.d(TAG, "receive mHandler(-1)!!!!!!!!");
				// Log.d(TAG, "usageRate====================" + usageRate);
				// Log.d(TAG, "storageInfo====================" + storageInfo);

				installMemoryBar.setMax(100);
				installMemoryBar.setProgress(num);
				// 刷新view
				installMemoryBar.invalidate();
				// Log.d(TAG, "case -1 installAppCount=========" +
				// installAppCount);

				break;
			case 2:
				try {

					tvInstallCount.setText(String.format(getResources()
							.getString(R.string.install_app_count),
							installAppCount));
					installMemory.setText(storageInfo);
					installMemory.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		};
	};

	public MatrixRecommendView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MatrixRecommendView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MatrixRecommendView(Context context) {
		super(context);
		init();
	}

	public MatrixRecommendView(Context context,
			RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		init();
	}

	/**
	 * 初始化推荐页面的所有推荐位相关的View
	 */
	protected void init() {

		recommendDAO = new RecommendDAO(getContext());
		sharedPreferences = getContext().getSharedPreferences(
				HiveviewApplication.RecommendTag, Context.MODE_WORLD_READABLE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		View view = inflate(getContext(), R.layout.matrix_recommend_view, null);

		this.addView(view, params);

		// 初始化推荐位Layout
		matrix_recommend_layout_0 = findViewById(R.id.matrix_recommend_layout_0);
		matrix_recommend_layout_1 = findViewById(R.id.matrix_recommend_layout_1);
		matrix_recommend_layout_2 = findViewById(R.id.matrix_recommend_layout_2);
		matrix_recommend_layout_3 = findViewById(R.id.matrix_recommend_layout_3);
		matrix_recommend_layout_4 = findViewById(R.id.matrix_recommend_layout_4);

		// 初始化焦点图Layout
		thumb_layout_0 = findViewById(R.id.thumb_layout_0);
		thumb_layout_1 = findViewById(R.id.thumb_layout_1);
		thumb_layout_2 = findViewById(R.id.thumb_layout_2);
		thumb_layout_3 = findViewById(R.id.thumb_layout_3);
		thumb_layout_4 = findViewById(R.id.thumb_layout_4);

		// 初始化推荐位的文本信息显示的TextView
		matrix_recommend_flipper_0_text_view = (MarqueeText) findViewById(R.id.matrix_recommend_flipper_0_text_view);
		matrix_recommend_flipper_1_text_view = (MarqueeText) findViewById(R.id.matrix_recommend_flipper_1_text_view);

		// 初始化切换推荐位的ViewFlipper
		matrix_recommend_flipper_0 = (ViewFlipper) findViewById(R.id.matrix_recommend_flipper_0);
		matrix_recommend_flipper_1 = (ViewFlipper) findViewById(R.id.matrix_recommend_flipper_1);

		// 初始化存储空间TextView
		tvInstallCount = (TextView) findViewById(R.id.matrix_recommend_install_count);
		tvInstallCount.setText(R.string.install_app_count_zero);// 初始化时隐藏占位符
		installMemory = (TextView) findViewById(R.id.matrix_recommend_memory_used);
		installMemoryBar = (ProgressBar) findViewById(R.id.matrix_recommend_memory_used_progress);

		recommend_usercenter_search = (ImageView) findViewById(R.id.recommend_usercenter_search);
		// 2015.10.12 修改为国内外都显示用户中心
		// if (AppConstant.ISDOMESTIC) {
		recommend_usercenter_search
				.setBackgroundResource(R.drawable.recommend_usercenter);
		// }// 国外显示搜索
		// else {
		// recommend_usercenter_search
		// .setBackgroundResource(R.drawable.matrix_recommend_search);
		// }
		// 推荐位0位置的焦点方向

		matrix_recommend_layout_0
				.setNextFocusUpId(R.id.navigation_tab_recommend_text);

		matrix_recommend_layout_0
				.setNextFocusRightId(R.id.matrix_recommend_layout_2);
		matrix_recommend_layout_0.setNextFocusLeftId(R.id.matrix_tv_layout_6);
		matrix_recommend_layout_0.setNextFocusDownId(R.id.thumb_layout_0);

		// 推荐位1位置的焦点方向
		matrix_recommend_layout_1
				.setNextFocusUpId(R.id.matrix_recommend_layout_4);
		matrix_recommend_layout_1
				.setNextFocusRightId(R.id.matrix_cinema_layout_6_new_v);
		matrix_recommend_layout_1
				.setNextFocusLeftId(R.id.matrix_recommend_layout_0);
		matrix_recommend_layout_1.setNextFocusDownId(matrix_recommend_layout_1
				.getId());

		// 推荐位2位置的焦点方向
		matrix_recommend_layout_2
				.setNextFocusUpId(R.id.navigation_tab_recommend_text);
		matrix_recommend_layout_2
				.setNextFocusRightId(R.id.matrix_recommend_layout_3);
		matrix_recommend_layout_2
				.setNextFocusLeftId(R.id.matrix_recommend_layout_0);
		matrix_recommend_layout_2
				.setNextFocusDownId(R.id.matrix_recommend_layout_4);

		// 推荐位3位置的焦点方向
		matrix_recommend_layout_3
				.setNextFocusUpId(R.id.navigation_tab_recommend_text);
		matrix_recommend_layout_3
				.setNextFocusRightId(R.id.matrix_cinema_layout_1_new_v);
		matrix_recommend_layout_3
				.setNextFocusLeftId(R.id.matrix_recommend_layout_2);
		matrix_recommend_layout_3
				.setNextFocusDownId(R.id.matrix_recommend_layout_4);

		// 推荐位4位置的焦点方向
		matrix_recommend_layout_4
				.setNextFocusUpId(R.id.matrix_recommend_layout_2);
		matrix_recommend_layout_4
				.setNextFocusRightId(R.id.matrix_cinema_layout_6_new_v);
		matrix_recommend_layout_4
				.setNextFocusLeftId(R.id.matrix_recommend_layout_0);
		matrix_recommend_layout_4
				.setNextFocusDownId(R.id.matrix_recommend_layout_1);

		// 缩略图位置0的Layout的焦点方向
		thumb_layout_0.setNextFocusUpId(R.id.matrix_recommend_layout_0);
		thumb_layout_0.setNextFocusRightId(R.id.thumb_layout_1);
		// thumb_layout_0.setNextFocusLeftId(R.id.matrix_tv_layout_7);
		thumb_layout_0.setNextFocusDownId(thumb_layout_0.getId());

		// 缩略图位置1的Layout的焦点方向
		thumb_layout_1.setNextFocusUpId(R.id.matrix_recommend_layout_0);
		thumb_layout_1.setNextFocusRightId(R.id.thumb_layout_2);
		thumb_layout_1.setNextFocusLeftId(R.id.thumb_layout_0);
		thumb_layout_1.setNextFocusDownId(thumb_layout_1.getId());

		// 缩略图位置2的Layout的焦点方向
		thumb_layout_2.setNextFocusUpId(R.id.matrix_recommend_layout_0);
		thumb_layout_2.setNextFocusRightId(R.id.thumb_layout_3);
		thumb_layout_2.setNextFocusLeftId(R.id.thumb_layout_1);
		thumb_layout_2.setNextFocusDownId(thumb_layout_2.getId());

		// 缩略图位置3的Layout的焦点方向
		thumb_layout_3.setNextFocusUpId(R.id.matrix_recommend_layout_0);
		thumb_layout_3.setNextFocusRightId(R.id.thumb_layout_4);
		thumb_layout_3.setNextFocusLeftId(R.id.thumb_layout_2);
		thumb_layout_3.setNextFocusDownId(thumb_layout_3.getId());

		// 缩略图位置4的Layout的焦点方向
		thumb_layout_4.setNextFocusUpId(R.id.matrix_recommend_layout_0);
		thumb_layout_4.setNextFocusRightId(R.id.matrix_recommend_layout_1);
		thumb_layout_4.setNextFocusLeftId(R.id.thumb_layout_3);
		thumb_layout_4.setNextFocusDownId(thumb_layout_4.getId());

		// 初始化推荐位ViewFliper
		flippers = new ViewFlipper[] { matrix_recommend_flipper_0,
				matrix_recommend_flipper_1 };

		for (int i = 0; i < flippers.length; i++) {
			Animation inAnimation = null;
			Animation outAnimation = null;
			if (i == 0) {
				inAnimation = AnimationUtils.loadAnimation(getContext(),
						R.anim.focus_fade_in);
				outAnimation = AnimationUtils.loadAnimation(getContext(),
						R.anim.focus_fade_out);
			} else {
				inAnimation = AnimationUtils.loadAnimation(getContext(),
						R.anim.down_in);
				outAnimation = AnimationUtils.loadAnimation(getContext(),
						R.anim.down_out);
			}

			inAnimation
					.setAnimationListener(new ViewFlipperInAnimationListener(
							flippers[i], i) {

						@Override
						public void onStartInAnimation(ViewFlipper viewFlipper,
								int viewFlipperIndexInPage) {
							// 得到当前推荐位对应的文本信息
							theFocusEntity = (RecommendEntity) flippers[viewFlipperIndexInPage]
									.getCurrentView().getTag();
							if (TextUtils.isEmpty(theFocusEntity
									.getContentDesc())) {
								desViews[viewFlipperIndexInPage]
										.setVisibility(View.INVISIBLE);
							} else {
								desViews[viewFlipperIndexInPage]
										.setVisibility(View.VISIBLE);
							}
							desViews[viewFlipperIndexInPage]
									.setText(theFocusEntity.getContentDesc());
							// setTextViewVaule(theFocusEntity);
							if (viewFlipperIndexInPage == 0) {

								int index = flippers[0].getDisplayedChild();
								for (int i = 0; i < thumbFades.length; i++) {
									thumbFades[i].setVisibility(View.VISIBLE);
								}
								thumbFades[index >= thumbFades.length ? (thumbFades.length - 1)
										: index].setVisibility(View.GONE);
							}
						}

						@Override
						public void onRepeatInAnimation(
								ViewFlipper viewFlipper,
								int viewFlipperIndexInPage) {

						}

						@Override
						public void onEndInAnimation(ViewFlipper viewFlipper,
								int viewFlipperIndexInPage) {

						}
					});
			flippers[i].setInAnimation(inAnimation);
			flippers[i].setOutAnimation(outAnimation);

		}

		// 推荐位焦点缩略图
		ImageView thumb_load_view_0 = (ImageView) findViewById(R.id.thumb_load_view_0);
		ImageView thumb_load_view_1 = (ImageView) findViewById(R.id.thumb_load_view_1);
		ImageView thumb_load_view_2 = (ImageView) findViewById(R.id.thumb_load_view_2);
		ImageView thumb_load_view_3 = (ImageView) findViewById(R.id.thumb_load_view_3);
		ImageView thumb_load_view_4 = (ImageView) findViewById(R.id.thumb_load_view_4);

		// 初始化焦点图图片的view
		thumbViews = new ImageView[] { thumb_load_view_0, thumb_load_view_1,
				thumb_load_view_2, thumb_load_view_3, thumb_load_view_4 };

		// 初始化焦点图图片的image
		thumbImages = new int[] { R.drawable.recommend_thumb1,
				R.drawable.recommend_thumb2, R.drawable.recommend_thumb3,
				R.drawable.recommend_thumb4, R.drawable.recommend_thumb5 };
		LargeImages = new int[] { R.drawable.recommend_large1,
				R.drawable.recommend_large2, R.drawable.recommend_large3,
				R.drawable.recommend_large4, R.drawable.recommend_large5 };
		largeTextStrings = new String[] { "实时演播", "新美力商城", "宣传片", "教学片", "" };

		// 初始化推荐位文本信息
		desViews = new MarqueeText[] { matrix_recommend_flipper_0_text_view,
				matrix_recommend_flipper_1_text_view };

		// 初始化焦点缩略图Layout数组
		thumbLayouts = new View[] { thumb_layout_0, thumb_layout_1,
				thumb_layout_2, thumb_layout_3, thumb_layout_4 };

		// 初始化焦点缩略图蒙版数组
		thumbFades = new View[] { findViewById(R.id.thumb_load_view_0_fade),
				findViewById(R.id.thumb_load_view_1_fade),
				findViewById(R.id.thumb_load_view_2_fade),
				findViewById(R.id.thumb_load_view_3_fade),
				findViewById(R.id.thumb_load_view_4_fade) };

		recommendEdgeViews = new View[] { matrix_recommend_layout_0,
				matrix_recommend_layout_2, matrix_recommend_layout_3,
				thumb_layout_0 };

		// 设置焦点缩略图焦点变化监听
		if (HomeActivity.isNeedChlitina) {
			for (int i = 0; i < thumbLayouts.length; i++) {
				thumbLayouts[i]
						.setOnFocusChangeListener(thumbLayoutFocusListener);
			}
			thumbLayouts[0].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(HiveviewApplication.mContext, 0 + "",
					// 1).show();
					AppUtil.openAppForPackageName("com.cisco.webex.meetings",
							getContext());
				}
			});
			thumbLayouts[1].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(HiveviewApplication.mContext, 1 + "", 1)
					// .show();
					Intent intent = new Intent();
					intent.setAction("com.hiveview.tv.home.webview");
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("ChlitinaUri",
							"http://mall.echlitina.com.cn/");
					HiveviewApplication.mContext.startActivity(intent);

				}
			});
			thumbLayouts[2].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(HiveviewApplication.mContext, 2 + "", 1)
					// .show();
					Intent intent = HiveviewApplication.mContext
							.getPackageManager().getLaunchIntentForPackage(
									"com.hiveview.premiere");
					intent.putExtra("ChlitinaType", 303);
					HiveviewApplication.mContext.startActivity(intent);
				}
			});
			thumbLayouts[3].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Toast.makeText(HiveviewApplication.mContext, 3 + "", 1)
					// .show();
					Intent intent = HiveviewApplication.mContext
							.getPackageManager().getLaunchIntentForPackage(
									"com.hiveview.premiere");
					intent.putExtra("ChlitinaType", 304);
					HiveviewApplication.mContext.startActivity(intent);
				}
			});
			// thumbLayouts[i].setOnClickListener(thumbLayoutClickListener);
			// thumbLayouts[4].setOnClickListener(new
			// ThumbLayoutClickListener());

		} else {
			for (int i = 0; i < thumbLayouts.length; i++) {
				thumbLayouts[i]
						.setOnFocusChangeListener(thumbLayoutFocusListener);
				// thumbLayouts[i].setOnClickListener(thumbLayoutClickListener);
				thumbLayouts[i]
						.setOnClickListener(new ThumbLayoutClickListener());
			}
		}
		// 初始化推荐位Layout
		recommendLayouts = new View[] { matrix_recommend_layout_0,
				matrix_recommend_layout_1, matrix_recommend_layout_2,
				matrix_recommend_layout_3, matrix_recommend_layout_4 };
		Log.d("MatrixRecomment", "recommendLayouts:::"
				+ recommendLayouts.length);
		// 设置各个推荐位上的焦点监听和单击监听
		if (HomeActivity.isNeedChlitina) {
			for (int i = 0; i < recommendLayouts.length; i++) {
				recommendLayouts[i]
						.setOnFocusChangeListener(recommendFocusListener);
			}
			recommendLayouts[0].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					RecommendEntity entity = (RecommendEntity) flippers[0]
							.getCurrentView().getTag();
					// Toast.makeText(HiveviewApplication.mContext,
					// entity.getContentDesc(), 1).show();
					if (entity.getContentDesc().equals("实时演播")) {
						// Intent intent = new Intent();
						// intent.setAction("com.hiveview.tv.home.webview");
						// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// intent.putExtra("ChlitinaUri",
						// "http://germes.gensee.com/training/site/s/39625343 ");
						// HiveviewApplication.mContext.startActivity(intent);
						AppUtil.openAppForPackageName(
								"com.cisco.webex.meetings", getContext());
					} else if (entity.getContentDesc().equals("新美力商城")) {
						Intent intent = new Intent();
						intent.setAction("com.hiveview.tv.home.webview");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("ChlitinaUri",
								"http://mall.echlitina.com.cn/");
						HiveviewApplication.mContext.startActivity(intent);
					} else if (entity.getContentDesc().equals("宣传片")) {
						Intent intent = HiveviewApplication.mContext
								.getPackageManager().getLaunchIntentForPackage(
										"com.hiveview.premiere");
						intent.putExtra("ChlitinaType", 303);
						HiveviewApplication.mContext.startActivity(intent);
					} else {
						if (entity.getContentDesc().equals("教学片")) {
							Intent intent = HiveviewApplication.mContext
									.getPackageManager()
									.getLaunchIntentForPackage(
											"com.hiveview.premiere");
							intent.putExtra("ChlitinaType", 304);
							HiveviewApplication.mContext.startActivity(intent);
						}
					}
				}
			});
		} else {
			for (int i = 0; i < recommendLayouts.length; i++) {
				recommendLayouts[i]
						.setOnFocusChangeListener(recommendFocusListener);
				// recommendLayouts[i].setOnClickListener(recommendLayoutClickListener);
				recommendLayouts[i]
						.setOnClickListener(new RecommendLayoutClickListener());
			}
		}
		// 设置推荐位功能按键单击监听
		matrix_recommend_layout_2
				.setOnClickListener(new tvRecordClickListener());
		matrix_recommend_layout_3
				.setOnClickListener(new tvSearchClickListener());
		matrix_recommend_layout_4.setOnClickListener(new AppStoreListener());

		// 监听大焦点图向按键事件，保持大焦点图向下按键时到对应的小焦点图
		matrix_recommend_layout_0.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				try {
					if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
							&& event.getAction() == KeyEvent.ACTION_DOWN) {
						int index = matrix_recommend_flipper_0
								.getDisplayedChild();
						view.setNextFocusDownId(thumbLayouts[index].getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		hashMap = new HashMap<View, String>();
		hashMap.put(thumb_layout_0, "3315");
		hashMap.put(thumb_layout_1, "3316");
		hashMap.put(thumb_layout_2, "3317");
		hashMap.put(thumb_layout_3, "3318");
		hashMap.put(thumb_layout_4, "3324");
		hashMap.put(matrix_recommend_layout_0, "0000");
		hashMap.put(matrix_recommend_layout_1, "3325");// 推荐位
		hashMap.put(matrix_recommend_layout_2, "3319");// 观看记录
		hashMap.put(matrix_recommend_layout_3, "3320");// 影片搜素
		hashMap.put(matrix_recommend_layout_4, "3321");// 已安装应用游戏

		hashMap1 = new HashMap<Integer, String>();
		hashMap1.put(0, "3301");
		hashMap1.put(1, "3302");
		hashMap1.put(2, "3303");
		hashMap1.put(3, "3304");
		hashMap1.put(4, "3305");

		super.init();

		// stopPageViewCustomAnimation();

	}

	private void setTextViewVaule(RecommendEntity entity) {
		switch (isViewFilperFocus) {
		case 0:
			desViews[0].setText(entity.getContentName());
			desViews[1].setText(entity.getContentName());
			break;
		case 1:
			desViews[0].setText(entity.getContentDesc());
			desViews[1].setText(entity.getContentName());
			break;
		case 2:
			desViews[0].setText(entity.getContentName());
			desViews[1].setText(entity.getContentName());
			break;
		case 3:
			desViews[1].setText(entity.getContentDesc());
			desViews[0].setText(entity.getContentName());
			break;
		case 4:
			desViews[1].setText(entity.getContentName());
			desViews[0].setText(entity.getContentName());
			break;
		default:
			desViews[0].setText(entity.getContentName());
			desViews[1].setText(entity.getContentName());
			break;
		}
	}

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class ThumbLayoutClickListener extends SimpleOnClickListener {

		@Override
		public DataHolder doOnClick(View view) {
			int index = -1;
			for (int i = 0; i < thumbLayouts.length; i++) {
				if (view == thumbLayouts[i]) {
					index = i;
				}
			}

			RecommendEntity entity = (RecommendEntity) thumbLayouts[index]
					.getTag();
			if (null == entity) {
				return null;
			}
			int typrIndex = 0;
			// 根据内容类型响应Action
			try {
				typrIndex = responseActionStartActivity(
						entity,
						(null != hashMap.get(view) ? hashMap.get(view) : "2222"));
			} catch (Exception e) {
				Log.d(TAG, "apk lost!!!!");
			}
			Log.d(VIEW_LOG_TAG, "viewPosition::"
					+ (null != hashMap.get(view) ? hashMap.get(view) : "2222"));
			return new DataHolder.Builder(getContext())
					.setDataType(
							null == dataType[typrIndex] ? DataType.CLICK_TAB_RECOMMEND
									: dataType[typrIndex])
					.setEntity(entity)
					.setSenceName(AppScene.getScene())
					.setSrcType(
							null == itemType[typrIndex] ? ItemType.VIDEO
									: itemType[typrIndex])
					.setTabNo(Tab.RECOMMEND)
					.setViewPosition(
							null != hashMap.get(view) ? hashMap.get(view)
									.toString() : "2222222").build();

		}
	}

	/**
	 * @author lihongji
	 * 
	 *         直播节目
	 */
	class GetTvList extends SafeRunnable {

		private int tvid;

		public GetTvList(int contentId) {
			// TODO Auto-generated constructor stub
			tvid = contentId;
		}

		@Override
		public void requestData() {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",
					Locale.CHINA);
			String date = dateFormat.format(new Date());
			liveEntity = new LiveStreamEntity();
			ArrayList<LiveStreamEntity> tvList = service.getTvList("1", "",
					date, date);
			if (tvList != null && tvList.size() != 0) {
				for (LiveStreamEntity entity : tvList) {
					if (tvid == entity.getTv_id()) {
						liveEntity = entity;
						sendLoadDataResultMessage(LOAD_TVLIST_SUCCESS);
					}
				}

			}
		}

		@Override
		public void processServiceException(ServiceException e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		}
	}

	// by zhangpengzhan
	HashMap<View, String> hashMap;
	HashMap<Integer, String> hashMap1;

	/**
	 * 响应点击事件 点击后进入 详情页
	 */
	class RecommendLayoutClickListener extends SimpleOnClickListener {

		@Override
		public DataHolder doOnClick(View view) {
			int index = -1;
			for (int i = 0; i < recommendLayouts.length; i++) {
				if (view == recommendLayouts[i]) {
					index = i;
				}
			}
			View v = (View) view;

			// 获取推荐位绑定的数据
			RecommendEntity entity = null;

			/* start by ZhaiJianfeng */
			/* 暂时去掉视频通话占位，改为推荐位 */
			/*
			 * if (index == 5) {// 打开视频通话 //
			 * AppUtil.openApp("com.lenovo.videotalk", getContext());
			 * ToastUtils.alertShow(getContext(), "暂未开放，敬请期待"); return new
			 * DataHolder
			 * .Builder(getContext()).setDataType(DataType.CLICK_TAB_RECOMMEND
			 * ).setEntity(entity)
			 * .setSenceName(AppScene.getScene()).setSrcType(
			 * ItemType.VIDEO).setTabNo
			 * (Tab.RECOMMEND).setViewPosition("3310").build(); }
			 */
			/* end by ZhaiJianfeng */
			if (flippers[index].getCurrentView() == null
					|| flippers[index].getChildCount() == 0) {
				return null;
			}
			entity = (RecommendEntity) flippers[index].getCurrentView()
					.getTag();
			if (null == entity)
				return null;
			int typrIndex = 0;
			// 根据内容类型响应Action
			try {
				typrIndex = responseActionStartActivity(entity,
						(null != hashMap.get(v) ? hashMap.get(v).toString()
								: "222"));
			} catch (Exception e) {
				Log.d(TAG, "apk lost!!!!");
			}

			Log.d(VIEW_LOG_TAG, ""
					+ (null != hashMap.get(v) ? hashMap.get(v).toString()
							: "222") + "==map.size::" + hashMap.size());
			return new DataHolder.Builder(getContext())
					.setDataType(
							null == dataType[typrIndex] ? DataType.CLICK_TAB_RECOMMEND
									: dataType[typrIndex])
					.setEntity(entity)
					.setSenceName(AppScene.getScene())
					.setViewPosition(
							null != hashMap.get(v) ? (hashMap.get(v).equals(
									"0000") ? hashMap1.get(flippers[index]
									.getDisplayedChild()) : hashMap.get(v)
									.toString()) : "00")
					.setSrcType(
							null == itemType[typrIndex] ? ItemType.VIDEO
									: itemType[typrIndex])
					.setTabNo(Tab.RECOMMEND).build();

		}

	}

	// end by zhangpenzhan
	/**
	 * 根据推荐位内容类型，响应不同的不同的Action，启动不同的Activity
	 * 
	 * @Title: MatrixRecommendView
	 * @author:陈丽晓
	 * @Description:
	 * @param entity
	 *            推荐位内容类型
	 * @param positionid
	 */
	private int responseActionStartActivity(RecommendEntity entity,
			String positionid) {
		// 获取当前数据类型对应的详情Activity的Action
		String action = ContentInvoker.getInstance().getContentAction(
				entity.getFocusType());
		// logic:执行逻辑1：正常执行 2.调用新闻apk 取消使用，替换为存数据库
		String openApp = sharedPreferences.getString(
				String.valueOf(entity.getContentId()), "1");
		if(HomeActivity.isBesTV&&positionid.equals("3325")){//百视通写死
			entity.setIsApk("0");
			entity.setApkPackage("com.bestv.ott.baseservices");
		}
		Log.d(TAG, "openApp::" + openApp);
		if (openNewsApp.equals(entity.getIsApk())) {
			Log.d(TAG, "openApp::true");
			AppUtil.openAppForPackageName(entity.getApkPackage(), getContext());
		} else if (!TextUtils.isEmpty(action)) {// 有详情的Action
			if (action.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_HALL)// 极清2.0影厅
					|| action
							.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_SUBJECT)// 极清2.0专题
					|| action
							.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_ALBUM)) {// 极清2.0专辑
				if(!AppConstant.ISDOMESTIC){
				// ==============================================start_lihongji
					// 调到极清点播详情页
					try {
						Intent it = new Intent();
						ComponentName componentName = new ComponentName(
								"com.hiveview.bluelight",
								"com.hiveview.bluelight.activity.BlueLightActivity");
						it.setComponent(componentName);
						it.putExtra("page",
								"com.hiveview.bluelight.page.DispatchPage");
						it.putExtra("contentId", entity.getContentId() + "");
						it.putExtra("contentType", entity.getFocusType() + "");
						Log.d(TAG,
								"contentId=====" + entity.getContentId()
										+ "||getFocusType========="
										+ entity.getFocusType());
						getContext().startActivity(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
				// ==============================================end_lihongji
				}else{
					try {
						Intent it = new Intent();
						ComponentName componentName = new ComponentName("com.hiveview.bluelight", "com.hiveview.bluelight.activity.BlueLightActivity");
						it.setComponent(componentName);
						it.putExtra("page", "com.hiveview.bluelight.page.DispatchPage");
						it.putExtra("contentId", entity.getContentId() + "");
						it.putExtra("contentType", entity.getFocusType() + "");
						Log.d(TAG, "contentId=====" + entity.getContentId() + "||getFocusType=========" + entity.getFocusType() );
						getContext().startActivity(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}else if(action
					.equals(ContentInvoker.CONTENT_ACTION_BLUE_2_FORSALES)){
				try {
					Intent it = new Intent();
					ComponentName componentName = new ComponentName(
							"com.hiveview.bluelight",
							"com.hiveview.bluelight.activity.BlueLightActivity");
					it.setComponent(componentName);
					it.putExtra("page",
							"com.hiveview.bluelight.page.DispatchPage");
					it.putExtra("contentId", entity.getCpId() + "");
					it.putExtra("contentType", entity.getFocusType()
							+ "");
					Log.d(TAG,
							"cp=====" + entity.getCpId()
									+ "||getFocusType========="
									+ entity.getFocusType());
					getContext().startActivity(it);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			else if (action.equals(ContentInvoker.CONTENT_ACTION_ONLIVE)) {
				
				if(!AppConstant.ISDOMESTIC){
					// 调到极清直播详情页
					try {
						Intent it = new Intent();
						ComponentName componentName = new ComponentName(
								"com.hiveview.bluelight",
								"com.hiveview.bluelight.activity.BlueLightActivity");
						it.setComponent(componentName);
						it.putExtra("page",
								"com.hiveview.bluelight.page.DispatchPage");
						it.putExtra("contentId", entity.getContentId() + "");
						it.putExtra("cpId", entity.getCpId()+ "");
						it.putExtra("contentType", entity.getFocusType()+"");
						getContext().startActivity(it);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (action
					.equals(ContentInvoker.CONTENT_ACTION_BLUE__PREMIERE)) {// 极清首映类型
				Log.v(TAG,
						"CONTENT_ACTION_BLUE__PREMIERE=="
								+ entity.getContentId());
				Intent intent = getContext().getPackageManager()
						.getLaunchIntentForPackage("com.hiveview.premiere");
				intent.putExtra("page_id", 103);
				intent.putExtra("id", entity.getContentId());
				getContext().startActivity(intent);
			} else if (action.equals(ContentInvoker.CONTENT_ACTION_BLUE)) {// 蓝光极清类型的视频
				Intent intent = new Intent(action);
				intent.putExtra("videoset_id", entity.getContentId());
				getContext().sendBroadcast(intent);
				return 0;
			} else if (action
					.equals(ContentInvoker.CONTENT_ACTION_BLUE_SUBJECT_DETAIL)) {// 极清专题详情
				Intent intent = new Intent(action);
				intent.putExtra("subject_id", entity.getContentId());
				getContext().sendBroadcast(intent);
				return 4;
			} else if (action.equals(ContentInvoker.CONTENT_ACTION_APP_DETAIL)
					|| action.equals(ContentInvoker.CONTENT_ACTION_APP_ALBUM)) {
				Intent intent = new Intent(action);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				if (action.equals(ContentInvoker.CONTENT_ACTION_APP_ALBUM)) {// 应用专题的Action
					intent.putExtra("subject_id", entity.getContentId());
					intent.putExtra("subjectPositionId", positionid);
					if (entity.getFocusType() == AppConstant.APP_TYPE_ALBUM) {
						intent.putExtra("categoryId", "2");
					} else if (entity.getFocusType() == AppConstant.GAME_TYPE_ALBUM) {
						intent.putExtra("categoryId", "1");
					} else if (entity.getFocusType() == AppConstant.EDU_TYPE_ALBUM) {
						intent.putExtra("categoryId", "3");
					}
					getContext().startActivity(intent);
					return 2;
				} else {// 应用详情的Action
					intent.putExtra("appid", entity.getContentId());
					getContext().startActivity(intent);
				}
				return 1;
			}

			else if (action.equals(ContentInvoker.CONTENT_ACTION_MOVIESUBJECT)) {// 专题
				SubjectListEntity mSubjectEntity = new SubjectListEntity();
				mSubjectEntity.setImgSize(entity.getImgSize());
				mSubjectEntity.setSubjectBgImg(entity.getSubjectBgImg());
				mSubjectEntity.setSubjectDesc(entity.getContentDesc());
				mSubjectEntity.setSubjectId(entity.getContentId());
				mSubjectEntity.setSubjectName(entity.getContentName());
				mSubjectEntity.setSubjectPic(entity.getFocusThumbImg());
				Log.v("action",
						"CONTENT_ACTION_MOVIESUBJECT1"
								+ mSubjectEntity.toString());
				String sizeInfo = mSubjectEntity.getImgSize();
				Log.v("action", "CONTENT_ACTION_MOVIESUBJECT2" + sizeInfo);
				// 进入详情页图面的url，不等于空， 长度大于0，包含有x字符
				if (null != sizeInfo && sizeInfo.length() > 0
						&& sizeInfo.contains("x")) {
					LogUtil.info(sizeInfo);
					String[] sizeArray = sizeInfo.split("x");
					int width = Integer.parseInt(sizeArray[0]);
					int height = Integer.parseInt(sizeArray[1]);
					Intent intent = new Intent();
					intent.putExtra("entity", mSubjectEntity);
					if (width > height) {
						Log.v("action", "CONTENT_ACTION_MOVIESUBJECT3");
						intent.setClass(getContext(),
								SubjectDetailLandspaceActivity.class);
					} else {
						Log.v("action", "CONTENT_ACTION_MOVIESUBJECT4");
						intent.setClass(getContext(),
								SubjectDetailPortraitActivity.class);
					}

					getContext().startActivity(intent);
					return 0;
				}

			} else if (action.equals(ContentInvoker.CONTENT_ACTION_MOVIEDETAIL)) {
				// 获取奇艺当前数据类型对应的详情Activity的Action
				String action_QIYI = null;
				if (!TextUtils.isEmpty(entity.getShowType())) {
					if (entity.getVideoId() > 0) {
						action_QIYI = null;
					} else {
						action_QIYI = ContentInvoker.getInstance()
								.getContentAction(
										Integer.parseInt(entity.getShowType()));
					}
				}
				Log.v("action", "entity.getShowType()" + entity.getShowType()
						+ "action_QIYI" + action_QIYI);
				if (!TextUtils.isEmpty(action_QIYI)) {// 奇艺，搜狐的数据并且有详情页视频，如电影，电视剧，综艺，动漫，
					Intent intent = new Intent(action_QIYI);
					intent.putExtra("id", entity.getContentId());
					intent.putExtra("type", entity.getShowType());
					intent.putExtra("source", AppConstant.SOURCE_ROMMAND);
					getContext().startActivity(intent);

					return 0;
				} else {
					/* start by huzuwei */
					String videoid = String.valueOf(entity.getVideoId());
					// 其中这个showtype其实就是频道列表classfirstType也相当与cid,这个是单片无详情的直接播放
					PlayerParamsUtils.getVideoPlayParams(entity.getContentId(),
							Integer.valueOf(entity.getShowType()), videoid,
							getContext(), null);
					return 0;
					/* end by huzuwei */
				}
			} else {// 无详情页的视频（如：音乐，体育，片花，旅游记录片等），点击直接播放就行
				Intent intent = new Intent(getContext(), VideoService.class);
				intent.putExtra("id", entity.getContentId());
				intent.putExtra("type", entity.getShowType());
				getContext().startService(intent);
				BaseActivity.player_Entity = entity;
				isPlayer = true;
				KeyEventHandler.post(new DataHolder.Builder(getContext())
						.setTabNo(Tab.TAB).setViewPosition("0306")
						.setSource(AppConstant.SOURCE_ROMMAND)
						.setEntity((RecommendEntity) entity)
						.setDataType(DataType.CLICK_TAB_RECOMMEND)
						.setSrcType(ItemType.VIDEO).build());
				return 0;
			}

		}

		return 0;
	}

	/**
	 * 推荐位的焦点变化监听
	 */
	private OnFocusChangeListener recommendFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			if (isFocused) {
				if (view.equals(matrix_recommend_layout_0)) {
					isViewFilperFocus = 1;
					desViews[0].setStart(true);
					// setTextViewVaule(theFocusEntity);
				} else if (view.equals(matrix_recommend_layout_1)) {
					isViewFilperFocus = 3;
					desViews[1].setStart(true);
					// setTextViewVaule(theFocusEntity);
				}
				Log.d(TAG, "OnFocusChangeListener->isViewFilperFocus::"
						+ isViewFilperFocus);
				view.bringToFront();
				/*
				 * if (view.getId() == R.id.matrix_recommend_layout_2) {//
				 * 可露头的推荐位，动画要特殊处理
				 * findViewById(R.id.matrix_recommend_flipper_2_out_layout
				 * ).bringToFront();
				 * AnimationUtil.loadBigAnimation(findViewById(
				 * R.id.matrix_recommend_flipper_2_out_layout)); }
				 */

				AnimationUtil.loadBigAnimation(view);// 放大动画

				AnimationUtil.loadBigAnimation(view);
				AlphaAnimation alphaAnim = new AlphaAnimation(0.9f, 1.0f);
				alphaAnim.setDuration(100);
				alphaAnim.setInterpolator(new LinearInterpolator());
				view.startAnimation(alphaAnim);
				Log.v(TAG, "mcurrentfocus=" + view.getId());
				HiveviewApplication.mcurrentfocus = view;
			} else {
				if (view.equals(matrix_recommend_layout_0)) {
					isViewFilperFocus = 2;
					desViews[0].setStart(false);
					// setTextViewVaule(theFocusEntity);
				} else if (view.equals(matrix_recommend_layout_1)) {
					isViewFilperFocus = 4;
					desViews[1].setStart(false);
					// setTextViewVaule(theFocusEntity);
				}
				AnimationUtil.loadSmallAnimation(view);
				/*
				 * if (view.getId() == R.id.matrix_recommend_layout_2) {//
				 * 可露头的推荐位失去焦点缩小处理
				 * AnimationUtil.loadSmallAnimation(findViewById(
				 * R.id.matrix_recommend_flipper_2_out_layout)); }
				 */
			}

		}
	};;

	/**
	 * 焦点图缩略图焦点的变化监听
	 */
	private OnFocusChangeListener thumbLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			int index = -1;
			for (int i = 0; i < thumbLayouts.length; i++) {
				thumbFades[i].setVisibility(View.VISIBLE);
				if (thumbLayouts[i] == view) {
					index = i;// 设置蒙版隐藏的位置
					thumbFades[i].setVisibility(View.GONE);
				}
			}

			if (flippers[0].getChildCount() == 0)
				return;

			if (isFocused) {
				if (flippers[0].getDisplayedChild() != index) {
					flippers[0].setDisplayedChild(index);// 设置焦点大图在ViewFliper中轮播
				}
				Log.v(TAG, "mcurrentfocus=" + view.getId());
				HiveviewApplication.mcurrentfocus = view;
				thumbFades[index].setVisibility(View.GONE);// 对应位置的缩略图焦点隐藏
				RecommendEntity entity = (RecommendEntity) flippers[0]
						.getCurrentView().getTag();
				desViews[0].setText(entity.getContentDesc());
				flippers[0].stopFlipping();

			} else {
				flippers[0].startFlipping();
				for (int k = 0; k < flippers[0].getChildCount(); k++) {
					flippers[0].getChildAt(k).clearAnimation();
				}
			}
		}
	};

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		case LOAD_TVLIST_SUCCESS:
			try {
				Intent intent = new Intent();
				ComponentName cn = new ComponentName(
						"com.hiveview.cloudscreen.videolive",
						"com.hiveview.cloudscreen.videolive.MainActivity");
				intent.setComponent(cn);
				intent.putExtra(MatrixTVForeidgnView.EXTRA_URL,
						liveEntity.getLiveurl());
				intent.putExtra(MatrixTVForeidgnView.EXTRA_TV_ID,
						liveEntity.getTv_id());
				intent.putExtra(MatrixTVForeidgnView.EXTRA_CP,
						liveEntity.getCp());
				intent.putExtra(MatrixTVForeidgnView.EXTRA_CP_ID,
						liveEntity.getCpId());
				intent.putExtra(MatrixTVForeidgnView.EXTRA_IS_VIP,
						liveEntity.getIsVip());
				HiveviewApplication.mContext.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case LOAD_DATA_SUCCESS:
			Log.d(TAG, "LOAD_DATA_SUCCESS");
			setMatrixData();
			int flipersLength = flippers.length;
			// 推荐位只有一个推荐图的时候，动画不能执行，特此在这里加载一遍
			for (int i = 0; i < flipersLength; i++) {
				try {
					// 该情况只是应对只有一个推荐位的问题
					if (flippers[i].getChildCount() == 1) {
						RecommendEntity entity = (RecommendEntity) flippers[i]
								.getCurrentView().getTag();
						if (TextUtils.isEmpty(entity.getContentDesc())) {
							desViews[i].setVisibility(View.INVISIBLE);
						} else {
							desViews[i].setVisibility(View.VISIBLE);
							desViews[i].setText(entity.getContentDesc());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			break;
		case LOAD_DATA_FAIL:// 请求数据失败，重新去请求数据
			Log.d(TAG, "LOAD_DATA_FAIL");
			// Intent intentService = new Intent(getContext(),
			// LoadService.class);
			// intentService.putExtra("isNeedDeviceCheck", true);
			// getContext().startService(intentService);
			// 因为加载数据失败，这时候只能显示只有默认图的Launcher了
			getContext()
					.sendBroadcast(
							new Intent(
									HomeActions.ACTION_RECOMMEND_LARGE_LOAD_COMPLETE));
			// activityHandler.sendEmptyMessage(HomeActivity.RECOMMEND_DATA_REFLESH_SUCCESS);
			break;
		default:
			break;
		}
	}

	@Override
	public void loadData(boolean isReflesh) {
		super.loadData(isReflesh);
		// Log.d(TAG, "before loadData loadImage : " +
		// (System.currentTimeMillis() - time) + "  " +
		// System.currentTimeMillis());
		this.setVisibility(View.VISIBLE);
		int count = 0;
		// Log.d(TAG, "before while loadImage" + (System.currentTimeMillis() -
		// time) + "  " + System.currentTimeMillis());
		do {
			recommends = recommendDAO.queryMatrix(null, BaseDAO.MATRIX_TYPE
					+ " = " + BaseDAO.MATRIX_RECOMMEND, null,
					"position limit " + 5);
			Log.v(TAG, recommends.toString());
			if (recommends.get(0).size() > 0 || count == readDataFromDBCount) {
				break;
			}
			count++;
			try {
				Thread.sleep(readDataFromDBInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (recommends.get(0).size() == 0);

		// Log.d(TAG, "after while loadImage" + (System.currentTimeMillis() -
		// time) + "  " + System.currentTimeMillis());
		if (null != recommends && recommends.get(0).size() > 0) {
			checkPageIsIdle();
		} else {
			sendLoadDataResultMessage(LOAD_DATA_FAIL);
		}
		// Log.d(TAG, "after checkPageIsIdle or sendMessage loadImage" +
		// (System.currentTimeMillis() - time) + "  " +
		// System.currentTimeMillis());
	}

	@Override
	public void updateUIRefleshData() {
		sendLoadDataResultMessage(LOAD_DATA_SUCCESS);
	}

	/**
	 * 把各个推荐的数据设置到推荐位上
	 * 
	 * @param recommends
	 */
	private void setMatrixData() {
		try {
			getInstallAppCount(true);// 加载安装应用按钮
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Log.d(TAG, "setMetrixData loadImage : " + (System.currentTimeMillis()
		// - time));
		try {
			int count = RECOMMEND_FOCUS_COUNT;// recommends.size();
			Log.d(TAG, count < AppConstant.NO_5 ? "setMatrixData has return!!!"
					: "setMatrixData is go on!!!");
			if (count < AppConstant.NO_2)
				return;

			android.view.ViewGroup.LayoutParams params = new android.view.ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			for (int i = 0; i < count; i++) {
				ArrayList<RecommendEntity> subList = recommends.get(i);// 获取每个推荐位上的数据列表
				for (int l = 0; l < recommends.get(0).size(); l++) {
					Log.v("subList.toString", recommends.get(0).get(l)
							.toString());
				}
				Log.v("subList", "subList.count===" + count);
				/*
				 * int size = subList.size(); int dataSize = subList.size();
				 */
				int size = subList.size();
				int dataSize = subList.size();
				int viewSize = flippers[i].getChildCount();
				if(HomeActivity.isBesTV&&i==1){
					size=1;
					dataSize=1;
				}
				if (dataSize > viewSize) {// 当推荐位数据增加时，补充显示推荐位的View
					Log.d(TAG, "recommend pageview " + i
							+ " position recommend data add");
					for (int j = 0; j < dataSize; j++) {
						RecommendEntity entity = subList.get(j);
						if (j < viewSize) {// 判断现有的View推荐位是否满足显示的内容类型
							checkRecommendMatchView(i, j, entity);
						} else {// 根据内容类型，增加新增的推荐位View
							if (entity.getFocusType() != AppConstant.APP_TYPE_DETAIL
									&& entity.getFocusType() != AppConstant.GAME_TYPE_DETAIL
									&& entity.getFocusType() != AppConstant.EDU_TYPE_DETAIL) {// 视频推荐位
								ImageView videoView = new ImageView(
										getContext());
								flippers[i].addView(videoView, params);
							} else if (entity.getFocusType() == AppConstant.APP_TYPE_DETAIL
									|| entity.getFocusType() == AppConstant.GAME_TYPE_DETAIL
									|| entity.getFocusType() == AppConstant.EDU_TYPE_DETAIL) {// 应用推荐位
								AppRecommendView appView = new AppRecommendView(
										getContext());
								flippers[i].addView(appView);
							}
						}
					}
				} else if (dataSize < viewSize) {// 当推荐位数据减少时，减少显示推荐位的View
					Log.d(TAG, "recommend pageview " + i
							+ " position recommend data reduce");
					for (int j = 0; j < viewSize; j++) {
						if (j < dataSize) {// 判断现有的View推荐位是否满足显示的内容类型
							RecommendEntity entity = subList.get(j);
							checkRecommendMatchView(i, j, entity);
						} else {// 根据内容类型，删除推荐位
							if (null != flippers[i]
									&& flippers[i].getChildCount() > 0) {
								flippers[i].removeViewAt(j);
							}
						}
					}
				} else if (dataSize == viewSize) {// 当轮播推荐位个数没有变化时，其内容次序可能发生变化，也需要验证其View能支持内容显示
					for (int j = 0; j < viewSize; j++) {
						RecommendEntity entity = subList.get(j);
						checkRecommendMatchView(i, j, entity);
					}
				}

				// 改变推荐位的轮播状态
				if (subList.size() > 0) {
					int sizes=0;
					if(HomeActivity.isBesTV&&i==1){
						sizes=1;
					}else{
						sizes	=subList.size();
					}
					changeFlipperFlipping(sizes, flippers[i], subList
							.get(0).getIntervalTime());
				}

				for (int k = 0; k < size; k++) {// 填充轮播推荐位数据
					RecommendEntity newEntity = subList.get(k);
					if (HomeActivity.isNeedChlitina) {
						if (newEntity.getPositionId().equals("1") && k <= 4) {
							newEntity.setContentDesc(largeTextStrings[k]);
						} else {
							newEntity.setContentDesc("");
						}
					}else if (HomeActivity.isBesTV) {
						if (newEntity.getPositionId().equals("2")){
							//newEntity.setContentDesc("百视通");
							newEntity.setContentDesc("");
						}
					}
					RecommendEntity oldEntity = null;
					ImageView imageView = null;
					boolean isNeedFillData = false;
					View childView = null;
					if (newEntity.getFocusType() == AppConstant.APP_TYPE_DETAIL
							|| newEntity.getFocusType() == AppConstant.GAME_TYPE_DETAIL
							|| newEntity.getFocusType() == AppConstant.EDU_TYPE_DETAIL) {// 应用类型的推荐位
						AppRecommendView recommendView = (AppRecommendView) flippers[i]
								.getChildAt(k);
						if (null != recommendView.getTag()) {
							oldEntity = (RecommendEntity) recommendView
									.getTag();
						}
						Log.v(TAG, "i1=" + i + ";k=" + k);
						childView = recommendView;
						imageView = recommendView.getImageView();
						recommendView.initProgressView(flippers[i]
								.getMeasuredWidth() - 50);
						mapDownload
								.put(newEntity.getContentId(), recommendView);
					} else {// 视频类型的推荐位
						imageView = (ImageView) flippers[i].getChildAt(k);
						imageView.setScaleType(ScaleType.FIT_XY);
						Log.v(TAG, "i2=" + i + ";k=" + k);
						if (null != imageView.getTag()) {
							oldEntity = (RecommendEntity) imageView.getTag();
						}
						childView = imageView;
					}

					if (null == oldEntity
							|| oldEntity.getContentId() != newEntity
									.getContentId()
							|| !oldEntity.getFocusLargeImg().equals(
									newEntity.getFocusLargeImg())) {
						childView.setTag(newEntity);
						isNeedFillData = true;
					}

					if (isNeedFillData) {

						if (HomeActivity.isNeedChlitina) {//克丽提哪
							if (newEntity.getPositionId().equals("1") && k <= 4) {
								imageView.setImageResource(LargeImages[k]);
							} else {
								// ImageLoader.getInstance().displayImage(
								// newEntity.getFocusLargeImg(),
								// imageView, options);
								imageView
										.setImageResource(R.drawable.recommend_layout_image1);
							}

						} else if (HomeActivity.isBesTV) {//百事通
							if (newEntity.getPositionId().equals("1") && k <= isLoadFinishArray.length - 1) {
								listenImageLoad(imageView,
										newEntity.getFocusLargeImg(), k);
							} if (newEntity.getPositionId().equals("2")){
								imageView
								.setImageResource(R.drawable.recommend_layout_image1_bestv);
							}else {
								 ImageLoader.getInstance().displayImage(
								 newEntity.getFocusLargeImg(),
								 imageView, options);
							}

						} else {//正常
							if (newEntity.getPositionId().equals("1")
									&& k <= isLoadFinishArray.length - 1) {// 监听图片加载
								listenImageLoad(imageView,
										newEntity.getFocusLargeImg(), k);
							} else {// 加载显示图片
								ImageLoader.getInstance().displayImage(
										newEntity.getFocusLargeImg(),
										imageView, options);
							}
						}
						if (HomeActivity.isNeedChlitina) {
							if (newEntity.getPositionId().equals("1") && k <= 4) { // 在第0个推荐位设置5个焦点图信息
								thumbViews[k].setScaleType(ScaleType.FIT_XY);
								thumbViews[k].setImageResource(thumbImages[k]);
								thumbLayouts[k].setTag(newEntity);
							}

						} else {
							if (newEntity.getPositionId().equals("1") && k <= 4) { // 在第0个推荐位设置5个焦点图信息
								thumbViews[k].setScaleType(ScaleType.FIT_XY);
								ImageLoader.getInstance().displayImage(
										newEntity.getFocusThumbImg(),
										thumbViews[k], options);
								thumbLayouts[k].setTag(newEntity);
							}
						}
						if (k == 0) {// 默认设置轮播位的文本标题显示

							desViews[i].setText(newEntity.getContentDesc());
						}
					}

				}

			}

			// 第一个大焦点图的轮播位是否轮播
			boolean firstFilpperIsFlip = false;
			for (int j = 0; j < thumbLayouts.length; j++) {
				if (thumbLayouts[j].isFocused()) {// 焦点图在缩略图上，大轮播位是不轮播的
					firstFilpperIsFlip = true;
				}
			}

			if (firstFilpperIsFlip) {// 停止轮播
				flippers[0].stopFlipping();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * 检测同一位置轮播推荐位内容与View是否匹配，如果不匹配进行替换
	 * 
	 * @Title: MatrixRecommendView
	 * @author:陈丽晓
	 * @Description:
	 * @param flipperIndex
	 *            轮播Flipper轮播推荐位在整个页面的位置
	 * @param recommendIndex
	 *            在单个Flipper轮播推荐位中推荐位索引
	 * @param entity
	 *            在单个推荐位显示的数据
	 */
	int flipperViewIndex = 0;

	private void checkRecommendMatchView(int flipperIndex, int recommendIndex,
			RecommendEntity entity) {
		View view = flippers[flipperIndex].getChildAt(recommendIndex);

		if (entity.getFocusType() != AppConstant.APP_TYPE_DETAIL
				&& entity.getFocusType() != AppConstant.GAME_TYPE_DETAIL
				&& entity.getFocusType() != AppConstant.EDU_TYPE_DETAIL) {// 此推荐位内容类型是视频类型
			if (!(view instanceof ImageView)) {// 此推荐位显示内容由应用类型改变为视频类型
				ImageView videoView = new ImageView(getContext());
				flippers[flipperIndex].removeViewAt(recommendIndex);
				flippers[flipperIndex].addView(videoView, recommendIndex);
				Log.d("MatrixR==>checkRecommendMatchView",
						"recommendIndexrecommendIndex:::" + recommendIndex);
			}
		} else if (entity.getFocusType() == AppConstant.APP_TYPE_DETAIL
				|| entity.getFocusType() == AppConstant.GAME_TYPE_DETAIL
				|| entity.getFocusType() == AppConstant.EDU_TYPE_DETAIL) {// 此推荐位内容类型是应用类型
			if (!(view instanceof AppRecommendView)) {// 此推荐位显示内容由视频类型改变为应用类型
				AppRecommendView appView = new AppRecommendView(getContext());
				flippers[flipperIndex].removeViewAt(recommendIndex);
				flippers[flipperIndex].addView(appView, recommendIndex);
			}
		}
	}

	private long time = System.currentTimeMillis();

	/**
	 * 监听大焦点图第三张的加载，加载完成时候发送一个广播，使主页面显示
	 * 
	 * @Title: MatrixRecommendView
	 * @author:陈丽晓
	 * @Description:
	 * @param imageView
	 * @param url
	 */
	private void listenImageLoad(ImageView imageView, String url,
			final int largeFocusIndex) {
		Log.d(TAG,
				"before listenImageLoad & loadImage : "
						+ (System.currentTimeMillis() - time) + "  "
						+ imageView.getMeasuredHeight() + "x"
						+ imageView.getMeasuredWidth());
		// ImageLoader.getInstance().displayImage(url, imageView, options, new
		// ListenImageLoad(largeFocusIndex));
		ImageLoader.getInstance().displayImage(url, imageView, options,
				new ListenImageLoad(largeFocusIndex),
				new ImageLoadingProgressListener() {

					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {
						Log.d(TAG, "onProgressUpdate loadImage : total : "
								+ total + "  current : " + current);
					}
				});
	}

	private Set<String> imageUris = new HashSet<String>();
	private Map<String, ImageViewSize> imageSizes = new HashMap<String, ImageViewSize>();

	class ListenImageLoad implements ImageLoadingListener {

		private int arrayIndex = 0;

		public ListenImageLoad(int index) {
			arrayIndex = index;
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			Log.d(TAG,
					"onLoadingStarted loadImage"
							+ (System.currentTimeMillis() - time));
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			Log.d(TAG,
					"onLoadingFailed loadImage"
							+ (System.currentTimeMillis() - time));
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {

			imageUris.add(imageUri);
			ImageViewSize size = new ImageViewSize();
			ImageViewAware aware = new ImageViewAware((ImageView) view);
			size.height = aware.getHeight();
			size.width = aware.getWidth();
			imageSizes.put(imageUri, size);

			isLoadFinishArray[arrayIndex] = true;
			boolean isAllLoadComplete = true;
			for (int i = 0; i < isLoadFinishArray.length; i++) {
				if (!isLoadFinishArray[i]) {
					isAllLoadComplete = false;
				}
			}

			if (isAllLoadComplete) {
				// getContext().sendBroadcast(new
				// Intent(HomeActions.ACTION_RECOMMEND_LARGE_LOAD_COMPLETE));
				dataCompleted.onCompleted();
				// activityHandler.sendEmptyMessage(HomeActivity.RECOMMEND_DATA_COMPLETED);
				Log.d(TAG, "before onLoadingComplete loadImage"
						+ " system time : " + System.currentTimeMillis());
				SharedPreferences preferences = getContext()
						.getSharedPreferences(AppConstant.PRE_LOAD_IMAGE,
								Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.clear();
				editor.putStringSet(AppConstant.PRE_LOAD_IMAGE_KEY, imageUris);
				for (Map.Entry<String, ImageViewSize> entry : imageSizes
						.entrySet()) {
					Set<String> set = new TreeSet<String>();
					set.add(entry.getValue().height + "");
					set.add(entry.getValue().width + "");
					editor.putStringSet(entry.getKey(), set);
				}
				editor.commit();
			}
			Log.d(TAG,
					"after onLoadingComplete  loadImage : "
							+ (System.currentTimeMillis() - time) + "  "
							+ System.currentTimeMillis());
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {

		}

	}

	/**
	 * 得到应用在盒子上的安装信息以及存储空间信息
	 */
	public void getInstallAppCount(final boolean isAnim) {
		// Log.d(TAG, "getInstallAppCount installAppCount=========" +
		// installAppCount);
		HttpTaskManager.getInstance().submit(new Runnable() {

			@Override
			public void run() {
				try {
					if (!isInitAppCount) {
						InitAppCountUtils initAppCount = new InitAppCountUtils(
								getContext());
						installAppCount = initAppCount.getInstallAppsByList(
								getContext()).size();
						isInitAppCount = true;
					}

					// 根据当前用户使用的盒子，适配获取存储空间信息
					StorageAdapter adapter = new StorageAdapter(DeviceSN
							.getDevice());
					GetStorageListener storageListener = adapter
							.getStorageInfo();

					if (null != storageListener) {
						Object[] objects = storageListener
								.getStorageInfo(getContext());
						usageRate = (Integer) objects[1];
						storageInfo = (String) objects[0];
						Log.d(TAG, "storageInfo1::" + storageInfo);
						mAnimactionHandler.sendEmptyMessage(2);
						Log.d(TAG, "storageInfo2::"
								+ "textview.settext;;success");
						if (isAnim) {
							Log.d(TAG, "animaction-start::" + isAnim);
							mAnimactionHandler.post(new BarRunnbale());
						} else {

						}
						Log.d(TAG, "isAnim::" + isAnim);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * @ClassName: BarRunnbale
	 * @Description: progressbar 增长动画线程
	 * @author: zhangpengzhan
	 * @date 2014年9月24日 上午10:56:26
	 * 
	 */
	class BarRunnbale implements Runnable {

		public void run() {
			num++;
			if (num <= usageRate) {
				post(this);
				mAnimactionHandler.sendEmptyMessage(-1);
				// Log.d("==", "num::" + num + "/" + usageRate);
			}
		}

	}

	public void setAppInstallCount(int count) {
		installAppCount = count;
		mAnimactionHandler.sendEmptyMessage(0);
		postDelayed(new Runnable() {

			@Override
			public void run() {
				getInstallAppCount(false);
			}
		}, 5000);

	}

	@Override
	public void onAppStoreReceive(Intent intent) {
		int id = intent.getIntExtra("app_id", 0);
		AppRecommendView recommendView = mapDownload.get(id);
		if (HomeActions.ACTION_DOWNLOAD.equals(intent.getAction())) {
			// 应用下载进度广播
			Log.v("aaaa", "应用下载进度广播");
			if (null != recommendView) {
				int progress = intent.getIntExtra("download_progress", 0);
				recommendView.setProgress(progress);// 更新进度
			}
		} else if (HomeActions.ACTION_DOWNLOAD_PAUSE.equals(intent.getAction())) {
			if (null != recommendView) {
				recommendView.hideDownloadProgressView();
			}
		} else if (HomeActions.ACTION_INSTALL_APP_SUCCESS.equals(intent
				.getAction())
				|| HomeActions.ACTION_INSTALL_APP_FAIL.equals(intent
						.getAction())) {
			// 应用安装成功或失败
			if (null != recommendView) {
				recommendView.hideInstallProgressView();// 隐藏安装进度
			}
		}

	}

	ViewGroup recommendTabView = null;

	@Override
	public ViewGroup getTopMenuView() {
		/**
		 * 目前lancher启动时该方法会被调用3次，recommendTabView每次分配的的地址都不同，这就造成后续调用时地址错乱；
		 * 这也就是造成了语音切换顶部TAB至首页时获取焦点出错的原因；
		 * 目前方法是通过空判断使首页recommendTabView加载指进行一次，后续需要优化首页加载过程。By zhangpengzhan
		 * and Spr_ypt
		 */
		if (null == recommendTabView) {
			recommendTabView = (ViewGroup) inflate(getContext(),
					R.layout.top_menu_recommend_layout, null);
			TextView topTabTextView = (TextView) recommendTabView
					.findViewById(R.id.navigation_tab_recommend_text);
			ImageView topFadeTabImageView = (ImageView) recommendTabView
					.findViewById(R.id.navigation_tab_recommend_fade);
			topTabTextView
					.setOnFocusChangeListener(new TopTabOnFocusChangeListener(
							topTabTextView, topFadeTabImageView));

			topTabTextView.setNextFocusDownId(R.id.matrix_recommend_layout_0);
			topTabTextView.setNextFocusUpId(R.id.navigation_tab_recommend_text);
			Log.d("getTopMenuView", "11::" + R.id.matrix_recommend_layout_0);
			viewFocusDirectionListener
					.setTopMenuViewFocusDirection(topTabTextView);
		}
		return recommendTabView;
	}

	@Override
	public View getBottomMenuView() {
		View recommendTabView = inflate(getContext(),
				R.layout.sub_navigation_common_recommend, null);
		View recommendFavouriteLayout = recommendTabView
				.findViewById(R.id.sub_navigation_common_recommend_favourite_text_layout);
		View recommendSettingLayout = recommendTabView
				.findViewById(R.id.sub_navigation_common_recommend_setting_text_layout);
		// View recommendUserLayout =
		// recommendTabView.findViewById(R.id.sub_navigation_common_recommend_user_text_layout);
		View recommendExternalLayout = recommendTabView
				.findViewById(R.id.sub_navigation_common_recommend_external_text_layout);

		recommendFavouriteLayout
				.setOnClickListener(new recommendFavouriteClickListener());
		recommendSettingLayout
				.setOnClickListener(new recommendSettingClickListener());
		// recommendUserLayout.setOnClickListener(new
		// recommendUserClickListener());
		recommendExternalLayout
				.setOnClickListener(new recommendExternalClickListener());
		bottomMenuViews = new View[] { recommendFavouriteLayout,
				matrix_recommend_layout_0, thumb_layout_0,
				recommendExternalLayout, matrix_recommend_layout_1,
				matrix_recommend_layout_3, matrix_recommend_layout_4 };

		viewFocusDirectionListener
				.setButtomMenuViewFocusDirection(bottomMenuViews);
		return recommendTabView;
	}

	/**
	 * 响应观看记录点击事件 点击后进入
	 */
	class tvRecordClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), PlayerRecordActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(
							String.valueOf(view.getId()),
							getResources().getString(
									R.string.sub_navigation_common_record_text))
					.setSenceName(AppScene.getScene()).setViewPosition("3319")
					.setSrcType(ItemType.BUTTON).setTabNo(Tab.RECOMMEND)
					.build();
		}
	}

	/**
	 * 响应搜索点击事件 点击后进入
	 */
	class tvSearchClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent();
			intent.setAction("com.hiveview.cloudscreen.user.UserLoginHomeActivity");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				Log.v(TAG, "startActivity");
				getContext().startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Log.v(TAG, "toast");
				ToastUtils.alert(getContext(), getContext().getResources()
						.getString(R.string.yet_open));
			}

			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(
							String.valueOf(view.getId()),
							getResources()
									.getString(
											R.string.sub_navigation_common_game_user_text))
					.setSenceName(AppScene.getScene()).setViewPosition("3320")
					.setSrcType(ItemType.BUTTON).setTabNo(Tab.RECOMMEND)
					.build();
		}
	}

	/**
	 * 响应点击事件 点击后进入
	 */
	public class AppStoreListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			try {
				Intent intent = new Intent();
				intent.setAction("com.hiveview.appstore.buy");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.putExtra("category_id", 2);
				getContext().startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(
							String.valueOf(view.getId()),
							getResources().getString(
									R.string.install_game_count))
					.setSenceName(AppScene.getScene())
					.setSrcType(ItemType.BUTTON).setViewPosition("3321")
					.setTabNo(Tab.RECOMMEND).build();
		}
	}

	/**
	 * 响应收藏点击事件 点击后进入
	 */
	class recommendFavouriteClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			Intent intent = new Intent(getContext(), CollectActivity.class);
			getContext().startActivity(intent);
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(
							String.valueOf(view.getId()),
							getResources()
									.getString(
											R.string.sub_navigation_common_favourite_text))
					.setSenceName(AppScene.getScene()).setViewPosition("3322")
					.setSrcType(ItemType.BUTTON).setTabNo(Tab.RECOMMEND)
					.build();
		}
	}

	/**
	 * 响应系统设置点击事件 点击后进入
	 */
	class recommendSettingClickListener extends SimpleOnClickListener {
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
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(
							String.valueOf(view.getId()),
							getResources()
									.getString(
											R.string.sub_navigation_common_setting_text))
					.setSenceName(AppScene.getScene()).setViewPosition("3314")
					.setSrcType(ItemType.BUTTON).setTabNo(Tab.FILM).build();
		}
	}

	/**
	 * 响应用户中心点击事件 点击后进入
	 * 
	 * class recommendUserClickListener extends SimpleOnClickListener {
	 * 
	 * @Override public DataHolder doOnClick(View view) { Intent intent = new
	 *           Intent(); intent.setAction("com.hiveview.user.usercenter");
	 *           intent.addCategory(Intent.CATEGORY_DEFAULT);
	 *           getContext().startActivity(intent);
	 * 
	 *           return new DataHolder.Builder(getContext())
	 *           .setDataType(DataType.CLICK_TAB_BUTTON)
	 *           .setButton(String.valueOf(view.getId()),
	 *           getResources().getString
	 *           (R.string.sub_navigation_common_user_text))
	 *           .setSenceName(AppScene
	 *           .getScene()).setViewPosition("3313").setSrcType
	 *           (ItemType.BUTTON).setTabNo(Tab.RECOMMEND).build(); } }
	 */

	/**
	 * 响应外接媒体点击事件 点击后进入
	 */
	class recommendExternalClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {

			AppUtil.openApp("com.hiveview.externalstorage", getContext());
			return new DataHolder.Builder(getContext())
					.setDataType(DataType.CLICK_TAB_BUTTON)
					.setButton(
							String.valueOf(view.getId()),
							getResources()
									.getString(
											R.string.sub_navigation_common_external_text))
					.setSenceName(AppScene.getScene()).setViewPosition("3323")
					.setSrcType(ItemType.BUTTON).setTabNo(Tab.RECOMMEND)
					.build();
		}
	}

}
