package com.sandals.server

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@RabbitListener(queues = ["accountManagement"])
@EnableRabbit
@Component
open class AccountsProcessorAsync {
    companion object {
        val received = AtomicInteger(0) // note - interim for testing
    }

    @RabbitHandler
    fun receive(message: String) {
        received.incrementAndGet()
        println("received message [$message]")
    }
}