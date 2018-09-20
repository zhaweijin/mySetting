package com.hiveview.tv.common.factory;

import android.content.Context;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.TVSelectTelevisionPageView;

/***
 * 分类选台生成TVSelectTelevisionPageView的工厂
 * @author maliang
 * 2014-04-22
 */
public class TvSelectTelevisionPageViewFactoryItem extends AbstractPageViewFactory{
	private CallBackItemViewFocusListener callbackFocusListener = null;
	private CallBackItemViewKeyListener itemViewKeyListener;
	@Override
	public HiveBaseView createInstance(Context context) {
		// TODO Auto-generated method stub
		TVSelectTelevisionPageView pageView = new TVSelectTelevisionPageView(context, 8, 2);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(itemViewKeyListener);
		return pageView;
	}
	
	public TvSelectTelevisionPageViewFactoryItem(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener) {
		this.callbackFocusListener = callbackFocusListener;
		this.itemViewKeyListener = itemViewKeyListener;
	}

}
