package com.hiveview.tv.service.request;

/**
 * 首页推荐位Request
 * 
 * @author chenlixiao
 * 
 */
public class MovieRecommandRequest extends BaseGetRequest {

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_TV_API_GET_MOVIE_RECOMMAND, ApiConstant.APP_VERSION);
	}

}
