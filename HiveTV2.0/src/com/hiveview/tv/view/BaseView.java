package com.hiveview.tv.view;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.tv.R;
import com.hiveview.tv.service.entity.ThirdListEntity;
import com.hiveview.tv.utils.LogUtil;

/***
 * 
 * @ClassName: HiveBaseView
 * @Description: TODO
 * 
 */
public abstract class BaseView extends FrameLayout {
	/**
	 * 输出的日志类名标签
	 */
	private final String CLASS_TAG = "BaseView";
	/**
	 * 当前类的全局Context
	 */
	protected Context mContext = null;

	/**
	 * 根据layout.xml载入成View的inflater
	 */
	protected LayoutInflater mInflater = null;

	/**
	 * 当前页面所有View的容器
	 */
	protected View viewContainer = null;

	/**
	 * ItemView的按键事件发生执行回调的Listener
	 */
	protected CallBackItemViewKeyListener mKeyListener = null;

	/**
	 * ItemView的焦点改变事件发生时执行回调的Listener
	 */
	protected CallBackItemViewFocusListener mFocusListener = null;

	/**
	 * ItemView单击事件回调接口
	 */
	protected CallBackItemViewClickListener mClickListener = null;

	/**
	 * 当前每页显示多少个
	 */
	protected int mEveryPageSize = 0;

	/**
	 * 当前页显示多少行
	 */
	protected int mEveryPageLine = 2;
	/**
	 * 每次要移动的H
	 * 
	 * @Fields h
	 */
	private int h = 0;
	/**
	 * animation 设置
	 * 
	 * @Fields pvhX
	 */
	private PropertyValuesHolder pvhX;
	/**
	 * 动画对象
	 * 
	 * @Fields translateAnimator
	 */
	private ObjectAnimator translateAnimator;
	/**
	 * 动画持续时间，默认300ms
	 * 
	 * @Fields animatorContinueTime
	 */
	private int animatorContinueTime = 300;
	/**
	 * 初始化，第一个view 的位置
	 * 
	 * @Fields viewFirstIndex
	 */
	private int viewFirstIndex = 0;

	/**
	 * view的实时的索引值
	 * 
	 * @Fields viewNowIndex
	 */
	private int viewNowIndex = 0;

	/**
	 * 第一个焦点来到这个view
	 * 
	 * @Fields isFirstRequestFocus
	 */
	private boolean isFirstRequestFocus = false;

	/**
	 * 存储当前页面上所有的ItemView
	 */
	protected ArrayList<View> mItemViewList = new ArrayList<View>();

	/**
	 * 执行下一个动画
	 * 
	 * @Fields hasNext
	 */
	private boolean hasNext = false;

	/**
	 * 存储当前页面数据
	 * 
	 * @Fields mItemEntityList
	 */
	protected ArrayList<HiveBaseEntity> mItemEntityList = new ArrayList<HiveBaseEntity>();

	public BaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public BaseView(Context context, List<ThirdListEntity> list) {
		super(context);
		this.mContext = context;
		initBase(context, list);
	}

	/**
	 * 初始化PageView
	 */
	private void initBase(Context context, List<ThirdListEntity> list) {
		this.mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		isFirstRequestFocus = false;
		initView(list);
	}

	/***
	 * 用于实例化当前页面的子View，在此方法中必须首先调用 initViewContainer(int layoutId)方法
	 * 
	 * @Title: HiveBaseView
	 * @Description: TODO
	 */
	protected abstract void initView(List<ThirdListEntity> list);

	/***
	 * 从layout文件中得到PageView的内容View
	 * 
	 * @Title: HiveBaseView
	 * @Description: TODO
	 * @param layoutId
	 *            布局文件Id
	 */
	protected void initViewContainer(int layoutId, LayoutParams lp) {
		viewContainer = mInflater.inflate(layoutId, null);
		initViewContainer(viewContainer, lp);

	}

	/***
	 * 从layout文件中得到PageView的内容View
	 * 
	 * @Title: HiveBaseView
	 * @Description: TODO
	 * @param layoutId
	 *            布局文件Id
	 */
	protected void initViewContainer(int layoutId) {
		viewContainer = mInflater.inflate(layoutId, null);
		initViewContainer(viewContainer);

	}

	/***
	 * 从layout文件中得到PageView的内容View
	 * 
	 * @Title: HiveBaseView
	 * @Description: TODO
	 * @param layoutId
	 *            布局文件Id
	 */
	protected void initViewContainer(View layoutId, LayoutParams lp) {
		viewContainer = layoutId;
		addView(layoutId, lp);// 内容View添加到PageView中
	}

