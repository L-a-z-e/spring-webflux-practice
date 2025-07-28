package com.laze.springwebfluxpractice.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PostR2dbcRepository extends R2dbcRepository<Post,Long> {
}
