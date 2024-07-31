package com.pulse.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.pulse.exception.custom.CustomException;
import com.pulse.exception.custom.EntityNotFoundException;
import com.pulse.exception.custom.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandler {

    @Value("${error.message.bad-credentials}")
    private String badCredentialsMessage;
    @Value("${error.message.disabled-user}")
    private String disabledUserMessage;

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> HandleRunTimeException(RuntimeException e){
        System.out.println(e.getClass());
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("message",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e){
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException e){
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.FORBIDDEN);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e){
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e){
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("message", badCredentialsMessage), HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException e){
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("message", disabledUserMessage),HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<?> handleJWTVerificationException(JWTDecodeException e){
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        e.printStackTrace();
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
    }
}
