package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="unit_info")
public class Place {
	private String unit_id;
	private String name;
	private int unit_brand_id;
	private String unit_brand_name;
	private int unit_type_id;
	private String logo;
	private String type;
	private String phone;
	private String email;
	private String website;
	private String address;
	private String description;
	private String keyword;
	private String opening_time;
	private String detail_info;
	private String outdoor_id;
	private int province_id;
	private String province_name;
	private int city_id;
	private String city_name;
	private int dist_id;
	private String dist_name;
	private int bdist_id;
	private String bdist_name;
	private String alias;
	private String show_name;
	private String zip_code;
	private double latitude;
	private double longitude;
	private String frame;
	private int has_indoor_map;
	private String last_modify_time;
	private int available;
	@Id
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getShow_name() {
		return show_name;
	}
	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getUnit_brand_id() {
		return unit_brand_id;
	}
	public void setUnit_brand_id(int unit_brand_id) {
		this.unit_brand_id = unit_brand_id;
	}
	public String getUnit_brand_name() {
		return unit_brand_name;
	}
	public void setUnit_brand_name(String unit_brand_name) {
		this.unit_brand_name = unit_brand_name;
	}
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOutdoor_id() {
		return outdoor_id;
	}
	public void setOutdoor_id(String outdoor_id) {
		this.outdoor_id = outdoor_id;
	}
	public int getHas_indoor_map() {
		return has_indoor_map;
	}
	public void setHas_indoor_map(int has_indoor_map) {
		this.has_indoor_map = has_indoor_map;
	}
	public int getProvince_id() {
		return province_id;
	}
	public void setProvince_id(int province_id) {
		this.province_id = province_id;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public int getDist_id() {
		return dist_id;
	}
	public void setDist_id(int dist_id) {
		this.dist_id = dist_id;
	}
	public String getDist_name() {
		return dist_name;
	}
	public void setDist_name(String dist_name) {
		this.dist_name = dist_name;
	}
	public int getBdist_id() {
		return bdist_id;
	}
	public void setBdist_id(int bdist_id) {
		this.bdist_id = bdist_id;
	}
	public String getBdist_name() {
		return bdist_name;
	}
	public void setBdist_name(String bdist_name) {
		this.bdist_name = bdist_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getDetail_info() {
		return detail_info;
	}
	public void setDetail_info(String detail_info) {
		this.detail_info = detail_info;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getOpening_time() {
		return opening_time;
	}
	public void setOpening_time(String opening_time) {
		this.opening_time = opening_time;
	}
	public String getLast_modify_time() {
		return last_modify_time;
	}
	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	
}
