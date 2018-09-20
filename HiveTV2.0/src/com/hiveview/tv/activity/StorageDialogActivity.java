/**
 * @Title StorageDialogActivity.java
 * @Package com.hiveview.tv.activity
 * @author ZhaiJianfeng
 * @date 2014-8-12 下午1:11:24
 * @Description 外接存储界面
 * @version V1.0
 */
package com.hiveview.tv.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hiveview.tv.R;
import com.hiveview.tv.utils.AnimationUtil;
import com.hiveview.tv.utils.FocuseWrapper;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @ClassName: StorageDialogActivity
 * @Description:外接存储对话框
 * @author: ZhaiJianfeng
 * 
 * @date 2014-8-12 下午1:11:24
 * 
 */
public class StorageDialogActivity extends BaseActivity{
	private static final String TAG = "StorageDialogActivity";
	public static final String KEY_STORAGTE_PATH = "KEY_STORAGE_PATH";
	private final int START_INDEX = 7;
	/**
	 * U盘文件夹父文件夹
	 */
	private String mStorageDir = "";
	/**
	 * activity退出标志，当退出代码强制退出程序时设置为true,确保焦点不会框不会变化
	 */
	private boolean exitFlag = false;
	/**
	 * U盘列表显示viewpager
	 */
	//private ViewPager vpStorageNames;
	/**
	 * u盘列表适配器
	 */
	//MyAdapter mAdapter;

	/**
	 * 页面所有View的容器
	 */
	private RelativeLayout mContainer;
	/**
	 * U盘列表版块的View容器
	 */
	private LinearLayout llStorageNameList;
	/**
	 * 全部版块的View容器
	 */
	private LinearLayout llStorageAll;
	private RelativeLayout rlStorageAllIcon;
	/**
	 * Picture版块的View容器
	 */
	private LinearLayout llStoragePicture;
	private RelativeLayout rlStoragePictureIcon;
	/**
	 * Music版块的View容器
	 */
	private LinearLayout llStorageMusic;
	private RelativeLayout rlStorageMusicIcon;
	/**
	 * Video版块的View容器
	 */
	private LinearLayout llStorageVideo;
	private RelativeLayout rlStorageVideoIcon;

	private View focusView;
	// private View contentFocusView;
	List<String> deviceNameList = new ArrayList<String>();
	/**
	 * 显示u盘名称的viewpager的count系数，形成假的无限循环
	 */
	private static final int SIZE = 1000;

	public interface onDemoLayoutFocusListener {

		void onDemoLayoutFocused(View view);

	}
	
	private FocuseWrapper wrapper;

	public void onFlyFocused(View view, float x, float y) {
		float destX = x - 18;
		float destY = y - 23;
		int duration = 100;
//		FocuseWrapper wrapper;
		if(wrapper == null){
			wrapper = new FocuseWrapper(focusView);
		}
		int addWidth = 40;
		int addHeight = 53;

		AnimatorSet set = new AnimatorSet();
		ObjectAnimator xAnimator = ObjectAnimator.ofFloat(focusView, "translationX", destX).setDuration(duration);
		ObjectAnimator yAnimator = ObjectAnimator.ofFloat(focusView, "translationY", destY).setDuration(duration);
		ObjectAnimator widthAnimator = ObjectAnimator.ofInt(wrapper, "width", wrapper.getWidth(), view.getWidth() + addWidth).setDuration(duration);
		ObjectAnimator heightAnimator = ObjectAnimator.ofInt(wrapper, "height", wrapper.getHeight(), view.getHeight() + addHeight).setDuration(duration);
		set.playTogether(xAnimator, yAnimator, widthAnimator, heightAnimator);
		set.start();
		focusView.bringToFront();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		Log.i(TAG,"onCreate---");
		super.onCreate(arg0);
		setContentView(R.layout.activity_storage_dialog);
		String uPath = getIntent().getStringExtra(KEY_STORAGTE_PATH);
		if (uPath == null || uPath.equals("")) {
			Log.e(TAG, "onCreate-->filePaht error");
			return;
		}
		addRegister();
		mStorageDir = uPath.substring(START_INDEX, uPath.lastIndexOf("/"));
		initView();

	}

