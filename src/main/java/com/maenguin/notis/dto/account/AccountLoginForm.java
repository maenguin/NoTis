package com.maenguin.notis.dto.account;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"email", "password"})
public class AccountLoginForm {

    private String email;
    private String password;

    public AccountLoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
