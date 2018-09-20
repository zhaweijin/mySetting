package com.hiveview.tv.service.entity;

import org.json.JSONArray;

import com.hiveview.box.framework.entity.HiveBaseEntity;


/**
 * 电视频道的节目实体
 * 
 * @author chenlixiao
 * 
 */
public class ProgramEntity extends HiveBaseEntity {
	/**
	 * 节目名称
	 */
	protected String name;
	/**
	 * 日期
	 */
	protected String date;
	/**
	 * 开始时间
	 */
	protected String start_time;
	/**
	 * 结束时间
	 */
	protected String end_time;
	/**
	 * id号
	 */
	protected String wiki_id;
	/**
	 * 海报图 url
	 */
	protected Object wiki_cover;
	/**
	 * 标签 类型
	 */
	protected JSONArray tags;
	/**
	 * 
	 */
	protected String hasvideo;
	/**
	 * 不知道是什么
	 */
	protected String source;

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

	public String getStart_time() {
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

	public Object getWiki_cover() {
		return wiki_cover;
	}

	public void setWiki_cover(Object wiki_cover) {
		this.wiki_cover = wiki_cover;
	}

	public JSONArray getTags() {
		return tags;
	}

	public void setTags(JSONArray tags) {
		this.tags = tags;
	}

	public String getHasvideo() {
		return hasvideo;
	}

	public void setHasvideo(String hasvideo) {
		this.hasvideo = hasvideo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		return "ProgramEntity [name=" + name + ", date=" + date
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", wiki_id=" + wiki_id + ", wiki_cover=" + wiki_cover
				+ ", tags=" + tags + ", hasvideo=" + hasvideo + ", source="
				+ source + "]";
	}

	
}
