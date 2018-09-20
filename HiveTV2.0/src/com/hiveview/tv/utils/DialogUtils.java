package com.hiveview.tv.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.hiveview.tv.view.CancelCollectDialog;
import com.hiveview.tv.view.HiveViewNetFaultDialog;

public class DialogUtils {
	/**
	 * 网络异常对话框
	 */
	private static HiveViewNetFaultDialog netFaultDialog = null;
	/**
	 * 取消收藏对话框
	 */
	private static CancelCollectDialog cancelCollectDialog = null;
	

	
	/**
	 * 显示网络故障的对话框
	 * 
	 * @param context
	 * @param clickListener
	 *            对话框上的两个按钮去设置和尝试重连的事件监听
	 * 
	 */
	public static void showDialogAboutNetFault(Context context, OnDialogClickListener clickListener) {
		netFaultDialog = new HiveViewNetFaultDialog(context, clickListener);
		netFaultDialog.setTitleContent("网络已经断开");
		netFaultDialog.setButtonsText("去设置", "尝试重连");
		netFaultDialog.show();
	}

	/**
	 * 显示取消手收藏对话框
	 * 
	 * @param context
	 * @param clickListener
	 */
	public static void showDialogCancelCollect(Context context, OnDialogClickListener clickListener) {
		cancelCollectDialog = new CancelCollectDialog(context, clickListener);
		cancelCollectDialog.show();
		
	}
	
	/**
	 * 清楚观看记录的对话框
	 * 
	 * @param context
	 * @param clickListener
	 */
	public static void showDialogCancelCollect(Context context, OnDialogClickListener clickListener,String message) {
		cancelCollectDialog = new CancelCollectDialog(context, clickListener);
		cancelCollectDialog.setMessageContent(message);
		//修改对话空的位置 author zhangpengzhan------->start
		   /* 
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = cancelCollectDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         * 
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
        lp.y = 125;
        lp.width = dialogWindow.getAttributes().WRAP_CONTENT; // 宽度
        lp.height = dialogWindow.getAttributes().WRAP_CONTENT; ; // 高度
        lp.alpha = 1.0f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
        /////------>end
		cancelCollectDialog.show();
	}

	/**
	 * 
	 * @Title: DialogUtils
	 * @author:lihongji
	 * @Description: 收藏对话框是否显示
	 * @return
	 */
	public static boolean  isDialogShow(){
		if(cancelCollectDialog!=null){
		return cancelCollectDialog.isDialogShow();
		}
		return false;
	}
	
	
	/**
	 * 关闭取消收藏对话框
	 */
	public static void closeDialogCancelCollect() {
		if (null != cancelCollectDialog && cancelCollectDialog.isShowing()) {		
			cancelCollectDialog.dismiss();
			cancelCollectDialog.setDialogShow(false);
			cancelCollectDialog = null;
		}
	}

	/**
	 * 关闭网络端口提示对话框
	 */
	public static void closeDialogAboutNetFault() {
		if (null != netFaultDialog && netFaultDialog.isShowing()) {
			netFaultDialog.dismiss();
			netFaultDialog = null;
		}
	}

}
