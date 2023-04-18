package com.itwillbs.spring_mvc_board_cba.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.itwillbs.spring_mvc_board_cba.mapper.*;
import com.itwillbs.spring_mvc_board_cba.vo.*;

@Service
public class MemberService {

	@Autowired
	private MemberMapper mapper;

	public int registMember(MemberVO member) {
		
		return mapper.insertMember(member);
	}

	// 패스워드 조회 비즈니스 로직 수행할 getPasswd() 메서드 정의
	public String getPasswd(String id) {
		return mapper.selectPasswd(id);
	}

	public MemberVO getMemberInfo(String id) {
		return mapper.selectMemberInfo(id);
	}

	public int updateMemberInfo(MemberVO member, String newPasswd) {
		return mapper.updateMemberInfo(member, newPasswd);
	}

	public int quitMember(String id) {
		return mapper.deleteMember(id);
	}

	
}
