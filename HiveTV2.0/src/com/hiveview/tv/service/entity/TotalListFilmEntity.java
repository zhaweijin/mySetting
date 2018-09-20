package com.hiveview.tv.service.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class TotalListFilmEntity extends HiveBaseEntity  implements Serializable{

	private static final long serialVersionUID = 2575300636389401316L;
	private int recCount;
	public int getRecCount() {
		return recCount;
	}
	public void setRecCount(int recCount) {
		this.recCount = recCount;
	}
	public ArrayList<FilmEntity> getFilms() {
		return films;
	}
	public void setFilms(ArrayList<FilmEntity> films) {
		this.films = films;
	}
	private ArrayList<FilmEntity> films;
	

}
