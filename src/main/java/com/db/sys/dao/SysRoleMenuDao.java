package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 基於此接口訪問角色/菜單關係表(sys_role_menus)
 */
public interface SysRoleMenuDao {
	/**
	 * 基於菜單id刪除關係表數據
	 * @param menuId 菜單id
	 * @return 刪除的行數
	 */
	int deleteObjectsByMenuId(Integer menuId);
	
	/**
	 * 基於角色id刪除角色菜單表關係數據
	 * @param roleId
	 * @return
	 */
	int deleteObjectsByRoleId(Integer roleId);
	
	/*
	 * 基於字段名以及對應的值執行刪除操作
	 * @param column
	 * @param id
	 * @return
	int deleteObjectsByColumnId(
		@Param("column")String column, 
		@Param("id")Integer id);*/
	
	/**
	 * 將角色和菜單的關係數據寫入到數據庫
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	int insertObjects(
			@Param("roleId")Integer roleId, 
			@Param("menuIds")Integer[] menuIds);
	
	/**
	 * 基於多個角色id查詢菜單id
	 * @param roleId
	 * @return
	 */
	List<Integer> findMenuIdsByRoleIds(@Param("roleIds")Integer[] roleIds);
	
}
