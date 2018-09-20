package com.hiveview.tv.service.entity;

import java.io.Serializable;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class AppFocusEntity extends HiveBaseEntity  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7542725213492840792L;

	
	private String apkPackage;
	
	private String classFirstVo;
	
	private String contentDesc ;
	
	private int contentId;
	
	private String contentName;
	
	private String contentType;
	
	private String contentTypeName;
	
	private long createdTime;
	
	private String focusLargeImg;
	
	private String focusThumbImg;
	
	private int focusType;
	
	private int id;
	
	private int intervalTime;
	
	private String isApk;
	
	private int isEffective;
	
	private int isIntranet;
	
	private String launcherBg;
	
	private String launcherId;
	
	private String launcherName;
	
	private int logic;
	
	private String positionId;
	
	private String positionName;
	
	private int seq;
	
	private String showType;
	
	private String type;
	
	private long updatedTime;
	
	private String imgSize;
	
	private int videoId;
	
	private String subjectBgImg;
	
	private int cpId;

	public String getApkPackage() {
		return apkPackage;
	}

	public void setApkPackage(String apkPackage) {
		this.apkPackage = apkPackage;
	}

	public String getClassFirstVo() {
		return classFirstVo;
	}

	public void setClassFirstVo(String classFirstVo) {
		this.classFirstVo = classFirstVo;
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

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentTypeName() {
		return contentTypeName;
	}

	public void setContentTypeName(String contentTypeName) {
		this.contentTypeName = contentTypeName;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public String getFocusLargeImg() {
		return focusLargeImg;
	}

	public void setFocusLargeImg(String focusLargeImg) {
		this.focusLargeImg = focusLargeImg;
	}

	public String getFocusThumbImg() {
		return focusThumbImg;
	}

	public void setFocusThumbImg(String focusThumbImg) {
		this.focusThumbImg = focusThumbImg;
	}



	public int getFocusType() {
		return focusType;
	}

	public void setFocusType(int focusType) {
		this.focusType = focusType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getIsApk() {
		return isApk;
	}

	public void setIsApk(String isApk) {
		this.isApk = isApk;
	}

	public int getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(int isEffective) {
		this.isEffective = isEffective;
	}

	public int getIsIntranet() {
		return isIntranet;
	}

	public void setIsIntranet(int isIntranet) {
		this.isIntranet = isIntranet;
	}

	public String getLauncherBg() {
		return launcherBg;
	}

	public void setLauncherBg(String launcherBg) {
		this.launcherBg = launcherBg;
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

	public int getLogic() {
		return logic;
	}

	public void setLogic(int logic) {
		this.logic = logic;
	}



	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getImgSize() {
		return imgSize;
	}

	public void setImgSize(String imgSize) {
		this.imgSize = imgSize;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	
	public String getSubjectBgImg() {
		return subjectBgImg;
	}

	public void setSubjectBgImg(String subjectBgImg) {
		this.subjectBgImg = subjectBgImg;
	}

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	@Override
	public String toString() {
		return "AppFocusEntity [apkPackage=" + apkPackage + ", classFirstVo="
				+ classFirstVo + ", contentDesc=" + contentDesc
				+ ", contentId=" + contentId + ", contentName=" + contentName
				+ ", contentType=" + contentType + ", contentTypeName="
				+ contentTypeName + ", createdTime=" + createdTime
				+ ", focusLargeImg=" + focusLargeImg + ", focusThumbImg="
				+ focusThumbImg + ", focusType=" + focusType + ", id=" + id
				+ ", intervalTime=" + intervalTime + ", isApk=" + isApk
				+ ", isEffective=" + isEffective + ", isIntranet=" + isIntranet
				+ ", launcherBg=" + launcherBg + ", launcherId=" + launcherId
				+ ", launcherName=" + launcherName + ", logic=" + logic
				+ ", positionId=" + positionId + ", positionName="
				+ positionName + ", seq=" + seq + ", showType=" + showType
				+ ", type=" + type + ", updatedTime=" + updatedTime
				+ ", imgSize=" + imgSize + ", videoId=" + videoId
				+ ", subjectBgImg=" + subjectBgImg + ", cpId=" + cpId + "]";
	}

}
