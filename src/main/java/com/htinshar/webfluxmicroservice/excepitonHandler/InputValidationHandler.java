package com.htinshar.webfluxmicroservice.excepitonHandler;

import com.htinshar.webfluxmicroservice.dto.InputFailedValidateResponse;
import com.htinshar.webfluxmicroservice.exception.InputValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InputValidationHandler {
    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<InputFailedValidateResponse> handleException(InputValidationException ex){
        InputFailedValidateResponse inputFailedValidateResponse = new InputFailedValidateResponse();
        inputFailedValidateResponse.setErrCode(ex.getErrorCode());
        inputFailedValidateResponse.setInput(ex.getInput());
        inputFailedValidateResponse.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(inputFailedValidateResponse);
    }
}
