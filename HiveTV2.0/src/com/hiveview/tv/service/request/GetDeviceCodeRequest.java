/**
 * @Title GetCaptchaRequest.java
 * @Package com.hiveview.user.request
 * @author 郭松胜
 * @date 2014-6-3 下午7:39:28
 * @Description TODO
 * @version V1.0
 */
package com.hiveview.tv.service.request;

/**
 * 获取设备码
 * @ClassName: GetDeviceCodeRequest
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014-6-3 下午7:39:28
 * 
 */
public class GetDeviceCodeRequest extends BaseGetRequest {
	/**
	 * 手设备Mac
	 */
	private String deviceMac;
	/**
	 * 设备Sn
	 */
	private String devicesSn;

	/**
	 * 构造
	 * 
	 * @param @param userPhone
	 */
	public GetDeviceCodeRequest(String deviceMac , String devicesSn) {
		super();
		this.deviceMac = deviceMac;
		this.devicesSn = devicesSn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hiveview.user.request.BaseGetRequest#executeToREST()
	 */
	@Override
	public String executeToREST() {
		String url = String.format(ApiConstant.API_GET_DEVICES_CODE, new Object[] { this.deviceMac ,  this.devicesSn });
		return url;
	}

}
