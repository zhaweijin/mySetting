package com.hiveview.tv.service.request;

/**
 * 直播流Request
 * 
 * @author 李红记
 * 
 */
public class GetLiveStreamListRequest extends BaseGetRequest {

	private String  platform_id;
	private String tv_id;
	private String  begin_date;
	private String end_date;
	
	
	public GetLiveStreamListRequest(String platform_id ,String tv_id ,
			String begin_date, String end_date) {
		super();
		this.platform_id = platform_id;
		this.tv_id = tv_id;
		this.begin_date = begin_date;
		this.end_date = end_date;
	}


	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_TV_API_GET_TV_LIST, 
				new Object[]{this.platform_id,this.tv_id, this.begin_date,this.end_date, ApiConstant.APP_VERSION});
	}

}
