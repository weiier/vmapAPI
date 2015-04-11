package com.buptmap.Service;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.TestDao;
/**
 * service
 * @author weiier
 *
 */
@Component("testService")
public class TestService {
	private TestDao testDao;
	private final ReadWriteLock lock =new ReentrantReadWriteLock();
	/**
	 * get data from testDao
	 * @param unit_id
	 * @return JSONArray
	 */
	public JSONArray all(String unit_id,String floorid){
		lock.writeLock().lock();
		try{
			return testDao.findAll(unit_id,floorid);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public JSONArray frame(String unit_id,String floorid){
		lock.writeLock().lock();
		try{
			return testDao.findFrame(unit_id,floorid);
		}finally{
			lock.writeLock().unlock();
		}
	}
	public TestDao getTestDao() {
		return testDao;
	}
	@Resource
	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}
	
}
