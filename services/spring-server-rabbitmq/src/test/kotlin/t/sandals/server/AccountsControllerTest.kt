package t.sandals.server

import com.sandals.server.AccountsProcessorAsync
import com.sandals.test.TestServer
import listenerContainerFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.net.URI


open class AccountsControllerTest() {
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

    @Configuration
    open class RabbitConfig {
        @Bean
        open fun rabbitAdmin(): RabbitAdmin {
            return RabbitAdmin(CachingConnectionFactory(URI("amqp://localhost:5672")))
        }

        @Bean
        open fun queue(): Queue {
            return Queue("accountManagement")
        }

        @Bean
        open fun rabbitTemplate(): RabbitTemplate {
            return RabbitTemplate(CachingConnectionFactory(URI("amqp://localhost:5672")))
        }

        @Bean
        open fun rabbitListenerContainerFactory(): SimpleRabbitListenerContainerFactory {
            val prefetch = 10
            val acknowledgeMode = AcknowledgeMode.AUTO
            val connectionFactory = CachingConnectionFactory("localhost")
            return listenerContainerFactory(connectionFactory, acknowledgeMode, prefetch)
        }
    }
}