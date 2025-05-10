package com.sandals.server

import com.sandals.accounts.AccountsDataGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountsController(private val gateway: AccountsDataGateway) {

    @GetMapping("/api/accounts")
    fun getAccounts() = gateway.getAccounts()
}