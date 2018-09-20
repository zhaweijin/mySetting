package com.hiveview.tv.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hiveview.tv.R;

public class ToastUtils {
	/**
	 * 通用提示
	 * 
	 * @param context
	 * @param text
	 *            提示内容
	 */
	public static void alert(Context context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		 alertShow(context, text) ;
	}

	/**
	 * 由于服务错误导致的错误提示
	 * 
	 * @param context
	 */
	public void alertServerError(Context context) {
		Toast.makeText(context, context.getResources().getString(R.string.alert_server_error), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 统一背景的toast提示
	 * 
	 * @Title: ToastUtils
	 * @author:张鹏展
	 * @Description:
	 * @param context
	 * @param text
	 */
	public static void alertShow(Context context, String text) {
		Toast mToast = new Toast(context);
		TextView mContentView = new TextView(context);
		mContentView.setTextColor(Color.WHITE);
		mContentView.setText(text+"");
		mContentView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
		LinearLayout ll = new LinearLayout(context);
		ll.setGravity(Gravity.CENTER);
		ll.setBackgroundResource(R.drawable.toast_bg);
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 31);
		lllp.gravity = Gravity.CENTER_VERTICAL;
		lllp.leftMargin = 15;
		lllp.rightMargin = 15;
		mContentView.setLayoutParams(lllp);
		ll.addView(mContentView);
		mToast.setView(ll);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}

}
