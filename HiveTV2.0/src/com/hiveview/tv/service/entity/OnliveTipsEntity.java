package com.hiveview.tv.service.entity;

public class OnliveTipsEntity extends ProgramEntity {
	
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 台标url
	 */
	private String televisionLogoUrl;
	/**
	 * 台标名称
	 */
	private String televisionName;
	
	/**
	 * 直播的时间
	 */
	private String tip_time;

	public String getTip_time() {
		return tip_time;
	}

	public void setTip_time(String tip_time) {
		this.tip_time = tip_time;
	}

	public String getTelevisionName() {
		return televisionName;
	}

	public void setTelevisionName(String televisionName) {
		this.televisionName = televisionName;
	}

	public String getTelevisionLogoUrl() {
		return televisionLogoUrl;
	}

	public void setTelevisionLogoUrl(String televisionLogoUrl) {
		this.televisionLogoUrl = televisionLogoUrl;
	}

	@Override
	public String toString() {
		return "OnliveTipsEntity [televisionLogoUrl=" + televisionLogoUrl
				+ ", name=" + name + ", date=" + date + ", start_time="
				+ start_time + ", end_time=" + end_time + ", wiki_id="
				+ wiki_id + ", wiki_cover=" + wiki_cover + ", tags=" + tags
				+ ", hasvideo=" + hasvideo + ", source=" + source + "]";
	}
	
}
