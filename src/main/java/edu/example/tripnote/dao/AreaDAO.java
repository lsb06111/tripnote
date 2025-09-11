package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.board.AreaDTO;

@Mapper
public interface AreaDAO {
	public List<AreaDTO> listParentLoc();
}
