package com.db.sys.service;

import java.util.List;

import com.db.common.vo.CheckBox;
import com.db.common.vo.PageObject;
import com.db.sys.entity.SysRole;
import com.db.sys.vo.SysRoleMenuVo;

public interface SysRoleService {
	
	/**
	 * 分頁查詢當前角色信息
	 * @param name
	 * @param pageCurrent
	 * @return
	 */
	PageObject<SysRole> findPageObjects(Integer pageCurrent, String name);
	
	/**
	 * 基於角色id刪除角色相關信息
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 將角色自身信息以及對應的菜單寫入到數據庫
	 * @param entity
	 * @param menuIds
	 * @return
	 */
	int saveObject(SysRole entity, Integer[] menuIds);
	
	/**
	 * 將角色自身信息以及對應的菜單更新到數據庫
	 * @param entity
	 * @param menuIds
	 * @return
	 */
	int updateObject(SysRole entity, Integer[] menuIds);
	
	/**
	 * 基於角色ID獲取角色以及對應的菜單信息
	 * @param id
	 * @return
	 */
	SysRoleMenuVo findObjectById(Integer id);
	
	/**
	 * 查詢所有角色信息
	 * @return
	 */
	List<CheckBox> findObjects();
	
	/**
	 * 判定角色名稱是否存在
	 * @param name
	 * @return
	 */
	boolean isRoleExist(String name);
}
