package com.hiveview.tv.service.entity;

import java.util.Date;

import com.hiveview.box.framework.entity.HiveBaseEntity;

import android.annotation.SuppressLint;

public class MoviePermissionEntity extends HiveBaseEntity {
    private static final long serialVersionUID = 1L;
    /** Ӱ��id */
    private int cpId;
    /** �ƷѰ���Чʱ�� */
    private String effectiveTime;
    /** �����json�ַ��� */
    private String extJson;
    /** ��һ�ι����ʱ�� */
    private String firstPayTime = new Date().getTime() + "";

    public int getCpId() {
        return this.cpId;
    }

    public void setCpId(int cpId) {
        this.cpId = cpId;
    }

    public long getEffectiveTime() {
        if (this.effectiveTime == null || this.effectiveTime.equals("null")) {
            return -1;
        }
        return Long.valueOf(this.effectiveTime);
    }

    @SuppressLint("DefaultLocale")
    public void setEffectiveTime(String effectiveTime) {
        if (effectiveTime == null || "null".equals(effectiveTime.toLowerCase())
                || "".equals(effectiveTime)) {
            effectiveTime = "0";
        } else {
            this.effectiveTime = effectiveTime;
        }
    }

    public long getFirstPayTime() {
        return Long.valueOf(this.firstPayTime);
    }

    public void setFirstPayTime(String firstPayTime) {
        if (firstPayTime == null || "null".equals(firstPayTime)) {
            firstPayTime = new Date().getTime() + "";
        } else {
            this.firstPayTime = firstPayTime;
        }
    }

	@Override
	public String toString() {
		return "MoviePermissionEntity [cpId=" + cpId + ", effectiveTime="
				+ effectiveTime + ", extJson=" + extJson + ", firstPayTime="
				+ firstPayTime + "]";
	}
    
    
}
