package com.kropsz.mslastfm.exception;

public class JsonConverterException extends RuntimeException {

    public JsonConverterException(String message) {
        super(message);
    }

    public JsonConverterException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
