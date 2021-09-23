package com.maenguin.notis.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"notionPageUri", "tistoryBlogName"})
public class NotisLinkForm {

    private String notionPageUri;

    private String tistoryBlogName;

    public NotisLinkForm(String notionPageUri, String tistoryBlogName) {
        this.notionPageUri = notionPageUri;
        this.tistoryBlogName = tistoryBlogName;
    }
}
