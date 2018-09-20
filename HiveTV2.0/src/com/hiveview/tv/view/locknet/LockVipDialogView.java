package com.hiveview.tv.view.locknet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.common.deviceinfo.device.Device;
import com.hiveview.tv.common.deviceinfo.device.DeviceFactory;

public class LockVipDialogView extends RelativeLayout{

	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 主容器
	 */
	private View rlMain;
	
	private static Dialog lockNetDialog = null;
	
	private View lockNetView = null;
	
	@SuppressLint("NewApi")
	public LockVipDialogView(Context context ,Bitmap bgScreenBitmap) {
		super(context);
		mContext = context;
		if(null == lockNetView){
			lockNetView = View.inflate(mContext, R.layout.lock_vip_layout, null);
			// 主容器
			rlMain = lockNetView.findViewById(R.id.rl_locknet_main);
			if (null != bgScreenBitmap) {
				rlMain.setBackground(new BitmapDrawable(mContext.getResources(), bgScreenBitmap));
			}
			// 创建dialog
//			lockNetDialog = new Dialog(mContext, R.style.lock_net_transparent);
//			lockNetDialog.setContentView(v);
		}
//		return lockNetDialog;
		this.addView(lockNetView);
	}


	/**
	 * @Title: LockNetDialogView
	 * @author:郭松胜
	 * @Description: TODO
	 * @param context
	 * @param bgScreenBitmap
	 * @return
	 */
	@SuppressLint("NewApi")
	public Dialog createLoadingDialog(Bitmap bgScreenBitmap) {
		if(null == lockNetDialog){
			View v = View.inflate(mContext, R.layout.lock_net_work_layout, null);
			// 主容器
			rlMain = v.findViewById(R.id.rl_locknet_main);
			if (null != bgScreenBitmap) {
				rlMain.setBackground(new BitmapDrawable(mContext.getResources(), bgScreenBitmap));
			}
			// 创建dialog
			lockNetDialog = new Dialog(mContext, R.style.lock_net_transparent);
			lockNetDialog.setContentView(v);
		}
		return lockNetDialog;

	}


}