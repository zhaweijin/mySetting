package com.hiveview.tv.service.request;

public class SystemAppListRequest extends BaseGetRequest {


	public SystemAppListRequest(String version) {
		this.version = version;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_SYSTEM_APP_LIST,ApiConstant.APP_VERSION);
	}

}
