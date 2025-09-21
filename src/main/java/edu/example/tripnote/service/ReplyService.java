package edu.example.tripnote.service;

import org.springframework.stereotype.Service;

import edu.example.tripnote.dao.ReplyDAO;
import edu.example.tripnote.domain.board.ReplyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReplyService {
	private final ReplyDAO replyDAO;
	public boolean save(ReplyDTO dto) {
		replyDAO.save(dto);
		return true;
	}
}
