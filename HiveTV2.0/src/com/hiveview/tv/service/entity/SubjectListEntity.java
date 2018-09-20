package com.hiveview.tv.service.entity;

import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SubjectListEntity extends HiveBaseEntity{
	
	private static final long serialVersionUID = 1L;
//    pageNo: 1, 
//    pageSize: 20, 
//    pageCount: 1, 
//    recCount: 10, 
	
	private String createdTime;
	private String imgSize;
	private int isEffective;
	private String launcherId;
	private String launcherName;
	private int seq;
	private String subjectBgImg;
	private String subjectDesc;
	private int subjectId;
	private String subjectName;
	private String subjectPic;
	private String updatedTime;
	
	private List<SubjectEntity> subjectContentVos;

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}

	public int getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(int isEffective) {
		this.isEffective = isEffective;
	}

	public String getLauncherId() {
		return launcherId;
	}

	public void setLauncherId(String launcherId) {
		this.launcherId = launcherId;
	}

	public String getLauncherName() {
		return launcherName;
	}

	public void setLauncherName(String launcherName) {
		this.launcherName = launcherName;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getSubjectBgImg() {
		return subjectBgImg;
	}

	public void setSubjectBgImg(String subjectBgImg) {
		this.subjectBgImg = subjectBgImg;
	}

	public String getSubjectDesc() {
		return subjectDesc;
	}

	public void setSubjectDesc(String subjectDesc) {
		this.subjectDesc = subjectDesc;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectPic() {
		return subjectPic;
	}

	public void setSubjectPic(String subjectPic) {
		this.subjectPic = subjectPic;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public List<SubjectEntity> getSubjectContentVos() {
		return subjectContentVos;
	}

	public void setSubjectContentVos(List<SubjectEntity> subjectContentVos) {
		this.subjectContentVos = subjectContentVos;
	}

	@Override
	public String toString() {
		return "SubjectListEntity [createdTime=" + createdTime + ", imgSize="
				+ imgSize + ", isEffective=" + isEffective + ", launcherId="
				+ launcherId + ", launcherName=" + launcherName + ", seq="
				+ seq + ", subjectBgImg=" + subjectBgImg + ", subjectDesc="
				+ subjectDesc + ", subjectId=" + subjectId + ", subjectName="
				+ subjectName + ", subjectPic=" + subjectPic + ", updatedTime="
				+ updatedTime + ", subjectContentVos=" + subjectContentVos
				+ "]";
	}

	
	

}
