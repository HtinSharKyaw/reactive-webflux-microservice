package com.htinshar.webfluxmicroservice.config;

import com.htinshar.webfluxmicroservice.dto.InputFailedValidateResponse;
import com.htinshar.webfluxmicroservice.dto.MultipleRequestDto;
import com.htinshar.webfluxmicroservice.dto.Response;
import com.htinshar.webfluxmicroservice.exception.InputValidationException;
import com.htinshar.webfluxmicroservice.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

    @Autowired
    private ReactiveMathService mathService;

    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        Integer input = Integer.valueOf(serverRequest.pathVariable("input"));
        Mono<Response> responseMono = this.mathService.findSquare(input);
        return ServerResponse.ok().body(responseMono, Response.class);

    }

    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
        Integer input = Integer.valueOf(serverRequest.pathVariable("input"));
        if (input < 10 || input > 20) {
//            InputFailedValidateResponse response = new InputFailedValidateResponse();
//            return ServerResponse.badRequest().bodyValue(response);
            return Mono.error(new InputValidationException(input));
        }
        Mono<Response> responseMono = this.mathService.findSquare(input);
        return ServerResponse.ok().body(responseMono, Response.class);

    }

    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
        Integer value = Integer.valueOf(serverRequest.pathVariable("input"));
        Flux<Response> responseFlux = this.mathService.multiplicationTable(value);
        return ServerResponse.ok()
                .body(responseFlux, Response.class);
        //server response containing flux response inside
        //you don't need to define Flux since you are planning to return Flux, the server Response type will not be Flux....
    }

    public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
        Integer value = Integer.valueOf(serverRequest.pathVariable("input"));
        Flux<Response> responseFlux = this.mathService.multiplicationTable(value);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, Response.class);
        //server response containing flux response inside
        //you don't need to define Flux since you are planning to return Flux, the server Response type will not be Flux....
    }

    public Mono<ServerResponse> tableStreamWithEvents(ServerRequest serverRequest) {
        Integer value = Integer.valueOf(serverRequest.pathVariable("value"));
        Flux<Response> responseFlux = this.mathService.multiplicationTable(value);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, Response.class);

    }

    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        Mono<MultipleRequestDto> requestDtoMono = serverRequest.bodyToMono(MultipleRequestDto.class);
        Mono<Response> multiply = this.mathService.multiply(requestDtoMono);
        return ServerResponse.ok().body(multiply, Response.class);
    }

    public Mono<ServerResponse> multiplyHandlerWithValidation(ServerRequest serverRequest){
        Mono<MultipleRequestDto> multipleRequestDtoMono = serverRequest.bodyToMono(MultipleRequestDto.class);
        Mono<Response> multiply = this.mathService.multiply(multipleRequestDtoMono);
        return ServerResponse.ok().body(multiply,Response.class);
    }
}
