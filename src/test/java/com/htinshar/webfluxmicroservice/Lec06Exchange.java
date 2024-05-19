package com.htinshar.webfluxmicroservice;

import com.htinshar.webfluxmicroservice.dto.InputFailedValidateResponse;
import com.htinshar.webfluxmicroservice.dto.MultipleRequestDto;
import com.htinshar.webfluxmicroservice.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.xmlunit.builder.Input;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec06Exchange extends BaseTest{

    @Autowired
    private WebClient webClient;
    //exchange = retrieve + additional information
    @Test
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

    private MultipleRequestDto buildRequestDto(int a, int b) {
        MultipleRequestDto multipleRequestDto = new MultipleRequestDto();
        multipleRequestDto.setFirst(a);
        multipleRequestDto.setSecond(b);
        return multipleRequestDto;
    }

    private Mono<Object> exchange(ClientResponse clientResponse){
        if(clientResponse.statusCode().value()==400)
            return clientResponse.bodyToMono(InputFailedValidateResponse.class);
        else
            return clientResponse.bodyToMono(Response.class);
    }

}
