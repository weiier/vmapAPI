package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.Null;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Proxy;
import com.buptmap.model.UnitChange;
import com.buptmap.model.Units;
import com.buptmap.model.Spot;

@Component("proxyDAO")
public class ProxyDAO {
	
	private HibernateTemplate hibernateTemplate = null;
	private SessionFactory sessionFactory;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	private Object[] params;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private String sql = null;
	//private String availableFields;
	
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
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
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	
	
	
	
	@SuppressWarnings("unchecked")
	public JSONArray Select(String proxy_id){
		jsonArray = new JSONArray();

		List<Object[]> resultList =  new ArrayList<Object[]>();
		
		sql = "select proxy_id,proxy_name,email,phone,address,proxy_color,other_info,password,time,show_name,contact,con_per,last_time,floor_id from Proxy"+
				 	" where proxy_id = ?";
		
		try{
			System.out.println(sql);
			params = new Object[]{proxy_id};
			resultList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			System.out.println(resultList.size());
			jsonObject = new JSONObject();
			Object[] proxyObj = null;
			if(resultList.size() == 1)
			{
				 proxyObj = resultList.get(0);
				 jsonObject.put("proxy_id", proxyObj[0]);
				 jsonObject.put("proxy_name", proxyObj[1]);
				 jsonObject.put("email", proxyObj[2]==null ? "" : proxyObj[2]);
				 jsonObject.put("phone", proxyObj[3]==null ? "" : proxyObj[3]);
				 jsonObject.put("address", proxyObj[4]==null ? "" : proxyObj[4]);
				 jsonObject.put("proxy_color", proxyObj[5]==null ? "" : proxyObj[5]);
				 jsonObject.put("other_info", proxyObj[6]==null ? "" : proxyObj[6]);
				 jsonObject.put("password", proxyObj[7]);
				 jsonObject.put("time", proxyObj[8]==null ? "" : proxyObj[8]);
				 jsonObject.put("show_name", proxyObj[9]);
				 jsonObject.put("contact", proxyObj[10]==null ? "" : proxyObj[10]);
				 jsonObject.put("con_per", proxyObj[11]==null ? "" : proxyObj[11]);
				 jsonObject.put("last_time", proxyObj[12]==null ? "" : proxyObj[12]);
				 jsonObject.put("floor_id", proxyObj[13]==null ? "" : proxyObj[13]);
				
				 jsonArray.add(jsonObject);
				
			}
			System.out.println(jsonArray);
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public boolean Insert(Proxy proxy){
		Session session = null;
		//LogUtil log = new LogUtil();
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(proxy);
			session.getTransaction().commit();
			//log.WriteLog("插入新province记录成功！province_id为："+province.getProvince_id());
			System.out.println("插入新province记录成功！province_id为："+proxy.getProxy_id());
			return true;
		}
		catch (Exception e) {
			//log.WriteLog(e.getMessage());
			return false;
		}
		finally{
			session.close(); session = null;
		}
	}
	
	public boolean edit(Proxy proxy){
		try {
			System.out.println(proxy.getProxy_id());
			getHibernateTemplate().update(proxy);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}
	
	public boolean updateState(String proxy_id,int state){
		try{
			this.hibernateTemplate.bulkUpdate("update Proxy set state="+state+" where proxy_id='"+proxy_id+"'");
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean delete(String proxy_id){
		
		sql = "delete from Proxy"+
				 	" where proxy_id='" + proxy_id +"'" ;
		
		try{
			System.out.println(sql);
			
			getHibernateTemplate().bulkUpdate(sql);
			
			//getHibernateTemplate().update(entity) 
			
			return true;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray All(){
		jsonArray = new JSONArray();

		List<Object[]> resultList =  new ArrayList<Object[]>();
		
		sql = "select proxy_id,proxy_name,email,phone,address,proxy_color,other_info,password,time,show_name,contact,con_per,last_time,floor_id from Proxy";
		
		try{
			System.out.println(sql);
			resultList = (List<Object[]>)getHibernateTemplate().find(sql);
			System.out.println(resultList.size());
			jsonObject = new JSONObject();
			Object[] proxyObj = null;
			for(int i = 0; i < resultList.size(); i ++)
			{
				 proxyObj = resultList.get(i);
				 jsonObject.put("proxy_id", proxyObj[0]);
				 jsonObject.put("proxy_name", proxyObj[1]);
				 jsonObject.put("email", proxyObj[2]==null ? "" : proxyObj[2]);
				 jsonObject.put("phone", proxyObj[3]==null ? "" : proxyObj[3]);
				 jsonObject.put("address", proxyObj[4]==null ? "" : proxyObj[4]);
				 jsonObject.put("proxy_color", proxyObj[5]==null ? "" : proxyObj[5]);
				 jsonObject.put("other_info", proxyObj[6]==null ? "" : proxyObj[6]);
				 jsonObject.put("password", proxyObj[7]);
				 jsonObject.put("time", proxyObj[8]==null ? "" : proxyObj[8]);
				 jsonObject.put("show_name", proxyObj[9]==null ? "" : proxyObj[9]);
				 jsonObject.put("contact", proxyObj[10]==null ? "" : proxyObj[10]);
				 jsonObject.put("con_per", proxyObj[11]==null ? "" : proxyObj[11]);
				 jsonObject.put("last_time", proxyObj[12]==null ? "" : proxyObj[12]);
				 jsonObject.put("floor_id", proxyObj[13]==null ? "" : proxyObj[13]);
				 jsonArray.add(jsonObject);
				
			}
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	

}
