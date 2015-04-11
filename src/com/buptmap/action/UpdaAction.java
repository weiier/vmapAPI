package com.buptmap.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.ProxyService;
import com.buptmap.Service.UpdateService;
import com.buptmap.Service.LeaseService;
import com.buptmap.model.Indoor;
import com.buptmap.model.IndoorChange;
import com.buptmap.model.Poi;
import com.buptmap.model.PoiChange;
import com.buptmap.model.UnitChange;
import com.buptmap.model.Units;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component
@Scope("prototype")
public class UpdaAction extends BaseAction{
	private Logger logger = Logger.getLogger(ChangeAction.class);
	private UpdateService updateService;
	private LeaseService leaseService;
	private ProxyService proxyService;
	private String jsonstr;
	private Map<String,Object> resultObj;

	/**
	 * Allocation proxy for block
	 * @return
	 * @throws IOException
	 */
	public String update_company() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			String unit_id = json.getString("unit_id");
			String company_id = json.getString("company_id");
			String version = json.getString("version");
			String mark = json.getString("mark");
			
			map.put("success", this.updateService.update_company(unit_id, company_id,version,mark));
			map.put("message", "更新成功");

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}		
	
	/**
	 * 更新申请单状态及block状态
	 * @return	json
	 * @throws IOException
	 */
	public String update_lease() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			String lease_id = json.getString("lease_id");
			String mark = json.getString("mark");
			
			map.put("success",this.leaseService.updateState(lease_id, mark));
			map.put("message", "更新成功");

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}		
	
	public String update_proxy() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			String unit_id = json.getString("unit_id");
			String proxy_id = json.getString("proxy_id");
			String version = json.getString("version");
			
			map.put("success",updateService.update_proxy(unit_id, proxy_id,version)&&proxyService.updateState(proxy_id, 1));
			map.put("message", "分配成功");

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}	

	/**
	 * Allocation company for block
	 * @return ok 
	 * @throws IOException
	 */
	public String units() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			UnitChange unit = new UnitChange();
			IndoorChange indoor = new IndoorChange();
			String version = json.getString("version");
			
			unit.setAction(2);
			unit.setAddress(json.getString("address"));
			unit.setAlias(json.getString("alias"));
			//unit.setAvailable(4);
			unit.setChecked(0);
			unit.setCreate_time(new Date());
			unit.setCreator("dr");
			unit.setDescription(json.getString("description"));
			unit.setDetail_info(json.getString("detail"));
			unit.setEmail(json.getString("email"));
			unit.setKeyword(json.getString("keyword"));
			unit.setLogo(json.getString("logo"));
			unit.setName(json.getString("name"));
			unit.setOpening_time(json.getString("open_time"));
			unit.setPhone(json.getString("phone"));
			unit.setUnit_id(json.getString("unit_id"));
			unit.setParent_unit_id(json.getString("parent_id"));
			unit.setShow_name(json.getString("show_name"));
			unit.setWebsite(json.getString("website"));
			
			unit.setBlock_area(json.getString("block_area"));
			unit.setBlock_length(json.getString("block_length"));
			unit.setBlock_state(json.getString("block_state"));
			unit.setBlock_width(json.getString("block_width"));
		
			unit.setBlock_rent(json.getString("block_rent"));
			unit.setBlock_tonnage(json.getString("block_tonnage"));
			unit.setBlock_discount(json.getString("block_discount"));
			
			unit.setProxy_id(json.getString("proxy_id"));
			unit.setReco_company_id(json.getString("reco_company_id"));
			unit.setRese_company_id(json.getString("rese_company_id"));
			unit.setSign_company_id(json.getString("sign_company_id"));
			unit.setPay_company_id(json.getString("pay_company_id"));
			unit.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			
			indoor.setAddress(json.getString("address"));
			//
			indoor.setBooth_num(json.getString("booth_num"));
			indoor.setCoord_x(Double.parseDouble(json.getString("coord_x")));
			indoor.setCoord_y(Double.parseDouble(json.getString("coord_y")));
			indoor.setCreate_time(new Date());
			indoor.setCreator("dr");
			indoor.setFloor_id(json.getString("floor_id"));
			indoor.setFrame(json.getString("frame"));
			indoor.setMax_x(Double.parseDouble(json.getString("max_x")));
			indoor.setMax_y(Double.parseDouble(json.getString("max_y")));
			indoor.setMin_x(Double.parseDouble(json.getString("min_x")));
			indoor.setMin_y(Double.parseDouble(json.getString("min_y")));
			indoor.setParent_unit_id(json.getString("parent_id"));
			//i.setType("商铺");
			indoor.setType(json.getString("type"));
			indoor.setUnit_id(json.getString("unit_id"));
			indoor.setAction(2);
			indoor.setChecked(0);
			indoor.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			
			
			map.put("success", this.updateService.updateUnits(unit,indoor,version));
			map.put("message", "Update");

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}	
	

	/**
	 * 	update indoor
	 * @return
	 * @throws IOException
	 */
	public String indoors() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			IndoorChange iChange = new IndoorChange();
			iChange.setAction(2);
			iChange.setAvailable(4);
			iChange.setChecked(0);
			iChange.setCoord_x(Double.parseDouble(json.getString("coord_x")));
			iChange.setCoord_y((Double.parseDouble(json.getString("coord_y"))));
			iChange.setCreate_time(new Date());
			iChange.setCreator("dr");
			iChange.setFrame(json.getString("frame"));
			iChange.setMax_x((Double.parseDouble(json.getString("max_x"))));
			iChange.setMax_y((Double.parseDouble(json.getString("max_y"))));
			iChange.setMin_x((Double.parseDouble(json.getString("min_x"))));
			iChange.setMin_y((Double.parseDouble(json.getString("min_y"))));
			iChange.setUnit_id(json.getString("unit_id"));
			iChange.setParent_unit_id(json.getString("parentid"));
			String version = json.getString("version");
			
			map.put("success", this.updateService.updateIndoor(iChange,version));
			map.put("message", "Update indoors");

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}	
	
	/**
	 * add all unit,indoor,poi and changes
	 * @return
	 * @throws IOException
	 */
	public String all() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			UnitChange uChange = new UnitChange();
			Units u = new Units();
			Poi p = new Poi();
			PoiChange pChange = new PoiChange();
			Indoor i = new Indoor();
			IndoorChange iChange= new IndoorChange();
			String version = json.getString("version");
			
			u.setAddress(json.getString("address"));
			u.setAlias(json.getString("alias"));
			u.setAvailable(4);
			u.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			u.setCreator("admin");
			u.setDescription(json.getString("description"));
			u.setDetail_info(json.getString("detail"));
			u.setEmail(json.getString("email"));
			u.setG_outdoor_id("00000000-0000-0000-0000-000000000000");
			u.setHas_indoor_map(0);
			u.setHas_outdoor("0");
			u.setKeyword(json.getString("keyword"));
			u.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			u.setLogo(json.getString("logo"));
			u.setModifier("dr");
			u.setName(json.getString("name"));
			u.setOpening_time(json.getString("open_time"));
			u.setPhone(json.getString("phone"));
			u.setShow_name(json.getString("show_name"));
			u.setUnit_brand_id(0);
			u.setUnit_id(UUID.randomUUID().toString());
			u.setUnit_type_id(1315);
			u.setWebsite(json.getString("website"));
			
			u.setBlock_area(json.getString("block_area"));
			u.setBlock_length(json.getString("block_length"));
			u.setBlock_state(json.getString("block_state"));
			u.setBlock_width(json.getString("block_width"));
			
			u.setBlock_rent(json.getString("block_rent"));
			u.setBlock_tonnage(json.getString("block_tonnage"));
			u.setBlock_discount(json.getString("block_discount"));
			
			uChange.setAddress(json.getString("address"));
			uChange.setAlias(json.getString("alias"));
			uChange.setAvailable(5);
			uChange.setCreate_time(new Date());
			uChange.setCreator("dr");
			uChange.setDescription(json.getString("description"));
			uChange.setDetail_info(json.getString("detail"));
			uChange.setEmail(json.getString("email"));
			uChange.setG_outdoor_id("00000000-0000-0000-0000-000000000000");
			uChange.setHas_indoor_map(0);
			uChange.setHas_outdoor(0);
			uChange.setKeyword(json.getString("keyword"));
			uChange.setLogo(json.getString("logo"));
			uChange.setName(json.getString("name"));
			uChange.setOpening_time(json.getString("open_time"));
			uChange.setPhone(json.getString("phone"));
			uChange.setShow_name(json.getString("show_name"));
			uChange.setUnit_brand_id(0);
			uChange.setUnit_id(u.getUnit_id());
			uChange.setUnit_type_id(1315);
			uChange.setWebsite(json.getString("website"));		
			uChange.setAction(1);
			uChange.setChecked(0);
			uChange.setParent_unit_id(json.getString("parent_id"));
			
			uChange.setBlock_area(json.getString("block_area"));
			uChange.setBlock_length(json.getString("block_length"));
			uChange.setBlock_state(json.getString("block_state"));
			uChange.setBlock_width(json.getString("block_width"));
			
			uChange.setBlock_rent(json.getString("block_rent"));
			uChange.setBlock_tonnage(json.getString("block_tonnage"));
			uChange.setBlock_discount(json.getString("block_discount"));
			
			uChange.setProxy_id(json.getString("proxy_id"));
			uChange.setReco_company_id(json.getString("reco_company_id"));
			uChange.setRese_company_id(json.getString("rese_company_id"));
			uChange.setSign_company_id(json.getString("sign_company_id"));
			uChange.setPay_company_id(json.getString("pay_company_id"));
			uChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			
			p.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			p.setCreator("admin");
			p.setHas_indoor_map(0);
			p.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			p.setModifier("dr");
			p.setPoi_id(UUID.randomUUID().toString());
			p.setUnit_id(u.getUnit_id());
			
			pChange.setCreate_time(new Date());
			pChange.setCreator("dr");
			pChange.setHas_indoor_map(0);
			pChange.setPoi_id(p.getPoi_id());
			pChange.setUnit_id(u.getUnit_id());
			pChange.setAction(1);
			pChange.setChecked(0);
			
			i.setAddress(json.getString("address"));
			i.setAvailable(4);
			i.setBooth_num(json.getString("booth_num"));
			i.setCoord_x(Double.parseDouble(json.getString("coord_x")));
			i.setCoord_y(Double.parseDouble(json.getString("coord_y")));
			i.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			i.setCreator("admin");
			i.setFloor_id(json.getString("floor_id"));
			i.setFrame(json.getString("frame"));
			i.setIndoor_id(UUID.randomUUID().toString());
			i.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
			i.setMax_x(Double.parseDouble(json.getString("max_x")));
			i.setMax_y(Double.parseDouble(json.getString("max_y")));
			i.setMin_x(Double.parseDouble(json.getString("min_x")));
			i.setMin_y(Double.parseDouble(json.getString("min_y")));
			i.setModifier("dr");
			i.setParent_unit_id(json.getString("parent_id"));
			i.setPoi_id(p.getPoi_id());
			//i.setType("商铺");
			i.setType(json.getString("type"));
			i.setUnit_id(u.getUnit_id());
			
			iChange.setAddress(json.getString("address"));
			iChange.setAvailable(5);
			iChange.setBooth_num(json.getString("booth_num"));
			iChange.setCoord_x(Double.parseDouble(json.getString("coord_x")));
			iChange.setCoord_y(Double.parseDouble(json.getString("coord_y")));
			iChange.setCreate_time(new Date());
			iChange.setCreator("dr");
			iChange.setFloor_id(json.getString("floor_id"));
			iChange.setFrame(json.getString("frame"));
			iChange.setIndoor_id(i.getIndoor_id());
			iChange.setMax_x(Double.parseDouble(json.getString("max_x")));
			iChange.setMax_y(Double.parseDouble(json.getString("max_y")));
			iChange.setMin_x(Double.parseDouble(json.getString("min_x")));
			iChange.setMin_y(Double.parseDouble(json.getString("min_y")));
			iChange.setParent_unit_id(json.getString("parent_id"));
			iChange.setPoi_id(p.getPoi_id());
			//i.setType("商铺");
			iChange.setType(json.getString("type"));
			iChange.setUnit_id(u.getUnit_id());
			iChange.setAction(1);
			iChange.setChecked(0);
			iChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			
			
			map.put("success", this.updateService.add(u, uChange, p, pChange, i, iChange));
			map.put("message","Add");
			resultObj = map;
			return SUCCESS;
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}

	/**
	 * delete unit,indoor
	 * @return
	 * @throws IOException
	 */
	public String delete() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			//{'unit_id':'test','parent_id':'test'}
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			String unit_id = json.getString("unit_id");
			String parent_id = json.getString("parent_id");
			String version = json.getString("version");
			if(unit_id==null||parent_id==null){
				map.put("success", false);
				map.put("message", ErrorMessage.ParametersValueError);
			}else{
				map.put("success", this.updateService.delete(unit_id, parent_id,version));
				map.put("message", "Delete");
			}

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}	
	
	
	public String emerger() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object>  map = new HashMap<String,Object>();
		
		if(!validCheckResult.isEmpty()){
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(ErrorMessage.ParameterError);
			resultObj = map;
			return SUCCESS;
		}

		if(jsonstr == null|| jsonstr.length() == 0){
			map.put("success", false);
			map.put("message",ErrorMessage.ParametersValueError);
			resultObj = map;
			return SUCCESS;
		}
		
		try{
			//{'unit_id':'test','parent_id':'test'}
			System.out.println(jsonstr);
			JSONObject json = new JSONObject(jsonstr);
			String unit_ids = json.getString("delete_id");
			String parent_id = json.getString("parent_id");
			String version = json.getString("version");
			if(unit_ids==null||parent_id==null){
				map.put("success", false);
				map.put("message", ErrorMessage.ParametersValueError);
			}else{
				String[] unit_id = unit_ids.split("\\*");
				for(int  j = 0; j < unit_id.length; j ++)
				{
					map.put("删除：" + unit_id[j], this.updateService.delete(unit_id[j], parent_id,version));
				}
				
				UnitChange uChange = new UnitChange();
				Units u = new Units();
				Poi p = new Poi();
				PoiChange pChange = new PoiChange();
				Indoor i = new Indoor();
				IndoorChange iChange= new IndoorChange();
				
				u.setAddress(json.getString("address"));
				u.setAlias(json.getString("alias"));
				u.setAvailable(4);
				u.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
				u.setCreator("admin");
				u.setDescription(json.getString("description"));
				u.setDetail_info(json.getString("detail"));
				u.setEmail(json.getString("email"));
				u.setG_outdoor_id("00000000-0000-0000-0000-000000000000");
				u.setHas_indoor_map(0);
				u.setHas_outdoor("0");
				u.setKeyword(json.getString("keyword"));
				u.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
				u.setLogo(json.getString("logo"));
				u.setModifier("dr");
				u.setName(json.getString("name"));
				u.setOpening_time(json.getString("open_time"));
				u.setPhone(json.getString("phone"));
				u.setShow_name(json.getString("show_name"));
				u.setUnit_brand_id(0);
				u.setUnit_id(UUID.randomUUID().toString());
				u.setUnit_type_id(1315);
				u.setWebsite(json.getString("website"));
				
				u.setBlock_area(json.getString("block_area"));
				u.setBlock_length(json.getString("block_length"));
				u.setBlock_state(json.getString("block_state"));
				u.setBlock_width(json.getString("block_width"));
				
				u.setBlock_rent(json.getString("block_rent"));
				u.setBlock_tonnage(json.getString("block_tonnage"));
				u.setBlock_discount(json.getString("block_discount"));
				
				uChange.setAddress(json.getString("address"));
				uChange.setAlias(json.getString("alias"));
				uChange.setAvailable(5);
				uChange.setCreate_time(new Date());
				uChange.setCreator("dr");
				uChange.setDescription(json.getString("description"));
				uChange.setDetail_info(json.getString("detail"));
				uChange.setEmail(json.getString("email"));
				uChange.setG_outdoor_id("00000000-0000-0000-0000-000000000000");
				uChange.setHas_indoor_map(0);
				uChange.setHas_outdoor(0);
				uChange.setKeyword(json.getString("keyword"));
				uChange.setLogo(json.getString("logo"));
				uChange.setName(json.getString("name"));
				uChange.setOpening_time(json.getString("open_time"));
				uChange.setPhone(json.getString("phone"));
				uChange.setShow_name(json.getString("show_name"));
				uChange.setUnit_brand_id(0);
				uChange.setUnit_id(u.getUnit_id());
				uChange.setUnit_type_id(1315);
				uChange.setWebsite(json.getString("website"));		
				uChange.setAction(1);
				uChange.setChecked(0);
				uChange.setParent_unit_id(json.getString("parent_id"));
				
				uChange.setBlock_area(json.getString("block_area"));
				uChange.setBlock_length(json.getString("block_length"));
				uChange.setBlock_state(json.getString("block_state"));
				uChange.setBlock_width(json.getString("block_width"));
			
				uChange.setBlock_rent(json.getString("block_rent"));
				uChange.setBlock_tonnage(json.getString("block_tonnage"));
				uChange.setBlock_discount(json.getString("block_discount"));
				
				uChange.setProxy_id(json.getString("proxy_id"));
				uChange.setReco_company_id(json.getString("reco_company_id"));
				uChange.setRese_company_id(json.getString("rese_company_id"));
				uChange.setSign_company_id(json.getString("sign_company_id"));
				uChange.setPay_company_id(json.getString("pay_company_id"));
				uChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				
				
				p.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
				p.setCreator("admin");
				p.setHas_indoor_map(0);
				p.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
				p.setModifier("dr");
				p.setPoi_id(UUID.randomUUID().toString());
				p.setUnit_id(u.getUnit_id());
				
				pChange.setCreate_time(new Date());
				pChange.setCreator("dr");
				pChange.setHas_indoor_map(0);
				pChange.setPoi_id(p.getPoi_id());
				pChange.setUnit_id(u.getUnit_id());
				pChange.setAction(1);
				pChange.setChecked(0);
				
				i.setAddress(json.getString("address"));
				i.setAvailable(4);
				i.setBooth_num(json.getString("booth_num"));
				i.setCoord_x(Double.parseDouble(json.getString("coord_x")));
				i.setCoord_y(Double.parseDouble(json.getString("coord_y")));
				i.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
				i.setCreator("admin");
				i.setFloor_id(json.getString("floor_id"));
				i.setFrame(json.getString("frame"));
				i.setIndoor_id(UUID.randomUUID().toString());
				i.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));		
				i.setMax_x(Double.parseDouble(json.getString("max_x")));
				i.setMax_y(Double.parseDouble(json.getString("max_y")));
				i.setMin_x(Double.parseDouble(json.getString("min_x")));
				i.setMin_y(Double.parseDouble(json.getString("min_y")));
				i.setModifier("dr");
				i.setParent_unit_id(json.getString("parent_id"));
				i.setPoi_id(p.getPoi_id());
				//i.setType("商铺");
				i.setType(json.getString("type"));
				i.setUnit_id(u.getUnit_id());
				
				iChange.setAddress(json.getString("address"));
				iChange.setAvailable(5);
				iChange.setBooth_num(json.getString("booth_num"));
				iChange.setCoord_x(Double.parseDouble(json.getString("coord_x")));
				iChange.setCoord_y(Double.parseDouble(json.getString("coord_y")));
				iChange.setCreate_time(new Date());
				iChange.setCreator("dr");
				iChange.setFloor_id(json.getString("floor_id"));
				iChange.setFrame(json.getString("frame"));
				iChange.setIndoor_id(i.getIndoor_id());
				iChange.setMax_x(Double.parseDouble(json.getString("max_x")));
				iChange.setMax_y(Double.parseDouble(json.getString("max_y")));
				iChange.setMin_x(Double.parseDouble(json.getString("min_x")));
				iChange.setMin_y(Double.parseDouble(json.getString("min_y")));
				iChange.setParent_unit_id(json.getString("parent_id"));
				iChange.setPoi_id(p.getPoi_id());
				//i.setType("商铺");
				iChange.setType(json.getString("type"));
				iChange.setUnit_id(u.getUnit_id());
				iChange.setAction(1);
				iChange.setChecked(0);
				iChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				
				
				map.put("success", this.updateService.add(u, uChange, p, pChange, i, iChange));
				map.put("message","Add");
				
			}

			resultObj = map;
			logger.info("返回数据成功");	
			
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = map;
			return SUCCESS;
		}finally{
			request = null;
			if(validCheckResult != null){validCheckResult.clear();validCheckResult = null;}
		}
	}	

	
	
	
	@After
	public void destory(){
		logger.info("request end...");
	}

	public UpdateService getUpdateService() {
		return updateService;
	}
	@Resource
	public void setUpdateService(UpdateService updateService) {
		this.updateService = updateService;
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

	public ProxyService getProxyService() {
		return proxyService;
	}

	public void setProxyService(ProxyService proxyService) {
		this.proxyService = proxyService;
	}
	
	
}