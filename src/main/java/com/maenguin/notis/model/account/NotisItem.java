package com.maenguin.notis.model.account;

import com.maenguin.notis.model.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NotisItem extends BaseEntity {

    @Id
    @GeneratedValue
    private Long seq;

    private String notionPageUri;

    private String tistoryBlogName;

    private String tistoryPostId;

    @Embedded
    private Hash notionPageHash;

    private boolean useAutoUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_seq")
    private Account account;

    @Builder
    public NotisItem(String notionPageUri, String tistoryBlogName, String tistoryPostId, Hash notionPageHash, boolean useAutoUpdate, Account account) {
        this.notionPageUri = notionPageUri;
        this.tistoryBlogName = tistoryBlogName;
        this.tistoryPostId = tistoryPostId;
        this.notionPageHash = notionPageHash;
        this.useAutoUpdate = useAutoUpdate;
        this.account = account;
    }
}
