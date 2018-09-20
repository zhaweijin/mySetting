package com.hiveview.tv.view.pager3d;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.animation.Animation.AnimationListener;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 执行当PageView发生改变时要执行的动画库
 * 
 * @ClassName: PageChangeAnimationFactory
 * @Description: TODO
 * @author: 陈丽晓
 * @date 2014-6-11 下午3:02:14
 * 
 */
@SuppressLint("NewApi")
public class PageChangeAnimationFactory {

	private int duration = 600;

	/**
	 * TabPageView每次动画旋转偏转的正单元角度
	 */
	protected final float UNIT_ANGLE = 60.0f;

	/**
	 * TabPageView每次动画旋转偏转的两倍正单元角度
	 */
	protected final float DOUBLE_UNIT_ANGLE = 2 * UNIT_ANGLE;

	/**
	 * TabPageView每次动画旋转偏转的负单元角度
	 */
	protected final float NEGATIVE_UNIT_ANGLE = -60.0f;

	/**
	 * TabPageView每次动画旋转偏转的两倍负单元角度
	 */
	protected final float DOUBLE_NEGATIVE_UNIT_ANGLE = 2 * NEGATIVE_UNIT_ANGLE;

	/**
	 * 当TabPageView处于左侧，右侧，侧偏的位置时透明度
	 */
	protected final float SIDE_ALPHA = 0.0f;

	/**
	 * 从平面的0度位置偏转到UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation _0ToUnitRotateYAnimation = null;

	/**
	 * 从平面的UNIT_ANGLE度位置偏转到0度的动画,时长duration
	 */
	private MatrixRotateYAnimation unitTo0RotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到UNIT_ANGLE度的动画，时长0ms
	 */
	private MatrixRotateYAnimation _0ToUnitImmediatelyRotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到-UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation negUnitRotateYAnimation = null;

	/**
	 * 从平面的-UNIT_ANGLE度位置偏转到0度的动画,时长duration
	 */
	private MatrixRotateYAnimation negUnitTo0RotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到-UNIT_ANGLE度的动画,时长0ms
	 */
	private MatrixRotateYAnimation _0ToNegUnitImmediatelyRotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到2*-UNIT_ANGLE度的动画,时长0ms
	 */
	private MatrixRotateYAnimation _0ToNegUnit2ImmediatelyRotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到-UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation _0ToNegUnitRotateYAnimation = null;

	/**
	 * 从平面的-UNIT_ANGLE度位置偏转到2*-UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation negUnitToNeg2UnitRotateYAnimation = null;

	/**
	 * 从平面的2*-UNIT_ANGLE度位置偏转到-UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation neg2UnitToUnitRotateYAnimation = null;

	/**
	 * 从平面的2*UNIT_ANGLE度位置偏转到UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation _2UnitToUnitRotateYAnimation = null;

	/**
	 * 从平面的UNIT_ANGLE度位置偏转到2*UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation unitTo2UnitRotateYAnimation = null;

	/**
	 * 从平面的2*UNIT_ANGLE度位置偏转到2*-UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation _2UnitToNeg2UnitRotateYAnimation = null;

	/**
	 * 从平面的2*UNIT_ANGLE度位置偏转到2*-UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation neg2UnitTo2UnitRotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到2*UNIT_ANGLE度的动画,时长duration
	 */
	private MatrixRotateYAnimation unit2RotateYAnimation = null;

	/**
	 * 从平面的0度位置偏转到2*UNIT_ANGLE度的动画，时长0ms，不需要动画过程
	 */
	private MatrixRotateYAnimation _0ToUnit2ImmediatelyRotateYAnimation = null;

	private String TAG = "PageChangeAnimationFactory";

