package com.buptmap.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.interceptor.annotations.After;

import com.buptmap.Service.SpotService;
import com.buptmap.util.CoreBusiness;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class SpotAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7173506518594234218L;
	private JSONArray jsonArray = null;
	private JSONObject resultObj;
	
	SpotService spotService;
	
	public SpotService getSpotService() {
		return spotService;
	}
	@Resource(name="spotService")
	public void setSpotService(SpotService spotService) {
		this.spotService = spotService;
	}
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(SpotAction.class);
	
	private String spot;
	private String place;
	private double radius=500.00;
	private double latitude;
	private double longitude;
	private String format = "json";
	
	public double getRadius() {
		return radius;
	}
	public void setRadius(Double radius) {
		if(radius==null) this.radius=100.00;
		else this.radius = radius;
	}
	
	public void xml() throws IOException{
		//HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/xml");
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = response.getWriter();
		String string = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
				"<result><person><name>tom</name><age>13</age></person><person><name>jim</name><age>16</age></person></result>";
		pw.write( string );return;
	}
	
	public void around1() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = response.getWriter();
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			logger.info(validCheckResult.get(false));
			pw.write(resultObj.toString());return;
		}
		
		spot = para.bParas.get("spot");
		if(spot == null || spot.length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParametersValueError);
			pw.write(resultObj.toString());return;
		}

		JSONArray array = new JSONArray();
		try{
			double[] pos = spotService.getLatAndLon(spot);
			if(pos == null || pos.length < 2){
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				logger.info(ErrorMessage.NoResultError);
				pw.write(resultObj.toString());return;
			}
			latitude = pos[0];longitude=pos[1];
			array = spotService.around(latitude, longitude, radius, para.bParas.get("type"));
			
			if(para.bParas.get("format").toLowerCase().equals("xml")){ format = "xml"; }
			
			if(array != null){
				if(format.equals("xml")){
					response.setContentType("text/xml");
					String result = null;
					if(array.size() != 0){
						result = CoreBusiness.formXML(array, true);
					}
					else {
						result = CoreBusiness.formXML(ErrorMessage.NoResultError, false);
						logger.info(ErrorMessage.NoResultError);
					}
					System.out.println(result);
					pw.write(result); 
					return;
				}
				else{
					if(array.size() != 0){
						map.put("success", true);
						map.put("total", array.size());
						map.put("rows", array);
						resultObj = JSONObject.fromObject(map);
					}
					else {
						map.put("success", true);
						map.put("message", ErrorMessage.NoResultError);
						logger.info(ErrorMessage.NoResultError);
						resultObj = JSONObject.fromObject(map);
					}
				}
			}
			else {
				map.put("success", true);
				map.put("total", 0);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			pw.write(resultObj.toString());return;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.toString());
			pw.write(resultObj.toString());return;
		}
		finally{
			if(map != null) map.clear();
			if(array != null) array.clear();
			pw.flush();
			pw.close();
		}
	}
	
	public String around() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("spot")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParameterError);
			return SUCCESS;
		}
		
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			logger.info(validCheckResult.get(false));
			return SUCCESS;
		}
		
		spot = para.bParas.get("spot");
		if(spot == null || spot.length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParametersValueError);
			return SUCCESS;
		}

		JSONArray array = new JSONArray();
		try{
			double[] pos = spotService.getLatAndLon(spot);
			if(pos == null || pos.length < 2){
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				logger.info(ErrorMessage.NoResultError);
				return SUCCESS;
			}
			latitude = pos[0];longitude=pos[1];
			array = spotService.around(latitude, longitude, radius, para.bParas.get("type"));
			
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
					map.put("total", 0);
					map.put("message", ErrorMessage.NoResultError);
					logger.info(ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
				}
			}
			else {
				map.put("success", false);
				map.put("total", 0);
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

	public String detail() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("spot")){
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
		
		spot = para.bParas.get("spot");
		if(spot == null || spot.length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParametersValueError);
			return SUCCESS;
		}

		JSONArray jsonArray = new JSONArray();
		try{
			jsonArray = spotService.detail(spot, openlevel);
			if(jsonArray != null){
				if(jsonArray.size() != 0){
					map.put("success", true);
					map.put("total", jsonArray.size());
					map.put("rows", jsonArray);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");
				}
				else {
					map.put("success", false);
					map.put("message", ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
					logger.info(ErrorMessage.NoResultError);
				}
				System.out.println("mark");
				return SUCCESS;
			}
			else {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				logger.info(ErrorMessage.NoResultError);
				return SUCCESS;
			}
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
			if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
			//lock.writeLock().unlock();
		}
	}
	
	
	public String all() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("spot")){
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
		
		spot = para.bParas.get("spot");
		if(spot == null || spot.length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParametersValueError);
			return SUCCESS;
		}

		JSONArray jsonArray = new JSONArray();
		try{
			jsonArray = spotService.all(spot);
			if(jsonArray != null){
				if(jsonArray.size() != 0){
					map.put("success", true);
					map.put("total", jsonArray.size());
					map.put("rows", jsonArray);
					resultObj = JSONObject.fromObject(map);
					logger.info("返回数据成功");
				}
				else {
					map.put("success", false);
					map.put("message", ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
					logger.info(ErrorMessage.NoResultError);
				}
				System.out.println("mark");
				return SUCCESS;
			}
			else {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				logger.info(ErrorMessage.NoResultError);
				return SUCCESS;
			}
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
			if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
			//lock.writeLock().unlock();
		}
	}
	

	public void search() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		Map<Boolean, String> validCheckResult = validCheck(request);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter pw = response.getWriter();
		String result = null;
		if(para.bParas.containsKey("format") && para.bParas.get("format").toLowerCase().equals("xml")){ 
			format = "xml";
			response.setContentType("text/xml");
		}
		String jsoncallback = "";
		if(para.bParas.containsKey("jsoncallback")){
			jsoncallback = para.bParas.get("jsoncallback");
		}
		
		if(!para.bParas.containsKey("spot")){
			if(format.equals("xml")){
				result = CoreBusiness.formXML(ErrorMessage.ParameterError, false);
			}
			else{
				result = CoreBusiness.formJson(ErrorMessage.ParameterError, false, jsoncallback);
			}
			logger.info(ErrorMessage.ParameterError);
			pw.write(result);return;
		}
		
		if (!validCheckResult.isEmpty()) {
			if(format.equals("xml")){
				result = CoreBusiness.formXML(validCheckResult.get(false), false);
			}
			else{
				result = CoreBusiness.formJson(validCheckResult.get(false), false, jsoncallback);
			}
			logger.info(validCheckResult.get(false));
			pw.write(result);return;
		}
		System.out.println("format:"+format);
		place = para.bParas.get("place");
		spot = para.bParas.get("spot");
		if(spot == null || spot.length() == 0) {
			if(format.equals("xml")){
				result = CoreBusiness.formXML(ErrorMessage.ParametersValueError, false);
			}
			else{
				result = CoreBusiness.formJson(ErrorMessage.ParametersValueError, false,jsoncallback);
			}
			logger.info(ErrorMessage.ParametersValueError);
			pw.write(result);return;
		}

		jsonArray = new JSONArray();
		try{
			jsonArray = spotService.search(spot, place, para.bParas.get("city"), para.bParas.get("type"));
			
			if(jsonArray != null){
				if(format.equals("xml")){
					if(jsonArray.size() != 0){
						result = CoreBusiness.formXML(jsonArray, true);
						logger.info("返回数据成功");
					}
					else {
						result = CoreBusiness.formXML(ErrorMessage.NoResultError, false);
						logger.info(ErrorMessage.NoResultError);
					}
				}
				else{
					if(jsonArray.size() != 0){
						result = CoreBusiness.formJson(jsonArray, true, jsoncallback);
						logger.info("返回数据成功");
					}
					else {
						logger.info(ErrorMessage.NoResultError);
						result = CoreBusiness.formJson(ErrorMessage.NoResultError, false, jsoncallback);
					}
				}
			}
			else {
				if(format.equals("xml")){
					result = CoreBusiness.formXML(ErrorMessage.NoResultError, false);
				}
				else{
					result = CoreBusiness.formJson(ErrorMessage.NoResultError, false, jsoncallback);
				}
				logger.info(ErrorMessage.NoResultError);
			}
			pw.write(result);return;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.toString());
			if(format.equals("xml")) result = CoreBusiness.formXML(e.toString(), false);
			else result = CoreBusiness.formJson(e.toString(), false, jsoncallback);
			pw.write(result); return;
		}
		finally{
			response = null;
			if(pw != null) { pw.flush(); pw.close(); }
			request = null;
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(result != null){ result = null; }
			if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public String search1() throws IOException{
		//lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			logger.info(validCheckResult.get(false));
			return SUCCESS;
		}
		
		if(!para.bParas.containsKey("spot")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParameterError);
			return SUCCESS;
		}
		place = para.bParas.get("place");
		spot = para.bParas.get("spot");
		if(spot == null || spot.length() == 0) {
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			logger.info(ErrorMessage.ParametersValueError);
			return SUCCESS;
		}

		JSONArray array = new JSONArray();
		try{
			array = spotService.search(spot, place, para.bParas.get("city"), para.bParas.get("type"));

			if(array != null){
				if(array.size() != 0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
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
