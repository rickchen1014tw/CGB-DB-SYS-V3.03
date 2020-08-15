package com.db.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.common.vo.JsonResult;
import com.db.common.vo.PageObject;
import com.db.sys.entity.SysLog;
import com.db.sys.service.SysLogService;

@Controller	//告訴spring此類交給spring管理
@RequestMapping("/log")	//定義對外的url
public class SysLogController {
	@Autowired	//通過反射拿到這個屬性為他賦值
	@Qualifier("sysLogServiceImpl") //若SysLogService有多個子類對象在bean池，可以用@Qualifier來指定要注入的子類對象
	private SysLogService sysLogService;

	@RequestMapping("doLogListUI")
	public String doLogListUI() {
		return"sys/log_list";
	}
	
	@RequestMapping("doDeleteObjects")
	@ResponseBody
	public JsonResult doDeleteObject(Integer...ids) {
		sysLogService.deleteObjects(ids);
		return new JsonResult("delete OK");
	}
	
	/*
	@GetMapping("doFindPageObjects")
	@ResponseBody
	public PageObject<SysLog> doFindPageObjects(Integer pageCurrent, String username) {
		PageObject<SysLog> pageObjects = sysLogService.findPageObjects(pageCurrent, username);
		return pageObjects;
		當pageCurrent傳入一個不正確的值，頁面上會出現一個不友好的錯誤信息(Http Status 500)，
		  我們應該想辦法把這種錯誤的提示進行一個信息的轉換，一種比較友好的方式進行呈現，那怎麼做呢?
		  我們接著會對我們service層返回的數據再次進行封裝，不能直接把service層的數據直接輸出給客戶端，
		  正確的情況下沒有問題，一旦出錯了出現的信息不符合我們的要求，那怎麼做呢?
		  一般情況下控制層向客戶端傳遞數據都會給他一個狀態信息狀態碼，
		  我們任何一個系統，你不但要考慮正確信息的輸出還要考慮錯誤信息的輸出，
		  接下來我們會構建一個JsonResult類，把service層的信息跟狀態碼狀態信息封裝到這個對象裡，
		  再把這個對象輸出給客戶端
		 	
	}*/
	
	@GetMapping("doFindPageObjects")
	@ResponseBody
	public JsonResult doFindPageObjects(Integer pageCurrent, String username) {		
		PageObject<SysLog> pageObjects = sysLogService.findPageObjects(pageCurrent, username);
		return new JsonResult(pageObjects);
	}
	/*
	 * 在這個controller裡也可以定義異常處理方法，
	 * 	@ExceptionHandler(RuntimeException.class)//能夠處理的異常為RuntimeException，以及其子類
	 *@ResponseBody
	 *public JsonResult doHandleRuntimeException(RuntimeException e) {
	 *	e.printStackTrace();
	 *	return new JsonResult(e);
	 *}
	 *但若controller內部沒有定義異常處理方法時，系統就會去找有沒有全局異常處理方法@ControllerAdvice
	 */
}

