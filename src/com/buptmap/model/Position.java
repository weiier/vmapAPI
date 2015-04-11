package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="position_info")
public class Position {
	private String indoor_id;
	private Double scale;
	private Integer dpi;
	private Double coord_x;
	private Double coord_y;
	private String floor_id;
	private String floor_chn;
	private String unit_id;
	private String parent_unit_id;
	private String poi_id;
	private String frame;
	private String name;
	private String parent_name;
	private String type;
	private int has_indoor_map;
	private String parent_poi_id;
	
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Id
	public String getIndoor_id() {
		return indoor_id;
	}
	public void setIndoor_id(String indoor_id) {
		this.indoor_id = indoor_id;
	}
	public Double getScale() {
		return scale;
	}
	public void setScale(Double scale) {
		this.scale = scale;
	}
	public Integer getDpi() {
		return dpi;
	}
	public void setDpi(Integer dpi) {
		this.dpi = dpi;
	}
	public Double getCoord_x() {
		return coord_x;
	}
	public void setCoord_x(Double coord_x) {
		this.coord_x = coord_x;
	}
	public Double getCoord_y() {
		return coord_y;
	}
	public void setCoord_y(Double coord_y) {
		this.coord_y = coord_y;
	}
	public String getFloor_id() {
		return floor_id;
	}
	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}
	public String getFloor_chn() {
		return floor_chn;
	}
	public void setFloor_chn(String floor_chn) {
		this.floor_chn = floor_chn;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getParent_unit_id() {
		return parent_unit_id;
	}
	public void setParent_unit_id(String parent_unit_id) {
		this.parent_unit_id = parent_unit_id;
	}
	public String getPoi_id() {
		return poi_id;
	}
	public void setPoi_id(String poi_id) {
		this.poi_id = poi_id;
	}
	public int getHas_indoor_map() {
		return has_indoor_map;
	}
	public void setHas_indoor_map(int has_indoor_map) {
		this.has_indoor_map = has_indoor_map;
	}
	public String getParent_poi_id() {
		return parent_poi_id;
	}
	public void setParent_poi_id(String parent_poi_id) {
		this.parent_poi_id = parent_poi_id;
	}
	
}
