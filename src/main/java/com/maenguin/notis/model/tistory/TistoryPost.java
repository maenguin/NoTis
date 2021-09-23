package com.maenguin.notis.model.tistory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"blogName", "title", "content"})
public class TistoryPost {

    private String blogName;
    private String title;
    private String content;

    public TistoryPost(String blogName, String title, String content) {
        this.blogName = blogName;
        this.title = title;
        this.content = content;
    }
}
