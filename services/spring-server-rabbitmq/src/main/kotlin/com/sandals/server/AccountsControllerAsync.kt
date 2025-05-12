package com.sandals.server

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class AccountsControllerAsync(private val queue: Queue, private val template: RabbitTemplate) {

    @PostMapping("/api/accounts")
    fun createAccount(): ResponseEntity<CreateAccountResponse> {
        try {
            val epochMilli = Instant.now().toEpochMilli()
            this.template.convertAndSend(queue.name, "aValue-$epochMilli")
            return ResponseEntity(CreateAccountResponse("pending"), HttpStatus.CREATED)
        } catch (e: Exception) {
            return ResponseEntity(CreateAccountResponse("pending"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    data class CreateAccountResponse(val status: String)
}