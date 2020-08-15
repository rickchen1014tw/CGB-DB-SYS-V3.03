package com.db.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.sys.entity.SysLog;

/**
 * 定義用戶行為日誌接口
 */
public interface SysLogDao {
	/**
	 * 基於查詢條件統計總記錄數
	 * @param username 查詢條件
	 * @return 總記錄數
	 */
	/*
	 * 當方法參數只有一個參數，要是沒有用在動態sql中可以不加@Param註解，但是現在我們有用在動態sql中
	 * <if test="username!=null...，所以要加上註解。
	 * 如果不加會出現錯誤org.mybatis.spring.MyBatisSystemException: nested exception is org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'username' in 'class java.lang.String'
	 * 我們不可能在String類裡加get方法，所以要加上註解把他放到map裡，讓系統由get(key)去找value。
	 */
	int getRowCount(@Param("username")String username);
	/**
	 * 基於條件查詢當前頁要呈現的日誌信息
	 * @param username 用戶名
	 * @param startIndex 當前面的起始位置
	 * @param pageSize 頁面大小(每頁最多顯示多少行記錄)
	 * @return 當前頁記錄
	 */
	List<SysLog> findPageObjects(
			@Param("username")String username, 
			@Param("startIndex")Integer startIndex, 
			@Param("pageSize")Integer pageSize);
	
	
	/**
	 * 基於id刪除日誌信息
	 */
	int deleteObjects(@Param("ids")Integer... ids);	//系統底層會把ids當作k，傳過來的值當作value，存儲到map
/*
 * 在這個接口裡面(數據層)是否允許名字相同參數不同的方法(overloading)?
 * 不行，這是MyBatis的限制，因為映射文件裡的id是方法名，而id不能重覆，
 * 所以dao這一層沒辦法寫overloading的方法
 */

	/**
	 * 將日誌信息寫入到數據庫
	 * @param entity
	 * @return
	 */
	int insertObject(SysLog entity);
}

