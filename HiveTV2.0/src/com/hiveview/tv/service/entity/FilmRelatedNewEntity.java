package com.hiveview.tv.service.entity;

import java.util.ArrayList;
import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmRelatedNewEntity extends HiveBaseEntity {
	
	private static final long serialVersionUID = -973359795431521427L;
	private List<CastEntity> castList = new ArrayList<CastEntity>();
	private List<FilmNewEntity> videsetList = new ArrayList<FilmNewEntity>();
	public List<CastEntity> getCastList() {
		return castList;
	}
	public void setCastList(List<CastEntity> castList) {
		this.castList = castList;
	}
	public List<FilmNewEntity> getVidesetList() {
		return videsetList;
	}
	public void setVidesetList(List<FilmNewEntity> videsetList) {
		this.videsetList = videsetList;
	}
	
}
