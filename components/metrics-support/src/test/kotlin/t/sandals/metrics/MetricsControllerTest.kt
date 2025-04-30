package t.sandals.metrics

import com.codahale.metrics.MetricRegistry
import com.sandals.test.TestServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import kotlin.test.assertEquals


open class MetricsControllerTest() {
    private val server = TestServer(8887, "com.sandals.metrics,t.sandals.metrics")

    @Before
    fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        server.stop()
    }

    @Test
    fun testGetMetrics() {
        assertEquals("{}", RestTemplate().getForObject<String>("http://localhost:8887/api/test"))

        val response = RestTemplate().getForObject<String>("http://localhost:8887/api/metrics")
        assertTrue(response.contains("Meters"))
        assertTrue(response.contains("test-requests"))
        assertTrue(response.contains("count = 1"))
    }

    /// supporting

    @Configuration
    open class MetricsConfig {
        @Bean
        open fun metricRegistry() = MetricRegistry()
    }

    @RestController
    class TestController(metrics: MetricRegistry) {
        private val meter = metrics.meter("test-requests")

        @RequestMapping("/api/test")
        fun getTest(): String {
            meter.mark()
            return "{}"
        }
    }
}