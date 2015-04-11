package com.buptmap.DAO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * initial parameters in url
 * @author Peter
 * */
public class ParameterDAO {
	public String client;
	public String vkey;
	public Map<String, String> bParas=new HashMap<String,String>();  //store parameters
	public String parasStr = "";  //join parameter values together
	//private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(ParameterDAO.class);
	
	/**
	 * initial ParameterDAO
	 * @param  request  HttpServletRequest  当前http请求
	 * */
	public ParameterDAO(HttpServletRequest request) throws UnsupportedEncodingException{
		//lock.writeLock().lock();
		//System.out.println(request.getCharacterEncoding());
		request.setCharacterEncoding("utf-8"); //可要可不要的一句
		
		System.out.println("accept-charset: "+request.getHeader("accept-charset"));
		//System.out.println(request.getRequestURI());
		System.out.println("queryString: "+request.getQueryString());
		//boolean isMozilla = false;
		String userAgent = request.getHeader("user-agent");
		System.out.println(userAgent);
		
		//String charSetType = "gbk";
		//if(userAgent!=null && userAgent.contains("Mozilla")){
			//charSetType = "utf-8";
		//	isMozilla = true;
		//}
		client = request.getParameter("client");
		vkey = request.getParameter("vkey");
		logger.info("request start......");
		logger.info("client : { uid:\""+ client +"\"; vkey:\"" + vkey +"\" }");
		//System.out.println("request start\nclient : { uid:\""+ client +"\"; vkey:\"" + vkey +"\" }");
		try{
			//store parameter values and join them together except jsoncallback and vkey
			Enumeration<String> queryPara = request.getParameterNames();
			String logMessage = "parameters : { ";
			
			for (;queryPara.hasMoreElements();) {
				String curPara = queryPara.nextElement().toLowerCase();
				//String curPara = queryPara.nextElement();
				//change parameter value's char set type by browser to avoid messy code 
				//System.out.println(request.getParameter(curPara));
				//System.out.println(curPara);
				String value = new String(request.getParameter(curPara).getBytes("ISO-8859-1"), "utf-8");
				//String value = new String(request.getParameter(curPara));
				//if(!isMozilla) 
					//value = new String(request.getParameter(curPara).getBytes("ISO-8859-1"), "gb2312");
				
				if(!curPara.equals("jsoncallback") && !curPara.equals("vkey")){  //join values together for MD5 check
					parasStr += value;
					if(!curPara.equals("client")){
						logMessage += curPara+":"+"\""+value+"\""+"; ";
					}
				}
				bParas.put(curPara, value);  //store parameter values
			}
			logMessage = logMessage.substring(0, logMessage.length()) + "}";
			//System.out.println(logMessage);
			logger.info(logMessage);
			
			return;
		}catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();// TODO: handle exception
		}finally{
			//lock.writeLock().unlock();
		}
	}
	
	/**
	 * check URI
	 * @return  boolean
	 * */
	public boolean isURIValid()//含有client和vkey
	{
		//lock.writeLock().lock();
		try{
			if(client!=null && vkey!=null)
			{
				return true;
			}
			return false;
		}finally{
			//lock.writeLock().unlock();
		}
	}
}
