package com.hiveview.tv.service.request;

public class HotWordRequest extends BaseGetRequest {

	private String hotword;

	public HotWordRequest(String hotword) {
		this.hotword = hotword;
	}

	@Override
	public String executeToREST() {
		String s = String.format(ApiConstant.HIVEVIEW_TV_API_HOT_WORD, hotword);
		return s;
	}

}
