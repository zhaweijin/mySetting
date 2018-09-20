package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.SearchViewPagerItem;


/**
 * @author zhangpengzhan
 *
 * 2014年4月17日
 *	下午6:03:38
 */
public class SearchItemFactory extends AbstractPageViewFactory {
	
	/**
	 * 当前焦点位置的监听 ，落在那个view 上
	 */
	CallBackItemViewFocusListener callbackFocusListener;

	/**
	 * 按键监听
	 */
	OnClickListener clickListener;
	/**
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 * 
	 * 
	 */
	public SearchItemFactory(CallBackItemViewFocusListener callbackFocusListener, OnClickListener clickListener) {
		super();
		this.callbackFocusListener = callbackFocusListener;
		this.clickListener = clickListener;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see com.hiveview.box.framework.view.factory.AbstractPageViewFactory#createInstance(android.content.Context)
	 * viewpager 中每个viewitem 添加的回调
	 */
	public HiveBaseView createInstance(Context context) {
		//设置 viewpager 的规格 8个view item 分为2行显示 
		SearchViewPagerItem pageView = new SearchViewPagerItem(context, 12, 2);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setItemViewClickListener(clickListener);
		return pageView;
	}

}
