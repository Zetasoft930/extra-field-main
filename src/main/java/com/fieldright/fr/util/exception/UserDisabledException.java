package com.fieldright.fr.util.exception;

public class UserDisabledException extends RuntimeException{

    public UserDisabledException(String msg){
        super(msg);
    }

    public UserDisabledException(){
        super();
    }
}
