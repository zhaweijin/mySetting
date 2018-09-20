package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hiveview.tv.R;

public class TeleplayVideoPageView extends FrameLayout {

	private LayoutInflater inflater;
	private Context mContext;
	private View vContainer;

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
	private OnClickListener clickListener;
	private OnFocusChangeListener focusChangeListener;

	public TeleplayVideoPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public TeleplayVideoPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TeleplayVideoPageView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public void setVideoViewOnFocusListener(OnFocusChangeListener listener) {
		btn1.setOnFocusChangeListener(listener);
		btn2.setOnFocusChangeListener(listener);
		btn3.setOnFocusChangeListener(listener);
		btn4.setOnFocusChangeListener(listener);
		btn5.setOnFocusChangeListener(listener);
		btn6.setOnFocusChangeListener(listener);
		btn7.setOnFocusChangeListener(listener);
		btn8.setOnFocusChangeListener(listener);
		btn9.setOnFocusChangeListener(listener);
		btn10.setOnFocusChangeListener(listener);
		btn11.setOnFocusChangeListener(listener);
		btn12.setOnFocusChangeListener(listener);
		btn13.setOnFocusChangeListener(listener);
		btn14.setOnFocusChangeListener(listener);
		btn15.setOnFocusChangeListener(listener);
		btn16.setOnFocusChangeListener(listener);
		btn17.setOnFocusChangeListener(listener);
		btn18.setOnFocusChangeListener(listener);
		btn19.setOnFocusChangeListener(listener);
		btn20.setOnFocusChangeListener(listener);
	}

