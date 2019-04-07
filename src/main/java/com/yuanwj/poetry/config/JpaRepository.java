package com.yuanwj.poetry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(value = "com.yuanwj.poetry.repository")
@Configuration
public class JpaRepository {

}
