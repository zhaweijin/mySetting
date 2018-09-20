package com.hiveview.tv.service.request;


public class GetCastRequest extends BaseGetRequest {

	private int castId;
	
	public GetCastRequest(int castId) {
		super();
		this.castId = castId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_CAST_DETAIL, new Object[]{this.castId, ApiConstant.APP_VERSION});
	}

}
