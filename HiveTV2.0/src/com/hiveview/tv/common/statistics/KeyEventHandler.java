/**
 * @Title KeyEventHandler.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月27日 下午2:50:06
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

import com.hiveview.tv.common.statistics.SimpleOnClickListener.DataHolder;

/**
 * 发送数据方法，响应相对的Event后发送数据，DataHolder为空不发送统计数据
 * @ClassName KeyEventHandler
 * @Description 
 * @author haozening
 * @date 2014年6月27日 下午2:50:06
 * 
 */
public class KeyEventHandler {

	public static void post(DataHolder holder) {
		new StatisticsSender().sender(holder);
	}
	
}
