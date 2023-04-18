package com.itwillbs.spring_mvc_board_cba.controller;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import com.itwillbs.spring_mvc_board_cba.service.*;
import com.itwillbs.spring_mvc_board_cba.vo.*;

@Controller
public class BoardController {

	@Autowired
	private BoardService service;
	
	// 글쓰기 폼
	@GetMapping("/BoardWriteForm.bo")
	public String writeForm(HttpSession session, Model model) {
		if(session.getAttribute("sId") == null) {
			model.addAttribute("msg", "로그인 필수!");
			model.addAttribute("target", "MemberLoginForm.me");
			return "success";
		}
		return "board/board_write_form";
	}
	
	// 파일 업로드 기능이 포함된 폼 파라미터 처리할 경우
	
	// 1.
//	@PostMapping("/BoardWritePro.bo")
//	public String writePro(
//			String board_name, String board_subject, String board_content, MultipartFile file) {
//		System.out.println(board_name + ", " + board_subject + ", " + board_content);
//		System.out.println("업로드 파일명 : " + file.getOriginalFilename());
//		return "";
//	}
	
	// 2.
//	@PostMapping("/BoardWritePro.bo")
//	public String writePro(
//			@RequestParam Map<String, String> map, MultipartFile file) {
//		System.out.println(map.get("board_name") + ", " + map.get("board_subject") + ", " + map.get("board_content"));
//		System.out.println("업로드 파일명 : " + file.getOriginalFilename());
//		return "";
//	}
	
	// 3.
	@PostMapping("/BoardWritePro.bo")
	public String writePro(BoardVO board, HttpServletRequest request, HttpSession session, Model model) {
//		System.out.println(board);
//		System.out.println("업로드 파일명 : " + board.getFile().getOriginalFilename());
		String uploadDir = "/resources/upload";		// 프로젝트상의 업로드 경로
//		String saveDir = request.getServletContext().getRealPath(uploadDir);
		String saveDir = session.getServletContext().getRealPath(uploadDir);
//		System.out.println("실제 업로드 경로 : " + saveDir);
		try {
			// --------------------------------------------------------------
			// 업로드 디렉토리를 날짜별 디렉토리로 분류하기
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			board.setBoard_file_path("/" + sdf.format(date));
			saveDir = saveDir + board.getBoard_file_path();	// 기본 업로드 경로 + 서브 디렉토리 경로
			// --------------------------------------------------------------
			Path path = Paths.get(saveDir);
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// BoardVO 객체에 전달된 MultipartFile 객체 꺼내기
		MultipartFile mFile = board.getFile();	// 단일 파일
//		MultipartFile[] mFiles = board.getFiles();	// 복수 파일
		
		String originalFileName = mFile.getOriginalFilename();
		System.out.println("원본 파일 : " + originalFileName);
		
		// 파일명 중복 방지
		String uuid = UUID.randomUUID().toString();
//		System.out.println(uuid);
//		originalFileName = UUID.randomUUID().toString() + "_" + originalFileName;
		// 결론
//		board.setBoard_file(UUID.randomUUID().toString() + "_" + originalFileName);
		// 파일명 길이조절
		board.setBoard_file(uuid.substring(0, 8) + "_" + originalFileName);
		System.out.println("실제 업로드 될 파일명 : " + originalFileName);
		
		// 게시물 등록 작업 요청
		int insertCount = service.registBoard(board);
		
		if(insertCount > 0) {
			try {
				mFile.transferTo(new File(saveDir, board.getBoard_file()));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			return "redirect:/BoardList.bo";
		} else {
			model.addAttribute("msg", "글 쓰기 실패!");
			return "fail_back";
		}
	}
}
