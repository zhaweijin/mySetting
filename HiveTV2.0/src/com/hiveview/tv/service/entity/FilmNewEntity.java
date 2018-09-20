package com.hiveview.tv.service.entity;

import java.io.Serializable;

import android.R.integer;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmNewEntity extends HiveBaseEntity implements Serializable {

	/**
	 * 繼承的
	 */
	private static final long serialVersionUID = -5004328965492768955L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * 唯一id
	 */
	private int uid;
	private int cid;
	private String cname;
	private String cp;
	private int currCount;
	private String focusName;
	private int eqLen;
	private String desc;
	private String directors;
	private int id;
	private String initIssueTime;
	private String mainActors;
	private String name;
	private int playCount;
	private String picUrl;
	private String posterUrl;
	private double score;
	private int seriesType;
	private String streams;
	private String tagNames;
	private String time;
	private int total;
	private int type3d;
	/**
	 * entity 在列表中的位置
	 * @Fields positionId
	 */
	private int position_id ;
	private Integer source;// 标识是点播的数据还是极清的 0:点播 1:极清
	private int subject_id;
	private String subject_name;
	
	private int cpid;
	
	private int live_cp;
	
	private String live_uri;
	
	private String isvip;
	
	
	public int getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public int getCurrCount() {
		return currCount;
	}
	public void setCurrCount(int currCount) {
		this.currCount = currCount;
	}
	public String getFocusName() {
		return focusName;
	}
	public void setFocusName(String focusName) {
		this.focusName = focusName;
	}
	public int getEqLen() {
		return eqLen;
	}
	public void setEqLen(int eqLen) {
		this.eqLen = eqLen;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInitIssueTime() {
		return initIssueTime;
	}
	public void setInitIssueTime(String initIssueTime) {
		this.initIssueTime = initIssueTime;
	}
	public String getMainActors() {
		return mainActors;
	}
	public void setMainActors(String mainActors) {
		this.mainActors = mainActors;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
	public String getPosterUrl() {
		return posterUrl;
	}
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getSeriesType() {
		return seriesType;
	}
	public void setSeriesType(int seriesType) {
		this.seriesType = seriesType;
	}
	public String getStreams() {
		return streams;
	}
	public void setStreams(String streams) {
		this.streams = streams;
	}
	public String getTagNames() {
		return tagNames;
	}
	public void setTagNames(String tagNames) {
		this.tagNames = tagNames;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getType3d() {
		return type3d;
	}
	public void setType3d(int type3d) {
		this.type3d = type3d;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	
	
	public int getPosition_id() {
		return position_id;
	}
	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public int getCpid() {
		return cpid;
	}
	public void setCpid(int cpid) {
		this.cpid = cpid;
	}
	public int getLive_cp() {
		return live_cp;
	}
	public void setLive_cp(int live_cp) {
		this.live_cp = live_cp;
	}
	public String getLive_uri() {
		return live_uri;
	}
	public void setLive_uri(String live_uri) {
		this.live_uri = live_uri;
	}
	public String getIsvip() {
		return isvip;
	}
	public void setIsvip(String isvip) {
		this.isvip = isvip;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	@Override
	public String toString() {
		return "FilmNewEntity [uid=" + uid + ", cid=" + cid + ", cname="
				+ cname + ", cp=" + cp + ", currCount=" + currCount
				+ ", focusName=" + focusName + ", eqLen=" + eqLen + ", desc="
				+ desc + ", directors=" + directors + ", id=" + id
				+ ", initIssueTime=" + initIssueTime + ", mainActors="
				+ mainActors + ", name=" + name + ", playCount=" + playCount
				+ ", picUrl=" + picUrl + ", posterUrl=" + posterUrl
				+ ", score=" + score + ", seriesType=" + seriesType
				+ ", streams=" + streams + ", tagNames=" + tagNames + ", time="
				+ time + ", total=" + total + ", type3d=" + type3d
				+ ", position_id=" + position_id + ", source=" + source
				+ ", subject_id=" + subject_id + ", subject_name="
				+ subject_name + ", cpid=" + cpid + ", live_cp=" + live_cp
				+ ", live_uri=" + live_uri + ", isvip=" + isvip + "]";
	}


	

}
