package me.minseok.board.Service;

import me.minseok.board.domain.Comment;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentServiceImplTest {
    @Autowired
    CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registerComment() {
        int number = 10;

        final Logger logger = LoggerFactory.getLogger(this.getClass());

        Comment params = new Comment();
        params.setBoardId((long) 1); // 댓글을 추가할 게시글 번호
        params.setContent("1번 게시글");
        params.setWriter(1 + "번 회원");
        commentService.registerComment(params);


    }

    @Test
    public void deleteComment() {
        commentService.deleteComment((long) 1); // 삭제할 댓글 번호
    }

    @Test
    public void getCommentList() {
        Comment params = new Comment();
        params.setBoardId((long) 50); // 댓글을 추가할 게시글 번호

        List<Comment> list = commentService.getCommentList(params);
        System.out.println(list.toString());
    }

    @Test
    public void getPredict() throws Exception {
    }
}