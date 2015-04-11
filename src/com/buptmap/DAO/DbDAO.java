package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("dbDAO")
public class DbDAO {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	JSONArray jsonArray = null;
	JSONObject jsonObject = null;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(DbDAO.class);
	
	@SuppressWarnings("unchecked")
	public JSONArray updated(String modifyTime){
		//lock.writeLock().lock();
		List<Object[]> updateddbList = new ArrayList<Object[]>();
		
		jsonArray = new JSONArray();
		try{
			String sql = "select unit_id,available from Db where last_modify_time > ?";
			
			updateddbList = (List<Object[]>)getHibernateTemplate().find(sql, ""+modifyTime+"");
			
			if(updateddbList.isEmpty()) return null;
			
			jsonObject = new JSONObject();
			Object[] placeObj = null;
			for (int i = 0; i < updateddbList.size(); i++) {
				placeObj = updateddbList.get(i);
				jsonObject.put("unit_id", placeObj[0].toString().toLowerCase());
				jsonObject.put("available", placeObj[1]);
				jsonArray.add(jsonObject);
			}
			return jsonArray;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}finally{
			if(updateddbList != null){ updateddbList.clear(); updateddbList = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public String getMaxModifyTime(){
		//lock.writeLock().lock();
		List<String> timeList = new ArrayList<String>();
		
		try{
			String sql = "select max(last_modify_time) from Db";
			
			timeList = (List<String>) getHibernateTemplate().find(sql);
			if(!timeList.isEmpty()){
				return timeList.get(0);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}finally{
			if(timeList != null) { timeList.clear(); timeList = null; }
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        System.gc();
    }
}
