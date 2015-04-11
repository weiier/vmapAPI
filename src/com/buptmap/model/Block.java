package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_block")
public class Block {
	private String block_id;
	private String unit_id;
	private String version;
	private double block_length;
	private double block_width;
	private double block_area;
	private int block_state;
	private String proxy_id;
	private String reco_company_id;
	private String rese_company_id;
	private String sign_company_id;
	private String pay_company_id;
	private String other_info;
	
	
	public String getBlock_id() {
		return block_id;
	}
	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}
	@Id
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public double getBlock_length() {
		return block_length;
	}
	public void setBlock_length(double block_length) {
		this.block_length = block_length;
	}
	public double getBlock_width() {
		return block_width;
	}
	public void setBlock_width(double block_width) {
		this.block_width = block_width;
	}
	public double getBlock_area() {
		return block_area;
	}
	public void setBlock_area(double block_area) {
		this.block_area = block_area;
	}
	public int getBlock_state() {
		return block_state;
	}
	public void setBlock_state(int block_state) {
		this.block_state = block_state;
	}
	public String getProxy_id() {
		return proxy_id;
	}
	public void setProxy_id(String proxy_id) {
		this.proxy_id = proxy_id;
	}
	public String getReco_company_id() {
		return reco_company_id;
	}
	public void setReco_company_id(String reco_company_id) {
		this.reco_company_id = reco_company_id;
	}
	public String getRese_company_id() {
		return rese_company_id;
	}
	public void setRese_company_id(String rese_company_id) {
		this.rese_company_id = rese_company_id;
	}
	public String getSign_company_id() {
		return sign_company_id;
	}
	public void setSign_company_id(String sign_company_id) {
		this.sign_company_id = sign_company_id;
	}
	public String getPay_company_id() {
		return pay_company_id;
	}
	public void setPay_company_id(String pay_company_id) {
		this.pay_company_id = pay_company_id;
	}
	public String getOther_info() {
		return other_info;
	}
	public void setOther_info(String other_info) {
		this.other_info = other_info;
	}
	
	

}
