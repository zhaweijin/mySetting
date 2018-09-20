package com.hiveview.tv.common.factory;

import android.content.Context;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.TvChannelPageView;

public class TvChannelPageViewFactory extends AbstractPageViewFactory {

	private CallBackItemViewFocusListener callbackFocusListener = null;
	private CallBackItemViewKeyListener itemViewKeyListener;

	public TvChannelPageViewFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener) {
		this.callbackFocusListener = callbackFocusListener;
		this.itemViewKeyListener = itemViewKeyListener;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		TvChannelPageView pageView = new TvChannelPageView(context, 12, 3);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(itemViewKeyListener);
		return pageView;
	}

}
