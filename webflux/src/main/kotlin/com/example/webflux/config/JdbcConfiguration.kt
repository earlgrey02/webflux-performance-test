package com.example.webflux.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.data.r2dbc.repository.R2dbcRepository

@Import(DataSourceAutoConfiguration::class)
@EnableJdbcRepositories(
    basePackages = ["com.example.webflux.repository"],
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [R2dbcRepository::class]
    )]
)
@Configuration
class JdbcConfiguration