	/**
	 * 
	 * @Title: StorageDialogActivity
	 * @author:ZhaiJianfeng
	 * @Description: 初始化视图
	 */
	private void initView() {
		/* 获取各控件句柄 */
		mContainer = (RelativeLayout) findViewById(R.id.ll_storage_show);
		llStorageAll = (LinearLayout) findViewById(R.id.ll_storage_all);
		rlStorageAllIcon = (RelativeLayout) findViewById(R.id.rl_storage_all_icon);
		llStoragePicture = (LinearLayout) findViewById(R.id.ll_storage_picture);
		rlStoragePictureIcon = (RelativeLayout) findViewById(R.id.rl_storage_picture_icon);
		llStorageMusic = (LinearLayout) findViewById(R.id.ll_storage_music);
		rlStorageMusicIcon = (RelativeLayout) findViewById(R.id.rl_storage_music_icon);
		llStorageVideo = (LinearLayout) findViewById(R.id.ll_storage_video);
		rlStorageVideoIcon = (RelativeLayout) findViewById(R.id.rl_storage_video_icon);
		focusView = findViewById(R.id.matrix_tv_focus_layout);
		llStorageAll.setOnFocusChangeListener(OnFocusChangeListenerImpl);
		llStoragePicture.setOnFocusChangeListener(OnFocusChangeListenerImpl);
		llStorageVideo.setOnFocusChangeListener(OnFocusChangeListenerImpl);
		llStorageMusic.setOnFocusChangeListener(OnFocusChangeListenerImpl);
		llStorageAll.setOnClickListener(OnClickListenerImpl);
		llStoragePicture.setOnClickListener(OnClickListenerImpl);
		llStorageVideo.setOnClickListener(OnClickListenerImpl);
		llStorageMusic.setOnClickListener(OnClickListenerImpl);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(llStoragePicture != null && exitFlag == false){
			llStoragePicture.requestFocus();
		}
	}

	/**
	 * 焦点变化监听
	 */
	private OnFocusChangeListener OnFocusChangeListenerImpl = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.startAnimation(AnimationUtil.getBigAnimation(StorageDialogActivity.this));
				switch (v.getId()) {
				case R.id.ll_storage_all:
					onFlyFocused(rlStorageAllIcon, llStorageAll.getX(), rlStorageAllIcon.getY());
					break;
				case R.id.ll_storage_picture:
					onFlyFocused(rlStoragePictureIcon, llStoragePicture.getX(), rlStoragePictureIcon.getY());
					break;
				case R.id.ll_storage_music:
					onFlyFocused(rlStorageMusicIcon, llStorageMusic.getX(), rlStorageMusicIcon.getY());
					break;
				case R.id.ll_storage_video:
					onFlyFocused(rlStorageVideoIcon, llStorageVideo.getX(), rlStorageVideoIcon.getY());
					break;
				default:
					break;
				}
			} else {
				v.startAnimation(AnimationUtil.getLitterAnimation(StorageDialogActivity.this));
			}
			v.bringToFront();
			mContainer.invalidate();
		}
	};

	/**
	 * click监听
	 */
	private OnClickListener OnClickListenerImpl = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				Intent it = new Intent();
				switch (v.getId()) {
				case R.id.ll_storage_all:/*全部文件*/
					it.putExtra("file_type", 1004);
					break;
				case R.id.ll_storage_picture:/*图片*/
					it.putExtra("file_type", 1003);
					break;
				case R.id.ll_storage_music:/*音乐*/
					it.putExtra("file_type", 1002);
					break;
				case R.id.ll_storage_video:/*视频*/
					it.putExtra("file_type", 1001);
					break;
				default:
					break;
				}
				it.setAction("com.hiveview.externalstorage.action.APP_HOME");//外接存储主界面的activity的action
				startActivity(it);
				exitFlag = true;
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG,"onStart----");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG,"onResume----");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG,"onPause----");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG,"onStop----");
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG,"onDestroy----");
		super.onDestroy();
		if(mountStatusReceiver != null){
			this.unregisterReceiver(mountStatusReceiver);
		}
	}

	private void addRegister() {
		/* 增加mount状态变化广播接收器 */
		IntentFilter mountFilter = new IntentFilter();
		mountFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		mountFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		/*
		 * mountFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
		 * mountFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		 */
		mountFilter.addDataScheme("file");
		this.registerReceiver(mountStatusReceiver, mountFilter);
	}
	/**
	 * 
	 * @Title: StorageDialogActivity
	 * @author:ZhaiJianfeng
	 * @Description: 获取u盘列表
	 */
	private void getMountList() {
		Log.i(TAG, "getMountList----");
		File dir = new File(mStorageDir);
		File[] files = dir.listFiles();
		deviceNameList.clear();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				if (files[i].getName().lastIndexOf("sdcard") >= 0) {
					continue;
				}
				deviceNameList.add(files[i].getName());
			}
		}
		Log.i(TAG,"deviceNameList.size()=="+deviceNameList.size());
		if(deviceNameList.size()<1){/*如果获取到u盘列表长度为0，则说明当前没有插入u盘，finish当前activity*/
			exitFlag = true;
			this.finish();
		}	
	}
	private BroadcastReceiver mountStatusReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {

/*			if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED) || intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)) {
				String mountPaht = intent.getDataString();
				mCurrentMountName = mountPaht.substring(mountPaht.lastIndexOf("/") + 1, mountPaht.length());
				getMountList();
			}*/
			if (intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED) || intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)) {
				getMountList();
			}
		}
	};

}
