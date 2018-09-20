package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class AssociateWordEntity extends HiveBaseEntity {

	private static final long serialVersionUID = 954695293670676208L;
	private String word;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public String toString() {
		return "AssociateWordEntity [word=" + word + "]";
	}
	
	
	
}
