package com.db.sys.entity;

import java.io.Serializable;

public class SysRole extends BaseEntity {

	private static final long serialVersionUID = 7814052030747453305L;
	private Integer id;
	/**用戶名稱*/
	private String name;
	/**角色備注*/
	private String note;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
