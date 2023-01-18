package com.dreamx.tasks.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.cfg4j.source.reload.ReloadStrategy;
import org.cfg4j.source.reload.strategy.PeriodicalReloadStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class BaseConfig {
    @Bean
    public ConfigurationProvider configurationProvider() {
        var filesPath = Paths.get("src/main/resources");
        ConfigFilesProvider configFilesProvider = () -> List.of(Paths.get("application.yaml"));
        ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);
        ImmutableEnvironment environment = new ImmutableEnvironment(filesPath.toString());
        ReloadStrategy reloadStrategy = new PeriodicalReloadStrategy(5, TimeUnit.SECONDS);

        return new ConfigurationProviderBuilder()
                .withConfigurationSource(source)
                .withReloadStrategy(reloadStrategy)
                .withEnvironment(environment)
                .build();
    }

}
