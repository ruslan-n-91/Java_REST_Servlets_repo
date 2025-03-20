package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for managing a database connection.
 */
public final class ConnectionManagerImpl implements ConnectionManager {
    private final Properties properties = new Properties();

    private static final String DB_PROPERTIES_FILE = "db.properties";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_URL = "db.url";
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";

    /**
     * Creates a connection manager and loads the database parameters from the file in the resources folder.
     */
    public ConnectionManagerImpl() {
        loadProperties();
        loadDriver(getProperty(DB_DRIVER));
    }

    /**
     * Creates a connection manager and uses passed parameters for the database connection.
     */
    public ConnectionManagerImpl(String driver, String url, String username, String password) {
        properties.setProperty(DB_DRIVER, driver);
        properties.setProperty(DB_URL, url);
        properties.setProperty(DB_USERNAME, username);
        properties.setProperty(DB_PASSWORD, password);
        loadDriver(getProperty(DB_DRIVER));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                getProperty(DB_URL),
                getProperty(DB_USERNAME),
                getProperty(DB_PASSWORD));
    }

    private String getProperty(String property) {
        return properties.getProperty(property);
    }

    private void loadProperties() {
        try (InputStream dbPropertiesFile = ConnectionManagerImpl.class
                .getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE)) {
            properties.load(dbPropertiesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadDriver(String databaseDriver) {
        try {
            Class.forName(databaseDriver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
