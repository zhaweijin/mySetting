package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * 
 * 热词、联想词实体
 * 
 * @author chenlixiao
 * 
 */
public class WordsEntity extends HiveBaseEntity {
	private String hotWord;

	public String getHotWord() {
		return hotWord;
	}

	public void setHotWord(String hotWord) {
		this.hotWord = hotWord;
	}

}
