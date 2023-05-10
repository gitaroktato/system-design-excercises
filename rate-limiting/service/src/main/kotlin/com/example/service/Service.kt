package com.example.service

import com.example.service.plugins.configureMessaging
import com.example.service.plugins.configureMetrics
import com.example.service.plugins.configureRouting
import com.example.service.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8081
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureMetrics()
    configureSerialization()
    val mq = configureMessaging()
    configureRouting(mq)
}
