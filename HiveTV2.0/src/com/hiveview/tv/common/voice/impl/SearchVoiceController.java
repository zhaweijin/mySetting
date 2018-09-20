package com.hiveview.tv.common.voice.impl;

import android.content.Context;
import android.content.Intent;

import com.hiveview.tv.common.voice.IVoiceController;
import com.hiveview.tv.common.voice.search.IVoiceSearch;

/**
 * 语音搜索接口
 * @author haozening
 *
 */
public class SearchVoiceController implements IVoiceController {
	
	private IVoiceSearch voiceSearch;
	
	public SearchVoiceController(IVoiceSearch voiceSearch) {
		super();
		this.voiceSearch = voiceSearch;
	}
	
	@Override
	public void control(Context context, Intent intent) {
		voiceSearch.search(intent);
	}
	

}
