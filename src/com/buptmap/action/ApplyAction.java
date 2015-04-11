package com.buptmap.action;

import com.buptmap.Service.AdminService;
import com.buptmap.Service.ApplyService;
import com.buptmap.Service.CompanyService;
import com.buptmap.Service.ProxyService;
import com.buptmap.model.Apply;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ApplyAction {
	
	private String login_id;
	private String apply_id;
	private String proxy_id;
	private String floor_id;
	private int mode;
	private ApplyService applyService;
	private Map<String,Object> resultObj;
	
	
	public String select(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			applyArray = applyService.select(login_id,mode);
			System.out.println(applyArray.size());
			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String delete(){
		int id = Integer.parseInt(apply_id);
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

				map.put("success", applyService.delete(id));
				resultObj = JSONObject.fromObject(map);
				return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
	}
	
	public String updatesign(){
		int id = Integer.parseInt(apply_id);
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

				map.put("success", applyService.updatesign(id));
				resultObj = JSONObject.fromObject(map);
				return "success";
		}
		catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return "success";
		}
	}
	
	
	public String find(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		int id = Integer.parseInt(apply_id);
		try {

			applyArray = applyService.find(id);
			System.out.println(applyArray.size());
			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String adminSelectNew(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			applyArray = applyService.adminselectnew();
			System.out.println(applyArray.size());
			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String adminSelectOld(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			applyArray = applyService.adminselectold();
			System.out.println(applyArray.size());
			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}

	
	public String adminSelectInform(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			applyArray = applyService.adminselectinform();
			System.out.println(applyArray.size());
			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}

	public String proxySelectSelf(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			applyArray = applyService.proxyselectself(login_id);

			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String proxySelectAdmin(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {

			applyArray = applyService.proxyselectadmin(login_id);
			System.out.println(applyArray.size());
			System.out.println(applyArray);
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String proxySelectNew(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			applyArray = applyService.applyNew(proxy_id, floor_id);
			if(applyArray != null && applyArray.size() != 0){
				applyArray.addAll(applyService.commonNew());
			}else{
				applyArray = applyService.commonNew();
			}
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String proxySelectOld(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			applyArray = applyService.applyOld(proxy_id, floor_id);
			
			if(applyArray != null && applyArray.size() != 0){
				applyArray.addAll(applyService.commonOld());
			}else{
				applyArray = applyService.commonOld();
			}
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	

	public String allSelectNew(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			applyArray = applyService.applyAllnew(floor_id);
			
			if(applyArray != null && applyArray.size() != 0){
				applyArray.addAll(applyService.commonNew());
			}else{
				applyArray = applyService.commonNew();
			}
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String allSelectOld(){
		JSONArray applyArray = new JSONArray();
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			applyArray = applyService.applyAllold(floor_id);
			
			if(applyArray != null && applyArray.size() != 0){
				applyArray.addAll(applyService.commonOld());
			}else{
				applyArray = applyService.commonOld();
			}
			
			if (applyArray != null && applyArray.size() != 0) {
				map.put("success", true);
				map.put("total", applyArray.size());
				map.put("apply", applyArray);
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
			if(applyArray != null) { applyArray.clear(); applyArray = null; }
		}
	}
	
	public String getProxy_id() {
		return proxy_id;
	}
	public void setProxy_id(String proxy_id) {
		this.proxy_id = proxy_id;
	}
	public String getFloor_id() {
		return floor_id;
	}
	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	public String getApply_id() {
		return apply_id;
	}
	
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public void setApply_id(String apply_id) {
		this.apply_id = apply_id;
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
}
