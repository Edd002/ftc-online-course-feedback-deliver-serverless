package fiap.tech.challenge.online.course.feedback.deliver.serverless.property;

import java.util.Properties;

public class DataSourceProperties {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public DataSourceProperties() {
        Properties config = EnvPropertiesLoader.loadProperties(getClass().getClassLoader());
        this.host = config.getProperty("spring.application.datasource.hostname");
        this.port = Integer.parseInt(config.getProperty("spring.application.datasource.port"));
        this.database = config.getProperty("spring.application.datasource.database");
        this.username = config.getProperty("spring.application.datasource.username");
        this.password = config.getProperty("spring.application.datasource.password");
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