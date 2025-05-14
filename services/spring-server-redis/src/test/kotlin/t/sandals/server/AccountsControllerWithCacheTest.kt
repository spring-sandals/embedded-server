package t.sandals.server

import com.sandals.accounts.Account
import com.sandals.jdbc.dataSource
import com.sandals.test.TestServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject


open class AccountsControllerWithCacheTest {
    private val testServer = TestServer(8887, "com.sandals.accounts,com.sandals.server,t.sandals.server")
    private val dataSource = dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1)
    private val template = JdbcTemplate(dataSource)

    @Before
    fun setUp() {
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
        testServer.start()
    }

    @After
    fun tearDown() {
        testServer.stop()
    }

    @Test
    fun testGetAccount() {
        val account = RestTemplate().getForObject<Account>("http://localhost:8887/api/accounts/12002")
        assertEquals("Hamburg Inn No. 2", account.name)

        assertEquals(1, template.update("update accounts set name = ? where id = ?", "Hamburg Inn No. 4", 12002))
        val stale = RestTemplate().getForObject<Account>("http://localhost:8887/api/accounts/12002")
        assertEquals("Hamburg Inn No. 2", stale.name)
    }

    /// supporting

    @Configuration
    open class DataSourceConfig {
        @Bean
        open fun getJdbcTemplate(): JdbcTemplate {
            return JdbcTemplate(dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1))
        }
    }

    @Configuration
    open class RedisConfig {
        @Bean
        open fun getRedisTemplate(): RedisTemplate<Long, Account> {
            val factory = JedisConnectionFactory()
            factory.start()

            val redisTemplate = RedisTemplate<Long, Account>()
            redisTemplate.connectionFactory = factory
            return redisTemplate
        }
    }
}