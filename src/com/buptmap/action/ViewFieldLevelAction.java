package com.buptmap.action;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.buptmap.DAO.ViewFieldLevelDAO;
import com.buptmap.model.Place;
import com.buptmap.model.Spot;

import com.opensymphony.xwork2.interceptor.annotations.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ViewFieldLevelAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8727037861647362993L;
	private JSONObject resultObj;
	private String levels;
	private String view;
	private String params;
	ViewFieldLevelDAO field;
	
	public ViewFieldLevelDAO getField() {
		return field;
	}
	@Resource(name="viewFieldLevelDAO")
	public void setField(ViewFieldLevelDAO field) {
		this.field = field;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(ViewFieldLevelAction.class);
	
	public String place(){
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("total", 1);
		map.put("rows", "{'name':'place'}");
		resultObj = JSONObject.fromObject("{'total':1,rows:[{'name':'place'}]}");
		System.out.println(resultObj.toString());
		return SUCCESS;
	}
	
	public String deleteUser(){
		//lock.writeLock().lock();
		try{
			resultObj = JSONObject.fromObject(field.deleteUser(params));
			if(field != null) { field.destory(); field = null; }
			return SUCCESS;
		}finally{
			//lock.writeLock().unlock();
		}
	}
	
	public String editUser(){
		//lock.writeLock().lock();
		try{
			String[] para = params.split(",");
			resultObj = JSONObject.fromObject(field.editUser(para));
			return SUCCESS;
		}finally{
			//lock.writeLock().unlock();
		}
	}
	
	public String addUser(){
		//lock.writeLock().lock();
		try{
			String[] para = params.split(",");
			resultObj = JSONObject.fromObject(field.addUser(para));
			return SUCCESS;
		}finally{
			//lock.writeLock().unlock();
		}
	}
	
	public String getKey(){
		//lock.writeLock().lock();
		UUID uuid = UUID.randomUUID();
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			map.put("success", true);
			map.put("key", uuid.toString().toUpperCase());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}catch (Exception e) {
			map.put("success", false);
			map.put("message", e.getMessage());
			logger.error(e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null){ map.clear(); map = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String getUsers(){
		//lock.writeLock().lock();
		Map<String,Object> map = new HashMap<String,Object>();
		JSONArray array = null;
		
		try{
			array = field.getUsers();
			map.put("total", array.size());
			map.put("rows", array);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}catch (Exception e) {
			map.put("success", false);
			map.put("message", e.getMessage());
			logger.error(e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null){ map.clear(); map = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String getFields(){
		//lock.writeLock().lock();
		Class<?> classType = null;
		if(view.equals("unit_info")) classType = Place.class;
		else if(view.equals("spot_info")) classType = Spot.class;
		
		Map<String,Object> map = new HashMap<String,Object>();
		JSONArray array = null;
		try{
			array = field.getFields(classType, view);
			map.put("total", array.size());
			map.put("rows", array);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}catch (Exception e) {
			map.put("success", false);
			map.put("message", e.getMessage());
			logger.error(e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null){ map.clear(); map = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String update(){
		//lock.writeLock().lock();
		Map<String,Object> map = new HashMap<String,Object>();
		JSONArray array = null;
		
		Class<?> classType = null;
		try{
			if(view.equals("unit_info")) classType = Place.class;
			else if(view.equals("spot_info")) classType = Spot.class;

			if(field.updateLevels(levels, view)){
				array = field.getFields(classType, view);
				map.put("success", true);
				map.put("message", "更新field等级成功");
				map.put("total", array.size());
				map.put("rows", array);
				logger.info("更新field等级成功");
			}
			else{
				map.put("success", false);
				map.put("message", "更新field等级失败");
				logger.info("更新field等级失败");
			}
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}catch (Exception e) {
			map.put("success", false);
			map.put("message", e.getMessage());
			logger.error(e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null){ map.clear(); map = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		logger.info("request end......");
		if(resultObj != null) { resultObj.clear(); resultObj = null; }
        System.gc();
    }
}
