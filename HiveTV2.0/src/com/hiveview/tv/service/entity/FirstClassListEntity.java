package com.hiveview.tv.service.entity;

import com.hiveview.box.framework.entity.HiveBaseEntity;

public class FirstClassListEntity extends HiveBaseEntity {

	/**
	 * @Fields serialVersionUID:TODO
	 */
	private static final long serialVersionUID = 8584055024959497908L;
	private int firstclass_id;
	private String firstclass_name;
	private String pic;
	private String icon;
	private int show_type;

	public int getFirstclass_id() {
		return firstclass_id;
	}

	public void setFirstclass_id(int firstclass_id) {
		this.firstclass_id = firstclass_id;
	}

	public String getFirstclass_name() {
		return firstclass_name;
	}

	public void setFirstclass_name(String firstclass_name) {
		this.firstclass_name = firstclass_name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getShow_type() {
		return show_type;
	}

	public void setShow_type(int show_type) {
		this.show_type = show_type;
	}

	@Override
	public String toString() {
		return "FirstClassListEntity [firstclass_id=" + firstclass_id + ", firstclass_name=" + firstclass_name + ", pic=" + pic + ", icon=" + icon
				+ ", show_type=" + show_type + "]";
	}



}
