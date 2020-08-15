package com.db.sys.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.sys.dao.SysLogDao;
import com.db.sys.entity.SysLog;

/*
 * 可以在xml中配置讓系統自動生成這個實現類，在實際應用當中，除了系統掃描自動生成實現類，
 * 我們也可能自己寫，因為我們可能想個性化，例如加上 異常處理，處理日誌輸出...
 */
@Repository	 //沒有設定id，默認是defaultSysLogDao
public class DefaultSysLogDao implements SysLogDao {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	/* 思考，這裡能不能直接注入一個SqlSession對象? 答案是不可以，
	 * 因為SqlSession對象是一個線程非安全的對象，不能共享
	 */
	
	@Override
	public int deleteObjects(Integer... ids) {
		//1.獲取SqlSession對象
		SqlSession session = sqlSessionFactory.openSession();
		//2.執行刪除操作
		SysLogDao dao = session.getMapper(SysLogDao.class);
		int rows = dao.deleteObjects(ids);
		session.commit();
		//3.釋放資源
		session.close();
		return rows;
	}
	@Override
	public int getRowCount(String username) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<SysLog> findPageObjects(String username, Integer startIndex, Integer pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int insertObject(SysLog entity) {
		// TODO Auto-generated method stub
		return 0;
	}

}
