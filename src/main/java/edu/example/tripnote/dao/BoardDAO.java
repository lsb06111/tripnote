package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.vo.BoardVO;

@Mapper
public interface BoardDAO {
	public List<BoardVO> listAll();
}
