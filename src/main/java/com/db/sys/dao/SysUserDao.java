package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.sys.entity.SysUser;
import com.db.sys.vo.SysUserDeptVo;

public interface SysUserDao {
	/**
	 * 按條件查詢總記錄數
	 * @param username
	 * @return
	 */
	int getRowCount(@Param("username")String username);
	/**
	 * 按條件查詢當前頁記錄
	 * @param username
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<SysUserDeptVo> findPageObjects(
			@Param("username")String username,
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize);
	
	/**
	 * 禁用或啟用用戶信息
	 * @param id 用戶id
	 * @param valid 狀態
	 * @param modifiedUser 修改用戶
	 * @return 修改的行數
	 */
	int validById(
			@Param("id")Integer id, 
			@Param("valid")Integer valid, 
			@Param("modifiedUser")String modifiedUser);
	
	/**
	 * 保存用戶自身信息
	 * @param entity
	 * @return
	 */
	int insertObject(SysUser entity);
	
	/**
	 * 基於用戶id查詢用戶以及對應的部門信息
	 * @param id
	 * @return
	 */
	SysUserDeptVo findObjectById(Integer id);
	
	/**
	 * 更新用戶自身信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysUser entity);
	
	/**
	 * 基於用戶名查詢用戶信息
	 * @param username
	 * @return
	 */
	SysUser findUserByUserName(String username);
	
	/**
	 * 基於用戶id修改用戶的密碼
	 * @param password
	 * @param salt
	 * @param id
	 * @return
	 */
	int updatePassword(
			@Param("password")String password,
			@Param("salt")String salt,
			@Param("id")Integer id);

}
