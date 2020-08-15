package com.db.sys.service;

import java.util.List;
import java.util.Map;

import com.db.common.vo.Node;
import com.db.sys.entity.SysMenu;

public interface SysMenuService {

	List<Map<String,Object>> findObjects();
	
	/**
	 * 基於菜單id刪除菜單數據以及關係表中的數據
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	List<Node> findZtreeMenuNodes();
	
	/**
	 * 將菜單信息保存到數據庫
	 * @param menu
	 * @return
	 */
	int saveObject(SysMenu menu);
	
	/**
	 * 將菜單信息更新到數據庫
	 * @param menu
	 * @return
	 */
	int updateObject(SysMenu menu);
}
