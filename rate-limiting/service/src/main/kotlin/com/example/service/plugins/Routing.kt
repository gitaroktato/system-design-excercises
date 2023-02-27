package com.example.service.plugins

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
        get("/direct/{key?}") {
            val key = call.parameters["id"] ?: return@get call.respondText(
                "Missing key",
                status = HttpStatusCode.BadRequest
            )
            println("Getting value for key: $key")
            val entry = DynamoDb.getValueForKey("key_values", "key", key)
            call.respondText(entry.orEmpty())
        }
        get("/async/{key?}") {
            val key = call.parameters["id"] ?: return@get call.respondText(
                "Missing key",
                status = HttpStatusCode.BadRequest
            )
            val apiKey = call.parameters["apiKey"] ?: return@get call.respondText(
                "Missing API key",
                status = HttpStatusCode.BadRequest
            )
            println("Async getting value for key: $key with API key $apiKey")
            val entry = RabbitMq.call(apiKey, key)
            call.respondText(entry)
        }
    }
}
