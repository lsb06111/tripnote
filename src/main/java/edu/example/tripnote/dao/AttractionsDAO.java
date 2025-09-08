package edu.example.tripnote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.example.tripnote.domain.AttractionDTO;

@Mapper
public interface AttractionsDAO {

	public boolean insert(AttractionDTO dto);
	
	public List<AttractionDTO> getAttractionList(String tripDest);
}
