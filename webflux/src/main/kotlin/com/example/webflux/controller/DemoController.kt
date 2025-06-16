package com.example.webflux.controller

import com.example.webflux.repository.DemoRepository
import com.example.webflux.repository.ReactiveDemoRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import kotlin.math.sqrt

@RestController
@RequestMapping("/webflux")
class DemoController(
    private val demoRepository: DemoRepository,
    private val reactiveDemoRepository: ReactiveDemoRepository,
) {
    @GetMapping("/cpu")
    fun `CPU Bound`() =
        repeat(100_000_000) { sqrt(it.toDouble()) }

    @GetMapping("/io/blocking")
    fun `Blocking IO Bound`() {
        demoRepository.findById(1)
    }

    @GetMapping("/io/non-blocking")
    fun `Non-blocking IO Bound`(): Mono<Void> =
        reactiveDemoRepository.findById(1)
            .then()
}
