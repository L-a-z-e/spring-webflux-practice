package com.laze.springwebfluxpractice.controller;

import com.laze.springwebfluxpractice.dto.PostR2dbcCreateRequest;
import com.laze.springwebfluxpractice.dto.PostR2dbcResponse;
import com.laze.springwebfluxpractice.service.PostServiceR2dbc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/r2dbc/posts")
public class PostR2dbcController {

    private final PostServiceR2dbc postServiceR2dbc;

    @PostMapping("")
    public Mono<PostR2dbcResponse> createPost(@RequestBody PostR2dbcCreateRequest request) {
        return postServiceR2dbc.create(request.getUserId(), request.getTitle(), request.getContent())
                .map(PostR2dbcResponse::of);
    }

    @GetMapping("")
    public Flux<PostR2dbcResponse> findAllPosts() {
        return postServiceR2dbc.findAll()
                .map(PostR2dbcResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostR2dbcResponse>> findPost(@PathVariable Long id) {
        return postServiceR2dbc.findById(id)
                .map(u -> ResponseEntity.ok().body(PostR2dbcResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePost(@PathVariable Long id) {
        return postServiceR2dbc.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
