package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hiveview.tv.service.exception.ServiceException;

import android.util.Log;

/**
 * 添加根据channelName获取channelCode Request
 * 
 * @ClassName: GetChannelsByNamesRequest
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年8月8日 下午2:19:06
 * 
 */
public class GetChannelsByNamesRequest extends HuanWangBasePostRequest {
	/**
	 * TAG
	 */
	private static final String TAG = "GetChannelsByNamesRequest";
	/**
	 * 电台名称数组
	 */
	private String[] names;

	/**
	 * 构造
	 * 
	 * @param @param names 电台名称数组
	 */
	public GetChannelsByNamesRequest(String[] names) {
		this.names = names;
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HUANWANG_DOMAIN);
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();

		JSONObject json = getCommonJson();
		try {
			json.put("action", ApiConstant.HUANWANG_GETCHANEEL_CODE_NAME);
			JSONObject jsonParam = new JSONObject();
			String codeTmp = "[";
			if (names != null) {
				for (int i = 0; i < names.length; i++) {
					codeTmp += names[i] + ",";
				}
				codeTmp += "]";
				codeTmp = codeTmp.replace(",]", "]");

			}

			JSONArray array = new JSONArray(codeTmp);
			jsonParam.put("names", array);

			json.put("param", jsonParam);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, json.toString());
		paramList.add(new BasicNameValuePair("jsonstr", json.toString()));
		httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		return httpPost;
	}

	@Override
	public String executeUrl() throws ServiceException {
		return ApiConstant.HUANWANG_DOMAIN+"|"+ApiConstant.HUANWANG_GETCHANEEL_CODE_NAME;
	}
}
