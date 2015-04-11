package com.buptmap.Service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.buptmap.DAO.ProvinceDAO;
import com.buptmap.model.Province;

@Component("provinceService")
public class ProvinceService {
	
	private ProvinceDAO provinceDAO;
	
	public ProvinceDAO getProvinceDAO() {
		return provinceDAO;
	}
	@Resource(name="provinceDAO")
	public void setProvinceDAO(ProvinceDAO provinceDAO) {
		this.provinceDAO = provinceDAO;
	}
	
	public List<Province> Search(String unitName){
		return provinceDAO.Select(unitName);
	}
	
	public boolean AddItem(Province province){
		return provinceDAO.Insert(province);
	}
}
