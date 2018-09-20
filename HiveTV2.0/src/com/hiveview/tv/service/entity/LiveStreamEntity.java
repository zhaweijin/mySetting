package com.hiveview.tv.service.entity;


import com.hiveview.box.framework.entity.HiveBaseEntity;

public class LiveStreamEntity extends HiveBaseEntity {


	private int tv_id;

	private String tv_name;
	
	private String tv_pyname;
	
	private String tvlogo;
	
	private String liveurl;
	
	private int cp;
	
	private int isVip;
	
	private int cpId;
	
	private int isFreeLimit  ;
	
	public int getTv_id() {
		return tv_id;
	}

	public void setTv_id(int tv_id) {
		this.tv_id = tv_id;
	}

	public String getTv_name() {
		return tv_name;
	}

	public void setTv_name(String tv_name) {
		this.tv_name = tv_name;
	}

	public String getTv_pyname() {
		return tv_pyname;
	}

	public void setTv_pyname(String tv_pyname) {
		this.tv_pyname = tv_pyname;
	}

	public String getTvlogo() {
		return tvlogo;
	}

	public void setTvlogo(String tvlogo) {
		this.tvlogo = tvlogo;
	}

	public String getLiveurl() {
		return liveurl;
	}

	public void setLiveurl(String liveurl) {
		this.liveurl = liveurl;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public int getIsVip() {
		return isVip;
	}

	public void setIsVip(int isVip) {
		this.isVip = isVip;
	}

	public int getCpId() {
		return cpId;
	}

	public void setCpId(int cpId) {
		this.cpId = cpId;
	}

	public int getIsFreeLimit() {
		return isFreeLimit;
	}

	public void setIsFreeLimit(int isFreeLimit) {
		this.isFreeLimit = isFreeLimit;
	}

	@Override
	public String toString() {
		return "LiveStreamEntity [tv_id=" + tv_id + ", tv_name=" + tv_name
				+ ", tv_pyname=" + tv_pyname + ", tvlogo=" + tvlogo
				+ ", liveurl=" + liveurl + ", cp=" + cp + ", isVip=" + isVip
				+ ", cpId=" + cpId + ", isFreeLimit=" + isFreeLimit + "]";
	}



}
