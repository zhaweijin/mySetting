package com.hiveview.tv.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hiveview.tv.R;

public class TeleplayVideoGroupView extends FrameLayout {

	private LayoutInflater inflater;
	private Context mContext;
	private View vContainer;

	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private IButtonFocusListener buttonFocusListener;

	public TeleplayVideoGroupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public TeleplayVideoGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public TeleplayVideoGroupView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vContainer = inflater.inflate(R.layout.teleplay_video_group_item_layout, null);
		addView(vContainer);

		btn1 = (Button) vContainer.findViewById(R.id.btnG1);
		btn2 = (Button) vContainer.findViewById(R.id.btnG2);
		btn3 = (Button) vContainer.findViewById(R.id.btnG3);
		btn4 = (Button) vContainer.findViewById(R.id.btnG4);
		btn5 = (Button) vContainer.findViewById(R.id.btnG5);
	}

	/**
	 * 往前翻页的时候，最后一个请求焦点
	 * 
	 * @Title: TeleplayVideoGroupView
	 * @author:张鹏展
	 * @Description:
	 */
	public void requestTheOneFouces() {
		btn5.requestFocus();
	}

	/**
	 * 创建电视剧分组按钮
	 * 
	 * @param p
	 * @param text
	 * @param realPosition
	 */
	public void createGroupButton(int p, String text, int realPosition) {
		if (p == 0) {
			setButtonAsButtonGroup(realPosition, btn1, text);
		} else if (p == 1) {
			setButtonAsButtonGroup(realPosition, btn2, text);
		} else if (p == 2) {
			setButtonAsButtonGroup(realPosition, btn3, text);
		} else if (p == 3) {
			setButtonAsButtonGroup(realPosition, btn4, text);
		} else if (p == 4) {
			setButtonAsButtonGroup(realPosition, btn5, text);
		}

	}

	/**
	 * 设置当某个按钮要作为分组按钮显示在屏幕中所拥有的属性
	 * 
	 * @param position
	 * @param btn
	 * @param text
	 */
	private void setButtonAsButtonGroup(int position, Button btn, String text) {
		btn.setVisibility(View.VISIBLE);
		btn.setText(text);
		btn.setTag(position);
		btn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					if (null != buttonFocusListener) {
						int p = (Integer) arg0.getTag();
						buttonFocusListener.processFocus(p);
					}
				}
			}
		});
	}

	/**
	 * 当分组按钮获得焦点时的处理，去翻动电视剧集的ViewPager
	 * 
	 * @author chenlixiao
	 * 
	 */
	public interface IButtonFocusListener {
		public void processFocus(int position);
	}

	public void setSpecialButtonTextColor(int num) {
		int p = num % 5;
		int orange = getResources().getColor(R.color.teleplay_orange);
		int white = getResources().getColor(R.color.white);
		if (p == 0) {
			btn1.setTextColor(orange);
			btn2.setTextColor(white);
			btn3.setTextColor(white);
			btn4.setTextColor(white);
			btn5.setTextColor(white);
		} else if (p == 1) {
			btn2.setTextColor(orange);
			btn1.setTextColor(white);
			btn3.setTextColor(white);
			btn4.setTextColor(white);
			btn5.setTextColor(white);
		} else if (p == 2) {
			btn3.setTextColor(orange);
			btn1.setTextColor(white);
			btn2.setTextColor(white);
			btn4.setTextColor(white);
			btn5.setTextColor(white);
		} else if (p == 3) {
			btn4.setTextColor(orange);
			btn1.setTextColor(white);
			btn2.setTextColor(white);
			btn3.setTextColor(white);
			btn5.setTextColor(white);
		} else if (p == 4) {
			btn5.setTextColor(orange);
			btn1.setTextColor(white);
			btn2.setTextColor(white);
			btn3.setTextColor(white);
			btn4.setTextColor(white);
		}

	}

	public IButtonFocusListener getButtonFocusListener() {
		return buttonFocusListener;
	}

	public void setButtonFocusListener(IButtonFocusListener buttonFocusListener) {
		this.buttonFocusListener = buttonFocusListener;
	}

	/**
	 * 根据num确定在每一页当中那个按钮应该获得焦点，主要是为了处理电视剧11-20集按钮向下的焦点的位置
	 * 
	 * @param num
	 */
	public void setVideoPageButtonNextFocus(int num) {
		int p = num % 5;
		if (p == 0) {
			btn1.requestFocus();
		} else if (p == 1) {
			btn2.requestFocus();
		} else if (p == 2) {
			btn3.requestFocus();
		} else if (p == 3) {
			btn4.requestFocus();
		} else if (p == 4) {
			btn5.requestFocus();
		}
	}

}
