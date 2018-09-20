package com.hiveview.tv.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class CategoryViewPager extends ViewPager {

	public CategoryViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CategoryViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (this.getCurrentItem() == this.getAdapter().getCount() - 1) {
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}
}
