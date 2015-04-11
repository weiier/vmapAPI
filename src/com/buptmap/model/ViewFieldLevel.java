package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="view_field_level")
public class ViewFieldLevel {
	private String id;
	private String view_name;
	private String field_level;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Id
	public String getView_name() {
		return view_name;
	}
	public void setView_name(String view_name) {
		this.view_name = view_name;
	}
	public String getField_level() {
		return field_level;
	}
	public void setField_level(String field_level) {
		this.field_level = field_level;
	}
	
}
