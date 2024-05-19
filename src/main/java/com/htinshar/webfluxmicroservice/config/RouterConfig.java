package com.htinshar.webfluxmicroservice.config;

import com.htinshar.webfluxmicroservice.dto.InputFailedValidateResponse;
import com.htinshar.webfluxmicroservice.exception.InputValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Configuration
public class RouterConfig {

    @Autowired
    private RequestHandler requestHandler;

    @Bean
    public RouterFunction<ServerResponse> highLevelRouterFunction() {
        return RouterFunctions.route()
                .path("router", this::serverResponseRouterFunction)
                .build();
    }


    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")), requestHandler::squareHandler)
                .GET("square/{input}",request -> ServerResponse.badRequest().bodyValue("only 10 - 19 allowed"))
                .GET("table/{input}", request -> requestHandler.tableHandler(request))
                .GET("table/{input}/eventStream", request -> requestHandler.tableStreamHandler(request))
                .GET("table/{value}/streaming", request -> requestHandler.tableStreamWithEvents(request))
                .POST("multiply", request -> requestHandler.multiplyHandler(request))
                .GET("square/{input}/validation", request -> requestHandler.squareHandlerWithValidation(request))
                .onError(InputValidationException.class, exceptionHandler())
                .build();
    }


    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, req) -> {
            InputValidationException exception = (InputValidationException) err;
            InputFailedValidateResponse response = new InputFailedValidateResponse();
            response.setMessage(exception.getMessage());
            response.setInput(exception.getInput());
            response.setErrCode(exception.getErrorCode());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }

}
