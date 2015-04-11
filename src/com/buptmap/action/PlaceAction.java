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
import org.springframework.stereotype.Component;

import com.buptmap.Service.MapService;
import com.buptmap.Service.PlaceService;
import com.buptmap.Service.SpotService;
import com.buptmap.util.LogUtil;

import com.opensymphony.xwork2.interceptor.annotations.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
@Component("placeAction")
public class PlaceAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2174514397870141821L;
	private static final int GATEID = 4003;
	
	PlaceService placeService;
	MapService mapService;
	SpotService spotService;
	
	public PlaceService getPlaceService() {
		return placeService;
	}
	@Resource(name="placeService")
	public void setPlaceService(PlaceService placeService) {
		this.placeService = placeService;
	}

	public MapService getMapService() {
		return mapService;
	}
	@Resource(name="mapService")
	public void setMapService(MapService mapService) {
		this.mapService = mapService;
	}
	
	public SpotService getSpotService() {
		return spotService;
	}
	@Resource(name="spotService")
	public void setSpotService(SpotService spotService) {
		this.spotService = spotService;
	}

	private JSONObject resultObj;

	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	private String place;
	private String city;
	private String floor;
	private Double latitude;
	private Double longitude;
	private Double radius = 100.00;  //querying radius, meter, default 100 meters	
	private String type;  //querying type
	LogUtil log = new LogUtil();
	private Logger logger = Logger.getLogger(PlaceAction.class);
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		if(radius == null) this.radius = 100.00;
		else this.radius = radius;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public String passage() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String, Object> map = new HashMap<String, Object>();
		
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
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		place = para.bParas.get("place");
		if((place == null || place.length() == 0)){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		else{
			JSONArray passageArray = new JSONArray();
			JSONArray parentArray = new JSONArray();
			//String parent_id = null;
			try{
				System.out.println(para.bParas.get("place")+"place");
				//parent_id = spotService.getParentId(place);
				passageArray = placeService.passage(place, GATEID);
				parentArray = spotService.getCoord(place);
				//spotArray = spotService.all(place);
				//System.out.println(" placeArray.size():"+placeArray.size() + " mapArray.size():"+mapArray.size());
				if(passageArray != null && parentArray != null){
					if(passageArray.size() != 0  && parentArray.size() != 0){
						map.put("success", true);
						map.put("passage", passageArray);
						map.put("place", parentArray);
						//map.put("spot", spotArray);
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
				e.printStackTrace();
				logger.error(e.getMessage());
				map.put("success", false);
				map.put("message", e.toString());
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			finally{
				request = null;
				if(map != null) { map.clear(); map = null; }
				if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
				if(passageArray != null) { passageArray.clear(); passageArray = null; }
				if(parentArray != null) { parentArray.clear(); parentArray = null; }
				//lock.writeLock().unlock();
			}
		}
	}
	
	public String all_in_one() throws IOException{
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
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		place = para.bParas.get("place");
		if((place == null || place.length() == 0)){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		else{
			JSONArray placeArray = new JSONArray();
			JSONArray mapArray = new JSONArray();
			//JSONArray spotArray = new JSONArray();
			try{
				System.out.println(para.bParas.get("place")+"place");
				placeArray = placeService.all(place);
				mapArray = mapService.all(place);
				//spotArray = spotService.all(place);
				//System.out.println(" placeArray.size():"+placeArray.size() + " mapArray.size():"+mapArray.size());
				if(placeArray != null && mapArray != null){
					if(placeArray.size() != 0  && mapArray.size() != 0){
						map.put("success", true);
						map.put("place", placeArray);
						map.put("map", mapArray);
						//map.put("spot", spotArray);
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
				e.printStackTrace();
				logger.error(e.getMessage());
				map.put("success", false);
				map.put("message", e.toString());
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			finally{
				request = null;
				if(map != null) { map.clear(); map = null; }
				if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
				if(placeArray != null) { placeArray.clear(); placeArray = null; }
				if(mapArray != null) { mapArray.clear(); mapArray = null; }
				//lock.writeLock().unlock();
			}
		}
	}

	public String around() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if((!para.bParas.containsKey("place") &&
				(!para.bParas.containsKey("latitude") || !para.bParas.containsKey("longitude")))||
				!para.bParas.containsKey("city")){
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
		city = para.bParas.get("city");
		type = para.bParas.get("type");
		if(((place == null || place.length() == 0) && 
				(latitude == null || longitude == null)) || (city == null || city.length() == 0)){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		else{
			JSONArray array = new JSONArray();
			try{
				System.out.println(para.bParas.get("place")+"place");
				array = placeService.around(place, latitude, longitude, radius, type, city);

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
				e.printStackTrace();
				logger.error(e.getMessage());
				map.put("success", false);
				map.put("message", e.toString());
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			finally{
				request = null;
				if(map != null) { map.clear(); map = null; }
				if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
				if(array != null) { array.clear(); array = null; }
				//lock.writeLock().unlock();
			}
		}
	}

	public String detail() throws IOException{
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
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		place = para.bParas.get("place");
		System.out.println(place);
		if(place == null || place.length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}

		JSONArray array = new JSONArray();
		try{
			array = placeService.detail(place, openlevel);
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
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
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
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("city")){
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
		city = para.bParas.get("city");
		if(place == null || place.length() == 0 || city == null || city.length() == 0){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		//System.out.println(place);
		JSONArray array = new JSONArray();
		try{
			array = placeService.search(place, city);

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
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(array != null) { array.clear(); array = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * 获取place的公共设施
	 * */
	public String facilities() throws IOException {
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
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		place = para.bParas.get("place");
		floor = para.bParas.get("floor");
		if(place == null || place.length() == 0){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		//System.out.println(place);
		JSONArray array = new JSONArray();
		try{
			array = placeService.getPublicFacilities(place, floor);

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
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
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
