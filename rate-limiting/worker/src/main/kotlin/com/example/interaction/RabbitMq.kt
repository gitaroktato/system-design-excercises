package com.example.interaction

import com.rabbitmq.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

object RabbitMq {
    private val channel: Channel
    private val workerConsumerTag: String
    private val cancelCallback: CancelCallback
    private val deliverCallback: DeliverCallback

    init {
        channel = ConnectionFactory().newConnection("amqp://guest:guest@localhost:5672/").createChannel()
        // Setting QoS per channel.
        channel.basicQos(1, false)
        workerConsumerTag = "SimpleConsumer - ${ProcessHandle.current().pid()}"

        println("[$workerConsumerTag] Waiting for messages...")
        deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery -> runBlocking {
                val key = String(delivery.body, StandardCharsets.UTF_8)
                println("[$consumerTag] Received key: '$key'")
                channel.basicAck(delivery.envelope.deliveryTag, false)
                println("[$consumerTag] Calling DynamoDB")
                val entry = DynamoDb.getValueForKey("key_values", "key", key)
                println("[$consumerTag] Received value: '$entry'")
                channel.basicPublish(
                    "",
                    delivery.properties.replyTo,
                    delivery.properties,
                    entry!!.toByteArray(charset("UTF-8"))
                )
            }
        }
        cancelCallback = CancelCallback { consumerTag: String? ->
            println("[$consumerTag] was canceled")
        }
    }

    fun consumeFrom(queueName: String) {
        channel.basicConsume(
            queueName,
            false,
            "$workerConsumerTag [$queueName]",
            deliverCallback,
            cancelCallback
        )
    }
}
