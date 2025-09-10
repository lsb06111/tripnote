package edu.example.tripnote.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.example.tripnote.Constants;
import edu.example.tripnote.dao.BoardDAO;
import edu.example.tripnote.domain.PageResponseDTO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static edu.example.tripnote.Constants.BOARD_Block_SIZE;
import static edu.example.tripnote.Constants.BOARD_PAGE_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardDAO boardDAO;
	
	public PageResponseDTO<BoardDTO> listAll(BoardParamDTO boardParam) {
		int page = boardParam.getPage();
		int size = BOARD_PAGE_SIZE;
		int offset;
		if (page>0) 
			offset =  (page-1) * size;
		else
			offset = 0;
		boardParam.setOffset(offset);
		int totalCount = boardDAO.countListAll(boardParam);
		List<BoardDTO> list = boardDAO.listAll(boardParam);
		
		int totalPages = (int)Math.ceil(((double)totalCount/size)); 
		int totalBlocks = (int)Math.ceil((double)totalPages/BOARD_Block_SIZE);
		int block = (int)Math.ceil((double)page/BOARD_Block_SIZE);
		if (totalBlocks <0) totalBlocks = 0;
		int blockStart = (block-1)*BOARD_Block_SIZE + 1;
		int blockEnd = block < totalBlocks ? (block*BOARD_Block_SIZE) : (block*BOARD_Block_SIZE -  totalPages % BOARD_Block_SIZE);
		boolean hasPrev = block>1 ? true:false;
		boolean hasNext = totalBlocks>block ? true:false;
		PageResponseDTO<BoardDTO> response = new PageResponseDTO<>(list,page,totalCount,totalPages,hasPrev, hasNext,blockStart, blockEnd);
		
		log.debug("BoardService listAll- totalblock : " + totalBlocks);
		log.debug("BoardService listAll- block : " + block);
		log.debug("BoardService listAll- start : " + blockStart);
		log.debug("BoardService listAll- end : " + blockEnd);
		return response;
	}

}
