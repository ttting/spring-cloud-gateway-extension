package io.extension.springframework.cloud.gateway;

import io.extension.springframework.cloud.gateway.route.ZookeeperRouteDefinitionRepository;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by jiangtiteng on 2021/1/15
 */
@SpringBootApplication
public class TestApplication {

    @Autowired
    private CuratorFramework curatorFramework;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public ZookeeperRouteDefinitionRepository zookeeperRouteDefinitionRepository() throws Exception {
        return new ZookeeperRouteDefinitionRepository(curatorFramework, "/gt/config");
    }
}
