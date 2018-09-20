package com.hiveview.tv.service.request;

import com.hiveview.tv.common.AppConstant;

public class GetAppFocusListRequest extends BaseGetRequest {
	
	private String navigation_id;
	private String languageString;

	


	public GetAppFocusListRequest(String navigation_id, String languageString) {
		super();
		this.navigation_id = navigation_id;
		this.languageString = languageString;
	}




	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_APP_FOCUS_LIST,navigation_id,AppConstant.ISDOMESTIC?"":("-"+this.languageString),ApiConstant.APP_VERSION);
	}
}
