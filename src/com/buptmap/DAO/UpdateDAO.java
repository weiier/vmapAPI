package com.buptmap.DAO;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import com.buptmap.model.Indoor;
import com.buptmap.model.IndoorChange;
import com.buptmap.model.Poi;
import com.buptmap.model.PoiChange;
import com.buptmap.model.UnitChange;
import com.buptmap.model.Units;
import com.buptmap.model.Version;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
@Component
public class UpdateDAO {
	private HibernateTemplate hibernateTemplate;
	private Logger logger = Logger.getLogger(ChangeDAO.class);
	
	public boolean update_proxy(String id, String proxy_id, String version)
	{
		List<Units> result = new ArrayList<Units>();
		List<Indoor> result2 =  new ArrayList<Indoor>();
		List<UnitChange> result3 = new ArrayList<UnitChange>();
		List<Object[]> versiontime = new ArrayList<Object[]>();
		String[] unit_id = id.split("\\*");
		
		versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+version+"'");
		Object[] versionObj = null;
		versionObj = versiontime.get(0);
		String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
		String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
		for (int i = 0; i < unit_id.length; i++) {
			result = hibernateTemplate.find("from Units  where unit_id='"+unit_id[i]+"'");
			result2 = hibernateTemplate.find("from Indoor  where unit_id='"+unit_id[i]+"'");
			result3 = hibernateTemplate.find("from UnitChange u where u.unit_id='"+unit_id[i]+"' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
			
			if (result.size() == 1){
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
				unitChange.setBlock_state("0");
				unitChange.setBlock_width(u.getBlock_width());
				unitChange.setBlock_discount(u.getBlock_discount());
				unitChange.setBlock_rent(u.getBlock_rent());
				unitChange.setBlock_tonnage(u.getBlock_tonnage());
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
				unitChange.setPay_company_id(u.getPay_company_id());
				unitChange.setPhone(u.getPhone());
				unitChange.setProxy_id(proxy_id);
				unitChange.setWebsite(u.getWebsite());
				unitChange.setUnit_type_id(u.getUnit_type_id());
				unitChange.setUnit_id(unit_id[i]);
				unitChange.setUnit_brand_id(u.getUnit_brand_id());
				unitChange.setSign_company_id(u.getSign_company_id());
				unitChange.setShow_name(u.getShow_name());
				unitChange.setRese_company_id(u.getRese_company_id());
				unitChange.setReco_company_id(u.getReco_company_id());
				unitChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				
				if (result3.size() == 0) {
					
					this.hibernateTemplate.save(unitChange);
					this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+unit_id[i]+"'");

				}
				else {
					UnitChange testChange = result3.get(0);
					testChange.setProxy_id(proxy_id);
					testChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					this.hibernateTemplate.update(testChange);
					this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+unit_id[i]+"'");
				}
				
			}
			//新增的处理
			else {
				if (result3.size() ==1) {
					UnitChange testChange = result3.get(0);
					testChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					
					testChange.setProxy_id(proxy_id);
					this.hibernateTemplate.update(testChange);
					
				}
			}
		}
		
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
		this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
		+"' where version_id='" + version + "'");
		return true;
	}

	/*
	 * 为block块分配pay_company_id
	 * 
	 */
	 
	public boolean update_company(String unit_id, String company_id, String version,String mark)
	{
	
		List<Units> result = new ArrayList<Units>();
		List<Indoor> result2 =  new ArrayList<Indoor>();
		List<UnitChange> result3 = new ArrayList<UnitChange>();
		List<Object[]> versiontime = new ArrayList<Object[]>();
		
		versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+version+"'");
		Object[] versionObj = null;
		versionObj = versiontime.get(0);
		String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
		String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
		
		result = hibernateTemplate.find("from Units  where unit_id='"+unit_id+"'");
		System.out.println("1111111111111");
		result2 = hibernateTemplate.find("from Indoor  where unit_id='"+unit_id+"'");
		result3 = hibernateTemplate.find("from UnitChange u where u.unit_id='"+unit_id+"' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
		System.out.println(result.size());
		System.out.println(result3.size());
		if(result.size() == 1) {
			System.out.println("asfasfsfsafasas");
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
			unitChange.setUnit_id(unit_id);
			unitChange.setUnit_brand_id(u.getUnit_brand_id());
			unitChange.setShow_name(u.getShow_name());
			unitChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			if (result3.size() == 0) {
				unitChange.setPay_company_id(mark.equals("pay_company_id") ? company_id : u.getPay_company_id());
				unitChange.setRese_company_id(mark.equals("rese_company_id") ? company_id+"*" :u.getRese_company_id());
				unitChange.setReco_company_id(mark.equals("reco_company_id") ? company_id+"*" :u.getReco_company_id());
				unitChange.setSign_company_id(mark.equals("sign_company_id") ? company_id : u.getSign_company_id());
				this.hibernateTemplate.save(unitChange);
				if (mark.equals("pay_company_id")) {
					unitChange.setBlock_state("1");
				}
				else {
					if (mark.equals("rese_company_id")) {
						unitChange.setBlock_state("3");
					}
					else {
						if (mark.equals("sign_company_id")) {
							unitChange.setBlock_state("2");
						}
						else {
							unitChange.setBlock_state("0");
						}
					}
				}
				unitChange.setCreate_time(new Date());
				this.hibernateTemplate.update(unitChange);
				this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+unit_id+"'");

			}
			else {
				UnitChange testChange = result3.get(0);
				
				if (mark.equals("rese_company_id")) {
					String rese_company = testChange.getRese_company_id();
					if (rese_company != null && rese_company != "") {
						rese_company = rese_company + company_id + "*";
					}
					else {
						rese_company = company_id + "*";
					}
					testChange.setRese_company_id(rese_company);
			
				}
				else if (mark.equals("reco_company_id")) {
					String reco_company = testChange.getReco_company_id();
					if (reco_company != null && reco_company != "") {
						reco_company = reco_company + company_id + "*";
					}
					else {
						reco_company = company_id + "*";
					}
					
					testChange.setReco_company_id(reco_company);
					
				}
				testChange.setPay_company_id(mark.equals("pay_company_id") ? company_id : testChange.getPay_company_id());
				testChange.setSign_company_id(mark.equals("sign_company_id") ? company_id : testChange.getSign_company_id());
				this.hibernateTemplate.update(testChange);
				
				if (testChange.getPay_company_id() != null && !testChange.getPay_company_id().equals("") ) {
					testChange.setBlock_state("1");
				}
				else {
					if (testChange.getSign_company_id() != "" && testChange.getSign_company_id() != null) {
						testChange.setBlock_state("2");
					}
					else {
						if (testChange.getRese_company_id() != "" && testChange.getRese_company_id() != null){
							testChange.setBlock_state("3");
						}
						else {
							testChange.setBlock_state("0");
						}
					}

				}
				
				testChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				
				this.hibernateTemplate.update(testChange);
				this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+unit_id+"'");

			}
			

			
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
			this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
			+"' where version_id='" + version + "'");
			return true;
		}
		
		else {
			System.out.println("55555555555新增的");
			if (result3.size() == 1) {
				UnitChange unitChange = result3.get(0);
				if (unitChange.getAvailable() == 5) {

				unitChange.setPay_company_id(mark.equals("pay_company_id") ? company_id : unitChange.getPay_company_id());
				unitChange.setRese_company_id(mark.equals("rese_company_id") ? unitChange.getRese_company_id()+company_id+"*" :unitChange.getRese_company_id());
				unitChange.setReco_company_id(mark.equals("reco_company_id") ? unitChange.getReco_company_id()+company_id+"*" :unitChange.getReco_company_id());
				unitChange.setSign_company_id(mark.equals("sign_company_id") ? company_id : unitChange.getSign_company_id());
				
				unitChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				
				this.hibernateTemplate.update(unitChange);
				System.out.println(unitChange.getPay_company_id());
				
				if (unitChange.getPay_company_id() != null && !unitChange.getPay_company_id().equals("") ) {
					
					unitChange.setBlock_state("1");
				}
				else {
					if (unitChange.getSign_company_id() != null && !unitChange.getSign_company_id().equals("")  ) {
						unitChange.setBlock_state("2");
					}
					else {
						if ( unitChange.getRese_company_id() != null && !unitChange.getRese_company_id().equals("")) {
							unitChange.setBlock_state("3");
						}
						else {
							unitChange.setBlock_state("0");
						}
					}
				}
				this.hibernateTemplate.update(unitChange);

				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
				this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
				+"' where version_id='" + version + "'");
				return true;
			}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * update unit
	 * @param unit change info
	 * @return
	 */
	public boolean updateUnit(UnitChange u,IndoorChange iChange, String version){
		try{
			List<Object[]> versiontime = new ArrayList<Object[]>();
			List<Object[]> unitsList = new ArrayList<Object[]>();
			List<Object[]> indoorsList = new ArrayList<Object[]>();
			
			versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+version+"'");
			Object[] versionObj = null;
			versionObj = versiontime.get(0);
			String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
			String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
			List<UnitChange> unitChanges = this.hibernateTemplate.find("from UnitChange u where u.unit_id='"+u.getUnit_id()+"'and u.last_modify_time between '"+create+"' and '"+modify+"'");
			List<IndoorChange> indoorChanges = this.hibernateTemplate.find("from IndoorChange i where i.unit_id='" + iChange.getUnit_id()+"'and i.last_modify_time between '"+create+"' and '"+modify+"'");
			if (unitChanges != null && unitChanges.size() != 0) {
				UnitChange hChange = unitChanges.get(0);
				u.setUnitChange_id(hChange.getUnitChange_id());
				/*u.setProxy_id(hChange.getProxy_id());
				u.setReco_company_id(hChange.getReco_company_id());
				u.setRese_company_id(hChange.getRese_company_id());
				u.setSign_company_id(hChange.getSign_company_id());
				u.setPay_company_id(hChange.getPay_company_id());*/
				u.setAvailable(hChange.getAvailable());
				this.hibernateTemplate.update(u);
				this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+u.getUnit_id()+"'");

			}
			if (unitChanges == null || unitChanges.size() == 0){
				u.setAvailable(4);
				this.hibernateTemplate.save(u);
				this.hibernateTemplate.bulkUpdate("update Units set available=4 where unit_id='"+u.getUnit_id()+"'");

			}
			
			if (indoorChanges != null && indoorChanges.size() != 0 ) {
				IndoorChange tIndoorChange = indoorChanges.get(0);
				iChange.setIndoorChange_id(tIndoorChange.getIndoorChange_id());
				iChange.setAvailable(tIndoorChange.getAvailable());
				this.hibernateTemplate.update(iChange);
			}
			if (indoorChanges == null || indoorChanges.size() == 0) {
				
			
				iChange.setAvailable(4);
				this.hibernateTemplate.save(iChange);
				this.hibernateTemplate.bulkUpdate("update Indoor set available = 4 where unit_id='"+iChange.getUnit_id()+"'");
			}

			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
			this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
			+"' where unit_id='"+u.getParent_unit_id()+"' and isAvailable=1");
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
		
		
	}
	
	/**
	 *  update indoor
	 * @param indoorChange
	 * @return
	 */
	public boolean updateIndoor(IndoorChange iChange,String version){
		try{
			List<Object[]> versiontime = new ArrayList<Object[]>();
			
			versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+ version +"'");
			Object[] versionObj = null;
			versionObj = versiontime.get(0);
			String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
			String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
			
			List<Indoor> indoors = this.hibernateTemplate.find("from Indoor where unit_id='"+iChange.getUnit_id()+"'");
			List<IndoorChange> temps = this.hibernateTemplate.find("from IndoorChange i where i.unit_id='"+iChange.getUnit_id()+"'and i.last_modify_time between '"+create+"' and '"+modify+"'");
		
			if(indoors !=null&&indoors.size()>0){
				Indoor i = indoors.get(0);
				iChange.setAddress(i.getAddress());
				iChange.setBooth_num(i.getBooth_num());
				iChange.setFloor_id(i.getFloor_id());
				iChange.setIndoor_id(i.getIndoor_id());
				iChange.setParent_poi_id(i.getParent_poi_id());
				iChange.setPoi_id(i.getPoi_id());
				iChange.setType(i.getType());
				iChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				if (temps == null || temps.size() == 0) {
					this.hibernateTemplate.save(iChange);
					i.setAvailable(4);
					i.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));	
					this.hibernateTemplate.update(i);

				}
				else{
					IndoorChange temp = temps.get(0);
					iChange.setIndoorChange_id(temp.getIndoorChange_id());
					this.hibernateTemplate.update(iChange);
				}

				String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
				this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
				+"' where unit_id='"+iChange.getParent_unit_id()+"' and isAvailable=1");
				
				return true;

			}else{
				logger.info("no indoor found, update failed");
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
		
		
	}
	
	/**
	 * add a point
	 * @param unit  
	 * @param unitChange
	 * @param poi
	 * @param poiChange
	 * @param indoor
	 * @param indoorChange
	 * @return
	 */
	public boolean add(Units u,UnitChange uChange,Poi p,PoiChange pChange,Indoor i,IndoorChange iChange){
		
		try{
			//this.hibernateTemplate.save(u);		
			//this.hibernateTemplate.save(i);
			this.hibernateTemplate.save(p);
			this.hibernateTemplate.save(uChange);
			this.hibernateTemplate.save(iChange);
			this.hibernateTemplate.save(pChange);
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
			this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
			+"' where unit_id='"+uChange.getParent_unit_id()+"' and isAvailable=1");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
	}
	/**
	 * delete a point
	 * @param unitid
	 * @param parentid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean delete(String unitid,String parentid,String version){
		try{
			String[] unit_id = unitid.split("\\*");
			System.out.println("合并测试之unit_id.size:"+unit_id.length);
			for (int j = 0; j < unit_id.length; j++) {
				
				
				Units u = (Units) this.hibernateTemplate.get(Units.class, unit_id[j]);
				List<Indoor> indoors = this.hibernateTemplate.find("from Indoor where unit_id='"+unit_id[j]+"'");
				if(u != null&&indoors != null&&indoors.size() > 0){
					Indoor i = indoors.get(0);
					UnitChange uChange = new UnitChange();
					IndoorChange iChange = new IndoorChange();
					uChange.setAction(0);
					uChange.setAddress(u.getAddress());
					uChange.setAlias(u.getAlias());
					uChange.setAvailable(3);
					uChange.setChecked(0);
					uChange.setCreate_time(new Date());
					uChange.setCreator("dr");
					uChange.setDescription(u.getDescription());
					uChange.setDetail_info(u.getDetail_info());
					uChange.setEmail(u.getEmail());
					uChange.setG_outdoor_id(u.getG_outdoor_id());
					uChange.setHas_indoor_map(u.getHas_indoor_map());
					if(u.getHas_outdoor()==null){
						uChange.setHas_outdoor(0);
					}else{
						uChange.setHas_outdoor(Integer.parseInt(u.getHas_outdoor()));
					}
					uChange.setKeyword(u.getKeyword());
					uChange.setLogo(u.getLogo());
					uChange.setName(u.getName());
					uChange.setOpening_time(u.getOpening_time());
					uChange.setParent_unit_id(parentid);
					uChange.setPhone(u.getPhone());
					uChange.setShow_name(u.getShow_name());
					uChange.setUnit_brand_id(u.getUnit_brand_id());
					uChange.setUnit_id(u.getUnit_id());
					uChange.setUnit_type_id(u.getUnit_type_id());
					uChange.setWebsite(u.getWebsite());
					uChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					
					iChange.setAction(0);
					iChange.setAddress(i.getAddress());
					iChange.setAvailable(3);
					iChange.setBooth_num(i.getBooth_num());
					iChange.setChecked(0);
					iChange.setCoord_x(i.getCoord_x());
					iChange.setCoord_y(i.getCoord_y());
					iChange.setCreate_time(new Date());
					iChange.setCreator("dr");
					iChange.setFloor_id(i.getFloor_id());
					iChange.setFrame(i.getFrame());
					iChange.setIndoor_id(i.getIndoor_id());
					iChange.setMax_x(i.getMax_x());
					iChange.setMax_y(i.getMax_y());
					iChange.setMin_x(i.getMin_x());
					iChange.setMin_y(i.getMin_y());
					iChange.setParent_poi_id(i.getParent_poi_id());
					iChange.setParent_unit_id(i.getParent_unit_id());
					iChange.setPoi_id(i.getPoi_id());
					iChange.setType(i.getType());
					iChange.setUnit_id(i.getUnit_id());
					iChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					
					List<Object[]> versiontime = new ArrayList<Object[]>();
					
					versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+version+"'");
					Object[] versionObj = null;
					versionObj = versiontime.get(0);
					String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
					String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
					
					List<UnitChange> utemps = this.hibernateTemplate.find("from UnitChange u where u.unit_id='" + unit_id[j] + "' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
			
					List<IndoorChange> itemps = this.hibernateTemplate.find("from IndoorChange u where u.unit_id='" + unit_id[j] + "' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
			
					 
					if (utemps == null || utemps.size() == 0) {
						this.hibernateTemplate.save(uChange);
					}
					else {
						UnitChange utemp = utemps.get(0);
						utemp.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						utemp.setAvailable(3);//uChange.setUnitChange_id(utemp.getUnitChange_id());
						this.hibernateTemplate.update(utemp);
					}
					if (itemps == null || itemps.size() == 0) {
						this.hibernateTemplate.save(iChange);
						
					}
					else {
						IndoorChange itemp = itemps.get(0);
						itemp.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						itemp.setAvailable(3);//iChange.setIndoorChange_id(itemp.getIndoorChange_id());
						this.hibernateTemplate.update(itemp);
					}
					
					i.setAvailable(4);
					i.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));	
					u.setAvailable(4);
					u.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));	
					this.hibernateTemplate.update(i);
					this.hibernateTemplate.update(u);
					
					String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
					this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
					+"' where unit_id='"+parentid+"' and isAvailable=1");
					
				}
				
				//处理新增的删除
				else{
					List<Object[]> versiontime = new ArrayList<Object[]>();
					
					versiontime = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+version+"'");
					Object[] versionObj = null;
					versionObj = versiontime.get(0);
					String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
					String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
					
					List<UnitChange> utemps = this.hibernateTemplate.find("from UnitChange u where u.unit_id='" + unit_id[j] + "' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
			
					List<IndoorChange> itemps = this.hibernateTemplate.find("from IndoorChange u where u.unit_id='" + unit_id[j] + "' and u.last_modify_time between '"+create+"' and '"+modify+"' order by u.last_modify_time desc");
			
					if (utemps.size() != 0 && utemps != null ) {
						UnitChange uChange = utemps.get(0);
						IndoorChange iChange = itemps.get(0);
						
						uChange.setAvailable(3);
						iChange.setAvailable(3);
						uChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						iChange.setLast_modify_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						this.hibernateTemplate.update(iChange);
						this.hibernateTemplate.update(uChange);
						
						String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());			
						this.hibernateTemplate.bulkUpdate("update Version set lastModifyTime='"+date
						+"' where unit_id='"+parentid+"' and isAvailable=1");
						
					}
					
				}
			}
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
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



