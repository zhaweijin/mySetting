package com.hiveview.tv.service.entity;

import java.util.ArrayList;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class SubjectInfo extends HiveBaseEntity {

	private static final long serialVersionUID = 1L;
	private int contentCid;
	private String contentDesc;
	private int contentId;
	private String contentImg;
	private String contentName;
	private int contentType;
	private String createdTime;
	private int id;
	private int isEffective;
	private int seq;
	private String showType;
	private int subjectId;
	private String updatedTime;
	private String videosetImg;
	private String videosetTvImg;
	public int getContentCid() {
		return contentCid;
	}
	public void setContentCid(int contentCid) {
		this.contentCid = contentCid;
	}
	public String getContentDesc() {
		return contentDesc;
	}
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public String getContentImg() {
		return contentImg;
	}
	public void setContentImg(String contentImg) {
		this.contentImg = contentImg;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIsEffective() {
		return isEffective;
	}
	public void setIsEffective(int isEffective) {
		this.isEffective = isEffective;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getShowType() {
		return showType;
	}
	public void setShowType(String showType) {
		this.showType = showType;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getVideosetImg() {
		return videosetImg;
	}
	public void setVideosetImg(String videosetImg) {
		this.videosetImg = videosetImg;
	}
	public String getVideosetTvImg() {
		return videosetTvImg;
	}
	public void setVideosetTvImg(String videosetTvImg) {
		this.videosetTvImg = videosetTvImg;
	}
	@Override
	public String toString() {
		return "SubjectInfo [contentCid=" + contentCid + ", contentDesc="
				+ contentDesc + ", contentId=" + contentId + ", contentImg="
				+ contentImg + ", contentName=" + contentName
				+ ", contentType=" + contentType + ", createdTime="
				+ createdTime + ", id=" + id + ", isEffective=" + isEffective
				+ ", seq=" + seq + ", showType=" + showType + ", subjectId="
				+ subjectId + ", updatedTime=" + updatedTime + ", videosetImg="
				+ videosetImg + ", videosetTvImg=" + videosetTvImg + "]";
	}
	
	
	
	

}
