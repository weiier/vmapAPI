package com.buptmap.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.buptmap.Service.DbService;
import com.buptmap.Service.PlaceService;
import com.buptmap.Service.SpotService;
import com.opensymphony.xwork2.interceptor.annotations.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UpdateAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5007765814376387894L;
	
	private JSONObject resultObj;

	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	private PlaceService placeService;
	private SpotService spotService;
	private DbService dbService;
	
	public PlaceService getPlaceService() {
		return placeService;
	}
	@Resource(name="placeService")
	public void setPlaceService(PlaceService placeService) {
		this.placeService = placeService;
	}
	public SpotService getSpotService() {
		return spotService;
	}
	@Resource(name="spotService")
	public void setSpotService(SpotService spotService) {
		this.spotService = spotService;
	}
	public DbService getDbService() {
		return dbService;
	}
	@Resource(name="dbService")
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	
	private JSONArray jsonArray = null;
	private String modifyTime = null;
	private String place = null;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(UpdateAction.class);
	
	public String info() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("mtime")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		modifyTime = para.bParas.get("mtime");
		place = para.bParas.get("place");
		if((modifyTime == null || modifyTime.length() == 0)){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		JSONArray placeArray = new JSONArray();
		JSONArray spotArray = new JSONArray();
		try{
			String placeMaxModifyTime = null; 
			String spotMaxModifyTime = null;
			System.out.println("modifyTime: "+para.bParas.get("mtime"));
			placeArray = placeService.updated(modifyTime, place);
			spotArray = spotService.updated(modifyTime, place);
			
			if((spotArray == null || spotArray.isEmpty()) && (placeArray == null || placeArray.isEmpty())){
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			
			if(placeArray != null && !placeArray.isEmpty()) placeMaxModifyTime =placeService.getMaxModifyTime();
			if(spotArray != null && !spotArray.isEmpty()) spotMaxModifyTime = spotService.getMaxModifyTime();
			
			if(placeMaxModifyTime == null && spotMaxModifyTime == null) {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			else if(placeMaxModifyTime == null) modifyTime = spotMaxModifyTime;
			else if(spotMaxModifyTime == null) modifyTime = placeMaxModifyTime;
			else modifyTime = placeMaxModifyTime.compareTo(spotMaxModifyTime) > 0 ? placeMaxModifyTime : spotMaxModifyTime;
			
			int mall = 0;
			int spot = 0;
			if(placeArray != null) mall = placeArray.size();
			if(spotArray != null) spot = spotArray.size();
			
			if((spotArray != null) && (placeArray != null)){ 
				map.put("success", true);
				map.put("mall_total", mall);
				map.put("spot_total", spot);
				map.put("mall", placeArray);
				map.put("spot", spotArray);
				map.put("modify_time", modifyTime);
				resultObj = JSONObject.fromObject(map);
				logger.info("返回数据成功");
			}
			else if(spotArray == null){
				map.put("success", true);
				map.put("mall_total", mall);
				map.put("spot_total", spot);
				map.put("mall", placeArray);
				map.put("modify_time", modifyTime);
				resultObj = JSONObject.fromObject(map);
				logger.info("返回数据成功");
			}
			else if(placeArray == null){
				map.put("success", true);
				map.put("mall_total", mall);
				map.put("spot_total", spot);
				map.put("spot", spotArray);
				map.put("modify_time", modifyTime);
				resultObj = JSONObject.fromObject(map);
				logger.info("返回数据成功");
			}
			else{
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}finally{
			if(placeArray != null){ placeArray.clear(); placeArray = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String db() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("mtime")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		modifyTime = para.bParas.get("mtime");
		if((modifyTime == null || modifyTime.length() == 0)){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		try{
			//System.out.println("modifyTime: "+para.bParas.get("mtime"));
			jsonArray = dbService.updated(modifyTime);
			modifyTime = dbService.getMaxModifyTime();
			
			if(modifyTime == null) {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			
			if(jsonArray != null){
				if(jsonArray.size() != 0){
					map.put("total", jsonArray.size());
					map.put("success", true);
					map.put("rows", jsonArray);
					map.put("modify_time", modifyTime);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");
				}
				else {
					map.put("success", false);
					map.put("message", ErrorMessage.NoResultError);
					logger.info(ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
				}
			}
			else{
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}finally{
			if(jsonArray != null){ jsonArray.clear(); jsonArray = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		logger.info("request end......");
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        System.gc();
    }
}
