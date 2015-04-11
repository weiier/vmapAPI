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
import org.springframework.context.annotation.Scope;

import com.buptmap.Service.PositionService;
import com.buptmap.util.LogUtil;

import com.opensymphony.xwork2.interceptor.annotations.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class PositionAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5735778032268164724L;
	
	PositionService positionService;
	
	public PositionService getPositionService() {
		return positionService;
	}
	@Resource(name="positionService")
	public void setPositionService(PositionService positionService) {
		this.positionService = positionService;
	}

	private JSONObject resultObj;
	
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	private String place;
	private String spot;
	private String floor;
	Double x;
	Double y;
	LogUtil log = new LogUtil();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(PositionAction.class);
	
	public Double getX() {
		return x;
	}
	public void setX(Double x) {
		this.x = x;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	
	public String pos2add() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor") || 
				!para.bParas.containsKey("x") || !para.bParas.containsKey("y")){
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
		if(place == null || floor == null || place.length() == 0 || floor.length()==0
				|| x == null || y == null || x.toString().length() == 0 || y.toString().length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		//JSONObject obj = new JSONObject();
		try{
			array = positionService.pos2add(place, floor, x, y);
			//System.out.println(array.size());
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
			else{
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.toString());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(map != null){ map.clear(); map = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String add2pos() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("spot")){
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
		spot = para.bParas.get("spot");
		if(place == null || spot == null || place.length() == 0 || spot.length()==0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		//JSONObject obj = new JSONObject();
		try{
			array = positionService.add2pos(place, spot);
			
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
			else{
				map.put("success", true);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.toString());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
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
