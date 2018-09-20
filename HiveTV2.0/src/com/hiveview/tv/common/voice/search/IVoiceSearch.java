package com.hiveview.tv.common.voice.search;

import android.content.Intent;


public interface IVoiceSearch {

	String PREVIOUS = "PREV";
	String NEXT = "NEXT";
	String INDEX = "INDEX";
	
	/**
	 * 搜索
	 */
	public void search(Intent intent);
}
