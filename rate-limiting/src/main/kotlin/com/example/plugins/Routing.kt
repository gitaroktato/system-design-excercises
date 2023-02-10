package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/ping") {
            call.respondText("Hello World!")
        }
        get("/{key?}") {
            val key = call.parameters["id"] ?: return@get call.respondText(
                "Missing key",
                status = HttpStatusCode.BadRequest
            )
            println("Getting value for key: $key")
            val entry = getValueForKey("key_values", "key", key)
            call.respondText(entry.orEmpty())
        }
    }
}
