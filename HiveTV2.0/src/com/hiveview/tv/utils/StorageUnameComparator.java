package com.hiveview.tv.utils;

import java.util.Comparator;

import android.annotation.SuppressLint;

/***
 * 根据u盘的盘符排序
 * @author maliang
 *
 */
public class StorageUnameComparator implements
		Comparator<String> {

	@SuppressLint("NewApi")
	@Override
	public int compare(String lhs, String rhs) {
		if(lhs != null && rhs != null && lhs.compareTo(rhs)>0){
			return 1;
		}else{
			return -1;
		}
	}

}
