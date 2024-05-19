package com.htinshar.webfluxmicroservice;

import com.htinshar.webfluxmicroservice.dto.MultipleRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04HeaderTesting extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void testHeader(){
        Mono<MultipleRequestDto> dtoMono = this.webClient
                .post()
                .uri("/reactive-math/multiply")
                .bodyValue(calculateMultipleRequestDto(2, 3))
                .headers(header -> header.set("someKey", "value"))
                .retrieve()
                .bodyToMono(MultipleRequestDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(dtoMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    public MultipleRequestDto calculateMultipleRequestDto(int a, int b) {
        MultipleRequestDto dto = new MultipleRequestDto();
        dto.setFirst(a);
        dto.setSecond(b);
        return dto;
    }
}
