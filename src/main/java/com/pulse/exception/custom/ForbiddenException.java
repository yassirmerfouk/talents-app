package com.pulse.exception.custom;

public class ForbiddenException extends RuntimeException{

    public ForbiddenException(){
        super("You have no permission for this action.");
    }

    public ForbiddenException(String msg){
        super(msg);
    }
}
