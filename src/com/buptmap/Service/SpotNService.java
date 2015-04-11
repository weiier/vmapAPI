package com.buptmap.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;
import com.buptmap.DAO.SpotNDAO;
import com.opensymphony.xwork2.interceptor.annotations.After;
/**
 * New SpotService
 * @author weiier
 *
 */
@Component("spotNService")
public class SpotNService {
	private SpotNDAO spotNDAO;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * @param unit_id 
	 * @param floorid
	 * @return JSONArray spots
	 */
	public JSONArray  all(String unit_id,String floorid,int versionid){
		lock.writeLock().lock();
		try{
			return this.spotNDAO.findAll(unit_id, floorid,versionid);
		}finally{
			lock.writeLock().unlock();
		}
	}
	

	public SpotNDAO getSpotNDAO() {
		return spotNDAO;
	}
	@Resource
	public void setSpotNDAO(SpotNDAO spotNDAO) {
		this.spotNDAO = spotNDAO;
	}
	@After
	public void destory(){
		if(spotNDAO != null) {spotNDAO.destory(); spotNDAO = null; }
	}
}
