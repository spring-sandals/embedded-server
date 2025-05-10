package com.sandals.server

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import kotlin.random.Random

@RestController
class AccountsControllerAsync(private val template: KafkaTemplate<String, String>) {

    @PostMapping("/api/accounts")
    fun createAccount(): ResponseEntity<CreateAccountResponse> {
        try {
            val partition = Random.nextInt(0, 3)
            val epochMilli = Instant.now().toEpochMilli()
            val record = ProducerRecord("accountManagement", partition, "aKey", "aValue-$epochMilli")
            template.send(record)
            return ResponseEntity(CreateAccountResponse("pending"), HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(CreateAccountResponse("pending"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    data class CreateAccountResponse(val status: String)
}