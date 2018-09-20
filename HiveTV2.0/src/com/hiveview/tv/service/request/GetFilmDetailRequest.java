package com.hiveview.tv.service.request;

public class GetFilmDetailRequest extends BaseGetRequest {

	private String videosetId;
	
	public GetFilmDetailRequest(String videosetId) {
		this.videosetId = videosetId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_DETAIL, new Object[]{this.videosetId, ApiConstant.APP_VERSION});
	}

}
