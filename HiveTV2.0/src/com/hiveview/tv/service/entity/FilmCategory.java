package com.hiveview.tv.service.entity;

import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmCategory extends HiveBaseEntity {
	
	private List<FilmCategorySecondary> classThirdList; 
    private int firstclass_id; 
    private int secondclass_id; 
    private String secondclass_name;
	public List<FilmCategorySecondary> getClassThirdList() {
		return classThirdList;
	}
	public void setClassThirdList(List<FilmCategorySecondary> classThirdList) {
		this.classThirdList = classThirdList;
	}
	public int getFirstclass_id() {
		return firstclass_id;
	}
	public void setFirstclass_id(int firstclass_id) {
		this.firstclass_id = firstclass_id;
	}
	public int getSecondclass_id() {
		return secondclass_id;
	}
	public void setSecondclass_id(int secondclass_id) {
		this.secondclass_id = secondclass_id;
	}
	public String getSecondclass_name() {
		return secondclass_name;
	}
	public void setSecondclass_name(String secondclass_name) {
		this.secondclass_name = secondclass_name;
	}
	@Override
	public String toString() {
		return "FilmCategory [classThirdList=" + classThirdList
				+ ", firstclass_id=" + firstclass_id + ", secondclass_id="
				+ secondclass_id + ", secondclass_name=" + secondclass_name
				+ "]";
	}
	
}
