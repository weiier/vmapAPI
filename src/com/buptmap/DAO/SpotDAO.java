package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.interceptor.annotations.After;

import com.buptmap.util.BoundsUtil;
import com.buptmap.util.TableFieldUtil;

/**
 * for spot api
 * @author Peter
 * */
@Component("spotDAO")
public class SpotDAO {
	private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
	
	private HibernateTemplate hibernateTemplate = null;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	private Object[] params;
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(SpotDAO.class);
	
	/**
	 * get unit's parent_unit_id by unique unit reference
	 * @param  unit_id  String  unique unit reference
	 * @return  String
	 * */
	@SuppressWarnings("unchecked")
	public String getParentId(String unit_id){
		//lock.writeLock().lock();
		List<String> spotList = new ArrayList<String>();
		
		try{
			String sql = "select parent_unit_id from Spot where unit_id = ?";
			params = new Object[] {unit_id};
			
			spotList = (List<String>)getHibernateTemplate().find(sql, params);
			if(spotList.isEmpty()){
				return null;
			}
			else {
				String parent_id = null;
				if(spotList != null && spotList.size() > 0) parent_id= spotList.get(0);
				return parent_id;
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(spotList != null){spotList.clear(); spotList = null;}
			//lock.writeLock().unlock();
		}
		
	} 
	
	/**
	 * get unit's coord position by unique unit reference
	 * @param  unit_id  String  unique unit reference
	 * @return  double[]
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray getCoord(String unit_id){
		List<Object[]> coordList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			String sql = "select name,coord_x,coord_y,unit_id from Spot where unit_id = ?";
			System.out.println(sql);
			params = new Object[]{unit_id};
			coordList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			
			Object[] coordObj = null;
			
			if(coordList != null && coordList.size() > 0){
				coordObj = coordList.get(0);
				jsonObject.put("name", coordObj[0]);
				jsonObject.put("coord_x", coordObj[1]);
				jsonObject.put("coord_y", coordObj[2]);
				jsonObject.put("unit_id", coordObj[3]);

				jsonArray.add(jsonObject);
			}
			
			if(coordObj != null) coordObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(coordList != null){coordList.clear(); coordList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * get unit's latitude and longitude by unique unit reference
	 * @param  unit_id  String  unique unit reference
	 * @return  double[]
	 * */
	@SuppressWarnings("unchecked")
	public double[] getLatAndLon(String unit_id){
		//lock.writeLock().lock();
		List<Object[]> spotList = new ArrayList<Object[]>();
		
		try{
			String sql = "select latitude,longitude from Spot where unit_id = ?";
			params = new Object[] {unit_id};
			
			spotList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			if(spotList.isEmpty()){
				return null;
			}
			else {
				Object[] spot = spotList.get(0);
				double lat = Double.parseDouble(spot[0].toString());
				double lon = Double.parseDouble(spot[1].toString());
				return new double[]{lat,lon};
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(spotList != null){spotList.clear(); spotList = null;}
			//lock.writeLock().unlock();
		}
		
	} 
	
	@SuppressWarnings("unchecked")
	public JSONArray all(String parent_unit_id, List<String> fieldNames){
		//lock.writeLock().lock();
		List<Object[]> spotList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
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
			String sql = "select " + fields + " from Spot where parent_unit_id = ?";
			System.out.println(sql);
			
			params = new Object[] {parent_unit_id};
			spotList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			
			jsonObject = new JSONObject();
			Object[] spotObj = null;
			for(int i = 0; i < spotList.size(); i++){
				spotObj = spotList.get(i);
				for(int j=0; j<fieldNames.size(); j++){
					if(spotObj[j] != null) jsonObject.put(fieldNames.get(j), spotObj[j]);
					else jsonObject.put(fieldNames.get(j), spotObj[j]);
				}
				jsonArray.add(jsonObject);
			}
			if(spotObj != null) spotObj = null;	
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(spotList != null){spotList.clear(); spotList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api spot.around
	 * @param  latitude  Double  geographical latitude
	 * @param  longitude  Double  geographical longitude
	 * @param  radius  Double  the radius of searching area
	 * @param  type  the type looked for
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray around(Double latitude,Double longitude,Double radius,String type){
		//lock.writeLock().lock();
		BoundsUtil spotUtil = new BoundsUtil();
		List<Object[]> spotList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			String sql = "select indoor_id,unit_id,name,city_name,type,latitude,longitude,keyword,floor_chn,parent_unit_id,parent_name from Spot"+
						 " where latitude > ? and latitude < ? " + 
						 " and longitude > ? and longitude < ? " ;
			if(type != null && type.length() > 0){
				sql+=" and (type = ? or keyword like ?)";
				
			}
			//get the bounds of circumscribing square centered by (latitude,longitude)			
			double[] bounds = spotUtil.getBounds(latitude, longitude, radius);
			
			//get unit information in this square
			if(type != null && type.length()>0){
				params = new Object[]{bounds[1],bounds[0],bounds[2],bounds[3],""+type+"","%"+type+"%"};
			}
			else params = new Object[]{bounds[1],bounds[0],bounds[2],bounds[3]};
			
			spotList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			Object[] spotObj = null;
			for (int i = 0; i < spotList.size(); i++) {
				spotObj = spotList.get(i);
				double lat = Double.parseDouble(spotObj[5].toString());
				double lon = Double.parseDouble(spotObj[6].toString());
				
				//check whether current (lat,lon) is in the circle centered by (latitude,longitude)
				if(spotUtil.checkInBounds(latitude, longitude, lat, lon, radius)){
					jsonObject.put("indoor_id", spotObj[0]);
					jsonObject.put("unit_id", spotObj[1]);
					if(spotObj[2] == null){ spotObj[2] = ""; }
					jsonObject.put("unit_name", spotObj[2]);
					jsonObject.put("parent_unit_id", spotObj[9]);
					if(spotObj[10] == null){ spotObj[10] = ""; }
					jsonObject.put("parent_name", spotObj[10]);
					
					jsonObject.put("floor_chn", spotObj[8]);
					if(spotObj[3] == null){ spotObj[3] = ""; }
					jsonObject.put("city_name", spotObj[3]);
					if(spotObj[4] == null){ spotObj[4] = ""; }
					jsonObject.put("type", spotObj[4]);
					
					jsonObject.put("latitude", lat);
					jsonObject.put("longitude", lon);
					
					if(spotObj[7] == null){ spotObj[7] = ""; }
					jsonObject.put("keyword", spotObj[7]);
					jsonArray.add(jsonObject);
				}
			}
			if(spotObj != null) spotObj = null;	
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(spotList != null){spotList.clear(); spotList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api spot.detail
	 * @param  unit_id  String  unique unit reference
	 * @param  level  String  client's authority level
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray detail(String unit_id, String level, List<String> avaliableFieldNames){
		//lock.writeLock().lock();
		List<Object[]> spotList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			System.out.println("level:"+level);
			String availableFields = "";
			for(int i=0; i<avaliableFieldNames.size(); i++){
				availableFields += avaliableFieldNames.get(i) + ",";
			}
			if(availableFields.length() >= 1){
				availableFields = availableFields.substring(0, availableFields.length()-1);
			}
			else{
				return null;
			}
			String sql = "select " + availableFields + " from Spot where unit_id = ?";
			System.out.println(sql);
			
			spotList = (List<Object[]>)getHibernateTemplate().find(sql, unit_id);
			//JSONObject spotJsonObj = new JSONObject();
			jsonObject = new JSONObject();
			Object[] spotObj = null;
			System.out.println(avaliableFieldNames.size());
			for(int i = 0; i < spotList.size(); i++){
				spotObj = spotList.get(i);
				for(int j=0; j<avaliableFieldNames.size(); j++){
					if(spotObj[j] != null)jsonObject.put(avaliableFieldNames.get(j), spotObj[j]);
					else jsonObject.put(avaliableFieldNames.get(j), "");
				}
				jsonArray.add(jsonObject);
			}
			if(spotObj != null) spotObj = null;	
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(spotList != null){spotList.clear(); spotList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api spot.search
	 * @param  spot  String  spot searching info
	 * @param  unit_id  String the unit's unique reference searching in
	 * @param  city  String  city searching info
	 * @param  type  String  the type looked for
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray search(String spot,String unit_id,String city,String type){
		
		//lock.writeLock().lock();
		List<Object[]> floorList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		List<Object> paramList = new ArrayList<Object>();
		
		try{
			String sql = "";
			
			if(unit_id == null || unit_id.length() == 0){
				sql = "select indoor_id,unit_id,name,type,floor_id,floor_chn,"+
						 "parent_unit_id,parent_name,coord_x,coord_y,has_indoor_map,parent_poi_id"+
						 " from Spot where (name like ? or booth_num like ?)";
			}
			else {
				sql = "select indoor_id,unit_id,name,type,floor_id,floor_chn,"+
					 "parent_unit_id,parent_name,coord_x,coord_y,has_indoor_map,parent_poi_id"+
					 " from Spot where (name like ? or booth_num like ?) and parent_unit_id = ?";
			}
			if(city != null && city.length() > 0) sql += " and city_name like ?" ;
			if(type != null && type.length()>0) sql += " and type = ?";
			sql += " order by floor_num";
			
			paramList.add("%"+spot+"%"); paramList.add("%"+spot+"%");
			if(unit_id != null && unit_id.length()>0) {
				paramList.add(unit_id);
			}
			if(city != null && city.length()>0) {
				paramList.add("%"+city+"%");
			}
			if(type != null && type.length()>0) {
				paramList.add(type);
			}
			
			params = new Object[paramList.size()];
			for(int i=0; i<paramList.size(); i++){
				params[i] = paramList.get(i);
			}
			
			floorList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			//JSONObject floorJsonObj = new JSONObject();
			jsonObject = new JSONObject();
			Object[] spotObj = null;
			for(int i = 0; i < floorList.size(); i++){
				spotObj = floorList.get(i);
				jsonObject.put("indoor_id", spotObj[0]);
				jsonObject.put("unit_id", spotObj[1]);
				jsonObject.put("unit_name", spotObj[2]);
				if(spotObj[3]==null) spotObj[3]="";
				jsonObject.put("type", spotObj[3]);
				jsonObject.put("floor_id", spotObj[4]);
				jsonObject.put("floor_chn", spotObj[5]);
				jsonObject.put("parent_unit_id", spotObj[6]);
				jsonObject.put("parent_unit_name", spotObj[7]);
				jsonObject.put("x", spotObj[8]);
				jsonObject.put("y", spotObj[9]);
				jsonObject.put("has_indoor_map", spotObj[10]);
				jsonObject.put("parent_poi_id", spotObj[11]);
				jsonArray.add(jsonObject);
			}
			if(spotObj != null) spotObj = null;	
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(floorList != null){floorList.clear(); floorList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray updated(String modifyTime, String place){
		//lock.writeLock().lock();
		List<Object[]> placeList = new ArrayList<Object[]>();
		List<String> fieldList = TableFieldUtil.updatedSpotFieldList();
		jsonArray = new JSONArray();
		Object[] placeObj = null;
		
		try{
			String sql = "select ";
					
			for(int i=0; i<fieldList.size(); i++){
				sql += fieldList.get(i) + ",";
			}
			sql = sql.substring(0, sql.length()-1)+" from Spot where last_modify_time > ?";
			
			if(place != null && place.length() > 0) {
				sql += " and parent_unit_id = ?";
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
			String sql = "select max(last_modify_time) from Spot";
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
