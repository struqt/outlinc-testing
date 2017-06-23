package outlinc.testing.mysql;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangkang on 23/06/2017
 */
public class DatabaseMySQL {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMySQL.class);
    private DB database = null;
    private String jdbcURL;

    public String getJdbcURL() {
        return jdbcURL;
    }

    public int getPort() {
        return null == database ? -1 : database.getConfiguration().getPort();
    }

    public DatabaseMySQL start(String databaseName) {
        return start(0, databaseName);
    }

    synchronized public DatabaseMySQL start(int port, String databaseName) {
        if (database != null) {
            return this;
        }
        DBConfigurationBuilder builder = DBConfigurationBuilder.newBuilder();
        builder.setPort(port); /* Port 0: detect free port */
        try {
            database = DB.newEmbeddedDB(builder.build());
            database.start();
            database.createDB(databaseName);
            jdbcURL = builder.getURL(databaseName);
        } catch (ManagedProcessException e) {
            log.error(e.getMessage(), e);
            database = null;
            return this;
        }
        return this;
    }

    synchronized public void stop() {
        if (database == null) {
            return;
        }
        try {
            database.stop();
        } catch (ManagedProcessException e) {
            log.error(e.getMessage(), e);
        }
    }

}
