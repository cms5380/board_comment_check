package me.minseok.board.domain;

import lombok.Data;
import me.minseok.board.paging.Criteria;
import me.minseok.board.paging.PaginationInfo;

import java.time.LocalDateTime;

@Data
public class Common extends Criteria {

    /** 페이징 정보 */
    private PaginationInfo paginationInfo;

    /** 삭제 여부 */
    private Boolean deleteYn;

    /** 등록일 */
    private LocalDateTime insertTime;

    /** 수정일 */
    private LocalDateTime updateTime;

    /** 삭제일 */
    private LocalDateTime deleteTime;


}
