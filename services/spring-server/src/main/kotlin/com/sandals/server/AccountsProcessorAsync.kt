package com.sandals.server

import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@EnableKafka
@Component
class AccountsProcessorAsync {
    companion object {
        val received = AtomicInteger(0) // note - interim for testing
    }

    @KafkaListener(topics = ["accountManagement"], groupId = "accountGroup")
    fun processMessage(message: String) {
        received.incrementAndGet()
        println("received message [$message]")
    }
}