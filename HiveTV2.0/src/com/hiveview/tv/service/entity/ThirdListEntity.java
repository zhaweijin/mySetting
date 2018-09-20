package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

/**
 * 分类筛选的每个子项
 * 
 * @ClassName: ThirdListEntity
 * @Description:
 * @author: zhangpengzhan
 * @date 2014年7月4日 上午9:41:45
 * 
 */
public class ThirdListEntity extends HiveBaseEntity {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = -5004328965492768955L;

	/**
	 * 三级分类的标识
	 * 
	 * @Fields thirdclass_id
	 */
	private int thirdclass_id;
	/**
	 * 三级分类的标识，即标签
	 * 
	 * @Fields thirdclass_name
	 */
	private String thirdclass_name;

	public void setThirdClassName(String thirdclass_name) {
		this.thirdclass_name = thirdclass_name;
	}

	public String getThirdClassName() {
		return thirdclass_name;
	}
	public void setThirdClassId(int thirdclass_id){
		this.thirdclass_id = thirdclass_id;
	}
	public int getThirdClassId(){
		return thirdclass_id;
	}

	@Override
	public String toString() {
		return "ThirdListEntity [thirdclass_id=" + thirdclass_id
				+ ", thirdclass_name=" + thirdclass_name + "]";
	}

	
}
