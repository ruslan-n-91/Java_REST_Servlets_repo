package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for managing a database connection. Thread safe singleton pattern was used.
 */
public final class ConnectionManagerImpl implements ConnectionManager {
    private static final Properties DB_PROPERTIES = new Properties();
    private static final String DB_PROPERTIES_FILE = "db.properties";

    private static final String DB_DRIVER = "db.driver";
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";

    private static volatile ConnectionManagerImpl instance;

    private ConnectionManagerImpl() {
        loadProperties();
        loadDriver(getProperty(DB_DRIVER));
    }

    public static ConnectionManagerImpl getInstance() {
        ConnectionManagerImpl localInstance = instance;

        if (localInstance == null) {
            synchronized (ConnectionManagerImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConnectionManagerImpl();
                }
            }
        }

        return localInstance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                getProperty(DB_URL),
                getProperty(DB_USERNAME),
                getProperty(DB_PASSWORD));
    }

    private void loadDriver(String databaseDriver) {
        try {
            Class.forName(databaseDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadProperties() {
        try (InputStream dbPropertiesFile = ConnectionManagerImpl.class
                .getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE)) {
            DB_PROPERTIES.load(dbPropertiesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getProperty(String key) {
        return DB_PROPERTIES.getProperty(key);
    }
}
