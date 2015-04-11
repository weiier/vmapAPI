package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Indoor;
import com.buptmap.model.IndoorChange;
import com.buptmap.model.Lease;
import com.buptmap.model.UnitChange;
import com.buptmap.model.Units;
@Component
public class LeaseDAO {
	private HibernateTemplate hibernateTemplate;
	
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	
	public boolean insert(Lease l){
		try{
			this.hibernateTemplate.save(l);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public JSONArray selectNew(){
			List<Object[]> leases = new ArrayList<Object[]>();
			leases = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where state=0 order by last_modify_time desc");
			return this.searchUtil(leases);
	}
	public JSONArray selectOld(){
		List<Object[]> leases = new ArrayList<Object[]>();
		leases = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where state != 0  order by last_modify_time desc");
		return this.searchUtil(leases);
	}
	public JSONArray selectCompany(String company_id){
			List<Object[]> leases = new ArrayList<Object[]>();
			leases = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where company_id='"+company_id+"'");
			return this.searchUtil(leases);
	}
	
	public JSONArray selectProxyNewById(String proxy_id,String floor_id){
			List<Object[]> leaseList = new ArrayList<Object[]>();
			List<Object[]> leases = new ArrayList<Object[]>();
			List<Object[]> versionList = new ArrayList<Object[]>();
			List<Object[]> unitList = new ArrayList<Object[]>();
			Object[] leaseObj = null;
			Object[] versionObj = null;
			leaseList = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where " +
					"state=0 and floor_id='"+floor_id+"' order by last_modify_time desc");
			if(leaseList != null && leaseList.size() > 0){
				for(int i = 0; i < leaseList.size(); i++){
					 leaseObj = leaseList.get(i);
					versionList = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[6]+"'");
					if(versionList!=null&&versionList.size()>0){
						versionObj = versionList.get(0);
						String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
						String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
						unitList = hibernateTemplate.find("select unit_id from UnitChange u  where u.unit_id='"+leaseObj[2]+"' and u.create_time between '"
						+create+"' and '"+modify+"' and u.proxy_id='"+proxy_id+"' order by u.create_time desc");
						if(unitList != null && unitList.size() > 0){
							leases.add(leaseObj);
						}
					}
				}
			}
			return this.searchUtil(leases);
	}
	
