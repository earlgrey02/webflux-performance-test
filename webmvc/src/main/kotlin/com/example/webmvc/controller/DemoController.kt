package com.example.webmvc.controller

import com.example.webmvc.repository.DemoRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.sqrt

@RestController
@RequestMapping("/webmvc")
class DemoController(
    private val demoRepository: DemoRepository,
) {
    @GetMapping("/cpu")
    fun `CPU Bound`() {
        repeat(10_000_000) { sqrt(it.toDouble()) }
    }

    @GetMapping("/io")
    fun `IO Bound`() {
        demoRepository.findById(1)
    }
}
