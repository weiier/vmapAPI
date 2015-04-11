package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.interceptor.annotations.After;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.util.BoundsUtil;

/**
 * for api position
 * @author Peter
 * */
@Component("positionDAO")
public class PositionDAO {
private HibernateTemplate hibernateTemplate = null;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	private Object[] params;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();	
	private Logger logger = Logger.getLogger(PositionDAO.class);
	
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
	
	/**
	 * for api position.pos2add
	 * @param  unit_id  String  unique unit reference
	 * @param  floor_id  String  unique floor reference
	 * @param  x  Double  coordinate x
	 * @param  y  Double  coordinate y
	 * @return  JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray pos2add(String unit_id,String floor_id,Double x,Double y){
		//lock.writeLock().lock();
		List<Object[]> positionList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		Double[] framePoint = null;
		
		try{
			String sql = "select frame,floor_chn,type,parent_unit_id,parent_name,indoor_id,unit_id,name,has_indoor_map,parent_poi_id"+
					 " from Position where parent_unit_id = ? and floor_id = ?";
			params = new Object[] {unit_id, floor_id};
			
			positionList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			Object[] positionObj = null;
			for(int i = 0; i < positionList.size(); i++){
				positionObj = positionList.get(i);
				String[] frame = positionObj[0].toString().split(";");
				framePoint = new Double[frame.length*2];
				int j=0;
				for (String s : frame) {  //get current unit's bounds
					String[] point = s.split(",");
					framePoint[j] = new Double(point[0]);
					framePoint[j+1] = new Double(point[1]);
					System.out.println(framePoint[j]+" "+framePoint[j+1]);
					j+=2;
				}
				
				if(BoundsUtil.inPolygon(x, y, framePoint)){  //check whether the point is in current unit's bounds, and chose the unit
					jsonObject.put("indoor_id", positionObj[5]);
					jsonObject.put("floor_chn", positionObj[1]);
					jsonObject.put("type", positionObj[2]);
					jsonObject.put("parent_unit_id", positionObj[3]);
					jsonObject.put("parent_name", positionObj[4]);
					jsonObject.put("unit_id", positionObj[6]);
					jsonObject.put("unit_name", positionObj[7]);
					jsonObject.put("has_indoor_map", positionObj[8]);
					jsonObject.put("parent_poi_id", positionObj[9]);
					jsonArray.add(jsonObject);
					return jsonArray;
				}
				//else return null;
			}
			if(positionObj != null) positionObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(positionList != null){positionList.clear(); positionList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
			if(framePoint != null) framePoint = null;
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * for api position.add2pos
	 * @param  unit_id  String  unique unit reference
	 * @param  indoor_id  String  unique indoor reference
	 * @return  JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray add2pos(String parent_unit_id,String unit_id){
		//lock.writeLock().lock();
		List<Object[]> positionList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			String sql = "select coord_x,coord_y,scale,dpi,floor_chn,parent_unit_id,indoor_id,unit_id"+
					 " from Position where parent_unit_id = ? and unit_id = ?";
			params = new Object[] {parent_unit_id, unit_id};
			
			positionList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			jsonObject = new JSONObject();
			Object[] positionObj = null;
			for(int i = 0; i < positionList.size(); i++){
				positionObj = positionList.get(i);
				double x = Double.parseDouble(positionObj[0].toString());
				double y = Double.parseDouble(positionObj[1].toString());
				
				jsonObject.put("x", x);
				jsonObject.put("y", y);
				jsonObject.put("floor_chn", positionObj[4]);
				jsonObject.put("parent_unit_id", positionObj[5]);
				jsonObject.put("indoor_id", positionObj[6]);
				jsonObject.put("unit_id", positionObj[7]);
				jsonArray.add(jsonObject);
			}
			if(positionObj != null) positionObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(positionList != null){positionList.clear(); positionList = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
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
