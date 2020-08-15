package com.db.common.util;

import java.util.List;
import com.db.common.vo.PageObject;
import com.db.sys.entity.SysRole;

/**
 * 分頁工具類
 */
public abstract class PageUtil {
	/**泛型方法*//**泛型是實現通用編程的一種方式，可以讓代碼更加靈活 */
	public static <T> PageObject<T> newInstance(Integer pageCurrent, int rowCount, int pageSize, List<T> records) {
		PageObject<T> po = new PageObject<T>();
		po.setRowCount(rowCount);
		po.setRecords(records);
		po.setPageSize(pageSize);
		po.setPageCurrent(pageCurrent);
		po.setPageCount((rowCount-1)/pageSize + 1);
		return po;
	}
}
