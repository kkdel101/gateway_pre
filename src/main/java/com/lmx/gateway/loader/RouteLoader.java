package com.lmx.gateway.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 项目启动时，动态加载所有的路由
 */

@Component
public class RouteLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteLoader.class);


    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;


    public List<RouteDefinition> getAllRoutes() {
        List<RouteDefinition> result = new ArrayList<>();
        Flux<RouteDefinition> routeDefinitions = routeDefinitionLocator.getRouteDefinitions();
        routeDefinitions.subscribe(routeDefinition -> {
            result.add(routeDefinition);
        });
        return result;
    }


    /**
     *
     * @param id
     * @param currentPath  需要拦截的请求路径
     * @param targetPath   需要转发过去的请求路径
     * @param targetUri    转发的目的地址
     */
    public String addRoute(String id, String currentPath, String targetPath, String targetUri) {

        //新建路由
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(id);


        //设置对应的预言
        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        PredicateDefinition predicateDefinition = new PredicateDefinition(String.format("Path=%s", currentPath));
        predicateDefinitions.add(predicateDefinition);
        routeDefinition.setPredicates(predicateDefinitions);

        //设置对应的过滤器
//        List<FilterDefinition> filterDefinitions = new ArrayList<>();
//        String filterStr = String.format("RewritePath=%s,%s", currentPath, targetPath);
//        FilterDefinition filterDefinition = new FilterDefinition(filterStr);
//        filterDefinitions.add(filterDefinition);
//        routeDefinition.setFilters(filterDefinitions);

        try {
            URI targetUrl = new URI(targetUri);
            routeDefinition.setUri(targetUrl);
        } catch (URISyntaxException e) {
          LOGGER.error("URI解析失败{}",e);
        }
        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        return id;
    }

}
