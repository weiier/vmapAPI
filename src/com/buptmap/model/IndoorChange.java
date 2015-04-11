package com.buptmap.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="tb_indoor_change")
public class IndoorChange {
	private int indoorChange_id;
	private String indoor_id;
	private String floor_id;
	private Double coord_x;
	private Double coord_y;
	private String booth_num;
	private String frame;
	private String address;
	private String type;
	private int available;
	private Date create_time;
	private String creator;
	private String unit_id;
	private String parent_unit_id;
	private String poi_id;
	private String parent_poi_id;
	private Double max_x;
	private Double max_y;
	private Double min_x;
	private Double min_y;
	private int action;
	private int checked;
	private String last_modify_time;
	@Id
	@GeneratedValue
	public int getIndoorChange_id() {
		return indoorChange_id;
	}
	public void setIndoorChange_id(int indoorChange_id) {
		this.indoorChange_id = indoorChange_id;
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
	public String getIndoor_id() {
		return indoor_id;
	}
	public void setIndoor_id(String indoor_id) {
		this.indoor_id = indoor_id;
	}
	public String getFloor_id() {
		return floor_id;
	}
	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
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
	public String getBooth_num() {
		return booth_num;
	}
	public void setBooth_num(String booth_num) {
		this.booth_num = booth_num;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}

	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
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
	public String getParent_poi_id() {
		return parent_poi_id;
	}
	public void setParent_poi_id(String parent_poi_id) {
		this.parent_poi_id = parent_poi_id;
	}
	public Double getMax_x() {
		return max_x;
	}
	public void setMax_x(Double max_x) {
		this.max_x = max_x;
	}
	public Double getMax_y() {
		return max_y;
	}
	public void setMax_y(Double max_y) {
		this.max_y = max_y;
	}
	public Double getMin_x() {
		return min_x;
	}
	public void setMin_x(Double min_x) {
		this.min_x = min_x;
	}
	public Double getMin_y() {
		return min_y;
	}
	public void setMin_y(Double min_y) {
		this.min_y = min_y;
	}
	public String getLast_modify_time() {
		return last_modify_time;
	}
	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}
	
	
}
