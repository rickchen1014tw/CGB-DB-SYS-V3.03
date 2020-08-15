package com.db.common.vo;

import java.io.Serializable;

/**
 * 借助此對象封裝控制層數據
 * 1)業務層返回的數據
 * 2)狀態碼
 * 3)狀態信息
 */
public class JsonResult implements Serializable {

	private static final long serialVersionUID = 5265293026695392649L;
	/**狀態碼:1 表示ok，0表示error */
	private int state = 1;
	/**狀態碼對應的狀態信息 */
	private String message = "OK";
	/**正確數據(輸出到客戶端的數據)*/
	private Object data;
	
	public JsonResult() {
	}
	
	public JsonResult(String messgae) {
		this.message = messgae;
	}
	
	public JsonResult(Object data) {
		this.data = data;
	}
	
	/**
	 * 通過此構造方法初始化錯誤信息
	 */
	public JsonResult(Throwable e) {
		this.state = 0;
		this.message = e.getMessage();
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
