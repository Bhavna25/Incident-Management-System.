package com.rapifuzz.exceptions;

import com.rapifuzz.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handlerBusinessException(BusinessException ex){
        String message = ex.getMessage();
        ApiResponse res =ApiResponse.builder().message(message).success(false).httpStatus(HttpStatus.FORBIDDEN).build();
        return new ResponseEntity<>(res,HttpStatus.FORBIDDEN);
    }

}
