package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.BoardSaveReqDTO;
import edu.example.tripnote.domain.board.BoardVO;
import edu.example.tripnote.domain.board.NewBoardDTO;

@Mapper
public interface BoardDAO {
	public List<BoardDTO> listAll(BoardParamDTO boardParam);
	public int countListAll(BoardParamDTO boardParam);
	public boolean save(NewBoardDTO boardDTO);
	public boolean saveDraft(NewBoardDTO boardDTO);
	public BoardDTO getBoardById(int id);
}
