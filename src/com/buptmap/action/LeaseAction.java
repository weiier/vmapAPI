package com.buptmap.action;

import com.buptmap.Service.ChangeService;
import com.buptmap.Service.LeaseService;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LeaseAction {
	
	private String company_id;
	private String unit_id;
	private int version_id;
	private String floor_id;
	private String proxy_id;
	private int lease_id;
	private String rent;
	private LeaseService leaseService;
	private ChangeService changeService;
	private Map<String,Object> resultObj;
	
	public String selectNew(){
		JSONArray leaseArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			leaseArray = this.leaseService.selectNewLease();
			if (leaseArray != null && leaseArray.size() != 0) {
				map.put("success", true);
				map.put("total", leaseArray.size());
				map.put("leases", leaseArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(leaseArray != null) { leaseArray.clear(); leaseArray = null; }
		}
	}
	
	public String selectOld(){
		JSONArray leaseOldArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			leaseOldArray = this.leaseService.selectOldLease();
			if (leaseOldArray != null && leaseOldArray.size() != 0) {
				map.put("success", true);
				map.put("total", leaseOldArray.size());
				map.put("leases", leaseOldArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(leaseOldArray != null) { leaseOldArray.clear(); leaseOldArray = null; }
		}
	}

	public String proxySelectNew(){
		JSONArray proxyNewArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			proxyNewArray = this.leaseService.selectProxyNew(proxy_id, floor_id);
			if (proxyNewArray != null && proxyNewArray.size() != 0) {
				map.put("success", true);
				map.put("total", proxyNewArray.size());
				map.put("leases", proxyNewArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(proxyNewArray != null) { proxyNewArray.clear(); proxyNewArray = null; }
		}
	}
	
	public String proxySelectOld(){
		JSONArray proxyNewArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			proxyNewArray = this.leaseService.selectProxyOld(proxy_id, floor_id);
			if (proxyNewArray != null && proxyNewArray.size() != 0) {
				map.put("success", true);
				map.put("total", proxyNewArray.size());
				map.put("leases", proxyNewArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(proxyNewArray != null) { proxyNewArray.clear(); proxyNewArray = null; }
		}
	}
	
	public String allSelectNew(){
		JSONArray proxyNewArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			proxyNewArray = this.leaseService.selectProxysNew(floor_id);
			if (proxyNewArray != null && proxyNewArray.size() != 0) {
				map.put("success", true);
				map.put("total", proxyNewArray.size());
				map.put("leases", proxyNewArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(proxyNewArray != null) { proxyNewArray.clear(); proxyNewArray = null; }
		}
	}
	
	public String allSelectOld(){
		JSONArray proxyNewArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			proxyNewArray = this.leaseService.selectProxysOld(floor_id);
			if (proxyNewArray != null && proxyNewArray.size() != 0) {
				map.put("success", true);
				map.put("total", proxyNewArray.size());
				map.put("leases", proxyNewArray);
				resultObj = JSONObject.fromObject(map);
			}
			else {
				map.put("success", false);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(proxyNewArray != null) { proxyNewArray.clear(); proxyNewArray = null; }
		}
	}
	
	public String select(){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray leaseArray = new JSONArray();
		try {
			leaseArray = this.leaseService.select(lease_id);
			map.put("leases", leaseArray);
			map.put("total", leaseArray.size());
			map.put("success", true);
			resultObj = JSONObject.fromObject(map);
			return "success";//SUCCESS;
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);;
			return "success"; //SUCCESS;
		}
		
	}
	
	public String select_lease(){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray leaseArray = new JSONArray();
		try {
			leaseArray = this.leaseService.selectSelf(company_id);
			map.put("leases", leaseArray);
			map.put("total", leaseArray.size());
			map.put("success",true);
			resultObj = JSONObject.fromObject(map);
			return "success";//SUCCESS;
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success"; //SUCCESS;
		}
		
	}
	
	public String edit_rent(){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("success",this.leaseService.updateRent(lease_id, rent));
			map.put("message", "修改");
			resultObj = JSONObject.fromObject(map);
			return "success";//SUCCESS;
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success"; //SUCCESS;
		}
		
	}
	
	public String select_companys(){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONArray companyArray = new JSONArray();
		JSONArray array = new JSONArray();
		try {
			array = changeService.unit(unit_id, version_id);
			companyArray = this.leaseService.selectCompany(unit_id, version_id);
			if(array != null && array.size()!=0){
					map.put("success", true);
					map.put("count", companyArray.size());
					map.put("rows", array);
					map.put("company", companyArray);
					resultObj = JSONObject.fromObject(map);
			}else{
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return "success";//SUCCESS;
		} catch (Exception e) {
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success"; //SUCCESS;
		}
		
	}
	
	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public Map<String, Object> getResultObj() {
		return resultObj;
	}
	public void setResultObj(Map<String, Object> resultObj) {
		this.resultObj = resultObj;
	}
	public LeaseService getLeaseService() {
		return leaseService;
	}
	@Resource
	public void setLeaseService(LeaseService leaseService) {
		this.leaseService = leaseService;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	
	public int getVersion_id() {
		return version_id;
	}

	public void setVersion_id(int version_id) {
		this.version_id = version_id;
	}

	public ChangeService getChangeService() {
		return changeService;
	}

	public void setChangeService(ChangeService changeService) {
		this.changeService = changeService;
	}

	public int getLease_id() {
		return lease_id;
	}

	public void setLease_id(int lease_id) {
		this.lease_id = lease_id;
	}

	public String getRent() {
		return rent;
	}

	public void setRent(String rent) {
		this.rent = rent;
	}

	public String getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}

	public String getProxy_id() {
		return proxy_id;
	}

	public void setProxy_id(String proxy_id) {
		this.proxy_id = proxy_id;
	}

	
}
