package com.buptmap.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.interceptor.annotations.After;
import com.buptmap.util.BoundsUtil;
import com.buptmap.util.LogUtil;

import net.sf.json.JSONObject;

@Scope("prototype")
public class GPSAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6164345140545330456L;
	
	private JSONObject resultObj;
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	private String lat;
	private String lon;
	private String place;
	
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}

	LogUtil log = new LogUtil();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(GPSAction.class);
	
	public String gps2coord() throws IOException{
		lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("lat") || !para.bParas.containsKey("lon")){
			map.put("success", false);
			map.put("message", ErrorMessage.ParameterError);
			logger.info(ErrorMessage.ParameterError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		if (!validCheckResult.isEmpty()) {
			map.put("success", false);
			map.put("message", validCheckResult.get(false));
			logger.info(validCheckResult.get(false));
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		place = para.bParas.get("place");
		lat = para.bParas.get("lat");
		lon = para.bParas.get("lon");
		System.out.println("lat:"+lat+"  lon:"+lon);
		
		if((lon == null || lon.length() == 0) || (lat == null || lat.length() == 0)){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		Double latitude = Double.parseDouble(lat);
		Double longitude = Double.parseDouble(lon);
		
		//获取分块的frame需要改成从数据库读取
		List<String> gpsFrameStr = new ArrayList<String>();
		//gpsFrameStr.add("116.354659,39.957965,116.361053,39.965134");
		gpsFrameStr.add("116.355584,39.960106;116.357312,39.960106;116.357312,39.960743;116.355584,39.960743;116.355584,39.960106");//jiaosan
		gpsFrameStr.add("116.355557,39.960689;116.357266,39.960689;116.357266,39.961687;116.355557,39.961687;116.355557,39.960689");//xiaohuayuan
		gpsFrameStr.add("116.355512,39.961674;116.357237,39.961674;116.357237,39.962275;116.355512,39.962275;116.355512,39.961674");//jiaosi
		gpsFrameStr.add("116.355488,39.962223;116.357196,39.962223;116.357196,39.963111;116.355488,39.963111;116.355488,39.962223");//xueyi xuesi
		
		List<String> pixFrameStr = new ArrayList<String>();
		//gpsFrameStr.add("116.354659,39.957965,116.361053,39.965134");
//		pixFrameStr.add("237,863;446,863;446,974;237,974;237,863");//jiaosan
//		pixFrameStr.add("237,735;446,735;446,863;237,863;237,735");//xiaohuayuan
//		pixFrameStr.add("237,603;446,737;446,737;237,603;237,603");//jiaosi
//		pixFrameStr.add("237,486;446,486;446,605;237,605;237,486");//xueyi xuesi
//		
		pixFrameStr.add("237,863;446,974");//jiaosan
		pixFrameStr.add("237,735;446,863");//xiaohuayuan
		pixFrameStr.add("237,603;446,737");//jiaosi
		pixFrameStr.add("237,486;446,605");//xueyi xuesi
		
		Double[] gpsFrame = null;
		Double[] pixFrame = null;
		try{
			int pixX = 0;
			int pixY = 0;
			for(int i=0; i<gpsFrameStr.size(); i++){
				System.out.println(gpsFrameStr.get(i));
				gpsFrame = getFrame(gpsFrameStr.get(i), gpsFrame);
				if(gpsFrame != null && gpsFrame.length > 0){
					if(inFrame(longitude, latitude, gpsFrame)){
						//pixFrame = new Double[2*gpsFrame.length];
						pixFrame = getFrame(pixFrameStr.get(i), pixFrame);
						pixX = lon2pix(longitude,gpsFrame[0], gpsFrame[2], pixFrame[0], pixFrame[2]);
						pixY = lat2pix(latitude,gpsFrame[3], gpsFrame[5], pixFrame[1], pixFrame[3]);
						System.out.println("pixX:"+pixX+"  pixY:"+pixY);
						map.put("pixelX", pixX);
						map.put("pixelY", pixY);
						map.put("success", true);
						resultObj = JSONObject.fromObject(map);
						return SUCCESS;
					}
				}
			}
			map.put("success", false);
			map.put("message", "不在frame中");
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			map.put("success", false);
			map.put("message", e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			gpsFrame = null; gpsFrameStr = null; pixFrame = null; pixFrameStr = null;
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(map != null){ map.clear(); map = null; }
			lock.writeLock().unlock();
		}
	}
	
	//经度和纬度的计算不一样
	/**
	 * 经度转换成所在图片的像素点x坐标
	 * @param lon Double 待转换经度值
	 * @param lon1 Double 地图对应的gps坐标框的其中之一，可以是左边的，也可以是右边的
	 * @param lon2 Double 地图对应的gps坐标框的其中之一，可以是左边的，也可以是右边的
	 * @param num1 Double 地图对应的像素坐标框的其中之一，可以是左边的，也可以是右边的
	 * @param num2 Double 地图对应的像素坐标框的其中之一，可以是左边的，也可以是右边的
	 * @return int 像素点x坐标
	 * */
	public int lon2pix(Double lon, Double lon1, Double lon2, Double num1, Double num2) {
		return (int)((double)((lon-Math.min(lon1, lon2))/(Math.abs(lon1-lon2)))*((double)(Math.abs(num1-num2))+Math.min(num1,num2)));
	}
	
	/**
	 * 纬度转换成所在图片的像素点y坐标
	 * @param lat Double 待转换经度值
	 * @param lat1 Double 地图对应的gps坐标框的其中之一，可以是上边的，也可以是下边的
	 * @param lat2 Double 地图对应的gps坐标框的其中之一，可以是上边的，也可以是下边的
	 * @param num1 Double 地图对应的像素坐标框的其中之一，可以是上边的，也可以是下边的
	 * @param num2 Double 地图对应的像素坐标框的其中之一，可以是上边的，也可以是下边的
	 * @return int 像素点x坐标
	 * */
	public int lat2pix(Double lat, Double lat1, Double lat2, Double num1, Double num2) {
		return (int)((double)((Math.max(lat1, lat2)-lat)/(Math.abs(lat1-lat2)))*((double)(Math.abs(num1-num2))+Math.min(num1,num2)));
	}
	
	/**
	 * 根据点组成的String获取点的Double数组，String格式为"x0,y0;x1,y1;x2,y2;x0,y0"
	 * @param frameStr String 点组成的String
	 * @param frame Double[] 点组成的Double数组
	 * @return Double[] 点组成的Double数组
	 * */
	public Double[] getFrame(String frameStr, Double[] frame){
		String[] temp = null;
		String[] pointStrs = null;
		try{
			pointStrs = frameStr.split(";");
			frame = new Double[2*pointStrs.length];
			for(int i=0; i<pointStrs.length; i++){
				System.out.println(pointStrs[i]);
				temp = pointStrs[i].split(",");
				frame[2*i] = Double.parseDouble(temp[0]);
				frame[2*i+1] = Double.parseDouble(temp[1]);
				System.out.println("x:"+frame[2*i]+"  y:"+frame[2*i+1]);
			}
			return frame;
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		finally{
			temp = null; pointStrs = null;
		}
		
	}
	
	/**
     * 水平/垂直交叉点数判别法（适用于任意多边形）
     * @param  x  double  待判断点的x坐标
     * @param  y  double  待判断点的y坐标
     * @param  frame  Double[]  unit的边框点，按顺时针或逆时针方向排列 
     * @return  boolean	是否在该unit内部
     * */
	public boolean inFrame(Double lon, Double lat, Double[] frame){
		return BoundsUtil.inPolygon(lon, lat, frame);
	}
	
	@After
    public void destory() {
		if(resultObj != null) { resultObj.clear(); resultObj = null; }
        System.gc();
    }
}
