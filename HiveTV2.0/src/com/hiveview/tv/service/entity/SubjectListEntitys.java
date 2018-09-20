package com.hiveview.tv.service.entity;

import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SubjectListEntitys extends HiveBaseEntity{
	
	private static final long serialVersionUID = 1L;
//    pageNo: 1, 
//    pageSize: 20, 
//    pageCount: 1, 
//    recCount: 10, 
	
	private int pageCount;
	private int pageNo;
	private int pageSize;
	private int recCount;

	
	private List<SubjectListEntity> pageContent;


	public int getPageCount() {
		return pageCount;
	}


	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}


	public int getPageNo() {
		return pageNo;
	}


	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getRecCount() {
		return recCount;
	}


	public void setRecCount(int recCount) {
		this.recCount = recCount;
	}


	public List<SubjectListEntity> getPageContent() {
		return pageContent;
	}


	public void setPageContent(List<SubjectListEntity> pageContent) {
		this.pageContent = pageContent;
	}


	@Override
	public String toString() {
		return "SubjectListEntitys [pageCount=" + pageCount + ", pageNo="
				+ pageNo + ", pageSize=" + pageSize + ", recCount=" + recCount
				+ ", pageContent=" + pageContent + "]";
	}



	
	

}
