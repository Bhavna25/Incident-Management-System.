package com.rapifuzz.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException(){
        super("Business Exception occurs...");
    }

    public BusinessException(String message){
        super(message);
    }
}
