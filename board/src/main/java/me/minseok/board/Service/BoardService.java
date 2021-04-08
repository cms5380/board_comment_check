package me.minseok.board.Service;

import java.util.List;

import me.minseok.board.domain.Board;
import me.minseok.board.paging.Criteria;

public interface BoardService {

	public boolean registerBoard(Board params);

	public Board getBoardDetail(Long id);

	public boolean deleteBoard(Long id);

	public List<Board> getBoardList(Board board);

}
