package com.buptmap.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * create and write log
 * @author Peter
 * */
public class LogUtil {

	/**
	 * write log if log file exists, create it and write else
	 * @param  logPath  String  log path
	 * @param  msg  String  content write to log
	 * */
	public void writeLog(String logPath, String msg){
		String logFilePathName=null;
		Calendar cd = null;
		
		try {
			cd = Calendar.getInstance();  //get log creating time
			int year=cd.get(Calendar.YEAR);
			String month=addZero(cd.get(Calendar.MONTH)+1);
			String day=addZero(cd.get(Calendar.DAY_OF_MONTH));
			String hour=addZero(cd.get(Calendar.HOUR_OF_DAY));
			String min=addZero(cd.get(Calendar.MINUTE));
			String sec=addZero(cd.get(Calendar.SECOND));
			
			File fileParentDir=new File(logPath);
			
			if (!fileParentDir.exists()) fileParentDir.mkdirs();  //create log folder if it is not exists
			logFilePathName=logPath+year+month+day+hour+".log";
			
			File log = new File(logFilePathName);
			if(!log.exists()) {  //create log if it is not exists
				log.createNewFile();
			}
			
			PrintWriter printWriter=new PrintWriter(new FileOutputStream(logFilePathName, true));  //write log		
			String time="["+year+"-"+month+"-"+day+" "+hour+":"+min+":"+sec+"] ";
			printWriter.println(time+msg);
			printWriter.flush();
			printWriter.close();
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(cd != null) { cd.clear(); cd = null; }
		}
	}
	
	/**
	 * add zero before single digital to keep format
	 * @param  i  int  number need to be handle
	 * @return  String
	 * */
	public String addZero(int i) {
		if (i<10) {
			String tmpString="0"+i;
			return tmpString;
		}
		else {
			return String.valueOf(i);
		}  
	}

}
