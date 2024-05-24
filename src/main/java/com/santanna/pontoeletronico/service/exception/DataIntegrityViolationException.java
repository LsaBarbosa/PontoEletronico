package com.santanna.pontoeletronico.service.exception;

public class DataIntegrityViolationException extends RuntimeException{
    public DataIntegrityViolationException(String msg){
        super(msg);
    }
}
