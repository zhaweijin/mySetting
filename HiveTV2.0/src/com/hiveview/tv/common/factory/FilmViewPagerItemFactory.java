package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.FlimViewPageItemView;

/**
 * @ClassName: FilmViewPagerItemFactory
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年6月6日 下午2:01:08
 * 
 */
public class FilmViewPagerItemFactory extends AbstractPageViewFactory {
	/**
	 * 当前焦点位置的监听 ，落在那个view 上
	 */
	CallBackItemViewFocusListener callbackFocusListener;
	/**
	 * 当前焦点所在view 的 按键监听
	 */
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	/**
	 * item的点击监听
	 * @Fields clickListener
	 */
	OnClickListener clickListener;

	/**
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 * 
	 * 
	 */
	public FilmViewPagerItemFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener,OnClickListener clickListener) {
		super();
		this.callbackFocusListener = callbackFocusListener;
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.factory.AbstractPageViewFactory#
	 * createInstance(android.content.Context) viewpager 中每个viewitem 添加的回调
	 */
	public HiveBaseView createInstance(Context context) {
		// 设置 viewpager 的规格 12个view item 分为2行显示
		FlimViewPageItemView pageView = new FlimViewPageItemView(context, 6, 1);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		return pageView;
	}

}
