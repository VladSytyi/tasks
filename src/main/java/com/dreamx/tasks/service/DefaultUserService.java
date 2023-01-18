package com.dreamx.tasks.service;

import com.dreamx.tasks.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

public class DefaultUserService implements UserService {

    private static final Logger logger = LogManager.getLogger();

    private final IdempotencyService<User> idempotencyService;

    public DefaultUserService(IdempotencyService<User> idempotencyService) {
        this.idempotencyService = idempotencyService;
    }

    @Override
    public Mono<User> findById(Long id) {
        return Mono.just(new User(1L, "John", "Doe", "john@mail.com", "","salt", LocalDateTime.now(), LocalDateTime.now()));
    }

    @Override
    public Flux<User> getAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<User> createUser(String key, User user) {
        return idempotencyService.exists(key)
                .flatMap(exists -> {
                    if (exists) {
                        logger.info("User already exists");
                        return Mono.just(user);
                    }
                    // TODO: check if user already exists and if not save it into the database
                    return idempotencyService.set(key, user)
                            .then(Mono.just(user));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> updateUserById(String userId, User user) {
        // TODO Auto-generated method stub
        return null;
    }


}
