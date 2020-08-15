package com.db.common.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class SysExceptionAspect {
	
	//擴展功能，出異常就把異常記錄到文件中
	//@AfterThrowing("execution(* com.db..*.*(..))") 
	//這樣寫啟動時出錯了，還不清楚什麼原因，課堂上沒有仔細找，改用下面這個語句試
	//可能跟定義的表達式包含ShiroUserRealm類有關
	@AfterThrowing("bean(*ServiceImpl)")
	public void logExp() {
		System.out.println("出異常，記錄到文件中");
	}
}
