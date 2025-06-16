package com.example.webflux.repository

import com.example.webflux.entity.Demo
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface ReactiveDemoRepository : R2dbcRepository<Demo, Long>
