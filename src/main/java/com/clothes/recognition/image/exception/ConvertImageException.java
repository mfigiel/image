package com.clothes.recognition.image.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConvertImageException extends RuntimeException {
    public ConvertImageException(String message) {
        super(message);
    }
}
