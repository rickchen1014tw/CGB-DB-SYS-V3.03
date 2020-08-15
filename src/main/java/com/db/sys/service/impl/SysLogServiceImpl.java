package com.db.sys.service.impl;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.db.common.exception.ServiceException;
import com.db.common.util.PageUtil;
import com.db.common.vo.PageObject;
import com.db.sys.dao.SysLogDao;
import com.db.sys.entity.SysLog;
import com.db.sys.service.SysLogService;

@Service
//@Scope("singleton")
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public PageObject<SysLog> findPageObjects(Integer pageCurrent, String username) {
		//1.參數的合法性校驗
		if(pageCurrent == null || pageCurrent < 1)
			throw new IllegalArgumentException("頁碼值無效");
		//2.基於用戶名統計總記錄數並進行驗證
		int rowCount = sysLogDao.getRowCount(username);
		if(rowCount == 0)
			throw new ServiceException("沒有找到對應記錄");
		//3.基於查詢條件查詢當前頁的日誌信息
		//3.1定義頁面大小
		int pageSize = 3;
		//3.2計算起始位置
		int startIndex = (pageCurrent -1) * pageSize;
		//3.3基於起始位置執行當前頁數據的查詢
		List<SysLog> records = sysLogDao.findPageObjects(username, startIndex, pageSize);
		if(records.size() == 0)
			throw new ServiceException("沒有找到對應記錄");
		//4.對查詢結果進行封裝並返回
		/*PageObject<SysLog> po = new PageObject<SysLog>();
		po.setRowCount(rowCount);
		po.setRecords(records);
		po.setPageCurrent(pageCurrent);
		po.setPageSize(pageSize);
		po.setPageCount((rowCount-1)/pageSize + 1);*/
		PageObject<SysLog> po = PageUtil.newInstance(pageCurrent, rowCount, pageSize, records);
		return po;
	}
	
	@RequiresPermissions("sys:log:delete")
	@Override
	public int deleteObjects(Integer... ids) {
		//1.判定參數有效性
		if(ids == null || ids.length == 0)
			throw new IllegalArgumentException("請先選擇");
		//2.基於參數id執行刪除操作
		int rows = 0;
		try {
			rows = sysLogDao.deleteObjects(ids);
		} catch (Throwable e) {
			e.printStackTrace();
			//報警，給運維人員發短信
			throw new ServiceException("系統維護中");
		}
		if(rows == 0)
			throw new ServiceException("記錄可能已經不存在了");
		//3.返回刪除結果
		return rows;
	}

}
