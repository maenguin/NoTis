package com.maenguin.notis.dto.tistory;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"clientId", "clientSecret", "rediectUri", "code"})
public class TistoryGetAccessTokenDto {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String code;

    public TistoryGetAccessTokenDto(String clientId, String clientSecret, String redirectUri, String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.code = code;
    }
}
