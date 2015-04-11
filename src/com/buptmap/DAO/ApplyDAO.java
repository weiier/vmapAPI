package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.DAO.ProxyDAO;
import com.buptmap.model.Apply;
import com.buptmap.model.Indoor;
import com.buptmap.model.Proxy;


@Component("applyDAO")
public class ApplyDAO {
	
	private ProxyDAO proxyDAO;
	private HibernateTemplate hibernateTemplate = null;
	private SessionFactory sessionFactory;
		
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public ProxyDAO getProxyDAO() {
		return proxyDAO;
	}
	public void setProxyDAO(ProxyDAO proxyDAO) {
		this.proxyDAO = proxyDAO;
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
	
	public Apply selectById(int id){
		List<Apply> applys = new ArrayList<Apply>();
		applys = this.hibernateTemplate.find("from Apply where apply_id="+id);
		if(applys != null && applys.size() > 0){
			return applys.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray find(int id){
		jsonArray = new JSONArray();

		List<Object[]> resultList =  new ArrayList<Object[]>();
		
		sql = "select create_id,content,last_modify_time,state,reply,apply_id,title,sign,send_id from Apply"+
			 	" where  apply_id = '" + id + "'order by last_modify_time desc";
		
		
		try{
			
			resultList = (List<Object[]>)getHibernateTemplate().find(sql);
			System.out.println(resultList.size());
			jsonObject = new JSONObject();
			Object[] applyObj = null;
			if(resultList.size() == 1)
			{
				 applyObj = resultList.get(0);
				 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
				 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
				 jsonObject.put("state", applyObj[3]==null ? "" : applyObj[3]);
				 jsonObject.put("reply", applyObj[4]==null ? "" : applyObj[4]);
				 jsonObject.put("title", applyObj[6]==null ? "" : applyObj[6]);
				 jsonObject.put("sign", applyObj[7]==null ? "" : applyObj[7]);
				 jsonObject.put("create_id", applyObj[0]==null ? "" : applyObj[0]);
				 jsonArray.add(jsonObject);
				
			}
			return jsonArray;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	
	public JSONArray Select(String id,int mode){
		
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
		
		if(id.equals("admin")){
			
			if(mode == 1){
				String sql1 = "select create_id,content,last_modify_time,apply_id,title,send_id,state from Apply"+
					 	" where state = 0 and send_id='admin' order by last_modify_time desc";
				try{
					System.out.println(sql);
					resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
					System.out.println(resultList.size());
					jsonObject = new JSONObject();
					Object[] applyObj = null;
					if(resultList.size() > 0)
					{
						for(int i = 0; i < resultList.size(); i++)
						{
							 applyObj = resultList.get(i);
							 jsonObject.put("create_id", applyObj[0]);
							 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
							 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
							 jsonObject.put("apply_id", applyObj[3]==null ? "" : applyObj[3]);
							 jsonObject.put("title", applyObj[4]==null ? "" : applyObj[4]);
							 jsonObject.put("state", applyObj[6]==null ? "" : applyObj[6]);
							 jsonArray.add(jsonObject);
						}
						return jsonArray;
					}
					else {
						return null;
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
					return null;
				}

			}
			
			else if(mode == 2){
				String sql1 = "select create_id,content,last_modify_time,apply_id,title,send_id,state from Apply"+
					 	" where state != 0 and send_id='admin' order by last_modify_time desc";
				try{
					System.out.println(sql);
					resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
					System.out.println(resultList.size());
					jsonObject = new JSONObject();
					Object[] applyObj = null;
					if(resultList.size() > 0)
					{
						for(int i = 0; i < resultList.size(); i++)
						{
							applyObj = resultList.get(i);
							 jsonObject.put("create_id", applyObj[0]);
							 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
							 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
							 jsonObject.put("apply_id", applyObj[3]==null ? "" : applyObj[3]);
							 jsonObject.put("title", applyObj[4]==null ? "" : applyObj[4]);
							 jsonObject.put("state", applyObj[6]==null ? "" : applyObj[6]);
							 jsonArray.add(jsonObject);
						}
						return jsonArray;
					}
					else {
						return null;
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
					return null;
				}
			}
			else {
				return null;
			}
		}
		else {
			List<Proxy> proxyList = this.hibernateTemplate.find("from Proxy where proxy_id='"+id+"'");
			if (proxyList != null && proxyList.size() != 0) {
				
				String time = proxyList.get(0).getLast_time();
				System.out.println(time);
				if(mode == 1){
					String sql1 = "select create_id,content,last_modify_time,state,reply,apply_id,title,sign,send_id from Apply"+
						 	" where send_id='" + id + "' order by last_modify_time desc"  ;
					try{
						System.out.println(sql);
						resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
						System.out.println(resultList.size());
						jsonObject = new JSONObject();
						Object[] applyObj = null;
						if(resultList.size() > 0 )
						{
							for(int i = 0; i < resultList.size(); i++)
							{
								 applyObj = resultList.get(i);
								 jsonObject.put("create_id", applyObj[0]==null ? "" : applyObj[0]);
								 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
								 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
								 jsonObject.put("state", applyObj[3]==null ? "" : applyObj[3]);
								 jsonObject.put("reply", applyObj[4]==null ? "" : applyObj[4]);
								 jsonObject.put("apply_id", applyObj[5]==null ? "" : applyObj[5]);
								 jsonObject.put("title", applyObj[6]==null ? "" : applyObj[6]);
								 jsonObject.put("sign", applyObj[7]==null ? "" : applyObj[7]);
								 jsonArray.add(jsonObject);
							}
							 return jsonArray;
						}
						else {
							return null;
						}
					}
					catch (Exception e) {
						System.out.println(e.toString());
						return null;
					}
				}
				else if (mode == 2) {
					String sql1 = "select create_id,content,last_modify_time,state,reply,apply_id,title,sign,send_id from Apply"+
						 	" where  create_id = '" + id + "'order by last_modify_time desc";
					try{
						System.out.println(sql);
						resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
						System.out.println(resultList.size());
						jsonObject = new JSONObject();
						Object[] applyObj = null;
						if(resultList.size() > 0 )
						{
							for(int i = 0; i < resultList.size(); i++)
							{
								 applyObj = resultList.get(i);
								 jsonObject.put("create_id", applyObj[0]==null ? "" : applyObj[0]);
								 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
								 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
								 jsonObject.put("state", applyObj[3]==null ? "" : applyObj[3]);
								 jsonObject.put("reply", applyObj[4]==null ? "" : applyObj[4]);
								 jsonObject.put("apply_id", applyObj[5]==null ? "" : applyObj[5]);
								 jsonObject.put("title", applyObj[6]==null ? "" : applyObj[6]);
								 jsonObject.put("sign", applyObj[7]==null ? "" : applyObj[7]);
								 jsonArray.add(jsonObject);
							}
							 return jsonArray;
						}
						else {
							return null;
						}
					}
					catch (Exception e) {
						System.out.println(e.toString());
						return null;
					}
				}
				else {
					return null;
				}
				
			}
			else {
				return null;
			}
		}
	}

	public JSONArray AdminSelect_new(){
			String sql1 = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign from Apply"+
				 	" where state = 0  order by last_modify_time desc";
			return this.searchUnit(sql1);
	}
	public JSONArray AdminSelect_old(){
			String sql1 = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign from Apply"+
				 	" where state != 0  order by last_modify_time desc";
			return this.searchUnit(sql1);
	}
	public JSONArray AdminSelect_inform(){
		
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
		
				String sql1 = "select create_id,content,last_modify_time,apply_id,title,send_id,state from Apply"+
					 	" where create_id='admin' order by last_modify_time desc";
				try{
					System.out.println(sql);
					resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
					System.out.println(resultList.size());
					jsonObject = new JSONObject();
					Object[] applyObj = null;
					if(resultList.size() > 0)
					{
						for(int i = 0; i < resultList.size(); i++)
						{
							applyObj = resultList.get(i);
							 jsonObject.put("send_id", applyObj[5]);
							 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
							 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
							 jsonObject.put("apply_id", applyObj[3]==null ? "" : applyObj[3]);
							 jsonObject.put("title", applyObj[4]==null ? "" : applyObj[4]);
							 jsonObject.put("state", applyObj[6]==null ? "" : applyObj[6]);
							 jsonArray.add(jsonObject);
						}
						return jsonArray;
					}
					else {
						return null;
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
					return null;
				}
	}
	
	public JSONArray ProxySelect_self(String id){
		
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
	
					String sql1 = "select create_id,content,last_modify_time,state,reply,apply_id,title,sign,send_id from Apply"+
						 	" where  create_id = '" + id + "'order by last_modify_time desc";
					try{
						System.out.println(sql);
						resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
						System.out.println(resultList.size());
						jsonObject = new JSONObject();
						Object[] applyObj = null;
						if(resultList.size() > 0 )
						{
							for(int i = 0; i < resultList.size(); i++)
							{
								 applyObj = resultList.get(i);
								 jsonObject.put("create_id", applyObj[0]==null ? "" : applyObj[0]);
								 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
								 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
								 jsonObject.put("state", applyObj[3]==null ? "" : applyObj[3]);
								 jsonObject.put("reply", applyObj[4]==null ? "" : applyObj[4]);
								 jsonObject.put("apply_id", applyObj[5]==null ? "" : applyObj[5]);
								 jsonObject.put("title", applyObj[6]==null ? "" : applyObj[6]);
								 jsonObject.put("sign", applyObj[7]==null ? "" : applyObj[7]);
								 jsonArray.add(jsonObject);
							}
							 return jsonArray;
						}
						else {
							return null;
						}
					}
					catch (Exception e) {
						System.out.println(e.toString());
						return null;
					}
	}

	
	public JSONArray ProxySelect_admin(String id){
		
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
			List<Proxy> proxyList = this.hibernateTemplate.find("from Proxy where proxy_id='"+id+"'");
			if (proxyList != null && proxyList.size() != 0) {
				
				String time = proxyList.get(0).getLast_time();
				System.out.println(time);

					String sql1 = "select create_id,content,last_modify_time,state,reply,apply_id,title,sign,send_id from Apply"+
						 	" where send_id='" + id + "' order by last_modify_time desc"  ;
					try{
						System.out.println(sql);
						resultList = (List<Object[]>)getHibernateTemplate().find(sql1);
						System.out.println(resultList.size());
						jsonObject = new JSONObject();
						Object[] applyObj = null;
						if(resultList.size() > 0 )
						{
							for(int i = 0; i < resultList.size(); i++)
							{
								 applyObj = resultList.get(i);
								 jsonObject.put("create_id", applyObj[0]==null ? "" : applyObj[0]);
								 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
								 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
								 jsonObject.put("state", applyObj[3]==null ? "" : applyObj[3]);
								 jsonObject.put("reply", applyObj[4]==null ? "" : applyObj[4]);
								 jsonObject.put("apply_id", applyObj[5]==null ? "" : applyObj[5]);
								 jsonObject.put("title", applyObj[6]==null ? "" : applyObj[6]);
								 jsonObject.put("sign", applyObj[7]==null ? "" : applyObj[7]);
								 jsonArray.add(jsonObject);
							}
							 return jsonArray;
						}
						else {
							return null;
						}
					}
					catch (Exception e) {
						System.out.println(e.toString());
						return null;
					}
			}
			else {
				return null;
			}
	}

	/**
	 * 
	 * 根据proxy_id和floor_id返回专员管理的咨询
	 */
	public JSONArray proxySelect_old(String proxy_id,String floor_id){
		
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
		List<Object[]> versionList = new ArrayList<Object[]>();
		List<Object[]> unitList = new ArrayList<Object[]>();
		//改动：插入的create_id为company_id
		
				String sql = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign,version_id from Apply"+
					 	" where state != 0 and floor_id='"+floor_id+"' and send_id != 'admin' order by last_modify_time desc";
				try{
					resultList = (List<Object[]>)getHibernateTemplate().find(sql);
					jsonObject = new JSONObject();
					Object[] applyObj = null;
					Object[] companyObj = null;
					Object[] versionObj = null;
					if(resultList.size() > 0 )
					{
						for(int i = 0; i < resultList.size(); i++)
						{
							applyObj = resultList.get(i);
							
							versionList = hibernateTemplate.find("select createTime,lastModifyTime from Version  where version_id='"+applyObj[8]+"'");
							if(versionList != null && versionList.size() > 0){
								versionObj = versionList.get(0);
								String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
								String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
								unitList = this.hibernateTemplate.find("select unit_id from UnitChange where unit_id='"+applyObj[5]
										+"' and proxy_id='"+proxy_id+"' and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
								if(unitList.size() > 0){
									resultList2 = this.hibernateTemplate.find("select show_name,phone from Company where company_id='"+applyObj[0]+"'");
									
									if(resultList2.size() > 0 && resultList2 != null){
										companyObj = resultList2.get(0);
										jsonObject.put("show_name", companyObj[0]==null?"":companyObj[0]);
										jsonObject.put("phone", companyObj[1]==null?"":companyObj[1]);
										 jsonObject.put("create_id", applyObj[0]);
										 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
										 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
										 jsonObject.put("apply_id", applyObj[3]==null ? "" : applyObj[3]);
										 jsonObject.put("title", applyObj[4]==null ? "" : applyObj[4]);
										 jsonObject.put("state", applyObj[6]==null ? "" : applyObj[6]);
										 jsonObject.put("sign", applyObj[7]==null ? "0" : applyObj[7]);
										 jsonArray.add(jsonObject);
									}
								}
							}
						}
						return jsonArray;
					}
					else {
						return null;
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
					return null;
				}
	}
	
	public JSONArray proxySelect_new(String proxy_id,String floor_id){
		
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
		List<Object[]> versionList = new ArrayList<Object[]>();
		List<Object[]> unitList = new ArrayList<Object[]>();
		//改动：插入的create_id为company_id
		
				String sql = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign,version_id from Apply"+
					 	" where state = 0 and floor_id='"+floor_id+"' and send_id != 'admin' order by last_modify_time desc";
				try{
					resultList = (List<Object[]>)getHibernateTemplate().find(sql);
					jsonObject = new JSONObject();
					Object[] applyObj = null;
					Object[] companyObj = null;
					Object[] versionObj = null;
					if(resultList.size() > 0 ){
						for(int i = 0; i < resultList.size(); i++){
							applyObj = resultList.get(i);
							
							versionList = hibernateTemplate.find("select createTime,lastModifyTime from Version  where version_id='"+applyObj[8]+"'");
							if(versionList != null && versionList.size() > 0){
								versionObj = versionList.get(0);
								String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
								String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
								
								unitList = this.hibernateTemplate.find("select unit_id from UnitChange where unit_id='"+applyObj[5]
										+"' and proxy_id='"+proxy_id+"' and last_modify_time between '"+create+"' and '"+modify+"'  order by create_time desc");
								if(unitList.size() > 0){
									
									resultList2 = this.hibernateTemplate.find("select show_name,phone from Company where company_id='"+applyObj[0]+"'");
									
									if(resultList2.size() > 0 && resultList2 != null){
										companyObj = resultList2.get(0);
										jsonObject.put("show_name", companyObj[0]==null?"":companyObj[0]);
										jsonObject.put("phone", companyObj[1]==null?"":companyObj[1]);
										 jsonObject.put("create_id", applyObj[0]);
										 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
										 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
										 jsonObject.put("apply_id", applyObj[3]==null ? "" : applyObj[3]);
										 jsonObject.put("title", applyObj[4]==null ? "" : applyObj[4]);
										 jsonObject.put("state", applyObj[6]==null ? "" : applyObj[6]);
										 jsonObject.put("sign", applyObj[7]==null ? "0" : applyObj[7]);
										 jsonArray.add(jsonObject);
									}
								}
							
							}
						}
						return jsonArray;
					}
					else {
						return null;
					}
				}
				catch (Exception e) {
					System.out.println(e.toString());
					return null;
				}
	}
	
	
	/**
	 * 
	 * 返回整个楼层的咨询信息
	 */
	public JSONArray allSelect_new(String floor_id){
				String sqlStr = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign,version_id from Apply"+
					 	" where state = 0 and floor_id='"+floor_id+"' and send_id != 'admin' order by last_modify_time desc";
				return this.searchUnit(sqlStr);
	}
	
	public JSONArray allSelect_old(String floor_id){
				String sqlStr = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign,version_id from Apply"+
					 	" where state != 0 and floor_id='"+floor_id+"' and send_id != 'admin' order by last_modify_time desc";
				return this.searchUnit(sqlStr);
	}
	
	public JSONArray commonSelectNew(){
				String sqlStr = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign,version_id from Apply"+
					 	" where state = 0 and send_id = 'admin' order by last_modify_time desc";
				return this.searchUnit(sqlStr);
	}
	
	public JSONArray commonSelectOld(){
				String sqlStr = "select create_id,content,last_modify_time,apply_id,title,send_id,state,sign,version_id from Apply"+
					 	" where state != 0 and send_id = 'admin' order by last_modify_time desc";
				return this.searchUnit(sqlStr);
	}
	
	public boolean Insert(Apply apply){
		Session session = null;
		try{
			System.out.println("apply content为："+ apply.getApply_id());
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(apply);
			session.getTransaction().commit();
		
			System.out.println("插入新apply记录成功！apply_id为："+ apply.getApply_id());
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		finally{
			session.close(); session = null;
		}
	}
	
	public boolean edit(Apply apply){
		try {
			getHibernateTemplate().update(apply);
			return true;
			
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public boolean delete(int apply_id){
		
		sql = "delete from Apply"+
				 	" where apply_id=" + apply_id ;
		
		try{
			getHibernateTemplate().bulkUpdate(sql);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}

	public boolean updatesign(int apply_id){
		
		sql = "update Apply set sign = '1'"+
				 	" where apply_id=" + apply_id ;
		
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
	
	private JSONArray searchUnit(String searchSql){
		jsonArray = new JSONArray();
		List<Object[]> resultList =  new ArrayList<Object[]>();
		List<Object[]> resultList2 =  new ArrayList<Object[]>();
		try{
			resultList = (List<Object[]>)getHibernateTemplate().find(searchSql);
			jsonObject = new JSONObject();
			Object[] applyObj = null;
			Object[] companyObj = null;
			if(resultList.size() > 0 ){
				for(int i = 0; i < resultList.size(); i++){
					applyObj = resultList.get(i);
					resultList2 = this.hibernateTemplate.find("select show_name,phone from Company where company_id='"+applyObj[0]+"'");
					
					if(resultList2.size() > 0 && resultList2 != null){
						companyObj = resultList2.get(0);
						jsonObject.put("show_name", companyObj[0]==null?"":companyObj[0]);
						jsonObject.put("phone", companyObj[1]==null?"":companyObj[1]);
						 jsonObject.put("create_id", applyObj[0]);
						 jsonObject.put("content", applyObj[1]==null ? "" : applyObj[1]);
						 jsonObject.put("last_modify_time", applyObj[2]==null ? "" : applyObj[2]);
						 jsonObject.put("apply_id", applyObj[3]==null ? "" : applyObj[3]);
						 jsonObject.put("title", applyObj[4]==null ? "" : applyObj[4]);
						 jsonObject.put("state", applyObj[6]==null ? "" : applyObj[6]);
						 jsonObject.put("sign", applyObj[7]==null ? "0" : applyObj[7]);
						 jsonArray.add(jsonObject);
					}
				}
				return jsonArray;
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
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
}
