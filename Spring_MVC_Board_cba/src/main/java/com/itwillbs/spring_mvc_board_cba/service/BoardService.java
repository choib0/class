package com.itwillbs.spring_mvc_board_cba.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.itwillbs.spring_mvc_board_cba.mapper.*;
import com.itwillbs.spring_mvc_board_cba.vo.*;

@Service
public class BoardService {

	@Autowired
	private BoardMapper mapper;

	public int registBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}
	
}
