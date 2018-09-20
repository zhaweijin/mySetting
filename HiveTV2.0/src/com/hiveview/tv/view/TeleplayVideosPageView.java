package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.view.HiveBaseView;
import com.hiveview.tv.R;
import com.hiveview.tv.activity.TeleplayDetailActivity;
import com.hiveview.tv.service.entity.VideoEntity;
import com.hiveview.tv.service.entity.VideoNewEntity;

/**
 * @ClassName: TeleplayVideosPageView
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年6月6日 下午5:39:53
 * 
 */
public class TeleplayVideosPageView extends HiveBaseView {

	/**
	 * 各种按钮的控件
	 * 
	 * @Fields btn1
	 */
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button btn6;
	private Button btn7;
	private Button btn8;
	private Button btn9;
	private Button btn10;
	private Button btn11;
	private Button btn12;
	private Button btn13;
	private Button btn14;
	private Button btn15;
	private Button btn16;
	private Button btn17;
	private Button btn18;
	private Button btn19;
	private Button btn20;

	private Context context;

	public TeleplayVideosPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public TeleplayVideosPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TeleplayVideosPageView(Context context, int everyPageSize, int everyPageLine) {
		super(context, everyPageSize, everyPageLine);
		this.context = context;
	}

	/*
	 * 初始化控件 (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#initView()
	 */
	@Override
	protected void initView() {
		// 加入控件所在的布局文件
		initViewContainer(R.layout.teleplay_video_page_item_layout);
		// 初始化按钮控件
		btn1 = (Button) findItemView(R.id.teleplay_btn1);
		btn2 = (Button) findItemView(R.id.teleplay_btn2);
		btn3 = (Button) findItemView(R.id.teleplay_btn3);
		btn4 = (Button) findItemView(R.id.teleplay_btn4);
		btn5 = (Button) findItemView(R.id.teleplay_btn5);
		btn6 = (Button) findItemView(R.id.teleplay_btn6);
		btn7 = (Button) findItemView(R.id.teleplay_btn7);
		btn8 = (Button) findItemView(R.id.teleplay_btn8);
		btn9 = (Button) findItemView(R.id.teleplay_btn9);
		btn10 = (Button) findItemView(R.id.teleplay_btn10);
		btn11 = (Button) findItemView(R.id.teleplay_btn11);
		btn12 = (Button) findItemView(R.id.teleplay_btn12);
		btn13 = (Button) findItemView(R.id.teleplay_btn13);
		btn14 = (Button) findItemView(R.id.teleplay_btn14);
		btn15 = (Button) findItemView(R.id.teleplay_btn15);
		btn16 = (Button) findItemView(R.id.teleplay_btn16);
		btn17 = (Button) findItemView(R.id.teleplay_btn17);
		btn18 = (Button) findItemView(R.id.teleplay_btn18);
		btn19 = (Button) findItemView(R.id.teleplay_btn19);
		btn20 = (Button) findItemView(R.id.teleplay_btn20);
		setBtnUpRequestFocus1(R.id.iv_film_detail_play);
		setBtnUpRequestFocus2(R.id.iv_film_detail_fav);
		// 添加焦点监听方法
		btn1.setOnFocusChangeListener(new BtnFocusListener(btn1, 0));
		btn2.setOnFocusChangeListener(new BtnFocusListener(btn2, 1));
		btn3.setOnFocusChangeListener(new BtnFocusListener(btn3, 2));
		btn4.setOnFocusChangeListener(new BtnFocusListener(btn4, 3));
		btn5.setOnFocusChangeListener(new BtnFocusListener(btn5, 4));
		btn6.setOnFocusChangeListener(new BtnFocusListener(btn6, 5));
		btn7.setOnFocusChangeListener(new BtnFocusListener(btn7, 6));
		btn8.setOnFocusChangeListener(new BtnFocusListener(btn8, 7));
		btn9.setOnFocusChangeListener(new BtnFocusListener(btn9, 8));
		btn10.setOnFocusChangeListener(new BtnFocusListener(btn10, 9));
		btn11.setOnFocusChangeListener(new BtnFocusListener(btn11, 10));
		btn12.setOnFocusChangeListener(new BtnFocusListener(btn12, 11));
		btn13.setOnFocusChangeListener(new BtnFocusListener(btn13, 12));
		btn14.setOnFocusChangeListener(new BtnFocusListener(btn14, 13));
		btn15.setOnFocusChangeListener(new BtnFocusListener(btn15, 14));
		btn16.setOnFocusChangeListener(new BtnFocusListener(btn16, 15));
		btn17.setOnFocusChangeListener(new BtnFocusListener(btn17, 16));
		btn18.setOnFocusChangeListener(new BtnFocusListener(btn18, 17));
		btn19.setOnFocusChangeListener(new BtnFocusListener(btn19, 18));
		btn20.setOnFocusChangeListener(new BtnFocusListener(btn20, 19));


	}

