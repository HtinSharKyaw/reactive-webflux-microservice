package com.htinshar.webfluxmicroservice.controller;

import com.htinshar.webfluxmicroservice.dto.MultipleRequestDto;
import com.htinshar.webfluxmicroservice.dto.Response;
import com.htinshar.webfluxmicroservice.service.ReactiveMathService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.AbstractJackson2Encoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reactive-math")
@RequiredArgsConstructor
public class ReactiveMathController {
    private final ReactiveMathService reactiveMathService;

    @GetMapping("/square/{input}")
    public Mono<Response> findSquare(@PathVariable int input){
        return this.reactiveMathService.findSquare(input);
    }

    @GetMapping("/table/{input}")
    public Flux<Response> multiplicationTable(@PathVariable int input){
        return this.reactiveMathService.multiplicationTable(input);
    }


    @GetMapping(value = "/table/{input}/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> multiplicationTableStream(@PathVariable int input){
        return this.reactiveMathService.multiplicationTable(input);
    }

    @PostMapping("multiply")
    public Mono<Response> multiply(@RequestBody Mono<MultipleRequestDto> requestDtoMono,
                                   @RequestHeader Map<String,String> headers){
        System.out.println(headers);
        return this.reactiveMathService.multiply(requestDtoMono);
    }

}
