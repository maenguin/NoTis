package com.maenguin.notis.dto.tistory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"name", "url", "title"})
public class TistoryBlogDto {

    private String name;
    private String url;
    private String title;

    public TistoryBlogDto(String name, String url, String title) {
        this.name = name;
        this.url = url;
        this.title = title;
    }
}
