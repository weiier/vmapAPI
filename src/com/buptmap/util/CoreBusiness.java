package com.buptmap.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

public class CoreBusiness {
	
	public static String formJson(String message, boolean success, String jsoncallback){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("success", success);
			map.put("message", message);
			if(jsoncallback.equals(null) || jsoncallback.length() == 0)
				return JSONObject.fromObject(map).toString();
			else return jsoncallback+"("+JSONObject.fromObject(map).toString()+")";
		}catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.getMessage());
			return jsoncallback+"("+JSONObject.fromObject(map).toString()+")";
			// TODO: handle exception
		}
		finally{
			if(map != null){ map.clear(); map = null; }
		}
	}
	
	public static String formJson(JSONArray array, boolean success, String jsoncallback){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			map.put("success", success);
			map.put("total", array.size());
			map.put("rows", array);
			if(jsoncallback.equals(null) || jsoncallback.length() == 0)
				return JSONObject.fromObject(map).toString();
			else return jsoncallback+"("+JSONObject.fromObject(map).toString()+")";
		}catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.getMessage());
			return jsoncallback+"("+JSONObject.fromObject(map).toString()+")";
			// TODO: handle exception
		}
		finally{
			if(map != null){ map.clear(); map = null; }
		}
	}
	
	public static String formXML(String message, boolean success){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<results>\n");
		sb.append("<success>"+success+"</success>\n");
		sb.append("<message>"+message+"</message>\n");
		sb.append("</results>");
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static String formXML(JSONArray array, boolean success){
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
		sb.append("<results>\n");
		sb.append("<success>"+success+"</success>\n");
		sb.append("<total>"+array.size()+"</total>\n");
		sb.append("<rows>");
		JSONObject object = null;
		for(int i=0; i<array.size();i++){
			object = array.getJSONObject(i);
			Iterator<String> o = object.keys();
			sb.append("<unit>\n");
			for(;o.hasNext();){
				String temp = o.next();
				sb.append("<"+temp+">");
				sb.append(object.getString(temp));
				sb.append("</"+temp+">\n");
			}
			sb.append("</unit>\n");
		}
		sb.append("</rows>\n");
		sb.append("</results>");
		if(object != null) { object.clear(); object = null; }
		return sb.toString();
	}
	
	/**
	 * turn coordinate position to pixel position
	 * @param  coord  double  coordinate position
	 * @param  dpi  double
	 * @param  scale  double
	 * @return  int  pixel position
	 * */
	public static int getPixByCoord(double coord, double dpi, double scale){
		return (int)((double)(coord * dpi * 1000) / (double)(25.4 * scale));//pixel position;
	}
	
	/**
	 * turn pixel position to coordinate position
	 * @param  pix  double  pixel position
	 * @param  dpi  double
	 * @param  scale  double
	 * @return  Double  pixel position
	 * */
	public static Double getCoordByPix(double pix, double dpi, double scale){
		return (double)((double)(pix * 25.4 * scale) / (double)(dpi * 1000));//coordinate position;
	}
	
	/**
	 * md5 encryption
	 * @param  str  String  the string need to encrypt
	 * @return  String  encrypted string
	 * */
	public static String MD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		//create md5 instance
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64en = new BASE64Encoder();
		
		//md5 encryption
		String newstr = base64en.encode(md5.digest(str.getBytes("utf-8"))).toUpperCase();
		return newstr;
	}
	
}
