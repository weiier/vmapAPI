package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_company")
public class Company {
	
	private String company_id;
	private String show_name;
	private String company_name_ch;
	private String company_name_en;
	private String email;
	private String phone;
	private String address_ch;
	private String address_en;
	private String con_per;
	private String contact;
	private String alt_con;
	private String company_color;
	private String other_info;
	private String last_time;
	private String registration_date;
	@Id
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getShow_name() {
		return show_name;
	}
	public void setShow_name(String show_name) {
		this.show_name = show_name;
	}
	public String getCompany_name_ch() {
		return company_name_ch;
	}
	public void setCompany_name_ch(String company_name_ch) {
		this.company_name_ch = company_name_ch;
	}
	public String getCompany_name_en() {
		return company_name_en;
	}
	public void setCompany_name_en(String company_name_en) {
		this.company_name_en = company_name_en;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress_ch() {
		return address_ch;
	}
	public void setAddress_ch(String address_ch) {
		this.address_ch = address_ch;
	}
	public String getAddress_en() {
		return address_en;
	}
	public void setAddress_en(String address_en) {
		this.address_en = address_en;
	}
	public String getCon_per() {
		return con_per;
	}
	public void setCon_per(String con_per) {
		this.con_per = con_per;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAlt_con() {
		return alt_con;
	}
	public void setAlt_con(String alt_con) {
		this.alt_con = alt_con;
	}
	public String getCompany_color() {
		return company_color;
	}
	public void setCompany_color(String company_color) {
		this.company_color = company_color;
	}
	public String getOther_info() {
		return other_info;
	}
	public void setOther_info(String other_info) {
		this.other_info = other_info;
	}
	public String getLast_time() {
		return last_time;
	}
	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}
	public String getRegistration_date() {
		return registration_date;
	}
	public void setRegistration_date(String registration_date) {
		this.registration_date = registration_date;
	}
	
}
