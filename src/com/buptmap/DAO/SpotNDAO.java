package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.IndoorChange;
import com.buptmap.model.UnitChange;
import com.opensymphony.xwork2.interceptor.annotations.After;

/**
 * new spotDao
 * @author weiier
 */
@Component("spotNDAO")
public class SpotNDAO {
	private HibernateTemplate hibernateTemplate;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private Logger logger = Logger.getLogger(SpotNDAO.class);
	/**
	 * get all unit information that were contained by certain unit
	 * @param unit_id
	 * @return JSONArray
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findAll(String unitid,String floorid,int versionid){
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> units = new ArrayList<Object[]>();
		List<Object[]> indoors = new ArrayList<Object[]>();
		List<Object[]> unitChanges = new ArrayList<Object[]>();
		List<Object[]> indoorChanges = new ArrayList<Object[]>();
		List<Object[]> proxyList = new ArrayList<Object[]>();
		List<Object[]> companyList = new ArrayList<Object[]>();
		Object[] versionObj = null;
		System.out.println(unitid);
		System.out.println(floorid);
		System.out.println(versionid);
		try{
			jsonArray = new JSONArray();
			//find spot not change
			result = hibernateTemplate.find("select name,coord_x,coord_y,unit_id,unit_type_id,frame,min_x," +
					"min_y,max_x,max_y,font,block_length,block_width,block_area,booth_num,block_state,proxy_id,pay_company_id,block_rent,block_tonnage,block_discount from Spot s where s.parent_unit_id='"+unitid+"'and floor_id='"+floorid
					+"' and s.available=1 and s.iavailable=1");
			
			for(int i =0;i<result.size();i++){
				Object[] resultObj = null; 
				jsonObject = new JSONObject();
				resultObj = result.get(i);		
				jsonObject.put("name", resultObj[0]);
				jsonObject.put("coord_x", resultObj[1]);
				jsonObject.put("coord_y", resultObj[2]);
				jsonObject.put("unit_id", resultObj[3]);
				jsonObject.put("type_id",resultObj[4] );
				jsonObject.put("frame", resultObj[5]);
				jsonObject.put("min_x",resultObj[6] );
				jsonObject.put("min_y", resultObj[7]);
				jsonObject.put("max_x", resultObj[8]);
				jsonObject.put("max_y", resultObj[9]);
				if(resultObj[10]!=null){
					jsonObject.put("font", (char)(Integer.parseInt( resultObj[10].toString())));
				}else{
					jsonObject.put("font", "");
				}
				jsonObject.put("block_length", resultObj[11] == null ? "" : resultObj[11]);
				jsonObject.put("block_width", resultObj[12] == null ? "" : resultObj[12]);
				jsonObject.put("block_area", resultObj[13] == null ? "" : resultObj[13]);
				jsonObject.put("booth_num", resultObj[14] == null ? "" : resultObj[14]);
				
				jsonObject.put("block_rent", resultObj[18] == null ? "" : resultObj[18]);
				jsonObject.put("block_tonnage", resultObj[19] == null ? "" : resultObj[19]);
				jsonObject.put("block_discount", resultObj[20] == null ? "" : resultObj[20]);
				
				jsonObject.put("block_state", resultObj[15] == null||resultObj[15].equals("") ? "0" : resultObj[15]);
				if (resultObj[16] != null && resultObj[16] != "") {
					proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[16] + "'");
					if (proxyList != null && proxyList.size() != 0) {
						Object[] proxyObject = proxyList.get(0);
						jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
						jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
						jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
						jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
						
					}
					else {
						jsonObject.put("proxy_id", "");
						jsonObject.put("proxy_name", "");
						jsonObject.put("proxy_color", "");
						jsonObject.put("proxy_show_name", "");
					}
				
				}
				else {
					jsonObject.put("proxy_id", "");
					jsonObject.put("proxy_name", "");
					jsonObject.put("proxy_color", "");
					jsonObject.put("proxy_show_name", "");
				}
				
				if (resultObj[17] != null && resultObj[17] != "") {
					companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + resultObj[17] + "'");
					if (companyList.size() != 0 && companyList != null) {
						Object[] companyObject = companyList.get(0);
						jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
						jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
						jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
						jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
					}
					
					else {
						jsonObject.put("company_id", "");
						jsonObject.put("company_show_name", "");
						jsonObject.put("con_per", "");
						jsonObject.put("phone", "");
					}
				}
				else {
					jsonObject.put("company_id", "");
					jsonObject.put("company_show_name", "");
					jsonObject.put("con_per", "");
					jsonObject.put("phone", "");
					
				}
				
				
				jsonArray.add(jsonObject);
			}
			
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where unit_id='"+unitid+"' and version_id='"+versionid+"'");
			if(version==null||version.size()==0){				
				version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where unit_id='"+unitid+"' and isAvailable=1");
				logger.info("No Such Version");
			}
			versionObj = version.get(0);
			String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
			String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
			
			//find spots change's unit info and indoors
			//unit bianhua 
			units =  hibernateTemplate.find("select name,coord_x,coord_y,unit_id,unit_type_id,frame,min_x," +
					"min_y,max_x,max_y,font,block_length,block_width,block_area,booth_num,block_state,proxy_id,pay_company_id,block_rent,block_tonnage,block_discount from Spot s where s.parent_unit_id='"+unitid+"'and floor_id='"+floorid
					+"' and s.available=4 and s.iavailable=1");
			if (units.size()!=0 && units != null) {
				for(int j =0;j<units.size();j++){
					Object[] unitsObj = null; 
					Object[] unitChange = null;
					Object[] indoorChange = null;
					jsonObject = new JSONObject();
					unitsObj = units.get(j);	
					unitChanges = hibernateTemplate.find("select parent_unit_id,unit_id,name,unit_type_id,create_time,creator,action,checked,available,block_length,block_width,block_area,block_state,proxy_id,pay_company_id," +
							"block_rent,block_tonnage,block_discount from UnitChange u  where u.unit_id='"+unitsObj[3]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
				
					if(unitChanges.size() != 0 && unitChanges != null){
						unitChange = unitChanges.get(0);
						jsonObject.put("name", unitChange[2]);
				    	jsonObject.put("type_id",unitChange[3]);
						jsonObject.put("coord_x", unitsObj[1]);
						jsonObject.put("coord_y", unitsObj[2]);
						jsonObject.put("unit_id", unitsObj[3]);
						jsonObject.put("frame", unitsObj[5]);
						jsonObject.put("min_x",unitsObj[6] );
						jsonObject.put("min_y", unitsObj[7]);
						jsonObject.put("max_x", unitsObj[8]);
						jsonObject.put("max_y", unitsObj[9]);
						jsonObject.put("booth_num", unitsObj[14]);
						jsonObject.put("block_length", unitChange[9] == null ? "" : unitChange[9]);
						jsonObject.put("block_width", unitChange[10] == null ? "" : unitChange[10]);
						jsonObject.put("block_area", unitChange[11] == null ? "" : unitChange[11]);
						
						jsonObject.put("block_rent", unitChange[15] == null ? "" : unitChange[15]);
						jsonObject.put("block_tonnage", unitChange[16] == null ? "" : unitChange[16]);
						jsonObject.put("block_discount", unitChange[17] == null ? "" : unitChange[17]);
						
						jsonObject.put("font", unitsObj[10] == null ? "" : (char)(Integer.parseInt( unitsObj[10].toString())));
						
						jsonObject.put("block_state", unitChange[12] == null||unitChange[12].equals("") ? "0" : unitChange[12]);
						if (unitChange[13] != null && unitChange[13] != "") {
							proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitChange[13] + "'");
							
							if (proxyList != null && proxyList.size() != 0) {
								Object[] proxyObject = proxyList.get(0);
								jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
								jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
								jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
								jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
								
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
						
						}
						else {
							jsonObject.put("proxy_id", "");
							jsonObject.put("proxy_name", "");
							jsonObject.put("proxy_color", "");
							jsonObject.put("proxy_show_name", "");
						}
						
						if (unitChange[14] != null && unitChange[14] != "") {
							companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitChange[14] + "'");
							if (companyList.size() != 0 && companyList != null) {
								Object[] companyObject = companyList.get(0);
								jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
								jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
								jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
								jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
							}
							
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
							}					
						}
						else {
							jsonObject.put("company_id", "");
							jsonObject.put("company_show_name", "");
							jsonObject.put("con_per", "");
							jsonObject.put("phone", "");
							
						}
						
					} 
					else{
						jsonObject.put("name", unitsObj[0]);
						jsonObject.put("coord_x", unitsObj[1]);
						jsonObject.put("coord_y", unitsObj[2]);
						jsonObject.put("unit_id", unitsObj[3]);
						jsonObject.put("type_id",unitsObj[4]);
						jsonObject.put("frame", unitsObj[5]);
						jsonObject.put("min_x",unitsObj[6] );
						jsonObject.put("min_y", unitsObj[7]);
						jsonObject.put("max_x", unitsObj[8]);
						jsonObject.put("max_y", unitsObj[9]);
						if(unitsObj[10]!=null){
							jsonObject.put("font", (char)(Integer.parseInt( unitsObj[10].toString())));
						}else{
							jsonObject.put("font", "");
						}
						jsonObject.put("block_length", unitsObj[11] == null ? "" : unitsObj[11]);
						jsonObject.put("block_width", unitsObj[12] == null ? "" : unitsObj[12]);
						jsonObject.put("block_area", unitsObj[13] == null ? "" : unitsObj[13]);
						jsonObject.put("booth_num", unitsObj[14] == null ? "" : unitsObj[14]);
						
						jsonObject.put("block_rent", unitsObj[18] == null ? "" : unitsObj[18]);
						jsonObject.put("block_tonnage", unitsObj[19] == null ? "" : unitsObj[19]);
						jsonObject.put("block_discount", unitsObj[20] == null ? "" : unitsObj[20]);
						
						jsonObject.put("block_state", unitsObj[15] == null||unitsObj[15].equals("") ? "0" : unitsObj[15]);
						if (unitsObj[16] != null && unitsObj[16] != "") {
							proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitsObj[16] + "'");
							if (proxyList != null && proxyList.size() != 0) {
								Object[] proxyObject = proxyList.get(0);
								jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
								jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
								jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
								jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
								
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
						
						}
						else {
							jsonObject.put("proxy_id", "");
							jsonObject.put("proxy_name", "");
							jsonObject.put("proxy_color", "");
							jsonObject.put("proxy_show_name", "");
						}
						
						if (unitsObj[17] != null && unitsObj[17] != "") {
							companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitsObj[17] + "'");
							
							
						}
						else {
							jsonObject.put("company_id", "");
							jsonObject.put("company_show_name", "");
							jsonObject.put("con_per", "");
							jsonObject.put("phone", "");
							
						}
					}

					if(jsonObject!=null){
						jsonArray.add(jsonObject);
					}
					
				}
				
			}
			
			units =  hibernateTemplate.find("select name,coord_x,coord_y,unit_id,unit_type_id,frame,min_x," +
					"min_y,max_x,max_y,font,block_length,block_width,block_area,booth_num,block_state,proxy_id,pay_company_id,block_rent,block_tonnage,block_discount from Spot s where s.parent_unit_id='"+unitid+"'and floor_id='"+floorid
					+"' and s.available=1 and s.iavailable=4");
			if (units.size()!=0 && units != null) {
				for(int j =0;j<units.size();j++){
					Object[] unitsObj = null; 
					Object[] unitChange = null;
					Object[] indoorChangeObj = null;
					jsonObject = new JSONObject();
					unitsObj = units.get(j);	
					indoorChanges = hibernateTemplate.find("select coord_x,coord_y,unit_id,frame,min_x,min_y,max_x,max_y,booth_num" +
							" from IndoorChange i  where i.unit_id='"+unitsObj[3]+"' and i.last_modify_time between '"+create+"' and '"+modify+"'");
				
					if(indoorChanges.size() != 0 && indoorChanges != null){
						indoorChangeObj = indoorChanges.get(0);
						jsonObject.put("name", unitsObj[0]);
						jsonObject.put("coord_x", indoorChangeObj[0]);
						jsonObject.put("coord_y", indoorChangeObj[1]);
						jsonObject.put("unit_id", indoorChangeObj[2]);
						jsonObject.put("type_id",unitsObj[4]);
						jsonObject.put("frame", indoorChangeObj[3]);
						jsonObject.put("min_x",indoorChangeObj[4] );
						jsonObject.put("min_y", indoorChangeObj[5]);
						jsonObject.put("max_x", indoorChangeObj[6]);
						jsonObject.put("max_y", indoorChangeObj[7]);
						if(unitsObj[10]!=null){
							jsonObject.put("font", (char)(Integer.parseInt( unitsObj[10].toString())));
						}else{
							jsonObject.put("font", "");
						}
						jsonObject.put("block_length", unitsObj[11] == null ? "" : unitsObj[11]);
						jsonObject.put("block_width", unitsObj[12] == null ? "" : unitsObj[12]);
						jsonObject.put("block_area", unitsObj[13] == null ? "" : unitsObj[13]);
						
						jsonObject.put("block_rent", unitsObj[18] == null ? "" : unitsObj[18]);
						jsonObject.put("block_tonnage", unitsObj[19] == null ? "" : unitsObj[19]);
						jsonObject.put("block_discount", unitsObj[20] == null ? "" : unitsObj[20]);
						
						jsonObject.put("booth_num",  indoorChangeObj[8]);

						jsonObject.put("block_state", unitsObj[15] == null||unitsObj[15].equals("") ? "0" : unitsObj[15]);
						if (unitsObj[16] != null && unitsObj[16] != "") {
							proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitsObj[16] + "'");
							if (proxyList != null && proxyList.size() != 0) {
								Object[] proxyObject = proxyList.get(0);
								jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
								jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
								jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
								jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
								
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
						
						}
						else {
							jsonObject.put("proxy_id", "");
							jsonObject.put("proxy_name", "");
							jsonObject.put("proxy_color", "");
							jsonObject.put("proxy_show_name", "");
						}
						
						if (unitsObj[17] != null && unitsObj[17] != "") {
							companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitsObj[17] + "'");
							if (companyList.size() != 0 && companyList != null) {
								Object[] companyObject = companyList.get(0);
								jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
								jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
								jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
								jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
							}
							
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
							}
						}
						else {
							jsonObject.put("company_id", "");
							jsonObject.put("company_show_name", "");
							jsonObject.put("con_per", "");
							jsonObject.put("phone", "");
							
						}
						
					}
					else {
					
						jsonObject.put("name", unitsObj[0]);
						jsonObject.put("coord_x", unitsObj[1]);
						jsonObject.put("coord_y", unitsObj[2]);
						jsonObject.put("unit_id", unitsObj[3]);
						jsonObject.put("type_id",unitsObj[4]);
						jsonObject.put("frame", unitsObj[5]);
						jsonObject.put("min_x",unitsObj[6] );
						jsonObject.put("min_y", unitsObj[7]);
						jsonObject.put("max_x", unitsObj[8]);
						jsonObject.put("max_y", unitsObj[9]);
						if(unitsObj[10]!=null){
							jsonObject.put("font", (char)(Integer.parseInt( unitsObj[10].toString())));
						}else{
							jsonObject.put("font", "");
						}
						jsonObject.put("block_length", unitsObj[11] == null ? "" : unitsObj[11]);
						jsonObject.put("block_width", unitsObj[12] == null ? "" : unitsObj[12]);
						jsonObject.put("block_area", unitsObj[13] == null ? "" : unitsObj[13]);
						jsonObject.put("booth_num", unitsObj[14] == null ? "" : unitsObj[14]);
						
						jsonObject.put("block_rent", unitsObj[18] == null ? "" : unitsObj[18]);
						jsonObject.put("block_tonnage", unitsObj[19] == null ? "" : unitsObj[19]);
						jsonObject.put("block_discount", unitsObj[20] == null ? "" : unitsObj[20]);
						
						jsonObject.put("block_state", unitsObj[15] == null||unitsObj[15].equals("") ? "0" : unitsObj[15]);
						if (unitsObj[16] != null && unitsObj[16] != "") {
							proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitsObj[16] + "'");
							if (proxyList != null && proxyList.size() != 0) {
								Object[] proxyObject = proxyList.get(0);
								jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
								jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
								jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
								jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
								
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
						
						}
						else {
							jsonObject.put("proxy_id", "");
							jsonObject.put("proxy_name", "");
							jsonObject.put("proxy_color", "");
							jsonObject.put("proxy_show_name", "");
						}
						
						if (unitsObj[17] != null && unitsObj[17] != "") {
							companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitsObj[17] + "'");
							if (companyList.size() != 0 && companyList != null) {
								Object[] companyObject = companyList.get(0);
								jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
								jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
								jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
								jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
							}
							
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
							}						
							
						}
						else {
							jsonObject.put("company_id", "");
							jsonObject.put("company_show_name", "");
							jsonObject.put("con_per", "");
							jsonObject.put("phone", "");
							
						}

					}
					if(jsonObject!=null){
						jsonArray.add(jsonObject);
					}
					
				}
					
				
			}	
			
			units =  hibernateTemplate.find("select name,coord_x,coord_y,unit_id,unit_type_id,frame,min_x," +
					"min_y,max_x,max_y,font,block_length,block_width,block_area,booth_num,block_state,proxy_id,pay_company_id,block_rent,block_tonnage,block_discount from Spot s where s.parent_unit_id='"+unitid+"'and floor_id='"+floorid
					+"' and s.available=4 and s.iavailable=4");
			
			if (units.size()!=0 && units != null) {
				for(int j =0;j<units.size();j++){
					Object[] unitsObj = null; 
					Object[] unitChange = null;
					Object[] indoorChangeObj = null;
					jsonObject = new JSONObject();
					unitsObj = units.get(j);
					
					unitChanges = hibernateTemplate.find("select parent_unit_id,unit_id,name,unit_type_id,create_time,creator,action,checked,available,block_length,block_width,block_area,block_state,proxy_id,pay_company_id," +
							"block_rent,block_tonnage,block_discount from UnitChange u  where u.unit_id='"+unitsObj[3]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'");
					indoorChanges = hibernateTemplate.find("select coord_x,coord_y,unit_id,frame,min_x,min_y,max_x,max_y,booth_num" +
							" from IndoorChange i  where i.unit_id='"+unitsObj[3]+"' and i.last_modify_time between '"+create+"' and '"+modify+"'");
				
					if (unitChanges.size() != 0 && unitChanges != null ) {
						unitChange = unitChanges.get(0);
						if (unitChange[8].equals(3) || unitChange[8].equals("3")) {
							continue;//jsonObject = null;
							
						}
					}
					
					
					//a y
					if(indoorChanges.size() != 0 && indoorChanges != null){
						indoorChangeObj = indoorChanges.get(0);
						//a y b y
						if (unitChanges.size() != 0 && unitChanges != null) {
							unitChange = unitChanges.get(0);
							
							jsonObject.put("name", unitChange[2]);
					    	jsonObject.put("type_id",unitChange[3]);
							jsonObject.put("coord_x", indoorChangeObj[0]);
							jsonObject.put("coord_y", indoorChangeObj[1]);
							jsonObject.put("unit_id", indoorChangeObj[2]);
							jsonObject.put("frame", indoorChangeObj[3]);
							jsonObject.put("min_x",indoorChangeObj[4] );
							jsonObject.put("min_y", indoorChangeObj[5]);
							jsonObject.put("max_x", indoorChangeObj[6]);
							jsonObject.put("max_y", indoorChangeObj[7]);
							jsonObject.put("booth_num",  indoorChangeObj[8]);
							jsonObject.put("block_length", unitChange[9] == null ? "" : unitChange[9]);
							jsonObject.put("block_width", unitChange[10] == null ? "" : unitChange[10]);
							jsonObject.put("block_area", unitChange[11] == null ? "" : unitChange[11]);
							
							jsonObject.put("block_rent", unitChange[15] == null ? "" : unitChange[15]);
							jsonObject.put("block_tonnage", unitChange[16] == null ? "" : unitChange[16]);
							jsonObject.put("block_discount", unitChange[17] == null ? "" : unitChange[17]);
							
							jsonObject.put("font", unitsObj[10] == null ? "" : (char)(Integer.parseInt( unitsObj[10].toString())));
							
							
							jsonObject.put("block_state", unitChange[12] == null||unitChange[12].equals("") ? "0" : unitChange[12]);
							if (unitChange[13] != null && unitChange[13] != "") {
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitChange[13] + "'");
								if (proxyList != null && proxyList.size() != 0) {
									Object[] proxyObject = proxyList.get(0);
									jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
									jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
									jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
									jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
									
								}
								else {
									jsonObject.put("proxy_id", "");
									jsonObject.put("proxy_name", "");
									jsonObject.put("proxy_color", "");
									jsonObject.put("proxy_show_name", "");
								}
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
							
							if (unitChange[14] != null && unitChange[14] != "") {
								companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitChange[14] + "'");
								if (companyList.size() != 0 && companyList != null) {
									Object[] companyObject = companyList.get(0);
									jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
									jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
									jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
									jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
								}
								
								else {
									jsonObject.put("company_id", "");
									jsonObject.put("company_show_name", "");
									jsonObject.put("con_per", "");
									jsonObject.put("phone", "");
								}							
							}
							
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
								
							}
						}
						
						
						//a y b n
						else {
							jsonObject.put("name", unitsObj[0]);
							jsonObject.put("coord_x", indoorChangeObj[0]);
							jsonObject.put("coord_y", indoorChangeObj[1]);
							jsonObject.put("unit_id", indoorChangeObj[2]);
							jsonObject.put("type_id",unitsObj[4]);
							jsonObject.put("frame", indoorChangeObj[3]);
							jsonObject.put("min_x",indoorChangeObj[4] );
							jsonObject.put("min_y", indoorChangeObj[5]);
							jsonObject.put("max_x", indoorChangeObj[6]);
							jsonObject.put("max_y", indoorChangeObj[7]);
							if(unitsObj[10]!=null){
								jsonObject.put("font", (char)(Integer.parseInt( unitsObj[10].toString())));
							}else{
								jsonObject.put("font", "");
							}
							jsonObject.put("block_length", unitsObj[11] == null ? "" : unitsObj[11]);
							jsonObject.put("block_width", unitsObj[12] == null ? "" : unitsObj[12]);
							jsonObject.put("block_area", unitsObj[13] == null ? "" : unitsObj[13]);
							jsonObject.put("booth_num",  indoorChangeObj[8]);
							
							jsonObject.put("block_rent", unitsObj[18] == null ? "" : unitsObj[18]);
							jsonObject.put("block_tonnage", unitsObj[19] == null ? "" : unitsObj[19]);
							jsonObject.put("block_discount", unitsObj[20] == null ? "" : unitsObj[20]);
							
							jsonObject.put("block_state", unitsObj[15] == null||unitsObj[15].equals("") ? "0" : unitsObj[15]);
							if (unitsObj[16] != null && unitsObj[16] != "") {
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitsObj[16] + "'");
								if (proxyList != null && proxyList.size() != 0) {
									Object[] proxyObject = proxyList.get(0);
									jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
									jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
									jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
									jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
									
								}
								else {
									jsonObject.put("proxy_id", "");
									jsonObject.put("proxy_name", "");
									jsonObject.put("proxy_color", "");
									jsonObject.put("proxy_show_name", "");
								}
							
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
							
							if (unitsObj[17] != null && unitsObj[17] != "") {
								companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitsObj[17] + "'");
								if (companyList.size() != 0 && companyList != null) {
									Object[] companyObject = companyList.get(0);
									jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
									jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
									jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
									jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
								}
								
								else {
									jsonObject.put("company_id", "");
									jsonObject.put("company_show_name", "");
									jsonObject.put("con_per", "");
									jsonObject.put("phone", "");
								}						
								

							}
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
								
							}

							
						}
	
					}
					else{
					
						if (unitChanges.size() != 0 && unitChanges != null) {
							unitChange = unitChanges.get(0);

							jsonObject.put("name", unitChange[2]);
					    	jsonObject.put("type_id",unitChange[3]);
							jsonObject.put("coord_x", unitsObj[1]);
							jsonObject.put("coord_y", unitsObj[2]);
							jsonObject.put("unit_id", unitsObj[3]);
							jsonObject.put("frame", unitsObj[5]);
							jsonObject.put("min_x",unitsObj[6] );
							jsonObject.put("min_y", unitsObj[7]);
							jsonObject.put("max_x", unitsObj[8]);
							jsonObject.put("max_y", unitsObj[9]);
							jsonObject.put("booth_num", unitsObj[14]);
							jsonObject.put("block_length", unitChange[9] == null ? "" : unitChange[9]);
							jsonObject.put("block_width", unitChange[10] == null ? "" : unitChange[10]);
							jsonObject.put("block_area", unitChange[11] == null ? "" : unitChange[11]);
							
							jsonObject.put("block_rent", unitChange[15] == null ? "" : unitChange[15]);
							jsonObject.put("block_tonnage", unitChange[16] == null ? "" : unitChange[16]);
							jsonObject.put("block_discount", unitChange[17] == null ? "" : unitChange[17]);
							
							jsonObject.put("font", "");
							
							jsonObject.put("block_state", unitChange[12] == null||unitChange[12].equals("") ? "0" : unitChange[12]);
							if (unitChange[13] != null && unitChange[13] != "") {
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitChange[13] + "'");
								if (proxyList != null && proxyList.size() != 0) {
									Object[] proxyObject = proxyList.get(0);
									jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
									jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
									jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
									jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
									
								}
								else {
									jsonObject.put("proxy_id", "");
									jsonObject.put("proxy_name", "");
									jsonObject.put("proxy_color", "");
									jsonObject.put("proxy_show_name", "");
								}
							
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
							
							if (unitChange[14] != null && unitChange[14] != "") {
								companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitChange[14] + "'");
								if (companyList.size() != 0 && companyList != null) {
									Object[] companyObject = companyList.get(0);
									jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
									jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
									jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
									jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
								}
								
								else {
									jsonObject.put("company_id", "");
									jsonObject.put("company_show_name", "");
									jsonObject.put("con_per", "");
									jsonObject.put("phone", "");
								}						
								

							}
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
								
							}
							
							
							
						}
						else {
							
							jsonObject.put("name", unitsObj[0]);
							jsonObject.put("coord_x", unitsObj[1]);
							jsonObject.put("coord_y", unitsObj[2]);
							jsonObject.put("unit_id", unitsObj[3]);
							jsonObject.put("type_id",unitsObj[4]);
							jsonObject.put("frame", unitsObj[5]);
							jsonObject.put("min_x",unitsObj[6] );
							jsonObject.put("min_y", unitsObj[7]);
							jsonObject.put("max_x", unitsObj[8]);
							jsonObject.put("max_y", unitsObj[9]);
							if(unitsObj[10]!=null){
								jsonObject.put("font", (char)(Integer.parseInt( unitsObj[10].toString())));
							}else{
								jsonObject.put("font", "");
							}
							jsonObject.put("block_length", unitsObj[11] == null ? "" : unitsObj[11]);
							jsonObject.put("block_width", unitsObj[12] == null ? "" : unitsObj[12]);
							jsonObject.put("block_area", unitsObj[13] == null ? "" : unitsObj[13]);
							jsonObject.put("booth_num", unitsObj[14] == null ? "" : unitsObj[14]);
							
							jsonObject.put("block_rent", unitsObj[18] == null ? "" : unitsObj[18]);
							jsonObject.put("block_tonnage", unitsObj[19] == null ? "" : unitsObj[19]);
							jsonObject.put("block_discount", unitsObj[20] == null ? "" : unitsObj[20]);
							
							jsonObject.put("block_state", unitsObj[15] == null||unitsObj[15].equals("") ? "0" : unitsObj[15]);
							if (unitsObj[16] != null && unitsObj[16] != "") {
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitsObj[16] + "'");
								if (proxyList != null && proxyList.size() != 0) {
									Object[] proxyObject = proxyList.get(0);
									jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
									jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
									jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
									jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
									
								}
								else {
									jsonObject.put("proxy_id", "");
									jsonObject.put("proxy_name", "");
									jsonObject.put("proxy_color", "");
									jsonObject.put("proxy_show_name", "");
								}
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
							
							if (unitsObj[17] != null && unitsObj[17] != "") {
								companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitsObj[17] + "'");
								if (companyList.size() != 0 && companyList != null) {
									Object[] companyObject = companyList.get(0);
									jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
									jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
									jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
									jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
								}
								
								else {
									jsonObject.put("company_id", "");
									jsonObject.put("company_show_name", "");
									jsonObject.put("con_per", "");
									jsonObject.put("phone", "");
								}						
								

							}
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
								
							}

							
						}
						
					}/*
					*/
					if(jsonObject!=null){
						jsonArray.add(jsonObject);
					}
				}
			}
			
			
			
			
			List<UnitChange> addunit = hibernateTemplate.find("from UnitChange u  where u.parent_unit_id='"+unitid+"' and u.last_modify_time between '"+create+"' and '"+modify+"' and u.available=5");
			
			
			if (addunit.size()!=0 && addunit != null ) {
				for(int j =0;j<addunit.size();j++){
					
					UnitChange unitChange1 = addunit.get(j);
					List<IndoorChange> addindoor = hibernateTemplate.find(" from IndoorChange i  where i.unit_id='"+unitChange1.getUnit_id()+"' and i.last_modify_time between '"+create+"' and '"+modify+"' and i.available=5");
					if (addindoor.size() != 0&&addindoor != null) {
						IndoorChange indoorChange1 = addindoor.get(0);
						

						
						jsonObject.put("name", unitChange1.getName());
						jsonObject.put("coord_x", indoorChange1.getCoord_x());
						jsonObject.put("coord_y", indoorChange1.getCoord_y());
						jsonObject.put("unit_id", unitChange1.getUnit_id());
						jsonObject.put("type_id",unitChange1.getUnit_type_id());
						jsonObject.put("frame", indoorChange1.getFrame());
						jsonObject.put("min_x", indoorChange1.getMin_x());
						jsonObject.put("min_y", indoorChange1.getMin_y());
						jsonObject.put("max_x", indoorChange1.getMax_x());
						jsonObject.put("max_y", indoorChange1.getMax_y());
						jsonObject.put("font", "");
						
						jsonObject.put("block_length", unitChange1.getBlock_length() == null ? "" : unitChange1.getBlock_length());
						jsonObject.put("block_width", unitChange1.getBlock_width() == null ? "" : unitChange1.getBlock_width());
						jsonObject.put("block_area", unitChange1.getBlock_area() == null ? "" : unitChange1.getBlock_area());
						jsonObject.put("block_rent", unitChange1.getBlock_rent() == null ? "" : unitChange1.getBlock_rent());
						jsonObject.put("block_tonnage", unitChange1.getBlock_tonnage() == null ? "" : unitChange1.getBlock_tonnage());
						jsonObject.put("block_discount", unitChange1.getBlock_discount() == null ? "" : unitChange1.getBlock_discount());
						
						jsonObject.put("booth_num", indoorChange1.getBooth_num() == null ? "" : indoorChange1.getBooth_num());
						
						jsonObject.put("block_state", unitChange1.getBlock_state() == null||unitChange1.equals("") ? "0" : unitChange1.getBlock_state());
						if (unitChange1.getProxy_id() != null && unitChange1.getProxy_id() != "") {
							proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitChange1.getProxy_id() + "'");
							if (proxyList != null && proxyList.size() != 0) {
								Object[] proxyObject = proxyList.get(0);
								jsonObject.put("proxy_id", proxyObject[2] == null ? "": proxyObject[2]);
								jsonObject.put("proxy_name", proxyObject[0] == null ? "" :  proxyObject[0]);
								jsonObject.put("proxy_color", proxyObject[1] == null ? "" :  proxyObject[1]);
								jsonObject.put("proxy_show_name", proxyObject[3] == null ? "" :  proxyObject[3]);
								
							}
							else {
								jsonObject.put("proxy_id", "");
								jsonObject.put("proxy_name", "");
								jsonObject.put("proxy_color", "");
								jsonObject.put("proxy_show_name", "");
							}
						}
						else {
							jsonObject.put("proxy_id", "");
							jsonObject.put("proxy_name", "");
							jsonObject.put("proxy_color", "");
							jsonObject.put("proxy_show_name", "");
						}
						
						if (unitChange1.getPay_company_id() != null && unitChange1.getPay_company_id() != "") {
							companyList = hibernateTemplate.find("select company_id,show_name,con_per,phone from Company where company_id='" + unitChange1.getPay_company_id() + "'");
							if (companyList.size() != 0 && companyList != null) {
								Object[] companyObject = companyList.get(0);
								jsonObject.put("company_id", companyObject[0] == null ? "": companyObject[0]);
								jsonObject.put("company_show_name", companyObject[1] == null ? "": companyObject[1]);
								jsonObject.put("con_per", companyObject[2] == null ? "": companyObject[2]);
								jsonObject.put("phone", companyObject[3] == null ? "": companyObject[3]);
							}
							
							else {
								jsonObject.put("company_id", "");
								jsonObject.put("company_show_name", "");
								jsonObject.put("con_per", "");
								jsonObject.put("phone", "");
							}						
						}
						else {
							jsonObject.put("company_id", "");
							jsonObject.put("company_show_name", "");
							jsonObject.put("con_per", "");
							jsonObject.put("phone", "");
							
						}

						if(jsonObject!=null){
							jsonArray.add(jsonObject);
						}				
					}
				
				}
					
			}	
			
			return jsonArray;
		}catch (Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(result != null){result.clear(); result = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
		}
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        System.gc();
    }
}
