package com.hiveview.tv.common.factory;

import android.content.Context;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.television.OnLiveEpgPageItem;
import com.hiveview.tv.view.television.OnVoiceEpgPageItem;

public class OnVoiceEpgPageViewFactory extends AbstractPageViewFactory {

	private CallBackItemViewFocusListener callbackFocusListener = null;
	private CallBackItemViewKeyListener itemViewKeyListener;

	public OnVoiceEpgPageViewFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener) {
		this.callbackFocusListener = callbackFocusListener;
		this.itemViewKeyListener = itemViewKeyListener;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		OnVoiceEpgPageItem pageView = new OnVoiceEpgPageItem(context, 7, 1);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(itemViewKeyListener);
		return pageView;
	}

}
