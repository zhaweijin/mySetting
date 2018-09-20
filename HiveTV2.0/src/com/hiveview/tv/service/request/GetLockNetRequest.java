package com.hiveview.tv.service.request;
/**
 * 锁网
 * @ClassName: GetLockNetRequest
 * @Description: TODO
 * @author: guosongsheng
 * @date 2014年9月19日 下午6:06:13
 *
 */
public class GetLockNetRequest extends BaseGetRequest {
	/**
	 * 设备类型
	 */
	private String model;
	/**
	 * MAC
	 */
	private String mac;
	/**
	 * SN
	 */
	private String sn;
	/**
	 * 加密参数
	 */
	private String sign;

	public GetLockNetRequest(String model, String mac, String sn, String sign) {
		super();
		this.model = model;
		this.mac = mac;
		this.sn = sn;
		this.sign = sign;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_LOCK_NET, new Object[] { this.model, this.mac, this.sn, this.sign });
	}

}
