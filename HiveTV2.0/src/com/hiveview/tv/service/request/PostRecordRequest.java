package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.hiveview.tv.service.exception.ServiceException;

public class PostRecordRequest extends BasePostRequest {

	private String selectMode;
	private String type;
	private String keyword;
	private int pageNo;
	private int pageSize;
	
	public PostRecordRequest(String selectMode, String type, String keyword, int pageNo, int pageSize) {
		super();
		this.selectMode = selectMode;
		this.type = type;
		this.keyword = keyword;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}


	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HIVEVIEW_API_GET_SEARCH);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("keyword", keyword));
		parameters.add(new BasicNameValuePair("select_model", selectMode));
		parameters.add(new BasicNameValuePair("pageNo", pageNo + ""));
		parameters.add(new BasicNameValuePair("pageSize", pageSize + ""));
		parameters.add(new BasicNameValuePair("record_type", type));
		parameters.add(new BasicNameValuePair("version", ApiConstant.APP_VERSION));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"utf-8");
		httpPost.setEntity(entity);
		return httpPost;
	}


	@Override
	public String executeUrl() throws ServiceException {
		return ApiConstant.HIVEVIEW_API_GET_SEARCH;
	}

}
