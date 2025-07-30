package com.laze.springwebfluxpractice.service;

import com.laze.springwebfluxpractice.repository.Post;
import com.laze.springwebfluxpractice.repository.PostR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostServiceR2dbc {
    private final PostR2dbcRepository postR2dbcRepository;

    // create
    public Mono<Post> create(Long userId, String title, String content) {
        return postR2dbcRepository.save(Post.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .build()
        );
    }
    // read
    public Flux<Post> findAll() {
        return postR2dbcRepository.findAll();
    }

    public Mono<Post> findById(Long id) {
        return postR2dbcRepository.findById(id);
    }

    public Flux<Post> findAllByUserId(Long userId) {
        return postR2dbcRepository.findAllByUserId(userId);
    }

    // delete
    public Mono<Void> deleteById(Long id) {
        return postR2dbcRepository.deleteById(id);
    }

}
