package t.sandals.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.sandals.jdbc.dataSource
import com.sandals.test.TestServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


open class AccountsControllerTest() {
    private val testServer = TestServer(8887, "com.sandals.accounts,com.sandals.server,t.sandals.server")
    private val dataSource = dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1)
    private val template = JdbcTemplate(dataSource)

    @Before
    fun setUp() {
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
        testServer.start()
    }

    @After
    fun tearDown() {
        testServer.stop()
    }

    @Test
    fun testGetAccounts() {
        val response = RestTemplate().getForObject<String>("http://localhost:8887/api/accounts")
        val accounts = ObjectMapper().readValue(response, List::class.java)
        assertEquals(3, accounts.size)
    }

    /// supporting

    @Configuration
    open class DataSourceConfig {
        @Bean
        open fun getJdbcTemplate(): JdbcTemplate {
            return JdbcTemplate(dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1))
        }
    }
}