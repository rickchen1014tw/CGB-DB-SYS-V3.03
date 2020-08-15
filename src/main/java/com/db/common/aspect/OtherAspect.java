package com.db.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class OtherAspect {
	
	//把切面表達式提取出來，不然就得在每個通知裡個別定義表達式，一旦表達式要改就要一個一個改
	@Pointcut("bean(sysUserServiceImpl)")
	public void doAspect() {}

	@Before("doAspect()")
	public void before() {
		System.out.println("@Before");
	}
	
	@After("doAspect()")
	public void after() {
		System.out.println("@After");
	}
	
	@AfterReturning("doAspect()")
	public void afterReturning() {
		System.out.println("@AfterReturning");
	}
	
	@AfterThrowing("doAspect()")
	public void afterThrowing(JoinPoint jp) {
	//public void afterThrowing() { 可以不寫參數也可以寫，看需不需要使用到，上面的其它方法亦同
		System.out.println("@AfterThrowing");
	}
	
	//@Around必須如下寫方法的signature，也只有@Around可以使用ProceedingJoinPoint jp
	//其它要使用JoinPoint jp
	@Around("doAspect()")
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("@Around before");
		Object result = jp.proceed();
		System.out.println("@Around after");
		return result;
	}
}
