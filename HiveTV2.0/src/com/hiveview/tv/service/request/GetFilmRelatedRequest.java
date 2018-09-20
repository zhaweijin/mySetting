package com.hiveview.tv.service.request;

public class GetFilmRelatedRequest extends BaseGetRequest {

	private int videoSetId;
	
	public GetFilmRelatedRequest(int videoSetId) {
		this.videoSetId = videoSetId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_RELATED_FILM, new Object[]{this.videoSetId, ApiConstant.APP_VERSION});
	}

}
