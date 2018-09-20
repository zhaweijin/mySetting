package com.hiveview.tv.service.request;

/**
 * 首页推荐位Request
 * 
 * @author chenlixiao
 * 
 */
public class BluelightRecommandRequest extends BaseGetRequest {

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_TV_API_GET_BLUELIGHT_RECOMMAND, ApiConstant.APP_VERSION, ApiConstant.BLUELIGHT_TYPE);
	}

}
