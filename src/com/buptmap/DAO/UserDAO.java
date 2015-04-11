package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.interceptor.annotations.After;

/**
 * initial class UserDAO, also contains some methods for authority check
 * @author Peter
 * */
@Component("user")
public class UserDAO {
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(UserDAO.class);
	public String uid = "";
	public String openlevel = "";
	public String version = "";
	public String keystr = "";
	
	public List<String> apiList = new ArrayList<String>();
	public List<String> mallList = new ArrayList<String>();
	public List<String> cityList = new ArrayList<String>();
	
	/**
	 * initial UserDAO
	 * @param  uid  String  client ID, get from http request
	 * */
	@SuppressWarnings("unchecked")
	public void userInit(String uid){
		//lock.writeLock().lock();
		Object[] user = null;
		List<Object[]> userList = new ArrayList<Object[]>();
		
		try{
			String sql = "select id,openlevel,version,keystr from User where id = ? and valid = 1";
			//System.out.println("userInit:"+sql);
			userList = (List<Object[]>)getHibernateTemplate().find(sql, uid);
			if(!userList.isEmpty()) {
				user = userList.get(0);

				if(!user.equals(null)){
					this.uid = uid;
					this.openlevel = user[1].toString();
					if(user[2] != null) this.version = user[2].toString();
					if(user[3] != null) this.keystr = user[3].toString();

				}
				
				setApiList(uid);
				setMallList(uid);
				setCityList(uid);
			}
			else this.uid = null;
			return ;
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return;//log.writeLog(logPath, e.toString());
		}
		finally{
        	if(user != null) user = null;
        	if(userList != null) { userList.clear(); userList = null; }
        	//lock.writeLock().unlock();
		}
	}
	
	/**
	 * initial available malls
	 * @param  id  String  client ID
	 * */
	@SuppressWarnings("unchecked")
	public void setMallList( String id ){
		//lock.writeLock().lock();
		String sql = "select mall_id from Mall where key_id = ?";
		List<String> malls = new ArrayList<String>();
		
		try{
			
			malls = (List<String>)getHibernateTemplate().find(sql, id);
			for(int i = 0; i<malls.size(); i++){
				mallList.add(malls.get(i));
				//System.out.println("malls["+i+"]:"+malls.get(i));
			}
			return;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			
		}
        finally{
        	if(malls != null) { malls.clear(); malls = null; }
        	//lock.writeLock().unlock();
        }
    }
	
	/**
	 * initial available cities
	 * @param  id  String  client ID
	 * */
    @SuppressWarnings("unchecked")
	public void setCityList( String id ){
    	//lock.writeLock().lock();
        String sql = "select city from City where key_id = ?";
        List<String> cities = new ArrayList<String>();
		try{
			
			cities = (List<String>)getHibernateTemplate().find(sql, id);
			for(int i = 0; i<cities.size(); i++){
				this.cityList.add(cities.get(i));
				//System.out.println("cities["+i+"]:"+cities.get(i));
			}
			return;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			
		}
		finally{
        	if(cities != null) { cities.clear(); cities = null; }
        	//lock.writeLock().unlock();
		}
    }
    
    /**
	 * initial available apis
	 * @param  id  String  client ID
	 * */
    public void setApiList( String id ){
    	//lock.writeLock().lock();
        String sql = "select api_name from Api where key_id = ?";
        String[] apis = null;
		try{
			
			String api = (String) getHibernateTemplate().find(sql, id).get(0);
			apis = api.split(",");
			for(int i = 0; i<apis.length; i++){
				this.apiList.add(apis[i]);
				//System.out.println("apis["+i+"]:"+apis[i]);
			}
			return;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			
		}
		finally{
        	if(apis != null){ apis = null; }
        	//lock.writeLock().unlock();
		}
    }
    
    /**
     * check access authority for current spot
     * @param  spot String  unique spot reference
     * @return  boolean
     * */
    public boolean isSpotValid( String spot){
    	//lock.writeLock().lock();
    	String sql = "select parent_unit_id from Spot where unit_id=?";
        String place_ref = null;
		try{
			
			place_ref = (String) getHibernateTemplate().find(sql, spot).get(0);
			System.out.println("place_ref:"+place_ref);
			if(mallList.contains(place_ref)){
                return true;
            }
			return false;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.getMessage());
			return false;
		}
		finally{
			//lock.writeLock().unlock();
        }
    }
    
    /**
     * check access authority for current mall
     * @param  spot String  unique mall reference
     * @return  boolean
     * */
    public boolean isMallValid( String mall ){
    	//lock.writeLock().lock();
    	if(mallList.contains(mall)){
            return true;
        }
        else 
        {
            String sql = "select city_name from Place where unit_id=?";
            String city = null;
    		try{
    			
    			city = (String) getHibernateTemplate().find(sql, mall).get(0);
    			//System.out.println("city:"+city);
    			if(isCityValid(city)){
                    return true;
                }
    			return false;
    		}
    		catch (Exception e) {
    			System.out.println(e.toString());
    			logger.error(e.getMessage());
    			return false;
    		}
    		finally{
    			//lock.writeLock().unlock();
    		}
        }
    }
    
    /**
     * check access authority for current city
     * @param  spot String  unique city name
     * @return  boolean
     * */
    public boolean isCityValid( String city ){
    	//lock.writeLock().lock();
    	try{
    		if(cityList.contains(city)){
    			return true;
    		}
    		else
    			return false;
    	}finally{
    		//lock.writeLock().unlock();
    	}
    }
    
    /**
     * check access authority for current api
     * @param  spot String  unique api name
     * @return  boolean
     * */
    public boolean isApiValid( String api ){
        if(apiList.contains(api)){
            return true;
        }
        else return false;
    }
    
    @After
    public void destory() {
		System.gc();
    }
}
