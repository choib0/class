package com.itwillbs.spring_mvc_board_cba.controller;


import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.itwillbs.spring_mvc_board_cba.service.*;
import com.itwillbs.spring_mvc_board_cba.vo.*;

@Controller
public class MemberController {

	@Autowired
	private MemberService service;
	
	@GetMapping(value="/MemberJoinForm.me")
	public String joinForm() {
		return "member/member_join_form";
	}
	
	@PostMapping(value="/MemberJoinPro.me")
	public String joinPro(MemberVO member, Model model) {
		
		// -------------- BCryptPasswordEncoder(암호화) ----------------
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String securePasswd = passwordEncoder.encode(member.getPasswd());
//		System.out.println("평문 : " + member.getPasswd());
//		System.out.println("암호문 : " + securePasswd);
		member.setPasswd(securePasswd);
		// -------------------------------------------------------------
		int insertCount = service.registMember(member);
		if(insertCount > 0) {
			return "redirect:/";
		} else {
			model.addAttribute("msg", "회원 가입 실패!");
			return "fail_back";
		}
		
	}
	
	@GetMapping(value="/MemberLoginForm.me")
	public String MemberLoginForm() {
		return "member/member_login_form";
	}
	
	@PostMapping(value="/MemberLoginPro.me")
	public String MemberLoginPro(MemberVO member, Model model, HttpSession session) {
		
		// BCryptPasswordEncoder 객체 이용한 로그인
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwd = service.getPasswd(member.getId());
//		System.out.println(passwd);
		if(passwd == null || !passwordEncoder.matches(member.getPasswd(), passwd)) {
			model.addAttribute("msg", "로그인 실패!");
			return "fail_back";
		} else {
			session.setAttribute("sId", member.getId());
			return "redirect:/";
		}
	}
	
	@GetMapping("/MemberLogout.me")
	public String MemberLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/MemberInfo.me")
	public String info(HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다");
			return "fail_back";
		}
		
		MemberVO member = service.getMemberInfo(id);
//		System.out.println(member.getEmail());
//		member.setEmail1(member.getEmail().split("@")[0]);
//		member.setEmail2(member.getEmail().split("@")[1]);
		
		model.addAttribute("member", member);
		
		return "member/member_info";
	}
	
	@PostMapping("/MemberUpdate.me")
	public String MemberUpdate(MemberVO member, @RequestParam String newPasswd, HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다");
			return "fail_back";
		}
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String passwd = service.getPasswd(id);
		if(passwd == null || !passwordEncoder.matches(member.getPasswd(), passwd)) {
			model.addAttribute("msg", "수정 권한 없음!");
			return "fail_back";
		}
		
		if(newPasswd.equals("")) {
			newPasswd = passwordEncoder.encode(newPasswd);
		}
		
		int updateCount = service.updateMemberInfo(member, newPasswd);
		if(updateCount > 0) {
			model.addAttribute("msg", "회원 정보 수정 성공");
			model.addAttribute("target", "MemberInfo.me");
			return "success";
		} else {
			model.addAttribute("msg", "회원 정보 수정 실패");
			return "fail_back";
		}
		
	}
	
	@GetMapping("/MemberQuitForm.me")
	public String quitForm() {
		return "member/member_quit_form";
	}
	
	// 회원 탈퇴를 위한 비즈니스 로직 수행
	@PostMapping("/MemberQuitPro.me")
	public String quitPro(@RequestParam String passwd, HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		
		String dbPasswd = service.getPasswd(id);

		System.out.println("평문 암호 : " + passwd + ", 해싱 암호 : " + dbPasswd);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(passwordEncoder.matches(passwd, dbPasswd)) {
			int deleteCount = service.quitMember(id);
			if(deleteCount > 0) {
				session.invalidate();
				
				model.addAttribute("msg", "탈퇴가 완료되었습니다!");
				model.addAttribute("target", "./");
				return "success";
			} else {
				model.addAttribute("msg", "탈퇴 실패!");
				return "fail_back";
			}
			
		} else {
			model.addAttribute("msg", "권한이 없습니다!");
			return "fail_back";
		}
		
	}
	
}





