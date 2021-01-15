package io.extension.springframework.cloud.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by jiangtiteng on 2019-06-15
 */
@ConfigurationProperties(prefix = "spring.curator")
public class CuratorFrameworkProperties {
    /**
     * connectionString
     */
    private String connectionString;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
