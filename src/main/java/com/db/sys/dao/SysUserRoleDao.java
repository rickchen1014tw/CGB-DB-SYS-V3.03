package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;


/**
 * 借助此dao操作用戶和角色關係
 * @author chenminghong
 */
public interface SysUserRoleDao {

	/**
	 * 基於角色id刪除角色和用戶表關數據
	 * @param roleId
	 * @return
	 */
	int deleteObjectsByRoleId(Integer roleId);
	
	/**
	 * 將用戶角色關係數據寫入用戶角色關係表
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	int insertObjects(
			@Param("userId")Integer userId, 
			@Param("roleIds")Integer[] roleIds);
	
	/**
	 * 基於用戶id獲取用戶角色信息
	 * @param userId
	 * @return
	 */
	List<Integer> findRoleIdsByUserId(Integer userId);
	
	/**
	 * 基於用戶id刪除用戶和角色關係數據
	 * @param userId
	 * @return
	 */
	int deleteObjectsByUserId(Integer userId);
	
}
