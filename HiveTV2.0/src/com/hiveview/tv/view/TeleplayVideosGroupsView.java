package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.VideoNewEntity;
import com.hiveview.tv.utils.StringUtils;

/**
 * @ClassName: TeleplayVideosGroupsView
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年6月9日 下午3:27:34
 * 
 */
public class TeleplayVideosGroupsView extends HiveBaseView {


	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;


	public TeleplayVideosGroupsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public TeleplayVideosGroupsView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public TeleplayVideosGroupsView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);

	}

	/*
	 * 初始化view
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#initView()
	 */
	@Override
	protected void initView() {
		initViewContainer(R.layout.teleplay_video_group_item_layout);
		btn1 = (Button) findItemView(R.id.btnG1);
		btn2 = (Button) findItemView(R.id.btnG2);
		btn3 = (Button) findItemView(R.id.btnG3);
		btn4 = (Button) findItemView(R.id.btnG4);
		btn5 = (Button) findItemView(R.id.btnG5);
	}

	/*
	 *显示btn上边的文字消息 
	 *  (non-Javadoc)
	 * @see com.hiveview.box.framework.view.HiveBaseView#displayTextDataToItemView(com.hiveview.box.framework.entity.HiveBaseEntity, int)
	 */
	@Override
	public void displayTextDataToItemView(HiveBaseEntity baseEntity, int index) {
		VideoNewEntity entity = (VideoNewEntity) baseEntity;
		if (null != entity) {
			if (index == 0) {
				setItemViewTag(entity, 0, btn1);
				setTextData(btn1, entity);
			} else if (index == 1) {
				setItemViewTag(entity, 1, btn2);
				setTextData(btn2, entity);
			} else if (index == 2) {
				setItemViewTag(entity, 2, btn3);
				setTextData(btn3, entity);
			} else if (index == 3) {
				setItemViewTag(entity, 3, btn4);
				setTextData(btn4, entity);
			} else if (index == 4) {
				setItemViewTag(entity, 4, btn5);
				setTextData(btn5, entity);
			}

		}
	}

	/**
	 * 设置btn上文字信息
	 * 
	 * @Title: TeleplayVideosGroupsView
	 * @author:张鹏展
	 * @Description:
	 * @param btn
	 * @param entity
	 */
	private void setTextData(Button btn, VideoNewEntity entity) {
		if (null != entity) {
			btn.setVisibility(View.VISIBLE);
			if (!StringUtils.isEmpty(entity.getEpname())) {
				btn.setText(entity.getEpname());
			}
		}

	}
	
	/**
	 * 改变btn上的文字颜色
	 * @Title: TeleplayVideosGroupsView
	 * @author:张鹏展
	 * @Description: 
	 * @param itemIndex
	 */
	public Button setBtnChangeColor(int itemIndex){
		Button btn =null;
		switch (itemIndex) {
		case 0:
			btn = btn1;
			btn1.setTextColor(getResources().getColor(R.color.yellow));
			break;
		case 1:
			btn = btn2;
			btn2.setTextColor(getResources().getColor(R.color.yellow));
			break;
		case 2:
			btn = btn3;
			btn3.setTextColor(getResources().getColor(R.color.yellow));
			break;
		case 3:
			btn = btn4;
			btn4.setTextColor(getResources().getColor(R.color.yellow));
			break;
		case 4:
			btn = btn5;
			btn5.setTextColor(getResources().getColor(R.color.yellow));
			break;
		}
		return btn;
	}

	/*
	 * 设置图片内容，本次不需要
	 *  (non-Javadoc)
	 * @see com.hiveview.box.framework.view.HiveBaseView#displayImageToItemView(com.hiveview.box.framework.entity.HiveBaseEntity, int)
	 */
	@Override
	public void displayImageToItemView(HiveBaseEntity entity, int index) {
		// TODO Auto-generated method stub

	}

	/*
	 * 销毁view的 时候调用的
	 *  (non-Javadoc)
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn3.setVisibility(View.INVISIBLE);
		btn4.setVisibility(View.INVISIBLE);
		btn5.setVisibility(View.INVISIBLE);
	}

	/**
	 * 添加按键监听
	 * @Title: TeleplayVideosGroupsView
	 * @author:张鹏展
	 * @Description: 
	 * @param clickListener
	 */
	public void setItemViewClickListener(OnClickListener clickListener) {
		btn1.setOnClickListener(clickListener);
		btn2.setOnClickListener(clickListener);
		btn3.setOnClickListener(clickListener);
		btn4.setOnClickListener(clickListener);
		btn5.setOnClickListener(clickListener);
	}

}
