package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class QiyiFilmRecommandEntity extends HiveBaseEntity {
	private String albumId;
	private String albumName;
	private int playLength;
	private String issueTime;
	private String mainActor;
	private String albumDesc;
	private String albumPic;
	private String directors;
	private String tag;

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public int getPlayLength() {
		return playLength;
	}

	public void setPlayLength(int playLength) {
		this.playLength = playLength;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public String getMainActor() {
		return mainActor;
	}

	public void setMainActor(String mainActor) {
		this.mainActor = mainActor;
	}

	public String getAlbumDesc() {
		return albumDesc;
	}

	public void setAlbumDesc(String albumDesc) {
		this.albumDesc = albumDesc;
	}

	public String getAlbumPic() {
		return albumPic;
	}

	public void setAlbumPic(String albumPic) {
		this.albumPic = albumPic;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	

}
