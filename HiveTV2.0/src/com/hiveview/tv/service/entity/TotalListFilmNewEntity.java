package com.hiveview.tv.service.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class TotalListFilmNewEntity extends HiveBaseEntity  implements Serializable{

	private static final long serialVersionUID = 2575300636389401316L;
	private int recCount;
	public int getRecCount() {
		return recCount;
	}
	public void setRecCount(int recCount) {
		this.recCount = recCount;
	}
	public ArrayList<FilmNewEntity> getFilms() {
		return films;
	}
	public void setFilms(ArrayList<FilmNewEntity> films) {
		this.films = films;
	}
	private ArrayList<FilmNewEntity> films;
	

}
