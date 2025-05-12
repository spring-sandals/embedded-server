package t.sandals.server

import com.sandals.server.AccountsProcessorAsync
import com.sandals.web.EmbeddedServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForEntity

class ApplicationTest() {
    private val application = EmbeddedServer(8887, "com.sandals")
    private val restTemplate = RestTemplate()
    private val template = RabbitTemplate()

    @Before
    fun setUp() {
        application.start()
    }

    @After
    fun tearDown() {
        application.stop()
    }

    @Test
    fun testStart() {
        val endpoint = "http://localhost:8887"

        val response = restTemplate.getForObject<String>(endpoint)
        assertTrue(response.contains("Spring Sandals"))

        val about = restTemplate.getForObject<String>("$endpoint/about.html")
        assertTrue(about.contains("unstyled about!"))

        val metrics = restTemplate.getForObject<String>("$endpoint/api/metrics")
        assertTrue(metrics.contains("index-requests"))
        assertTrue(metrics.contains("count = 1"))

        val accounts = restTemplate.postForEntity<String>("http://localhost:8887/api/accounts", String::class.java)
        Assert.assertEquals("""{"status":"pending"}""", accounts.body)
        Assert.assertEquals(HttpStatus.CREATED, accounts.statusCode)
        waitForQueues { AccountsProcessorAsync.received.get() < 1 }
    }

    /// supporting

    private fun waitForQueues(function: () -> Boolean) {
        do Thread.sleep(100) while (function())
    }
}