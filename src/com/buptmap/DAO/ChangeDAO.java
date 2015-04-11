package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.constraints.Null;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Company;
import com.buptmap.model.IndoorChange;
import com.buptmap.model.UnitChange;
import com.opensymphony.xwork2.interceptor.annotations.After;
@Component
public class ChangeDAO {
	private HibernateTemplate hibernateTemplate;
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	private Logger logger = Logger.getLogger(ChangeDAO.class);
	
	/**
	 * 
	 * @param unitid
	 * @param create
	 * @param modify
	 * @return unit changes of a unit by certain version,by time zone
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findUnits(String unitid,String create,String modify){
		List<Object[]> result = new ArrayList<Object[]>();
		try{
			result = hibernateTemplate.find("select parent_unit_id,unit_id,name,opening_time,create_time,creator,action,checked " +
					"from UnitChange u  where u.unit_id='"+unitid+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
			
			jsonArray = new JSONArray();
			for(int i=0;i<result.size();i++){
				Object[] resultObj = null; 
				jsonObject = new JSONObject();
				resultObj = result.get(i);		
				jsonObject.put("parent", resultObj[0]);
				jsonObject.put("unit_id", resultObj[1]);
				jsonObject.put("name", resultObj[2]);
				jsonObject.put("opening_time", resultObj[3]);
				jsonObject.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[4]));
				jsonObject.put("creator", resultObj[5]);
				jsonObject.put("action", resultObj[6]);
				jsonObject.put("checked", resultObj[7]);
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
	 * @param versionid
	 * @return last change of a certain unit
	 * 加入了其他block的信息长宽面积，公司信息等 BY baoke
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findUnit(String unitid,int versionid){
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> unitChanges = new ArrayList<Object[]>();
		List<Object[]> indoorChanges = new ArrayList<Object[]>();
		List<Object[]> proxyList = new ArrayList<Object[]>();
		try{
			jsonArray = new JSONArray();
			Object[] resultObj = null; 
			jsonObject = new JSONObject();
			//find spot not change
			resultUnit = hibernateTemplate.find("select show_name,address,website,phone,email,opening_time," +
					"logo,description,keyword,available,alias,detail_info,name,booth_num,type,block_length,block_width,block_area,block_state,proxy_id,reco_company_id,rese_company_id,sign_company_id,pay_company_id,iavailable from Spot s where s.unit_id='"+unitid+"'");
			if(resultUnit!=null&&resultUnit.size()>0){
				resultObj = resultUnit.get(0);		
				if(resultObj[9].equals(1) && resultObj[24].equals(1)){
					if(resultObj[0]==null)resultObj[0]="";
					jsonObject.put("show_name", resultObj[0]);
					jsonObject.put("address", resultObj[1]);
					jsonObject.put("opening_time", resultObj[5]);
					if(resultObj[7]==null)resultObj[7]="";
					jsonObject.put("description", resultObj[7]);
					jsonObject.put("keyword", resultObj[8]);
					if(resultObj[10]==null)resultObj[10]="";
					jsonObject.put("alias", resultObj[10]);
					if(resultObj[11]==null)resultObj[11]="";
					jsonObject.put("detail", resultObj[11]);
					jsonObject.put("name", resultObj[12]);
					jsonObject.put("booth_num", resultObj[13]);
					jsonObject.put("type", resultObj[14]);
					jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
					jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
					jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
					jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
					if(resultObj[19] != null && resultObj[19] != "")
					{
						proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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
				}
				else{
					Object[] versionObj = null;
					Object[] unitObj = null;
					Object[] indoorObj = null;
					
					version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"'");
					
					if(version!=null&&version.size()>0){
						versionObj = version.get(0);
						String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
						String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
												
						unitChanges = hibernateTemplate.find("select show_name,address,website,phone,email,opening_time, logo,description,keyword,available,alias,detail_info,name,block_length,block_width,block_area,block_state,proxy_id,reco_company_id,rese_company_id,sign_company_id,pay_company_id" +
								" from UnitChange u  where u.unit_id='"+unitid+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
					
						indoorChanges = hibernateTemplate.find("select type,booth_num from IndoorChange i  where i.unit_id='"+unitid+"' and i.create_time between '"
								+create+"' and '"+modify+"' order by i.create_time desc");
						
						//indoor的type和booth分开set是因为取值不同，可能为indoor或者原来的resultobj，那样字段位置就不同。而unitobj的字段位置相同
						
						if (resultObj[9].equals(4) && resultObj[24].equals(4)) {							
							if(unitChanges!=null&&unitChanges.size()>0){
								if (indoorChanges!=null&&indoorChanges.size()>0) {
									
									unitObj = unitChanges.get(0);		
									indoorObj = indoorChanges.get(0);
									jsonObject.put("show_name", unitObj[0]);
									jsonObject.put("address", unitObj[1]);
									jsonObject.put("opening_time", unitObj[5]);
									if(unitObj[7]==null)unitObj[7]="";
									jsonObject.put("description", unitObj[7]);
									jsonObject.put("keyword", unitObj[8]);
									if(unitObj[10]==null)unitObj[10]="";
									jsonObject.put("alias", unitObj[10]);
									if(unitObj[11]==null)unitObj[11]="";
									jsonObject.put("detail", unitObj[11] == null ? "" : unitObj[11] );
									jsonObject.put("name", unitObj[12] == null ? "" : unitObj[12]);
									jsonObject.put("booth_num", indoorObj[1]);
									jsonObject.put("type", indoorObj[0] == null ? "" : indoorObj[0] );
									jsonObject.put("block_length", unitObj[13] == null ? "" :  unitObj[13]);
									jsonObject.put("block_width", unitObj[14] == null ? "" :  unitObj[14]);
									jsonObject.put("block_area", unitObj[15] == null ? "" :  unitObj[15]);
									jsonObject.put("block_state", unitObj[16] == null ? "" :  unitObj[16]);
									if(unitObj[17] != null && unitObj[17] != "")
									{
										proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitObj[17] + "'");
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

								}
								
								else {
									unitObj = unitChanges.get(0);		
									jsonObject.put("show_name", unitObj[0]);
									jsonObject.put("address", unitObj[1]);
									jsonObject.put("opening_time", unitObj[5]);
									if(unitObj[7]==null)unitObj[7]="";
									jsonObject.put("description", unitObj[7]);
									jsonObject.put("keyword", unitObj[8]);
									if(unitObj[10]==null)unitObj[10]="";
									jsonObject.put("alias", unitObj[10]);
									if(unitObj[11]==null)unitObj[11]="";
									jsonObject.put("detail", unitObj[11] == null ? "" : unitObj[11] );
									jsonObject.put("name", unitObj[12] == null ? "" : unitObj[12]);
									jsonObject.put("booth_num",  resultObj[13]);
									jsonObject.put("type", resultObj[14] == null ? "" : resultObj[14] );
									jsonObject.put("block_length", unitObj[13] == null ? "" :  unitObj[13]);
									jsonObject.put("block_width", unitObj[14] == null ? "" :  unitObj[14]);
									jsonObject.put("block_area", unitObj[15] == null ? "" :  unitObj[15]);
									jsonObject.put("block_state", unitObj[16] == null ? "" :  unitObj[16]);
									if(unitObj[17] != null && unitObj[17] != "")
									{
										proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitObj[17] + "'");
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

									
									
									
									
									
								}
									
							}
								
							
							
						}
						
						else if((unitChanges == null || unitChanges.size() == 0)&&indoorChanges!=null&&indoorChanges.size()>0){
							indoorObj = indoorChanges.get(0);
							if(resultObj[0]==null)resultObj[0]="";
							jsonObject.put("show_name", resultObj[0]);
							jsonObject.put("address", resultObj[1]);
							jsonObject.put("opening_time", resultObj[5]);
							if(resultObj[7]==null)resultObj[7]="";
							jsonObject.put("description", resultObj[7]);
							jsonObject.put("keyword", resultObj[8]);
							if(resultObj[10]==null)resultObj[10]="";
							jsonObject.put("alias", resultObj[10]);
							if(resultObj[11]==null)resultObj[11]="";
							jsonObject.put("detail", resultObj[11]);
							jsonObject.put("name", resultObj[12]);
							jsonObject.put("booth_num", indoorObj[1]);
							jsonObject.put("type", indoorObj[0] == null ? "" : indoorObj[0] );
							jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
							jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
							jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
							jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
							if(resultObj[19] != null && resultObj[19] != "")
							{
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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
						}
					
						
						else if(unitChanges!=null&&unitChanges.size()>0&&(indoorChanges==null || indoorChanges.size() == 0)){
							unitObj = unitChanges.get(0);		
							
							jsonObject.put("show_name", unitObj[0]);
							jsonObject.put("address", unitObj[1]);
							jsonObject.put("opening_time", unitObj[5]);
							if(unitObj[7]==null)unitObj[7]="";
							jsonObject.put("description", unitObj[7]);
							jsonObject.put("keyword", unitObj[8]);
							if(unitObj[10]==null)unitObj[10]="";
							jsonObject.put("alias", unitObj[10]);
							if(unitObj[11]==null)unitObj[11]="";
							jsonObject.put("detail", unitObj[11] == null ? "" : unitObj[11] );
							jsonObject.put("name", unitObj[12] == null ? "" : unitObj[12]);
							jsonObject.put("booth_num", resultObj[13]);
							jsonObject.put("type", resultObj[14] == null ? "" : resultObj[14] );
							jsonObject.put("block_length", unitObj[13] == null ? "" :  unitObj[13]);
							jsonObject.put("block_width", unitObj[14] == null ? "" :  unitObj[14]);
							jsonObject.put("block_area", unitObj[15] == null ? "" :  unitObj[15]);
							jsonObject.put("block_state", unitObj[16] == null ? "" :  unitObj[16]);
							if(unitObj[17] != null && unitObj[17] != "")
							{
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitObj[17] + "'");
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
						}
						else if((unitChanges == null ||unitChanges.size() == 0)&&(indoorChanges==null || indoorChanges.size() == 0)){
							if(resultObj[0]==null)resultObj[0]="";
							jsonObject.put("show_name", resultObj[0]);
							jsonObject.put("address", resultObj[1]);
							jsonObject.put("opening_time", resultObj[5]);
							if(resultObj[7]==null)resultObj[7]="";
							jsonObject.put("description", resultObj[7]);
							jsonObject.put("keyword", resultObj[8]);
							if(resultObj[10]==null)resultObj[10]="";
							jsonObject.put("alias", resultObj[10]);
							if(resultObj[11]==null)resultObj[11]="";
							jsonObject.put("detail", resultObj[11]);
							jsonObject.put("name", resultObj[12]);
							jsonObject.put("booth_num", resultObj[13]);
							jsonObject.put("type", resultObj[14]);
							jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
							jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
							jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
							jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
							if(resultObj[19] != null && resultObj[19] != "")
							{
								proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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
						}
					}
				}
				jsonObject.put("unit_id", unitid);
				jsonArray.add(jsonObject);	
			}
			
			else {
				
				
				version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"'");
				
				if(version!=null&&version.size()>0){
					Object[] versionObj = version.get(0);
					String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
					String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
									
					List<UnitChange> addunit = hibernateTemplate.find("from UnitChange u  where u.unit_id='"+unitid+"' and u.last_modify_time between '"+create+"' and '"+modify+"' and u.available=5");
					
					
					if (addunit.size()!=0 && addunit != null ) {
						
							
							UnitChange unitChange1 = addunit.get(0);
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
								jsonObject.put("booth_num", indoorChange1.getBooth_num() == null ? "" : indoorChange1.getBooth_num());
								
								
								jsonObject.put("block_state", unitChange1.getBlock_state() == null ? "" : unitChange1.getBlock_state());
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
								
								
	
								if(jsonObject!=null){
									jsonArray.add(jsonObject);
								}
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
			if(resultUnit != null){resultUnit.clear(); resultUnit = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
		}
	}
	/**
	 *  all changes of a certain unit and version
	 *  unit has indoormap
	 *  加入了长宽面积_by baoke
	 * @param unitid
	 * @param versionid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findAll(String unitid,int versionid){
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> resultIndoor = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> resultSpot = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		try{
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"' and unit_id='"+unitid+"'");
			Object[] versionObj = null;
			if(version!=null&&version.size()>0){
				versionObj = version.get(0);
				String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
				String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
				resultUnit = hibernateTemplate.find("select parent_unit_id,unit_id,name,opening_time,create_time,creator,action,checked,block_length,block_width,block_area " +
					"from UnitChange u  where u.parent_unit_id='"+unitid+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
				for(int i=0;i<resultUnit.size();i++){
					Object[] resultObj = null; 
					jsonObject = new JSONObject();
					resultObj = resultUnit.get(i);		
					
					resultSpot = hibernateTemplate.find("select unit_id,name,type,address,parent_unit_id,floor_id,show_name,block_length,block_width,block_area " +
							"from Spot s where s.unit_id='"+resultObj[1]+"'");
					
					if(resultSpot!=null&&resultSpot.size()>0){
						Object[] spot = resultSpot.get(0);
						jsonObject.put("parent", resultObj[0]);
						jsonObject.put("unit_id", resultObj[1]);
						jsonObject.put("name", resultObj[2]);
						jsonObject.put("opening_time", resultObj[3]);
						jsonObject.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[4]));
						jsonObject.put("creator", resultObj[5]);
						jsonObject.put("action", resultObj[6]);
						jsonObject.put("checked", resultObj[7]);
						jsonObject.put("floor_id", spot[5]);
						jsonObject.put("block_length", resultObj[8] == null ? "" : resultObj[8]);
						jsonObject.put("block_width", resultObj[9] == null ? "" : resultObj[9]);
						jsonObject.put("block_area", resultObj[10] == null ? "" : resultObj[10]);
						jsonObject.put("type", "detail");
						jsonArray.add(jsonObject);
					}
					
					
					
				}
				
				resultIndoor = hibernateTemplate.find("select parent_unit_id,unit_id,type,booth_num,create_time,creator,action,checked,floor_id " +
						"from IndoorChange i  where i.parent_unit_id='"+unitid+"' and i.create_time between '"+create+"' and '"+modify+"' order by i.create_time desc");
				for(int i=0;i<resultIndoor.size();i++){
					Object[] resultObj = null; 
					jsonObject = new JSONObject();
					resultObj = resultIndoor.get(i);		
					
					resultSpot = hibernateTemplate.find("select unit_id,name,type,address,parent_unit_id,floor_id,show_name " +
							"from Spot s where s.unit_id='"+resultObj[1]+"'");
					if(resultSpot!=null&&resultSpot.size()>0){
						Object[] spot = resultSpot.get(0);
						jsonObject.put("name", spot[1]);
						jsonObject.put("parent", resultObj[0]);
						jsonObject.put("unit_id", resultObj[1]);
						jsonObject.put("type", resultObj[2]);
						jsonObject.put("booth_num", resultObj[3]);
						jsonObject.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[4]));
						jsonObject.put("creator", resultObj[5]);
						jsonObject.put("action", resultObj[6]);
						jsonObject.put("checked", resultObj[7]);
						jsonObject.put("floor_id",  resultObj[8]);
						jsonObject.put("type", "frame");
						jsonArray.add(jsonObject);
					}
				}
			}else{
				logger.info("No Such Version");
			}
			return jsonArray;
		}catch(Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(resultUnit!=null){resultUnit.clear();resultUnit = null;}
			if(resultIndoor!=null){resultIndoor.clear();resultIndoor = null;}
			if(jsonObject!=null){jsonObject.clear();jsonObject = null;}
		}
	}
	
	/**
	 *  all changes of a certain unit and version
	 *  unit doesn't have indoormap
	 *  加入了长宽面积_by baoke
	 * @param unitid
	 * @param versionid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray findOne(String unitid,int versionid){
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> resultIndoor = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		jsonArray = new JSONArray();
		try{
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"'");
			Object[] versionObj = null;
			if(version!=null&&version.size()>0){
				versionObj = version.get(0);
				String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
				String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
				resultUnit = hibernateTemplate.find("select parent_unit_id,unit_id,name,opening_time,create_time,creator,action,checked,block_length,block_width,block_area " +
					"from UnitChange u  where u.unit_id='"+unitid+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
				for(int i=0;i<resultUnit.size();i++){
					Object[] resultObj = null; 
					
					jsonObject = new JSONObject();
					resultObj = resultUnit.get(i);		
					jsonObject.put("parent", resultObj[0]);
					jsonObject.put("unit_id", resultObj[1]);
					jsonObject.put("name", resultObj[2]);
					jsonObject.put("opening_time", resultObj[3]);
					jsonObject.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[4]));
					jsonObject.put("creator", resultObj[5]);
					jsonObject.put("action", resultObj[6]);
					jsonObject.put("checked", resultObj[7]);
					jsonObject.put("block_length", resultObj[8] == null ? "" : resultObj[8]);
					jsonObject.put("block_width", resultObj[9] == null ? "" : resultObj[9]);
					jsonObject.put("block_area", resultObj[10] == null ? "" : resultObj[10]);
					
					jsonObject.put("type", "detail");
					jsonArray.add(jsonObject);
				}
				
				resultIndoor = hibernateTemplate.find("select parent_unit_id,unit_id,type,booth_num,create_time,creator,action,checked " +
						"from IndoorChange i  where i.unit_id='"+unitid+"' and i.create_time between '"+create+"' and '"+modify+"' order by i.create_time desc");
				for(int i=0;i<resultIndoor.size();i++){
					Object[] resultObj = null; 
					
					jsonObject = new JSONObject();
					resultObj = resultIndoor.get(i);		
					jsonObject.put("parent", resultObj[0]);
					jsonObject.put("unit_id", resultObj[1]);
					jsonObject.put("type", resultObj[2]);
					jsonObject.put("booth_num", resultObj[3]);
					jsonObject.put("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(resultObj[4]));
					jsonObject.put("creator", resultObj[5]);
					jsonObject.put("action", resultObj[6]);
					jsonObject.put("checked", resultObj[7]);
					jsonObject.put("type", "frame");
					jsonArray.add(jsonObject);
				}
			}else{
				logger.info("No Such Version");
			}
			return jsonArray;
		}catch(Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
			if(resultUnit!=null){resultUnit.clear();resultUnit = null;}
			if(resultIndoor!=null){resultIndoor.clear();resultIndoor = null;}
			if(jsonObject!=null){jsonObject.clear();jsonObject = null;}
		}
	}
	
	
	public JSONArray findUnit_test(String unitid,int versionid){
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> unitChanges = new ArrayList<Object[]>();
		List<Object[]> indoorChanges = new ArrayList<Object[]>();
		List<Object[]> proxyList = new ArrayList<Object[]>();
		try{
			jsonArray = new JSONArray();
			Object[] resultObj = null; 
			jsonObject = new JSONObject();
			//find spot not change
			resultUnit = hibernateTemplate.find("select show_name,address,website,phone,email,opening_time," +
					"logo,description,keyword,available,alias,detail_info,name,booth_num,type,block_length,block_width,block_area,block_state," +
					"proxy_id,reco_company_id,rese_company_id,sign_company_id,pay_company_id,iavailable,block_rent,block_tonnage,block_discount from Spot s where s.unit_id='"+unitid+"'");
			if(resultUnit!=null&&resultUnit.size()>0){
				resultObj = resultUnit.get(0);		
				if(resultObj[9].equals(1) && resultObj[24].equals(1)){
					if(resultObj[0]==null)resultObj[0]="";
					jsonObject.put("show_name", resultObj[0]);
					jsonObject.put("address", resultObj[1]);
					jsonObject.put("opening_time", resultObj[5]);
					if(resultObj[7]==null)resultObj[7]="";
					jsonObject.put("description", resultObj[7]);
					jsonObject.put("keyword", resultObj[8]);
					if(resultObj[10]==null)resultObj[10]="";
					jsonObject.put("alias", resultObj[10]);
					if(resultObj[11]==null)resultObj[11]="";
					jsonObject.put("detail", resultObj[11]);
					jsonObject.put("name", resultObj[12]);
					jsonObject.put("booth_num", resultObj[13]);
					jsonObject.put("type", resultObj[14]);
					jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
					jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
					jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
					jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
					
					jsonObject.put("block_rent", resultObj[25] == null ? "" :  resultObj[25]);
					jsonObject.put("block_tonnage", resultObj[26] == null ? "" :  resultObj[26]);
					jsonObject.put("block_discount", resultObj[27] == null ? "" :  resultObj[27]);
					
					if(resultObj[19] != null && resultObj[19] != "")
					{
						proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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
				}
				//有变化的情况
				else{
					Object[] versionObj = null;
					Object[] unitObj = null;
					Object[] indoorObj = null;
					
					version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"'");
					
					if(version!=null&&version.size()>0){
						versionObj = version.get(0);
						String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
						String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
												
						unitChanges = hibernateTemplate.find("select show_name,address,website,phone,email,opening_time, logo,description,keyword,available,alias,detail_info,name,block_length,block_width,block_area,block_state," +
								"proxy_id,reco_company_id,rese_company_id,sign_company_id,pay_company_id,block_rent,block_tonnage,block_discount" +
								" from UnitChange u  where u.unit_id='"+unitid+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
					
						indoorChanges = hibernateTemplate.find("select type,booth_num from IndoorChange i  where i.unit_id='"+unitid+"' and i.create_time between '"
								+create+"' and '"+modify+"' order by i.create_time desc");
						
						//indoor的type和booth分开set是因为取值不同，可能为indoor或者原来的resultobj，那样字段位置就不同。而unitobj的字段位置相同
						//a=4 b=4
						if (resultObj[9].equals(4) && resultObj[24].equals(4)) {							
							//a y b y
							if(unitChanges!=null&&unitChanges.size()>0){
								if (indoorChanges!=null&&indoorChanges.size()>0) {
									unitObj = unitChanges.get(0);		
									indoorObj = indoorChanges.get(0);
									jsonObject.put("show_name", unitObj[0]);
									jsonObject.put("address", unitObj[1]);
									jsonObject.put("opening_time", unitObj[5]);
									if(unitObj[7]==null)unitObj[7]="";
									jsonObject.put("description", unitObj[7]);
									jsonObject.put("keyword", unitObj[8]);
									if(unitObj[10]==null)unitObj[10]="";
									jsonObject.put("alias", unitObj[10]);
									if(unitObj[11]==null)unitObj[11]="";
									jsonObject.put("detail", unitObj[11] == null ? "" : unitObj[11] );
									jsonObject.put("name", unitObj[12] == null ? "" : unitObj[12]);
									jsonObject.put("booth_num", indoorObj[1]);
									jsonObject.put("type", indoorObj[0] == null ? "" : indoorObj[0] );
									jsonObject.put("block_length", unitObj[13] == null ? "" :  unitObj[13]);
									jsonObject.put("block_width", unitObj[14] == null ? "" :  unitObj[14]);
									jsonObject.put("block_area", unitObj[15] == null ? "" :  unitObj[15]);
									jsonObject.put("block_state", unitObj[16] == null ? "" :  unitObj[16]);
									
									jsonObject.put("block_rent", unitObj[22] == null ? "" :  unitObj[22]);
									jsonObject.put("block_tonnage", unitObj[23] == null ? "" :  unitObj[23]);
									jsonObject.put("block_discount", unitObj[24] == null ? "" :  unitObj[24]);
									if(unitObj[17] != null && unitObj[17] != "")
									{
										proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitObj[17] + "'");
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

								}
								//a y b n
								else {
									unitObj = unitChanges.get(0);		
									indoorObj = indoorChanges.get(0);
									jsonObject.put("show_name", unitObj[0]);
									jsonObject.put("address", unitObj[1]);
									jsonObject.put("opening_time", unitObj[5]);
									if(unitObj[7]==null)unitObj[7]="";
									jsonObject.put("description", unitObj[7]);
									jsonObject.put("keyword", unitObj[8]);
									if(unitObj[10]==null)unitObj[10]="";
									jsonObject.put("alias", unitObj[10]);
									if(unitObj[11]==null)unitObj[11]="";
									jsonObject.put("detail", unitObj[11] == null ? "" : unitObj[11] );
									jsonObject.put("name", unitObj[12] == null ? "" : unitObj[12]);
									jsonObject.put("booth_num", resultObj[13]);
									jsonObject.put("type", resultObj[14]);
									jsonObject.put("block_length", unitObj[13] == null ? "" :  unitObj[13]);
									jsonObject.put("block_width", unitObj[14] == null ? "" :  unitObj[14]);
									jsonObject.put("block_area", unitObj[15] == null ? "" :  unitObj[15]);
									jsonObject.put("block_state", unitObj[16] == null ? "" :  unitObj[16]);
									
									jsonObject.put("block_rent", unitObj[22] == null ? "" :  unitObj[22]);
									jsonObject.put("block_tonnage", unitObj[23] == null ? "" :  unitObj[23]);
									jsonObject.put("block_discount", unitObj[24] == null ? "" :  unitObj[24]);
									if(unitObj[17] != null && unitObj[17] != "")
									{
										proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitObj[17] + "'");
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

								}

									
							}
							
							else {
								
								if (indoorChanges!=null&&indoorChanges.size()>0) {
									//a n b y
									indoorObj = indoorChanges.get(0);
									if(resultObj[0]==null)resultObj[0]="";
									jsonObject.put("show_name", resultObj[0]);
									jsonObject.put("address", resultObj[1]);
									jsonObject.put("opening_time", resultObj[5]);
									if(resultObj[7]==null)resultObj[7]="";
									jsonObject.put("description", resultObj[7]);
									jsonObject.put("keyword", resultObj[8]);
									if(resultObj[10]==null)resultObj[10]="";
									jsonObject.put("alias", resultObj[10]);
									if(resultObj[11]==null)resultObj[11]="";
									jsonObject.put("detail", resultObj[11]);
									jsonObject.put("name", resultObj[12]);
									jsonObject.put("booth_num", indoorObj[1]);
									jsonObject.put("type", indoorObj[0] == null ? "" : indoorObj[0] );
									jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
									jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
									jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
									jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
									
									jsonObject.put("block_rent", resultObj[25] == null ? "" :  resultObj[25]);
									jsonObject.put("block_tonnage", resultObj[26] == null ? "" :  resultObj[26]);
									jsonObject.put("block_discount", resultObj[27] == null ? "" :  resultObj[27]);
									if(resultObj[19] != null && resultObj[19] != "")
									{
										proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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

								}
								else {// a n b n
									
									if(resultObj[0]==null)resultObj[0]="";
									jsonObject.put("show_name", resultObj[0]);
									jsonObject.put("address", resultObj[1]);
									jsonObject.put("opening_time", resultObj[5]);
									if(resultObj[7]==null)resultObj[7]="";
									jsonObject.put("description", resultObj[7]);
									jsonObject.put("keyword", resultObj[8]);
									if(resultObj[10]==null)resultObj[10]="";
									jsonObject.put("alias", resultObj[10]);
									if(resultObj[11]==null)resultObj[11]="";
									jsonObject.put("detail", resultObj[11]);
									jsonObject.put("name", resultObj[12]);
									jsonObject.put("booth_num", resultObj[13]);
									jsonObject.put("type", resultObj[14]);
									jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
									jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
									jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
									jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
									
									jsonObject.put("block_rent", resultObj[25] == null ? "" :  resultObj[25]);
									jsonObject.put("block_tonnage", resultObj[26] == null ? "" :  resultObj[26]);
									jsonObject.put("block_discount", resultObj[27] == null ? "" :  resultObj[27]);
									if(resultObj[19] != null && resultObj[19] != "")
									{
										proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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

								}
	
							}
									
						}
						
						if (resultObj[9].equals(1) && resultObj[24].equals(4)){
							if (indoorChanges!=null&&indoorChanges.size()>0) {
								//a n b y
								indoorObj = indoorChanges.get(0);
								if(resultObj[0]==null)resultObj[0]="";
								jsonObject.put("show_name", resultObj[0]);
								jsonObject.put("address", resultObj[1]);
								jsonObject.put("opening_time", resultObj[5]);
								if(resultObj[7]==null)resultObj[7]="";
								jsonObject.put("description", resultObj[7]);
								jsonObject.put("keyword", resultObj[8]);
								if(resultObj[10]==null)resultObj[10]="";
								jsonObject.put("alias", resultObj[10]);
								if(resultObj[11]==null)resultObj[11]="";
								jsonObject.put("detail", resultObj[11]);
								jsonObject.put("name", resultObj[12]);
								jsonObject.put("booth_num", indoorObj[1]);
								jsonObject.put("type", indoorObj[0] == null ? "" : indoorObj[0] );
								jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
								jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
								jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
								jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
								
								jsonObject.put("block_rent", resultObj[25] == null ? "" :  resultObj[25]);
								jsonObject.put("block_tonnage", resultObj[26] == null ? "" :  resultObj[26]);
								jsonObject.put("block_discount", resultObj[27] == null ? "" :  resultObj[27]);
								if(resultObj[19] != null && resultObj[19] != "")
								{
									proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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

							}
							else {// a n b n
								
								if(resultObj[0]==null)resultObj[0]="";
								jsonObject.put("show_name", resultObj[0]);
								jsonObject.put("address", resultObj[1]);
								jsonObject.put("opening_time", resultObj[5]);
								if(resultObj[7]==null)resultObj[7]="";
								jsonObject.put("description", resultObj[7]);
								jsonObject.put("keyword", resultObj[8]);
								if(resultObj[10]==null)resultObj[10]="";
								jsonObject.put("alias", resultObj[10]);
								if(resultObj[11]==null)resultObj[11]="";
								jsonObject.put("detail", resultObj[11]);
								jsonObject.put("name", resultObj[12]);
								jsonObject.put("booth_num", resultObj[13]);
								jsonObject.put("type", resultObj[14]);
								jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
								jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
								jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
								jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
								
								jsonObject.put("block_rent", resultObj[25] == null ? "" :  resultObj[25]);
								jsonObject.put("block_tonnage", resultObj[26] == null ? "" :  resultObj[26]);
								jsonObject.put("block_discount", resultObj[27] == null ? "" :  resultObj[27]);
								if(resultObj[19] != null && resultObj[19] != "")
								{
									proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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

							}

						}
						
						if (resultObj[9].equals(4) && resultObj[24].equals(1))
						{
							if(unitChanges!=null&&unitChanges.size()>0){

								unitObj = unitChanges.get(0);		
								
								jsonObject.put("show_name", unitObj[0]);
								jsonObject.put("address", unitObj[1]);
								jsonObject.put("opening_time", unitObj[5]);
								if(unitObj[7]==null)unitObj[7]="";
								jsonObject.put("description", unitObj[7]);
								jsonObject.put("keyword", unitObj[8]);
								if(unitObj[10]==null)unitObj[10]="";
								jsonObject.put("alias", unitObj[10]);
								if(unitObj[11]==null)unitObj[11]="";
								jsonObject.put("detail", unitObj[11] == null ? "" : unitObj[11] );
								jsonObject.put("name", unitObj[12] == null ? "" : unitObj[12]);
								jsonObject.put("booth_num", resultObj[13]);
								jsonObject.put("type", resultObj[14]);
								jsonObject.put("block_length", unitObj[13] == null ? "" :  unitObj[13]);
								jsonObject.put("block_width", unitObj[14] == null ? "" :  unitObj[14]);
								jsonObject.put("block_area", unitObj[15] == null ? "" :  unitObj[15]);
								jsonObject.put("block_state", unitObj[16] == null ? "" :  unitObj[16]);
								
								jsonObject.put("block_rent", unitObj[22] == null ? "" :  unitObj[22]);
								jsonObject.put("block_tonnage", unitObj[23] == null ? "" :  unitObj[23]);
								jsonObject.put("block_discount", unitObj[24] == null ? "" :  unitObj[24]);
								if(unitObj[17] != null && unitObj[17] != "")
								{
									proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + unitObj[17] + "'");
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
								
							}
							else {
								
								if(resultObj[0]==null)resultObj[0]="";
								jsonObject.put("show_name", resultObj[0]);
								jsonObject.put("address", resultObj[1]);
								jsonObject.put("opening_time", resultObj[5]);
								if(resultObj[7]==null)resultObj[7]="";
								jsonObject.put("description", resultObj[7]);
								jsonObject.put("keyword", resultObj[8]);
								if(resultObj[10]==null)resultObj[10]="";
								jsonObject.put("alias", resultObj[10]);
								if(resultObj[11]==null)resultObj[11]="";
								jsonObject.put("detail", resultObj[11]);
								jsonObject.put("name", resultObj[12]);
								jsonObject.put("booth_num", resultObj[13]);
								jsonObject.put("type", resultObj[14]);
								jsonObject.put("block_length", resultObj[15] == null ? "" :  resultObj[15]);
								jsonObject.put("block_width", resultObj[16] == null ? "" :  resultObj[16]);
								jsonObject.put("block_area", resultObj[17] == null ? "" :  resultObj[17]);
								jsonObject.put("block_state", resultObj[18] == null ? "" :  resultObj[18]);
								
								jsonObject.put("block_rent", resultObj[25] == null ? "" :  resultObj[25]);
								jsonObject.put("block_tonnage", resultObj[26] == null ? "" :  resultObj[26]);
								jsonObject.put("block_discount", resultObj[27] == null ? "" :  resultObj[27]);
								if(resultObj[19] != null && resultObj[19] != "")
								{
									proxyList = hibernateTemplate.find("select proxy_name,proxy_color,proxy_id,show_name from Proxy where proxy_id='" + resultObj[19] + "'");
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
							}
														
						}

					}
	
				}
				jsonObject.put("unit_id", unitid);
				jsonArray.add(jsonObject);	
			}
			
			//新增的情况处理完毕~
			else {
				
				
				version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"'");
				
				if(version!=null&&version.size()>0){
					Object[] versionObj = version.get(0);
					String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
					String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
									
					List<UnitChange> addunit = hibernateTemplate.find("from UnitChange u  where u.unit_id='"+unitid+"' and u.last_modify_time between '"+create+"' and '"+modify+"' and u.available=5");
					
					
					if (addunit.size()!=0 && addunit != null ) {
						
							
							UnitChange unitChange1 = addunit.get(0);
							List<IndoorChange> addindoor = hibernateTemplate.find(" from IndoorChange i  where i.unit_id='"+unitChange1.getUnit_id()+"' and i.last_modify_time between '"+create+"' and '"+modify+"' and i.available=5");
							if (addindoor.size() != 0&&addindoor != null) {
								IndoorChange indoorChange1 = addindoor.get(0);

								jsonObject.put("show_name", unitChange1.getShow_name());
								jsonObject.put("address", unitChange1.getAddress());
								jsonObject.put("opening_time", unitChange1.getOpening_time());
								
								jsonObject.put("description", unitChange1.getDescription() == null ? "" : unitChange1.getDescription());
								jsonObject.put("keyword", unitChange1.getKeyword() == null ? "" : unitChange1.getKeyword());
								
								jsonObject.put("alias", unitChange1.getAlias() == null ? "" : unitChange1.getAlias());
								jsonObject.put("detail", unitChange1.getDetail_info() == null ? "" :  unitChange1.getDetail_info() );
								jsonObject.put("name", unitChange1.getName() == null ? "" : unitChange1.getName());
								jsonObject.put("type", indoorChange1.getType() == null ? "" : indoorChange1.getType());
								
								
								jsonObject.put("block_length", unitChange1.getBlock_length() == null ? "" : unitChange1.getBlock_length());
								jsonObject.put("block_width", unitChange1.getBlock_width() == null ? "" : unitChange1.getBlock_width());
								jsonObject.put("block_area", unitChange1.getBlock_area() == null ? "" : unitChange1.getBlock_area());
								jsonObject.put("booth_num", indoorChange1.getBooth_num() == null ? "" : indoorChange1.getBooth_num());
								
								
								jsonObject.put("block_state", unitChange1.getBlock_state() == null ? "" : unitChange1.getBlock_state());
								jsonObject.put("block_rent", unitChange1.getBlock_rent() == null ? "" : unitChange1.getBlock_rent());
								jsonObject.put("block_tonnage", unitChange1.getBlock_tonnage() == null ? "" : unitChange1.getBlock_tonnage());
								jsonObject.put("block_discount", unitChange1.getBlock_discount() == null ? "" : unitChange1.getBlock_discount());
								
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
								
								
	
								if(jsonObject!=null){
									jsonArray.add(jsonObject);
								}
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
			if(resultUnit != null){resultUnit.clear(); resultUnit = null;}
			if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
		}
	}
	
	
	
	/*
	 * 为一个展位寻找与其有关的company信息
	 * 一定是在change里面
	 * 正在努力建设
	 */
	public JSONArray findUnit_company(String unitid,int versionid){
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> unitChanges = new ArrayList<Object[]>();
		try{
			
			Object[] versionObj = null;
			Object[] unitObj = null;
			jsonArray = new JSONArray();
			
			
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+versionid+"'");
			
			if(version!=null&&version.size()>0){
				versionObj = version.get(0);
				String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
				String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
										
				unitChanges = hibernateTemplate.find("select rese_company_id,reco_company_id,sign_company_id,pay_company_id" +
						" from UnitChange u  where u.unit_id='"+unitid+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
			
				if (unitChanges != null && unitChanges.size() != 0) {
					jsonObject = new JSONObject();
					unitObj = unitChanges.get(0);
					System.out.println(unitObj[0]);
					System.out.println(unitObj[1]);
					System.out.println(unitObj[2]);
					System.out.println(unitObj[3]);
					if (unitObj[0] != null && unitObj[0] != "") {
						String[] rese_id = unitObj[0].toString().split("\\*");
						for (int i = 0; i < rese_id.length; i++) {
							System.out.println(rese_id[i]);
						}
						for (int i = 0; i < rese_id.length; i++) {
							jsonObject = new JSONObject();
							List<Company> companylisList = hibernateTemplate.find("from Company where company_id='" + rese_id[i] + "'");
							if (companylisList.size() != 0 && companylisList!= null) {
								Company addcompany = companylisList.get(0);
								jsonObject.put("company_id", addcompany.getCompany_id());
								jsonObject.put("show_name", addcompany.getShow_name());
								jsonObject.put("company_name_ch", addcompany.getCompany_name_ch());
								jsonObject.put("company_name_en", addcompany.getCompany_name_en());
								jsonObject.put("email", addcompany.getEmail());
								jsonObject.put("phone", addcompany.getPhone());
								jsonObject.put("address_ch", addcompany.getAddress_ch());
								jsonObject.put("address_en", addcompany.getAddress_en());
								jsonObject.put("con_per", addcompany.getCon_per());
								jsonObject.put("contact", addcompany.getContact());
								jsonObject.put("alt_con", addcompany.getAlt_con());
								jsonObject.put("company_color", addcompany.getCompany_color());
								jsonObject.put("other_info", addcompany.getOther_info());
								jsonObject.put("mark", "rese_company_id");
								
								jsonArray.add(jsonObject);
							}
						}
					}
				   if (unitObj[1] != null && unitObj[1] != "") {
						String[] reco_id = unitObj[1].toString().split("\\*");
						
						for (int i = 0; i < reco_id.length; i++) {
							jsonObject = new JSONObject();
							List<Company> companylisList = hibernateTemplate.find("from Company where company_id='" + reco_id[i] + "'");
							if (companylisList.size() != 0 && companylisList!= null) {
								Company addcompany = companylisList.get(0);
								jsonObject.put("company_id", addcompany.getCompany_id());
								jsonObject.put("show_name", addcompany.getShow_name());
								jsonObject.put("company_name_ch", addcompany.getCompany_name_ch());
								jsonObject.put("company_name_en", addcompany.getCompany_name_en());
								jsonObject.put("email", addcompany.getEmail());
								jsonObject.put("phone", addcompany.getPhone());
								jsonObject.put("address_ch", addcompany.getAddress_ch());
								jsonObject.put("address_en", addcompany.getAddress_en());
								jsonObject.put("con_per", addcompany.getCon_per());
								jsonObject.put("contact", addcompany.getContact());
								jsonObject.put("alt_con", addcompany.getAlt_con());
								jsonObject.put("company_color", addcompany.getCompany_color());
								jsonObject.put("other_info", addcompany.getOther_info());
								jsonObject.put("mark", "reco_company_id");
								
								jsonArray.add(jsonObject);
							}
						}
					}
					
					if (unitObj[2] != null && unitObj[2] != "") {
						jsonObject = new JSONObject();
						String sign_id = unitObj[2].toString();
						List<Company> companylisList = hibernateTemplate.find("from Company where company_id='" + sign_id + "'");
						if (companylisList.size() != 0 && companylisList!= null) {
							Company addcompany = companylisList.get(0);
							jsonObject.put("company_id", addcompany.getCompany_id());
							jsonObject.put("show_name", addcompany.getShow_name());
							jsonObject.put("company_name_ch", addcompany.getCompany_name_ch());
							jsonObject.put("company_name_en", addcompany.getCompany_name_en());
							jsonObject.put("email", addcompany.getEmail());
							jsonObject.put("phone", addcompany.getPhone());
							jsonObject.put("address_ch", addcompany.getAddress_ch());
							jsonObject.put("address_en", addcompany.getAddress_en());								
							jsonObject.put("con_per", addcompany.getCon_per());
							jsonObject.put("contact", addcompany.getContact());
							jsonObject.put("alt_con", addcompany.getAlt_con());
							jsonObject.put("company_color", addcompany.getCompany_color());
							jsonObject.put("other_info", addcompany.getOther_info());
							jsonObject.put("mark", "sign_company_id");
								
							jsonArray.add(jsonObject);
							}
						
					}
					
					if (unitObj[3] != null && unitObj[3] != "") {
						jsonObject = new JSONObject();
						String pay_id = unitObj[3].toString();
						List<Company> companylisList = hibernateTemplate.find("from Company where company_id='" + pay_id + "'");
						if (companylisList.size() != 0 && companylisList!= null) {
							Company addcompany = companylisList.get(0);
							jsonObject.put("company_id", addcompany.getCompany_id());
							jsonObject.put("show_name", addcompany.getShow_name());
							jsonObject.put("company_name_ch", addcompany.getCompany_name_ch());
							jsonObject.put("company_name_en", addcompany.getCompany_name_en());
							jsonObject.put("email", addcompany.getEmail());
							jsonObject.put("phone", addcompany.getPhone());
							jsonObject.put("address_ch", addcompany.getAddress_ch());
							jsonObject.put("address_en", addcompany.getAddress_en());								
							jsonObject.put("con_per", addcompany.getCon_per());
							jsonObject.put("contact", addcompany.getContact());
							jsonObject.put("alt_con", addcompany.getAlt_con());
							jsonObject.put("company_color", addcompany.getCompany_color());
							jsonObject.put("other_info", addcompany.getOther_info());
							jsonObject.put("mark", "pay_company_id");
								
							jsonArray.add(jsonObject);
							}
						
					}
					
				}
			}
			return jsonArray;
			//return null;
		}catch (Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}finally{
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
