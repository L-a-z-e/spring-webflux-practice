package com.laze.springwebfluxpractice.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository {
    Mono<User> save(User user);
    Flux<User> findAll();
    Mono<User> findById(Long id);
    Mono<Integer> deleteById(Long id);
}