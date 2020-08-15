package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.CheckBox;
import com.db.sys.entity.SysRole;
import com.db.sys.vo.SysRoleMenuVo;

public interface SysRoleDao {
	
	int getRowCount(@Param("name")String name);
	
	List<SysRole> findPageObjects(
			@Param("name")String name, 
			@Param("startIndex")Integer startIndex, 
			@Param("pageSize")Integer pageSize);
	
	/**
	 * 基於角色id刪除角色自身信息
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	/**
	 * 將角色信息寫入數據庫
	 * @param entity
	 * @return
	 */
	int insertObject(SysRole entity);
	
	/**
	 * 將角色信息更新到數據庫
	 * @param entity
	 * @return
	 */
	int updateObject(SysRole entity);
	
	/**
	 * 基於角色id查詢角色以及對應的菜單信息
	 * @param id
	 * @return
	 */
	SysRoleMenuVo findObjectById(Integer id);
	
	/**
	 * 查詢所有角色信息(id,name)
	 * @return
	 */
	List<CheckBox> findObjects();
	
	/**
	 * 統計角色記錄數
	 * @param name
	 * @return
	 */
	int getRowCountByName(String name);
}
