package com.maenguin.notis.model.notion;

import com.maenguin.notis.model.tistory.TistoryPost;
import lombok.Getter;

@Getter
public class NotionPage {
    private String title;
    private String content;
    private String category;

    public NotionPage(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
