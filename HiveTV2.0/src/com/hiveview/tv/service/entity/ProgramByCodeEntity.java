package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;


/**
 * 电视频道的节目实体
 * 
 * @author chenlixiao
 * 
 */
public class ProgramByCodeEntity extends HiveBaseEntity {
	private String channel_code;
	private String channel_name;
	private String channel_logo;
	private String name;
	private String date;
	private String start_time;
	private String end_time;
	private String wiki_id;
	private String wiki_cover;
	private Object tags;
	private String next_name;
	private String next_date;
	private String next_start_time;
	
	public String getChannel_code() {
		return channel_code;
	}
	public void setChannel_code(String channel_code) {
		this.channel_code = channel_code;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getChannel_logo() {
		return channel_logo;
	}
	public void setChannel_logo(String channel_logo) {
		this.channel_logo = channel_logo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Object getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getWiki_id() {
		return wiki_id;
	}
	public void setWiki_id(String wiki_id) {
		this.wiki_id = wiki_id;
	}
	public String getWiki_cover() {
		return wiki_cover;
	}
	public void setWiki_cover(String wiki_cover) {
		this.wiki_cover = wiki_cover;
	}
	public Object getTags() {
		return tags;
	}
	public void setTags(Object tags) {
		this.tags = tags;
	}
	public String getNext_name() {
		return next_name;
	}
	public void setNext_name(String next_name) {
		this.next_name = next_name;
	}
	public String getNext_date() {
		return next_date;
	}
	public void setNext_date(String next_date) {
		this.next_date = next_date;
	}
	public String getNext_start_time() {
		return next_start_time;
	}
	public void setNext_start_time(String next_start_time) {
		this.next_start_time = next_start_time;
	}

	
	
}
