package com.htinshar.webfluxmicroservice.controller;

import com.htinshar.webfluxmicroservice.dto.Response;
import com.htinshar.webfluxmicroservice.exception.InputValidationException;
import com.htinshar.webfluxmicroservice.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
public class ReactiveMathValidationController {
    @Autowired
    private ReactiveMathService reactiveMathService;

    @GetMapping("/square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input) {
        if (input < 10 || input > 20) {
            throw new InputValidationException(input);
        }
        return this.reactiveMathService.findSquare(input);
    }

    @GetMapping("/square/{input}/mono-error")
    public Mono<Response> monoError(@PathVariable int input) {
        return Mono.just(input)
                .handle((value, responseSynchronousSink) -> {
                    if (value >= 10 && value <= 20) {
                        responseSynchronousSink.next(value);
                    } else {
                        responseSynchronousSink.error(new InputValidationException(value));
                    }
                })
                .cast(Integer.class)
                .flatMap(i -> this.reactiveMathService.findSquare(i));
    }

    @GetMapping("/square/{input}/badRequest")
    public Mono<ResponseEntity<Response>> badRequest(@PathVariable int input) {
        return Mono.just(input)
                .filter( i -> i > 10 && i < 20)
                .flatMap( i -> this.reactiveMathService.findSquare(i))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
