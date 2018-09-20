package com.hiveview.cloudtv.settings.upload;

import java.util.List;

import android.content.IntentSender.SendIntentException;

public class DeviceInfo {
	private String mac;
	private String sn;
	private String model;
	private String romVersion;
	private String romBuildData;
	private List<String> sendApkInfoList;
	private String userId;//暂时传null
	private String loginDate;//暂时传null
	private List<ApkInfo> apkInfoList;
	private String totalSize;//精确到M
	private String unUsedSize;//M
	private String unHoldSize;//M
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
	public String getRomBuildData() {
		return romBuildData;
	}
	public void setRomBuildData(String romBuildData) {
		this.romBuildData = romBuildData;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public List<ApkInfo> getApkInfoList() {
		return apkInfoList;
	}
	public void setApkInfoList(List<ApkInfo> apkInfoList) {
		this.apkInfoList = apkInfoList;
	}
	public List<String> getSendApkInfoList() {
		return sendApkInfoList;
	}
	public void setSendApkInfoList(List<String> sendApkInfoList) {
		this.sendApkInfoList = sendApkInfoList;
	}
	public String getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}
	public String getUnUsedSize() {
		return unUsedSize;
	}
	public void setUnUsedSize(String unUsedSize) {
		this.unUsedSize = unUsedSize;
	}
	public String getUnHoldSize() {
		return unHoldSize;
	}
	public void setUnHoldSize(String unHoldSize) {
		this.unHoldSize = unHoldSize;
	}
	
	
	
	
	

}
