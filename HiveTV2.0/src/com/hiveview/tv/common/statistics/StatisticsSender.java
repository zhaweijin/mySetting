/**
 * @Title StatisticsSender.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月26日 下午2:33:30
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

import java.lang.reflect.Field;
import java.util.Map;

import android.util.Log;

import com.hiveview.tv.activity.HomeActivity;
import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;

/**
 * 统计数据发送类，格式化数据并且按规定的数据格式发送数据
 * 
 * @ClassName StatisticsSender
 * @Description
 * @author haozening
 * @date 2014年6月26日 下午2:33:30
 * 
 */
public class StatisticsSender {

	private static final String TAG = "StatisticsSender";

	public void sender(DataHolder holder) {
		if (null != holder) {
			// Log.d(TAG, "DataType : " + holder.dataType.toString());
			formatData(holder, holder.dataType);
		}
	}

	private void formatData(DataHolder holder, DataType dataType) {
		StringBuilder stringBuilder = new StringBuilder();
		if (null != dataType) {
			FieldsMap fieldsMap = dataType.getFields();
			Object object = null;
			try {
				Class<?> clazz = Class.forName(holder.dataType.getClazz());
				if (null == holder.entity) {
					object = clazz.newInstance();
				} else {
					object = holder.entity;
				}
				for (Map.Entry<String, String> entry : fieldsMap.entrySet()) {
					String fieldName = entry.getValue();
					Field field = clazz.getDeclaredField(fieldName);
					field.setAccessible(true);
					Object value = field.get(object);
					if (null == value) {
						value = "";
					}
					Log.d(TAG, fieldName + ": " + value.toString());
					stringBuilder.append(entry.getKey()).append("=").append(value.toString()).append("&");
				}
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		combineDataHolderProperty(stringBuilder, holder);
		if (stringBuilder.length() > 0) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		send(holder, stringBuilder.toString());
	}

	/**
	 * 合并DataHolder其他属性
	 * 
	 * @Title combineDataHolderProperty
	 * @author haozening
	 * @Description
	 * @param builder
	 * @param holder
	 */
	private void combineDataHolderProperty(StringBuilder builder, DataHolder holder) {
		if (isNull(holder.beginTime)) {
			append(builder, "beginTime", holder.beginTime);
		}
		if (isNull(holder.endTime)) {
			append(builder, "endTime", holder.endTime);
		}
		if (isNull(holder.noAction)) {
			append(builder, "noAction", holder.noAction);
		}
		if (isNull(holder.positionId)) {
			append(builder, "positionId", holder.positionId);
		}
		if (isNull(holder.source)) {
			append(builder, "source", holder.source);
		}
		//start:合并属性cp,author:huzuwei
		if (isNull(holder.cp)) {
			append(builder, "cp", holder.cp);
		}
		//end
		//start author zhangpengzhan
		if(isNull(holder.intervalDay)){
			append(builder, "intervalDay", holder.intervalDay);
		}
		//end
		if (isNull(holder.stayTimeLength)) {
			append(builder, "stayTimeLength", holder.stayTimeLength);
		}
		if (isNull(holder.timeLength)) {
			append(builder, "timeLength", holder.timeLength);
		}
		if (null != holder.srcType) {
			if (isNull(holder.srcType.toString())) {
				append(builder, "srcType", holder.srcType.toString());
			}
		}
//		if (isNull(holder.intervalDay)) {
//			append(builder, "intervalDay", holder.intervalDay);
//		}
	}

	/**
	 * 如果设置了数据，就返回true
	 * 
	 * @Title isNull
	 * @author haozening
	 * @Description
	 * @param str
	 * @return
	 */
	private boolean isNull(String str) {
		return null != str && !str.equals("");
	}

	/**
	 * 合并url
	 * 
	 * @Title append
	 * @author haozening
	 * @Description
	 * @param builder
	 * @param urlParam
	 * @param value
	 */
	private void append(StringBuilder builder, String urlParam, String value) {
		builder.append(urlParam).append("=").append(value).append("&");
	}

	private void send(DataHolder holder, String sendInfo) {
		Log.d(TAG, "tabNo : " + holder.tabNo.toString() + "|  viewPosition : " + holder.viewPosition + "|   sendInfo : " + sendInfo);
		Statistics mStatistics =new Statistics(holder.context, holder.tabNo.toString(), holder.viewPosition, sendInfo);
		mStatistics.sendPre(HomeActivity.statisticsHandler);
	}
}
