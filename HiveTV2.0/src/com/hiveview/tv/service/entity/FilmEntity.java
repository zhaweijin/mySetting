package com.hiveview.tv.service.entity;

import java.io.Serializable;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FilmEntity extends HiveBaseEntity implements Serializable{

	/**
	 * 繼承的
	 */
	private static final long serialVersionUID = -5004328965492768955L;
	/**
	 * 一下是点播列表里边项目点击进去的窗口的
	 * viewpager 的实体类 以时尚 生活为例子
	 */
	/**
	 * 视频的id 例如：383895
	 */
	private int videoset_id;
	/**
	 * 视频的类型   例如：17
	 */
	private int videoset_type;
	/**
	 * 视频的名字 例如：魅惑花瓣眼 Emporio Armani2012秋冬时装秀后台彩妆花絮 
	 * 需要显示的内容啊
	 */
	private String videoset_name;
	/**
	 * 视频缩略图的地址   viewpager显示的图片
	 */
	private String videoset_img;
	/**
	 * 奇异给的缩略图  例子：这个显示不错来啊
	 */
	private String videoset_tv_img;
	/**
	 * 视频的标签 例如："欧洲,彩妆,意大利,春夏,米兰,趋势报道,美容,秀场
	 */
	private String tag;
	/**
	 * 一个时间  例如：30130416 暂时没用到
	 * 
	 */
	private String is_suetime;
	/**
	 * 主管  例子传入值为空
	 */
	private String director;
	/**
	 * 演员  例子为空
	 */
	private String actors;
	/**
	 * 简报  例如：魅惑花瓣眼 Emporio Armani2012秋冬时装秀后台彩妆花絮
	 */
	private String videoset_brief;
	/**
	 * 时长  单位是 秒 例如：118
	 * 需要显示的内容啊
	 */
	private int time_length;
	/**
	 * 总是  例如：1
	 */
	private int  videoset_total;
	/**
	 * 描述 例如：后台彩妆花絮  需要显示的内容啊
	 */
	private String videoset_focus;
	/**
	 * 年份   例如：3013  这是嘛啊  穿越啊
	 */
	private String years;
	/**
	 * 传入为空
	 */
	private String twodim_code;
	/**
	 * 自营或者是奇艺 例如：0或者1
	 */
	private int cp;
	/**
	 * 一个二维的数组样式  例如：628240,394767
	 * qiyiTV专辑ID,qiyiVRS专辑ID
	 */
	private String cp_videoset_id;
	/**
	 *  评分
	 */
	private String socre;
	/**
	 *播放次数 例如：87894
	 */
	private int play_count;
	/**
	 * 是不是3d 例如：0 表示不是
	 */
	private int is3D;
	/**
	 *  二维码
	 */
	private int isSeries;
	/**
	 *更新到的集数
	 */
	private int videoset_update;
	/**
	 * 又是一个空值  例如：“”
	 */
	private String data_rate;
	
	/**
	 * entity 在列表中的位置
	 * @Fields positionId
	 */
	private int position_id ;
	private Integer source;// 标识是点播的数据还是极清的 0:点播 1:极清
	private int subject_id;
	private String subject_name;
	public int getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
	
	/**
	 * 唯一id
	 */
	private int uid;
	
	/**
	 * 奇艺的播放器需要的这两个数据去定位一个视频源
	 * @Fields vrsAlbumId
	 */
	private String vrsAlbumId ;
	private String vrsTvId ;
	public int getPositionId(){
		return position_id;
	}
	
	public void setPositionId(int position_id){
		this.position_id = position_id;
	}
	
	public String getVrsAlbumId() {
		return vrsAlbumId;
	}
	public void setVrsAlbumId(String vrsAlbumId) {
		this.vrsAlbumId = vrsAlbumId;
	}
	public String getVrsTvId() {
		return vrsTvId;
	}
	public void setVrsTvId(String vrsTvId) {
		this.vrsTvId = vrsTvId;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getVideoset_id() {
		return videoset_id;
	}
	public void setVideoset_id(int videoset_id) {
		this.videoset_id = videoset_id;
	}
	public int getVideoset_type() {
		return videoset_type;
	}
	public void setVideoset_type(int videoset_type) {
		this.videoset_type = videoset_type;
	}
	public String getVideoset_name() {
		return videoset_name;
	}
	public void setVideoset_name(String videoset_name) {
		this.videoset_name = videoset_name;
	}
	public String getVideoset_img() {
		return videoset_img;
	}
	public void setVideoset_img(String videoset_img) {
		this.videoset_img = videoset_img;
	}
	public String getVideoset_tv_img() {
		return videoset_tv_img;
	}
	public void setVideoset_tv_img(String videoset_tv_img) {
		this.videoset_tv_img = videoset_tv_img;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getIs_suetime() {
		return is_suetime;
	}
	public void setIs_suetime(String is_suetime) {
		this.is_suetime = is_suetime;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getVideoset_brief() {
		return videoset_brief;
	}
	public void setVideoset_brief(String videoset_brief) {
		this.videoset_brief = videoset_brief;
	}
	public int getTime_length() {
		return time_length;
	}
	public void setTime_length(int time_length) {
		this.time_length = time_length;
	}
	public int getVideoset_total() {
		return videoset_total;
	}
	public void setVideoset_total(int videoset_total) {
		this.videoset_total = videoset_total;
	}
	public String getVideoset_focus() {
		return videoset_focus;
	}
	public void setVideoset_focus(String videoset_focus) {
		this.videoset_focus = videoset_focus;
	}
	public String getYears() {
		return years;
	}
	public void setYears(String years) {
		this.years = years;
	}
	public String getTwodim_code() {
		return twodim_code;
	}
	public void setTwodim_code(String twodim_code) {
		this.twodim_code = twodim_code;
	}
	public int getCp() {
		return cp;
	}
	public void setCp(int cp) {
		this.cp = cp;
	}
	public String getCp_videoset_id() {
		return cp_videoset_id;
	}
	public void setCp_videoset_id(String cp_videoset_id) {
		this.cp_videoset_id = cp_videoset_id;
	}
	public String getSocre() {
		return socre;
	}
	public void setSocre(String socre) {
		this.socre = socre;
	}
	public int getPlay_count() {
		return play_count;
	}
	public void setPlay_count(int play_count) {
		this.play_count = play_count;
	}
	public int getIs3D() {
		return is3D;
	}
	public void setIs3D(int is3d) {
		is3D = is3d;
	}
	public int getIsSeries() {
		return isSeries;
	}
	public void setIsSeries(int isSeries) {
		this.isSeries = isSeries;
	}
	public int getVideoset_update() {
		return videoset_update;
	}
	public void setVideoset_update(int videoset_update) {
		this.videoset_update = videoset_update;
	}
	public String getData_rate() {
		return data_rate;
	}
	public void setData_rate(String data_rate) {
		this.data_rate = data_rate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getPosition_id() {
		return position_id;
	}
	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	@Override
	public String toString() {
		return "FilmEntity [videoset_id=" + videoset_id + ", videoset_type=" + videoset_type + ", videoset_name=" + videoset_name + ", videoset_img="
				+ videoset_img + ", videoset_tv_img=" + videoset_tv_img + ", tag=" + tag + ", is_suetime=" + is_suetime + ", director=" + director
				+ ", actors=" + actors + ", videoset_brief=" + videoset_brief + ", time_length=" + time_length + ", videoset_total=" + videoset_total
				+ ", videoset_focus=" + videoset_focus + ", years=" + years + ", twodim_code=" + twodim_code + ", cp=" + cp + ", cp_videoset_id="
				+ cp_videoset_id + ", socre=" + socre + ", play_count=" + play_count + ", is3D=" + is3D + ", isSeries=" + isSeries
				+ ", videoset_update=" + videoset_update + ", data_rate=" + data_rate + ", position_id=" + position_id + ", source=" + source
				+ ", subject_id=" + subject_id + ", subject_name=" + subject_name + ", uid=" + uid + ", vrsAlbumId=" + vrsAlbumId + ", vrsTvId="
				+ vrsTvId + "]";
	}





}
