package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.DbDAO;

@Component("dbService")
public class DbService {
	private DbDAO dbDAO;

	public DbDAO getDbDAO() {
		return dbDAO;
	}
	@Resource(name="dbDAO")
	public void setDbDAO(DbDAO dbDAO) {
		this.dbDAO = dbDAO;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public JSONArray updated(String modifyTime){
		lock.writeLock().lock();
		try{
			return dbDAO.updated(modifyTime);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public String getMaxModifyTime(){
		lock.writeLock().lock();
		try{
			return dbDAO.getMaxModifyTime();
		}finally{
			lock.writeLock().unlock();
		}
	}
}
