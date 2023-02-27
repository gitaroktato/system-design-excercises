package com.example.worker.plugins

import com.example.interaction.DynamoDb
import com.example.interaction.RabbitMq
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/ping") {
            call.respondText("Hello World!")
        }
    }
}
