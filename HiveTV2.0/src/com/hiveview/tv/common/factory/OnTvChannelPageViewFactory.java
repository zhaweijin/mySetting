package com.hiveview.tv.common.factory;

import android.content.Context;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.OnTvChannelPageView;

public class OnTvChannelPageViewFactory extends AbstractPageViewFactory {

	private CallBackItemViewFocusListener callbackFocusListener = null;
	private CallBackItemViewKeyListener itemViewKeyListener;

	public OnTvChannelPageViewFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener) {
		this.callbackFocusListener = callbackFocusListener;
		this.itemViewKeyListener = itemViewKeyListener;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		OnTvChannelPageView pageView = new OnTvChannelPageView(context, 4, 1);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(itemViewKeyListener);
		return pageView;
	}

}
