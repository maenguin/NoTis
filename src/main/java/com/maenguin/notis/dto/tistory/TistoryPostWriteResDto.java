package com.maenguin.notis.dto.tistory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"status", "postId", "url"})
public class TistoryPostWriteResDto {

    private int status;
    private String postId;
    private String url;

    public TistoryPostWriteResDto(int status, String postId, String url) {
        this.status = status;
        this.postId = postId;
        this.url = url;
    }
}
