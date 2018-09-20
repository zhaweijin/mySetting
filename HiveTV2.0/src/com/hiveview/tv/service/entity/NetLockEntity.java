package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class NetLockEntity extends HiveBaseEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 错误码
	 */
	private String code;
	/**
	 * long型时间
	 */
	private String longTime;
	/**
	 * String型时间
	 */
	private String stringTime;
	/**
	 * 返回信息
	 */
	private String message;
	/**
	 * 返回值
	 */
	private boolean result;
	/**
	 * 缓存
	 */
	private boolean cached;
	/**
	 * 缓存时间
	 */
	private String cachedTime;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLongTime() {
		return longTime;
	}
	public void setLongTime(String longTime) {
		this.longTime = longTime;
	}
	public String getStringTime() {
		return stringTime;
	}
	public void setStringTime(String stringTime) {
		this.stringTime = stringTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public boolean isCached() {
		return cached;
	}
	public void setCached(boolean cached) {
		this.cached = cached;
	}
	public String getCachedTime() {
		return cachedTime;
	}
	public void setCachedTime(String cachedTime) {
		this.cachedTime = cachedTime;
	}
	
	
}
