package me.minseok.board.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.minseok.board.Mapper.CommentMapper;
import me.minseok.board.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public Map<String, Long> registerComment(Comment comment){
        Map<String, Long> ret = new HashMap<>();

        if(comment.getId() == null){
            ret.put("isRegistered", (long)commentMapper.insertComment(comment));
        }else{
            ret.put("isRegistered", (long)commentMapper.updateComment(comment));
        }

        ret.put("id", commentMapper.selectCommentId(comment));
        return ret;
    }

    @Override
    public boolean deleteComment(Long id) {
        int ret = 0;
        Comment comment = commentMapper.selectCommentDetail(id);
        if(comment != null && comment.getDeleteYn() == false){
            ret = commentMapper.deleteComment(id);
        }

        return ret == 1 ? true : false;
    }

    @Override
    public List<Comment> getCommentList(Comment comment) {
        List<Comment> list = Collections.emptyList();

        int commentTotalCount = commentMapper.selectCommentTotalCount(comment);

        if (commentTotalCount > 0) {
            list = commentMapper.selectCommentList(comment);
        }


        return list;
    }
}
