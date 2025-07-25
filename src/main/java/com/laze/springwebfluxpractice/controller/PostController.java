package com.laze.springwebfluxpractice.controller;

import com.laze.springwebfluxpractice.client.PostClient;
import com.laze.springwebfluxpractice.dto.PostResponse;
import com.laze.springwebfluxpractice.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostClient postClient;

    @GetMapping("/{id}")
    public Mono<PostResponse> getPostContent(@PathVariable Long id) {
        return postClient.getPost(id)
                .onErrorResume(error -> Mono.just(new PostResponse(id.toString(), "Fallback data %d".formatted(id))));
    }

    @GetMapping("/search")
    public Flux<PostResponse> getMultiplePostContent(@RequestParam(name = "ids") List<Long> idList) {
        return Flux.fromIterable(idList)
                .flatMap(this::getPostContent)
                .log();
    }

    @GetMapping("/search2")
    public Flux<PostResponse> getParallelMultiplePostContent(@RequestParam(name = "ids") List<Long> idList) {
        return Flux.fromIterable(idList)
                .parallel()
                .log()
                .runOn(Schedulers.parallel())
                .flatMap(this::getPostContent)
                .log()
                .sequential();
    }
}
