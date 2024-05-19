package com.htinshar.webfluxmicroservice;

import com.htinshar.webfluxmicroservice.dto.MultipleRequestDto;
import com.htinshar.webfluxmicroservice.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec05BadRequest extends BaseTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void badRequestTest() {
        Mono<Response> responseMono = this.webClient
                .get()
                .uri("/square/{input}/throw", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .doOnNext(System.out::println)
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(responseMono)
                .verifyError(WebClientResponseException.BadRequest.class);

    }

    private MultipleRequestDto buildRequestDto(int a, int b) {
        MultipleRequestDto multipleRequestDto = new MultipleRequestDto();
        multipleRequestDto.setFirst(a);
        multipleRequestDto.setSecond(b);
        return multipleRequestDto;
    }
}
