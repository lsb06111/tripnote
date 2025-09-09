package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.BoardVO;

@Mapper
public interface BoardDAO {
	public List<BoardDTO> listAll(BoardParamDTO boardParam);
	public int countListAll(BoardParamDTO boardParam);
}
