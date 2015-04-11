package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.sf.json.JSONArray;

import com.buptmap.DAO.PositionDAO;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("positionService")
public class PositionService {
	private PositionDAO positionDAO;

	public PositionDAO getpositionDAO() {
		return positionDAO;
	}
	@Resource(name="positionDAO")
	public void setpositionDAO(PositionDAO positionDAO) {
		this.positionDAO = positionDAO;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public JSONArray add2pos(String unit_id,String indoor_id){
		lock.writeLock().lock();
		try{
			return positionDAO.add2pos(unit_id, indoor_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray pos2add(String unit_id,String floor_id,Double x,Double y){
		lock.writeLock().lock();
		try{
			return positionDAO.pos2add(unit_id, floor_id, x, y);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	@After
	void destroy(){
		if(positionDAO != null) {positionDAO.destory(); positionDAO = null; }
	}
}
