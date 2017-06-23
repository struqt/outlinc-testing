package outlinc.testing.mysql;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by wangkang on 23/06/2017
 */
public class DatabaseMySQLTest {

    @Test
    public void testConnection() throws SQLException {
        assertNotNull(database.getJdbcURL());
        Connection conn = DriverManager.getConnection(database.getJdbcURL());
        assertTrue(conn.isValid(5));
        conn.close();
    }

    static private DatabaseMySQL database;

    @BeforeClass
    static public void beforeAll() {
        database = new DatabaseMySQL().start("test");
    }

    @AfterClass
    static public void afterAll() {
        if (database != null) {
            database.stop();
            database = null;
        }
    }

}
