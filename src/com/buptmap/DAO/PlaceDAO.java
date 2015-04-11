package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.interceptor.annotations.After;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.buptmap.util.BoundsUtil;
import com.buptmap.util.TableFieldUtil;

/**
 * for place api
 * @author Peter
 * */
@Transactional
@Component("placeDAO")
public class PlaceDAO {
	private HibernateTemplate hibernateTemplate = null;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	private Object[] params;
	//String viewName = "unit_info";
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private String sql = null;
	private String availableFields;
	
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getAvailableFields() {
		return availableFields;
	}
	public void setAvailableFields(String availableFields) {
		this.availableFields = availableFields;
	}

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(PlaceDAO.class);
	
	/**
	 * get unit's latitude and longitude by unique unit reference
	 * @param  unit_id  String  unique unit reference
	 * @return  double[]
	 * */
	@SuppressWarnings("unchecked")
	public double[] getLatAndLon(String unit_id){
		//lock.writeLock().lock();
		List<Object[]> placeList = new ArrayList<Object[]>();
		
		try{
			sql = "select latitude,longitude from Place where unit_id = ?";
			placeList = (List<Object[]>)getHibernateTemplate().find(sql, unit_id);
			
			if(placeList.isEmpty()){return null;}
			else {
				Object[] place = placeList.get(0);
				double lat = Double.parseDouble(place[0].toString());
				double lon = Double.parseDouble(place[1].toString());
				if(place != null) place = null;
				return new double[]{lat,lon};
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(placeList != null){placeList.clear(); placeList = null;}
			//lock.writeLock().unlock();
		}
		
	} 
	
	@SuppressWarnings("unchecked")
	public JSONArray passage(String unit_id, int unit_type_id){
		List<Object[]> passageList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			sql = "select name,coord_x,coord_y,unit_id from Spot where parent_unit_id = ? and unit_type_id = ?";
			System.out.println(sql);
			params = new Object[]{unit_id, unit_type_id};
			passageList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			
			Object[] passageObj = null;
			
			if(passageList != null && passageList.size() > 0){
				passageObj = passageList.get(0);
				jsonObject.put("name", passageObj[0]);
				jsonObject.put("coord_x", passageObj[1]);
				jsonObject.put("coord_y", passageObj[2]);
				jsonObject.put("unit_id", passageObj[3]);

				jsonArray.add(jsonObject);
			}
			
			if(passageObj != null) passageObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(passageList != null){passageList.clear(); passageList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray all(final String unit_id, List<String> fieldNames){
		//lock.writeLock().lock();
		List<Object[]> placeList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			fieldNames = TableFieldUtil.getPlaceAllFieldNames();
			String fields = "";
			for(int i=0; i<fieldNames.size(); i++){
				fields += fieldNames.get(i) + ",";
			}
			if(fields.length() >= 1){
				fields = fields.substring(0, fields.length()-1);
			}
			else{
				return null;
			}
			sql = "select " + fields + " from Place where unit_id = ?";
			System.out.println(sql);
			
			placeList = (List<Object[]>)getHibernateTemplate().find(sql, unit_id);
			jsonObject = new JSONObject();
			
			Object[] placeObj = null;
			for(int i = 0; i < placeList.size(); i++){
				placeObj = placeList.get(i);
				for(int j=0; j<fieldNames.size(); j++){
					System.out.print(placeObj[j]+",");
					if(placeObj[j] != null) jsonObject.put(fieldNames.get(j), placeObj[j]);
					else jsonObject.put(fieldNames.get(j), placeObj[j]);
				}
				jsonArray.add(jsonObject);
			}
			if(placeObj != null) placeObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(placeList != null){placeList.clear(); placeList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api place.around
	 * @param  latitude  Double  geographical latitude
	 * @param  longitude  Double  geographical longitude
	 * @param  radius  Double  the radius of searching area
	 * @param  type  the type looked for
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray around(Double latitude, Double longitude, Double radius, String type, String city){
		//lock.writeLock().lock();
		BoundsUtil placeUtil = new BoundsUtil();
		List<Object[]> placeList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		try{
			sql = "select unit_id,name,city_name,type,latitude,longitude,keyword from Place"+
						 " where latitude > ? and latitude < ? " + 
						 " and longitude > ? and longitude < ? ";
			sql += " and city_name = ?";
			if(type != null && type.length()>0){
				sql+=" and (type = ? or keyword like ? or description like ?)";
			}
			//get the bounds of circumscribing square centered by (latitude,longitude)			
			double[] bounds = placeUtil.getBounds(latitude, longitude, radius);
			params = new Object[]{bounds[1],bounds[0],bounds[2],bounds[3],""+city+""};
			if(type != null && type.length()>0){
				params = new Object[]{bounds[1],bounds[0],bounds[2],bounds[3],""+city+"",""+type+"","%"+type+"%","%"+type+"%"};
			}
			placeList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			Object[] placeObj = null;
			for (int i = 0; i < placeList.size(); i++) {
				placeObj = placeList.get(i);
				double lat = Double.parseDouble(placeObj[4].toString());
				double lon = Double.parseDouble(placeObj[5].toString());
				
				if(placeUtil.checkInBounds(latitude, longitude, lat, lon, radius)){
					jsonObject.put("unit_id", placeObj[0]);
					if(placeObj[1] == null){ placeObj[1] = ""; }
					jsonObject.put("name", placeObj[1]);
					if(placeObj[2] == null){ placeObj[2] = ""; }
					jsonObject.put("city_name", placeObj[2]);
					if(placeObj[3] == null){ placeObj[3] = ""; }
					jsonObject.put("type", placeObj[3]);
					
					jsonObject.put("latitude", lat);
					jsonObject.put("longitude", lon);
					
					if(placeObj[6] == null){ placeObj[6] = ""; }
					jsonObject.put("keyword", placeObj[6]);
					jsonArray.add(jsonObject);
				}
			}
			if(placeObj != null) placeObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(placeList != null){placeList.clear(); placeList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			if(params != null) { params = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api place.detail
	 * @param  unit_id  String  unique unit reference
	 * @param  level  String  client's authority level
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray detail(String unit_id, String level, List<String> avaliableFieldNames){
		List<Object[]> placeList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		//lock.writeLock().lock();
		try{
			availableFields = "";
			for(int i=0; i<avaliableFieldNames.size(); i++){
				availableFields += avaliableFieldNames.get(i) + ",";
			}
			if(availableFields.length() >= 1){
				availableFields = availableFields.substring(0, availableFields.length()-1);
			}
			else{
				return null;
			}
			sql = "select " + availableFields + " from Place where unit_id = ?";
			System.out.println(sql);
			
			placeList = (List<Object[]>)getHibernateTemplate().find(sql, unit_id);
			jsonObject = new JSONObject();
			
			Object[] placeObj = null;
			for(int i = 0; i < placeList.size(); i++){
				placeObj = placeList.get(i);
				for(int j=0; j<avaliableFieldNames.size(); j++){
					if(placeObj[j] != null) jsonObject.put(avaliableFieldNames.get(j), placeObj[j]);
					else jsonObject.put(avaliableFieldNames.get(j), "");
				}
				jsonArray.add(jsonObject);
			}
			if(placeObj != null) placeObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(placeList != null){placeList.clear(); placeList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api place.search
	 * @param  unit  String  unit searching info
	 * @param  city  String  city searching info
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray search(String unit, String city) {
		//lock.writeLock().lock();
		List<Object[]> placeList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		try{
			sql = "select unit_id,name,city_name,dist_name,bdist_name from Place";
			
			if((unit == null || unit.length() == 0) 
					&& (city == null || city.length() == 0)){
				sql += " order by unit_id";
			}
			else if(city != null && city.length() != 0 
					&& unit != null && unit.length() != 0){
				sql += " where name like ? and city_name like ? order by unit_id";
				params = new Object[] {"%"+unit+"%","%"+city+"%"};
			}
			else if(unit != null && unit.length() != 0){
				sql += " where name like ?  order by unit_id";
				params = new Object[] {"%"+unit+"%"};
			}
			else if(city != null && city.length() != 0){
				sql += " where city_name like ?  order by unit_id";
				params = new Object[] { "%"+city+"%" };
				System.out.println(city);
			}
			System.out.println(sql);
			placeList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			Object[] placeObj = null;
			for (int i = 0; i < placeList.size(); i++) {
				placeObj = placeList.get(i);
				if(placeObj[0] == null){ placeObj[0] = ""; }
				jsonObject.put("unit_id", placeObj[0]);
				if(placeObj[1] == null){ placeObj[1] = ""; }
				jsonObject.put("name", placeObj[1]);
				if(placeObj[2] == null){ placeObj[2] = ""; }
				jsonObject.put("city_name", placeObj[2]);
				if(placeObj[3] == null){ placeObj[3] = ""; }
				jsonObject.put("dist_name", placeObj[3]);
				if(placeObj[4] == null){ placeObj[4] = ""; }
				jsonObject.put("bdist_name", placeObj[4]);
				jsonArray.add(jsonObject);
			}
			if(placeObj != null) placeObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(placeList != null){placeList.clear(); placeList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getPublicFacilities(String unit, String floor) {
		jsonArray = new JSONArray();
		List<Object[]> list;
		try {
			//取出公共设施的位置信息
			sql = "select unit_id,name,floor_id,coord_x,coord_y,frame,unit_type_eng,font from Spot where parent_unit_id = '"+unit+"' and unit_type_id > 3000";
			if (floor != null && !"".equals(floor)) {
				sql += " and floor_id = '"+floor+"'";
			}
			list = (List<Object[]>)getHibernateTemplate().find(sql);
			JSONObject obj = new JSONObject();
			Object[] objs = null;
			for (int i = 0; i < list.size(); i++) {
				objs = list.get(i);
				obj.put("unit_id", objs[0]);
				obj.put("name", objs[1]);
				obj.put("floor_id", objs[2]);
				obj.put("coord_x", objs[3]);
				obj.put("coord_y", objs[4]);
				obj.put("frame", objs[5]);
				obj.put("unit_type_eng", objs[6]);
				if(objs[7]!=null){
					obj.put("font", (char)(Integer.parseInt(objs[7].toString())));
				}else{
					obj.put("font", "");
				}
				jsonArray.add(obj);
			}
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		} finally {
			list = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray updated(String modifyTime, String place){
		//lock.writeLock().lock();
		List<Object[]> placeList = new ArrayList<Object[]>();
		List<String> fieldList = TableFieldUtil.updatedPlaceFieldList();
		jsonArray = new JSONArray();
		Object[] placeObj = null;
		
		try{
			sql = "select ";
			
			for(int i=0; i<fieldList.size(); i++){
				sql += fieldList.get(i) + ",";
			}
			sql = sql.substring(0, sql.length()-1)+" from Place where last_modify_time > ?";
			
			if(place != null && place.length() > 0) {
				sql += " and unit_id = ?";
				params = new Object[] {""+modifyTime+"", ""+place+""};
			}
			else params = new Object[] {""+modifyTime+""};
			System.out.println(sql);
			
			placeList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			for (int i = 0; i < placeList.size(); i++) {
				placeObj = placeList.get(i);
				for(int j=0; j < fieldList.size(); j++) {
					jsonObject.put(fieldList.get(j), placeObj[j]);
				}
				jsonArray.add(jsonObject);
				jsonObject.clear();
			}
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(placeList != null){ placeList.clear(); placeList = null; }
			if(fieldList != null){ fieldList.clear(); fieldList = null; }
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getMaxModifyTime(){
		//lock.writeLock().lock();
		List<String> timeList = new ArrayList<String>();
		
		try{
			sql = "select max(last_modify_time) from Place";
			timeList = (List<String>)getHibernateTemplate().find(sql);
			if(!timeList.isEmpty()){
				return timeList.get(0);
			}
			return null;
		}catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(timeList != null){ timeList.clear(); timeList = null;}
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        System.gc();
    }
}
