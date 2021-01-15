package io.extension.springframework.cloud.gateway;

import io.extension.springframework.cloud.gateway.route.ZookeeperRouteDefinitionRepository;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jiangtiteng on 2021/1/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ZookeeperRouteDefinitionRepositoryTest {
    @Autowired
    private CuratorFramework curatorFramework;

    @Test
    public void testLoadConfig() throws Exception {
        ZookeeperRouteDefinitionRepository zrp = new ZookeeperRouteDefinitionRepository(curatorFramework,"/gateway/config");
        System.out.println(zrp.getRouteDefinitions());
    }
}
