package io.extension.springframework.cloud.gateway.route;

import com.fasterxml.jackson.core.type.TypeReference;
import io.extension.springframework.cloud.gateway.utils.JacksonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;

/**
 * Created by jiangtiteng on 2021/1/15
 */
public class ZookeeperRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {

    private CuratorFramework curatorFramework;

    private String routeDefinitionConfigPath;

    private ApplicationEventPublisher applicationEventPublisher;

    private final Map<String, RouteDefinition> routes = synchronizedMap(
            new LinkedHashMap<String, RouteDefinition>());

    private static final Log log = LogFactory.getLog(ZookeeperRouteDefinitionRepository.class);


    public ZookeeperRouteDefinitionRepository(CuratorFramework curatorFramework, String routeDefinitionConfigPath) throws Exception {
        this.curatorFramework = curatorFramework;
        this.routeDefinitionConfigPath = routeDefinitionConfigPath;
        load();
        initWatcher(this.routeDefinitionConfigPath);
    }

    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(this.routes.values());
    }

    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition -> {
            routes.put(routeDefinition.getId(), routeDefinition);
            try {
                saveToZookeeper(new LinkedList<>(this.routes.values()));
            } catch (Exception e) {
                return Mono.defer(() -> Mono.error(e));
            }
            return Mono.empty();
        });
    }

    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(s -> {
            try {
                load();
                this.routes.remove(s);
                saveToZookeeper(new LinkedList<>(this.routes.values()));
                return Mono.empty();
            } catch (Exception e) {
                return Mono.defer(() -> Mono.error(e));
            }
        });

    }

    private void saveToZookeeper(List<RouteDefinition> routeDefinitions) throws Exception {
        String data = JacksonUtils.getInstance().writeValueAsString(routeDefinitions);
        curatorFramework.setData().forPath(this.routeDefinitionConfigPath, data.getBytes());
    }

    private Map<String, RouteDefinition> load() throws Exception {
        List<RouteDefinition> routeDefinitions;
        Stat forPath = curatorFramework.checkExists().forPath(this.routeDefinitionConfigPath);
        if (forPath != null) {
            byte[] configBytes = curatorFramework.getData().forPath(this.routeDefinitionConfigPath);
            routeDefinitions = JacksonUtils.getInstance().readValue(new String(configBytes), new TypeReference<List<RouteDefinition>>() {
            });

            this.routes.clear();
            this.routes.putAll(routeDefinitions.stream().collect(Collectors.toMap(RouteDefinition::getId, e -> e)));
            return routes;
        } else {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.routeDefinitionConfigPath);
            return routes;
        }

    }

    private void initWatcher(String configPath) {
        NodeCache nodeCache = new NodeCache(curatorFramework, configPath);
        try {
            nodeCache.start();
            nodeCache.getListenable().addListener(() -> {
                load();
                this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            });
        } catch (Exception e) {
            log.error("init  watcher error", e);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
