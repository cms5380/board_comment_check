package me.minseok.board.Service;

import me.minseok.board.domain.Comment;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CommentService {
    public boolean registerComment(Comment comment);
    public boolean deleteComment(Long id);
    public List<Comment> getCommentList(Comment comment);
    public boolean getPredict(Comment comment);
}
