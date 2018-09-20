package com.hiveview.tv.view;

import com.hiveview.tv.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LiveAnimationView extends ImageView {
	public LiveAnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LiveAnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LiveAnimationView(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.setImageResource(R.drawable.animation_domy_live);
		final AnimationDrawable mAnimationDrawable = (AnimationDrawable) this.getDrawable();
		mAnimationDrawable.start();
	}
}
