package com.hiveview.tv.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.utils.OnDialogClickListener;

public class CancelCollectDialog extends Dialog implements android.view.View.OnClickListener {

	private OnDialogClickListener clickListener;
	private View vOk;
	private View tvCancel;
	/**
	 * 是否显示清除观看记录对话框
	 */
	private static boolean isShow=false;

	public CancelCollectDialog(Context context, OnDialogClickListener clickListener) {
		super(context, R.style.AlphaTextDialog);
		this.clickListener = clickListener;
		init(context);
	}

	public CancelCollectDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public CancelCollectDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public void setTitleContent(String content) {
		TextView tvContent = (TextView) this.findViewById(R.id.tv_content);
		tvContent.setText(content);
	};
	
	/**
	 * 设置对话框的内容文字信息
	 * @param message
	 */
	public void setMessageContent(String message){
		TextView tvContent = (TextView) this.findViewById(R.id.app_store_popop_message_text);
		tvContent.setText(message);
	}

	private void init(Context context) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.cancel_collect_layout);
		vOk = findViewById(R.id.app_store_popop_ok_layout);
		tvCancel = findViewById(R.id.app_store_popop_cancel_layout);
		vOk.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View v) {
		if (v == tvCancel) {
			clickListener.onCancel();
		} else if (v == vOk) {
			clickListener.onConfirm();
		}

	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		isShow=false;
	}
	
	@Override
	public void show() {
		super.show();
		isShow=true;
		vOk.requestFocus();
	}

	public  boolean  isDialogShow(){
		return isShow;
	}
	public void setDialogShow(boolean boo){
		isShow=boo;
	}
}
