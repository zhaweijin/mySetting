package com.hiveview.tv.activity;

import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.utils.OnDialogClickListener;
import com.hiveview.tv.view.AuxiliaryNetworkView;
import com.hiveview.tv.view.HiveViewNetFaultDialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NetExceptionActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout tvConfirm_l;
	private LinearLayout tvCancel_l;
	private TextView tvConfirm;
	private TextView tvCancel;
	private TextView tvTitle;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.dialog_net_fault_layout);
		tvConfirm_l = (LinearLayout) this.findViewById(R.id.tv_confirm_l);
		tvCancel_l = (LinearLayout) this.findViewById(R.id.tv_cancel_l);
		tvConfirm = (TextView) this.findViewById(R.id.tv_confirm);
		tvCancel = (TextView) this.findViewById(R.id.tv_cancel);
		tvTitle=(TextView) this.findViewById(R.id.tv_content);
		tvConfirm_l.setOnClickListener(this);
		tvCancel_l.setOnClickListener(this);
		tvTitle.setText(getResources().getString(R.string.net_exception_message));
		tvConfirm.setText(getResources().getString(R.string.net_exception_setting));
		tvCancel.setText(getResources().getString(R.string.net_exception_cancel));
		tvConfirm_l.requestFocus();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvCancel_l) {
			finish();
		} else if (v == tvConfirm_l) {
			Intent intent = new Intent();
			intent.setAction("com.hiveview.settings.ACTION_SETTING");
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			int netStatus = HiveviewApplication.getNetStatus();
			intent.putExtra(AuxiliaryNetworkView.CONNECTION_STATUS, netStatus);
			startActivity(intent);
			finish();
		}
	}
}
