package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.CompanyDAO;
import com.buptmap.DAO.CompanyNDAO;
import com.buptmap.model.Company;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("companyService")
public class CompanyService {
	
	private CompanyDAO companyDAO;
	private CompanyNDAO companyNDAO;
	public CompanyDAO getCompanyDAO() {
		return companyDAO;
	}
	@Resource(name="companyDAO")
	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	
	public CompanyNDAO getCompanyNDAO() {
		return companyNDAO;
	}
	@Resource
	public void setCompanyNDAO(CompanyNDAO companyNDAO) {
		this.companyNDAO = companyNDAO;
	}
	
	public JSONArray Select(String company_id){
		return companyDAO.Select(company_id);
	}

	public JSONArray All(){
		return companyDAO.All();
	}
	
	public boolean Insert(Company company){
	//	System.out.println(company.getAddress());
		return companyDAO.Insert(company);
	}
	
	public boolean edit(Company company){
		//System.out.println(company.getAddress());
		return companyDAO.edit(company);
	}

	public boolean delete(String company_id){
		return companyDAO.delete(company_id);
	}

	public boolean checkName(String name){
		return companyNDAO.checkName(name);
	}
	
	public boolean checkPhone(String phone){
		return companyNDAO.checkPhone(phone);
	}
	
	public boolean checkEmail(String email){
		return companyNDAO.checkEmail(email);
	}
}
