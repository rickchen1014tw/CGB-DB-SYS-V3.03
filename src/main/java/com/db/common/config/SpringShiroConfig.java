package com.db.common.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.db.sys.service.realm.ShiroUserRealm;

/**
 * @Configuration 描述的bean一般為一個配置類
 * @author chenminghong
 *
 */
@Configuration
public class SpringShiroConfig {
	/**
	 * 第三方的bean要配置在這個配置類裡，自己寫的bean不用，因為自己寫的bean可以在類上面寫註解
	 * @Bean等效於@Service,@Controller...
	 * 此註釋用於告訴Spring，此方法的返回值要存儲到Spring容器中
	 * @Bean一般用戶描述方法，然後將方法的返回值交給Spring管理，其中@Bean註解中的內容為bean
	 * 對象的key
	 */	
	//這個的SecurityManager不是java.lang包中的那個
	@Bean("securityManager")
	public SecurityManager newSecurityManager( 
			//@Autowired ShiroUserRealm realm, @Autowiared RememberMeManager rememberMeManager) { @Autowired可以省略
			ShiroUserRealm realm, RememberMeManager rememberMeManager) {
		DefaultWebSecurityManager sm = new DefaultWebSecurityManager();
		sm.setRealm(realm);
		sm.setCacheManager(newCacheManager());
		sm.setRememberMeManager(rememberMeManager);
		sm.setSessionManager(newDefaultWebSessionManager());
		return sm;
	}
	
	@Bean("shiroFilterFactory")
	public ShiroFilterFactoryBean newShiroFilterFactoryBean(
			@Autowired SecurityManager securityManager) {//@Autowired可以省略，在調用這個方法時，系統會自動按類型找到對應的對象
		//1.構建ShiroFilterFactoryBean對象(負責創建ShiroFilterFactory工廠對象)
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		//2.設置安全管理器
		factoryBean.setSecurityManager(securityManager);
		//3.設置登錄頁面的url(非認證用戶要跳轉到此url對應的頁面)
		factoryBean.setLoginUrl("/doLoginUI.do");
		//4.設置過瀘規測(哪些允許匿名訪問，哪些需要認證訪問)
		//可以在xml版本的setFilterChainDefinitionMap()加斷點看系統用什麼map
		//從setFilterChainDefinitionMap()只看得出參數是Map接口，不知道具體是什麼Map
		//但邏輯上可以知道這個Map應該會記錄添加順序，前面是可以匿名訪問，後面需要認證才能訪問，所以應該是用LinkedHashMap
		Map<String,String> filterMap = new LinkedHashMap<String,String>(); 
		filterMap.put("/bower_components/**", "anon");
		filterMap.put("/build/**", "anon");
		filterMap.put("/dist/**", "anon");
		filterMap.put("/plugins/**", "anon");
		filterMap.put("/user/doLogin.do", "anon");
		filterMap.put("/doLogout.do", "logout");
		//filterMap.put("/**", "authc"); 記住我功能需設置為user
		filterMap.put("/**", "user");
		factoryBean.setFilterChainDefinitionMap(filterMap);
		return factoryBean; 
		//其實這邊也可以回傳factoryBean.getObject(); 只是回傳的型態要改成ShiroFilterFactory
	}
	
	//@Bean註解沒有指定名字時，默認bean的名字為方法名
	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor newLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	
	@DependsOn("lifecycleBeanPostProcessor")
	@Bean
	public DefaultAdvisorAutoProxyCreator newDefaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}
	
	@Bean
	public AuthorizationAttributeSourceAdvisor newAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = 
				new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
	
	//配置緩存管理器(可以緩存用戶的權限訊息)
	public MemoryConstrainedCacheManager newCacheManager() {
		return new MemoryConstrainedCacheManager();
	}
	
	//這邊用一種不一樣的寫法，把兩個bean的定義合併成一個
	@Bean
	public CookieRememberMeManager newCookieRememberMeManager() {
		CookieRememberMeManager cookieManager = new CookieRememberMeManager();
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setMaxAge(7*24*60*60);
		cookieManager.setCookie(cookie);
		return cookieManager;		
	}
	
	public DefaultWebSessionManager newDefaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(21600000);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		return sessionManager;
	}	
}
