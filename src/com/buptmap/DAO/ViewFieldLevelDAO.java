package com.buptmap.DAO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.interceptor.annotations.After;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.KeyTable;
import com.buptmap.model.ViewFieldLevel;

/**
 * get available table or view fields
 * @author Peter
 * */
@Component("viewfieldlevelDAO")
public class ViewFieldLevelDAO {
	private HibernateTemplate hibernateTemplate = null;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	Map<String, Object> result = null;
	JSONArray jsonArray = null;
	JSONObject jsonObject = null;
	List<String> fieldStrs = null;
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(ViewFieldLevelDAO.class);
	
	
	public Map<String, Object> getResult() {
		return result;
	}
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public List<String> getFieldStrs() {
		return fieldStrs;
	}
	public void setFieldStrs(List<String> fieldStrs) {
		this.fieldStrs = fieldStrs;
	}
	public boolean updateLevels(String levels, String view){
		//lock.writeLock().lock();
		
		try{
			ViewFieldLevel viewFieldLevel = (ViewFieldLevel) getHibernateTemplate().get(ViewFieldLevel.class, view);
			viewFieldLevel.setField_level(levels);
			getHibernateTemplate().update(viewFieldLevel);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
		finally{
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> deleteUser(String uid){
		//lock.writeLock().lock();
		result = new HashMap<String, Object>();
		List<KeyTable> keyList = new ArrayList<KeyTable>();
		
		try{
			String sql = "from KeyTable v where v.id = ?";
			boolean success = false;
			String message = "执行过程中出现意外，导致修改用户失败";
			keyList = getHibernateTemplate().find(sql, uid);
			if(keyList.size() > 0){ 
				getHibernateTemplate().deleteAll(keyList);
				success = true; 
				message = "删除用户成功";
			}
			result.put("message", message);
			result.put("success", success);
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("message", e.getMessage());
			result.put("success", false);
			return result;
		}
		finally{
			//lock.writeLock().unlock();
		}
	}
	
	public Map<String, Object> editUser(String[] param){
		//lock.writeLock().lock();
		result = new HashMap<String, Object>();
		
		try{
			KeyTable user = (KeyTable) getHibernateTemplate().get(KeyTable.class, param[0]);
			user.setVersion(param[1]);
			user.setOpenlevel(param[2]);
			user.setValid(param[3]);
			user.setKeystr(param[4]);
			
			boolean success = true;
			String message = "修改用户成功";
			getHibernateTemplate().update(user);
			
			result.put("message", message);
			result.put("success", success);
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("message", e.getMessage());
			result.put("success", false);
			return result;
		}
		finally{
			//lock.writeLock().unlock();
		}
	}
	
	public Map<String, Object> addUser(String[] param){
		//lock.writeLock().lock();
		result = new HashMap<String, Object>();
		
		try{

			KeyTable user = new KeyTable();
			user.setId(param[0]);
			user.setVersion(param[1]);
			user.setOpenlevel(param[2]);
			user.setValid(param[3]);
			user.setKeystr(param[4]);
			
			getHibernateTemplate().save(user);
			boolean success = true;
			String message = "新增用户成功";
			
			result.put("message", message);
			result.put("success", success);
			return result;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("message", e.getMessage());
			result.put("success", false);
			return result;
		}
		finally{
			//lock.writeLock().unlock();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getUsers(){
		//lock.writeLock().lock();
		jsonArray = new JSONArray();
		Object[] user = null;
		List<Object[]> userList = new ArrayList<Object[]>();
		jsonObject = new JSONObject();
		
		try{
			String  sql = "select id,version,openlevel,valid,keystr from KeyTable";
			
			userList = getHibernateTemplate().find(sql);
			for(int i=0; i<userList.size(); i++){
				user = userList.get(i);
				jsonObject.put("id", user[0]);
				jsonObject.put("version", user[1]);
				jsonObject.put("openlevel", user[2]);
				jsonObject.put("valid", user[3]);
				jsonObject.put("keystr", user[4]);
				jsonArray.add(jsonObject);
			}
			if(user != null) user = null;
			return jsonArray;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			if(userList != null) { userList.clear(); userList = null; }
			//lock.writeLock().unlock();
		}
	}
	
	public JSONArray getFields(Class<?> classType, String view){
		//lock.writeLock().lock();
		Field[] fields = null;
		jsonArray = new JSONArray();
		String[] levels = null;
		jsonObject = new JSONObject();
		try{
			String  sql = "select field_level from ViewFieldLevel where view_name = ?";
			String fieldLevels = getHibernateTemplate().find(sql, view).toString();
			fieldLevels = fieldLevels.substring(1, fieldLevels.length() - 1);
			levels = fieldLevels.split(",");
			System.out.print(fieldLevels);
			fields = classType.getDeclaredFields();
			for(int i=0; i<fields.length; i++){
				jsonObject.put("name", fields[i].getName());
				if(i<levels.length) jsonObject.put("level", levels[i]);
				else jsonObject.put("level", "");
				jsonArray.add(jsonObject);
			}
			return jsonArray;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			levels = null;
		}
	}
	
	/**
	 * get available field names
	 * @param  viewName  String  view name
	 * @param  level   String  client's access authority
	 * @param  classtype  Class<?>  class type
	 * @return  List<String>  available field names
	 * */
	public List<String> getAllFieldNames(String viewName, Class<?> classType) {
		//lock.writeLock().lock();
		Field[] fields = null;
		
		try{
			fields = classType.getDeclaredFields();
			fieldStrs = new ArrayList<String>() ;
			
			for (int i=0; i < fields.length; i++) {
				fieldStrs.add(fields[i].getName());
				//System.out.println("fieldList.add("+i+", "+"\""+""+fields[i].getName()+""+"\""+");");
			}
			return fieldStrs;
		}
		catch(Exception e){ 
			e.printStackTrace();
			logger.error(e.getMessage());
			return null; 
		}
		finally{ 
			fields = null;
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * get available field names
	 * @param  viewName  String  view name
	 * @param  level   String  client's access authority
	 * @param  classtype  Class<?>  class type
	 * @return  List<String>  available field names
	 * */
	public List<String> getAvailableFieldNames(String viewName, String level, Class<?> classType) {
		String[] fieldLevels = null;
		//lock.writeLock().lock();
		Field[] fieldList = classType.getDeclaredFields();//TableFieldUtil.getPlaceAllFieldNames();
		try{
			fieldStrs = new ArrayList<String>() ;
			fieldLevels = getFieldLevels(viewName);
			System.out.println(fieldLevels.length + " " +fieldList.length + " " + level);
			for (int i=0; i < fieldList.length; i++) {
				if(Integer.parseInt(fieldLevels[i]) != 0){
					
					if(fieldLevels[i].compareTo(level)<=0){ 
						fieldStrs.add(fieldList[i].getName());
						//System.out.println("fieldList.add("+i+", "+"\""+""+fieldList.size()+""+"\""+");");
					}
				}
			}

			return fieldStrs;
		}
		catch(Exception e){ 
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null; 
		}
		finally{ 
			fieldLevels = null; 
			if(fieldList != null) { fieldList = null; }
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * get field levels
	 * @param  viewName  String  view name
	 * @return  String[]  field levels
	 * */
	public String[] getFieldLevels(String viewName){
		//lock.writeLock().lock();
		try{
			String sql = "select field_level from ViewFieldLevel where view_name=?";
			
			String field_level = getHibernateTemplate().find(sql, viewName).toString();
			field_level = field_level.substring(1, field_level.length()-1);
			System.out.println(viewName);
			System.out.println(field_level);
			//String[] levels = field_level.split(",");
			return field_level.split(",");
		}
		catch(Exception e){
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return null;
		}
		finally{ 
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
        if(jsonArray != null) { jsonArray.clear(); jsonArray = null; }
        if(jsonObject != null) { jsonObject.clear(); jsonObject = null; }
        if(fieldStrs != null) { fieldStrs.clear(); fieldStrs = null; }
        if(result != null) { result.clear(); result = null; }
        System.gc();
    }
}
