package com.htinshar.webfluxmicroservice;

import com.htinshar.webfluxmicroservice.dto.InputFailedValidateResponse;
import com.htinshar.webfluxmicroservice.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec06Exchange6 extends BaseTest {

    @Autowired
    private WebClient webClient;

    //exchange = retrieve + additional information (Http status code)
    public void badRequestTest() {
        Mono<Object> responseMono = this.webClient
                .get()
                .uri("/square/{input}/throw", 5)
                .exchangeToMono(this::exchange)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

    }

    private Mono<Object> exchange(ClientResponse cr) {
        if (cr.statusCode().value() == 400) {
            return cr.bodyToMono(InputFailedValidateResponse.class);
        } else {
            return cr.bodyToMono(Response.class);
        }

    }

}
