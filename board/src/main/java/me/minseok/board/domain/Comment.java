package me.minseok.board.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private Long boardId;
    private String content;
    private String writer;
    private Boolean deleteYn;
    private Boolean cleanCommentYn;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
