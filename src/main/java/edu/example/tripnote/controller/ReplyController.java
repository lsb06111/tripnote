package edu.example.tripnote.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.example.tripnote.domain.board.AllReplyResDTO;
import edu.example.tripnote.domain.board.ReplyDTO;
import edu.example.tripnote.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/reply")
@RequiredArgsConstructor
@Controller
public class ReplyController {
	private final ReplyService replyService;
	
	@PostMapping("/save")
	@ResponseBody
	public void save(@RequestBody ReplyDTO dto) {
		replyService.save(dto);
	}
	
	@GetMapping("/list")
	@ResponseBody
	public AllReplyResDTO listAll(int id) {
		return replyService.getReplysByBoardId(id);
	}
}
