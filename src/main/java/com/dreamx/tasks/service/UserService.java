package com.dreamx.tasks.service;

import com.dreamx.tasks.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> findById(Long id);
    Flux<User> getAllUsers();
    Mono<User> createUser(String key, User user);
    Mono<User> updateUserById(String userId, User user);
}