	/**
	 * btn 获取焦点的时候上边文字显示反色
	 * 
	 * @ClassName: BtnFocusListener
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月17日 上午11:54:49
	 * 
	 */
	class BtnFocusListener implements OnFocusChangeListener {
		/**
		 * 获取到焦点的btn
		 * 
		 * @Fields btn
		 */
		Button btn;
		/**
		 * btn的索引值
		 * 
		 * @Fields btnIndex
		 */
		int btnIndex;

		public BtnFocusListener(Button btn, int btnIndex) {
			this.btn = btn;
			this.btnIndex = btnIndex;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (null != btnGetFocus)
				btnGetFocus.btnGetFocus(v, btnIndex, hasFocus);
		}

	}

	/**
	 * 返回获取焦点的view 和 viewIndex
	 * 
	 * @Fields btnGetFocus
	 */
	OnBtnGetFocus btnGetFocus;

	public OnBtnGetFocus getBtnGetFocus() {
		return btnGetFocus;
	}

	public void setBtnGetFocus(OnBtnGetFocus btnGetFocus) {
		this.btnGetFocus = btnGetFocus;
	}

	/**
	 * 返回获取焦点的view 和 viewIndex
	 * 
	 * @ClassName: BtnGetFocus
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年6月17日 下午3:01:56
	 * 
	 */
	public interface OnBtnGetFocus {
		void btnGetFocus(View v, int viewIndex, boolean has);
	}

	/*
	 * 显示要相关的文字信息到btn上边去 (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.view.HiveBaseView#displayTextDataToItemView
	 * (com.hiveview.box.framework.entity.HiveBaseEntity, int)
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
			} else if (index == 5) {
				setItemViewTag(entity, 5, btn6);
				setTextData(btn6, entity);
			} else if (index == 6) {
				setItemViewTag(entity, 6, btn7);
				setTextData(btn7, entity);
			} else if (index == 7) {
				setItemViewTag(entity, 7, btn8);
				setTextData(btn8, entity);
			} else if (index == 8) {
				setItemViewTag(entity, 8, btn9);
				setTextData(btn9, entity);
			} else if (index == 9) {
				setItemViewTag(entity, 9, btn10);
				setTextData(btn10, entity);
			} else if (index == 10) {
				setItemViewTag(entity, 10, btn11);
				setTextData(btn11, entity);
			} else if (index == 11) {
				setItemViewTag(entity, 11, btn12);
				setTextData(btn12, entity);
			} else if (index == 12) {
				setItemViewTag(entity, 12, btn13);
				setTextData(btn13, entity);
			} else if (index == 13) {
				setItemViewTag(entity, 13, btn14);
				setTextData(btn14, entity);
			} else if (index == 14) {
				setItemViewTag(entity, 14, btn15);
				setTextData(btn15, entity);
			} else if (index == 15) {
				setItemViewTag(entity, 15, btn16);
				setTextData(btn16, entity);
			} else if (index == 16) {
				setItemViewTag(entity, 16, btn17);
				setTextData(btn17, entity);
			} else if (index == 17) {
				setItemViewTag(entity, 17, btn18);
				setTextData(btn18, entity);
			} else if (index == 18) {
				setItemViewTag(entity, 18, btn19);
				setTextData(btn19, entity);
			} else if (index == 19) {
				setItemViewTag(entity, 19, btn20);
				setTextData(btn20, entity);
			}
		}

	}

	public void firstViewRequsetFocus() {
		btn1.requestFocus();
	}

	/**
	 * 在获取当前页的时候可以制定按钮获取焦点
	 * 
	 * @Title: TeleplayVideosPageView
	 * @author:张鹏展
	 * @Description:
	 * @param index
	 * @return
	 */
	public View setBtnRequsetFocus(int index) {
		// 清除本页的其他颜色
		setClearColor();
		Button btn = null;
		if (index == 0) {
			btn = btn1;
		} else if (index == 1) {
			btn = btn2;
		} else if (index == 2) {
			btn = btn3;
		} else if (index == 3) {
			btn = btn4;
		} else if (index == 4) {
			btn = btn5;
		} else if (index == 5) {
			btn = btn6;
		} else if (index == 6) {
			btn = btn7;
		} else if (index == 7) {
			btn = btn8;
		} else if (index == 8) {
			btn = btn9;
		} else if (index == 9) {
			btn = btn10;
		} else if (index == 10) {
			btn = btn11;
		} else if (index == 11) {
			btn = btn12;
		} else if (index == 12) {
			btn = btn13;
		} else if (index == 13) {
			btn = btn14;
		} else if (index == 14) {
			btn = btn15;
		} else if (index == 15) {
			btn = btn16;
		} else if (index == 16) {
			btn = btn17;
		} else if (index == 17) {
			btn = btn18;
		} else if (index == 18) {
			btn = btn19;
		} else if (index == 19) {
			btn = btn20;
		}

		btn.setTextColor(0xFFFCFD61);
		return btn;
	}

