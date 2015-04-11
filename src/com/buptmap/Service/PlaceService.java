package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.PlaceDAO;
import com.buptmap.DAO.ViewFieldLevelDAO;
import com.buptmap.model.Place;
import com.buptmap.util.TableFieldUtil;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("placeService")
public class PlaceService {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private PlaceDAO placeDAO;
	private ViewFieldLevelDAO fieldDAO;
	public PlaceDAO getPlaceDAO() {
		return placeDAO;
	}
	@Resource(name="placeDAO")
	public void setPlaceDAO(PlaceDAO placeDAO) {
		this.placeDAO = placeDAO;
	}
	public ViewFieldLevelDAO getFieldDAO() {
		return fieldDAO;
	}
	@Resource(name="viewfieldlevelDAO")
	public void setFieldDAO(ViewFieldLevelDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}
	private List<String> fieldList;
	
	public List<String> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}
	
	public String getMaxModifyTime(){
		lock.writeLock().lock();
		try{
			return placeDAO.getMaxModifyTime();
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray updated(String modifyTime, String place){
		lock.writeLock().lock();
		try{
			return placeDAO.updated(modifyTime, place);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray passage(String unit_id, int GATEID) {
		lock.writeLock().lock();
		try{
			return placeDAO.passage(unit_id, GATEID);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray all(String unit_id){
		lock.writeLock().lock();
		try{
			List<String> fieldNames = TableFieldUtil.getPlaceAllFieldNames();
			if(fieldNames != null && !fieldNames.isEmpty())
				return placeDAO.all(unit_id, fieldNames);
			return null;
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray around(String unit_id, Double latitude, Double longitude, Double radius, String type, String city){
		lock.writeLock().lock();
		try{
			if(latitude == null || longitude == null){
				double[] pos = placeDAO.getLatAndLon(unit_id);
				latitude = pos[0];
				longitude = pos[1];
			}
			System.out.println(type);
			System.out.println(radius);
			if(type == null) type = "";
			return placeDAO.around(latitude, longitude, radius, type, city);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray detail(String unit_id, String level){
		lock.writeLock().lock();
		try{
			fieldList = fieldDAO.getAvailableFieldNames("unit_info", level, Place.class);
			return placeDAO.detail(unit_id, level, fieldList);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray search(String unitName, String cityName) {
		lock.writeLock().lock();
		try{
			return placeDAO.search(unitName, cityName);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray getPublicFacilities(String unit_id, String floor) {
		lock.writeLock().lock();
		try {
			return placeDAO.getPublicFacilities(unit_id, floor);
		} finally {
			lock.writeLock().unlock();
		}
	}
	
	@After
	void destroy(){
		if(placeDAO != null) {placeDAO.destory(); placeDAO = null; }
	}
}
