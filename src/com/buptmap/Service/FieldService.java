package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.buptmap.DAO.ViewFieldLevelDAO;

@Component("fieldService")
public class FieldService {
	ViewFieldLevelDAO fieldDAO;

	public ViewFieldLevelDAO getFieldDAO() {
		return fieldDAO;
	}
	@Resource(name="viewfieldlevelDAO")
	public void setFieldDAO(ViewFieldLevelDAO fieldDAO) {
		this.fieldDAO = fieldDAO;
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public List<String> getAvailableFieldNames(String viewName, String level, Class<?> classType){
		lock.writeLock().lock();
		try{
			return fieldDAO.getAvailableFieldNames(viewName, level, classType);
		}finally{
			lock.writeLock().unlock();
		}
	}
	
	public List<String> getAllFieldNames(String viewName, Class<?> classType) {
		lock.writeLock().lock();
		try{
			return fieldDAO.getAllFieldNames(viewName, classType);
		}finally{
			lock.writeLock().unlock();
		}
	}
}
