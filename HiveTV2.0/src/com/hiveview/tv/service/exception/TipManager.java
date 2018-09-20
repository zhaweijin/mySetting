package com.hiveview.tv.service.exception;

import java.util.HashMap;

import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;
import com.hiveview.tv.utils.HiveViewErrorCode;

public class TipManager {

	private static TipManager container = new TipManager();
	private static HashMap<String, String>  map = new HashMap<String, String>();  

	public static TipManager getInstance(){

		if(map.size() == 0) 
			initTips();

		return container;
	}

	public String getTip(String code){

		return map.get(code);
	}
	
	private static void initTips() {

		map.put(HiveViewErrorCode.E0000000, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000000));// 鉴权未通过
		map.put(HiveViewErrorCode.E0000404, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000404));// 访问的接口不存在
		map.put(HiveViewErrorCode.E0000500, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000404));// 服务器程序报错
		map.put(HiveViewErrorCode.E0000502, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000404));// 服务端返回502错误
		map.put(HiveViewErrorCode.E0000599, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000599));// 网络未连接
		map.put(HiveViewErrorCode.E0000598, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000598));// json解析出错
		map.put(HiveViewErrorCode.E0000600, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000404));// 服务端返回数据为空

		map.put(HiveViewErrorCode.E0000601, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000601_E0000603));// 数据字段报错
		map.put(HiveViewErrorCode.E0000602, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000602));// 用户所观看的视频剧集已经下线
		map.put(HiveViewErrorCode.E0000603, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000601_E0000603));// 推荐位对应的类型出错导致客户端响应出错
		map.put(HiveViewErrorCode.E0000604, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000604));// 天气信息获取失败
		map.put(HiveViewErrorCode.E0000605, HiveviewApplication.mContext
				.getResources().getString(R.string.Errorcode_E0000599));// 网络运行时候异常，就是因为硬件、软件、网络流，协议，超时等原因导致网络访问失败

	}
	
	private TipManager(){};

}
