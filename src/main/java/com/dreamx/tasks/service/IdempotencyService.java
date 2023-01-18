package com.dreamx.tasks.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface IdempotencyService<T> {

    Mono<Void> set(String key, T value);
    Mono<Boolean> exists(String key);
}
