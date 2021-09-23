package com.maenguin.notis.controller;

import com.maenguin.notis.dto.NotisLinkForm;
import com.maenguin.notis.dto.account.AccountJoinForm;
import com.maenguin.notis.dto.account.AccountLoginForm;
import com.maenguin.notis.model.account.Account;
import com.maenguin.notis.service.AccountService;
import com.maenguin.notis.service.TistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/join")
    public String join(HttpServletRequest request, AccountJoinForm accountJoinForm) {
        var seq = accountService.join(accountJoinForm.toEntity());
        var session = request.getSession();
        session.setAttribute("accountEmail", accountJoinForm.getEmail());
        session.setAttribute("accessToken", accountJoinForm.getAccessToken());
        session.setAttribute("accountSeq", seq);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, AccountLoginForm accountLoginForm) {
        log.debug("login called.. {}", accountLoginForm);
        var accountSeq = accountService.login(accountLoginForm.getEmail(), accountLoginForm.getPassword());
        var account = accountService.getAccountBySeq(accountSeq);
        var session = request.getSession();
        session.setAttribute("accessToken", account.getTistoryAccessToken());
        session.setAttribute("accountSeq", account.getSeq());
        return "redirect:/notisItem";
    }

    @PostMapping("/account/{seq}/notisItem")
    public String linkNotionTistory(@PathVariable("seq") Long accountSeq, NotisLinkForm notisLinkForm) {
        log.debug("linkNotionTistory called.. seq={} {}",accountSeq, notisLinkForm);
        accountService.linkNotionTistory(notisLinkForm.getNotionPageUri(), notisLinkForm.getTistoryBlogName(), accountSeq);
        return "redirect:/notisItem";
    }
}


