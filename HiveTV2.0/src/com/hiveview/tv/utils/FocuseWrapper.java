package com.hiveview.tv.utils;

import android.view.View;

public class FocuseWrapper {
	private View content;

	public FocuseWrapper(View content) {
		this.content = content;
	}

	public int getWidth() {
		return content.getLayoutParams().width;
	}

	public int getHeight() {
		return content.getLayoutParams().height;
	}

	public void setWidth(int w) {
		content.getLayoutParams().width = w;
		content.requestLayout();
	}

	public void setHeight(int h) {
		content.getLayoutParams().height = h;
		content.requestLayout();
	}
}