	/***
	 * 从layout文件中得到PageView的内容View
	 * 
	 * @Title: HiveBaseView
	 * @Description: TODO
	 * @param layoutId
	 *            布局文件Id
	 */
	protected void initViewContainer(View layoutId) {
		viewContainer = layoutId;
		addView(layoutId);// 内容View添加到PageView中
	}

	/**
	 * 初始化指定id的View，并添加按键监听，为处理用户按键设置焦点设置相关信息 ，注意此时的View的Id一定是能获得焦点的View
	 * 
	 * @param itemViewId
	 * 
	 * @return
	 */
	protected View findItemView(int itemViewId) {
		View itemView = viewContainer.findViewById(itemViewId);
		return findItemView(itemView);
	}

	/**
	 * 行数标识
	 * 
	 * @Fields lineIndex
	 */
	private int lineIndex = 0;

	/**
	 * 设置行数标识
	 * 
	 * @Title: BaseView
	 * @author:张鹏展
	 * @Description:
	 * @param lineIndex
	 */
	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * 跟随数据list的大小去设置view 的状态 可以优化
	 * 
	 * @param itemViewId
	 * 
	 * @return
	 */
	protected void findItemViewByList(ArrayList<ThirdListEntity> mItemEntityList) {
		ViewGroup v = (ViewGroup) viewContainer;
		int childs = v.getChildCount();
		if (null == mItemEntityList)
			return;
		for (int i = 0; i < mItemEntityList.size(); i++) {
			if (i < childs) {
				// 得到对应的view
				View itemView = v.getChildAt(i);
				// 获取数据对象列表的数据
				ThirdListEntity entity = (ThirdListEntity) mItemEntityList.get(i);
				// 判断是显示还是隐藏
				itemView.setVisibility(entity.getThirdClassName().equals("none") ? View.INVISIBLE : View.VISIBLE);
				// 把数据附着给view
				itemView.setTag(entity);
				// 设置文字信息
				((TextView) itemView.findViewById(R.id.textView1)).setText(entity.getThirdClassName());
				if (!entity.getThirdClassName().equals("none")) {
					findItemView(itemView);
				}
			}
		}
	}

	public int getH() {
		return h;
	}