	/**
	 * 创建或返回一个从0度偏转到正UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get0ToUnitRotateAnimation(TabBasePageView pageView) {
		if (null == _0ToUnitRotateYAnimation) {
			_0ToUnitRotateYAnimation = createRotateYAnimation(0f, UNIT_ANGLE, false);
		}

		AnimatorSet set = new AnimatorSet();
		Animator alphaAnimator = ObjectAnimator.ofFloat(pageView, "alpha", 1f, SIDE_ALPHA).setDuration(duration);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 1f, 0.93f).setDuration(duration);
		set.playTogether(alphaAnimator, scaleAnimator);
		pageView.setPageViewChangeAnimator(set, _0ToUnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从UNIT_ANGLE度偏转到正0度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getUnitTo0RotateAnimation(TabBasePageView pageView) {
		if (null == unitTo0RotateYAnimation) {
			unitTo0RotateYAnimation = createRotateYAnimation(UNIT_ANGLE, 0f, false);
		}

		AnimatorSet set = new AnimatorSet();
		Animator alphaAnimator = ObjectAnimator.ofFloat(pageView, "alpha", SIDE_ALPHA, 1f).setDuration(duration);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 0.93f, 1f).setDuration(duration);
		set.playTogether(alphaAnimator, scaleAnimator);
		pageView.setPageViewChangeAnimator(set, unitTo0RotateYAnimation);
	}

	/**
	 * 创建或返回一个从0度偏转到正UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get0ToUnitImmediatelyAnimation(TabBasePageView pageView) {
		if (null == _0ToUnitImmediatelyRotateYAnimation) {
			_0ToUnitImmediatelyRotateYAnimation = createRotateYAnimation(0f, UNIT_ANGLE, true);
		}
		AnimatorSet set=new AnimatorSet();
		Animator alphaAnimator = ObjectAnimator.ofFloat(pageView, "alpha", 1f, SIDE_ALPHA).setDuration(0);
		Animator scaleAnimator=ObjectAnimator.ofFloat(pageView, "scaleY", 1f, 0.93f).setDuration(0);
		set.playTogether(alphaAnimator,scaleAnimator);
		pageView.setPageViewChangeAnimator(set, _0ToUnitImmediatelyRotateYAnimation);
	}

	/**
	 * 创建或返回一个从0度偏转到正2*UNIT_ANGLE度的动画，时长0毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public MatrixRotateYAnimation getUnit2RotateYAnimation() {
		if (null == unit2RotateYAnimation) {
			unit2RotateYAnimation = createRotateYAnimation(0f, DOUBLE_UNIT_ANGLE, false);
		}
		return unit2RotateYAnimation;
	}

	/**
	 * 创建或返回一个从0度偏转到正2*UNIT_ANGLE度的动画，时长0毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get0ToUnit2ImmediatelyRotateYAnimation(TabBasePageView pageView) {
		if (null == _0ToUnit2ImmediatelyRotateYAnimation) {
			_0ToUnit2ImmediatelyRotateYAnimation = createRotateYAnimation(0f, DOUBLE_UNIT_ANGLE, true);
		}

		pageView.setAlpha(0f);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 1f, 0.93f).setDuration(0);
		pageView.setPageViewChangeAnimator(scaleAnimator, _0ToUnit2ImmediatelyRotateYAnimation);
	}

	/**
	 * 创建或返回一个从0度偏转到-UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public MatrixRotateYAnimation getNegUnitRotateAnimation() {
		if (null == negUnitRotateYAnimation) {
			negUnitRotateYAnimation = createRotateYAnimation(0f, NEGATIVE_UNIT_ANGLE, false);
		}
		return negUnitRotateYAnimation;
	}

	/**
	 * 创建或返回一个从0度偏转到-UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getNegUnitTo0RotateAnimation(TabBasePageView pageView) {
		if (null == negUnitTo0RotateYAnimation) {
			negUnitTo0RotateYAnimation = createRotateYAnimation(NEGATIVE_UNIT_ANGLE, 0f, false);
		}

		AnimatorSet set = new AnimatorSet();
		Animator alphaAnimator = ObjectAnimator.ofFloat(pageView, "alpha", SIDE_ALPHA, 1f).setDuration(duration);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 0.93f, 1f).setDuration(duration);
		set.playTogether(alphaAnimator, scaleAnimator);
		pageView.setPageViewChangeAnimator(set, negUnitTo0RotateYAnimation);
	}

	/**
	 * 创建或返回一个从0度偏转到2*-UNIT_ANGLE度的动画，时长0毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get0ToNegUnitImmediatelyRotateAnimation(TabBasePageView pageView) {
		if (null == _0ToNegUnitImmediatelyRotateYAnimation) {
			_0ToNegUnitImmediatelyRotateYAnimation = createRotateYAnimation(0f, NEGATIVE_UNIT_ANGLE, true);
		}

		AnimatorSet set = new AnimatorSet();
		Animator alphaAnimator = ObjectAnimator.ofFloat(pageView, "alpha", 1f, SIDE_ALPHA).setDuration(0);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 1f, 0.93f).setDuration(0);
		set.playTogether(alphaAnimator, scaleAnimator);

		pageView.setPageViewChangeAnimator(set, _0ToNegUnitImmediatelyRotateYAnimation);
	}

	/**
	 * 创建或返回一个从0度偏转到-UNIT_ANGLE度的动画，时长0毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get0ToNegUnit2ImmediatelyRotateAnimation(TabBasePageView pageView) {
		if (null == _0ToNegUnit2ImmediatelyRotateYAnimation) {
			_0ToNegUnit2ImmediatelyRotateYAnimation = createRotateYAnimation(0f, DOUBLE_NEGATIVE_UNIT_ANGLE, true);
		}

		pageView.setAlpha(0f);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 1f, 0.93f).setDuration(0);
		pageView.setPageViewChangeAnimator(scaleAnimator, _0ToNegUnit2ImmediatelyRotateYAnimation);
	}

	/**
	 * 创建或返回一个从0度偏转到-UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get0ToNegUnitRotateAnimation(TabBasePageView pageView) {
		if (null == _0ToNegUnitRotateYAnimation) {
			_0ToNegUnitRotateYAnimation = createRotateYAnimation(0f, NEGATIVE_UNIT_ANGLE, false);
		}
		_0ToNegUnitRotateYAnimation.setAnimationListener(null);

		AnimatorSet set = new AnimatorSet();
		Animator alphaAnimator = ObjectAnimator.ofFloat(pageView, "alpha", 1f, SIDE_ALPHA).setDuration(duration);
		Animator scaleAnimator = ObjectAnimator.ofFloat(pageView, "scaleY", 1f, 0.93f).setDuration(duration);
		set.playTogether(alphaAnimator, scaleAnimator);
		pageView.setPageViewChangeAnimator(set, _0ToNegUnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从-UNIT_ANGLE度偏转到-2*UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getNegUnitToNeg2UnitRotateYAnimation(TabBasePageView pageView) {
		if (null == negUnitToNeg2UnitRotateYAnimation) {
			negUnitToNeg2UnitRotateYAnimation = createRotateYAnimation(NEGATIVE_UNIT_ANGLE, DOUBLE_NEGATIVE_UNIT_ANGLE, false);
		}
		negUnitToNeg2UnitRotateYAnimation.setAnimationListener(null);
		Animator animator = ObjectAnimator.ofFloat(pageView, "alpha", SIDE_ALPHA, 0f).setDuration(duration / 2);
		pageView.setPageViewChangeAnimator(animator, negUnitToNeg2UnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从-UNIT_ANGLE度偏转到-2*UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getNegUnitToNeg2UnitRotateYAnimation(TabBasePageView pageView, AnimationListener listener) {
		if (null == negUnitToNeg2UnitRotateYAnimation) {
			negUnitToNeg2UnitRotateYAnimation = createRotateYAnimation(NEGATIVE_UNIT_ANGLE, DOUBLE_NEGATIVE_UNIT_ANGLE, false);
		}
		negUnitToNeg2UnitRotateYAnimation.setAnimationListener(listener);
		Animator animator = ObjectAnimator.ofFloat(pageView, "alpha", SIDE_ALPHA, 0f).setDuration(duration / 2);
		pageView.setPageViewChangeAnimator(animator, negUnitToNeg2UnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从-2*UNIT_ANGLE度偏转到-UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getNeg2UnitToNegUnitRotateYAnimation(TabBasePageView pageView) {
		if (null == neg2UnitToUnitRotateYAnimation) {
			neg2UnitToUnitRotateYAnimation = createRotateYAnimation(DOUBLE_NEGATIVE_UNIT_ANGLE, NEGATIVE_UNIT_ANGLE, false);
		}

		Animator animator = ObjectAnimator.ofFloat(pageView, "alpha", 0.0f, SIDE_ALPHA).setDuration(duration);
		pageView.setPageViewChangeAnimator(animator, neg2UnitToUnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从2*UNIT_ANGLE度偏转UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get2UnitToUnitRotateYAnimation(TabBasePageView pageView) {
		if (null == _2UnitToUnitRotateYAnimation) {
			_2UnitToUnitRotateYAnimation = createRotateYAnimation(DOUBLE_UNIT_ANGLE, UNIT_ANGLE, false);
		}

		Animator animator = ObjectAnimator.ofFloat(pageView, "alpha", 0f, SIDE_ALPHA).setDuration(duration);
		pageView.setPageViewChangeAnimator(animator, _2UnitToUnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从2*UNIT_ANGLE度偏转2*-UNIT_ANGLE度的动画，时长0毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void get2UnitToNeg2UnitRotateYAnimation(TabBasePageView pageView) {
		if (null == _2UnitToNeg2UnitRotateYAnimation) {
			_2UnitToNeg2UnitRotateYAnimation = createRotateYAnimation(DOUBLE_UNIT_ANGLE, DOUBLE_NEGATIVE_UNIT_ANGLE, true);
		}
		pageView.setAlpha(0f);
		pageView.setPageViewChangeAnimator(null, _2UnitToNeg2UnitRotateYAnimation);
	}

	/**
	 * 创建或返回一个从-2*UNIT_ANGLE度偏转2*UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getNeg2UnitTo2UnitRotateYAnimation(TabBasePageView pageView) {
		if (null == neg2UnitTo2UnitRotateYAnimation) {
			neg2UnitTo2UnitRotateYAnimation = createRotateYAnimation(DOUBLE_NEGATIVE_UNIT_ANGLE, DOUBLE_UNIT_ANGLE, true);
		}

		pageView.setAlpha(0f);
		pageView.setPageViewChangeAnimator(null, neg2UnitTo2UnitRotateYAnimation);

	}

	/**
	 * 创建或返回一个从UNIT_ANGLE度偏转2*UNIT_ANGLE度的动画，时长duration毫秒
	 * 
	 * @Title: RotateYAnimationFactory
	 * @author:陈丽晓
	 * @Description: TODO
	 * @return
	 */
	public void getUnitTo2UnitRotateYAnimation(TabBasePageView pageView) {
		if (null == unitTo2UnitRotateYAnimation) {
			unitTo2UnitRotateYAnimation = createRotateYAnimation(UNIT_ANGLE, DOUBLE_UNIT_ANGLE, false);
		}

		Animator animator = ObjectAnimator.ofFloat(pageView, "alpha", SIDE_ALPHA, 0f).setDuration(duration / 2);
		pageView.setPageViewChangeAnimator(animator, unitTo2UnitRotateYAnimation);
	}

	/**
	 * 根据初始角度和目标角度创建一个偏转动画
	 * 
	 * @Title: View3DPager
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param fromDegree
	 *            初始角度
	 * @param toDegree
	 *            目标角度值
	 * @param isImmediately
	 *            是否即时执行，true: 动画时间为0，否则动画时间为600ms
	 * @return
	 */
	protected MatrixRotateYAnimation createRotateYAnimation(float fromDegree, float toDegree, boolean isImmediately) {
		MatrixRotateYAnimation rotateYAnimation = new MatrixRotateYAnimation(fromDegree, toDegree);
		rotateYAnimation.setFillAfter(true);
		rotateYAnimation.setDuration(isImmediately ? 0 : duration);
		return rotateYAnimation;
	}

}
