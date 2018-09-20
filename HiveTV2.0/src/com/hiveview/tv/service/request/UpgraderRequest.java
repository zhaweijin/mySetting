package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.hiveview.tv.service.exception.ServiceException;

import android.util.Log;

/**
 * 静默升级
 * 
 * @ClassName: UpgraderRequest
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年9月17日 上午10:15:23
 * 
 */
public class UpgraderRequest extends BasePostRequest {
	/**
	 * 设备类型
	 */
	private String model;
	/**
	 * Rom版本号
	 */
	private String romVersion;
	/**
	 * mac
	 * 
	 */
	private String mac;
	/**
	 * sn
	 */
	private String sn;

	/**
	 * @param @param model 设备类型
	 * @param @param romVersion Rom版本号
	 */
	public UpgraderRequest(String model, String romVersion,String mac,String sn) {
		super();
		this.model = model;
		this.romVersion = romVersion;
		this.mac=mac;
		this.sn=sn;
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(ApiConstant.HIVEVIEW_API_UPGRADER);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("model", model));
		parameters.add(new BasicNameValuePair("romVersion", romVersion));
		parameters.add(new BasicNameValuePair("mac", mac));
		parameters.add(new BasicNameValuePair("sn", sn));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
		httpPost.setEntity(entity);
		Log.i("UpgraderRequest", "UpgraderRequest.............." + ApiConstant.HIVEVIEW_API_UPGRADER);
		Log.i("UpgraderRequest", "UpgraderRequest.............." + parameters.toString());
		return httpPost;
	}

	@Override
	public String executeUrl() throws ServiceException {
		return ApiConstant.HIVEVIEW_API_UPGRADER;
	}
}
