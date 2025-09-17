package edu.example.tripnote.service;

import static edu.example.tripnote.Constants.BOARD_BlOCK_SIZE;
import static edu.example.tripnote.Constants.BOARD_PAGE_SIZE;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.example.tripnote.dao.BoardDAO;
import edu.example.tripnote.dao.ReplyDAO;
import edu.example.tripnote.dao.ReviewContentDAO;
import edu.example.tripnote.domain.board.BoardDTO;
import edu.example.tripnote.domain.board.BoardDetailResDTO;
import edu.example.tripnote.domain.board.BoardParamDTO;
import edu.example.tripnote.domain.board.BoardSaveReqDTO;
import edu.example.tripnote.domain.board.NewBoardDTO;
import edu.example.tripnote.domain.board.PageResponseDTO;
import edu.example.tripnote.domain.board.ReplyDTO;
import edu.example.tripnote.domain.board.ReviewContentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardDAO boardDAO;
	private final ReviewContentDAO reviewContentDAO;
	private final ReplyDAO replyDAO;

	public PageResponseDTO<BoardDTO> listAll(BoardParamDTO boardParam) {
		int page = boardParam.getPage();
		int offset;
		offset = page > 0 ? (page - 1) * BOARD_PAGE_SIZE : 0;
		boardParam.setOffset(offset);
		int totalCount = boardDAO.countListAll(boardParam);
		log.debug("totalCount : " + totalCount);

		List<BoardDTO> list = boardDAO.listAll(boardParam);

		int totalPages = (int) Math.ceil(((double) totalCount / BOARD_PAGE_SIZE));
		int totalBlocks = (int) Math.ceil((double) totalPages / BOARD_BlOCK_SIZE);
		int block = (int) Math.ceil((double) page / BOARD_BlOCK_SIZE);
		if (totalBlocks < 0)
			totalBlocks = 0;
		int blockStart = (block - 1) * BOARD_BlOCK_SIZE + 1;
		int blockEnd = block < totalBlocks ? block * BOARD_BlOCK_SIZE
				: (totalBlocks - 1) * BOARD_BlOCK_SIZE + totalPages % BOARD_BlOCK_SIZE;
		boolean hasPrev = block > 1 ? true : false;
		boolean hasNext = totalBlocks > block ? true : false;
		PageResponseDTO<BoardDTO> response = new PageResponseDTO<>(list, page, totalCount, totalPages, hasPrev, hasNext,
				blockStart, blockEnd);

		log.debug("BoardService listAll- totalPages : " + totalPages);
		log.debug("BoardService listAll- totalblock : " + totalBlocks);
		log.debug("BoardService listAll- block : " + block);
		log.debug("BoardService listAll- blockStart : " + blockStart);
		log.debug("BoardService listAll- blockEnd : " + blockEnd);
		log.debug("BoardService listAll- hasPrev : " + hasPrev);
		log.debug("BoardService listAll- hasNext : " + hasNext);
		return response;
	}

	public boolean save(BoardSaveReqDTO req) {
		NewBoardDTO boardDTO = new NewBoardDTO();
		boardDTO.setIntro(req.getIntro());
		boardDTO.setCourseId(req.getCourseId());
		boardDTO.setTitle(req.getTitle());
		boardDTO.setUserId(req.getUserId());

		List<ReviewContentDTO> reviewContents = req.getContents();
		boolean result1 = boardDAO.save(boardDTO);
		for (ReviewContentDTO contentDTO : reviewContents) {
			contentDTO.setBoardId(boardDTO.getId());
			MultipartFile file = contentDTO.getFile();
			if (!file.isEmpty()) {
				String savedPath = saveFileToFolder(file);
				contentDTO.setImgSrc(savedPath);
			}
		}

		boolean result2 = reviewContentDAO.save(reviewContents);
		log.debug("result2 : " + result2);
		return result1 & result2;
	}

	// 파일 네이밍 전략 추후 보충 , db 접근 경로 보충
	private String saveFileToFolder(MultipartFile file) {
		String path = "c:/tripnote_resource/board_images/";
		File isDir = new File(path);
		if (!isDir.isDirectory()) { // 폴더 없으면 생성
			isDir.mkdirs();
		}
		String filename = file.getOriginalFilename();
		File dest = new File(path + filename);
		try {
			file.transferTo(dest);
		} catch (IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}
		return "/db접근url/" + filename; // DB에 저장할 접근 경로
	}

	public boolean saveDraft(BoardSaveReqDTO req) {
		// return boardDAO.saveDraft(req.getBoardDTO()) &
		// reviewContentDAO.saveDraft(req.getContents());
		return true;
	}

	public BoardDTO getBoardById(int id) {
		return boardDAO.getBoardById(id);
	}

	public BoardDetailResDTO getBoardDetailById(int id) {
		BoardDetailResDTO respond = new BoardDetailResDTO();
		BoardDTO boardDTO = getBoardById(id);
		List<ReviewContentDTO> contents = reviewContentDAO.getContentsByBoardId(id);
		log.info("게시물 내용들 : " + contents.toString());
		//List<ReplyDTO> replyList = replyDAO.getReplysByBoardId(id);
		log.info("댓글리스트");
		return respond;
	}
}
