package com.hiveview.tv.service.request;

import android.util.Log;

import com.hiveview.tv.utils.DeviceBoxUtils;

/**
 * 首页推荐位Request
 * 
 * @author chenlixiao
 * 
 */
public class HomeRecommandRequest extends BaseGetRequest {

	private String TAG = "HomeRecommandRequest";

	@Override
	public String executeToREST() {
		//Log.d(TAG ,"URL:::"+ String.format(HIVEVIEW_TV_API_GET_HOME_RECOMMAND, APP_VERSION,DeviceUtils.getSn()));
		return String.format(ApiConstant.HIVEVIEW_TV_API_GET_HOME_RECOMMAND, ApiConstant.APP_VERSION,DeviceBoxUtils.getSn());
	}

}
