package com.hiveview.tv.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.PrivateCredentialPermission;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.hiveview.box.framework.image.DisplayImageOptions;
import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.box.framework.image.ImageScaleType;
import com.hiveview.tv.R;
import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;
import com.hiveview.tv.common.statistics.KeyEventHandler;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;
import com.hiveview.tv.common.statistics.Tab;
import com.hiveview.tv.service.dao.AppFocusDAO;
import com.hiveview.tv.service.dao.BaseDAO;
import com.hiveview.tv.service.dao.ChannelDAO;
import com.hiveview.tv.service.dao.EducationDAO;
import com.hiveview.tv.service.dao.GameFocusDAO;
import com.hiveview.tv.service.dao.PageSkinDAO;
import com.hiveview.tv.service.dao.RecommendDAO;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.entity.FirstClassListEntity;
import com.hiveview.tv.service.entity.LiveStreamEntity;
import com.hiveview.tv.service.entity.MoviePermissionEntity;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.service.entity.SkinEntity;
import com.hiveview.tv.service.exception.ErrorCode;
import com.hiveview.tv.service.exception.ServiceException;
import com.hiveview.tv.service.request.ApiConstant;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.BlueLightVipUtil;
import com.hiveview.tv.utils.DateUtils;
import com.hiveview.tv.utils.DeviceBoxUtils;
import com.hiveview.tv.view.MatrixTVForeidgnView;
import com.hiveview.tv.view.pager3d.HomeActions;

/**
 * 此服务主要是 通过launcher来启动 1.数据（图片）的预加载 完成释放掉
 * 
 * 
 * 
 */
public class LoadService extends IntentService {

	private static final String TAG = LoadService.class.getSimpleName();
	private HiveTVService service = new HiveTVService();
	public static final String DATA_ACTION = "DATA_ACTION";
	protected DisplayImageOptions options = new DisplayImageOptions.Builder()
			.resetViewBeforeLoading(false).cacheOnDisc(true)
			.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(HiveviewApplication.Bitmapconfig)
			.considerExifParams(true).build();

	private boolean isNeedDeviceCheck = false;

	private boolean isHasCache = false;
	// 首次鉴权进入启动活跃用户定时器(屏蔽可能有多次鉴权)
	private boolean isFirstSendAct = true;

	/**
	 * 定时任务的管理类
	 */
	private static AlarmManager alarmManager;
	/**
	 * 定时任务要触发的广播
	 */
	private static PendingIntent pendingIntent;
	/**
	 * @Fields activiteAction:活跃用户统计
	 */
	private String activiteAction = "com.hiveview.tv.activeStatistics";
	/**
	 * @Fields 五分钟的时长
	 */
	private static int fiveMinu = 5 * 60 * 1000;

	/**
	 * @Fields RecommectTag 存储内外网相关信息
	 */
	SharedPreferences RecommectTag;
	Editor RecommectTagEditor;

	/**
	 * @Fields 存储直播流
	 */
	SharedPreferences live_sSharedPreferences;
	Editor live_editor;

	public LoadService() {
		super(TAG);
	}

	public static void cancleAlarm() {
		if (alarmManager != null && pendingIntent != null)
			alarmManager.cancel(pendingIntent);
	}

	/**
	 * 首页、极清推荐位数据
	 */
	ArrayList<RecommendEntity> recommendList = null;
	ArrayList<RecommendEntity> bluelightList = null;

