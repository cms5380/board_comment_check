package me.minseok.board.Mapper;

import java.util.List;
import java.util.Map;

import me.minseok.board.domain.Comment;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CommentMapper {

    public int insertComment(Comment params);
    public Comment selectCommentDetail(Long id);
    public int updateComment(Comment params);
    public int updateCommentClean(Comment params);
    public int deleteComment(Long id);
    public long selectCommentId(Comment params);
    public List<Comment> selectCommentList(Comment params);
    public int selectCommentTotalCount(Comment params);

}