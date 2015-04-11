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

import com.opensymphony.xwork2.interceptor.annotations.After;
@Component
public class UnitDAO {
	private HibernateTemplate hibernateTemplate;
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	private Logger logger = Logger.getLogger(UnitDAO.class);
	/**
	 * 
	 * @return all units that have indoor map
	 * 加入后来的block信息 BYbaoke
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findUnit(){
		List<Object[]> result = new ArrayList<Object[]>();
		try{
			System.out.println("12312312312");
			result = hibernateTemplate.find("select unit_id,name,type,address,description,opening_time,show_name " +
					"from Unit u where u.available=1");
			//,block_length,block_width,block_area,block_state,proxy_id,reco_company_id,rese_company_id,sign_company_id,pay_company_id
			jsonArray = new JSONArray();
		
			for(int i=0;i<result.size();i++){
				Object[] resultObj = null; 
				jsonObject = new JSONObject();
				resultObj = result.get(i);		
				jsonObject.put("unit_id", resultObj[0]);
				jsonObject.put("name", resultObj[1]);
				jsonObject.put("type", resultObj[2]);
				jsonObject.put("address", resultObj[3]);
				if(resultObj[4]==null){resultObj[4]="";}
				jsonObject.put("description", resultObj[4]);
				jsonObject.put("opening_time", resultObj[5]);
				jsonObject.put("show_name", resultObj[6]);
				/*
				jsonObject.put("block_length", resultObj[7] == null ? "" :  resultObj[7]);
				jsonObject.put("block_width", resultObj[8] == null ? "" :  resultObj[8]);
				jsonObject.put("block_area", resultObj[9] == null ? "" :  resultObj[9]);
				jsonObject.put("block_state", resultObj[10] == null ? "" :  resultObj[10]);
				jsonObject.put("proxy_id", resultObj[11] == null ? "" :  resultObj[11]);
				jsonObject.put("reco_company_id", resultObj[12] == null ? "" :  resultObj[12]);
				jsonObject.put("rese_company_id", resultObj[13] == null ? "" :  resultObj[13]);
				jsonObject.put("sign_company_id", resultObj[14] == null ? "" :  resultObj[14]);
				jsonObject.put("pay_company_id", resultObj[15] == null ? "" :  resultObj[15]);
				*/
				jsonArray.add(jsonObject);
			}
			return jsonArray;
		}catch(Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(result!=null){result.clear();result = null;}
			if(jsonObject!=null){jsonObject.clear();jsonObject = null;}
		}
	}
	/**
	 * 
	 * @param unitid
	 * @return all versions by unitid
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findVersion(String unitid){
		List<Object[]> result = new ArrayList<Object[]>();
		try{
			result = hibernateTemplate.find("select version_id,unitVersion,unit_id,isAvailable,description,createTime,lastModifyTime " +
					"from Version  where unit_id='"+unitid+"'");
			jsonArray = new JSONArray();
			for(int i=0;i<result.size();i++){
				Object[] resultObj = null; 
				jsonObject = new JSONObject();
				resultObj = result.get(i);		
				jsonObject.put("id", resultObj[0]);
				jsonObject.put("version", resultObj[1]);
				jsonObject.put("unit_id", resultObj[2]);
				jsonObject.put("isAvailable", resultObj[3]);
				jsonObject.put("description", resultObj[4]);
				jsonObject.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[5]));
				jsonObject.put("lastModifyTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[6]));
				
				jsonArray.add(jsonObject);
			}
			return jsonArray;
		}catch(Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(result!=null){result.clear();result = null;}
			if(jsonObject!=null){jsonObject.clear();jsonObject = null;}
		}
	}
	
	/**
	 * search by keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findkey(String keyword,String low,String high,String floor_id,String parent_id,String version_id){
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> resultIndoor = new ArrayList<Object[]>();
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		Object[] resultObj = null; 
		Object[] resultUnitObj = null;
		Object[] resultIndoorObj = null;
		Object[] versionObj = null;
		String[] keyStr = keyword.split(";");
		String keysql = "(";
		System.out.println(keyStr.length);
		for(int i = 0; i < keyStr.length; i++){
			if(i==keyStr.length-1){
				keysql += "type like '%"+keyStr[i]+"%' )";
			}else{
				keysql += "type like '%"+keyStr[i]+"%' or ";
			} 
		}
		try{
			jsonArray = new JSONArray();
			
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where unit_id='"+parent_id+"' and version_id='"+version_id+"'");
			versionObj = version.get(0);
			String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
			String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			//不变
			result = hibernateTemplate.find("select unit_id,name,type,parent_unit_id,floor_id,show_name,block_state,booth_num,block_area,block_tonnage " +
					"from Spot s where s.available=1 and s.iavailable=1 and "+keysql+" and s.unit_type_id <=3000 and s.floor_id='"+floor_id+
					"' and s.parent_unit_id='"+parent_id+"' and (s.block_area between "+low+" and "+high+" or s.block_tonnage between "+low+" and "+high+")");
		
			for(int i=0;i<result.size();i++){
				jsonObject = new JSONObject();
				resultObj = result.get(i);
					jsonObject.put("unit_id", resultObj[0]);
					jsonObject.put("name", resultObj[1] == null?"":resultObj[1]);
					jsonObject.put("type", resultObj[2]);
					jsonObject.put("parent_id", resultObj[3]);
					jsonObject.put("floor_id", resultObj[4]);
					jsonObject.put("show_name", resultObj[5] == null?"":resultObj[5]);
					jsonObject.put("block_state", resultObj[6] == null ? "" :  resultObj[6]);
					jsonObject.put("booth_num", resultObj[7] == null ? "" :  resultObj[7]);
					jsonObject.put("block_area", resultObj[8] == null ? "" :  resultObj[8]);
					jsonObject.put("block_tonnage", resultObj[9] == null ? "" :  resultObj[9]);
				jsonArray.add(jsonObject);		
			}
			//均变
			result = hibernateTemplate.find("select unit_id,name,type,parent_unit_id,floor_id,show_name,block_state,booth_num  " +
					"from Spot s where s.available=4 and s.iavailable=4  and s.unit_type_id <=3000 and s.floor_id='"+floor_id+
					"' and s.parent_unit_id='"+parent_id+"')");
			for(int i=0;i<result.size();i++){
				resultObj = result.get(i);
				resultIndoor = hibernateTemplate.find("select unit_id,type,parent_unit_id,floor_id,booth_num from IndoorChange i where" +
						keysql+" and i.unit_id='"+resultObj[0]+"' and i.available=4 and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
				resultUnit = hibernateTemplate.find("select name,show_name,block_state,block_area,block_tonnage from UnitChange u where (u.block_area between "
						+low+" and "+high+" or u.block_tonnage between "+low+" and "+high+") and u.unit_id='"+resultObj[0]
								+"' and u.available=4 and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
				if(resultIndoor.size() > 0 && resultIndoor != null && resultUnit.size() > 0 && resultUnit != null){
					resultUnitObj = resultUnit.get(0);
					resultIndoorObj = resultIndoor.get(0);
					jsonObject = new JSONObject();
					
					jsonObject.put("unit_id", resultIndoorObj[0]);
					jsonObject.put("name", resultUnitObj[0] == null?"":resultUnitObj[0]);
					jsonObject.put("type", resultIndoorObj[1] == null?"":resultIndoorObj[1]);
					jsonObject.put("parent_id", resultIndoorObj[2]);
					jsonObject.put("floor_id", resultIndoorObj[3]);
					jsonObject.put("show_name", resultUnitObj[1] == null?"":resultUnitObj[1]);
					jsonObject.put("block_state", resultUnitObj[2] == null?"":resultUnitObj[2]);
					jsonObject.put("block_area", resultUnitObj[3] == null?"":resultUnitObj[3]);
					jsonObject.put("block_tonnage", resultUnitObj[4] == null?"":resultUnitObj[4]);
					jsonObject.put("booth_num", resultIndoorObj[4] == null?"":resultIndoorObj[4]);
					jsonArray.add(jsonObject);		
				}
			}
			
			//indoor 
			result = hibernateTemplate.find("select unit_id,name,type,parent_unit_id,floor_id,show_name,block_state,booth_num,block_area,block_tonnage " +
					"from Spot s where s.available=1 and s.iavailable=4 and s.unit_type_id <=3000 and s.floor_id='"+floor_id+"' and s.parent_unit_id='"
					+parent_id+"' and (s.block_area between "+low+" and "+high+" or s.block_tonnage between "+low+" and "+high+")");
			for(int i=0;i<result.size();i++){
				resultObj = result.get(i);
				resultIndoor = hibernateTemplate.find("select unit_id,type,parent_unit_id,floor_id,booth_num from IndoorChange i where" +
						keysql+" and i.unit_id='"+resultObj[0]+"' and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");

				if(resultIndoor.size() > 0 && resultIndoor != null){
					resultIndoorObj = resultIndoor.get(0);
					jsonObject = new JSONObject();
					
					jsonObject.put("unit_id", resultIndoorObj[0]);
					jsonObject.put("name", resultObj[1] == null?"":resultObj[1]);
					jsonObject.put("type", resultIndoorObj[1] == null?"":resultIndoorObj[1]);
					jsonObject.put("parent_id", resultIndoorObj[2]);
					jsonObject.put("floor_id", resultIndoorObj[3]);
					jsonObject.put("show_name",resultObj[5] == null?"":resultObj[5]);
					jsonObject.put("block_state",resultObj[6] == null ? "" :  resultObj[6]);
					jsonObject.put("block_area", resultObj[8] == null ? "" :  resultObj[8]);
					jsonObject.put("block_tonnage", resultObj[9] == null ? "" :  resultObj[9]);
					jsonObject.put("booth_num", resultIndoorObj[4] == null?"":resultIndoorObj[4]);
					jsonArray.add(jsonObject);		
				}
			}
			// unit
			result = hibernateTemplate.find("select unit_id,name,type,parent_unit_id,floor_id,show_name,block_state,booth_num  " +
					"from Spot s where s.available=4 and s.iavailable=1 and "+keysql+" and s.unit_type_id <=3000 and s.floor_id='"+floor_id+
					"' and s.parent_unit_id='"+parent_id+"'");
			for(int i=0;i<result.size();i++){
				resultObj = result.get(i);
				resultUnit = hibernateTemplate.find("select name,show_name,block_state,block_area,block_tonnage from UnitChange u where (u.block_area between "
						+low+" and "+high+" or u.block_tonnage between "+low+" and "+high+") and u.unit_id='"+resultObj[0]+"' and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
				if( resultUnit.size() > 0 && resultUnit != null){
					resultUnitObj = resultUnit.get(0);
					jsonObject = new JSONObject();
					
					jsonObject.put("name", resultUnitObj[0] == null?"":resultUnitObj[0]);
					jsonObject.put("show_name", resultUnitObj[1] == null?"":resultUnitObj[1]);
					jsonObject.put("block_state", resultUnitObj[2] == null?"":resultUnitObj[2]);
					jsonObject.put("block_area", resultUnitObj[3] == null?"":resultUnitObj[3]);
					jsonObject.put("block_tonnage", resultUnitObj[4] == null?"":resultUnitObj[4]);
					jsonObject.put("unit_id", resultObj[0]);
					jsonObject.put("type", resultObj[2]);
					jsonObject.put("parent_id", resultObj[3]);
					jsonObject.put("floor_id", resultObj[4]);
					jsonObject.put("booth_num", resultObj[7] == null ? "" :  resultObj[7]);
					jsonArray.add(jsonObject);		
				}
			}
			
			//添加
			resultIndoor = hibernateTemplate.find("select unit_id,type,parent_unit_id,floor_id,booth_num from IndoorChange i where" +
					keysql+"and i.available=5 and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
			for(int i = 0; i < resultIndoor.size(); i++){
				resultIndoorObj = resultIndoor.get(i);
				resultUnit = hibernateTemplate.find("select name,show_name,block_state,block_area,block_tonnage from UnitChange u where (u.block_area between "
						+low+" and "+high+" or u.block_tonnage between "+low+" and "+high+") and u.unit_id='"+resultIndoorObj[0]
								+"' and u.available=5 and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
				if(resultUnit.size()>0&&resultUnit != null){
					
					jsonObject = new JSONObject();
					resultUnitObj = resultUnit.get(0);
					jsonObject.put("unit_id", resultIndoorObj[0]);
					jsonObject.put("name", resultUnitObj[0] == null?"":resultUnitObj[0]);
					jsonObject.put("type", resultIndoorObj[1] == null?"":resultIndoorObj[1]);
					jsonObject.put("parent_id", resultIndoorObj[2]);
					jsonObject.put("floor_id", resultIndoorObj[3]);
					jsonObject.put("show_name", resultUnitObj[1] == null?"":resultUnitObj[1]);
					jsonObject.put("block_state", resultUnitObj[2] == null?"":resultUnitObj[2]);
					jsonObject.put("block_area", resultUnitObj[3] == null?"":resultUnitObj[3]);
					jsonObject.put("block_tonnage", resultUnitObj[4] == null?"":resultUnitObj[4]);
					jsonObject.put("booth_num", resultIndoorObj[4] == null?"":resultIndoorObj[4]);
					jsonArray.add(jsonObject);	
				}
			}
			
			return jsonArray;
		}catch(Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(result!=null){result.clear();result = null;}
			if(resultUnit!=null){resultUnit.clear();resultUnit = null;}
			if(resultIndoor!=null){resultIndoor.clear();resultIndoor = null;}
			if(jsonObject!=null){jsonObject.clear();jsonObject = null;}
		}
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
    }
	
}
