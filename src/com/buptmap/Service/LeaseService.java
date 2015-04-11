package com.buptmap.Service;

import javax.annotation.Resource;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;
import com.buptmap.DAO.LeaseDAO;
import com.buptmap.model.Lease;

@Component
public class LeaseService {
	private LeaseDAO leaseDAO;
	
	public boolean insert(Lease l){
		return this.leaseDAO.insert(l);
	}
	
	public boolean delete(int id){
		return this.leaseDAO.delete(id);
	}
	
	public JSONArray selectNewLease(){
		return this.leaseDAO.selectNew();
	}
	
	public JSONArray selectOldLease(){
		return this.leaseDAO.selectOld();
	}
	
	public JSONArray selectSelf(String company_id){
		return this.leaseDAO.selectCompany(company_id);
	}
	
	public JSONArray selectCompany(String unit_id,int version_id){
		return this.leaseDAO.selectByUnit(unit_id,version_id);
	}
	
	public JSONArray selectProxyNew(String proxy_id,String floor_id){
		return this.leaseDAO.selectProxyNewById(proxy_id, floor_id);
	}
	
	public JSONArray selectProxyOld(String proxy_id,String floor_id){
		return this.leaseDAO.selectProxyOldById(proxy_id, floor_id);
	}
	
	public JSONArray selectProxysNew(String floor_id){
		return this.leaseDAO.selectProxysNew(floor_id);
	}
	
	public JSONArray selectProxysOld(String floor_id){
		return this.leaseDAO.selectProxysOld(floor_id);
	}
	
	public JSONArray select(int id){
		return this.leaseDAO.select(id);
	}
	
	public boolean updateRent(int lease_id,String rent){
		return this.leaseDAO.editRent(lease_id, rent);
	}

	public boolean updateState(String lease_id,String mark){
		return this.leaseDAO.updateState(lease_id, mark);
	}
	public LeaseDAO getLeaseDAO() {
		return leaseDAO;
	}
	@Resource
	public void setLeaseDAO(LeaseDAO leaseDAO) {
		this.leaseDAO = leaseDAO;
	}
	
	
}
