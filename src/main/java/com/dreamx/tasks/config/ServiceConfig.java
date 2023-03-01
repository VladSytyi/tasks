package com.dreamx.tasks.config;

import com.dreamx.tasks.model.User;
import com.dreamx.tasks.service.DefaultUserService;
import com.dreamx.tasks.service.DragonflyIdempotencyService;
import com.dreamx.tasks.service.IdempotencyService;
import com.dreamx.tasks.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    private final JedisConfig jedisConfig;
    private final BaseConfig baseConfig;
    private final RepositoryConfig repositoryConfig;

    public ServiceConfig(JedisConfig jedisConfig, BaseConfig baseConfig, RepositoryConfig repositoryConfig) {
        this.jedisConfig = jedisConfig;
        this.baseConfig = baseConfig;
        this.repositoryConfig = repositoryConfig;
    }

    @Bean
    public UserService userService() {
        return new DefaultUserService(
                idempotencyService(),
                repositoryConfig.userRepository()
        );
    }


    @Bean
    public IdempotencyService<User> idempotencyService() {
        var idempotentCacheTtl = baseConfig.configurationProvider().getProperty("cache.idempotent.ttl", String.class);
        return new DragonflyIdempotencyService(
                jedisConfig.jedisPool(),
                idempotentCacheTtl
        );
    }


}
