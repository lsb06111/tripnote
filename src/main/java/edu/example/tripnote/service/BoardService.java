package edu.example.tripnote.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.example.tripnote.dao.BoardDAO;
import edu.example.tripnote.domain.PageResponseDTO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.BoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardDAO boardDAO;
	
	public PageResponseDTO<BoardDTO> listAll(BoardParamDTO boardParam) {
		int page = boardParam.getPage();
		int size = boardParam.getSize();
		int offset;
		if (page>0) 
			offset =  (page-1) * size;
		else
			offset = 0;
		boardParam.setOffset(offset);
		int totalCount = boardDAO.countListAll(boardParam);
		List<BoardDTO> list = boardDAO.listAll(boardParam);
		
		int totalPages = (int)Math.ceil(((double)totalCount/size)); 
		boolean hasPrev = page>1 ? true:false;
		boolean hasNext = totalPages>page ? true:false;
		PageResponseDTO<BoardDTO> response = new PageResponseDTO<>(list,page,size, totalCount,totalPages,hasPrev, hasNext );
		return response;
	}

}
