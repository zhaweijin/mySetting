package com.hiveview.tv.common;

import android.content.Context;

public interface GetStorageListener {
	/**
	 * 获取盒子存储空间使用信息的方法
	 * 
	 * @Title: GetStorageListener
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param context
	 * @return Object 数组，包含两个元素 0 是字符串类型，表示当前使用情况的文本表示；1 位置是整形，表示已使用的空间所占的百分百
	 */
	public Object[] getStorageInfo(Context context);
}
