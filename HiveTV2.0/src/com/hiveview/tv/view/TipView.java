package com.hiveview.tv.view;

import com.hiveview.tv.service.exception.TipManager;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TipView extends RelativeLayout{
	
	private TextView phoneTextView;
	private TextView codeTextView;
	private TextView desTextView;

	public TipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TipView(Context context) {
		super(context);
		init();
	}

	private void init(){
		
		phoneTextView = new TextView(getContext());
		LayoutParams phoneParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		phoneParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		phoneParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		phoneParams.leftMargin = 27;
		phoneParams.bottomMargin = 35;
		phoneTextView.setTextColor(Color.GRAY);
		phoneTextView.setBackgroundColor(Color.TRANSPARENT);
		phoneTextView.setTextSize(24);
		phoneTextView.setText("E0000023");
		this.addView(phoneTextView, phoneParams);
		
		codeTextView = new TextView(getContext());
		LayoutParams codeParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		codeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		codeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		codeParams.rightMargin = 27;
		codeParams.bottomMargin = 35;
		codeTextView.setTextColor(Color.GRAY);
		codeTextView.setBackgroundColor(Color.TRANSPARENT);
		codeTextView.setTextSize(24);
		codeTextView.setText("E0000023");
		this.addView(codeTextView, codeParams);
		
		desTextView = new TextView(getContext());
		LayoutParams desParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		desParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		desParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		desParams.rightMargin = 27;
		desParams.bottomMargin = 35;
		desTextView.setTextColor(Color.WHITE);
		desTextView.setBackgroundColor(Color.TRANSPARENT);
		desTextView.setTextSize(40);
		desTextView.setText("错误提示错误提示");
		desTextView.setGravity(Gravity.CENTER);
		this.addView(desTextView, desParams);
		
	}
	
	public void showTipWithCode(String code){
		
		TipManager manager = TipManager.getInstance();
		String content = manager.getTip(code);
		desTextView.setText(content);
		codeTextView.setText("错误代码:"+code);
		phoneTextView.setText("客服电话:"+"01012345678");
	}
	
}
 