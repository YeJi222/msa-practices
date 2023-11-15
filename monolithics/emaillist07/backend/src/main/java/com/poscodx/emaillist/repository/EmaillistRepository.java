package com.poscodx.emaillist.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscodx.emaillist.vo.EmaillistVo;

@Repository
public class EmaillistRepository {
	@Autowired
	private SqlSession sqlSession;

	public List<EmaillistVo> findAll(String keyword) {
		System.out.println("repository: " + keyword);
		System.out.println(sqlSession.selectList("emaillist.findAll", keyword));
		return sqlSession.selectList("emaillist.findAll", keyword);
	}

	public Long insert(EmaillistVo emaillistVo) {
		sqlSession.insert("emaillist.insert", emaillistVo);
		return emaillistVo.getNo();
	}
	
}
