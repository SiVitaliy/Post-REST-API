package com.example.demo.util;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String m){
        super(m);
    }
}
