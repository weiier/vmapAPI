package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="spot_info")
public class Spot {
	private String indoor_id;
	private String unit_id;
	private String name;
	private String parent_unit_id;
	private String parent_name;
	private String city_name;
	private Double coord_x;
	private Double coord_y;
	private String dist_name;
	private String bdist_name;
	private String floor_id;
	private String floor_chn;
	private String type;
	private String address;
	private String booth_num;
	private String website;
	private String phone;
	private String email;
	private String opening_time;
	private String unit_brand_name;
	private Double latitude;
	private Double longitude;
	private String logo;
	private String description;
	private String keyword;
	private String frame;
	private String alias;
	private String detail_info;
	private String show_name;
	private String unit_brand_id;
	private int floor_num;
	private int has_indoor_map;
	private String parent_poi_id;
	private String poi_id;
	private int unit_type_id;
	private int available;
	private Double max_x;
	private Double max_y;
	private Double min_x;
	private Double min_y;
	private String unit_type_eng;
	private Integer font;
	private int iavailable;
	private String create_time;
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
	public String getIndoor_id() {
		return indoor_id;
	}
	public void setIndoor_id(String indoor_id) {
		this.indoor_id = indoor_id;
	}
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
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOpening_time() {
		return opening_time;
	}
	public void setOpening_time(String opening_time) {
		this.opening_time = opening_time;
	}
	public String getParent_unit_id() {
		return parent_unit_id;
	}
	public void setParent_unit_id(String parent_unit_id) {
		this.parent_unit_id = parent_unit_id;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getDist_name() {
		return dist_name;
	}
	public void setDist_name(String dist_name) {
		this.dist_name = dist_name;
	}
	public String getBdist_name() {
		return bdist_name;
	}
	public void setBdist_name(String bdist_name) {
		this.bdist_name = bdist_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBooth_num() {
		return booth_num;
	}
	public void setBooth_num(String booth_num) {
		this.booth_num = booth_num;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
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
	public String getUnit_brand_name() {
		return unit_brand_name;
	}
	public void setUnit_brand_name(String unit_brand_name) {
		this.unit_brand_name = unit_brand_name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
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
	public String getFrame() {
		return frame;
	}
	public void setFrame(String frame) {
		this.frame = frame;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getDetail_info() {
		return detail_info;
	}
	public void setDetail_info(String detail_info) {
		this.detail_info = detail_info;
	}
	public String getShow_name() {
		return show_name;
	}
	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}
	public String getUnit_brand_id() {
		return unit_brand_id;
	}
	public void setUnit_brand_id(String unit_brand_id) {
		this.unit_brand_id = unit_brand_id;
	}
	public int getFloor_num() {
		return floor_num;
	}
	public void setFloor_num(int floor_num) {
		this.floor_num = floor_num;
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
	public int getUnit_type_id() {
		return unit_type_id;
	}
	public void setUnit_type_id(int unit_type_id) {
		this.unit_type_id = unit_type_id;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public String getPoi_id() {
		return poi_id;
	}
	public void setPoi_id(String poi_id) {
		this.poi_id = poi_id;
	}
	public String getUnit_type_eng() {
		return unit_type_eng;
	}
	public void setUnit_type_eng(String unit_type_eng) {
		this.unit_type_eng = unit_type_eng;
	}
	public Integer getFont() {
		return font;
	}
	public void setFont(Integer font) {
		this.font = font;
	}
	public int getIavailable() {
		return iavailable;
	}
	public void setIavailable(int iavailable) {
		this.iavailable = iavailable;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
