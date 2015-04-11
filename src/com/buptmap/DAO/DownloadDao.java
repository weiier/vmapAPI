package com.buptmap.DAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import java.io.File;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.buptmap.model.Company;
import com.buptmap.model.Indoor;
import com.buptmap.model.IndoorChange;
import com.buptmap.model.UnitChange;
import com.buptmap.model.Proxy;

@Component("downloadDAO")

public class DownloadDao {
	
	private HibernateTemplate hibernateTemplate = null;
	private SessionFactory sessionFactory;
	private Object[] params;
	private JSONArray jsonArray = null;
	private JSONObject jsonObject = null;
	private String sql = null;
	
	@SuppressWarnings("unchecked")
	public boolean getUserData(){
		String[] title = {"账号", "姓名", "手机","注册日期","最后登录"};
		
		List<Object[]> result = new ArrayList<Object[]>();
		
		try{
			WritableWorkbook book = Workbook.createWorkbook(new File("C:\\Tomcat7.0\\webapps\\vmapAPI\\userData.xls"));
			WritableSheet sheet = book.createSheet("第一页", 0);
			//写入表格标题
			int i,k,j=0;
			for(i=0;i<title.length;i++){
				Label label = new Label(i,j,title[i]);
				sheet.addCell(label);
			}
			j++;
			
			//写入数据
			result = this.hibernateTemplate.find("select show_name,con_per,phone,registration_date,last_time from Company");
			if(result.size()>0){
				for(i=0;i<result.size();i++){
					
					Object[] unitObjects = result.get(i);
					for(k=0;k<unitObjects.length;k++){
						
						sheet.addCell(new Label(k, j, unitObjects[k].toString()));
					}
					j++;
				}
				
			}

			book.write();
			book.close();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	
	@SuppressWarnings("unchecked")
	public boolean getLeaseData(){
		String[] title = {"申请单号", "铺位号", "类型","长度","宽度","面积","单位租金","吨位","折扣率","申请人","联系方式","租期(月)","申请状态","专员帐号","申请时间"};
		
		List<Object[]> leases = new ArrayList<Object[]>();
		List<Object[]> companyList = new ArrayList<Object[]>();
		List<Object[]> resultUnit = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<Object[]> unitChanges = new ArrayList<Object[]>();
		List<Object[]> indoorChanges = new ArrayList<Object[]>();
		try{
			WritableWorkbook book = Workbook.createWorkbook(new File("C:\\Tomcat7.0\\webapps\\vmapAPI\\leaseData.xls"));
			WritableSheet sheet = book.createSheet("第一页", 0);
			sheet.setColumnView(1, 15);
			sheet.setColumnView(10, 15);
			sheet.setColumnView(12, 15);
			sheet.setColumnView(14, 20);
			//写入表格标题
			int i,j=0;
			for(i=0;i<title.length;i++){
				Label label = new Label(i,j,title[i]);
				sheet.addCell(label);
			}
			j++;
			
			//写入数据
			leases = this.hibernateTemplate.find("select id,company_id,unit_id,state,rent,last_modify_time,version_id from Lease order by last_modify_time desc");
			if(leases.size()>0){
						Object[] companyObj = null;
						Object[] resultObj = null; 
						Object[] leaseObj = null;
						for(i=0;i<leases.size();i++){
							leaseObj = leases.get(i);
							sheet.addCell(new Label(0,j,leaseObj[0].toString()));
							sheet.addCell(new Label(11,j,leaseObj[4].toString()));
							sheet.addCell(new Label(14,j,leaseObj[5].toString()));
							
							if(leaseObj[3].equals("0") || leaseObj[3].equals(0)){
								sheet.addCell(new Label(12,j,"未审核"));
							}else if(leaseObj[3].equals("1") || leaseObj[3].equals(1)){
								sheet.addCell(new Label(12,j,"同意出租"));
							}else if(leaseObj[3].equals("2") || leaseObj[3].equals(2)){
								sheet.addCell(new Label(12,j,"未同意出租"));
							}
							
							companyList = this.hibernateTemplate.find("select company_id,phone,con_per from Company where company_id = '"+leaseObj[1]+"'");
							if(companyList != null && companyList.size() > 0){
								companyObj = companyList.get(0);
								sheet.addCell(new Label(9,j,companyObj[2].toString()));
								sheet.addCell(new Label(10,j,companyObj[1].toString()));
							}else{
								System.out.println("company is empty");
								sheet.addCell(new Label(9,j,"-"));
								sheet.addCell(new Label(10,j,"-"));
							}	
							
		
							resultUnit = hibernateTemplate.find("select available,iavailable,booth_num,type,block_length,block_width,block_area," +
									"block_rent,block_tonnage,block_discount,proxy_id from Spot s where s.unit_id='"+leaseObj[2]+"'");
							if(resultUnit!=null&&resultUnit.size()>0){
								resultObj = resultUnit.get(0);		
								if(resultObj[0].equals(1) && resultObj[1].equals(1)){
									sheet.addCell(new Label(1, j, resultObj[2].toString()));
									sheet.addCell(new Label(2, j, resultObj[3].toString()));
									sheet.addCell(new Label(3, j, resultObj[4].toString()));
									sheet.addCell(new Label(4, j, resultObj[5].toString()));
									sheet.addCell(new Label(5, j, resultObj[6].toString()));
									sheet.addCell(new Label(6, j, resultObj[7].toString()));
									sheet.addCell(new Label(7, j, resultObj[8].toString()));
									sheet.addCell(new Label(8, j, resultObj[9].toString()));
									sheet.addCell(new Label(13, j, resultObj[10].toString()));
								}	else{
									Object[] versionObj = null;
									Object[] unitObj = null;
									Object[] indoorObj = null;
									
									version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[6]+"'");
									if(version!=null&&version.size()>0){
										versionObj = version.get(0);
										String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
										String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
																
										unitChanges = hibernateTemplate.find("select block_length,block_width,block_area,block_rent,block_tonnage,block_discount,proxy_id" +
												" from UnitChange u  where u.unit_id='"+leaseObj[2]+"' and u.create_time between '"+create+"' and '"+modify+"' and available!=3 order by u.create_time desc");
									
										indoorChanges = hibernateTemplate.find("select type,booth_num from IndoorChange i  where i.unit_id='"+leaseObj[2]+"' and i.create_time between '"
												+create+"' and '"+modify+"' and available!=3 order by i.create_time desc");
										
										//a=4 b=4
										if (resultObj[0].equals(4) && resultObj[1].equals(4)) {							
											if(unitChanges!=null&&unitChanges.size()>0){
												if (indoorChanges!=null&&indoorChanges.size()>0) {
													unitObj = unitChanges.get(0);		
													indoorObj = indoorChanges.get(0);
													
													sheet.addCell(new Label(1, j, indoorObj[1].toString()));
													sheet.addCell(new Label(2, j, indoorObj[0].toString()));
													sheet.addCell(new Label(3, j, unitObj[0].toString()));
													sheet.addCell(new Label(4, j, unitObj[1].toString()));
													sheet.addCell(new Label(5, j, unitObj[2].toString()));
													sheet.addCell(new Label(6, j, unitObj[3].toString()));
													sheet.addCell(new Label(7, j, unitObj[4].toString()));
													sheet.addCell(new Label(8, j, unitObj[5].toString()));
													sheet.addCell(new Label(13, j, unitObj[6].toString()));
												}else {
													unitObj = unitChanges.get(0);		
													sheet.addCell(new Label(1, j, resultObj[2].toString()));
													sheet.addCell(new Label(2, j, resultObj[3].toString()));
													sheet.addCell(new Label(3, j, unitObj[0].toString()));
													sheet.addCell(new Label(4, j, unitObj[1].toString()));
													sheet.addCell(new Label(5, j, unitObj[2].toString()));
													sheet.addCell(new Label(6, j, unitObj[3].toString()));
													sheet.addCell(new Label(7, j, unitObj[4].toString()));
													sheet.addCell(new Label(8, j, unitObj[5].toString()));
													sheet.addCell(new Label(13, j, unitObj[6].toString()));
												}
											}else {
												if (indoorChanges!=null&&indoorChanges.size()>0) {
													indoorObj = indoorChanges.get(0);
													
													sheet.addCell(new Label(1, j, indoorObj[1].toString()));
													sheet.addCell(new Label(2, j, indoorObj[0].toString()));
													sheet.addCell(new Label(3, j, resultObj[4].toString()));
													sheet.addCell(new Label(4, j, resultObj[5].toString()));
													sheet.addCell(new Label(5, j, resultObj[6].toString()));
													sheet.addCell(new Label(6, j, resultObj[7].toString()));
													sheet.addCell(new Label(7, j, resultObj[8].toString()));
													sheet.addCell(new Label(8, j, resultObj[9].toString()));
													sheet.addCell(new Label(13, j, resultObj[10].toString()));
												}else {
													sheet.addCell(new Label(1, j, resultObj[2].toString()));
													sheet.addCell(new Label(2, j, resultObj[3].toString()));
													sheet.addCell(new Label(3, j, resultObj[4].toString()));
													sheet.addCell(new Label(4, j, resultObj[5].toString()));
													sheet.addCell(new Label(5, j, resultObj[6].toString()));
													sheet.addCell(new Label(6, j, resultObj[7].toString()));
													sheet.addCell(new Label(7, j, resultObj[8].toString()));
													sheet.addCell(new Label(8, j, resultObj[9].toString()));
													sheet.addCell(new Label(13, j, resultObj[10].toString()));
												}
											}
										}
										
										if (resultObj[0].equals(1) && resultObj[1].equals(4)){
											if (indoorChanges!=null&&indoorChanges.size()>0) {
												indoorObj = indoorChanges.get(0);
												
												sheet.addCell(new Label(1, j, indoorObj[1].toString()));
												sheet.addCell(new Label(2, j, indoorObj[0].toString()));
												sheet.addCell(new Label(3, j, resultObj[4].toString()));
												sheet.addCell(new Label(4, j, resultObj[5].toString()));
												sheet.addCell(new Label(5, j, resultObj[6].toString()));
												sheet.addCell(new Label(6, j, resultObj[7].toString()));
												sheet.addCell(new Label(7, j, resultObj[8].toString()));
												sheet.addCell(new Label(8, j, resultObj[9].toString()));
												sheet.addCell(new Label(13, j, resultObj[10].toString()));
											}else {
												sheet.addCell(new Label(1, j, resultObj[2].toString()));
												sheet.addCell(new Label(2, j, resultObj[3].toString()));
												sheet.addCell(new Label(3, j, resultObj[4].toString()));
												sheet.addCell(new Label(4, j, resultObj[5].toString()));
												sheet.addCell(new Label(5, j, resultObj[6].toString()));
												sheet.addCell(new Label(6, j, resultObj[7].toString()));
												sheet.addCell(new Label(7, j, resultObj[8].toString()));
												sheet.addCell(new Label(8, j, resultObj[9].toString()));
												sheet.addCell(new Label(13, j, resultObj[10].toString()));
											}
										}
										
										if (resultObj[0].equals(4) && resultObj[1].equals(1)){
											if(unitChanges!=null&&unitChanges.size()>0){
												unitObj = unitChanges.get(0);		
												
												sheet.addCell(new Label(1, j, resultObj[2].toString()));
												sheet.addCell(new Label(2, j, resultObj[3].toString()));
												sheet.addCell(new Label(3, j, unitObj[0].toString()));
												sheet.addCell(new Label(4, j, unitObj[1].toString()));
												sheet.addCell(new Label(5, j, unitObj[2].toString()));
												sheet.addCell(new Label(6, j, unitObj[3].toString()));
												sheet.addCell(new Label(7, j, unitObj[4].toString()));
												sheet.addCell(new Label(8, j, unitObj[5].toString()));
												
												sheet.addCell(new Label(13, j, unitObj[6] == null? "" : unitObj[6].toString()));
											}
											else {
												sheet.addCell(new Label(1, j, resultObj[2].toString()));
												sheet.addCell(new Label(2, j, resultObj[3].toString()));
												sheet.addCell(new Label(3, j, resultObj[4].toString()));
												sheet.addCell(new Label(4, j, resultObj[5].toString()));
												sheet.addCell(new Label(5, j, resultObj[6].toString()));
												sheet.addCell(new Label(6, j, resultObj[7].toString()));
												sheet.addCell(new Label(7, j, resultObj[8].toString()));
												sheet.addCell(new Label(8, j, resultObj[9].toString()));
												sheet.addCell(new Label(13, j, resultObj[10].toString()));
											}
										}
									}
								}
							}else {
								version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where  version_id='"+leaseObj[6]+"'");
								
								if(version!=null&&version.size()>0){
									Object[] versionObj = version.get(0);
									String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
									String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
													
									List<Object[]> addunit = hibernateTemplate.find("select block_length,block_width,block_area,block_rent,block_tonnage,block_discount,proxy_id " +
											"from UnitChange u  where u.unit_id='"+leaseObj[2]+"' and u.last_modify_time between '"+create+"' and '"+modify+"' and u.available=5");
									
									if (addunit.size()!=0 && addunit != null ) {
											Object[] unitChange1 = addunit.get(0);
											List<Object[]> addindoor = hibernateTemplate.find("select type,booth_num from IndoorChange i  where i.unit_id='"+leaseObj[2]
													+"' and i.last_modify_time between '"+create+"' and '"+modify+"' and i.available=5");
											if (addindoor.size() != 0&&addindoor != null) {
												Object[] indoorChange1 = addindoor.get(0);

												sheet.addCell(new Label(1, j, indoorChange1[1].toString()));
												sheet.addCell(new Label(2, j, indoorChange1[0].toString()));
												sheet.addCell(new Label(3, j, unitChange1[0].toString()));
												sheet.addCell(new Label(4, j, unitChange1[1].toString()));
												sheet.addCell(new Label(5, j, unitChange1[2].toString()));
												sheet.addCell(new Label(6, j, unitChange1[3].toString()));
												sheet.addCell(new Label(7, j, unitChange1[4].toString()));
												sheet.addCell(new Label(8, j, unitChange1[5].toString()));
												sheet.addCell(new Label(13, j, unitChange1[6].toString()));
										}
									}
								}
							}
							j++;
						}
					}
				book.write();
				book.close();
				return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean getConsultData(){
		
		String[] title = {"商铺号", "客户姓名", "客户手机","咨询内容","专员应答"};
		
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> user_result = new ArrayList<Object[]>();
		List<Object[]> indoor_result = new ArrayList<Object[]>();
		List<Object[]> indoorChange_result = new ArrayList<Object[]>();
		
		try{
			WritableWorkbook book = Workbook.createWorkbook(new File("C:\\Tomcat7.0\\webapps\\vmapAPI\\consultData.xls"));
			WritableSheet sheet = book.createSheet("第一页", 0);
			
			//写入表格标题
			int col,row=0;//表格行和列
			for(col=0;col<title.length;col++){
				Label label = new Label(col,row,title[col]);
				sheet.addCell(label);
			}
			row++;//现在row=1表示第二行
			//查询apply表中的send_id，若为admin则直接获取相关信息写入表；若为特定unit_id则再进行相关查询分析
			result = this.hibernateTemplate.find("select create_id,content,reply,send_id from Apply");
			
			if(result.size()>0){
				for(int i=0;i<result.size();i++){
					
					Object[] unitObjects = result.get(i);
					if(unitObjects[3].toString().equals("admin") ){
						
						user_result = this.hibernateTemplate.find("select con_per,phone,company_id from Company where company_id='"+unitObjects[0].toString()+"'");
						if(user_result.size()>0){
							Object[] user_unitObjects = user_result.get(0);
							sheet.addCell(new Label(0,row,"-"));
							sheet.addCell(new Label(1,row,user_unitObjects[0].toString()));
							sheet.addCell(new Label(2,row,user_unitObjects[1].toString()));
							sheet.addCell(new Label(3,row,unitObjects[1].toString()));
							sheet.addCell(new Label(4,row,unitObjects[2].toString()));
							row++;
						}
					}
					else{//特定unit_id的资讯
						
						indoor_result = this.hibernateTemplate.find("select booth_num,available,unit_id from Indoor where unit_id='"+unitObjects[3].toString()+"'");
						
						if(indoor_result.size()>0){
							Object[] indoor_unitObjects = indoor_result.get(0);
							if(indoor_unitObjects[1].toString().equals("1")){//unit_id未修改，直接取booth_num为商铺号
								
								user_result = this.hibernateTemplate.find("select con_per,phone,company_id from Company where company_id='"+unitObjects[0].toString()+"'");
								if(user_result.size()>0){
									Object[] user_unitObjects = user_result.get(0);
									sheet.addCell(new Label(0,row,indoor_unitObjects[0].toString()));
									sheet.addCell(new Label(1,row,user_unitObjects[0].toString()));
									sheet.addCell(new Label(2,row,user_unitObjects[1].toString()));
									sheet.addCell(new Label(3,row,unitObjects[1].toString()));
									sheet.addCell(new Label(4,row,unitObjects[2].toString()));
									row++;
								}
							}
							else if(indoor_unitObjects[1].toString().equals("4")){//unit_id被修改，进一步去indoor_change表里确认商铺号
								indoorChange_result = this.hibernateTemplate.find("select booth_num,available,unit_id from IndoorChange where unit_id='"+unitObjects[3].toString()+"'");
								if(indoorChange_result.size()>0){
									Object[] indoorChange_unitObjects = indoorChange_result.get(0);
									if(indoorChange_unitObjects[1].toString().equals("4") || indoorChange_unitObjects[1].toString().equals("5")){
										
										user_result = this.hibernateTemplate.find("select con_per,phone,company_id from Company where company_id='"+unitObjects[0].toString()+"'");
										if(user_result.size()>0){
											Object[] user_unitObjects = user_result.get(0);
											sheet.addCell(new Label(0,row,indoorChange_unitObjects[0].toString()));
											sheet.addCell(new Label(1,row,user_unitObjects[0].toString()));
											sheet.addCell(new Label(2,row,user_unitObjects[1].toString()));
											sheet.addCell(new Label(3,row,unitObjects[1].toString()));
											sheet.addCell(new Label(4,row,unitObjects[2].toString()));
											row++;
										}
									}
								}
								else{
									user_result = this.hibernateTemplate.find("select con_per,phone,company_id from Company where company_id='"+unitObjects[0].toString()+"'");
									if(user_result.size()>0){
										//System.out.println("55555");
										Object[] user_unitObjects = user_result.get(0);
										sheet.addCell(new Label(0,row,indoor_unitObjects[0].toString()));
										sheet.addCell(new Label(1,row,user_unitObjects[0].toString()));
										sheet.addCell(new Label(2,row,user_unitObjects[1].toString()));
										sheet.addCell(new Label(3,row,unitObjects[1].toString()));
										sheet.addCell(new Label(4,row,unitObjects[2].toString()));
										row++;
									}
								}
							}
							
						}
						else{
							
							indoorChange_result = this.hibernateTemplate.find("select booth_num,available,unit_id from IndoorChange where unit_id='"+unitObjects[3].toString()+"'");
							if(indoorChange_result.size()>0){
								Object[] indoorChange_unitObjects = indoorChange_result.get(0);
								if(indoorChange_unitObjects[1].toString().equals("5")){
									user_result = this.hibernateTemplate.find("select con_per,phone,company_id from Company where company_id='"+unitObjects[0].toString()+"'");
									if(user_result.size()>0){
										Object[] user_unitObjects = user_result.get(0);
										sheet.addCell(new Label(0,row,indoorChange_unitObjects[0].toString()));
										sheet.addCell(new Label(1,row,user_unitObjects[0].toString()));
										sheet.addCell(new Label(2,row,user_unitObjects[1].toString()));
										sheet.addCell(new Label(3,row,unitObjects[1].toString()));
										sheet.addCell(new Label(4,row,unitObjects[2].toString()));
										row++;
									}
								}
								
							}
						}
					}
				}
			}
			
			sheet.setColumnView(0,16);
			sheet.setColumnView(1,11);
			sheet.setColumnView(2,13);
			sheet.setColumnView(3,50);
			sheet.setColumnView(4,50);
			
			book.write();
			book.close();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean getfile(String unitid, String versionid, String floorid){
		
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<UnitChange> unitChanges = new ArrayList<UnitChange>();
		List<IndoorChange> indoorChanges = new ArrayList<IndoorChange>();
		List<Proxy> proxyList = new ArrayList<Proxy>();
		List<Company> companyList = new ArrayList<Company>();
		Object[] versionObj = null;
		System.out.println(unitid);
		System.out.println(floorid);
		System.out.println(versionid);
		
		String title[]={"商铺号","面积","吨位","租金","折扣率","类型","状态","专员名","专员联系方式","承租者","承租者联系方式"};
   
        try { 
        	System.out.println("dao");
            //t.xls为要新建的文件名
        	int j = 0;  
            WritableWorkbook book= Workbook.createWorkbook(new File("C:\\Tomcat7.0\\webapps\\vmapAPI\\total.xls")); 
            //生成名为“第一页”的工作表，参数0表示这是第一页 
            WritableSheet sheet=book.createSheet("第一页",0); 
            //写入内容
            for(int i=0;i<11;i++)  
            {//title
            	System.out.println(title[i]);
                sheet.addCell(new Label(i,j,title[i])); //列行内容，从0计数
            }
           
            j++;
            System.out.println("开始处理");
            //unit=1&&indoor=1
            result = hibernateTemplate.find("select name,block_area,booth_num,block_state,block_tonnage,block_rent,block_discount,type from Spot s where s.parent_unit_id='"+unitid+"'and s.floor_id='"+floorid	
            		+ "' and s.available=1 and s.iavailable=1 and s.unit_type_id <=3000");
			if (result.size() > 0) {
				for(int i =0; i < result.size();i ++){
					Object[] unitObjects = result.get(i);
					sheet.addCell(new Label(0, j, unitObjects[2].toString()));
					sheet.addCell(new Label(1, j, unitObjects[1].toString()));
					sheet.addCell(new Label(2, j, unitObjects[4].toString()));
					sheet.addCell(new Label(3, j, unitObjects[5].toString()));
					sheet.addCell(new Label(4, j, unitObjects[6].toString()));
					sheet.addCell(new Label(5, j, unitObjects[7].toString()));
					sheet.addCell(new Label(6, j, "未出租"));
					sheet.addCell(new Label(7, j, ""));
					sheet.addCell(new Label(8, j, ""));
					sheet.addCell(new Label(9, j, ""));
					sheet.addCell(new Label(10, j, ""));
					j++;
				}
			}
			System.out.println("1,1");
			
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where unit_id='"+unitid+"' and version_id='"+versionid+"'");
			versionObj = version.get(0);
			String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
			String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
			//unit=4&&indoor=1
			result = hibernateTemplate.find("select unit_id,block_area,booth_num,block_state,block_tonnage,block_rent,block_discount,type from Spot s where s.parent_unit_id='"+unitid+"'and s.floor_id='"+floorid	
					+ "' and s.available=4 and s.iavailable=1 and s.unit_type_id <=3000");
			if (result.size() > 0 ) {
				for (int i = 0; i < result.size(); i++) {
					Object[] unitObjects = result.get(i);
					unitChanges = hibernateTemplate.find("from UnitChange u where u.unit_id='"+unitObjects[0]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
				
					if (unitChanges.size() != 0 && unitChanges != null) {
						UnitChange utemp = unitChanges.get(0);
						sheet.addCell(new Label(0, j, unitObjects[2].toString()));
						sheet.addCell(new Label(1, j, utemp.getBlock_area()));
						sheet.addCell(new Label(2, j, utemp.getBlock_tonnage()));
						sheet.addCell(new Label(3, j, utemp.getBlock_rent()));
						sheet.addCell(new Label(4, j, utemp.getBlock_discount()));
						sheet.addCell(new Label(5, j, unitObjects[7].toString()));
						sheet.addCell(new Label(6, j, "未出租"));
						sheet.addCell(new Label(9, j, ""));
						sheet.addCell(new Label(10, j, ""));
						if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
							proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
							if (proxyList.size() != 0 && proxyList != null) {
								Proxy proxy = proxyList.get(0);
								sheet.addCell(new Label(7, j, proxy.getProxy_name()));
								sheet.addCell(new Label(8, j, proxy.getPhone()));
							}
						}
						else {
							sheet.addCell(new Label(7, j, ""));
							sheet.addCell(new Label(8, j, ""));
						}
					
						if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
							companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
							if (companyList.size() != 0 && companyList != null) {
								Company company = companyList.get(0);
								sheet.addCell(new Label(6, j, "已出租"));
								sheet.addCell(new Label(9, j, company.getShow_name()));
								sheet.addCell(new Label(10, j, company.getPhone()));	
							}
						} 
					}
					else {
						sheet.addCell(new Label(0, j, unitObjects[2].toString()));
						sheet.addCell(new Label(1, j, unitObjects[1].toString()));
						sheet.addCell(new Label(2, j, unitObjects[4].toString()));
						sheet.addCell(new Label(3, j, unitObjects[5].toString()));
						sheet.addCell(new Label(4, j, unitObjects[6].toString()));
						sheet.addCell(new Label(5, j, unitObjects[7].toString()));
						sheet.addCell(new Label(6, j, "未出租"));
						sheet.addCell(new Label(7, j, ""));
						sheet.addCell(new Label(8, j, ""));
						sheet.addCell(new Label(9, j, ""));
						sheet.addCell(new Label(10, j, ""));
					}
					j++;
				}
			}
			System.out.println("4,1");
			
			result = hibernateTemplate.find("select unit_id,block_area,booth_num,block_state,block_tonnage,block_rent,block_discount,type from Spot s where s.parent_unit_id='"+unitid+"'and s.floor_id='"+floorid
					+ "' and s.available=1 and s.iavailable=4  and s.unit_type_id <=3000");
			if (result.size() > 0 ) {
				for (int i = 0; i < result.size(); i++) {
					Object[] unitObjects = result.get(i);
					indoorChanges = hibernateTemplate.find("from IndoorChange u where u.unit_id='"+unitObjects[0]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
				
					if (indoorChanges.size() != 0 && indoorChanges != null) {
						IndoorChange itemp = indoorChanges.get(0);
						sheet.addCell(new Label(0, j, itemp.getBooth_num()));
						sheet.addCell(new Label(1, j, unitObjects[1].toString()));
						sheet.addCell(new Label(2, j, unitObjects[4].toString()));
						sheet.addCell(new Label(3, j, unitObjects[5].toString()));
						sheet.addCell(new Label(4, j, unitObjects[6].toString()));
						sheet.addCell(new Label(5, j, unitObjects[7].toString()));
						sheet.addCell(new Label(6, j, "未出租"));
						sheet.addCell(new Label(7, j, ""));
						sheet.addCell(new Label(8, j, ""));
						sheet.addCell(new Label(9, j, ""));
						sheet.addCell(new Label(10, j, ""));
					}
					else {
						sheet.addCell(new Label(0, j, unitObjects[2].toString()));
						sheet.addCell(new Label(1, j, unitObjects[1].toString()));
						sheet.addCell(new Label(2, j, unitObjects[4].toString()));
						sheet.addCell(new Label(3, j, unitObjects[5].toString()));
						sheet.addCell(new Label(4, j, unitObjects[6].toString()));
						sheet.addCell(new Label(5, j, unitObjects[7].toString()));
						sheet.addCell(new Label(6, j, "未出租"));
						sheet.addCell(new Label(7, j, ""));
						sheet.addCell(new Label(8, j, ""));
						sheet.addCell(new Label(9, j, ""));
						sheet.addCell(new Label(10, j, ""));
					}
					j++;
				}
			}
			System.out.println("1,4");
			
			result = hibernateTemplate.find("select unit_id,block_area,booth_num,block_state,block_tonnage,block_rent,block_discount,type from Spot s where s.parent_unit_id='"+unitid+"'and s.floor_id='"+floorid
					+ "' and s.available=4 and s.iavailable=4  and s.unit_type_id <=3000");
			if (result.size() > 0 ) {
				for (int i = 0; i < result.size(); i++) {
					Object[] unitObjects = result.get(i);
					indoorChanges = hibernateTemplate.find("from IndoorChange u where u.unit_id='"+unitObjects[0]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
					unitChanges = hibernateTemplate.find("from UnitChange u where u.unit_id='"+unitObjects[0]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
					if (unitChanges.size() != 0 && unitChanges != null ) {
						UnitChange utemp = unitChanges.get(0);
						if (utemp.getAvailable() == 3) {
							continue;
						}
					}
					
					if (unitChanges.size() != 0 && unitChanges != null ) {
						if (indoorChanges.size() != 0 && indoorChanges != null) {

							UnitChange utemp = unitChanges.get(0);
							IndoorChange itemp = indoorChanges.get(0);
							sheet.addCell(new Label(0, j, itemp.getBooth_num()));
							sheet.addCell(new Label(1, j, utemp.getBlock_area()));
							sheet.addCell(new Label(2, j, utemp.getBlock_tonnage()));
							sheet.addCell(new Label(3, j, utemp.getBlock_rent()));
							sheet.addCell(new Label(4, j, utemp.getBlock_discount()));
							sheet.addCell(new Label(5, j, itemp.getType()));
							sheet.addCell(new Label(6, j, "未出租"));
							sheet.addCell(new Label(9, j, ""));
							sheet.addCell(new Label(10, j, ""));
							if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
								proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
								if (proxyList.size() != 0 && proxyList != null) {
									Proxy proxy = proxyList.get(0);
									sheet.addCell(new Label(7, j, proxy.getProxy_name()));
									sheet.addCell(new Label(8, j, proxy.getPhone()));
								}
							}
							else {
								sheet.addCell(new Label(7, j, ""));
								sheet.addCell(new Label(8, j, ""));
							}
							
							if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
								
								companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
								if (companyList.size() != 0 && companyList != null) {
									Company company = companyList.get(0);
									sheet.addCell(new Label(6, j, "已出租"));
									sheet.addCell(new Label(9, j, company.getShow_name()));
									sheet.addCell(new Label(10, j, company.getPhone()));	
								}
							} 
						}
						else {

							UnitChange utemp = unitChanges.get(0);
							sheet.addCell(new Label(0, j, unitObjects[2].toString()));
							sheet.addCell(new Label(1, j, utemp.getBlock_area()));
							sheet.addCell(new Label(2, j, utemp.getBlock_tonnage()));
							sheet.addCell(new Label(3, j, utemp.getBlock_rent()));
							sheet.addCell(new Label(4, j, utemp.getBlock_discount()));
							sheet.addCell(new Label(5, j, unitObjects[7].toString()));
							
							if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
								proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
								if (proxyList.size() != 0 && proxyList != null) {
									Proxy proxy = proxyList.get(0);
									sheet.addCell(new Label(7, j, proxy.getProxy_name()));
									sheet.addCell(new Label(8, j, proxy.getPhone()));
								}
							}
							else {
								sheet.addCell(new Label(7, j, ""));
								sheet.addCell(new Label(8, j, ""));
							}
							
							if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
								companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
								if (companyList.size() != 0 && companyList != null) {
									Company company = companyList.get(0);
									sheet.addCell(new Label(6, j, "出租"));
									sheet.addCell(new Label(9, j, company.getShow_name()));
									sheet.addCell(new Label(10, j, company.getPhone()));	
								}
							} 
						}
					}
					else {
						if (indoorChanges.size() != 0 && indoorChanges != null) {
							IndoorChange itemp = indoorChanges.get(0);
							sheet.addCell(new Label(0, j, itemp.getBooth_num()));
							sheet.addCell(new Label(1, j, unitObjects[1].toString()));
							sheet.addCell(new Label(2, j, unitObjects[4].toString()));
							sheet.addCell(new Label(3, j, unitObjects[5].toString()));
							sheet.addCell(new Label(4, j, unitObjects[6].toString()));
							sheet.addCell(new Label(5, j, itemp.getType()));
							sheet.addCell(new Label(6, j, "未出租"));
							sheet.addCell(new Label(7, j, ""));
							sheet.addCell(new Label(8, j, ""));
							sheet.addCell(new Label(9, j, ""));
							sheet.addCell(new Label(10, j, ""));
						}
						else {
							sheet.addCell(new Label(0, j, unitObjects[2].toString()));
							sheet.addCell(new Label(1, j, unitObjects[1].toString()));
							sheet.addCell(new Label(2, j, unitObjects[4].toString()));
							sheet.addCell(new Label(3, j, unitObjects[5].toString()));
							sheet.addCell(new Label(4, j, unitObjects[6].toString()));
							sheet.addCell(new Label(5, j, unitObjects[7].toString()));
							sheet.addCell(new Label(6, j, "未出租"));
							sheet.addCell(new Label(7, j, ""));
							sheet.addCell(new Label(8, j, ""));
							sheet.addCell(new Label(9, j, ""));
							sheet.addCell(new Label(10, j, ""));
						}
					}
					j++;
				}
			}
			
			System.out.println("4,4");
			
			unitChanges = hibernateTemplate.find("from UnitChange u  where u.parent_unit_id='"+unitid+"' and u.last_modify_time between '"+create+"' and '"+modify
					+"' and u.available=5 and u.unit_type_id <= 3000");
			
			if (unitChanges != null && unitChanges.size() != 0) {
				for(int i =0; i < unitChanges.size(); i++,j++){
					UnitChange utemp = unitChanges.get(i);
					indoorChanges = hibernateTemplate.find("from IndoorChange u where u.unit_id='"+utemp.getUnit_id()+"'");
					if (indoorChanges.size() != 0 && indoorChanges != null) {
						IndoorChange itemp = indoorChanges.get(0);
						
						sheet.addCell(new Label(0, j, itemp.getBooth_num()));
						sheet.addCell(new Label(1, j, utemp.getBlock_area()));
						sheet.addCell(new Label(2, j, utemp.getBlock_tonnage()));
						sheet.addCell(new Label(3, j, utemp.getBlock_rent()));
						sheet.addCell(new Label(4, j, utemp.getBlock_discount()));
						sheet.addCell(new Label(5, j, itemp.getType()));
						sheet.addCell(new Label(6, j, "未出租"));
						sheet.addCell(new Label(9, j, ""));
						sheet.addCell(new Label(10, j, ""));
						
						if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
							proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
							if (proxyList.size() != 0 && proxyList != null) {
								Proxy proxy = proxyList.get(0);
								sheet.addCell(new Label(7, j, proxy.getProxy_name()));
								sheet.addCell(new Label(8, j, proxy.getPhone()));
							}
						}
						else {
							sheet.addCell(new Label(7, j, ""));
							sheet.addCell(new Label(8, j, ""));
						}
						
				
						if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
							companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
							if (companyList.size() != 0 && companyList != null) {
								Company company = companyList.get(0);
								sheet.addCell(new Label(6, j, "出租"));
								sheet.addCell(new Label(9, j, company.getShow_name()));
								sheet.addCell(new Label(10, j, company.getPhone()));	
							}
						} 
					}
				}
			}
			System.out.println("5,5");
            
            //写入数据
            book.write(); 
            //关闭文件
            book.close(); 
            return true;
        }
        catch(Exception e) {
        	return false;
        } 
	}
	
	
	@SuppressWarnings("unchecked")
   public boolean proxygetfile(String unitid, String versionid, String floorid,String proxyid){
		
		List<Object[]> result = new ArrayList<Object[]>();
		List<Object[]> version = new ArrayList<Object[]>();
		List<UnitChange> unitChanges = new ArrayList<UnitChange>();
		List<IndoorChange> indoorChanges = new ArrayList<IndoorChange>();
		List<Indoor> indoors = new ArrayList<Indoor>();
		List<Proxy> proxyList = new ArrayList<Proxy>();
		List<Company> companyList = new ArrayList<Company>();
		Object[] versionObj = null;
		System.out.println(unitid);
		System.out.println(floorid);
		System.out.println(versionid);
		
		String title[]={"展位号","面积","代理方名字","代理方联系方式","展位状态","参展方中文名","参展方英名"};
   
        try { 
        	System.out.println("dao");
            //t.xls为要新建的文件名
        	int j = 0;
            WritableWorkbook book= Workbook.createWorkbook(new File("D:\\apache-tomcat-7.0.56-windows-x64\\apache-tomcat-7.0.56\\webapps\\vmap\\"+ proxyid +".xls")); 
            //生成名为“第一页”的工作表，参数0表示这是第一页 
            WritableSheet sheet=book.createSheet("第一页",0); 
            String nameString = "展位";
            //写入内容
            for(int i=0;i<7;i++)  
            {//title
            	System.out.println(title[i]);
                sheet.addCell(new Label(i,j,title[i])); //列行内容，从0计数
            }
           
            j++;
            System.out.println("开始处理");
            //unit=1&&indoor=1
           
			version = hibernateTemplate.find("select createTime,lastModifyTime from Version  where unit_id='"+unitid+"' and version_id='"+versionid+"'");
			versionObj = version.get(0);
			String create = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[0]);
			String modify = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(versionObj[1]);
			
			//unit=4&&indoor=1
			result = hibernateTemplate.find("select name,block_area,booth_num,block_state,unit_id from Spot s where s.parent_unit_id='"+unitid+"'and s.floor_id='"+floorid	+ "'and s.name='"+ nameString + "' and s.available=4 ");
			if (result.size() > 0 ) {
				List<UnitChange> utempChanges = new ArrayList<UnitChange>();
				for (int i = 0; i < result.size(); i++) {
					Object[] unitObjects = result.get(i);
					unitChanges = hibernateTemplate.find("from UnitChange u where u.unit_id='"+unitObjects[4]+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
					if (unitChanges != null && unitChanges.size() != 0) {
						UnitChange utemp = unitChanges.get(0);
						if (utemp.getProxy_id() != null && !utemp.getProxy_id().equals("") && utemp.getProxy_id().equals(proxyid)) {
							utempChanges.add(utemp);
						}
					}
				}
				System.out.println(utempChanges.size());
				if(utempChanges != null && utempChanges.size() > 0){
					for (int i = 0; i < utempChanges.size(); i++,j++) {
						UnitChange utemp = utempChanges.get(i);
						if (utemp.getAvailable() == 3) {
							continue;
						}
						indoorChanges = hibernateTemplate.find("from IndoorChange u where u.unit_id='"+utemp.getUnit_id()+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
						if (indoorChanges.size() != 0 && indoorChanges != null) {
							IndoorChange itemp = indoorChanges.get(0);
							sheet.addCell(new Label(0, j, itemp.getBooth_num()));
							sheet.addCell(new Label(1, j, utemp.getBlock_area()));
							sheet.addCell(new Label(4, j, "未出售"));
							sheet.addCell(new Label(5, j, ""));
							sheet.addCell(new Label(6, j, ""));
							if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
								proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
								if (proxyList.size() != 0 && proxyList != null) {
									Proxy proxy = proxyList.get(0);
									sheet.addCell(new Label(2, j, proxy.getProxy_name()));
									sheet.addCell(new Label(3, j, proxy.getPhone()));
								}
							}
							else {
								sheet.addCell(new Label(2, j, ""));
								sheet.addCell(new Label(3, j, ""));
							}
							if(utemp.getRese_company_id() != null && utemp.getRese_company_id() != "") {
								companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getRese_company_id() + "'");
								if (companyList.size() != 0 && companyList != null) {
									Company company = companyList.get(0);
									sheet.addCell(new Label(4, j, "预留"));
									sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
									sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
								}
							}
							if (utemp.getSign_company_id() != null && utemp.getSign_company_id() != "") {
								companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getSign_company_id() + "'");
								if (companyList.size() != 0 && companyList != null) {
									Company company = companyList.get(0);
									sheet.addCell(new Label(4, j, "签约"));
									sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
									sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
								}
							}
							if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
								companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
								if (companyList.size() != 0 && companyList != null) {
									Company company = companyList.get(0);
									sheet.addCell(new Label(4, j, "出售"));
									sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
									sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
								}
							} 
						}
						else {
							indoors = hibernateTemplate.find("from Indoor u where u.unit_id='"+utemp.getUnit_id()+"' and u.last_modify_time between '"+create+"' and '"+modify+"'  order by u.create_time desc");
							if (indoors != null && indoors.size() != 0) {
								Indoor itmp = indoors.get(0);
								
								sheet.addCell(new Label(0, j, itmp.getBooth_num()));
								sheet.addCell(new Label(1, j, utemp.getBlock_area()));
								sheet.addCell(new Label(4, j, "未出售"));
								sheet.addCell(new Label(5, j, ""));
								sheet.addCell(new Label(6, j, ""));
								if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
									proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
									if (proxyList.size() != 0 && proxyList != null) {
										Proxy proxy = proxyList.get(0);
										sheet.addCell(new Label(2, j, proxy.getProxy_name()));
										sheet.addCell(new Label(3, j, proxy.getPhone()));
									}
								}
								else {
									sheet.addCell(new Label(2, j, ""));
									sheet.addCell(new Label(3, j, ""));
								}
								if(utemp.getRese_company_id() != null && utemp.getRese_company_id() != "") {
									
									companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getRese_company_id() + "'");
									if (companyList.size() != 0 && companyList != null) {
										Company company = companyList.get(0);
										sheet.addCell(new Label(4, j, "预留"));
										sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
										sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
									}
								}
								if (utemp.getSign_company_id() != null && utemp.getSign_company_id() != "") {
									companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getSign_company_id() + "'");
									if (companyList.size() != 0 && companyList != null) {
										Company company = companyList.get(0);
										sheet.addCell(new Label(4, j, "签约"));
										sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
										sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
									}
								}
								if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
									
									companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
									if (companyList.size() != 0 && companyList != null) {
										Company company = companyList.get(0);
										sheet.addCell(new Label(4, j, "出售"));
										sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
										sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
									}
								} 
							}
						}
					}
				}
			}
			unitChanges = hibernateTemplate.find("from UnitChange u  where u.parent_unit_id='"+unitid+"' and u.last_modify_time between '"+create+"' and '"+modify+"' and u.available=5 and u.name='"+ nameString	+"' and u.proxy_id='" + proxyid + "'");
			if (unitChanges != null && unitChanges.size() != 0) {
				for(int i =0; i < unitChanges.size(); i++){
					UnitChange utemp = unitChanges.get(i);
					indoorChanges = hibernateTemplate.find("from IndoorChange u where u.unit_id='"+utemp.getUnit_id()+"' and u.floor_id='"+ floorid + "'" );
					if (indoorChanges.size() != 0 && indoorChanges != null) {
						IndoorChange itemp = indoorChanges.get(0);
						
						sheet.addCell(new Label(0, j, itemp.getBooth_num()));
						sheet.addCell(new Label(1, j, utemp.getBlock_area()));
						sheet.addCell(new Label(4, j, "未出售"));
						sheet.addCell(new Label(5, j, ""));
						sheet.addCell(new Label(6, j, ""));
						
						if (utemp.getProxy_id() != null && utemp.getProxy_id() != "") {
							proxyList = hibernateTemplate.find("from Proxy where proxy_id='" + utemp.getProxy_id() + "'");
							if (proxyList.size() != 0 && proxyList != null) {
								Proxy proxy = proxyList.get(0);
								sheet.addCell(new Label(2, j, proxy.getProxy_name()));
								sheet.addCell(new Label(3, j, proxy.getPhone()));
							}
						}
						else {
							sheet.addCell(new Label(2, j, ""));
							sheet.addCell(new Label(3, j, ""));
						}
						
						if (utemp.getSign_company_id() != null && utemp.getSign_company_id() != "") {
							companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getSign_company_id() + "'");
							if (companyList.size() != 0 && companyList != null) {
								Company company = companyList.get(0);
								sheet.addCell(new Label(4, j, "签约"));
								sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
								sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
							}
							
						}
				
						if (utemp.getPay_company_id() != null && utemp.getPay_company_id() != "") {
							
							companyList = hibernateTemplate.find("from Company where company_id='" + utemp.getPay_company_id() + "'");
							if (companyList.size() != 0 && companyList != null) {
								Company company = companyList.get(0);
								sheet.addCell(new Label(4, j, "出售"));
								sheet.addCell(new Label(5, j, company.getCompany_name_ch()));
								sheet.addCell(new Label(6, j, company.getCompany_name_en()));	
							}
							
						} 
					   
						
						j ++;
					}
				}
			}
			System.out.println("3,3");
            /* for(int i=0;i<4;i++)    //context
            {
                for(int j=0;j<3;j++)
                {
                    sheet.addCell(new Label(j+1,i+1,context[i][j])); 
                }
            }
            sheet.addCell(new Label(0,1,"教师"));
            sheet.addCell(new Label(0,3,"助教"));
            */
            /*合并单元格.合并既可以是横向的，也可以是纵向的
             *WritableSheet.mergeCells(int m,int n,int p,int q);   表示由(m,n)到(p,q)的单元格组成的矩形区域合并
             * */
          //  sheet.mergeCells(0,1,0,2);
           // sheet.mergeCells(0,3,0,4);
            
            //写入数据
            book.write(); 
            //关闭文件
            book.close(); 
            return true;
        }
        catch(Exception e) {
        	return false;
        } 
	}
	
   public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name="hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
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
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
}
