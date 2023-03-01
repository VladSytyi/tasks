package com.dreamx.tasks.repository;

import com.dreamx.tasks.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> findById(Long id);
    Flux<User> findAll();
    Mono<User> create(Long id, User user);

    Mono<User> update(Long id, User user);

    Mono<Long> generateId();
}
