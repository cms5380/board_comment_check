package me.minseok.board.Mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.minseok.board.domain.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardMapperTest {
    @Autowired
    BoardMapper boardMapper;

    @Test
    public void test(){
        Board board = new Board();
        board.setTitle("게시글 제목");
        board.setContent("게시글 내용");
        board.setWriter("테스터");

        int result = boardMapper.insertBoard(board);
        System.out.println("결과는 " + result + "입니다.");
    }

    @Test
    public void testOfSelectDetail() {
        Board board = boardMapper.selectBoardDetail((long) 4);
        try {
            String boardJson = new ObjectMapper().writeValueAsString(board);

            System.out.println("=========================");
            System.out.println(boardJson);
            System.out.println("=========================");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOfUpdate() {
        Board params = new Board();
        params.setTitle("1번 게시글 제목을 수정합니다.");
        params.setContent("1번 게시글 내용을 수정합니다.");
        params.setWriter("홍길동");
        params.setId((long) 1);

        int result = boardMapper.updateBoard(params);
        if (result == 1) {
            Board board = boardMapper.selectBoardDetail((long) 1);
            try {
                String boardJson = new ObjectMapper().writeValueAsString(board);

                System.out.println("=========================");
                System.out.println(boardJson);
                System.out.println("=========================");

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testOfDelete() {
        int result = boardMapper.deleteBoard((long) 1);
        if (result == 1) {
            Board board = boardMapper.selectBoardDetail((long) 1);
            try {
                String boardJson = new ObjectMapper().writeValueAsString(board);

                System.out.println("=========================");
                System.out.println(boardJson);
                System.out.println("=========================");

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void testMultipleInsert() {
        for (int i = 1; i <= 350; i++) {
            Board params = new Board();
            params.setTitle("게시글 제목");
            params.setContent("게시글 내용");
            params.setWriter("게시글 작성자");
            boardMapper.insertBoard(params);
        }
    }
    @Test
    public void testSelectList() {

    }
}