package com.db.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.db.common.annotation.RequiredLog;
import com.db.common.exception.ServiceException;
import com.db.common.util.PageUtil;
import com.db.common.util.ShiroUtil;
import com.db.common.vo.PageObject;
import com.db.sys.dao.SysUserDao;
import com.db.sys.dao.SysUserRoleDao;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysUserService;
import com.db.sys.vo.SysUserDeptVo;

//類上定義共性，方法上定義特性
//默認是運行時異常RuntimeException會回滾, 假如需要所有的異常都回滾則需定義為Throwable.class
//timeout定義超時時間，默認是-1(永遠不會超時)，超時時間一到會拋出異常回滾
//假如只做查詢，可以把readOnly設為true，以提高併發效能，默認是false
@Transactional(isolation=Isolation.READ_COMMITTED, 
				rollbackFor=Throwable.class,
				timeout=60,
				readOnly=false) //readOnly=false 默認值
@Service
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	//SLF4j
	private static Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
	
	//這邊的事務控制設置為readOnly=true，因為我們除了事務控制的切面還有日誌的切面，當事務控制切面執行後，
	//再去執行日誌切面時會進行數據庫寫入日誌記錄，此時這個寫入的操作會加入到事務裡，因為readOnly=true，
	//此時就會拋出SQLException:Connection is read-only的異常，這跟事務的傳播特性有關
	@Transactional(readOnly=true)
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(Integer pageCurrent, String username) {
		//1.參數校驗
		if(pageCurrent==null || pageCurrent<1)
			throw new IllegalArgumentException("頁碼值無效");
		//2.查詢總記錄數，並進行校驗
		int rowCount = sysUserDao.getRowCount(username);
		if(rowCount == 0)
			throw new IllegalArgumentException("沒有對應記錄");
		//3.查詢當前頁要呈現的記錄
		int pageSize = 3;
		int startIndex = (pageCurrent - 1) * pageSize;
		List<SysUserDeptVo> records = sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.封裝數據並返回
		return PageUtil.newInstance(pageCurrent, rowCount, pageSize, records);
	}

	@RequiredLog("禁用啟用")
	@RequiresPermissions("sys:user:valid")
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		//1.參數校驗
		if(id==null || id<1)
			throw new IllegalArgumentException("id值無效");
		if(valid==null || (valid!=0&&valid!=1))
			throw new IllegalArgumentException("valid值無效");
		//2.修改狀態
		int rows = sysUserDao.validById(id, valid, modifiedUser);
		if(rows == 0)
			throw new ServiceException("記錄可能已經不存在");
		//3.返回結果
		return rows;
	}

	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		//1.參數校驗
		if(entity==null)
			throw new IllegalArgumentException("保存對象不能為空");
		if(StringUtils.isEmpty(entity.getUsername())) {
			//if(LogContent.enable) {
				log.error("username is null");
			//}	
			throw new IllegalArgumentException("用戶名不能為空");	
		}	
		if(StringUtils.isEmpty(entity.getPassword()))
			throw new IllegalArgumentException("密碼不能為空");
		//...
		if(roleIds==null || roleIds.length==0)
			throw new ServiceException("必須為用戶分配角色");
		//2.保存用戶自身信息
		//2.1構建一個鹽值對象
		String salt = UUID.randomUUID().toString();
		//2.2對密碼進行加密
		//String password1 = DigestUtils.md5DigestAsHex((entity.getPassword()+salt).getBytes()); //Spring框架提供
		String password1 = DigestUtils.md5DigestAsHex((salt+entity.getPassword()).getBytes()); //Spring框架提供
		System.out.println("password1=" + password1);
		SimpleHash sh = new SimpleHash(//Shiro框架提供
				"MD5",	//algorithmName 算法名稱
				entity.getPassword(), //source要加碼的對象
				salt,	//鹽值
				1);//hashIterations 加密次數
		String password2 = sh.toHex();
		System.out.println("password2=" + password2);	
		entity.setPassword(password2);
		entity.setSalt(salt);
		int rows = sysUserDao.insertObject(entity);
		//3.保存用戶和角色關係數據
		System.out.println(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//4.返回結果
		return rows;
	}
	
	@Transactional(readOnly=true)
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		//1.參數校驗
		if(id==null || id<1)
			throw new IllegalArgumentException("id值不正確");
		//2.基於id查詢用戶以及對應的部門信息
		SysUserDeptVo user = sysUserDao.findObjectById(id);
		if(user==null)
			throw new ServiceException("用戶信息不存在");
		//3.基於id查詢用戶對應的角色id
		List<Integer> roleIds = sysUserRoleDao.findRoleIdsByUserId(id);
		//4.對信息進行封裝	
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}

	//@Transactional 類上定義共性，方法上定義特性，因為這個類中的方法都要進行事務控制，所以把他提到類上面定義
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.參數校驗
		if(entity==null)
			throw new IllegalArgumentException("保存對象不能為空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用戶名不能為空");
		//...
		if(roleIds==null || roleIds.length==0)
			throw new ServiceException("必須為用戶分配角色");
		//2.保存用戶自身信息
		int rows = sysUserDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("記錄可能已經不存在");
		//3.保存用戶和角色關係數據
		//3.1先刪除關係數據
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		//3.2再添加新的關係數據
		int insertRows = sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		if(insertRows != 1)
			throw new ServiceException("update error");
		//4.返回結果
		return rows;
	}

	@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
		System.out.println("password="+password+"newPassword"+newPassword+"cfgPassword"+cfgPassword);
		//1.參數驗證
		//1.1原密碼校驗
		if(StringUtils.isEmpty(password))
			throw new IllegalArgumentException("原密碼不能為空");
		SysUser user = ShiroUtil.getUser();	//自己寫的工具類
		SimpleHash sh = new SimpleHash("MD5", password, user.getSalt(), 1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new IllegalArgumentException("原密碼不正確");
		//1.2新密碼非空校驗，校驗其它(密碼位數，包含字母和數字...，用正則表達式)
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密碼不能為空");
		if(!newPassword.equals(cfgPassword))
			throw new IllegalArgumentException("兩次輸入的密碼不相等");
		//2.修改密碼
		//2.1新密碼加密
		String newSalt = UUID.randomUUID().toString();
		sh = new SimpleHash("MD5", newPassword, newSalt, 1);
		//2.2新密碼更新到數據庫
		int rows = sysUserDao.updatePassword(sh.toHex(), newSalt, user.getId());
		if(rows == 0)
			throw new ServiceException("更新失敗");
		//3.返回結果
		return rows;
	}

}
