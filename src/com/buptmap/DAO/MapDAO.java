package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.interceptor.annotations.After;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

/**
 * for map api
 * @author Peter
 * */
@Component("mapDAO")
public class MapDAO {
	
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	private Object[] params = null;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private Map<String, String> infoMap = null;
	private String sql = null;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(MapDAO.class);
	
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
	public Map<String, String> getInfoMap() {
		return infoMap;
	}
	public void setInfoMap(Map<String, String> infoMap) {
		this.infoMap = infoMap;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	/**
	 * get map's calculate info( dpi, scale, map path ), used to calculate unit's pixel position in map
	 * @param  unit_id  String  unique unit reference
	 * @param  floor_id  String  unique floor reference
	 * @param  level  int  map level
	 * @return  Map<String,String>
	 * */
	@SuppressWarnings("unchecked")
	public Map<String, String> getCalculateInfo(String unit_id, String floor_id, int level){
		//lock.writeLock().lock();
		List<Object[]> calcuList = new ArrayList<Object[]>();
		Object[] objects = null;
		infoMap = new HashMap<String, String>();
		
		try{
			sql = "select scale,dpi,map_path"+
					 " from Map where unit_id = ? and floor_id = ?";
			if(level != -1) {
				sql += " and map_level = ?";
				params = new Object[] {unit_id, floor_id, level};
			}
			else params = new Object[] {unit_id, floor_id};
			
			calcuList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			if(calcuList.isEmpty()) {
				return null;
			}
			objects = calcuList.get(0);
			infoMap.put("scale", objects[0].toString());
			infoMap.put("dpi", objects[1].toString());
			infoMap.put("path", objects[2].toString());
			if(objects != null) objects = null;
			return infoMap;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(objects != null) objects = null;
			if(calcuList != null){calcuList.clear(); calcuList = null;}
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * get unit's coordinate(coord_x, coord_y) by unique unit reference
	 * @param  unit_id  String  unique unit reference
	 * @return  double[]  coordinate x,y
	 * */
	@SuppressWarnings("unchecked")
	public double[] getCoordinate(String unit_id, String parent_unit_id){
		//lock.writeLock().lock();
		List<Object[]> coordList = new ArrayList<Object[]>();
		
		try{
			sql = "select coord_x, coord_y"+
					 " from Spot where unit_id = ? and parent_unit_id = ?";
			params = new Object[] {unit_id, parent_unit_id};
				
			coordList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			if(coordList.isEmpty()) {
				return null;
			}
			double coord_x = Double.parseDouble(coordList.get(0)[0].toString());
			double coord_y = Double.parseDouble(coordList.get(0)[1].toString());
			return new double[] {coord_x, coord_y};
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(coordList != null){coordList.clear(); coordList = null;}
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * get map's storing path by unique unit reference, floor id, and map level
	 * @param  unit_id  String  unique unit reference
	 * @param  floor_id  String  unique floor reference
	 * @param  level  int  map level
	 * @return String  map path
	 * */
	@SuppressWarnings("unchecked")
	public String getMapPath(String unit_id,String floor_id,Integer level, String style){
		//lock.writeLock().lock();
		List<String> mapList = new ArrayList<String>();
		
		try{
			sql = "select map_path"+
					 " from Map where unit_id = ? and floor_id = ?"+
					 " and map_level = ?";
			if(style != null) {
				sql += " and style = ?";
				params = new Object[] {unit_id, floor_id, level, style};
			}
			else params = new Object[] {unit_id, floor_id, level};
			
			mapList = (List<String>) getHibernateTemplate().find(sql, params);
			if(!mapList.isEmpty())
				return mapList.get(0);
			
			return null;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(mapList != null) { mapList.clear(); mapList = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for map.detail
	 * @param  unit_id  String  unique unit reference
	 * @param  floor_id  String  unique floor reference
	 * @param  level  int  map level
	 * @return  JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray all(String unit_id){
		List<Object[]> mapList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		//lock.writeLock().lock();
		try{
			sql = "select floor_id,scale,dpi,map_level,"+
					 "max_map_level,map_path,origin_x,origin_y,unit_id,style,floor_brief"+
					 " from Map where unit_id = ? order by floor_num, style,map_level";
			
			mapList = (List<Object[]>)getHibernateTemplate().find(sql, unit_id);
			if(mapList.isEmpty()) {
				return null;
			}
			
			jsonObject = new JSONObject();
			Object[] mapObj = null;
			for(int i = 0; i < mapList.size(); i++){
				mapObj = mapList.get(i);
				jsonObject.put("floor_id", mapObj[0]);
				jsonObject.put("scale", mapObj[1]);
				jsonObject.put("dpi", mapObj[2]);
				jsonObject.put("map_level", mapObj[3]);
				jsonObject.put("max_map_level", mapObj[4]);
				jsonObject.put("map_path", mapObj[5]);
				jsonObject.put("origin_x", mapObj[6]);
				jsonObject.put("origin_y", mapObj[7]);
				jsonObject.put("parent_unit_id", mapObj[8]);
				jsonObject.put("style", mapObj[9]);
				jsonObject.put("floor_brief", mapObj[10]);
				jsonArray.add(jsonObject);
			}
			if(mapObj != null) mapObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(mapList != null){mapList.clear(); mapList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for map.detail
	 * @param  unit_id  String  unique unit reference
	 * @param  floor_id  String  unique floor reference
	 * @param  level  int  map level
	 * @return  JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray detail(String unit_id,String floor_id,Integer level){
		//lock.writeLock().lock();
		List<Object[]> mapList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			sql = "select floor_chn,scale,dpi,map_level,"+
					 "max_map_level,map_path,unit_id"+
					 " from Map where unit_id = ? and floor_id = ?";
			//System.out.println(type);
			if(level != -1) {
				sql += " and map_level = ?";
				params = new Object[] {unit_id, floor_id, level};
			}
			else params = new Object[] {unit_id, floor_id};
			mapList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			
			if(mapList.isEmpty()) {
				return null;
			}
			
			jsonObject = new JSONObject();
			Object[] mapObj = null;
			for(int i = 0; i < mapList.size(); i++){
				mapObj = mapList.get(i);
				jsonObject.put("floor_chn", mapObj[0]);
				jsonObject.put("scale", mapObj[1]);
				jsonObject.put("dpi", mapObj[2]);
				jsonObject.put("map_level", mapObj[3]);
				jsonObject.put("max_map_level", mapObj[4]);
				jsonObject.put("map_path", mapObj[5]);
				jsonObject.put("unit_id", mapObj[6]);
				
				jsonArray.add(jsonObject);
			}
			if(mapObj != null) mapObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(mapList != null){mapList.clear(); mapList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        if(infoMap != null) { infoMap.clear(); infoMap = null; }
        System.gc();
    }
}
