package com.buptmap.action;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.buptmap.Service.MapService;
import com.buptmap.util.CoreBusiness;
import com.buptmap.util.FileOps;
import com.buptmap.util.LogUtil;

import com.opensymphony.xwork2.interceptor.annotations.After;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class MapAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5206600497635202076L;
	
	private JSONObject resultObj;
	public JSONObject getResultObj() {
		return resultObj;
	}
	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
	MapService mapService;
	public MapService getMapService() {
		return mapService;
	}
	@Resource(name="mapService")
	public void setMapService(MapService mapService) {
		this.mapService = mapService;
	}

	private File imageFile;
	private String imageUrl;
	private String jsoncallback;
	private HttpServletRequest request;
	private String size;
	private LogUtil log = new LogUtil();

	public String getJsonCallBack() {
		return jsoncallback;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public void setJsonCallBack(String jsoncallback) {
		if(jsoncallback.length() == 0) this.jsoncallback = null;
		this.jsoncallback = jsoncallback;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public File getImageFile() {
		return imageFile;
	}
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	private String place;
	private String floor;
	private String level;
	private String spot;
	private String style;

	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String GetMapPath(){

		return "";
	}
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Logger logger = Logger.getLogger(MapAction.class);
	
	/**
	 * 根据文件所在存储路径读取文件的字节流
	 * @param  filePath  String  文件存储路径 如：D:/image.svg
	 * @return  byte[]  文件字节流
	 * */
	public byte[] readerFileStream(String filePath)  
			throws IOException {  
		BufferedInputStream bis = null; 
		byte[] buff = null;
		try{
			File f = new File(filePath);
			if(f.exists()){
				int length = (int)f.length();
				buff = new byte[length]; 

				bis = new BufferedInputStream(new FileInputStream(filePath));
				int bytesRead;
				do{
					bytesRead = bis.read(buff, 0, buff.length);
				}while(-1 != bytesRead);
				logger.info("文件读取成功");
			}
			else {
				logger.info("请求文件不存在");
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		finally{
			if(bis != null) bis.close();
		}
		return buff;
	}  
	
	/**
	 * 根据svg图片的存储路径，获取其字节流，输出到web
	 * */
	public String showImage2() throws Exception {  
		imageUrl = basePicPath + "mappictures/D972C22F-0D6E-4557-BE13-BB279F82EE44/Floor0/BUPT_0.svg";
		System.out.println(logPath);
		
		String data = "";
		byte[] bis = readerFileStream(imageUrl);
        
        data = new String(bis, "utf-8");
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("success", true);
        m.put("data", data);
        resultObj = JSONObject.fromObject(m);
		return SUCCESS;  
	}  
	
	public String showImageTest() throws Exception {  
		
		if(place == null || place.length() == 0 || floor == null || floor.length() == 0 
				|| level == null || level.length() == 0){
			return ERROR;
		}
		HttpServletResponse response = ServletActionContext.getResponse(); 
		HttpServletRequest request = ServletActionContext.getRequest();

		String contextPath = request.getContextPath();

		String requestURI = request.getRequestURI();
		String action = requestURI.substring(requestURI.lastIndexOf('/')+1, requestURI.indexOf('!'));
		String method = requestURI.substring(requestURI.indexOf('!')+1);
		System.out.println("getRequestURI():"+request.getRequestURI()+" action:"+action+" method:"+method); //可以得到action和method
		System.out.println("getQueryString():"+request.getQueryString()); //可以得到各个参数


		Enumeration<String> queryPara = request.getParameterNames();
		for (;queryPara.hasMoreElements();) {
			String paraName = queryPara.nextElement();
			System.out.println(paraName);
			System.out.println(request.getParameterValues(paraName)[0]);
		}

		Map<String, String[]> queryParaMap = request.getParameterMap();
		for(int i=0;i<queryParaMap.size();i++){
			System.out.println(queryParaMap.keySet().toArray()[i]);
			//System.out.println(queryParaMap.values().toArray()[i]);
		}

		String svgImgUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + contextPath;
		svgImgUrl += "/map/"+ place +"/"+ floor +"/BUPT_"+ level +".svg";
		jsoncallback = request.getParameter("jsoncallback");
		System.out.println(svgImgUrl + " " + jsoncallback);

		if(jsoncallback == null) response.sendRedirect(svgImgUrl);
		else {
			System.out.println(jsoncallback + "("+svgImgUrl+")");
			response.sendRedirect(jsoncallback + "("+svgImgUrl+")");
		}
		return null;  
	} 
	
	/**
	 * 根据svg图片的存储路径，获取其字节流，输出到web
	 * */
	public String svg() throws Exception {  
		lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor") || !para.bParas.containsKey("style")){
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
		floor = para.bParas.get("floor");
		style = para.bParas.get("style").toLowerCase();
		
		if(place == null || place.length() == 0 || floor == null || floor.length() == 0 || style == null || style.length() == 0){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		byte[] bytes = null;
		try{
			int map_level = 0;
			String sourcePath = mapService.getMapPath(place, floor, map_level, style);
			if(sourcePath == null){
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
				return SUCCESS;
			}
			System.out.println(sourcePath);
			HttpServletResponse res = ServletActionContext.getResponse();
			res.sendRedirect("http://222.199.184.12/vmapres/"+sourcePath);
			
			//imageUrl = basePicPath + sourcePath;

//			File svgFile = new File(imageUrl);
//			if(!svgFile.exists()){
//				map.put("success", false);
//				map.put("message", ErrorMessage.FileNotExistError);
//				resultObj = JSONObject.fromObject(map);
//				return SUCCESS;
//			}
			
			
//			bytes = readerFileStream(imageUrl);
//			OutputStream outStream = res.getOutputStream();  
//
//			res.setContentLength(bytes.length);
//			res.setContentType("image/svg+xml;charset=UTF-8");
//
//			outStream.write(bytes, 0, bytes.length);  
//			outStream.flush(); 
//			outStream.close(); 

			return null; 
		}catch (Exception e) {
			logger.error(e.toString());
			map.put("success", false);
			map.put("message", e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			if(bytes != null) bytes = null;
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			lock.writeLock().unlock();
		} 
	}  
	
	public String search() throws Exception{
		lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor") || 
				!para.bParas.containsKey("spot")){
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
		floor = para.bParas.get("floor");
		spot = para.bParas.get("spot");
		
		if(place == null || place.length() == 0 || floor == null || floor.length() == 0 
				|| spot == null || spot.length() == 0){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		double[] coordinate = mapService.getCoordinate(spot, place);
		if(coordinate == null || coordinate.length < 2){
			map.put("success", false);
			map.put("message", ErrorMessage.NoResultError);
			logger.info(ErrorMessage.NoResultError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}

		level = para.bParas.get("level");
		int map_level = -1;
		if(level != null || level.length() != 0) map_level = Integer.parseInt(level);

		Map<String, String> calculateInfo = mapService.getCalculateInfo(place, floor, map_level);
		if(calculateInfo == null || calculateInfo.isEmpty()){
			map.put("success", false);
			map.put("message", ErrorMessage.NoResultError);
			logger.info(ErrorMessage.NoResultError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}

		String[] mn = null;
		int width = 0;
		int height = 0;
		size = null;
		if(para.bParas.containsKey("size")){
			size = para.bParas.get("size");
			if(size != null && !size.equals("")){
				mn = size.split("[*]");
				width = Integer.parseInt(mn[0]);
				height = Integer.parseInt(mn[1]);
			}
		}
		//System.out.println("当前spot的相对坐标: coord_x: " + coordinate[0] + "， coord_y:" + coordinate[1]);
		
		String mapPath = calculateInfo.get("path");
		double scale = Double.parseDouble(calculateInfo.get("scale"));
		double dpi = Double.parseDouble(calculateInfo.get("dpi"));
		//System.out.println("当前spot的scale,dpi:"+scale+", "+dpi);
		
		int pixelx = CoreBusiness.getPixByCoord(coordinate[0], dpi, scale);
		int pixely = CoreBusiness.getPixByCoord(coordinate[1], dpi, scale);
		//System.out.println("当前spot的像素点:" + pixelx+", "+pixely);
		//System.out.println("当前请求截图的宽度和高度: "+width+", "+height);

		imageUrl = basePicPath + mapPath;
		//System.out.println("当前请求图片的路径: "+imageUrl);
		
		File svgFile = new File(imageUrl);
		if(!svgFile.exists()){
			map.put("success", false);
			map.put("message", ErrorMessage.FileNotExistError);
			logger.info(ErrorMessage.FileNotExistError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		String imageType = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);  
		// 取得图片读入器
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(imageType);
		ImageReader reader = (ImageReader)readers.next();
		// 取得图片读入流
		HttpServletResponse response = ServletActionContext.getResponse(); 
		File file = null;
		InputStream is = null;
		ImageInputStream iis = null;
		try{
			// create File by file path
			file = new File(imageUrl);  
			is = new FileInputStream(file);  
			iis = ImageIO.createImageInputStream(is);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			BufferedImage srcImg = reader.read(0,param);
			//System.out.println("当前图片的像素大小x*y: "+(srcImg.getWidth()) + ", " + (srcImg.getHeight()));
			
			Rectangle rect = param.getSourceRegion();
			pixelx = (pixelx-width/2);
			pixelx = pixelx<0?0:pixelx;
			pixely = (pixely-height/2);
			pixely = pixely<0?0:pixely;
			
			int minusX = srcImg.getWidth()-pixelx;
			int minusY = srcImg.getHeight()-pixely;
			//System.out.println("当前可截图宽，高范围: "+minusX + ", " + minusY);
			width = width>minusX?minusX:width;
			height = height>minusY?minusY:height;
			
			//System.out.println("截图的原点像素点:" + pixelx+", "+pixely);
			//System.out.println("截图的宽度和高度: "+width+", "+height);
			
			if(size!=null&&!size.equals("")){
				rect = new Rectangle(pixelx, pixely, width, height);
			}
			param.setSourceRegion(rect);
			BufferedImage subImg = reader.read(0,param);
			//System.out.println("截图的像素大小x*y: "+(subImg.getWidth()) + ", " + (subImg.getHeight()));
			
			OutputStream out = response.getOutputStream();  
			ImageIO.write(subImg, imageType, out);  
			out.flush(); out.close(); 
			subImg.flush(); subImg = null;
			srcImg.flush(); srcImg = null;
			iis.flush(); iis.close();
			is.close();
			
			return null;
		}
		catch(Exception e){
			map.put("success", false);
			map.put("message", e.getMessage());
			resultObj = JSONObject.fromObject(map);
			e.printStackTrace();
			logger.error(e.toString());
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(calculateInfo != null) { calculateInfo.clear(); calculateInfo = null; }
			coordinate = null; mn = null;
			lock.writeLock().unlock();
		}
	}
	
	//在页面上显示图片(暂支持 png格式图片)
	public String around() throws Exception {  
		lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor") || 
				!para.bParas.containsKey("level")){
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
		floor = para.bParas.get("floor");
		level = para.bParas.get("level");
		
		if(place == null || place.length() == 0 || floor == null || floor.length() == 0 
				|| level == null || level.length() == 0){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		
		int map_level = Integer.parseInt(level);
		String sourcePath = mapService.getMapPath(place, floor, map_level, null);
		
		if(sourcePath == null){
			map.put("success", false);
			map.put("message", ErrorMessage.NoResultError);
			logger.info(ErrorMessage.NoResultError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		System.out.println(sourcePath);
		
		HttpServletResponse res = ServletActionContext.getResponse();
		res.sendRedirect("http://222.199.184.12:52242/"+sourcePath);
		
		//imageUrl = basePicPath + sourcePath;
		
		//HttpServletResponse response = ServletActionContext.getResponse(); 
		
		try{
			// create File by file path
//			File file = new File(imageUrl);
//			if(!file.exists()){
//				map.put("success", false);
//				map.put("message", ErrorMessage.FileNotExistError);
//				resultObj = JSONObject.fromObject(map);
//				return SUCCESS;
//			}
//			
//			InputStream is = new FileInputStream(file);  
//			Image image = ImageIO.read(is);// read image 
//			String imageType = imageUrl.substring(imageUrl.lastIndexOf(".") + 1);  
//			RenderedImage img = (RenderedImage) image;  
//			OutputStream out = response.getOutputStream();  
//			ImageIO.write(img, imageType, out);
//
//			out.flush(); out.close();
//			is.close(); is = null;
//			image.flush(); image = null;
//			img = null;

			return null;  
		}catch(Exception e){
			logger.error(e.toString());
			map.put("success", false); map.put("message", e.getMessage());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			if(map != null) { map.clear(); map = null; }
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			lock.writeLock().unlock();
		}
	}  
	
	//根据place refer,floor,level参数，获取该楼层图片的计算信息。
	public String detail() throws IOException{
		lock.writeLock().lock();
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<Boolean, String> validCheckResult = validCheck(request);
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(!para.bParas.containsKey("place") || !para.bParas.containsKey("floor")){
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
		floor = para.bParas.get("floor");
		level = para.bParas.get("level");
		if(place == null || floor == null || place.length() == 0 || floor.length()==0) {
			map.put("success", true);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		
		try{
			Integer map_level = -1; 
			if(level!=null) map_level=Integer.parseInt(level); 
			array = mapService.detail(place,floor,map_level);
			if(array != null){
				if(array.size() != 0){
					map.put("success", true);
					map.put("total", array.size());
					map.put("rows", array);
					logger.info("返回数据成功");
					resultObj = JSONObject.fromObject(map);
				}
				else {
					map.put("success", false);
					map.put("message", ErrorMessage.NoResultError);
					logger.info(ErrorMessage.NoResultError);
					resultObj = JSONObject.fromObject(map);
				}
			}
			else {
				map.put("success", false);
				map.put("message", ErrorMessage.NoResultError);
				logger.info(ErrorMessage.NoResultError);
				resultObj = JSONObject.fromObject(map);
			}
			return SUCCESS;
		}
		catch (Exception e) {
			System.out.println(e.toString());
			logger.error(e.toString());
			map.put("success", false);
			map.put("message", e.toString());
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		finally{
			request = null;
			if(validCheckResult != null) { validCheckResult.clear(); validCheckResult = null; }
			if(map != null){ map.clear(); map = null; }
			if(array != null) { array.clear(); array = null; }
			lock.writeLock().unlock();
		}
	}
	
	@After
    public void destory() {
		logger.info("start end......");
		if(mapService != null) mapService = null;
		if(resultObj != null) { resultObj.clear(); resultObj = null; }
        System.gc();
    }
}
