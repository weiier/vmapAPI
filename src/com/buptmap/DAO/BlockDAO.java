package com.buptmap.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.buptmap.model.Block;

@Component("blockDAO")
public class BlockDAO {
	
	private String sql = null;
	private Object[] params;
	private JSONArray jsonArray = null;
    private JSONObject jsonObject = null;
	
	private HibernateTemplate hibernateTemplate = null;
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
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

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray Proxy_Select(String proxy_id){
		jsonArray = new JSONArray();

		List<Object[]> resultList =  new ArrayList<Object[]>();
		
		sql = "select block_id,unit_id,version,block_length,block_width from Block"+
				 	" where proxy_id = ?";
		
		return null;
	}

}
