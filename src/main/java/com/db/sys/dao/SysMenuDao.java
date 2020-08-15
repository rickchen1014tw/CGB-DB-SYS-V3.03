package com.db.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.db.common.vo.Node;
import com.db.sys.entity.SysMenu;

/**
 * 菜單數據層對象
 */
public interface SysMenuDao {

	List<Map<String,Object>> findObjects();
	
	/**
	 * 統計菜單是否有子菜單
	 * @param id
	 * @return 子菜單的個數
	 */
	int getChildCount(Integer id);
	
	/**
	 * 基於id刪除菜單對象
	 * @param id
	 * @return 刪除的行數
	 */
	int deleteObject(Integer id);
	
	/**
	 * 查詢菜單的節點信息
	 * @return
	 */
	List<Node> findZtreeMenuNodes();
	
	
	/**
	 * 將內存中的menu對象持久化到數據庫
	 * @param menu 菜單對象，其內容最初來自頁面
	 * @return 插入的行數
	 */
	int insertObject(SysMenu menu);
	
	/**
	 * 將內存中的menu對象更新到數據庫
	 * @param menu 菜單對象，其內容最初來自頁面
	 * @return 修改的行數
	 */
	int updateObject(SysMenu menu);
	
	/**
	 * 獲取多個菜單id對應的權限標識
	 * @param menuIds
	 * @return
	 */
	List<String> findPermissions(@Param("menuIds")Integer[] menuIds);
}
