package com.hiveview.tv.service.request;

import com.hiveview.tv.common.AppConstant;

public class GetSubjectListRequest extends BaseGetRequest {

	private int pageSize;
	private int pageNumber;
	private String launcherId;
	private String subjectId;
	private String isEffective;
	private String language;
	
	public GetSubjectListRequest(String launcherId,String subjectId,String isEffective,int pageSize, int pageNumber,String language) {
		this.launcherId=launcherId;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.subjectId=subjectId;
		this.isEffective=isEffective;
		this.language=language;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_SUBJECT_LIST, 
				new Object[]{ this.launcherId,this.subjectId ,this.isEffective,this.pageNumber, this.pageSize,AppConstant.ISDOMESTIC?"":("-"+this.language), ApiConstant.APP_VERSION});
	}

}
