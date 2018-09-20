package com.hiveview.tv.service.entity;

import java.util.ArrayList;
import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmRelatedEntity extends HiveBaseEntity {
	
	private static final long serialVersionUID = -973359795431521427L;
	private List<CastEntity> castList = new ArrayList<CastEntity>();
	private List<FilmEntity> videsetList = new ArrayList<FilmEntity>();
	public List<CastEntity> getCastList() {
		return castList;
	}
	public void setCastList(List<CastEntity> castList) {
		this.castList = castList;
	}
	public List<FilmEntity> getVidesetList() {
		return videsetList;
	}
	public void setVidesetList(List<FilmEntity> videsetList) {
		this.videsetList = videsetList;
	}
	
}
