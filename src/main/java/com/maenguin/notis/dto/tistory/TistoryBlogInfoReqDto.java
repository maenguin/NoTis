package com.maenguin.notis.dto.tistory;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString(of = {"id", "userId", "blogs"})
public class TistoryBlogInfoReqDto {

    private String id;
    private String userId;
    private List<TistoryBlogDto> blogs;

    public TistoryBlogInfoReqDto(String id, String userId, List<TistoryBlogDto> blogs) {
        this.id = id;
        this.userId = userId;
        this.blogs = blogs;
    }
}
