package com.example.service.plugins

import com.example.interaction.RabbitMq
import io.ktor.server.application.*

fun Application.configureMessaging(): RabbitMq {
    val mq = RabbitMq(log)
    mq.open()
    mq.init("api_one")
    mq.init("api_two")
    return mq
}