package com.sandals.server

import com.codahale.metrics.MetricRegistry
import com.sandals.jdbc.dataSource
import com.sandals.web.EmbeddedServer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

@Configuration
@PropertySource("classpath:application.properties", ignoreResourceNotFound = true)
open class Application {

    @Value("\${com.sandals.server.postgresql.url:\${DATABASE_URL}}")
    private val postgresqlURL: String = ""

    @Bean
    open fun getJdbcTemplate() = JdbcTemplate(dataSource(postgresqlURL, 1))

    @Bean
    open fun metricRegistry() = MetricRegistry()

    @Suppress("DEPRECATION")
    @Bean
    open fun freemarkerConfig(): FreeMarkerConfigurer {
        val configurer = FreeMarkerConfigurer()
        val configuration = freemarker.template.Configuration()
        configuration.setClassForTemplateLoading(Application::class.java, "/templates")
        configurer.configuration = configuration
        return configurer
    }

    @Bean
    open fun freemarkerViewResolver(): FreeMarkerViewResolver = FreeMarkerViewResolver().apply {
        isCache = true
        setPrefix("")
        setSuffix(".ftl")
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8888
    EmbeddedServer(port, "com.sandals").start()
}