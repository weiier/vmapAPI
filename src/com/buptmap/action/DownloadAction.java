package com.buptmap.action;

import java.io.InputStream;

import javax.servlet.Servlet;


import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.DownloadService;
import com.opensymphony.xwork2.ActionSupport;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;



@Component
@Scope("prototype")

public class DownloadAction extends ActionSupport{
	
	private DownloadService downloadService;
	private Map<String,Object> resultObj;
	private String jsonstr;
	
	
	
	public String getJsonstr() {
		return jsonstr;
	}



	public void setJsonstr(String jsonstr) {
		this.jsonstr = jsonstr;
	}



	public DownloadService getDownloadService() {
		return downloadService;
	}



	public void setDownloadService(DownloadService downloadService) {
		this.downloadService = downloadService;
	}





	public Map<String, Object> getResultObj() {
		return resultObj;
	}



	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}



	public String getfile(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			String unit_id = json.getString("unit_id");
			String version = json.getString("version");
			String floor = json.getString("floor_id");
			System.out.println("action");
			map.put("success", downloadService.getfile(unit_id, version,floor));
			
			resultObj = map;
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success";
		}
	}
	
	public String getUserData(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("success", downloadService.getUserData());
			resultObj = map;
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success";
		}
	}
	
	public String getLeaseData(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("success", downloadService.getLeaseData());
			resultObj = map;
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success";
		}
	}
	
	public String getConsultData(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("success", downloadService.getConsultData());
			resultObj = map;
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success";
		}
	}
	
	public String targetfile(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject json = new JSONObject(jsonstr);
			String unit_id = json.getString("unit_id");
			String version = json.getString("version");
			String floor = json.getString("floor_id");
			String proxy = json.getString("proxy_id");
			System.out.println("action");
			map.put("success", downloadService.targetfile(unit_id, version,floor,proxy));
			
			resultObj = map;
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success";
		}
	}
	

}
