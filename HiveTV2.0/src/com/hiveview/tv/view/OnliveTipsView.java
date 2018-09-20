package com.hiveview.tv.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiveview.tv.R;
import com.hiveview.tv.activity.OnlivePlayerActivity;

public class OnliveTipsView extends RelativeLayout {

	private static final String TAG = "OnliveTipsView";

	/**
	 * 确定按钮
	 */
	private Button btnGotoOnlive;

	private Handler mHandler = null;

	private View view = null;

	public Button getBtnGotoOnlive() {
		return btnGotoOnlive;
	}

	/**
	 * 不在提醒按钮
	 */
	private Button btnNoTipsAgain;

	/**
	 * 提醒界面的小图标
	 */
	private ImageView imageView;
	/**
	 * 是否在执行动画
	 */
	private boolean isAnimation = false;

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	/**
	 * 提示界面的提示信息
	 */
	private TextView textView;

	private FrameLayout flGotoOlive;
	private FrameLayout flNoTipsAgain;

	private Context mContext;

	OnConfirmAndCancal listener;

	public OnliveTipsView(Context context, Handler handler) {
		super(context);
		mContext = context;
		this.mHandler = handler;
		init();
	}

	public void setListener(OnConfirmAndCancal listener) {
		this.listener = listener;
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.view_onlive_tips, null);
		Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.in_translate_top);
		view.startAnimation(animation);
		this.addView(view);

		btnGotoOnlive = (Button) this.findViewById(R.id.btn_goto_onlive);
		flGotoOlive = (FrameLayout) this.findViewById(R.id.fl_goto_onlive);
		btnNoTipsAgain = (Button) this.findViewById(R.id.btn_no_tips_again);
		flNoTipsAgain = (FrameLayout) this.findViewById(R.id.fl_no_tip_again);
		// 提示小图标
		imageView = (ImageView) this.findViewById(R.id.iv_television_logo);
		// 提示信息
		textView = (TextView) this.findViewById(R.id.tv_television_info);

		flGotoOlive.setOnFocusChangeListener(mOnFocusChangeListener);
		flNoTipsAgain.setOnFocusChangeListener(mOnFocusChangeListener);

		flGotoOlive.setOnClickListener(mOnClickListener);
		flNoTipsAgain.setOnClickListener(mOnClickListener);

		flGotoOlive.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
					// listener.OnCancal();
					startOnliveTipAnimation(R.id.fl_no_tip_again);
				}
				return false;
			}
		});
		flNoTipsAgain.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
					// listener.OnCancal();
					startOnliveTipAnimation(R.id.fl_no_tip_again);
				}
				return false;
			}
		});

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startOnliveTipAnimation(R.id.fl_no_tip_again);
			}
		}, 30 * 1000);
	}

	/**
	 * 填充提示信息
	 * 
	 * @param string
	 */
	public void setTextInfo(String string) {
		if (null != textView) {
			textView.setText("");
			textView.setText(string);
		}
	}

	public interface OnConfirmAndCancal {
		void OnConfirm();

		void OnCancal();
	}

	/**
	 * 关闭
	 * 
	 * @Title: OnliveTipsView
	 * @author:郭松胜
	 * @Description: TODO
	 */
	public void closeOnliveTip() {
		startOnliveTipAnimation(R.id.fl_no_tip_again);
	}

	/**
	 * 按键
	 */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startOnliveTipAnimation(v.getId());
		}
	};

	/**
	 * 执行动画
	 * 
	 * @Title: OnliveTipsView
	 * @author:郭松胜
	 * @Description: TODO
	 * @param viewId
	 */
	private void startOnliveTipAnimation(int viewId) {
		if (!isAnimation) {
			isAnimation = true;
			Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.out_translate_top);
			animation.setAnimationListener(new OnliveTipAnimationListener(viewId));
			view.startAnimation(animation);
		}
	}

	/**
	 * @ClassName: OnliveTipAnimationListener
	 * @Description: TODO
	 * @author: guosongsheng
	 * @date 2014年9月3日 下午6:59:35
	 * 
	 */
	class OnliveTipAnimationListener implements AnimationListener {
		/**
		 * 点击View的id
		 */
		private int viewId;

		/**
		 * @param @param viewId
		 */
		public OnliveTipAnimationListener(int viewId) {
			super();
			this.viewId = viewId;
		}

		@Override
		public void onAnimationEnd(Animation arg0) {
			// 动画执行完后执行后续操作
			switch (viewId) {
			case R.id.fl_goto_onlive:
				listener.OnConfirm();
				//关闭直播页面的浮层
				closeOnliveMenu();
				break;
			case R.id.fl_no_tip_again:
				listener.OnCancal();
				break;
			}
			isAnimation = false;
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {
		}

	}

	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				switch (v.getId()) {
				case R.id.fl_goto_onlive: 
					btnGotoOnlive.setTextColor(Color.parseColor("#FFFFFF"));
					break;
				case R.id.fl_no_tip_again:
					btnNoTipsAgain.setTextColor(Color.parseColor("#FFFFFF"));
					break;
				}
			} else {
				switch (v.getId()) {
				case R.id.fl_goto_onlive:
					btnGotoOnlive.setTextColor(Color.parseColor("#9a9a9a"));
					break;
				case R.id.fl_no_tip_again:
					btnNoTipsAgain.setTextColor(Color.parseColor("#9a9a9a"));
					break;
				}
			}
		}

	};
	
	/**
	 * 如果直播提醒在直播里面出现 点击去看的时候关闭浮层
	 * @Title: OnliveTipsView
	 * @author:郭松胜
	 * @Description: TODO
	 */
	public void closeOnliveMenu() {
		if(OnlivePlayerActivity.isMeunShow()){
			OnlivePlayerActivity.closeMune();
		}
	}
}
