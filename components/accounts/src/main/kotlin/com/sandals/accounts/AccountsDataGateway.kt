package com.sandals.accounts

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AccountsDataGateway(private val jdbcTemplate: JdbcTemplate) {

    fun getAccounts(): List<Account> = jdbcTemplate.query("select id, name, value from accounts") { rs, _ ->
        Account(
            rs.getLong(1),
            rs.getString(2),
            rs.getDouble(3)
        )
    }

    fun getAccount(id: Long): Account? = jdbcTemplate.queryForObject(
        "select id, name, value from accounts where id = ?",
        { rs, _ ->
            Account(
                rs.getLong(1),
                rs.getString(2),
                rs.getDouble(3)
            )
        }, id
    )
}