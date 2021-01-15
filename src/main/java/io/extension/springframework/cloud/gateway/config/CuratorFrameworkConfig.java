package io.extension.springframework.cloud.gateway.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiangtiteng on 2019-06-15
 */
@Configuration
@EnableConfigurationProperties(CuratorFrameworkProperties.class)
public class CuratorFrameworkConfig {

    @Bean
    public CuratorFramework curatorFramework(CuratorFrameworkProperties properties) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        RetryPolicy retryPolicy = new RetryForever(5000);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(properties.getConnectionString(), retryPolicy);
        curatorFramework.start();
        return curatorFramework;
    }
}
