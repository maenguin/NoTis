package com.maenguin.notis.service;

import com.maenguin.notis.dto.tistory.TistoryItemResDto;
import com.maenguin.notis.dto.tistory.TistoryPostWriteResDto;
import com.maenguin.notis.dto.tistory.TistoryReponse;
import com.maenguin.notis.dto.tistory.TistoryBlogInfoReqDto;
import com.maenguin.notis.model.tistory.TistoryPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TistoryService {

    private final WebClient.Builder webClientBuilder;

    public TistoryService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${notis.tistory.clientId}")
    private String clientId;
    @Value("${notis.tistory.clientSecret}")
    private String clientSecret;
    @Value("${notis.tistory.redirectUri}")
    private String redirectUri;

    String accessToken = "3cbfb7193a4313d87f609e931d26ccec_93bfb603fd47d01bbddaf211513d2cdb";
    String outputType = "json";

    public String authorize() {
        String stateParam = "maenguin";
        String responseType = "code";
        var result = webClientBuilder.baseUrl("https://www.tistory.com")
                .build()
                .get()
                .uri("/oauth/authorize?client_id={clientId}&redirect_uri={redirctUri}&state={stateParam}&response_type={responseType}",
                        clientId,
                        redirectUri,
                        stateParam,
                        responseType)
                .retrieve()
                .bodyToMono(String.class);
        log.debug("authorize: result -> {}", result.block());
        return result.block();
    }

    public String getAccessToken(String code) {
        String grantType = "authorization_code";
        Mono<String> result;
        try {
            result = webClientBuilder.baseUrl("https://www.tistory.com")
                    .build()
                    .get()
                    .uri("/oauth/access_token?client_id={clientId}&client_secret={clientSecret}&redirect_uri={redirectUri}&code={code}&grant_type={grantType}",
                            clientId,
                            clientSecret,
                            redirectUri,
                            code,
                            grantType)
                    .retrieve()
                    .bodyToMono(String.class);
            var block = result.block();
            log.info("result -> {}", block);
            return block.split("=")[1];
        } catch (WebClientResponseException e) {
            log.error("error -> {}", e.getResponseBodyAsString());
        }
        return "";
    }

    public String getBlogInfo() {

        Mono<String> result;
        try {
            result = webClientBuilder.baseUrl("https://www.tistory.com")
                    .build()
                    .get()
                    .uri("/apis/blog/info?access_token={accessToken}&output={outputType}",
                            accessToken,
                            outputType)
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("result -> {}", result.block());
            return result.block();
        } catch (WebClientResponseException e) {
            log.error("error -> {}", e.getResponseBodyAsString());
        }
        return "";
    }

    public TistoryReponse<TistoryItemResDto<TistoryBlogInfoReqDto>> getBlogInfo(String accessToken) {
        Mono<TistoryReponse<TistoryItemResDto<TistoryBlogInfoReqDto>>> result;
        try {
             result = webClientBuilder.baseUrl("https://www.tistory.com")
                    .build()
                    .get()
                    .uri("/apis/blog/info?access_token={accessToken}&output={outputType}",
                            accessToken,
                            outputType)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TistoryReponse<TistoryItemResDto<TistoryBlogInfoReqDto>>>() {});
            var block = result.block();
            log.info("result -> {}", block);
            return block;
        } catch (WebClientResponseException e) {
            log.error("error -> {}", e.getResponseBodyAsString());
        }
        return null;
    }

    public String getPostList(String blogName, int page) {
        log.info("getPostList called.. param : { blogName = {}, page = {}}", blogName, page);
        Mono<String> result;
        try {
            result = webClientBuilder.baseUrl("https://www.tistory.com")
                    .build()
                    .get()
                    .uri("/apis/post/list?access_token={accessToken}&output={outputType}&blogName={blogName}&page={page}",
                            accessToken,
                            outputType,
                            blogName,
                            page)
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("result -> {}", result.block());
            return result.block();
        } catch (WebClientResponseException e) {
            log.error("error -> {}", e.getResponseBodyAsString());
        }
        return "";
    }

    public String getPostDetail(String blogName, String postId) {
        log.info("getPostDetail called - param : { blogName = {}, postId = {}}", blogName, postId);
        Mono<String> result;
        try {
            result = webClientBuilder.baseUrl("https://www.tistory.com")
                    .build()
                    .get()
                    .uri("/apis/post/read?access_token={accessToken}&output={outputType}&blogName={blogName}&postId={postId}",
                            accessToken,
                            outputType,
                            blogName,
                            postId)
                    .retrieve()
                    .bodyToMono(String.class);
            log.info("result -> {}", result.block());
            return result.block();
        } catch (WebClientResponseException e) {
            log.error("error -> {}", e.getResponseBodyAsString());
        }
        return "";
    }

    public TistoryReponse<TistoryPostWriteResDto> writePost(TistoryPost tistoryPost) {
        log.info("writePost called");
        Mono<TistoryReponse<TistoryPostWriteResDto>> result;
        try {
            result = webClientBuilder.baseUrl("https://www.tistory.com")
                    .build()
                    .post()
                    .uri("/apis/post/write?access_token={accessToken}&output={outputType}",
                            accessToken,
                            outputType)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData("blogName", tistoryPost.getBlogName())
                            .with("title", tistoryPost.getTitle())
                            .with("content", tistoryPost.getContent()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<TistoryReponse<TistoryPostWriteResDto>>() {});
            var block = result.block();
            log.debug("result -> {}", block);
            return block;
        } catch (WebClientResponseException e) {
            log.error("error -> {}", e.getResponseBodyAsString());
        }
        return null;
    }
}
