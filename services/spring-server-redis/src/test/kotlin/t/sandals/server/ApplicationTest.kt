package t.sandals.server

import com.sandals.accounts.Account
import com.sandals.jdbc.dataSource
import com.sandals.web.EmbeddedServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

class ApplicationTest {
    private val application = EmbeddedServer(8887, "com.sandals")
    private val restTemplate = RestTemplate()
    private val dataSource = dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1)
    private val template = JdbcTemplate(dataSource)

    @Before
    fun setUp() {
        template.execute("delete from accounts")
        application.start()
    }

    @After
    fun tearDown() {
        application.stop()
    }

    @Test
    fun testStart() {
        template.execute("delete from accounts")
        template.update(
            """
            insert into accounts (id, name, value)
             values
             (12001, 'John''s Grocery, Inc.', 6000000),
             (12002, 'Hamburg Inn No. 2', 0),
             (12003, 'Record Collector', 1400000)
        """
        )

        val endpoint = "http://localhost:8887"

        val response = restTemplate.getForObject<String>(endpoint)
        assertTrue(response.contains("Spring Sandals"))

        val about = restTemplate.getForObject<String>("$endpoint/about.html")
        assertTrue(about.contains("unstyled about!"))

        val metrics = restTemplate.getForObject<String>("$endpoint/api/metrics")
        assertTrue(metrics.contains("index-requests"))
        assertTrue(metrics.contains("count = 1"))

        val account = restTemplate.getForObject<Account>("$endpoint/api/accounts/12002")
        Assert.assertEquals("Hamburg Inn No. 2", account.name)

        Assert.assertEquals(1, template.update("update accounts set name = ? where id = ?", "Hamburg Inn No. 4", 12002))
        val stale = RestTemplate().getForObject<Account>("http://localhost:8887/api/accounts/12002")
        Assert.assertEquals("Hamburg Inn No. 2", stale.name)
    }
}