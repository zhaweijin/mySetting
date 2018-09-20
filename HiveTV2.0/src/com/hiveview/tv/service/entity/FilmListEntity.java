package com.hiveview.tv.service.entity;

import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmListEntity extends HiveBaseEntity {
	private int pageNo; 
	private int pageSize;
	private int pageCount;
	private int recCount; 
    private List<FilmEntity> pageContent;
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
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getRecCount() {
		return recCount;
	}
	public void setRecCount(int recCount) {
		this.recCount = recCount;
	}
	public List<FilmEntity> getPageContent() {
		return pageContent;
	}
	public void setPageContent(List<FilmEntity> pageContent) {
		this.pageContent = pageContent;
	}
	@Override
	public String toString() {
		return "FilmListEntity [pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", pageCount=" + pageCount + ", recCount=" + recCount
				+ ", pageContent=" + pageContent + "]";
	}
	
    
}
