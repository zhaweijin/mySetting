/**
 * @Title XunfeiComplexSearch.java
 * @Package com.hiveview.tv.common.voice.search.impl
 * @author haozening
 * @date 2014年8月7日 下午8:14:15
 * @Description 
 * @version V1.0
 */
package com.hiveview.tv.common.voice.search.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hiveview.tv.common.voice.search.IVoiceSearch;

/**
 * @ClassName XunfeiComplexSearch
 * @Description 
 * @author haozening
 * @date 2014年8月7日 下午8:14:15
 * 
 */
public class XunfeiComplexSearch implements IVoiceSearch {

	private static final String DATA_URL = "data_url";
	private Context context;
	private Class<? extends Activity> clazz;
	
	public XunfeiComplexSearch(Context context, Class<? extends Activity> clazz) {
		this.context = context;
		this.clazz = clazz;
	}
	@Override
	public void search(Intent intent) {
		Intent listIntent = new Intent(context, clazz);
		listIntent.putExtra(DATA_URL, intent.getStringExtra("url"));
		listIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(listIntent);
	}

}
