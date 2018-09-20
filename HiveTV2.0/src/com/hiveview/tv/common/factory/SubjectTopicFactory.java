package com.hiveview.tv.common.factory;

import android.content.Context;
import android.view.View.OnClickListener;

import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.box.framework.view.HiveBaseView.CallBackItemViewKeyListener;
import com.hiveview.box.framework.view.factory.AbstractPageViewFactory;
import com.hiveview.tv.view.SubjectTopicListItemPageView;
/**
 * 类名 SubjectTopicFactory
 * @author gusongsheng  2014-4-17
 * 
 */
public class SubjectTopicFactory extends AbstractPageViewFactory {

	CallBackItemViewKeyListener callBackItemViewKeyListener;
	OnClickListener clickListener;
	/**
	 * 空构造
	 */
	public SubjectTopicFactory() {
	};
	/**
	 * 构造方法
	 * @param callbackFocusListener
	 * @param itemViewKeyListener
	 * @param clickListener
	 */
	public SubjectTopicFactory(CallBackItemViewKeyListener itemViewKeyListener, OnClickListener clickListener) {
		super();
		this.callBackItemViewKeyListener = itemViewKeyListener;
		this.clickListener = clickListener;
	}
	/**
	 * 生产一个SubjectTopicListItemPageView
	 * @param context 上下文对象
	 * @return pageView SubjectTopicListItemPageView对象
	 */
	@Override
	public HiveBaseView createInstance(Context context) {
		SubjectTopicListItemPageView pageView = new SubjectTopicListItemPageView(context, 6, 2);
		pageView.setCallBackKeyListener(callBackItemViewKeyListener);
		pageView.setItemViewClickListener(clickListener);
		return pageView;
	}

}
