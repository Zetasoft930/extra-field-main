package com.fieldright.fr.util.exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(String msg){
        super(msg);
    }
}