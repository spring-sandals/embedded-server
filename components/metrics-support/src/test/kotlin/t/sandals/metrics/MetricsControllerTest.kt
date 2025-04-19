package t.sandals.metrics

import com.codahale.metrics.MetricRegistry
import com.fasterxml.jackson.databind.ObjectMapper
import com.sandals.web.EmbeddedServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


open class MetricsControllerTest() {
    private var application = EmbeddedServer(
        8887,
        "com.sandals.metrics,t.sandals.metrics"
    )

    @Before
    fun setUp() {
        application.start()
    }

    @After
    fun tearDown() {
        application.stop()
    }

    @Test
    fun testGetMetrics() {
        val response = RestTemplate().getForObject<String>("http://localhost:8887/api/metrics")
        assertTrue(response.contains("Meters"))
        assertTrue(response.contains("test-requests"))
    }

    @Configuration
    @EnableWebMvc
    open class WebConfig : WebMvcConfigurer {
        override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
            converters.add(MappingJackson2HttpMessageConverter(ObjectMapper()))
        }

        @Bean
        open fun metricRegistry() = MetricRegistry()
    }

    @RestController
    class TestController(metrics: MetricRegistry) {
        private val meter = metrics.meter("test-requests")

        @RequestMapping("/api/test")
        fun getTest() = "{}"
    }
}