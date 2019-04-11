package cn.goktech.dolphin.gateway.filter;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author funcas
 * @version 1.0
 * @date 2018年10月14日
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    public static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("request = {}", JSON.toJSON(exchange.getRequest()));
        List<String> tkList = exchange.getRequest().getHeaders().get("Authorization");
        // TODO: 2019-04-04 判断ignore url 
        if(tkList == null || tkList.size() == 0) {
            return chain.filter(exchange);
        }

        // 向headers中放文件，记得build
        ServerHttpRequest host = exchange
                .getRequest()
                .mutate()
                .header("Authorization", tkList != null ? tkList.get(0) : "")
                .build();
        //将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
