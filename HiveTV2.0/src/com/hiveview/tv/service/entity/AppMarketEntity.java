package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class AppMarketEntity extends HiveBaseEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private int appId;	//应用标识
	private int seq;	//展示次序
	private int appType;//0：系统应用		 1：安装应用	
	
	/**
	 * 应用启动包名称
	 */
	private String bundleId;	
	/**
	 *  应用名称
	 */
	private String appName;		//应用名称
	private String appIcon;		//应用图标
	private String appDesc;		//应用描述
	private String tagName;		//标签 "娱乐、视频"
	/**
	 * 开发商
	 */
	private String developer;
	/**
	 * apk版本号
	 */
	private String versionNo;
	/**
	 * 文件大小
	 */
	private String appSize;
	/**
	 *  版本描述
	 */
	private String versionDesc;
	private long ctime;		//更新时间
	/**
	 * 版本路径
	 */
	private String versionUrl;
	/**
	 * md5
	 */
	private String md5;
	private String size;
	/**
	 * 设备型号
	 */
	private String model;
	/**
	 * Rom版本
	 */
	private String romVersion;
	/**
	 * 上线状态 1表示上线，0表示下线
	 */
	private String status ;
	
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getAppType() {
		return appType;
	}
	public void setAppType(int appType) {
		this.appType = appType;
	}

	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppDesc() {
		return appDesc;
	}
	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public String getVersionDesc() {
		return versionDesc;
	}
	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}
	public long getCtime() {
		return ctime;
	}
	public void setCtime(long ctime) {
		this.ctime = ctime;
	}
	public String getVersionUrl() {
		return versionUrl;
	}
	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRomVersion() {
		return romVersion;
	}
	public void setRomVersion(String romVersion) {
		this.romVersion = romVersion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
