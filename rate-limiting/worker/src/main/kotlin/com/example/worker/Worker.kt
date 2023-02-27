package com.example.worker

import com.example.interaction.RabbitMq
import com.example.worker.plugins.configureMetrics
import com.example.worker.plugins.configureRouting
import com.example.worker.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureMetrics()
    configureSerialization()
    configureRouting()
    RabbitMq.consumeFrom("api_one")
    RabbitMq.consumeFrom("api_two")
}
