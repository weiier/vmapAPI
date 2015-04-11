package com.buptmap.action;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;

import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.Service.LeaseService;
import com.buptmap.Service.ProxyService;
import com.buptmap.Service.UpdateService;
import com.buptmap.model.Lease;
import com.buptmap.model.Proxy;
import com.buptmap.Service.CompanyService;
import com.buptmap.model.Company;
import com.buptmap.Service.ApplyService;
import com.buptmap.model.Apply;;
@Component
@Scope("prototype")
public class InsertAction {
	
	private String jsonstr;
	private ProxyService proxyService;
	private CompanyService companyService;
	private ApplyService applyService;
	private LeaseService LeaseService;
	private UpdateService updateService;
	private Map<String,Object> resultObj;
	
	
	public String insert_proxy()
	{
		UUID idUuid =UUID.randomUUID();
		JSONArray proxyArray = new JSONArray();
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
			proxy.setState(0);
			proxy.setLast_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			proxy.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			if(this.proxyService.Insert(proxy) ){
				map.put("success", true);//.add(u, uChange, p, pChange, i, iChange));
				map.put("message","add");
			}
			else {
				map.put("success", false);//.add(u, uChange, p, pChange, i, iChange));
				map.put("message","proxy_id重复，请重新分配");
				
			}
			
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
	
	
	/**
	 * 插入申请单
	 * @return
	 * @throws IOException
	 */
	public String insert_lease() throws IOException{
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return "success";
		}
		
		try{
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			String unit_id = json.getString("unit_id");
			String company_id = json.getString("company_id");
			String version = json.getString("version");
			String rent = json.getString("rent");
			String floor_id = json.getString("floor_id");
			
			Lease l = new Lease();
			l.setCompany_id(company_id);
			l.setRent(rent);
			l.setState(0);
			l.setUnit_id(unit_id);
			l.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			l.setSign(0);
			l.setVersion_id(Integer.parseInt(version));
			l.setFloor_id(floor_id);
			
			map.put("success", this.LeaseService.insert(l));
			map.put("message", "插入申请单");

			resultObj = map;
			
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success";
		}
	}

	
	public String insert_company()
	{
		UUID idUuid =UUID.randomUUID();
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
			company.setCompany_id(idUuid.toString());
			company.setCompany_name_ch(json.getString("company_name_ch"));
			company.setCompany_name_en(json.getString("company_name_en"));
			company.setCon_per(json.getString("con_per"));
			company.setContact(json.getString("contact"));
			company.setEmail(json.getString("email"));
			company.setOther_info(json.getString("other_info"));
			company.setPhone(json.getString("phone"));
			company.setShow_name(json.getString("show_name"));
			company.setAlt_con(json.getString("alt_con"));
			company.setLast_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			company.setRegistration_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			map.put("success", this.companyService.Insert(company));//.add(u, uChange, p, pChange, i, iChange));
			map.put("message","add");
			map.put("company_id", idUuid.toString());
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
	
	public String insert_apply()
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
			String proxyStrings = json.getString("send_id");
			String[] proxys = proxyStrings.split(";");

			for (int i = 0; i < proxys.length; i++) {
				Apply apply = new Apply();
				apply.setCreate_id(json.getString("create_id"));
				apply.setSend_id(proxys[i]);
				apply.setTitle(json.getString("title"));
				apply.setSign(json.getString("sign"));
				apply.setContent(json.getString("content"));
				apply.setState(json.getString("state"));
				apply.setReply(json.getString("reply"));
				apply.setFloor_id(json.getString("floor_id"));
				apply.setVersion_id(json.getString("version_id"));
				apply.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				map.put("success", this.applyService.insert(apply));
			}
			map.put("message","add");
			resultObj = map;
			return "success";//SUCCESS;
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return "success"; //SUCCESS;
		}
	}
	
	public LeaseService getLeaseService() {
		return LeaseService;
	}

	@Resource
	public void setLeaseService(LeaseService leaseService) {
		LeaseService = leaseService;
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
	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}


	public UpdateService getUpdateService() {
		return updateService;
	}

	@Resource
	public void setUpdateService(UpdateService updateService) {
		this.updateService = updateService;
	}
	
}
	
	

	
	
	
	