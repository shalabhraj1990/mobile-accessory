package com.spring.boot.accessory.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestTimeLoggingFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		long startTime = System.currentTimeMillis();
		return chain.filter(exchange).doOnSuccess(response -> {
			log.error("Total time to process :" + (System.currentTimeMillis() - startTime));
		}).doOnError(th -> {
			log.error("error :" + th.getMessage());
			log.error("Total time to process :" + (System.currentTimeMillis() - startTime));
		});
	}

}
