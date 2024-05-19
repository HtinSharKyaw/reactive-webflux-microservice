package com.htinshar.webfluxmicroservice.config;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CalculatorHandler {

    //creating multiple handler intentionally
    public Mono<ServerResponse> additionalHandler(ServerRequest serverRequest) {
        int firstNumber = getValue(serverRequest, "a");
        int secondNumber = getValue(serverRequest, "b");
        return ServerResponse.ok().bodyValue(firstNumber + secondNumber);
    }

    public Mono<ServerResponse> subtractHandler(ServerRequest serverRequest) {
        int firstNumber = getValue(serverRequest, "a");
        int secondNumber = getValue(serverRequest, "b");
        return ServerResponse.ok().bodyValue(firstNumber - secondNumber);
    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        int firstNumber = getValue(serverRequest, "a");
        int secondNumber = getValue(serverRequest, "b");
        return ServerResponse.ok().bodyValue(firstNumber * secondNumber);
    }

    public Mono<ServerResponse> divideHandler(ServerRequest serverRequest) {
        int firstNumber = getValue(serverRequest, "a");
        int secondNumber = getValue(serverRequest, "b");
        return ServerResponse.ok().bodyValue(firstNumber / secondNumber);
    }

    private int getValue(ServerRequest serverRequest, String key) {
        return Integer.parseInt(serverRequest.pathVariable(key));
    }
}
