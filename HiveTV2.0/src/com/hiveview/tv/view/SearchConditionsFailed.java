package com.hiveview.tv.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.tv.R;

/**
 * 分类搜索失败的提示信息
 * 
 * @ClassName: SearchConditionsFailed
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年7月15日 上午11:05:36
 * 
 */
public class SearchConditionsFailed extends FrameLayout implements OnClickListener {

	/**
	 * 分类搜索失败的提示信息1
	 * 
	 * @Fields tvHint1
	 */
	private TextView tvHint1;

	/**
	 * 分类搜索失败的提示信息2
	 * 
	 * @Fields tvHint2
	 */
	private TextView tvHint2, tvHint2_1, tvHint2_2, tvHint2_3, tvHint2_4;
	/**
	 * 打开视频搜索的按钮
	 * 
	 * @Fields search_iamge
	 */
	private ImageView search_iamge;

	/**
	 * 布局文件
	 * 
	 * @Fields relativeLayout
	 */
	private RelativeLayout relativeLayout;

	public SearchConditionsFailed(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SearchConditionsFailed(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
	}

	public SearchConditionsFailed(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	/**
	 * @Title: SearchConditionsFailed
	 * @author:张鹏展
	 * @Description:
	 */
	private void initHintView1(boolean isStat) {
		if (isStat) {
			tvHint1.setVisibility(View.VISIBLE);
		} else {
			tvHint1.setVisibility(View.INVISIBLE);
		}

	}

	/**
	 * @Title: SearchConditionsFailed
	 * @author:张鹏展
	 * @Description:
	 */
	private void initHintView2(boolean isStat) {
		if (isStat) {
			tvHint2.setVisibility(View.VISIBLE);
			tvHint2_1.setVisibility(View.VISIBLE);
			tvHint2_2.setVisibility(View.VISIBLE);
			tvHint2_3.setVisibility(View.VISIBLE);
			tvHint2_4.setVisibility(View.VISIBLE);
			search_iamge.setVisibility(View.VISIBLE);
			search_iamge.setOnClickListener(this);
		} else {
			tvHint2.setVisibility(View.INVISIBLE);
			tvHint2_1.setVisibility(View.INVISIBLE);
			tvHint2_2.setVisibility(View.INVISIBLE);
			tvHint2_3.setVisibility(View.INVISIBLE);
			tvHint2_4.setVisibility(View.INVISIBLE);
			search_iamge.setVisibility(View.INVISIBLE);
		}
	}

	private void initView() {
		// 添加布局
		relativeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.search_conditions_fail, null);
		//提示信息1
		tvHint1 = (TextView) relativeLayout.findViewById(R.id.search_fail_hint1);
		//提示信息2
		tvHint2 = (TextView) relativeLayout.findViewById(R.id.search_fail_hint2);
		tvHint2_1 = (TextView) relativeLayout.findViewById(R.id.search_fail_hint2_1);
		tvHint2_2 = (TextView) relativeLayout.findViewById(R.id.search_fail_hint2_2);
		tvHint2_3 = (TextView) relativeLayout.findViewById(R.id.search_fail_hint2_3);
		tvHint2_4 = (TextView) relativeLayout.findViewById(R.id.search_fail_hint2_4);
		// 打开视频搜索按钮
		search_iamge = (ImageView) relativeLayout.findViewById(R.id.search_fail_hint2_5);
		// 添加布局到当前
		addView(relativeLayout);
	}

	/**
	 * 初始化要显示的布局 不调用就不会显示里边的view true initHintView1 显示 false initHintView2 显示
	 * 
	 * @Title: SearchConditionsFailed
	 * @author:张鹏展
	 * @Description:
	 * @param isSwitch
	 */
	public void initView(boolean isSwitch) {
		initHintView1(isSwitch);
		initHintView2(!isSwitch);
	}

	/*
	 * 点击监听事件 (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		// 搜索按钮的图片
		case R.id.search_fail_hint2_5:
			if (null != searchBtnOnClick)
				searchBtnOnClick.SearchOnClick(v);
			break;
		}

	}

	/**
	 * 回调监听
	 * 
	 * @Fields searchBtnOnClick
	 */
	private SearchBtnOnClick searchBtnOnClick;

	/**
	 * 设置监听回调
	 * 
	 * @Title: SearchConditionsFailed
	 * @author:张鹏展
	 * @Description:
	 * @param searchBtnOnClick
	 */
	public void setSearchBtnOnClick(SearchBtnOnClick searchBtnOnClick) {
		this.searchBtnOnClick = searchBtnOnClick;
	}

	/**
	 * 按钮监听回调
	 * 
	 * @ClassName: SearchBtnOnClick
	 * @Description:
	 * @author: zhangpengzhan
	 * @date 2014年7月15日 上午11:33:35
	 * 
	 */
	interface SearchBtnOnClick {
		public void SearchOnClick(View v);
	}

}
