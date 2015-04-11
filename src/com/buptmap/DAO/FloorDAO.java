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

/**
 * for floor api
 * @author Peter
 * */
@Component("floorDAO")
public class FloorDAO {
	private HibernateTemplate hibernateTemplate = null;
	
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
	private String sql = null;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(FloorDAO.class);
	
	/**
	 * for api floor.around
	 * @param  unit_id  unique unit reference
	 * @param  floor_id  unique floor reference
	 * @param  type  the type looked for
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray around(String unit_id,String floor_id,String type){
		//lock.writeLock().lock();
		List<Object[]> floorList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			sql = "select indoor_id,unit_id,name,floor_id,floor_chn,"+
					 "parent_unit_id,parent_name"+
					 " from Spot where parent_unit_id = ? and floor_id = ?";  //create sql query string
			
			if(type != null && type.length()>0) { 
				sql += " and type = ?";
				params = new Object[] {unit_id, floor_id, type};
			}  //check whether there has requirement for type
			else params = new Object[] {unit_id, floor_id};
			floorList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			
			jsonObject = new JSONObject();
			Object[] floorObj = null;
			for(int i = 0; i < floorList.size(); i++){  //create json data
				floorObj = floorList.get(i);
				jsonObject.put("indoor_id", floorObj[0]);
				jsonObject.put("unit_id", floorObj[1]);
				jsonObject.put("unit_name", floorObj[2]);
				jsonObject.put("floor_id", floorObj[3]);
				jsonObject.put("floor_chn", floorObj[4]);
				jsonObject.put("parent_unit_id", floorObj[5]);
				jsonObject.put("parent_unit_name", floorObj[6]);
				
				jsonArray.add(jsonObject);
			}
			if(floorObj != null) floorObj = null;
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
	
	/**
	 * for api floor.detail
	 * @param  unit_id  unique unit reference
	 * @param  floor_id  unique floor reference
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray detail(String unit_id, String floor_id){
		//lock.writeLock().lock();
		List<Object[]> floorList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			sql = "select name,floor_id,floor_num,floor_chn,"+
						 "floor_alias,floor_brief,description,frame"+
						 " from Floor where unit_id = ? and floor_id = ?";
			params = new Object[] {unit_id, floor_id};
			
			floorList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			
			jsonObject = new JSONObject();
			Object[] floorObj = null;
			for(int i = 0; i < floorList.size(); i++){
				floorObj = floorList.get(i);
				if(floorObj[0] == null){floorObj[0] = "";}
				jsonObject.put("name", floorObj[0]);
				
				jsonObject.put("floor_id", floorObj[1]);
				jsonObject.put("floor_num", floorObj[2]);
				jsonObject.put("floor_chn", floorObj[3]);
				jsonObject.put("floor_brief", floorObj[5]);
				
				if(floorObj[4] == null){floorObj[4] = "";}
				jsonObject.put("floor_alias", floorObj[4]);
				if(floorObj[6] == null){floorObj[6] = "";}
				jsonObject.put("description", floorObj[6]);
				jsonObject.put("frame", floorObj[7]);
				jsonArray.add(jsonObject);
			}
			if(floorObj != null) floorObj = null;
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
	
	/**
	 * for api floor.search
	 * @param  unit_id  unique unit reference
	 * @return JSONArray
	 * */
	@SuppressWarnings("unchecked")
	public JSONArray search(String unit_id){
		//lock.writeLock().lock();
		List<Object[]> floorList = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		
		try{
			sql = "select name,floor_id,floor_num,floor_chn,"+
						 "floor_alias,floor_brief,description"+
						 " from Floor where unit_id = ? order by floor_num";
			
			floorList = (List<Object[]>)getHibernateTemplate().find(sql, unit_id);
			
			jsonObject = new JSONObject();
			Object[] floorObj = null;
			for(int i = 0; i < floorList.size(); i++){
				floorObj = floorList.get(i);
				if(floorObj[0] == null){floorObj[0] = "";}
				jsonObject.put("unit_name", floorObj[0]);
				jsonObject.put("floor_id", floorObj[1]);
				jsonObject.put("floor_num", floorObj[2]);
				jsonObject.put("floor_chn", floorObj[3]);
				jsonObject.put("floor_brief", floorObj[5]);
				if(floorObj[4] == null){floorObj[4] = "";}
				jsonObject.put("floor_alias", floorObj[4]);
				if(floorObj[6] == null){floorObj[6] = "";}
				jsonObject.put("description", floorObj[6]);
				
				jsonArray.add(jsonObject);
			}
			if(floorObj != null) floorObj = null;
			return jsonArray;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(floorList != null){floorList.clear(); floorList = null;}
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
