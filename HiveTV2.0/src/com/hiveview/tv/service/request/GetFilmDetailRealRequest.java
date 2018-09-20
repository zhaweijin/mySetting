package com.hiveview.tv.service.request;

public class GetFilmDetailRealRequest extends BaseGetRequest {

	private String videosetId;
	
	public GetFilmDetailRealRequest(String videosetId) {
		this.videosetId = videosetId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_DETAL_REAL, new Object[]{this.videosetId, ApiConstant.APP_VERSION});
	}

}
