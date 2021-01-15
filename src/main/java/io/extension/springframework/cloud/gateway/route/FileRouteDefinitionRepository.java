package io.extension.springframework.cloud.gateway.route;

import com.fasterxml.jackson.core.type.TypeReference;
import io.extension.springframework.cloud.gateway.utils.JacksonUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiangtiteng on 2021/1/13
 */
public class FileRouteDefinitionRepository implements RouteDefinitionRepository {

    private List<RouteDefinition> routeDefinitionList = new ArrayList<>();

    private String configPath;

    public FileRouteDefinitionRepository(String configPath) throws IOException {
        this.configPath = configPath;
        load();
    }

    private void load() throws IOException {
        String jsonStr = Files.lines(Paths.get(configPath)).collect(Collectors.joining());
        this.routeDefinitionList = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<RouteDefinition>>() {
        });
    }

    @PostConstruct
    public void init() throws IOException {
        load();
    }

    @EventListener
    public void listenEvent(RefreshRoutesEvent event) throws IOException {
        load();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routeDefinitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
    }
}
