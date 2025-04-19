package t.sandals.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.sandals.jdbc.dataSource
import com.sandals.web.EmbeddedServer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


open class AccountsControllerTest() {
    private val application = EmbeddedServer(
        8887,
        "com.sandals.accounts,com.sandals.server,t.sandals.server"
    )
    private val dataSource = dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1)
    private val template = JdbcTemplate(dataSource)

    @Test
    fun testGetAccounts() {
        application.start()
        template.execute("delete from accounts")
        template.update(
            """
            insert into accounts (name, total_contract_value)
             values
             ('John''s Grocery, Inc.', 6000000),
             ('Hamburg Inn No. 2', 0),
             ('Record Collector', 1400000)
        """
        )

        val response = RestTemplate().getForObject<String>("http://localhost:8887/api/accounts")
        val accounts = ObjectMapper().readValue(response, List::class.java)
        assertEquals(3, accounts.size)
        application.stop()
    }

    @Configuration
    @EnableWebMvc
    open class WebConfig : WebMvcConfigurer {
        override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
            converters.add(MappingJackson2HttpMessageConverter(ObjectMapper()))
        }
    }

    @Configuration
    open class DataSourceConfig {
        @Bean
        open fun getJdbcTemplate(): JdbcTemplate {
            return JdbcTemplate(dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1))
        }
    }
}