package com.db.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.db.sys.dao.SysMenuDao;
import com.db.sys.dao.SysRoleMenuDao;
import com.db.sys.dao.SysUserDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysUser;

@Service
public class ShiroUserRealm extends AuthorizingRealm {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysMenuDao sysMenuDao;
	/**
	 * 設置憑置匹配器(此方法用於告訴認證管理器採用什麼加密算法對用戶輸入的密碼進行加密)
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		//1.構建CredentialsMatcher對象
		HashedCredentialsMatcher hcm = new HashedCredentialsMatcher();
		//2.設置加密算法
		hcm.setHashAlgorithmName("MD5");
		//3.設置加密次數
		hcm.setHashIterations(1);
		super.setCredentialsMatcher(hcm);
	}
	
	/**
	 * 通過此方法完成認證信息的獲取和封裝
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.從token中獲取用戶信息(前端用戶輸入)
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		//2.基於用戶名從數據庫中查詢用戶信息
		String username = upToken.getUsername();
		SysUser user = sysUserDao.findUserByUserName(username);
		//3.判定用戶是否存在
		if(user == null)
			throw new UnknownAccountException();
		//4.判定用戶是否已被禁用
		if(user.getValid()==0)
			throw new LockedAccountException();
		//5.封裝用戶信息並返回
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
				user, //principal 表示身份信息
				user.getPassword(), //hashedCredentials 已加密的密碼
				credentialsSalt, //salt
				getName()); //realmName 
				//"ShiroUserRealm");
		return info; //此對象會返回給認證管理器
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("==doGetAuthorizationInfo==");//假如沒有加緩存，則每次都要執行這個方法查詢用戶的權限
		//1.獲取登錄用戶id
		SysUser user =(SysUser) principals.getPrimaryPrincipal();
		//2.基於用戶id獲取用戶對應的角色id並判定
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(user.getId());
		if(roleIds==null || roleIds.isEmpty())
			throw new AuthorizationException();
		//3.基於角色id獲取對應的菜單id
		Integer[] array = {};
		List<Integer> menuIds = sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
		if(menuIds==null || menuIds.isEmpty())
			throw new AuthorizationException();
		//4.基於菜單id獲取權限標識
		List<String> permissions = sysMenuDao.findPermissions(menuIds.toArray(array));
		if(permissions==null || permissions.isEmpty())
			throw new AuthorizationException();
		//5.封裝數據並返回
		Set<String> permissionSet = new HashSet<String>();
		for(String per:permissions) {
			if(!StringUtils.isEmpty(per)) {
				permissionSet.add(per);
			}
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permissionSet);
		return info; //此對象會返回給授權管理器
	}
}
