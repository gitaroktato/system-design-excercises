package com.example.worker

import com.example.interaction.DynamoDb
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.*
import java.nio.charset.StandardCharsets

fun main(): Unit = runBlocking {
    val factory = ConnectionFactory()
    val connection = factory.newConnection("amqp://guest:guest@localhost:5672/")
    val channel = connection.createChannel()
    // Setting QoS per channel.
    channel.basicQos(1, false)
    val consumerTag = "SimpleConsumer - ${ProcessHandle.current().pid()}"

    println("[$consumerTag] Waiting for messages...")
    val deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
        val key = String(delivery.body, StandardCharsets.UTF_8)
        println("[$consumerTag] Received key: '$key'")
        channel.basicAck(delivery.envelope.deliveryTag, false)
        runBlocking {
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
    val cancelCallback = CancelCallback { consumerTag: String? ->
        println("[$consumerTag] was canceled")
    }

    channel.basicConsume("test_queue", false, consumerTag, deliverCallback, cancelCallback)
}
