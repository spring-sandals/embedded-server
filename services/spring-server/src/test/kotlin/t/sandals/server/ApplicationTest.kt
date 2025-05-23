package t.sandals.server

import com.sandals.web.EmbeddedServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

class ApplicationTest() {
    private val application = EmbeddedServer(8887, "com.sandals")
    private val restTemplate = RestTemplate()

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
    }
}