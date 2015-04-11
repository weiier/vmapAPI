package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.UpdateDAO;
import com.buptmap.model.Indoor;
import com.buptmap.model.IndoorChange;
import com.buptmap.model.Poi;
import com.buptmap.model.PoiChange;
import com.buptmap.model.UnitChange;
import com.buptmap.model.Units;

@Component
public class UpdateService {
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private UpdateDAO updateDAO;
	
	public boolean update_company(String unit_id, String company_id,String version,String mark){
		lock.writeLock().lock();
		try{
			return this.updateDAO.update_company(unit_id,company_id,version,mark);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean update_proxy(String unit_id, String proxy_id, String version){
		lock.writeLock().lock();
		try{
			return this.updateDAO.update_proxy(unit_id,proxy_id,version);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean updateUnits(UnitChange u,IndoorChange i, String version){
		lock.writeLock().lock();
		try{
			return this.updateDAO.updateUnit(u,i,version);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean updateIndoor(IndoorChange i, String version){
		lock.writeLock().lock();
		try{
			return this.updateDAO.updateIndoor(i,version);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean add(Units u,UnitChange uChange,Poi p,PoiChange pChange,Indoor i,IndoorChange iChange){
		lock.writeLock().lock();
		try{
			return this.updateDAO.add(u, uChange, p, pChange, i, iChange);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public boolean delete(String unit_id,String parent_id,String version){
		lock.writeLock().lock();
		try{
			return this.updateDAO.delete(unit_id, parent_id,version);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public UpdateDAO getUpdateDAO() {
		return updateDAO;
	}

	@Resource
	public void setUpdateDAO(UpdateDAO updateDAO) {
		this.updateDAO = updateDAO;
	}


}







