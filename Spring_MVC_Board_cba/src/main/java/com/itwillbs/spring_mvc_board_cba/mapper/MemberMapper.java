package com.itwillbs.spring_mvc_board_cba.mapper;

import org.apache.ibatis.annotations.*;

import com.itwillbs.spring_mvc_board_cba.vo.*;

public interface MemberMapper {

	int insertMember(MemberVO member);

	String selectPasswd(String id);

	MemberVO selectMemberInfo(String id);

//	int updateMemberInfo(MemberVO member, String newPasswd);	// 오류발생
	int updateMemberInfo(@Param("member") MemberVO member, @Param("newPasswd") String newPasswd);
	
	// 회원 탈퇴
	int deleteMember(String id);
	

}
