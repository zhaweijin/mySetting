package com.hiveview.tv.service.request;


/**
 * @author zhangpengzhan
 * 生成 要访问的url
 * 2014年4月18日
 *	上午9:33:32
 */
public class VarietyPagerGetConnectorRequst extends BaseGetRequest {

	/**
	 * 视频的类型  例如：16 电影  18 搞笑
	 */
	private int videoSetType;
	/**
	 * 每页的数据
	 */
	private int pageSize;
	/**
	 * 第几页
	 */
	private int pageNumber;
	
	public VarietyPagerGetConnectorRequst(int videoSetType, int pageSize, int pageNumber) {
		this.videoSetType = videoSetType;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
	}

	/* 
	 * 生成url
	 * (non-Javadoc)
	 * @see com.hiveview.tv.service.request.BaseGetRequest#executeToREST()
	 */
	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_FILM_LIST, 
				new Object[]{this.videoSetType, this.pageSize, this.pageNumber, ApiConstant.APP_VERSION});
	}

}
