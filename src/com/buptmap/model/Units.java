package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_unit")
public class Units {
	private String unit_id;
	private int unit_brand_id;
	private int unit_type_id;
	private String g_outdoor_id;
	private String has_outdoor;
	private String name;
	private String show_name;
	private String logo;
	private String phone;
	private String email;
	private String website;
	private String address;
	private String opening_time;
	private String description;
	private String keyword;
	private String alias;
	private int available;
	private String detail_info;
	private String create_time;
	private String creator;
	private String last_modify_time;
	private String modifier;
	private int has_indoor_map;
	private String block_length;
	private String block_width;
	private String block_area;
	private String block_state;
	private String proxy_id;
	private String reco_company_id;
	private String rese_company_id;
	private String sign_company_id;
	private String pay_company_id;
	private String block_rent;
	private String block_tonnage;
	private String block_discount;
 @Id
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public int getUnit_brand_id() {
		return unit_brand_id;
	}
	public void setUnit_brand_id(int unit_brand_id) {
		this.unit_brand_id = unit_brand_id;
	}
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public String getG_outdoor_id() {
		return g_outdoor_id;
	}
	public void setG_outdoor_id(String g_outdoor_id) {
		this.g_outdoor_id = g_outdoor_id;
	}

	public String getHas_outdoor() {
		return has_outdoor;
	}
	public void setHas_outdoor(String has_outdoor) {
		this.has_outdoor = has_outdoor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getShow_name() {
		return show_name;
	}
	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
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
	public String getOpening_time() {
		return opening_time;
	}
	public void setOpening_time(String opening_time) {
		this.opening_time = opening_time;
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
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public String getDetail_info() {
		return detail_info;
	}
	public void setDetail_info(String detail_info) {
		this.detail_info = detail_info;
	}
	

	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getHas_indoor_map() {
		return has_indoor_map;
	}
	public void setHas_indoor_map(int has_indoor_map) {
		this.has_indoor_map = has_indoor_map;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getLast_modify_time() {
		return last_modify_time;
	}
	public void setLast_modify_time(String last_modify_time) {
		this.last_modify_time = last_modify_time;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getBlock_length() {
		return block_length;
	}
	public void setBlock_length(String block_length) {
		this.block_length = block_length;
	}
	public String getBlock_width() {
		return block_width;
	}
	public void setBlock_width(String block_width) {
		this.block_width = block_width;
	}
	public String getBlock_area() {
		return block_area;
	}
	public void setBlock_area(String block_area) {
		this.block_area = block_area;
	}
	public String getBlock_state() {
		return block_state;
	}
	public void setBlock_state(String block_state) {
		this.block_state = block_state;
	}
	public String getProxy_id() {
		return proxy_id;
	}
	public void setProxy_id(String proxy_id) {
		this.proxy_id = proxy_id;
	}
	public String getReco_company_id() {
		return reco_company_id;
	}
	public void setReco_company_id(String reco_company_id) {
		this.reco_company_id = reco_company_id;
	}
	public String getRese_company_id() {
		return rese_company_id;
	}
	public void setRese_company_id(String rese_company_id) {
		this.rese_company_id = rese_company_id;
	}
	public String getSign_company_id() {
		return sign_company_id;
	}
	public void setSign_company_id(String sign_company_id) {
		this.sign_company_id = sign_company_id;
	}
	public String getPay_company_id() {
		return pay_company_id;
	}
	public void setPay_company_id(String pay_company_id) {
		this.pay_company_id = pay_company_id;
	}
	public String getBlock_rent() {
		return block_rent;
	}
	public void setBlock_rent(String block_rent) {
		this.block_rent = block_rent;
	}
	public String getBlock_tonnage() {
		return block_tonnage;
	}
	public void setBlock_tonnage(String block_tonnage) {
		this.block_tonnage = block_tonnage;
	}
	public String getBlock_discount() {
		return block_discount;
	}
	public void setBlock_discount(String block_discount) {
		this.block_discount = block_discount;
	}
	
	
}