	/**
	 * 异步请求网络数据
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void onHandleIntent(Intent intentArgs) {
		isNeedDeviceCheck = intentArgs.getBooleanExtra("isNeedDeviceCheck",
				false);
		RecommectTag = getSharedPreferences(HiveviewApplication.RecommendTag,
				Context.MODE_WORLD_READABLE);
		RecommectTagEditor = RecommectTag.edit();

		live_sSharedPreferences = getSharedPreferences(
				MatrixTVForeidgnView.nameMatrixTV, Context.MODE_WORLD_WRITEABLE);
		live_editor = live_sSharedPreferences.edit();

		Log.i(TAG, "onHandleIntent-->start" + "|isNeedDevice:"
				+ isNeedDeviceCheck);
		// getApplication().getSharedPreferences("loadImage",
		// Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0),
		// "LoadService"+isNeedDeviceCheck).commit();
		boolean isDeviceCheckSuccess = false;

		try {
			if (isNeedDeviceCheck) {// 开始鉴权
				// 清空上次鉴权的信息
				Log.i(TAG, "onHandleIntent-->POS 1");
				SharedPreferences spCookies = getApplication()
						.getSharedPreferences(AppConstant.SP_COOKIES,
								Context.MODE_PRIVATE);
				Editor edit = spCookies.edit();
				edit.putString(AppConstant.SP_COOKIES, null);
				edit.commit();

				String errorCode = "";
				int devCount = 0;
				while (!errorCode.equals(ErrorCode.CODE_SUCCESS)
						&& devCount <= 10
						&& HiveTVService.isAvailable(getApplicationContext())) {
					if (HiveTVService.isAvailable(getApplicationContext())) {// 网络状态正常
						Log.i(TAG, "onHandleIntent-->POS 2");
						try {
							Log.i(TAG, "onHandleIntent-->POS 2.1");
							Device device = DeviceFactory.getInstance()
									.getDevice();
							errorCode = service
									.deviceCheck(
											ApiConstant.APP_VERSION,
											AppUtil.getLocaldeviceId(getApplicationContext()),
											device.getMac(), ApiConstant.UUID);
							isDeviceCheckSuccess = true;// 鉴权成功
							Log.i(TAG, "onHandleIntent-->POS 2.2");
							break;
						} catch (Exception e) {
							e.printStackTrace();
							Log.i(TAG, "onHandleIntent-->POS 3");
							isDeviceCheckSuccess = false;
						}
						devCount++;
					} else {
						Thread.sleep(200);
					}
				}
				Log.i(TAG, "onHandleIntent-->POS 4");
				try {
					if (null == alarmManager) {
						alarmManager = (AlarmManager) LoadService.this
								.getSystemService(Context.ALARM_SERVICE);
					}

					Log.i(TAG, "onHandleIntent-->POS 5");
					// 开机统计
					Log.i(TAG,
							"LoadService::onHandleIntent-->before boot statistics");
					SharedPreferences sharedPreferences = LoadService.this
							.getSharedPreferences("boot_time",
									Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					// 上次开机时间(开机统计)
					long last_time = sharedPreferences.getLong(
							"last_boot_time", new Date().getTime());
					Date last_date = new Date(last_time);

					java.util.Date dt = new Date();
					Log.d(TAG, "系统时间:" + System.currentTimeMillis());
					// 日期转换
					// SimpleDateFormat sdf = new SimpleDateFormat("MM",
					// Locale.CHINA);
					// SimpleDateFormat sdf2 = new SimpleDateFormat("dd",
					// Locale.CHINA);
					// String months_string = sdf.format(dt);
					// String days_string = sdf2.format(dt);
					// int months = Integer.parseInt(months_string);
					// int days = (Integer.parseInt(days_string)) + 0;
					//
					// String last_months_string = sdf.format(last_date);
					// String last_days_string = sdf2.format(last_date);
					// int last_months = Integer.parseInt(last_months_string);
					// int last_days = (Integer.parseInt(last_days_string)) + 0;
					long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
					// 开机天数默认0
					String intervalDay;
					long diff = dt.getTime() - last_date.getTime();
					intervalDay = String.valueOf(diff / nd);// 计算差多少天

					// if(last_months==months){
					// intervalDay=String.valueOf(days-last_days);
					// }else{
					// intervalDay=String.valueOf((months-last_months)*30+days-last_days);
					// }
					Log.v(TAG, "intervalDay=" + intervalDay);
					// 本次开机时间(开机统计)
					editor.putLong("last_boot_time", dt.getTime());
					// 上次开机时间(活跃用户统计)
					long last_activetime = sharedPreferences.getLong(
							"last_active_time", dt.getTime() - nd);
					// 上次开机后使用时间(活跃用户统计)
					int last_waittime = sharedPreferences.getInt(
							"last_wait_time", 0);
					// 是否发送
					boolean issend = sharedPreferences.getBoolean("issend",
							false);

					if (isFirstSendAct) {
						isFirstSendAct = false;

						Log.v(TAG, "issend" + issend);
						Date last_activedate = new Date(last_activetime);
						long activediff = dt.getTime()
								- last_activedate.getTime();
						int activediffDay = (int) (activediff / nd);
						Log.v(TAG, "activediff" + last_activedate.getTime()
								+ ";activediffDay" + activediffDay);
						if (activediffDay == 0) {
							Log.v(TAG, "今天已启动过issend" + issend + "start");
							// 判断是否发送过
							// shi
							if (issend) {
								Log.v(TAG, "issend" + issend);
								editor.putLong("last_active_time", dt.getTime());
							}
							// fou
							else {
								Intent broadcastIntent = new Intent(
										activiteAction);
								pendingIntent = PendingIntent.getBroadcast(
										LoadService.this,
										(int) System.currentTimeMillis(),
										broadcastIntent, 0);
								alarmManager.setRepeating(AlarmManager.RTC,
										System.currentTimeMillis() + fiveMinu,
										fiveMinu, pendingIntent);
							}
							Log.v(TAG, "今天已启动过issend" + issend + "end");

						} else if (activediffDay >= 1) {
							Log.v(TAG, "今天初次启动issend" + issend);
							editor.putLong("last_active_time", dt.getTime());
							editor.putInt("last_wait_time", 0);
							editor.putBoolean("issend", false);
							Intent broadcastIntent = new Intent(activiteAction);
							pendingIntent = PendingIntent.getBroadcast(
									LoadService.this,
									(int) System.currentTimeMillis(),
									broadcastIntent, 0);
							alarmManager.setRepeating(AlarmManager.RTC,
									System.currentTimeMillis() + fiveMinu,
									fiveMinu, pendingIntent);

						}
						editor.commit();// 别忘了提交哦
					}
				} catch (Exception e) {
					Log.i(TAG, "onHandleIntent-->POS 6");
					e.printStackTrace();
				}
				Log.i(TAG, "onHandleIntent-->POS 7");
				if (!isDeviceCheckSuccess) {// 鉴权失败后，后面的不在请求数据
					// Thread.sleep(10000);
					Log.i(TAG, "onHandleIntent-->POS 8");
					sendBroadcast(new Intent(
							HomeActions.BOX_START_DEVICE_CHECK_FAIL_ACTION));// 发送鉴权失败的广播
					return;
				}
				Log.i(TAG, "onHandleIntent-->POS 9");
				try {
					// 开机广告
					ContentResolver resolver = getContentResolver();
					resolver.call(
							Uri.parse("content://HiveViewAuthoritiesDataProvider"),
							"sendBootAdStatistic", null, null);
					Log.i(TAG,
							"content://HiveViewAuthoritiesDataProvider/sendBootAdStatistic");
				} catch (Exception e) {
					Log.d(TAG, "开机广告==>error");
					e.printStackTrace();
				}
			}
			Log.i(TAG, "onHandleIntent-->POS 10");
			// 检测本地是否有缓存
			RecommendDAO dao = new RecommendDAO(getApplicationContext());
			// GameFocusDAO gDao = new GameFocusDAO(getApplicationContext());

			// start author:zhangpengzhan
			// 缓存判断,只是判断数据库中是否有数据,然而是否与线上一致并没有判断,因此缓存有可能是不对的
			// 客户端最好不留有自己的缓存数据,每次从线上拉去保证运营的更换能够及时生效

			/*
			 * if (dao.isCache() && gDao.isCache()) { Log.i(TAG,
			 * "onHandleIntent-->POS 11"); isHasCache = true; }
			 */
			// end
			// 鉴权通过需要通知蓝光去预加载数据
			Intent intent = new Intent();
			intent.setAction("com.hiveview.action.DEVICE_CHECK_COMPLETED");
			sendBroadcast(intent);
			Log.i(TAG, "onHandleIntent-->POS 12");
			if (isNeedDeviceCheck || !isHasCache) {
				Log.i(TAG, "onHandleIntent-->POS 13");
				// getApplication().getSharedPreferences("loadImage",
				// Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0),
				// " date form network").commit();
				// 蜂巢2.0去预加载数据
				if (!AppConstant.ISDOMESTIC) {
					try {
						loadTvList();
					} catch (Exception e) {
						Log.i(TAG, "loadTvList");
						e.printStackTrace();
					}
				}
				loadRecommendData();// 请求推荐，影院推荐位
				loadAppFocusData();// 请求推荐、极清、游戏，应用,教育推荐位信息
				loadChannels();// 请求频道信息
				// loadThemeData();// 请求背景图信息
			} else {
				// getApplication().getSharedPreferences("loadImage",
				// Context.MODE_PRIVATE).edit().putString(DateUtils.getAfterMinuteDate(0),
				// " date form datebases").commit();
				Log.i(TAG, "onHandleIntent-->POS 14");
				ArrayList<ArrayList<RecommendEntity>> recommendCache = dao
						.queryMatrix(null, null, null, null);
				for (int i = 0; i < recommendCache.size(); i++) {
					ArrayList<RecommendEntity> subList = recommendCache.get(i);
					for (int k = 0; k < subList.size(); k++) {
						RecommendEntity entity = subList.get(k);
						ImageLoader.getInstance().loadImageSync(
								entity.getFocusLargeImg(), options);
						if (entity.getPositionId().equals("1")) {
							ImageLoader.getInstance().loadImageSync(
									entity.getFocusThumbImg(), options);
						}
					}
				}
			}

		} catch (Exception e) {
			Log.i(TAG, "onHandleIntent-->POS 15");
			e.printStackTrace();
		} finally {
			Log.i(TAG, "onHandleIntent-->POS 16");
			// 当前的请求是刷新数据的请求，才去发送刷新数据的广播
			if (isNeedDeviceCheck || !isHasCache) {
				Log.i(TAG, "onHandleIntent-->!isNeedDeviceCheck || !isHasCache");
				Log.i(TAG, "onHandleIntent-->POS 17");
				Intent intent = new Intent();
				intent.setAction(DATA_ACTION);
				sendBroadcast(intent);
			}
		}

	}

	private void loadTvList() {
		// TODO Auto-generated method stub
		Log.v("Live_tv", "loadTvList");
		String uriString = live_sSharedPreferences.getString(
				MatrixTVForeidgnView.tvUri, MatrixTVForeidgnView.defaultUri);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		String date = dateFormat.format(new Date());
		LiveStreamEntity liveEntity = new LiveStreamEntity();
		ArrayList<LiveStreamEntity> tvList = service.getTvList("1", "", date,
				date);

		if (uriString.equals(MatrixTVForeidgnView.defaultUri)) {
			// 首次进入数据存入数据库 切换到小窗口界面再取出数据播放
			if (tvList != null && tvList.size() != 0) {
				liveEntity = tvList.get(tvList.size() - 1);
				if (liveEntity.getLiveurl() != null
						&& liveEntity.getLiveurl() != "") {
					live_editor.putString(MatrixTVForeidgnView.tvUri,
							liveEntity.getLiveurl());
					live_editor.putString(MatrixTVForeidgnView.default_tvUri,
							liveEntity.getLiveurl());
				}
				if (liveEntity.getTv_name() != null
						&& liveEntity.getTv_name() != "") {
					live_editor.putString(MatrixTVForeidgnView.tvName,
							liveEntity.getTv_name());
					live_editor.putString(MatrixTVForeidgnView.default_tvName,
							liveEntity.getTv_name());
				}
				live_editor.putInt(MatrixTVForeidgnView.tvID,
						liveEntity.getTv_id());
				live_editor.putInt(MatrixTVForeidgnView.default_tvID,
						liveEntity.getTv_id());
				live_editor.putInt(MatrixTVForeidgnView.tvCP,
						liveEntity.getCp());
				live_editor.putInt(MatrixTVForeidgnView.default_tvCP,
						liveEntity.getCp());
				live_editor.putInt(MatrixTVForeidgnView.tvCPID,
						liveEntity.getCpId());
				live_editor.putInt(MatrixTVForeidgnView.default_tvCPID,
						liveEntity.getCpId());
				live_editor.putInt(MatrixTVForeidgnView.tvisVip,
						liveEntity.getIsVip());
				live_editor.putInt(MatrixTVForeidgnView.default_tvisVip,
						liveEntity.getIsVip());
				live_editor.putInt(MatrixTVForeidgnView.tvisFreeLimit,
						liveEntity.getIsFreeLimit());
				live_editor.putInt(MatrixTVForeidgnView.default_tvisFreeLimit,
						liveEntity.getIsFreeLimit());
				live_editor.commit();
			}
		} else {
			// 取的当前节目
			LiveStreamEntity currentEntity = new LiveStreamEntity();
			LiveStreamEntity defaultEntity = new LiveStreamEntity();
			int is_vip = live_sSharedPreferences.getInt(
					MatrixTVForeidgnView.tvisVip,
					MatrixTVForeidgnView.defaultIsVip);
			int tv_id = live_sSharedPreferences.getInt(
					MatrixTVForeidgnView.tvID, MatrixTVForeidgnView.defaultID);
			int isFreeLimit = live_sSharedPreferences.getInt(
					MatrixTVForeidgnView.tvisFreeLimit,
					MatrixTVForeidgnView.defaultisFreeLimit);
			int cpId = live_sSharedPreferences.getInt(
					MatrixTVForeidgnView.tvCPID,
					MatrixTVForeidgnView.defaultCPID);
			// 判断当前影片是否有效：
			boolean isEffective = false;
			MoviePermissionEntity mMoviePermissionEntity;
			// 是vip再进行操作
			if (is_vip == 1) {
				// 根据影厅id读取数据库
				ArrayList<MoviePermissionEntity> resultList = BlueLightVipUtil
						.query(null, "cpId = ?", new String[] { cpId + "" },
								null); // cpId为影厅id

				if (resultList != null && resultList.size() != 0) {
					mMoviePermissionEntity = resultList.get(0);
					if (System.currentTimeMillis()
							- mMoviePermissionEntity.getFirstPayTime() <= mMoviePermissionEntity
								.getEffectiveTime()) {
						isEffective = true;
					} else {
						isEffective = false;
					}
				} else {
					isEffective = false;
				}
				//限免状态下再操作
				if (isFreeLimit == 1) {
					if (!isEffective) {
						Log.v(TAG, "初始-未购买-限免");
						if (tvList != null && tvList.size() != 0) {
							defaultEntity = tvList.get(tvList.size() - 1);
							for (LiveStreamEntity entity : tvList) {
								if (tv_id == entity.getTv_id()) {
									currentEntity = entity;
								}
							}
						}
						if (currentEntity.getIsFreeLimit() == 1) {
							// 限时免费不做操作
							Log.v(TAG, "未购买-限免-不操作");
						} else {
							// 不限免后取一个默认数据播放，
							Log.v(TAG, "未购买-不限免-刷新数据");
							Intent intent = new Intent();
							intent.setAction(MatrixTVForeidgnView.ACTION_TV_FROM_LAUNCHER_S);
							intent.putExtra(MatrixTVForeidgnView.EXTRA_TV_NAME,
									defaultEntity.getTv_name());
							intent.putExtra(MatrixTVForeidgnView.EXTRA_URL,
									defaultEntity.getLiveurl());
							intent.putExtra(MatrixTVForeidgnView.EXTRA_TV_ID,
									defaultEntity.getTv_id());
							intent.putExtra(MatrixTVForeidgnView.EXTRA_CP,
									defaultEntity.getCp());
							intent.putExtra(MatrixTVForeidgnView.EXTRA_IS_VIP,
									defaultEntity.getIsVip());
							intent.putExtra(MatrixTVForeidgnView.EXTRA_CP_ID,
									defaultEntity.getCpId());
							intent.putExtra(
									MatrixTVForeidgnView.EXTRA_IS_FreeLimit,
									defaultEntity.getIsFreeLimit());
							sendBroadcast(intent);
						}
					}
				}
			}
		}
	}

	/**
	 * 请求背景图信息
	 */
	private void loadThemeData() {
		ArrayList<SkinEntity> bgUrlList = service
				.getPageSkins(getApplicationContext());
		// 预加载背景图
		if (bgUrlList.size() > 0) {
			ImageLoader.getInstance().loadImageSync(
					bgUrlList.get(bgUrlList.size() - 1).getImg_url_outside(),
					options);
		}

		PageSkinDAO skinDAO = new PageSkinDAO(getApplicationContext());
		if (bgUrlList.size() > 0) {
			skinDAO.delete(null, null);
			skinDAO.insert(bgUrlList);
		}

	}

	/**
	 * 请求首页的推荐，电视的推荐位数据信息并缓存到本地
	 */
	private void loadRecommendAndBlueData() {
		recommendList = new ArrayList<RecommendEntity>();
		bluelightList = new ArrayList<RecommendEntity>();
		// 推荐位数据
		ArrayList<AppFocusEntity> recomList = service.getAppFocusList("I1001",
				getResources().getString(R.string.language)); // 请求推荐位1
		// 极清数据
		ArrayList<AppFocusEntity> blueList = service.getAppFocusList("B1001",
				getResources().getString(R.string.language)); // 请求极清推荐位5
		if (null != recomList && recomList.size() != 0) {
			for (int i = 0; i < recomList.size(); i++) {
				AppFocusEntity appentity = recomList.get(i);
				RecommendEntity recomentity = new RecommendEntity();
				recomentity.setApkPackage(appentity.getApkPackage());
				recomentity.setClassFirstVo(appentity.getClassFirstVo());
				recomentity.setContentDesc(appentity.getContentDesc());
				recomentity.setContentId(appentity.getContentId());
				recomentity.setContentName(appentity.getContentName());
				recomentity.setContentType(appentity.getContentType());
				recomentity.setContentTypeName(appentity.getContentTypeName());
				recomentity.setCreatedTime(appentity.getCreatedTime());
				recomentity.setFocusLargeImg(appentity.getFocusLargeImg());
				recomentity.setFocusThumbImg(appentity.getFocusThumbImg());
				recomentity.setFocusType(appentity.getFocusType());
				recomentity.setId(appentity.getId());
				recomentity.setIntervalTime(appentity.getIntervalTime());
				recomentity.setIsApk(appentity.getIsApk());
				recomentity.setIsEffective(appentity.getIsEffective());
				recomentity.setIsIntranet(appentity.getIsIntranet());
				recomentity.setLauncherBg(appentity.getLauncherBg());
				recomentity.setLauncherId(appentity.getLauncherId());
				recomentity.setLauncherName(appentity.getLauncherName());
				recomentity.setLogic(appentity.getLogic());
				recomentity.setPositionId(appentity.getPositionId());
				recomentity.setPositionName(appentity.getPositionName());
				recomentity.setSeq(appentity.getSeq());
				recomentity.setShowType(appentity.getShowType());
				recomentity.setType(appentity.getType());
				recomentity.setUpdatedTime(appentity.getUpdatedTime());
				recomentity.setImgSize(appentity.getImgSize());
				recomentity.setVideoId(appentity.getVideoId());
				recomentity.setSubjectBgImg(appentity.getSubjectBgImg());
				recomentity.setCpId(appentity.getCpId());
				recommendList.add(recomentity);
			}
		}
		if (null != blueList && blueList.size() != 0) {
			for (int i = 0; i < blueList.size(); i++) {
				AppFocusEntity blueentity = blueList.get(i);
				RecommendEntity recomentity = new RecommendEntity();
				recomentity.setApkPackage(blueentity.getApkPackage());
				recomentity.setClassFirstVo(blueentity.getClassFirstVo());
				recomentity.setContentDesc(blueentity.getContentDesc());
				recomentity.setContentId(blueentity.getContentId());
				recomentity.setContentName(blueentity.getContentName());
				recomentity.setContentType(blueentity.getContentType());
				recomentity.setContentTypeName(blueentity.getContentTypeName());
				recomentity.setCreatedTime(blueentity.getCreatedTime());
				recomentity.setFocusLargeImg(blueentity.getFocusLargeImg());
				recomentity.setFocusThumbImg(blueentity.getFocusThumbImg());
				recomentity.setFocusType(blueentity.getFocusType());
				recomentity.setId(blueentity.getId());
				recomentity.setIntervalTime(blueentity.getIntervalTime());
				recomentity.setIsApk(blueentity.getIsApk());
				recomentity.setIsEffective(blueentity.getIsEffective());
				recomentity.setIsIntranet(blueentity.getIsIntranet());
				recomentity.setLauncherBg(blueentity.getLauncherBg());
				recomentity.setLauncherId(blueentity.getLauncherId());
				recomentity.setLauncherName(blueentity.getLauncherName());
				recomentity.setLogic(blueentity.getLogic());
				recomentity.setPositionId(blueentity.getPositionId());
				recomentity.setPositionName(blueentity.getPositionName());
				recomentity.setSeq(blueentity.getSeq());
				recomentity.setShowType(blueentity.getShowType());
				recomentity.setType(blueentity.getType());
				recomentity.setUpdatedTime(blueentity.getUpdatedTime());
				recomentity.setImgSize(blueentity.getImgSize());
				recomentity.setVideoId(blueentity.getVideoId());
				recomentity.setSubjectBgImg(blueentity.getSubjectBgImg());
				recomentity.setCpId(blueentity.getCpId());
				bluelightList.add(recomentity);
			}
		}
	}

	private void loadRecommendData() {
		RecommectTagEditor.clear();
		RecommectTagEditor.commit();
		loadRecommendAndBlueData();
		// 读取内外网标识的存储数据
		String outer = getSharedPreferences(HiveviewApplication.intranet,
				MODE_WORLD_READABLE).getString(HiveviewApplication.intranet,
				"-1");
		Log.d(TAG, "outer:::" + outer);
		// 预加载推荐页面的图片
		for (int i = 0; i < recommendList.size(); i++) {
			RecommendEntity entity = recommendList.get(i);
			Log.v(TAG, "entity==============recommendList" + entity.toString());
			// 更新信息
			if (!HiveviewApplication.isIntranet) {
				if (!outer.equals(String.valueOf(entity.getIsIntranet()))) {
					getSharedPreferences(HiveviewApplication.intranet,
							MODE_WORLD_READABLE)
							.edit()
							.putString(HiveviewApplication.intranet,
									String.valueOf(entity.getIsIntranet()))
							.commit();
				}
				HiveviewApplication.isIntranet = true;
				Log.d(TAG,
						HiveviewApplication.intranet + ":::"
								+ String.valueOf(entity.getIsIntranet()));
			}
			RecommectTagEditor.putString(String.valueOf(entity.getContentId()),
					entity.getIsApk());
			try {
				ImageLoader.getInstance().loadImageSync(
						entity.getFocusLargeImg(), options);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (entity.getPositionId().equals("1")) {
				ImageLoader.getInstance().loadImageSync(
						entity.getFocusThumbImg(), options);
			}
		}

		RecommendDAO recommendDAO = new RecommendDAO(getApplicationContext());

		// ArrayList<RecommendEntity> movieList =
		// service.getMovieRecommands(getApplicationContext());// 请求影院的推荐位数据
		// ArrayList<RecommendEntity> bluelightList =
		// service.getBluelightRecommands(getApplicationContext());// 请求影院的推荐位数据

		// 预加载极清页面的图片
		for (int i = 0; i < bluelightList.size(); i++) {
			RecommendEntity entity = bluelightList.get(i);
			Log.v(TAG, "entity==============bluelightList" + entity.toString());
			RecommectTagEditor.putString(String.valueOf(entity.getContentId()),
					entity.getIsApk());
		}
		RecommectTagEditor.commit();
		recommendDAO.delete(null, null);// 清空上次请求的推荐位数据
		recommendDAO.insert(recommendList, BaseDAO.MATRIX_RECOMMEND);
		// recommendDAO.insert(movieList, BaseDAO.MATRIX_MOVIE);
		recommendDAO.insert(bluelightList, BaseDAO.MATRIX_BLUELIGHT);

	}

	/**
	 * 请求点播频道的频道信息，如电影，电视剧，音乐，体育,并缓存到本地
	 */
	private void loadChannels() {
		ArrayList<FirstClassListEntity> channelList = service
				.getFirstClassList(getResources().getString(R.string.language));
		ChannelDAO channelDAO = new ChannelDAO(getApplicationContext());
		channelDAO.delete(null, null);
		channelDAO.insert(channelList);
	}

	/**
	 * 请求游戏，应用的推荐位信息，并缓存到本地
	 */
	private void loadAppFocusData() {
		ArrayList<AppFocusEntity> gameList = service.getAppFocusList("G1001",
				getResources().getString(R.string.language)); // 请求游戏推荐位1
		ArrayList<AppFocusEntity> appList = service.getAppFocusList("A1001",
				getResources().getString(R.string.language)); // 请求应用推荐位5
		ArrayList<AppFocusEntity> educationList = service.getAppFocusList(
				"E1001", getResources().getString(R.string.language)); // 请求教育推荐位3
		Log.d("TAG", "游戏数剧=》》" + gameList);
		Log.d("TAG", "应用数据=》》" + appList);
		Log.d("TAG", "教育数剧=》》" + educationList);

		AppFocusDAO appFocusDao = new AppFocusDAO(getApplicationContext());
		appFocusDao.delete(null, null);
		appFocusDao.insert(appList);

		GameFocusDAO gameFocusDao = new GameFocusDAO(getApplicationContext());
		gameFocusDao.delete(null, null);
		gameFocusDao.insert(gameList);
		Log.d("TAG", "GAMEDAO==>" + gameFocusDao.APP_ID);

		EducationDAO educationDAO = new EducationDAO(getApplicationContext());
		educationDAO.delete(null, null);
		educationDAO.insert(educationList);
		Log.d("TAG", "educationDAO==>" + educationDAO.APP_ID.toString());
	}
	/**
	 * 请求教育的推荐位信息，并缓存到本地
	 */
	// private void loadEducationData() {
	// ArrayList<EducationEntity> educationList =
	// service.getEducationRelatedList(3); // 请求游戏推荐位
	// // ArrayList<AppFocusEntity> appList = service.getAppFocusList(5); //
	// 请求应用推荐位
	//
	// EducationDAO educationDAO = new EducationDAO(getApplicationContext());
	// educationDAO.delete(null, null);
	// educationDAO.insert(educationList);
	//
	//
	// // AppFocusDAO appFocusDao = new AppFocusDAO(getApplicationContext());
	// // appFocusDao.delete(null, null);
	// // appFocusDao.insert(appList);
	//
	// // GameFocusDAO gameFocusDao = new GameFocusDAO(getApplicationContext());
	// // gameFocusDao.delete(null, null);
	// // gameFocusDao.insert(gameList);
	// }
}
