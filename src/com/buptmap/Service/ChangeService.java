package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;
import com.buptmap.DAO.ChangeDAO;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component
public class ChangeService {
	private ChangeDAO changeDAO;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	
	public JSONArray units(String unitid,String create,String modify){
		lock.writeLock().lock();
		try{
			return this.changeDAO.findUnits(unitid, create, modify);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray all(String unitid,int versionid){
		lock.writeLock().lock();
		try{
			return this.changeDAO.findAll(unitid, versionid);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray one(String unitid,int versionid){
		lock.writeLock().lock();
		try{
			return this.changeDAO.findOne(unitid, versionid);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray unit(String unitid,int versionid){
			lock.writeLock().lock();
			try{
				return this.changeDAO.findUnit_test(unitid, versionid);
			}finally{
				lock.writeLock().unlock();
			}
	}
	
	public JSONArray find_company(String unitid,int versionid){
		lock.writeLock().lock();
		try{
			return this.changeDAO.findUnit_company(unitid, versionid);
		}finally{
			lock.writeLock().unlock();
		}

	}
	
	public ChangeDAO getChangeDAO() {
		return changeDAO;
	}
@Resource
	public void setChangeDAO(ChangeDAO changeDAO) {
		this.changeDAO = changeDAO;
	}

	@After
	public void destory(){
		if(changeDAO!=null){
			changeDAO.destory();
			changeDAO = null;
		}
	}
}
