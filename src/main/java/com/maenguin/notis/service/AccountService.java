package com.maenguin.notis.service;

import com.maenguin.notis.model.account.Account;
import com.maenguin.notis.model.account.Hash;
import com.maenguin.notis.model.account.NotisItem;
import com.maenguin.notis.model.notion.NotionPageExtractor;
import com.maenguin.notis.model.tistory.TistoryPost;
import com.maenguin.notis.repository.AccountRepository;
import com.maenguin.notis.repository.NotisItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final NotisItemRepository notisItemRepository;
    private final NotionPageExtractor notionPageExtractor;
    private final TistoryService tistoryService;


    @Transactional
    public Long join(Account account) {
        validateDuplicateAccount(account);
        accountRepository.save(account);
        return account.getSeq();
    }

    public Long login(String email, String password) {
        var findAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("아이디나 패스워드가 일치하지 않습니다."));
        if (!findAccount.getPassword().equals(password)) {
            throw  new IllegalArgumentException("아이디나 패스워드가 일치하지 않습니다.");
        }
        return findAccount.getSeq();
    }

    public Account getAccountBySeq(Long accountSeq) {
        return accountRepository.findById(accountSeq)
                .orElseThrow(() -> new RuntimeException("계정을 찾을 수 없습니다."));
    }

    @Transactional
    public boolean linkNotionTistory(String notionPageUri, String tistoryBlogName, Long accountSeq) {
        log.debug("linkNotionTistory called.. notionPageUri={}, tistoryBlogName={}, accountSeq={}", notionPageUri, tistoryBlogName, accountSeq);
        var notionPage = notionPageExtractor.extractPage(notionPageUri);
        var tistoryPost = new TistoryPost(tistoryBlogName, notionPage.getTitle(), notionPage.getContent());
        var response = tistoryService.writePost(tistoryPost);
        var tistoryResult = response.getTistory();
        if (tistoryResult.getStatus() == 200) {
            var account = getAccountBySeq(accountSeq);
            var notisItem = NotisItem.builder()
                    .notionPageUri(notionPageUri)
                    .tistoryBlogName(tistoryBlogName)
                    .tistoryPostId(tistoryResult.getPostId())
                    .notionPageHash(new Hash(notionPage.getContent()))
                    .account(account)
                    .build();
            notisItemRepository.save(notisItem);
        }
        return true;
    }


    private void validateDuplicateAccount(Account account) {
        var findMembers = accountRepository.findByEmail(account.getEmail());
        if (findMembers.isPresent()) {
            throw new IllegalStateException("이미 존재하는 계정입니다.");
        }
    }
}
