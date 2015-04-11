package com.buptmap.action;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.interceptor.annotations.After;

import com.buptmap.DAO.ParameterDAO;
import com.buptmap.DAO.UserDAO;
import com.buptmap.util.CoreBusiness;
import com.opensymphony.xwork2.ActionSupport;

/**
 * check url
 * @author Peter
 * */
@Scope("prototype")
public class BaseAction extends ActionSupport {
	
	private static final long serialVersionUID = -1754030090132832937L;
	
	protected String uid;
	protected String openlevel;
	public UserDAO user;
	
	public UserDAO getUser() {
		return user;
	}
	@Resource(name="user")
	public void setUserDAO(UserDAO user) {
		this.user = user;
	}

	public ParameterDAO para;
	public String logPath;
	public String basePicPath;
	private Map<Boolean, String> result = null;
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(BaseAction.class);
	
	/**
	 * check url
	 * @param  request  HttpServletRequest  current http request
	 * @return  result  Map<Boolean, String>  if check failed, then return false and message; return null if succeeded
	 * */
	public Map<Boolean, String> validCheck(HttpServletRequest request) throws IOException {
		//lock.writeLock().lock();
		try{
			result = new HashMap<Boolean, String>();
			uid = request.getParameter("client");
			para = new ParameterDAO(request);
			logPath = request.getSession().getServletContext().getInitParameter("logPath");
			basePicPath = request.getSession().getServletContext().getInitParameter("basePicPath");

			if(!para.isURIValid()){  //uri is invalid, without client and vkey
				result.put(false, ErrorMessage.URIError); 
				return result;
			}

			String requestURI = request.getRequestURI();//System.out.println("requestURI:"+requestURI);
			String action = requestURI.substring(requestURI.lastIndexOf("/")+1, requestURI.lastIndexOf("!"));
			String method = requestURI.substring(requestURI.indexOf('!')+1).toLowerCase();  //get action method
			logger.info("api : { action:"+action+"; method:"+method+" }");

			if(uid == null || uid.length() == 0){  //client is null
				result.put(false, ErrorMessage.ClientError); return result;
			}
			
			user.userInit(uid);
			
			if(user.uid == null){  //client is not exists
				result.put(false, ErrorMessage.ClientError); return result;
			}
			
			try {  //MD5 authority check
				String md51 = CoreBusiness.MD5(para.parasStr+user.keystr);
				String md52 = CoreBusiness.MD5(para.parasStr+para.bParas.get("vkey"));
				//System.out.println("md51:"+md51);
				//System.out.println("md52:"+md52);
				if(!md51.equals(md52)){
					result.put(false, ErrorMessage.VkeyError); return result;
				}
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.toString());
				e.printStackTrace();
			}

			this.openlevel = user.openlevel;

			if(!user.isApiValid(method)){  //check access authority for this api
				result.put(false, ErrorMessage.ApiError); return result;
			}
			/*
			if(para.bParas.containsKey("place") && para.bParas.get("place").length() == 36){  //check access authority for this place
				if(!user.isMallValid(para.bParas.get("place"))){
					result.put(false, ErrorMessage.PlaceError); return result;
				}
			}

			if(para.bParas.containsKey("city")){  //check access authority for this city
				if(!user.isCityValid(para.bParas.get("city"))){
					result.put(false, ErrorMessage.PlaceError); return result;
				}
			}
	*/
			return result;
		}finally{
			//lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
        if(result != null) { result.clear(); result = null; }
        //if(resultObj != null) { resultObj.clear(); resultObj = null; }
        System.gc();
    }
}
