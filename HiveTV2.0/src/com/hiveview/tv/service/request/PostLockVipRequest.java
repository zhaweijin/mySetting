package com.hiveview.tv.service.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.hiveview.tv.service.exception.ServiceException;

import android.content.Context;

public class PostLockVipRequest extends BasePostRequest {
	private String ClientID;
	/**
	 * MAC
	 */
	private String MacAddress;
	/**
	 * SN
	 */
	private String SequenceNo;
	/**
	 * 时间
	 */
	private String Timestamp;
	/**
	 * 加密参数
	 */
	private String Sign;
	
	private Context context;

	public PostLockVipRequest(Context context, String ClientID,String MacAddress,String SequenceNo,String Timestamp,String Sign) {
		super();
		this.context = context;
		this.ClientID = ClientID;
		this.MacAddress = MacAddress;
		this.SequenceNo = SequenceNo;
		this.Timestamp = Timestamp;
		this.Sign = Sign;
	}

	@Override
	public HttpPost executeToEntity() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		HttpPost httpPost=new HttpPost(ApiConstant.HIVEVIEW_API_LOCK_VIP);
		List<NameValuePair>  paramList=new ArrayList<NameValuePair>();
		paramList.add(new BasicNameValuePair("MacAddress", MacAddress));
		paramList.add(new BasicNameValuePair("SequenceNo", SequenceNo));
		paramList.add(new BasicNameValuePair("Timestamp", Timestamp));
		paramList.add(new BasicNameValuePair("Sign", Sign));
		httpPost.setEntity(new UrlEncodedFormEntity(paramList,"UTF-8"));
		return httpPost;
	}

	@Override
	public String executeUrl() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
