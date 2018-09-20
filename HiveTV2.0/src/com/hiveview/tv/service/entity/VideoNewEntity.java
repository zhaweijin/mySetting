package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class VideoNewEntity extends HiveBaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 155319625585392553L;
	private int cid;
	private int cp;
	private String desc;
	private String directors;
	private String epfocus;
	private int eplen;
	private String epname;
	private int eporder;
	private int eptype;
	private String epvid;
	private String mainActors;
	private String picurl;
	private int videoId;
	private int videosetId;
	private String year;
		
	
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getEpfocus() {
		return epfocus;
	}

	public void setEpfocus(String epfocus) {
		this.epfocus = epfocus;
	}

	public int getEplen() {
		return eplen;
	}

	public void setEplen(int eplen) {
		this.eplen = eplen;
	}

	public String getEpname() {
		return epname;
	}

	public void setEpname(String epname) {
		this.epname = epname;
	}

	public int getEporder() {
		return eporder;
	}

	public void setEporder(int eporder) {
		this.eporder = eporder;
	}

	public int getEptype() {
		return eptype;
	}

	public void setEptype(int eptype) {
		this.eptype = eptype;
	}

	public String getEpvid() {
		return epvid;
	}

	public void setEpvid(String epvid) {
		this.epvid = epvid;
	}

	public String getMainActors() {
		return mainActors;
	}

	public void setMainActors(String mainActors) {
		this.mainActors = mainActors;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

	public int getVideosetId() {
		return videosetId;
	}

	public void setVideosetId(int videosetId) {
		this.videosetId = videosetId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "VideoNewEntity [cid=" + cid + ", cp=" + cp + ", desc=" + desc + ", directors=" + directors + ", epfocus=" + epfocus + ", eplen="
				+ eplen + ", epname=" + epname + ", eporder=" + eporder + ", eptype=" + eptype + ", epvid=" + epvid + ", mainActors=" + mainActors
				+ ", picurl=" + picurl + ", videoId=" + videoId + ", videosetId=" + videosetId + ", year=" + year + "]";
	}

	


	
	
	 
	
}
