package com.example.service

import com.example.interaction.RabbitMq
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import com.example.service.plugins.configureRouting
import io.ktor.server.application.*

class ServiceTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting(RabbitMq(log))
        }
        client.get("/ping").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
