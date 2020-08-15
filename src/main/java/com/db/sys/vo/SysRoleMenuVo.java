package com.db.sys.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 通過此對象封裝:
 * 1)基於角色id查詢到的角色自身信息
 * 2)基於角色id查詢到的菜單id
 */
public class SysRoleMenuVo implements Serializable {

	private static final long serialVersionUID = 5126600396520882247L;
	/**角色id*/
	private Integer id;
	/**角色名稱*/
	private String name;
	/**角色備註*/
	private String note;
	/**角色對應的菜單id*/
	private List<Integer> menuIds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<Integer> getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(List<Integer> menuIds) {
		this.menuIds = menuIds;
	}
}
