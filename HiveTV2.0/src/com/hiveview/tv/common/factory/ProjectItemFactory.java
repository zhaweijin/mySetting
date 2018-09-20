package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.ProjectItemViewContainer;

public class ProjectItemFactory extends AbstractPageViewFactory {
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	OnClickListener clickListener;

	public ProjectItemFactory() {}
	/**
	 * 这是按键的监听和点击事件的监听
	 * @param @param itemViewKeyListener
	 * @param @param clickListener
	 */
	public ProjectItemFactory(CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener) {
		this.callBackItemViewKeyListener = itemViewKeyListener;
		 this.clickListener = clickListener;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		ProjectItemViewContainer pageView = new ProjectItemViewContainer(context, 6, 1);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		return pageView;
	}
}
