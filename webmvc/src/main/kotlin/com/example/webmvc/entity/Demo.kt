package com.example.webmvc.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
class Demo {
    @Id
    var id: Long? = null
}
