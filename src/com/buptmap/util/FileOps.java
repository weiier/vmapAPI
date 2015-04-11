package com.buptmap.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileOps {
	/**  
     *  copy file
     *  @param  sourcePath  String  source path  
     *  @param  targetPath  String  target path  
     *  @return  boolean  
     */  
   public void copyFile(String sourcePath, String targetPath) {  
       try{  
    	   int bytesum = 0;  
           int  byteread = 0;  
           File sourceFile = new File(sourcePath);  
           if (sourceFile.exists()) {  //file exists
               InputStream inStream = new FileInputStream(sourcePath);  //read source file
               FileOutputStream fs = new FileOutputStream(targetPath);  
               byte[] buffer = new byte[1444];  
                
               while((byteread = inStream.read(buffer)) != -1) {  
                   bytesum += byteread;  //byte numbers
                   System.out.println(bytesum);  
                   fs.write(buffer, 0, byteread);  
               }  
               inStream.close();  
           }  
       }  
       catch(Exception e) {  
           System.out.println(e.toString());  
           e.printStackTrace();  
       } 
   }
   
   /**  
    *  copy picture
    *  @param  sourcePath  String  source picture
    *  @param  targetPath  String  target picture
    *  @return  boolean  
    */  
   public boolean copyImage(String sourcePath, String targetPath){  
	   LogUtil log = new LogUtil();
	   File sourceImage = new File(sourcePath); 
	   File targetImage = new File(targetPath);
	   FileInputStream fi = null;  
	   try {  
		   fi = new FileInputStream(sourceImage);  
	   } 
	   catch (FileNotFoundException e) {  
		   log.writeLog("", e.getMessage());
		   e.printStackTrace();  
		   return false;
	   }
	   
	   BufferedInputStream in=new BufferedInputStream(fi);  
	   FileOutputStream fo = null;  
	   try {  
		   fo = new FileOutputStream(targetImage);  
	   } 
	   catch (FileNotFoundException e) {
		   log.writeLog("", e.getMessage());
		   e.printStackTrace();  
		   return false;
	   } 
	   BufferedOutputStream out=new BufferedOutputStream(fo);  

	   byte[] buf=new byte[1024];  
	   int len;  
	   try {  
		   len = in.read(buf);  
		   while(len!=-1){  
			   out.write(buf, 0, len);  
			   len=in.read(buf);  
		   }  
		   out.close();  
		   fo.close();  
		   in.close();  
		   fi.close();  
		   return true;
	   }
	   catch (IOException e) {  
		   log.writeLog("", e.getMessage());
		   e.printStackTrace(); 
		   return false;
	   }  
   }
   
   /**  
    *  delete file folder and file or sub folder in it
    *  @param  targetPath  String  folder path
    *  @return  boolean  
    */  
   public boolean DeleteFiles(String targetPath){
	   try{
		   File file = new File(targetPath);
		   if(!file.isDirectory()){
			   return file.delete();
		   }
		   String[] fileList = file.list();
		   for(int i=0; i<fileList.length; i++){
			   File subFile = new File(targetPath + fileList[i]);
			   if(!subFile.isDirectory()){
				   subFile.delete();
			   }
			   else{
				   DeleteFiles(targetPath + fileList[i]);
			   }
		   }
		   return true;
	   }
	   catch (Exception e) {
		// TODO: handle exception
		   System.out.print(e.toString());
		   return false;
	   }
   }
   
   /**
	 * check whether file exists
	 * @param  filePath  String  file path
	 * @return  boolean
	 **/
   public boolean fileExists(String filePath){
	   if(new File(filePath).exists()) return true;
	   return false;
   }
}
