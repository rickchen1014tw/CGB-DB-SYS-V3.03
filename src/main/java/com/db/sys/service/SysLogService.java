package com.db.sys.service;

import com.db.common.vo.PageObject;
import com.db.sys.entity.SysLog;

/**
 * 日誌模塊業務接口: 負責日誌業務的規範定義
 */
public interface SysLogService {
	/**
	 * 按條件執行用戶行為日誌的查詢操作
	 * @param pageCurrent 當前頁碼
	 * @param username	查詢條件
	 * @return 當前需要的日誌數據
	 */
	PageObject<SysLog> findPageObjects(Integer pageCurrent, String username);
	
	/**
	 * 基於id執行日誌刪除業務
	 * @param ids 對應多個id的值
	 * @return 刪除記錄的行數
	 */
	int deleteObjects(Integer... ids);
}
