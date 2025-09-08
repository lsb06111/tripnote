package edu.example.tripnote.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.example.tripnote.dao.BoardDAO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardDAO boardDAO;
	
	public List<BoardDTO> listAll() {
		return boardDAO.listAll();
	}

}
