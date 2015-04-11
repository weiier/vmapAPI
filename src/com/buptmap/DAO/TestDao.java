package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

/**
 *  for data test
 * @author weiier
 */
@Component("testDao")
public class TestDao {
	private HibernateTemplate hibernateTemplate;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	/**
	 * get all unit information that were contained by certain unit
	 * @param unit_id
	 * @return JSONArray
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findAll(String unit_id,String floorid){
		List<Object[]> result = new ArrayList<Object[]>();
		result = hibernateTemplate.find("select name,coord_x,coord_y,unit_id,unit_type_id,frame,min_x," +
				"min_y,max_x,max_y,font from Spot s where s.parent_unit_id='"+unit_id+"'and floor_id='"+floorid+"'");
		jsonArray = new JSONArray();
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
			
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	public JSONArray findFrame(String unit_id,String floorid){
		List<Object[]> result = new ArrayList<Object[]>();
		result = hibernateTemplate.find("select floor_id,frame,floor_chn,floor_alias,floor_brief,name from Floor f where f.unit_id='"+unit_id+
				"'and f.floor_id='"+floorid+"'");
		jsonArray = new JSONArray();
		for(int i =0;i<result.size();i++){
			Object[] resultObj = null; 
			jsonObject = new JSONObject();
			resultObj = result.get(i);		
			jsonObject.put("floor_id", resultObj[0]);
			jsonObject.put("frame", resultObj[1]);
			jsonObject.put("floor_chn", resultObj[2]);
			jsonObject.put("floor_alias", resultObj[3]);
			jsonObject.put("floor_brief",resultObj[4] );
			jsonObject.put("name", resultObj[5]);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
