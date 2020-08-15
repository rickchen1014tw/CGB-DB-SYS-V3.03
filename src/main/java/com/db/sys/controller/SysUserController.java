package com.db.sys.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.common.util.ShiroUtil;
import com.db.common.vo.JsonResult;
import com.db.sys.entity.SysUser;
import com.db.sys.service.SysUserService;

@RequestMapping("/user")
@Controller
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("doUserListUI")
	public String doUserListUI() {
		return "sys/user_list";
	}
	
	@RequestMapping("doUserEditUI")
	public String doUsertEditUI() {
		return "sys/user_edit";
	}
	
	@RequestMapping("doPwdEditUI")
	public String doPwdEditUI() {
		return "sys/pwd_edit";
	}
	
	@RequestMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(Integer pageCurrent, String username) {
		return new JsonResult(sysUserService.findPageObjects(pageCurrent, username));
	}
	
	@RequestMapping("doValidById")
	@ResponseBody
	public JsonResult doValidById(Integer id, Integer valid) {
		//獲取登錄用戶信息
		SysUser user = ShiroUtil.getUser();
		sysUserService.validById(id, valid, user.getUsername());
		return new JsonResult("update OK");
	}
	
	@RequestMapping("doSaveObject")
	@ResponseBody
	public JsonResult doSaveObject(SysUser entity, Integer[] roleIds) {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save OK");
	}
	
	@RequestMapping("doFindObjectById")
	@ResponseBody
	public JsonResult doFindObjectById(Integer id) {
		Map<String,Object> map = sysUserService.findObjectById(id);
		return new JsonResult(map);
	}
	
	@RequestMapping("doUpdateObject")
	@ResponseBody
	public JsonResult doUpdateObject(SysUser entity, Integer[] roleIds) {
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update OK");
	}
	
	@RequestMapping("doLogin")
	@ResponseBody
	public JsonResult doLogin(String username, String password, boolean isRememberMe) {
		//提交信息到業務層
		//1.獲取主題對象(Subject)
		Subject subject = SecurityUtils.getSubject();
		//2.提交信息(提交給Shiro的securityManager)
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		if(isRememberMe) {
			token.setRememberMe(isRememberMe);
		}
		subject.login(token);	//執行登錄認證
		return new JsonResult("login ok");
	}
	
	@RequestMapping("doUpdatePassword")
	@ResponseBody
	public JsonResult doUpdatePassword(String pwd, String newPwd, String cfgPwd) {
		sysUserService.updatePassword(pwd, newPwd, cfgPwd);
		return new JsonResult("update ok");	
	}
}
