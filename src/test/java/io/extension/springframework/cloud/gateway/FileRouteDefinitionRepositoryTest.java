package io.extension.springframework.cloud.gateway;

import io.extension.springframework.cloud.gateway.route.FileRouteDefinitionRepository;
import io.extension.springframework.cloud.gateway.route.ZookeeperRouteDefinitionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * Created by jiangtiteng on 2021/1/15
 */
@SpringBootApplication(exclude = TestApplication.class)
public class FileRouteDefinitionRepositoryTest {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public FileRouteDefinitionRepository fileRouteDefinitionRepository() throws IOException {
        return new FileRouteDefinitionRepository("./config");
    }

}
