package com.db.sys.service.impl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.db.common.exception.ServiceException;
import com.db.common.util.PageUtil;
import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;
import com.db.sys.dao.SysRoleDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysRole;
import com.db.sys.service.SysRoleService;
import com.db.sys.vo.SysRoleMenuVo;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	
	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public PageObject<SysRole> findPageObjects(Integer pageCurrent, String name) {
		//1.參數校驗
		if(pageCurrent == null || pageCurrent < 1)
			throw new IllegalArgumentException("當前頁碼不正確");
		//2.查詢總記錄數並進行校驗
		int rowCount = sysRoleDao.getRowCount(name);
		if(rowCount == 0)
			throw new ServiceException("沒有記錄");
		//3.查詢當前頁記錄
		int pageSize = 3;
		int startIndex = (pageCurrent - 1) * pageCurrent;
		List<SysRole> records = sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//4./進行封裝並返回
		PageObject<SysRole> po = PageUtil.newInstance(pageCurrent, rowCount, pageSize, records);
		return po;
	}

	@Override
	public int deleteObject(Integer id) {
		//1.參數校驗
		if(id==null || id<1)
			throw new IllegalArgumentException("id值無效");
		//2.執行刪除
		//我們目前表有邏上的關聯，但沒有設置外鍵是弱關聯，所以先刪除誰都可以，
		//但若是表有設置外鍵是強關聯，先刪除角色信息是刪除不了的，因為有關係數據引用著他，
		//若是強關係，要用下面的順序來刪除
		//2.1刪除角色和用戶關係數據
		sysUserRoleDao.deleteObjectsByRoleId(id);
		//2.2刪除角色和菜單關係數據
		sysRoleMenuDao.deleteObjectsByRoleId(id);
		//sysRoleMenuDao.deleteObjectsByColumnId("role_id",id);
		//2.3刪除角色自身信息
		int rows = sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("記錄可能已經不存在了");
		//3.返回結果
		return rows;
	}

	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.參數校驗
		if(entity == null)
			throw new IllegalArgumentException("保存角色不能為空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能為空");
		if(menuIds == null || menuIds.length == 0)
			throw new IllegalArgumentException("必須為角色分配權限");
		//2.保存角色自身信息
		int rows = sysRoleDao.insertObject(entity);
		//3.保存角色和菜單的關係數據
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		//4.返回結果
		return rows;
	}

	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		//1.參數校驗
		if(id == null || id < 1)
			throw new IllegalArgumentException("id參數");
		//2.基於id查詢數據
		//2.1查詢角色自身信息
		//sysRoleDao.findObjectbyId(id);
		//2.2查詢角色對應的菜單id
		//sysRoleDao.findMenuIdsByRoleId(id);
		//再把兩次查詢的結果封裝到SysRoleMenuVo對象中
		//現在的做法是把兩次查詢整合
		 SysRoleMenuVo roleMenuVo = sysRoleDao.findObjectById(id);
		if(roleMenuVo == null)
			throw new ServiceException("記錄可能已經不存在");
		//3.返回結果
		return roleMenuVo;
	}

	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.參數校驗
		if(entity == null)
			throw new IllegalArgumentException("保存角色不能為空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能為空");
		if(menuIds == null || menuIds.length == 0)
			throw new IllegalArgumentException("必須為角色分配權限");
		//2.更新角色自身信息
		int rows = sysRoleDao.updateObject(entity);
		//3.保存角色和菜單的關係數據
		//3.1先刪除關係數據
		sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
		//3.2再添加新的關係數據
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		//4.返回結果
		return rows;
	}

	@Override
	public List<CheckBox> findObjects() {
		List<CheckBox> list = sysRoleDao.findObjects();
		if(list==null || list.isEmpty())
			throw new ServiceException("系統沒有角色信息");
		return list;
	}

	@Override
	public boolean isRoleExist(String name) {
		if(StringUtils.isEmpty(name))
			throw new IllegalArgumentException("角色名不能為空");
		int rowCount = sysRoleDao.getRowCount(name);
		if(rowCount>0)
			throw new ServiceException("此記錄已存在");
		return false;
	}
}
