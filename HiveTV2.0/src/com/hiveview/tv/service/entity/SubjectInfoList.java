package com.hiveview.tv.service.entity;

import java.util.ArrayList;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SubjectInfoList extends HiveBaseEntity {

	private static final long serialVersionUID = 1L;
	private int pageCount;
	private int pageNo;
	private int pageSize;
	private int recCount;
	
	private ArrayList<SubjectInfo> pageContent;

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

	public ArrayList<SubjectInfo> getPageContent() {
		return pageContent;
	}

	public void setPageContent(ArrayList<SubjectInfo> pageContent) {
		this.pageContent = pageContent;
	}

	@Override
	public String toString() {
		return "SubjectInfoList [pageCount=" + pageCount + ", pageNo=" + pageNo
				+ ", pageSize=" + pageSize + ", recCount=" + recCount
				+ ", pageContent=" + pageContent + "]";
	}

	
	

}
