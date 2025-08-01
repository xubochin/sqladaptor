package com.sqladaptor.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private final Properties properties;
    
    public DatabaseConfig() {
        properties = new Properties();
        loadProperties();
    }
    
    private void loadProperties() {
        String configFileName = "application.properties";
        if (System.getProperty("test") != null) {
            configFileName = "application-test.properties";
        }
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            // 使用默认配置
        }
        
        // 设置默认值
        properties.putIfAbsent("database.type", "sqlite");
        properties.putIfAbsent("database.path", "target/redis_adapter.db");
        properties.putIfAbsent("database.host", "localhost");
        properties.putIfAbsent("database.port", "21212");
        properties.putIfAbsent("database.username", "");
        properties.putIfAbsent("database.password", "");
    }
    
    public String getDatabaseType() {
        return properties.getProperty("database.type");
    }
    
    public String getDatabasePath() {
        return properties.getProperty("database.path");
    }
    
    public String getHost() {
        return properties.getProperty("database.host");
    }
    
    public int getPort() {
        return Integer.parseInt(properties.getProperty("database.port"));
    }
    
    public String getUsername() {
        return properties.getProperty("database.username");
    }
    
    public String getPassword() {
        return properties.getProperty("database.password");
    }
}
