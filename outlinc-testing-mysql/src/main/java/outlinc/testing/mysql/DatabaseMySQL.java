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

    public DatabaseMySQL startSampleDB() {
        return this.start(0, "classicmodels", "sample.sql");
    }

    public DatabaseMySQL startSampleDB(int port) {
        return this.start(port, "classicmodels", "sample.sql");
    }

    synchronized public DatabaseMySQL start(int port, String databaseName, String initSql) {
        if (database != null) {
            return this;
        }
        DBConfigurationBuilder builder = DBConfigurationBuilder.newBuilder();
        builder.addArg("--user=root"); /* Fix fatal error in docker environment */
        builder.setPort(port); /* Port 0: detect free port */
        try {
            database = DB.newEmbeddedDB(builder.build());
            database.start();
            if (databaseName != null && databaseName.length() > 0) {
                database.createDB(databaseName);
            }
            if (initSql != null && initSql.length() > 0) {
                database.source(initSql);
            }
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
