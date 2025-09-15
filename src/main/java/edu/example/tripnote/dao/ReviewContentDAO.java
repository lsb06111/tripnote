package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.ReviewContentDTO;

@Mapper
public interface ReviewContentDAO {
	public void save(List<ReviewContentDTO> contentDTO);
}
