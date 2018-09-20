package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.OnLivesTipsListItemPageView;
import com.hiveview.tv.view.OnLivesTipsListItemPageView.GetFocusEntity;
import com.hiveview.tv.view.PlayerRecordListItemPageView;

/**
 * @author zhangpengzhan
 * 
 *         2014年4月14日 下午2:49:49
 */
public class OnLivesTipsFactory extends AbstractPageViewFactory {

	/**
	 * 当前焦点位置的监听 ，落在那个view 上
	 */
	CallBackItemViewFocusListener callbackFocusListener;
	/**
	 * 当前焦点所在view 的 按键监听
	 */
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	/**
	 * 按键监听
	 */
	OnClickListener clickListener;
	/**
	 * 焦点所在的entity
	 * @Fields focusEntity
	 */
	GetFocusEntity focusEntity;

	/**
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 * 
	 * 
	 */
	public OnLivesTipsFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener,
			OnClickListener clickListener,GetFocusEntity focusEntity) {
		super();
		this.callbackFocusListener = callbackFocusListener;
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
		this.focusEntity =focusEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.factory.AbstractPageViewFactory#
	 * createInstance(android.content.Context) viewpager 中每个viewitem 添加的回调
	 */
	public HiveBaseView createInstance(Context context) {
		// 设置 viewpager 的规格 12个view item 分为2行显示
		OnLivesTipsListItemPageView pageView = new OnLivesTipsListItemPageView(context, 8, 2);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setFocusEntity(focusEntity);
		//pageView.setItemViewClickListener(clickListener);
		return pageView;
	}

}
