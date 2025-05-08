package t.sandals.server

import com.sandals.server.AccountsProcessorAsync
import com.sandals.test.TestServer
import org.apache.kafka.clients.admin.NewTopic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

open class AccountsControllerAsyncTest() {
    private val testServer = TestServer(8887, "com.sandals.accounts,com.sandals.server,t.sandals.server")

    @Before
    fun setUp() {
        testServer.start()
    }

    @After
    fun tearDown() {
        testServer.stop()
    }

    @Test
    fun testCreateAccounts() {
        val response = RestTemplate().postForEntity<String>("http://localhost:8887/api/accounts", String::class.java)
        assertEquals("""{"status":"pending"}""", response.body)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        waitForQueues { AccountsProcessorAsync.received.get() < 1 }
    }

    /// supporting

    private fun waitForQueues(function: () -> Boolean) {
        do Thread.sleep(100) while (function())
    }

    @EnableKafka
    @Configuration
    open class KafkaConfig {
        @Bean
        open fun kafkaAdmin(): KafkaAdmin {
            return KafkaAdmin(mapOf("bootstrap.servers" to "localhost:9094"))
        }

        @Bean
        open fun topic(): NewTopic {
            return NewTopic("accountManagement", 3, 1.toShort())
        }

        @Bean
        open fun kafkaTemplate(): KafkaTemplate<String, String> {
            val configs = mapOf(
                "key.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
                "bootstrap.servers" to "localhost:9094",
                "security.protocol" to "PLAINTEXT"
            )
            return KafkaTemplate(DefaultKafkaProducerFactory(configs))
        }

        @Bean
        open fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
            val configs = mapOf(
                "auto.offset.reset" to "earliest",
                "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
                "bootstrap.servers" to "localhost:9094",
                "security.protocol" to "PLAINTEXT",
            )
            val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()
            containerFactory.consumerFactory = DefaultKafkaConsumerFactory(configs)
            return containerFactory
        }
    }
}