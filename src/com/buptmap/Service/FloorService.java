package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.FloorDAO;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("floorService")
public class FloorService {
	private FloorDAO floorDAO;

	public FloorDAO getFloorDAO() {
		return floorDAO;
	}
	@Resource(name="floorDAO")
	public void setFloorDAO(FloorDAO floorDAO) {
		this.floorDAO = floorDAO;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public JSONArray around(String unit_id,String floor_id,String type){
		lock.writeLock().lock();
		try{
			return floorDAO.around(unit_id, floor_id, type);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray detail(String unit_id, String floor_id){
		lock.writeLock().lock();
		try{
			return floorDAO.detail(unit_id, floor_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray search(String unit_id){
		lock.writeLock().lock();
		try{
			return floorDAO.search(unit_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	@After
	void destroy(){
		if(floorDAO != null) {floorDAO.destory(); floorDAO = null; }
	}
}
