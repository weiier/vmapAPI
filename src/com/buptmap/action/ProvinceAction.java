package com.buptmap.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.buptmap.Service.ProvinceService;
import com.buptmap.model.Province;
import com.opensymphony.xwork2.ActionSupport;

@Component("provinceAction")
public class ProvinceAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8300138977024241619L;
	private JSONObject resultObj;
	
	public JSONObject getResultObj() {
		return resultObj;
	}
	
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	private String unitName;
	
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String search() {
		List<Province> places = new ArrayList<Province>();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		ProvinceService provinceService = (ProvinceService)applicationContext.getBean("provinceService");
		places = provinceService.Search(unitName);
		
		Map<String,Object> map = new HashMap<String,Object>();
		JSONArray array = JSONArray.fromObject(places);
		//System.out.println(array.size());
		if(array.size() != 0){
			map.put("success", true);
			map.put("total", places.size());
			map.put("rows", array);
			resultObj = JSONObject.fromObject(map);
		}
		else {
			map.put("success", true);
			map.put("total", 0);
			resultObj = JSONObject.fromObject(map);
		}
		return SUCCESS;
	}
	
	public String Add(){
		Province province = new Province();
		province.setProvince_id(2);
		province.setName("婀栧寳鐪�");
		province.setSeq(2);
		province.setLast_modify_time(new Date());
		
		HttpServletResponse resp=ServletActionContext.getResponse();  
        resp.setContentType("application/json");  
        
		Map<String,Object> map = new HashMap<String,Object>();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		ProvinceService provinceService = (ProvinceService)applicationContext.getBean("provinceService");
		System.out.println(province.getName());
		if(provinceService.AddItem(province)){
			map.put("success", true);
			map.put("message", "鏂板province鎴愬姛锛乸rovince_id涓猴細"+province.getProvince_id());
			resultObj = JSONObject.fromObject(map);
		}
		else {
			map.put("success", true);
			map.put("message", "鏂板province澶辫触锛�");
			resultObj = JSONObject.fromObject(map);
		}
		System.out.println(resultObj);
		return SUCCESS;
	}
}
