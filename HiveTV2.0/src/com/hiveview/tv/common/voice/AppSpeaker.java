package com.hiveview.tv.common.voice;


import android.content.Context;

import com.iflytek.xiri.Feedback;



/**
 * 继承科大讯飞语音控制类APP，定义额外功能
 * @author haozening
 *
 */
public class AppSpeaker extends Feedback {

	private int wordsLength;
	
	public AppSpeaker(Context context) {
		super(context);
	}
	
	public void showText(String text, boolean speak) {
		int type = Feedback.SILENCE;
		if (!speak) {
			type = Feedback.DIALOG;
		}
		super.feedback(text, type);
		wordsLength = text.length();
	}
	
	/**
	 * 通过文字长度获取说话时长
	 * @return
	 */
	public int getSpeakTime() {
		return (int) (2300 / 8f * wordsLength) + 500;
	}
	
	public void close() {
		
	}

}
