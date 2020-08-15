package com.db.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.expression.AccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.db.common.exception.ServiceException;

/**
 * spring mvc中的handler攔截器定義
 * 基於此攔截器實現按時間的攔截和放行
 * 記得還要在spring-web.xml配置
 * @author chenminghong
 * 
 * 這個攔截器不是基於代理對象去實現的，是基於回調
 *
 */
@Component
public class TimeAccessInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 此方法在後端控制器方法執行之前執行
	 * @return 此返回值決定了這個請求繼續傳遞還是到此為止
	 * return true: 繼續執行下一個攔截器，沒有攔截器就執行controller
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("TimeAccessInterceptor preHandle...");
		//1.獲取一個日曆對象(通過此對象設置時間)
		Calendar c = Calendar.getInstance();
		//2.設置開始允許訪問時間
		c.set(Calendar.HOUR_OF_DAY, 9);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long start = c.getTimeInMillis();
		//3.設置結束允許訪問時間
		c.set(Calendar.HOUR_OF_DAY, 22);
		long end = c.getTimeInMillis();
		//4.基於時間設置攔截和放行
		long time = System.currentTimeMillis();
		if(time<start || time>end)
			throw new ServiceException("此時間點不允許訪問");
		return true;
	}
	
	/*
	 * 在Controller(Handler)之後執行這個方法，在DispatcherServlet渲染視圖之前
	@Override
	public void postHandle(
	 */
	
	/*
	 * 在完成請求的處理後，在DispatcherServlet渲染視圖之後
	@Override
	public void afterCompletion(
	 */
	
	
}
