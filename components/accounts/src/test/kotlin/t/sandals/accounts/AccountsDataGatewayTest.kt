package t.sandals.accounts

import com.sandals.accounts.AccountsDataGateway
import com.sandals.jdbc.dataSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate

open class AccountsDataGatewayTest {
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
    }

    @Test
    fun testGetAccounts() {
        val accounts = AccountsDataGateway(template).getAccounts()
        assertEquals(3, accounts.size)
    }

    @Test
    fun testGetAccount() {
        val account = AccountsDataGateway(template).getAccount(12002)
        assertEquals("Hamburg Inn No. 2", account!!.name)
    }
}