package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewFocusListener;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.VarietyVideosPageView;
import com.hiveview.tv.view.VarietyVideosPageView.OnBtnGetFocus;
import com.hiveview.tv.view.VideoListItemPageView;
import com.hiveview.tv.view.VideoListItemPageView.GetFocusEntity;


public class VarietyVideosPagerViewFactory extends AbstractPageViewFactory {

	/**
	 * 当焦点落到某个item的时候焦点监听
	 * 
	 * @Fields callbackFocusListener
	 */
	CallBackItemViewFocusListener callbackFocusListener;
	/**
	 * 焦点落到的item的按键监听
	 * 
	 * @Fields callBackItemViewKeyListener
	 */
	CallBackItemViewKeyListener callBackItemViewKeyListener;
	/**
	 * 焦点所在的item的点击时间
	 * 
	 * @Fields clickListener
	 */
	OnClickListener clickListener;
	/**
	 * 返回获取焦点的view 和 viewIndex
	 * @Fields btnGetFocus
	 */
	OnBtnGetFocus btnGetFocus;


	/**
	 * 创建工厂的方法
	 * @param @param callbackFocusListener
	 * @param @param itemViewKeyListener
	 * @param @param clickListener
	 */
	public VarietyVideosPagerViewFactory(CallBackItemViewFocusListener callbackFocusListener, CallBackItemViewKeyListener itemViewKeyListener,
			OnClickListener clickListener,OnBtnGetFocus btnGetFocus) {
		super();
		this.callbackFocusListener = callbackFocusListener;
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
		this.btnGetFocus = btnGetFocus;
	}

	/* 
	 * 初始化相关接口
	 * (non-Javadoc)
	 * @see com.hiveview.box.framework.view.factory.AbstractPageViewFactory#createInstance(android.content.Context)
	 */
	@Override
	public HiveBaseView createInstance(Context context) {
		VarietyVideosPageView pageView = new VarietyVideosPageView(context, 20, 2);
		pageView.setCallBackFocusListener(callbackFocusListener);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		pageView.setBtnGetFocus(btnGetFocus);
		return pageView;
	}


}
