package com.buptmap.util;

import java.util.ArrayList;
import java.util.List;

public class TableFieldUtil {
	
	public static List<String> updatedPlaceFieldList(){
		//String sql = "select unit_id,unit_brand_id,unit_type_id,city_id,dist_id,bdist_id,outdoor_id,alias,show_name,name,"
		//		+"description,detail_info,keyword,opening_time,logo,phone,email,website,address,latitude,longitude,frame"
		//		+" from Place where last_modify_time > ?";
		List<String> fieldList = new ArrayList<String>();
		
		fieldList.add(0, "unit_id");
		fieldList.add(1, "available");
		fieldList.add(2, "unit_brand_id");
		fieldList.add(3, "unit_type_id");
		fieldList.add(4, "city_id");
		fieldList.add(5, "dist_id");
		fieldList.add(6, "bdist_id");
		fieldList.add(7, "outdoor_id");
		fieldList.add(8, "alias");
		fieldList.add(9, "show_name");
		fieldList.add(10, "name");
		fieldList.add(11, "description");
		fieldList.add(12, "detail_info");
		fieldList.add(13, "keyword");
		fieldList.add(14, "opening_time");
		fieldList.add(15, "phone");
		fieldList.add(16, "email");
		fieldList.add(17, "website");
		fieldList.add(18, "address");
		fieldList.add(19, "latitude");
		fieldList.add(20, "longitude");
		fieldList.add(21, "frame");
		fieldList.add(22, "logo");
		
		return fieldList;
	}
	
	public static List<String> updatedSpotFieldList(){
		//String sql = "select unit_id,indoor_id,parent_unit_id,floor_id,booth_num,type,address,"+
		//		"coord_x,coord_y,frame,max_x,max_y,min_x,min_y"
		//		+" from Spot where last_modify_time > ?";
		
		List<String> fieldList = new ArrayList<String>();
		
		fieldList.add(0, "unit_id");
		fieldList.add(1, "available");
		fieldList.add(2, "indoor_id");
		fieldList.add(3, "parent_unit_id");
		fieldList.add(4, "floor_id");
		fieldList.add(5, "booth_num");
		fieldList.add(6, "type");
		fieldList.add(7, "address");
		fieldList.add(8, "coord_x");
		fieldList.add(9, "coord_y");
		fieldList.add(10, "frame");
		fieldList.add(11, "max_x");
		fieldList.add(12, "max_y");
		fieldList.add(13, "min_x");
		fieldList.add(14, "min_y");
		fieldList.add(15, "unit_type_id");
		fieldList.add(16, "description");
		fieldList.add(17, "opening_time");
		fieldList.add(18, "unit_brand_id");
		fieldList.add(19, "name");
		fieldList.add(20, "show_name");
		fieldList.add(21, "website");
		fieldList.add(22, "detail_info");
		fieldList.add(23, "logo");
		
		return fieldList;
	}
	
	public static List<String> getPlaceAllFieldNames(){
		List<String> fieldList = new ArrayList<String>();
		
		fieldList.add(0, "unit_id");
		fieldList.add(1, "name");
		fieldList.add(2, "unit_brand_id");
		fieldList.add(3, "unit_brand_name");
		fieldList.add(4, "unit_type_id");
		fieldList.add(5, "logo");
		fieldList.add(6, "type");
		fieldList.add(7, "phone");
		fieldList.add(8, "email");
		fieldList.add(9, "website");
		fieldList.add(10, "address");
		fieldList.add(11, "description");
		fieldList.add(12, "keyword");
		fieldList.add(13, "opening_time");
		fieldList.add(14, "detail_info");
		fieldList.add(15, "outdoor_id");
		fieldList.add(16, "province_id");
		fieldList.add(17, "province_name");
		fieldList.add(18, "city_id");
		fieldList.add(19, "city_name");
		fieldList.add(20, "dist_id");
		fieldList.add(21, "dist_name");
		fieldList.add(22, "bdist_id");
		fieldList.add(23, "bdist_name");
		fieldList.add(24, "alias");
		fieldList.add(25, "show_name");
		fieldList.add(26, "zip_code");
		fieldList.add(27, "latitude");
		fieldList.add(28, "longitude");
		fieldList.add(29, "frame");
		fieldList.add(30, "has_indoor_map");
		fieldList.add(31, "last_modify_time");
		fieldList.add(32, "available");
		
		return fieldList;
	}
}
