package com.hiveview.tv.utils;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.view.KeyEvent;

public class KeyMappingHashMapUtil {

	/**
	 * 音量+ ,android原生“声音加键” 映射为 KEYCODE_VOLUME_UP
	 */
	public final static int IRCODE_STB_KEY_VOLUME_INC = 0x0000000B;
	/**
	 * 音量-,android原生“声音减键” 映射为 IRCODE_STB_KEY_VOLUME_DEC
	 */
	public final static int IRCODE_STB_KEY_VOLUME_DEC = 0x0000000C;
	/**
	 * 静音,android原生“静音键” 映射为 IRCODE_STB_KEY_MUTE
	 */
	public final static int IRCODE_STB_KEY_MUTE = 0x0000000D;
	/**
	 * 节目+，android原生“节目加键” 映射为 IRCODE_STB_KEY_PROGRAM_INC
	 */
	public final static int IRCODE_STB_KEY_PROGRAM_INC = 0x0000000E;
	/**
	 * 节目-
	 */
	public final static int IRCODE_STB_KEY_PROGRAM_DEC = 0x0000000F;
	/**
	 * 上 ,android原生“向上键” 映射为 IRCODE_STB_KEY_UP 声音加
	 */
	public final static int IRCODE_STB_KEY_UP = 0x00000010;
	/**
	 * 下,android原生“向下键” 映射为 KEYCODE_DPAD_DOWN 声音减
	 */
	public final static int IRCODE_STB_KEY_DOWN = 0x00000011;
	/**
	 * 左,android原生“向左键” 映射为 KEYCODE_DPAD_LEFT 频道减
	 */
	public final static int IRCODE_STB_KEY_LEFT = 0x00000012;
	/**
	 * 右,android原生“向右键” 映射为 KEYCODE_DPAD_RIGHT 频道加
	 */
	public final static int IRCODE_STB_KEY_RIGHT = 0x00000013;
	/**
	 * @Fields IRCODE_STB_KEY_CONFIRM 确认
	 */
	public final static int IRCODE_STB_KEY_CONFIRM = 0x00000015;
	/**
	 * @Fields IRCODE_STB_KEY_BACK 返回键
	 */
	public final static int IRCODE_STB_KEY_BACK = 0x00000014;
	/**
	 * @Fields KEYCODE_PAGE_UP_BLUETOOTH 蓝牙的节目加键值
	 */
	public final static int KEYCODE_PAGE_UP_BLUETOOTH = 166;
	/**
	 * @Fields KEYCODE_PAGE_UP_BLUETOOTH 蓝牙的节目减键值
	 */
	public final static int KEYCODE_PAGE_DOWN_BLUETOOTH = 167;
	/**
	 * 键值映射map
	 */
	public static HashMap<Integer, Integer> keyMappingHashMap = null;

	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, Integer> getInstance() {
		if (null == keyMappingHashMap) {
			keyMappingHashMap = new HashMap<Integer, Integer>();
			initMappingHashMap();
		}
		return keyMappingHashMap;
	}

	private static void initMappingHashMap() {

		// android原生“向上键” 映射为 IRCODE_STB_KEY_UP
		keyMappingHashMap.put(KeyEvent.KEYCODE_DPAD_UP, IRCODE_STB_KEY_UP);
		// android原生“向下键” 映射为 IRCODE_STB_KEY_DOWN
		keyMappingHashMap.put(KeyEvent.KEYCODE_DPAD_DOWN, IRCODE_STB_KEY_DOWN);

		// android原生“向左键” 映射为 IRCODE_STB_KEY_LEFT
		keyMappingHashMap.put(KeyEvent.KEYCODE_DPAD_LEFT, IRCODE_STB_KEY_LEFT);
		// android原生“向右键” 映射为 IRCODE_STB_KEY_RIGHT
		keyMappingHashMap.put(KeyEvent.KEYCODE_DPAD_RIGHT, IRCODE_STB_KEY_RIGHT);

		// android原生“声音加键” 映射为 KEYCODE_VOLUME_UP
		keyMappingHashMap.put(KeyEvent.KEYCODE_VOLUME_UP, IRCODE_STB_KEY_VOLUME_INC);
		// android原生“声音减键” 映射为 IRCODE_STB_KEY_VOLUME_DEC
		keyMappingHashMap.put(KeyEvent.KEYCODE_VOLUME_DOWN, IRCODE_STB_KEY_VOLUME_DEC);
		// android原生“静音键” 映射为 IRCODE_STB_KEY_MUTE
		keyMappingHashMap.put(KeyEvent.KEYCODE_VOLUME_MUTE, IRCODE_STB_KEY_MUTE);
		
		// android原生“节目加键” 映射为 IRCODE_STB_KEY_PROGRAM_INC
		keyMappingHashMap.put(KEYCODE_PAGE_UP_BLUETOOTH, IRCODE_STB_KEY_PROGRAM_INC);
		// android原生“节目减键” 映射为 IRCODE_STB_KEY_PROGRAM_DEC
		keyMappingHashMap.put(KEYCODE_PAGE_DOWN_BLUETOOTH, IRCODE_STB_KEY_PROGRAM_DEC);

		// 蓝牙遥控器“节目加键” 映射为 IRCODE_STB_KEY_PROGRAM_INC
		keyMappingHashMap.put(KeyEvent.KEYCODE_PAGE_UP, IRCODE_STB_KEY_PROGRAM_INC);
		// 蓝牙遥控器“节目减键” 映射为 IRCODE_STB_KEY_PROGRAM_DEC
		keyMappingHashMap.put(KeyEvent.KEYCODE_PAGE_DOWN, IRCODE_STB_KEY_PROGRAM_DEC);

		// “确认键” 映射为 IRCODE_STB_KEY_CONFIRM
		keyMappingHashMap.put(KeyEvent.KEYCODE_DPAD_CENTER, IRCODE_STB_KEY_CONFIRM);
		// "返回键" 映射为 IRCODE_STB_KEY_BACK
		keyMappingHashMap.put(KeyEvent.KEYCODE_BACK, IRCODE_STB_KEY_BACK);
	}

}
