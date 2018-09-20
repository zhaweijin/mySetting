package com.hiveview.tv.utils;

import java.util.HashMap;

import com.hiveview.tv.R;
import com.hiveview.tv.common.HiveviewApplication;

public class ClientErrorDataCenter {

	private volatile static ClientErrorDataCenter instance;

	private HashMap<String, String> map = new HashMap<String, String>();

	private ClientErrorDataCenter() {
		map.put(HiveViewErrorCode.E0000000, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000000));// 鉴权未通过
		map.put(HiveViewErrorCode.E0000404, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000404));// 访问的接口不存在
		map.put(HiveViewErrorCode.E0000500, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000404));// 服务器程序报错
		map.put(HiveViewErrorCode.E0000502, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000404));// 服务端返回502错误
		map.put(HiveViewErrorCode.E0000599, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000599));// 网络未连接
		map.put(HiveViewErrorCode.E0000598, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000598));// json解析出错
		map.put(HiveViewErrorCode.E0000600, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000404));// 服务端返回数据为空

		map.put(HiveViewErrorCode.E0000601, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000601_E0000603));// 数据字段报错
		map.put(HiveViewErrorCode.E0000602, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000602));// 用户所观看的视频剧集已经下线
		map.put(HiveViewErrorCode.E0000603, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000601_E0000603));// 推荐位对应的类型出错导致客户端响应出错
		map.put(HiveViewErrorCode.E0000604, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000604));// 天气信息获取失败
		map.put(HiveViewErrorCode.E0000605,HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000599));// 网络运行时候异常，就是因为硬件、软件、网络流，协议，超时等原因导致网络访问失败
		map.put(HiveViewErrorCode.E0000607, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000607));
		map.put(HiveViewErrorCode.E0000608, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000608));
		map.put(HiveViewErrorCode.E0000609, HiveviewApplication.mContext.getResources().getString(R.string.Errorcode_E0000609));
	}

	/** Returns singleton class instance */
	public static ClientErrorDataCenter getInstance() {
		if (instance == null) {
			synchronized (ClientErrorDataCenter.class) {
				if (instance == null) {
					instance = new ClientErrorDataCenter();
				}
			}
		}

		return instance;
	}

	/**
	 * 根据错误码，获取友好提示的内容
	 * 
	 * @Title: ClientErrorDataCenter
	 * @author:陈丽晓
	 * @Description: TODO
	 * @param errorCode
	 * @return
	 */
	public String getErrorContentByErrorCode(String errorCode) {
		return null != map.get(errorCode) ? map.get(errorCode) : "";
	}
}
