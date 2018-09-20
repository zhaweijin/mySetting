package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnFocusChangeListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.television.EpgPageItem;

public class EpgPageViewFactory extends AbstractPageViewFactory {

	private CallBackItemViewFocusListener callbackFocusListener = null;
	private CallBackItemViewKeyListener itemViewKeyListener;

	public EpgPageViewFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener) {
		this.callbackFocusListener = callbackFocusListener;
		this.itemViewKeyListener = itemViewKeyListener;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		EpgPageItem pageView = new EpgPageItem(context, 7, 1);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(itemViewKeyListener);
		return pageView;
	}

}
