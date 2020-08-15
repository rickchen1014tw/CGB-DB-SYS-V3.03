package com.db.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 業務層用於封裝分頁數據的一個值對像(Value Object)
 * 
 */
public class PageObject<T> implements Serializable {

	private static final long serialVersionUID = -7368493786259905794L;
	/**當前頁記錄*/
	private List<T> records;
	/**總行數(通過查詢獲得)*/
	private Integer rowCount=0;
	/**頁面大小*/
	private Integer pageSize=3;
	/**總頁數(通過計算獲得)*/
	private Integer pageCount=0;
	/**當前頁的頁碼值*/
	private Integer pageCurrent=1;
	
	public List<T> getRecords() {
		return records;
	}
	public void setRecords(List<T> records) {
		this.records = records;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getPageCurrent() {
		return pageCurrent;
	}
	public void setPageCurrent(Integer pageCurrent) {
		this.pageCurrent = pageCurrent;
	}
}
