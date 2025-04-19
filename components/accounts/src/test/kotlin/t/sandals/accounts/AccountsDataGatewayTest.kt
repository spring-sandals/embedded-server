package t.sandals.accounts

import com.sandals.accounts.AccountsDataGateway
import com.sandals.jdbc.dataSource
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate

open class AccountsDataGatewayTest {
    private val dataSource = dataSource("jdbc:postgresql://localhost/sandals_test?user=initialdev&password=initialdev", 1)
    private val template = JdbcTemplate(dataSource)

    @Test
    fun testGetAccounts() {
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

        val accounts = AccountsDataGateway(template).getAccounts()
        assertEquals(3, accounts.size)
    }
}