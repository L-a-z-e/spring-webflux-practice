package com.laze.springwebfluxpractice.controller;

import com.laze.springwebfluxpractice.dto.UserCreateRequest;
import com.laze.springwebfluxpractice.dto.UserResponse;
import com.laze.springwebfluxpractice.dto.UserUpdateRequest;
import com.laze.springwebfluxpractice.repository.User;
import com.laze.springwebfluxpractice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    @Test
    void createUser() {
        when(userService.create("laze", "laze@email.com")).thenReturn(Mono.just(new User(1L,"laze", "laze@email.com", LocalDateTime.now(), LocalDateTime.now())));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateRequest("laze", "laze@email.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("laze", res.getName());
                    assertEquals("laze@email.com", res.getEmail());
                });
    }

    @Test
    void findAllUsers() {
        when(userService.findAll()).thenReturn(
                Flux.just(
                        new User(1L,"laze1", "laze1@email.com", LocalDateTime.now(), LocalDateTime.now()),
                        new User(2L,"laze2", "laze2@email.com", LocalDateTime.now(), LocalDateTime.now()),
                        new User(3L,"laze3", "laze3@email.com", LocalDateTime.now(), LocalDateTime.now())
                )
        );

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserResponse.class)
                .hasSize(3);

    }
    @Test
    void findById() {

        when(userService.findById(1L)).thenReturn(
                Mono.just(new User(1L,"laze1", "laze1@email.com", LocalDateTime.now(), LocalDateTime.now()))
        );
        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("laze1", res.getName());
                    assertEquals("laze1@email.com", res.getEmail());
                });
    }

    @Test
    void notFoundById() {

        when(userService.findById(1L)).thenReturn(
                Mono.empty()
        );
        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void deleteById() {
        when(userService.deleteById(1L)).thenReturn(
                Mono.empty()
        );
        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void updateUser() {
        when(userService.update(1L, "laze1", "laze1@email.com")).thenReturn(
                Mono.just(new User(1L,"laze1", "laze1@email.com", LocalDateTime.now(), LocalDateTime.now())));

        webTestClient.put().uri("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdateRequest("laze1", "laze1@email.com"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("laze1", res.getName());
                    assertEquals("laze1@email.com", res.getEmail());
                });
    }
}