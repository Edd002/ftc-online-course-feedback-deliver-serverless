package fiap.tech.challenge.online.course.feedback.deliver.serverless.config;

import java.util.Properties;

public class DataSourceProperties {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public DataSourceProperties() {
        Properties config = EnvPropertiesLoader.loadProperties(getClass().getClassLoader());
        this.host = config.getProperty("application.datasource.hostname");
        this.port = Integer.parseInt(config.getProperty("application.datasource.port"));
        this.database = config.getProperty("application.datasource.database");
        this.username = config.getProperty("application.datasource.username");
        this.password = config.getProperty("application.datasource.password");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getJdbcUrl() {
        return "jdbc:postgresql://" + this.getHost() + ":" + this.getPort() + "/" + this.getDatabase();
    }
}