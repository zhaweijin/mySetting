package com.hiveview.tv.view;



import java.io.File;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.StorageDialogActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class AuxiliaryStorageView extends RelativeLayout{
	protected static final String TAG = "AuxiliaryStorageView";
	public static final String ACTION_STORAGE_MOUNT_STATUS_CHANGED = "com.hiveview.storage.ACTION_STORAGE_MOUNT_STATUS_CHANGED";
	public static final String STORAGE_MOUNT_STATUS_KEY = "storage_mount_status";
	public static final String DM1001_DM1002_USB_PATH = "/storage/external_storage/sda1";
	public static final String CVTE_USB_PATH = "/storage/external_storage/usb/usb1_1";
	
	private Context mContext;
	private ImageView ivMountState;
	
	public AuxiliaryStorageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public AuxiliaryStorageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public AuxiliaryStorageView(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	private void init(){
		long time = System.currentTimeMillis();
		ivMountState = new ImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		params.topMargin = 10;
		ivMountState.setImageResource(R.drawable.storage);
		this.addView(ivMountState, params);
		File dm1001_1002_file = new File(DM1001_DM1002_USB_PATH);
		File cvteFile = new File(CVTE_USB_PATH);
        if (dm1001_1002_file.exists() || cvteFile.exists()) {
        	ivMountState.setVisibility(VISIBLE);
        } else {
        	ivMountState.setVisibility(INVISIBLE);
        }
		addRegister();
		Log.d(TAG, "loadImage AuxiliaryStorageView::init " + (System.currentTimeMillis() - time));
	}	

	private void addRegister() {
		/*增加mount状态变化广播接收器*/
		IntentFilter storageFilter = new IntentFilter();
		storageFilter.addAction(ACTION_STORAGE_MOUNT_STATUS_CHANGED);
		storageFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		storageFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		storageFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		storageFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		storageFilter.addAction(Intent.ACTION_MEDIA_EJECT);
//		storageFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
//		storageFilter.addAction(Intent.ACTION_MEDIA_EJECT);
		storageFilter.addDataScheme("file");
		mContext.registerReceiver(storageReceiver, storageFilter);
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		setUnregister();
		super.finalize();
	}
	
	public void setUnregister(){
		mContext.unregisterReceiver(storageReceiver);
	}
	private BroadcastReceiver storageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.v(TAG, intent.getAction());
			if (intent.getAction().equals(ACTION_STORAGE_MOUNT_STATUS_CHANGED)) {
				int storageMountStatus = intent.getIntExtra(STORAGE_MOUNT_STATUS_KEY, 0);
				if (1 == storageMountStatus) {
					ivMountState.setVisibility(VISIBLE);
				} else {
					ivMountState.setVisibility(INVISIBLE);
				}
			}
			if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)
					|| intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)) {
				ivMountState.setVisibility(VISIBLE);
				
 				//弹出U盘对话界面，幺康说，不弹出外界存储浮层页面---
 				/*Intent storageIntent = new Intent(getContext(), StorageDialogActivity.class);
				storageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				storageIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				storageIntent.putExtra(StorageDialogActivity.KEY_STORAGTE_PATH, intent.getDataString());
				getContext().startActivity(storageIntent);*/
				
			}
			if (intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED)
					|| intent.getAction().equals(Intent.ACTION_MEDIA_EJECT)
					|| intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED)
					|| intent.getAction().equals(Intent.ACTION_MEDIA_BAD_REMOVAL)) {
				ivMountState.setVisibility(INVISIBLE);
			}
		}
	};
}
