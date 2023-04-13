package com.example.worker

import com.example.worker.plugins.configureMessaging
import com.example.worker.plugins.configureMetrics
import com.example.worker.plugins.configureRouting
import com.example.worker.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val meterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    configureMessaging(meterRegistry)
    configureMetrics(meterRegistry)
    configureSerialization()
    configureRouting()
}
