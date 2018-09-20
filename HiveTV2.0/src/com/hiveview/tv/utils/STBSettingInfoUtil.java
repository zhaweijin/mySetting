/**
 * IflytekUtil.java[V 1.0.0]
 * classes : com.hiveview.tv.utils.IflytekUtil
 * 李红记 Creat at 2014年4月22日 上午9:38:57
 */
package com.hiveview.tv.utils;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.iflytek.STV.STV_Operator_Common;
import com.iflytek.STV.STV_Operator_Common.E_STBInfo;

/**
 * 获取指控设置运营商信息
 * 
 * @author jia
 * 
 */
public class STBSettingInfoUtil {

	private static final String TAG = "STBSettingInfoUtil";
	public static final String HDMIIN_SCENE = "HDMI_IN";
	// STV_Operator_Common 对象
	private static STV_Operator_Common stv_operator = new STV_Operator_Common();
	// 存放STB
	private static ArrayList<E_STBInfo> stbList;

	/*
	 * 
	 * 初始化接口
	 */
	public static void InitOperator(Context ctx) {

		stv_operator.initOperator(ctx, new STV_Operator_Common.ConnectionEvent() {

			@Override
			public void OnReturn(boolean arg0) {
				// TODO Auto-generated method stub
				Log.i("test_output", "和服务连接 " + (arg0 ? "正常" : "断开"));

			}
		});

	}

	/*
	 * 调出机顶盒设置
	 */
	public static void STBShowTVSetUI(Context context) {
		stv_operator.showMainSettingsUI();
	}

	/*
	 * 反初始化接口
	 */
	public static void UninitOperator() {

		stv_operator.uninitOperator();

	}

	/*
	 * 获取当前智控设置的运营商信息
	 */
	public static ArrayList<E_STBInfo> getSTBSetting() {
		stbList = stv_operator.getSTBSettingList();

		return stbList;

	}

	/**
	 * 通知超级指控响应遥控器按键为机顶盒按键
	 */
	public static void notifyTVKeyPress(int keycode) {
		Log.i(TAG, "notifyTVKeyPress-->start");
		stv_operator.notifyTVKeyPress(keycode);
		Log.i(TAG, "notifyTVKeyPress-->end");
	}

	/**
	 * 使用当前默认的机顶盒进行按键转发
	 * 
	 * @Title: STBSettingInfoUtil
	 * @author:郭松胜
	 * @Description: TODO
	 * @param keycode
	 */
	public static void notifySTBIrKeyPress(int keycode) {
		Log.i(TAG, "notifySTBIrKeyPress-------------------------->" + keycode);
		stv_operator.notifySTBIrKeyPress(keycode);
	}

	public static void adaptSTBSetCurrentScene(Context context, String HDMIIN_SCENE) {
		STV_Operator_Common.adapt_stb_setCurrentScene(context, HDMIIN_SCENE);
	}

	public static void adaptSTBDefineScenes(Context context) {
		ArrayList<String> scene = new ArrayList<String>();
		scene.add(HDMIIN_SCENE);
		STV_Operator_Common.adapt_stb_defineScenes(context, scene);
	}

}
