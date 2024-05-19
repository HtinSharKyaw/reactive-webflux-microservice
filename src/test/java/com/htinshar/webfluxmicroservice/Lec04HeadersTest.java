package com.htinshar.webfluxmicroservice;

import com.htinshar.webfluxmicroservice.dto.MultipleRequestDto;
import com.htinshar.webfluxmicroservice.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04HeadersTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void headerTest() {
        Mono<Response> responseMono = this.webClient
                .post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(2, 3))
                .headers(header -> header.set("someKey", "someValue"))
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();

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


}
