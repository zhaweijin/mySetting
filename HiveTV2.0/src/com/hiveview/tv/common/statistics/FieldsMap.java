/**
 * @Title FieldsMap.java
 * @Package com.hiveview.tv.common.statistics
 * @author haozening
 * @date 2014年6月26日 下午2:20:05
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.statistics;

import java.util.HashMap;

/**
 * 初始化FieldsMap时，传入String数组，长度2,第一个元素是Entity属性，第二个元素是统计数据对应的url参数名
 * @ClassName FieldsMap
 * @Description 
 * @author haozening
 * @date 2014年6月26日 下午2:20:05
 * 
 */
public class FieldsMap extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	public FieldsMap(String[]... map) {
		for (int i = 0; i < map.length; i++) {
			put(map[i][1], map[i][0]);
		}
	}
}