package me.minseok.board.Service;

import java.util.Collections;
import java.util.List;

import me.minseok.board.Mapper.BoardMapper;
import me.minseok.board.domain.Board;
import me.minseok.board.paging.Criteria;
import me.minseok.board.paging.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	@Override
	public boolean registerBoard(Board params) {
		int queryResult = 0;

		if (params.getId() == null) {
			queryResult = boardMapper.insertBoard(params);
		} else {
			queryResult = boardMapper.updateBoard(params);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public Board getBoardDetail(Long id) {
		Board board = boardMapper.selectBoardDetail(id);
		int cnt = board.getViewCnt();
		board.setViewCnt(cnt+1);

		boardMapper.viewCntPlus(board);

		return board;
	}

	@Override
	public boolean deleteBoard(Long id) {
		int queryResult = 0;

		Board board = boardMapper.selectBoardDetail(id);

		if (board != null && board.getDeleteYn()==false) {
			queryResult = boardMapper.deleteBoard(id);
		}

		return (queryResult == 1) ? true : false;
	}

	@Override
	public List<Board> getBoardList(Board params) {
		List<Board> boardList = Collections.emptyList();

		int boardTotalCount = boardMapper.selectBoardTotalCount(params);

		PaginationInfo paginationInfo = new PaginationInfo(params);
		paginationInfo.setTotalRecordCount(boardTotalCount);

		params.setPaginationInfo(paginationInfo);

		if (boardTotalCount > 0) {
			boardList = boardMapper.selectBoardList(params);
		}

		return boardList;
	}

}