	/**
	 * 获取焦点书
	 * 
	 * @Title: TeleplayVideosPageView
	 * @author:张鹏展
	 * @Description:
	 * @param index
	 * @return
	 */
	public View getBtn(int index) {
		// 清除本页的其他颜色
		Button btn = null;
		if (index == 0) {
			btn1.requestFocus();
		} else if (index == 1) {
			btn2.requestFocus();
		} else if (index == 2) {
			btn3.requestFocus();
		} else if (index == 3) {
			btn4.requestFocus();
		} else if (index == 4) {
			btn5.requestFocus();
		} else if (index == 5) {
			btn6.requestFocus();
		} else if (index == 6) {
			btn7.requestFocus();
		} else if (index == 7) {
			btn8.requestFocus();
		} else if (index == 8) {
			btn9.requestFocus();
		} else if (index == 9) {
			btn10.requestFocus();
		} else if (index == 10) {
			btn11.requestFocus();
		} else if (index == 11) {
			btn12.requestFocus();
		} else if (index == 12) {
			btn13.requestFocus();
		} else if (index == 13) {
			btn14.requestFocus();
		} else if (index == 14) {
			btn15.requestFocus();
		} else if (index == 15) {
			btn16.requestFocus();
		} else if (index == 16) {
			btn17.requestFocus();
		} else if (index == 17) {
			btn18.requestFocus();
		} else if (index == 18) {
			btn19.requestFocus();
		} else if (index == 19) {
			btn20.requestFocus();
		}
		// btn.requestFocus();
		return btn;
	}


	/*
	 * end by huzuwei
	 */
	/**
	 * 获取btn start by huzuwei
	 */
	public View getBtnView(int index) {
		// 清除本页的其他颜色

		Button btn = null;
		if (index == 0) {
			btn = btn1;
		} else if (index == 1) {
			btn = btn2;
		} else if (index == 2) {
			btn = btn3;
		} else if (index == 3) {
			btn = btn4;
		} else if (index == 4) {
			btn = btn5;
		} else if (index == 5) {
			btn = btn6;
		} else if (index == 6) {
			btn = btn7;
		} else if (index == 7) {
			btn = btn8;
		} else if (index == 8) {
			btn = btn9;
		} else if (index == 9) {
			btn = btn10;
		} else if (index == 10) {
			btn = btn11;
		} else if (index == 11) {
			btn = btn12;
		} else if (index == 12) {
			btn = btn13;
		} else if (index == 13) {
			btn = btn14;
		} else if (index == 14) {
			btn = btn15;
		} else if (index == 15) {
			btn = btn16;
		} else if (index == 16) {
			btn = btn17;
		} else if (index == 17) {
			btn = btn18;
		} else if (index == 18) {
			btn = btn19;
		} else if (index == 19) {
			btn = btn20;
		}
		return btn;
	}

	/*
	 * end by huzuwei
	 */
	/**
	 * btn 向上焦点走向
	 * 
	 * @Title: TeleplayVideosPageView
	 * @author:张鹏展
	 * @Description: TODO
	 * @param id
	 */
	public void setBtnUpRequestFocus1(int id) {
		btn1.setNextFocusUpId(id);
		btn2.setNextFocusUpId(id);
		btn3.setNextFocusUpId(id);
		btn4.setNextFocusUpId(id);
		btn5.setNextFocusUpId(id);

	}

	public void setBtnUpRequestFocus2(int id) {
		btn6.setNextFocusUpId(id);
		btn7.setNextFocusUpId(id);
		btn8.setNextFocusUpId(id);
		btn9.setNextFocusUpId(id);
		btn10.setNextFocusUpId(id);
	}

	public void setBtnDownRequestFocus1(int id) {
		btn1.setNextFocusDownId(id);
		btn2.setNextFocusDownId(id);
		btn3.setNextFocusDownId(id);
		btn4.setNextFocusDownId(id);
		btn5.setNextFocusDownId(id);

	}

	public void setBtnDownRequestFocus2(int id) {
		btn6.setNextFocusDownId(id);
		btn7.setNextFocusDownId(id);
		btn8.setNextFocusDownId(id);
		btn9.setNextFocusDownId(id);
		btn10.setNextFocusDownId(id);
	}

