package com.buptmap.action;


import java.io.InputStream;  

import org.apache.struts2.ServletActionContext;  

import com.opensymphony.xwork2.ActionSupport;  

public class DownfileAction extends ActionSupport  {
	private String inputPath;
	public void setInputPath(String value)
	{
		inputPath=value;
	}
	public InputStream getTargetFile() 
    {  
		System.out.println(inputPath);
		
		InputStream inputStream = ServletActionContext.getServletContext().getResourceAsStream(inputPath);  
		if (inputStream == null) {
			System.out.println("sbsb");
		}
		
			return ServletActionContext.getServletContext().getResourceAsStream(inputPath);
		
		
    }  
      
    @Override  
    public String execute() throws Exception  
    {  
        return SUCCESS;  
    }  

}
