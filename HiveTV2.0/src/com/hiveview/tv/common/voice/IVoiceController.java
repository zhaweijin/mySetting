package com.hiveview.tv.common.voice;

import android.content.Context;
import android.content.Intent;

/**
 * 接受控制的目标
 * @author haozening
 *
 */
public interface IVoiceController {
	String COMMAND = "_command";
	String ACTION = "_action";
	public void control(Context context, Intent intent);
}
