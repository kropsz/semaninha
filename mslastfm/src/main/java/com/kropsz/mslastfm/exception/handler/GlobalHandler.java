package com.kropsz.mslastfm.exception.handler;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kropsz.mslastfm.exception.ValueRequiredException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request,
            BindingResult result) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campos Inválidos: ", result));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> unexpectedErrorException(RuntimeException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler({ IOException.class, URISyntaxException.class })
    public ResponseEntity<ErrorMessage> ioException(Exception ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(ValueRequiredException.class)
    public ResponseEntity<ErrorMessage> nullOrEmptyException(ValueRequiredException ex, HttpServletRequest request) {
        log.error("Api Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

}