package com.sandals.server

import com.sandals.accounts.Account
import com.sandals.accounts.AccountsDataGateway
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
open class AccountsControllerWithCache(
    private val gateway: AccountsDataGateway,
    private val redisTemplate: RedisTemplate<Long, Account>
) {
    @Cacheable(value = ["account"], key = "id")
    @GetMapping("/api/accounts/{id}")
    open fun getAccount(@PathVariable("id") id: Long) = getCachedAccount(id)

    private fun getCachedAccount(id: Long): Account? {
        val operation = redisTemplate.opsForValue()

        val cached = operation.get(id)
        if (cached != null) return cached

        val found = gateway.getAccount(id)
        if (found != null) redisTemplate.opsForValue().set(id, found, Duration.ofMinutes(20))
        return found
    }
}