package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.LandSpaceItemViewContainer;
import com.hiveview.tv.view.ProjectItemViewContainer;

public class LandSpaceItemFactory extends AbstractPageViewFactory {
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	OnClickListener clickListener;

	public LandSpaceItemFactory() {}
	/**
	 * 这是按键的监听和点击事件的监听
	 * @param @param itemViewKeyListener
	 * @param @param clickListener
	 */
	public LandSpaceItemFactory(CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener) {
		this.callBackItemViewKeyListener = itemViewKeyListener;
		 this.clickListener = clickListener;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		LandSpaceItemViewContainer pageView = new LandSpaceItemViewContainer(context, 4, 1);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		return pageView;
	}
}
