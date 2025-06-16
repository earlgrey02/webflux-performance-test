package com.example.webflux.repository

import com.example.webflux.entity.Demo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DemoRepository : CrudRepository<Demo, Long>
