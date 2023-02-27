package com.example.worker.plugins

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureMetrics(meterRegistry: PrometheusMeterRegistry): MeterRegistry {
    install(MicrometerMetrics) {
        registry = meterRegistry
        distributionStatisticConfig = DistributionStatisticConfig
            .Builder()
            .percentilesHistogram(true)
            .build()
    }
    routing {
        get("/metrics") {
            call.respond(meterRegistry.scrape())
        }
    }
    return meterRegistry
}