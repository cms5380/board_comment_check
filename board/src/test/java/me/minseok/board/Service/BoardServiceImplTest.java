package me.minseok.board.Service;

import me.minseok.board.Mapper.BoardMapper;
import me.minseok.board.domain.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {
    @Autowired
    private BoardMapper boardMapper;

    @Test
    void registerBoard() {
    }

    @Test
    void getBoardDetail() {
    }

    @Test
    void deleteBoard() {
        int queryResult = 0;
        Board board = boardMapper.selectBoardDetail((long) 2);
        System.out.println(board.toString());
        if (board != null && board.getDeleteYn() == false) {
            queryResult = boardMapper.deleteBoard((long) 2);
        }
//        assertEquals(queryResult, 1);
        System.out.println((queryResult == 1) ? true : false);
    }

    @Test
    void getBoardList() {
    }
}