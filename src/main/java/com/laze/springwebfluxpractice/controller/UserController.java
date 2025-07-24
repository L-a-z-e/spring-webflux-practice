package com.laze.springwebfluxpractice.controller;

import com.laze.springwebfluxpractice.dto.UserCreateRequest;
import com.laze.springwebfluxpractice.dto.UserResponse;
import com.laze.springwebfluxpractice.dto.UserUpdateRequest;
import com.laze.springwebfluxpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public Mono<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request.getName(), request.getEmail())
                .map(UserResponse::of);
    }

    @GetMapping
    public Flux<UserResponse> findAllUsers() {
        return userService.findAll()
                .map(UserResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable  Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserResponse.of(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteById(@PathVariable  Long id) {
        return userService.deleteById(id).then(
                Mono.just(ResponseEntity.noContent().build())
        );
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable  Long id, @RequestBody UserUpdateRequest request) {
        return userService.update(id, request.getName(),  request.getEmail())
                .map(user -> ResponseEntity.ok(UserResponse.of(user)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
