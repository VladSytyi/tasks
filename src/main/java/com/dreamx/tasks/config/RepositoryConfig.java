package com.dreamx.tasks.config;

import com.dreamx.tasks.repository.DefaultUserRepo;
import com.dreamx.tasks.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository() {
        return new DefaultUserRepo();
    }
}
