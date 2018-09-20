package com.hiveview.tv.service.request;

public class QiyiRelativeRequest extends BaseGetRequest {

	private int chnnid;
	private int id;

	public QiyiRelativeRequest(int chnnid, int id) {
		this.chnnid = chnnid;
		this.id = id;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.AIQIYI_RELATIVERECOMMANDS, new Object[] { this.chnnid, this.id, ApiConstant.APP_VERSION });
	}

}
