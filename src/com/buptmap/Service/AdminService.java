package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.AdminDAO;
import com.buptmap.model.Admin;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("adminService")
public class AdminService {
	
	private AdminDAO adminDAO;

	public AdminDAO getAdminDAO() {
		return adminDAO;
	}
	@Resource(name="adminDAO")
	public void setAdminDAO(AdminDAO adminDAO) {
		this.adminDAO = adminDAO;
	}
	
	public boolean edit(Admin admin){
		//System.out.println(proxy.getAddress());
		return adminDAO.edit(admin);
	}

}
