package com.buptmap.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="keytable")
public class KeyTable {
	private String id;
	private String version;
	private String openlevel;
	private String valid;
	private String keystr;
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getOpenlevel() {
		return openlevel;
	}
	public void setOpenlevel(String openlevel) {
		this.openlevel = openlevel;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getKeystr() {
		return keystr;
	}
	public void setKeystr(String keystr) {
		this.keystr = keystr;
	}
	
}
