package com.hiveview.tv.view;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.utils.OnDialogClickListener;

public class HiveViewNetFaultDialog extends Dialog implements android.view.View.OnClickListener {
	/**
	 * TAG
	 */
	private static final String TAG = "HiveViewNetFaultDialog";
	private OnDialogClickListener clickListener;
	private LinearLayout tvConfirm_l;
	private LinearLayout tvCancel_l;
	private TextView tvConfirm;
	private TextView tvCancel;

	public HiveViewNetFaultDialog(Context context, OnDialogClickListener clickListener) {
		super(context, R.style.AlphaTextDialog);
		this.clickListener = clickListener;
		init(context);
	}

	public HiveViewNetFaultDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public HiveViewNetFaultDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public void setTitleContent(String content) {
		TextView tvContent = (TextView) this.findViewById(R.id.tv_content);
		tvContent.setText(content);
	};

	public void setButtonsText(String confirmText, String cancelText) {
		tvConfirm.setText(confirmText);
		tvCancel.setText(cancelText);
	}

	private void init(Context context) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_net_fault_layout);
		tvConfirm_l = (LinearLayout) this.findViewById(R.id.tv_confirm_l);
		tvCancel_l = (LinearLayout) this.findViewById(R.id.tv_cancel_l);
		tvConfirm = (TextView) this.findViewById(R.id.tv_confirm);
		tvCancel = (TextView) this.findViewById(R.id.tv_cancel);
		tvConfirm_l.setOnClickListener(this);
		tvCancel_l.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick.......................................");
		if (v == tvCancel_l) {
			clickListener.onCancel();
		} else if (v == tvConfirm_l) {
			clickListener.onConfirm();
		}

	}

	@Override
	public void show() {
		findViewById(R.id.tv_confirm_l).requestFocus();
		super.show();
	}

}
