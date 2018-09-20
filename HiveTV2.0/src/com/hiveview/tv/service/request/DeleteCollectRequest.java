package com.hiveview.tv.service.request;

public class DeleteCollectRequest extends BaseGetRequest {
	
	private int videoSetId;

	
	public DeleteCollectRequest(int videoSetId) {
		this.videoSetId = videoSetId;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_DELETE_COLLECT_LIST, 
				new Object[]{this.videoSetId, ApiConstant.APP_VERSION});
	}

}
