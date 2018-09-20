package com.hiveview.tv.service.request;

public class GetCollectRecordListRequest extends BaseGetRequest {

	private int pageSize;
	private int pageNumber;
	
	public GetCollectRecordListRequest(int pageSize, int pageNumber) {
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_COLLECT_LIST, 
				new Object[]{this.pageNumber, this.pageSize, ApiConstant.APP_VERSION});
	}

}
