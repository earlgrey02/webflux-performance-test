package com.example.webmvc.repository

import com.example.webmvc.entity.Demo
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DemoRepository : CrudRepository<Demo, Long>
