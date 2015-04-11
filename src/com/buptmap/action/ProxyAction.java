package com.buptmap.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import com.buptmap.Service.ProxyService;
import com.buptmap.model.Proxy;
import com.opensymphony.xwork2.ActionSupport;

@Component
@Scope("prototype")
public class ProxyAction extends ActionSupport{

	private String proxy_id;
	//private String jsonstr;
	private ProxyService proxyService;
	private JSONObject resultObj;
	
	
	public String getProxy_id() {
		return proxy_id;
	}
	public void setProxy_id(String proxy_id) {
		this.proxy_id = proxy_id;
	}

	public ProxyService getProxyService() {
		return proxyService;
	}
	public void setProxyService(ProxyService proxyService) {
		this.proxyService = proxyService;
	}
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	public String select(){
		JSONArray proxyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			proxyArray = proxyService.Select(proxy_id);

			System.out.println(proxyArray.size());
			System.out.println(proxyArray);
			if (proxyArray != null && proxyArray.size() != 0) {
				map.put("success", true);
				map.put("total", proxyArray.size());
				map.put("proxy", proxyArray);
				resultObj = JSONObject.fromObject(map);
				
				
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(proxyArray != null) { proxyArray.clear(); proxyArray = null; }
		}
	}
	
	public String delete(){
		JSONArray proxyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

				map.put("success", proxyService.delete(proxy_id));
				map.put("proxy", proxyArray);
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
	}
	
	public String all(){
		JSONArray proxyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			proxyArray = proxyService.all();

			System.out.println(proxyArray.size());
			System.out.println(proxyArray);
			if (proxyArray != null && proxyArray.size() != 0) {
				map.put("success", true);
				map.put("total", proxyArray.size());
				map.put("proxy", proxyArray);
				resultObj = JSONObject.fromObject(map);
				
				
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(proxyArray != null) { proxyArray.clear(); proxyArray = null; }
		}
	}
	
	
}
