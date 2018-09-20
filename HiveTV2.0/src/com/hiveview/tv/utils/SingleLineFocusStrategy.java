package com.hiveview.tv.utils;

import java.util.List;

import android.R.integer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.strategy.AbstractFocusStrategy;

/**
 * 
 * @ClassName: ThreeLineFocusStrategy
 * @Description: TODO
 * @author: 周一川
 * @date 2014-7-21 下午5:53:26
 *
 */
public class SingleLineFocusStrategy extends AbstractFocusStrategy {

	private static final String TAG = "ThreeLineFocusStrategy";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.strategy.AbstractFocusStrategy#defineStrategy
	 * (android.view.KeyEvent, com.hiveview.box.framework.entity.HiveBaseEntity,
	 * int, int)
	 */
	@Override
	public void defineStrategy(KeyEvent event, HiveBaseEntity entity, int everyPageSize, int everyPageLine) {
		// 焦点向左右移动翻页时 焦点的位置
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
			getToRightLines(everyPageSize, everyPageLine, entity.getPositionInItemView());
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
			getToLeftLines(everyPageSize, everyPageLine, entity.getPositionInItemView());
		}
	}

	/**
	 * 
	 * @Title: ThreeLineFocusStrategy
	 * @author:周一川
	 * @Description: TODO
	 * @param everyPageSize
	 * @param everyPageLine
	 * @param positionInItemView
	 */
	private void getToRightLines(int everyPageSize, int everyPageLine, int positionInItemView) {
		// 最右边一列的 最上边一条 的位置
//		int positionRightFirst = everyPageSize - everyPageLine;
		int eveyLineSize = everyPageSize / everyPageLine;
		if (positionInItemView == eveyLineSize - 1) {
			System.out.println("当前的位置==" + positionInItemView + "一页多少条 1==" + everyPageSize + "一页多少行==" + everyPageLine);
			// 当前数据为最后一列的第1条数据时 下一页焦点位置在的第一个位置
			mFocusPosition = 0;
			return;
		} else if (positionInItemView == (2 * eveyLineSize - 1)) {
			System.out.println("当前的位置==" + positionInItemView + "一页多少条 2==" + everyPageSize + "一页多少行==" + everyPageLine);
			// 当前数据为最后一列的第2条数据时 下一页焦点位置在的第2个位置
			mFocusPosition = positionInItemView - everyPageLine;
			Log.i(TAG, "下一个焦点" + mFocusPosition);
		} else if (positionInItemView == (3 * eveyLineSize - 1)) {
			System.out.println("当前的位置==" + positionInItemView + "一页多少条 3==" + everyPageSize + "一页多少行==" + everyPageLine);
			// 当前数据为最后一列的第3条数据时 下一页焦点位置在的第3个位置
			mFocusPosition = positionInItemView - everyPageLine;
			Log.i(TAG, "下一个焦点" + mFocusPosition);
		}
	}

	/**
	 * 
	 * @Title: ThreeLineFocusStrategy
	 * @author:周一川
	 * @Description: TODO
	 * @param everyPageSize
	 * @param everyPageLine
	 * @param positionInItemView
	 */                                                                                                              
	private void getToLeftLines(int everyPageSize, int everyPageLine, int positionInItemView) {
		// 最右边一列的 最上边一条 的位置
		int positionRightFirst = everyPageSize - everyPageLine;
		if (positionInItemView == 0) {
			System.out.println("当前的位置==" + positionInItemView + "一页多少条 1==" + everyPageSize + "一页多少行==" + everyPageLine);
			// 当前位置为第1个元素时 上一页焦点为最后一列的第一个元素
			mFocusPosition = positionRightFirst - 6;
		} else if (positionInItemView == 4) {
			System.out.println("当前的位置==" + positionInItemView + "一页多少条 2==" + everyPageSize + "一页多少行==" + everyPageLine);
			// 当前位置为第2个元素时 上一页焦点为最后一列的第2个元素
			mFocusPosition = positionRightFirst - 2;
		} else if (positionInItemView == 8) {
			System.out.println("当前的位置==" + positionInItemView + "一页多少条 3==" + everyPageSize + "一页多少行==" + everyPageLine);
			// 当前位置为第3个元素时 上一页焦点为最后一列的第3个元素
			mFocusPosition = positionRightFirst + 2;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hiveview.box.framework.strategy.AbstractFocusStrategy#setFocusByStrategy
	 * (java.util.List, int, int, int, int, int, int)
	 */
	@Override
	public void setFocusByStrategy(List<View> mItemViewList, int positionInItemView, int everyPageSize, int everyPageLine, int localDataSize,
			int localPageTotal, int currentPageIndex) {
		// // 每行的个数
		int everyLines = everyPageSize / everyPageLine;
		// 判断最后一页时，例如某PageView是2行，第二行没有数据，应该按照getDefaultFocusPosition()返回位置，设置焦点
		if (currentPageIndex + 1 == localPageTotal && localDataSize <= ((currentPageIndex + 1) * everyPageSize - 4 * everyLines)) {
			mItemViewList.get(getDefaultFocusPosition()).requestFocus();
			return;
		}
		// 当前元素的位置位属于边界值时执行
		if ((positionInItemView == everyLines || positionInItemView == everyLines - 1 || positionInItemView == 0
				|| positionInItemView == everyPageSize - 1 || positionInItemView == 2 * everyLines - 1 || positionInItemView == 2 * everyLines
				|| positionInItemView == 3 * everyLines - 1 || positionInItemView == 3 * everyLines)) {
			mItemViewList.get(positionInItemView).requestFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.box.framework.strategy.AbstractFocusStrategy#
	 * getDefaultFocusPosition()
	 */
	@Override
	public int getDefaultFocusPosition() {
		return 0;
	}

}
