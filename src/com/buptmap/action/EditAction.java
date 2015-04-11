package com.buptmap.action;

import com.buptmap.Service.ProxyService;
import com.buptmap.model.Proxy;
import com.buptmap.Service.ApplyService;
import com.buptmap.model.Apply;
import com.buptmap.Service.CompanyService;
import com.buptmap.model.Company;
import com.buptmap.Service.AdminService;
import com.buptmap.model.Admin;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class EditAction {
	
	private String jsonstr;
	private ProxyService proxyService;
	private CompanyService companyService;
	private ApplyService applyService;
	private AdminService adminService;
	private Map<String,Object> resultObj;

	
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
	public ProxyService getProxyService() {
		return proxyService;
	}
	public void setProxyService(ProxyService proxyService) {
		this.proxyService = proxyService;
	}
	public CompanyService getCompanyService() {
		return companyService;
	}
	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
	
	public ApplyService getApplyService() {
		return applyService;
	}
	public void setApplyService(ApplyService applyService) {
		this.applyService = applyService;
	}
	public AdminService getAdminService() {
		return adminService;
	}
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	
	public String edit_proxy()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return "success";
		}

		try {
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			Proxy proxy = new Proxy();
			proxy.setProxy_id(json.getString("proxy_id"));
			proxy.setAddress(json.getString("address"));
			proxy.setEmail(json.getString("email"));
			proxy.setOther_info(json.getString("other_info"));
			proxy.setPassword(json.getString("password"));
			proxy.setPhone(json.getString("phone"));
			proxy.setProxy_color(json.getString("proxy_color"));
			proxy.setProxy_name(json.getString("proxy_name"));
			proxy.setCon_per(json.getString("con_per"));
			proxy.setContact(json.getString("contact"));
			proxy.setShow_name(json.getString("show_name"));
			proxy.setFloor_id(json.getString("floor_id"));
			proxy.setLast_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			proxy.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			map.put("success", this.proxyService.edit(proxy));//.add(u, uChange, p, pChange, i, iChange));
			map.put("message","edit");
			resultObj = map;
			return "success";//SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception

			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success"; //SUCCESS;
		}
	}
	
	public String edit_company()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return "success";
		}

		try {
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			Company company = new Company();
			company.setAddress_ch(json.getString("address_ch"));
			company.setAddress_en(json.getString("address_en"));
			company.setCompany_color(json.getString("company_color"));
			company.setCompany_id(json.getString("company_id"));
			company.setCompany_name_ch(json.getString("company_name_ch"));
			company.setCompany_name_en(json.getString("company_name_en"));
			company.setCon_per(json.getString("con_per"));
			company.setContact(json.getString("contact"));
			company.setEmail(json.getString("email"));
			company.setOther_info(json.getString("other_info"));
			company.setPhone(json.getString("phone"));
			company.setShow_name(json.getString("show_name"));
			company.setAlt_con(json.getString("alt_con"));
		
			map.put("success", this.companyService.edit(company));//.add(u, uChange, p, pChange, i, iChange));
			map.put("message","edit");
			resultObj = map;
			return "success";//SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception

			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success"; //SUCCESS;
		}
		
	}
		public String edit_apply()
		{
			Map<String, Object> map = new HashMap<String, Object>();
			
			if(jsonstr == null|| jsonstr.length() == 0){
				map.put("success", false);
				map.put("message",ErrorMessage.ParametersValueError);
				resultObj = map;
				return "success";
			}

			try {
				System.out.println(jsonstr);
				JSONObject json = new JSONObject(jsonstr);
				Apply apply = this.applyService.selectById(Integer.parseInt(json.getString("apply_id")));
				
				if(apply != null){
					apply.setTitle(json.getString("title"));
					apply.setSign(json.getString("sign"));
					apply.setContent(json.getString("content"));
					apply.setState(json.getString("state"));
					apply.setReply(json.getString("reply"));
					apply.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					map.put("success", this.applyService.edit(apply));//.add(u, uChange, p, pChange, i, iChange));
					map.put("message","edit");
				}else{
					map.put("success", false);
					map.put("message", "apply not found");
				}
				resultObj = map;
				return "success";
			} catch (Exception e) {
				map.put("success", false);
				map.put("message", e.toString());
				resultObj = map;
				return "success"; //SUCCESS;
			}
	}
	
	public String edit_admin()
	{
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return "success";
		}

		try {
			Admin admin = new Admin();
			JSONObject json = new JSONObject(jsonstr);
			
			admin.setPassword(json.getString("password"));
			admin.setUsername("admin");
			admin.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
			map.put("success", this.adminService.edit(admin));//.add(u, uChange, p, pChange, i, iChange));
			map.put("message","edit");
			resultObj = map;
			return "success";//SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception

			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success"; //SUCCESS;
		}
	}


}
