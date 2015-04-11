package com.buptmap.DAO;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import com.buptmap.model.Company;
@Component
public class CompanyNDAO {
	private HibernateTemplate hibernateTemplate;

	public boolean checkName(String name){
		List<Company> companys = this.hibernateTemplate.find("from Company c where c.show_name='"+name+"'");
		if(companys.size() > 0 && companys != null){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkPhone(String phone){
		List<Company> companys = this.hibernateTemplate.find("from Company c where c.phone='"+phone+"'");
		if(companys.size() > 0 && companys != null){
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkEmail(String email){
		List<Company> companys = this.hibernateTemplate.find("from Company c where c.email='"+email+"'");
		if(companys.size() > 0 && companys != null){
			return true;
		}else {
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
