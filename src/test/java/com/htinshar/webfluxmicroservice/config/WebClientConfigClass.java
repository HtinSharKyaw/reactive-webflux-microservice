package com.htinshar.webfluxmicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfigClass {

    @Bean
    public WebClient webClient() {
        //ExchangeFilterFunction
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                //.defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("userName","password"))
                .filter((clientRequest,exchangeFunction)-> sessionTokenGenerator(clientRequest,exchangeFunction))
                //.filter((request, exchangeFunction) -> sessionToken(request, exchangeFunction))
                .build();
    }

//    private Mono<ClientResponse> sessionTokenGenerator(ClientRequest request, ExchangeFunction exchangeFunction){
//        System.out.printf("generating session token");
//        //request is immutable
//        ClientRequest clientRequest = ClientRequest.from(request).headers(httpHeader -> httpHeader.setBearerAuth("json-web-token")).build();
//        return exchangeFunction.exchange(clientRequest);
//    }

    private Mono<ClientResponse> sessionTokenGenerator(ClientRequest clientRequest,ExchangeFunction exchangeFunction){
        ClientRequest request = clientRequest.attribute("auth")
                .map(value -> value.equals("basic") ? withBasicAuth(clientRequest) : withBearAuth(clientRequest))
                .orElse(clientRequest);
        return exchangeFunction.exchange(request);
    }

    private ClientRequest withBasicAuth(ClientRequest clientRequest){
        return ClientRequest.from(clientRequest)
                .headers(httpHeader -> httpHeader.setBasicAuth("userName", "password"))
                .build();
    }

    private ClientRequest withBearAuth(ClientRequest clientRequest){
        return ClientRequest.from(clientRequest)
                .headers(httpHeader -> httpHeader.setBearerAuth("json-web-token"))
                .build();
    }



//    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction exchangeFunction) {
//        System.out.println("Generation Session Token");
//        ClientRequest clientRequest = ClientRequest.from(request).headers(header -> header.setBearerAuth("some-lengthy-jwt")).build();
//        return exchangeFunction.exchange(clientRequest);
//    }

//    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction exchangeFunction) {
//        //auth -> basic or oauth
//        ClientRequest clientRequest = request.attribute("auth")
//                .map(v -> v.equals("basic") ? withBasicAuthentication(request) : withOAuthAuthentication(request))
//                .orElse(request);
//        return exchangeFunction.exchange(clientRequest);
//    }
//
//    private ClientRequest withBasicAuthentication(ClientRequest clientRequest) {
//        return ClientRequest.from(clientRequest)
//                .headers(header -> header.setBasicAuth("username", "password"))
//                .build();
//    }
//
//    private ClientRequest withOAuthAuthentication(ClientRequest clientRequest){
//        return ClientRequest.from(clientRequest)
//                .headers(header -> header.setBearerAuth("some-token"))
//                .build();
//    }
}
