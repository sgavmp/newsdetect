package com.ikip.newsdetect.main.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.ikip.newsdetect")
@EntityScan(basePackages = "com.ikip.newsdetect.model")
public class DatabaseConfiguration {
}
