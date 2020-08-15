package com.db.common.web;

import javax.security.auth.login.CredentialException;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.common.vo.JsonResult;

/**
 * @ControllerAdvice 修飾的類為一個全局異常處理類
 * 此類需要在配置文件(spring-web.xml)中掃描配置
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	/**
	 * ExceptionHandler 注解修飾的方法為異常處理方法
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)//能夠處理的異常為RuntimeException，以及其子類
	@ResponseBody
	public JsonResult doHandleRuntimeException(RuntimeException e) {
		e.printStackTrace();
		return new JsonResult(e);
	}
	
	@ExceptionHandler(ShiroException.class)
	@ResponseBody
	public JsonResult doHandleShiroException(ShiroException e) {
		log.error("shiro "+e.getMessage());
		e.printStackTrace();
		JsonResult result = new JsonResult();
		result.setState(0);
		if(e instanceof UnknownAccountException) {
			result.setMessage("帳戶不存在");
		}else if (e instanceof LockedAccountException) {
			result.setMessage("帳戶已被禁用");
		}else if(e instanceof IncorrectCredentialsException) {
			result.setMessage("密碼不正確");
		}else if(e instanceof AuthorizationException){
			result.setMessage("沒有此操作權限");
		}
		return result;
	}
}
