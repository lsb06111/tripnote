package edu.example.tripnote.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.example.tripnote.dao.AreaDAO;
import edu.example.tripnote.domain.board.AreaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AreaService {
	private final AreaDAO areaDAO;
	
	public List<AreaDTO> listParentLoc(){
		return areaDAO.listParentLoc();
	}
}