	public JSONArray selectProxyOldById(String proxy_id,String floor_id){
		List<Object[]> leaseList = new ArrayList<Object[]>();
		List<Object[]> leases = new ArrayList<Object[]>();
		List<Object[]> versionList = new ArrayList<Object[]>();
		List<Object[]> unitList = new ArrayList<Object[]>();
		Object[] leaseObj = null;
		Object[] versionObj = null;
		leaseList = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where " +
				"state!=0 and floor_id='"+floor_id+"' order by last_modify_time desc");
		if(leaseList != null && leaseList.size() > 0){
			for(int i = 0; i < leaseList.size(); i++){
				 leaseObj = leaseList.get(i);
				versionList = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[6]+"'");
				if(versionList!=null&&versionList.size()>0){
					versionObj = versionList.get(0);
					String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
					String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
					unitList = hibernateTemplate.find("select unit_id from UnitChange u  where u.unit_id='"+leaseObj[2]+"' and u.create_time between '"
					+create+"' and '"+modify+"' and u.proxy_id='"+proxy_id+"' order by u.create_time desc");
					if(unitList != null && unitList.size() > 0){
						leases.add(leaseObj);
					}
				}
			}
		}
		return this.searchUtil(leases);
	}
	
	public JSONArray selectProxysOld(String floor_id){
		List<Object[]> leases = new ArrayList<Object[]>();
		leases = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where " +
				"state!=0 and floor_id='"+floor_id+"' order by last_modify_time desc");
		return this.searchUtil(leases);
	}
	
	public JSONArray selectProxysNew(String floor_id){
		List<Object[]> leases = new ArrayList<Object[]>();
		leases = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where " +
				"state=0 and floor_id='"+floor_id+"' order by last_modify_time desc");
		return this.searchUtil(leases);
	}
	
	public JSONArray selectByUnit(String unit_id,int version_id){
		try{
			jsonArray = new JSONArray();
			List<Object[]> leaseList = new ArrayList<Object[]>();
			List<Object[]> companyList = new ArrayList<Object[]>();
			
			leaseList = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where unit_id='"
			+unit_id+"' and version_id='"+version_id+"'");
			if(leaseList != null && leaseList.size() > 0){
				Object[] leaseObj = null;
				Object[] proxyObj = null;
				for(int i = 0 ;  i < leaseList.size(); i++){
					jsonObject = new JSONObject();
					leaseObj = leaseList.get(i);
					
					jsonObject.put("lease_id", leaseObj[0]);
					jsonObject.put("company_id", leaseObj[1]);
					jsonObject.put("unit_id", leaseObj[2]);
					jsonObject.put("state", leaseObj[3]);
					jsonObject.put("rent", leaseObj[4]);
					jsonObject.put("last_modify_time", leaseObj[5] == null ? "" : leaseObj[5] );
					jsonObject.put("version_id", leaseObj[6]);
					companyList = this.hibernateTemplate.find("select company_id,show_name,company_name_ch,company_name_en,email,phone,address_ch,address_en,con_per,contact,company_color,other_info,alt_con from Company"+
						 	" where company_id = '"+leaseObj[1]+"'");
					
					if(companyList != null && companyList.size() > 0){
						proxyObj = companyList.get(0);
						 jsonObject.put("company_id", proxyObj[0]);
						 jsonObject.put("company_show_name", proxyObj[1]);
						 jsonObject.put("company_name_ch", proxyObj[2]==null ? "" : proxyObj[2]);
						 jsonObject.put("company_name_en", proxyObj[3]==null ? "" : proxyObj[3]);
						 jsonObject.put("email", proxyObj[4]==null ? "" : proxyObj[4]);
						 jsonObject.put("phone", proxyObj[5]==null ? "" : proxyObj[5]);
						 jsonObject.put("address_ch", proxyObj[6]==null ? "" : proxyObj[6]);
						 jsonObject.put("address_en", proxyObj[7]);
						 jsonObject.put("con_per", proxyObj[8]==null ? "" : proxyObj[8]);
						 jsonObject.put("contact", proxyObj[9]==null ? "" : proxyObj[9]);
						 jsonObject.put("company_color", proxyObj[10]==null ? "" : proxyObj[10]);
						 jsonObject.put("other_info", proxyObj[11]==null ? "" : proxyObj[11]);
						 jsonObject.put("alt_con", proxyObj[12]==null ? "" : proxyObj[12]);
					}else{
						System.out.println("company is empty");
					}	
					
					jsonArray.add(jsonObject);
				}
			}else{
				System.out.println("lease is empty");
			}
			return jsonArray;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public JSONArray select(int lease_id){
				List<Object[]> leases = new ArrayList<Object[]>();
				leases= this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease where id='"+lease_id+"'");
				return this.searchUtil(leases);
		}
	
	
	public boolean editRent(int  lease_id,String rent){
		try {
			this.hibernateTemplate.bulkUpdate("update Lease l set l.rent='"+rent+"' where l.id='"+lease_id+"'");
			return true;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateState(String lease_id,String state){
				List<Object[]> leaseList = this.hibernateTemplate.find("select company_id,unit_id,version_id from Lease where id='"+lease_id+"'");
				if(leaseList != null && leaseList.size() > 0){
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());		
					this.hibernateTemplate.bulkUpdate("update Lease set state="+Integer.parseInt(state)+",last_modify_time='"
					+date+"' where id='"+lease_id+"'");
					Object[] leaseObj = leaseList.get(0);
					
					List<Units> result = new ArrayList<Units>();
					List<Indoor> result2 =  new ArrayList<Indoor>();
					List<UnitChange> result3 = new ArrayList<UnitChange>();
					List<Object[]> versiontime = new ArrayList<Object[]>();
					
					versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[2]+"'");
					Object[] versionObj = null;
					versionObj = versiontime.get(0);
					String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
					String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
					
					result = hibernateTemplate.find("from Units  where unit_id='"+leaseObj[1]+"'");
					result2 = hibernateTemplate.find("from Indoor  where unit_id='"+leaseObj[1]+"'");
					result3 = hibernateTemplate.find("from UnitChange u where u.unit_id='"+leaseObj[1]+"' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
	
					if(result.size() == 1) {
						Indoor indoor = result2.get(0);
						System.out.println(indoor.getParent_unit_id());
						Units u = result.get(0);
						UnitChange unitChange = new UnitChange();
						unitChange.setAction(2);
						unitChange.setAddress(u.getAddress());
						unitChange.setAlias(u.getAlias());
						unitChange.setAvailable(4);
						unitChange.setBlock_area(u.getBlock_area());
						unitChange.setBlock_length(u.getBlock_length());
						unitChange.setBlock_width(u.getBlock_width());
						unitChange.setBlock_rent(u.getBlock_rent());
						unitChange.setBlock_tonnage(u.getBlock_tonnage());
						unitChange.setBlock_discount(u.getBlock_discount());
						unitChange.setChange_id(0);
						unitChange.setChecked(0);
						unitChange.setCreate_time(new Date());
						unitChange.setCreator(u.getCreator());
						unitChange.setDescription(u.getDescription());
						unitChange.setDetail_info(u.getDetail_info());
						unitChange.setEmail(u.getEmail());
						unitChange.setG_outdoor_id(u.getG_outdoor_id());
						unitChange.setHas_indoor_map(u.getHas_indoor_map());
						unitChange.setHas_outdoor( u.getHas_outdoor()== null|| u.getHas_outdoor()== "" ?  0 : Integer.parseInt(u.getHas_outdoor()));
						unitChange.setKeyword(u.getKeyword());
						unitChange.setLogo(u.getLogo());
						unitChange.setName(u.getName());
						unitChange.setOpening_time(u.getOpening_time());
						unitChange.setParent_unit_id(indoor.getParent_unit_id());
						unitChange.setPhone(u.getPhone());
						unitChange.setProxy_id(u.getProxy_id());
						unitChange.setWebsite(u.getWebsite());
						unitChange.setUnit_type_id(u.getUnit_type_id());
						unitChange.setUnit_id(leaseObj[1].toString());
						unitChange.setUnit_brand_id(u.getUnit_brand_id());
						unitChange.setShow_name(u.getShow_name());
						unitChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						unitChange.setBlock_state(state.equals("1") ? "1" : "0");
						if(state.equals("1")){
							unitChange.setPay_company_id(leaseObj[0].toString());
						}
						if (result3.size() == 0) {
							unitChange.setCreate_time(new Date());
							this.hibernateTemplate.save(unitChange);
							this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+leaseObj[1]+"'");

						}
						else {
							UnitChange testChange = result3.get(0);
							testChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							testChange.setBlock_state(state.equals("1") ? "1" : "0");
							if(state.equals("1")){
								testChange.setPay_company_id(leaseObj[0].toString());
							}
							this.hibernateTemplate.update(testChange);
							this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+leaseObj[1]+"'");
						}
							
					}
					
					else {
						if (result3.size() == 1) {
							UnitChange unitChange = result3.get(0);
							if (unitChange.getAvailable() == 5) {
								
							unitChange.setBlock_state(state.equals("1") ? "1" : "0");
							if(state.equals("1")){
								unitChange.setPay_company_id(leaseObj[0].toString());
							}
							unitChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							this.hibernateTemplate.update(unitChange);

						}else {
								return false;
							}
						}
						else {
							return false;
						}
					}
					
					this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
							+"' where version_id='" + leaseObj[2] + "'");
							return true;
					
				}else{
					System.out.println(" lease is  empty");
					return false;
				}
			
	}
	
	public boolean delete(int id){
		try{
			this.hibernateTemplate.bulkUpdate("");					
			return true;
		}catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
	private JSONArray searchUtil(List<Object[]> leaseList){
		try{
		jsonArray = new JSONArray();
		List<Object[]> companyList = new ArrayList<Object[]>();
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> unitChanges = new ArrayList<Object[]>();
		List<Object[]> indoorChanges = new ArrayList<Object[]>();
		
		if(leaseList != null && leaseList.size() > 0){
			Object[] leaseObj = null;
			Object[] proxyObj = null;
			Object[] resultObj = null; 
			for(int i = 0 ;  i < leaseList.size(); i++){
				jsonObject = new JSONObject();
				leaseObj = leaseList.get(i);
				
				jsonObject.put("lease_id", leaseObj[0]);
				jsonObject.put("company_id", leaseObj[1]);
				jsonObject.put("unit_id", leaseObj[2]);
				jsonObject.put("state", leaseObj[3]);
				jsonObject.put("rent", leaseObj[4]);
				jsonObject.put("last_modify_time", leaseObj[5] == null ? "" : leaseObj[5] );
				jsonObject.put("version_id", leaseObj[6]);
				companyList = this.hibernateTemplate.find("select company_id,show_name,company_name_ch,company_name_en,email,phone,address_ch,address_en,con_per,contact,company_color,other_info,alt_con from Company"+
					 	" where company_id = '"+leaseObj[1]+"'");
				
				if(companyList != null && companyList.size() > 0){
					proxyObj = companyList.get(0);
					 jsonObject.put("company_id", proxyObj[0]);
					 jsonObject.put("company_show_name", proxyObj[1]);
					 jsonObject.put("company_name_ch", proxyObj[2]==null ? "" : proxyObj[2]);
					 jsonObject.put("company_name_en", proxyObj[3]==null ? "" : proxyObj[3]);
					 jsonObject.put("email", proxyObj[4]==null ? "" : proxyObj[4]);
					 jsonObject.put("phone", proxyObj[5]==null ? "" : proxyObj[5]);
					 jsonObject.put("address_ch", proxyObj[6]==null ? "" : proxyObj[6]);
					 jsonObject.put("address_en", proxyObj[7]);
					 jsonObject.put("con_per", proxyObj[8]==null ? "" : proxyObj[8]);
					 jsonObject.put("contact", proxyObj[9]==null ? "" : proxyObj[9]);
					 jsonObject.put("company_color", proxyObj[10]==null ? "" : proxyObj[10]);
					 jsonObject.put("other_info", proxyObj[11]==null ? "" : proxyObj[11]);
					 jsonObject.put("alt_con", proxyObj[12]==null ? "" : proxyObj[12]);
				}else{
					System.out.println("company is empty");
				}	
				
				resultUnit = hibernateTemplate.find("select show_name,address,website,phone,email,opening_time," +
						"logo,description,keyword,available,alias,detail_info,name,booth_num,type,block_length,block_width,block_area,block_state,proxy_id," +
						"reco_company_id,rese_company_id,sign_company_id,pay_company_id,iavailable,block_rent,block_tonnage,block_discount from Spot s where s.unit_id='"+leaseObj[2]+"'");
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
						
					}
					//有变化的情况
					else{
						Object[] versionObj = null;
						Object[] unitObj = null;
						Object[] indoorObj = null;
						
						version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[6]+"'");
						if(version!=null&&version.size()>0){
							versionObj = version.get(0);
							String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
							String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
													
							unitChanges = hibernateTemplate.find("select show_name,address,website,phone,email,opening_time, logo,description,keyword,available,alias,detail_info,name,block_length,block_width,block_area,block_state," +
									"proxy_id,reco_company_id,rese_company_id,sign_company_id,pay_company_id,block_rent,block_tonnage,block_discount" +
									" from UnitChange u  where u.unit_id='"+leaseObj[2]+"' and u.create_time between '"+create+"' and '"+modify+"' order by u.create_time desc");
						
							indoorChanges = hibernateTemplate.find("select type,booth_num from IndoorChange i  where i.unit_id='"+leaseObj[2]+"' and i.create_time between '"
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
								}
							}
						}
					}
				}
				//新增的情况处理完毕~
				else {
					version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[6]+"'");
					
					if(version!=null&&version.size()>0){
						Object[] versionObj = version.get(0);
						String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
						String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
										
						List<UnitChange> addunit = hibernateTemplate.find("from UnitChange u  where u.unit_id='"+leaseObj[2]+"' and u.last_modify_time between '"+create+"' and '"+modify+"' and u.available=5");
						
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
							}
						}
					}
				}
				jsonArray.add(jsonObject);
			}
		}else{
			System.out.println("lease is empty");
		}
		return jsonArray;
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	
	@Resource
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
