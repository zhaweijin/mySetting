package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hiveview.tv.R;
import com.hiveview.tv.utils.ToastUtils;

public class AppRecommendView extends LinearLayout {

	/**
	 * 应用推荐位图
	 */
	private ImageView imageView;

	/**
	 * 下载进度条
	 */
	private RelativeLayout rLayout;
	private ProgressBar progressBar;

	/**
	 * 安装动画条
	 */
	private InstallProgressView installProgressView;

	/**
	 * 是否更新进度
	 */
	private boolean isUpdateProgress = true;

	public AppRecommendView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AppRecommendView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AppRecommendView(Context context) {
		super(context);
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.app_recommend_layout, null);
		addView(contentView);
		imageView = (ImageView) findViewById(R.id.imageView1);
		progressBar = (ProgressBar) findViewById(R.id.appDownloadIcon);
		rLayout=(RelativeLayout) findViewById(R.id.appDownloadIcon_layout);
		installProgressView = (InstallProgressView) findViewById(R.id.appDownloadSetUpIcon);
	}

	public ImageView getImageView() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ScaleType.FIT_XY);
		return imageView;
	}

	public void initProgressView(int width) {
		// 设置下载进度条的在推荐位显示位置
		RelativeLayout.LayoutParams paramsBar = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 20);
		paramsBar.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.imageView1);
		paramsBar.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		paramsBar.bottomMargin = 20;
		paramsBar.leftMargin = 25;
		paramsBar.rightMargin = 25;
		rLayout.setLayoutParams(paramsBar);
		rLayout.setLayoutParams(paramsBar);
		progressBar.setMax(100);

		// 设置安装进度条的在推荐位显示位置
		RelativeLayout.LayoutParams paramsInstall = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 20);
		paramsInstall.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.imageView1);
		paramsInstall.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		paramsInstall.bottomMargin = 20;
		paramsInstall.leftMargin = 25;
		paramsInstall.rightMargin = 25;
		installProgressView.setLayoutParams(paramsInstall);

	}

	/**
	 * 设置app下载进度
	 * 
	 * @Title: AppRecommendView
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param progress
	 */
	public void setProgress(int progress) {
		if (progressBar.getVisibility() != View.VISIBLE) {
			progressBar.setVisibility(View.VISIBLE);
			rLayout.setVisibility(View.VISIBLE);
		}

		if (isUpdateProgress || progress == 100) {
			progressBar.setProgress(progress);

			if (progress == 100 && isUpdateProgress) {// 处在用户选择的页面，安装动画开始执行
				progressBar.setVisibility(View.INVISIBLE);
				rLayout.setVisibility(View.INVISIBLE);
				installProgressView.setVisibility(View.VISIBLE, true);
			} else if (progress == 100 && !isUpdateProgress) {// 处在用户不选择的页面，安装动画显示，但是动画不执行
				progressBar.setVisibility(View.INVISIBLE);
				rLayout.setVisibility(View.INVISIBLE);
				installProgressView.setVisibility(View.VISIBLE, false);
			}
		}
	}

	/**
	 * 隐藏安装进度条
	 * 
	 * @Title: AppRecommendView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void hideInstallProgressView() {
		rLayout.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		installProgressView.setVisibility(View.INVISIBLE);
	}

	public void hideDownloadProgressView() {
		rLayout.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
		
	}

	/**
	 * 停止安装动画的执行
	 * 
	 * @Title: AppRecommendView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void stopInstallAnim() {
		installProgressView.stopInstallAnim();
	}

	/**
	 * 停止更新下载进度
	 * 
	 * @Title: AppRecommendView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void stopUpdateProgress() {
		isUpdateProgress = false;
	}

	/**
	 * 可以更新进度
	 * 
	 * @Title: AppRecommendView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void startUpdateProgress() {
		isUpdateProgress = true;
	}

	/**
	 * 启动安装动画的执行
	 * 
	 * @Title: AppRecommendView
	 * @author:陈丽晓
	 * @Description: TODO
	 */
	public void startInstallAnim() {
		Log.v("aaaa", "启动安装动画的执行");
		if (installProgressView.getVisibility() == View.VISIBLE) {
			installProgressView.startInstallAnim();
		}
	}

}
