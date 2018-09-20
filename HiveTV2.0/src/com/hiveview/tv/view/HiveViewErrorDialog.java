package com.hiveview.tv.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.utils.AppUtil;
import com.hiveview.tv.utils.ClientErrorDataCenter;
import com.hiveview.tv.utils.DeviceSN;
import com.hiveview.tv.utils.DeviceBoxUtils;

public class HiveViewErrorDialog extends Dialog implements View.OnClickListener, View.OnKeyListener {

	private String errorCode;
	private Context mContext = null;
	private boolean isCloseActivity = false;

	public HiveViewErrorDialog(Context context, String eCode, boolean isClose) {
		super(context, R.style.AlphaTextDialog);
		errorCode = eCode;
		mContext = context;
		isCloseActivity = isClose;
		init(context);
	}

	public HiveViewErrorDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	private void init(Context context) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_error_layout);
		View closeView = findViewById(R.id.v_close);

		// 单击关闭按钮监听
		closeView.setOnClickListener(this);
		closeView.setOnKeyListener(this);

		// 显示盒子的MAC地址
		TextView tvMac = (TextView) findViewById(R.id.tv_mac_text);

		tvMac.setText(String.format(context.getString(R.string.dialog_mac_text), DeviceBoxUtils.getMac()));

		// 显示盒子的SN地址
		TextView tvSn = (TextView) findViewById(R.id.tv_sn_text);
		tvSn.setText(String.format(context.getString(R.string.dialog_sn_text), DeviceBoxUtils.getSn()));

		// 显示错误码
		TextView tvErrorCode = (TextView) findViewById(R.id.tv_error_code_text);
		tvErrorCode.setText(String.format(context.getString(R.string.dialog_error_code_text), errorCode));

		// 显示错误码对应的内容
		TextView tvErrorText = (TextView) findViewById(R.id.tv_error_text);
		tvErrorText.setText(ClientErrorDataCenter.getInstance().getErrorContentByErrorCode(errorCode));

	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.v_close) {
			dismiss();
			if (isCloseActivity) {
				((Activity) mContext).finish();
			}
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (v.getId() == R.id.v_close && event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
			((Activity) mContext).finish();
		}
		return false;
	}

}
