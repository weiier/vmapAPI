package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.UnitDAO;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component
public class UnitService {
	private UnitDAO unitDAO;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public JSONArray all(){
		lock.writeLock().lock();
		try{
			return this.unitDAO.findUnit();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray version(String unitid){
		lock.writeLock().lock();
		try{
			return this.unitDAO.findVersion(unitid);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray search(String keyword,String low,String high,String floor_id,String parent_id,String version){
		lock.writeLock().lock();
		try{
			return this.unitDAO.findkey(keyword,low,high,floor_id,parent_id,version);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public UnitDAO getUnitDAO() {
		return unitDAO;
	}
	@Resource
	public void setUnitDAO(UnitDAO unitDAO) {
		this.unitDAO = unitDAO;
	}
	@After
	public void destory(){
		if(unitDAO!=null){
			unitDAO.destory();
			unitDAO = null;
		}
	}
}