	/**
	 * 初始化指定id的View，并添加按键监听，为处理用户按键设置焦点设置相关信息 ，注意此时的View的Id一定是能获得焦点的View，
	 * <p>
	 * 即：属性android:focusable="true"
	 */
	protected View findItemView(View focusView) {
		if (null == focusView) {
			Log.e(VIEW_LOG_TAG, "focus view is null!");
			return null;
		}
		mItemViewList.add(focusView);
		// 添加点击监听
		focusView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (null != mClickListener) {
					mClickListener.onCallBackClick(arg0);
				}
			}
		});

		focusView.setOnFocusChangeListener(new OnFocusChangeListener() {// 监听ItemView焦点改变事件
					@Override
					public void onFocusChange(View v, boolean hasFocus) {// 回调焦点改变事件
						if (null != mFocusListener) {
							mFocusListener.onCallBackFocusChange(lineIndex, v, hasFocus);
						}
						if (!isFirstRequestFocus && hasFocus) {
							requestFirstViewFocus(true);
							return;
						}
						if (!isFirstRequestFocus)
							;
						// ((TextView)v.findViewById(R.id.textView1)).setTextColor(0xffff8b00);

					}
				});

		focusView.setOnKeyListener(new OnKeyListener() {// 监听ItemView按键
					@Override
					public boolean onKey(View item, int arg1, KeyEvent event) {
						// 上一个动画没有执行完成就不执行下一个
						if (hasNext)
							return true;

						if (null != mKeyListener) {// 回调按键监听
							mKeyListener.onKey(lineIndex, item, arg1, event);
						}
						// 当前的view的列表不为中的情况
						if (null != mItemViewList && mItemViewList.size() != 0)
							viewNowIndex = mItemViewList.indexOf(item);
						// view 上按下左键
						if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
							Log.d(CLASS_TAG, "KEYCODE_DPAD_LEFT==>viewIndex::" + mItemViewList.indexOf(item) + "h=" + h + "viewNowIndex="
									+ viewNowIndex);
							// 如果在第一个的话就不能在移动了.其他的情况下获取他上一个view 的寬移动
							h += viewNowIndex == 0 ? 0 : mItemViewList.get(mItemViewList.indexOf(item) - 1).getWidth();
							// 执行属性动画操作
							if (viewNowIndex == 0)
								return true;
							((TextView) item.findViewById(R.id.textView1)).setTextColor(0xffffffff);
							hasNext = true;
							setAnimatorObject(h, mItemViewList.get(mItemViewList.indexOf(item) - 1), item);
						}
						// view 上按下右键
						if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
							Log.d(CLASS_TAG, "KEYCODE_DPAD_RIGHT==>viewIndex::" + mItemViewList.indexOf(item) + "h=" + h + "viewNowIndex="
									+ viewNowIndex);
							// 如果是最后一个就不移动了，但是在最后一个的情况下就也不 移动了
							h -= viewNowIndex == mItemViewList.size() - 1 ? 0 : item.getWidth();
							// 执行属性动画操作
							if (viewNowIndex == mItemViewList.size() - 1)
								return true;
							((TextView) item.findViewById(R.id.textView1)).setTextColor(0xffffffff);
							hasNext = true;
							setAnimatorObject(h, mItemViewList.get(mItemViewList.indexOf(item) + 1), item);
						}
						if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
							isFirstRequestFocus = false;
							if (null != downView)
								downView.requestFocus();
						}
						if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
							isFirstRequestFocus = false;
							if (null != upView)
								upView.requestFocus();
						}
						Log.d(CLASS_TAG, "setOnKeyListener==>h::" + h);
						return false;
					}
				});

		return focusView;
	}

	/**
	 * 
	 * @Title: BaseView
	 * @author:lihongji
	 * @Description: 语音选择
	 * @param nowIndex
	 */
	public void moveTo(int nowIndex,int lineIndex,int firstsize) {
		View item1 = mItemViewList.get(viewNowIndex);
		((TextView) mItemViewList.get(viewNowIndex).findViewById(R.id.textView1)).setTextColor(0xffffffff);
		View item = mItemViewList.get(nowIndex);
		 h = item.getWidth() * (firstsize - nowIndex);
		viewNowIndex=nowIndex;
		LogUtil.info("moveTo-->h=" + h+",viewNowIndex"+viewNowIndex);
		if (null != mFocusListener) {
			mFocusListener.onCallBackFocusChange(lineIndex, item, true);
		}
		mItemViewList.get(viewNowIndex).requestFocus();
		mItemViewList.get(viewNowIndex).requestFocusFromTouch();
		((TextView) mItemViewList.get(viewNowIndex).findViewById(R.id.textView1)).setTextColor(0xffff8b00);
		hasNext = true;
		setAnimatorObject(h, item, item1);
	}

	/**
	 * 自定义动画时间
	 * 
	 * @Title: BaseView
	 * @author:张鹏展
	 * @Description:
	 * @param animatorContinueTime
	 */
	public void setAnimatorContinueTime(int animatorContinueTime) {
		this.animatorContinueTime = animatorContinueTime;
	}

	/**
	 * 初始化第一个view 默认是第几个的位置
	 * 
	 * @Title: BaseView
	 * @author:张鹏展
	 * @Description:
	 * @param viewFirstIndex
	 */
	public void setFirstViewIndex(int viewFirstIndex) {
		// 如果是错误的情况就不设置
		if (null == mItemViewList || mItemViewList.size() == 0 || viewFirstIndex > mItemViewList.size())
			return;
		// 设置初始化获取焦点的view
		this.viewFirstIndex = viewFirstIndex;
		this.viewNowIndex = viewFirstIndex;
		((TextView) mItemViewList.get(viewNowIndex).findViewById(R.id.textView1)).setTextColor(0xffff8b00);
		Log.d(CLASS_TAG, "setFirstViewIndex==>viewFirstIndex::" + viewFirstIndex);
	}

	public void requestFirstViewFocus(boolean isFirst) {
		// 表示焦点在当前view
		isFirstRequestFocus = isFirst;
		// 找到那个view获取焦点
		if (viewNowIndex < mItemViewList.size()) {
			mItemViewList.get(viewNowIndex).requestFocus();
			mItemViewList.get(viewNowIndex).requestFocusFromTouch();
			((TextView) mItemViewList.get(viewNowIndex).findViewById(R.id.textView1)).setTextColor(0xffff8b00);
		}
		// 初始化或者重置view要移动的数量
		Log.d(CLASS_TAG, "requestFirstViewFocus==>viewFirstIndex::" + viewFirstIndex + "realy get focus::"
				+ mItemViewList.get(viewNowIndex).requestFocus());
	}

	public void requestFirstViewFocus() {
		// 找到那个view获取焦点
		if (viewFirstIndex < mItemViewList.size() && !isFirstRequestFocus) {
			mItemViewList.get(viewFirstIndex).requestFocus();
			mItemViewList.get(viewFirstIndex).requestFocusFromTouch();
		}
		((TextView) mItemViewList.get(viewFirstIndex).findViewById(R.id.textView1)).setTextColor(0xffff8b00);
		// 初始化或者重置view要移动的数量
		h = 0;
		Log.d(CLASS_TAG, "requestFirstViewFocus==>viewFirstIndex::" + viewFirstIndex);
	}

	/**
	 * 设置ItemView的存储Entity和位置信息
	 */
	public void setItemViewTag(HiveBaseEntity entity, int positionInItemView, View item1) {
		item1.setVisibility(View.VISIBLE);
		entity.setPositionInItemView(positionInItemView);
		item1.setTag(entity);
	}

	/**
	 * 上下要走的view
	 * 
	 * @Fields upView
	 */
	private View upView = null;
	private View downView = null;

	/**
	 * 按上健要获取焦点
	 * 
	 * @Title: BaseView
	 * @author:张鹏展
	 * @Description:
	 * @param v
	 * @return
	 */
	public void setUpViewRequest(View v) {
		this.upView = (View) v;
	}

	/**
	 * 按下健要获取焦点
	 * 
	 * @Title: BaseView
	 * @author:张鹏展
	 * @Description:
	 * @param v
	 * @return
	 */
	public void setDownViewRequest(View v) {
		this.downView = (View) v;
	}

	/**
	 * 重置view 的位置
	 * 
	 * @Title: BaseView
	 * @author:张鹏展
	 * @Description:
	 */
	public void setResetViewIndex() {
		// 如果为空或者firstview设置不正确就退出设置
		if (null == mItemViewList || mItemViewList.size() == 0 || viewFirstIndex > mItemViewList.size())
			return;
		((TextView) mItemViewList.get(viewNowIndex).findViewById(R.id.textView1)).setTextColor(0xffffffff);
		// 重置view 的位置
		viewNowIndex = viewFirstIndex;
		if (null != mFocusListener) {
			ThirdListEntity entity = new ThirdListEntity();
			entity.setThirdClassId(0);
			entity.setThirdClassName(getResources().getString(R.string.list_top_category));
			View v = new View(getContext());
			v.setTag(entity);
			mFocusListener.onCallBackFocusChange(0, v, true);
		}
		if (null != mKeyListener) {// 回调按键监听
			ThirdListEntity entity = new ThirdListEntity();
			entity.setThirdClassId(0);
			entity.setThirdClassName(getResources().getString(R.string.list_top_category));
			View v = new View(getContext());
			v.setTag(entity);
			mKeyListener.onKey(lineIndex, v, 0, null);
		}
		// 执行属性动画操作
		setAnimatorObject((float) 0, null, null);
		((TextView) mItemViewList.get(viewFirstIndex).findViewById(R.id.textView1)).setTextColor(0xffff8b00);
		// 初始化或者重置view要移动的数量
		h = 0;

		// requestFirstViewFocus();
	}

	public View getViewList() {
		return mItemViewList.get(viewNowIndex);
	}

	// 设置动画的位置属性
	private void setAnimatorObject(float h, final View view, final View nowView) {
		pvhX = PropertyValuesHolder.ofFloat("translationX", h);
		translateAnimator = ObjectAnimator.ofPropertyValuesHolder(viewContainer, pvhX);
		translateAnimator.setDuration(animatorContinueTime);
		translateAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				requestLayout();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				hasNext = false;
				if (null != nowView)
					((TextView) nowView.findViewById(R.id.textView1)).setTextColor(0xffffffff);
				if (null != view)
					((TextView) view.findViewById(R.id.textView1)).setTextColor(0xffff8b00);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		translateAnimator.start();
	}

	/**
	 * 设置ItemView焦点改变回调监听
	 */
	public void setCallBackFocusListener(CallBackItemViewFocusListener mFocusListener) {
		this.mFocusListener = mFocusListener;
	}

	/**
	 * 设置ItemView按键事件回调监听
	 */
	public void setCallBackKeyListener(CallBackItemViewKeyListener mKeyListener) {
		this.mKeyListener = mKeyListener;
	}

	/**
	 * 设置ItemView单击事件回调接口
	 */
	public void setCallBackClickListener(CallBackItemViewClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}

	/**
	 * 定义当PageView中的每一项View发生点击事件，焦点事件，按键事件时分别回调的方法;此接口的具体实现应该是在Activity中，
	 * 主要是考虑到PageView中的Item发生事件可能要操作ViewPager之外的View。
	 */
	public interface CallBackItemViewKeyListener {
		/**
		 * View上发生按键事件回调此方法
		 */
		public boolean onKey(int lines, View v, int keyCode, KeyEvent event);
	}

	/**
	 * 当ItemView焦点发生变化时回调
	 */
	public interface CallBackItemViewFocusListener {
		public void onCallBackFocusChange(int lines, View arg0, boolean has);
	}

	/**
	 * 当前获得焦点的ItemView发生单击事件回调此接口
	 * 
	 */
	public interface CallBackItemViewClickListener {
		public void onCallBackClick(View v2);
	}

}
