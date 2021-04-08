package me.minseok.board.Mapper;

import java.util.List;
import java.util.Map;

import me.minseok.board.domain.Board;
import me.minseok.board.paging.Criteria;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BoardMapper {

	public int insertBoard(Board params);

	public Board selectBoardDetail(Long id);

	public int updateBoard(Board params);

	public int viewCntPlus(Board params);

	public int deleteBoard(Long id);

	public List<Board> selectBoardList(Board board);

	public int selectBoardTotalCount(Board board);

}