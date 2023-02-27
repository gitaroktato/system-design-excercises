package com.example.service.plugins

import com.example.interaction.RabbitMq
import io.ktor.server.application.*

fun Application.configureMessaging() {
    RabbitMq.init("api_one")
    RabbitMq.init("api_two")
}