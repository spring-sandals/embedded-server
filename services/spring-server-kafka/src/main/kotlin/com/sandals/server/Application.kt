package com.sandals.server

import com.codahale.metrics.MetricRegistry
import com.sandals.web.EmbeddedServer
import listenerContainerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver

@Configuration
@PropertySource("classpath:application.properties", ignoreResourceNotFound = true)
open class Application {

    @Value("\${com.sandals.server.kafka.bootstrap.servers:\${KAFKA_BOOTSTRAP_SERVERS}}")
    private val kafkaBootstrapServers: String = ""

    @Value("\${com.sandals.server.kafka.security.protocol:\${KAFKA_SECURITY_PROTOCOL}}")
    private val kafkaSecurityProtocol: String = ""

    @Bean
    open fun kafkaTemplate(): KafkaTemplate<String, String> {
        val configs = mapOf(
            "key.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
            "value.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
            "bootstrap.servers" to kafkaBootstrapServers,
            "security.protocol" to kafkaSecurityProtocol
        )
        return KafkaTemplate(DefaultKafkaProducerFactory(configs))
    }

    @Bean
    open fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val configs = mapOf(
            "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
            "value.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
            "auto.offset.reset" to "earliest",
            "bootstrap.servers" to kafkaBootstrapServers,
            "security.protocol" to kafkaSecurityProtocol
        )
        return listenerContainerFactory(configs)
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