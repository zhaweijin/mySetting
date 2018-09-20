package com.hiveview.cloudtv.settings.widget;

import com.hiveview.cloudtv.settings.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;


public class ResolutionDialog extends Dialog {
	private Context mContext;
	private LayoutInflater mFactory = null;
	private View mView = null;

	public ResolutionDialog(Context context) {
		super(context, R.style.CustomProgressNewDialog);
		mContext = context;
		mFactory = LayoutInflater.from(mContext);
		mView = mFactory.inflate(R.layout.diy_reset, null);

		final WindowManager.LayoutParams WMLP = this.getWindow().getAttributes();
		WMLP.gravity = Gravity.CENTER;
		this.getWindow().setAttributes(WMLP);

		this.setContentView(mView);
	}

}