package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Company;

@Component("companyDAO")
public class CompanyDAO {
	
	private HibernateTemplate hibernateTemplate = null;
	private SessionFactory sessionFactory;
		
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	private Object[] params;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private String sql = null;
	

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
	public JSONArray Select(String company_id){
		jsonArray = new JSONArray();

		List<Object[]> resultList =  new ArrayList<Object[]>();
		
		sql = "select company_id,show_name,company_name_ch,company_name_en,email,phone,address_ch,address_en,con_per,contact,company_color,other_info,alt_con from Company"+
				 	" where company_id = ?";
		
		try{
			System.out.println(sql);
			params = new Object[]{company_id};
			resultList = (List<Object[]>)getHibernateTemplate().find(sql, params);
			System.out.println(resultList.size());
			jsonObject = new JSONObject();
			Object[] proxyObj = null;
			if(resultList.size() == 1)
			{
				 proxyObj = resultList.get(0);
				 jsonObject.put("company_id", proxyObj[0]);
				 jsonObject.put("show_name", proxyObj[1]);
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
				
				 jsonArray.add(jsonObject);
				
			}
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public boolean Insert(Company company){
		Session session = null;
		//LogUtil log = new LogUtil();
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(company);
			session.getTransaction().commit();
			//log.WriteLog("插入新province记录成功！province_id为："+province.getProvince_id());
			System.out.println("插入新company记录成功！为："+company.getCompany_id());
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
	
	public boolean edit(Company company){
		try {
			getHibernateTemplate().update(company);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}
	
	public boolean delete(String company_id){
		
		sql = "delete from Company"+
				 	" where company_id='" + company_id +"'" ;
		try{
			System.out.println(sql);
			getHibernateTemplate().bulkUpdate(sql);
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
		
		sql = "select company_id,show_name,email,phone,con_per,contact,last_time from Company";
		
		try{
			resultList = (List<Object[]>)getHibernateTemplate().find(sql);
			jsonObject = new JSONObject();
			Object[] companyObj = null;
			for(int i = 0; i < resultList.size(); i ++)
			{
				companyObj = resultList.get(i);
				 jsonObject.put("user_id", companyObj[0]);
				 jsonObject.put("account", companyObj[1]==null?"":companyObj[1]);
				 jsonObject.put("email", companyObj[2]==null ? "" : companyObj[2]);
				 jsonObject.put("phone", companyObj[3]==null ? "" : companyObj[3]);
				 jsonObject.put("con_per", companyObj[4]==null ? "" : companyObj[4]);
				 jsonObject.put("password", companyObj[5]==null ? "" : companyObj[5]);
				 jsonObject.put("last_time", companyObj[6]==null ? "" : companyObj[6]);
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
