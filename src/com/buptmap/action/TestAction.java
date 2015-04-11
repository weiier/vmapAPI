package com.buptmap.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.buptmap.util.Base64;
import com.buptmap.util.ProcessProperties;
import com.opensymphony.xwork2.Action;
import net.sf.json.JSONObject;

@Scope("prototype")
public class TestAction extends BaseAction {
	private static final Logger logger = Logger.getLogger(TestAction.class);
	private JSONObject resultObj;
	
	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	private ProcessProperties props = new ProcessProperties();
	
	public String jsonTest() throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			Base64 base = new Base64();
			String picPath = props.getValue("svgPath");
			try {
			map.put("success", true);
			map.put("data","data:image/svg+xml;base64,"+base.encode(picPath,""));
			//System.out.println(data);
			resultObj = JSONObject.fromObject(map);
			} catch (IOException ie) {
				resultObj = JSONObject.fromObject("{success:false,message:'place、floor不存在或者有误'}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = JSONObject.fromObject("{success:false,message:'"+e.getMessage()+"'}");
		} finally {
			map.clear();
		}
		return SUCCESS;
	}
	
	public String json() throws IOException {
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
		
		String place = para.bParas.get("place");
		String floor = para.bParas.get("floor");
		
		if(place == null || place.length() == 0 || floor == null || floor.length() == 0){
			map.put("success", false);
			map.put("message", ErrorMessage.ParametersValueError);
			logger.info(ErrorMessage.ParametersValueError);
			resultObj = JSONObject.fromObject(map);
			return SUCCESS;
		}
		try {
			Base64 base = new Base64();
			String picPath = props.getValue("basePath")+place.toLowerCase()+"/"+floor+"/svg_0.svg";
			try {
			map.put("success", true);
			map.put("data","data:image/svg+xml;base64,"+base.encode(picPath,""));
			//System.out.println(data);
			resultObj = JSONObject.fromObject(map);
			} catch (IOException ie) {
				ie.printStackTrace();
				resultObj = JSONObject.fromObject("{success:false,message:'place、floor不存在或者有误'}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = JSONObject.fromObject("{success:false,message:'"+e.getMessage()+"'}");
		} finally {
			map.clear();
		}
		return SUCCESS;
	}
	
	
	public String test() throws IOException {
		String path = "C:/zzc/svg.txt";
		//Map<String, Object> map = new HashMap<String, Object>();
		HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/plain");
        PrintWriter pw = response.getWriter();
		try {
			File f = new File(path);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String data = "";
			while ((line = br.readLine()) != null) {
				data += line;
			}
			
			//map.put("success", true);
			//map.put("data",data);
			//System.out.println(data);
			//resultObj = data;
			pw.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			pw.flush();
			pw.close();
		}
		return null;
	}
}
