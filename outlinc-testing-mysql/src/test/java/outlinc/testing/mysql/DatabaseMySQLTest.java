package outlinc.testing.mysql;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.*;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseMySQLTest {

    @Test
    public void test01_connection() throws SQLException {
        assertNotNull(database.getJdbcURL());
        Connection conn = DriverManager.getConnection(database.getJdbcURL());
        assertTrue(conn.isValid(5));
        conn.close();
    }

    @Test
    public void test02_sql_query() throws SQLException {
        Connection conn = DriverManager.getConnection(database.getJdbcURL());
        assertTrue(conn.isValid(5));
        final int limit = 10;
        PreparedStatement statement = conn.prepareStatement("" +
            "select `orderDate` `orderDate`, count(*) `orderCount` " +
            "from `orders` " +
            "group by `orderDate` " +
            "having `orderCount`>1 " +
            "order by `orderCount` desc " +
            "limit ?");
        statement.setInt(1, 10);
        ResultSet resultSet = statement.executeQuery();
        int count = 0;
        while (resultSet.next()) {
            ++count;
        }
        assertEquals(limit, count);
        resultSet.close();
        statement.close();
        conn.close();
    }


    static private DatabaseMySQL database;

    @BeforeClass
    static public void beforeAll() throws SQLException {
        database = new DatabaseMySQL().startSampleDB(63306);
        DriverManager.getConnection(database.getJdbcURL()).close();
    }

    @AfterClass
    static public void afterAll() {
        if (database != null) {
            database.stop();
            database = null;
        }
    }

}
