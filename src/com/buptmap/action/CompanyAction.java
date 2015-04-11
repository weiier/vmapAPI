package com.buptmap.action;
import java.io.File;
import jxl.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import com.buptmap.Service.CompanyService;
import com.buptmap.model.Company;
import com.opensymphony.xwork2.ActionSupport;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WritableCell;
@Component
@Scope("prototype")
public class CompanyAction extends ActionSupport {
	
	private String company_id;
	private String jsonstr;
	private String email;
	private String name;
	private String phone;
	private CompanyService companyService;
	private JSONObject resultObj;
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getJsonstr() {
		return jsonstr;
	}
	public void setJsonstr(String jsonstr) {
		try {
			this.jsonstr = new String(jsonstr.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public CompanyService getCompanyService() {
		return companyService;
	}
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	public String select(){
		JSONArray companyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			companyArray = companyService.Select(company_id);
			if (companyArray != null && companyArray.size() != 0) {
				map.put("success", true);
				map.put("total", companyArray.size());
				map.put("company", companyArray);
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
			if(companyArray != null) { companyArray.clear(); companyArray = null; }
		}
	}
	
	public String all(){
		JSONArray companyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			companyArray = companyService.All();
			if (companyArray != null && companyArray.size() != 0) {
				map.put("success", true);
				map.put("total", companyArray.size());
				map.put("company", companyArray);
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
			if(companyArray != null) { companyArray.clear(); companyArray = null; }
		}
	}
	
	public String delete(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
				map.put("success", companyService.delete(company_id));
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
	
	public String checkName(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
				map.put("success", companyService.checkName(name));
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
	
	public String checkPhone(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
				map.put("success", companyService.checkPhone(phone));
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
	
	public String checkEmail(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
				map.put("success", companyService.checkEmail(email));
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
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	

}
