package com.example.interaction

import com.rabbitmq.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel as CoroutineChannel
import java.nio.charset.StandardCharsets
import java.util.*

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


    suspend fun call(key: String): String = withContext(Dispatchers.IO) {
        val corrId = UUID.randomUUID().toString()
        val replyQueueName = channel.queueDeclare().queue

        val props = AMQP.BasicProperties.Builder()
            .correlationId(corrId)
            .replyTo(replyQueueName)
            .build()

        channel.basicPublish("", "test_queue", props, key.toByteArray(charset("UTF-8")))
        val consumerChannel = CoroutineChannel<String>()
        channel.basicConsume(replyQueueName, true, object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                if (properties.correlationId == corrId) {
                    launch {
                        val result = String(body, charset("UTF-8"))
                        consumerChannel.send(result)
                    }
                }
            }
        })
        consumerChannel.receive()
    }
}
