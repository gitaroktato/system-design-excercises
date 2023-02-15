package com.example.interaction

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.RpcClient
import com.rabbitmq.client.RpcClientParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

object RabbitMq {

    private val queueName: String
    private val channel: Channel
    private val replyQueueName: String
    private val rpcClient: RpcClient

    init {
        channel = ConnectionFactory().newConnection("amqp://guest:guest@localhost:5672/").createChannel()
        queueName = channel.queueDeclare(
            "test_queue",
            false, false,
            false,
            null
        ).queue
        replyQueueName = channel.queueDeclare().queue
        val params = RpcClientParams()
        params.channel(channel)
        params.exchange("")
        params.routingKey(queueName)
        params.replyTo(replyQueueName)
        rpcClient = RpcClient(params)
    }

    suspend fun send(key: String) {
        channel.basicPublish(
                "",
                queueName,
                null,
                key.toByteArray(StandardCharsets.UTF_8)
            )
        println(" [x] Sent '$key'")
    }


    suspend fun call(key: String): String = withContext(Dispatchers.IO) {
        rpcClient.stringCall(key)
    }
}
