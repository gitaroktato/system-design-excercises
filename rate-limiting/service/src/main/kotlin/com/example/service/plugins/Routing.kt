package com.example.service.plugins

import com.example.interaction.DynamoDb
import com.example.interaction.RabbitMq
import io.github.resilience4j.kotlin.ratelimiter.decorateSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.Duration


fun Application.configureRouting(mq: RabbitMq) {
    val config = RateLimiterConfig.custom()
        .limitRefreshPeriod(Duration.ofSeconds(1))
        .limitForPeriod(500)
        .timeoutDuration(Duration.ofSeconds(1))
        .build()
    val rateLimiterRegistry = RateLimiterRegistry.of(config)

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
        get("/limited/{key?}") {
            val key = call.parameters["id"] ?: return@get call.respondText(
                "Missing key",
                status = HttpStatusCode.BadRequest
            )
            val apiKey = call.parameters["apiKey"] ?: return@get call.respondText(
                "Missing API key",
                status = HttpStatusCode.BadRequest
            )
            val limiter = rateLimiterRegistry.rateLimiter(apiKey)
            limiter.decorateSuspendFunction {
                println("Getting value for key: $key")
                val entry = DynamoDb.getValueForKey("key_values", "key", key)
                call.respondText(entry.orEmpty())
            }.invoke()
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
            call.application.log.info("Async getting value for key: $key with API key $apiKey")
            val entry = mq.call(apiKey, key)
            call.respondText(entry)
        }
    }
}
