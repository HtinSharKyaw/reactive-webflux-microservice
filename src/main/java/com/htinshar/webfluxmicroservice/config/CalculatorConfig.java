package com.htinshar.webfluxmicroservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class CalculatorConfig {

    @Autowired
    private CalculatorHandler calculatorHandler;

    @Bean
    public RouterFunction<ServerResponse> higherLevelFunction() {
        return RouterFunctions.route()
                .path("calculator", this::calculateCalculator)
                .build();
    }

    public RouterFunction<ServerResponse> calculateCalculator() {
        return RouterFunctions.route()
                .GET("{a}/{b}", isOperation("+"), request -> calculatorHandler.additionalHandler(request))
                .GET("{a}/{b}", isOperation("-"), request -> calculatorHandler.subtractHandler(request))
                .GET("{a}/{b}", isOperation("*"), request -> calculatorHandler.multiplyHandler(request))
                .GET("{a}/{b}", isOperation("/"), request -> calculatorHandler.divideHandler(request))
                .GET("{a}/{b}", req -> ServerResponse.badRequest().bodyValue("OP should be + - * / "))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> calculateValue(){
        return RouterFunctions
                .route()
                .GET("{a}/{b}", request -> calculatorHandler.additionalHandler(request))
                .build();
    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(header -> operation.equalsIgnoreCase(header.asHttpHeaders()
                .toSingleValueMap()
                .get("OP")));
    }

}
