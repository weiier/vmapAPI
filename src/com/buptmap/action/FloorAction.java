package com.buptmap.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.interceptor.annotations.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.buptmap.Service.FloorService;
import com.buptmap.util.LogUtil;

@Scope("prototype")
public class FloorAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7329926196128442910L;
	
	FloorService floorService;
	public FloorService getFloorService() {
		return floorService;
	}
	@Resource(name="floorService")
	public void setFloorService(FloorService floorService) {
		this.floorService = floorService;
	}
	
	private JSONObject resultObj;
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	LogUtil log = new LogUtil();
	private String place;
	private String floor;
	private String type;
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(FloorAction.class);
	
	public String around() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor")){
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
		
		place = para.bParas.get("place");
		floor = para.bParas.get("floor");
		type = para.bParas.get("type");
		if(place == null || floor == null || place.length() == 0 || floor.length()==0) {
			map.put("success", true);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		//JSONObject obj = new JSONObject();
		try{
			array = floorService.around(place,floor,type);
			
			if(array != null){
				if(array.size() != 0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
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
			else {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String detail() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor")){
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
		
		place = para.bParas.get("place");
		floor = para.bParas.get("floor");
		if(place == null || floor == null || place.length() == 0 || floor.length()==0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		//JSONObject obj = new JSONObject();
		try{
			array = floorService.detail(place,floor);
			
			if(array != null){
				if(array.size() != 0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					logger.info("返回数据成功");
					resultObj = JSONObject.fromObject(map);
				}
				else {
					map.put("success", false);
					map.put("message", ErrorMessage.NoResultError);
					logger.info(ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
				}
			}
			else {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String search() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			log.writeLog(logPath, validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		place = para.bParas.get("place");
		if(place == null || place.length() == 0) {
			map.put("success", true);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		
		try{
			array = floorService.search(place);
			
			if(array != null){
				if(array.size() != 0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					logger.info("返回数据成功");
					resultObj = JSONObject.fromObject(map);
				}
				else {
					map.put("success", false);
					map.put("message", ErrorMessage.NoResultError);
					logger.info(ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
				}
			}
			else {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		logger.info("request end......");
		if(floorService != null) floorService = null; 
		if(resultObj != null) { resultObj.clear(); resultObj = null; }
        System.gc();
    }
}
