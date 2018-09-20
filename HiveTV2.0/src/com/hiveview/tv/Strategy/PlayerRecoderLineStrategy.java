package com.hiveview.tv.Strategy;

import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;
import com.hiveview.box.framework.strategy.AbstractFocusStrategy;

import android.view.KeyEvent;
import android.view.View;

/**
 * 播放记录的焦点策略
 * 
 * @ClassName: PlayerRecoderLineStrategy
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年7月10日 下午2:53:44
 * 
 */
public class PlayerRecoderLineStrategy extends AbstractFocusStrategy {

	@Override
	public void defineStrategy(KeyEvent event, HiveBaseEntity entity, int everyPageSize, int everyPageLine) {// 右下角
		if (entity.getPositionInItemView() == (everyPageSize - 1) && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
			mFocusPosition = everyPageSize / everyPageLine;
		} else if (entity.getPositionInItemView() == 0 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {// 左上角
			mFocusPosition = (everyPageSize / everyPageLine - 1);
		} else if (entity.getPositionInItemView() == (everyPageSize / everyPageLine - 1) && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {// 右上角
			mFocusPosition = 0;
		} else if (entity.getPositionInItemView() == (everyPageSize / everyPageLine) && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {// 左下角
			mFocusPosition = everyPageSize - 1;
		}
	}

	@Override
	public int getDefaultFocusPosition() {
		return 0;
	}

	@Override
	public void setFocusByStrategy(List<View> mItemViewList, int positionInItemView, int everyPageSize, int everyPageLine, int localDataSize,
			int localPageTotal, int currentPageIndex) {
		// 判断最后一页时，例如某PageView是2行，第二行没有数据，应该按照getDefaultFocusPosition()返回位置，设置焦点
		if (currentPageIndex + 1 == localPageTotal && localDataSize <= ((currentPageIndex) * everyPageSize + 1)) {
			mItemViewList.get(getDefaultFocusPosition()).requestFocus();
			return;
		}

		if ((positionInItemView == everyPageSize / everyPageLine || positionInItemView == everyPageSize / everyPageLine - 1
				|| positionInItemView == 0 || positionInItemView == everyPageSize - 1)) {
			//如果不显示的话就让第一个显示
			if (mItemViewList.get(positionInItemView).isShown()) {
				mItemViewList.get(positionInItemView).requestFocus();
			} else {
				mItemViewList.get(positionInItemView - everyPageSize / 2).requestFocus();
			}

		}
	}

}
