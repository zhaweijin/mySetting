package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.hiveview.tv.service.exception.ServiceException;

public class SearchPostRequest extends BasePostRequest {

	private String keyword;
	private int cast_id;
	private int select_model;
	private int record_type;
	private int pageNo;
	private int pageSize;

	public SearchPostRequest(String keyword, int cast_id, int select_model, int record_type, int pageNo, int pageSize) {
		this.keyword = keyword;
		this.cast_id = cast_id;
		this.select_model = select_model;
		this.record_type = record_type;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HIVEVIEW_API_POST_SEARCH_LIST);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		paramList.add(new BasicNameValuePair("keyword", keyword));
		if (cast_id != 0) {
			paramList.add(new BasicNameValuePair("cast_id", cast_id + ""));
		}
		paramList.add(new BasicNameValuePair("select_model", select_model + ""));
		if (record_type != 0) {
			paramList.add(new BasicNameValuePair("record_type", record_type + ""));
		}
		paramList.add(new BasicNameValuePair("pageNo", pageNo + ""));
		paramList.add(new BasicNameValuePair("pageSize", pageSize + ""));
		paramList.add(new BasicNameValuePair("vsersion", ApiConstant.APP_VERSION));
		httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		return httpPost;
	}

	@Override
	public String executeUrl() throws ServiceException {
		return ApiConstant.HIVEVIEW_API_POST_SEARCH_LIST;
	}

}
