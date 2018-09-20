package com.hiveview.tv.service.request;

public class GetCastRelatedRequest extends BaseGetRequest {

	private int castId;
	
	public GetCastRelatedRequest(int castId) {
		super();
		this.castId = castId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_CAST_RELATED_FILM, new Object[]{this.castId, ApiConstant.APP_VERSION});
	}

}
