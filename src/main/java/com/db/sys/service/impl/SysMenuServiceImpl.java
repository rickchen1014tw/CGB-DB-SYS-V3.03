package com.db.sys.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.db.common.annotation.RequiredCache;
import com.db.common.exception.ServiceException;
import com.db.common.vo.Node;
import com.db.sys.dao.SysMenuDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.entity.SysMenu;
import com.db.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {
	
	@Autowired
	private SysMenuDao sysMenuDao;
	//@Autowired
	@Resource(name="sysRoleMenuDao")	//@Resource等效於@Autowired+@Qualifier
	private SysRoleMenuDao sysRoleMenuDao;
	
	@RequiredCache
	@Override
	public List<Map<String, Object>> findObjects() {
		List<Map<String, Object>> list = sysMenuDao.findObjects();
		if(list == null || list.size() == 0) //寫成if(list.size()==0)||list==null), 若是list是null的情況下會出現NullPointerException
			throw new ServiceException("沒有對應的菜單信息");
		return list;
	}

	@Override
	public int deleteObject(Integer id) {
		//1.參數校驗
		if(id==null || id<1)
			throw new IllegalArgumentException("id值無效");
		//2.基於id判定此菜單是否有子元素
		int childCount = sysMenuDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("請先刪除子元素");
		//3.基於id刪除角色菜單關係數據
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//4.基於id刪除菜單自身數據
		int rows = sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("記錄可能已經不存在");
		//5.返回結果
		return rows;
		/**
		 * 這裡沒有考慮事務的控制，sysRoleMenuDao.deleteObjectsByMenuId(id);跟sysMenuDao.deleteObject(id);
		 * 要嘛都完成，要嘛都不完成，不然數據表會出錯
		 */
	}

	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}

	@Override
	public int saveObject(SysMenu menu) {
		//1.校驗參數
		if(menu == null)
			throw new IllegalArgumentException("保存對象不能為空");
		if(StringUtils.isEmpty(menu.getName()))
			throw new IllegalArgumentException("菜單名不能為空");
		//...依此驗證
		//2.保存數據
		int rows = sysMenuDao.insertObject(menu);
		//3.返回結果
		return rows;
	}

	@Override
	public int updateObject(SysMenu menu) {
		//1.校驗參數
		if(menu == null)
			throw new IllegalArgumentException("保存對象不能為空");
		if(StringUtils.isEmpty(menu.getName()))
			throw new IllegalArgumentException("菜單名不能為空");
		//...依此驗證
		//2.保存數據
		int rows = sysMenuDao.updateObject(menu);
		if(rows == 0)
			throw new ServiceException("記錄可能已經不存在");
		//3.返回結果
		return rows;
	}
}
