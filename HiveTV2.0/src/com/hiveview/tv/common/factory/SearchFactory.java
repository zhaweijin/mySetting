package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.SearchListItemPageView;
import com.hiveview.tv.view.SearchListItemPageView.ItemViewFocusChange;
/**
 * 类名 MovieFactory
 * @author gusongsheng  2014-4-17
 * 
 */
public class SearchFactory extends AbstractPageViewFactory {

	CallBackItemViewKeyListener callBackItemViewKeyListener;
	OnClickListener clickListener;
	ItemViewFocusChange itemViewFocusChange;
	/**
	 * 空构造
	 */
	public SearchFactory() {
	};
	/**
	 * 构造方法
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 */
	public SearchFactory(CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener,ItemViewFocusChange itemViewFocusChange) {
		super();
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
		this.itemViewFocusChange = itemViewFocusChange;
	}
	/**
	 * 生产一个MovieListItemPageView
	 * @param context 上下文对象
	 * @return pageView MovieListItemPageView对象
	 */
	@Override
	public HiveBaseView createInstance(Context context) {
		SearchListItemPageView pageView = new SearchListItemPageView(context, 12, 2);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		pageView.setItemViewFocusChange(itemViewFocusChange);
		return pageView;
	}

}
