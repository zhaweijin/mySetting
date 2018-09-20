package com.hiveview.tv.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hiveview.tv.R;

/**
 * 对话框工具类
 * 使用方法：<br>
 * <dl>
 * <dt>使用AlertDialogUtil的默认View</dt>
 * <dd>调用getInstance方法获取实例，通过实例对AlertDialog进行设置、获取状态等操作。<br>
 * 如果未设置确定按钮监听器 <b>setOnEnsureButtonListener</b>，确定按钮将不会显示，<br>
 * 取消按钮同理 <b>setOnCancelButtonClick</b></dd>
 * <dt>使用自定义View</dt>
 * <dd>与使用默认View不同，需要调用<b>setViewRes(int)</b>和<b>setInitView(InitView)</b>对自定义View进行设置，其他操作同默认View。</dd>
 * </dl>
 * @author haozening
 *
 */
public class AlertDialogUtil {

	private AlertDialog alertDialog;
	private Listener mListener;
	private int mRes = -1;

	private InitView mInitView;
	private static AlertDialogUtil alertDialogUtil;
	private Context mContext;
	private View view;
	private Builder builder;
	private String mContent;
	private boolean isCancelable = true;
	private boolean isOwnView = false;
	public static AlertDialogUtil getInstance(Context context, Listener listener) {
		if (alertDialogUtil == null) {
			alertDialogUtil = new AlertDialogUtil();
		}
		alertDialogUtil.clear();
		alertDialogUtil.mContext = context;
		alertDialogUtil.mListener = listener;

		return alertDialogUtil;
	}
	
	/**
	 * 可以指定自己的视图
	 * @param res
	 * @return
	 */
	public AlertDialogUtil setViewRes(int res) {
		mRes = res;
		return this;
	}
	/**
	 * 设置对话框消失监听器
	 * @param onCancelListener
	 * @return
	 */
//	public AlertDialogUtil setOnCancelListener(OnCancelListener onCancelListener) {
//		mOnCancelListener = onCancelListener;
//		return this;
//	}
	/**
	 * 设置对话框取消按钮监听器
	 * @param onCancelListener
	 * @return
	 */
//	public AlertDialogUtil setOnCancelButtonClick(OnButtonClickListener onClickListener) {
//		mOnCancelButtonClick = onClickListener;
//		return this;
//	}
	/**
	 * 设置对话框确定按钮监听器
	 * @param onCancelListener
	 * @return
	 */
//	public AlertDialogUtil setOnEnsureButtonListener(OnButtonClickListener onClickListener) {
//		mOnEnsureButtonClick = onClickListener;
//		return this;
//	}
	/**
	 * 设置对话框内容
	 * @param content
	 * @return
	 */
	public AlertDialogUtil setContent(String content) {
		mContent = content;
		return this;
	}
	/**
	 * 设置初始化视图的监听器，用在自定义View上
	 * @param initView
	 */
	public AlertDialogUtil setInitView(InitView initView) {
		mInitView = initView;
		return this;
	}
	public AlertDialogUtil setCancelable(boolean cancelable) {
		this.isCancelable = cancelable;
		return this;
	}
	/**
	 * 显示对话框
	 * @param width 宽
	 * @param height 高
	 */
	public AlertDialogUtil show(int width, int height) {
		if (mRes <= 0) {
			view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_common, null);
			isOwnView = false;
		} else {
			view = LayoutInflater.from(mContext).inflate(mRes, null);
			isOwnView = true;
		}
		builder = new Builder(mContext);
		builder.create();
		alertDialog = builder.show();
		alertDialog.setContentView(view);
		alertDialog.setCancelable(isCancelable);
		Button cancel = null;
		Button ensure = null;
		if (isOwnView) {
			mInitView.setOwnView(alertDialog, view);
			alertDialog.getWindow().setLayout(mInitView.getWidth(), mInitView.getHeight()); //设置指定宽高
			if (mInitView.getCancelButtonRes() > 0) {
				cancel = (Button) view.findViewById(mInitView.getCancelButtonRes());
			}
			if (mInitView.getEnsureButtonRes() > 0) {
				ensure = (Button) view.findViewById(mInitView.getEnsureButtonRes());
			}
		} else {
			alertDialog.getWindow().setLayout(width, height); //设置指定宽高
			cancel = (Button) view.findViewById(R.id.btn_cancel);
			ensure = (Button) view.findViewById(R.id.btn_ensure);
			TextView textView = (TextView) view.findViewById(R.id.tv_content);
			textView.setText(mContent);
		}
		
//	a
		if (ensure != null) {
			ensure.setVisibility(View.VISIBLE);
			ensure.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onDialogEnsure(alertDialog);
				}
			});
		}

		return this;
	}
	/**
	 * 默认显示方法
	 */
	public AlertDialogUtil show() {
		return show(640, 310);
	}
	/**
	 * 隐藏AlertDialog
	 */
	public void dismiss() {
		alertDialog.dismiss();
	}
	/**
	 * 是否是显示状态
	 * @return
	 */
	public boolean isShowing() {
		if (alertDialog != null) {
			return alertDialog.isShowing();
		} else {
			return false;
		}
	}
	/**
	 * 初始化自定义View的时候设置自己的View
	 * @author haozening
	 */
	public interface InitView{
		/**
		 * 设置View高
		 * @return
		 */
		public int getHeight();
		/**
		 * 设置View宽
		 * @return
		 */
		public int getWidth();
		/**
		 * 设置取消按钮资源ID
		 * @return
		 */
		public int getCancelButtonRes();
		/**
		 * 设置确定按钮资源ID
		 * @return
		 */
		public int getEnsureButtonRes();
		/**
		 * 设置自己View中的其他View的内容及行为
		 */
		public void setOwnView(AlertDialog alertDialog, View contentView);
	}
	/**
	 * 清空对象缓存的属性信息
	 */
	private void clear() {
		mContext = null;
		mListener = null;
		mInitView = null;
//		mOnCancelButtonClick = null;
//		mOnEnsureButtonClick = null;
		mRes = -1;
		alertDialog = null;
		builder = null;
		isCancelable = true;
		isOwnView = false;
		view = null;
		
	}
//	/**
//	 * 确定取消按钮点击事件
//	 * @author haozening
//	 *
//	 */
//	public interface OnButtonClickListener {
//		public void onClick(View v, AlertDialog alertDialog);
//	}
	public interface Listener {
		void onDialogEnsure(AlertDialog alertDialog);
		void onDialogCancel(AlertDialog alertDialog);
	}

}
