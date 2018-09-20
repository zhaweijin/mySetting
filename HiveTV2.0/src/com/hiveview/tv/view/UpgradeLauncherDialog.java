package com.hiveview.tv.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.SYSUpDataEntity;
import com.hiveview.tv.utils.TypefaceUtils;

public class UpgradeLauncherDialog extends Dialog {
	
	private SYSUpDataEntity launcherEntity;
	private DataUpdateCallBack callback;

	public UpgradeLauncherDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	protected UpgradeLauncherDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public UpgradeLauncherDialog(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		
		setContentView(R.layout.dialog_launcher_upgrade);
		final TextView tvTitleName = (TextView) findViewById(R.id.tv_app_dialog_title_name);
		final TextView tvContent = (TextView) findViewById(R.id.tv_app_dialog_content);

		TextView tv_dialog_cancel 	= (TextView) findViewById(R.id.tv_dialog_cancel);
		TextView tv_dialog_confirm 	= (TextView) findViewById(R.id.tv_dialog_confirm);
		TextView tv_dialog_confirm_middle 	= (TextView) findViewById(R.id.tv_dialog_confirm_middle);
		
		tv_dialog_cancel.setText(R.string.later_upgrade);
		tv_dialog_confirm.setText(R.string.upgrade);
		tv_dialog_confirm_middle.setText(R.string.upgrade);
		
		tv_dialog_cancel.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_dialog_confirm.setTypeface(TypefaceUtils.getStandardfFontFace());
		tv_dialog_confirm_middle.setTypeface(TypefaceUtils.getStandardfFontFace());
		tvTitleName.setTypeface(TypefaceUtils.getStandardfFontFace());
		tvContent.setTypeface(TypefaceUtils.getStandardfFontFace());
		
		final LinearLayout ll_dialog_cancel	= (LinearLayout) findViewById(R.id.ll_dialog_cancel);
		final LinearLayout ll_dialog_confirm = (LinearLayout) findViewById(R.id.ll_dialog_confirm);
		final LinearLayout ll_dialog_confirm_middle = (LinearLayout) findViewById(R.id.ll_dialog_confirm_middle);
		
		ll_dialog_cancel.setOnClickListener(cacleClickListener);
		ll_dialog_confirm.setOnClickListener(upgradeClickListener);
		ll_dialog_confirm_middle.setOnClickListener(upgradeClickListener);
		
		callback = new DataUpdateCallBack() {
			
			@Override
			public void onExecuteUpdateInfo(SYSUpDataEntity entity) {
				tvTitleName.setText("有新版本" + entity.getVersion());
				tvContent.setText(entity.getFeatures());
			}
			
			@Override
			public void onExecuteUpdateModel(boolean isOptional) {
				
				if(!isOptional){
					ll_dialog_cancel.setVisibility(View.GONE);
					ll_dialog_confirm.setVisibility(View.GONE);
					ll_dialog_confirm_middle.setVisibility(View.VISIBLE);
				}else{
					ll_dialog_cancel.setVisibility(View.VISIBLE);
					ll_dialog_confirm.setVisibility(View.VISIBLE);
					ll_dialog_confirm_middle.setVisibility(View.GONE);
				}
			}
		};

	}
	
	private interface DataUpdateCallBack{
		void onExecuteUpdateInfo(SYSUpDataEntity entity);
		void onExecuteUpdateModel(boolean isOptional);
	}
	
	public void setLauncherData(SYSUpDataEntity launcherEntity){
		this.launcherEntity = launcherEntity;
		callback.onExecuteUpdateInfo(launcherEntity);
	}
	
	public void setOptionalModel(boolean isOptional){
		callback.onExecuteUpdateModel(isOptional);
	}
	
	public SYSUpDataEntity getLauncherData(){
		return launcherEntity;
	}
	
	private android.view.View.OnClickListener upgradeClickListener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO 升级界面接口
		}
	};
	
	private android.view.View.OnClickListener cacleClickListener = new android.view.View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			dismiss();
		}
	};
	
}
