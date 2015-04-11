package com.buptmap.Service;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.buptmap.DAO.DownloadDao;
@Component("downloadService")

public class DownloadService {

	private DownloadDao downloadDao ;

	public DownloadDao getDownloadDao() {
		return downloadDao;
	}
	@Resource(name="downloadDAO")
	public void setDownloadDao(DownloadDao downloadDao) {
		this.downloadDao = downloadDao;
	}
	
	public boolean getfile(String unit_id, String version, String floor)
	{
		return downloadDao.getfile(unit_id, version, floor);
	}
	
	public boolean getUserData(){
		return this.downloadDao.getUserData();
	}
	
	public boolean getLeaseData(){
		return this.downloadDao.getLeaseData();
	}
	
	public boolean getConsultData(){
		return this.downloadDao.getConsultData();
	}
	
	public boolean targetfile(String unit_id, String version, String floor,String proxy)
	{
		return downloadDao.proxygetfile(unit_id, version, floor, proxy);
	}
}
