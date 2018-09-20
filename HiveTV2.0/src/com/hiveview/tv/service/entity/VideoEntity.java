package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class VideoEntity extends HiveBaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 155319625585392553L;
	private int sequence; 
	private int video_id; 
	private String video_name; 
	private String video_img; 
	private String video_focus; 
	private String video_brief; 
	private int year; 
	private String keyword; 
	private int video_type; 
	private String season; 
	private String phase;
	private int content_type;
	private String cp_video_id;
	private String cp_videoset_id;
	private int  videoset_id;

	public int getVideoset_id(){
		return videoset_id;
	}
	
	public void setVideoset_id(int  videoset_id){
		this.videoset_id = videoset_id;
	}
	
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getVideo_id() {
		return video_id;
	}
	public void setVideo_id(int video_id) {
		this.video_id = video_id;
	}
	public String getVideo_name() {
		return video_name;
	}
	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}
	public String getVideo_img() {
		return video_img;
	}
	public void setVideo_img(String video_img) {
		this.video_img = video_img;
	}
	public String getVideo_focus() {
		return video_focus;
	}
	public void setVideo_focus(String video_focus) {
		this.video_focus = video_focus;
	}
	public String getVideo_brief() {
		return video_brief;
	}
	public void setVideo_brief(String video_brief) {
		this.video_brief = video_brief;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getVideo_type() {
		return video_type;
	}
	public void setVideo_type(int video_type) {
		this.video_type = video_type;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public int getContent_type() {
		return content_type;
	}
	public void setContent_type(int content_type) {
		this.content_type = content_type;
	}
	
	
	public String getCp_video_id() {
		return cp_video_id;
	}
	public void setCp_video_id(String cp_video_id) {
		this.cp_video_id = cp_video_id;
	}
	
	
	public String getCp_videoset_id() {
		return cp_videoset_id;
	}
	public void setCp_videoset_id(String cp_videoset_id) {
		this.cp_videoset_id = cp_videoset_id;
	}
	@Override
	public String toString() {
		return "VideoEntity [sequence=" + sequence + ", video_id=" + video_id
				+ ", video_name=" + video_name + ", video_img=" + video_img
				+ ", video_focus=" + video_focus + ", video_brief="
				+ video_brief + ", year=" + year + ", keyword=" + keyword
				+ ", video_type=" + video_type + ", season=" + season
				+ ", phase=" + phase + ", content_type=" + content_type + "]";
	}
	
}
