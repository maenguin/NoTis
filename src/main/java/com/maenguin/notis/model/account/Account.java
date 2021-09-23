package com.maenguin.notis.model.account;

import com.maenguin.notis.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    private String email;

    private String password;

    private String tistoryAccessToken;

    private boolean tokenExpired;

    @OneToMany(mappedBy = "account")
    private List<NotisItem> notisItems = new ArrayList<>();

    @Builder
    public Account(String email, String password, String tistoryAccessToken, boolean tokenExpired, List<NotisItem> notisItems) {
        this.email = email;
        this.password = password;
        this.tistoryAccessToken = tistoryAccessToken;
        this.tokenExpired = tokenExpired;
        this.notisItems = notisItems;
    }
}
