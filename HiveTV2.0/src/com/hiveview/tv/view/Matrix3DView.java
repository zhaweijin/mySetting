package com.hiveview.tv.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.hiveview.tv.common.AppConstant;
import com.hiveview.tv.common.anim.MatrixRotateYAnimation;
import com.hiveview.tv.service.entity.AppFocusEntity;
import com.hiveview.tv.service.entity.FilmEntity;
import com.hiveview.tv.service.entity.ProgramByCodeEntity;
import com.hiveview.tv.service.entity.RecommendEntity;
import com.hiveview.tv.view.pager3d.TabBasePageView;

public class Matrix3DView extends RelativeLayout {

	private AnimationSet animationSet0 = new AnimationSet(true);
	private AnimationSet animationSet1 = new AnimationSet(true);
	private AnimationSet animationSet2 = new AnimationSet(true);
	private AnimationSet animationSet3 = new AnimationSet(true);
	private AnimationSet animationSet4 = new AnimationSet(true);
	private AnimationSet animationSet5 = new AnimationSet(true);
	private AnimationSet animationSet6 = new AnimationSet(true);
	private AnimationSet animationSet7 = new AnimationSet(true);

	private MatrixRotateYAnimation rotateAnimation0 = new MatrixRotateYAnimation(-36.0f, 0.0f);
	private MatrixRotateYAnimation rotateAnimation1 = new MatrixRotateYAnimation(0.0f, 36.0f);
	private MatrixRotateYAnimation rotateAnimation2 = new MatrixRotateYAnimation(36.0f, 0.0f);
	private MatrixRotateYAnimation rotateAnimation3 = new MatrixRotateYAnimation(0.0f, -36.0f);
	private MatrixRotateYAnimation rotateAnimation4 = new MatrixRotateYAnimation(72.0f, 36.0f);
	private MatrixRotateYAnimation rotateAnimation5 = new MatrixRotateYAnimation(-72.0f, -36.0f);
	private MatrixRotateYAnimation rotateAnimation6 = new MatrixRotateYAnimation(36.0f, 72.0f);
	private MatrixRotateYAnimation rotateAnimation7 = new MatrixRotateYAnimation(-36.0f, -72.0f);

