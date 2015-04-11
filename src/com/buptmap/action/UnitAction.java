package com.buptmap.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.FloorService;
import com.buptmap.Service.SpotNService;
import com.buptmap.Service.UnitService;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component
@Scope("prototype")
public class UnitAction extends BaseAction {
	/**
	 * @author weiier
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(UnitAction.class);
	private SpotNService spotNService;
	private UnitService unitService;
	private FloorService floorService;
	private JSONObject resultObj;
	private String unit;
	private String floor;
	private String version;
	private String create;
	private String modify;
	private String key;
	private String low;
	private String high;
	/**
	 * units has indoormap
	 * @return 
	 * @throws IOException
	 */
	public String search() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		JSONArray array = new JSONArray();
		try{
			array = unitService.all();
			if(array != null&&array.size()!=0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");	
			}else{
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
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}finally{
			request = null;
			if(map != null){map.clear();map = null;}
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
			if(array != null){array.clear();array = null;}
		}
	}
	
	/**
	 * search by keyword
	 * @return 
	 * @throws IOException
	 */
	public String query() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if(key.equals("null")||key.equals("")||low.equals("null")||low.equals("")||high.equals("null")||high.equals("")
				||floor.equals("null")||floor.equals("")||unit.equals("null")||unit.equals("")||version.equals("null")||version.equals("")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		JSONArray array = new JSONArray();

		try{
			array = unitService.search(key,low,high,floor,unit,version);
			if(array != null&&array.size()!=0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");	
			}else{
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
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}finally{
			request = null;
			if(map != null){map.clear();map = null;}
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
			if(array != null){array.clear();array = null;}
		}
	}
	
	/**
	 * units by certain version certain floor certain unit
	 * @return
	 * @throws IOException
	 */
	public String around() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		if(!para.bParas.containsKey("unit")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		unit  = para.bParas.get("unit");
		floor = para.bParas.get("floor");
		version = para.bParas.get("version");
		if(unit == null|| unit.length() == 0||version==null||floor==null){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		JSONArray frame = new JSONArray();
		try{
			array = spotNService.all(unit, floor,Integer.parseInt(version));
			frame = this.floorService.detail(unit, floor);
			if(array != null&&array.size()!=0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					map.put("F", frame);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");	
			}else{
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
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}finally{
			request = null;
			if(map != null){map.clear();map = null;}
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
			if(array != null){array.clear();array = null;}
		}
	}
	/**
	 * all versions by a unit
	 * @return 
	 * @throws IOException
	 */
	public String version() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		if(!para.bParas.containsKey("unit")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		unit  = para.bParas.get("unit");
		if(unit == null|| unit.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		try{
			array = unitService.version(unit);
			if(array != null&&array.size()!=0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");	
			}else{
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
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}finally{
			request = null;
			if(map != null){map.clear();map = null;}
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
			if(array != null){array.clear();array = null;}
		}
	}
	
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	public Logger getLogger() {
		return logger;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public FloorService getFloorService() {
		return floorService;
	}
	@Resource
	public void setFloorService(FloorService floorService) {
		this.floorService = floorService;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCreate() {
		return create;
	}

	public void setCreate(String create) {
		this.create = create;
	}

	public String getModify() {
		return modify;
	}

	public void setModify(String modify) {
		this.modify = modify;
	}

	
	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		try {
			this.key = new String(key.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public UnitService getUnitService() {
		return unitService;
	}
	@Resource
	public void setUnitService(UnitService unitService) {
		this.unitService = unitService;
	}
	public SpotNService getSpotNService() {
		return spotNService;
	}
	@Resource
	public void setSpotNService(SpotNService spotNService) {
		this.spotNService = spotNService;
	}
	@After
	public void destory(){
		logger.info("request end...");
		if(resultObj != null){resultObj.clear();resultObj = null;}
	}
}
