package com.db.common.util;

import org.apache.shiro.SecurityUtils;

import com.db.sys.entity.SysUser;

public abstract class ShiroUtil {
	private ShiroUtil() {} //把構造方法設為private，這樣子類就沒辦法調到無參的構造方法，就沒辦法繼承這個類了也沒辦法new這個類

	public static SysUser getUser() {
		SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
}
