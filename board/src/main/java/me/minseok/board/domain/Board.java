package me.minseok.board.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Board extends Common{
    /** 번호 (PK) */
    private Long id;

    /** 제목 */
    private String title;

    /** 내용 */
    private String content;

    /** 작성자 */
    private String writer;

    /** 조회 수 */
    private int viewCnt;

    /** 공지 여부 */
    private Boolean noticeYn;

    /** 비밀 여부 */
    private Boolean secretYn;
}
