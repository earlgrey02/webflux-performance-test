package com.example.webmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebMvcApplication

fun main(vararg args: String) {
    runApplication<WebMvcApplication>(*args)
}
