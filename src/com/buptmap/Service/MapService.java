package com.buptmap.Service;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.MapDAO;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("mapService")
public class MapService {
	private MapDAO mapDAO;

	public MapDAO getMapDAO() {
		return mapDAO;
	}
	@Resource(name="mapDAO")
	public void setMapDAO(MapDAO mapDAO) {
		this.mapDAO = mapDAO;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public Map<String, String> getCalculateInfo(String unit_id, String floor_id, int level){
		lock.writeLock().lock();
		try{
			return mapDAO.getCalculateInfo(unit_id, floor_id, level);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public double[] getCoordinate(String unit_id, String parent_unit_id){
		lock.writeLock().lock();
		try{
			return mapDAO.getCoordinate(unit_id, parent_unit_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray all(String unit_id){
		lock.writeLock().lock();
		try{
			return mapDAO.all(unit_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray detail(String unit_id,String floor_id,Integer level){
		lock.writeLock().lock();
		try{
			return mapDAO.detail(unit_id, floor_id, level);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public String getMapPath(String unit_id,String floor_id,Integer level, String style){
		lock.writeLock().lock();
		try{
			return mapDAO.getMapPath(unit_id, floor_id, level, style);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	@After
	void destroy(){
		if(mapDAO != null) {mapDAO.destory(); mapDAO = null; }
	}
}
