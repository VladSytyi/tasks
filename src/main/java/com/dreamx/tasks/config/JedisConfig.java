package com.dreamx.tasks.config;

import com.dreamx.tasks.config.properties.CacheConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    private final BaseConfig baseConfig;

    public JedisConfig(BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
    }

    @Bean
    public JedisPool jedisPool() {
        var cacheConfig = cacheConfig();
        var defaultPoolConfig = new JedisPoolConfig();
        return new JedisPool(defaultPoolConfig, cacheConfig.host(), cacheConfig.port());
    }

    private CacheConfig cacheConfig() {
        return baseConfig.configurationProvider().bind("cache.dragonfly", CacheConfig.class);
    }




}
