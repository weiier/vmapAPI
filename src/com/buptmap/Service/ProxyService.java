package com.buptmap.Service;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.ProxyDAO;
import com.buptmap.model.Proxy;
import com.opensymphony.xwork2.interceptor.annotations.After;

@Component("proxyService")
public class ProxyService {
	private ProxyDAO proxyDAO;

	public ProxyDAO getProxyDAO() {
		return proxyDAO;
	}

	@Resource(name="proxyDAO")
	public void setProxyDAO(ProxyDAO proxyDAO) {
		this.proxyDAO = proxyDAO;
	}
	
	public JSONArray Select(String proxy_id){
		return proxyDAO.Select(proxy_id);
	}
	
	public JSONArray all(){
		return proxyDAO.All();
	}
	
	public boolean Insert(Proxy proxy){
		System.out.println(proxy.getAddress());
		return proxyDAO.Insert(proxy);
	}
	
	public boolean edit(Proxy proxy){
		System.out.println(proxy.getAddress());
		return proxyDAO.edit(proxy);
	}

	public boolean updateState(String proxy_id,int state){
		return proxyDAO.updateState(proxy_id, state);
	}
	
	public boolean delete(String proxy_id){
		return proxyDAO.delete(proxy_id);
	}
}
