package com.db.common.vo;

import java.io.Serializable;

/**
 * FAQ?為什麼設計這個對象
 * 1)使用此對象封裝和checkbox相關的信息
 * FAQ?那使用map封裝不可以嗎?可以，但是
 * 1)值類型不可控
 * 2)可讀性不好(打開map源碼不知道其內部存儲了什麼)
 */
public class CheckBox implements Serializable {

	private Integer id;
	private String name;
	
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
	
	
}
