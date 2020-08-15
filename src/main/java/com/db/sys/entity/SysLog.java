package com.db.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 借助此對象封裝從數據庫查詢到的日誌信息
 */
public class SysLog implements Serializable {
	
	private static final long serialVersionUID = -7132932380096552894L;
	
	private Integer id;
	//用戶名
	private String username;
	//用戶操作
	private String operation;
	//請求方法
	private String method;
	//請求參數
	private String params;
	//執行時長(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//創建時間
	private Date createdTime;
	
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
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Override
	public String toString() {
		return "SysLog [id=" + id + ", username=" + username + ", operation=" + operation + ", method=" + method
				+ ", params=" + params + ", time=" + time + ", ip=" + ip + ", createdTime=" + createdTime + "]";
	}
}
