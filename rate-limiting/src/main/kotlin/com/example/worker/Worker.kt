package com.example.worker

import com.example.interaction.DynamoDb
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

fun main(): Unit = runBlocking {
    val factory = ConnectionFactory()
    val connection = factory.newConnection("amqp://guest:guest@localhost:5672/")
    val channel = connection.createChannel()
    // Setting QoS per channel.
    channel.basicQos(20, false)
    val consumerTag = "SimpleConsumer - ${ProcessHandle.current().pid()}"

    channel.queueDeclare("test_queue", false, false, false, null)

    println("[$consumerTag] Waiting for messages...")
    val deliverCallback = DeliverCallback { tag: String?, delivery: Delivery ->
        val key = String(delivery.body, StandardCharsets.UTF_8)
        println("[$tag] Received key: '$key'")
        runBlocking {
            println("[$tag] Calling DynamoDB")
            val entry = DynamoDb.getValueForKey("key_values", "key", key)
            println("[$tag] Received value: '$entry'")
            channel.basicPublish(
                "",
                delivery.properties.replyTo,
                delivery.properties,
                entry!!.toByteArray(charset("UTF-8"))
            )
        }
    }
    val cancelCallback = CancelCallback { tag: String? ->
        println("[$tag] was canceled")
    }

    channel.basicConsume("test_queue", true, consumerTag, deliverCallback, cancelCallback)
}
