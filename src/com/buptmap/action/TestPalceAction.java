package com.buptmap.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.buptmap.Service.TestService;
/**
 * @author weiier
 * test data
 */
@Component("testPalceAction")
@Scope("prototype")
public class TestPalceAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private TestService testService;
	private JSONObject resultObj;
	/**
	 * return all unit
	 * @return SUCCESS
	 */
	public String all(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String,Object> map = new HashMap<String,Object>();
		String id = request.getParameter("unit_id");
		JSONArray array = new JSONArray();
		JSONArray frame = new JSONArray();
		array = testService.all(id,"Floor1");
		frame = testService.frame(id,"Floor1");
		if(array==null||array.size()==0){
			array = testService.all(id, "Floor0");
			frame = testService.frame(id,"Floor0");
		}
		map.put("success", true);
		map.put("rows", array);
		map.put("F", frame);
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	
	public String changeFloor(){
		HttpServletRequest request = ServletActionContext.getRequest();
		Map<String,Object> map = new HashMap<String,Object>();
		String id = request.getParameter("unit_id");
		String floor = request.getParameter("floor_id");
		JSONArray array = new JSONArray();
		JSONArray frame = new JSONArray();
		array = testService.all(id,floor);
		frame = testService.frame(id, floor);
		map.put("success", true);
		map.put("rows", array);
		map.put("F", frame);
		resultObj = JSONObject.fromObject(map);
		return SUCCESS;
	}
	public TestService getTestService() {
		return testService;
	}
	@Resource(name="testService")
	public void setTestService(TestService testService) {
		this.testService = testService;
	}


	public JSONObject getResultObj() {
		return resultObj;
	}

	public void setResultObj(JSONObject resultObj) {
		this.resultObj = resultObj;
	}
	
}
