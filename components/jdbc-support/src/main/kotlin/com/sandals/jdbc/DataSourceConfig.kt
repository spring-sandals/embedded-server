package com.sandals.jdbc

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun dataSource(url: String, maximumPoolSize: Int): DataSource {
    return HikariDataSource(HikariConfig().apply {
        jdbcUrl = url
        this.maximumPoolSize = maximumPoolSize
        validate()
    })
}