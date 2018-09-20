package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.VideoListItemPageView;
import com.hiveview.tv.view.VideoListItemPageView.GetFocusEntity;

public class VideoItemFactory extends AbstractPageViewFactory {

	CallBackItemViewFocusListener callbackFocusListener;
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	OnClickListener clickListener;
	GetFocusEntity getFocusEntity;


	public VideoItemFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener,GetFocusEntity getFocusEntity) {
		super();
		this.callbackFocusListener = callbackFocusListener;
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
		this.getFocusEntity = getFocusEntity;
	}

	@Override
	public HiveBaseView createInstance(Context context) {
		VideoListItemPageView  pageView = new VideoListItemPageView(context, 12, 2);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		pageView.setFocusEntity(getFocusEntity);
		return pageView;
	}




}
