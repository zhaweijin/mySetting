package com.hiveview.tv.service.entity;

import java.util.List;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * 分类筛选类型的集合实体类
 * @ClassName: ThirdListResultEntity
 * @Description: 
 * @author: zhangpengzhan
 * @date 2014年7月4日 上午9:19:59
 * 
 */
public class ThirdListResultEntity extends HiveBaseEntity {

	/**
	 * 
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -5004328965492768955L;
	
	/**
	 * 包含当前类型的数据的集合
	 * @Fields thirdListEntityList
	 */
	private List<ThirdListEntity> classThirdList;
	/**
	 * 一级分类的标识
	 * @Fields firstclass_id
	 */
	private int firstclass_id;
	/**
	 * 二级分类的标识
	 * @Fields secondclass_id
	 */
	private int secondclass_id;
	/**
	 * 二级分类的名称
	 * @Fields secondclass_name
	 */
	private String secondclass_name;
	

	
	public void setClassThirdList( List<ThirdListEntity> classThirdList){
		this.classThirdList = classThirdList;
	}
	public List<ThirdListEntity> getClassThirdList(){
		return classThirdList;
	}
	
	public void setFirstClassId(int firstclass_id){
		this.firstclass_id = firstclass_id;
	}
	public int getFirstClassId(){
		return firstclass_id;
	}
	public void setSecondClassName(String secondclass_name){
		this.secondclass_name = secondclass_name;
	}
	public String setSecondClassName(){
		return secondclass_name;
	}
	public void setSecondClassId(int secondclass_id){
		this.secondclass_id = secondclass_id;
	}
	public int getSecondClassId(){
		return secondclass_id;
	}
	@Override
	public String toString() {
		return "ThirdListResultEntity [classThirdList=" + classThirdList
				+ ", firstclass_id=" + firstclass_id + ", secondclass_id="
				+ secondclass_id + ", secondclass_name=" + secondclass_name
				+ "]";
	}
	
}
