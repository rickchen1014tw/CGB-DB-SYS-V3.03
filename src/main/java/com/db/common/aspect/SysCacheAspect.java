package com.db.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Aspect
@Service
public class SysCacheAspect {
	/**
	 * 定義切入點，其中@annotation為註解方式切入點表達式定義
	 */
	//@Pointcut("execution(* com.db.sys.service..*.find*(..))")	(任意返回值類型，service包以及子包內所有類(*)find開頭的所有方法任意參數)
	@Pointcut("@annotation(com.db.common.annotation.RequiredCache)") //還需要在方法上加註解
	public void doCache() {}
	
	//@Around("@annotation(com.db.common.annotation.RequiredCache)") 這樣寫可以省略上面的doCache()方法
	@Around("doCache()")
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		//1.先從緩存取數據
		System.out.println("query data from cache");
		//2.緩存沒有則執行業務
		System.out.println("execute target method");
		Object result = jp.proceed();
		//3.將查詢結果存儲到緩存對象
		System.out.println("put data to cache");
		return result;
	}
}
