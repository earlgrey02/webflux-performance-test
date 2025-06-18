package com.example.webflux.controller

import com.example.webflux.repository.DemoRepository
import com.example.webflux.repository.ReactiveDemoRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import kotlin.math.sqrt

@RestController
@RequestMapping("/webflux")
class DemoController(
    private val demoRepository: DemoRepository,
    private val reactiveDemoRepository: ReactiveDemoRepository,
) {
    @GetMapping("/cpu")
    fun `CPU Bound`() {
        repeat(10_000_000) { sqrt(it.toDouble()) }
    }

    @GetMapping("/cpu-with-scheduler")
    fun `CPU Bound with Scheduler`(): Mono<Void> =
        Mono.fromCallable { `CPU Bound`() }
            .subscribeOn(Schedulers.parallel())
            .then()

    @GetMapping("/blocking-io")
    fun `Blocking IO Bound`() {
        demoRepository.findById(1)
    }

    @GetMapping("/non-blocking-io")
    fun `Non-blocking IO Bound`(): Mono<Void> =
        reactiveDemoRepository.findById(1)
            .then()

    @GetMapping("/blocking-io-with-scheduler")
    fun `Blocking IO Bound with Scheduler`(): Mono<Void> =
        Mono.fromCallable { `Blocking IO Bound`() }
            .subscribeOn(Schedulers.boundedElastic())
            .then()
}
