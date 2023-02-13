package com.example.plugins

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import java.nio.charset.StandardCharsets

object RabbitMq {

    val channel: Channel

    init {
        channel = ConnectionFactory().newConnection("amqp://guest:guest@localhost:5672/").createChannel()
        channel.queueDeclare("test_queue", false, false, false, null)
    }

    fun send() {
        val message = "Hello World!"
        channel.basicPublish(
                "",
                "test_queue",
                null,
                message.toByteArray(StandardCharsets.UTF_8)
            )
        println(" [x] Sent '$message'")
    }
}


