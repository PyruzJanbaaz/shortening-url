package com.pyruz.shortening.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomServiceException extends RuntimeException {
    final transient Object[] objects;
    final HttpStatus status;

    public CustomServiceException(String message, Object[] objects, HttpStatus status) {
        super(message);
        this.objects = objects;
        this.status = status;
    }
}
