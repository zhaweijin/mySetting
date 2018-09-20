/**
 * @Title SimpleOnClickListener.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月25日 下午4:30:48
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 发送统计数据，DataHolder为空不发送统计数据
 * 
 * @ClassName SimpleOnClickListener
 * @Description
 * @author haozening
 * @date 2014年6月25日 下午4:30:48
 * 
 */
public abstract class SimpleOnClickListener implements OnClickListener {

	public void postOnClick(DataHolder holder) {
		KeyEventHandler.post(holder);
	}

	@Override
	public void onClick(View v) {
		DataHolder holder = doOnClick(v);
		postOnClick(holder);
	}

	public abstract DataHolder doOnClick(View v);

	public static class DataHolder {
		public static final DataHolder NULL = new DataHolder();
		public String senceName;
		public Object entity;
		public DataType dataType;
		public ItemType srcType;
		public String beginTime;
		public String endTime;
		public String timeLength;
		public String stayTimeLength;
		public String source;
		// start:添加cp字段,author:huzuwei
		public String cp;
		// end
		// start author:zhangpengzhan 距离上次开机的时间
		public String intervalDay;
		// end
		public String noAction;
		public String positionId;
		public ButtonInfo buttonInfo = ButtonInfo.NULL;
		public MovieDemandQueryInfo movieListInfo = MovieDemandQueryInfo.NULL;
		/**
		 * actionType默认为0000
		 */
		public String viewPosition = "XXXX";

		/**
		 * 默认为00
		 */
		// start 修改tabNo默认值为TAB ,author:huzuwei
		// public Tab tabNo = Tab.DEFAULT;
		public Tab tabNo = Tab.TAB;
		// end
		public Context context;

		public static class Builder {
			public DataHolder holder = new DataHolder();

			public Builder(Context context) {
				holder.context = context;
			}

			public Builder setSenceName(String senceName) {
				holder.senceName = senceName;
				return this;
			}

			public Builder setEntity(Object entity) {
				holder.entity = entity;
				return this;
			}

			public Builder setDataType(DataType dataType) {
				holder.dataType = dataType;
				return this;
			}

			public Builder setViewPosition(String viewPosition) {
				holder.viewPosition = viewPosition;
				return this;
			}

			public Builder setTabNo(Tab tabNo) {
				holder.tabNo = tabNo;
				return this;
			}

			public Builder setSrcType(ItemType type) {
				holder.srcType = type;
				return this;
			}

			public Builder setBeginTime(String beginTime) {
				holder.beginTime = beginTime;
				return this;
			}

			public Builder setEndTime(String endTime) {
				holder.endTime = endTime;
				return this;
			}

			public Builder setTimeLength(String timeLength) {
				holder.timeLength = timeLength;
				return this;
			}

			public Builder setStayTimeLength(String stayTimeLength) {
				holder.stayTimeLength = stayTimeLength;
				return this;
			}

			public Builder setSource(String source) {
				holder.source = source;
				return this;
			}

			// start:添加cp方法,author:huzuwei
			public Builder setCp(String cp) {
				holder.cp = cp;
				return this;
			}

			// end
			// start : author zhangpengzhan
			public Builder setIntervalDay(String intervalDay) {
				holder.intervalDay = intervalDay;
				return this;
			}

			public Builder setNoAction(String noAction) {
				holder.noAction = noAction;
				return this;
			}

			public Builder setPositionId(String positionId) {
				holder.positionId = positionId;
				return this;
			}

			public Builder setButton(String buttonId, String buttonName) {
				holder.buttonInfo.buttonId = buttonId;
				holder.buttonInfo.buttonName = buttonName;
				holder.entity = holder.buttonInfo;
				return this;
			}

			public Builder setMovieDemandQueryInfo(String firstClassName, String firstClassId) {
				holder.movieListInfo.firstClassId = firstClassId;
				holder.movieListInfo.firstClassName = firstClassName;
				holder.entity = holder.movieListInfo;
				return this;
			}

			public DataHolder build() {
				return holder;
			}

		}
	}

}
