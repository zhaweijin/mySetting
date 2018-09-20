package com.hiveview.tv.service.request;

public class GetVideoSetListByTagRequest extends BaseGetRequest {

	private int videoSetType;
	private String tag;
	private int pageSize;
	private int pageNumber;
	
	public GetVideoSetListByTagRequest(int videoSetType, String tag,int pageSize, int pageNumber) {
		this.videoSetType = videoSetType;
		this.tag = tag;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_LIST_BY_TAG, 
				new Object[]{this.videoSetType, this.tag,this.pageSize, this.pageNumber, ApiConstant.APP_VERSION});
	}

}
