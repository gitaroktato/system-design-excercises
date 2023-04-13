package com.example.interaction

import com.rabbitmq.client.*
import io.ktor.util.logging.*
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.runBlocking
import java.nio.charset.StandardCharsets

class RabbitMq(private val logger: Logger, private val meterRegistry: MeterRegistry) {
    private val channel: Channel
    private val workerConsumerTag: String
    private val cancelCallback: CancelCallback
    private val deliverCallback: DeliverCallback

    init {
        channel = ConnectionFactory().newConnection("amqp://guest:guest@localhost:5672/").createChannel()
        // Setting QoS per channel.
        channel.basicQos(1, false)
        // Setting up workers
        workerConsumerTag = "SimpleConsumer - ${ProcessHandle.current().pid()}"
        deliverCallback = DeliverCallback { consumerTag: String?, delivery: Delivery ->
            runBlocking {
                val sample: Timer.Sample = Timer.start(meterRegistry)
                val key = String(delivery.body, StandardCharsets.UTF_8)
                logger.info("[$consumerTag] Received key: '$key'")
                channel.basicAck(delivery.envelope.deliveryTag, false)
                logger.info("[$consumerTag] Calling DynamoDB")
                val entry = DynamoDb.getValueForKey("key_values", "key", key)
                logger.info("[$consumerTag] Received value: '$entry'")
                channel.basicPublish(
                    "",
                    delivery.properties.replyTo,
                    delivery.properties,
                    entry!!.toByteArray(charset("UTF-8"))
                )
                sample.stop(meterRegistry.timer("mq_execution_time", "consumer_tag", consumerTag))
            }
        }
        cancelCallback = CancelCallback { consumerTag: String? ->
            logger.error("[$consumerTag] was canceled")
        }
        // Ready
        logger.info("[$workerConsumerTag] Waiting for messages...")
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
