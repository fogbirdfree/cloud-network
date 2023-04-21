package com.dongruan.graduation.networkdiskgateway.config;

import com.alibaba.fastjson.JSON;
import com.dongruan.graduation.networkdiskssoclient.rpc.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthorityGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        if (cookies.get("user") == null || "".equals(cookies.get("user"))) {
            Result data = Result.create();
            data.setCode(401);
            data.setMessage("未登录！请登录后继续访问！");
            ServerHttpResponse response = exchange.getResponse();
            DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONString(data).getBytes(StandardCharsets.UTF_8));
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}