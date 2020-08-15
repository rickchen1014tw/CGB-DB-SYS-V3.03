package com.db.common.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.db.common.annotation.RequiredLog;
import com.db.common.util.IPUtils;
import com.db.common.util.ShiroUtil;
import com.db.sys.dao.SysLogDao;
import com.db.sys.dao.SysUserDao;
import com.db.sys.entity.SysLog;

/**
 * 通過此類為系統中的某些業務操作添加日誌擴展功能
 * 其中: @Aspect 描述的類為一個切面對象，這樣的類中通常會由兩大部分構成
 * 1)Pointcut: 切入點(織入擴展功能的點)
 * 2)Advice: 通知(定義擴展功能)
 */

/*如果沒有加Order(1)，目前我們系統會先跑事務控制的切面再跑日誌切面，所以點選左邊用戶管理選單，執行查詢用戶數據
 * 因為查詢的事務控制設置為readOnly=true，propagation=Propagation.REQUIRED(默認)，
 * 日誌切面會參與到事務控制切面裡，而日誌在這裡會要插入一條數據，會因為事務控制設定readOnly=true導致錯誤
 * java.sql.SQLException: Connection is read-only
 * 解法:設置執行順序，讓日誌切面不參與到事務控制裡(設置@Order)或是設定readOnly=false
 */
@Order(1)
@Aspect
@Service //切面對象也需要交給Spring管理
public class SysLogAspect {
	
	@Autowired
	private SysLogDao sysLogDao;
	/**
	 * 定義切入點
	 * 切入點的定義要藉助@Pointcut
	 * bean(sysUserServiceImp)為切入點表達式
	 * 其語法結構:
	 * 1)bean(bean的名字)，例如bean(sysUserServiceImp) (類首字母小寫)
	 * 2)bean(bean表達式)，例如bean(*ServiceImpl)
	 */
	//@Pointcut("bean(*ServiceImpl)")  對所有 *ServiceImpl類的業務方法執行時執行擴展功能
	//@Pointcut("@annotation(com.db.common.annotation.RequiredLog)") 有@RequireLog註解就加上擴展功能
	@Pointcut("bean(sysUserServiceImpl)") //我們現在要在sysUserServiceImpl對象的業務方法執行時執行擴展功能
	public void doLog() {}
	//doLog()方法名是隨意取的
	/**
	 * @Around 此註解描述的方法為一個advice(通知)，這個通知可以在
	 * 目標方法執行之前和之後做一些事情
	 * @param jp 連接點對象(封裝了目標方法信息)
	 * @return 目標方法的執行結果
	 * @throws Throwable
	 */
	//@Around("bean(sysUserServiceImp)") //這樣寫就可以省略上面的doLog()方法
	@Around("doLog()") //MethodInterceptor
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		long t1 = System.currentTimeMillis();
		//可以在這裡加斷點，會看到系統底層攔截到你要執行sysUserServiceImpl的業務方法，會創建一個代理對象
		//controller轉而調用代理對象執行業務方法，代理對象再調用切面的擴展業務方法，然後再執行目標類的業務方法
		Object result = jp.proceed(); //proceed()底層會執行下一個切面方法，沒有下一個切面則執行目標方法
		long t2 = System.currentTimeMillis();
		Method method = getTargetMethod(jp);
		String methodName = getTargetMethodName(method);
		//輸出訊息應該使用Logger，這裡先跳過
		System.out.println(methodName + " execution time: " + (t2 - t1)); //一般不建議這樣輸出，會影響效能，用Log4j之類的來輸出
		saveObject(jp,(t2-t1));
		return result;
	}
	
	/**
	 * 將用戶行為日誌信息寫入數據庫
	 * @param jp
	 * @param time
	 * @throws Exception 
	 * @throws NoSuchMethodException 
	 */
	private void saveObject(ProceedingJoinPoint jp, long time) throws NoSuchMethodException, Exception {
		//1.獲取用戶行為日誌信息
		//用戶登錄後會把用戶信息寫到session，這邊從session把用戶信息給取出來
		String username = ShiroUtil.getUser().getUsername();
		Method targetMethod = getTargetMethod(jp);
		String method = getTargetMethodName(targetMethod);
		String params = Arrays.toString(jp.getArgs());
		RequiredLog rlog = targetMethod.getDeclaredAnnotation(RequiredLog.class);
		String operation = "operation";
		if(rlog!=null && !StringUtils.isEmpty(rlog.value())) {
			operation = rlog.value();
		}	
		String ip = IPUtils.getIpAddr();
		//2.封裝用戶行為日誌信息
		SysLog log = new SysLog();
		log.setMethod(method);
		log.setParams(params);
		log.setOperation(operation);
		log.setIp(ip);
		log.setUsername(username);
		log.setTime(time);
		log.setCreatedTime(new Date());
		//3.將日誌信息持久化
		sysLogDao.insertObject(log);
	}

	/**
	 * 基於連接點信息獲取目標方法對象
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	private Method getTargetMethod(ProceedingJoinPoint jp) throws NoSuchMethodException, SecurityException {
		//1.獲取目標類
		Class<? extends Object> targetCls = jp.getTarget().getClass();
		System.out.println("targetCls: " + targetCls);  //輸出: targetCls: class com.db.sys.service.impl.SysUserServiceImpl
		//2.獲取方法簽名信息(包含方法名，參數列表等信息)
		MethodSignature ms = (MethodSignature) jp.getSignature();	
		System.out.println("MethodSignature: " + ms);	//輸出: MethodSignature: PageObject com.db.sys.service.PageService.findPageObjects(Integer,String)
		//3.獲取目標方法對象
		Method method = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
		System.out.println("Method: " + method);	//Method: public com.db.common.vo.PageObject com.db.sys.service.impl.SysUserServiceImpl.findPageObjects(java.lang.Integer,java.lang.String)
		return method;
	}
	
	/**
	 * 獲取目標方法的名稱: 類全名+方法名
	 * @param method
	 * @return
	 */
	private String getTargetMethodName(Method method) {
		//去看Method的toString原碼怎麼寫
		return new StringBuilder(method.getDeclaringClass().getName())
				.append(".")
				.append(method.getName()).toString();	
	}

}
