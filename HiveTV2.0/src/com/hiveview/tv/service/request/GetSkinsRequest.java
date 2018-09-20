package com.hiveview.tv.service.request;

public class GetSkinsRequest extends BaseGetRequest {

	public GetSkinsRequest() {
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_BG_LIST, ApiConstant.APP_VERSION);
	}

}
