package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="floor_info")
public class Floor {
	private String unit_id;
	private String floor_id;
	private String frame;
	private int floor_num;
	private String floor_chn;
	private String floor_alias;
	private String floor_brief;
	private String description;
	private String name;
	@Id
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getFloor_id() {
		return floor_id;
	}
	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}
	public int getFloor_num() {
		return floor_num;
	}
	public void setFloor_num(int floor_num) {
		this.floor_num = floor_num;
	}
	public String getFloor_chn() {
		return floor_chn;
	}
	public void setFloor_chn(String floor_chn) {
		this.floor_chn = floor_chn;
	}
	public String getFloor_alias() {
		return floor_alias;
	}
	public void setFloor_alias(String floor_alias) {
		this.floor_alias = floor_alias;
	}
	public String getFloor_brief() {
		return floor_brief;
	}
	public void setFloor_brief(String floor_brief) {
		this.floor_brief = floor_brief;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	
}
