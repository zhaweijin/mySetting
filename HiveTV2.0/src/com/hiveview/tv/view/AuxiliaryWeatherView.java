package com.hiveview.tv.view;

import java.util.ArrayList;

import com.hiveview.box.framework.image.ImageLoader;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.utils.StringUtils;
import com.hiveview.weather.service.WeatherLoadService;
import com.hiveview.weather.service.dao.WeatherDAO;
import com.hiveview.weather.service.entity.WeatherEntity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AuxiliaryWeatherView
 * @Description: 增加类显示天气状态图标
 * @author: ZhaiJianfeng
 * @date 2014-6-23 下午6:06:04
 * 
 */
@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class AuxiliaryWeatherView extends RelativeLayout {
	protected static final String TAG = "AuxiliaryWeatherView";
	/**
	 * @Fields NETWORK_AVAILABLE_DELAY_MS:天气数据轮询时间6个小时
	 */
	private static final int NETWORK_AVAILABLE_DELAY_MS = 360 * 60 * 1000;
	private static final int NETWORK_INAVAILABLE_DELAY_MS = 5 * 60 * 1000;
	private Context mContext;
	private ImageLoader loadView;
	private ImageView ivWeather;
	private TextView tvTemperature;
	private String weatherIconUrl = "";
	private static long nowDate = 0;
	private long nowTime = 10 * 60 * 1000;

	public AuxiliaryWeatherView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public AuxiliaryWeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public AuxiliaryWeatherView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		long time = System.currentTimeMillis();
		loadView = ImageLoader.getInstance();
		ivWeather = new ImageView(getContext());
		tvTemperature = new TextView(getContext());

		LayoutParams ivParams = new LayoutParams(34, 34);
		ivParams.addRule(RelativeLayout.ALIGN_LEFT);
		ivParams.topMargin = -2;
		ivWeather.setId(1);
		this.addView(ivWeather, ivParams);

		LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tvParams.addRule(RelativeLayout.RIGHT_OF, 1);
		tvParams.topMargin = 5;
		this.addView(tvTemperature, tvParams);
		addRegister();
		mRefreshWeatherHandler.sendEmptyMessage(0);
		Log.d(TAG, "loadImage AuxiliaryWeatherView::init  " + (System.currentTimeMillis() - time));
	}

	private void addRegister() {
		/* 增加天气广播接收器，当天气数据请求结束时触发 */
		IntentFilter weatherFilter = new IntentFilter();
		weatherFilter.addAction(WeatherLoadService.WEATHER_DATA_SUCCESS_ACTION);
		weatherFilter.addAction(WeatherLoadService.WEATHER_DATA_FAIL_ACTION);
		mContext.registerReceiver(weatherReceiver, weatherFilter);

		/* 增加网络状态监听器，当AuxiliaryNetworkView中检测到网络状态变化时发出广播 */
		IntentFilter netStatusFilter = new IntentFilter();
		netStatusFilter.addAction(AuxiliaryNetworkView.NET_STATUS_NOTIFY_ACTION);
		mContext.registerReceiver(netStatusReceiver, netStatusFilter);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		setUnregister();
		super.finalize();
	}

	public void setUnregister() {
		if (null != netStatusReceiver) {
			mContext.unregisterReceiver(weatherReceiver);
			mContext.unregisterReceiver(netStatusReceiver);
		}
	}

	private void requestWeatherData() {
		Log.d("WeatherService", "根据地区信息获取接口::" + TAG);
		try {
			/*
			 * mContext.getSharedPreferences("W", 0) .edit() .putString(new
			 * java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new
			 * java.util.Date(System.currentTimeMillis())) + "",
			 * "time").commit();
			 */
			Intent intent = new Intent();
			intent.setAction(WeatherLoadService.WEATHER_DATA_ACTION);
			mContext.startService(intent);
		} catch (Exception e) {

		}
	}

	private void loadWeatherData() {
		WeatherDAO weatherDAO = new WeatherDAO(mContext.getApplicationContext());
		ArrayList<WeatherEntity> weatherList = weatherDAO.query(null, null, null, null);
		if (weatherList.size() < 1)
			return;
		WeatherEntity entity = weatherList.get(0);
		setWeatherData(entity);
	}

	private void setWeatherData(WeatherEntity entity) {
		Log.i(TAG, "setWeatherData-->function in.");
		Log.i(TAG, "setWeatherData-->entity.getTemperature():" + entity.getTemperature());
		tvTemperature.setText(entity.getTemperature());
		Log.i(TAG, "setWeatherData-->entity.getDayIcon():" + entity.getDayIcon());
		/* 图片相同是不刷新图片 */
		if (entity.getDayIcon() != null && !weatherIconUrl.equals(entity.getDayIcon())) {
			weatherIconUrl = entity.getDayIcon();
			loadView.displayImage(entity.getDayIcon(), ivWeather);
		}
		Log.i(TAG, "setWeatherData-->function out.");
	}

	private BroadcastReceiver weatherReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {

			if (intent.getAction().equals(WeatherLoadService.WEATHER_DATA_SUCCESS_ACTION)) {
				loadWeatherData();

			} else if (intent.getAction().equals(WeatherLoadService.WEATHER_DATA_FAIL_ACTION)) {
				loadWeatherData();
			}
		}
	};
	private BroadcastReceiver netStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {

			if (intent.getAction().equals(AuxiliaryNetworkView.NET_STATUS_NOTIFY_ACTION)) {
				mRefreshWeatherHandler.sendEmptyMessage(0);
			}
		}
	};

	private Handler mRefreshWeatherHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i(TAG, "handleMessage-->start");
			long refreshDelayMs = NETWORK_AVAILABLE_DELAY_MS;
			if (AuxiliaryNetworkView.WIFI_STATE_EABLED == HiveviewApplication.getNetStatus()
					|| AuxiliaryNetworkView.ETHERNET_STATE_EABLED == HiveviewApplication.getNetStatus()) {
				refreshDelayMs = NETWORK_AVAILABLE_DELAY_MS;
				Log.i(TAG, "handleMessage-->m::");
				long nowDateTime = StringUtils.getTime(StringUtils.getNowDate());
				if (nowDateTime - nowDate > nowTime) {
					requestWeatherData();
					nowDate = StringUtils.getTime(StringUtils.getNowDate());
				}
			}
			mRefreshWeatherHandler.removeMessages(0);
			mRefreshWeatherHandler.sendEmptyMessageDelayed(0, refreshDelayMs);
			Log.i(TAG, "handleMessage-->end");
		}
	};

}
