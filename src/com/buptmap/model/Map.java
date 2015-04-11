package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="map_info")
public class Map {
	private int map_id;
	private String floor_id;
	private String floor_chn;
	private String unit_id;
	private Double scale;
	private int map_level;
	private int max_map_level;
	private int dpi;
	private String map_path;
	private Double origin_x;
	private Double origin_y;
	private int map_style_id;
	private String style;
	private String floor_brief;
	@Id
	public int getMap_id() {
		return map_id;
	}
	public void setMap_id(int map_id) {
		this.map_id = map_id;
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
	public Double getScale() {
		return scale;
	}
	public void setScale(Double scale) {
		this.scale = scale;
	}
	public int getMap_level() {
		return map_level;
	}
	public void setMap_level(int map_level) {
		this.map_level = map_level;
	}
	public int getMax_map_level() {
		return max_map_level;
	}
	public void setMax_map_level(int max_map_level) {
		this.max_map_level = max_map_level;
	}
	public int getDpi() {
		return dpi;
	}
	public void setDpi(int dpi) {
		this.dpi = dpi;
	}
	public String getMap_path() {
		return map_path;
	}
	public void setMap_path(String map_path) {
		this.map_path = map_path;
	}
	public Double getOrigin_x() {
		return origin_x;
	}
	public void setOrigin_x(Double origin_x) {
		this.origin_x = origin_x;
	}
	public Double getOrigin_y() {
		return origin_y;
	}
	public void setOrigin_y(Double origin_y) {
		this.origin_y = origin_y;
	}
	public int getMap_style_id() {
		return map_style_id;
	}
	public void setMap_style_id(int map_style_id) {
		this.map_style_id = map_style_id;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getFloor_brief() {
		return floor_brief;
	}
	public void setFloor_brief(String floor_brief) {
		this.floor_brief = floor_brief;
	}
	
}
