package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.MovieListItemPageView;
import com.hiveview.tv.view.MovieListItemPageView.ViewItemFocusChangeListeners;
/**
 * 类名 MovieFactory
 * @author gusongsheng  2014-4-17
 * 
 */
public class MovieFactory extends AbstractPageViewFactory {

	private CallBackItemViewKeyListener callBackItemViewKeyListener;
	private OnClickListener clickListener;
	/**
	 * @Fields focusChangeListeners:焦点监听实例
	 */
	private ViewItemFocusChangeListeners focusChangeListeners;
	/**
	 * 空构造
	 */
	public MovieFactory() {
	};
	
	/**
	 * 构造方法
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 */
	public MovieFactory(CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener,ViewItemFocusChangeListeners focusChangeListeners) {
		super();
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
		this.focusChangeListeners = focusChangeListeners;
	}
	/**
	 * 生产一个MovieListItemPageView
	 * @param context 上下文对象
	 * @return pageView MovieListItemPageView对象
	 */
	@Override
	public HiveBaseView createInstance(Context context) {
		MovieListItemPageView pageView = new MovieListItemPageView(context, 12, 2);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		pageView.setViewItemFocusChangeListeners(focusChangeListeners);
		return pageView;
	}

}
