package com.example.service

import com.example.interaction.RabbitMq
import com.example.service.plugins.configureMetrics
import com.example.service.plugins.configureRouting
import com.example.service.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureMetrics()
    configureSerialization()
    configureRouting()
    RabbitMq.init("api_one")
    RabbitMq.init("api_two")
}
