package com.hiveview.tv.service;

import android.R.integer;

import com.hiveview.tv.service.request.BaseGetRequest;

public class GetDownLoadURIRequest extends BaseGetRequest {

	private String url;
	
	public GetDownLoadURIRequest(String url) {
		// TODO Auto-generated constructor stub
		this.url=url;
	}

	@Override
	public String executeToREST() {
		// TODO Auto-generated method stub
		return url;
	}

}
