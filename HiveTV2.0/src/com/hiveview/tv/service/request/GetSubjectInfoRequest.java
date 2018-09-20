package com.hiveview.tv.service.request;

public class GetSubjectInfoRequest extends BaseGetRequest {

	private String id;
	private int pageSize;
	private int pageNumber;
	private String subjectId;
	private String isEffective;
	
	
	public GetSubjectInfoRequest(String id,String subjectId,String isEffective,int pageSize,int pageNumber) {
		this.id = id;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		this.subjectId = subjectId;
		this.isEffective = isEffective;
	}

	@Override
	public String executeToREST() {
		return String.format(ApiConstant.HIVEVIEW_API_GET_SUBJECT_INFO, 
				new Object[]{ this.id,this.subjectId ,this.isEffective,this.pageNumber, this.pageSize, ApiConstant.APP_VERSION});
	}

}
