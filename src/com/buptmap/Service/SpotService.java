package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.SpotDAO;
import com.buptmap.DAO.ViewFieldLevelDAO;
import com.buptmap.model.Spot;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("spotService")
public class SpotService {
	private SpotDAO spotDAO;
	private ViewFieldLevelDAO fieldDAO;
	
	public SpotDAO getSpotDAO() {
		return spotDAO;
	}
	@Resource(name="spotDAO")
	public void setSpotDAO(SpotDAO spotDAO) {
		this.spotDAO = spotDAO;
	}
	public ViewFieldLevelDAO getFieldDAO() {
		return fieldDAO;
	}
	@Resource(name="viewfieldlevelDAO")
	public void setFieldDAO(ViewFieldLevelDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public String getParentId(String unit_id) {
		lock.writeLock().lock();
		try{
			return spotDAO.getParentId(unit_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray getCoord(String unit_id){
		lock.writeLock().lock();
		try{
			return spotDAO.getCoord(unit_id);
			
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public double[] getLatAndLon(String unit_id){
		lock.writeLock().lock();
		try{
			return spotDAO.getLatAndLon(unit_id);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray updated(String modifyTime, String place){
		lock.writeLock().lock();
		try{
			return spotDAO.updated(modifyTime, place);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public String getMaxModifyTime(){
		lock.writeLock().lock();
		try{
			return spotDAO.getMaxModifyTime();
		}finally{
			lock.writeLock().unlock();
		}
	}
	/**
	 * get unit by parent_unit_id
	 * @param unit_id
	 * @return array
	 */
	public JSONArray all(String unit_id){
		//先获取spot的所有字段名
		lock.writeLock().lock();
		try{
			return spotDAO.all(unit_id, fieldDAO.getAllFieldNames("spot_info", Spot.class));
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray around(Double latitude,Double longitude,Double radius,String type){
		lock.writeLock().lock();
		try{
			return spotDAO.around(latitude, longitude, radius, type);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray detail(String unit_id, String ulevel){
		lock.writeLock().lock();
		try{
			return spotDAO.detail(unit_id, ulevel, fieldDAO.getAvailableFieldNames("spot_info", ulevel, Spot.class));
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray search(String spot_name,String unit_id,String city_name,String type){
		lock.writeLock().lock();
		try{
			return spotDAO.search(spot_name, unit_id, city_name, type);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	@After
	void destroy(){
		if(spotDAO != null) {spotDAO.destory(); spotDAO = null; }
	}
}