	/**
	 * 
	 * @Title: TeleplayVideosPageView
	 * @author:张鹏展
	 * @Description:
	 */
	public void setClearColor() {
		btn1.setTextColor(0xFFFFFFFF);
		btn2.setTextColor(0xFFFFFFFF);
		btn3.setTextColor(0xFFFFFFFF);
		btn4.setTextColor(0xFFFFFFFF);
		btn5.setTextColor(0xFFFFFFFF);
		btn6.setTextColor(0xFFFFFFFF);
		btn7.setTextColor(0xFFFFFFFF);
		btn8.setTextColor(0xFFFFFFFF);
		btn9.setTextColor(0xFFFFFFFF);
		btn10.setTextColor(0xFFFFFFFF);
		btn11.setTextColor(0xFFFFFFFF);
		btn12.setTextColor(0xFFFFFFFF);
		btn13.setTextColor(0xFFFFFFFF);
		btn14.setTextColor(0xFFFFFFFF);
		btn15.setTextColor(0xFFFFFFFF);
		btn16.setTextColor(0xFFFFFFFF);
		btn17.setTextColor(0xFFFFFFFF);
		btn18.setTextColor(0xFFFFFFFF);
		btn19.setTextColor(0xFFFFFFFF);
		btn20.setTextColor(0xFFFFFFFF);
	}

	/**
	 * 清除第一个view 的颜色
	 * 
	 * @Title: TeleplayVideosPageView
	 * @author:张鹏展
	 * @Description:
	 */
	public void clearFirstBtnColor() {
		btn1.setTextColor(0xffffffff);
	}

	/**
	 * 添加item的点击事件
	 * 
	 * @Title: VideoListItemPageView
	 * @author:张鹏展
	 * @Description:
	 * @param listener
	 */
	public void setItemViewClickListener(OnClickListener listener) {
		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener);
		btn3.setOnClickListener(listener);
		btn4.setOnClickListener(listener);
		btn5.setOnClickListener(listener);
		btn6.setOnClickListener(listener);
		btn7.setOnClickListener(listener);
		btn8.setOnClickListener(listener);
		btn9.setOnClickListener(listener);
		btn10.setOnClickListener(listener);
		btn11.setOnClickListener(listener);
		btn12.setOnClickListener(listener);
		btn13.setOnClickListener(listener);
		btn14.setOnClickListener(listener);
		btn15.setOnClickListener(listener);
		btn16.setOnClickListener(listener);
		btn17.setOnClickListener(listener);
		btn18.setOnClickListener(listener);
		btn19.setOnClickListener(listener);
		btn20.setOnClickListener(listener);

	}

	/**
	 * 显示btn和设置btn的文字信息
	 * 
	 * @Title: TeleplayVideosPageView
	 * @author:张鹏展
	 * @Description:
	 * @param btn
	 * @param entity
	 */
	private void setTextData(Button btn, VideoNewEntity entity) {
		btn.setVisibility(View.VISIBLE);
		if (null != entity) {

			String video_name_plager = TeleplayDetailActivity.playerRecordEntityId;
			btn.setText(String.valueOf(entity.getEporder()));
			if (String.valueOf(entity.getEporder()).equals(video_name_plager))
				btn.setTextColor(0xFFFCFD61);

		}

	}

	/*
	 * 调用的销毁方法用于隐藏相应的控件，防止最后一页的时候出现，多出实际数量的item (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.view.HiveBaseView#destoryFromViewPager()
	 */
	@Override
	public void destoryFromViewPager() {
		// TODO Auto-generated method stub
		setClearColor();
		btn1.setVisibility(View.INVISIBLE);
		btn2.setVisibility(View.INVISIBLE);
		btn3.setVisibility(View.INVISIBLE);
		btn4.setVisibility(View.INVISIBLE);
		btn5.setVisibility(View.INVISIBLE);
		btn6.setVisibility(View.INVISIBLE);
		btn7.setVisibility(View.INVISIBLE);
		btn8.setVisibility(View.INVISIBLE);
		btn9.setVisibility(View.INVISIBLE);
		btn10.setVisibility(View.INVISIBLE);
		btn11.setVisibility(View.INVISIBLE);
		btn12.setVisibility(View.INVISIBLE);
		btn13.setVisibility(View.INVISIBLE);
		btn14.setVisibility(View.INVISIBLE);
		btn15.setVisibility(View.INVISIBLE);
		btn16.setVisibility(View.INVISIBLE);
		btn17.setVisibility(View.INVISIBLE);
		btn18.setVisibility(View.INVISIBLE);
		btn19.setVisibility(View.INVISIBLE);
		btn20.setVisibility(View.INVISIBLE);
	}

	@Override
	public void displayImageToItemView(HiveBaseEntity entity, int index) {
		// TODO Auto-generated method stub

	}

}
