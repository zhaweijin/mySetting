package com.hiveview.tv.service.entity;

import android.R.integer;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class PlayerRecordEntity extends HiveBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5004328965492768955L;

	/**
	 * 一下数据以电视剧《男媒婆》为例子
	 */
	/**
	 * 专辑id 一个很长的字符串
	 * 
	 */
	private String albumId;

	/**
	 * 专辑电视剧的名字 “男媒婆”
	 */
	private String albumName;

	/**
	 * 专辑海报封面的图片
	 */
	private String albumPhotoName;

	/**
	 * 。。。。得到的是com.qiyi.video.sdk.model.QYTVImage@43e2d968 图片 奇异做了封装
	 */
	private String image;

	/**
	 * 名字“男媒婆”
	 */
	private String name;

	/**
	 * 开始时间 ，一个整型的数据为秒数 “1276”
	 */
	private int startTime;

	/**
	 * 播放器传入这两个就能接着上次的时间点播放
	 */
	/**
	 * id “200202002”
	 */
	private String vrsAlbumId;

	/**
	 * id “221144000”
	 */
	private String vrsTvId;
	// =============================================//

	/**
	 * 播放的时间 用于区别今天昨天后天
	 */
	private long palyerDate;

	/**
	 * 区别横图(false) 竖图 (true)
	 */
	private String orientation;

	/**
	 * 已播放的时间数
	 */
	private String surplusTime;

	/**
	 * 总时间数
	 */
	private String duration;

	/**
	 * 电视剧 当前播放的集数 综艺的话是第几期
	 */
	private String albums;

	private int source;

	/**
	 * 看点描述
	 */
	private String description;

	/**
	 * 是否是假数据
	 */
	private boolean isShow = true;

	/**
	 * 那一天的记录 今天1 昨天2 后天3
	 */
	private String witchDay;

	private int cp;

	private int videoset_type;
	/**
	 * 频道列表播放剧集
	 */
	private String currentEpisode;
	private FilmNewEntity entity;
	
	private String Liveurl;
	
	private int isVip;
	
	private int cpId;
	
	private long ShowDate;
	
	
	

	public int getVideoset_type() {
		return videoset_type;
	}

	public void setVideoset_type(int videoset_type) {
		this.videoset_type = videoset_type;
	}

	public String getWitchDay() {
		return witchDay;
	}

	public void setWitchDay(String witchDay) {
		this.witchDay = witchDay;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumPhotoName() {
		return albumPhotoName;
	}

	public void setAlbumPhotoName(String albumPhotoName) {
		this.albumPhotoName = albumPhotoName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public String getVrsAlbumId() {
		return vrsAlbumId;
	}

	public void setVrsAlbumId(String vrsAlbumId) {
		this.vrsAlbumId = vrsAlbumId;
	}

	public String getVrsTvId() {
		return vrsTvId;
	}

	public void setVrsTvId(String vrsTvId) {
		this.vrsTvId = vrsTvId;
	}

	public long getPalyerDate() {
		return palyerDate;
	}

	public void setPalyerDate(long palyerDate) {
		this.palyerDate = palyerDate;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getSurplusTime() {
		return surplusTime;
	}

	public void setSurplusTime(String surplusTime) {
		this.surplusTime = surplusTime;
	}

	public String getAlbums() {
		return albums;
	}

	public void setAlbums(String albums) {
		this.albums = albums;
	}

	public String getCurrentEpisode() {
		return currentEpisode;
	}

	public void setCurrentEpisode(String currentEpisode) {
		this.currentEpisode = currentEpisode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public FilmNewEntity getEntity() {
		return entity;
	}

	public void setEntity(FilmNewEntity entity) {
		this.entity = entity;
	}

	public String getLiveurl() {
		return Liveurl;
	}

	public void setLiveurl(String liveurl) {
		Liveurl = liveurl;
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

	public long getShowDate() {
		return ShowDate;
	}

	public void setShowDate(long showDate) {
		ShowDate = showDate;
	}

	@Override
	public String toString() {
		return "PlayerRecordEntity [albumId=" + albumId + ", albumName="
				+ albumName + ", albumPhotoName=" + albumPhotoName + ", image="
				+ image + ", name=" + name + ", startTime=" + startTime
				+ ", vrsAlbumId=" + vrsAlbumId + ", vrsTvId=" + vrsTvId
				+ ", palyerDate=" + palyerDate + ", orientation=" + orientation
				+ ", surplusTime=" + surplusTime + ", duration=" + duration
				+ ", albums=" + albums + ", source=" + source
				+ ", description=" + description + ", isShow=" + isShow
				+ ", witchDay=" + witchDay + ", cp=" + cp + ", videoset_type="
				+ videoset_type + ", currentEpisode=" + currentEpisode
				+ ", entity=" + entity + ", Liveurl=" + Liveurl + ", isVip="
				+ isVip + ", cpId=" + cpId + ", ShowDate=" + ShowDate + "]";
	}

	

}
