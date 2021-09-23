package com.maenguin.notis.controller;

import com.maenguin.notis.dto.tistory.TistoryBlogDto;
import com.maenguin.notis.model.notion.SeleniumNotionPageExtractor;
import com.maenguin.notis.model.account.Sha256Converter;
import com.maenguin.notis.model.tistory.TistoryPost;
import com.maenguin.notis.repository.NotisItemRepository;
import com.maenguin.notis.service.TistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final TistoryService tistoryService;
    private final NotisItemRepository notisItemRepository;

    @GetMapping("")
    public String homePage() {
        return "home";
    }

    @GetMapping("/notisItem")
    public String notisItemPage(HttpServletRequest request, Model model) {
        var session = request.getSession();
        var accountSeq = (Long) session.getAttribute("accountSeq");
        var accessToken = (String) session.getAttribute("accessToken");
        log.debug("notisItemPage called.. session [accountSeq={}, accessToken={}]", accountSeq, accessToken);
        if (accountSeq == null || accessToken == null) {
            return "redirect:/login";
        }
        var notisItems = notisItemRepository.findByAccountSeq(accountSeq);
        model.addAttribute("accountSeq", accountSeq);
        model.addAttribute("notisItems", notisItems);
        var blogInfoResponse = tistoryService.getBlogInfo(accessToken);
        if (blogInfoResponse.getTistory().getStatus() == 200) {
            var blogNames = blogInfoResponse.getTistory().getItem().getBlogs().stream().map(TistoryBlogDto::getName).collect(Collectors.toList());
            model.addAttribute("blogNames", blogNames);
        }
        return "notisItemList";
    }



}
