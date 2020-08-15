package com.db.sys.service;

import com.db.common.vo.PageObject;
import com.db.sys.vo.SysUserDeptVo;

public interface PageService<T> {

	PageObject<T> findPageObjects(Integer pageCurrent, String username);

}
