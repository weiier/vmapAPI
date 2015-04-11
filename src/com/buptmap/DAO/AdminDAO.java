package com.buptmap.DAO;

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

import com.buptmap.model.Admin;


@Component("adminDAO")
public class AdminDAO {
	
	private HibernateTemplate hibernateTemplate = null;
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public boolean edit(Admin admin){
		try {
			
			getHibernateTemplate().update(admin);
			return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}

}
