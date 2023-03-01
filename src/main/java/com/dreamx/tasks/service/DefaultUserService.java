package com.dreamx.tasks.service;

import com.dreamx.tasks.model.User;
import com.dreamx.tasks.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

public class DefaultUserService implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private final IdempotencyService<User> idempotencyService;
    private final UserRepository repository;

    public DefaultUserService(IdempotencyService<User> idempotencyService, UserRepository repository) {
        this.idempotencyService = idempotencyService;
        this.repository = repository;
    }

    @Override
    public Mono<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Flux<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Mono<User> createUser(String key, User user) {
        return idempotencyService.exists(key)
                .flatMap(exists -> {
                    if (exists) {
                        logger.info("User already exists");
                        return Mono.just(user);
                    }
                    return idempotencyService.set(key, user)
                            .flatMap(e -> generateIdAndSave(user));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> updateUserById(Long userId, User user) {
        return findById(userId)
                .switchIfEmpty(Mono.error(() -> new IllegalArgumentException("No User found")))
                .flatMap(v -> repository.update(userId, user));

    }

    private Mono<User> generateIdAndSave(User user) {
        return repository.generateId()
                .doOnNext(res -> logger.info("Generated Id {} for user with email {}", res, user.email()))
                .flatMap(id -> repository.create(id, user));
    }


}
