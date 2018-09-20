package com.hiveview.tv.view;

import java.util.List;

import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.WordsEntity;
import com.hiveview.tv.utils.LogUtil;
import com.hiveview.tv.view.television.MarqueeText;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class ThinkWordsView extends LinearLayout {

	private Context mContext;
	private View vContainer;
	private MarqueeText tvWords1;
	private MarqueeText tvWords2;
	private MarqueeText tvWords3;
	private MarqueeText tvWords4;
	private MarqueeText tvWords5;
	private MarqueeText tvWords6;
	private MarqueeText tvWords7;
	private MarqueeText tvWords8;
	private MarqueeText tvWords9;
	private MarqueeText tvWords10;

	public ThinkWordsView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public ThinkWordsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public ThinkWordsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vContainer = inflater.inflate(R.layout.search_think_words_layout, null);
		tvWords1 = (MarqueeText) vContainer.findViewById(R.id.tv_words_1);
		tvWords2 = (MarqueeText) vContainer.findViewById(R.id.tv_words_2);
		tvWords3 = (MarqueeText) vContainer.findViewById(R.id.tv_words_3);
		tvWords4 = (MarqueeText) vContainer.findViewById(R.id.tv_words_4);
		tvWords5 = (MarqueeText) vContainer.findViewById(R.id.tv_words_5);
		tvWords6 = (MarqueeText) vContainer.findViewById(R.id.tv_words_6);
		tvWords7 = (MarqueeText) vContainer.findViewById(R.id.tv_words_7);
		tvWords8 = (MarqueeText) vContainer.findViewById(R.id.tv_words_8);
		tvWords9 = (MarqueeText) vContainer.findViewById(R.id.tv_words_9);
		tvWords10 = (MarqueeText) vContainer.findViewById(R.id.tv_words_10);
		addView(vContainer);
	}

	/**
	 * 设置数据源
	 * 
	 * @param list
	 */
	public void setDataSource(List<WordsEntity> list, OnClickListener listener) {
		if (null == list) {
			LogUtil.info("words list is null!!!!!");
			return;
		}

		initBeforeSetDataSource();

		if (list.size() > 0) {
			int count = list.size();
			for (int i = 0; i < count; i++) {
				WordsEntity entity = list.get(i);
				if (i == 0) {
					setWordsToTextView(tvWords1, entity.getHotWord(), listener);
				} else if (i == 1) {
					setWordsToTextView(tvWords2, entity.getHotWord(), listener);
				} else if (i == 2) {
					setWordsToTextView(tvWords3, entity.getHotWord(), listener);
				} else if (i == 3) {
					setWordsToTextView(tvWords4, entity.getHotWord(), listener);
				} else if (i == 4) {
					setWordsToTextView(tvWords5, entity.getHotWord(), listener);
				} else if (i == 5) {
					setWordsToTextView(tvWords6, entity.getHotWord(), listener);
				} else if (i == 6) {
					setWordsToTextView(tvWords7, entity.getHotWord(), listener);
				} else if (i == 7) {
					setWordsToTextView(tvWords8, entity.getHotWord(), listener);
				} else if (i == 8) {
					setWordsToTextView(tvWords9, entity.getHotWord(), listener);
				} else if (i == 9) {
					setWordsToTextView(tvWords10, entity.getHotWord(), listener);
				}
			}
		} else {
			initBeforeSetDataSource();
		}
	}

	/**
	 * 设置数据源之前的初始化,清除之前的显示状态
	 */
	private void initBeforeSetDataSource() {
		tvWords1.setVisibility(View.INVISIBLE);
		tvWords2.setVisibility(View.INVISIBLE);
		tvWords3.setVisibility(View.INVISIBLE);
		tvWords4.setVisibility(View.INVISIBLE);
		tvWords5.setVisibility(View.INVISIBLE);
		tvWords6.setVisibility(View.INVISIBLE);
		tvWords7.setVisibility(View.INVISIBLE);
		tvWords8.setVisibility(View.INVISIBLE);
		tvWords9.setVisibility(View.INVISIBLE);
		tvWords10.setVisibility(View.INVISIBLE);
	}

	/**
	 * 初始化每一个联想词View
	 * 
	 * @param tv
	 * @param text
	 */
	private void setWordsToTextView(MarqueeText tv, String text, OnClickListener listener) {
		tv.setVisibility(View.VISIBLE);
		tv.setText(text);
		tv.setOnFocusChangeListener(focusChangeListener);
		if (null != listener) {
			tv.setOnClickListener(listener);
		}

	}

	/**
	 * 当联想词view焦点变化时的处理
	 */
	OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			MarqueeText tv = (MarqueeText) v;
			if (hasFocus) {
				tv.setStart(true);
				tv.setText(tv.getText().toString());
			} else {
				tv.setStart(false);
			}
		}
	};

}
