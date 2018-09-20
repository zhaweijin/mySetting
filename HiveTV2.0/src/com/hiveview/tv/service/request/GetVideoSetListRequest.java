package com.hiveview.tv.service.request;

public class GetVideoSetListRequest extends BaseGetRequest {

	private int videoSetType;
	private int pageSize;
	private int pageNumber;
	
	public GetVideoSetListRequest(int videoSetType, int pageSize, int pageNumber) {
		this.videoSetType = videoSetType;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_LIST, 
				new Object[]{this.videoSetType, this.pageSize, this.pageNumber, ApiConstant.APP_VERSION});
	}

}