	private AlphaAnimation alphaAnimation0 = new AlphaAnimation(0.0f, 0.3f);
	private AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.3f, 1.0f);
	private AlphaAnimation alphaAnimation2 = new AlphaAnimation(1.0f, 0.3f);
	private AlphaAnimation alphaAnimation3 = new AlphaAnimation(0.3f, 0.0f);

	private ScaleAnimation scaleAnimation0 = new ScaleAnimation(1.0f, 1.0f, 0.93f, 1.0f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
			ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
	private ScaleAnimation scaleAnimation1 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0.93f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
			ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
	private ScaleAnimation scaleAnimation3 = new ScaleAnimation(1.0f, 1.0f, 0.85f, 0.93f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
			ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
	private ScaleAnimation scaleAnimation4 = new ScaleAnimation(1.0f, 1.0f, 0.93f, 0.85f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
			ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

	private int unit = 60;

	private TranslateAnimation translateAnimation0 = new TranslateAnimation(unit, 0, 0, 0);
	private TranslateAnimation translateAnimation1 = new TranslateAnimation(0, -unit, 0, 0);
	private TranslateAnimation translateAnimation2 = new TranslateAnimation(-unit, 0, 0, 0);
	private TranslateAnimation translateAnimation3 = new TranslateAnimation(0, unit, 0, 0);
	private TranslateAnimation translateAnimation4 = new TranslateAnimation(unit * 2, unit, 0, 0);
	private TranslateAnimation translateAnimation5 = new TranslateAnimation(unit, unit * 2, 0, 0);
	private TranslateAnimation translateAnimation6 = new TranslateAnimation(-unit * 2, -unit, 0, 0);
	private TranslateAnimation translateAnimation7 = new TranslateAnimation(-unit, -unit * 2, 0, 0);

	private int duration = 600;

	private View[] views;

	private int currentIndex = 2;

	public Matrix3DView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Matrix3DView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Matrix3DView(Context context) {
		super(context);
		init();
	}

	/**
	 * 初始化推荐，电视，影院，游戏，应用的页面
	 */
	public void initMatrix() {
		int count = getChildCount();
		views = new View[count];
		for (int i = 0; i < count; i++) {
			views[i] = getChildAt(i);
			views[i].setVisibility(View.INVISIBLE);
		}

		views[2].setVisibility(View.VISIBLE);

	}

	private void init() {

		animationSet0.setInterpolator(new DecelerateInterpolator());
		animationSet0.setDuration(duration);
		animationSet0.setFillAfter(true);

		animationSet1.setInterpolator(new DecelerateInterpolator());
		animationSet1.setDuration(duration);
		animationSet1.setFillAfter(true);

		animationSet2.setInterpolator(new DecelerateInterpolator());
		animationSet2.setDuration(duration);
		animationSet2.setFillAfter(true);

		animationSet3.setInterpolator(new DecelerateInterpolator());
		animationSet3.setDuration(duration);
		animationSet3.setFillAfter(true);

		animationSet4.setInterpolator(new DecelerateInterpolator());
		animationSet4.setDuration(duration);
		animationSet4.setFillAfter(true);

		animationSet5.setInterpolator(new DecelerateInterpolator());
		animationSet5.setDuration(duration);
		animationSet5.setFillAfter(true);

		animationSet6.setInterpolator(new DecelerateInterpolator());
		animationSet6.setDuration(duration);
		animationSet6.setFillAfter(true);

		animationSet7.setInterpolator(new DecelerateInterpolator());
		animationSet7.setDuration(duration);
		animationSet7.setFillAfter(true);

		animationSet0.addAnimation(rotateAnimation7);
		animationSet0.addAnimation(translateAnimation5);
		animationSet0.addAnimation(alphaAnimation3);
		animationSet0.addAnimation(scaleAnimation4);

		animationSet1.addAnimation(rotateAnimation3);
		animationSet1.addAnimation(translateAnimation3);
		animationSet1.addAnimation(alphaAnimation2);
		animationSet1.addAnimation(scaleAnimation1);

		animationSet2.addAnimation(rotateAnimation2);
		animationSet2.addAnimation(translateAnimation2);
		animationSet2.addAnimation(alphaAnimation1);
		animationSet2.addAnimation(scaleAnimation0);

		animationSet3.addAnimation(rotateAnimation4);
		animationSet3.addAnimation(translateAnimation6);
		animationSet3.addAnimation(alphaAnimation0);
		animationSet3.addAnimation(scaleAnimation3);

		animationSet4.addAnimation(rotateAnimation6);
		animationSet4.addAnimation(translateAnimation7);
		animationSet4.addAnimation(alphaAnimation3);
		animationSet4.addAnimation(scaleAnimation4);

		animationSet5.addAnimation(rotateAnimation1);
		animationSet5.addAnimation(translateAnimation1);
		animationSet5.addAnimation(alphaAnimation2);
		animationSet5.addAnimation(scaleAnimation1);

		animationSet6.addAnimation(rotateAnimation0);
		animationSet6.addAnimation(translateAnimation0);
		animationSet6.addAnimation(alphaAnimation1);
		animationSet6.addAnimation(scaleAnimation0);

		animationSet7.addAnimation(rotateAnimation5);
		animationSet7.addAnimation(translateAnimation4);
		animationSet7.addAnimation(alphaAnimation0);
		animationSet7.addAnimation(scaleAnimation3);

	}

	@SuppressLint("NewApi")
	private void setCurrentItem(int index, int type) {

		views[0].setAlpha(0);
		views[1].setAlpha(0);
		views[2].setAlpha(0);
		views[3].setAlpha(0);
		views[4].setAlpha(0);

		views[0].clearAnimation();
		views[1].clearAnimation();
		views[2].clearAnimation();
		views[3].clearAnimation();
		views[4].clearAnimation();

		switch (index) {
		case 0:

			if (type == 0) {

			} else if (type == 1) {
				views[0].setAlpha(1.0f);
				views[1].setAlpha(1.0f);
				views[2].setAlpha(1.0f);

				views[0].startAnimation(animationSet1);
				views[1].startAnimation(animationSet2);
				views[2].startAnimation(animationSet3);

				views[1].bringToFront();
			}

			break;
		case 1:

			if (type == 0) {

				views[0].setAlpha(1.0f);
				views[1].setAlpha(1.0f);
				views[2].setAlpha(1.0f);

				views[2].startAnimation(animationSet4);
				views[1].startAnimation(animationSet5);
				views[0].startAnimation(animationSet6);

				views[0].bringToFront();

			} else if (type == 1) {

				views[0].setAlpha(1.0f);
				views[1].setAlpha(1.0f);
				views[2].setAlpha(1.0f);
				views[3].setAlpha(1.0f);

				views[0].startAnimation(animationSet0);
				views[1].startAnimation(animationSet1);
				views[2].startAnimation(animationSet2);
				views[3].startAnimation(animationSet3);

				views[2].bringToFront();
			}

			break;
		case 2:

			if (type == 0) {

				views[0].setAlpha(1.0f);
				views[1].setAlpha(1.0f);
				views[2].setAlpha(1.0f);
				views[3].setAlpha(1.0f);

				views[3].startAnimation(animationSet4);
				views[2].startAnimation(animationSet5);
				views[1].startAnimation(animationSet6);
				views[0].startAnimation(animationSet7);

				views[1].bringToFront();

			} else if (type == 1) {

				views[1].setAlpha(1.0f);
				views[2].setAlpha(1.0f);
				views[3].setAlpha(1.0f);
				views[4].setAlpha(1.0f);

				views[1].startAnimation(animationSet0);
				views[2].startAnimation(animationSet1);
				views[3].startAnimation(animationSet2);
				views[4].startAnimation(animationSet3);

				views[3].bringToFront();
			}

			break;
		case 3:

			if (type == 0) {

				views[1].setAlpha(1.0f);
				views[2].setAlpha(1.0f);
				views[3].setAlpha(1.0f);
				views[4].setAlpha(1.0f);

				views[4].startAnimation(animationSet4);
				views[3].startAnimation(animationSet5);
				views[2].startAnimation(animationSet6);
				views[1].startAnimation(animationSet7);

				views[2].bringToFront();

			} else if (type == 1) {

				views[2].setAlpha(1.0f);
				views[3].setAlpha(1.0f);
				views[4].setAlpha(1.0f);

				views[2].startAnimation(animationSet0);
				views[3].startAnimation(animationSet1);
				views[4].startAnimation(animationSet2);

				views[4].bringToFront();
			}

			break;
		case 4:

			if (type == 0) {

				views[2].setAlpha(1.0f);
				views[3].setAlpha(1.0f);
				views[4].setAlpha(1.0f);

				views[4].startAnimation(animationSet5);
				views[3].startAnimation(animationSet6);
				views[2].startAnimation(animationSet7);

				views[3].bringToFront();

			} else if (type == 1) {

			}

			break;

		}

	}

	public void showNext() {
		if (currentIndex == (views.length - 1))
			return;
		matrixListener.onMatrixPageChangeStart(currentIndex);
		setCurrentItem(currentIndex, 1);
		currentIndex++;
		matrixListener.onMatrixPageChangeComplete(currentIndex);
	}

	public void showPrevious() {
		if (currentIndex == 0)
			return;
		matrixListener.onMatrixPageChangeStart(currentIndex);
		setCurrentItem(currentIndex, 0);
		currentIndex--;
		matrixListener.onMatrixPageChangeComplete(currentIndex);
	}

	public void setCurrentItem(int index) {

		matrixListener.onMatrixPageChangeStart(index);

		for (int i = 0; i < views.length; i++) {
			views[i].setAlpha(0);
		}

		MatrixRotateYAnimation currentItemRotateAnimation1 = new MatrixRotateYAnimation(0.0f, 36.0f);
		MatrixRotateYAnimation currentItemRotateAnimation2 = new MatrixRotateYAnimation(0.0f, -36.0f);

		TranslateAnimation currentItemTranslateAnimation1 = new TranslateAnimation(0, -unit, 0, 0);
		TranslateAnimation currentItemTranslateAnimation2 = new TranslateAnimation(0, unit, 0, 0);

		ScaleAnimation currentItemScaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.93f, 1.0f, ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);

		AlphaAnimation currentItemAlphaAnimation = new AlphaAnimation(1.0f, 0.3f);

		AnimationSet currentAnimationSet1 = new AnimationSet(true);
		AnimationSet currentAnimationSet2 = new AnimationSet(true);

		currentAnimationSet1.setDuration(duration);
		currentAnimationSet1.setFillAfter(true);
		currentAnimationSet1.setInterpolator(new DecelerateInterpolator());

		currentAnimationSet2.setDuration(duration);
		currentAnimationSet2.setFillAfter(true);
		currentAnimationSet2.setInterpolator(new DecelerateInterpolator());

		currentAnimationSet1.addAnimation(currentItemRotateAnimation1);
		currentAnimationSet1.addAnimation(currentItemTranslateAnimation1);
		currentAnimationSet1.addAnimation(currentItemScaleAnimation);
		currentAnimationSet1.addAnimation(currentItemAlphaAnimation);

		currentAnimationSet2.addAnimation(currentItemRotateAnimation2);
		currentAnimationSet2.addAnimation(currentItemTranslateAnimation2);
		currentAnimationSet2.addAnimation(currentItemScaleAnimation);
		currentAnimationSet2.addAnimation(currentItemAlphaAnimation);

		views[index].setAlpha(1);
		if (index > 0 && index < views.length) {

			views[index].bringToFront();
			views[index + 1].setAlpha(1);
			views[index - 1].setAlpha(1);
			views[index + 1].startAnimation(currentAnimationSet1);
			views[index - 1].startAnimation(currentAnimationSet2);

		} else if (index == 0) {

			views[index].bringToFront();
			views[index + 1].setAlpha(1);
			views[index + 1].startAnimation(currentAnimationSet1);

		} else if (index == views.length) {

			views[index].bringToFront();
			views[index - 1].setAlpha(1);
			views[index - 1].startAnimation(currentAnimationSet2);

		}

		currentIndex = index;
		matrixListener.onMatrixPageChangeComplete(currentIndex);
	}

	private onMatrixPageChangeListener matrixListener;

	public void setOnMatrixPageChangeListener(onMatrixPageChangeListener matrixListener) {
		this.matrixListener = matrixListener;
	}

	public interface onMatrixPageChangeListener {
		void onMatrixPageChangeStart(int mCurrentIndex);

		void onMatrixPageChangeComplete(int mCurrentIndex);
	}

	public void setMatrixGameData(ArrayList<ArrayList<AppFocusEntity>> gameList) {
		MatrixGameView matrixGameView = (MatrixGameView) views[0];

		matrixGameView.getInstallGameCount();
	}

	public void setMatrixTVRecommendMediaData(ArrayList<FilmEntity> films) {
		/* start by guosongsheng 美国测试使用 */
		TabBasePageView matrixTVView = null;
		if (AppConstant.ISDOMESTIC) {
			matrixTVView = (MatrixTVView) views[1];
		} else {
			matrixTVView = (MatrixTVForeidgnView) views[1];
		}
		/* end by guosongsheng */
	}

	public void setMatrixTVProgramData(ArrayList<ProgramByCodeEntity> liveList) {
		/* start by guosongsheng 美国测试使用 */
		TabBasePageView matrixTVView = null;
		if (AppConstant.ISDOMESTIC) {
			matrixTVView = (MatrixTVView) views[1];
		} else {
			matrixTVView = (MatrixTVForeidgnView) views[1];
		}
		/* end by guosongsheng */

	}

	public void setMatrixRecommendData(ArrayList<ArrayList<RecommendEntity>> recommendList) {
		MatrixRecommendView matrixRecommendView = (MatrixRecommendView) views[2];

	}

	public void setMatrixMovieData(ArrayList<ArrayList<RecommendEntity>> movieList) {
		MatrixMovieView matrixMovieView = (MatrixMovieView) views[3];

	}

	public void setMatrixAppData(ArrayList<ArrayList<AppFocusEntity>> appList) {
		MatrixAppView matrixAppView = (MatrixAppView) views[4];

		matrixAppView.getInstallAppCount();
	}

	public MatrixAppView getMatrixAppView() {
		return (MatrixAppView) views[4];
	}

	public MatrixGameView getMatrixGameView() {
		return (MatrixGameView) views[0];
	}

	/**
	 * 页面完全呈现，显示加载好的Tab页面
	 */
	public void setOtherTabViewVisble() {
		views[0].setVisibility(View.VISIBLE);
		views[1].setVisibility(View.VISIBLE);
		// views[2].setVisibility(View.VISIBLE);
		views[3].setVisibility(View.VISIBLE);
		views[4].setVisibility(View.VISIBLE);
	}

}
