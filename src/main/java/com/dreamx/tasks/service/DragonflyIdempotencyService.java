package com.dreamx.tasks.service;

import com.dreamx.tasks.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;
import java.util.Map;

public class DragonflyIdempotencyService implements IdempotencyService<User> {

    private final JedisPool jedisPool;
    private final String cacheTtl;

    public DragonflyIdempotencyService(JedisPool jedisPool, String cacheTtl) {
        this.jedisPool = jedisPool;
        this.cacheTtl = cacheTtl;
    }


    @Override
    public Mono<Void> set(String key, User user) {
        return Mono.fromCallable(jedisPool::getResource)
                .map(jedis -> jedis.setex(key, Duration.parse(cacheTtl).toSeconds(), user.email()))
                .doFinally(it -> jedisPool.close())
                .then();
    }

    @Override
    public Mono<Boolean> exists(String key) {
        return Mono.fromCallable(jedisPool::getResource)
                .map(jedis -> jedis.exists(key))
//                .filter(it -> it)
//                .switchIfEmpty(Mono.empty())
                .defaultIfEmpty(false)
                .doFinally(e -> jedisPool.close());
    }

}
