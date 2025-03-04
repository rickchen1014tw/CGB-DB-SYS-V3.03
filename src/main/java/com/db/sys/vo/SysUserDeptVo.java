package com.db.sys.vo;

import java.io.Serializable;
import java.util.Date;

import com.db.sys.entity.BaseEntity;
import com.db.sys.entity.SysDept;

/**
 * 定義值對象封裝用戶以及關聯的部門信息
 */
public class SysUserDeptVo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -389518316459761407L;
	private Integer id;
	private String username;
	private String password;//md5
	private String salt;	//鹽值:加密時使用，會對應一個隨機字符串
	private String email;
	private String mobile;
	private Integer valid=1;
	private SysDept sysDept; //private Integer deptId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Integer getValid() {
		return valid;
	}
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	public SysDept getSysDept() {
		return sysDept;
	}
	public void setSysDept(SysDept sysDept) {
		this.sysDept = sysDept;
	}
}
