package com.maenguin.notis.controller;

import com.maenguin.notis.dto.tistory.TistoryReponse;
import com.maenguin.notis.dto.tistory.TistoryBlogInfoReqDto;
import com.maenguin.notis.model.tistory.TistoryPost;
import com.maenguin.notis.service.TistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("tistory")
@RequiredArgsConstructor
@Slf4j
public class TistoryController {

    private final TistoryService tistoryService;

    @GetMapping("/authorize")
    @ResponseBody
    public String tistoryAuthorize() {
        log.debug("tistoryAuthorize called");
        return tistoryService.authorize();
    }

    @GetMapping("/access_token")
    public String tistoryAccessToken(Optional<String> code, Optional<String> state, Model model) {
        log.debug("tistoryAccessToken called with [ code = {}, state = {}]", code, state);
        if (code.isPresent()) {
            var accessToken = tistoryService.getAccessToken(code.get());
            var blogInfo = tistoryService.getBlogInfo(accessToken);
            var tistory = blogInfo.getTistory();
            if (tistory.getStatus() == 200) {
                model.addAttribute("accessToken", accessToken);
                model.addAttribute("accountEmail", tistory.getItem().getId());
            }
            return "join";
        }
        return "home";
    }

//    @GetMapping("/blog/info")
//    @ResponseBody
//    public TistoryReponse<TistoryBlogInfoReqDto> blogInfo() {
//        return tistoryService.getBlogInfo("3cbfb7193a4313d87f609e931d26ccec_93bfb603fd47d01bbddaf211513d2cdb");
//    }

    @GetMapping("/post/list")
    @ResponseBody
    public String postList(@RequestParam Optional<String> blogName, @RequestParam Optional<Integer> page) {
        return tistoryService.getPostList(blogName.orElseThrow(), page.orElse(1));
    }

    @GetMapping("/post/read")
    @ResponseBody
    public String postDetail(@RequestParam Optional<String> blogName, @RequestParam Optional<String> postId) {
        return tistoryService.getPostDetail(blogName.orElseThrow(), postId.orElseThrow());
    }

//    @GetMapping("/post/write")
//    @ResponseBody
//    public String writePost(@RequestParam String blogName, @RequestParam String title, @RequestParam Optional<String> content) {
//        var result = tistoryService.writePost(blogName, title, content.orElse(""));
//        log.info("result -> {}", result);
//        return result;
//    }

    @GetMapping("/post/write")
    public String writePostPage() {
        return "tistory/writePost";
    }

    @PostMapping("/post/write")
    @ResponseBody
    public String writePost(TistoryPost tistoryPost) {
        log.info("tistoryPostWriteDto -> {}", tistoryPost);
        tistoryService.writePost(tistoryPost);
        return "";
    }
}
