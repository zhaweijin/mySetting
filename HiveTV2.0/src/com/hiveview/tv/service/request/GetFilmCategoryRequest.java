package com.hiveview.tv.service.request;

public class GetFilmCategoryRequest extends BaseGetRequest {

	private int videoSetType;
	
	public GetFilmCategoryRequest(int videoSetType) {
		this.videoSetType = videoSetType;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_CATEGORY, new Object[]{this.videoSetType, ApiConstant.APP_VERSION});
	}

}
