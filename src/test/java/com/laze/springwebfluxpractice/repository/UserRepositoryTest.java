package com.laze.springwebfluxpractice.repository;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Test
    void save() {
        User user = User.builder().name("laze").email("laze@email.com").build();
        StepVerifier.create(userRepository.save(user))
                .assertNext(i -> {
                    assertEquals("laze", i.getName());
                    assertEquals(1L, i.getId());
                })
                .verifyComplete();
    }

    @Test
    void findAll() {
        userRepository.save(User.builder().name("laze1").email("laze1@email.com").build());
        userRepository.save(User.builder().name("laze2").email("laze2@email.com").build());
        userRepository.save(User.builder().name("laze3").email("laze3@email.com").build());
        StepVerifier.create(userRepository.findAll())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        userRepository.save(User.builder().name("laze1").email("laze1@email.com").build());
        userRepository.save(User.builder().name("laze2").email("laze2@email.com").build());
        userRepository.save(User.builder().name("laze3").email("laze3@email.com").build());

        StepVerifier.create(userRepository.findById(1L))
                .assertNext(i -> {
                    assertEquals("laze1", i.getName());
                    assertEquals("laze1@email.com", i.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void deleteById() {
        userRepository.save(User.builder().name("laze1").email("laze1@email.com").build());
        userRepository.save(User.builder().name("laze2").email("laze2@email.com").build());
        userRepository.save(User.builder().name("laze3").email("laze3@email.com").build());

        StepVerifier.create(userRepository.deleteById(1L))
                .expectNextCount(1)
                .verifyComplete();
    }
}