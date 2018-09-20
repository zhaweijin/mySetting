package com.hiveview.tv.service.request;

public class GetCastWithCountRequest extends BaseGetRequest {

	private String keyCode;
	private int videSetType;

	public GetCastWithCountRequest(String keyCode, int videoSetType) {
		super();
		this.keyCode = keyCode;
		this.videSetType = videoSetType;
	}

	@Override
	public String executeToREST() {
		if (videSetType > 0) {
			return String.format(ApiConstant.HIVEVIEW_API_SEARCH_COUNT_BY_HUMAN_LIST, keyCode, videSetType);
		} else {
			return String.format(ApiConstant.HIVEVIEW_API_SEARCH_COUNT_BY_HUMAN_LIST, keyCode,"");
		}
	}

}
