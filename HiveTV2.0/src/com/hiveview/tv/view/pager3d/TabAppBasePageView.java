package com.hiveview.tv.view.pager3d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.AttributeSet;

import com.hiveview.tv.view.AppRecommendView;

@SuppressLint("UseSparseArrays")
public abstract class TabAppBasePageView extends TabBasePageView {

	/**
	 * 存储App推荐位的bug
	 */
	protected HashMap<Integer, AppRecommendView> mapDownload = new HashMap<Integer, AppRecommendView>();

	private DownloadProgressRecevier recevier;

	public TabAppBasePageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TabAppBasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabAppBasePageView(Context context, RecommendViewFocusDirectionListener listener) {
		super(context, listener);
		initRecevier();
	}

	public TabAppBasePageView(Context context) {
		super(context);
		initRecevier();
	}

	private void initRecevier() {
		recevier = new DownloadProgressRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction(HomeActions.ACTION_DOWNLOAD);// 注册App下载进度的Action
		filter.addAction(HomeActions.ACTION_INSTALL_APP_SUCCESS);// 注册App安装成功的Action
		filter.addAction(HomeActions.ACTION_INSTALL_APP_FAIL);// 注册App安装失败的Action
		filter.addAction(HomeActions.ACTION_DOWNLOAD_PAUSE);//注册App下载中断网暂停广播
		getContext().registerReceiver(recevier, filter);
	}
	
	public void setUnregister(){
		getContext().unregisterReceiver(recevier);
	}
	
	@Override
	protected void finalize() throws Throwable {
		getContext().unregisterReceiver(recevier);
	}

	/**
	 * 下载安装广播回调此方法
	 * 
	 * @Title: TabAppBasePageView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param intent
	 */
	public abstract void onAppStoreReceive(Intent intent);

	/**
	 * 下载安装的注册广播
	 * 
	 * @ClassName: DownloadProgressRecevier
	 * @Description: TODO
	 * @author: 张鹏展
	 * @date 2014-6-22 下午3:29:19
	 * 
	 */
	class DownloadProgressRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			onAppStoreReceive(intent);
		}
	}

	@Override
	public void startPageViewCustomAnimation() {
		super.startPageViewCustomAnimation();
		// 当前页是用户选择的页，此时的下载和安装动画，应该动起来
		Iterator<Entry<Integer, AppRecommendView>> iter = mapDownload.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, AppRecommendView> entry = iter.next();
			AppRecommendView recommendView = entry.getValue();
			recommendView.startInstallAnim();
			recommendView.startUpdateProgress();
		}
	}

	@Override
	public void stopPageViewCustomAnimation() {
		super.stopPageViewCustomAnimation();
		// 当前页不是用户只面对的页面，此时的下载和安装动画，从体验角度考虑应该停止，否则页面会卡顿
		Iterator<Entry<Integer, AppRecommendView>> iter = mapDownload.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, AppRecommendView> entry = iter.next();
			AppRecommendView recommendView = entry.getValue();
			recommendView.stopInstallAnim();// 停止安装东阿虎
			recommendView.stopUpdateProgress();// 停止下载进度更新
		}
	}
	
	
	
	
	

}
