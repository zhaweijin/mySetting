package com.hiveview.tv.service.request;

public class GetCastInfoRequest extends BaseGetRequest {
	
	private int castId;

	
	public GetCastInfoRequest(int castId) {
		this.castId = castId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_CAST_INFO, 
				new Object[]{this.castId, ApiConstant.APP_VERSION});
	}

}
