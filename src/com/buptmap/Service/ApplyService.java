package com.buptmap.Service;

import javax.annotation.Resource;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;
import com.buptmap.DAO.ApplyDAO;
import com.buptmap.model.Apply;

@Component("applyService")
public class ApplyService {
	
	private ApplyDAO applyDAO;

	public ApplyDAO getApplyDAO() {
		return applyDAO;
	}
	@Resource(name="applyDAO")
	public void setApplyDAO(ApplyDAO applyDAO) {
		this.applyDAO = applyDAO;
	}
	
	public boolean edit(Apply apply){
		return applyDAO.edit(apply);
	}
	
	public boolean insert(Apply apply){
		return applyDAO.Insert(apply);
	}
	
	public boolean delete(int id){
		return applyDAO.delete(id);
	}
	public boolean updatesign(int id){
		return applyDAO.updatesign(id);
	}
	
	public JSONArray select(String id, int mode){
		return applyDAO.Select(id,mode);
	}
	
	public JSONArray adminselectnew(){
		return applyDAO.AdminSelect_new();
	}
	public JSONArray adminselectold(){
		return applyDAO.AdminSelect_old();
	}
	public JSONArray adminselectinform(){
		return applyDAO.AdminSelect_inform();
	}
	
	public JSONArray proxyselectself(String id){
		return applyDAO.ProxySelect_self(id);
	}
	public JSONArray proxyselectadmin(String id){
		return applyDAO.ProxySelect_admin(id);
	}
	public JSONArray find(int id){
		return applyDAO.find(id);
	}
	
	public Apply selectById(int id){
		return applyDAO.selectById(id);
	}
	/**
	 * 根据proxy_id和floor_id返回申请，部分权限
	 */

	public JSONArray applyNew(String proxy_id,String floor_id){
		return this.applyDAO.proxySelect_new(proxy_id, floor_id);
	}
	
	public JSONArray applyOld(String proxy_id,String floor_id){
		return this.applyDAO.proxySelect_old(proxy_id, floor_id);
	}
	
	public JSONArray applyAllnew(String floor_id){
		return this.applyDAO.allSelect_new(floor_id);
	}
	
	public JSONArray applyAllold(String floor_id){
		return this.applyDAO.allSelect_old(floor_id);
	}
	
	public JSONArray commonNew(){
		return this.applyDAO.commonSelectNew();
	}
	
	public JSONArray commonOld(){
		return this.applyDAO.commonSelectOld();
	}
}