	public void setVideoViewOnClickListener(OnClickListener listener) {
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

	private void init() {

		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vContainer = inflater.inflate(R.layout.teleplay_video_page_item_layout, null);
		btn1 = (Button) vContainer.findViewById(R.id.teleplay_btn1);
		btn2 = (Button) vContainer.findViewById(R.id.teleplay_btn2);
		btn3 = (Button) vContainer.findViewById(R.id.teleplay_btn3);
		btn4 = (Button) vContainer.findViewById(R.id.teleplay_btn4);
		btn5 = (Button) vContainer.findViewById(R.id.teleplay_btn5);
		btn6 = (Button) vContainer.findViewById(R.id.teleplay_btn6);
		btn7 = (Button) vContainer.findViewById(R.id.teleplay_btn7);
		btn8 = (Button) vContainer.findViewById(R.id.teleplay_btn8);
		btn9 = (Button) vContainer.findViewById(R.id.teleplay_btn9);
		btn10 = (Button) vContainer.findViewById(R.id.teleplay_btn10);
		btn11 = (Button) vContainer.findViewById(R.id.teleplay_btn11);
		btn12 = (Button) vContainer.findViewById(R.id.teleplay_btn12);
		btn13 = (Button) vContainer.findViewById(R.id.teleplay_btn13);
		btn14 = (Button) vContainer.findViewById(R.id.teleplay_btn14);
		btn15 = (Button) vContainer.findViewById(R.id.teleplay_btn15);
		btn16 = (Button) vContainer.findViewById(R.id.teleplay_btn16);
		btn17 = (Button) vContainer.findViewById(R.id.teleplay_btn17);
		btn18 = (Button) vContainer.findViewById(R.id.teleplay_btn18);
		btn19 = (Button) vContainer.findViewById(R.id.teleplay_btn19);
		btn20 = (Button) vContainer.findViewById(R.id.teleplay_btn20);

		addView(vContainer);
	}

	/**
	 * 翻页时候第一个view 获取焦点
	 * 
	 * @Title: TeleplayVideoPageView
	 * @author:张鹏展
	 * @Description:
	 */
	public void getFocusView1() {
		btn1.requestFocus();
	}

	/**
	 * 翻页时候第二个view 获取焦点
	 * 
	 * @Title: TeleplayVideoPageView
	 * @author:张鹏展
	 * @Description:
	 */
	public void getFocusView2() {
		btn10.requestFocus();
	}

	/**
	 * 翻页时候第三个view 获取焦点
	 * 
	 * @Title: TeleplayVideoPageView
	 * @author:张鹏展
	 * @Description:
	 */
	public void getFocusView3() {
		btn11.requestFocus();
	}

	/**
	 * 翻页时候第四个view 获取焦点
	 * 
	 * @Title: TeleplayVideoPageView
	 * @author:张鹏展
	 * @Description:
	 */
	public void getFocusView4() {
		btn20.requestFocus();
	}

	/**
	 * 设置从1-10集用户点遥控器按键向上，要转移到id对于的View上
	 * 
	 * @param id
	 */
	public void setVideo1To10FocusUpId(int id) {
		btn1.setNextFocusUpId(id);
		btn2.setNextFocusUpId(id);
		btn3.setNextFocusUpId(id);
		btn4.setNextFocusUpId(id);
		btn5.setNextFocusUpId(id);
		btn6.setNextFocusUpId(id);
		btn7.setNextFocusUpId(id);
		btn8.setNextFocusUpId(id);
		btn9.setNextFocusUpId(id);
		btn10.setNextFocusUpId(id);
	}

	/**
	 * 设置从11-20集用户点遥控器按键向，焦点要转移到分组也对应View上，此处设置监听器，Acivity代码负责实现此接口
	 * 
	 * @param id
	 */
	private void setVideo11To20DownFocusListener(OnKeyListener listener) {
		btn11.setOnKeyListener(listener);
		btn12.setOnKeyListener(listener);
		btn13.setOnKeyListener(listener);
		btn14.setOnKeyListener(listener);
		btn15.setOnKeyListener(listener);
		btn16.setOnKeyListener(listener);
		btn17.setOnKeyListener(listener);
		btn18.setOnKeyListener(listener);
		btn19.setOnKeyListener(listener);
		btn20.setOnKeyListener(listener);
	}

	/**
	 * 设置从1-10集用户点遥控器按键向，焦点要转移到分组也对应View上，此处设置监听器，Acivity代码负责实现此接口
	 * 
	 * @param id
	 */
	private void setVideo1To10DownFocusListener(OnKeyListener listener) {
		btn1.setOnKeyListener(listener);
		btn2.setOnKeyListener(listener);
		btn3.setOnKeyListener(listener);
		btn4.setOnKeyListener(listener);
		btn5.setOnKeyListener(listener);
		btn6.setOnKeyListener(listener);
		btn7.setOnKeyListener(listener);
		btn8.setOnKeyListener(listener);
		btn9.setOnKeyListener(listener);
		btn10.setOnKeyListener(listener);
	}

	/**
	 * 填充每页数据
	 * 
	 * @param videos
	 */
	public void createPageView(int position) {
		int positionNum = position % 20;
		position++;
		if (positionNum == 0) {
			setVideoView(btn1, position);
		} else if (positionNum == 1) {
			setVideoView(btn2, position);
		} else if (positionNum == 2) {
			setVideoView(btn3, position);
		} else if (positionNum == 3) {
			setVideoView(btn4, position);
		} else if (positionNum == 4) {
			setVideoView(btn5, position);
		} else if (positionNum == 5) {
			setVideoView(btn6, position);
		} else if (positionNum == 6) {
			setVideoView(btn7, position);
		} else if (positionNum == 7) {
			setVideoView(btn8, position);
		} else if (positionNum == 8) {
			setVideoView(btn9, position);
		} else if (positionNum == 9) {
			setVideoView(btn10, position);
		} else if (positionNum == 10) {
			setVideoView(btn11, position);
		} else if (positionNum == 11) {
			setVideoView(btn12, position);
		} else if (positionNum == 12) {
			setVideoView(btn13, position);
		} else if (positionNum == 13) {
			setVideoView(btn14, position);
		} else if (positionNum == 14) {
			setVideoView(btn15, position);
		} else if (positionNum == 15) {
			setVideoView(btn16, position);
		} else if (positionNum == 16) {
			setVideoView(btn17, position);
		} else if (positionNum == 17) {
			setVideoView(btn18, position);
		} else if (positionNum == 18) {
			setVideoView(btn19, position);
		} else if (positionNum == 19) {
			setVideoView(btn20, position);
		}

	}

	/**
	 * 填充每页数据
	 * 
	 * @param videos
	 */
	public void createPageViewText(int position, String text, int btnTag) {
		int positionNum = position % 20;
		position++;
		if (positionNum == 0) {
			setVideoView(btn1, position, text, btnTag);
		} else if (positionNum == 1) {
			setVideoView(btn2, position, text, btnTag);
		} else if (positionNum == 2) {
			setVideoView(btn3, position, text, btnTag);
		} else if (positionNum == 3) {
			setVideoView(btn4, position, text, btnTag);
		} else if (positionNum == 4) {
			setVideoView(btn5, position, text, btnTag);
		} else if (positionNum == 5) {
			setVideoView(btn6, position, text, btnTag);
		} else if (positionNum == 6) {
			setVideoView(btn7, position, text, btnTag);
		} else if (positionNum == 7) {
			setVideoView(btn8, position, text, btnTag);
		} else if (positionNum == 8) {
			setVideoView(btn9, position, text, btnTag);
		} else if (positionNum == 9) {
			setVideoView(btn10, position, text, btnTag);
		} else if (positionNum == 10) {
			setVideoView(btn11, position, text, btnTag);
		} else if (positionNum == 11) {
			setVideoView(btn12, position, text, btnTag);
		} else if (positionNum == 12) {
			setVideoView(btn13, position, text, btnTag);
		} else if (positionNum == 13) {
			setVideoView(btn14, position, text, btnTag);
		} else if (positionNum == 14) {
			setVideoView(btn15, position, text, btnTag);
		} else if (positionNum == 15) {
			setVideoView(btn16, position, text, btnTag);
		} else if (positionNum == 16) {
			setVideoView(btn17, position, text, btnTag);
		} else if (positionNum == 17) {
			setVideoView(btn18, position, text, btnTag);
		} else if (positionNum == 18) {
			setVideoView(btn19, position, text, btnTag);
		} else if (positionNum == 19) {
			setVideoView(btn20, position, text, btnTag);
		}

	}

	public void setKeyDownToGroup(OnKeyListener listener) {
		if (btn11.getVisibility() == View.VISIBLE) {// 这一页已经有10个了，应当设置第二行的按钮在有焦点的情况下，焦点设置到对应的分组按钮上
			setVideo11To20DownFocusListener(listener);
		} else {
			setVideo1To10DownFocusListener(listener);
		}
	}

	/**
	 * Button确定要显示在屏幕上的时候，索要拥有的基本属性
	 * 
	 * @param btn
	 * @param text
	 */
	private void setVideoView(Button btn, int position) {
		btn.setVisibility(View.VISIBLE);
		btn.setTag(position - 1);
		btn.setText(position + "");
	}

	/**
	 * Button确定要显示在屏幕上的时候，索要拥有的基本属性
	 * 
	 * @param btn
	 * @param text
	 */
	private void setVideoView(Button btn, int position, String text, int btnTag) {
		btn.setVisibility(View.VISIBLE);
		btn.setTag(btnTag);
		btn.setText(text);
	}

	/**
	 * 如果从焦点要从其他地方，往此Page转移要转移到此PageView上的第一个Button位置
	 */
	public void setDefault01Btn() {
		btn1.requestFocus();
	}

}
