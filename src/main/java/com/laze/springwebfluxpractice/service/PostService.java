package com.laze.springwebfluxpractice.service;

import com.laze.springwebfluxpractice.client.PostClient;
import com.laze.springwebfluxpractice.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {
    private PostClient postClient;

    public Mono<PostResponse> getPostContent(Long id) {
        return postClient.getPost(id);
    }
}
