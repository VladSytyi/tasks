package com.dreamx.tasks.repository;

import com.dreamx.tasks.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultUserRepo implements UserRepository {

    private final Map<Long, User> repo;

    public DefaultUserRepo() {
        this.repo = new LinkedHashMap<>();
        repo.putIfAbsent(1L, new User(1L, "John", "Doe", "john@mail.com", "", LocalDateTime.now(), LocalDateTime.now()));
        repo.putIfAbsent(2L, new User(2L, "Alex", "Huber", "alxua@mail.com", "", LocalDateTime.now(), LocalDateTime.now()));
        repo.putIfAbsent(3L, new User(3L, "Liza", "Cash", "cashl@mail.com", "", LocalDateTime.now(), LocalDateTime.now()));
        repo.putIfAbsent(4L, new User(4L, "Vova", "Guta", "gutt@mail.com", "", LocalDateTime.now(), LocalDateTime.now()));
    }

    @Override
    public Mono<User> findById(Long id) {
        return Mono.just(repo)
                .filter(v -> v.containsKey(id))
                .map(e -> e.get(id))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<User> findAll() {
        return Flux.fromIterable(repo.values());
    }

    @Override
    public Mono<User> create(Long userId, User user) {
        return Mono.just(user)
                .map(u -> repo.putIfAbsent(userId, user))
                .thenReturn(user);
    }

    @Override
    public Mono<User> update(Long userId, User user) {
        return Mono.just(repo).map(e -> repo.replace(userId, user));
    }

    public Mono<Long> generateId() {
        return Mono.just(repo)
                .map(Map::keySet)
                .map(Collections::max)
                .map(v -> ++v);
    }


}
