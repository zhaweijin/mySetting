package com.hiveview.tv.service.request;

public class SaveCollectRecordRequest extends BaseGetRequest {
	
	private int videoSetId;
	private int videoType;

	
	public SaveCollectRecordRequest(int videoSetId, int videoType) {
		this.videoSetId = videoSetId;
		this.videoType = videoType;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_SAVE_COLLECT_LIST, 
				new Object[]{this.videoSetId, this.videoType, ApiConstant.APP_VERSION});
	}

}
