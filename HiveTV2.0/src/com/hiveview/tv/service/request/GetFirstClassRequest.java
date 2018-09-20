package com.hiveview.tv.service.request;

import com.hiveview.tv.common.AppConstant;

public class GetFirstClassRequest extends BaseGetRequest {
	
	private String languageString;
	
	public GetFirstClassRequest(String languageString) {
		super();
		this.languageString = languageString;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_FIRSTCLASS, 1,AppConstant.ISDOMESTIC?"":("/"+this.languageString), ApiConstant.APP_VERSION);
	}

}
