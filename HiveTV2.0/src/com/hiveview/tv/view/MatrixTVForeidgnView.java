package com.hiveview.tv.view;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.activity.OnlivePlayerActivity;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.content.CategoryDetailManager;
import com.hiveview.tv.common.statistics.SimpleOnClickListener;
import com.hiveview.tv.hdmiin.SWHDMIInManagerViewHandler;
import com.hiveview.tv.onlive.player.HiveviewVideoView;
import com.hiveview.tv.service.HiveTVService;
import com.hiveview.tv.service.entity.DispatchEntity;
import com.hiveview.tv.service.entity.LiveStreamEntity;
import com.hiveview.tv.service.entity.MoviePermissionEntity;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.SafeRunnable;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.BlueLightVipUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.STBSettingInfoUtil;
import com.hiveview.tv.utils.TypefaceUtils;
import com.hiveview.tv.view.pager3d.TabBasePageView;
import com.hiveview.tv.view.television.ChannelChangedListener;
import com.hiveview.tv.view.television.MarqueeText;

@SuppressLint("HandlerLeak")
public class MatrixTVForeidgnView extends TabBasePageView implements
		ChannelChangedListener {
	/**
	 * TAG
	 */
	private static final String TAG = "MatrixTVForeidgnView";
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 接口
	 */
	private HiveTVService dataService = null;
	/**
	 * 播放器layout
	 */
	private View matrixTvLayout3;
	/**
	 * 当前时间
	 */
	private TextView tvCurrentdate;
	/**
	 * 当前日期
	 */
	private MarqueeText tvCurrentay;
	/**
	 * 保存当前电视剧
	 */
	private SharedPreferences sp;
	private Editor ed;
	public static String nameMatrixTV = "MatrixTV";
	private String nameString;
	private String uriString;
	private int id;
	private int cp;
	private int isVip;
	private int cpId;
	private int isFreeLimit  ;
	
	public static String tvName = "tvName";
	public static String tvUri = "tvUri";
	public static String tvID = "tvId";
	public static String tvCP = "tvCp";
	public static String tvCPID = "tvCpId";
	public static String tvisVip = "isVip";
	public static String tvisFreeLimit   = "isFreeLimit";
	
	public static String default_tvName = "default_tvName";
	public static String default_tvUri = "default_tvUri";
	public static String default_tvID = "default_tvId";
	public static String default_tvCP = "default_tvCp";
	public static String default_tvCPID = "default_tvCpId";
	public static String default_tvisVip = "default_isVip";
	public static String default_tvisFreeLimit = "default_isFreeLimit";

	public static String defaultUri = "http://101.246.189.136:81/test/2/live.m3u8";
	public static String defaultName = "CCTV-1";
	public static int defaultID = 6;
	public static int defaultCP = 2;
	public static int defaultCPID = 1;
	public static int defaultIsVip = 0;
	public static int defaultisFreeLimit = 1;
	/**
	 * 轮播传过来的name,uri
	 */
	private String live_name, live_uri;
	/**
	 * 轮播传过来的id,cp
	 */
	private int live_id, live_cp, live_isVip,live_cpId,live_isFreeLimit;
	/**
	 * 数据来源
	 */
	private String source=null;
	static HiveviewVideoView hv;
	public static final String ACTION_SMALL_SCREEN_SHOW = "com.hiveview.tv.view.hdmiin.small_show_f";
	public static final String ACTION_SMALL_SCREEN_UNSHOW = "com.hiveview.tv.view.hdmiin.small_unshow_f";
	public static final String ACTION_SMALL_SCREEN_PAUSE = "com.hiveview.tv.view.hdmiin.small_pause_f";

	public final static int HANDLER_SMALL_SCREEN_SHOW = 1;
	public final static int HANDLER_SMALL_SCREEN_UNSHOW = 2;
	public final static int HANDLER_SMALL_SCREEN_PAUSE = 3;
	public final static int HANDLER_TV_FROM_LAUNCHER_S = 4;
	private final int LOAD_TVLIST_SUCCESS = 401;
	/**
	 * 启动轮播播放器携带的参数
	 */
	public final static String EXTRA_URL = "com.hiveview.cloudscreen.videolive.PLAY_URL";

	/**
	 * 启动轮播播放器携带的参数
	 */
	public final static String EXTRA_CP = "com.hiveview.cloudscreen.videolive.PLAY_CP";

	/**
	 * isVip
	 */
	public final static String EXTRA_IS_VIP = "com.hiveview.cloudscreen.videolive.IS_VIP";

	/**
	 * 附加参数，Tv_id
	 */
	public final static String EXTRA_TV_ID = "com.hiveview.cloudscreen.videolive.PLAY_TV_ID";
	/**
	 * 附加参数，Tv_Name
	 */
	public final static String EXTRA_TV_NAME = "com.hiveview.cloudscreen.videolive.PLAY_TV_NAME";
	
	/**
	 * cpId 厅id
	 */
	public static String EXTRA_CP_ID = "com.hiveview.cloudscreen.videolive.CP_ID";

	/*0：非限免，1：限免（vip频道中可以播放）*/
	public static String EXTRA_IS_FreeLimit = "com.hiveview.cloudscreen.videolive.IS_FreeLimit";
	
	protected static final int HANDLER_REQUESS_SECONDURI = 5;

	/**
	 * 变小广播
	 */
	public static String ACTION_TV_FROM_LAUNCHER_S = "com.hiveview.tv.view.tv_station_small";

	/**
	 * HDMIIn未接入时的挡板
	 */
	private ImageView tvSource;

	/** 访问网络的service */
	protected HiveTVService service;

	// 判断当前影片是否有效：
	boolean isEffective = false;
	
	private MoviePermissionEntity mMoviePermissionEntity;

	public MatrixTVForeidgnView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public MatrixTVForeidgnView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public MatrixTVForeidgnView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public MatrixTVForeidgnView(Context context,
			RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		mContext = context;
		init();
	}

	protected void init() {
		/* 利用文字换台接口将hdmiin频道设置为上次关机时频道 */
		Log.v("Live_tv", "init");
		dataService = new HiveTVService();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		View view = inflate(getContext(), R.layout.matrix_tv_foreidgn_view,
				null);
		this.addView(view, params);
		sp = getContext().getSharedPreferences(nameMatrixTV,
				Context.MODE_WORLD_WRITEABLE);
		ed = sp.edit();
		uriString = sp.getString(tvUri, defaultUri);
		nameString = sp.getString(tvName, defaultName);
		id = sp.getInt(tvID, defaultID);
		cp = sp.getInt(tvCP, defaultCP);
		cpId = sp.getInt(tvCPID, defaultCPID);
		isVip = sp.getInt(tvisVip, defaultIsVip);
		isFreeLimit=sp.getInt(tvisFreeLimit, defaultisFreeLimit);

		tvCurrentdate = (TextView) findViewById(R.id.tv_tv_date);
		tvCurrentay = (MarqueeText) findViewById(R.id.tv_tv_day);
		tvSource = (ImageView) findViewById(R.id.view_hdmi_small_image);
		/*
		 * tvCurrentdate.setText(DateUtils.dateToStringYM(System.currentTimeMillis
		 * ()));
		 * tvCurrentdate.setTypeface(TypefaceUtils.getStandardfFontFace());
		 */

		tvCurrentay.setText(nameString);
		tvCurrentay.setStart(true);
		tvCurrentay.setTypeface(TypefaceUtils.getStandardfFontFace());

		matrixTvLayout3 = view.findViewById(R.id.matrix_tv_layout_3_f);
		// 设置向上焦点到电视
		matrixTvLayout3.setNextFocusUpId(R.id.navigation_tab_tv_foreidg_text);
		// 设置向下焦点到电视机顶盒设置
		matrixTvLayout3.setNextFocusDownId(matrixTvLayout3.getId());
		// 设置向左焦点
		matrixTvLayout3.setNextFocusLeftId(R.id.matrix_game_layout_4);
		// 设置向右的焦点
		matrixTvLayout3.setNextFocusRightId(R.id.matrix_bluelight_layout_0);

		matrixTvLayout3.setOnFocusChangeListener(TvLayoutFocusListener);
		
		matrixTvLayout3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openScreen();
				/*
				 * Intent intent = new Intent((Activity) mContext,
				 * OnlivePlayerActivity.class); mContext.sendBroadcast(new
				 * Intent(HiveviewHdmiInView.ACTION_SMALL_SCREEN_SWITCH));
				 * ((Activity) mContext).startActivity(intent);
				 */
			}
		});
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SWHDMIInManagerViewHandler.SWHDMIIN);
		intentFilter.addAction(ACTION_SMALL_SCREEN_SHOW);
		intentFilter.addAction(ACTION_SMALL_SCREEN_UNSHOW);
		intentFilter.addAction(ACTION_SMALL_SCREEN_PAUSE);
		intentFilter.addAction(ACTION_TV_FROM_LAUNCHER_S);
		mContext.registerReceiver(mIntentReceiver, intentFilter);
		hv = (HiveviewVideoView) findViewById(R.id.view_hdmi_small);
		hv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Log.v(TAG, "MatrixTVForeidgnView===setOnCompletionListener");
				try {
					mp.start();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
		hv.setVisibility(View.VISIBLE);
		super.init();

	}

	public void setUnregister() {
		mContext.unregisterReceiver(mIntentReceiver);
	};
	
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_SMALL_SCREEN_SHOW:
				uriString = sp.getString(tvUri, defaultUri);
				nameString = sp.getString(tvName, defaultName);
				cp = sp.getInt(tvCP, defaultCP);
				cpId = sp.getInt(tvCPID, defaultCPID);
				id = sp.getInt(tvID, defaultID);
				isVip = sp.getInt(tvisVip, defaultIsVip);
				isFreeLimit=sp.getInt(tvisFreeLimit, defaultisFreeLimit);
				Log.v(TAG, "nameString2:" + nameString + ";id:" + id
						+ ";cp:" + cp + ";live_isVip:" + isVip+";live_uri:"+uriString+";live_isFreeLimit:"+isFreeLimit);
				String currentUri = null;
				if (isVip == 1) {// 1 vip

					// 根据影厅id读取数据库
					ArrayList<MoviePermissionEntity> resultList = BlueLightVipUtil
							.query(null, "cpId = ?", new String[] { cpId + "" },
									null); // cpId为影厅id
					
					if(resultList!=null&&resultList.size()!=0){
						mMoviePermissionEntity = resultList
							.get(0);
					if (System.currentTimeMillis()
							- mMoviePermissionEntity.getFirstPayTime() <= mMoviePermissionEntity
							.getEffectiveTime()) {
						isEffective = true;
					} else {
						isEffective = false;
					}
					}else{
						isEffective = false;	
					}
					
					if(isFreeLimit==1){
						tvCurrentay.setText(nameString);
						currentUri = uriString;	
					}else{
					if (isEffective) {
						Log.v(TAG, "购买了");
						tvCurrentay.setText(nameString);
						currentUri = uriString;
					} else {
						Log.v(TAG, "未购买了");
						ed.putString(MatrixTVForeidgnView.tvUri,
								sp.getString(default_tvUri, defaultUri));
						ed.putString(MatrixTVForeidgnView.tvName,
								sp.getString(default_tvName, defaultName));
						ed.putInt(MatrixTVForeidgnView.tvID,
								sp.getInt(default_tvID, defaultID));
						ed.putInt(MatrixTVForeidgnView.tvCP,
								sp.getInt(default_tvCP, defaultCP));
						ed.putInt(MatrixTVForeidgnView.tvCPID,
								sp.getInt(default_tvCPID, defaultCPID));
						ed.putInt(MatrixTVForeidgnView.tvisVip,
								sp.getInt(default_tvisVip, defaultIsVip));
						ed.putInt(MatrixTVForeidgnView.tvisFreeLimit, sp.getInt(default_tvisFreeLimit, defaultisFreeLimit));
						ed.commit();
						cp=sp.getInt(tvCP, defaultCP);
						currentUri = sp.getString(default_tvUri, defaultUri);
						tvCurrentay.setText(sp.getString(default_tvName,
								defaultName));
					}}
				} else if (isVip == 0) { // 非vip
					tvCurrentay.setText(nameString);
					currentUri = uriString;
				}

				if (cp == 1) { // 1 请求调度服务 得到新的uri去播放
					requestURI(currentUri);
					Log.v(TAG, "nameString3=" + currentUri);
				} else {// 其他直接播放
					try {
						Log.v(TAG, "nameString4=" + currentUri);
						hv.setVideoPath(currentUri);
						hv.start();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				break;
			case HANDLER_REQUESS_SECONDURI:
				Log.v(TAG, "mUri=" + mUri);
				try {
					hv.setVideoURI(mUri);
					hv.start();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				break;
			case HANDLER_SMALL_SCREEN_UNSHOW:
				// Log.v("HANDLER_SMALL_SCREEN_UNSHOW", "uriString=" +
				// uriString);
				try {
				hv.stopPlayback();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				break;
			case HANDLER_SMALL_SCREEN_PAUSE:
				// Log.v("HANDLER_SMALL_SCREEN_PAUSE", "uriString=" +
				// uriString);
				try {
					hv.stopPlayback();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				break;
			case HANDLER_TV_FROM_LAUNCHER_S:
				// Log.v("HANDLER_TV_FROM_LAUNCHER_S", "uriString=" +
				// uriString);
				// hv.setVideoPath(uriString);
				// hv.start();
				if (live_name != null && !live_name.equals("")) {
					Log.v(TAG, "live_name"+live_name.equals(""));
					ed.putString(tvName, live_name);
				}
				if (live_uri != null && live_uri != "") {
					ed.putString(tvUri, live_uri);
				}
				ed.putInt(tvID, live_id);
				ed.putInt(tvCP, live_cp);
				ed.putInt(tvisVip, live_isVip);
				ed.putInt(tvCPID, live_cpId);
				ed.putInt(tvisFreeLimit, live_isFreeLimit);
				if(live_isVip==0){
					Log.v(TAG, "非vip");
					if (live_name != null && live_name != "") {
						ed.putString(default_tvName, live_name);
					}
					if (live_uri != null && live_uri != "") {
						ed.putString(default_tvUri, live_uri);
					}
					ed.putInt(default_tvID, live_id);
					ed.putInt(default_tvCP, live_cp);
					ed.putInt(default_tvisVip, live_isVip);
					ed.putInt(default_tvCPID, live_cpId);
					ed.putInt(default_tvisFreeLimit, live_isFreeLimit);
				}else{
					if(live_isFreeLimit==1){
						Log.v(TAG, "vip限时免费");
						if (live_name != null && live_name != "") {
							ed.putString(default_tvName, live_name);
						}
						if (live_uri != null && live_uri != "") {
							ed.putString(default_tvUri, live_uri);
						}
						ed.putInt(default_tvID, live_id);
						ed.putInt(default_tvCP, live_cp);
						ed.putInt(default_tvisVip, live_isVip);
						ed.putInt(default_tvCPID, live_cpId);
						ed.putInt(default_tvisFreeLimit, live_isFreeLimit);	
					}
				}
				
				ed.commit();
				break;
			}
		}
	};

	/**
	 * cp为1 二次请求的uri
	 */
	Uri mUri;

	/****
	 * 分类菜单网络请求
	 */
	public void requestURI(final String uriString) {
		submitRequest(new SafeRunnable() {

			@Override
			public void requestData() {
				// TODO Auto-generated method stub
				service = new HiveTVService();
				ArrayList<DispatchEntity> DispatchEntitys = service
						.getDownLoadURI(uriString);
				if (DispatchEntitys != null && DispatchEntitys.size() != 0) {
					DispatchEntity dispatchEntity = DispatchEntitys.get(0);
					mUri = Uri.parse(dispatchEntity.getU());
					handler.sendEmptyMessage(HANDLER_REQUESS_SECONDURI);
				}
			}

			@Override
			public void processServiceException(ServiceException e) {
				// TODO Auto-generated method stub
				Log.e("requestURI", "here?" + e.getErrorCode());
			}

		});

	}

	@Override
	public void onChannelChanged(String ChannelName) {
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	@Override
	protected void processData(int msgWhat) {
		switch (msgWhat) {
		default:
			break;
		}
	}

	@Override
	public void loadData(boolean isRefleshData) {

	}

	@Override
	public void updateUIRefleshData() {

	}
	
	
	// 推荐位直播流实体
	private LiveStreamEntity liveEntity;
	
	/**
	 * @author lihongji
	 * 
	 *         直播节目
	 */
	class GetTvList extends SafeRunnable {

		private int tvid;

		public GetTvList(int tvid) {
			// TODO Auto-generated constructor stub
			this.tvid = tvid;
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

	/**
	 * 切大屏
	 */
	public void openScreen() {
		uriString = sp.getString(tvUri, defaultUri);
		id = sp.getInt(tvID, defaultID);
		cp = sp.getInt(tvCP, defaultCP);
		cpId = sp.getInt(tvCPID, defaultCPID);
		isVip = sp.getInt(tvisVip, defaultIsVip);
		isFreeLimit=sp.getInt(tvisFreeLimit, defaultisFreeLimit);
		try {
			Log.v(TAG, "openScreen  nameString6 :" + uriString + "; cp:" + cp
					+ ";isVip:" + isVip+";isFreeLimit:"+isFreeLimit);
			Intent intent = new Intent();
			ComponentName cn = new ComponentName(
					"com.hiveview.cloudscreen.videolive",
					"com.hiveview.cloudscreen.videolive.MainActivity");
			intent.setComponent(cn);
			intent.putExtra(EXTRA_URL, uriString);
			intent.putExtra(EXTRA_TV_ID, id);
			intent.putExtra(EXTRA_CP, cp);
			intent.putExtra(EXTRA_CP_ID, cpId);
			intent.putExtra(EXTRA_IS_VIP, isVip);
			intent.putExtra(EXTRA_IS_FreeLimit, isFreeLimit);
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 推荐位的焦点变化监听
	 */
	private OnFocusChangeListener TvLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean isFocused) {

			if (isFocused) {// 推荐位获得焦点的动画效果
				HiveviewApplication.mcurrentfocus=view;
				Log.v(TAG, "mcurrentfocus="+view.getId());
			} else {// 推荐位失去焦点的动画效果

			}

		}
	};
	
	@Override
	public View getBottomMenuView() {
		View tvTabView = inflate(getContext(), R.layout.sub_navigation_tv, null);
		// 分类选台
		View tvCategoryLayout = tvTabView
				.findViewById(R.id.sub_navigation_tv_category_text_layout);
		tvCategoryLayout.setVisibility(View.GONE);
		// 直播提醒
		View tvNotificationLayout = tvTabView
				.findViewById(R.id.sub_navigation_tv_notification_text_layout);
		tvNotificationLayout.setVisibility(View.GONE);
		// 快速选台
		View tvDirectionLayout = tvTabView
				.findViewById(R.id.sub_navigation_tv_direction_text_layout);
		// 设置宽
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
				240, LayoutParams.WRAP_CONTENT);
		param.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		tvDirectionLayout.setLayoutParams(param);
		// 设置向上焦点
		tvDirectionLayout.setNextFocusUpId(R.id.matrix_tv_layout_3);
		// 设置向下焦点
		tvDirectionLayout
				.setNextFocusDownId(R.id.sub_navigation_tv_direction_text_layout);
		// 设置项左焦点
		tvDirectionLayout
				.setNextFocusLeftId(R.id.sub_navigation_tv_direction_text_layout);
		// 设置向右焦点
		tvDirectionLayout
				.setNextFocusRightId(R.id.sub_navigation_tv_direction_text_layout);

		TextView tvSetting = (TextView) tvDirectionLayout
				.findViewById(R.id.sub_navigation_tv_direction_text_view);
		tvSetting.setText(mContext.getResources().getString(
				R.string.sub_navigation_tv_setting_text));

		tvDirectionLayout.setOnClickListener(new TvTabViewClickListener());

		bottomMenuViews = new View[] { tvCategoryLayout, tvDirectionLayout,
				tvNotificationLayout };
		viewFocusDirectionListener
				.setButtomMenuViewFocusDirection(bottomMenuViews);
		return tvTabView;
	}

	/**
	 * 响应点击事件 点击后进入 快速选台
	 */
	class TvTabViewClickListener extends SimpleOnClickListener {
		@Override
		public DataHolder doOnClick(View view) {
			STBSettingInfoUtil.STBShowTVSetUI(mContext);
			return null;
		}
	}

	@Override
	public ViewGroup getTopMenuView() {

		ViewGroup tvTabView = (ViewGroup) inflate(getContext(),
				R.layout.top_menu_tv_foreidg_layout, null);
		TextView topTabTextView = (TextView) tvTabView
				.findViewById(R.id.navigation_tab_tv_foreidg_text);
		ImageView topFadeTabImageView = (ImageView) tvTabView
				.findViewById(R.id.navigation_tab_tv_foreidg_fade);

		topTabTextView.setText(R.string.navigation_tab_tv_text);
		topTabTextView
				.setOnFocusChangeListener(new TopTabOnFocusChangeListener(
						topTabTextView, topFadeTabImageView));
		Log.d(TAG,
				"getTopMenuView===> set the view next down foucs way::"
						+ R.id.matrix_tv_layout_3_f + "||focuable:"
						+ matrixTvLayout3.isFocusable() + "||focued:"
						+ matrixTvLayout3.isFocused());
		topTabTextView.setNextFocusDownId(matrixTvLayout3.getId());

		// 2131231222
		Log.d(TAG, "getTopMenuView===> get the view next down foucs way::"
				+ tvTabView.getNextFocusDownId());
		topTabTextView.setNextFocusUpId(R.id.navigation_tab_tv_foreidg_text);
		viewFocusDirectionListener
				.setRecommendViewsFocusDirection(new View[] { matrixTvLayout3 });
		return tvTabView;
	}

	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String value = intent.getAction();
			Log.v(TAG, "value:" + value);
			if (SWHDMIInManagerViewHandler.SWHDMIIN.equals(value)) {
				String isSignalable = intent.getStringExtra("isSignalable");
				if (isSignalable.equals("true")) {
					tvSource.setVisibility(View.GONE);
					Log.i(TAG,
							"SWHDMIInManagerViewHandler   -----------------------> HDMIRxReceiver : "
									+ isSignalable);
				} else if (isSignalable.equals("false")) {
					tvSource.setVisibility(View.VISIBLE);
					Log.i(TAG,
							"SWHDMIInManagerViewHandler   -----------------------> HDMIRxReceiver : "
									+ isSignalable);
				}
			} else if (ACTION_SMALL_SCREEN_PAUSE.equals(value)) {
				handler.sendEmptyMessage(HANDLER_SMALL_SCREEN_PAUSE);
			} else if (ACTION_SMALL_SCREEN_UNSHOW.equals(value)) {
				handler.sendEmptyMessage(HANDLER_SMALL_SCREEN_UNSHOW);
			} else if (ACTION_SMALL_SCREEN_SHOW.equals(value)) {
				handler.sendEmptyMessage(HANDLER_SMALL_SCREEN_SHOW);
			} else if (ACTION_TV_FROM_LAUNCHER_S.equals(value)) {
				live_name = intent.getStringExtra(EXTRA_TV_NAME);
				live_uri = intent.getStringExtra(EXTRA_URL);
				live_id = intent.getIntExtra(EXTRA_TV_ID, defaultID);
				live_cp = intent.getIntExtra(EXTRA_CP, defaultCP);
				live_isVip = intent.getIntExtra(EXTRA_IS_VIP, defaultIsVip);
				live_cpId=intent.getIntExtra(EXTRA_CP_ID, defaultCPID);
				live_isFreeLimit=intent.getIntExtra(EXTRA_IS_FreeLimit, defaultisFreeLimit);
				Log.v(TAG, "nameString1:" + live_name + ";id:" + live_id
						+ ";cp:" + live_cp + ";live_isVip:" + live_isVip+";live_uri:"+live_uri+";live_isFreeLimit:"+live_isFreeLimit);
				handler.sendEmptyMessage(HANDLER_TV_FROM_LAUNCHER_S);
			}

		}

	};

}
