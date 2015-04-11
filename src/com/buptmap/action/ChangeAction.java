package com.buptmap.action;

import java.io.IOException;
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

import com.buptmap.Service.ChangeService;
import com.buptmap.Service.FloorService;
import com.buptmap.Service.SpotNService;
import com.buptmap.Service.UnitService;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component
@Scope("prototype")
public class ChangeAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(ChangeAction.class);
	private SpotNService spotNService;
	private UnitService unitService;
	private ChangeService changeService;
	private FloorService floorService;
	private JSONObject resultObj;
	private String unit;
	private String parent;
	private String floor;
	private String create;
	private String modify;
	private int version;
	/**
	 * the changes of units by time zone
	 * @return all unitChanges  by a certain version 
	 * @throws IOException
	 */
	public String units() throws IOException{
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
		create = para.bParas.get("create");
		modify = para.bParas.get("modify");
		if(unit == null|| unit.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		try{
			array = changeService.units(unit, create, modify);
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
	 * 现在使用，点击查看
	 * @return the last unitChange  by a certain version 
	 * @throws IOException
	 */
	public String unit() throws IOException{
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
		version = Integer.parseInt(para.bParas.get("version"));
		if(unit == null|| unit.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		try{
			array = changeService.unit(unit, version);
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
	 *  all changes of a certain unit and versions
	 *  parentid is unit that has indoormap,unitid is one unit
	 * @return
	 * @throws IOException
	 */
	public String all() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		if(!para.bParas.containsKey("unit")&&!para.bParas.containsKey("parent")){
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
		
		version = Integer.parseInt(para.bParas.get("version"));
		
		JSONArray array = new JSONArray();
		try{
			if(parent!=null){
				array = changeService.all(parent, version);
			}else if(unit!=null){
				array = changeService.one(unit, version);
			}
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
	 * 现在使用，代理点击查看,返回unit及公司信息
	 * @return the last unitChange  by a certain version 
	 * @throws IOException
	 */
	public String unit_proxy() throws IOException{
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
		version = Integer.parseInt(para.bParas.get("version"));
		if(unit == null|| unit.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		JSONArray array_com = new JSONArray();
		try{
			array = changeService.unit(unit, version);
			array_com = changeService.find_company(unit, version);
			if(array != null&&array.size()!=0){
					map.put("success", true);
					map.put("count", array_com.size());
					map.put("rows", array);
					map.put("company", array_com);
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

	
	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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
	
	public ChangeService getChangeService() {
		return changeService;
	}
	@Resource
	public void setChangeService(ChangeService changeService) {
		this.changeService = changeService;
	}

	@After
	public void destory(){
		logger.info("request end...");
		if(resultObj != null){resultObj.clear();resultObj = null;}
	}
}
