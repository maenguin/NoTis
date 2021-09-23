package com.maenguin.notis.dto.tistory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"clientId", "clientSecret", "redirectUri"})
public class TistoryAuthorizeDto {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public TistoryAuthorizeDto(String clientId, String clientSecret, String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}
