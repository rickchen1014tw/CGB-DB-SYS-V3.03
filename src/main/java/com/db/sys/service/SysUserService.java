package com.db.sys.service;

import java.util.Map;

import com.db.common.vo.PageObject;
import com.db.sys.entity.SysUser;
import com.db.sys.vo.SysUserDeptVo;

public interface SysUserService extends PageService<SysUserDeptVo> {
	
	int validById(Integer id, Integer valid, String modifiedUser);
	
	/**
	 * 保存用戶以及用戶對應的角色信息
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	int saveObject(SysUser entity, Integer[] roleIds);
	
	Map<String,Object> findObjectById(Integer id);
	
	/**
	 * 更新用戶以及用戶對應的角色信息
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	int updateObject(SysUser entity, Integer[] roleIds);
	
	int updatePassword(String password, String newPassword, String cfgPassword);
	
	
}
