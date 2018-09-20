package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public abstract class ViewPagerBaseView extends FrameLayout{
	
	/**
	 * 当前类的全局Context
	 */
	protected Context mContext = null;

	/**
	 * 根据layout.xml载入成View的inflater
	 */
	protected LayoutInflater mInflater = null;

	/**
	 * 当前页面所有View的容器
	 */
	protected View viewContainer = null;

	protected OnPageViewItemListener mListener = null;

	public ViewPagerBaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initBase(context);
	}

	public ViewPagerBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBase(context);
	}

	public ViewPagerBaseView(Context context, OnPageViewItemListener listener) {
		super(context);
		initBase(context);
		this.mListener = listener;
	}

	private void initBase(Context context) {
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initView();
	}

	/**
	 * 用于实例化当前页面的子View，在此方法中必须首先调用 initViewContainer(int layoutId)方法
	 */
	protected abstract void initView();

	/**
	 * 初始化当前页面总的View容器viewContainer，并添加到当前显示的View，就是添加到当前定义的View。
	 * 
	 * @param layoutId
	 *            布局文件id,如：R.layout.sss
	 */
	protected void initViewContainer(int layoutId) {
		viewContainer = mInflater.inflate(layoutId, null);
		addView(viewContainer);
	}

	/**
	 * 
	 * 把数据设置到ItemView上
	 * 
	 * @param entity
	 *            数据对象
	 * @param index
	 *            此数据对象应该设置在在PageView上index位置上的ItemView
	 */
	public abstract void HandView(HiveBaseEntity entity, int index,boolean isSetImage);
	
	/**
	 * 
	 * 隐藏没有数据的view
	 * 
	 * @param index
	 * 			  应该隐藏view的index
	 * */
	public abstract void HideView(int index);

	/**
	 * 定义当PageView中的每一项View发生点击事件，焦点事件，按键事件时分别回调的方法;此接口的具体实现应该是在Activity中，
	 * 主要是考虑到PageView中的Item发生事件可能要操作ViewPager之外的View。
	 * 
	 * @author chenlixiao
	 * 
	 */
	public interface OnPageViewItemListener {
		/**
		 * View发生点击事件回调此方法
		 * 
		 * @param view
		 */
		public void OnPageViewClick(View view);

		/**
		 * View发生焦点改变事件回调此方法
		 * 
		 * @param v
		 * @param hasFocus
		 */
		public void onPageViewFocusChange(View v, boolean hasFocus);

		/**
		 * View上发生按键事件回调此方法
		 * 
		 * @param v
		 * @param keyCode
		 * @param event
		 */
		public void onPageViewKey(View v, int keyCode, KeyEvent event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		return super.dispatchKeyEvent(event);
	}

	public void requestDefaultLetfAndDownFocus() {
	}

	public void requestDefaultLetfAndUpFocus() {
	}

	public void requestDefaultRightAndDownFocus() {
	}

	public void requestDefaultRightAndUpFocus() {
	}

	public void requestLastDefaultRightAndDownFocus() {
	}

	public void requestLastDefaultRightAndUpFocus() {
	}
	
	
	public OnClickDpadListener onClickDpadListener;
	
	public void setDpadListener(OnClickDpadListener onClickDpadListener){
		this.onClickDpadListener = onClickDpadListener;
	}
	
	public interface OnClickDpadListener {
		void leftDpadLeftAndUp();
		void leftDpadLeftAndDown();
		void rightDpadRightAndUp();
		void rightDpadRightAndDown();
		void defaultPosition();
	}
	
}
