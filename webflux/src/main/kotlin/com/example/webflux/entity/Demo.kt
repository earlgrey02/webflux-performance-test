package com.example.webflux.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
class Demo {
    @Id
    var id: Long? = null
}
