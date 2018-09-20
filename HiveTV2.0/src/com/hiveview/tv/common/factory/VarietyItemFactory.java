package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.PlayerRecordListItemPageView;
import com.hiveview.tv.view.VarietyViewPagerItem;
import com.hiveview.tv.view.VarietyViewPagerItem.ViewItemFocusChangeListeners;


/**
 * @author zhangpengzhan
 *
 * 2014年4月17日
 *	下午6:03:38
 */
public class VarietyItemFactory extends AbstractPageViewFactory {
	
	/**
	 * 当前焦点位置的监听 ，落在那个view 上
	 */
	CallBackItemViewFocusListener callbackFocusListener;
	/**
	 * 当前焦点所在view 的 按键监听
	 */
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	/**
	 * 按键监听
	 */
	OnClickListener clickListener;
	
	/**
	 * @Fields focusChangeListeners:焦点监听实例
	 */
	private ViewItemFocusChangeListeners focusChangeListeners;
	/**
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 * 
	 * 
	 */
	public VarietyItemFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener,ViewItemFocusChangeListeners focusChangeListeners) {
		super();
		this.callbackFocusListener = callbackFocusListener;
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
		this.focusChangeListeners = focusChangeListeners;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.hiveview.box.framework.view.factory.AbstractPageViewFactory#createInstance(android.content.Context)
	 * viewpager 中每个viewitem 添加的回调
	 */
	public HiveBaseView createInstance(Context context) {
		//设置 viewpager 的规格 8个view item 分为2行显示 
		VarietyViewPagerItem pageView = new VarietyViewPagerItem(context, 8, 2);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		pageView.setViewItemFocusChangeListeners(focusChangeListeners);
		return pageView;
	}

}
