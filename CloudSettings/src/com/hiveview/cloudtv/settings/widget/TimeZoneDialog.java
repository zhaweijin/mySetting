/**
 * 
 */
package com.hiveview.cloudtv.settings.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.hiveview.cloudtv.settings.R;

/**
 * @author dell-pc
 *
 */
public class TimeZoneDialog extends Dialog{
	private Context mContext;
	private LayoutInflater mFactory = null;
	private View mView = null;

	public TimeZoneDialog(Context context) {
		super(context, R.style.CustomProgressNewDialog);
		mContext = context;
		mFactory = LayoutInflater.from(mContext);
		mView = mFactory.inflate(R.layout.time_zone_dialog, null);

		final WindowManager.LayoutParams WMLP = this.getWindow().getAttributes();
		WMLP.gravity = Gravity.CENTER;
		this.getWindow().setAttributes(WMLP);

		this.setContentView(mView);
	}
}
