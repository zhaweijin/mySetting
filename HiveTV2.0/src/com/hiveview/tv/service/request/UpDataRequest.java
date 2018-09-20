package com.hiveview.tv.service.request;

public class UpDataRequest extends BaseGetRequest {


	public UpDataRequest(String version) {
		this.version = version;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_SYS_UPDATA , ApiConstant.APP_VERSION);
	}

}
