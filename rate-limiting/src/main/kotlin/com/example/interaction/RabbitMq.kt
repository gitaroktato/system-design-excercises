package com.example.interaction

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.RpcClient
import com.rabbitmq.client.RpcClientParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

object RabbitMq {

    private var channel: Channel
    private val rpcClients = mutableMapOf<String, RpcClient>()

    init {
        channel = ConnectionFactory().newConnection("amqp://guest:guest@localhost:5672/").createChannel()
    }

    fun init(queueName: String) {
        val args = mapOf(
            "x-max-length" to 5,
            "x-overflow" to "reject-publish"
        )
        channel.queueDeclare(
            queueName,
            false, false,
            false,
            args
        )
        val replyQueueName = channel.queueDeclare().queue
        val params = RpcClientParams()
        params.channel(channel)
        params.exchange("")
        params.routingKey(queueName)
        params.replyTo(replyQueueName)
        val rpcClient = RpcClient(params)
        rpcClients[queueName] = rpcClient
    }

    suspend fun send(queueName: String, key: String) {
        channel.basicPublish(
                "",
                queueName,
                null,
                key.toByteArray(StandardCharsets.UTF_8)
            )
        println(" [x] Sent '$key'")
    }

    suspend fun call(queueName: String, key: String): String = withContext(Dispatchers.IO) {
        rpcClients[queueName]!!.stringCall(key)
    }
}
