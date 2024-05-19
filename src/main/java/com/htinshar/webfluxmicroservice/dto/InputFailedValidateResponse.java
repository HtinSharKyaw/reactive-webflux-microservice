package com.htinshar.webfluxmicroservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputFailedValidateResponse {
    private int errCode;
    private int input;
    private String message;
}
