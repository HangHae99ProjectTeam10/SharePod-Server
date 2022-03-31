package com.spring.sharepod.v1.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class SearchForm {
    /**
     * 카테고리
     */
    @Length(max = 100)
    private String category;
    /**
     * 글 작성 위치
     */
    @Length(max = 100)
    private String boardRegion;

    private Long startNum;

    private String searchTitle;
}
