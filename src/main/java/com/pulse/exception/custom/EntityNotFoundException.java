package com.pulse.exception.custom;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String msg){
        super(msg);
    }
}
