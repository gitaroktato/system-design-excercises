package com.example.worker.plugins;

import com.example.interaction.RabbitMq
import io.ktor.server.application.*
import io.micrometer.core.instrument.MeterRegistry

fun Application.configureMessaging(registry: MeterRegistry) {
    val mq = RabbitMq(log, registry)
    mq.consumeFrom("api_one")
    mq.consumeFrom("api_two")
}
