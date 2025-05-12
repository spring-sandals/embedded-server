package com.sandals.server

import com.codahale.metrics.MetricRegistry
import com.sandals.web.EmbeddedServer
import listenerContainerFactory
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
import java.net.URI

@Configuration
@PropertySource("classpath:application.properties", ignoreResourceNotFound = true)
open class Application {
    @Value("\${com.sandals.server.rabbitmq.url:\${RABBITMQ_URL}}")
    private val rabbitmqlURL: String = ""

    @Bean
    open fun queue(): Queue {
        return Queue("accountManagement")
    }

    @Bean
    open fun rabbitTemplate(): RabbitTemplate {
        return RabbitTemplate(CachingConnectionFactory(URI(rabbitmqlURL)))
    }

    @Bean
    open fun rabbitListenerContainerFactory(): SimpleRabbitListenerContainerFactory {
        val prefetch = 10
        val acknowledgeMode = AcknowledgeMode.AUTO
        val connectionFactory = CachingConnectionFactory("localhost")
        return listenerContainerFactory(connectionFactory, acknowledgeMode, prefetch)
    }

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