package com.buptmap.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_poi_change")
public class PoiChange {
	private int poiChange_id;
	private String poi_id;
	private String unit_id;
	private int has_indoor_map;
	private String creator;
	private Date create_time;
	private int action;
	private int checked;
	
	@Id
	@GeneratedValue
	public int getPoiChange_id() {
		return poiChange_id;
	}
	public void setPoiChange_id(int poiChange_id) {
		this.poiChange_id = poiChange_id;
	}
	public String getPoi_id() {
		return poi_id;
	}
	public void setPoi_id(String poi_id) {
		this.poi_id = poi_id;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public int getHas_indoor_map() {
		return has_indoor_map;
	}
	public void setHas_indoor_map(int has_indoor_map) {
		this.has_indoor_map = has_indoor_map;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	
}
