package com.example.webflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.netty.ReactorNetty

@SpringBootApplication
class WebFluxApplication

fun main(vararg args: String) {
    System.setProperty(ReactorNetty.IO_WORKER_COUNT, "8")

    runApplication<WebFluxApplication>(*args)
}
