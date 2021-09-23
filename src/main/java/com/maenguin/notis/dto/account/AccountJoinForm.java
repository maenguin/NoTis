package com.maenguin.notis.dto.account;

import com.maenguin.notis.model.account.Account;
import lombok.Getter;

@Getter
public class AccountJoinForm {

    private String email;
    private String password;
    private String accessToken;

    public AccountJoinForm(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }

    public Account toEntity() {
        return Account.builder()
                .email(email)
                .password(password)
                .tistoryAccessToken(accessToken)
                .build();
    }
}
