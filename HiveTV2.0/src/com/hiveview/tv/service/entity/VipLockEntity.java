package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

@SuppressWarnings("serial")
public class VipLockEntity extends HiveBaseEntity{

	private Boolean Result;
	
	private String ErrorMessage;
	
	private boolean Status;
	
	private String DamaiID;

	public Boolean getResult() {
		return Result;
	}

	public void setResult(Boolean result) {
		Result = result;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public String getDamaiID() {
		return DamaiID;
	}

	public void setDamaiID(String damaiID) {
		DamaiID = damaiID;
	}

	@Override
	public String toString() {
		return "VipLockEntity [Result=" + Result + ", ErrorMessage="
				+ ErrorMessage + ", Status=" + Status + ", DamaiID=" + DamaiID
				+ "]";
	}
	
	
}
