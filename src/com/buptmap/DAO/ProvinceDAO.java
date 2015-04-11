package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.buptmap.model.Province;

@Component("provinceDAO")
public class ProvinceDAO {
	
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<Province> Select(String unitName){
		String sql = "select province_id,name from Province";//+
				 //" where name like '%"+unitName+"'";
		Session session = null;
		List<Province> resultList = new ArrayList<Province>();
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			Query query = session.createQuery(sql);
			resultList = (List<Province>)query.list();
			return resultList;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public boolean Insert(Province province){
		Session session = null;
		//LogUtil log = new LogUtil();
		try{
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(province);
			session.getTransaction().commit();
			//log.WriteLog("插入新province记录成功！province_id为："+province.getProvince_id());
			System.out.println("插入新province记录成功！province_id为："+province.getProvince_id());
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
}
